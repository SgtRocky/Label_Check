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

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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
    //Config file reading every line one by one
    public String line1;
    public String line2;
    public String line3;
    public String line4;
    public String line5;
    public String line6;
    public String line7;
    public String line8;
    public String line9;
    public String line10;
    public String line11;
    public String line12;
    public String line13;
    public String line14;
    public String line15;
    public String line16;
    public String line17;
    public String line18;
    public String line19;
    public String line20;
    public String line21;
    public String line22;
    public String line23;
    public String line24;
    public String line25;
    public String line26;
    public String line27;
    public String line28;
    public String line29;
    public String line30;

    public String confLine1;
    public String confLine2;

    String har;
    String tab;
    String date;
    String time;
    int timer;

    @FXML
    public void initialize() throws IOException {

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

    public void playError(){

//        Media sound = new Media(new File("Error.mp3").toURI().toString());
//        MediaPlayer mediaPlayer = new MediaPlayer(sound);
//        mediaPlayer.play();

        AudioClip soundHorn = new AudioClip(new File("Error.mp3").toURI().toString());
        soundHorn.play();
    }

    //Checking if tab nr is entered correctly with HL prefix and 8 digits
    //If entered correctly unlocks harnessOne text field
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
                alert.showAndWait();
            }
        });
    }

    public void handleHarnessThreeEnter() throws IOException {
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

    public void handleHarnessFourEnter() throws IOException {
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
            generateOutputFile();
            write("log.txt", "a");

        } else {
            ats.setText("NOK");
            ats.setFont(Font.font("Arial", 20));
            ats.setStyle("-fx-text-inner-color: red;");
            write("log.txt", "a");
        }
    }

    public void scanSettings() {
        scanConf.nextLine();
        confLine1 = scanConf.nextLine();
        scanConf.nextLine();
        confLine2 = scanConf.nextLine();
    }

    public void readConfig() {
        scan.nextLine();
        scan.nextLine();
        scan.nextLine();
        scan.nextLine();
        line1 = scan.nextLine();
        line1 = line1.replace("harness", har);
        line1 = line1.replace("tab", tab);
        line1 = line1.replace("date", date);
        line1 = line1.replace("time", time);

        line2 = scan.nextLine();
        line2 = line2.replace("harness", har);
        line2 = line2.replace("tab", tab);
        line2 = line2.replace("date", date);
        line2 = line2.replace("time", tab);

        line3 = scan.nextLine();
        line3 = line3.replace("harness", har);
        line3 = line3.replace("tab", tab);
        line3 = line3.replace("date", date);
        line3 = line3.replace("time", time);

        line4 = scan.nextLine();
        line4 = line4.replace("harness", har);
        line4 = line4.replace("tab", tab);
        line4 = line4.replace("date", date);
        line4 = line4.replace("time", time);

        line5 = scan.nextLine();
        line5 = line5.replace("harness", har);
        line5 = line5.replace("tab", tab);
        line5 = line5.replace("date", date);
        line5 = line5.replace("time", time);

        line6 = scan.nextLine();
        line6 = line6.replace("harness", har);
        line6 = line6.replace("tab", tab);
        line6 = line6.replace("date", date);
        line6 = line6.replace("time", time);

        line7 = scan.nextLine();
        line7 = line7.replace("harness", har);
        line7 = line7.replace("tab", tab);
        line7 = line7.replace("date", date);
        line7 = line7.replace("time", time);

        line8 = scan.nextLine();
        line8 = line8.replace("harness", har);
        line8 = line8.replace("tab", tab);
        line8 = line8.replace("date", date);
        line8 = line8.replace("time", time);

        line9 = scan.nextLine();
        line9 = line9.replace("harness", har);
        line9 = line9.replace("tab", tab);
        line9 = line9.replace("date", date);
        line9 = line9.replace("time", time);

        line10 = scan.nextLine();
        line10 = line10.replace("harness", har);
        line10 = line10.replace("tab", tab);
        line10 = line10.replace("date", date);
        line10 = line10.replace("time", time);

        line11 = scan.nextLine();
        line11 = line11.replace("harness", har);
        line11 = line11.replace("tab", tab);
        line11 = line11.replace("date", date);
        line11 = line11.replace("time", time);

        line12 = scan.nextLine();
        line12 = line12.replace("harness", har);
        line12 = line12.replace("tab", tab);
        line12 = line12.replace("date", date);
        line12 = line12.replace("time", time);


        line13 = scan.nextLine();
        line13 = line13.replace("harness", har);
        line13 = line13.replace("tab", tab);
        line13 = line13.replace("date", date);
        line13 = line13.replace("time", time);

        line14 = scan.nextLine();
        line14 = line14.replace("harness", har);
        line14 = line14.replace("tab", tab);
        line14 = line14.replace("date", date);
        line14 = line14.replace("time", time);

        line15 = scan.nextLine();
        line15 = line15.replace("harness", har);
        line15 = line15.replace("tab", tab);
        line15 = line15.replace("date", date);
        line15 = line15.replace("time", time);

        line16 = scan.nextLine();
        line16 = line16.replace("harness", har);
        line16 = line16.replace("tab", tab);
        line16 = line16.replace("date", date);
        line16 = line16.replace("time", time);

        line17 = scan.nextLine();
        line17 = line17.replace("harness", har);
        line17 = line17.replace("tab", tab);
        line17 = line17.replace("date", date);
        line17 = line17.replace("time", time);

        line18 = scan.nextLine();
        line18 = line18.replace("harness", har);
        line18 = line18.replace("tab", tab);
        line18 = line18.replace("date", date);
        line18 = line18.replace("time", myTime.getText());

        line19 = scan.nextLine();
        line19 = line19.replace("harness", har);
        line19 = line19.replace("tab", tab);
        line19 = line19.replace("date", date);
        line19 = line19.replace("time", time);

        line20 = scan.nextLine();
        line20 = line20.replace("harness", har);
        line20 = line20.replace("tab", tab);
        line20 = line20.replace("date", date);
        line20 = line20.replace("time", time);

        line21 = scan.nextLine();
        line21 = line21.replace("harness", har);
        line21 = line21.replace("tab", tab);
        line21 = line21.replace("date", date);
        line21 = line21.replace("time", time);

        line22 = scan.nextLine();
        line22 = line22.replace("harness", har);
        line22 = line22.replace("tab", tab);
        line22 = line22.replace("date", date);
        line22 = line22.replace("time", time);

        line23 = scan.nextLine();
        line23 = line23.replace("harness", har);
        line23 = line23.replace("tab", tab);
        line23 = line23.replace("date", date);
        line23 = line23.replace("time", time);

        line24 = scan.nextLine();
        line24 = line24.replace("harness", har);
        line24 = line24.replace("tab", tab);
        line24 = line24.replace("date", date);
        line24 = line24.replace("time", time);

        line25 = scan.nextLine();
        line25 = line25.replace("harness", har);
        line25 = line25.replace("tab", tab);
        line25 = line25.replace("date", date);
        line25 = line25.replace("time", time);

        line26 = scan.nextLine();
        line26 = line26.replace("harness", har);
        line26 = line26.replace("tab", tab);
        line26 = line26.replace("date", date);
        line26 = line26.replace("time", time);

        line27 = scan.nextLine();
        line27 = line27.replace("harness", har);
        line27 = line27.replace("tab", tab);
        line27 = line27.replace("date", date);
        line27 = line27.replace("time", time);

        line28 = scan.nextLine();
        line28 = line28.replace("harness", har);
        line28 = line28.replace("tab", tab);
        line28 = line28.replace("date", date);
        line28 = line28.replace("time", time);

        line29 = scan.nextLine();
        line29 = line29.replace("harness", har);
        line29 = line29.replace("tab", tab);
        line29 = line29.replace("date", date);
        line29 = line29.replace("time", time);

        line30 = scan.nextLine();
        line30 = line30.replace("harness", har);
        line30 = line30.replace("tab", tab);
        line30 = line30.replace("date", date);
        line30 = line30.replace("time", time);
    }

    private void generateOutputFile() throws IOException {
        File fileObject = new File("label.txt");
        fileObject.createNewFile();
        FileWriter fileW = new FileWriter("label.txt");
        BufferedWriter bufferW = new BufferedWriter(fileW);
        //------------
        bufferW.write(line1);
        bufferW.newLine();
        bufferW.write(line2);
        bufferW.newLine();
        bufferW.write(line3);//
        bufferW.newLine();
        bufferW.write(line4);
        bufferW.newLine();
        bufferW.write(line5);
        bufferW.newLine();
        bufferW.write(line6);
        bufferW.newLine();
        bufferW.write(line7);
        bufferW.newLine();
        bufferW.write(line8);
        bufferW.newLine();
        bufferW.write(line9);
        bufferW.newLine();
        bufferW.write(line10);
        bufferW.newLine();
        bufferW.write(line11);
        bufferW.newLine();
        bufferW.write(line12);
        bufferW.newLine();
        bufferW.write(line13);
        bufferW.newLine();
        bufferW.write(line14);
        bufferW.newLine();
        bufferW.write(line15);
        bufferW.newLine();
        bufferW.write(line16);
        bufferW.newLine();
        bufferW.write(line17);
        bufferW.newLine();
        bufferW.write(line18);
        bufferW.newLine();
        bufferW.write(line19);
        bufferW.newLine();
        bufferW.write(line20);
        bufferW.newLine();
        bufferW.write(line21);
        bufferW.newLine();
        bufferW.write(line22);
        bufferW.newLine();
        bufferW.write(line23);
        bufferW.newLine();
        bufferW.write(line24);
        bufferW.newLine();
        bufferW.write(line25);
        bufferW.newLine();
        bufferW.write(line26);
        bufferW.newLine();
        bufferW.write(line27);
        bufferW.newLine();
        bufferW.write(line28);
        bufferW.newLine();
        bufferW.write(line29);
        bufferW.newLine();
        bufferW.write(line30);
        bufferW.close();
        //------------
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", ("COPY label.txt " + confLine2));
        builder.redirectErrorStream(true);
        builder.start();
    }
    

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