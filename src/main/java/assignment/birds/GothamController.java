package assignment.gotham_characters;

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
public class GothamController implements Initializable {

    @FXML
    private MenuBar mainMenu;
    @FXML
    private ImageView image;
    @FXML
    private BorderPane GothamPortal;
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
    GothamRecord character = null;
    int characterSize = 1;

    @FXML
    public void exit() {
        Stage stage = (Stage) mainMenu.getScene().getWindow();
        stage.close();
    }

    public void find() {
        // Create a new key for the current character
        DataKey key = new DataKey(this.name.getText(), characterSize);

        // Try to find the character in the db
        try {
            character = database.find(key);
            showCharacter();
        } catch (DictionaryException ex) {
            displayAlert(ex.getMessage()); // Display error if not found
        }
    }

    public void delete() {
        GothamRecord nextCharacter = null;
        // Try to find the successor character, if not assign null
        try {
            nextCharacter = database.successor(character.getDataKey());
        } catch(DictionaryException de) {
            System.out.println("Could not find character successor: " + de);
        }

        // Try to find predecessor character, if not assign null
        GothamRecord prevCharacter = null;
        try {
            prevCharacter = database.predecessor(character.getDataKey());
        } catch(DictionaryException de) {
            System.out.println("Could not find character predecessor: " + de);
        }

        try {
            // Try to remove the character from the database
            database.remove(character.getDataKey());

        } catch (DictionaryException de) {
            // If it can't be done, return
            System.out.println("Could not remove character: " + de);
            return;
        }

        // If the database is now empty, display an empty page
        if (database.isEmpty()) {
            this.GothamPortal.setVisible(false);
            displayAlert("No more birds in the database to show");
        }   else {
            // If there is a previous character, display it
            if (prevCharacter != null) {
                character = prevCharacter;
                showCharacter();
            } else if (nextCharacter != null) { // Otherwise display the next character in line
                character = nextCharacter;
                System.out.println("Bird is now " + character.getDataKey().getCharacterName());

                showCharacter();
            } else { // Or show nothing if there is no previous or next character
                this.GothamPortal.setVisible(false);
                displayAlert("No more birds in the database to show");
            }
        }
    }

    private void showCharacter() {
        play.setDisable(false);
        puase.setDisable(true);
        if (player != null) {
            player.stop();
        }
        String img = character.getImage();
        Image characterImage = new Image("file:src/main/resources/assignment/birds/images/" + img);
        image.setImage(characterImage);
        title.setText(character.getDataKey().getCharacterName());
        about.setText(character.getAbout());
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
                this.characterSize = 1;
                break;
            case "Medium":
                this.characterSize = 2;
                break;
            case "Large":
                this.characterSize = 3;
                break;
            default:
                break;
        }
    }

    public void first() {
        // Write this method
        character = database.smallest();
        System.out.println(character.getDataKey().getCharacterName());
        database.inorder();
        showCharacter();
    }

    public void last() {
        // Write this method
        character = database.largest();
        System.out.println(character.getDataKey().getCharacterName());
        showCharacter();
    }

    public void next() {
        // Write this method;

        // Get key of the current character
        DataKey key = character.getDataKey();
        System.out.println("not the problem " + character.getDataKey().getCharacterName());

        try {
            // Try to find the successor of the current character
            GothamRecord newCharacter = database.successor(character.getDataKey());

            if(newCharacter == null) { // If it can't be found return
                System.out.println("error");
                return;
            }
            System.out.println(character.getDataKey().getCharacterName());
            character = newCharacter;
            showCharacter();
        } catch (DictionaryException de) { // If error, print to console
            System.out.println("There are no elements after " + character.getDataKey().getCharacterName());
        }
    }

    public void previous() {
        // Get key of current character
        DataKey key = character.getDataKey();
        System.out.println("not the problem");

        try {
            // Try to find the predecessor to current character
            GothamRecord tempCharacter = database.predecessor(key);
            if(tempCharacter == null) { // If it doesn't exist, return
                System.out.println("error");
                return;
            }
            // Show the character
            System.out.println(character.getDataKey().getCharacterName());
            character = tempCharacter;
            showCharacter();
        } catch (DictionaryException de) { // Catch exceptions
            System.out.println("There are no elements before " + character.getDataKey().getCharacterName());
        }
    }

    public void play() {
        String filename = "src/main/resources/assignment/birds/sounds/" + character.getSound();
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
            String characterName = "";
            String description;
            int size = 0;
            input = new Scanner(new File("GothamDatabase.txt"));
            while (input.hasNext()) // read until  end of file
            {
                String data = input.nextLine();
                switch (line % 3) {
                    case 0:
                        size = Integer.parseInt(data);
                        break;
                    case 1:
                        characterName = data;
                        break;
                    default:
                        description = data;
                        database.insert(new GothamRecord(new DataKey(characterName, size), description, characterName + ".mp3", characterName + ".jpg"));
                        break;
                }
                line++;
            }
        } catch (IOException e) {
            System.out.println("There was an error in reading or opening the file: GothamDatabase.txt");
            System.out.println(e.getMessage());
        } catch (DictionaryException ex) {
            Logger.getLogger(GothamController.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.GothamPortal.setVisible(true);
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
