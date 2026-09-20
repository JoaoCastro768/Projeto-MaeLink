# MãeLink API — Sprint 3

## Identificação

**Projeto:** MãeLink  
**Equipe:** MãeLink  
**Integrante:** João Castro — RM 554628  
**Disciplina:** Microservice and Web Engineering & IT Services  
**Repositório GitHub:** https://github.com/JoaoCastro768/maelink-microservices-api

---

## Sobre o projeto

O MãeLink é uma solução digital voltada ao apoio da jornada de doação de leite humano. A proposta é conectar a doadora aos bancos de leite, organizar a triagem inicial, permitir o agendamento, acompanhar o status da doação e concentrar orientações e notificações em um único fluxo.

Esta Sprint transforma a arquitetura definida anteriormente em uma API REST executável. A implementação mantém os domínios previstos na Sprint 2: **Doadoras, Triagem, Bancos de Leite, Agendamentos, Doações, Notificações e Auditoria**, além dos contratos de autenticação e conteúdo educativo.

A API foi estruturada em módulos de domínio dentro de uma aplicação Spring Boot. Isso mantém a separação de responsabilidades e os contratos REST definidos na arquitetura, mas deixa a execução acadêmica mais simples: um único processo da aplicação e uma instância PostgreSQL. Os limites entre os domínios continuam separados por pacotes, services, repositories e DTOs.

> A triagem do MãeLink é apenas organizacional e não substitui avaliação de profissionais de saúde ou do banco de leite humano.

---

## Como os critérios da atividade foram atendidos

| Critério | Implementação |
|---|---|
| Modelagem e persistência | Entidades JPA persistidas em PostgreSQL, com migrations Flyway e dados iniciais de demonstração |
| Operações da API | Endpoints REST para cadastro, consulta, triagem, bancos, agendamento, doação, status, timeline, notificações e auditoria |
| Arquitetura | Controllers acessam Services; Services concentram regras; Repositories ficam restritos à persistência |
| Entrada e saída | DTOs específicos para requests e responses; entidades JPA não são retornadas diretamente |
| Validação | Bean Validation, regras de negócio e tratamento global de erros |
| Versionamento | Todas as rotas funcionais usam o prefixo `/api/v1` |
| Documentação | OpenAPI/Swagger disponível em `/swagger-ui.html` |
| Rastreabilidade | Auditoria persistida e `X-Request-Id` devolvido em cada resposta |
| Execução | Docker Compose sobe PostgreSQL + API com um único comando |

---

## Entidades da Sprint 2

### Doadora

- `id`
- `nome`
- `contato`
- `cidade`
- `consentimento_lgpd`

### Triagem

- `id`
- `doadora_id`
- `respostas`
- `resultado`
- `criado_em`

### BancoLeite

- `id`
- `nome`
- `endereco`
- `area_atendida`
- `capacidade`

### Agendamento

- `id`
- `doadora_id`
- `banco_id`
- `data`
- `status`

A implementação também possui `tipo_atendimento` e `observacao`, porque o protótipo da Sprint 2 previa coleta domiciliar, visita, contato e orientação.

### Doacao

- `id`
- `agendamento_id`
- `volume`
- `status`
- `timeline`

### Notificacao

- `id`
- `usuario_id`
- `canal`
- `mensagem`
- `situacao`

### Auditoria

- `id`
- `usuario`
- `acao`
- `timestamp`
- `ip_origem`

---

## Arquitetura do código

```text
src/main/java/br/com/fiap/maelink/
├── auth/
├── auditoria/
├── bancoleite/
├── common/
│   ├── api/
│   └── exception/
├── config/
├── conteudo/
├── doacao/
├── doadora/
├── agendamento/
├── notificacao/
└── triagem/
```

Cada domínio possui, quando aplicável:

```text
controller -> service -> repository -> banco de dados
       |          |
      DTOs     regras de negócio
```

Os controllers não acessam repositories diretamente.

---

## Tecnologias

