package assignment.birds;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ouda
 */
public class BirdsController implements Initializable {

    @FXML
    private MenuBar mainMenu;
    @FXML
    private ImageView image;
    @FXML
    private BorderPane BirdPortal;
    @FXML
    private Label title;
    @FXML
    private Label about;
    @FXML
    private Button play;
    @FXML
    private Button puase;
    @FXML
    private ComboBox size;
    @FXML
    private TextField name;
    Media media;
    MediaPlayer player;
    OrderedDictionary database = null;
    BirdRecord bird = null;
    int birdSize = 1;

    @FXML
    public void exit() {
        Stage stage = (Stage) mainMenu.getScene().getWindow();
        stage.close();
    }

    public void find() {
        DataKey key = new DataKey(this.name.getText(), birdSize);
        try {
            bird = database.find(key);
            showBird();
        } catch (DictionaryException ex) {
            displayAlert(ex.getMessage());
        }
    }

    public void delete() {
        BirdRecord previousBird = null;
        try {
            previousBird = database.predecessor(bird.getDataKey());
        } catch (DictionaryException ex) {

        }
        BirdRecord nextBird = null;
        try {
            nextBird = database.successor(bird.getDataKey());
        } catch (DictionaryException ex) {

        }
        DataKey key = bird.getDataKey();
        try {
            database.remove(key);
        } catch (DictionaryException ex) {
            System.out.println("Error in delete "+ ex);
        }
        if (database.isEmpty()) {
            this.BirdPortal.setVisible(false);
            displayAlert("No more birds in the database to show");
        } else {
            if (previousBird != null) {
                bird = previousBird;
                showBird();
            } else if (nextBird != null) {
                bird = nextBird;
                showBird();
            }
        }
    }

    private void showBird() {
        play.setDisable(false);
        puase.setDisable(true);
        if (player != null) {
            player.stop();
        }
        String img = bird.getImage();
        Image birdImage = new Image("file:src/main/resources/assignment/birds/images/" + img);
        image.setImage(birdImage);
        title.setText(bird.getDataKey().getBirdName());
        about.setText(bird.getAbout());
    }

    private void displayAlert(String msg) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Alert.fxml"));
            Parent ERROR = loader.load();
            AlertController controller = (AlertController) loader.getController();

            Scene scene = new Scene(ERROR);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.getIcons().add(new Image("file:src/main/resources/assignment/birds/images/UMIcon.png"));
            stage.setTitle("Dictionary Exception");
            controller.setAlertText(msg);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException ex1) {

        }
    }

    public void getSize() {
        switch (this.size.getValue().toString()) {
            case "Small":
                this.birdSize = 1;
                break;
            case "Medium":
                this.birdSize = 2;
                break;
            case "Large":
                this.birdSize = 3;
                break;
            default:
                break;
        }
    }

    public void first() {
        // Write this method
        bird = database.smallest();
        System.out.println(bird.getDataKey().getBirdName());
        database.inorder();
        showBird();
    }

    public void last() {
        // Write this method
        bird = database.largest();
        System.out.println(bird.getDataKey().getBirdName());
        showBird();
    }

    public void next() {
        // Write this method;
    }

    public void previous() {
        // Write this method
    }

    public void play() {
        String filename = "src/main/resources/assignment/birds/sounds/" + bird.getSound();
        media = new Media(new File(filename).toURI().toString());
        player = new MediaPlayer(media);
        play.setDisable(true);
        puase.setDisable(false);
        player.play();
    }

    public void puase() {
        play.setDisable(false);
        puase.setDisable(true);
        if (player != null) {
            player.stop();
        }
    }

    public void loadDictionary() {
        Scanner input;
        int line = 0;
        try {
            String birdName = "";
            String description;
            int size = 0;
            input = new Scanner(new File("BirdsDatabase.txt"));
            while (input.hasNext()) // read until  end of file
            {
                String data = input.nextLine();
                switch (line % 3) {
                    case 0:
                        size = Integer.parseInt(data);
                        break;
                    case 1:
                        birdName = data;
                        break;
                    default:
                        description = data;
                        database.insert(new BirdRecord(new DataKey(birdName, size), description, birdName + ".mp3", birdName + ".jpg"));
                        break;
                }
                line++;
            }
        } catch (IOException e) {
            System.out.println("There was an error in reading or opening the file: BirdsDatabase.txt");
            System.out.println(e.getMessage());
        } catch (DictionaryException ex) {
            Logger.getLogger(BirdsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.BirdPortal.setVisible(true);
        this.first();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        database = new OrderedDictionary();
        size.setItems(FXCollections.observableArrayList(
                "Small", "Medium", "Large"
        ));
        size.setValue("Small");
    }

}
