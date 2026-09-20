from pathlib import Path
import sqlite3, re, sys

ROOT = Path(__file__).resolve().parents[1]
SQL = ROOT / "sql"

def statements(text: str):
    # Remove comentários de linha antes de separar por ;.
    # Os scripts desta entrega não possuem ; dentro de literais SQL.
    clean = "\n".join(
        line for line in text.splitlines()
        if not line.lstrip().startswith("--") and not line.lstrip().startswith("\\")
    )
    return [s.strip() for s in clean.split(";") if s.strip()]

conn = sqlite3.connect(":memory:")
conn.execute("PRAGMA foreign_keys = ON")
try:
    conn.executescript((SQL/"01_ddl.sql").read_text(encoding="utf-8"))
    conn.executescript((SQL/"02_massa_dados.sql").read_text(encoding="utf-8"))

    expected = {"usuario":15,"doadora":30,"banco_leite":10,"triagem":30,"agendamento":28,"doacao":24,"lote_leite":24,"notificacao":30,"auditoria":30}
    counts = {t: conn.execute(f"SELECT COUNT(*) FROM {t}").fetchone()[0] for t in expected}
    assert counts == expected, (counts, expected)
    assert sum(counts.values()) == 221

    query_files = ["03_consultas_operacionais.sql","04_consultas_relacionais.sql","05_consultas_analiticas.sql","06_dashboard_indicadores.sql","07_validacoes_integridade.sql"]
    totals = {}
    for fn in query_files:
        executed = 0
        for st in statements((SQL/fn).read_text(encoding="utf-8")):
            if not st: continue
            conn.execute(st).fetchall()
            executed += 1
        totals[fn] = executed
    assert totals["03_consultas_operacionais.sql"] == 5
    assert totals["04_consultas_relacionais.sql"] == 5
    assert totals["05_consultas_analiticas.sql"] == 5
    assert totals["06_dashboard_indicadores.sql"] == 5

    # Integridade lógica deve retornar só zeros.
    integ = conn.execute("""SELECT
      (SELECT COUNT(*) FROM triagem t LEFT JOIN doadora d ON d.id_doadora=t.id_doadora WHERE d.id_doadora IS NULL),
      (SELECT COUNT(*) FROM agendamento a LEFT JOIN doadora d ON d.id_doadora=a.id_doadora WHERE d.id_doadora IS NULL),
      (SELECT COUNT(*) FROM agendamento a LEFT JOIN banco_leite b ON b.id_banco=a.id_banco WHERE b.id_banco IS NULL),
      (SELECT COUNT(*) FROM doacao x LEFT JOIN doadora d ON d.id_doadora=x.id_doadora WHERE d.id_doadora IS NULL),
      (SELECT COUNT(*) FROM doacao x LEFT JOIN banco_leite b ON b.id_banco=x.id_banco WHERE b.id_banco IS NULL),
      (SELECT COUNT(*) FROM lote_leite l LEFT JOIN doacao x ON x.id_doacao=l.id_doacao WHERE x.id_doacao IS NULL),
      (SELECT COUNT(*) FROM banco_leite WHERE estoque_atual_ml > capacidade_ml),
      (SELECT COUNT(*) FROM doacao WHERE volume_ml <= 0 OR volume_ml > 3000)
    """).fetchone()
    assert all(x == 0 for x in integ), integ

    # Testes negativos de constraints/FKs (cada um deve falhar).
    invalid_cases = [
        "INSERT INTO usuario(id_usuario,nome,email,perfil,senha_hash,ativo) VALUES(999,'Teste','usuario01@maelink.local','ADMIN','x',TRUE)",
        "INSERT INTO triagem(id_triagem,id_doadora,data_triagem,resultado,profissional_responsavel) VALUES(999,999,'2026-01-01','APTA','Teste')",
        "INSERT INTO doacao(id_doacao,id_doadora,id_banco,volume_ml,data_coleta,status) VALUES(999,1,1,-10,'2026-01-01','COLETADA')",
        "INSERT INTO banco_leite(id_banco,nome,endereco,cidade,uf,capacidade_ml,estoque_atual_ml,contato,ativo) VALUES(999,'X','Y','Z','SP',100,101,'1',TRUE)",
    ]
    for sql in invalid_cases:
        try:
            conn.execute(sql)
            raise AssertionError("Constraint deveria rejeitar: " + sql)
        except sqlite3.IntegrityError:
            conn.rollback()

    # Valores dos 5 KPIs.
    kpis = []
    for st in statements((SQL/"06_dashboard_indicadores.sql").read_text(encoding="utf-8")):
        if st: kpis.append(conn.execute(st).fetchall())
    assert kpis[0][0][0] == 11978
    assert round(float(kpis[0][0][1]),2) == 11.98
    assert round(float(kpis[1][0][0]),2) == 73.33
    assert round(float(kpis[2][0][0]),2) == 81.82
    assert kpis[3][0][0] == "Banco de Leite Guarulhos" and kpis[3][0][1] == 1802
    assert kpis[4][0][0] == 4

    print("VALIDACAO LOCAL: OK")
    print("Registros:", counts, "TOTAL=221")
    print("Consultas executadas:", totals)
    print("Constraints negativas: 4/4 rejeitadas corretamente")
    print("KPIs: 11978 ml | 73.33% | 81.82% | Guarulhos 1802 ml | 4")
finally:
    conn.close()
