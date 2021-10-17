FROM openjdk:8
VOLUME /data
EXPOSE 8080
ENV ACTIVE_PROFILE=mysql

WORKDIR /app
COPY wiflix-1.9.1.jar streama.jar
COPY entrypoint.sh entrypoint.sh

ENTRYPOINT ["sh", "entrypoint.sh"]
