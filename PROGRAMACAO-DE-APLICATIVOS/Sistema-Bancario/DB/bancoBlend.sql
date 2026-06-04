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

-- ========== CRIAÇÃO DOS PROCEDIMENTOS QUE ACONTECERÃO ========

	DELIMITER $$

	DROP PROCEDURE IF EXISTS proc_operacoes$$

    CREATE PROCEDURE proc_operacoes (
    IN p_valor_op DECIMAL(10,2),
    IN p_id_conta INT,
    IN p_tipo_op ENUM('DEPOSITO', 'SAQUE','CONSULTA')
    )
    BEGIN
		DECLARE v_saldo DECIMAL(10,2);

        SELECT saldo_atual INTO v_saldo
		FROM ContaBancaria WHERE id_conta = p_id_conta;

        IF p_tipo_op = 'DEPOSITO' THEN
			IF p_valor_op > 0 THEN
			UPDATE ContaBancaria SET saldo_atual = saldo_atual + p_valor_op
			WHERE id_conta = p_id_conta;

			INSERT INTO Transacoes (valor_op, tipo, id_conta, data_op)
			VALUES (p_valor_op, p_tipo_op, p_id_conta, NOW());

			ELSE
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Saldo insuficiente';
            END IF;

		ELSEIF p_tipo_op = 'SAQUE' THEN
			IF p_valor_op <= v_saldo THEN
			UPDATE ContaBancaria SET saldo_atual = saldo_atual - p_valor_op
			WHERE id_conta = p_id_conta;

            INSERT INTO Transacoes (valor_op, tipo, id_conta, data_op)
			VALUES (p_valor_op, p_tipo_op, p_id_conta, NOW());

            ELSE
			SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Saldo insuficiente';
            END IF;

		ELSEIF p_tipo_op = 'CONSULTA' THEN
			SELECT saldo_atual FROM ContaBancaria
            WHERE id_conta = p_id_conta;
			END IF;
END$$
DELIMITER ;