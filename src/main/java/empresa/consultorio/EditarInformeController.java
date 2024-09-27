/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package empresa.consultorio;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modelos.Informe;
import modelos.Lote;

/**
 * FXML Controller class
 *
 * @author Milagros Taboada
 */
public class EditarInformeController implements Initializable {

    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnAñadirInforme;
    @FXML
    private TableView<Informe> tblInforme;
    @FXML
    private TableColumn<Informe, String> colPaciente;
    @FXML
    private TableColumn<Informe, String> colMotivoConsulta;
    @FXML
    private TableColumn<Informe, String> colHallazgos;
    @FXML
    private TableColumn<Informe, String> colFecha;

    private Informe i = new Informe();
    private ObservableList<Informe> registros;
    ObservableList<Lote> registrosFiltrados;
    private ObservableList<String> InformesList;
    @FXML
    private Button btnVerPacientes;
    @FXML
    private Button btnTratamiento;
    @FXML
    private JFXButton imgCerrar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        InformesList = FXCollections.observableArrayList();
        inicializarTabla();
        mostrarDatos();

        // TODO
    }

    private void inicializarTabla() {
        colPaciente.setCellValueFactory(new PropertyValueFactory<>("nombrePaciente"));
        colMotivoConsulta.setCellValueFactory(new PropertyValueFactory<>("motivoConsulta"));
        colHallazgos.setCellValueFactory(new PropertyValueFactory<>("hallazgos"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

    }

    private void mostrarDatos() {
        registros = FXCollections.observableArrayList(i.consulta());
        tblInforme.setItems(registros);
    }

    @FXML
    private void AñadirInforme(ActionEvent event) {

        abrirFormulario("crearInforme.fxml", "Añadir informe ", null);
    }

    @FXML
    private void modificarProducto(MouseEvent event) {
        Informe InformeSeleccionado = tblInforme.getSelectionModel().getSelectedItem();
        if (InformeSeleccionado != null) {
            abrirFormulario("ModificarInforme.fxml", "Editar Informe", InformeSeleccionado);
        }
    }

    
    @FXML
    public void verPacientes(ActionEvent event) {
        try {
            App.switchView("/empresa/consultorio/pacientesPrincipal.fxml", "Pacientes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirFormulario(String fxml, String titulo, Informe informeExistente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquear interacción con otras ventanas

            // Si se proporciona un informe existente, inicializar el controlador del formulario
            if (informeExistente != null) {
                ModificarInformeController controller = loader.getController();
                controller.initData(informeExistente.getIdInforme(), informeExistente);
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
        registros.addAll(i.consulta());
    }

    @FXML
    public void VerTratamiento(ActionEvent event) {
        try {
            App.switchView("/empresa/consultorio/VerTratamiento.fxml", "Tratamiento");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCerrar(ActionEvent event) {
        Platform.exit(); // Cierra la aplicación
    }

}
