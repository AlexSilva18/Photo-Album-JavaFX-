package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.*;
import application.Photos;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

//Nick Nasta

public class login implements Initializable {
	
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private Button login_button;
	adminInterface admin = new adminInterface();
	int userFlag = 0;
    static int initFlag = 0;
    User currUser = new User();

	public void login() {
		if(username.getText().equals("")){
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Error");
			alert.setHeaderText("You Must Enter A Username");
			alert.setContentText("Please enter a username");
			alert.showAndWait();
			return;
		}

		// Check for valid username
        for (User u : admin.user_list){
            if (username.getText().equals(u.getUsername()))
                userFlag =1 ;
        }
        if (userFlag == 0){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Username");
            alert.setContentText("Please enter a valid username");
            alert.showAndWait();
            return;
        }

        currUser.setCurrUser(username.getText());

		//Admin login, need to switch the scene to admin
		if(username.getText().equals("admin")){
            Photos.switchScenes("../view/adminUI.fxml", login_button);
		}
		//stock user login
		else if(username.getText().equals("stock")){
            Photos.switchScenes("../view/nonAdminUI.fxml", login_button);
		}
		//User login, need to switch to user view
		//Need to check list of users to make sure they are in there and password is correct
		else{
            Photos.switchScenes("../view/nonAdminUI.fxml", login_button);

		}
		
	}
	
	public void captureKey(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER)
            login();
    }
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
	    if (initFlag == 0) {
            admin.getUsersFromFile();
            initFlag = 1;
        }

    }
	
}
