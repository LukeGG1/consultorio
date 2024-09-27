package empresa.consultorio;

import clases.conexion;
import clases.reporte;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import modelos.Paciente;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.textfield.TextFields;

public class PacientesPrincipalController extends conexion implements Initializable {

    private JFXButton btnPacientes;
    @FXML
    private JFXButton btnInventario;
    @FXML
    private JFXButton btnFactura;
    @FXML
    private JFXButton btnContabilidad;

    @FXML
    private JFXButton btnAyuda;
    @FXML
    private ImageView menu;
    @FXML
    private AnchorPane pane1;
    @FXML
    private AnchorPane pane2;

    private boolean isMenuOpen = false;

    @FXML
    private TableView<Paciente> tblPacientes;
    @FXML
    private TableColumn<Paciente, String> colId;
    @FXML
    private TableColumn<Paciente, String> colNombre;
    @FXML
    private TableColumn<Paciente, String> colApellido;
    @FXML
    private TableColumn<Paciente, String> colSexo;
    @FXML
    private TableColumn<Paciente, String> colFechaNac;
    @FXML
    private TableColumn<Paciente, String> colCorreo;
    @FXML
    private TableColumn<Paciente, String> colTelefono;
    @FXML
    private TextField txtBuscar;

    private Paciente p = new Paciente();

    private ObservableList<Paciente> RegistrosPacientes;
    ObservableList<Paciente> RegistrosPacientesFiltrados;
    private ObservableList<String> PacientesList;

    @FXML
    private Button btnAñadir;
    @FXML
    private Button btnVerInforme;
    @FXML
    private JFXButton btnMenu;
    @FXML
    private VBox pane3;
    @FXML
    private Button btnReporte;
    @FXML
    private TextField txtBuscar1;
    @FXML
    private JFXButton imgCerrar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializar la lista de pacientes
        PacientesList = FXCollections.observableArrayList();
        inicializarTabla();
        mostrarDatos();
        cargarNombresPacientes("paciente", PacientesList);
        configurarAutocompletado(txtBuscar, PacientesList);
        configurarAutocompletado(txtBuscar1, PacientesList);

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
    private void Añadir(ActionEvent event) {
        abrirFormulario("pacientes.fxml", "Añadir Paciente", null);
    }

    @FXML
    private void modificarPaciente(MouseEvent event) {
        Paciente pacienteSeleccionado = tblPacientes.getSelectionModel().getSelectedItem();
        if (pacienteSeleccionado != null) {
            abrirFormulario("pacientes.fxml", "Editar Paciente", pacienteSeleccionado);
        }
    }

    private void abrirFormulario(String fxml, String titulo, Paciente pacienteExistente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquear interacción con otras ventanas

            // Si se proporciona un paciente existente, inicializar el controlador del formulario
            if (pacienteExistente != null) {
                PacientesController controller = loader.getController();
                controller.initData(pacienteExistente);
            }

            stage.showAndWait();

            // Actualizar tabla después de cerrar formulario
            actualizarTabla();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void actualizarTabla() {
        RegistrosPacientes.clear();
        RegistrosPacientes.addAll(p.consulta());
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

    private void cargarItems(String itemName, ObservableList<String> itemList) {
        String sql = "SELECT nombre FROM " + itemName;
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                itemList.add(rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void cargarNombresPacientes(String itemName, ObservableList<String> itemList) {
        String sql = "SELECT CONCAT(nombre, ' ', apellido) AS nombre_completo FROM " + itemName;
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                itemList.add(rs.getString("nombre_completo"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void inicializarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colSexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));
        colFechaNac.setCellValueFactory(new PropertyValueFactory<>("fechaNac"));
    }

    private void mostrarDatos() {
        RegistrosPacientes = FXCollections.observableArrayList(p.consulta());
        tblPacientes.setItems(RegistrosPacientes);
    }

    @FXML
    private void busqueda(KeyEvent event) {
        RegistrosPacientesFiltrados = FXCollections.observableArrayList();
        String buscar = txtBuscar.getText();
        if (buscar.isEmpty()) {
            tblPacientes.setItems(RegistrosPacientes);
        } else {
            RegistrosPacientesFiltrados.clear();
            for (Paciente registro : RegistrosPacientes) {
                if (registro.getNombre().toLowerCase().contains(buscar.toLowerCase())) {
                    RegistrosPacientesFiltrados.add(registro);
                }
            }
            tblPacientes.setItems(RegistrosPacientesFiltrados);
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
    public void switchToProductos(ActionEvent event) {
        try {
            App.switchView("/empresa/consultorio/VerProducto.fxml", "Productos");
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
    private void verInforme(ActionEvent event) {
        

        try {
            App.switchView("/empresa/consultorio/EditarInforme.fxml", "Informes");
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    private void CrearReporte(ActionEvent event) throws UnsupportedEncodingException {
        String nombreCompleto = txtBuscar1.getText();
        String[] nombreApellido = obtenerNombreYApellido(nombreCompleto);

        if (nombreApellido.length < 2) {
            System.out.println("Por favor, ingrese tanto el nombre como el apellido.");
            return;
        }

        String nombre = nombreApellido[0];
        String apellido = nombreApellido[1];
        int idPaciente = obtenerIdPaciente(nombre, apellido);

        if (idPaciente != -1) {
            reporte r = new reporte();
            r.generarReportePaciente("/empresa/consultorio/InformeP.jasper", "Reporte Paciente", idPaciente);
        } else {
            // Manejar el caso donde no se encontró el paciente
            System.out.println("Paciente no encontrado.");
        }
    }

    private String[] obtenerNombreYApellido(String nombreCompleto) {
        return nombreCompleto.trim().split(" ", 2);
    }

    private int obtenerIdPaciente(String nombre, String apellido) {
        String sql = "SELECT id_paciente FROM paciente WHERE nombre = ? AND apellido = ?";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, nombre);
            stm.setString(2, apellido);

            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_paciente");
                } else {
                    throw new SQLException("Paciente no encontrado");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return -1; // O maneja el error de otra manera
        }
    }

    @FXML
    private void switchToProductosimg(MouseEvent event) {
        switchToProductos(null);
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
    private void switchToFacturaimg(MouseEvent event) {
        switchToFactura(null);
    }

    @FXML
    private void handleCerrar(ActionEvent event) {
        Platform.exit(); // Cierra la aplicación
    }
}
