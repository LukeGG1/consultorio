/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package empresa.consultorio;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelos.lote;
import modelos.producto;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Milagros Taboada
 */
public class VerProductoController implements Initializable {
    @FXML
    private TableView<producto> tblProducto;
    

    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnAñadir;
    @FXML
    private Button btnVerLotes;
    @FXML
    private TableColumn<producto, String> colProducto;
    @FXML
    private TableColumn<producto, Integer> colPrecio;
    @FXML
    private TableColumn<producto, String> colDescripcion;
    
    
    private producto p = new producto();
    private ObservableList<producto> registros;
    ObservableList<producto> registrosFiltrados;
    private ObservableList<String> productosList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        productosList = FXCollections.observableArrayList();
        inicializarTabla();
        mostrarDatos();
        cargarNombresProductos(productosList);
        configurarAutocompletado(txtBuscar, productosList);
       
        // TODO
    }   
    
    @FXML
    private void busqueda(KeyEvent event) {
        registrosFiltrados = FXCollections.observableArrayList();
        String buscar = txtBuscar.getText();
        if(buscar.isEmpty()){
            tblProducto.setItems(registros);
        }else{
            registrosFiltrados.clear();
            for (producto registro : registros) {
                if(registro.getNombre().toLowerCase().contains(buscar.toLowerCase())){
                    registrosFiltrados.add(registro);
                }
            }
            tblProducto.setItems(registrosFiltrados);
        }
    }
    private void inicializarTabla() {
        colProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        
    }

    private void mostrarDatos() {
        registros = FXCollections.observableArrayList(p.consulta());
        tblProducto.setItems(registros);
    }

    @FXML
    private void Añadir(ActionEvent event) {
        abrirFormulario("Producto.fxml", "Añadir Producto", null);
    }

    @FXML
    private void verLotes(ActionEvent event) {
        abrirFxml("inventario.fxml","Ver producto");
        
    }
    
    private void abrirFxml(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(VerProductoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @FXML
    private void modificarProducto(MouseEvent event) {
        producto productoSeleccionado = tblProducto.getSelectionModel().getSelectedItem();
        if (productoSeleccionado != null) {
            abrirFormulario("productoEditar.fxml", "Editar producto", productoSeleccionado);
        }
    }
    private void abrirFormulario(String fxml, String titulo,producto productoExistente) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle(titulo);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL); // Bloquear interacción con otras ventanas

        // Si se proporciona un lote existente, inicializar el controlador del formulario
        if (productoExistente != null) {
            ProductoEditarController controller = loader.getController();
            controller.initData(productoExistente.getIdProducto(), productoExistente);
        }

        stage.showAndWait();

        // Actualizar tabla después de cerrar formulario
        actualizarTabla();

    } catch (IOException ex) {
        ex.printStackTrace();
    }
    }
    
    private void actualizarTabla() {
        registros.clear();
        registros.addAll(p.consulta());
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

   
}
