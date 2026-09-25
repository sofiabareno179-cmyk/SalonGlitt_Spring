#run-dev.ps1
param(
    [string]$Port = ""
)

$ErrorActionPreference = "Stop"

# Cargar variables desde .env (si existe) al entorno del proceso
if (Test-Path -LiteralPath ".env") {
    Get-Content -LiteralPath ".env" | ForEach-Object {
        $line = $_.Trim()
        if ($line -and -not $line.StartsWith("#") -and $line.Contains("=")) {
            $kv = $line -split "=", 2
            [System.Environment]::SetEnvironmentVariable($kv[0].Trim(), $kv[1].Trim(), "Process")
        }
    }
} elseif (Test-Path -LiteralPath ".env.example") {
    Write-Host "AVISO: No existe .env. Copiando desde .env.example..." -ForegroundColor Yellow
    Copy-Item ".env.example" ".env"
}

if (-not $env:JAVA_HOME) {
    $jdk = Get-ChildItem 'C:\Program Files\Java' -Directory -Filter 'jdk-*' -ErrorAction SilentlyContinue |
        Sort-Object Name -Descending | Select-Object -First 1
    if ($jdk) {
        $env:JAVA_HOME = $jdk.FullName
        [System.Environment]::SetEnvironmentVariable('JAVA_HOME', $jdk.FullName, 'User')
        Write-Host "JAVA_HOME establecido en: $($jdk.FullName)" -ForegroundColor Yellow
    } else {
        Write-Host "ERROR: No se encontró JDK en C:\Program Files\Java" -ForegroundColor Red
        exit 1
    }
}

if (-not (($env:Path -split ';') -contains "$($env:JAVA_HOME)\bin")) {
    $env:Path = "$($env:JAVA_HOME)\bin;$($env:Path)"
}

if (-not $Port) {
    $Port = if ($env:SERVER_PORT) { $env:SERVER_PORT } else { "8080" }
}

Write-Host "Compilando el proyecto..." -ForegroundColor Cyan
& ".\mvnw.cmd" clean compile
if ($LASTEXITCODE -ne 0) {
    Write-Host "Error al compilar. Abortando." -ForegroundColor Red
    exit $LASTEXITCODE
}

Write-Host "Ejecutando la aplicacion en el puerto $Port..." -ForegroundColor Cyan
$env:SERVER_PORT = $Port
& ".\mvnw.cmd" spring-boot:run