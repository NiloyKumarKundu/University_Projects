package Client;

import Server.ClientHandler;
import Server.Server;
import animatefx.animation.FadeIn;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;


public class Controller {
    @FXML
    public Pane pnSignIn;
    @FXML
    public Pane pnSignUp;
    @FXML
    public Button btnSignUp;
    @FXML
    public Button getStarted;
    @FXML
    public ImageView btnBack;
    @FXML
    public TextField regName;
    @FXML
    public TextField regPass;
    @FXML
    public TextField regEmail;
    @FXML
    public TextField regFirstName;
    @FXML
    public TextField regPhoneNo;
    @FXML
    public RadioButton male;
    @FXML
    public RadioButton female;
    @FXML
    public Label controlRegLabel;
    @FXML
    public Label success;
    @FXML
    public Label goBack;
    @FXML
    public TextField userName;
    @FXML
    public TextField passWord;
    @FXML
    public Label loginNotifier;
    @FXML
    public Label nameExists;
    @FXML
    public Label checkEmail;
    public static String username, password, gender;
    public static ArrayList<User> loggedInUser = new ArrayList<>();
    public static ArrayList<User> users = new ArrayList<User>();

    public void registration() {
        if (!regName.getText().equalsIgnoreCase("")
                && !regPass.getText().equalsIgnoreCase("")
                && !regEmail.getText().equalsIgnoreCase("")
                && !regFirstName.getText().equalsIgnoreCase("")
                && !regPhoneNo.getText().equalsIgnoreCase("")
                && (male.isSelected() || female.isSelected())) {
            if(checkUser(regName.getText())) {
                if(checkEmail(regEmail.getText())) {
                    User newUser = new User();
                    newUser.name = regName.getText();
                    newUser.password = regPass.getText();
                    newUser.email = regEmail.getText();
                    newUser.fullName = regFirstName.getText();
                    newUser.phoneNo = regPhoneNo.getText();
                    if (male.isSelected()) {
                        newUser.gender = "Male";
                    } else {
                        newUser.gender = "Female";
                    }
                    users.add(newUser);
                    goBack.setOpacity(1);
                    success.setOpacity(1);
                    makeDefault();
                    if (controlRegLabel.getOpacity() == 1) {
                        controlRegLabel.setOpacity(0);
                    }
                    if (nameExists.getOpacity() == 1) {
                        nameExists.setOpacity(0);
                    }
                } else {
                    checkEmail.setOpacity(1);
                    setOpacity(nameExists, goBack, controlRegLabel, success);
                }
            } else {
                nameExists.setOpacity(1);
                setOpacity(success, goBack, controlRegLabel, checkEmail);
            }
        } else {
            controlRegLabel.setOpacity(1);
            setOpacity(success, goBack, nameExists, checkEmail);
        }
    }

    private void setOpacity(Label a, Label b, Label c, Label d) {
        if(a.getOpacity() == 1 || b.getOpacity() == 1 || c.getOpacity() == 1 || d.getOpacity() == 1) {
            a.setOpacity(0);
            b.setOpacity(0);
            c.setOpacity(0);
            d.setOpacity(0);
        }
    }


    private void setOpacity(Label controlRegLabel, Label checkEmail, Label nameExists) {
        controlRegLabel.setOpacity(0);
        checkEmail.setOpacity(0);
        nameExists.setOpacity(0);
    }

    private boolean checkUser(String username) {
        for(User user : users) {
            if(user.name.equalsIgnoreCase(username)) {
                return false;
            }
        }
        return true;
    }

    private boolean checkEmail(String email) {
        for(User user : users) {
            if(user.email.equalsIgnoreCase(email)) {
                return false;
            }
        }
        return true;
    }

    private void makeDefault() {
        regName.setText("");
        regPass.setText("");
        regEmail.setText("");
        regFirstName.setText("");
        regPhoneNo.setText("");
        male.setSelected(true);
        setOpacity(controlRegLabel, checkEmail, nameExists);
    }


    public void login() {
        username = userName.getText();
        password = passWord.getText();
        boolean login = false;
        for (User x : users) {
            if (x.name.equalsIgnoreCase(username) && x.password.equalsIgnoreCase(password)) {
                login = true;
                loggedInUser.add(x);
                System.out.println(x.name);
                gender = x.gender;
                break;
            }
        }
        if (login) {
            changeWindow();
        } else {
            loginNotifier.setOpacity(1);
        }
    }

    public void changeWindow() {
        try {
            Stage stage = (Stage) userName.getScene().getWindow();
            Parent root = FXMLLoader.load(this.getClass().getResource("Room.fxml"));
            stage.setScene(new Scene(root, 330, 560));
            stage.setTitle(username + "");
            stage.setOnCloseRequest(event -> {
                System.exit(0);
            });
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource().equals(btnSignUp)) {
            new FadeIn(pnSignUp).play();
            pnSignUp.toFront();
        }
        if (event.getSource().equals(getStarted)) {
            new FadeIn(pnSignIn).play();
            pnSignIn.toFront();
        }
        loginNotifier.setOpacity(0);
        userName.setText("");
        passWord.setText("");
    }

    @FXML
    private void handleMouseEvent(MouseEvent event) {
        if (event.getSource() == btnBack) {
            new FadeIn(pnSignIn).play();
            pnSignIn.toFront();
        }
        regName.setText("");
        regPass.setText("");
        regEmail.setText("");
    }
}