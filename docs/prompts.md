
---

## Ajuste script open_pr.sh para novo formato de branches

* Data: 2026-05-26 00:00
* Autor: joaopuel
* Tipo: Zero shot

### Prompt original
```
Ajuste esse script. As branches desse projeto tem o seguinte formato feature/<task-id>-<descrição-da-implementação> ou fix/<task-id>-<descrição-da-implementação> ou docs/<task-id>-<descrição-da-implementação>
```

---

## Criar skill para finalizar demanda e abrir PR

* Data: 2026-05-26 00:00
* Autor: joaopuel
* Tipo: Zero shot

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
* Tipo: Zero shot

### Prompt original
```
/finish-issue Atualize essa skill para primeiro verificar se há alguma alteração não commitada com git status. Proibido prosseguir o fluxo caso ainda haja alterações não commitadas
```

---

## Atualizar templates de issues com tópicos padronizados

* Data: 2026-05-28 00:00
* Autor: joaopuel
* Tipo: Zero shot

### Prompt original
```
Atualize os templates presentes nos assets da skill /manage-github-issue. Todos os templates devem ter os tópicos:
1. Decrição da demanda: breve descrição do que deve ser implementado/atualizado/corrigido.
2. Objetivo: Lista de objetivos que devem ser realizados no decorrer da demanda
3. Escopo: Lista de pontos/trechos/arquivos que demanda propoe modificar
4. Resultado esperado: checklist de resultados esperados ao final da demanda
```

---

## Remover suporte a EPIC issues da skill manage-github-issue

* Data: 2026-05-28 00:00
* Autor: joaopuel
* Tipo: Zero shot

### Prompt original
```
Atualize também a skill para descosiderar EPIC issues. Remover toda referência a EPIC issues e remover o asset
```

---

## Criar steering de produto para sistema de reservas de poltronas

* Data: 2026-05-28 14:00
* Autor: joaopuel
* Tipo: Zero shot

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
* Tipo: Zero shot

### Prompt original
```
Esse projeto teve seu nome e nome do repositório alterado para SkyBook. Procure por todo o projeto onde o nome do projeto/repositório estavam sendo utilizados e atualize.
```

---

## Criar steerings de estrutura e tecnologias do projeto

* Data: 2026-05-28 00:00
* Autor: joaopuel
* Tipo: Zero shot

### Prompt original
```
Instrução
Crie steerings da estrutura e tecnologias do projeto
Detalhes
1. Steering structure:
1.1. O projeto deve ser criado serguindo a arquitetura MVC
1.2. Devem ser criados DTOs para armazenar os dados de entradas e saídas de endpoints
1.3. Devem ser utilizadas entidades para a manipulação e persistência dos dados no banco H2
2. Steering tech:
2.1. Leia as informações do arquivo pom para obter as tecnologias e bibliotecas utilizadas no projeto
```

---

## Documentar modelagem de entidades com diagrama Mermaid

* Data: 2026-05-28 15:30
* Autor: joaopuel
* Tipo: Zero shot

### Prompt original
```
Vamos criar agora a modelagem da base de dados. Criando 3 entitdades:
AirplaneSeatEntity: armazena dados do assento do avião.
UserEntity: armazena dados do usuário
BookingEntity: armazena os dados da reserva
Detalhes
1. Crie um diagrama do tipo Mermaid para representar as entidades em um arquivo no diretório docs/
2. Adicione as entidades no arquivo de estrutura do projeto
Restrições
Apenas crie e atualize as documentações.
Proibido criar as entidades de fato neste momento.
```

---

## Mover issue para "Em Revisão" ao abrir PR no open_pr.sh

* Data: 2026-05-28 00:00
* Autor: joaopuel
* Tipo: Zero shot

### Prompt original
```
Altere o script para também mover a task para a coluna "Em revisão". Use como exemplo a parte de movimentar a issue no script c:\git\projeto-avaliativo-m12-BookingFlow\.kiro\skills\start-issue\scripts\start_issue.sh
```

---

## Criar entidades JPA conforme modelagem do projeto

* Data: 2026-05-28 00:00
* Autor: joaopuel
* Tipo: Few shot

### Prompt original
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

---

## Criar template README baseado nos requisitos do projeto avaliativo

* Data: 2026-05-29 00:00
* Autor: joaopuel
* Tipo: Contextual

### Prompt original
```
Crie um template do README de acordo com os requisito dispostos nesse arquivo. Adicione o template no diretório docs/templates
```

---

## Reorganizar template README em 3 partes estruturadas

* Data: 2026-05-29 00:00
* Autor: joaopuel
* Tipo: Zero shot

### Prompt original
```
Organize o template em 3 partes:
Primeira parte: deve conter a visão geral do projeto. Nome da aplicação, descrição, funcionalidades, vídeo de demosntração.
Segunda parte: deve conter as especificações do projeto e como executá-lo. Arquitetura, Descrição das Camadas, Tecnologias, Pré-requisitos, Como executar localmente, endpoits, cenários de uso, Como executar os testes, Pipeline Ci/CD, melhorias futuras.
Terceira parte: deve conter como a IA foi utilizada no fluxo de desenvolvimento. Ferramentas de IA Utilizadas, Padrões de Prompting Aplicados, Ciclos de Geração e Refinamento com IA, Refatoração com IA, Análise Crítica — Saída Incorreta da IA
```

