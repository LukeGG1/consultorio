package empresa.consultorio;

import clases.conexion;
import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.textfield.TextFields;
import modelos.Paciente;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class PacientesController extends conexion {

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField apellidoTextField;

    @FXML
    private ComboBox<String> sexoComboBox;

    @FXML
    private DatePicker txtFechaNac;

    @FXML
    private TextField correoTextField;

    @FXML
    private TextField telefonoTextField;
    @FXML
    private VBox alergiaContainer;

    @FXML
    private TextField alergiaTextField;
    @FXML
    private VBox cirugiaContainer;

    @FXML
    private TextField cirugiaTextField;

    private ObservableList<String> alergiaList;
    private ObservableList<String> cirugiaList;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnEliminar;

    private Paciente p = new Paciente();
    private Boolean modificarP = false;
    @FXML
    private JFXButton imgCerrar;

    public void initialize() {
        alergiaList = FXCollections.observableArrayList();
        cirugiaList = FXCollections.observableArrayList();
        sexoComboBox.setItems(FXCollections.observableArrayList("Masculino", "Femenino"));
        cargarAlergias();
        cargarCirugias();
        configurarAutocompletado(alergiaTextField, alergiaList);
        configurarAutocompletado(cirugiaTextField, cirugiaList);
    }

    public void initData(Paciente pacienteSeleccionado) {
        modificarP = true;
        p = pacienteSeleccionado;
        nombreTextField.setText(p.getNombre());
        apellidoTextField.setText(p.getApellido());
        sexoComboBox.setValue(p.getSexo());
        txtFechaNac.setValue(LocalDate.parse(p.getFechaNac()));
        correoTextField.setText(p.getCorreo());
        telefonoTextField.setText(String.valueOf(p.getTelefono()));
        btnGuardar.setText("Modificar");
        btnGuardar.setOnAction(e -> {
            try {
                modificarPaciente();
            } catch (SQLException ex) {
                Logger.getLogger(PacientesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        btnEliminar.setVisible(true);

        // Cargar alergias del paciente
        p.getAlergias().forEach(this::agregarAlergia);

        // Cargar cirugías del paciente
        p.getCirugias().forEach(this::agregarCirugia);
    }

    @FXML
    private void agregarOtraAlergia() {
        agregarAlergia("");
    }

    @FXML
    private void agregarOtraCirugia() {
        agregarCirugia("");
    }

    private void agregarAlergia(String alergia) {
        HBox hbox = new HBox();
        TextField textField = new TextField(alergia);
        textField.setPromptText("Ingrese otra alergia");
        textField.setStyle("-fx-background-color: transparent;\n"
                + "    -fx-border-width: 0px 0px 0.5px 0px; \n"
                + "    -fx-border-color: black;\n"
                + "    -fx-background-insets: 0, 0 0 1 0;\n"
                + "    -fx-focus-color: #0078d7;\n"
                + "     -fx-text-fill: black;\n"
                + "     -fx-prompt-text-fill: #A9A9A9; \n"
                + "     -fx-font-family: \"Georgia\";"
        );
        hbox.setMargin(textField, new Insets(10, 0, 0, 100));
        Button button = new Button("-");
        button.setStyle("-fx-background-color: linear-gradient(to bottom right,#EEF7FF, #EEF7FF);\n"
                + "    -fx-background-radius: 50;\n"
                + "    -fx-font-family: \"Georgia\"; ");
        hbox.setMargin(button, new Insets(10, 0, 0, 15));
        button.setOnAction(e -> alergiaContainer.getChildren().remove(hbox));
        hbox.getChildren().addAll(textField, button);
        alergiaContainer.getChildren().add(hbox);
    }

    private void agregarCirugia(String cirugia) {
        HBox hbox = new HBox();
        TextField textField = new TextField(cirugia);
        textField.setPromptText("Ingrese otra cirugía");
        textField.setStyle("-fx-background-color: transparent;\n"
                + "    -fx-border-width: 0px 0px 0.5px 0px; \n"
                + "    -fx-border-color: black;\n"
                + "    -fx-background-insets: 0, 0 0 1 0;\n"
                + "    -fx-focus-color: #0078d7;\n"
                + "     -fx-text-fill: black;\n"
                + "     -fx-prompt-text-fill: #A9A9A9; \n"
                + "     -fx-font-family: \"Georgia\";"
        );
        hbox.setMargin(textField, new Insets(10, 0, 0, 100));
        Button button = new Button("-");
        button.setStyle("-fx-background-color: linear-gradient(to bottom right,#EEF7FF, #EEF7FF);\n"
                + "    -fx-background-radius: 50;\n"
                + "    -fx-font-family: \"Georgia\"; ");
        hbox.setMargin(button, new Insets(10, 0, 0, 15));
        button.setOnAction(e -> cirugiaContainer.getChildren().remove(hbox));
        hbox.getChildren().addAll(textField, button);
        cirugiaContainer.getChildren().add(hbox);
    }

    @FXML
    public void guardarPaciente() throws SQLException {
        String nombre = nombreTextField.getText().trim();
        String apellido = apellidoTextField.getText().trim();
        String sexo = sexoComboBox.getValue();
        String fechaNac = txtFechaNac.getValue().toString();
        String correo = correoTextField.getText().trim();
        String telefono = telefonoTextField.getText().trim();

        p.setNombre(nombre);
        p.setApellido(apellido);
        p.setSexo(sexo);
        p.setFechaNac(fechaNac);
        p.setCorreo(correo);
        p.setTelefono(telefono);

        if (p.createPaciente()) {
            for (javafx.scene.Node node : alergiaContainer.getChildren()) {
                if (node instanceof HBox) {
                    HBox hbox = (HBox) node;
                    TextField textField = (TextField) hbox.getChildren().get(0);
                    String nombreAlergia = textField.getText().trim();
                    if (!nombreAlergia.isEmpty()) {
                        p.verificarOAlergia(nombreAlergia);
                        p.agregarAlergiaAPaciente(nombreAlergia);
                    }
                }
            }

            for (javafx.scene.Node node : cirugiaContainer.getChildren()) {
                if (node instanceof HBox) {
                    HBox hbox = (HBox) node;
                    TextField textField = (TextField) hbox.getChildren().get(0);
                    String nombreCirugia = textField.getText().trim();
                    if (!nombreCirugia.isEmpty()) {
                        p.verificarOCirugia(nombreCirugia);
                        p.agregarCirugiaAPaciente(nombreCirugia);
                    }
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText("Paciente guardado exitosamente.");
            alert.showAndWait();
            handleCerrar(null);

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo guardar el paciente.");
            alert.showAndWait();
        }
    }

    private void agregarOtroItem(VBox container, ObservableList<String> list, String promptText) {
        HBox hbox = new HBox();
        TextField textField = new TextField();
        textField.setPromptText(promptText);
        list.add(textField.getText());
        Button button = new Button("-");
        button.setOnAction(e -> container.getChildren().remove(hbox));
        hbox.getChildren().addAll(textField, button);
        container.getChildren().add(hbox);
    }

    private void cargarAlergias() {
        try (Connection con = getCon()) {
            String sql = "SELECT nombre FROM alergia";
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                alergiaList.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarCirugias() {
        try (Connection con = getCon()) {
            String sql = "SELECT nombre FROM cirugia";
            ResultSet rs = con.createStatement().executeQuery(sql);
            while (rs.next()) {
                cirugiaList.add(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void configurarAutocompletado(TextField textField, List<String> items) {
        TextFields.bindAutoCompletion(textField, items);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !items.contains(newValue)) {
                items.add(newValue);
            }
        });
    }

    public void modificarPaciente() throws SQLException {
        String nombre = nombreTextField.getText().trim();
        String apellido = apellidoTextField.getText().trim();
        String sexo = sexoComboBox.getValue();
        String fechaNac = txtFechaNac.getValue().toString();
        String correo = correoTextField.getText().trim();
        String telefono = telefonoTextField.getText().trim();

        List<String> nuevasAlergias = alergiaContainer.getChildren().stream()
                .filter(node -> node instanceof HBox)
                .map(node -> ((HBox) node).getChildren().get(0))
                .filter(TextField.class::isInstance)
                .map(TextField.class::cast)
                .map(TextField::getText)
                .filter(text -> !text.trim().isEmpty())
                .collect(Collectors.toList());

        List<String> nuevasCirugias = cirugiaContainer.getChildren().stream()
                .filter(node -> node instanceof HBox)
                .map(node -> ((HBox) node).getChildren().get(0))
                .filter(TextField.class::isInstance)
                .map(TextField.class::cast)
                .map(TextField::getText)
                .filter(text -> !text.trim().isEmpty())
                .collect(Collectors.toList());

        if (p.modificarPaciente(nombre, apellido, sexo, fechaNac, correo, telefono, nuevasAlergias, nuevasCirugias)) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText("Paciente guardado exitosamente.");
            alert.showAndWait();
            handleCerrar(null);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo guardar el paciente.");
            alert.showAndWait();
        }
    }

    @FXML
    public void eliminarPaciente() {
        if (p != null && p.eliminarPaciente()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText("Paciente eliminado exitosamente.");
            alert.showAndWait();
            handleCerrar(null);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No se pudo eliminar el paciente.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleCerrar(ActionEvent event) {
        Stage stage = (Stage) btnGuardar.getScene().getWindow();
        stage.close();
    }
}