/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package empresa.consultorio;

import modelos.Producto;
import com.jfoenix.controls.JFXButton;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modelos.Lote;

/**
 * FXML Controller class
 *
 * @author Milagros Taboada
 */
public class ProductoController {

    @FXML
    private JFXButton btnLote;
    @FXML
    private Button btnProducto;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextArea txtDescripcion;

    private final FileChooser fc = new FileChooser();

    Producto p = new Producto();
    ObservableList<Producto> registros;
    ObservableList<Producto> registrosFiltrados;
    boolean modificar = false;
    @FXML
    private JFXButton imgCerrar;

    /**
     * Initializes the controller class.
     */
    @FXML
    private void añadirLote(ActionEvent event) {
        abrirFxml("lote.fxml", "Formulario Cliente");
        btnLote.getScene().getWindow().hide();
    }

    private void abrirFxml(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ProductoController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void guardarProducto(ActionEvent event) {
        String nombre = txtNombre.getText();
        int precio = Integer.parseInt(txtPrecio.getText());
        String descripcion = txtDescripcion.getText();

        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setDescripcion(descripcion);

        if (modificar) {
            if (p.modificar()) {
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("El sistema comunica:");
                alerta.setHeaderText(null);
                alerta.setContentText("Modificado correctamente");
                alerta.show();
                handleCerrar(null);

            } else {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("El sistema comunica:");
                alerta.setHeaderText(null);
                alerta.setContentText("Registro no modificado.");
                alerta.show();
            }
            modificar = false;

        } else {
            if (p.insertar()) {
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setTitle("El sistema comunica:");
                alerta.setHeaderText(null);
                alerta.setContentText("Insertado correctamente");
                alerta.show();
                handleCerrar(null);
            } else {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Els sistema comunica:");
                alerta.setHeaderText(null);
                alerta.setContentText("No se pudo insertar");
                alerta.show();
                

            }

        }
    }

    @FXML
    private void handleCerrar(ActionEvent event) {
        Stage stage = (Stage) btnProducto.getScene().getWindow();
        stage.close();
    }
}
