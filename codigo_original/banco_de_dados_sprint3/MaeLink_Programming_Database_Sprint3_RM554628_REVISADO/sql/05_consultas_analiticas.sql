-- PARTE 2.3 - Consultas Analiticas (5 consultas)

-- QA1 - Volume e quantidade de doacoes por banco; exibe apenas bancos com 2 ou mais doacoes.
SELECT b.id_banco, b.nome,
       COUNT(d.id_doacao) AS quantidade_doacoes,
       SUM(CASE WHEN d.status <> 'DESCARTADA' THEN d.volume_ml ELSE 0 END) AS volume_valido_ml,
       ROUND(AVG(d.volume_ml), 2) AS volume_medio_ml
FROM banco_leite b
INNER JOIN doacao d ON d.id_banco = b.id_banco
GROUP BY b.id_banco, b.nome
HAVING COUNT(d.id_doacao) >= 2
ORDER BY volume_valido_ml DESC;

-- QA2 - Resultado das triagens e percentual sobre o total.
SELECT resultado,
       COUNT(*) AS quantidade,
       ROUND(100.0 * COUNT(*) / (SELECT COUNT(*) FROM triagem), 2) AS percentual
FROM triagem
GROUP BY resultado
ORDER BY quantidade DESC;

-- QA3 - Conversao por banco: agendamentos concluidos e doacoes registradas.
SELECT b.id_banco, b.nome,
       COUNT(DISTINCT CASE WHEN a.status = 'CONCLUIDO' THEN a.id_agendamento END) AS agendas_concluidas,
       COUNT(DISTINCT d.id_doacao) AS doacoes_registradas,
       ROUND(100.0 * COUNT(DISTINCT d.id_doacao) /
             NULLIF(COUNT(DISTINCT CASE WHEN a.status = 'CONCLUIDO' THEN a.id_agendamento END), 0), 2) AS taxa_conversao_pct
FROM banco_leite b
LEFT JOIN agendamento a ON a.id_banco = b.id_banco
LEFT JOIN doacao d ON d.id_agendamento = a.id_agendamento
GROUP BY b.id_banco, b.nome
HAVING COUNT(a.id_agendamento) > 0
ORDER BY taxa_conversao_pct DESC, doacoes_registradas DESC;

-- QA4 - Bancos cujo volume valido supera a media de volume por banco.
SELECT b.id_banco, b.nome,
       SUM(CASE WHEN d.status <> 'DESCARTADA' THEN d.volume_ml ELSE 0 END) AS volume_valido_ml
FROM banco_leite b
INNER JOIN doacao d ON d.id_banco = b.id_banco
GROUP BY b.id_banco, b.nome
HAVING SUM(CASE WHEN d.status <> 'DESCARTADA' THEN d.volume_ml ELSE 0 END) > (
    SELECT AVG(volume_banco)
    FROM (
        SELECT SUM(CASE WHEN status <> 'DESCARTADA' THEN volume_ml ELSE 0 END) AS volume_banco
        FROM doacao
        GROUP BY id_banco
    ) medias
)
ORDER BY volume_valido_ml DESC;

-- QA5 - Doadoras cujo volume total doado supera a media de volume total por doadora.
SELECT d.id_doadora, d.nome,
       COUNT(doa.id_doacao) AS quantidade_doacoes,
       SUM(CASE WHEN doa.status <> 'DESCARTADA' THEN doa.volume_ml ELSE 0 END) AS volume_total_ml
FROM doadora d
INNER JOIN doacao doa ON doa.id_doadora = d.id_doadora
GROUP BY d.id_doadora, d.nome
HAVING SUM(CASE WHEN doa.status <> 'DESCARTADA' THEN doa.volume_ml ELSE 0 END) > (
    SELECT AVG(total_doadora)
    FROM (
        SELECT SUM(CASE WHEN status <> 'DESCARTADA' THEN volume_ml ELSE 0 END) AS total_doadora
        FROM doacao
        GROUP BY id_doadora
    ) totais
)
ORDER BY volume_total_ml DESC;
