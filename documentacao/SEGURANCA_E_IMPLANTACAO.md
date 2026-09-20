# Checklist antes de qualquer lançamento público

## Situação de funcionamento

- Há cadastro validado, login com scrypt + cookie HttpOnly/SameSite, perfil da mãe e dados mínimos do bebê, diretório de unidades aprovadas, horários criados por equipe, solicitação de vagas em transação SQLite, confirmação/cancelamento, registro de doação, histórico e auditoria básica de ações.
- **Não existem hospitais credenciados de fábrica.** O administrador precisa verificar parceria documental e cadastrar unidades oficiais. A agenda interna não sincroniza com sistemas hospitalares existentes.
- Para operação em rede de hospitais, será necessário definir contrato de API, titularidade e governança da agenda, notificações, autenticação institucional/MFA, SLA e responsabilidades de confirmação, atendimento e suporte.

## Controle de dados

- Dados de mãe: nome completo, data de nascimento, contato, cidade/UF. Dados mínimos do bebê: primeiro nome, data de nascimento e sexo opcional. Não coletar CPF, endereço residencial, resultados de exames ou prontuário sem finalidade, base legal, processo e controles próprios.
- O questionário online é **informativo e administrativo**, não faz triagem clínica nem determina aptidão de doação. Todas as dúvidas sobre medicamento, saúde, sintomas, infecções e critérios clínicos são direcionadas a banco de leite/profissional.
- Crianças: dados do bebê só devem ser tratados em seu melhor interesse; exigir política e fundamentação legal revisadas pelos responsáveis institucionais. O formulário exclui autoadesão de responsáveis menores de 18 anos por não incluir fluxo de proteção/representação apropriado; atendimento deve ser buscado diretamente na rede de saúde.
- O banco SQLite desta versão **não tem criptografia de campo ou criptografia em repouso embutida**. Foi incluído um comando manual de backup SQLite consistente com verificação de integridade (`cd site; npm run backup -- /DESTINO_SEGURO/copia.sqlite`), mas não há agendamento de cópias, criptografia automática de backups, rotação nem exclusão automatizada de prontuários. Proteger o volume/infraestrutura com criptografia, criptografia de backup e gestão de chaves; preferir banco gerenciado com isolamento por hospital e conexões TLS em produção.
- O consentimento apresentado no formulário **não substitui** a determinação adequada de base legal, política de privacidade e acordo com o controlador/hospitais; obter revisão jurídica e de proteção de dados. Necessário implementar canal e fluxo formal para direitos do titular (acesso, retificação, eliminação quando cabível, oposição) e prazo de retenção; a interface de atualização cobre somente correção do perfil.

## Acesso e segurança

- ADMIN = administração da plataforma, acesso excepcional a unidades; provisionado por variáveis de ambiente.
- GESTOR = gestão da agenda apenas de suas unidades vinculadas; criado pelo administrador.
- ATENDENTE = equipe hospitalar / enfermagem da unidade vinculada; vê nomes/contatos necessários à agenda e confirma/cancela/registra doações.
- DOADORA = apenas seus próprios dados, agendamentos e histórico; nunca cria funcionários nem publica horários.
- O back-end confere vínculos por hospital e posse de registros; esconder botões não constitui controle de acesso.
- Não implantar sem TLS/HTTPS, `COOKIE_SECURE=true`, proteção de rede, política de senha/MFA para equipes, backups testados, revisão de CSRF e headers atrás do proxy, testes de carga, avaliação de vulnerabilidades, monitoração e gerenciamento de incidentes.
- Rate limits por IP em memória são apropriados apenas para instância única; em múltiplos servidores, usar limitação centralizada e IP real validado pelo proxy confiável. Ter proteção anti-abuso e mitigação DDoS.
- O assistente processa somente pergunta digitada; não recebe automaticamente perfis, prontuários ou histórico. Filtros de PII são parciais e **não garantem anonimização**; consentimento separado, auditoria de fornecedor e restrição de dados devem ser validados.
- Não faça testes com pacientes reais antes de autorização da instituição.

