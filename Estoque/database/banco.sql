-- ============================================
-- Exercício 2 - Controle de Estoque com Categorias
-- Script para criação do banco de dados MySQL
-- ============================================

CREATE DATABASE IF NOT EXISTS estoque_db;
USE estoque_db;

-- Tabela de Categorias
CREATE TABLE IF NOT EXISTS categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

-- Tabela de Produtos (com chave estrangeira para Categoria)
CREATE TABLE IF NOT EXISTS produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    quantidade INT NOT NULL DEFAULT 0,
    preco DECIMAL(10, 2) NOT NULL,
    categoria_id INT NOT NULL,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);
