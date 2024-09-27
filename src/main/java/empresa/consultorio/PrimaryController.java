package empresa.consultorio;

import clases.HashPassword;
import static clases.Utilities.showAlert;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import modelos.Usuario;

public class PrimaryController {

    @FXML
    private Button btnIniciarSesion;
    @FXML
    private Button btnRegistrarse;
    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtContra;
    @FXML
    private Button btnIniciar;
    Usuario user = new Usuario();
    @FXML
    private JFXButton imgCerrar;

   
    @FXML
    private void login(ActionEvent event) {
        try {
            String enteredUsername = txtUsuario.getText();
            String enteredPassword = txtContra.getText();

            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Advertencia", "Por favor ingrese ambos Usuario y Contraseña.");
                return;
            }

            user.setUsername(enteredUsername);
            user.setPassword(enteredPassword);

            Optional<String> storedPassword = user.findPassword();

            if (storedPassword.isPresent() && HashPassword.verify(enteredPassword, storedPassword.get())) {
                App.setRoot("/empresa/consultorio/Secondary.fxml", "Menu");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Nombre de Usuario o Contraseña incorrectos.");
            }

        } catch (IOException e) {
            Logger.getLogger(PrimaryController.class.getName()).log(Level.SEVERE, null, e);
            showAlert(Alert.AlertType.ERROR, "Error", "Ocurrio un error al tratar de ingresar.");
        }
    }    

    private void register(ActionEvent event) {
        String enteredUsername = txtUsuario.getText();
        String enteredPassword = txtContra.getText();
            
        user.setUsername(enteredUsername);
        user.setPassword(enteredPassword);
        user.setNivel(1);
        
        if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Advertencia", "Por favor ingrese ambos Usuario y Contraseña.");
            return;
        }

        user.insert();
        showAlert(Alert.AlertType.INFORMATION, "Proceso exitoso", "Se ha añadido correctamente el usuario");
    }
    @FXML
    private void switchToRegister(ActionEvent event) {
        btnIniciar.setOnAction(e -> register(e));
        btnIniciar.setText("Registrar");
        aplicarAnimacionYEstilo(btnIniciarSesion, true);
        aplicarAnimacionYEstilo(btnRegistrarse, false);
    }
    
    @FXML
    private void switchToLogin(ActionEvent event) {
        btnIniciar.setOnAction(e -> login(e));
        btnIniciar.setText("Iniciar");
        aplicarAnimacionYEstilo(btnIniciarSesion, false);
        aplicarAnimacionYEstilo(btnRegistrarse, true);
    }
    

    private void aplicarAnimacionYEstilo(Button button, boolean isSelected) {
    ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), button);

    if (isSelected) {
        button.getStyleClass().remove("btnbtnDeLogin");
        button.getStyleClass().add("btnDeLoginActivo");
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
    } else {
        button.getStyleClass().remove("btnDeLoginActivo");
        button.getStyleClass().add("btnDeLogin");
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
    }

    scaleTransition.play();
    
}
    @FXML
    private void handleCerrar(ActionEvent event) {
        Platform.exit(); // Cierra la aplicación
    }
}
