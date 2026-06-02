import java.sql.*;
import java.util.Scanner;

public class MainClinica {

    // ============================================================
    // CONFIGURAÇÕES DO BANCO - altere se necessário
    // ============================================================
    static final String URL     = "jdbc:mysql://localhost:3306/clinica_db";
    static final String USUARIO = "root";
    static final String SENHA   = "";   // coloque sua senha aqui

    static Connection conexao;
    static Scanner scanner = new Scanner(System.in);

    // ============================================================
    // CONEXÃO
    // ============================================================
    static void conectar() throws SQLException {
        conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
        System.out.println("Conectado ao banco de dados.");
    }

    // ============================================================
    // PACIENTES
    // ============================================================
    static void cadastrarPaciente() throws SQLException {
        System.out.print("Nome do paciente: ");
        String nome = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        PreparedStatement ps = conexao.prepareStatement("INSERT INTO paciente (nome, telefone) VALUES (?, ?)");
        ps.setString(1, nome);
        ps.setString(2, telefone);
        ps.executeUpdate();
        System.out.println("Paciente cadastrado com sucesso!");
    }

    static void listarPacientes() throws SQLException {
        ResultSet rs = conexao.createStatement().executeQuery("SELECT * FROM paciente");
        System.out.println("\n--- PACIENTES ---");
        boolean tem = false;
        while (rs.next()) {
            tem = true;
            System.out.println("ID: " + rs.getInt("id") + " | Nome: " + rs.getString("nome") + " | Tel: " + rs.getString("telefone"));
        }
        if (!tem) System.out.println("Nenhum paciente cadastrado.");
        System.out.println("-----------------\n");
    }

    // ============================================================
    // MÉDICOS
    // ============================================================
    static void cadastrarMedico() throws SQLException {
        System.out.print("Nome do médico: ");
        String nome = scanner.nextLine();
        System.out.print("Especialidade: ");
        String especialidade = scanner.nextLine();

        PreparedStatement ps = conexao.prepareStatement("INSERT INTO medico (nome, especialidade) VALUES (?, ?)");
        ps.setString(1, nome);
        ps.setString(2, especialidade);
        ps.executeUpdate();
        System.out.println("Médico cadastrado com sucesso!");
    }

    static void listarMedicos() throws SQLException {
        ResultSet rs = conexao.createStatement().executeQuery("SELECT * FROM medico");
        System.out.println("\n--- MÉDICOS ---");
        boolean tem = false;
        while (rs.next()) {
            tem = true;
            System.out.println("ID: " + rs.getInt("id") + " | Nome: " + rs.getString("nome") + " | Especialidade: " + rs.getString("especialidade"));
        }
        if (!tem) System.out.println("Nenhum médico cadastrado.");
        System.out.println("---------------\n");
    }

    // ============================================================
    // CONSULTAS
    // ============================================================
    static void agendarConsulta() throws SQLException {
        listarPacientes();
        System.out.print("ID do paciente: ");
        int pacienteId = Integer.parseInt(scanner.nextLine());

        listarMedicos();
        System.out.print("ID do médico: ");
        int medicoId = Integer.parseInt(scanner.nextLine());

        System.out.print("Data e hora (formato: AAAA-MM-DD HH:MM): ");
        String dataHora = scanner.nextLine();

        // Verifica se paciente existe
        PreparedStatement cp = conexao.prepareStatement("SELECT id FROM paciente WHERE id = ?");
        cp.setInt(1, pacienteId);
        if (!cp.executeQuery().next()) { System.out.println("Paciente não encontrado!"); return; }

        // Verifica se médico existe
        PreparedStatement cm = conexao.prepareStatement("SELECT id FROM medico WHERE id = ?");
        cm.setInt(1, medicoId);
        if (!cm.executeQuery().next()) { System.out.println("Médico não encontrado!"); return; }

        PreparedStatement ps = conexao.prepareStatement(
            "INSERT INTO consulta (data_hora, status, paciente_id, medico_id) VALUES (?, 'agendada', ?, ?)");
        ps.setString(1, dataHora);
        ps.setInt(2, pacienteId);
        ps.setInt(3, medicoId);
        ps.executeUpdate();
        System.out.println("Consulta agendada com sucesso!");
    }

    static void cancelarConsulta() throws SQLException {
        listarTodasConsultas();
        System.out.print("ID da consulta para cancelar: ");
        int id = Integer.parseInt(scanner.nextLine());

        PreparedStatement check = conexao.prepareStatement("SELECT id, status FROM consulta WHERE id = ?");
        check.setInt(1, id);
        ResultSet rs = check.executeQuery();
        if (!rs.next()) { System.out.println("Consulta não encontrada!"); return; }
        if (rs.getString("status").equals("cancelada")) { System.out.println("Consulta já está cancelada."); return; }

        PreparedStatement ps = conexao.prepareStatement("UPDATE consulta SET status = 'cancelada' WHERE id = ?");
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("Consulta cancelada com sucesso!");
    }

