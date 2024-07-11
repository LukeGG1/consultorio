package empresa.consultorio;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class SecondaryController implements Initializable{

    @FXML
    private ImageView exit,menu;
    @FXML
    private AnchorPane pane1,pane2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        exit.setOnMouseClicked(event -> {
            System.exit(0);
        });
        
        pane1.setVisible(false);
        
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5),pane1);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
        
        TranslateTransition traslateTransition = new TranslateTransition(Duration.seconds(0.5),pane2);
        traslateTransition.setByX(-600);
        traslateTransition.play();
        
        menu.setOnMouseClicked(event -> {
            pane1.setVisible(true);
            
            FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), pane1);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(0.15);
            fadeTransition.play();

            TranslateTransition traslateTransition1 = new TranslateTransition(Duration.seconds(0.5), pane2);
            traslateTransition.setByX(+600);
            traslateTransition.play(); 
        });
        
        pane1.setOnMouseClicked(event -> {
            
            FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5), pane1);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(0.15);
            fadeTransition.play();
            
            fadeTransition1.setOnFinished(event1 -> {
                pane1.setVisible(false);
            
            });
            
            TranslateTransition traslateTransition1 = new TranslateTransition(Duration.seconds(0.5), pane2);
            traslateTransition.setByX(-600);
            traslateTransition.play();
            
        
        });
        
    }
}