# SalonGlitt_Spring

Backend API de Salon Glitt construido con Spring Boot.

## Requisitos

- JDK 17 o superior (probado con JDK 24)
- No es necesario instalar Maven: el proyecto incluye el Maven Wrapper (`mvnw` / `mvnw.cmd`).

## Puesta en marcha

1. Crea el archivo de entorno:

   ```powershell
   Copy-Item .env.example .env
   ```

   (Opcional) ajusta los valores de `.env`.

2. Compila el proyecto:

   ```powershell
   .\mvnw.cmd clean compile
   ```

3. Ejecuta la aplicación en desarrollo:

   ```powershell
   .\run-dev.ps1
   ```

   La aplicación quedará disponible en `http://localhost:8080`.

## Estructura del proyecto

```
├── .mvn/wrapper/          # Configuración del Maven Wrapper
├── .vscode/               # Configuración del editor
├── src/
│   ├── main/
│   │   ├── java/co/salonglitt/
│   │   │   ├── controller/    # Controladores REST
│   │   │   └── service/       # Lógica de negocio
│   │   └── resources/         # application.properties, etc.
│   └── test/java/co/salonglitt/
│       ├── controller/        # Tests de controladores
│       └── service/           # Tests de servicios
├── target/                # Artefactos compilados (generado)
├── .env.example           # Plantilla de variables de entorno
├── .gitignore
├── mvnw / mvnw.cmd        # Maven Wrapper
├── pom.xml
└── run-dev.ps1            # Script de desarrollo
```

## Comandos útiles

| Comando                  | Descripción                          |
| ------------------------ | ------------------------------------ |
| `.\mvnw.cmd clean compile` | Compila el proyecto                |
| `.\mvnw.cmd test`          | Ejecuta los tests                  |
| `.\mvnw.cmd spring-boot:run` | Ejecuta la aplicación            |
| `.\mvnw.cmd package`       | Genera el JAR en `target/`         |
| `.\run-dev.ps1`            | Ejecuta en modo desarrollo         |