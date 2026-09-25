# STAGE 1: Compilation (Build)
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# First copy the pom.xml
COPY pom.xml .

# Second copy the source
COPY src ./src

# We tell Maven to compile the project and package the .jar.
RUN mvn clean package -DskipTests

# ---------------------------------------------------

# STAGE 2: Run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the file .jar
COPY --from=builder /app/target/*.jar app.jar

# App Port
EXPOSE 8080

# Init Aplication
ENTRYPOINT ["java", "-jar", "app.jar"]