-- Criar banco de dados
CREATE DATABASE api_db;

-- Conectar ao banco de dados
\c api_db;

-- Criar tabela de produtos (se não usar o ddl-auto: update)
CREATE TABLE IF NOT EXISTS produtos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(500) NOT NULL,
    preco NUMERIC(10,2) NOT NULL,
    quantidade INTEGER NOT NULL,
    data_criacao TIMESTAMP,
    data_atualizacao TIMESTAMP
);

-- Inserir alguns dados de exemplo
INSERT INTO produtos (nome, descricao, preco, quantidade, data_criacao, data_atualizacao) 
VALUES 
    ('Notebook Dell', 'Notebook Dell Inspiron 15, 16GB RAM, 512GB SSD', 4500.00, 10, NOW(), NOW()),
    ('Mouse Logitech', 'Mouse sem fio Logitech MX Master 3', 350.00, 50, NOW(), NOW()),
    ('Teclado Mecânico', 'Teclado mecânico Redragon com RGB', 280.00, 30, NOW(), NOW())
ON CONFLICT DO NOTHING;
