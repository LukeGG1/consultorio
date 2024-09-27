/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package empresa.consultorio;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import modelos.Producto;

/**
 * FXML Controller class
 *
 * @author Milagros Taboada
 */
public class VerProductoController implements Initializable {

    @FXML
    private JFXButton btnPacientes;
    @FXML
    private JFXButton btnFactura;
    @FXML
    private JFXButton btnContabilidad;
    @FXML
    private TableView<Producto> tblProducto;
    @FXML
    private JFXButton btnAyuda;
    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnAñadir;
    @FXML
    private Button btnVerLotes;
    @FXML
    private TableColumn<Producto, String> colProducto;
    @FXML
    private TableColumn<Producto, Integer> colPrecio;
    @FXML
    private TableColumn<Producto, String> colDescripcion;
    @FXML
    private JFXButton btnMenu;

    @FXML
    private AnchorPane pane1, pane2;

    private boolean isMenuOpen = false;

    private Producto p = new Producto();
    private ObservableList<Producto> registrosp;
    ObservableList<Producto> registrosFiltrados;
    private ObservableList<String> ProductosList;

    @FXML
    private ImageView menu;
    @FXML
    private VBox pane3;
    @FXML
    private JFXButton imgCerrar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarTabla();
        mostrarDatos();

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
        // TODO
    }

    private void inicializarTabla() {
        colProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

    }

    private void mostrarDatos() {
        registrosp = FXCollections.observableArrayList(p.consulta());
        tblProducto.setItems(registrosp);
    }

    @FXML
    private void Añadir(ActionEvent event) {
        abrirFormulario("Producto.fxml", "Añadir Producto", null);

    }

    @FXML
    private void modificarProducto(MouseEvent event) {
        Producto productoSeleccionado = tblProducto.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            abrirFormulario("productoEditar.fxml", "Editar producto", productoSeleccionado);
        }
    }

    private void abrirFormulario(String fxml, String titulo, Producto productoExistente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquear interacción con otras ventanas

            // Si se proporciona un lote existente, inicializar el controlador del formulario
            if (productoExistente != null) {
                ProductoEditarController controller = loader.getController();
                controller.initData(productoExistente.getIdProducto(), productoExistente);
            }

            stage.showAndWait();

            // Actualizar tabla después de cerrar formulario
            actualizarTabla();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void actualizarTabla() {
        registrosp.clear();
        registrosp.addAll(p.consulta());
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
    public void switchToPacientes(ActionEvent event) {
        try {
            App.switchView("/empresa/consultorio/pacientesPrincipal.fxml", "Pacientes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToContabilidad(ActionEvent event) {
        try {
            App.switchView("/empresa/consultorio/Contabilidad.fxml", "Contabilidad");
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
    private void abrirInventario(ActionEvent event) {
        try {
            App.switchView("/empresa/consultorio/inventario.fxml", "Inventario");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToMenu(ActionEvent event) {

        try {
            App.switchView("/empresa/consultorio/secondary.fxml", "Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void busqueda(KeyEvent event) {
        registrosFiltrados = FXCollections.observableArrayList();
        String buscar = txtBuscar.getText();
        if (buscar.isEmpty()) {
            tblProducto.setItems(registrosp);
        } else {
            registrosFiltrados.clear();
            for (Producto registro : registrosp) {
                if (registro.getNombre().toLowerCase().contains(buscar.toLowerCase())) {
                    registrosFiltrados.add(registro);
                }
            }
            tblProducto.setItems(registrosFiltrados);
        }
    }

    @FXML
    void switchToPacientesimg(MouseEvent event) {
        switchToPacientes(null);
    }

    @FXML
    private void switchToFacturaimg(MouseEvent event) {
        switchToFactura(null);
    }

    @FXML
    private void switchToContabilidadimg(MouseEvent event) {
        switchToContabilidad(null);
    }

    @FXML
    private void switchToMenuimg(MouseEvent event) {
        switchToMenu(null);
    }

    @FXML
    private void handleCerrar(ActionEvent event) {
        Platform.exit(); // Cierra la aplicación
    }
}
