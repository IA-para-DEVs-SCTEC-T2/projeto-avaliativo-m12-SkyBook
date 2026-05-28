---
inclusion: always
---

# SkyBook — Visão de Produto

## O que é o SkyBook

O **SkyBook** é um sistema de reservas de poltronas de aeronave. Ele permite que passageiros visualizem a disponibilidade de assentos, realizem reservas e obtenham um resumo consolidado com o valor total a pagar.

## Escopo do MVP

As três funcionalidades que compõem o MVP são:

### 1. Listagem de poltronas
Exibir todas as poltronas da aeronave com seu status atual:
- **Disponível** — poltrona pode ser reservada
- **Indisponível** — poltrona já está ocupada/reservada

### 2. Reserva de poltronas
Permitir que o usuário selecione e reserve uma ou mais poltronas disponíveis. Após a reserva, o status das poltronas selecionadas deve ser atualizado para indisponível.

### 3. Resumo das reservas
Apresentar um resumo consolidado contendo:
- As poltronas reservadas na sessão
- O valor individual de cada poltrona
- O valor total da reserva

## Fora do escopo do MVP

As funcionalidades abaixo **não fazem parte do MVP** e não devem ser implementadas nesta fase:
- Autenticação e cadastro de usuários
- Pagamento online
- Cancelamento de reservas
- Múltiplos voos ou aeronaves
- Histórico de reservas
