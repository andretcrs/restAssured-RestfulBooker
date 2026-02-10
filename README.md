==================================================

# Desafio Rest Assured – Automação de Testes de API

Descrição do Projeto:
Este projeto tem como objetivo demonstrar a automação de testes de API REST utilizando boas práticas de QA e arquitetura de testes, aplicado à API Restful-Booker.  
O foco está em confiabilidade, manutenibilidade, uso de massa de dados dinâmica e integração com CI/CD, simulando um cenário real de automação profissional.

==================================================

## Tecnologias Utilizadas

- Java 21 (LTS)
- Maven (Gerenciamento de dependências)
- Rest Assured (Testes de API REST)
- JUnit 5 (Framework de testes)
- Allure Reports (Relatórios detalhados)
- Datafaker (Massa de dados dinâmica)
- AssertJ (Asserções fluentes)

==================================================

<hr/>

<h2> Estrutura do Projeto</h2>
<pre>
<code>
DesafioRestAssured
├── .github/workflows
│   └── pipeline.yml          # Configuração da Pipeline CI/CD (GitHub Actions)
├── src
│   ├── main
│   │   └── java              # Classes de suporte da aplicação
│   └── test
│       ├── java
│       │   └── br.com.desafio
│       │       ├── base      # Configuração base e Health Check (BaseTest.java)
│       │       ├── client    # Camada de requisições HTTP (AuthClient, BookingClient)
│       │       ├── config    # Gerenciamento de propriedades (ConfigManager)
│       │       ├── factory   # Massa de dados dinâmica (BookingDataFactory)
│       │       ├── model     # DTOs para Request e Response
│       │       ├── tests     # Suítes de testes automatizados (CRUD completo)
│       │       └── utils     # Classes utilitárias
│       └── resources
│           ├── application.properties
│           └── allure.properties
├── pom.xml                   # Configuração de dependências e plugins Maven
└── README.md
</code>
</pre>

<hr/>


==================================================

## Cenários de Teste Automatizados

Exiba os cenários abaixo em uma tabela Markdown com as colunas:
Categoria | Classe de Teste | Objetivo do Cenário

- Infra | HealthCheckTest | Valida se a API está online (/ping) retornando status 201
- Auth | AuthTest | Valida a geração de token administrativo
- Booking | BookingPostTest | Criação de reserva com massa de dados dinâmica
- Booking | BookingGetTest | Consulta de IDs e filtragem por nome/sobrenome
- Booking | BookingPutTest | Atualização total de reserva existente (Requer Token)
- Booking | BookingDeleteTest | Exclusão de reservas e validação via status 404

==================================================

##  Executando os Testes

### Execução Local

Incluir um bloco de código bash com os comandos:
- mvn clean test
- mvn allure:serve

==================================================

## Pipeline CI/CD (GitHub Actions)

- O ambiente é preparado com JDK 21
- O Maven executa a suíte completa de testes de API
- O histórico do Allure é processado
- O relatório é publicado automaticamente no GitHub Pages

==================================================

## Boas Práticas Aplicadas

- Data Factory Pattern: Massa de dados 100% dinâmica com Datafaker
- Client Pattern: Abstração da lógica HTTP em classes especializadas
- Separation of Concerns: DTOs isolados da lógica de teste
- Health Check: Validação prévia da infraestrutura antes da execução dos testes funcionais

==================================================

