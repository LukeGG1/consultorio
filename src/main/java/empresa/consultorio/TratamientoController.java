package empresa.consultorio;

import clases.conexion;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import modelos.Informe;
import modelos.Tratamiento;
import org.controlsfx.control.textfield.TextFields;

public class TratamientoController extends conexion implements Initializable {

    @FXML
    private TextField txtProducto;
    @FXML
    private TextField txtFrecuencia;
    @FXML
    private TextField txtDosis;
    @FXML
    private TextField txtCantidad;
    @FXML
    private TextArea txtInstrucciones;
    @FXML
    private Button btnGuardarTratamiento;
    @FXML
    private Button btnEliminarTratamiento;

    private Tratamiento t = new Tratamiento();
    private ObservableList<String> productoList;
    private ObservableList<Informe> informeList;
    private boolean modificar = false;
    private int idInforme;
    @FXML
    private TextField txtInforme;
    @FXML
    private JFXButton imgCerrar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productoList = FXCollections.observableArrayList();
        cargarProductos();
        configurarAutocompletado(txtProducto, productoList);

        informeList = FXCollections.observableArrayList();
        cargarInformes(informeList);
        configurarAutocompletadoInforme(txtInforme, informeList);
    }

    public void initData(Tratamiento tratamientoSeleccionado) {
        modificar = true;
        btnEliminarTratamiento.setVisible(true);
        t = tratamientoSeleccionado;
        txtProducto.setText(t.getNombreProducto());
        txtFrecuencia.setText(t.getFrecuencia());
        txtDosis.setText(t.getDosis());
        txtCantidad.setText(String.valueOf(t.getCantidad()));
        txtInstrucciones.setText(t.getInstrucciones());
        txtInforme.setText(getNombreInformeById(t.getInformeIdInforme()));
    }

    public void initIdInforme(int idInforme) {
        this.idInforme = idInforme;
    }

    @FXML
    private void guardarTratamiento(ActionEvent event) {
        String nombreProducto = txtProducto.getText();
        String nombreInforme = txtInforme.getText();

        int productoId = buscarIdProducto(nombreProducto);
        idInforme = buscarIdInforme(nombreInforme);

        if (productoId != -1 && idInforme != -1) {
            String frecuencia = txtFrecuencia.getText();
            String dosis = txtDosis.getText();
            int cantidad = Integer.parseInt(txtCantidad.getText());
            String instrucciones = txtInstrucciones.getText();

            t.setFrecuencia(frecuencia);
            t.setDosis(dosis);
            t.setCantidad(cantidad);
            t.setInstrucciones(instrucciones);
            t.setProductoIdInProducto(productoId);
            t.setInformeIdInforme(idInforme);

            if (modificar) {
                if (t.modificar()) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "El sistema comunica:", "Modificado correctamente");
                    handleCerrar(null);
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "El sistema comunica:", "Registro no modificado.");
                }
                modificar = false;
            } else {
                if (t.insertar()) {
                    mostrarAlerta(Alert.AlertType.CONFIRMATION, "El sistema comunica:", "Insertado correctamente");
                    handleCerrar(null);
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "El sistema comunica:", "No se pudo insertar");
                }
            }
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "El sistema comunica:", "Producto o informe no encontrado");
        }
    }

    @FXML
    private void eliminarTratamiento(ActionEvent event) {
        if (modificar) {
            if (t.eliminar()) {
                mostrarAlerta(Alert.AlertType.INFORMATION, "El sistema comunica:", "Eliminado correctamente");
                handleCerrar(null);
            } else {
                mostrarAlerta(Alert.AlertType.ERROR, "El sistema comunica:", "No se pudo eliminar");
            }
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "El sistema comunica:", "No se ha seleccionado ningún tratamiento para eliminar");
        }
    }

    private int buscarIdProducto(String nombreProducto) {
        String sql = "SELECT id_producto FROM producto WHERE nombre = ?";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setString(1, nombreProducto);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_producto");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    private int buscarIdInforme(String nombreInforme) {
        for (Informe informe : informeList) {
            if ((informe.getNombrePaciente() + " " + informe.getFecha()).equals(nombreInforme)) {
                return informe.getIdInforme();
            }
        }
        return -1;
    }

    private void configurarAutocompletado(TextField textField, ObservableList<String> itemList) {
        TextFields.bindAutoCompletion(textField, param -> {
            List<String> filteredList = itemList.stream()
                    .filter(item -> item.toLowerCase().contains(param.getUserText().toLowerCase()))
                    .collect(Collectors.toList());
            return filteredList;
        });
    }

    private void configurarAutocompletadoInforme(TextField textField, ObservableList<Informe> itemList) {
        TextFields.bindAutoCompletion(textField, param -> {
            List<String> filteredList = itemList.stream()
                    .map(informe -> informe.getNombrePaciente() + " " + informe.getFecha())
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
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                itemList.add(rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void cargarInformes(ObservableList<Informe> itemList) {
        String sql = "SELECT i.id_informe, p.nombre, p.apellido, i.fecha FROM paciente p, informe i WHERE p.id_paciente = i.paciente_id_paciente";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id_informe");
                String nombre = rs.getString("nombre") + " " + rs.getString("apellido");
                String fecha = rs.getString("fecha");

                Informe informe = new Informe(id, "", "", fecha, nombre, 0);
                itemList.add(informe);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private String getNombreInformeById(int idInforme) {
        for (Informe informe : informeList) {
            if (informe.getIdInforme() == idInforme) {
                return informe.getNombrePaciente() + " " + informe.getFecha();
            }
        }
        return "";
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.show();
    }

    public int obtenerIdProductoPorNombre(String nombreProducto) {
        int idProducto = -1; // Valor predeterminado si no se encuentra el producto
        String sql = "SELECT id_producto FROM producto WHERE nombre_producto = ?";

        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, nombreProducto);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    idProducto = rs.getInt("id_producto");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return idProducto;
    }

    @FXML
    private void handleCerrar(ActionEvent event) {
        Stage stage = (Stage) btnGuardarTratamiento.getScene().getWindow();
        stage.close();
    }
}
