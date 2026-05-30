# Arquitetura e Estrutura do Projeto

## Estrutura de Diretórios Raiz

```
projeto-avaliativo-m12-SkyBook/
├── backend/    # API REST — Java/Spring Boot
├── frontend/   # Interface web (a ser implementada)
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
├── controller      # Controllers REST (camada MVC - Controller)
├── service         # Lógica de negócio (camada MVC - intermediária)
├── repository      # Interfaces Spring Data JPA
├── model           # Entidades JPA (camada MVC - Model)
└── dto             # DTOs de request e response
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
frontend/
├── public/                 # Arquivos estáticos públicos
├── src/
│   ├── atoms/              # Componentes atômicos (botões, inputs, badges)
│   ├── molecules/          # Componentes moleculares (cards, campos compostos)
│   ├── organisms/          # Seções completas (mapa de assentos, formulários)
│   ├── templates/          # Layouts de página sem dados
│   ├── pages/              # Páginas com dados reais
│   ├── services/           # Comunicação com a API REST do backend
│   ├── hooks/              # Custom hooks React
│   ├── assets/             # Imagens, ícones e fontes
│   └── main.jsx            # Ponto de entrada da aplicação
├── .env                    # Variáveis de ambiente (URL base da API)
├── package.json
└── README.md
```

### Convenções

- Cada componente em seu próprio diretório com arquivo `index.jsx`
- Comunicação com o backend exclusivamente via camada `services/`
- Variável de ambiente `VITE_API_BASE_URL` para URL base da API
- Componentes nomeados em PascalCase; arquivos em kebab-case
