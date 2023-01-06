package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
    public TextField harnessOne;
    @FXML
    private TextField harnessTwo;
    @FXML
    private TextField harnessThree;
    @FXML
    private TextField harnessFour;
    @FXML
    private Button oK;
    @FXML
    private TextField ats;
    @FXML
    private TextField tabNr;
    @FXML
    private TextField tabDisplay;
    @FXML
    public Label myDate;
    @FXML
    public Label myTime;
    @FXML
    public Label lblTimer;

    public Scanner scan;

    public Scanner scanConf;

    public String lineX;

    public String confLine1;
    public String confLine2;

    public AudioClip soundHorn = new AudioClip(new File("Error.mp3").toURI().toString());


    String har;
    String tab;
    String date;
    String time;
    int timer;

    @FXML
    public void initialize() throws IOException {

        File configure = new File("config.txt");
        File labelConfigure = new File("lblconfig.txt");

        //Checking if config file exists
        if (!configure.exists()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            playError();
            alert.setTitle("Config File Missing");
            alert.setHeaderText("Config.txt File is missing");
            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setDefaultButton(false);
            alert.showAndWait();
        } else if (!labelConfigure.exists()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            playError();
            alert.setTitle("Config File Missing");
            alert.setHeaderText("lblconfig.txt File is missing");
            Button okButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setDefaultButton(false);
            alert.showAndWait();
        }

        scanConf = new Scanner(new File("config.txt"));

        scanSettings();
        harnessOne.setDisable(true);
        harnessTwo.setDisable(true);
        ats.setDisable(true);
        tabDisplay.setDisable(true);
        harnessThree.setDisable(true);
        harnessFour.setDisable(true);
        handleTabNrEnter();
        handleHarnessOneEnter();
        handleHarnessTwoEnter();
        handleHarnessThreeEnter();
        handleHarnessFourEnter();


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
            timer++;

            // Timer is required limits user while scanning to 3 seconds per scan
            if (timer >= 3) {
                timer = 3;
            }

            myTime.setText(hour + ":" + minute + ":" + second);
            myTime.setFont(Font.font("Arial", 12));
            myDate.setText(year + "-" + month + "-" + day);
            myDate.setFont(Font.font("Arial", 12));
            lblTimer.setFont(Font.font("Arial", 60));
            lblTimer.setText(String.valueOf(timer));
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
    //Resets timer to progibit scanning
    @FXML
    public void textFieldDisable() {
        int lenth = tabNr.getLength();
        //Checking if tab nr has HL prefix and 8 digits
        if (tabNr.getText().startsWith("HL") && lenth == 8) {
            String text = tabNr.getText();
            boolean disableButtons = text.isEmpty() || text.trim().isEmpty();
            harnessTwo.setDisable(disableButtons);
            timer = 0;
        }
    }

    //Checking if harnessOne text field is entered
    //If entered unlock harnessTwo field
    //Also add person barcode to display TexField tabDisplay
    @FXML
    public void onHarnessOneEntered() {
        String text = harnessTwo.getText();
        boolean disableButtons = text.isEmpty() || text.trim().isEmpty();
        tabNr.setDisable(true);
        harnessOne.setDisable(disableButtons);
        text = tabNr.getText();
        text = text.substring(1);
        tabDisplay.setText(text);
        tabDisplay.setFont(Font.font("Arial", 20));
    }

    //Adding prefix to harness code in order to make it match with test label prefix
    public String harnessAddPrefix(String harn) {
        String text = harn;
        String textFinal = confLine1 + text;
        return textFinal;
    }

    //Reset button function deletes all fields and enables tab nr field
    public void reset() {
        harnessOne.setText("");
        harnessTwo.setText("");
        harnessOne.setDisable(true);
        harnessTwo.setDisable(true);
        ats.setText("");
        tabNr.setText("");
        tabDisplay.setText("");
        tabNr.setDisable(false);
        harnessThree.setDisable(true);
        harnessFour.setDisable(true);
        harnessThree.setText("");
        harnessFour.setText("");
        timer = 0;
    }

    public void handleTabNrEnter() {
        tabNr.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                harnessTwo.requestFocus();
            }
        });
    }

    public void handleHarnessOneEnter() {
        harnessOne.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER) && timer >= 3) {
                harnessThree.setDisable(false);
                harnessThree.requestFocus();
                harnessThree.setText("");
                harnessOne.setDisable(true);
                timer = 0;
            } else if (timer < 3) {
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

    public void handleHarnessTwoEnter() {
        harnessTwo.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER) && timer >= 3) {
                harnessOne.setDisable(false);
                harnessOne.requestFocus();
                harnessOne.setText("");
                harnessTwo.setDisable(true);
                timer = 0;

            } else if (timer < 3) {
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

    public void handleHarnessThreeEnter() {
        harnessThree.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER) && timer >= 3) {
                harnessFour.setDisable(false);
                harnessFour.requestFocus();
                harnessFour.setText("");
                harnessThree.setDisable(true);
                timer = 0;
            } else if (timer < 3) {
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

    public void handleHarnessFourEnter() {
        harnessFour.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                oK.fire();
                harnessTwo.setText("");
                harnessTwo.requestFocus();
                harnessTwo.setDisable(false);
                harnessFour.setDisable(true);
            } else if (timer < 3) {
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
        if (harnessOne.getText() == null || harnessTwo.getText() == null || harnessThree.getText() == null || harnessFour.getText() == null) {
            ats.setText("Empty field");
            ats.setFont(Font.font("Arial", 20));
            ats.setStyle("-fx-text-inner-color: blue;");
        } else if (harnessAddPrefix(harnessOne.getText()).equals(harnessTwo.getText()) && harnessOne.getText().equals(harnessThree.getText()) && harnessOne.getText().equals(harnessFour.getText()) && harnessThree.getText().equals(harnessFour.getText())) {
            ats.setText("OK");
            ats.setFont(Font.font("Arial", 20));
            ats.setStyle("-fx-text-inner-color: green;");
            scan = new Scanner(new File("lblconfig.txt"));
            har = harnessOne.getText();
            tab = tabDisplay.getText();
            date = myDate.getText();
            time = myTime.getText();
            readConfig();
            write("log.txt", "a");

        } else {
            ats.setText("NOK");
            ats.setFont(Font.font("Arial", 20));
            ats.setStyle("-fx-text-inner-color: red;");
            write("log.txt", "a");
        }
    }
    //Reading line 2 and 4 from the config file
    public void scanSettings() {
        scanConf.nextLine();
        confLine1 = scanConf.nextLine();
        scanConf.nextLine();
        confLine2 = scanConf.nextLine();
    }

    //Reading label configuration txt file, and writing the label file then executing it via CMD
    public void readConfig() throws IOException {

        List<String> lines = new ArrayList<String>();

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

        File fileObject = new File("label2.txt");
        fileObject.createNewFile();
        FileWriter fileW = new FileWriter("label2.txt");
        BufferedWriter bufferW = new BufferedWriter(fileW);

        for (String s : lines) {
            System.out.println(s);
            bufferW.write(s);
            bufferW.newLine();
        }
        bufferW.close();

        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", ("COPY label2.txt " + confLine2));
        builder.redirectErrorStream(true);
        builder.start();
        System.out.println("Run");
    }


    //writing log.txt file
    protected static String defaultLogFile = "log.txt";

    public static void write(String s) throws IOException {
        write(defaultLogFile);
    }

    public void write(String f, String s) throws IOException {
        String harness = harnessOne.getText();
        String harness2 = harnessTwo.getText();
        String outcome = ats.getText();
        String date = myDate.getText();
        String time = myTime.getText();
        String tab = tabDisplay.getText();
        FileWriter aWriter = new FileWriter(f, true);
        aWriter.write(date + ";" + outcome + ";" + harness2 + ";" + harness + ";" + time + ";" + tab + System.getProperty("line.separator"));
        aWriter.flush();
        aWriter.close();
    }
}