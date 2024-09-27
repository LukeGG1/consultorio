package empresa.consultorio;

import clases.conexion;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import modelos.Lote;
import org.controlsfx.control.textfield.TextFields;

public class InventarioController extends conexion implements Initializable {

    @FXML
    private TableView<Lote> tblLote;
    @FXML
    private TableColumn<Lote, String> colProducto;
    @FXML
    private TableColumn<Lote, Integer> colCantidad;
    @FXML
    private TableColumn<Lote, String> colLote;
    @FXML
    private TableColumn<Lote, String> colVencimiento;
    @FXML
    private TextField txtBuscar;

    private Lote l = new Lote();
    private ObservableList<Lote> registros;
    ObservableList<Lote> registrosFiltrados;
    private ObservableList<String> lotesList;
    private JFXButton btnPacientes;
    private JFXButton btnInventario;
    private JFXButton btnContabilidad;
    private JFXButton btnConfiguracion;
    private JFXButton btnAyuda;
    @FXML
    private Button btnVerProductos;
    @FXML
    private Button btnAñadir;

    @FXML
    private ImageView menu;
    private AnchorPane pane1;

    private boolean isMenuOpen = false;
    @FXML
    private JFXButton imgCerrar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lotesList = FXCollections.observableArrayList();
        inicializarTabla();
        mostrarDatos();
        cargarNombresProductos(lotesList);
        configurarAutocompletado(txtBuscar, lotesList);

    }

    @FXML
    private void Añadir(ActionEvent event) {
        abrirFormulario("lote.fxml", "Añadir lote ", null);
    }

    @FXML
    private void modificarLote(MouseEvent event) {
        Lote loteSeleccionado = tblLote.getSelectionModel().getSelectedItem();
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

    private void abrirFormulario(String fxml, String titulo, Lote loteExistente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquear interacción con otras ventanas

            // Si se proporciona un lote existente, inicializar el controlador del formulario
            if (loteExistente != null) {
                LoteEditarController controller = loader.getController();
                controller.initData(loteExistente.getIdLote(), loteExistente);
            }

            // Añadir listener para actualizar la tabla al cerrar el formulario
            stage.setOnHiding(event -> actualizarTabla());

            stage.showAndWait();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void busqueda(KeyEvent event) {
        registrosFiltrados = FXCollections.observableArrayList();
        String buscar = txtBuscar.getText();
        if (buscar.isEmpty()) {
            tblLote.setItems(registros);
        } else {
            registrosFiltrados.clear();
            for (Lote registro : registros) {
                if (registro.getNombreProducto().toLowerCase().contains(buscar.toLowerCase())) {
                    registrosFiltrados.add(registro);
                }
            }
            tblLote.setItems(registrosFiltrados);
        }
    }

    private void actualizarTabla() {
        registros.clear();
        registros.addAll(l.consulta());
    }

    @FXML
    private void verProductos(ActionEvent event) {
        try {
            App.switchView("/empresa/consultorio/VerProducto.fxml", "Ver Productos");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                itemList.add(rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleCerrar(ActionEvent event) {
        Platform.exit(); // Cierra la aplicación
    }

}
