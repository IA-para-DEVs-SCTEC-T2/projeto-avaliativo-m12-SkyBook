
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
