# SalonGlitt_Spring

Backend API de Salon Glitt construido con Spring Boot. Usa Spring Data JPA sobre PostgreSQL; las tablas se crean automáticamente (`ddl-auto=update`).

## Requisitos

- JDK 17 o superior (probado con JDK 24)
- PostgreSQL en ejecución
- No es necesario instalar Maven: el proyecto incluye el Maven Wrapper (`mvnw` / `mvnw.cmd`).

## Puesta en marcha

1. Crea el archivo de entorno:

   ```powershell
   Copy-Item .env.example .env
   ```

   (Opcional) ajusta los valores de `.env` (credenciales de PostgreSQL, puerto, etc.).

2. Compila el proyecto:

   ```powershell
   .\mvnw.cmd clean compile
   ```

3. Ejecuta la aplicación en desarrollo:

   ```powershell
   .\run-dev.ps1
   ```

   La aplicación quedará disponible en `http://localhost:8080` y Swagger UI en `http://localhost:8080/swagger-ui.html`.

   > Nota: el esquema de la base de datos se genera automáticamente al arrancar (datasource apuntando a `DB_HOST`/`DB_PORT`/`DB_NAME`/`DB_USER`/`DB_PASSWORD` desde `.env`).

## Esquema de datos (tablas)

| Tabla | Descripción |
| ----- | ----------- |
| `perfiles` | Roles (ADMIN, CLIENTE, ESTILISTA) |
| `usuarios` | Clientes y estilistas (N:1 perfiles) |
| `servicios` | Servicios del salón (corte, tinte, uñas...) |
| `citas` | Citas (N:1 usuario cliente, N:1 servicio) |
| `agendas` | Bloques de disponibilidad (N:1 usuario estilista) |
| `bloqueos` | Bloqueos de disponibilidad (N:1 usuario estilista) |
| `catalogo_precios` | Precios de servicios por período (N:1 servicio) |
| `inventario` | Stock de productos (1:1 producto) |
| `notificaciones` | Notificaciones a usuarios (N:1 usuario) |
| `productos` | Productos del salón (N:1 proveedor) |
| `proveedores` | Proveedores |
| `recordatorios` | Recordatorios de citas (N:1 cita) |
| `promociones` | Descuentos sobre servicio o producto |

## Estructura del proyecto

```
├── .mvn/wrapper/          # Configuración del Maven Wrapper
├── .vscode/               # Configuración del editor
├── src/
│   ├── main/
│   │   ├── java/co/salonglitt/
│   │   │   ├── controller/    # Controladores REST
│   │   │   ├── dto/           # Objetos de transferencia (request/response)
│   │   │   ├── entity/        # Entidades JPA (tablas)
│   │   │   ├── exception/     # Manejo global de errores
│   │   │   ├── repository/    # Repositorios Spring Data JPA
│   │   │   └── service/       # Lógica de negocio
│   │   └── resources/         # application.properties, etc.
│   └── test/java/co/salonglitt/
│       ├── controller/        # Tests de controladores
│       └── repository/        # Tests de persistencia (H2)
├── target/                # Artefactos compilados (generado)
├── .env.example           # Plantilla de variables de entorno
├── .env                   # Variables de entorno (local, ignorado por git)
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