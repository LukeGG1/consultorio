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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modelos.Informe;
import modelos.Lote;
import modelos.Tratamiento;

public class VerTratamientoController implements Initializable {

    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnAñadirTratamiento;
    @FXML
    private Button btnVerPacientes;
    @FXML
    private Button btnInforme;
    @FXML
    private TableView<Tratamiento> tblTratamiento;
    @FXML
    private TableColumn<Tratamiento, String> colPaciente;
    @FXML
    private TableColumn<Tratamiento, String> colfechaInforme;
    @FXML
    private TableColumn<Tratamiento, String> colProducto;

    private Tratamiento t = new Tratamiento();
    private ObservableList<Tratamiento> registros;
    @FXML
    private JFXButton imgCerrar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        registros = FXCollections.observableArrayList();
        inicializarTabla();
        actualizarTabla();
    }

    private void inicializarTabla() {
        colPaciente.setCellValueFactory(new PropertyValueFactory<>("nombrePaciente"));
        colfechaInforme.setCellValueFactory(new PropertyValueFactory<>("fechaInforme"));
        colProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        tblTratamiento.setItems(registros);
    }

    @FXML
    private void AñadirTratamiento(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/empresa/consultorio/tratamiento.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initStyle(StageStyle.UNDECORATED);
            stage.showAndWait();
            actualizarTabla();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void verPacientes(ActionEvent event) {

        try {
            App.switchView("/empresa/consultorio/pacientesPrincipal.fxml", "Pacientes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void VerInforme(ActionEvent event) {
        try {
            App.switchView("/empresa/consultorio/editarInforme.fxml", "Pacientes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void modificarProducto(MouseEvent event) {
        if (event.getClickCount() == 2) {  // Check for double click
            Tratamiento tratamientoSeleccionado = tblTratamiento.getSelectionModel().getSelectedItem();
            if (tratamientoSeleccionado != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/empresa/consultorio/tratamiento.fxml"));
                    Parent root = loader.load();

                    TratamientoController tratamientoController = loader.getController();
                    tratamientoController.initData(tratamientoSeleccionado);

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.initStyle(StageStyle.UNDECORATED);
                    stage.showAndWait();
                    actualizarTabla();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void actualizarTabla() {
        registros.clear();
        registros.addAll(t.consulta());
    }

    @FXML
    private void handleCerrar(ActionEvent event) {
        Platform.exit(); // Cierra la aplicación
    }
}
