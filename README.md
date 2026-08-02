# Robot Management Service

API para gerenciamento de robos com persistencia em MongoDB, publicacao de eventos no Kafka e metricas com Actuator/Prometheus.

## Stack

- Java 21
- Spring Boot 4.1
- Spring Web MVC
- Spring Data MongoDB
- Spring Kafka
- Spring Actuator + Micrometer (Prometheus)
- Gradle

## Funcionalidades

- CRUD de robos
- Alteracao de status (`ACTIVE` / `INACTIVE`)
- Validacao de dados de entrada
- Tratamento global de erros
- Publicacao de eventos Kafka:
  - `robot.created`
  - `robot.updated`
  - `robot.change-status`
  - `robot.deleted`
- Metricas de negocio:
  - `robot_created_total`
  - `robot_updated_total`
  - `robot_deleted_total`
  - `robot_active_total`
  - `robot_inactive_total`

## Requisitos

- JDK 21
- Docker e Docker Compose (recomendado para infraestrutura local)

## Configuracao

### 1. Infra local (MongoDB + Kafka + Kafka UI + Prometheus)

```bash
docker compose up -d
```

Servicos:

- MongoDB: `localhost:27017`
- Kafka: `localhost:9092`
- Kafka UI: `http://localhost:8080`
- Prometheus: `http://localhost:9090`

### 2. Variaveis de ambiente (perfil padrao)

Se executar sem perfil `local`, configure:

```bash
MONGODB_URI=mongodb://localhost:27017/robots
KAFKA_BOOTSTRAP_SERVERS=localhost:9092
ROBOT_CREATED_TOPIC=robot.created
ROBOT_UPDATED_TOPIC=robot.updated
ROBOT_CHANGE_STATUS_TOPIC=robot.change-status
ROBOT_DELETED_TOPIC=robot.deleted
```

### 3. Perfil local

O projeto possui `application-local.yaml` com valores locais prontos.

## Executando a aplicacao

```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

Aplicacao disponivel em: `http://localhost:8081`

## Endpoints

Base path: `/robots`

### Criar robo

`POST /robots`

```json
{
  "name": "Atlas",
  "model": "X100",
  "serialNumber": "SN-001"
}
```

### Listar robos (paginado)

`GET /robots?page=0&size=10`

### Buscar por robotId

`GET /robots/{id}`

### Atualizar robo

`PUT /robots/{id}`

```json
{
  "name": "Atlas v2",
  "model": "X200",
  "status": "ACTIVE",
  "serialNumber": "SN-001-NEW"
}
```

### Alterar somente status

`PATCH /robots/{id}/status`

```json
{
  "status": "INACTIVE"
}
```

### Remover robo

`DELETE /robots/{id}`

## Observabilidade

- Health check: `GET /actuator/health`
- Metricas Prometheus: `GET /actuator/prometheus`
- Endpoints expostos: `health`, `info`, `metrics`, `prometheus`

## Testes

```bash
./gradlew test
```
