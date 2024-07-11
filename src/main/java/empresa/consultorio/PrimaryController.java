package empresa.consultorio;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelos.usuario;

public class PrimaryController {

    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtContra;
    @FXML
    private Button btnIniciar;
    usuario u = new usuario();

    @FXML
    public void iniciar(ActionEvent event) {
        try {
            if (txtUsuario.getText().isEmpty() || txtContra.getText().isEmpty()) {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("El sistema comunica:");
                alerta.setHeaderText(null);
                alerta.setContentText("Llene todos los espacios");
                alerta.show();
            } else {
                // Realizar la autenticación
                boolean autenticado = u.loginAccount(txtUsuario.getText(), txtContra.getText());
                if (autenticado) {
                    Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                    alerta.setTitle("El sistema comunica:");
                    alerta.setHeaderText(null);
                    alerta.setContentText("Se ha ingresado correctamente");
                    alerta.show();

                    // Cargar y mostrar la nueva escena
                    btnIniciar.getScene().getWindow().hide();
                    
                    Parent root = FXMLLoader.load(getClass().getResource("/empresa/consultorio/secondary"));
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                } else {
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("El sistema comunica:");
                    alerta.setHeaderText(null);
                    alerta.setContentText("El nombre o la contraseña son incorrectos");
                    alerta.show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
