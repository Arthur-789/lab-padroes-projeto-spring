API REST que cadastra clientes e resolve automaticamente o endereço correspondente ao CEP informado, integrando-se com a API pública do [ViaCEP](https://viacep.com.br).

[![Java](https://img.shields.io/badge/Java-11-orange)](#)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen)](#)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED)](#)

---

## Sobre o projeto

Este projeto foi desenvolvido para o Lab "Explorando Padrões de Projetos na Prática com Java", da **[DIO (Digital Innovation One)](https://www.dio.me)**. A base original do laboratório demonstrava os padrões Singleton, Strategy e Facade aplicados a um cadastro simples de clientes com integração ao ViaCEP.
O projeto foi expandido, mantendo o objetivo original, mas acrescentando novos padrões de projeto, uma arquitetura em camadas mais rica, tratamento de erros consistente, testes automatizados e execução em conteiner.

---

## Padrões de Projeto utilizados

| Padrão | Onde | Papel no projeto |
|---|---|---|
| **Singleton** | `ClienteServiceImpl`, demais `@Service`/`@Component` | Gerenciados pelo container do Spring, garantindo uma única instância por contexto da aplicação. |
| **Strategy** | `ClienteService`, `CepResolver` e suas implementações | `ClienteService` define um contrato substituível para a regra de negócio. `CepResolver` permite múltiplas formas de resolver um CEP. |
| **Facade** | `ClienteRestController` | Expõe uma API REST simples. |
| **Chain of Responsibility** | `CepResolverChain` | Percorre as estratégias de resolução de CEP até uma delas conseguir resolver a requisição. |
| **Builder** | `Cliente`, `Endereco`, `ErroResponseDTO` (via Lombok `@Builder`) | Construção fluente e legível dos objetos de domínio. |
| **Factory Method** | `ClienteFactory` | Centraliza a criação de um `Cliente` a partir do DTO de entrada, incluindo a normalização do CEP. |
| **Observer** | `ClienteCadastradoEvent` + `LogNotificationObserver` + `EstatisticaCadastroObserver` | Ao cadastrar um cliente, um evento é publicado (`ApplicationEventPublisher`) e consumido por múltiplos observadores. |
| **Repository** | `ClienteRepository`, `EnderecoRepository` | Abstrai o acesso a dados via Spring Data JPA. |

---

## Tecnologias

- Java 11
- Spring Boot 2.7.18 (Web, Data JPA, Validation, Actuator)
- Spring Cloud OpenFeign
- springdoc-openapi (Swagger UI)
- H2 Database
- Lombok
- JUnit 5 + Mockito + AssertJ
- Docker & Docker Compose

---

## Pré-requisitos

- [Docker](https://docs.docker.com/get-docker/) (versão 20.10+)
- [Docker Compose](https://docs.docker.com/compose/install/) (já incluso no Docker Desktop)

Não é necessário instalar Java, Maven ou qualquer outra dependência.

---

## Como executar

Clone o repositório e, na raiz do projeto, execute:

```bash
docker compose up --build
```

Quando o log exibir `Started Application in ... seconds`, a API estará pronta em:

| Recurso | URL |
|---|---|
| API REST | http://localhost:8080/api/clientes |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Estatísticas por UF | http://localhost:8080/api/estatisticas/clientes-por-uf |
| Console do H2 | http://localhost:8080/h2-console |
| Health check | http://localhost:8080/actuator/health |

> **Dados de acesso do H2:** JDBC URL `jdbc:h2:mem:padroesdeprojeto`, usuário `user`, senha em branco.

Para parar a aplicação:

```bash
docker compose down
```

---

## Exemplos de uso da API

**Cadastrar um cliente:**

```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{"nome": "Grace Hopper", "cep": "01310-930"}'
```

**Listar todos os clientes:**

```bash
curl http://localhost:8080/api/clientes
```

**Buscar cliente por id:**

```bash
curl http://localhost:8080/api/clientes/1
```

**Atualizar um cliente:**

```bash
curl -X PUT http://localhost:8080/api/clientes/1 \
  -H "Content-Type: application/json" \
  -d '{"nome": "Ada Lovelace", "cep": "20040-020"}'
```

**Remover um cliente:**

```bash
curl -X DELETE http://localhost:8080/api/clientes/1
```