---
name: finish-issue
description: Finaliza a demanda em desenvolvimento, atualiza a documentação e abre o PR no GitHub. Use quando o usuário pedir para finalizar, concluir, encerrar a demanda, realizar o push ou abrir/criar o PR.
---

## Finalizar Demanda e Abrir PR

Esta skill automatiza a conclusão do trabalho em uma issue do GitHub, garantindo que a documentação esteja atualizada antes de abrir o PR.

### Quando usar

Ative esta skill sempre que o usuário solicitar a finalização de uma demanda ou abertura de PR, com frases como:
- "finaliza a demanda"
- "conclui a task"
- "abre o PR"
- "cria o PR"
- "faz o push"
- "encerra a issue"

### Instruções obrigatórias

Siga **rigorosamente** a ordem abaixo. Não pule etapas.

---

#### 1. Verificar alterações não commitadas

Execute o comando abaixo para verificar o estado do repositório:

```bash
git status
```

**Se houver arquivos modificados, adicionados ou deletados não commitados, interrompa imediatamente o fluxo** e informe ao usuário:

> "⛔ OPERAÇÃO BLOQUEADA: Existem alterações não commitadas. Por favor, realize o commit ou descarte as alterações antes de prosseguir."

Não avance para a próxima etapa enquanto houver alterações pendentes.

---

#### 2. Obter o número da issue

Execute o script para identificar a issue pela branch atual:

```bash
bash .kiro/skills/finish-issue/scripts/get_issue_id.sh
```

Armazene o número retornado. Se o script falhar, informe o erro ao usuário e interrompa.

---

#### 3. Buscar informações da issue no GitHub

Com o número da issue obtido, busque os detalhes da demanda:

```bash
gh issue view <ISSUE_ID> --json title,body,labels
```

Leia o título, a descrição e os critérios de aceite para entender o escopo da implementação.

---

#### 4. Analisar as implementações recentes

Execute o git diff para entender o que foi implementado:

```bash
git diff develop...HEAD
```

Analise as mudanças em relação à branch `develop` para compreender o que foi alterado.

---

#### 5. Revisar a documentação existente

Revise os seguintes artefatos para verificar se estão alinhados com a implementação atual:
- Todos os arquivos em `.kiro/steering/`
- O arquivo `README.md` na raiz do projeto
- Todos os arquivos em `docs/` (exceto `prompts.md`)

**Restrições obrigatórias:**
- **Proibido criar novos arquivos de documentação**
- **Proibido modificar skills, hooks ou specs**
- **Proibido adicionar funcionalidades, padrões ou convenções não implementadas**
- Apenas atualize documentações já existentes que estejam desatualizadas em relação à implementação

---

#### 6. Aguardar confirmação do usuário

Apresente ao usuário um resumo das alterações de documentação identificadas (ou informe que nenhuma atualização é necessária).

**É obrigatório aguardar a confirmação explícita do usuário antes de prosseguir.**

Não avance para a próxima etapa sem aprovação.

---

#### 7. Realizar o commit da documentação (se houver alterações)

Se houver alterações de documentação aprovadas pelo usuário, realize o commit:

```bash
git add <arquivos-de-documentação-alterados>
git commit -m "docs: Atualização da documentação"
```

Se não houver alterações de documentação, pule esta etapa.

---

#### 8. Executar o push e abrir o PR

Execute o script para realizar o push da branch e abrir o PR:

```bash
bash .kiro/skills/finish-issue/scripts/open_pr.sh
```

O script irá:
- Validar que não há alterações não commitadas
- Validar que a branch não é `main` ou `develop`
- Fazer o push da branch para o repositório remoto
- Criar o PR com base na issue, apontando para `develop`
