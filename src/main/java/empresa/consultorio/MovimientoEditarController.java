package empresa.consultorio;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import modelos.Movimiento;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class MovimientoEditarController implements Initializable {

    @FXML
    private TextField textTipo;
    @FXML
    private ComboBox<String> comboMotivo;

    @FXML
    private TextField textMonto;

    @FXML
    private TextArea textDescripcion;
    
    @FXML
    private DatePicker txtFechaMovimiento;

    private Movimiento movimiento;
    private String tipoMovimiento;  // Variable para almacenar el tipo de movimiento
    @FXML
    private JFXButton imgCerrar;
    
    @FXML
    private Button btnGuardar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializa el ComboBox con las opciones de motivos vacías inicialmente
        comboMotivo.setItems(FXCollections.observableArrayList());
    }

    public void initData(Movimiento movimiento) {
        this.movimiento = movimiento;
        // Rellena los campos con los datos del movimiento
        if (movimiento != null) {   
            textTipo.setText(movimiento.getTipo());
            tipoMovimiento = movimiento.getTipo();  // Establece el tipo de movimiento
            
            // Configura las opciones del ComboBox según el tipo de movimiento
            if (tipoMovimiento != null) {
                setComboMotivoOptions(tipoMovimiento);
            }
            
            comboMotivo.setValue(movimiento.getMotivo());
            textMonto.setText(String.valueOf(movimiento.getMonto()));
            txtFechaMovimiento.setValue(movimiento.getFecha().toLocalDate());
            textDescripcion.setText(movimiento.getDescripcion());
        }
    }
    
    private void setComboMotivoOptions(String tipoMovimiento) {
        // Configura las opciones del ComboBox basadas en el tipo de movimiento
        if (tipoMovimiento.equals("Ingreso")) {
            comboMotivo.setItems(FXCollections.observableArrayList("Consulta", "Producto", "Frecuencia"));
        } else if (tipoMovimiento.equals("Egreso")) {
            comboMotivo.setItems(FXCollections.observableArrayList("Servicio", "Lote", "Salario"));
        } else {
            // Opciones por defecto o en caso de tipo desconocido
            comboMotivo.setItems(FXCollections.observableArrayList());
        }
    }

    @FXML
    private void handleActualizar() {
        String motivo = comboMotivo.getValue();
        double monto;
        try {
            monto = Double.parseDouble(textMonto.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Monto inválido.", "Por favor, ingrese un número válido.");
            return;
        }
        String descripcion = textDescripcion.getText();
        Date fecha = Date.valueOf(txtFechaMovimiento.getValue());

        Movimiento m = new Movimiento(movimiento.getId(), tipoMovimiento, motivo, monto, descripcion, fecha);

        if (m.modificar()) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "El sistema comunica:", "Movimiento Modificado correctamente");
            handleCerrar(null);
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "El sistema comunica:", "Movimiento no modificado.");
        }
    }

    @FXML
    private void handleEliminar() {
        if (movimiento.eliminar()) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "El sistema comunica:", "Movimiento Eliminado correctamente");
            handleCerrar(null);
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "El sistema comunica:", "Movimiento no Eliminado.");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.show();
    }

    private void limpiarCampos() {
        comboMotivo.setValue(null);
        textMonto.clear();
        textDescripcion.clear();
        txtFechaMovimiento.setValue(null);
    }
    @FXML
    private void handleCerrar(ActionEvent event) {
        Stage stage = (Stage) btnGuardar.getScene().getWindow();
        stage.close();
    }
}
