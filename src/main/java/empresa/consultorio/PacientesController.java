package empresa.consultorio;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage; // Importar la clase Stage
import org.controlsfx.control.textfield.TextFields;

import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

public class PacientesController {

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField apellidoTextField;

    @FXML
    private ComboBox<String> sexoComboBox;

    @FXML
    private TextField edadTextField;

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

    // Referencia al escenario principal
    private Stage stage;

    // Método para establecer el escenario
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        alergiaList = FXCollections.observableArrayList();
        cirugiaList = FXCollections.observableArrayList();
        sexoComboBox.setItems(FXCollections.observableArrayList("Masculino", "Femenino", "Otro"));
        cargarAlergias();
        cargarCirugias();
        configurarAutocompletado(alergiaTextField, alergiaList);
        configurarAutocompletado(cirugiaTextField, cirugiaList);
    }

    @FXML
    private void agregarOtraAlergia() {
        agregarOtroItem(alergiaContainer, alergiaList, "Ingrese otra alergia");
    }

    @FXML
    private void agregarOtraCirugia() {
        agregarOtroItem(cirugiaContainer, cirugiaList, "Ingrese otra cirugía");
    }

    @FXML
    public void guardarPaciente() {
        String nombre = nombreTextField.getText().trim();
        String apellido = apellidoTextField.getText().trim();
        String sexo = sexoComboBox.getValue();
        int edad;
        try {
            edad = Integer.parseInt(edadTextField.getText().trim());
        } catch (NumberFormatException e) {
            mostrarMensaje("Error de Formato", "La edad debe ser un número.", Alert.AlertType.ERROR);
            return;
        }
        String correo = correoTextField.getText().trim();
        String telefono = telefonoTextField.getText().trim();

        String insertPacienteSql = "INSERT INTO paciente (nombre, apellido, sexo, edad, correo, telefono) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/consultorio", "root", "");
             PreparedStatement stm = con.prepareStatement(insertPacienteSql, Statement.RETURN_GENERATED_KEYS)) {

            stm.setString(1, nombre);
            stm.setString(2, apellido);
            stm.setString(3, sexo);
            stm.setInt(4, edad);
            stm.setString(5, correo);
            stm.setString(6, telefono);
            stm.executeUpdate();

            int idPaciente;
            try (ResultSet generatedKeys = stm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    idPaciente = generatedKeys.getInt(1);
                    guardarItemsPaciente(idPaciente, alergiaContainer, "paciente_has_alergia", "alergia");
                    guardarItemsPaciente(idPaciente, cirugiaContainer, "paciente_has_cirugia", "cirugia");

                    // Cerrar la ventana
                    stage.close();
                } else {
                    throw new SQLException("No se pudo obtener el ID del paciente insertado.");
                }
            }

            limpiarCampos();
            mostrarMensaje("Paciente Guardado", "El paciente ha sido guardado exitosamente.", Alert.AlertType.INFORMATION);

        } catch (SQLException ex) {
            ex.printStackTrace();
            mostrarMensaje("Error al Guardar Paciente", "Hubo un error al intentar guardar al paciente. Por favor, inténtelo de nuevo.", Alert.AlertType.ERROR);
        }
    }

    private void guardarItemsPaciente(int idPaciente, VBox container, String tableName, String itemName) {
        String insertItemSql = "INSERT INTO " + tableName + " (paciente_id_paciente, " + itemName + "id" + itemName + ") VALUES (?, (SELECT id_" + itemName + " FROM " + itemName + " WHERE nombre = ?))";
        for (javafx.scene.Node node : container.getChildren()) {
            if (node instanceof HBox) {
                HBox hbox = (HBox) node;
                TextField textField = (TextField) hbox.getChildren().get(0); // Assuming TextField is always the first child
                String nombreItem = textField.getText().trim();
                if (!nombreItem.isEmpty()) {
                    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/consultorio", "root", "");
                         PreparedStatement stm = con.prepareStatement(insertItemSql)) {

                        stm.setInt(1, idPaciente);
                        stm.setString(2, nombreItem);
                        stm.executeUpdate();

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }

    private void agregarOtroItem(VBox container, ObservableList<String> itemList, String promptText) {
        HBox hbox = new HBox();
        TextField nuevaTextField = new TextField();
        nuevaTextField.setPromptText(promptText);
        Button removeButton = new Button("-");
        removeButton.setOnAction(e -> container.getChildren().remove(hbox));
        hbox.getChildren().addAll(nuevaTextField, removeButton);
        configurarAutocompletado(nuevaTextField, itemList);
        container.getChildren().add(hbox);
    }

    private void cargarAlergias() {
        cargarItems("alergia", alergiaList);
    }

    private void cargarCirugias() {
        cargarItems("cirugia", cirugiaList);
    }

    private void cargarItems(String itemName, ObservableList<String> itemList) {
        String sql = "SELECT nombre FROM " + itemName;
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

    private void configurarAutocompletado(TextField textField, ObservableList<String> itemList) {
    TextFields.bindAutoCompletion(textField, param -> {
        List<String> filteredList = itemList.stream()
                .filter(item -> item.toLowerCase().contains(param.getUserText().toLowerCase()))
                .limit(3) // Limitar a 3 resultados
                .collect(Collectors.toList());
        return filteredList;
    });
}

    private void limpiarCampos() {
        nombreTextField.clear();
        apellidoTextField.clear();
        sexoComboBox.getSelectionModel().clearSelection();
        edadTextField.clear();
        correoTextField.clear();
        telefonoTextField.clear();
        alergiaContainer.getChildren().clear();
        cirugiaContainer.getChildren().clear();
        agregarOtraAlergia();
        agregarOtraCirugia();
    }

    private void mostrarMensaje(String titulo, String mensaje, Alert.AlertType tipoAlerta) {
        Alert alerta = new Alert(tipoAlerta);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}