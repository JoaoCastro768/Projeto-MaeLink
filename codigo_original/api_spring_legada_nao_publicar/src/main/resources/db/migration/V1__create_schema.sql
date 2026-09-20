CREATE TABLE doadoras (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    contato VARCHAR(160) NOT NULL,
    cidade VARCHAR(120) NOT NULL,
    consentimento_lgpd BOOLEAN NOT NULL
);

CREATE TABLE triagens (
    id BIGSERIAL PRIMARY KEY,
    doadora_id BIGINT NOT NULL REFERENCES doadoras(id),
    respostas TEXT NOT NULL,
    resultado VARCHAR(40) NOT NULL,
    criado_em TIMESTAMP NOT NULL
);

CREATE TABLE bancos_leite (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(160) NOT NULL,
    endereco VARCHAR(220) NOT NULL,
    area_atendida VARCHAR(180) NOT NULL,
    capacidade INTEGER NOT NULL CHECK (capacidade >= 0)
);

CREATE TABLE agendamentos (
    id BIGSERIAL PRIMARY KEY,
    doadora_id BIGINT NOT NULL REFERENCES doadoras(id),
    banco_id BIGINT NOT NULL REFERENCES bancos_leite(id),
    data TIMESTAMP NOT NULL,
    status VARCHAR(40) NOT NULL,
    tipo_atendimento VARCHAR(40) NOT NULL,
    observacao VARCHAR(300)
);

CREATE TABLE doacoes (
    id BIGSERIAL PRIMARY KEY,
    agendamento_id BIGINT NOT NULL UNIQUE REFERENCES agendamentos(id),
    volume NUMERIC(10,2),
    status VARCHAR(40) NOT NULL,
    timeline TEXT NOT NULL
);

CREATE TABLE notificacoes (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    canal VARCHAR(30) NOT NULL,
    mensagem VARCHAR(500) NOT NULL,
    situacao VARCHAR(30) NOT NULL,
    criado_em TIMESTAMP NOT NULL
);

CREATE TABLE auditorias (
    id BIGSERIAL PRIMARY KEY,
    usuario VARCHAR(160) NOT NULL,
    acao VARCHAR(300) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    ip_origem VARCHAR(64) NOT NULL
);

CREATE INDEX idx_triagem_doadora ON triagens(doadora_id);
CREATE INDEX idx_agendamento_doadora ON agendamentos(doadora_id);
CREATE INDEX idx_agendamento_banco ON agendamentos(banco_id);
CREATE INDEX idx_notificacao_usuario ON notificacoes(usuario_id);
