# 🍽️ TechRest - Sistema de Pedidos em Microsserviços

> Plataforma de pedidos para restaurantes, desenvolvida com microsserviços e comunicação assíncrona via RabbitMQ.

[![Java](https://img.shields.io/badge/Java-21-brightgreen)](https://www.java.com/) [![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.x-green)](https://spring.io/projects/spring-boot) [![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)](https://www.postgresql.org/) [![Docker](https://img.shields.io/badge/Docker-Compose-blue)](https://docs.docker.com/compose/)

---

## 📋 Sumário

- [🌟 Sobre o Projeto](#-sobre-o-projeto)
- [🛠️ Tecnologias](#️-tecnologias)
- [📦 Docker Compose](#-docker-compose)

---

## 🌟 Sobre o Projeto

O projeto **TechRest** simula uma plataforma de pedidos de restaurante dividida em **microsserviços independentes**, comunicando-se de forma **assíncrona** com **RabbitMQ** e armazenando dados com **PostgreSQL**.

### Microsserviços:

1. **cliente-service** 👤 – Cadastro e gerenciamento de clientes.  
2. **produto-service** 🍔 – Gerenciamento de produtos disponíveis no cardápio.  
3. **estoque-service** 📦 – Controle de estoque dos produtos.  
4. **pedido-service** 🧾 – Orquestra pedidos, comunica-se com os demais serviços e envia eventos.  
5. **pagamento-service** 💳 – Processa pagamentos com base nos eventos recebidos.

---

## 🛠️ Tecnologias

- **Java 21**  
- **Spring Boot 3.3.x**
  - Spring Web
  - Spring Data JPA
  - Spring Validation
  - Spring AMQP  
- **RabbitMQ** 🐰  
- **PostgreSQL 16**  
- **Docker & Docker Compose** 🐳  
- **Testes:** JUnit 5, Mockito, Testcontainers  
- **Jacoco** para cobertura de testes

---

## 🚀 Começando

1. Clone o repositório:
   ```bash
   git clone https://github.com/Gbalestrieri9/tech_rest.git

2. Configuração do Docker:
    ```bash
    # Parar containers em execução (se necessário)
    docker-compose down
    
    # Remover volumes antigos (se necessário)
    docker-compose down -v
    
    # Construir e iniciar os containers
    docker-compose up --build -d
    
    # Verificar logs
    docker-compose logs appointment-service
    
    # Verificar status dos containers
    docker ps
---

## 📦 Docker Compose

Levanta os seguintes containers para o ecossistema da aplicação:

- **cliente-service** (8081) – Microsserviço responsável pelo cadastro e consulta de clientes  
- **produto-service** (8082) – Microsserviço de gerenciamento dos produtos do restaurante  
- **estoque-service** (8083) – Microsserviço de controle de estoque dos produtos  
- **pedido-service** (8084) – Microsserviço de orquestração de pedidos e integração entre os serviços  
- **pagamento-service** (8085) – Microsserviço de processamento de pagamentos  
- **rabbitmq** (5672, 15672) – Broker de mensagens utilizado para comunicação assíncrona entre os microsserviços  
- **PostgreSQL** (5442–5446) – Um banco de dados dedicado para cada microsserviço:

  | Microsserviço       | Banco         | Porta Local |
  |---------------------|---------------|-------------|
  | cliente-service     | cliente_db    | 5442        |
  | produto-service     | produto_db    | 5443        |
  | estoque-service     | estoque_db    | 5444        |
  | pedido-service      | pedido_db     | 5445        |
  | pagamento-service   | pagamento_db  | 5446        |
