package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class Congrats {

    @FXML
    private Button button;


    public void action(javafx.event.ActionEvent event) throws IOException {
        congratulations(event);
    }

    public void congratulations(ActionEvent event) throws IOException {
        Parent mainParent = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene gameOver = new Scene(mainParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("HangMan");
        window.setResizable(false);
        window.alwaysOnTopProperty();
        window.setScene(gameOver);
        window.show();
    }
}
