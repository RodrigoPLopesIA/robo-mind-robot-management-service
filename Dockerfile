# Etapa de build
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Copia os arquivos do Gradle
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Baixa as dependências (melhora o cache)
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon

# Copia o código-fonte
COPY src src

# Gera o JAR
RUN ./gradlew bootJar --no-daemon

# Etapa de execução
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]