- Java 21
- Spring Boot 3.4.5
- Spring Web
- Spring Data JPA
- Jakarta Bean Validation
- PostgreSQL 16
- Flyway
- OpenAPI / Swagger UI
- Spring Boot Actuator
- Request ID (`X-Request-Id`) para rastreabilidade
- CORS configurado para desenvolvimento
- JJWT
- Docker / Docker Compose
- Maven

---

# Execução recomendada com Docker

Esta é a forma mais simples de executar o projeto em um ambiente limpo.

## Pré-requisitos

- Git
- Docker Desktop com Docker Compose

## 1. Clonar o repositório

```bash
git clone https://github.com/JoaoCastro768/maelink-microservices-api.git
cd maelink-microservices-api
```

## 2. Construir e iniciar banco + API

```bash
docker compose up --build
```

O Docker Compose cria:

- PostgreSQL na porta `5432`;
- API MãeLink na porta `8080`.

Aguarde até a aplicação indicar que o Spring Boot foi iniciado.

## 3. Verificar a aplicação

Health check:

```text
http://localhost:8080/actuator/health
```

Swagger:

```text
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:8080/v3/api-docs
```

## 4. Encerrar

```bash
docker compose down
```

Para remover também os dados persistidos do PostgreSQL:

```bash
docker compose down -v
```

Depois que a aplicação estiver no ar, também é possível executar um teste rápido:

**Windows PowerShell**

```powershell
.\scripts\smoke-test.ps1
```

**Linux/macOS**

```bash
./scripts/smoke-test.sh
```

---

# Execução local sem container da API

Também é possível executar o Spring Boot localmente e usar somente o PostgreSQL em Docker.

## Pré-requisitos

- Java 21
- Maven 3.9+
- Docker Desktop

## 1. Iniciar somente o banco

```bash
docker compose up -d db
```

## 2. Executar testes

```bash
mvn test
```

## 3. Iniciar a aplicação

```bash
mvn spring-boot:run
```

A API estará disponível em:

```text
http://localhost:8080
```

---

# Banco de dados

Por padrão, a aplicação utiliza:

```text
Host: localhost
Porta: 5432
Database: maelink
Usuário: maelink
Senha: maelink
```

Esses valores podem ser alterados pelas variáveis. O arquivo `.env.example` mostra um modelo de configuração:

```text
DB_HOST
DB_PORT
DB_NAME
DB_USER
DB_PASSWORD
```

Exemplo:

```bash
DB_HOST=localhost DB_PORT=5432 DB_NAME=maelink DB_USER=maelink DB_PASSWORD=maelink mvn spring-boot:run
```

As tabelas são criadas pelo Flyway na inicialização. O arquivo de criação está em:

```text
src/main/resources/db/migration/V1__create_schema.sql
```

Há também dados demonstrativos em:

```text
src/main/resources/db/migration/V2__seed_demo_data.sql
```

---

# Endpoints principais

Base URL:

```text
http://localhost:8080/api/v1
```

## Auth

| Método | Endpoint | Função |
|---|---|---|
| POST | `/auth/login` | Emite token JWT assinado |
| POST | `/auth/refresh` | Valida o Bearer token recebido e emite um novo token com o mesmo usuário/perfil |
| GET | `/auth/validate` | Valida assinatura, emissor e expiração e retorna os claims principais |

## Doadoras

| Método | Endpoint | Função |
|---|---|---|
| POST | `/doadoras` | Cadastra doadora |
| GET | `/doadoras` | Lista doadoras com paginação |
| GET | `/doadoras/{id}` | Consulta perfil |
| PUT | `/doadoras/{id}` | Atualiza perfil e consentimento |

## Triagem

| Método | Endpoint | Função |
|---|---|---|
| POST | `/triagens` | Registra respostas e calcula resultado preliminar |
| GET | `/triagens/{id}` | Consulta triagem |
| GET | `/triagens/{id}/resultado` | Retorna resultado + orientação |
| GET | `/triagens?doadoraId={id}` | Histórico da doadora |

