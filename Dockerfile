# ==========================================
# Etapa 1: Construcción (Build) con Alpine
# ==========================================
FROM maven:3.9.9-eclipse-temurin-17-alpine AS builder

WORKDIR /app

# Instalar git para descargar el código sin necesidad de vincular el repo a Coolify
RUN apk add --no-cache git

# Argumentos del repositorio (puedes cambiarlos según tu rama o si usas token)
ARG REPO_URL="https://github.com/sofiabareno179-cmyk/SalonGlitt_Spring.git"
ARG BRANCH="main"
ARG GITHUB_TOKEN=""

# Clonar el código del repositorio en /app
RUN if [ -n "$GITHUB_TOKEN" ]; then \
      git clone --depth 1 --branch ${BRANCH} https://${GITHUB_TOKEN}@github.com/sofiabareno179-cmyk/SalonGlitt_Spring.git . ; \
    else \
      git clone --depth 1 --branch ${BRANCH} ${REPO_URL} . ; \
    fi

# Empaquetar la aplicación omitiendo tests
RUN mvn clean package -DskipTests

# ==========================================
# Etapa 2: Entorno de Ejecución (Runtime) con Alpine
# ==========================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Crear usuario y grupo sin privilegios root en Alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar el JAR compilado en la etapa anterior
COPY --from=builder /app/target/*.jar app.jar

# Puerto expuesto por defecto
EXPOSE 8080

# Optimización de memoria para JVM en contenedores
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
