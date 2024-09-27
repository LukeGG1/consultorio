/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package empresa.consultorio;

import clases.conexion;
import com.jfoenix.controls.JFXButton;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelos.Informe;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Milagros Taboada
 */
public class ModificarInformeController extends conexion implements Initializable {

    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtMotivoConsulta;
    @FXML
    private TextArea txtHallazgos;
    @FXML
    private DatePicker txtFecha;
    @FXML
    private Button btnDerecho;
    @FXML
    private ImageView imDerecho;
    @FXML
    private Button btnIzquierdo;
    @FXML
    private ImageView imIzquierdo;
    @FXML
    private Button btnModificar;
    @FXML
    private Button btnEliminar;

    private ObservableList<String> pacientesList;

    private final FileChooser fc = new FileChooser();
    private Informe i = new Informe();
    @FXML
    private JFXButton imgCerrar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar directorio inicial y filtros de extensión
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de imagen", "*.png", "*.jpg", "*.gif")
        );
        pacientesList = FXCollections.observableArrayList();
        cargarNombresPacientes(pacientesList);
        configurarAutocompletado(txtNom, pacientesList);
        // TODO
    }

    public void initData(int idInforme, Informe i) {
        this.i = i;
        txtNom.setText(i.getNombrePaciente());
        txtMotivoConsulta.setText(i.getMotivoConsulta());
        txtHallazgos.setText(i.getHallazgos());
        txtFecha.setValue(LocalDate.parse(i.getFecha()));

        // Cargar y mostrar las imágenes desde la base de datos
        if (i.getImagenOjoIzquierdo() != null) {
            imIzquierdo.setImage(new Image(new ByteArrayInputStream(i.getImagenOjoIzquierdo())));
        }

        if (i.getImagenOjoDerecho() != null) {
            imDerecho.setImage(new Image(new ByteArrayInputStream(i.getImagenOjoDerecho())));
        }
    }

    @FXML
    private void Modificar(ActionEvent event) {
        i.setFecha(txtFecha.getValue().toString());
        i.setMotivoConsulta(txtMotivoConsulta.getText());
        i.setHallazgos(txtHallazgos.getText());
        i.setPacienteIdPaciente(obtenerIdPaciente(txtNom.getText()));

        if (i.modificar()) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "El sistema comunica:", "Modificado correctamente");
            handleCerrar(null);
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "El sistema comunica:", "Registro no modificado.");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.show();
    }

    private int obtenerIdPaciente(String nombreProducto) {
        String sql = "SELECT id_paciente FROM paciente WHERE nombre = ?";
        int idEncontrado = 0; // Variable para almacenar el ID encontrado

        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, nombreProducto);

            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    idEncontrado = rs.getInt("id_paciente");
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return idEncontrado;
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

    private void abrirTratamiento(ActionEvent event) {
        abrirFormulario("tratamiento.fxml", "Agregar tratamiento", i.getIdInforme());
    }

    @FXML
    private void agregarOjoIzq(ActionEvent event) {
        File file = fc.showOpenDialog(null);
        if (file != null) {
            try {
                Image image = new Image(file.toURI().toURL().toExternalForm());
                imIzquierdo.setImage(image);
                i.setImagenOjoIzquierdo(convertirImagenABytes(file)); // Actualizar la imagen en el objeto Informe
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No se seleccionó ningún archivo");
        }
    }

    @FXML
    private void agregarOjoDer(ActionEvent event) {
        File file = fc.showOpenDialog(null);
        if (file != null) {
            try {
                Image image = new Image(file.toURI().toURL().toExternalForm());
                imDerecho.setImage(image);
                i.setImagenOjoDerecho(convertirImagenABytes(file)); // Actualizar la imagen en el objeto Informe
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No se seleccionó ningún archivo");
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        if (i.eliminar()) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "El sistema comunica:", "Eliminado correctamente");
            handleCerrar(null);
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "El sistema comunica:", "Registro no eliminado.");
        }
    }

    private void cargarNombresPacientes(ObservableList<String> itemList) {
        String sql = "SELECT nombre FROM paciente";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql); ResultSet rs = stm.executeQuery()) {

            while (rs.next()) {
                itemList.add(rs.getString("nombre"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void abrirFormulario(String fxml, String titulo, int idInforme) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquear interacción con otras ventanas

            TratamientoController controller = loader.getController();
            controller.initIdInforme(idInforme);

            stage.showAndWait();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private byte[] convertirImagenABytes(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        }
    }

    @FXML
    private void handleCerrar(ActionEvent event) {
        Stage stage = (Stage) btnModificar.getScene().getWindow();
        stage.close();
    }
}
