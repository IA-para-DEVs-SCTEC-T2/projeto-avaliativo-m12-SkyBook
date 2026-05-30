# Funcionalidade: Tela de Listagem de Poltronas (`/skybook`)

Este documento registra os ciclos de geração e refinamento com IA utilizados no desenvolvimento da tela de listagem de poltronas da aeronave no frontend React.

---

## Ciclo 1 — Geração Inicial (Zero shot)

> Prompt enviado sem exemplos ou estrutura de raciocínio explícita. A IA recebeu a descrição da demanda e gerou a implementação completa a partir do zero.

**Padrão de prompting:** Zero shot  
**Data:** 2026-05-30 17:45  
**Autor:** joaopuel

### Prompt

```
Busque a issue 25 Tela de listagem de poltronas da aeronave e implemente as funcionalidades
```

### O que foi gerado

A partir deste prompt, a IA buscou os critérios de aceitação da issue #25 no GitHub e criou os seguintes artefatos seguindo a arquitetura Atomic Design:

- `services/seatsService.js` — chamada ao endpoint `GET /seats/listSeats` via axios
- `hooks/useSeatSelection.js` — hook com carregamento de poltronas, seleção/deseleção e cálculo do total acumulado
- `atoms/StatusBadge/index.jsx` — badge visual de status (disponível / indisponível / selecionado)
- `atoms/MoneyValue/index.jsx` — átomo de valor monetário formatado em BRL via `Intl.NumberFormat`
- `molecules/SeatCard/index.jsx` — quadrado clicável com código e preço, estados verde/vermelho/azul
- `organisms/SeatMap/index.jsx` — grid 10×6 com corredor central entre colunas C e D e legenda de cores
- `organisms/TotalPanel/index.jsx` — painel lateral com total em destaque e contagem de poltronas selecionadas
- `templates/SeatMapLayout/index.jsx` — layout com painel esquerdo + área principal direita
- `pages/SeatMapPage/index.jsx` — página completa com estados de loading e erro
- `App.jsx` atualizado para renderizar `SeatMapPage`

### Resultado

Build e testes passando. Implementação funcional cobrindo todos os cenários BDD da issue: exibição do grid, diferenciação visual por status, seleção/deseleção com atualização do total, bloqueio de poltronas indisponíveis, e tratamento de loading e erro.

---

## Ciclo 2 — Refinamento (Instrução direta)

> Prompt direto reportando um erro de runtime encontrado ao acessar a tela. A IA identificou a causa raiz e aplicou a correção no backend.

**Padrão de prompting:** Instrução direta  
**Data:** 2026-05-30 18:00  
**Autor:** joaopuel

### Prompt

```
O frontend está recebendo erro de CORS ao tentar acessar a API:
"Access to XMLHttpRequest at 'http://localhost:8080/skybook/seats/listSeats' from origin
'http://localhost:5173' has been blocked by CORS policy: No 'Access-Control-Allow-Origin'
header is present on the requested resource."

Configure o CORS no backend Spring Boot para permitir requisições vindas do frontend
em http://localhost:5173.
```

### O que foi refinado

A IA identificou que o backend não possuía configuração de CORS e criou:

- `config/WebConfig.java` — implementa `WebMvcConfigurer` e registra regra global de CORS:
  - `allowedOrigins("http://localhost:5173")`
  - `allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")`
  - `allowedHeaders("*")`

### Resultado

Com o backend reiniciado, o frontend passou a receber o header `Access-Control-Allow-Origin` nas respostas, eliminando o bloqueio do browser e permitindo o carregamento das poltronas.

---

## Ciclo 3 — Refinamento (Chain of Thought)

> Prompt com múltiplos ajustes solicitando que a IA descrevesse seu processo de pensamento antes de aplicar as alterações. A IA mapeou a causa raiz de cada problema e aplicou as correções de forma estruturada.

**Padrão de prompting:** Chain of Thought  
**Data:** 2026-05-30 18:10  
**Autor:** joaopuel

### Prompt

```
Ajuste o teal de listagem de poltronas:
1. A tela de listagem deve ter a rota /skybook
2. Ao acessar a aplicação pela rota http://localhost:5173/, o usuário deve ser redirecionado automaticamente para rota  /skybook já que consiste da homepage
3. O grid está desalinhado, a coluna D está sendo preenchida com componentes span do tamanho 64x85 em vez do tamanho 64x64 como as outras. Ajuse para que todas tenham o mesmo tamanho e tudo fique alinhado.
4. O componente span da letra D também apresenta um tamanho desproporcional. Com tamanho 88x21 em vez de 64x21 como os das demais letras. Ajute para que fique tudo alinhado.
Dividas as alterações em pequenos passos, mostrando sua linha de pensamento, e aplique as alterações.
```

