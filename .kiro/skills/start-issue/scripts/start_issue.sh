#!/bin/bash

set -e

read -p "Digite o número da Issue: " ISSUE_ID

TITLE=$(gh issue view "$ISSUE_ID" --json title --jq '.title')

SLUG=$(echo "$TITLE" \
 | sed -E 's/\[(STORY|EPIC|DOCS|TECH|BUG)\]//g' \
 | iconv -f utf-8 -t ascii//TRANSLIT \
 | tr '[:upper:]' '[:lower:]' \
 | sed -E 's/[^a-z0-9]+/-/g' \
 | sed -E 's/^-|-$//g')

TYPE="feature"

if [[ "$TITLE" == *"[DOCS]"* ]]; then
 TYPE="docs"
elif [[ "$TITLE" == *"[BUG]"* ]]; then
 TYPE="fix"
fi

BRANCH="${TYPE}/${ISSUE_ID}-${SLUG}"

echo
echo "Issue selecionada:"
echo "#$ISSUE_ID - $TITLE"
echo
echo "Branch:"
echo "$BRANCH"

git checkout develop
git pull
git checkout -b "$BRANCH"

echo
echo "Movendo issue #$ISSUE_ID para 'Em Andamento' no projeto..."

PROJECT_ID="PVT_kwDOEJ21384BYyYK"
STATUS_FIELD_ID="PVTSSF_lADOEJ21384BYyYKzhT1os0"
EM_ANDAMENTO_ID="47fc9ee4"

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
  ' -f project="$PROJECT_ID" -f item="$ITEM_ID" -f field="$STATUS_FIELD_ID" -f value="$EM_ANDAMENTO_ID" > /dev/null

  echo "Issue movida para 'Em Andamento' com sucesso."
fi
