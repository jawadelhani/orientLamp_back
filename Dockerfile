FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /build

# copy Maven metadata first for dependency caching
COPY pom.xml ./
COPY .mvn .mvn
COPY mvnw ./mvnw

# copy source
COPY src ./src

# build jar (skip tests for faster builds during development)
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# copy built jar from builder stage
COPY --from=build /build/target/*.jar ./app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
