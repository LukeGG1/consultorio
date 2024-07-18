package empresa.consultorio;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import modelos.Movimiento;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javafx.event.ActionEvent;

public class IngresoController {

    @FXML
    private ComboBox<String> comboMotivo;

    @FXML
    private TextField textMonto;

    @FXML
    private TextArea textDescripcion;
    
    @FXML
    private void initialize() {
        // Inicializar el ComboBox con motivos de ingreso
        comboMotivo.setItems(FXCollections.observableArrayList("Consulta", "Producto", "Frecuencia"));
    }

    @FXML
    private void handleGuardar(ActionEvent event) {
        String tipo = "Ingreso";
        String motivo = comboMotivo.getValue();
        double monto = Double.parseDouble(textMonto.getText());
        String descripcion = textDescripcion.getText();
        Timestamp fecha = Timestamp.valueOf(LocalDateTime.now());

        Movimiento nuevoMovimiento = new Movimiento(tipo, motivo, monto, descripcion, fecha);
        Movimiento.insertarMovimiento(nuevoMovimiento);

        mostrarAlerta("Éxito", "Ingreso guardado exitosamente.");

        // Limpiar los campos después de guardar
        limpiarCampos();
    }

    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private void limpiarCampos() {
        comboMotivo.setValue(null);
        textMonto.clear();
        textDescripcion.clear();
    }

    
}