#run-dev.ps1
param(
    [string]$Port = "8080"
)

$ErrorActionPreference = "Stop"

if (-not $env:JAVA_HOME) {
    $jdk = Get-ChildItem 'C:\Program Files\Java' -Directory -Filter 'jdk-*' -ErrorAction SilentlyContinue |
        Sort-Object Name -Descending | Select-Object -First 1
    if ($jdk) {
        $env:JAVA_HOME = $jdk.FullName
        Write-Host "JAVA_HOME establecido en: $($jdk.FullName)" -ForegroundColor Yellow
    } else {
        Write-Host "ERROR: No se encontró JDK en C:\Program Files\Java" -ForegroundColor Red
        exit 1
    }
}

if (-not (Test-Path -LiteralPath ".env")) {
    Write-Host "AVISO: No existe .env. Copiando desde .env.example..." -ForegroundColor Yellow
    Copy-Item ".env.example" ".env"
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