FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY target/*.jar showtime-service.jar

COPY elastic-apm-agent-1.54.0.jar /elastic-apm-agent.jar

EXPOSE 8084

CMD ["java", \
     "-Duser.timezone=Asia/Ho_Chi_Minh", \
     "-Dspring.profiles.active=prod", \
     "-javaagent:/elastic-apm-agent.jar", \
     "-Delastic.apm.server_urls=http://host.docker.internal:8200", \
     "-Delastic.apm.secret_token=supersecret", \
     "-Delastic.apm.service_name=showtime-service", \
     "-Delastic.apm.application_packages=com.cinema.showtimeservice", \
     "-jar", \
     "showtime-service.jar"]
