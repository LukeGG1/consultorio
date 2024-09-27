package empresa.consultorio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.application.Platform;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;
    private static final String initialTitle = "Login";
    private double x, y = 0;
    private static final String CSS_PATH = "/empresa/consultorio/stylePaciente.css"; // Ruta del archivo CSS

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/empresa/consultorio/primary.fxml"));
        Parent root = loader.load();

        // Establecer estilos de ventana sin decoraciones
        stage.initStyle(StageStyle.UNDECORATED);

        // Permitir arrastrar la ventana
        root.setOnMousePressed(event -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            primaryStage.setX(event.getScreenX() - x);
            primaryStage.setY(event.getScreenY() - y);
        });

        // Configurar escena y mostrar la ventana
        scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(CSS_PATH).toExternalForm()); // Cargar el CSS
        primaryStage.setTitle(initialTitle);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();

        // Ejecutar el centrado de la ventana después de que esté completamente cargada
        primaryStage.show();
        Platform.runLater(() -> centerStage(primaryStage));
    }

    public static void setRoot(String fxml, String titulo) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml));
        Parent root = fxmlLoader.load();
        scene.setRoot(root);
        scene.getStylesheets().clear(); // Limpiar estilos anteriores
        scene.getStylesheets().add(App.class.getResource(CSS_PATH).toExternalForm()); // Cargar el CSS
        primaryStage.setTitle(titulo);
        primaryStage.sizeToScene();
        Platform.runLater(() -> centerStage(primaryStage));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static void centerStage(Stage stage) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        stage.setX((bounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((bounds.getHeight() - stage.getHeight()) / 2);
    }

    // Método para cambiar de vista
    public static void switchView(String fxml, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml));
        Parent root = fxmlLoader.load();
        scene.setRoot(root);
        scene.getStylesheets().clear(); // Limpiar estilos anteriores
        scene.getStylesheets().add(App.class.getResource(CSS_PATH).toExternalForm()); // Cargar el CSS
        primaryStage.setTitle(title);
        primaryStage.sizeToScene();
        Platform.runLater(() -> centerStage(primaryStage));
    }
}