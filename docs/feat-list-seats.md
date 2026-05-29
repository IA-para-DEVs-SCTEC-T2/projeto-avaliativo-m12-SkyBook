# Funcionalidade: Listagem de Poltronas — Ciclos de Geração com IA

Documentação dos ciclos de geração e refinamento de código com IA para a feature de listagem de poltronas da aeronave (`GET /seats/listSeats`), conforme exigido pelo projeto avaliativo.

---

## Ciclo 1 — Zero Shot

> Prompt enviado diretamente à IA sem exemplos ou instruções de formato. Apenas o objetivo foi descrito.

### Padrão aplicado
**Zero Shot** — o modelo recebe apenas a instrução do que deve ser feito, sem exemplos de entrada/saída ou estrutura esperada.

### Prompt

```
Busque informações da issue 14 e implemente a funcionalidade proposta
```

### Contexto fornecido
- Steerings do projeto ativos: `product.md`, `structure.md`, `tech.md`
- Entidades JPA já existentes: `AirplaneSeatEntity`, `UserEntity`, `BookingEntity`
- Configuração do projeto: Spring Boot 4.0.6, H2, Lombok, SpringDoc OpenAPI

### Resultado gerado pela IA

A IA leu a issue #14 no GitHub e gerou os seguintes artefatos:

- `AirplaneSeatResponseDTO` — DTO de saída com `id`, `code`, `price`, `available`
- `AirplaneSeatRepository` — interface Spring Data JPA
- `AirplaneSeatService` — lista todas as poltronas, converte para DTO
- `AirplaneSeatController` — endpoint `GET /seats` com anotações `@Tag` e `@Operation`

### Avaliação do resultado

O código gerado estava funcional e compilou com sucesso. Porém, foram identificados pontos de melhoria:

- A rota do endpoint estava genérica (`GET /seats`) — o padrão do projeto exige rotas mais descritivas
- Faltavam Javadoc nos métodos e classes públicas
- Faltava a anotação `@ApiResponses` descrevendo os status HTTP retornados

Esses pontos motivaram o **Ciclo 2** de refinamento.

---

## Ciclo 2 — Few Shot (Refinamento)

> Prompt de refinamento com exemplo concreto da anotação esperada, guiando a IA para um padrão específico.

### Padrão aplicado
**Few Shot** — o prompt inclui um exemplo real da estrutura desejada (`@ApiResponses`), permitindo que a IA entenda o padrão e o aplique corretamente em todos os pontos necessários.

### Prompt

```
Ajustes:1. Adicione javadoc em todos os métodos publicos e nas classes criadas.2. O endpoints deve ter a roda /listSeats3. Adicione também no endpoint a anotação que descreve o status 200 retornado pelo endpoint. Para isso, siga o seguinte exemplo:@ApiResponses({     @ApiResponse(responseCode = "200", description = "descrição do status") })
```

### Exemplo fornecido no prompt (Few Shot)

```java
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "descrição do status")
})
```

### Resultado gerado pela IA

A IA aplicou os três ajustes solicitados:

1. **Javadoc** adicionado em todas as classes e métodos públicos:
   - `AirplaneSeatController` — Javadoc na classe e no método `listAllSeats`
   - `AirplaneSeatService` — Javadoc na classe e no método `listAllSeats`
   - `AirplaneSeatRepository` — Javadoc na interface
   - `AirplaneSeatResponseDTO` — Javadoc na classe e nos campos

2. **Rota atualizada** de `GET /seats` para `GET /seats/listSeats`

3. **`@ApiResponses`** adicionado ao endpoint:
   ```java
   @ApiResponses({
       @ApiResponse(responseCode = "200", description = "Lista de poltronas retornada com sucesso")
   })
   ```

### Avaliação do resultado

O refinamento foi aplicado corretamente em todos os pontos. O build compilou com sucesso (`BUILD SUCCESS`). O exemplo fornecido no prompt (Few Shot) foi determinante para que a IA entendesse o formato exato da anotação esperada.

---

## Resumo dos Ciclos

| Ciclo | Padrão    | Objetivo                                      | Resultado         |
|-------|-----------|-----------------------------------------------|-------------------|
| 1     | Zero Shot | Gerar a feature completa a partir da issue    | Funcional, com pontos de melhoria identificados |
| 2     | Few Shot  | Refinar: Javadoc + rota + `@ApiResponses`     | Ajustes aplicados corretamente, build OK |