## Bancos de leite

| Método | Endpoint | Função |
|---|---|---|
| GET | `/bancos` | Lista unidades |
| GET | `/bancos?cidade=São Paulo` | Filtra área atendida |
| GET | `/bancos/{id}` | Detalha unidade |
| GET | `/bancos/{id}/capacidade` | Consulta capacidade |
| POST | `/bancos` | Cadastra unidade |

## Agendamentos

| Método | Endpoint | Função |
|---|---|---|
| POST | `/agendamentos` | Cria solicitação de atendimento/coleta |
| GET | `/agendamentos/{id}` | Consulta agendamento |
| GET | `/agendamentos?doadoraId={id}` | Lista agenda da doadora |
| PATCH | `/agendamentos/{id}/status` | Atualiza status |
| PATCH | `/agendamentos/{id}/reagendar` | Reagenda |
| DELETE | `/agendamentos/{id}` | Cancela |

## Doações

| Método | Endpoint | Função |
|---|---|---|
| POST | `/doacoes` | Abre o acompanhamento de uma doação |
| GET | `/doacoes/{id}` | Consulta doação |
| GET | `/doacoes/{id}/timeline` | Retorna histórico da jornada |
| PATCH | `/doacoes/{id}/status` | Atualiza status e timeline |

## Notificações

| Método | Endpoint | Função |
|---|---|---|
| POST | `/notificacoes` | Simula envio de notificação |
| GET | `/notificacoes?usuarioId={id}` | Lista notificações da doadora |

## Conteúdo educativo

| Método | Endpoint | Função |
|---|---|---|
| GET | `/conteudos` | Lista orientações educativas do MVP |

## Auditoria

| Método | Endpoint | Função |
|---|---|---|
| GET | `/auditorias` | Lista rastros das operações sensíveis |

---

# Exemplos de teste

O projeto possui um arquivo pronto para o REST Client do VS Code/IntelliJ e uma coleção Postman:

```text
docs/requests.http
docs/MaeLink.postman_collection.json
```

## Criar doadora

```bash
curl -X POST http://localhost:8080/api/v1/doadoras \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Joana Silva",
    "contato": "joana.silva@example.com",
    "cidade": "São Paulo",
    "consentimentoLgpd": true
  }'
```

## Registrar triagem

```bash
curl -X POST http://localhost:8080/api/v1/triagens \
  -H "Content-Type: application/json" \
  -d '{
    "doadoraId": 1,
    "respostas": {
      "amamentando": "sim",
      "excessoLeite": "sim",
      "aceitaContato": "sim"
    }
  }'
```

## Listar bancos

```bash
curl "http://localhost:8080/api/v1/bancos?cidade=São%20Paulo"
```

## Criar agendamento

Use uma data futura ao testar:

```bash
curl -X POST http://localhost:8080/api/v1/agendamentos \
  -H "Content-Type: application/json" \
  -d '{
    "doadoraId": 1,
    "bancoId": 1,
    "data": "2027-01-20T10:00:00",
    "tipoAtendimento": "COLETA_DOMICILIAR",
    "observacao": "Retirada residencial"
  }'
```

## Consultar timeline de uma doação

```bash
curl http://localhost:8080/api/v1/doacoes/1/timeline
```

---

# Validações e tratamento de erro

A API utiliza Bean Validation nos DTOs de entrada.

Exemplos de regras:

- nome, contato e cidade da doadora são obrigatórios;
- consentimento LGPD precisa ser informado e aceito no cadastro;
- capacidade do banco não pode ser negativa;
- agendamento precisa possuir data futura;
- a doadora precisa possuir triagem apta para iniciar o agendamento;
- o banco selecionado precisa possuir capacidade operacional maior que zero;
- IDs relacionados precisam existir;
- um mesmo agendamento não pode gerar duas doações;
- a doação só pode ser aberta após confirmação/reagendamento válido;
- agendamento cancelado não pode ser reagendado;
- volume, quando informado, não pode ser negativo.

