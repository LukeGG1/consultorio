package empresa.consultorio;

import clases.conexion;
import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelos.DetalleFactura;
import modelos.Factura;
import modelos.Informe;
import modelos.InformeHasFactura;
import modelos.Tratamiento;
import org.controlsfx.control.textfield.TextFields;

public class FacturaController extends conexion implements Initializable {

    @FXML
    private DatePicker txtFecha;
    @FXML
    private ComboBox<String> pagoComboBox;
    @FXML
    private TextField txtInforme;
    @FXML
    private VBox tratamientoContainer;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnGuardar;
    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtMontoTotal;
    @FXML
    private TextField txtDescuento;
    @FXML
    private TextField txtMontoFinal;
    @FXML
    private TextField txtRuc;

    private float montoTotalFinal;

    private ObservableList<Informe> informeList;

    private static final String TEXT_FIELD_STYLE = "-fx-background-color: transparent;\n"
            + "    -fx-border-width: 0px 0px 0.5px 0px; \n"
            + "    -fx-border-color: black;\n"
            + "    -fx-background-insets: 0, 0 0 1 0;\n"
            + "    -fx-focus-color: #0078d7;\n"
            + "    -fx-text-fill: black;\n"
            + "    -fx-prompt-text-fill: #A9A9A9; \n"
            + "    -fx-font-family: \"Arial\";";
    @FXML
    private TextField productoTextField;
    @FXML
    private TextField precioTextField;
    @FXML
    private TextField cantidadTextField;
    @FXML
    private TextField subtotalTextField;
    @FXML
    private TextField descuentoTextField;
    @FXML
    private JFXButton imgCerrar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pagoComboBox.setItems(FXCollections.observableArrayList("Efectivo", "Credito", "Debito", "Transferencia"));
        informeList = FXCollections.observableArrayList();
        cargarInformes(informeList);
        configurarAutocompletadoInforme(txtInforme, informeList);

        // Inicializar y agregar los campos de totalField y descuentoMontoTotalField
        txtMontoTotal.setPromptText("Monto Total");
        txtMontoTotal.setEditable(false);
        txtMontoTotal.setStyle(TEXT_FIELD_STYLE);

