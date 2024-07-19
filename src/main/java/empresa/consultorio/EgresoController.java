package empresa.consultorio;

import java.sql.Date;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import modelos.Movimiento;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javafx.event.ActionEvent;

public class EgresoController {

    @FXML
    private ComboBox<String> comboMotivo;

    @FXML
    private TextField textMonto;

    @FXML
    private TextArea textDescripcion;
    
    @FXML
    private void initialize() {
        // Inicializar el ComboBox con motivos de ingreso
        comboMotivo.setItems(FXCollections.observableArrayList("Servicio", "Lote", "Salario"));
    }

    private void handleGuardar(ActionEvent event) {
        String tipo = "Egreso";
        String motivo = comboMotivo.getValue();

        double monto;
        try {
            monto = Double.parseDouble(textMonto.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error", "Monto inválido. Por favor, ingrese un número válido.");
            return;
        }

        String descripcion = textDescripcion.getText();
        Date fecha = Date.valueOf(LocalDate.now()); // Verifica si esta fecha es válida para tu base de datos

        Movimiento nuevoMovimiento = new Movimiento(tipo, motivo, monto, descripcion, fecha);

        try {
            Movimiento.insertarMovimiento(nuevoMovimiento);
            mostrarAlerta("Éxito", "Ingreso guardado exitosamente.");
        } catch (SQLException e) {
            mostrarAlerta("Error", "No se pudo guardar el ingreso: " + e.getMessage());
            e.printStackTrace(); // Esto te dará más detalles del error
        }

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