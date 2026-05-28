
---

## Ajuste script open_pr.sh para novo formato de branches

* Data: 2026-05-26 00:00
* Autor: joaopuel
* Tipo: Instrução direta

### Prompt original
```
Ajuste esse script. As branches desse projeto tem o seguinte formato feature/<task-id>-<descrição-da-implementação> ou fix/<task-id>-<descrição-da-implementação> ou docs/<task-id>-<descrição-da-implementação>
```

---

## Criar skill para finalizar demanda e abrir PR

* Data: 2026-05-26 00:00
* Autor: joaopuel
* Tipo: Instrução direta

### Prompt original
```
#Instrução
Crie um skill que deve ser responsável por finalizar a demanda e abrir o PR.

#Detalhes
1. Essa nova skill deve ser acionada sempre que for solicitado para finalizar/concluir a demanda ou realizar o push ou apertura/criação do PR.
2. Primeiro deve ser executado o script c:\git\projeto-avaliativo-m12-BookingFlow\get_issue_id.sh para obter o número da issue.
3. Com o número da issue, deve ser buscada as informações da demanda no github.
4. Em seguida, deve-se ser executado o comando de git diff e analisar as implementações recentes da demanda.
5. Depois, devem ser revisados os steerings, o README e demais documentações presentes no diretório /docs
6. Então. deve ser realizada a atualização da documentação referente caso ela não esteja mais de acordo com a nova configuração do projeto
7. É obrigatório aguardar a confirmação das modificações pelo usuário antes de prosseguir
8. Realizar o commit das alterações como "docs: Atualização da documentação"
9. Por fim, deve-se executar o script c:\git\projeto-avaliativo-m12-BookingFlow\open_pr.sh para realizar o push e a abertura do PR. Mova o script para o diretório da skill.

#Restrições
1. Proibido modificar skills, hooks ou specs do projeto.
2. Proibido adicionar ou inventar funcionalidades, padrões ou convenções não implementadas.
3. Proibido criar documentações. Apenas deve ser atualizadas documentações já existentes.
```

---

## Adicionar verificação de alterações não commitadas na skill finish-issue

* Data: 2026-05-26 00:00
* Autor: joaopuel
* Tipo: Instrução direta

### Prompt original
```
/finish-issue Atualize essa skill para primeiro verificar se há alguma alteração não commitada com git status. Proibido prosseguir o fluxo caso ainda haja alterações não commitadas
```

---

## Atualizar templates de issues com tópicos padronizados

* Data: 2026-05-28 00:00
* Autor: joaopuel
* Tipo: Instrução direta

### Prompt original
```
Atualize os templates presentes nos assets da skill /manage-github-issue. Todos os templates devem ter os tópicos:1. Decrição da demanda: breve descrição do que deve ser implementado/atualizado/corrigido.2. Objetivo: Lista de objetivos que devem ser realizados no decorrer da demanda3. Escopo: Lista de pontos/trechos/arquivos que demanda propoe modificar4. Resultado esperado: checklist de resultados esperados ao final da demanda
```

---

## Remover suporte a EPIC issues da skill manage-github-issue

* Data: 2026-05-28 00:00
* Autor: joaopuel
* Tipo: Instrução direta

### Prompt original
```
Atualize também a skill para descosiderar EPIC issues. Remover toda referência a EPIC issues e remover o asset
```

---

## Criar steering de produto para sistema de reservas de poltronas

* Data: 2026-05-28 14:00
* Autor: joaopuel
* Tipo: Instrução direta

### Prompt original
```
Instrução
Crie o steering the produto deste projeto.
Detalhes
1. Esse projeto é um sistema de reservas de poltronas de uma aeronave.
2. As funcionalidades para o MVP devem ser:
2.1. Listar as poltronas da aeronave com seu status (disponível ou não).
2.2. Realizar as reservas de poltronas escolhidas.
2.3. Obter o resumo das reservas e o valor total.
```

---

## Renomear projeto de BookingFlow para SkyBook em todo o projeto

* Data: 2026-05-28 14:30
* Autor: joaopuel
* Tipo: Instrução direta

### Prompt original
```
Esse projeto teve seu nome e nome do repositório alterado para SkyBook. Procure por todo o projeto onde o nome do projeto/repositório estavam sendo utilizados e atualize.
```
