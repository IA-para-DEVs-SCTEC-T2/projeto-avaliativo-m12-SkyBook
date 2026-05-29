# Funcionalidade: Reserva de Poltronas (`POST /bookings/bookSeat`)

Este documento registra os ciclos de geração e refinamento com IA utilizados no desenvolvimento da funcionalidade de reserva de poltronas da aeronave.

---

## Ciclo 1 — Geração Inicial (Zero shot)

> Prompt enviado sem exemplos ou estrutura de raciocínio explícita. A IA recebeu a descrição da demanda e gerou a implementação completa a partir do zero.

**Padrão de prompting:** Zero shot  
**Data:** 2026-05-29 18:30  
**Autor:** joaopuel

### Prompt

```
Busque a issue 15 Realização de reserva de poltrona e implemente suas funcionalidades e implemente testes unitários
```

### O que foi gerado

A partir deste prompt, a IA:

- Buscou os critérios de aceitação da issue #15 no GitHub
- Criou os seguintes artefatos:
  - `BookingRequestDTO` — DTO de entrada com `passengerName`, `passengerEmail` e `seatIds`
  - `BookingResponseDTO` — DTO de saída com dados da reserva criada
  - `BookingRepository` — interface Spring Data JPA para `BookingEntity`
  - `UserRepository` — interface com `findByEmail`
  - `BookingService` — lógica de negócio: validação de disponibilidade, criação/reutilização de usuário, persistência da reserva e atualização do status da poltrona
  - `BookingController` — endpoint `POST /bookings` retornando 201 Created
  - `BookingServiceTest` — 7 testes unitários cobrindo os 3 cenários BDD da issue
  - `BookingControllerTest` — 3 testes unitários

### Resultado

17 testes passando. Implementação funcional cobrindo os cenários: reserva de poltrona disponível, erro 409 para poltrona indisponível, erro 404 para poltrona não encontrada e reserva de múltiplas poltronas.

---

## Ciclo 2 — Refinamento (Few shot)

> Prompt com exemplo de código concreto fornecido pelo desenvolvedor. A IA usou o exemplo como referência para aplicar o padrão solicitado.

**Padrão de prompting:** Few shot  
**Data:** 2026-05-29 19:00  
**Autor:** joaopuel

### Prompt

```
Realize os seguintes ajustes:1. O endpoint para a realização da reserva deve ter a rota /bookings/bookSeat2. Crie um DTO para armazenar as informações de erros. Ele deve conter o status do erro, a mensagem de erro e o timestamp de quando ocorreu.3. Para lidar com os erros desse endpoint, deve ser criada a classe GlobalExceptionHandler que deve capturar os erros e retornar o objeto DTO que armazena as informações de erro.Como nesse exemplo:@ExceptionHandler(<excpetion-name>.class) public ResponseEntity<<error-dto>> handlerBadRequest(<excpetion-name> ex) {     return ResponseEntity //             .status(HttpStatus.<status>) //             .body(new <error-dto>(HttpStatus.<status>.name(), ex.getMessage(), LocalDateTime.now())); }
```

### O que foi refinado

A partir do exemplo fornecido no prompt, a IA:

- Alterou a rota do endpoint de `POST /bookings` para `POST /bookings/bookSeat`
- Criou `ErrorResponseDTO` com campos `status` (`HttpStatus`), `message` (`String`) e `timestamp` (`LocalDateTime`)
- Criou `GlobalExceptionHandler` com `@RestControllerAdvice` seguindo exatamente o padrão do exemplo:
  - Handler para `ResponseStatusException` → retorna o status da exceção + mensagem + timestamp
  - Handler genérico para `Exception` → retorna 500 + mensagem + timestamp

### Resultado

17 testes passando. Erros da API agora retornam respostas padronizadas via `ErrorResponseDTO`.

---

## Ciclo 3 — Padrão Diferente (Chain of Thought)

> Prompt solicitando explicitamente que a IA descrevesse seu processo de pensamento antes de aplicar as alterações. A IA mapeou todos os pontos afetados, descreveu cada passo e só então executou as mudanças.

**Padrão de prompting:** Chain of Thought  
**Data:** 2026-05-29 19:15  
**Autor:** joaopuel

### Prompt

```
Em vez de Ids, a lista de poltronas no objeto de entrada do enpoint bookSeat deve ser formado pelos códigos string das poltronas. Altere todos os pontos necessários.
Quebre essa alteração em pequenos passos, descrevendo seu processo de pensamento, e aplique as alterações.
```

### Raciocínio explicitado pela IA

A IA mapeou a cadeia de dependências antes de alterar qualquer arquivo:

1. `BookingRequestDTO` — origem da mudança: `List<Long> seatIds` → `List<String> seatCodes`
2. `AirplaneSeatRepository` — adicionar `findByCode(String code)` para busca por código
3. `BookingService` — `resolveAndValidateSeats` passa a receber `List<String>` e usar `findByCode`
4. `BookingServiceTest` — substituir `findById(Long)` por `findByCode(String)` e `List.of(1L)` por `List.of("1A")`
5. `BookingControllerTest` — substituir `List.of(1L, 2L)` por `List.of("1A", "1B")`

### O que foi alterado

- `BookingRequestDTO`: campo renomeado e tipo alterado de `List<Long>` para `List<String>`
- `AirplaneSeatRepository`: novo método `findByCode(String code)`
- `BookingService`: método privado `resolveAndValidateSeats` atualizado para busca por código
- `BookingServiceTest`: todos os mocks e dados de teste atualizados para usar códigos string
- `BookingControllerTest`: todos os dados de teste atualizados para usar códigos string

### Resultado

17 testes passando. O endpoint agora aceita `["1A", "3C"]` em vez de `[1, 3]` como entrada.
