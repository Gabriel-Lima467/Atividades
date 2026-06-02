import java.sql.*;
import java.util.Scanner;

public class Main {

    // ============================================================
    // CONFIGURAÇÕES DO BANCO DE DADOS - altere se necessário
    // ============================================================
    static final String URL    = "jdbc:mysql://localhost:3306/estoque_db";
    static final String USUARIO = "root";
    static final String SENHA   = "";          // coloque sua senha aqui

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
    // CATEGORIAS
    // ============================================================
    static void cadastrarCategoria() throws SQLException {
        System.out.print("Nome da categoria: ");
        String nome = scanner.nextLine();

        String sql = "INSERT INTO categoria (nome) VALUES (?)";
        PreparedStatement ps = conexao.prepareStatement(sql);
        ps.setString(1, nome);
        ps.executeUpdate();
        System.out.println("Categoria cadastrada com sucesso!");
    }

    static void listarCategorias() throws SQLException {
        String sql = "SELECT * FROM categoria";
        Statement st = conexao.createStatement();
        ResultSet rs = st.executeQuery(sql);

        System.out.println("\n--- CATEGORIAS ---");
        boolean temRegistro = false;
        while (rs.next()) {
            temRegistro = true;
            System.out.println("ID: " + rs.getInt("id") + " | Nome: " + rs.getString("nome"));
        }
        if (!temRegistro) {
            System.out.println("Nenhuma categoria cadastrada.");
        }
        System.out.println("------------------\n");
    }

    // ============================================================
    // PRODUTOS
    // ============================================================
    static void cadastrarProduto() throws SQLException {
        listarCategorias();

        System.out.print("ID da categoria do produto: ");
        int categoriaId = Integer.parseInt(scanner.nextLine());

        // Verifica se a categoria existe
        PreparedStatement check = conexao.prepareStatement("SELECT id FROM categoria WHERE id = ?");
        check.setInt(1, categoriaId);
        ResultSet rs = check.executeQuery();
        if (!rs.next()) {
            System.out.println("Categoria não encontrada!");
            return;
        }

        System.out.print("Nome do produto: ");
        String nome = scanner.nextLine();

        System.out.print("Quantidade em estoque: ");
        int quantidade = Integer.parseInt(scanner.nextLine());

        System.out.print("Preço: ");
        double preco = Double.parseDouble(scanner.nextLine());

        String sql = "INSERT INTO produto (nome, quantidade, preco, categoria_id) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conexao.prepareStatement(sql);
        ps.setString(1, nome);
        ps.setInt(2, quantidade);
        ps.setDouble(3, preco);
        ps.setInt(4, categoriaId);
        ps.executeUpdate();

        System.out.println("Produto cadastrado com sucesso!");
    }

    static void listarProdutosPorCategoria() throws SQLException {
        listarCategorias();

        System.out.print("ID da categoria para listar produtos: ");
        int categoriaId = Integer.parseInt(scanner.nextLine());

        String sql = "SELECT p.id, p.nome, p.quantidade, p.preco, c.nome AS categoria "
                + "FROM produto p "
                + "JOIN categoria c ON p.categoria_id = c.id "
                + "WHERE p.categoria_id = ?";

        PreparedStatement ps = conexao.prepareStatement(sql);
        ps.setInt(1, categoriaId);
        ResultSet rs = ps.executeQuery();

        System.out.println("\n--- PRODUTOS DA CATEGORIA ---");
        boolean temRegistro = false;
        while (rs.next()) {
            temRegistro = true;
            System.out.printf("ID: %d | Nome: %-20s | Qtd: %d | Preço: R$ %.2f | Categoria: %s%n",
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getInt("quantidade"),
                    rs.getDouble("preco"),
                    rs.getString("categoria"));
        }
        if (!temRegistro) {
            System.out.println("Nenhum produto nessa categoria.");
        }
        System.out.println("-----------------------------\n");
    }

    static void atualizarEstoque() throws SQLException {
        System.out.print("ID do produto para atualizar estoque: ");
        int id = Integer.parseInt(scanner.nextLine());

        // Verifica se o produto existe e mostra informações atuais
        PreparedStatement check = conexao.prepareStatement("SELECT * FROM produto WHERE id = ?");
        check.setInt(1, id);
        ResultSet rs = check.executeQuery();
        if (!rs.next()) {
            System.out.println("Produto não encontrado!");
            return;
        }
        System.out.println("Produto: " + rs.getString("nome") + " | Estoque atual: " + rs.getInt("quantidade"));

        System.out.print("Nova quantidade em estoque: ");
        int novaQuantidade = Integer.parseInt(scanner.nextLine());

        String sql = "UPDATE produto SET quantidade = ? WHERE id = ?";
        PreparedStatement ps = conexao.prepareStatement(sql);
        ps.setInt(1, novaQuantidade);
        ps.setInt(2, id);
        ps.executeUpdate();

        System.out.println("Estoque atualizado com sucesso!");
    }

    static void listarTodosProdutos() throws SQLException {
        String sql = "SELECT p.id, p.nome, p.quantidade, p.preco, c.nome AS categoria "
                + "FROM produto p "
                + "JOIN categoria c ON p.categoria_id = c.id "
                + "ORDER BY c.nome, p.nome";

        Statement st = conexao.createStatement();
        ResultSet rs = st.executeQuery(sql);

        System.out.println("\n--- TODOS OS PRODUTOS ---");
        boolean temRegistro = false;
        while (rs.next()) {
            temRegistro = true;
            System.out.printf("ID: %d | Nome: %-20s | Qtd: %d | Preço: R$ %.2f | Categoria: %s%n",
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getInt("quantidade"),
                    rs.getDouble("preco"),
                    rs.getString("categoria"));
        }
        if (!temRegistro) {
            System.out.println("Nenhum produto cadastrado.");
        }
        System.out.println("-------------------------\n");
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
                System.out.println("  CONTROLE DE ESTOQUE");
                System.out.println("==============================");
                System.out.println("1. Cadastrar Categoria");
                System.out.println("2. Listar Categorias");
                System.out.println("3. Cadastrar Produto");
                System.out.println("4. Listar Produtos por Categoria");
                System.out.println("5. Listar Todos os Produtos");
                System.out.println("6. Atualizar Estoque de Produto");
                System.out.println("0. Sair");
                System.out.print("Escolha: ");

                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1: cadastrarCategoria();        break;
                    case 2: listarCategorias();          break;
                    case 3: cadastrarProduto();          break;
                    case 4: listarProdutosPorCategoria();break;
                    case 5: listarTodosProdutos();       break;
                    case 6: atualizarEstoque();          break;
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