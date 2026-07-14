FROM maven:3.9-eclipse-temurin-11 AS build

WORKDIR /workspace

COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q clean package -DskipTests

FROM eclipse-temurin:11-jre-jammy AS runtime

RUN apt-get update \
	&& apt-get install -y --no-install-recommends wget \
	&& rm -rf /var/lib/apt/lists/*

RUN groupadd -r padroesdeprojeto && useradd -r -g padroesdeprojeto padroesdeprojeto

WORKDIR /app
COPY --from=build /workspace/target/padroesdeprojeto.jar ./padroesdeprojeto.jar

RUN chown -R padroesdeprojeto:padroesdeprojeto /app
USER padroesdeprojeto

ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8080

HEALTHCHECK --interval=15s --timeout=5s --start-period=40s --retries=5 \
	CMD wget -qO- http://localhost:8080/actuator/health | grep -q '"status":"UP"' || exit 1

ENTRYPOINT ["java", "-jar", "padroesdeprojeto.jar"]
