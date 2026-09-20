INSERT INTO doadoras (nome, contato, cidade, consentimento_lgpd) VALUES
('Ana Martins', 'ana.martins@example.com', 'São Paulo', true),
('Mariana Souza', '(11) 99999-1002', 'São Paulo', true);

INSERT INTO bancos_leite (nome, endereco, area_atendida, capacidade) VALUES
('Banco de Leite Humano - Unidade Centro', 'Rua das Flores, 120 - Centro', 'São Paulo - Centro', 85),
('Posto de Coleta - Unidade Sul', 'Av. das Acácias, 450 - Zona Sul', 'São Paulo - Zona Sul', 60),
('Banco de Leite Humano - Unidade Norte', 'Rua Horizonte, 88 - Zona Norte', 'São Paulo - Zona Norte', 72);

INSERT INTO triagens (doadora_id, respostas, resultado, criado_em) VALUES
(1, '{"amamentando":"sim","excessoLeite":"sim","aceitaContato":"sim"}', 'APTA_CONTATO', CURRENT_TIMESTAMP);

INSERT INTO agendamentos (doadora_id, banco_id, data, status, tipo_atendimento, observacao) VALUES
(1, 1, CURRENT_TIMESTAMP + INTERVAL '2 day', 'CONFIRMADO', 'COLETA_DOMICILIAR', 'Retirada residencial cadastrada');

INSERT INTO doacoes (agendamento_id, volume, status, timeline) VALUES
(1, NULL, 'AGENDADA', '[{"status":"SOLICITADA","descricao":"Solicitação criada"},{"status":"TRIAGEM_APROVADA","descricao":"Triagem aprovada"},{"status":"AGENDADA","descricao":"Coleta confirmada"}]');

INSERT INTO notificacoes (usuario_id, canal, mensagem, situacao, criado_em) VALUES
(1, 'EMAIL', 'Sua coleta foi agendada com sucesso.', 'ENVIADA', CURRENT_TIMESTAMP);
