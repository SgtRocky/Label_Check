package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class Controller {

    @FXML
    private TextField masterLabel;
    @FXML
    public TextField packingLabelOne;
    @FXML
    private TextField packingLabelTwo;
    @FXML
    private TextField packingLabelThree;
    @FXML
    private Button oK;
    @FXML
    private TextField outcome;
    @FXML
    private TextField userID;
    @FXML
    private TextField tabDisplay;
    @FXML
    public Label myDate;
    @FXML
    public Label myTime;
    @FXML
    public Label labelTimer;

    public Scanner scan;

    public Scanner scanConf;

    public String lineX;

    public String confLine1;
    public String confLine2;

    public AudioClip soundHorn;


    String har;
    String tab;
    String date;
    String time;
    int entryTimer;

    @FXML
    public void initialize() throws IOException {

        File configFile = new File("config.txt");
        File labelConfig = new File("lblconfig.txt");
        File errorSound = new File("Error.mp3");



        //Checking if config file exists
        if (!errorSound.exists()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            playError();
            alert.setTitle("Missing Sound Files");
            alert.setHeaderText("Sound Files Are Missing.");
            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setDefaultButton(false);
            alert.showAndWait();
        }else{
            soundHorn = new AudioClip(errorSound.toURI().toString());
        }
        if (!configFile.exists()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            playError();
            alert.setTitle("Config File Missing");
            alert.setHeaderText("Config.txt File is missing");
            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setDefaultButton(false);
            okButton.setOnAction((event) -> Platform.exit());
            alert.showAndWait();


        }
        if (!labelConfig.exists()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            playError();
            alert.setTitle("Config File Missing");
            alert.setHeaderText("lblconfig.txt File is missing");
            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setDefaultButton(false);
            okButton.setOnAction((event) -> Platform.exit());
            alert.showAndWait();



        }

        scanConf = new Scanner(configFile);
        scanConfig();
        packingLabelOne.setDisable(true);
        masterLabel.setDisable(true);
        outcome.setDisable(true);
        tabDisplay.setDisable(true);
        packingLabelTwo.setDisable(true);
        packingLabelThree.setDisable(true);
        handleUserIdEnter();
        handlePackingOneEnter();
        handleMasterLabelEnter();
        handlePackingTwoEnter();
        handlePackingThreeEnter();


        //Clock for the interface and the final printed label
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, actionEvent -> {
            Calendar cal = Calendar.getInstance();
            int second = LocalTime.now().getSecond();
            int minute = LocalTime.now().getMinute();
            int hour = LocalTime.now().getHour();
            int year = LocalDate.now().getYear();
            int month = cal.get(Calendar.MONTH);
            int day = LocalDate.now().getDayOfMonth();
            month += 1;
            entryTimer++;

            // Timer is required limits user while scanning to 3 seconds per scan
            if (entryTimer >= 3) {
                entryTimer = 3;
            }

            myTime.setText(hour + ":" + minute + ":" + second);
            myTime.setFont(Font.font("Arial", 12));
            myDate.setText(year + "-" + month + "-" + day);
            myDate.setFont(Font.font("Arial", 12));
            labelTimer.setFont(Font.font("Arial", 60));
            labelTimer.setText(String.valueOf(entryTimer));
        }),


                new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void playError() {
        soundHorn.play();
    }

    //Checking if tab nr is entered correctly with HL prefix and 8 digits
    //If entered correctly unlocks harnessOne text field
    //Resets timer to prohibit scanning
    @FXML
    public void textFieldDisable() {
        int lenth = userID.getLength();
        //Checking if tab nr has HL prefix and 8 digits
        if (userID.getText().startsWith("HL") && lenth == 8) {
            String text = userID.getText();
            boolean disableButtons = text.isEmpty() || text.trim().isEmpty();
            masterLabel.setDisable(disableButtons);
            entryTimer = 0;
        }
    }

    //Checking if masterLabel text field is entered
    //If entered unlock harnessTwo field
    //Also add person barcode to display TexField tabDisplay
    @FXML
    public void onMasterLabelEntered() {
        String text = masterLabel.getText();
        boolean disableButtons = text.isEmpty() || text.trim().isEmpty();
        userID.setDisable(true);
        packingLabelOne.setDisable(disableButtons);
        text = userID.getText();
        text = text.substring(1);
        tabDisplay.setText(text);
        tabDisplay.setFont(Font.font("Arial", 20));
    }

    //Adding prefix to harness code in order to make it match with test label prefix
    public String addPrefix(String t) {
        return confLine1 + t;
    }

    //Reset button function deletes all fields and enables tab nr field
    public void reset() {
        packingLabelOne.setText("");
        masterLabel.setText("");
        packingLabelOne.setDisable(true);
        masterLabel.setDisable(true);
        outcome.setText("");
        userID.setText("");
        tabDisplay.setText("");
        userID.setDisable(false);
        packingLabelTwo.setDisable(true);
        packingLabelThree.setDisable(true);
        packingLabelTwo.setText("");
        packingLabelThree.setText("");
        entryTimer = 0;
    }

    public void handleUserIdEnter() {
        userID.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                masterLabel.requestFocus();
            }
        });
    }

    public void handleMasterLabelEnter() {
        masterLabel.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER) && entryTimer >= 3) {
                packingLabelOne.setDisable(false);
                packingLabelOne.requestFocus();
                packingLabelOne.setText("");
                masterLabel.setDisable(true);
                entryTimer = 0;

            } else if (entryTimer < 3) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                playError();
                alert.setTitle("Error");
                alert.setHeaderText("Please wait 3 seconds before scanning the next label");
                Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setDefaultButton(false);
                okButton.requestFocus();
                alert.showAndWait();
            }
        });
    }

    public void handlePackingOneEnter() {
        packingLabelOne.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER) && entryTimer >= 3) {
                packingLabelTwo.setDisable(false);
                packingLabelTwo.requestFocus();
                packingLabelTwo.setText("");
                packingLabelOne.setDisable(true);
                entryTimer = 0;
            } else if (entryTimer < 3) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                playError();
                alert.setTitle("Error");
                alert.setHeaderText("Please wait 3 seconds before scanning the next label");
                Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setDefaultButton(false);
                alert.showAndWait();
            }
        });
    }



    public void handlePackingTwoEnter() {
        packingLabelTwo.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER) && entryTimer >= 3) {
                packingLabelThree.setDisable(false);
                packingLabelThree.requestFocus();
                packingLabelThree.setText("");
                packingLabelTwo.setDisable(true);
                entryTimer = 0;
            } else if (entryTimer < 3) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                playError();
                alert.setTitle("Error");
                alert.setHeaderText("Please wait 3 seconds before scanning the next label");
                Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setDefaultButton(false);
                alert.showAndWait();
            }
        });
    }

    public void handlePackingThreeEnter() {
        packingLabelThree.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                oK.fire();
                masterLabel.setText("");
                masterLabel.requestFocus();
                masterLabel.setDisable(false);
                packingLabelThree.setDisable(true);
            } else if (entryTimer < 3) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                playError();
                alert.setTitle("Error");
                alert.setHeaderText("Please wait 3 seconds before scanning the next label");
                Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setDefaultButton(false);
                alert.showAndWait();
            }
        });
    }

    //When button ok pressed following code checks if data fields match, if match print label
    @FXML
    public void onButtonClicked() throws IOException {
        if (packingLabelOne.getText() == null || masterLabel.getText() == null || packingLabelTwo.getText() == null || packingLabelThree.getText() == null) {
            outcome.setText("Empty field");
            outcome.setFont(Font.font("Arial", 20));
            outcome.setStyle("-fx-text-inner-color: blue;");
        } else if (addPrefix(packingLabelOne.getText()).equals(masterLabel.getText()) && packingLabelOne.getText().equals(packingLabelTwo.getText())
                && packingLabelOne.getText().equals(packingLabelThree.getText()) && packingLabelTwo.getText().equals(packingLabelThree.getText())) {
            outcome.setText("OK");
            outcome.setFont(Font.font("Arial", 20));
            outcome.setStyle("-fx-text-inner-color: green;");
            scan = new Scanner(new File("lblconfig.txt"));
            har = packingLabelOne.getText();
            tab = tabDisplay.getText();
            date = myDate.getText();
            time = myTime.getText();
            loadSettingsAndPrint();
            write("log.txt", "a");

        } else {
            outcome.setText("NOK");
            outcome.setFont(Font.font("Arial", 20));
            outcome.setStyle("-fx-text-inner-color: red;");
            write("log.txt", "a");
        }
    }
    //Reading line 2 and 4 from the config file
    public void scanConfig() {
        scanConf.nextLine();
        confLine1 = scanConf.nextLine();
        scanConf.nextLine();
        confLine2 = scanConf.nextLine();
    }

    //Reading label configuration txt file, and writing the label file then executing it via CMD
    public void loadSettingsAndPrint(){

        List<String> lines = new ArrayList<>();

        scan.nextLine();
        scan.nextLine();
        scan.nextLine();
        scan.nextLine();

        for (int i = 0; i < 30; i++) {

            lineX = scan.nextLine();
            lineX = lineX.replace("harness", har);
            lineX = lineX.replace("tab", tab);
            lineX = lineX.replace("date", date);
            lineX = lineX.replace("time", time);

            lines.add(lineX);

        }

        try {

        File fileObject = new File("label.txt");
        fileObject.createNewFile();
        FileWriter fileW = new FileWriter("label.txt");
        BufferedWriter bufferW = new BufferedWriter(fileW);

        for (String s : lines) {
            System.out.println(s);
            bufferW.write(s);
            bufferW.newLine();
        }
        bufferW.close();

        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", ("COPY label.txt " + confLine2));
        builder.redirectErrorStream(true);
        builder.start();
        System.out.println("Run");

        }catch (IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            playError();
            alert.setTitle("Print Error");
            alert.setHeaderText("Printing failed check CMD processes in task manager");
            alert.setContentText(e.getMessage());
            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setDefaultButton(false);
            okButton.setOnAction((event) -> reset());
            alert.showAndWait();
        }
    }


    //writing log.txt file
    protected static String defaultLogFile = "log.txt";

    public static void write(String s) {
        write(defaultLogFile);
    }

    public void write(String f, String s) throws IOException {
        String harness = packingLabelOne.getText();
        String harness2 = masterLabel.getText();
        String outcome = this.outcome.getText();
        String date = myDate.getText();
        String time = myTime.getText();
        String tab = tabDisplay.getText();
        FileWriter aWriter = new FileWriter(f, true);
        aWriter.write(date + ";" + outcome + ";" + harness2 + ";" + harness + ";" + time + ";" + tab + System.getProperty("line.separator"));
        aWriter.flush();
        aWriter.close();
    }
}