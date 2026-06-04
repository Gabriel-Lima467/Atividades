package Controller;

import DataBase.CorrentistaDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField senhaField;

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String senha = senhaField.getText();

        if (CorrentistaDAO.Login(email, senha)){
            System.out.println("Login bem sucedido!");
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setContentText("Email ou senha incorretos!");
            alert.showAndWait();;
        }
    }
    @FXML
    private void irParaCadastro(){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/cadastro.fxml"));
                Scene scene = new Scene(loader.load());

                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                System.err.println("Erro ao carregar tela: " + e.getMessage());

        }
    }
}