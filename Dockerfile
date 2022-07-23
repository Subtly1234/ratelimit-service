# TODO
FROM openjdk:8-jdk-alpine

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone

#ENV JAVA_OPTS ''

WORKDIR /app

ADD /target/ratelimit-service-0.0.1-SNAPSHOT.jar /ratelimit-service-0.0.1-SNAPSHOT.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","/ratelimit-service-0.0.1-SNAPSHOT.jar"]

#ENTRYPOINT ["sh", "-c", "set -e && java -XX:+PrintFlagsFinal \
#                                           -XX:+HeapDumpOnOutOfMemoryError \
#                                           -XX:HeapDumpPath=/heapdump/heapdump.hprof \
#                                           -XX:+UnlockExperimentalVMOptions \
#                                           -XX:+UseCGroupMemoryLimitForHeap \
#                                           $JAVA_OPTS -jar prometheus-test-demo-0.0.1-SNAPSHOT.jar"]