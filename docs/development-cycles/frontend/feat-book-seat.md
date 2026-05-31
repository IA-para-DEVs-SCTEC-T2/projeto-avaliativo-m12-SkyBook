# Funcionalidade: Tela de Reserva de Poltronas

Este documento registra o ciclo de geração e refinamento com IA utilizado no desenvolvimento da tela de reserva de poltronas no frontend React.

---

## Ciclo 1 — Geração Inicial (Chain of Thought)

> Prompt solicitando a implementação completa da funcionalidade a partir dos critérios da issue, com instrução explícita para dividir em pequenos passos e evidenciar o raciocínio antes de aplicar as alterações.

**Padrão de prompting:** Chain of Thought  
**Data:** 2026-05-30 19:15  
**Autor:** joaopuel

### Prompt

```
Busque as informações da issue 26 e implemente as funcionalidades.Divida as implementações em pequenos passos, evidenciando sua linha de raciocício, e prossiga com as modificações.
```

### Raciocínio explicitado pela IA

Antes de escrever qualquer arquivo, a IA analisou o estado atual do frontend e mapeou o que existia versus o que precisava ser criado:

**Existente (issue 25):**
- `SeatMapPage` — exibe mapa + painel de total, sem botão de reserva
- `TotalPanel` — mostra total e contagem, sem ação
- `useSeatSelection` — gerencia seleção, sem lógica de booking
- `seatsService` — apenas `fetchSeats`, sem chamada de reserva

**Gap identificado para a issue 26:**
- Faltava a camada de serviço para `POST /bookings/bookSeat`
- Faltava um componente base de modal reutilizável
- Faltavam os dois modais do fluxo (resumo e dados do passageiro)
- Faltava o hook que orquestra o fluxo de dois passos
- `TotalPanel` precisava do botão "Realizar Reserva"
- `useSeatSelection` precisava expor `clearSelection` e `refetch`
- `SeatMapPage` precisava integrar tudo

A IA definiu 8 passos de implementação em ordem de dependência antes de alterar qualquer arquivo.

### O que foi gerado

**Novos artefatos:**

- `services/bookingService.js` — `createBooking(seatCodes, name, email)` → `POST /bookings/bookSeat` via axios
- `atoms/Modal/index.jsx` — wrapper base com overlay, botão X, fechar por Escape e clique fora
- `organisms/ConfirmBookingModal/index.jsx` — Modal 1: lista de poltronas selecionadas + total + botões "Cancelar" / "Continuar"
- `organisms/PassengerFormModal/index.jsx` — Modal 2: campos nome e e-mail + botões "Cancelar" / "Confirmar Reserva" + exibição de erro sem perder seleção
- `hooks/useBooking.js` — orquestra o fluxo `idle → summary → form`, chama a API, trata erro e chama `onSuccess` após confirmação

**Artefatos atualizados:**

- `hooks/useSeatSelection.js` — adicionados `clearSelection` e `refetch` (via `useCallback` para evitar loop no `useEffect`)
- `organisms/TotalPanel/index.jsx` — adicionado botão "Realizar Reserva" com prop `onBook`; desabilitado sem seleção ativa
- `pages/SeatMapPage/index.jsx` — integra `useBooking`, `ConfirmBookingModal`, `PassengerFormModal`, `clearSelection` e `refetch` após reserva bem-sucedida
- `pages/SeatMapPage/SeatMapPage.test.jsx` — teste de contagem de botões atualizado para filtrar por `aria-label` de poltrona; novo teste para o botão "Realizar Reserva" desabilitado

### Resultado

40/40 testes passando. Implementação funcional cobrindo todos os cenários BDD da issue:

- Seleção de poltronas com indicação visual
- Botão "Realizar Reserva" habilitado somente com seleção ativa
- Modal 1 exibe resumo das poltronas e valor total com navegação correta
- Modal 2 coleta nome e e-mail com confirmação funcional
- Reserva confirmada via `POST /bookings/bookSeat`
- Mapa atualizado automaticamente após reserva bem-sucedida
- Mensagem de erro amigável sem perder a seleção atual

---

## Lição Aprendida - um único ciclo bem estruturado pode cobrir uma funcionalidade complexa

O fluxo de reserva envolve 8 artefatos (5 novos + 3 atualizados), dois modais encadeados, gerenciamento de estado assíncrono e re-fetch automático. Ainda assim, um único ciclo Chain of Thought foi suficiente para gerar tudo sem refinamentos adicionais. O diferencial foi a instrução de dividir em passos — isso induziu a IA a planejar antes de agir, resultando em uma implementação coesa e sem inconsistências entre os componentes.
