package empresa.consultorio;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class CrearInformeController implements Initializable {

    @FXML
    private Button btnIzquierdo;
    @FXML
    private Button btnDerecho;
    @FXML
    private ImageView imIzquierdo;
    @FXML
    private ImageView imDerecho;

    private final FileChooser fc = new FileChooser();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configurar directorio inicial y filtros de extensión
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        fc.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Archivos de imagen", "*.png", "*.jpg", "*.gif")
        );
    }

    @FXML
    private void agregarOjoIzq(ActionEvent event) {
        File file = fc.showOpenDialog(null);
        if (file != null) {
            try {
                Image image = new Image(file.toURI().toURL().toExternalForm());
                imIzquierdo.setImage(image);
            } catch (Exception e) {
                System.out.println("Error al cargar la imagen: " + e.getMessage());
                e.printStackTrace();  // Imprime el rastreo de la pila para ver el origen del error
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
            } catch (Exception e) {
                System.out.println("Error al cargar la imagen: " + e.getMessage());
                e.printStackTrace();  // Imprime el rastreo de la pila para ver el origen del error
            }
        } else {
            System.out.println("No se seleccionó ningún archivo");
        }
    }
}
