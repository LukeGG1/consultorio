package empresa.consultorio;

import modelos.lote;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Milagros Taboada
 */
public class LoteController implements Initializable {

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
    @FXML
    private TextField txtNom;
    
    private lote l = new lote();
    private ObservableList<String> productoList;
    private boolean modificar = false;
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productoList = FXCollections.observableArrayList();
        cargarProductos();
        configurarAutocompletado(txtNom, productoList);
    }

    @FXML
    private void guardar(ActionEvent event) {
        String nombreProducto = txtNom.getText(); // Obtener el nombre del producto ingresado

        // Buscar el ID del producto basado en el nombre
        int productoId = buscarIdProducto(nombreProducto);

        // Verificar si se encontró el ID del producto
        if (productoId != -1) {
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
            l.setProductoIdProducto(productoId); // Asignar el ID del producto al lote

            if (modificar) {
                if (l.modificar()) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "El sistema comunica:", "Modificado correctamente");
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "El sistema comunica:", "Registro no modificado.");
                }
                modificar = false;
            } else {
                if (l.insertar()) {
                    mostrarAlerta(Alert.AlertType.CONFIRMATION, "El sistema comunica:", "Insertado correctamente");
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "El sistema comunica:", "No se pudo insertar");
                }
            }
        } else {
            // Mostrar mensaje de error si no se encontró el producto
            mostrarAlerta(Alert.AlertType.ERROR, "El sistema comunica:", "Producto no encontrado");
        }
        
    }

private int buscarIdProducto(String nombreProducto) {
    String sql = "SELECT id_producto FROM producto WHERE nombre = ?";
    
    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/consultorio", "root", "");
         PreparedStatement stm = con.prepareStatement(sql)) {
        
        stm.setString(1, nombreProducto);
        try (ResultSet rs = stm.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("id_producto");
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    
    return -1; // Retorna -1 si no se encontró el producto
}

    private void configurarAutocompletado(TextField textField, ObservableList<String> itemList) {
        TextFields.bindAutoCompletion(textField, param -> {
            List<String> filteredList = itemList.stream()
                    .filter(item -> item.toLowerCase().contains(param.getUserText().toLowerCase()))
                    .collect(Collectors.toList());
            return filteredList;
        });
    }

    private void cargarProductos() {
        cargarItems("producto", productoList);
    }

    private void cargarItems(String itemName, ObservableList<String> itemList) {
        String sql = "SELECT nombre FROM " + itemName;
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

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.show();
    }

    
}
