package Controller;

import DataBase.CorrentistaDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class CadastroController {

    @FXML private TextField nomeField;
    @FXML private TextField cpfField;
    @FXML private TextField emailField;
    @FXML private PasswordField senhaField;

    @FXML
    private void handleCadastro() {
        String nome = nomeField.getText();
        String cpf = cpfField.getText();
        String email = emailField.getText();
        String senha = senhaField.getText();

        if (CorrentistaDAO.Cadastro(nome, cpf, senha, email)){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
                Scene scene = new Scene(loader.load());

                Stage stage = (Stage) nomeField.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                System.err.println("Erro ao carregar tela: " + e.getMessage());
            }
        }
    }

    @FXML
    private void irParaLogin() {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
                Scene scene = new Scene(loader.load());

                Stage stage = (Stage) nomeField.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                System.err.println("Erro ao carregar tela: " + e.getMessage());

        }
    }
}