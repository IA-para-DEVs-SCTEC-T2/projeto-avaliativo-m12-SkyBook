# Funcionalidade: Resumo Consolidado da Compra — Ciclos de Geração com IA

Documentação dos ciclos de geração e refinamento de código com IA para a feature de resumo consolidado das reservas (`GET /bookings/summary`), conforme exigido pelo projeto avaliativo.

---

## Ciclo 1 — Zero Shot

> Prompt enviado diretamente à IA sem exemplos ou instruções de formato. Apenas o objetivo foi descrito.

### Padrão aplicado
**Zero Shot** — o modelo recebe apenas a instrução do que deve ser feito, sem exemplos de entrada/saída ou estrutura esperada.

### Prompt

```
Busque a issue 16 Resumo consolidado da compra e implemente a funcionalidade e os testes
```

### Contexto fornecido
- Steerings do projeto ativos: `product.md`, `structure.md`, `tech.md`
- Implementações já existentes: `BookingService`, `AirplaneSeatService`, `UserService`, `BookingRepository`
- Configuração do projeto: Spring Boot 4.0.6, H2, Lombok, SpringDoc OpenAPI

### Resultado gerado pela IA

A IA leu a issue #16 no GitHub e gerou os seguintes artefatos:

- `BookingItemDTO` — DTO de item individual com `bookingId`, `seatCode`, `seatPrice`, `bookedAt`
- `BookingSummaryResponseDTO` — DTO de saída com `List<BookingResponseDTO>` e `totalAmount`
- `BookingService.getSummary()` — busca todas as reservas via `findAll()`, calcula total por `reduce`
- `BookingController GET /bookings/summary` — retorna 200 com o DTO de resumo
- Testes unitários: 2 cenários no `BookingServiceTest` + 2 no `BookingControllerTest`

### Avaliação do resultado

O código gerado estava funcional e compilou com sucesso (26 testes passando). Porém, foram identificados pontos de melhoria:

- O endpoint retornava todas as reservas do sistema, sem filtro por usuário — o requisito exige filtro por e-mail
- O DTO de resposta não incluía `passengerName` e `passengerEmail` — informações necessárias para identificar o passageiro
- O template de resposta esperado pelo produto diferia da estrutura gerada

Esses pontos motivaram o **Ciclo 2** de refinamento.

---

## Ciclo 2 — Few Shot (Refinamento)

> Prompt de refinamento com exemplo concreto do formato de resposta esperado, guiando a IA para a estrutura correta.

### Padrão aplicado
**Few Shot** — o prompt inclui um exemplo real da estrutura JSON esperada, permitindo que a IA entenda o formato exato e o aplique corretamente em todos os artefatos afetados.

### Prompt

```
Ajuste o endpoint /summary.
Ele deve receber o email do usuário e retornar apenas as reservas feitas naquele e-mail.
O resultado deve seguir este template:
{
  "passengerName": <name>,
  "passengerEmail": <email>,
  "totalAmount": <total>,
  "bookings": [
    {
      "bookingId": <id>,
      "seatCode": <code>,
      "seatPrice": <individual-price>,
      "bookedAt": <date-time>
    }
  ]
}
```

### Exemplo fornecido no prompt (Few Shot)

```json
{
  "passengerName": <name>,
  "passengerEmail": <email>,
  "totalAmount": <total>,
  "bookings": [
    {
      "bookingId": <id>,
      "seatCode": <code>,
      "seatPrice": <individual-price>,
      "bookedAt": <date-time>
    }
  ]
}
```

### Resultado gerado pela IA

A IA mapeou todos os pontos afetados pela mudança e aplicou os ajustes:

1. **`BookingItemDTO`** — novo DTO de item com `bookingId`, `seatCode`, `seatPrice`, `bookedAt` (sem `passengerName`, que agora fica no nível raiz)

2. **`BookingSummaryResponseDTO`** — reformulado com `passengerName`, `passengerEmail`, `totalAmount`, `List<BookingItemDTO>`

