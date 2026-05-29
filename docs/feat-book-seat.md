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

---

## Refatoração — SOLID e Clean Code no `BookingService`

> Documentação da refatoração aplicada com suporte de IA, conforme critério 11 do projeto avaliativo: prompt utilizado, estado anterior do código, resultado gerado e critério técnico aplicado.

**Padrão de prompting:** Chain of Thought  
**Data:** 2026-05-29 20:00  
**Autor:** joaopuel  
**Critério técnico:** SRP (Single Responsibility Principle) + DIP (Dependency Inversion Principle) do SOLID + Clean Code (métodos pequenos e com nome expressivo)

---

### Prompt utilizado

```
Refatore a classe BookingService, usando princípios do SOLID e clean code.
1. Por exemplo, seguindo o Princípio da Responsabilidade Única, separe a funcionalidade do método createBookings em métodos menores.
2. Além disso, faça ajustes para que a busca e a atualização de poltronas seja realizada no AirplaneSeatService. A busca e criação de usuáros seja feita no UserService. Enquanto o BookingService apenas lida com o que diz respeito as reservas.
3. Os repositórios airplaneSeatRepository, bookingRepository e userRepository somente devem ser acessados pelos serviços correspondentes.
Faça ajustes nos testes unitários para que estejam de acordo com estas alterações.
Divida as alterações em pequenos passos, demostrando a sua linha de pensamento, e, depois, aplique as alterações.
```

---

### Estado anterior (antes da refatoração)

`BookingService` acumulava três responsabilidades distintas:

```java
@Service
@RequiredArgsConstructor
public class BookingService {

    // Violação do DIP: dependia diretamente de 3 repositórios
    private final AirplaneSeatRepository airplaneSeatRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<BookingResponseDTO> createBookings(BookingRequestDTO request) {
        // Responsabilidade 1: buscar e validar poltronas (deveria ser do AirplaneSeatService)
        List<AirplaneSeatEntity> seats = resolveAndValidateSeats(request.getSeatCodes());

        // Responsabilidade 2: criar/reutilizar usuário (deveria ser do UserService)
        UserEntity user = resolveUser(request.getPassengerName(), request.getPassengerEmail());

        List<BookingResponseDTO> responses = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        for (AirplaneSeatEntity seat : seats) {
            // Responsabilidade 3: atualizar poltrona (deveria ser do AirplaneSeatService)
            seat.setAvailable(false);
            airplaneSeatRepository.save(seat);

            // Responsabilidade 4: persistir reserva (única responsabilidade legítima)
            BookingEntity booking = new BookingEntity();
            booking.setUser(user);
            booking.setSeat(seat);
            booking.setBookedAt(now);
            bookingRepository.save(booking);

            responses.add(new BookingResponseDTO(...));
        }
        return responses;
    }

    // Método privado que deveria estar no AirplaneSeatService
    private List<AirplaneSeatEntity> resolveAndValidateSeats(List<String> seatCodes) { ... }

    // Método privado que deveria estar no UserService
    private UserEntity resolveUser(String name, String email) { ... }
}
```

**Problemas identificados:**
- `BookingService` conhecia e acessava diretamente `AirplaneSeatRepository` e `UserRepository` — violação do DIP
- Um único método `createBookings` fazia busca, validação, atualização de poltrona, criação de usuário e persistência de reserva — violação do SRP
- Métodos privados `resolveAndValidateSeats` e `resolveUser` pertenciam conceitualmente a outros serviços

---

### Resultado gerado (após a refatoração)

**Novos artefatos criados:**

`UserService` — responsabilidade única: gerenciar usuários
```java
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository; // único acesso ao repositório

    public UserEntity resolveOrCreate(String name, String email) {
        return userRepository.findByEmail(email).orElseGet(() -> createUser(name, email));
    }

    private UserEntity createUser(String name, String email) { ... }
}
```

`AirplaneSeatService` — ganhou dois novos métodos públicos:
```java
public List<AirplaneSeatEntity> findAndValidateAvailableSeats(List<String> seatCodes) {
    return seatCodes.stream().map(this::findAvailableSeat).toList();
}

public AirplaneSeatEntity markAsUnavailable(AirplaneSeatEntity seat) {
    seat.setAvailable(false);
    return airplaneSeatRepository.save(seat);
}
```

`BookingService` refatorado — responsabilidade única: orquestrar reservas
```java
@Service
@RequiredArgsConstructor
public class BookingService {

    // Depende de serviços, não de repositórios — DIP respeitado
    private final BookingRepository bookingRepository;
    private final AirplaneSeatService airplaneSeatService;
    private final UserService userService;

    @Transactional
    public List<BookingResponseDTO> createBookings(BookingRequestDTO request) {
        List<AirplaneSeatEntity> seats =
            airplaneSeatService.findAndValidateAvailableSeats(request.getSeatCodes());
        UserEntity user =
            userService.resolveOrCreate(request.getPassengerName(), request.getPassengerEmail());
        LocalDateTime now = LocalDateTime.now();

        return seats.stream()
                .map(seat -> bookSeat(user, seat, now))
                .toList();
    }

    // Métodos privados com nomes expressivos — Clean Code
    private BookingResponseDTO bookSeat(UserEntity user, AirplaneSeatEntity seat, LocalDateTime bookedAt) {
        airplaneSeatService.markAsUnavailable(seat);
        BookingEntity booking = persistBooking(user, seat, bookedAt);
        return toResponseDTO(booking, seat, user);
    }

    private BookingEntity persistBooking(UserEntity user, AirplaneSeatEntity seat, LocalDateTime bookedAt) { ... }

    private BookingResponseDTO toResponseDTO(BookingEntity booking, AirplaneSeatEntity seat, UserEntity user) { ... }
}
```