### Raciocínio explicitado pela IA

A IA mapeou cada problema antes de alterar qualquer arquivo:

1. **Roteamento** — `react-router-dom` não estava instalado. Necessário instalar a versão 6.x (compatível com Node 18 do ambiente) e configurar `BrowserRouter` + `Routes` no `App.jsx`
2. **Redirect** — rota `/` deve usar `<Navigate to="/skybook" replace />` para redirecionar sem adicionar entrada no histórico
3. **Causa raiz do desalinhamento das células** — o corredor entre C e D era um `<span>` *dentro* do wrapper da célula D, somando sua largura (24px) à célula, resultando em 88×85px. Solução: corredor como elemento irmão no flex row, não filho do wrapper
4. **Causa raiz do desalinhamento do cabeçalho** — mesmo problema: o `<span>` da letra D englobava o corredor interno, ficando 88px de largura. Mesma correção aplicada ao cabeçalho

### O que foi alterado

- `package.json` — adicionado `react-router-dom@6.30.0`
- `App.jsx` — reescrito com `BrowserRouter`, `Routes`, `Route` e `Navigate`
- `organisms/SeatMap/index.jsx` — corredor extraído como `<div>` irmão no flex row, tanto no cabeçalho quanto nas fileiras; cada célula agora tem `width: 64px; height: 64px; flexShrink: 0` garantido por `<div>` dedicado

### Resultado

Build e testes passando. Grid alinhado com todas as células em 64×64px. Acesso a `http://localhost:5173/` redireciona automaticamente para `/skybook`.

---

## Lições Aprendidas

---

### Ciclo 1 — Zero shot: a IA estrutura bem, mas não testa no browser

O prompt inicial foi direto e sem exemplos. A IA interpretou os critérios BDD da issue, aplicou a arquitetura Atomic Design corretamente e gerou uma implementação funcional completa. O build e os testes unitários passaram. No entanto, dois problemas só foram descobertos ao rodar a aplicação no browser:

- Erro de CORS bloqueando as requisições à API
- Desalinhamento visual no grid causado por estrutura de elementos incorreta

**Lição:** Zero shot é eficiente para gerar a estrutura e a lógica de negócio, mas problemas de integração (CORS, layout) só aparecem em runtime. A revisão manual no browser é indispensável após a geração inicial — testes unitários não cobrem esses cenários.

---

### Ciclo 2 — Instrução direta: reportar o erro exato acelera a correção

Ao fornecer a mensagem de erro exata do browser no prompt, a IA identificou imediatamente a causa raiz (ausência de configuração de CORS no backend) e aplicou a correção sem ambiguidade. Não foi necessário descrever o problema em detalhes — a mensagem de erro foi suficiente.

**Lição:** Para erros de runtime, copiar a mensagem de erro exata no prompt é mais eficaz do que descrever o problema com palavras próprias. A IA reconhece padrões de erro conhecidos e aplica a solução diretamente.

---

### Ciclo 3 — Chain of Thought: raciocínio explícito resolve múltiplos problemas de uma vez

O prompt agrupou 4 ajustes distintos (rota, redirect, alinhamento de células, alinhamento de cabeçalho) e solicitou que a IA descrevesse seu processo de pensamento antes de agir. A IA identificou a causa raiz compartilhada dos dois problemas de alinhamento (corredor como filho em vez de irmão no flex row) e resolveu ambos com uma única abordagem estrutural, além de identificar a incompatibilidade de versão do `react-router-dom` com o Node 18 do ambiente.

**Lição:** Chain of Thought é especialmente útil quando múltiplos problemas têm causas raiz relacionadas. Forçar a IA a raciocinar antes de agir evita correções superficiais e revela dependências entre os problemas. O custo é um prompt mais longo, mas o ganho em precisão e completude compensa.

---

### Lição geral: geração + integração são etapas distintas

O desenvolvimento desta funcionalidade deixou evidente que a geração de código com IA e a validação de integração são etapas separadas. A IA gera código correto em isolamento, mas problemas de ambiente (CORS, versão de dependência) e de layout (estrutura de elementos no DOM) só aparecem ao rodar a aplicação. O fluxo mais eficiente é: gerar → rodar → reportar erros exatos → refinar.
