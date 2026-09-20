# Segurança, privacidade e limitações — MãeLink

## Implementado neste portal web

- Cadastro público gera **somente** perfil `DOADORA`; perfis `ATENDENTE` e `GESTOR` só podem ser criados por `ADMIN` autenticado. Administrador inicial requer configuração explícita no servidor, nunca cadastro pela interface pública.
- Autorização no backend para listagem, criação e atualização de solicitações. Doadoras veem apenas as próprias solicitações. Funcionários veem identificador e agenda simulados, não e-mails das doadoras. Somente administrador vê e-mails de funcionários.
- Senhas são derivadas com `scrypt`, salt aleatório exclusivo; sessões são tokens aleatórios de 256 bits cujos hashes ficam no banco, enviados por cookies `HttpOnly; SameSite=Strict`. `COOKIE_SECURE=true` marca cookies para HTTPS; **configure isto para implantação real**.
- Conteúdo dinâmico da interface é escapado, configurações não contêm senha fixa no código, cabeçalhos CSP, `nosniff`, `frame-ancestors`, `Referrer-Policy`; requisições de alteração exigem cabeçalho de aplicação e checagem da origem quando fornecida. Login e cadastro contam com limitação simples por processo.
- A triagem educativa **não é enviada ao servidor**. Perguntas digitadas ao assistente são respondidas localmente, sem solicitação de rede por pergunta ou persistência deliberada; o assistente nem conhece os dados da conta. O sistema não armazena dados clínicos.
- Conteúdo sobre doação usa links oficiais do Ministério da Saúde e evita decisões de elegibilidade individual. Unidades, nomes, distâncias, disponibilidades e solicitações estão sinalizados como fictícios; a lista nova abrange 27 UFs exclusivamente como cenários educacionais.

## O que ainda é indispensável antes de produção

1. Contratar/autorizar participação de bancos de leite e integrar dados reais verificados (endereços, contatos, horários, vagas), com autorização e procedimentos operacionais.
2. Rever conteúdo com especialistas de saúde; implementar canal de atendimento humano real e protocolo de urgência. Assistente tem base pequena: responde somente perguntas gerais cobertas por respostas fixas, sem prometer precisão universal.
3. Implementar HTTPS/TLS em proxy confiável, `COOKIE_SECURE=true`, monitoramento, gestão/rotação de senhas e segredos, backup criptografado e testes de segurança independentes.
4. Completar gestão de conta: verificação de e-mail, recuperação de senha, MFA para equipe, revogação administrativa de sessões, auditoria dos atos de equipe, limitação por IP compartilhado com Redis na escala e revisões LGPD (base legal, retenção, exclusão, transparência).
5. Substituir recurso experimental `node:sqlite` por tecnologia com suporte operacional de produção e política de migrations/backup.
6. Corrigir e testar separadamente o backend Java original **antes de qualquer integração**: atualmente `AuthController.login` emite JWT sem validar a senha do `LoginRequest`, e os endpoints de dados não estão protegidos por middleware de autenticação/autorização. A API legada está arquivada como referência, não como componente implantável.
7. Testar visualmente em dispositivos reais, com navegação por teclado e leitor de tela. A navegação HTTP local foi bloqueada pelo ambiente; capturas visuais foram realizadas em HTML isolado no Chromium, com respostas de API simuladas no navegador. Isso não equivale a uma auditoria completa de acessibilidade nem à validação de implantação.

## Exemplos de fluxo de proteção

- Pessoa A cria solicitação → pessoa B não a vê nem modifica; operador pode confirmar → concluir; doadora não pode forçar confirmação nem ver agenda de outras doadoras.
- Solicitação cancelada/concluída não pode ser alterada em seguida pelo mesmo endpoint.
- A interface não deve ser confundida com um sistema real de triagem médica ou agendamento hospitalar.

## Fontes públicas consultadas em 18/09/2026

- Ministério da Saúde, [Doação de leite](https://www.gov.br/saude/pt-br/assuntos/saude-de-a-a-z/d/doacao-de-leite).
- Ministério da Saúde, [Como doar](https://www.gov.br/saude/pt-br/assuntos/saude-de-a-a-z/d/doacao-de-leite/como-doar).

## Complemento do protótipo azul e rosa

- Disponibilidade ilustrativa é consultada por data sem expor identidades de outras mães; conflito de horário retorna HTTP 409. Uma implantação distribuída exigiria restrição de unicidade transacional no banco de produção.
- A página dedicada de login usa a sessão `HttpOnly` mantida pelo servidor; não guarda senha em `localStorage`. O cadastro público continua criando somente o perfil `DOADORA`.
- É indispensável firmar parcerias e integrar sistemas reais dos hospitais/bancos antes de oferecer datas, horários ou confirmações verdadeiras. **Nenhuma unidade fictícia deve ser usada para deslocamento real.**
