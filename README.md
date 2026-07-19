# Account Ledger API

API REST desenvolvida para gerenciamento de contas e processamento de eventos financeiros (depósitos, saques e transferências). O projeto foi construído com foco em extensibilidade, testabilidade e separação clara de responsabilidades.

## 🏗️ Arquitetura e Decisões de Design

Este projeto foi desenhado utilizando conceitos avançados de engenharia de software para garantir facilidade de manutenção a longo prazo:

* **Arquitetura Hexagonal (Ports and Adapters):** A aplicação foi estruturada para separar estritamente a camada HTTP (Inbound Adapters) e a persistência (Outbound Adapters) do núcleo de regras de negócio (Domínio). O domínio não possui conhecimento sobre como os dados entram ou saem do sistema.
* **Design Pattern (Strategy):** O processamento de eventos abdicou do uso de complexidade ciclomática (`if/else` ou `switch/case`) em favor do padrão Strategy. Isso garante a aderência absoluta ao **Open/Closed Principle (OCP)** do SOLID. Para adicionar suporte a um novo tipo de evento no futuro, basta criar uma nova classe que implemente a interface de estratégia; nenhuma alteração no código existente do serviço (`AccountService`) será necessária, garantindo máxima flexibilidade.

## 🧪Executando os Testes

A aplicação foi desenvolvida com foco em confiabilidade, possuindo cobertura através de Testes Unitários (validando o isolamento das estratégias e regras de negócio) e Testes de Integração (validando o fluxo de ponta a ponta e o comportamento E2E da API).

Para executar toda a suíte de testes, rode o comando abaixo:

```bash
mvn test
```

## 🚀 Como Executar

### Pré-requisitos
* **Java 17** (ou superior)
* **Maven** (ou utilize o `mvnw` embutido no projeto)

### Subindo a Aplicação
Para iniciar o servidor localmente, abra o terminal na raiz do projeto e execute:

```bash
mvn spring-boot:run
```

