BEGIN;

-- MãeLink - Programming and Database Management - Sprint 3
-- João Castro - RM 554628
-- Banco alvo: PostgreSQL 16 (também validado localmente em SQLite para revisão técnica)

DROP TABLE IF EXISTS auditoria;
DROP TABLE IF EXISTS notificacao;
DROP TABLE IF EXISTS lote_leite;
DROP TABLE IF EXISTS doacao;
DROP TABLE IF EXISTS agendamento;
DROP TABLE IF EXISTS triagem;
DROP TABLE IF EXISTS doadora;
DROP TABLE IF EXISTS banco_leite;
DROP TABLE IF EXISTS usuario;

CREATE TABLE usuario (
    id_usuario INTEGER PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    perfil VARCHAR(30) NOT NULL CHECK (perfil IN ('ADMIN','PROFISSIONAL_SAUDE','OPERADOR_BANCO','ANALISTA')),
    senha_hash VARCHAR(64) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    ultimo_acesso TIMESTAMP
);

CREATE TABLE doadora (
    id_doadora INTEGER PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    cpf_hash VARCHAR(64) NOT NULL UNIQUE,
    cep VARCHAR(9) NOT NULL,
    cidade VARCHAR(80) NOT NULL,
    uf CHAR(2) NOT NULL,
    consentimento_lgpd BOOLEAN NOT NULL CHECK (consentimento_lgpd IN (TRUE,FALSE)),
    data_consentimento TIMESTAMP NOT NULL,
    status VARCHAR(30) NOT NULL CHECK (status IN ('ATIVA','INATIVA','EM_ACOMPANHAMENTO')),
    data_cadastro TIMESTAMP NOT NULL
);

CREATE TABLE banco_leite (
    id_banco INTEGER PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    endereco VARCHAR(220) NOT NULL,
    cidade VARCHAR(80) NOT NULL,
    uf CHAR(2) NOT NULL,
    capacidade_ml INTEGER NOT NULL CHECK (capacidade_ml >= 0),
    estoque_atual_ml INTEGER NOT NULL CHECK (estoque_atual_ml >= 0),
    contato VARCHAR(30) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uq_banco_nome_cidade UNIQUE (nome, cidade),
    CONSTRAINT ck_estoque_capacidade CHECK (estoque_atual_ml <= capacidade_ml)
);

CREATE TABLE triagem (
    id_triagem INTEGER PRIMARY KEY,
    id_doadora INTEGER NOT NULL,
    data_triagem TIMESTAMP NOT NULL,
    resultado VARCHAR(20) NOT NULL CHECK (resultado IN ('APTA','NAO_APTA','PENDENTE')),
    observacoes VARCHAR(400),
    profissional_responsavel VARCHAR(120) NOT NULL,
    FOREIGN KEY (id_doadora) REFERENCES doadora(id_doadora)
);

CREATE TABLE agendamento (
    id_agendamento INTEGER PRIMARY KEY,
    id_doadora INTEGER NOT NULL,
    id_banco INTEGER NOT NULL,
    data_hora TIMESTAMP NOT NULL,
    tipo_atendimento VARCHAR(20) NOT NULL CHECK (tipo_atendimento IN ('ENTREGA','COLETA_DOMICILIAR')),
    status VARCHAR(20) NOT NULL CHECK (status IN ('SOLICITADO','AGENDADO','CONCLUIDO','CANCELADO')),
    observacoes VARCHAR(300),
    FOREIGN KEY (id_doadora) REFERENCES doadora(id_doadora),
    FOREIGN KEY (id_banco) REFERENCES banco_leite(id_banco),
    CONSTRAINT uq_agenda_doadora_horario UNIQUE (id_doadora, data_hora)
);

CREATE TABLE doacao (
    id_doacao INTEGER PRIMARY KEY,
    id_agendamento INTEGER UNIQUE,
    id_doadora INTEGER NOT NULL,
    id_banco INTEGER NOT NULL,
    volume_ml INTEGER NOT NULL CHECK (volume_ml > 0 AND volume_ml <= 3000),
    data_coleta TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('COLETADA','RECEBIDA','PROCESSADA','DESCARTADA')),
    temperatura_recebimento NUMERIC(4,1) CHECK (temperatura_recebimento IS NULL OR temperatura_recebimento BETWEEN -5.0 AND 20.0),
    FOREIGN KEY (id_agendamento) REFERENCES agendamento(id_agendamento),
    FOREIGN KEY (id_doadora) REFERENCES doadora(id_doadora),
    FOREIGN KEY (id_banco) REFERENCES banco_leite(id_banco)
);

CREATE TABLE lote_leite (
    id_lote INTEGER PRIMARY KEY,
    id_doacao INTEGER NOT NULL,
    codigo_rastreio VARCHAR(40) NOT NULL UNIQUE,
    data_validade DATE NOT NULL,
    status_analise VARCHAR(20) NOT NULL CHECK (status_analise IN ('EM_ANALISE','APROVADO','DESCARTADO')),
    resultado_microbiologico VARCHAR(30),
    FOREIGN KEY (id_doacao) REFERENCES doacao(id_doacao)
);

CREATE TABLE notificacao (
    id_notificacao INTEGER PRIMARY KEY,
    id_usuario INTEGER NOT NULL,
    mensagem VARCHAR(500) NOT NULL,
    canal VARCHAR(20) NOT NULL CHECK (canal IN ('EMAIL','SMS','PUSH','WHATSAPP')),
    status VARCHAR(20) NOT NULL CHECK (status IN ('PENDENTE','ENVIADA','LIDA','FALHA')),
    data_envio TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE TABLE auditoria (
    id_auditoria INTEGER PRIMARY KEY,
    id_usuario INTEGER NOT NULL,
    tabela VARCHAR(60) NOT NULL,
    operacao VARCHAR(20) NOT NULL CHECK (operacao IN ('INSERT','UPDATE','DELETE','LOGIN')),
    registro_id INTEGER,
    data_hora TIMESTAMP NOT NULL,
    detalhe VARCHAR(500),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario)
);

CREATE INDEX idx_triagem_doadora ON triagem(id_doadora);
CREATE INDEX idx_agendamento_doadora ON agendamento(id_doadora);
CREATE INDEX idx_agendamento_banco ON agendamento(id_banco);
CREATE INDEX idx_agendamento_status_data ON agendamento(status, data_hora);
CREATE INDEX idx_doacao_banco_data ON doacao(id_banco, data_coleta);
CREATE INDEX idx_lote_status ON lote_leite(status_analise);
CREATE INDEX idx_notificacao_status ON notificacao(status);
CREATE INDEX idx_auditoria_data ON auditoria(data_hora);

COMMIT;