3. **`BookingRepository`** — adicionado `findByUserEmail(String email)` para busca filtrada

4. **`UserService`** — adicionado `findByEmail(String email)` para manter o DIP (BookingService não acessa UserRepository diretamente)

5. **`BookingService.getSummary(email)`** — filtra por email, lança 404 se usuário não encontrado, calcula total apenas das reservas do passageiro

6. **`BookingController GET /bookings/summary?email=...`** — recebe `@RequestParam String email`

7. **Testes atualizados**:
   - `BookingServiceTest`: 3 cenários (com reservas, sem reservas, 404 para email não encontrado)
   - `BookingControllerTest`: 2 cenários (com reservas, lista vazia)

### Avaliação do resultado

O refinamento foi aplicado corretamente em todos os pontos. O build compilou com sucesso (26 testes passando). O template JSON fornecido no prompt (Few Shot) foi determinante para que a IA entendesse a estrutura exata esperada — especialmente a separação entre os dados do passageiro no nível raiz e os itens de reserva na lista `bookings`.

---

## Resumo dos Ciclos

| Ciclo | Padrão    | Objetivo                                                        | Resultado         |
|-------|-----------|-----------------------------------------------------------------|-------------------|
| 1     | Zero Shot | Gerar a feature completa a partir da issue                      | Funcional, mas sem filtro por email e com DTO diferente do esperado |
| 2     | Few Shot  | Refinar: filtro por email + novo formato de resposta com template JSON | Ajustes aplicados corretamente, build OK, 26 testes passando |

---

## Lições Aprendidas

Esta seção documenta as lições extraídas de cada ciclo de geração e refinamento com IA ao longo do desenvolvimento da funcionalidade de resumo consolidado da compra.

---

### Ciclo 1 — Zero shot: a IA gera uma base funcional, mas sem contexto de produto

O prompt inicial foi direto e sem exemplos. A IA buscou a issue no GitHub, interpretou os critérios BDD e gerou uma implementação funcional. No entanto, a saída não estava alinhada com o formato esperado pelo produto:

- O endpoint retornava todas as reservas do sistema, sem filtro por passageiro
- O DTO de resposta agrupava os dados de forma genérica, sem separar as informações do passageiro dos itens de reserva
- A estrutura `{ passengerName, passengerEmail, totalAmount, bookings: [...] }` não foi inferida automaticamente

**Lição:** Zero shot é eficiente para gerar uma base funcional rapidamente, mas a IA não conhece o formato de resposta esperado pelo produto. Quando há um template de saída definido, ele precisa ser fornecido explicitamente — a IA não o infere a partir da descrição textual da issue.

---

### Ciclo 2 — Few shot: o template JSON elimina ambiguidade na estrutura de resposta

Ao fornecer o template JSON completo no prompt, a IA entendeu exatamente a estrutura esperada e propagou a mudança por todos os artefatos afetados: DTO, repositório, serviço, controller e testes. A separação entre dados do passageiro no nível raiz e itens de reserva na lista `bookings` foi aplicada corretamente sem necessidade de correção posterior.

**Lição:** Few shot com template de resposta é o padrão mais eficaz quando o formato de saída da API é específico. Fornecer o JSON esperado como exemplo elimina a ambiguidade e reduz o risco de a IA gerar uma estrutura funcional, mas incompatível com o contrato da API. Quanto mais preciso o template, mais precisa a implementação gerada.

---

### Lição geral: o formato de saída precisa ser explicitado

Ao longo dos dois ciclos, ficou evidente que a IA consegue inferir a lógica de negócio a partir da issue, mas não consegue inferir o formato exato de resposta da API sem um exemplo concreto. A descrição textual "retornar resumo com valor total" é suficiente para gerar código funcional, mas insuficiente para garantir o contrato correto da API. O template JSON no Ciclo 2 foi o elemento que fechou essa lacuna.
