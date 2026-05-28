# Arquitetura e Estrutura do Projeto

## Padrão Arquitetural

O projeto segue a arquitetura **MVC (Model-View-Controller)**, organizada nas seguintes camadas:

- **Controller** — recebe as requisições HTTP, delega para a camada de serviço e retorna as respostas
- **Service** — contém a lógica de negócio
- **Repository** — responsável pelo acesso e persistência dos dados via Spring Data JPA
- **Model/Entity** — entidades JPA que representam as tabelas do banco de dados
- **DTO** — objetos de transferência de dados usados nas entradas e saídas dos endpoints

## Estrutura de Pacotes

```
com.ia.para.devs.skybook
├── controller      # Controllers REST (camada MVC - Controller)
├── service         # Lógica de negócio (camada MVC - intermediária)
├── repository      # Interfaces Spring Data JPA
├── model           # Entidades JPA (camada MVC - Model)
└── dto             # DTOs de request e response
```

## DTOs

- Todos os endpoints devem utilizar **DTOs** para receber dados de entrada (request) e retornar dados de saída (response)
- DTOs não devem expor diretamente as entidades JPA
- Nomeação sugerida: `<Entidade>RequestDTO` para entrada e `<Entidade>ResponseDTO` para saída

## Entidades JPA

- As entidades devem ser anotadas com `@Entity` e mapeadas para as tabelas do banco H2
- Usar `@Id` e `@GeneratedValue` para chaves primárias
- As entidades **não devem ser retornadas diretamente** pelos endpoints — sempre converter para DTO
- O banco de dados utilizado é o **H2 em memória**, gerenciado automaticamente pelo Spring Data JPA

### Entidades do domínio

| Entidade             | Tabela          | Descrição                                      |
|----------------------|-----------------|------------------------------------------------|
| `AirplaneSeatEntity` | `airplane_seat` | Dados de cada poltrona da aeronave (código, preço, disponibilidade) |
| `UserEntity`         | `app_user`      | Dados do usuário que realiza reservas          |
| `BookingEntity`      | `booking`       | Reserva — vínculo entre um usuário e um assento |

> Diagrama ER completo em [`docs/data-model.md`](../../docs/data-model.md)
