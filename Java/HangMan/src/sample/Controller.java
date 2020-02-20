package sample;


import com.sun.tools.javac.comp.Enter;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @FXML
    private ImageView head;
    @FXML
    private ImageView leftHand;
    @FXML
    private ImageView body;
    @FXML
    private ImageView rightHand;
    @FXML
    private ImageView leftLeg;
    @FXML
    private ImageView rightLeg;
    @FXML
    private Label firstInstruction;
    @FXML
    private Label guessingLabel;
    @FXML
    private Label leftChance;
    @FXML
    private Label guessingLabelTwo;
    @FXML
    private Label attemptChance;
    @FXML
    private Label guessingWord;
    @FXML
    private Label instructions;
    @FXML
    private Label here;
    @FXML
    private Label vowel;
    @FXML
    private Label consonent;
    @FXML
    private Button attempt;
    @FXML
    private Button startButton;
    @FXML
    private TextField userInput;

    int badGuessCounter = 0, congratsInfo = 0, goodGuessCounter = 0, index = 0;;
    int badGuessCheckerOne, badGuessCheckerTwo, random, badGuessResult;
    String generatedWord;
    private char[] guessingArray = new char[30];
    char[] repetedItem = new char[30];
    char inputLetter;
    static String sharedWord;


    String[] countryName = {"afghanistan", "armenia", "mexico", "bangladesh",
            "argentina", "australia", "brazil", "canada", "china",
            "bhutan", "india", "morocco", "belgium", "bolivia",
            "nepal", "netherlands", "cambodia", "chile", "pakistan",
            "panama", "peru", "portugal", "denmark", "russia", "egypt",
            "finland", "france", "spain", "sweden", "switzerland", "syria",
            "germany", "ghana", "greece", "thailand", "turkey", "indonesia",
            "iran", "italy", "uganda", "japan", "zimbabwe", "korea", "libya"};


    public void playAGame() {
        reset();
        manD_0();
        manD_1();
        manD_2();
        manD_3();
        manD_4();
        manD_5();
        makeOpacity();
        userInput.setDisable(false);
        attempt.setDisable(false);
        badGuessResult = 6;
        badGuessCounter = 0;
        leftChance.setText(badGuessResult + "");
        guessingLabel.setText(null);
        startButton.setText("Reset!");
        random = (int) (Math.random() * ((43 - 0) + 1)) + 0;
        generatedWord = countryName[random];
        sharedWord = generatedWord;
        //Arrays.fill(repetedItem, '0');


        for (int i = 0; i < generatedWord.length(); i++) {
            if ((generatedWord.charAt(i) == 'a') || (generatedWord.charAt(i) == 'e') || (generatedWord.charAt(i) == 'i') || (generatedWord.charAt(i) == 'o') || (generatedWord.charAt(i) == 'u')) {
                guessingArray[i] = 'X';
            } else
                guessingArray[i] = '_';
        }
        guessingLabel.setText(String.valueOf(guessingArray));
    }

    public void reset() {
        generatedWord = "";
        guessingLabel.setText("");
        firstInstruction.setText("");
        userInput.setText("");
        congratsInfo = 0;
        Arrays.fill(guessingArray, ' ');
        Arrays.fill(repetedItem, ' ');
    }

    public void makeOpacity() {
        here.setOpacity(1);
        vowel.setOpacity(1);
        consonent.setOpacity(1);
        guessingWord.setOpacity(1);
        instructions.setOpacity(1);
        attemptChance.setOpacity(1);
        guessingLabelTwo.setOpacity(1);
    }

    public void action(ActionEvent actionEvent) {
        try {
            String userInputText = userInput.getText();
            inputLetter = userInputText.charAt(0);
            badGuessCheckerTwo = 0;
            badGuessCheckerOne = 0;
            repetedItem[index] = inputLetter;
            index++;

            if (isRepeat()) {
                for (int i = 0; i < generatedWord.length(); i++) {
                    if (inputLetter == (generatedWord.charAt(i))) {
                        int index = i;
                        goodGuessCounter = 1;
                        guessingArray[index] = inputLetter;
                        guessingLabel.setText(String.valueOf(guessingArray));
                        badGuessCheckerTwo = 1;
                        congratsInfo++;
                    } else {
                        badGuessCheckerOne = 1;
                        firstInstruction.setText("Bad Guess!!");
                    }
                }

                if (goodGuessCounter == 1) {
                    firstInstruction.setText("Good Guess!!");
                    goodGuessCounter = 0;
                }

                if (congratsInfo == generatedWord.length())
                    congrats(actionEvent);


                if ((badGuessCheckerOne == 1) && (badGuessCheckerTwo == 0)) {
                    badGuessCounter++;
                    badGuessResult = 6 - badGuessCounter;
                    leftChance.setText(badGuessResult + "");
                    if (badGuessCounter == 1) {
                        man_0();
                    } else if (badGuessCounter == 2) {
                        man_1();
                    } else if (badGuessCounter == 3) {
                        man_2();
                    } else if (badGuessCounter == 4) {
                        man_3();
                    } else if (badGuessCounter == 5) {
                        man_4();
                    } else if (badGuessCounter == 6) {
                        man_5(actionEvent);
                    }
                }

            } else {
                firstInstruction.setText("You have already selected this letter!!");
            }
            userInput.setText("");
        } catch (RuntimeException e) {
            firstInstruction.setText("Please enter a letter!");
            System.out.println(e);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private boolean isRepeat() {
        int counter = 0;
        for (int i = 0; i < repetedItem.length; i++) {
            if (repetedItem[i] == inputLetter) {
                counter++;
            }
        }
        if (counter > 1)
            return false;
        else if (counter == 0 || counter == 1)
            return true;
        return false;
    }

    public void congrats(ActionEvent event) throws IOException {
        Parent congratsPage = FXMLLoader.load(getClass().getResource("Congrats.fxml"));
        Scene congo = new Scene(congratsPage);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("Congratulations");
        window.setScene(congo);
        window.show();
    }

    public void gameOver(ActionEvent event) throws IOException {
        Parent gameOverPage = FXMLLoader.load(getClass().getResource("gameOver.fxml"));
        Scene gameOver = new Scene(gameOverPage);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("Game Over");
        window.setScene(gameOver);
        window.show();
    }

    private void man_5(ActionEvent event) throws IOException {
        rightLeg.setOpacity(1);
        gameOver(event);
    }

    private void man_4() {
        leftLeg.setOpacity(1);
    }

    private void man_3() {
        rightHand.setOpacity(1);
    }

    private void man_2() {
        leftHand.setOpacity(1);
    }

    private void man_1() {
        body.setOpacity(1);
    }

    private void man_0() {
        head.setOpacity(1);
    }


    private void manD_5() {
        rightLeg.setOpacity(0);
    }

    private void manD_4() {
        leftLeg.setOpacity(0);
    }

    private void manD_3() {
        rightHand.setOpacity(0);
    }

    private void manD_2() {
        leftHand.setOpacity(0);
    }

    private void manD_1() {
        body.setOpacity(0);
    }

    private void manD_0() {
        head.setOpacity(0);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
