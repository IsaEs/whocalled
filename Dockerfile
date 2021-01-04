FROM openjdk:8
MAINTAINER Isa Es <merhaba@isaes.com.tr>
COPY /target/WhoCalled-0.0.1-SNAPSHOT.war whocalled-fat.war
EXPOSE 8080
ENTRYPOINT ["java","-jar","/whocalled-fat.war"]
