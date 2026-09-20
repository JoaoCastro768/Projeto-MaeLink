-- PARTE 2.4 - Consultas usadas no Dashboard Executivo

-- IND1 - Volume total valido registrado (ml e litros)
SELECT SUM(volume_ml) AS volume_total_ml,
       ROUND(SUM(volume_ml) / 1000.0, 2) AS volume_total_litros
FROM doacao
WHERE status <> 'DESCARTADA';

-- IND2 - Taxa de triagens aptas
SELECT ROUND(100.0 * SUM(CASE WHEN resultado = 'APTA' THEN 1 ELSE 0 END) / COUNT(*), 2) AS taxa_triagens_aptas_pct
FROM triagem;

-- IND3 - Conversao de doadoras aptas em pelo menos um agendamento
SELECT ROUND(
    100.0 * COUNT(DISTINCT a.id_doadora) /
    NULLIF((SELECT COUNT(DISTINCT id_doadora) FROM triagem WHERE resultado = 'APTA'), 0),
    2
) AS conversao_apta_agendamento_pct
FROM agendamento a
WHERE a.id_doadora IN (SELECT id_doadora FROM triagem WHERE resultado = 'APTA');

-- IND4 - Banco de leite com maior volume valido recebido
SELECT b.nome, SUM(d.volume_ml) AS volume_valido_ml
FROM banco_leite b
INNER JOIN doacao d ON d.id_banco = b.id_banco
WHERE d.status <> 'DESCARTADA'
GROUP BY b.id_banco, b.nome
ORDER BY volume_valido_ml DESC
LIMIT 1;

-- IND5 - Doadoras aptas que ainda nao possuem agendamento
SELECT COUNT(*) AS doadoras_aptas_sem_agendamento
FROM doadora d
WHERE EXISTS (
    SELECT 1 FROM triagem t WHERE t.id_doadora = d.id_doadora AND t.resultado = 'APTA'
)
AND NOT EXISTS (
    SELECT 1 FROM agendamento a WHERE a.id_doadora = d.id_doadora
);
