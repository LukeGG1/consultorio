package empresa.consultorio;

import clases.conexion;
import clases.reporte;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import modelos.Factura;
import modelos.Informe;
import net.sf.jasperreports.engine.JRException;
import org.controlsfx.control.textfield.TextFields;

public class FacturaPrincipalController extends conexion implements Initializable {

    @FXML
    private TableColumn<Factura, Integer> colId;
    @FXML
    private TableColumn<Factura, String> colRuc;
    @FXML
    private TableColumn<Factura, String> colNombre;
    @FXML
    private TableColumn<Factura, String> colApellido;
    @FXML
    private TableColumn<Factura, String> colFecha;
    @FXML
    private AnchorPane pane1;
    @FXML
    private ImageView menu;
    @FXML
    private TextField txtBuscar;
    @FXML
    private JFXButton btnAñadir;
    @FXML
    private VBox pane3;
    @FXML
    private AnchorPane pane2;
    @FXML
    private JFXButton btnMenu;
    @FXML
    private JFXButton btnInventario;
    @FXML
    private JFXButton btnContabilidad;
    @FXML
    private JFXButton btnAyuda;
    @FXML
    private Button btnReporte;
    @FXML
    private TextField txtBuscar1;
    @FXML
    private JFXButton btnPacientes;

    private boolean isMenuOpen = false;
    @FXML
    private TableView<Factura> tblFactura;
    private ObservableList<Informe> informeList;

    private Factura f = new Factura();
    private ObservableList<Factura> registros;
    ObservableList<Factura> registrosFiltrados;
    @FXML
    private JFXButton imgCerrar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

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

        informeList = FXCollections.observableArrayList();
        inicializarTabla();
        mostrarDatos();
        cargarInformes(informeList);
        configurarAutocompletadoInforme(txtBuscar1, informeList);
    }

    private void inicializarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idFactura"));
        colRuc.setCellValueFactory(new PropertyValueFactory<>("ruc"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
    }

    private void mostrarDatos() {
        registros = FXCollections.observableArrayList(f.consulta());
        tblFactura.setItems(registros);
    }

    private void configurarAutocompletadoInforme(TextField textField, ObservableList<Informe> itemList) {
        TextFields.bindAutoCompletion(textField, param -> {
            List<String> filteredList = itemList.stream()
                    .map(informe -> informe.getNombrePaciente() + " " + informe.getFecha())
                    .filter(item -> item.toLowerCase().contains(param.getUserText().toLowerCase()))
                    .collect(Collectors.toList());
            return filteredList;
        });
    }

    private void cargarInformes(ObservableList<Informe> itemList) {
        String sql = "SELECT i.id_informe, p.nombre, p.apellido, i.fecha FROM paciente p, informe i WHERE p.id_paciente = i.paciente_id_paciente";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id_informe");
                String nombre = rs.getString("nombre") + " " + rs.getString("apellido");
                String fecha = rs.getString("fecha");

                Informe informe = new Informe(id, "", "", fecha, nombre, 0);
                itemList.add(informe);
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
    private void modificarFactura(MouseEvent event) {
        Factura FacturaSeleccionada = tblFactura.getSelectionModel().getSelectedItem();
        if (FacturaSeleccionada != null) {
            abrirFormulario("ModificarInforme.fxml", "Editar Informe", FacturaSeleccionada);
        }
    }

    private void abrirFormulario(String fxml, String titulo, Factura facturaExistente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquear interacción con otras ventanas

            // Si se proporciona una factura existente, inicializar el controlador del formulario
            if (facturaExistente != null) {
                FacturaController controller = loader.getController();
                controller.initData(facturaExistente.getIdFactura(), facturaExistente);
            }

            // Añadir listener para actualizar la tabla al cerrar el formulario
            stage.setOnHiding(event -> actualizarTabla());

            stage.showAndWait();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void actualizarTabla() {
        registros.clear();
        registros.addAll(f.consulta());
    }

    @FXML
    private void busqueda(KeyEvent event) {
        // Implementar la lógica de búsqueda si es necesario
    }

    @FXML
    private void Añadir(ActionEvent event) {
        abrirFormulario("/empresa/consultorio/factura.fxml", "Nueva Factura", null);
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
    void switchToPacientesimg(MouseEvent event) {
        switchToPacientes(null);
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
    private void generarReporte(ActionEvent event) {
        String nombreCompleto = txtBuscar1.getText();
        int idInforme = obtenerIdInformePorNombre(nombreCompleto);

        if (idInforme > 0) {
            try {
                reporte reporteFactura = new reporte();
                reporteFactura.generarReporteInforme("/empresa/consultorio/FacturaCopia.jasper", "Factura", idInforme);
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("No se encontró el informe correspondiente.");
        }
    }

    private int obtenerIdInformePorNombre(String nombreCompleto) {
        for (Informe informe : informeList) {
            String nombre = informe.getNombrePaciente() + " " + informe.getFecha();
            if (nombre.equalsIgnoreCase(nombreCompleto)) {
                return informe.getIdInforme();
            }
        }
        return -1; // Retornar -1 si no se encuentra el informe
    }

    @FXML
    private void handleCerrar(ActionEvent event) {
        Platform.exit(); // Cierra la aplicación
    }
}
