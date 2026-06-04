DROP DATABASE IF EXISTS bancoBlend;
CREATE DATABASE bancoBlend;

USE bancoBlend;

-- ======= Tabela do Usuario ======

CREATE TABLE Correntista(
id_usuario INT PRIMARY KEY AUTO_INCREMENT,
nome VARCHAR(100) NOT NULL,
cpf VARCHAR(100) NOT NULL,
senha VARCHAR(150) NOT NULL,
email VARCHAR(150) NOT NULL
);

-- ======== Tabela da Conta Bancária ======
CREATE TABLE ContaBancaria(
id_conta INT PRIMARY KEY AUTO_INCREMENT,
criado_em TIMESTAMP,
id_usuario INT,
ativa BOOLEAN,
saldo_atual DECIMAL(10,2),

FOREIGN KEY (id_usuario) REFERENCES Correntista(id_usuario)
);

CREATE TABLE Transacoes(
valor_op DECIMAL(10,2) NOT NULL,
tipo ENUM('DEPOSITO', 'SAQUE','CONSULTA'),
id_conta INT,
data_op TIMESTAMP,

FOREIGN KEY (id_conta) REFERENCES ContaBancaria(id_conta)
);


CREATE VIEW Extrato AS
    SELECT c.nome, cb.id_conta, t.tipo, t.valor_op, t.data_op
    FROM Correntista c
    JOIN ContaBancaria cb ON c.id_usuario = cb.id_usuario
    JOIN Transacoes t ON cb.id_conta = t.id_conta;