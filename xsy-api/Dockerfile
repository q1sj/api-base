FROM openjdk:8u332-jdk
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
ADD ./target/*.jar  /app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
