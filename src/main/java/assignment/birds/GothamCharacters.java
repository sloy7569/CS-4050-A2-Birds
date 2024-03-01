package assignment.gotham_characters;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Ouda
 */
public class GothamCharacters extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("birds-view.fxml"));

        Scene scene = new Scene(root);

        stage.getIcons().add(new Image("file:src/main/resources/assignment/gotham_characters/images/UMIcon.png"));
        stage.setTitle("Gotham Portal");

        stage.setScene(scene);
        stage.show();


    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
