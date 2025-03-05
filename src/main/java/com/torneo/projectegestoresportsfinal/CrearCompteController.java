package com.torneo.projectegestoresportsfinal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class CrearCompteController {

    @FXML
    private TextField newUsernameField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label errorMessage;

    @FXML
    private void handleCreateAccount(ActionEvent event) throws IOException {
        String username = newUsernameField.getText();
        String password = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorMessage.setText("Tots els camps són obligatoris.");
            errorMessage.setVisible(true);
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorMessage.setText("Les contrasenyes no coincideixen.");
            errorMessage.setVisible(true);
            return;
        }

        try {
            // Guarda l'usuari a la base de dades simulada (usersDatabase)
            String hashedPassword = PasswordUtils.hashPassword(password);
            LoginController.usersDatabase.put(username, hashedPassword);

            // Torna a la pantalla de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
            Stage stage = (Stage) newUsernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            errorMessage.setText("Error en crear l'usuari.");
            errorMessage.setVisible(true);
        }
    }

    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/LoginView.fxml"));
        Stage stage = (Stage) newUsernameField.getScene().getWindow();
        stage.setScene(new Scene(loader.load()));
    }
}
