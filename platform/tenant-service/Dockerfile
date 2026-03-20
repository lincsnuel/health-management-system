# Run from the project ROOT: docker build -t tenant-service -f tenant-service/Dockerfile .
FROM maven:3.9.9-eclipse-temurin-25-alpine AS build
WORKDIR /app

# 1. Copy the Root Parent POM
COPY pom.xml .

# 2. Copy Shared Modules (Essential for your app to compile!)
COPY healthcore-common/ healthcore-common/
COPY healthcore-contracts/ healthcore-contracts/

# 3. Copy the specific service code
COPY tenant-service/ tenant-service/

# 4. Build the specific module and its dependencies
RUN mvn clean package -pl tenant-service -am -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/tenant-service/target/*.jar app.jar
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "app.jar"]