package empresa.consultorio;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import modelos.Movimiento;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;

public class MovimientoEditarController implements Initializable {

    @FXML
    private TextField textMonto;
    @FXML
    private TextArea textDescripcion;
    @FXML
    private ComboBox<String> comboMotivo;

    private Movimiento movimiento;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializa el ComboBox con los motivos disponibles
        comboMotivo.setItems(FXCollections.observableArrayList("Consulta", "Producto", "Frecuencia", "Servicio", "Lote", "Salario"));
    }

    public void initData(Movimiento movimiento) {
        this.movimiento = movimiento;
        // Rellena los campos con los datos del movimiento
        if (movimiento != null) {
            textMonto.setText(String.valueOf(movimiento.getMonto()));
            textDescripcion.setText(movimiento.getDescripcion());
            comboMotivo.setValue(movimiento.getMotivo());
        }
    }

    // Aquí puedes agregar otros métodos si necesitas actualizar automáticamente el modelo,
    // por ejemplo, cuando se cambie el contenido de los campos.
}
