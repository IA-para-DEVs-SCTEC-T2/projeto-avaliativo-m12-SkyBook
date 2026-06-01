# Arquitetura e Estrutura do Projeto

## Estrutura de Diretórios Raiz

```
projeto-avaliativo-m12-SkyBook/
├── backend/    # API REST — Java/Spring Boot
├── frontend/   # Interface web — React + Atomic Design
└── docs/       # Documentação do projeto
```

---

## Backend

### Padrão Arquitetural

O backend segue a arquitetura **MVC (Model-View-Controller)**, organizada nas seguintes camadas:

- **Controller** — recebe as requisições HTTP, delega para a camada de serviço e retorna as respostas
- **Service** — contém a lógica de negócio
- **Repository** — responsável pelo acesso e persistência dos dados via Spring Data JPA
- **Model/Entity** — entidades JPA que representam as tabelas do banco de dados
- **DTO** — objetos de transferência de dados usados nas entradas e saídas dos endpoints

### Estrutura de Pacotes

```
backend/src/main/java/com/ia/para/devs/skybook
├── config/
│   ├── AirplaneSeatDataLoader.java   # Carga inicial das 60 poltronas
│   └── WebConfig.java                # Configuração de CORS
├── controller/
│   ├── AirplaneSeatController.java   # GET /seats/listSeats
│   ├── BookingController.java        # POST /bookings/bookSeat, GET /bookings/summary
│   └── GlobalExceptionHandler.java   # Tratamento centralizado de erros
├── dto/
│   ├── AirplaneSeatResponseDTO.java
│   ├── BookingItemDTO.java
│   ├── BookingRequestDTO.java
│   ├── BookingResponseDTO.java
│   ├── BookingSummaryResponseDTO.java
│   └── ErrorResponseDTO.java
├── model/
│   ├── AirplaneSeatEntity.java       # Tabela airplane_seat
│   ├── BookingEntity.java            # Tabela booking
│   └── UserEntity.java               # Tabela app_user
├── repository/
│   ├── AirplaneSeatRepository.java
│   ├── BookingRepository.java
│   └── UserRepository.java
├── service/
│   ├── AirplaneSeatService.java
│   ├── BookingService.java
│   └── UserService.java
└── SkybookApplication.java           # Entry point
```

### DTOs

- Todos os endpoints devem utilizar **DTOs** para receber dados de entrada (request) e retornar dados de saída (response)
- DTOs não devem expor diretamente as entidades JPA
- Nomeação sugerida: `<Entidade>RequestDTO` para entrada e `<Entidade>ResponseDTO` para saída

### Entidades JPA

- As entidades devem ser anotadas com `@Entity` e mapeadas para as tabelas do banco H2
- Usar `@Id` e `@GeneratedValue` para chaves primárias
- As entidades **não devem ser retornadas diretamente** pelos endpoints — sempre converter para DTO
- O banco de dados utilizado é o **H2 em memória**, gerenciado automaticamente pelo Spring Data JPA

#### Entidades do domínio

| Entidade             | Tabela          | Descrição                                      |
|----------------------|-----------------|------------------------------------------------|
| `AirplaneSeatEntity` | `airplane_seat` | Dados de cada poltrona da aeronave (código, preço, disponibilidade) |
| `UserEntity`         | `app_user`      | Dados do usuário que realiza reservas          |
| `BookingEntity`      | `booking`       | Reserva — vínculo entre um usuário e um assento |

> Diagrama ER completo em [`docs/data-model.md`](../../docs/data-model.md)

---

## Frontend

### Padrão Arquitetural

O frontend segue os princípios do **Atomic Design**, que organiza os componentes em níveis de complexidade crescente:

| Nível | Descrição | Exemplos |
|---|---|---|
| **Atoms** | Elementos básicos e indivisíveis da UI | Botão, Input, Label, Badge de status |
| **Molecules** | Combinação de átomos com função específica | Card de poltrona, Campo de formulário com label |
| **Organisms** | Seções completas compostas de moléculas | Mapa de assentos, Formulário de reserva, Resumo da compra |
| **Templates** | Layout de página sem dados reais | Estrutura da tela de seleção de assentos |
| **Pages** | Templates com dados reais conectados à API | Página de listagem, Página de confirmação |

### Estrutura de Diretórios

```
frontend/src/
├── atoms/
│   ├── Modal/                        # Componente base de modal
│   ├── MoneyValue/                   # Exibição de valores monetários
│   ├── StatusBadge/                  # Badge de status da poltrona
│   └── Text/                         # Componente de texto
├── molecules/
│   └── SeatCard/                     # Card de poltrona (disponível/indisponível/selecionado)
├── organisms/
│   ├── BookingSummaryModal/          # Modal de consulta de reservas por e-mail
│   ├── ConfirmBookingModal/          # Modal passo 1 — resumo da seleção
│   ├── PassengerFormModal/           # Modal passo 2 — dados do passageiro
│   ├── SeatMap/                      # Grid de 60 poltronas
│   └── TotalPanel/                   # Painel lateral com total e botões de ação
├── templates/
│   ├── MainLayout/                   # Layout base da aplicação
│   └── SeatMapLayout/                # Layout da tela de seleção de poltronas
├── pages/
│   ├── HomePage/                     # Redireciona para /skybook
│   └── SeatMapPage/                  # Página principal com grid + painel
├── services/
│   ├── bookingService.js             # POST /bookings/bookSeat, GET /bookings/summary
│   └── seatsService.js               # GET /seats/listSeats
├── hooks/
│   ├── useBooking.js                 # Lógica de reserva e modais
│   └── useSeatSelection.js           # Gerenciamento de seleção e cálculo do total
├── App.jsx                           # Configuração de rotas
└── main.jsx                          # Entry point
```

### Convenções

- Cada componente em seu próprio diretório com arquivo `index.jsx`
- Diretórios de componentes nomeados em PascalCase (ex: `SeatCard/`, `TotalPanel/`)
- Comunicação com o backend exclusivamente via camada `services/`
- Variável de ambiente `VITE_API_BASE_URL` para URL base da API
