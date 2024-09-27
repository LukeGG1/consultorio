package empresa.consultorio;

import clases.conexion;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import modelos.Lote;
import org.controlsfx.control.textfield.TextFields;

public class LoteEditarController extends conexion implements Initializable {

    @FXML
    private Label labelId;
    @FXML
    private TextField txtNom;
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
    private Button btnModificar;
    @FXML
    private Button btnEliminar;
    
    private ObservableList<String> productosList;

    
    private Lote l = new Lote();
    @FXML
    private JFXButton imgCerrar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productosList = FXCollections.observableArrayList();
        cargarNombresProductos(productosList);
        configurarAutocompletado(txtNom, productosList);
    }

    public void initData(int idLote, Lote l) {
        this.l = l;  // Store the lote instance
        labelId.setText("ID del Lote: " + idLote);
        txtNom.setText(l.getNombreProducto());
        txtFechaLote.setValue(LocalDate.parse(l.getFechaLote()));
        txtFechaFabricacion.setValue(LocalDate.parse(l.getFechaFabricacion()));
        txtFechaVencimiento.setValue(LocalDate.parse(l.getFechaVencimiento()));
        txtCosto.setText(String.valueOf(l.getCostoLote()));
        txtCantidad.setText(String.valueOf(l.getCantidad()));
    }

    @FXML
    private void modificar(ActionEvent event) {
        l.setFechaLote(txtFechaLote.getValue().toString());
        l.setFechaFabricacion(txtFechaFabricacion.getValue().toString());
        l.setFechaVencimiento(txtFechaVencimiento.getValue().toString());
        l.setCostoLote(Integer.parseInt(txtCosto.getText()));
        l.setCantidad(Integer.parseInt(txtCantidad.getText()));
        l.setProductoIdProducto(obtenerIdProducto(txtNom.getText()));

        if (l.modificar()) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "El sistema comunica:", "Modificado correctamente");
            handleCerrar(null);
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "El sistema comunica:", "Registro no modificado.");
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        if (l.eliminar()) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "El sistema comunica:", "Eliminado correctamente");
            handleCerrar(null);
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
    private int obtenerIdProducto(String nombreProducto) {
    String sql = "SELECT id_producto FROM producto WHERE nombre = ?";
    int idEncontrado = 0; // Variable para almacenar el ID encontrado

    try (Connection con = getCon();
         PreparedStatement stm = con.prepareStatement(sql)) {
        
        stm.setString(1, nombreProducto);

        try (ResultSet rs = stm.executeQuery()) {
            if (rs.next()) {
                idEncontrado = rs.getInt("id_producto");
            }
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    return idEncontrado;
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
        try (Connection con = getCon();
             PreparedStatement stm = con.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                itemList.add(rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    private void handleCerrar(ActionEvent event) {
        Stage stage = (Stage) btnModificar.getScene().getWindow();
        stage.close();
    }
}
