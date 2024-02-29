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
        // Create a new key for the current bird
        DataKey key = new DataKey(this.name.getText(), birdSize);

        // Try to find the bird in the db
        try {
            bird = database.find(key);
            showBird();
        } catch (DictionaryException ex) {
            displayAlert(ex.getMessage()); // Display error if not found
        }
    }

    public void delete() {
        BirdRecord nextBird = null;
        // Try to find the successor bird, if not assign null
        try {
            nextBird = database.successor(bird.getDataKey());
        } catch(DictionaryException de) {
            System.out.println("Could not find bird successor: " + de);
        }

        // Try to find predecessor bird, if not assign null
        BirdRecord prevBird = null;
        try {
            prevBird = database.predecessor(bird.getDataKey());
        } catch(DictionaryException de) {
            System.out.println("Could not find bird predecessor: " + de);
        }

        try {
            // Try to remove the bird from the database
            database.remove(bird.getDataKey());

        } catch (DictionaryException de) {
            // If it can't be done, return
            System.out.println("Could not remove bird: " + de);
            return;
        }

        // If the database is now empty, display an empty page
        if (database.isEmpty()) {
            this.BirdPortal.setVisible(false);
            displayAlert("No more birds in the database to show");
        }   else {
            // If there is a previous bird, display it
            if (prevBird != null) {
                bird = prevBird;
                showBird();
            } else if (nextBird != null) { // Otherwise display the next bird in line
                bird = nextBird;
                System.out.println("Bird is now " + bird.getDataKey().getBirdName());

                showBird();
            } else { // Or show nothing if there is no previous or next bird
                this.BirdPortal.setVisible(false);
                displayAlert("No more birds in the database to show");
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

        // Get key of the current bird
        DataKey key = bird.getDataKey();
        System.out.println("not the problem " + bird.getDataKey().getBirdName());

        try {
            // Try to find the successor of the current bird
            BirdRecord newBird = database.successor(bird.getDataKey());

            if(newBird == null) { // If it can't be found return
                System.out.println("error");
                return;
            }
            System.out.println(bird.getDataKey().getBirdName());
            bird = newBird;
            showBird();
        } catch (DictionaryException de) { // If error, print to console
            System.out.println("There are no elements after " + bird.getDataKey().getBirdName());
        }
    }

    public void previous() {
        // Get key of current bird
        DataKey key = bird.getDataKey();
        System.out.println("not the problem");

        try {
            // Try to find the predecessor to current bird
            BirdRecord tempBird = database.predecessor(key);
            if(tempBird == null) { // If it doesn't exist, return
                System.out.println("error");
                return;
            }
            // Show the bird
            System.out.println(bird.getDataKey().getBirdName());
            bird = tempBird;
            showBird();
        } catch (DictionaryException de) { // Catch exceptions
            System.out.println("There are no elements before " + bird.getDataKey().getBirdName());
        }
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
