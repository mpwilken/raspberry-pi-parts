FROM adoptopenjdk/openjdk15
VOLUME /tmp
RUN mkdir /opt/app
COPY backend/target/parts-backend-*-SNAPSHOT.jar /opt/app/parts.jar
RUN mkdir /opt/app/images
COPY images /opt/app/images

EXPOSE 8084

ENTRYPOINT ["java", "-Dspring.profiles.active=h2,credentials", "-Dspring.flyway.placeholders.imgdir=/opt/app/images", "-jar", "/opt/app/parts.jar"]
