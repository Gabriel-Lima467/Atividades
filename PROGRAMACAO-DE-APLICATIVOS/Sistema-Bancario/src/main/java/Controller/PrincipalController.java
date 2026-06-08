package Controller;

import DataBase.ProcedureExecutor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.SessionManager;

public class PrincipalController {

    @FXML private Label bemVindoLabel;

    @FXML
    public void initialize() {
        bemVindoLabel.setText("Olá, " + SessionManager.getNome() + "!");
    }

    @FXML
    private void handleDeposito() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Depósito");
        dialog.setContentText("Valor a depositar:");
        dialog.showAndWait().ifPresent(valor -> {
            ProcedureExecutor.Operacoes(
                    new java.math.BigDecimal(valor),
                    SessionManager.getIdConta(),
                    "DEPOSITO"
            );
            mostrarSucesso("Depósito realizado com sucesso!");
        });
    }

    @FXML
    private void handleSaque() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Saque");
        dialog.setContentText("Valor a sacar:");
        dialog.showAndWait().ifPresent(valor -> {
            ProcedureExecutor.Operacoes(
                    new java.math.BigDecimal(valor),
                    SessionManager.getIdConta(),
                    "SAQUE"
            );
            mostrarSucesso("Saque realizado com sucesso!");
        });
    }

    @FXML
    private void handleConsulta() {
        ProcedureExecutor.Operacoes(
                java.math.BigDecimal.ZERO,
                SessionManager.getIdConta(),
                "CONSULTA"
        );
    }

    @FXML
    private void handleExtrato() {
        System.out.println("Extrato - a implementar");
    }

    @FXML
    private void handleSair() {
        try {
            SessionManager.limparSessao();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) bemVindoLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}