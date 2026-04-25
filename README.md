# OGOCX Microservices

Arquitetura de microsserviços construída com Java e Spring Boot para aprendizado prático. O projeto cobre autenticação JWT com refresh token rotacionado, comunicação assíncrona via RabbitMQ, auditoria com Elasticsearch e roteamento centralizado por um gateway.

## Serviços

| Serviço | Porta | Descrição |
|---|---|---|
| `gateway` | 8082 | Ponto de entrada. Valida JWT e roteia requisições |
| `user-service` | 8080 | Gerenciamento de usuários |
| `auth-service` | 8081 | Autenticação e emissão de tokens JWT |
| `ogocx-bus` | — | Biblioteca compartilhada de eventos do RabbitMQ |
| `ogocx-service-lib` | — | Biblioteca compartilhada de erros, correlationId e paginação |

## Tecnologias

- Java 21
- Spring Boot 4.0.5
- Spring Cloud Gateway (WebFlux)
- RabbitMQ
- PostgreSQL
- Elasticsearch
- JWT (access token + refresh token rotacionado)
- Docker Compose

## Arquitetura

```
Client
  │
  ▼
Gateway (8082)
  │  └── Valida JWT
  │  └── Injeta X-User-Id e X-User-Email nos headers
  │  └── Propaga X-Correlation-Id end-to-end
  │  └── Rate limiting em /auth
  │
  ├──▶ user-service (8080)
  │         └── Gerenciamento de usuários
  │         └── Publica eventos via RabbitMQ
  │         └── Auditoria no Elasticsearch
  │
  └──▶ auth-service (8081)
            └── Consome eventos do user-service via RabbitMQ
            └── Gerencia credenciais e refresh tokens
            └── Auditoria no Elasticsearch
```

## Como rodar

### Pré-requisitos

- Java 21+
- Maven
- Docker

### 1. Infraestrutura

```bash
cd infra
cp .env.example .env
# preencha as variáveis no .env
docker compose up -d
```

### 2. Bibliotecas internas

```bash
cd ogocx-bus && mvn clean install -q
cd ogocx-service-lib && mvn clean install -q
```

### 3. Cada serviço

```bash
cd user-service   # ou auth-service / gateway
cp .env.example .env
# preencha as variáveis no .env
./mvnw spring-boot:run
```

## Variáveis de ambiente

Cada serviço tem seu próprio `.env.example` com as variáveis necessárias. O `JWT_SECRET` deve ser o mesmo no `gateway` e no `auth-service`.

## Endpoints

Importe o arquivo `OGOCX_SOA.apidog.json` no ApiDog para ver todos os endpoints documentados.

Todas as requisições passam pelo gateway na porta `8082`.

### Autenticação

| Método | Endpoint | Descrição | Auth |
|---|---|---|---|
| POST | `/auth/sign-in` | Login | Não |
| POST | `/auth/refresh` | Renovar token | Não |
| POST | `/auth/logout` | Logout e revogação do refresh token | Sim |

### Usuários

| Método | Endpoint | Descrição | Auth |
|---|---|---|---|
| POST | `/users` | Criar usuário | Sim |
| GET | `/users` | Listar usuários | Sim |
| GET | `/users/{id}` | Buscar usuário por ID | Sim |
| PUT | `/users/{id}` | Atualizar usuário | Sim |

### Auditoria

| Método | Endpoint | Descrição | Auth |
|---|---|---|---|
| GET | `/users/audit` | Logs de auditoria do user-service | Sim |
| GET | `/auth/audit` | Logs de auditoria do auth-service | Sim |

> Rotas autenticadas exigem o header `Authorization: Bearer <access_token>`

## Segurança

- Refresh tokens são persistidos no banco e rotacionados a cada uso
- Access tokens têm vida curta e expiram naturalmente
- No logout o refresh token é revogado imediatamente
- Todos os erros seguem o formato RFC 7807 (Problem Details)
- Requisições rastreadas via `X-Correlation-Id` end-to-end

## Observações

Este projeto está em desenvolvimento ativo e pode conter erros. Sugestões e feedbacks são bem-vindos.