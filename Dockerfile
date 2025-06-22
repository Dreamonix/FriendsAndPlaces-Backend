FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make the Maven wrapper executable
RUN chmod +x ./mvnw

# Download dependencies (will be cached unless pom.xml changes)
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build the application
RUN ./mvnw package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Environment variables for database connection
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/friends_and_places_dev
ENV SPRING_DATASOURCE_USERNAME=dev_user
ENV SPRING_DATASOURCE_PASSWORD=dev_password
ENV SPRING_PROFILES_ACTIVE=dev

# Environment variable for JWT configuration
ENV JWT_SECRET=FiJyPNdycju8rMCzfVtH69mS5LCpAQZ4SmBshSt2jLA=
ENV JWT_EXPIRATION=86400000

# Environment variable for external API configuration
ENV EXTERNAL_API_API_KEY=6374d7074ccd4c618b523d292656f5a2

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
