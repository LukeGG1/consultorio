/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package empresa.consultorio;

import modelos.lote;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Milagros Taboada
 */
public class LoteController implements Initializable {

    @FXML
    private Button btnGuardar;
    @FXML
    private DatePicker txtFechaLote;
    @FXML
    private DatePicker txtFechaFabricacion;
    @FXML
    private DatePicker txtFechaVencimiento;
    @FXML
    private TextField txtCosto;
    @FXML
    private TextField txtCantidad;
    
    lote l = new lote();
    ObservableList<lote> registros;
    ObservableList<lote> registrosFiltrados;
    boolean modificar = false;

    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void guardar(ActionEvent event) {
        String fechaLoteTexto = txtFechaLote.getValue().toString(); 
        String fechaFabricacionTexto = txtFechaFabricacion.getValue().toString();
        String fechaVencimientoTexto = txtFechaVencimiento.getValue().toString();
        int costo = Integer.parseInt(txtCosto.getText());
        int cantidad = Integer.parseInt(txtCantidad.getText());
        
        l.setFechaLote(fechaLoteTexto);
        l.setFechaFabricacion(fechaFabricacionTexto);
        l.setFechaVencimiento(fechaVencimientoTexto);
        l.setCostoLote(costo);
        l.setCantidad(cantidad);
       
        if(modificar){
            if(l.modificar()){
                Alert alerta = new Alert(Alert.AlertType.INFORMATION);
                alerta.setTitle("El sistema comunica:");
                alerta.setHeaderText(null);
                alerta.setContentText("Modificado correctamente");
                alerta.show();
            }else {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                alerta.setTitle("Els sistema comunica:");
                alerta.setHeaderText(null);
                alerta.setContentText("Registro no modificado.");
                alerta.show();
            }
            modificar = false;
            
        }else{
            if (l.insertar()) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("El sistema comunica:");
            alerta.setHeaderText(null);
            alerta.setContentText("Insertado correctamente");
            alerta.show();
            }else{
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Els sistema comunica:");
            alerta.setHeaderText(null);
            alerta.setContentText("No se pudo insertar");
            alerta.show();
            
        }
        }
       
        
    }
    
}
