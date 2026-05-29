# Tecnologias e Bibliotecas

## Linguagem e Plataforma

- **Java 17**
- **Maven** como gerenciador de build e dependências

## Framework Principal

- **Spring Boot 4.0.6** (via `spring-boot-starter-parent`)

## Dependências de Produção

| Biblioteca | Finalidade |
|---|---|
| `spring-boot-starter-webmvc` | Camada web MVC, controllers REST |
| `spring-boot-starter-data-jpa` | Persistência de dados com JPA/Hibernate |
| `spring-boot-h2console` | Console web do banco H2 |
| `com.h2database:h2` | Banco de dados H2 em memória (runtime) |
| `springdoc-openapi-starter-webmvc-ui 3.0.2` | Documentação automática da API via Swagger UI |
| `org.projectlombok:lombok` | Redução de boilerplate (getters, setters, construtores) |

## Dependências de Teste

| Biblioteca | Finalidade |
|---|---|
| `spring-boot-starter-data-jpa-test` | Suporte a testes de repositório JPA |
| `spring-boot-starter-webmvc-test` | Suporte a testes de controllers MVC |

## Banco de Dados

- **H2** em memória, configurado via `application.properties`
- Console H2 disponível em `/h2-console` durante o desenvolvimento
- Schema gerenciado automaticamente pelo Hibernate (JPA)

## Documentação da API

- **Swagger UI** disponível via SpringDoc OpenAPI em `/swagger-ui.html`
- Anotações `@Operation`, `@Tag` e similares do pacote `io.swagger.v3.oas.annotations` podem ser usadas nos controllers

## Convenções de Build

- Plugin `spring-boot-maven-plugin` configurado para excluir Lombok do artefato final
- Plugin `maven-compiler-plugin` configurado com Lombok como annotation processor nas fases de compile e test-compile