## Procedimento de credenciamento

1. Identificar responsável legal pela instituição e validar que a unidade de fato opera como banco de leite/serviço autorizado.
2. Documentar parceria, base legal, responsabilidades de tratamento e atendimento de solicitações.
3. Obter autorização formal para divulgar nome/telefone/endereço e publicar vagas reais.
4. Registrar a verificação fora do aplicativo, revisar campos, então ativar a unidade no painel ADMIN; as duas confirmações do painel são declarações administrativas, **não são prova automática de parceria**.
5. Nomear gestor e profissionais; validar acesso e treinamento antes de abrir a agenda.

## Critérios de aceite de integração hospitalar

- Unidade e credenciais institucionais verificadas pelo hospital.
- Horários criados ou sincronizados com disponibilidade real, reconciliados com o sistema de agenda do hospital para evitar sobreposição entre canais.
- Confirmação/cancelamento e coleta geram registro de auditoria, com identificador estável e plano de contingência.
- Vagas não excedem capacidade mesmo com pedidos simultâneos.
- Profissionais só consultam dados de suas unidades; mãe só consulta seus próprios dados; logs nunca capturam corpo de requisição com dados pessoais.
- Testes completos de dispositivos móveis, leitores de tela, acessibilidade WCAG, segurança, backups/restauração e performance antes do lançamento.

## Versão seguinte: credenciamento público, mapa e diretório (2026-09)

- O formulário `/hospital-cadastro.html` apenas cria um pedido de análise. O ADMIN o revisa; mesmo um pedido APROVADO não cria credenciamento/agenda automaticamente.
- O diretório SP contém 50 unidades de **fonte governamental datada de 03/05/2016**. São referências para conferência, não parceiros atuais nem hospitais ativados; algumas entradas são centros/BLHs.
- O mapa da versão atual é regional e esquemático, gerado em SVG sem serviço de mapas externo; marcadores são restritos a parceiros habilitados e coordenadas comprovadas. A geolocalização é solicitada somente ao clicar no botão, pelo navegador (HTTPS/localhost); o site não envia coordenadas da mãe à API.
- O cadastro exibe orientações de erro por campo em tempo real, mas somente validação de backend confere integridade final. Não substitui auditoria de acessibilidade, segurança, proteção de dados e testes com navegador.
- Dados de crianças e informações relacionadas à saúde exigem avaliação específica de finalidade, base legal, minimização e melhor interesse; a análise clínica, inclusive orientações de preparo e elegibilidade, permanece com o serviço de saúde.


## Resultado da revisão técnica final (2026-09-19)

- Correções: `Permissions-Policy` passou a permitir que a própria origem solicite geolocalização mediante autorização nativa; consentimento aceito somente se booleano `true`; datas de mãe/bebê e nomes validados no servidor; agendamento usa fuso da operação configurável em `HOSPITAL_TIME_ZONE` (padrão `America/Sao_Paulo`); JSON UTF-8 dividido entre pacotes é processado sem corromper acentos; HEAD de arquivos estáticos não envia corpo; URLs com codificação inválida retornam 400.
- Backup: script `site/scripts/backup.mjs` usa API de backup SQLite consistente e verifica `PRAGMA integrity_check`. **O operador precisa configurar agendamento, criptografia de volume e cópias, retenção e testes periódicos de restauração.** Não coloque cópias no GitHub nem no diretório público.
- **Limitações críticas antes de produção:** não houve homologação com hospitais reais, integração de agenda externa, teste com API OpenAI real, pentest, revisão clínica/LGPD completa, MFA, recuperação de senha ou implantação TLS/HTTPS. Nenhum dado real deve ser importado enquanto pendentes. O fuso configurado é por implantação, não por hospital; operações em vários fusos do Brasil exigem suporte a fuso individual por unidade antes de uso real.
- Aplicativos antigos Android Kotlin e API Java estão arquivados em `codigo_original` e não fazem parte do servidor web publicado; a API Java está marcada `nao_publicar`. Esta auditoria executa a suíte do site, não compila ou homologa os projetos antigos.
