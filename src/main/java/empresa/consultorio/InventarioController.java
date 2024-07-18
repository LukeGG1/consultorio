package empresa.consultorio;

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
import javafx.util.Duration;
import modelos.lote;
import modelos.producto;
import org.controlsfx.control.textfield.TextFields;

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
    private TextField txtBuscar;

    private lote l = new lote();
    private ObservableList<lote> registros;
    ObservableList<lote> registrosFiltrados;
    private ObservableList<String> lotesList;
    @FXML
    private JFXButton btnPacientes;
    @FXML
    private JFXButton btnInventario;
    @FXML
    private JFXButton btnContabilidad;
    @FXML
    private JFXButton btnConfiguracion;
    @FXML
    private JFXButton btnAyuda;

    @FXML
    private ImageView exit, menu;
    @FXML
    private AnchorPane pane1, pane2;

    private boolean isMenuOpen = false;
    @FXML
    private Button btnAñadirIngreso;
    @FXML
    private Button btnAñadirEgreso;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lotesList = FXCollections.observableArrayList();
        inicializarTabla();
        mostrarDatos();
        cargarNombresProductos(lotesList);
        configurarAutocompletado(txtBuscar, lotesList);

        exit.setOnMouseClicked(event -> {
            System.exit(0);
        });

        // Inicializar el menú como cerrado
        pane1.setVisible(false);
        pane2.setTranslateX(-pane2.getPrefWidth()); // Asegúrate de que pane2 esté fuera de la vista

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

    @FXML
    private void busqueda(KeyEvent event) {
        registrosFiltrados = FXCollections.observableArrayList();
        String buscar = txtBuscar.getText();
        if (buscar.isEmpty()) {
            tblLote.setItems(registros);
        } else {
            registrosFiltrados.clear();
            for (lote registro : registros) {
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

    private void verProductos(ActionEvent event) {
        abrirFormulario("VerProducto.fxml", "Ver Producto", null);
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
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/consultorio", "root", "");
             PreparedStatement stm = con.prepareStatement(sql);
             ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                itemList.add(rs.getString("nombre"));
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
        translateTransition.setToX(-pane2.getWidth());
        translateTransition.play();
    }

    public void switchToPacientes(ActionEvent event) {
        btnPacientes.getScene().getWindow().hide();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/empresa/consultorio/secondary.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToProductos(ActionEvent event) {
        btnInventario.getScene().getWindow().hide();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/empresa/consultorio/secondary.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToContabilidad(ActionEvent event) {
        btnContabilidad.getScene().getWindow().hide();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/empresa/consultorio/inventario.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToAyuda(ActionEvent event) {
        btnAyuda.getScene().getWindow().hide();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/empresa/consultorio/inventario.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirInventario(ActionEvent event) {
        abrirFormulario("inventario.fxml", "Ver inventario", null);
        
    }

    @FXML
    private void AñadirIngreso(ActionEvent event) {
        abrirFormulario("Ingreso.fxml", "Ver ingreso", null);
    }

    @FXML
    private void AñadirEgreso(ActionEvent event) {
    }
}