---

### Avaliação do resultado

| Critério | Antes | Depois |
|---|---|---|
| SRP | ❌ 1 classe com 4 responsabilidades | ✅ Cada serviço tem 1 responsabilidade |
| DIP | ❌ `BookingService` acessava 3 repositórios | ✅ Cada repositório acessado apenas pelo serviço correspondente |
| Clean Code | ❌ Método `createBookings` com ~20 linhas misturando lógicas | ✅ Métodos pequenos com nomes expressivos: `bookSeat`, `persistBooking`, `toResponseDTO` |
| Testes | ❌ Mocks de repositórios em `BookingServiceTest` | ✅ Mocks de serviços; novos `UserServiceTest` e cenários em `AirplaneSeatServiceTest` |

**Resultado dos testes:** 21 testes passando (antes: 17).

---

## Lições Aprendidas

Esta seção documenta as lições extraídas de cada ciclo de geração, refinamento e refatoração com IA ao longo do desenvolvimento da funcionalidade de reserva de poltronas.

---

### Ciclo 1 — Zero shot: a IA gera, mas não conhece o contexto do projeto

O prompt inicial foi direto e sem exemplos. A IA buscou a issue no GitHub, interpretou os critérios BDD e gerou uma implementação funcional completa. No entanto, algumas decisões não estavam alinhadas com as convenções do projeto:

- O endpoint foi criado como `POST /bookings` em vez de `POST /bookings/bookSeat`
- O campo de entrada usava `seatIds` (lista de IDs numéricos) em vez de `seatCodes` (lista de códigos string)
- Não havia tratamento padronizado de erros

**Lição:** Zero shot é eficiente para gerar uma base funcional rapidamente, mas a saída precisa ser revisada criticamente. A IA não tem acesso às convenções implícitas do projeto — rotas, nomenclaturas e padrões de resposta precisam ser explicitados nos ciclos seguintes.

---

### Ciclo 2 — Few shot: exemplos concretos eliminam ambiguidade

Ao fornecer um exemplo real de `@ExceptionHandler` no prompt, a IA reproduziu exatamente o padrão esperado — sem inventar variações. O mesmo ocorreu com a rota: ao especificar `/bookings/bookSeat`, a alteração foi aplicada sem desvios.

**Lição:** Few shot é o padrão mais eficaz quando há um padrão de código específico a seguir. Fornecer um exemplo concreto elimina a ambiguidade e reduz a necessidade de correções posteriores. A IA usa o exemplo como âncora — quanto mais preciso o exemplo, mais precisa a saída.

---

### Ciclo 3 — Chain of Thought: raciocínio explícito evita alterações incompletas

Ao pedir que a IA descrevesse seu processo de pensamento antes de agir, ela mapeou todos os 5 pontos afetados pela mudança (`BookingRequestDTO`, `AirplaneSeatRepository`, `BookingService`, `BookingServiceTest`, `BookingControllerTest`) antes de alterar qualquer arquivo. Isso evitou que algum ponto fosse esquecido.

**Lição:** Chain of Thought é especialmente útil em alterações que propagam por múltiplos arquivos. Forçar a IA a raciocinar antes de agir reduz o risco de alterações incompletas ou inconsistentes. O custo é um prompt mais longo, mas o ganho em confiabilidade compensa.

---

### Refatoração — SOLID: a IA identifica violações, mas precisa de direção

A IA gerou o `BookingService` inicial com múltiplas responsabilidades porque o prompt do Ciclo 1 não especificava separação de camadas. Quando o prompt de refatoração explicitou os princípios SOLID e indicou quais responsabilidades deveriam migrar para quais serviços, a IA aplicou as mudanças corretamente — inclusive criando o `UserService` do zero e expandindo o `AirplaneSeatService`.

**Lição:** A IA não aplica SOLID automaticamente na geração inicial — ela prioriza funcionalidade. A refatoração precisa ser solicitada explicitamente, com os critérios técnicos descritos. Quando o prompt é específico (SRP, DIP, quais classes devem ter quais responsabilidades), a IA executa a refatoração de forma precisa e rastreável.

---

### Lição geral: o prompt define o teto da qualidade da saída

Ao longo dos 4 ciclos, ficou evidente que a qualidade da saída da IA é diretamente proporcional à qualidade do prompt. Um prompt vago gera código funcional, mas com decisões arbitrárias. Um prompt com exemplos, restrições e raciocínio explícito gera código alinhado com as convenções do projeto.

A IA é um parceiro de desenvolvimento eficaz — mas o desenvolvedor precisa saber o que quer e saber comunicar isso com precisão.
