-- PARTE 2.2 - Consultas Relacionais (5 consultas)

-- QR1 - Historico de doacoes com nome da doadora e banco de leite.
SELECT d.id_doacao, doad.nome AS doadora, b.nome AS banco, d.volume_ml, d.data_coleta, d.status
FROM doacao d
INNER JOIN doadora doad ON doad.id_doadora = d.id_doadora
INNER JOIN banco_leite b ON b.id_banco = d.id_banco
ORDER BY d.data_coleta DESC;

-- QR2 - Triagens com dados cadastrais da doadora.
SELECT t.id_triagem, d.nome, d.cidade, t.data_triagem, t.resultado, t.profissional_responsavel
FROM triagem t
INNER JOIN doadora d ON d.id_doadora = t.id_doadora
ORDER BY t.data_triagem;

-- QR3 - Todas as doadoras e seus agendamentos, incluindo doadoras sem agendamento.
SELECT d.id_doadora, d.nome, a.id_agendamento, a.data_hora, a.status AS status_agendamento
FROM doadora d
LEFT JOIN agendamento a ON a.id_doadora = d.id_doadora
ORDER BY d.nome, a.data_hora;

-- QR4 - Todos os bancos e seus agendamentos, inclusive unidades sem agendamento (RIGHT JOIN).
SELECT b.id_banco, b.nome AS banco, a.id_agendamento, a.data_hora, a.status
FROM agendamento a
RIGHT JOIN banco_leite b ON b.id_banco = a.id_banco
ORDER BY b.nome, a.data_hora;

-- QR5 - Lotes rastreados ate a doacao, doadora e banco de destino.
SELECT l.codigo_rastreio, l.status_analise, d.volume_ml, doad.nome AS doadora, b.nome AS banco
FROM lote_leite l
INNER JOIN doacao d ON d.id_doacao = l.id_doacao
INNER JOIN doadora doad ON doad.id_doadora = d.id_doadora
INNER JOIN banco_leite b ON b.id_banco = d.id_banco
ORDER BY l.codigo_rastreio;
