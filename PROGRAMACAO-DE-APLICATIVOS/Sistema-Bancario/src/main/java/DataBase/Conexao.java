package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    private static final String URL     = "jdbc:mysql://localhost:3306/bancoBlend";
    private static final String USUARIO = "root";
    private static final String SENHA   = "";
    private static Connection conexaoAtiva = null;

    private Conexao() {}

    public static Connection getConexao() {
        try {

            if (conexaoAtiva == null || conexaoAtiva.isClosed()) {
                conexaoAtiva = DriverManager.getConnection(URL, USUARIO, SENHA);
                System.out.println("Conexao com o banco estabelecida!");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco: " + e.getMessage());
        }
        return conexaoAtiva;
    }

    public static void fecharConexao() {
        try {
            if (conexaoAtiva != null && !conexaoAtiva.isClosed()) {
                conexaoAtiva.close();
                System.out.println("Conexao encerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Erro ao fechar conexao: " + e.getMessage());
        }
    }
}

