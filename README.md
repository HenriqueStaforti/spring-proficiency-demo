# Spring Proficiency Demo

Este projeto é uma demonstração prática de proficiência no ecossistema **Spring Boot**, reunindo diversos recursos amplamente utilizados em aplicações corporativas. Ele implementa um **CRUD de produtos**, utiliza **PostgreSQL** como banco de dados, **cache com Redis**, **mensageria com RabbitMQ**, e integração externa via **OpenFeign** para fins de auditoria.

O objetivo é apresentar práticas recomendadas de arquitetura, integração entre serviços, mecanismos de autenticação/autorização, mensageria, cache e comunicação externa.

---

## 🧾 **Tecnologias Utilizadas**

- **Spring Boot**
- **Spring Security**
- **Spring Data JPA**
- **Spring Data Redis**
- **OpenFeign**
- **RabbitMQ**
- **Flyway**
- **PostgreSQL**
- **Lombok**
- **MapStruct**
- **Docker + Docker Compose**
- **Maven**

---

## 📌 **Principais Funcionalidades**

### 🔐 Autenticação & Autorização (Spring Security)
- Criação e login de usuários.
- Geração de tokens JWT.
- A cada operação de login ou criação de usuário, é feita uma chamada externa via **OpenFeign** para um serviço de auditoria.

### 📦 CRUD de Produtos
- Endpoints para criação, atualização, listagem e remoção.
- Persistência utilizando **Spring Data JPA** com **PostgreSQL**.
- Migrações controladas via **Flyway**.

### ⚡ Cache com Redis
- As operações **GET** de produtos utilizam cache Redis para melhorar desempenho.
- Atualizações invalidam o cache automaticamente.

### 📨 Mensageria com RabbitMQ
- Ao criar um produto, o sistema publica uma mensagem no RabbitMQ.
- O próprio serviço consome essa mensagem para fins de demonstração.

---

## 🚀 **Como Rodar o Projeto**

### ⚙️ **Pré‑requisitos**
- Docker
- Java 17+
- Maven 3.8+
- Arquivo `.env` com variáveis obrigatórias (para o docker-compose)

---

### ▶️ **1. Subir os serviços necessários**

Na raiz do projeto execute:

```bash
docker compose up -d
```

Isso irá subir:
- PostgreSQL
- Redis
- RabbitMQ

---

### ▶️ **2. Executar a aplicação**

```bash
mvn spring-boot:run
```

A API estará disponível em:

```
http://localhost:8080
```

---

## 🧪 **Endpoints Principais**

### 🔐 **Autenticação**
- `POST /auth/register` — Cria novo usuário (dispara request externo via Feign)
- `POST /auth/login` — Gera token JWT (também audita via Feign)

### 📦 **Produtos**
- `GET /products` — Lista produtos (com cache Redis)
- `POST /products` — Cria produto (publica mensagem RabbitMQ)
- `GET /products/{id}` — Lista produto específico (com cache Redis)
- `PATCH /products/{id}` — Atualiza produto
- `DELETE /products/{id}` — Remove produto

---

## 📄 **Licença**

Este projeto está sob licença MIT. Sinta‑se à vontade para utilizar como referência.

---

## 👤 **Autor**
**Henrique Staforti**  
GitHub: https://github.com/HenriqueStaforti
