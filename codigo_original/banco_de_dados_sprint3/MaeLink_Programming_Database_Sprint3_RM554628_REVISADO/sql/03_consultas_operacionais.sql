-- PARTE 2.1 - Consultas Operacionais (5 consultas)

-- QO1 - Doadoras ativas da cidade de Sao Paulo, das mais recentes para as mais antigas.
SELECT id_doadora, nome, telefone, cidade, status, data_cadastro
FROM doadora
WHERE status = 'ATIVA' AND cidade = 'Sao Paulo'
ORDER BY data_cadastro DESC;

-- QO2 - Cidades distintas que possuem bancos de leite ativos.
SELECT DISTINCT cidade, uf
FROM banco_leite
WHERE ativo = TRUE
ORDER BY cidade;

-- QO3 - Agendamentos ainda abertos (solicitados ou agendados).
SELECT id_agendamento, id_doadora, id_banco, data_hora, tipo_atendimento, status
FROM agendamento
WHERE status IN ('SOLICITADO','AGENDADO')
ORDER BY data_hora;

-- QO4 - Doacoes com volume igual ou superior a 500 ml que nao foram descartadas.
SELECT id_doacao, id_doadora, id_banco, volume_ml, data_coleta, status
FROM doacao
WHERE volume_ml >= 500 AND status <> 'DESCARTADA'
ORDER BY volume_ml DESC;

-- QO5 - Notificacoes pendentes ou com falha, priorizando as mais antigas.
SELECT id_notificacao, id_usuario, canal, status, mensagem
FROM notificacao
WHERE status IN ('PENDENTE','FALHA')
ORDER BY id_notificacao;
