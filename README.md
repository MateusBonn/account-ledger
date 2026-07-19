# Account Ledger API

API REST desenvolvida para gerenciamento de contas e processamento de eventos financeiros (depósitos, saques e transferências). O projeto foi construído com foco em legibilidade, testabilidade e resolução eficiente do problema proposto.

## 🏗️ Arquitetura e Decisões de Design

A engenharia deste projeto foi pautada por pragmatismo e eficiência na entrega de valor:

* **Pragmatismo Arquitetural (Layered Architecture / MVC):** A hipótese inicial contemplava a estruturação via Arquitetura Hexagonal (Ports and Adapters) visando o isolamento absoluto do domínio. Contudo, ao avaliar o escopo e o ciclo de vida deste teste técnico, a arquitetura foi intencionalmente pivotada para o padrão tradicional de Camadas (MVC). Essa decisão evita *over-engineering* e reduz a sobrecarga cognitiva (*boilerplate* de mappers bidirecionais), priorizando a velocidade de compreensão do código e a aderência ao princípio **KISS** (*Keep It Simple, Stupid*).
* **Design Pattern (Strategy):** O processamento de eventos abdicou do uso de complexidade ciclomática (`if/else` ou `switch/case`) em favor do padrão Strategy. Isso garante a aderência absoluta ao **Open/Closed Principle (OCP)** do SOLID. Para adicionar suporte a um novo tipo de evento no futuro, basta criar uma nova classe que implemente a interface de estratégia; nenhuma alteração no código existente do serviço (`AccountService`) será necessária, garantindo flexibilidade onde ela realmente importa.

## 🧪 Executando os Testes

A aplicação foi desenvolvida com foco em confiabilidade, possuindo cobertura através de **Testes Unitários** (validando o isolamento das estratégias e regras de negócio) e **Testes de Integração** (validando o fluxo de ponta a ponta e o comportamento E2E da API).

Para executar toda a suíte de testes, rode o comando abaixo na raiz do projeto:

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

