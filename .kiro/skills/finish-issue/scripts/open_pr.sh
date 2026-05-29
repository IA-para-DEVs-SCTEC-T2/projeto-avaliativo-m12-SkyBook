#!/bin/bash

set -e

# Validação: alterações não commitadas
if ! git diff --quiet || ! git diff --cached --quiet; then
  echo "⛔ OPERAÇÃO BLOQUEADA: Existem alterações não commitadas. Por favor, realize o commit das alterações antes de executar o push."
  exit 1
fi

BRANCH=$(git branch --show-current)

# Validação: branch protegida
if [ "$BRANCH" = "main" ] || [ "$BRANCH" = "develop" ]; then
  echo "⛔ OPERAÇÃO BLOQUEADA: Push direto na branch '$BRANCH' é proibido. Use uma branch de feature, fix ou docs."
  exit 1
fi

ISSUE_ID=$(echo "$BRANCH" | sed -E 's#^[a-zA-Z]+/([0-9]+)-.*$#\1#')

if [ -z "$ISSUE_ID" ] || [ "$ISSUE_ID" = "$BRANCH" ]; then
 echo "Erro: não foi possível identificar o número da Issue pela branch."
 echo "Use o padrão: feature/<task-id>-<descrição>, fix/<task-id>-<descrição> ou docs/<task-id>-<descrição>"
 exit 1
fi

ISSUE_TITLE=$(gh issue view "$ISSUE_ID" --json title --jq '.title')

PR_TITLE=$(echo "$ISSUE_TITLE" \
 | sed -E 's/\[(STORY|EPIC|DOCS|TECH|BUG)\]//g' \
 | sed -E 's/^ *//g')

COMMIT_TYPE="feat"

if [[ "$BRANCH" == docs/* ]]; then
 COMMIT_TYPE="docs"
elif [[ "$BRANCH" == fix/* ]]; then
 COMMIT_TYPE="fix"
fi

BODY_FILE=$(mktemp)

cat > "$BODY_FILE" <<EOF
## O que foi feito

Implementação relacionada à Issue #$ISSUE_ID.

## Issue relacionada

Closes #$ISSUE_ID

## Validação da Issue

- [ ] Todos os itens aplicáveis do checklist técnico da Issue foram concluídos
- [ ] Os critérios BDD foram considerados, quando aplicável
- [ ] A Issue relacionada está pronta para ser fechada

## Checklist

- [ ] A User Story foi considerada, quando aplicável
- [ ] O código foi testado, quando aplicável
- [ ] A documentação foi atualizada, quando necessário
EOF

git push -u origin "$BRANCH"

gh pr create \
 --base develop \
 --head "$BRANCH" \
 --title "$COMMIT_TYPE: $PR_TITLE" \
 --body-file "$BODY_FILE"

rm "$BODY_FILE"

echo
echo "Movendo issue #$ISSUE_ID para 'Em Revisão' no projeto..."

PROJECT_ID="PVT_kwDOEJ21384BYyYK"
STATUS_FIELD_ID="PVTSSF_lADOEJ21384BYyYKzhT1os0"
EM_REVISAO_ID="df73e18b"

ITEM_ID=$(gh api graphql -f query='
  query {
    organization(login: "IA-para-DEVs-SCTEC-T2") {
      projectV2(number: 26) {
        items(first: 100) {
          nodes {
            id
            content {
              ... on Issue {
                number
              }
            }
          }
        }
      }
    }
  }
' --jq ".data.organization.projectV2.items.nodes[] | select(.content.number == $ISSUE_ID) | .id")

if [ -z "$ITEM_ID" ]; then
  echo "Aviso: issue #$ISSUE_ID não encontrada no projeto. Verifique se ela foi adicionada ao board."
else
  gh api graphql -f query='
    mutation($project: ID!, $item: ID!, $field: ID!, $value: String!) {
      updateProjectV2ItemFieldValue(input: {
        projectId: $project
        itemId: $item
        fieldId: $field
        value: { singleSelectOptionId: $value }
      }) {
        projectV2Item { id }
      }
    }
  ' -f project="$PROJECT_ID" -f item="$ITEM_ID" -f field="$STATUS_FIELD_ID" -f value="$EM_REVISAO_ID" > /dev/null

  echo "Issue movida para 'Em Revisão' com sucesso."
fi
