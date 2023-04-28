

FROM openjdk:17-jdk-slim as builder

ENV HOME=/usr/app
ARG SPRING_ACTIVE_PROFILE
ARG VERSION
RUN mkdir -p $HOME
WORKDIR $HOME
COPY . .
RUN --mount=type=cache,target=/root/.m2 ./mvnw clean package -DskipTests -Dspring.profiles.active=$SPRING_ACTIVE_PROFILE -Dversion=$VERSION

FROM openjdk:17-jdk-slim
ARG SPRING_ACTIVE_PROFILE
ARG VERSION
ENV HOME=/usr/app
COPY --from=builder $HOME/target/hrm-tool-$SPRING_ACTIVE_PROFILE-$VERSION.war app.jar
ENTRYPOINT ["java","-jar","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005","app.jar"]
EXPOSE 9800