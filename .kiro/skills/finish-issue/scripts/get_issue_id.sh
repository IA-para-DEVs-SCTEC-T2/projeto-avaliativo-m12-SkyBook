#!/bin/bash

BRANCH=$(git branch --show-current)

ISSUE_ID=$(echo "$BRANCH" | sed -E 's#^[a-zA-Z]+/([0-9]+)-.*$#\1#')

if [ -z "$ISSUE_ID" ] || [ "$ISSUE_ID" = "$BRANCH" ]; then
  echo "Erro: não foi possível identificar o número da Issue pela branch."
  echo "Use o padrão: feature/<task-id>-<descrição>, fix/<task-id>-<descrição> ou docs/<task-id>-<descrição>"
  exit 1
fi

echo "$ISSUE_ID"
