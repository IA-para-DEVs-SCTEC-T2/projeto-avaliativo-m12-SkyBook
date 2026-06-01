# Issues do Projeto SkyBook

Lista de todas as issues do repositório [IA-para-DEVs-SCTEC-T2/projeto-avaliativo-m12-SkyBook](https://github.com/IA-para-DEVs-SCTEC-T2/projeto-avaliativo-m12-SkyBook), abertas e fechadas.

---

## #32 — [DOCS] Revisão da documentação e gravação do vídeo de apresentação

**Status:** Aberta

**Descrição:**
Revisar toda a documentação do projeto e gravar o vídeo de apresentação final do SkyBook.

Conteúdo mínimo:
- Revisar e atualizar o README com instruções de instalação, execução e uso do projeto
- Gravar o vídeo de apresentação do projeto SkyBook

**Link:** https://github.com/IA-para-DEVs-SCTEC-T2/projeto-avaliativo-m12-SkyBook/issues/32

---

## #27 — [STORY] [Frontend] Tela de resumo consolidado da compra

**Status:** Fechada

**Descrição:**
Como passageiro, quero consultar um resumo consolidado das minhas reservas diretamente na tela de seleção de poltronas, para conferir as poltronas reservadas e o valor total a pagar sem sair da página.

Critérios de aceitação:
- Botão "Consultar Reservas" visível abaixo do botão "Realizar Reserva"
- Clique no botão abre modal com campo de e-mail e botão "Buscar"
- Busca chama `GET /bookings/summary` e atualiza o modal dinamicamente
- Modal exibe poltronas reservadas com código e preço individual
- Valor total da reserva é exibido corretamente
- Mensagem adequada quando e-mail não é encontrado
- Estados de loading e erro tratados adequadamente

**Link:** https://github.com/IA-para-DEVs-SCTEC-T2/projeto-avaliativo-m12-SkyBook/issues/27

---

## #26 — [STORY] [Frontend] Tela de reserva de poltronas

**Status:** Fechada

**Descrição:**
Como passageiro, quero selecionar e reservar uma ou mais poltronas disponíveis através de uma interface web, para garantir meu assento no voo de forma simples e intuitiva.

Fluxo de interação:
1. Passageiro seleciona uma ou mais poltronas disponíveis no mapa
2. Clica em "Realizar Reserva" (habilitado somente com ao menos uma poltrona selecionada)
3. Modal 1 — Resumo: lista de poltronas + valor total → botões "Continuar" / "Cancelar" / "X"
4. Modal 2 — Dados do passageiro: campos nome e e-mail → botões "Confirmar Reserva" / "Cancelar" / "X"
5. Após confirmação: `POST /bookings` → mapa atualizado automaticamente

**Link:** https://github.com/IA-para-DEVs-SCTEC-T2/projeto-avaliativo-m12-SkyBook/issues/26

---

## #25 — [STORY] [Frontend] Tela de listagem de poltronas da aeronave

**Status:** Fechada

**Descrição:**
Como passageiro, quero visualizar todas as poltronas da aeronave em um grid interativo, para identificar visualmente quais estão disponíveis e selecionar as que desejo reservar, acompanhando o valor total em tempo real.

Resultado esperado:
- Grid com 60 poltronas em 10 fileiras x 6 colunas (A-F)
- Cada poltrona é um quadrado com o valor unitário exibido no centro
- Poltronas disponíveis em verde; indisponíveis em vermelho
- Seleção de poltrona atualiza o total na parte esquerda da tela
- Total inicia em R$ 0,00 e acumula/decumula conforme seleção
- Dados consumidos do endpoint `GET /seats`

**Link:** https://github.com/IA-para-DEVs-SCTEC-T2/projeto-avaliativo-m12-SkyBook/issues/25

---

## #24 — [TECH] [Frontend] Configuração inicial da estrutura do projeto frontend

**Status:** Fechada

**Descrição:**
Configurar a estrutura inicial do projeto frontend do SkyBook, incluindo escolha de tecnologia, setup do ambiente de desenvolvimento, organização de pastas e integração com a API REST do backend.

Checklist técnico:
- Definir e configurar o framework/biblioteca frontend (React)
- Inicializar o projeto com estrutura de pastas organizada
- Configurar cliente HTTP para consumo da API REST (axios)
- Configurar variáveis de ambiente para URL base da API
- Configurar scripts de build e desenvolvimento no `package.json`
- Adicionar README com instruções de setup e execução do frontend
- Garantir que o projeto compila e executa sem erros

**Link:** https://github.com/IA-para-DEVs-SCTEC-T2/projeto-avaliativo-m12-SkyBook/issues/24

---

## #23 — [EPIC] MVP SkyBook — Frontend de Reservas de Poltronas

**Status:** Fechada

**Descrição:**
Entregar o frontend do MVP SkyBook com as três funcionalidades core: visualização das poltronas disponíveis, realização de reservas e exibição do resumo da compra, consumindo a API REST já implementada no backend.

Escopo do Epic:
- Tela de listagem de poltronas com status de disponibilidade
- Tela de reserva de uma ou mais poltronas disponíveis
- Tela de resumo consolidado das poltronas reservadas com valor individual e total

**Link:** https://github.com/IA-para-DEVs-SCTEC-T2/projeto-avaliativo-m12-SkyBook/issues/23

---

## #21 — [TECH] Configurar pipeline de CI/CD com GitHub Actions

**Status:** Fechada

**Descrição:**
Configurar um pipeline de CI/CD utilizando GitHub Actions para o projeto SkyBook, conforme requisito obrigatório do projeto avaliativo. O pipeline deve ser criado com suporte de IA, executar lint e testes automaticamente a cada push, e estar documentado com os prompts utilizados.

Resultado esperado:
- Pipeline de CI/CD configurado e funcional no GitHub Actions
- Pipeline executa ao menos build e testes automaticamente a cada push
- Pipeline criado com suporte de IA e prompts salvos em `docs/prompts/`
- Execução demonstrável no GitHub Actions

**Link:** https://github.com/IA-para-DEVs-SCTEC-T2/projeto-avaliativo-m12-SkyBook/issues/21

---

## #16 — [STORY] [Backend] Resumo consolidado da compra

**Status:** Fechada

**Descrição:**
Como passageiro, quero visualizar um resumo consolidado das minhas reservas, para conferir as poltronas selecionadas e o valor total a pagar.

Resultado esperado:
- Endpoint `GET /bookings/summary` retorna poltronas reservadas com preço individual
- Valor total da reserva é calculado e retornado corretamente
- Resposta utiliza DTO (não expõe entidades JPA diretamente)
- Retorna 404 quando o e-mail informado não for encontrado

**Link:** https://github.com/IA-para-DEVs-SCTEC-T2/projeto-avaliativo-m12-SkyBook/issues/16

---

## #15 — [STORY] [Backend] Realização de reserva de poltrona

**Status:** Fechada

**Descrição:**
Como passageiro, quero selecionar e reservar uma ou mais poltronas disponíveis, para garantir meu assento no voo.

Resultado esperado:
- Endpoint `POST /bookings` cria reserva com sucesso para poltrona disponível
- Status da poltrona é atualizado para indisponível após reserva
- Sistema retorna erro ao tentar reservar poltrona indisponível
- Reserva é vinculada ao usuário e à poltrona corretamente

**Link:** https://github.com/IA-para-DEVs-SCTEC-T2/projeto-avaliativo-m12-SkyBook/issues/15

---

## #14 — [STORY] [Backend] Listagem de poltronas da aeronave

**Status:** Fechada

**Descrição:**
Como passageiro, quero visualizar todas as poltronas da aeronave com seu status atual, para saber quais estão disponíveis para reserva.

Resultado esperado:
- Endpoint `GET /seats` retorna lista de poltronas com status
- Poltronas disponíveis e indisponíveis são diferenciadas na resposta
- Resposta utiliza DTO (não expõe entidade JPA diretamente)

**Link:** https://github.com/IA-para-DEVs-SCTEC-T2/projeto-avaliativo-m12-SkyBook/issues/14

---

## #13 — [EPIC] MVP SkyBook — Reservas de Poltronas

**Status:** Fechada

**Descrição:**
Entregar o MVP do SkyBook com as três funcionalidades core de backend: listagem de poltronas disponíveis, realização de reservas e exibição do resumo da compra.

Escopo do Epic:
- Listagem de poltronas com status de disponibilidade (disponível/indisponível)
- Reserva de uma ou mais poltronas disponíveis, atualizando o status para indisponível
- Resumo consolidado das poltronas reservadas com valor individual e total

**Link:** https://github.com/IA-para-DEVs-SCTEC-T2/projeto-avaliativo-m12-SkyBook/issues/13

---

## #9 — [STORY] Avaliar saída da IA e aplicar refinamento (ciclo 2)

**Status:** Fechada

**Descrição:**
Avaliar a saída gerada no ciclo 1 e aplicar refinamentos com suporte de IA. Este ciclo consiste em identificar problemas, lacunas ou melhorias no código gerado anteriormente e usar a IA com prompts refinados para corrigir e aprimorar o resultado.

Conteúdo mínimo:
- Revisar criticamente o código gerado no ciclo 1
- Identificar e documentar os problemas ou pontos de melhoria encontrados
- Elaborar prompt de refinamento com base nas observações do ciclo 1
- Aplicar o refinamento com suporte de IA e registrar o prompt em `docs/prompts/`
- Documentar o comparativo antes/depois do refinamento

**Link:** https://github.com/IA-para-DEVs-SCTEC-T2/projeto-avaliativo-m12-SkyBook/issues/9

---

## #8 — [STORY] Gerar código das funcionalidades principais com IA (ciclo 1)

**Status:** Fechada

**Descrição:**
Gerar o código das funcionalidades principais da aplicação BookingFlow com suporte de IA. Este é o primeiro ciclo de geração, onde o código inicial será produzido a partir de prompts estruturados, com foco em cobrir os requisitos funcionais definidos na arquitetura do projeto.

Conteúdo mínimo:
- Definir o prompt inicial para geração do código das funcionalidades principais
- Gerar o código com suporte de IA (mínimo 1 funcionalidade completa)
- Registrar o prompt utilizado em `docs/prompts/`
- Avaliar criticamente a saída gerada e documentar observações para o ciclo 2

**Link:** https://github.com/IA-para-DEVs-SCTEC-T2/projeto-avaliativo-m12-SkyBook/issues/8

---

## #7 — [DOCS] Criar estrutura inicial do projeto e README.md

**Status:** Fechada

**Descrição:**
Configurar a estrutura inicial do projeto BookingFlow e elaborar o README.md com as informações essenciais para orientar desenvolvedores e colaboradores sobre o propósito, configuração e uso da aplicação.

Resultado esperado:
- Estrutura de pacotes inicial do projeto definida e organizada
- `README.md` criado com todas as seções obrigatórias
- README alinhado ao domínio e escopo definidos no steering de produto

**Link:** https://github.com/IA-para-DEVs-SCTEC-T2/projeto-avaliativo-m12-SkyBook/issues/7

---

## #6 — [DOCS] Planejar arquitetura com suporte de IA e documentar decisões

**Status:** Fechada

**Descrição:**
Planejar a arquitetura da aplicação BookingFlow com suporte de IA e documentar as decisões técnicas tomadas. Os resultados serão formalizados como steerings de estrutura e tecnologia, servindo como contexto persistente para o Kiro orientar implementações futuras.

Resultado esperado:
- Estrutura de pacotes e camadas da aplicação definida
- Tecnologias e frameworks adotados documentados
- Decisões arquiteturais relevantes registradas
- Arquivo `.kiro/steering/structure.md` criado
- Arquivo `.kiro/steering/tech.md` criado

**Link:** https://github.com/IA-para-DEVs-SCTEC-T2/projeto-avaliativo-m12-SkyBook/issues/6

---

## #5 — [DOCS] Definir domínio e escopo da aplicação

**Status:** Fechada

**Descrição:**
Definir o domínio e escopo da aplicação BookingFlow para criar o steering de produto. O steering de produto servirá como contexto persistente para o Kiro, orientando decisões de implementação, arquitetura e priorização alinhadas ao propósito do sistema.

Resultado esperado:
- Domínio da aplicação descrito de forma clara e objetiva
- Escopo do MVP definido com funcionalidades incluídas e excluídas
- Arquivo `.kiro/steering/product.md` criado

**Link:** https://github.com/IA-para-DEVs-SCTEC-T2/projeto-avaliativo-m12-SkyBook/issues/5

---

## #3 — [TECH] Automações de fluxo

**Status:** Fechada

**Descrição:**
Implementar automações via skills do Kiro que suportem o fluxo de desenvolvimento do projeto BookingFlow, cobrindo: movimentação automática de tasks no GitHub Projects, abertura de Pull Requests via CLI/hooks, consolidação dos prompts utilizados nas interações com IA e atualização contínua da documentação viva do projeto.

Checklist técnico:
- Configurar automação de movimentação de tasks no GitHub Projects
- Criar skill para abertura automática de PRs com título, descrição e assignee padronizados
- Definir estrutura e local de armazenamento para consolidação dos prompts utilizados com IA
- Implementar skill de atualização da documentação viva

**Link:** https://github.com/IA-para-DEVs-SCTEC-T2/projeto-avaliativo-m12-SkyBook/issues/3

---

## #1 — [TECH] Configuração inicial do projeto e adição de skills do Kiro

**Status:** Fechada

**Descrição:**
Configuração inicial do workspace do projeto BookingFlow no Kiro, incluindo a criação das skills base que serão utilizadas ao longo do desenvolvimento para padronizar e automatizar tarefas recorrentes com o agente.

Checklist técnico:
- Configurar o projeto no repositório (estrutura inicial, .kiro, pom.xml)
- Criar skill para criação de steering files (`create-steering`)
- Criar skill para criação de hooks (`create-hook`)
- Criar skill para criação de skills (`create-skill`)
- Criar skill para gerenciamento de issues no GitHub (`manage-github-issue`)

**Link:** https://github.com/IA-para-DEVs-SCTEC-T2/projeto-avaliativo-m12-SkyBook/issues/1
