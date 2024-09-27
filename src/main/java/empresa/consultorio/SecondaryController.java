package empresa.consultorio;

import clases.reporte;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import javafx.stage.Stage;

public class SecondaryController implements Initializable {

    @FXML
    private Button btnPacientes;
    @FXML
    private Button btnProductos;
    @FXML
    private Button btnContabilidad;
    @FXML
    private Button btnAyuda;
    @FXML
    private Button btnFactura;
    @FXML
    private JFXButton imgCerrar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Inicializar el menú como cerrado
        // TODO
    }

    @FXML
    public void switchToPacientes(ActionEvent event) {
        try {
            App.switchView("/empresa/consultorio/pacientesPrincipal.fxml", "Pacientes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToProductos(ActionEvent event) {
        try {
            App.switchView("/empresa/consultorio/VerProducto.fxml", "Productos");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToContabilidad(ActionEvent event) {
        try {
            App.switchView("/empresa/consultorio/Contabilidad.fxml", "Contabilidad");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToAyuda(ActionEvent event) throws IOException {
        reporte r = new reporte ();
        r.abrirAyuda();
    }

    @FXML
    private void switchToFactura(ActionEvent event) {
        try {
            App.switchView("/empresa/consultorio/FacturaPrincipal_1.fxml", "Factura Principal");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCerrar(ActionEvent event) {
        Platform.exit(); // Cierra la aplicación
    }
    
   
}
