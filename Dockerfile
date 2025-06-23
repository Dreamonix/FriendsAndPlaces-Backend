FROM maven:3-openjdk-18 AS build
RUN mkdir ~/.m2
ADD pom.xml /build/
ADD src build/src/
WORKDIR /build/
RUN mvn package -DskipTests

FROM openjdk:19-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=build /build/target/friends_and_places*.jar /app/fap-backend.jar
ENTRYPOINT ["java", "-jar", "fap-backend.jar"]