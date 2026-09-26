
-- Criar tabelas de segurança
CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    nome_completo VARCHAR(200) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS usuario_perfis (
    usuario_id BIGINT NOT NULL,
    perfil VARCHAR(50) NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS produtos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(500) NOT NULL,
    preco NUMERIC(10,2) NOT NULL,
    quantidade INTEGER NOT NULL,
    data_criacao TIMESTAMP,
    data_atualizacao TIMESTAMP
);

-- Inserir dados de exemplo
INSERT INTO produtos (nome, descricao, preco, quantidade, data_criacao, data_atualizacao)
VALUES
    ('Notebook Dell', 'Notebook Dell Inspiron 15, 16GB RAM, 512GB SSD', 4500.00, 10, NOW(), NOW()),
    ('Mouse Logitech', 'Mouse sem fio Logitech MX Master 3', 350.00, 50, NOW(), NOW()),
    ('Teclado Mecânico', 'Teclado mecânico Redragon com RGB', 280.00, 30, NOW(), NOW())
ON CONFLICT DO NOTHING;
