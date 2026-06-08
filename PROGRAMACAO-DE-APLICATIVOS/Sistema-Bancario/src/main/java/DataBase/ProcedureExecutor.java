package DataBase;

import javafx.scene.control.Alert;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class ProcedureExecutor {

    public static void Operacoes(BigDecimal valor, int idConta, String tipo) {
        try (CallableStatement conn = Conexao.getConexao().prepareCall("{CALL proc_operacoes(?, ?, ?)}")) {

            conn.setBigDecimal(1, valor);
            conn.setInt(2, idConta);
            conn.setString(3, tipo);
            conn.execute();

        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setContentText("Email ou senha incorretos!");
            alert.showAndWait();
        }
    }
}
