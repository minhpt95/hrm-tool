FROM openjdk:17-jdk-slim as builder

ENV HOME=/usr/app
RUN mkdir -p $HOME

WORKDIR $HOME
COPY . .

RUN --mount=type=cache,target=/root/.m2 ./mvnw clean package -DskipTests

FROM openjdk:17-jdk-slim
COPY --from=builder /usr/app/target/*.war app.jar

ENTRYPOINT ["java","-jar","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005","app.jar"]