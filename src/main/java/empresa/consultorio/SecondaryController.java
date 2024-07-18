package empresa.consultorio;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import modelos.lote;

public class SecondaryController implements Initializable {
    private Button btnPacientes;
    private Button btnProductos;
    private Button btnContabilidad;
    private Button btnAyuda;

    @FXML
    private ImageView exit, menu;
    @FXML
    private AnchorPane pane1, pane2;

    private boolean isMenuOpen = false;
    @FXML
    private JFXButton btnInventario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        exit.setOnMouseClicked(event -> {
            System.exit(0);
        });

        // Inicializar el menú como cerrado
        pane1.setVisible(false);
        pane2.setTranslateX(-pane2.getPrefWidth()); // Asegúrate de que pane2 esté fuera de la vista

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
        translateTransition.setToX(-pane2.getWidth());
        translateTransition.play();
    }

    public void switchToPacientes(ActionEvent event) {
        btnPacientes.getScene().getWindow().hide();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/empresa/consultorio/secondary.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToProductos(ActionEvent event) {
        btnProductos.getScene().getWindow().hide();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/empresa/consultorio/secondary.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToContabilidad(ActionEvent event) {
        btnContabilidad.getScene().getWindow().hide();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/empresa/consultorio/secondary.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToAyuda(ActionEvent event) {
        btnAyuda.getScene().getWindow().hide();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/empresa/consultorio/secondary.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void abrirInventario(ActionEvent event) {
        abrirFormulario("inventario.fxml", "Ver inventario", null);
    }
    
    
    private void abrirFormulario(String fxml, String titulo, lote loteExistente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquear interacción con otras ventanas

            // Si se proporciona un lote existente, inicializar el controlador del formulario
            if (loteExistente != null) {
                LoteEditarController controller = loader.getController();
                controller.initData(loteExistente.getIdLote(), loteExistente);
            }

            stage.showAndWait();

           

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
