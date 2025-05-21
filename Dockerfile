FROM maven:3.9.0-eclipse-temurin-17-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Variables de entorno básicas para la conexión a la DB y puerto
ENV DATABASE_URL="r2dbc:postgresql://ep-wild-union-a4ause6j-pooler.us-east-1.aws.neon.tech/neondb?sslmode=require"
ENV DATABASE_USERNAME="neondb_owner"
ENV DATABASE_PASSWORD="npg_cDxL3daGV8tW"
ENV PORT=8086

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "app.jar"]
