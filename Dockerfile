FROM eclipse-temurin:11-jre-jammy

WORKDIR /app

COPY target/app.jar /app/app.jar
COPY target/dependency/ /app/dependency/

ENV CLASSPATH=/app/app.jar:/app/dependency/*

CMD ["java", "-cp", "/app/app.jar:/app/dependency/*", "com.sysdig.demo.App"]
