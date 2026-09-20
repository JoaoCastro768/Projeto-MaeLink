# Arquitetura — MãeLink API

## Relação com a Sprint 2

A implementação preserva os domínios apresentados no modelo TOGAF/Archi: Doadoras, Triagem, Banco de Leite, Agendamento, Doação, Notificação e Auditoria. O fluxo de negócio continua cadastro → triagem → encaminhamento → agendamento → acompanhamento.

## Organização

- **Controller:** contrato HTTP e validação de DTOs.
- **Service:** regras de negócio e transações.
- **Repository:** acesso JPA.
- **Entity:** persistência.
- **DTO:** entrada e saída sem exposição das entidades.
- **Flyway:** versionamento do schema.
- **GlobalExceptionHandler:** padronização de erros.

## Decisão de implantação

O desenho alvo da Sprint 2 prevê microsserviços independentes. Nesta entrega, os domínios foram implementados como módulos independentes dentro de um único processo Spring Boot para reduzir a complexidade de execução do trabalho acadêmico. Os contratos e limites de domínio foram mantidos, permitindo que cada módulo seja extraído futuramente para um serviço independente.

## Autenticação no MVP

O contrato de autenticação da Sprint 2 foi mantido com JWT assinado. O módulo `auth` emite tokens, valida assinatura/emissor/expiração e renova apenas tokens válidos. As rotas de negócio permanecem abertas neste MVP acadêmico para facilitar a avaliação via Swagger/Postman; a autorização obrigatória pode ser adicionada em uma etapa posterior sem alterar os contratos dos domínios.

## Conteúdo educativo

Conteúdo educativo é tratado como catálogo de leitura do MVP e não faz parte das sete entidades persistidas definidas no modelo de dados da Sprint 2. Por isso, permanece em memória nesta Sprint. Os domínios persistidos continuam sendo Doadora, Triagem, BancoLeite, Agendamento, Doacao, Notificacao e Auditoria.
