# Exemplos de Padrões de Prompt

Exemplos reais utilizados neste projeto, classificados por padrão de engenharia de prompt.

---

## Zero Shot

Prompt direto, sem exemplos. O modelo recebe apenas a instrução e o contexto necessário.

### Criar steering de produto para sistema de reservas de poltronas

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

**Por que Zero Shot?**
Nenhum exemplo de output foi fornecido. O modelo recebeu apenas a instrução e os dados de contexto, sendo esperado que inferisse o formato e conteúdo do steering por conta própria.

---

## Few Shot

Prompt que fornece um ou mais exemplos concretos do formato/resultado esperado antes de solicitar a tarefa.

### Criar entidades JPA conforme modelagem do projeto

```
Crie as entidades de acordo com a modelagem do projeto.
As entidades devem ser criadas seguindo este exemplo:
<code>
@Entity
@Data
@Table(name = "table_name")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class <entity-name>Entity {...}
<code>
Restrições
Apenas crie as entidades neste momento. Não crie outras funcionalidades não solicitadas.
```

**Por que Few Shot?**
O prompt fornece um exemplo concreto da estrutura esperada para as entidades (`@Entity @Data @Table...`). Esse exemplo guia o modelo sobre o padrão de código a seguir — característica central do padrão Few Shot.
