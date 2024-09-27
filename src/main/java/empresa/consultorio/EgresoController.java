package empresa.consultorio;

import java.sql.Date;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import modelos.Movimiento;
import java.time.LocalDate;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

public class EgresoController {

    @FXML
    private ComboBox<String> comboMotivo;

    @FXML
    private TextField textMonto;

    @FXML
    private TextArea textDescripcion;
    
    @FXML
    private DatePicker txtFechaMovimiento;
    @FXML
    private Button btnGuardar;
    
    @FXML
    private void initialize() {
        // Inicializar el ComboBox con motivos de egreso
        System.out.println("Initialize ejecutado");
        comboMotivo.setItems(FXCollections.observableArrayList("Servicio", "Lote", "Salario"));
    }

    @FXML
    private void handleGuardar(ActionEvent event) throws SQLException {
        String tipo = "Egreso";
        String motivo = comboMotivo.getValue();

        double monto;
        try {
            monto = Double.parseDouble(textMonto.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "El sistema comunica", "Monto inválido. Por favor, ingrese un número válido.");
            return;
        }

        String descripcion = textDescripcion.getText();
        LocalDate fechaLocal = txtFechaMovimiento.getValue();
        if (fechaLocal == null) {
            mostrarAlerta(Alert.AlertType.ERROR, "El sistema comunica", "Debe seleccionar una fecha.");
            return;
        }
        Date fecha = Date.valueOf(fechaLocal); // Convertir LocalDate a java.sql.Date

        Movimiento nuevoMovimiento = new Movimiento(tipo, motivo, monto, descripcion, fecha);

        if (nuevoMovimiento.insertar()) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "El sistema comunica:", "Movimiento Modificado correctamente");
            handleCerrar(null);
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "El sistema comunica:", "Movimiento no modificado.");
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
