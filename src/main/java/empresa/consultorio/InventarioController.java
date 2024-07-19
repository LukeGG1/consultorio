package empresa.consultorio;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelos.lote;

public class InventarioController implements Initializable {

    @FXML
    private TableView<lote> tblLote;
    @FXML
    private TableColumn<lote, String> colProducto;
    @FXML
    private TableColumn<lote, Integer> colCantidad;
    @FXML
    private TableColumn<lote, String> colLote;
    @FXML
    private TableColumn<lote, String> colVencimiento;
    @FXML
    private Button btnAñadir;

    private lote l = new lote();
    private ObservableList<lote> registros;
    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnVerProductos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarTabla();
        mostrarDatos();
    }

    @FXML
    private void Añadir(ActionEvent event) {
        abrirFormulario("Producto.fxml", "Añadir Producto", null);
    }

    @FXML
    private void modificarLote(MouseEvent event) {
        lote loteSeleccionado = tblLote.getSelectionModel().getSelectedItem();
        if (loteSeleccionado != null) {
            abrirFormulario("loteEditar.fxml", "Editar Lote", loteSeleccionado);
        }
    }

    private void inicializarTabla() {
        colProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colLote.setCellValueFactory(new PropertyValueFactory<>("fechaLote"));
        colVencimiento.setCellValueFactory(new PropertyValueFactory<>("fechaVencimiento"));
    }

    private void mostrarDatos() {
        registros = FXCollections.observableArrayList(l.consulta());
        tblLote.setItems(registros);
    }

    private void abrirFormulario(String fxml, String titulo, lote loteExistente) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle(titulo);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL); // Bloquear interacción con otras ventanas

        // Si se proporciona un lote existente, inicializar el controlador del formulario
        if (loteExistente != null) {
            LoteEditarController controller = loader.getController();
            controller.initData(loteExistente.getIdLote(), loteExistente);
        }

        stage.showAndWait();

        // Actualizar tabla después de cerrar formulario
        actualizarTabla();

    } catch (IOException ex) {
        ex.printStackTrace();
    }
}


    private void actualizarTabla() {
        registros.clear();
        registros.addAll(l.consulta());
    }

    @FXML
    private void verProductos(ActionEvent event) {
        abrirFormulario("VerProducto.fxml", "ver Producto", null);
    }
}