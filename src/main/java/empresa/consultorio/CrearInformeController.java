package empresa.consultorio;

import clases.conexion;
import com.jfoenix.controls.JFXButton;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modelos.Informe;
import org.controlsfx.control.textfield.TextFields;

public class CrearInformeController extends conexion implements Initializable {

    @FXML
    private Button btnIzquierdo;
    @FXML
    private Button btnDerecho;
    @FXML
    private ImageView imIzquierdo;
    @FXML
    private ImageView imDerecho;

    private final FileChooser fc = new FileChooser();
    @FXML
    private Button btnGuardarInforme;

    private Informe i = new Informe();
    private ObservableList<String> pacienteList;
    private boolean modificar = false;
    @FXML
    private TextField txtNom;
    @FXML
    private TextField txtMotivoConsulta;
    @FXML
    private TextArea txtHallazgos;
    @FXML
    private DatePicker txtFecha;
    private Button btnAñadirTratamiento;

    private byte[] imagenOjoIzquierdo;
    private byte[] imagenOjoDerecho;
    @FXML
    private JFXButton imgCerrar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar directorio inicial y filtros de extensión
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de imagen", "*.png", "*.jpg", "*.gif")
        );

        pacienteList = FXCollections.observableArrayList();
        cargarPacientes();
        configurarAutocompletado(txtNom, pacienteList);
    }

    @FXML
    private void agregarOjoIzq(ActionEvent event) {
        File file = fc.showOpenDialog(null);
        if (file != null) {
            try {
                Image image = new Image(file.toURI().toURL().toExternalForm());
                imIzquierdo.setImage(image);
                imagenOjoIzquierdo = convertirImagenABytes(file); // Convertir a bytes
            } catch (Exception e) {
                System.out.println("Error al cargar la imagen: " + e.getMessage());
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
                imagenOjoDerecho = convertirImagenABytes(file); // Convertir a bytes
            } catch (Exception e) {
                System.out.println("Error al cargar la imagen: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("No se seleccionó ningún archivo");
        }
    }

    @FXML
    private void guardarInforme(ActionEvent event) {
        String nombrePaciente = txtNom.getText();

        int pacienteId = buscarIdPaciente(nombrePaciente);

        if (pacienteId != -1) {
            String Motivo = txtMotivoConsulta.getText();
            String Hallazgos = txtHallazgos.getText();
            String fecha = txtFecha.getValue().toString();

            i.setMotivoConsulta(Motivo);
            i.setHallazgos(Hallazgos);
            i.setFecha(fecha);
            i.setPacienteIdPaciente(pacienteId);
            i.setImagenOjoIzquierdo(imagenOjoIzquierdo);
            i.setImagenOjoDerecho(imagenOjoDerecho);

            if (modificar) {
                if (i.modificar()) {
                    mostrarAlerta(Alert.AlertType.INFORMATION, "El sistema comunica:", "Modificado correctamente");
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "El sistema comunica:", "Registro no modificado.");
                }
                modificar = false;

            } else {
                if (i.insertar()) {
                    mostrarAlerta(Alert.AlertType.CONFIRMATION, "El sistema comunica:", "Insertado correctamente");
                    handleCerrar(null);
                } else {
                    mostrarAlerta(Alert.AlertType.ERROR, "El sistema comunica:", "No se pudo insertar");
                }
            }
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "El sistema comunica:", "Paciente no encontrado");
        }
    }

    private int buscarIdPaciente(String nombrePaciente) {
        String sql = "SELECT id_paciente FROM paciente WHERE nombre = ?";

        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {

            stm.setString(1, nombrePaciente);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_paciente");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return -1;
    }

    private void configurarAutocompletado(TextField textField, ObservableList<String> itemList) {
        TextFields.bindAutoCompletion(textField, param -> {
            List<String> filteredList = itemList.stream()
                    .filter(item -> item.toLowerCase().contains(param.getUserText().toLowerCase()))
                    .collect(Collectors.toList());
            return filteredList;
        });
    }

    private void cargarPacientes() {
        cargarItems("paciente", pacienteList);
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

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.show();
    }

    private void añadirTratamiento(ActionEvent event) {
        btnAñadirTratamiento.getScene().getWindow().hide();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/empresa/consultorio/tratamiento.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
        Stage stage = (Stage) btnGuardarInforme.getScene().getWindow();
        stage.close();
    }
}