---

## Ajustar template README com referências e estrutura de pastas

* Data: 2026-05-29 00:00
* Autor: joaopuel
* Tipo: Zero shot

### Prompt original
```
Alguns ajustes:
1. No tópivo arquitetura, adicione tambéma estruturada de pastas.
2. Adicione uma referência ao steering de produto logo após a parte de funcionalides do produto.
3. Adicione uma referência ao steering de arquitetura logo após a parte de arquitetura do projeto.
4. Adicione uma referência ao steering de tecnologias logo após a parte de tecnologias do projeto.
5. Após a parte de Descrição das Camadas, adicione um tópico sobre a modelagem do banco de dados e adicione referência ao arquivo do banco de dados data-model
```

---

## Atualizar README do projeto conforme template criado

* Data: 2026-05-29 00:00
* Autor: joaopuel
* Tipo: Zero shot

### Prompt original
```
Instrução
Agora atualize o README do projeto para ficar de acordo com o template criado.
Restrições
Não adicione funcionalidades, tecnologias, desiões de organização, exemplos de prompts, ferramentas utilizadas ou quais quer outras informações que não existem ou não estão detalhadas neste projeto neste momento.
Apenas atualize com as informações que já existem no projeto agora e, para as partes que não houver informações suficientes, manter os placeholders presentes no template.
```

---

## Ajustar skill manage-github-issue para inserir labels automaticamente

* Data: 2026-05-29 00:00
* Autor: joaopuel
* Tipo: Zero shot

### Prompt original
```
Ajuste a skill manage-git-issue para sempre inserir as labels apropriadas ao criar uma nova issue.
```

---

## Implementar listagem de poltronas da aeronave (issue 14)

* Data: 2026-05-29 00:00
* Autor: joaopuel
* Tipo: Zero shot

### Prompt original
```
Busque informações da issue 14 e implemente a funcionalidade proposta
```

---

## Adicionar Javadoc, rota /listSeats e @ApiResponses no endpoint

* Data: 2026-05-29 16:50
* Autor: joaopuel
* Tipo: Zero shot

### Prompt original
```
Ajustes:
1. Adicione javadoc em todos os métodos publicos e nas classes criadas.
2. O endpoints deve ter a roda /listSeats
3. Adicione também no endpoint a anotação que descreve o status 200 retornado pelo endpoint. Para isso, siga o seguinte exemplo:
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "descrição do status")
})
```

---

## Criar doc de ciclos de prompting para feat listagem de poltronas

* Data: 2026-05-29 17:10
* Autor: joaopuel
* Tipo: Zero shot

### Prompt original
```
Cire o arquivo docs/feat-list-seats.md e adicione os dois últimos prompts como exemplo de um prompt Zero Shot (Ciclo 1) seguido de um prompt Few Shot (Ciclo 2) de refinamento, seguindo as definições de ciclos definidas no c:\git\projeto-avaliativo-m12-SkyBook\docs\requisitos-projeto\IA PARA DESENVOLVEDORES [T1] - M1S08 - Projeto Avaliativo.md
```

---

## Criar DataLoader para inserir 60 poltronas na inicialização

* Data: 2026-05-29 17:30
* Autor: joaopuel
* Tipo: Chain of Thought

### Prompt original
```
Instrução
Crie uma classe em JAva com um método que deve ser executada assim que o projeto subir. O método deve ser resposável por inserir 60 poltronas diferentes assim que o projeto subir.
Detalhes
1. Assuma que o avião tem 6 poltronas por fileira.
2. Todas as poltronas devem estar disponíveis.
3. As duas primeiras fileiras devem ser da calsse executiva e ter o custo de 198,89.
4. As duas fileiras seguintes da a classe executiva, devem ser da classe econômicas premium e ter o custo de 149,90
5. Por último, será a classe econômica que terá o custo de 110,00 e serão todas as poltronas restantes.
6. Mostre a sua linha de pensamento enquanto executa a implementação.
```

---

## Ajustar cenários de uso e steering com definição da aeronave

* Data: 2026-05-29 17:50
* Autor: joaopuel
* Tipo: Zero shot

### Prompt original
```
Como não há funcionalidade para inserção de poltronas. No cenário 1 adicione "..." para representar mais poltronas e finalize com as últimas poltronas, como no exemplo abaixo:
```json
[
  { "id": 1, "code": "1A", "price": 198.89, "available": true },
  { "id": 2, "code": "1B", "price": 198.89, "available": false }
  ...
  { "id": 59, "code": "10E", "price": <valor>, "available": true },
  { "id": 60, "code": "10F", "price": <valor>, "available": false }
]
```
Adicione um aviso que somente um cenário deve ocorrer, pois no MVP é considerado apenas um avião com 60 assentos nesta formação.
Adicione também no steering do produto essa definião da aeronave que está sendo considerada.
Exclua o cenário 2 dessa funcionalidade.
```