Exemplo de erro de validação:

```json
{
  "timestamp": "2026-08-26T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Dados de entrada inválidos",
  "path": "/api/v1/doadoras",
  "violations": {
    "nome": "não deve estar em branco"
  }
}
```

---

# Fluxo principal demonstrável

Um roteiro simples para apresentação da API:

1. `POST /api/v1/doadoras` — cadastrar uma nutriz;
2. `POST /api/v1/triagens` — registrar triagem inicial;
3. `GET /api/v1/triagens/{id}/resultado` — consultar o encaminhamento;
4. `GET /api/v1/bancos` — consultar unidades;
5. `POST /api/v1/agendamentos` — solicitar coleta/atendimento;
6. `PATCH /api/v1/agendamentos/{id}/status` — equipe confirma o atendimento;
7. `POST /api/v1/doacoes` — iniciar acompanhamento;
8. `PATCH /api/v1/doacoes/{id}/status` — avançar a jornada;
9. `GET /api/v1/doacoes/{id}/timeline` — visualizar rastreabilidade;
10. `POST /api/v1/notificacoes` — enviar aviso;
11. `GET /api/v1/auditorias` — verificar ações registradas.

Esse fluxo corresponde à jornada desenhada nas Sprints anteriores: **cadastro → triagem → banco sugerido → agendamento → acompanhamento de status**.

---

# Observação sobre autenticação

A arquitetura da Sprint 2 previa um `Auth Service` com OAuth2/JWT. Para preservar esse contrato, a Sprint 3 implementa emissão, validação e renovação de JWT assinado. O endpoint `/api/v1/auth/refresh` só renova tokens válidos, e `/api/v1/auth/validate` permite demonstrar assinatura, emissor, perfil e expiração. Tokens ausentes, inválidos ou expirados recebem resposta HTTP `401`.

A autenticação de credenciais contra um provedor de identidade externo e a autorização obrigatória em todas as rotas ficaram fora do escopo desta entrega acadêmica, porque não são requisitos mínimos da rubrica. Essa decisão mantém os endpoints de negócio simples de testar no Swagger/Postman sem remover o contrato de autenticação definido na arquitetura.

A chave usada para o ambiente acadêmico pode ser sobrescrita por:

```text
JWT_SECRET
```

---


# Testes automatizados

Além do `contextLoads`, o projeto possui testes unitários das regras mais sensíveis:

- `JwtServiceTest`: emissão, validação, renovação e rejeição de token inválido;
- `TriagemServiceTest`: cálculo de resultado `APTA_CONTATO`;
- `AgendamentoServiceTest`: criação válida e bloqueio quando o banco está sem capacidade.

Para executar:

```bash
mvn test
```

Os scripts `scripts/smoke-test.ps1` e `scripts/smoke-test.sh` validam a API já em execução, incluindo health check, autenticação JWT e consultas principais.

---

# Estrutura de dados e migrations

```text
src/main/resources/db/migration/
├── V1__create_schema.sql
└── V2__seed_demo_data.sql
```

O `V1` contém todas as entidades do modelo da Sprint 2. O `V2` carrega apenas dados fictícios para facilitar a avaliação e a demonstração.

---

## Status da entrega

- [x] Entidades da Sprint 2 implementadas
- [x] Persistência PostgreSQL
- [x] Migrations Flyway
- [x] Controllers separados de repositories
- [x] Camada de services
- [x] DTOs de entrada e saída
- [x] Bean Validation
- [x] Tratamento global de erros
- [x] Rotas `/api/v1`
- [x] Swagger/OpenAPI
- [x] Health check
- [x] Exemplos de requisições
- [x] Dockerfile
- [x] Docker Compose
- [x] README com execução completa
- [x] JWT com validação e refresh funcional
- [x] Testes unitários de autenticação, triagem e agendamento

