/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package empresa.consultorio;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


// Referencia al escenario principal
    

/**
 * FXML Controller class
 *
 * @author Milagros Taboada
 */
public class InventarioController implements Initializable {

    @FXML
    private TableView<?> tblLote;
    @FXML
    private TableColumn<?, ?> colProducto;
    @FXML
    private TableColumn<?, ?> colCantidad;
    @FXML
    private TableColumn<?, ?> colLote;
    @FXML
    private TableColumn<?, ?> colVencimiento;
    @FXML
    private Button btnAñadir;
    
    
    private Stage stage;

    // Método para establecer el escenario
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    


    @FXML
    private void mostrarFila(MouseEvent event) {
    }

    @FXML
    private void Añadir(ActionEvent event) {
        abrirFxml("Producto.fxml","Formulario Cliente");
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
            Logger.getLogger(InventarioController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
