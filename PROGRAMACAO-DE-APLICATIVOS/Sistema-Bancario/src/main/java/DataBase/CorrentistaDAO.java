package DataBase;

import org.example.SessionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CorrentistaDAO {
   public static boolean Login(String email, String senha) {
       String sql = "SELECT cb.id_conta, c.nome  FROM Correntista c " +
                    "JOIN ContaBancaria cb ON c.id_usuario = cb.id_usuario " +
                    "WHERE c.email = ? AND c.senha = ?";

        try {
            PreparedStatement ps = Conexao.getConexao().prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, senha);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                SessionManager.setIdConta(rs.getInt("id_conta"));
                SessionManager.setNome(rs.getString("nome"));
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Erro: " + e.getMessage());
        }
        return false;

    }

    public static boolean Cadastro(String nome, String cpf, String senha, String email){
        if (nome.isEmpty() || cpf.isEmpty() || senha.isEmpty() || email.isEmpty()) {
            return false;}

       String sql = "INSERT INTO Correntista(nome, cpf, senha, email) VALUES (?, ?, ?, ?)";
        try {

            PreparedStatement ps = Conexao.getConexao().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, nome);
            ps.setString(2, cpf);
            ps.setString(3, senha);
            ps.setString(4, email);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int idUsuario = rs.getInt(1);

                // cria a conta bancária para o novo correntista
                String sqlConta = "INSERT INTO ContaBancaria(criado_em, id_usuario, ativa, saldo_atual) " +
                        "VALUES (NOW(), ?, TRUE, 0.00)";
                PreparedStatement psConta = Conexao.getConexao().prepareStatement(sqlConta);
                psConta.setInt(1, idUsuario);
                psConta.executeUpdate();
            }

            return true;

        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}
