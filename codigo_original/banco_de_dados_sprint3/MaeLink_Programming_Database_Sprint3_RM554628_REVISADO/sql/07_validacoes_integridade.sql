-- MãeLink - Validações de integridade e consistência
-- Resultado esperado: todas as colunas quantidade_erros devem ser 0.

SELECT 'triagem_sem_doadora' AS validacao, COUNT(*) AS quantidade_erros
FROM triagem t LEFT JOIN doadora d ON d.id_doadora = t.id_doadora
WHERE d.id_doadora IS NULL
UNION ALL
SELECT 'agendamento_sem_doadora', COUNT(*)
FROM agendamento a LEFT JOIN doadora d ON d.id_doadora = a.id_doadora
WHERE d.id_doadora IS NULL
UNION ALL
SELECT 'agendamento_sem_banco', COUNT(*)
FROM agendamento a LEFT JOIN banco_leite b ON b.id_banco = a.id_banco
WHERE b.id_banco IS NULL
UNION ALL
SELECT 'doacao_sem_doadora', COUNT(*)
FROM doacao x LEFT JOIN doadora d ON d.id_doadora = x.id_doadora
WHERE d.id_doadora IS NULL
UNION ALL
SELECT 'doacao_sem_banco', COUNT(*)
FROM doacao x LEFT JOIN banco_leite b ON b.id_banco = x.id_banco
WHERE b.id_banco IS NULL
UNION ALL
SELECT 'lote_sem_doacao', COUNT(*)
FROM lote_leite l LEFT JOIN doacao x ON x.id_doacao = l.id_doacao
WHERE x.id_doacao IS NULL
UNION ALL
SELECT 'estoque_acima_capacidade', COUNT(*)
FROM banco_leite WHERE estoque_atual_ml > capacidade_ml
UNION ALL
SELECT 'volume_doacao_invalido', COUNT(*)
FROM doacao WHERE volume_ml <= 0 OR volume_ml > 3000
UNION ALL
SELECT 'doacao_agendamento_inconsistente', COUNT(*)
FROM doacao x
JOIN agendamento a ON a.id_agendamento = x.id_agendamento
WHERE x.id_doadora <> a.id_doadora OR x.id_banco <> a.id_banco;