    static void listarConsultasPorMedico() throws SQLException {
        listarMedicos();
        System.out.print("ID do médico: ");
        int medicoId = Integer.parseInt(scanner.nextLine());

        String sql = "SELECT c.id, c.data_hora, c.status, p.nome AS paciente, m.nome AS medico "
                   + "FROM consulta c "
                   + "JOIN paciente p ON c.paciente_id = p.id "
                   + "JOIN medico m ON c.medico_id = m.id "
                   + "WHERE c.medico_id = ? ORDER BY c.data_hora";

        PreparedStatement ps = conexao.prepareStatement(sql);
        ps.setInt(1, medicoId);
        ResultSet rs = ps.executeQuery();

        System.out.println("\n--- CONSULTAS DO MÉDICO ---");
        boolean tem = false;
        while (rs.next()) {
            tem = true;
            System.out.printf("ID: %d | Data: %s | Status: %-10s | Paciente: %s%n",
                rs.getInt("id"), rs.getString("data_hora"),
                rs.getString("status"), rs.getString("paciente"));
        }
        if (!tem) System.out.println("Nenhuma consulta encontrada.");
        System.out.println("---------------------------\n");
    }

    static void listarConsultasPorPaciente() throws SQLException {
        listarPacientes();
        System.out.print("ID do paciente: ");
        int pacienteId = Integer.parseInt(scanner.nextLine());

        String sql = "SELECT c.id, c.data_hora, c.status, p.nome AS paciente, m.nome AS medico "
                   + "FROM consulta c "
                   + "JOIN paciente p ON c.paciente_id = p.id "
                   + "JOIN medico m ON c.medico_id = m.id "
                   + "WHERE c.paciente_id = ? ORDER BY c.data_hora";

        PreparedStatement ps = conexao.prepareStatement(sql);
        ps.setInt(1, pacienteId);
        ResultSet rs = ps.executeQuery();

        System.out.println("\n--- CONSULTAS DO PACIENTE ---");
        boolean tem = false;
        while (rs.next()) {
            tem = true;
            System.out.printf("ID: %d | Data: %s | Status: %-10s | Médico: %s%n",
                rs.getInt("id"), rs.getString("data_hora"),
                rs.getString("status"), rs.getString("medico"));
        }
        if (!tem) System.out.println("Nenhuma consulta encontrada.");
        System.out.println("-----------------------------\n");
    }

    static void listarTodasConsultas() throws SQLException {
        String sql = "SELECT c.id, c.data_hora, c.status, p.nome AS paciente, m.nome AS medico "
                   + "FROM consulta c "
                   + "JOIN paciente p ON c.paciente_id = p.id "
                   + "JOIN medico m ON c.medico_id = m.id "
                   + "ORDER BY c.data_hora";

        ResultSet rs = conexao.createStatement().executeQuery(sql);
        System.out.println("\n--- TODAS AS CONSULTAS ---");
        boolean tem = false;
        while (rs.next()) {
            tem = true;
            System.out.printf("ID: %d | Data: %s | Status: %-10s | Paciente: %-20s | Médico: %s%n",
                rs.getInt("id"), rs.getString("data_hora"),
                rs.getString("status"), rs.getString("paciente"), rs.getString("medico"));
        }
        if (!tem) System.out.println("Nenhuma consulta cadastrada.");
        System.out.println("--------------------------\n");
    }

    // ============================================================
    // MENU PRINCIPAL
    // ============================================================
    public static void main(String[] args) {
        try {
            conectar();

            int opcao;
            do {
                System.out.println("==============================");
                System.out.println("  SISTEMA DE AGENDAMENTO");
                System.out.println("==============================");
                System.out.println("--- Pacientes ---");
                System.out.println("1. Cadastrar Paciente");
                System.out.println("2. Listar Pacientes");
                System.out.println("--- Médicos ---");
                System.out.println("3. Cadastrar Médico");
                System.out.println("4. Listar Médicos");
                System.out.println("--- Consultas ---");
                System.out.println("5. Agendar Consulta");
                System.out.println("6. Cancelar Consulta");
                System.out.println("7. Listar Consultas por Médico");
                System.out.println("8. Listar Consultas por Paciente");
                System.out.println("9. Listar Todas as Consultas");
                System.out.println("0. Sair");
                System.out.print("Escolha: ");

                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1: cadastrarPaciente();          break;
                    case 2: listarPacientes();            break;
                    case 3: cadastrarMedico();            break;
                    case 4: listarMedicos();              break;
                    case 5: agendarConsulta();            break;
                    case 6: cancelarConsulta();           break;
                    case 7: listarConsultasPorMedico();   break;
                    case 8: listarConsultasPorPaciente(); break;
                    case 9: listarTodasConsultas();       break;
                    case 0: System.out.println("Saindo..."); break;
                    default: System.out.println("Opção inválida.");
                }

            } while (opcao != 0);

            conexao.close();

        } catch (SQLException e) {
            System.err.println("Erro de banco de dados: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Valor inválido digitado: " + e.getMessage());
        }
    }
}
