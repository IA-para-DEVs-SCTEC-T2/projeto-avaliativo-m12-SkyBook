# Prompt Improvement

Registro de prompts que geraram saídas da IA que precisaram ser refinadas, documentando o problema identificado e a solução aplicada.

---

## Caso 1 — Criação da skill finish-issue

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

### Problema

A skill foi criada sem incluir uma verificação inicial do estado do repositório. O fluxo iniciava diretamente pela obtenção do número da issue, sem garantir que não havia alterações não commitadas pendentes — o que poderia causar falha no passo final de push ou gerar um PR com código incompleto.

### Solução

Foi necessário um prompt de refinamento para adicionar a etapa de verificação:

```
/finish-issue Atualize essa skill para primeiro verificar se há alguma alteração não commitada com git status. Proibido prosseguir o fluxo caso ainda haja alterações não commitadas
```

A skill foi atualizada com um novo **passo 1** obrigatório que executa `git status` e interrompe o fluxo caso existam alterações pendentes, exibindo a mensagem:

> "⛔ OPERAÇÃO BLOQUEADA: Existem alterações não commitadas. Por favor, realize o commit ou descarte as alterações antes de prosseguir."

### Lição

Ao descrever fluxos de automação que envolvem git, incluir explicitamente no prompt as validações de pré-condição (estado do repositório, branch correta, etc.) para evitar a necessidade de refinamento posterior.

