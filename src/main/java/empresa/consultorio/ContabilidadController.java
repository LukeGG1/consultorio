/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import modelos.Movimiento;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Milagros Taboada
 */
public class ContabilidadController extends conexion implements Initializable {

    @FXML
    private TableView< Movimiento> tblMovimiento;
    @FXML
    private TableColumn<Movimiento, String> colTipo;
    @FXML
    private TableColumn<Movimiento, String> colMotivo;
    @FXML
    private TableColumn<Movimiento, Integer> colMonto;
    @FXML
    private TableColumn<Movimiento, String> colFecha;
    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnAñadirIngreso;
    @FXML
    private Button btnAñadirEgreso;

    @FXML
    private JFXButton btnPacientes;
    @FXML
    private JFXButton btnInventario;
    @FXML
    private JFXButton btnFactura;
    @FXML
    private JFXButton btnAyuda;
    @FXML
    private ImageView menu;
    @FXML
    private AnchorPane pane1, pane2;

    private Movimiento m = new Movimiento();
    private ObservableList<Movimiento> registros;
    ObservableList<Movimiento> registrosFiltrados;
    private ObservableList<String> MovimientoList;

    private boolean isMenuOpen = false;
    @FXML
    private JFXButton btnMenu;
    @FXML
    private VBox pane3;
    @FXML
    private JFXButton imgCerrar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MovimientoList = FXCollections.observableArrayList();
        inicializarTabla();
        mostrarDatos();
        cargarNombresProductos(MovimientoList);
        configurarAutocompletado(txtBuscar, MovimientoList);

        // Inicializar el menú como cerrado
        pane1.setVisible(false);
        pane2.setTranslateX(-(pane2.getPrefWidth() + 55)); // Asegúrate de que pane2 esté fuera de la vista

        menu.setOnMouseClicked(event -> {
            if (isMenuOpen) {
                closeMenu();
            } else {
                openMenu();
            }
        });

        pane1.setOnMouseClicked(event -> {
            closeMenu();
        });
    }

    @FXML
    private void modificarLote(MouseEvent event) {
        Movimiento MovimientoSeleccionado = tblMovimiento.getSelectionModel().getSelectedItem();
        if (MovimientoSeleccionado != null) {
            abrirFormulario("MovimientoEditar.fxml", "Editar Movimiento", MovimientoSeleccionado);
        }

    }

    private void inicializarTabla() {
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

    }

    private void mostrarDatos() {
        registros = FXCollections.observableArrayList(m.consulta());
        tblMovimiento.setItems(registros);
    }

    private void abrirFormulario(String fxml, String titulo, Movimiento MovimientoExistente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquear interacción con otras ventanas

            // Si se proporciona un lote existente, inicializar el controlador del formulario
            if (MovimientoExistente != null) {
                MovimientoEditarController controller = loader.getController();
                controller.initData(MovimientoExistente);

            }

            stage.showAndWait();

            // Actualizar tabla después de cerrar formulario
            actualizarTabla();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void busqueda(KeyEvent event) {
        registrosFiltrados = FXCollections.observableArrayList();
        String buscar = txtBuscar.getText();
        if (buscar.isEmpty()) {
            tblMovimiento.setItems(registros);
        } else {
            registrosFiltrados.clear();
            for (Movimiento registro : registros) {
                if (registro.getMotivo().toLowerCase().contains(buscar.toLowerCase())) {
                    registrosFiltrados.add(registro);
                }
            }
            tblMovimiento.setItems(registrosFiltrados);
        }
    }

    private void actualizarTabla() {
        registros.clear();
        registros.addAll(m.consulta());
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
        String sql = "SELECT motivo FROM movimiento";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                itemList.add(rs.getString("motivo"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void openMenu() {
        isMenuOpen = true;

        pane1.setVisible(true);

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), pane1);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(0.15);
        fadeTransition.play();

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), pane2);
        translateTransition.setToX(0);
        translateTransition.play();
    }

    private void closeMenu() {
        isMenuOpen = false;

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), pane1);
        fadeTransition.setFromValue(0.15);
        fadeTransition.setToValue(0);
        fadeTransition.play();

        fadeTransition.setOnFinished(event -> {
            pane1.setVisible(false);
        });

        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), pane2);
        translateTransition.setToX(-(pane2.getPrefWidth() + 55));
        translateTransition.play();
    }

    @FXML
    private void switchToMenu(ActionEvent event) {

        try {
            App.switchView("/empresa/consultorio/Secondary.fxml", "Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToPacientes(ActionEvent event) {
        try {
            App.switchView("/empresa/consultorio/pacientesPrincipal.fxml", "Pacientes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToProductos(ActionEvent event) {
        try {
            App.switchView("/empresa/consultorio/VerProducto.fxml", "Productos");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToAyuda(ActionEvent event) {

    }

    @FXML
    private void switchToFactura(ActionEvent event) {
        try {
            App.switchView("/empresa/consultorio/FacturaPrincipal_1.fxml", "Factura Principal");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void AñadirIngreso(ActionEvent event) {
        abrirFormulario("Ingreso.fxml", "Ver ingreso", null);
    }

    @FXML
    private void AñadirEgreso(ActionEvent event) {
        abrirFormulario("Egreso.fxml", "Ver Egreso", null);
    }

    @FXML
    void switchToPacientesimg(MouseEvent event) {
        switchToPacientes(null);
    }

    @FXML
    private void switchToProductosimg(MouseEvent event) {
        switchToProductos(null);
    }

    @FXML
    private void switchToMenuimg(MouseEvent event) {
        switchToMenu(null);
    }

    @FXML
    private void switchToFacturaimg(MouseEvent event) {
        switchToFactura(null);
    }

    @FXML
    private void handleCerrar(ActionEvent event) {
        Platform.exit(); // Cierra la aplicación
    }
}
