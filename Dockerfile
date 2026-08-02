FROM amazoncorretto:21-alpine AS builder

WORKDIR /app

# Copia apenas os arquivos necessários para aproveitar o cache
COPY gradlew .
COPY gradle gradle
COPY build.gradle* settings.gradle* ./

RUN chmod +x gradlew

# Baixa as dependências
RUN ./gradlew dependencies --no-daemon

# Copia o restante do projeto
COPY src src

# Gera o JAR
RUN ./gradlew clean bootJar -x test --no-daemon

# ==========================
# Runtime
# ==========================

FROM amazoncorretto:21-alpine

WORKDIR /app

# Cria um usuário sem privilégios
RUN addgroup -S spring && adduser -S spring -G spring

COPY --from=builder /app/build/libs/*.jar app.jar

RUN chown spring:spring app.jar

USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "/app/app.jar"]