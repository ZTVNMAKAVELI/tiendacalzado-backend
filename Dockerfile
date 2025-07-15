# Usa una imagen base con Java 21
FROM eclipse-temurin:21-jdk-alpine

# Crea un directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia tu JAR al contenedor
COPY target/opensource-0.0.1-SNAPSHOT.jar app.jar

# Expón el puerto (Render detecta el puerto automáticamente por la variable PORT)
ENV PORT=8080
EXPOSE 8080

# Inicia el servicio Spring Boot
ENTRYPOINT ["java", "-jar", "app.jar"]
