# Sistema de Gerenciamento de Portfólio

Este é um sistema de gerenciamento de portfólio desenvolvido com Spring Boot que permite aos usuários gerenciar projetos e membros da equipe.

## Tecnologias Utilizadas

- **Spring Framework** - Framework principal para construção da aplicação
- **JSP (JavaServer Pages)** - Tecnologia de visualização para renderização de páginas HTML
- **Bootstrap** - Framework frontend para design responsivo
- **JUnit** - Framework de testes para testes unitários
- **Mockito** - Framework de mock para testes
- **PostgreSQL** - Banco de dados relacional para armazenamento de dados
- **Flyway** - Ferramenta de migração de banco de dados
- **Spring Data JPA** - Camada de acesso a dados
- **Maven** - Gerenciamento de build e dependências
- **Docker** - Containerização

## Instruções de Configuração e Execução

### Opção 1: Usando Docker (Recomendado)

Este método usa Docker para executar tanto a aplicação quanto o banco de dados PostgreSQL.

#### Pré-requisitos
- Docker e Docker Compose instalados
- Java 17 ou superior
- Maven

#### Passos
1. Clone o repositório
2. Navegue até o diretório raiz do projeto
3. Execute a aplicação com: mvn spring-boot:run

Isso iniciará automaticamente o container PostgreSQL usando Docker Compose.
4. Acesse a aplicação em http://localhost:8084

### Opção 2: Usando Banco de Dados PostgreSQL Externo

Este método requer que você tenha PostgreSQL instalado e executando em sua máquina ou acessível remotamente.

#### Pré-requisitos
- Java 17 ou superior
- Maven
- Servidor de banco de dados PostgreSQL (instalado localmente ou acessível remotamente)

#### Passos
1. Clone o repositório
2. Crie um banco de dados PostgreSQL chamado `mydatabase`
3. Crie um usuário `myuser` com senha `secret` (ou use suas próprias credenciais)
4. Conceda todos os privilégios no banco de dados para o usuário
5. Execute a aplicação com: mvn spring-boot:run -Dspring-boot.run.profiles=local