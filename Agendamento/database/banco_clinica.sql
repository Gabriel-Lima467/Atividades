-- ============================================
-- Exercício 3 - Sistema de Agendamento
-- Script para criação do banco de dados MySQL
-- ============================================

CREATE DATABASE IF NOT EXISTS clinica_db;
USE clinica_db;

-- Tabela de Pacientes
CREATE TABLE IF NOT EXISTS paciente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(20)
);

-- Tabela de Médicos
CREATE TABLE IF NOT EXISTS medico (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    especialidade VARCHAR(100)
);

-- Tabela de Consultas (relaciona Paciente e Médico)
CREATE TABLE IF NOT EXISTS consulta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    data_hora DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'agendada',
    paciente_id INT NOT NULL,
    medico_id INT NOT NULL,
    FOREIGN KEY (paciente_id) REFERENCES paciente(id),
    FOREIGN KEY (medico_id) REFERENCES medico(id)
);
