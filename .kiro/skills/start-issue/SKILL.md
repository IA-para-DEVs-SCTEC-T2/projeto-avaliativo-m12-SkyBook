---
name: start-issue
description: Inicia o desenvolvimento de uma issue do GitHub criando a branch correta e movendo o card para "Em Andamento" no projeto. Use quando o usuário pedir para iniciar, começar, abrir ou dar início a uma nova issue, task, tarefa, atividade ou demanda de desenvolvimento.
---

## Iniciar Desenvolvimento de Issue

Esta skill automatiza o início do trabalho em uma issue do GitHub.

### Quando usar

Ative esta skill sempre que o usuário solicitar o início de uma nova issue, task, tarefa, atividade ou demanda, com frases como:
- "inicia a issue X"
- "começa a task X"
- "quero começar a trabalhar na issue X"
- "inicia o desenvolvimento da demanda X"
- "abre a issue X"

### Instruções obrigatórias

1. **Pergunte o número da issue** caso o usuário não tenha informado. Não execute o script sem esse número.

2. **Execute o script** `scripts/start_issue.sh` passando o número da issue como entrada interativa:

```bash
bash .kiro/skills/start-issue/scripts/start_issue.sh
```

> O script irá solicitar o número da issue interativamente. Informe o número fornecido pelo usuário quando solicitado.

### O que o script faz

- Lista as issues abertas do repositório
- Recebe o número da issue a ser iniciada
- Gera o nome da branch seguindo a convenção `tipo/numero-slug`
  - `feature/` para stories, epics e techs
  - `fix/` para bugs
  - `docs/` para documentação
- Faz checkout da branch `develop`, atualiza e cria a nova branch
- Move o card da issue para a coluna **"Em Andamento"** no projeto GitHub Projects
