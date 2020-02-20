package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;


public class GameOver extends Controller {
    @FXML
    private Button button;
    @FXML
    private Label label;
    @FXML
    private Label check;

    public void checkWord(ActionEvent event) {
        check.setOpacity(1);
        label.setText(sharedWord);
    }

    public void action(javafx.event.ActionEvent event) {
        try {
            congratulations(event);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void congratulations(ActionEvent event) throws IOException {
        Parent mainParent = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene gameOver = new Scene(mainParent);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setTitle("HangMan");
        window.setScene(gameOver);
        window.setResizable(false);
        window.alwaysOnTopProperty();
        window.show();
    }
}
