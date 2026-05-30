# SkyBook

---

## Parte 1 — Visão Geral

### Descrição

> O **SkyBook** é um sistema de reservas de poltronas de aeronave. Permite que passageiros visualizem a disponibilidade de assentos, realizem reservas e obtenham um resumo consolidado com o valor total a pagar.

### Funcionalidades

- Listagem de poltronas da aeronave com status atual (disponível / indisponível)
- Reserva de uma ou mais poltronas disponíveis, atualizando seu status automaticamente
- Resumo das reservas com valor individual de cada poltrona e valor total

> Escopo e domínio do produto detalhados em [`.kiro/steering/product.md`](.kiro/steering/product.md)

### Demonstração

🎥 [Assista ao vídeo de demonstração no YouTube](https://youtube.com/...)

### Quadro de Tarefas

📋 [Acompanhe o backlog no GitHub Projects](https://github.com/orgs/IA-para-DEVs-SCTEC-T2/projects/26/views/1)

### Melhorias Futuras

- [ ] Autenticação e cadastro de usuários
- [ ] Pagamento online
- [ ] Cancelamento de reservas
- [ ] Suporte a múltiplos voos ou aeronaves
- [ ] Histórico de reservas

---

## Parte 2 — Especificações e Execução

### Arquitetura

```mermaid
graph TD
    Client([Cliente / Requisição HTTP])
    Controller[Controller]
    Service[Service]
    Repository[Repository]
    DB[(H2 Database)]

    Client --> Controller
    Controller --> Service
    Service --> Repository
    Repository --> DB
```

#### Estrutura de Pacotes

```
com.ia.para.devs.skybook
├── controller      # Controllers REST
├── service         # Lógica de negócio
├── repository      # Interfaces Spring Data JPA
├── model           # Entidades JPA
└── dto             # DTOs de request e response
```

#### Decisões Técnicas

- Arquitetura MVC para separação clara de responsabilidades
- DTOs para desacoplar a camada de apresentação das entidades JPA — endpoints nunca expõem entidades diretamente
- H2 em memória para simplificar o ambiente de desenvolvimento, com schema gerenciado automaticamente pelo Hibernate

> Padrão arquitetural e estrutura de pacotes detalhados em [`.kiro/steering/structure.md`](.kiro/steering/structure.md)

### Descrição das Camadas

| Camada | Responsabilidade |
|---|---|
| Controller | Recebe requisições HTTP, delega para o Service, retorna respostas |
| Service | Contém a lógica de negócio |
| Repository | Acesso e persistência de dados via Spring Data JPA |
| Model/Entity | Entidades JPA mapeadas para o banco H2 |
| DTO | Objetos de transferência de dados (entrada e saída dos endpoints) |

### Modelagem do Banco de Dados

O banco de dados utilizado é o **H2 em memória**, gerenciado automaticamente pelo Hibernate via Spring Data JPA.

| Entidade | Tabela | Descrição |
|---|---|---|
| `UserEntity` | `app_user` | Dados do usuário que realiza reservas |
| `AirplaneSeatEntity` | `airplane_seat` | Dados de cada poltrona (código, preço, disponibilidade) |
| `BookingEntity` | `booking` | Reserva — vínculo entre um usuário e um assento |

> Diagrama ER completo e descrição das entidades em [`docs/data-model.md`](docs/data-model.md)

### Tecnologias

| Tecnologia | Versão | Finalidade |
|---|---|---|
| Java | 17 | Linguagem principal |
| Spring Boot | 4.0.6 | Framework web |
| Spring Data JPA | — | Persistência de dados |
| H2 Database | — | Banco em memória |
| Lombok | — | Redução de boilerplate |
| SpringDoc OpenAPI | 3.0.2 | Documentação Swagger |
| Maven | — | Build e dependências |

> Stack completa e configurações de build detalhadas em [`.kiro/steering/tech.md`](.kiro/steering/tech.md)

### Como Executar Localmente

#### Pré-requisitos

- Java 17+
- Maven 3.8+

#### Passos

```bash
# Clone o repositório
git clone https://github.com/IA-para-DEVs-SCTEC-T2/projeto-avaliativo-m12-SkyBook.git
cd projeto-avaliativo-m12-SkyBook

# Execute com Maven
./mvnw spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

### Endpoints

| Recurso | Método | URL |
|---|---|---|
| Listar poltronas | GET | `http://localhost:8080/skybook/seats/listSeats` |
| Reservar poltronas | POST | `http://localhost:8080/skybook/bookings/bookSeat` |
| Resumo das reservas | GET | `http://localhost:8080/skybook/bookings/summary?email={email}` |
| Swagger UI | — | `http://localhost:8080/skybook/swagger-ui.html` |
| H2 Console | — | `http://localhost:8080/skybook/h2-console` |

### Cenários de Uso

#### Cenário 1 — Listagem de poltronas disponíveis e indisponíveis

**Entrada:** `GET /skybook/seats/listSeats`

**Saída esperada:**
```json
[
  { "id": 1,  "code": "1A",  "price": 198.89, "available": true  },
  { "id": 2,  "code": "1B",  "price": 198.89, "available": false },
  ...
  { "id": 59, "code": "10E", "price": 110.00, "available": true  },
  { "id": 60, "code": "10F", "price": 110.00, "available": false }
]
```

#### Cenário 2 — Reserva de poltronas

**Entrada:** `POST /skybook/bookings/bookSeat`

```json
{
  "passengerName": "João Silva",
  "passengerEmail": "joao@email.com",
  "seatCodes": ["1A", "3C"]
}
```

**Saída esperada (201 Created):**
```json
[
  {
    "bookingId": 1,
    "seatCode": "1A",
    "seatPrice": 198.89,
    "passengerName": "João Silva",
    "bookedAt": "2026-05-29T20:00:00"
  },
  {
    "bookingId": 2,
    "seatCode": "3C",
    "seatPrice": 149.90,
    "passengerName": "João Silva",
    "bookedAt": "2026-05-29T20:00:00"
  }
]
```

**Erros possíveis:**
- `404 Not Found` — código de poltrona não existe
- `409 Conflict` — poltrona já está reservada

#### Cenário 3 — Resumo consolidado das reservas

**Entrada:** `GET /skybook/bookings/summary?email=joao@email.com`

**Saída esperada (200 OK):**
```json
{
  "passengerName": "João Silva",
  "passengerEmail": "joao@email.com",
  "totalAmount": 348.79,
  "bookings": [
    {
      "bookingId": 1,
      "seatCode": "1A",
      "seatPrice": 198.89,
      "bookedAt": "2026-05-29T20:00:00"
    },
    {
      "bookingId": 2,
      "seatCode": "3C",
      "seatPrice": 149.90,
      "bookedAt": "2026-05-29T20:00:00"
    }
  ]
}
```

**Erros possíveis:**
- `404 Not Found` — e-mail não encontrado no sistema

### Como Executar os Testes

```bash
./mvnw test
```

| Tipo | Cenários cobertos |
|---|---|
| Unitários | [ex: Service layer — regras de negócio] |
| Integração / API | [ex: Endpoints REST — status codes e payloads] |

### Pipeline CI/CD

Configurado via GitHub Actions em `.github/workflows/`.

**Executa a cada push:**
- Lint / verificação de estilo
- Testes automatizados

---

## Parte 3 — Uso de IA no Desenvolvimento

### Ferramentas de IA Utilizadas

| Etapa | Ferramenta | Modelo | Descrição do uso |
|---|---|---|---|
| Especificação | Kiro | Claude Sonnet 4.6 | Definição de requisitos, escopo e steerings do projeto |
| Arquitetura | Kiro | Claude Sonnet 4.6 | Planejamento da estrutura MVC e modelagem de dados |
| Geração de código | Kiro | Claude Sonnet 4.6 | Criação das entidades JPA e DTOs |
| Refatoração | Kiro | Claude Sonnet 4.6 | Refatoração do BookingService com princípios SOLID (SRP, DIP) e Clean Code |
| Testes | Kiro | Claude Sonnet 4.6 | Geração da suíte de testes unitários com JUnit e Mockito |
| Documentação | Kiro | Claude Sonnet 4.6 | Criação do data-model, template de README e docs/prompts.md |
| Pipeline CI/CD | [ex: Kiro] | [ex: Claude Sonnet 4.6] | [ex: Configuração do GitHub Actions] |

### Padrões de Prompting Aplicados

Os prompts utilizados estão organizados em [`docs/prompts.md`](docs/prompts.md).

#### Zero Shot

**Quando foi usado:** Criação do steering de produto — instrução direta sem exemplos prévios de formato ou conteúdo.

**Prompt original:**
```
Instrução
Crie o steering the produto deste projeto.
Detalhes
1. Esse projeto é um sistema de reservas de poltronas de uma aeronave.
2. As funcionalidades para o MVP devem ser:
2.1. Listar as poltronas da aeronave com seu status (disponível ou não).
2.2. Realizar as reservas de poltronas escolhidas.
2.3. Obter o resumo das reservas e o valor total.
```

#### Few Shot

**Quando foi usado:** Criação das entidades JPA — o prompt forneceu um exemplo concreto da estrutura de código esperada.

**Prompt original:**
```
Crie as entidades de acordo com a modelagem do projeto.
As entidades devem ser criadas seguindo este exemplo:
<code>
@Entity
@Data
@Table(name = "table_name")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class <entity-name>Entity {...}
<code>
Restrições
Apenas crie as entidades neste momento. Não crie outras funcionalidades não solicitadas.
```

#### Chain of Thought

**Quando foi usado:** [Cole aqui o prompt Chain of Thought utilizado]

**Prompt original:**
```
[Cole aqui o prompt Chain of Thought utilizado]
```

### Ciclos de Geração e Refinamento com IA

> Documentação detalhada dos ciclos em [`docs/feat-list-seats.md`](docs/feat-list-seats.md)

#### Ciclo 1 — [Nome da funcionalidade]

**Padrão:** [ex: Zero Shot]

**Prompt utilizado:** ver [`docs/prompts.md`](docs/prompts.md)

**Resultado gerado:** [Descreva o que foi gerado]

**Avaliação crítica:** [O que estava correto / incorreto / o que foi ajustado]

#### Ciclo 2 — [Nome da funcionalidade]

**Padrão:** [ex: Few Shot]

**Prompt utilizado:** ver [`docs/prompts.md`](docs/prompts.md)

**Resultado gerado:** [Descreva o que foi gerado]

**Avaliação crítica:** [O que estava correto / incorreto / o que foi ajustado]

#### Ciclo 3 — [Nome da funcionalidade]

**Padrão:** [ex: Chain of Thought]

**Prompt utilizado:** ver [`docs/prompts.md`](docs/prompts.md)

**Resultado gerado:** [Descreva o que foi gerado]

**Avaliação crítica:** [O que estava correto / incorreto / o que foi ajustado]

### Refatoração com IA

**Critério aplicado:** [ex: Princípio da Responsabilidade Única (SOLID), Clean Code]

**Antes:**
```java
// Código antes da refatoração
```

**Prompt utilizado:**
```
[Cole o prompt de refatoração aqui]
```

**Depois:**
```java
// Código após a refatoração
```

**Avaliação do resultado:** [Descreva o que melhorou e o que foi aprendido]

### Análise Crítica — Saída Incorreta da IA

**Problema identificado:**

[Descreva o que a IA gerou de incorreto ou insuficiente]

**Correção aplicada:**

[Descreva o que foi corrigido e como]

**Lição aprendida:**

[O que esse caso ensinou sobre o uso de IA no desenvolvimento]
