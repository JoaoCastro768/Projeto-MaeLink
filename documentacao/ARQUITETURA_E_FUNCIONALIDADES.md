# Evolução da plataforma MãeLink

**Origem:** último MVP Flutter Sprint 3 `CORRIGIDO` + API Spring Boot `REVISADO` disponíveis no contexto. O site **não existia** como código-fonte independente nesses pacotes; por isso o portal em `site/` foi desenvolvido como novo módulo sem substituir nem sobrescrever os originais.

## Fluxos web implementados

| Fluxo | Implementação | Limite |
| --- | --- | --- |
| Página inicial, navegação e design responsivo | HTML sem frameworks externos, CSS organizado e SVG vetorial discreto | Prévia de renderização desktop/mobile feita com navegador em HTML isolado; validação real em dispositivo ainda pendente |
| Busca de unidades | Filtro por cidade ou UF em 27 polos fictícios, sem hospitais reais | Unidades fictícias, sem geolocalização |
| Pré-triagem | Checklist informativo que não é enviado ao servidor | Não faz avaliação de saúde |
| Solicitação | Login → unidade → data/horário → criação em SQLite | Não agenda atendimento real |
| Acompanhamento | Consulta por sessão e perfil, atualização de status | Processo simulado |
| Perfis | Doadora, Atendente, Gestor e Administrador | Sem MFA, convite, revisão LGPD |
| Assistente | Perguntas gerais por correspondência de expressões e respostas fixas | Não é IA generativa, sem precisão universal |
| Orientações | Links explícitos para páginas do Ministério da Saúde | Conteúdo não substitui banco de leite |

## Decisões de design

- Fundo claro, paleta azul céu e rosa suave, superfícies brancas, poucos elementos por seção e espaçamentos amplos; inspiração estética minimalista sem copiar interfaces ou marcas de terceiros.
- Textos e botões dos blocos principais alinhados; labels de formulários permanecem alinhados à esquerda para legibilidade e associação com os respectivos campos.
- Foco visível por teclado, link para pular conteúdo, `aria-live` nos resultados, cabeçalhos semânticos, controles com rótulos, modo de movimento reduzido e versão mobile.
- Imagem de fundo vetorial leve (`site/public/assets/soft-waves.svg`) e ícone de gota original (`logo.svg`); nenhuma foto clínica real ou dado pessoal incorporado.

## Módulos e relacionamentos

```text
Navegador (HTML/CSS/JS)
  ├── FAQ e assistente de perguntas gerais (respostas locais e fontes públicas)
  └── API HTTP local, same-origin
       ├── Usuários + sessões + RBAC
       ├── Unidades fictícias em 27 UFs (catálogo ilustrativo)
       └── Solicitações demonstrativas e estados
            └── SQLite (arquivo local não público)

Legado preservado: Flutter + Android Kotlin + Spring Boot + scripts de BD.
Não há integração automática entre o portal novo e a API Java original.
```

### Atualização 18/09/2026

- Login e cadastro exclusivos para doadoras nas páginas `/entrar.html` e `/cadastro.html`; a entrada anterior por janela modal também permanece para o fluxo de agendamento.
- API `/api/availability?bankId=demo-sp&date=AAAA-MM-DD` retorna horários **fictícios** disponíveis ou ocupados. O servidor impede dupla reserva ilustrativa do mesmo horário, data e região durante o processo local.
- O filtro permite selecionar estado e pesquisar por cidade; todas as 27 unidades são descritas como **demonstrativas**.
- Sem acesso a prontuários, sem integração de hospitais, sem confirmação de doações reais e sem indicação de elegibilidade clínica.
