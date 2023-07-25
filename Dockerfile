# FROM eclipse-temurin:17-jdk-alpine
# VOLUME /tmp
# COPY target/*.jar app.jar
# ENTRYPOINT ["java","-jar","/app.jar"]

# FROM node:20-alpine3.17 as frontend
# WORKDIR /frontend
# COPY frontend/nutrimate .
# # ENV PORT=3000
# # COPY ["./frontend/nutrimate/package.json", "./frontend/nutrimate/package-lock.json*", "./frontend/"]
# RUN npm install
# RUN npm run-script build

FROM maven:3.9.3-eclipse-temurin-17 as backend
WORKDIR /backend
COPY backend .
# RUN mkdir -p src/main/resources/static
# COPY --from=frontend /frontend/build src/main/resources/static
RUN mvn clean package

FROM eclipse-temurin:17-alpine
COPY --from=backend /backend/target/backend-0.0.1-SNAPSHOT.jar ./app.jar
RUN mkdir logs/
COPY --from=backend /backend/logs/application.log ./logs/application.log
EXPOSE 8080
# RUN adduser -D user
# USER user
# CMD [ "sh", "-c", "java -Dserver.port=$PORT -Djava.security.egd=file:/dev/./urandom -jar app.jar" ]
ENTRYPOINT ["java","-jar","/app.jar"]
