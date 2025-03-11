package com.torneo.projectegestoresportsfinal.controladors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorMessage;

    // "Base de datos" de usuarios con contraseñas encriptadas para pruebas
    public static Map<String, String> usersDatabase = new HashMap<>();

    public LoginController() {
        // Aquí añadimos usuarios con contraseñas encriptadas (ejemplo de contraseñas)
        try {
            usersDatabase.put("admin", PasswordUtils.hashPassword("admin")); // Usuario admin con contraseña encriptada

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    // Manejador de login
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Verifica si el usuario existe y la contraseña es correcta
        if (validateLogin(username, password)) {
            // Si el login es exitoso, abre la ventana principal (MainView.fxml)
            try {
                // Cargar el archivo FXML de la vista MainView
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
                Parent root = loader.load();

                // Crear una nueva ventana (Stage) para la vista MainView
                Stage stage = new Stage();
                stage.setTitle("Gestor d'Esports");
                stage.setScene(new Scene(root));

                // Mostrar la ventana
                stage.show();

                // Cerrar la ventana de login (opcional)
                ((Stage) usernameField.getScene().getWindow()).close();

            } catch (IOException e) {
                e.printStackTrace();
                // Si ocurre algún error al cargar el FXML, lo imprimimos en consola
            }

        } else {
            // Si el login falla, muestra el mensaje de error
            errorMessage.setText("Usuario o contraseña incorrectos");
            errorMessage.setVisible(true);
        }
    }



    // Método de validación de login
    private boolean validateLogin(String username, String password) {
        try {
            String storedPasswordHash = usersDatabase.get(username);
            return storedPasswordHash != null && PasswordUtils.verifyPassword(password, storedPasswordHash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }


    public void handleCreateAccount(ActionEvent actionEvent) {
        try {
            // Cargar el archivo FXML de la vista CrearCompteView
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CrearCompteView.fxml"));
            Parent root = loader.load();

            // Crear una nueva ventana (Stage) para la vista CrearCompteView
            Stage stage = new Stage();
            stage.setTitle("Crear Cuenta");
            stage.setScene(new Scene(root));

            // Mostrar la ventana
            stage.show();

            // Opcional: Si quieres cerrar la ventana actual (login), descomenta la siguiente línea
            // ((Stage) usernameField.getScene().getWindow()).close();

            // Si ocurre algún error al cargar el FXML, lo imprimimos en consola
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
