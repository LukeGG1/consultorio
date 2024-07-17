/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package empresa.consultorio;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import modelos.lote;
import modelos.producto;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Milagros Taboada
 */
public class ProductoEditarController implements Initializable {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtPrecio;
    @FXML
    private TextArea txtDescripcion;
    @FXML
    private Button btnModificarProducto;
    @FXML
    private Button btnEliminar;
    
    private producto p = new producto();
    private ObservableList<String> productosList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productosList = FXCollections.observableArrayList();
        cargarNombresProductos(productosList);
        configurarAutocompletado(txtNombre, productosList);
        // TODO
    }  
    
    public void initData(int idProducto, producto p) {
        this.p = p;  // Store the lote instance
       
        txtNombre.setText(p.getNombre());
        txtDescripcion.setText(p.getDescripcion());
        txtPrecio.setText(String.valueOf(p.getPrecio()));
        
    }
    

    @FXML
    private void modificarProducto(ActionEvent event) {
        p.setNombre(txtNombre.getText());
        p.setPrecio(Integer.parseInt(txtPrecio.getText()));
        p.setDescripcion(txtDescripcion.getText());
        

        if (p.modificar()) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "El sistema comunica:", "Modificado correctamente");
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "El sistema comunica:", "Registro no modificado.");
        }
    }

    @FXML
    private void eliminarProducto(ActionEvent event) {
        if (p.eliminar()) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "El sistema comunica:", "Eliminado correctamente");
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "El sistema comunica:", "Registro no eliminado.");
        }
    }
    
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.show();
    }
    
    private void configurarAutocompletado(TextField textField, ObservableList<String> itemList) {
    TextFields.bindAutoCompletion(textField, param -> {
        List<String> filteredList = itemList.stream()
                .filter(item -> item.toLowerCase().contains(param.getUserText().toLowerCase()))
                .limit(3) // Limitar a 3 resultados
                .collect(Collectors.toList());
        return filteredList;
    });
}
    private void cargarNombresProductos(ObservableList<String> itemList) {
        String sql = "SELECT nombre FROM producto";
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/consultorio", "root", "");
             PreparedStatement stm = con.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                itemList.add(rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
}