---

## Testes unitários com JUnit e Mockito para Service e Controller

* Data: 2026-05-29 18:10
* Autor: joaopuel
* Tipo: Few shot

### Prompt original
```
Adicione testes unitários para AirplaneSeatService e AirplaneSeatController. Use Junit e Mockito. As classes de testes devem ser criadas da seguinte forma:
@ExtendWith(MockitoExtension.class)
class <class-name>Test {
```

---

## Implementar reserva de poltronas com testes unitários (issue 15)

* Data: 2026-05-29 18:30
* Autor: joaopuel
* Tipo: Zero shot

### Prompt original
```
Busque a issue 15 Realização de reserva de poltrona e implemente suas funcionalidades e implemente testes unitários
```

---

## Ajustar rota bookSeat, criar ErrorResponseDTO e GlobalExceptionHandler

* Data: 2026-05-29 19:00
* Autor: joaopuel
* Tipo: Few shot

### Prompt original
```
Realize os seguintes ajustes:
1. O endpoint para a realização da reserva deve ter a rota /bookings/bookSeat
2. Crie um DTO para armazenar as informações de erros. Ele deve conter o status do erro, a mensagem de erro e o timestamp de quando ocorreu.
3. Para lidar com os erros desse endpoint, deve ser criada a classe GlobalExceptionHandler que deve capturar os erros e retornar o objeto DTO que armazena as informações de erro.
Como nesse exemplo:
@ExceptionHandler(<excpetion-name>.class)
public ResponseEntity<<error-dto>> handlerBadRequest(<excpetion-name> ex) {
    return ResponseEntity
            .status(HttpStatus.<status>)
            .body(new <error-dto>(HttpStatus.<status>.name(), ex.getMessage(), LocalDateTime.now()));
}
```

---

## Substituir seatIds por seatCodes no endpoint bookSeat

* Data: 2026-05-29 19:15
* Autor: joaopuel
* Tipo: Chain of Thought

### Prompt original
```
Em vez de Ids, a lista de poltronas no objeto de entrada do enpoint bookSeat deve ser formado pelos códigos string das poltronas. Altere todos os pontos necessários.
Quebre essa alteração em pequenos passos, descrevendo seu processo de pensamento, e aplique as alterações.
```

---

## Criar doc feat-book-seat com 3 ciclos de prompting

* Data: 2026-05-29 19:30
* Autor: joaopuel
* Tipo: Contextual

### Prompt original
```
Crie o arquivo docs/feat-book-seat.md e adicione os 3 últimos prompts presentes em c:\git\projeto-avaliativo-m12-SkyBook\docs\prompts.mdseguindo a ordem dos ciclos 1 ,2 e 3, descritas no aquivo c:\git\projeto-avaliativo-m12-SkyBook\docs\requisitos-projeto\IA PARA DESENVOLVEDORES [T1] - M1S08 - Projeto Avaliativo.md
```

---

## Refatorar BookingService SOLID SRP com testes e Chain of Thought

* Data: 2026-05-29 20:00
* Autor: joaopuel
* Tipo: Chain of Thought

### Prompt original
```
Refatore a classe BookingService, usando princípios do SOLID e clean code.
1. Por exemplo, seguindo o Princípio da Responsabilidade Única, separe a funcionalidade do método createBookings em métodos menores.
2. Além disso, faça ajustes para que a busca e a atualização de poltronas seja realizada no AirplaneSeatService. A busca e criação de usuáros seja feita no UserService. Enquanto o BookingService apenas lida com o que diz respeito as reservas.
3. Os repositórios airplaneSeatRepository, bookingRepository e userRepository somente devem ser acessados pelos serviços correspondentes.
Faça ajustes nos testes unitários para que estejam de acordo com estas alterações.
Divida as alterações em pequenos passos, demostrando a sua linha de pensamento, e, depois, aplique as alterações.
```

---

## Documentar refatoração SOLID BookingService em feat-book-seat.md

* Data: 2026-05-29 20:15
* Autor: joaopuel
* Tipo: Contextual

### Prompt original
```
c:\git\projeto-avaliativo-m12-SkyBook\docs\feat-book-seat.mdAdicione no final do arquivo também o exemplo de refatoração aplicada com o último prompt, seguindo a requisição sobre isso do arquivo c:\git\projeto-avaliativo-m12-SkyBook\docs\requisitos-projeto\IA PARA DESENVOLVEDORES [T1] - M1S08 - Projeto Avaliativo.md
```

---

## Adicionar lições aprendidas dos ciclos em feat-book-seat.md

* Data: 2026-05-29 20:30
* Autor: joaopuel
* Tipo: Zero shot

### Prompt original
```
Adicione também a lição aprendida com com o refatoramento e as outros refinamentos no arquivo c:\git\projeto-avaliativo-m12-SkyBook\docs\feat-book-seat.md
```

---

## Implementar resumo consolidado da compra (issue 16)

* Data: 2026-05-29 21:00
* Autor: joaopuel
* Tipo: Zero shot

### Prompt original
```
Busque a issue 16 Resumo consolidado da compra e implemente a funcionalidade e os testes
```