        txtDescuento.setPromptText("Descuento (%)");
        txtDescuento.setStyle(TEXT_FIELD_STYLE);
        txtDescuento.setOnAction(e -> actualizarMontoTotal());
    }

    public void initData(int idFactura, Factura f) {
        Factura factura = f.buscarFactura();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (factura != null) {
            txtNombre.setText(factura.getNombre());
            txtApellido.setText(factura.getApellido());
            LocalDate fecha = LocalDate.parse(factura.getFecha(), formatter);
            txtFecha.setValue(fecha);
            pagoComboBox.setValue(factura.getMetodoPago());
            txtMontoTotal.setText(String.valueOf(factura.getMontoTotal()));
            txtDescuento.setText(String.valueOf(factura.getDescuento()));
            txtRuc.setText(factura.getRuc());

            btnGuardar.setText("Modificar");
            btnGuardar.setOnAction(e -> modificar());
            btnEliminar.setVisible(true);
        } else {
            System.out.println("Factura no encontrada.");
        }
    }

    @FXML
    private void agregarOtroTratamiento(ActionEvent event) {
        agregarTratamiento("", 0, 0);
    }

    private void agregarTratamiento(String producto, float precio, int cantidad) {
        HBox hbox = new HBox(10);

        TextField productoField = new TextField(producto);
        productoField.setPromptText("Producto");
        productoField.setStyle(TEXT_FIELD_STYLE);
        HBox.setMargin(productoField, new Insets(0, 0, 0, 20));

        TextField precioField = new TextField(String.valueOf(precio));
        precioField.setPromptText("Precio");
        precioField.setStyle(TEXT_FIELD_STYLE);

        TextField cantidadField = new TextField(String.valueOf(cantidad));
        cantidadField.setPromptText("Cantidad");
        cantidadField.setStyle(TEXT_FIELD_STYLE);

        TextField subtotalField = new TextField(String.valueOf(precio * cantidad));
        subtotalField.setPromptText("Subtotal");
        subtotalField.setEditable(false);
        subtotalField.setStyle(TEXT_FIELD_STYLE);

        TextField descuentoField = new TextField("0");
        descuentoField.setPromptText("Descuento");
        descuentoField.setStyle(TEXT_FIELD_STYLE);

        Button removeButton = new Button("-");
        HBox.setMargin(removeButton, new Insets(0, 20, 0, 0));
        removeButton.setStyle("-fx-background-color: linear-gradient(to bottom right,#EEF7FF, #EEF7FF);\n"
                + "    -fx-background-radius: 50;\n"
                + "    -fx-font-family: \"Arial\"; ");
        removeButton.setOnAction(e -> {
            tratamientoContainer.getChildren().remove(hbox);
            actualizarMontoTotal();
        });

        cantidadField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                try {
                    int nuevaCantidad = Integer.parseInt(newValue);
                    float subtotal = nuevaCantidad * precio;
                    subtotalField.setText(String.valueOf(subtotal));
                    actualizarMontoTotal();
                } catch (NumberFormatException e) {
                    cantidadField.setText(oldValue);
                }
            }
        });

        descuentoField.setOnAction(e -> {
            try {
                float descuento = Float.parseFloat(descuentoField.getText());
                float subtotal = cantidad * precio;
                float subtotalConDescuento = subtotal - (subtotal * (descuento / 100));
                subtotalField.setText(String.valueOf(subtotalConDescuento));
                actualizarMontoTotal();
            } catch (NumberFormatException ex) {
                descuentoField.setText("");
            }
        });

        hbox.getChildren().addAll(productoField, precioField, cantidadField, subtotalField, descuentoField, removeButton);
        tratamientoContainer.getChildren().add(hbox);
        actualizarMontoTotal();
    }

    @FXML
    private void guardar(ActionEvent event) {
        Factura factura = new Factura();
        factura.setNombre(txtNombre.getText());
        factura.setApellido(txtApellido.getText());
        factura.setFecha(txtFecha.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        factura.setMetodoPago(pagoComboBox.getValue());
        float descuento = txtDescuento.getText().isEmpty() ? 0 : Float.parseFloat(txtDescuento.getText());
        factura.setDescuento(descuento);
        factura.setRuc(txtRuc.getText());
        factura.setPacienteIdPaciente(obtenerIdPacientePorInforme(buscarIdInforme(txtInforme.getText())));

        montoTotalFinal = Float.parseFloat(txtMontoTotal.getText()); // Guardar el monto total original
        factura.setMontoTotal(montoTotalFinal);

        int facturaId = -1;
        if (btnGuardar.getText().equals("Guardar")) {
            facturaId = factura.insertar(); // Guardar la factura y obtener el ID generado
            if (facturaId != -1) {
                System.out.println("Factura guardada exitosamente.");
            } else {
                System.out.println("Error al guardar la factura.");
                return; // Salir si hubo un error al guardar la factura
            }
        } else if (btnGuardar.getText().equals("Modificar")) {
            factura.setIdFactura(Integer.parseInt(txtInforme.getText()));
            if (factura.modificar()) {
                facturaId = factura.getIdFactura(); // Usar el ID de la factura existente
                System.out.println("Factura modificada exitosamente.");
            } else {
                System.out.println("Error al modificar la factura.");
                return; // Salir si hubo un error al modificar la factura
            }
        }

        // Guardar los tratamientos como detalles de factura
        for (Node node : tratamientoContainer.getChildren()) {
            if (node instanceof HBox) {
                HBox hbox = (HBox) node;
                TextField productoField = (TextField) hbox.getChildren().get(0);
                TextField precioField = (TextField) hbox.getChildren().get(1);
                TextField cantidadField = (TextField) hbox.getChildren().get(2);
                TextField subtotalField = (TextField) hbox.getChildren().get(3);

                String producto = productoField.getText();
                String precioStr = precioField.getText();
                String cantidadStr = cantidadField.getText();
                String subtotalStr = subtotalField.getText();

                // Verificar si los campos están vacíos
                if (producto.isEmpty() || precioStr.isEmpty() || cantidadStr.isEmpty() || subtotalStr.isEmpty()) {
                    System.out.println("Uno o más campos están vacíos.");
                    continue; // Saltar a la siguiente iteración del bucle
                }

                try {
                    float precio = Float.parseFloat(precioStr);
                    int cantidad = Integer.parseInt(cantidadStr);
                    float subtotal = Float.parseFloat(subtotalStr);

                    // Crear una instancia de DetalleFactura
                    DetalleFactura detalleFactura = new DetalleFactura();
                    detalleFactura.setCantidad(cantidad);
                    detalleFactura.setSubtotal(subtotal);
                    detalleFactura.setDescuento(descuento);
                    detalleFactura.setFacturaIdFactura(facturaId); // Usar el ID de la factura
                    detalleFactura.asignarProductoIdPorNombre(producto);

                    // Guardar el detalle de factura en la base de datos
                    if (detalleFactura.agregarDetalleFactura()) {
                        System.out.println("Detalle de factura guardado exitosamente.");
                        handleCerrar(null);
                        mostrarAlerta(Alert.AlertType.CONFIRMATION, "El sistema comunica:", "Factura guardada exitosamente.");

                    } else {
                        System.out.println("Error al guardar el detalle de factura.");
                    }

                } catch (NumberFormatException e) {
                    System.out.println("Error de formato en uno de los campos.");
                }
            }
        }

        // Guardar la relación entre la factura y el informe
        int idInforme = buscarIdInforme(txtInforme.getText());
        if (idInforme != -1 && facturaId != -1) {
            InformeHasFactura informeHasFactura = new InformeHasFactura();
            if (informeHasFactura.insertar(idInforme, facturaId)) {
                System.out.println("Relación entre factura e informe guardada exitosamente.");
            } else {
                System.out.println("Error al guardar la relación entre la factura y el informe.");
            }
        } else {
            System.out.println("ID del informe o de la factura no válido.");
        }
    }

    private void modificar() {
        Factura factura = new Factura();
        factura.setIdFactura(Integer.parseInt(txtInforme.getText()));
        factura.setNombre(txtNombre.getText());
        factura.setApellido(txtApellido.getText());
        factura.setFecha(txtFecha.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        factura.setMetodoPago(pagoComboBox.getValue());

        montoTotalFinal = Float.parseFloat(txtMontoFinal.getText()); // Guardar el monto total original
        factura.setMontoTotal(montoTotalFinal);

        if (factura.modificar()) {
            mostrarAlerta(Alert.AlertType.CONFIRMATION, "El sistema comunica:", "Factura modificada exitosamente.");

            System.out.println("Factura modificada exitosamente.");
            handleCerrar(null);
        } else {
            System.out.println("Error al modificar la factura.");
        }
    }

    @FXML
    private void eliminar(ActionEvent event) {
        Factura factura = new Factura();
        factura.setIdFactura(Integer.parseInt(txtInforme.getText())); // Asume que `txtInforme` contiene el ID de la factura

        if (factura.eliminar()) {
            mostrarAlerta(Alert.AlertType.CONFIRMATION, "El sistema comunica:", "Factura eliminada exitosamente.");
            System.out.println("Factura eliminada exitosamente.");
            handleCerrar(null);
        } else {
            System.out.println("Error al eliminar la factura.");
        }
    }

    private String getNombreInformeById(int idInforme) {
        for (Informe informe : informeList) {
            if (informe.getIdInforme() == idInforme) {
                return informe.getNombrePaciente() + " " + informe.getFecha();
            }
        }
        return "";
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

    private int buscarIdInforme(String nombreInforme) {
        for (Informe informe : informeList) {
            if ((informe.getNombrePaciente() + " " + informe.getFecha()).equals(nombreInforme)) {
                return informe.getIdInforme();
            }
        }
        return -1;
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

    private void configurarAutocompletado(TextField textField, List<String> items) {
        TextFields.bindAutoCompletion(textField, items);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && !items.contains(newValue)) {
                items.add(newValue);
            }
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

    private void configurarEnterKeyAction(TextField textField, Runnable action) {
        textField.setOnAction(event -> action.run());
    }

    private void cargarDetallesInforme(int idInforme) {
        String sql = "SELECT p.nombre AS producto, p.precio, t.cantidad "
                + "FROM tratamiento t "
                + "JOIN producto p ON t.producto_id_producto = p.id_producto "
                + "WHERE t.informe_id_informe = ?";

        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, idInforme);
            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    String producto = rs.getString("producto");
                    float precio = rs.getFloat("precio");
                    int cantidad = rs.getInt("cantidad");

                    // Llama a la función para agregar los TextFields con los datos obtenidos
                    agregarTratamiento(producto, precio, cantidad);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FacturaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void buscarInforme() {
        String nombreInforme = txtInforme.getText();
        int idInforme = buscarIdInforme(nombreInforme);

        if (idInforme != -1) {
            cargarDetallesInforme(idInforme);
        } else {
            System.out.println("Informe no encontrado.");
        }
    }

    @FXML
    private void onBuscarInforme(ActionEvent event) {
        buscarInforme();
    }

    private void actualizarMontoTotal() {
        float total = 0;
        for (Node node : tratamientoContainer.getChildren()) {
            if (node instanceof HBox) {
                HBox hbox = (HBox) node;
                TextField subtotalField = (TextField) hbox.getChildren().get(3);
                try {
                    total += Float.parseFloat(subtotalField.getText().isEmpty() ? "0" : subtotalField.getText());
                } catch (NumberFormatException e) {
                    // Ignorar campos con texto no válido
                }
            }
        }
        txtMontoTotal.setText(String.format("%.2f", total));
        aplicarDescuento();
    }

    private void aplicarDescuento() {
        try {
            float descuento = Float.parseFloat(txtDescuento.getText().isEmpty() ? "0" : txtDescuento.getText());
            float monto = Float.parseFloat(txtMontoTotal.getText().isEmpty() ? "0" : txtMontoTotal.getText());

            float montoFinal = monto - (monto * (descuento / 100));
            txtMontoFinal.setText(String.format("%.2f", montoFinal));
        } catch (NumberFormatException e) {
            txtDescuento.setText(""); // Limpia el campo en caso de error
            txtMontoFinal.setText("0.00"); // Establece monto final a 0 en caso de error
            System.out.println("Error: El descuento o el monto total no es un número válido.");
        }
    }

    private int obtenerIdPacientePorInforme(int idInforme) {
        String sql = "SELECT p.id_paciente "
                + "FROM paciente p "
                + "JOIN informe i ON p.id_paciente = i.paciente_id_paciente "
                + "WHERE i.id_informe = ?";
        try (Connection con = getCon(); PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, idInforme);
            try (ResultSet rs = stm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_paciente");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FacturaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1; // Retorna -1 si no se encuentra el paciente
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.show();
    }

    @FXML
    private void handleCerrar(ActionEvent event) {
        Stage stage = (Stage) btnGuardar.getScene().getWindow();
        stage.close();
    }
}
