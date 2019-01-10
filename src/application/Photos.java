package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.stage.Stage;
import view.adminInterface;

import java.io.*;

public class Photos extends Application	{
    public static Stage currStage;



	@Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("../view/login.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
            stage.setOnCloseRequest(event -> adminInterface.saveInput());
            //System.out.println("SAVE");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void main (String args[]){
		launch(args);
    }

    /** Allows scenes to be switched between each panel
     * function switchScenes() is called in every button that changes GUI
     * @param filePath
     * @param button
     */
    public static void switchScenes(String filePath, Button button){
        try {
            FXMLLoader loader = new FXMLLoader(Photos.class.getResource(filePath));
            loader.setLocation(Photos.class.getResource(filePath));
            Stage stage = (Stage) button.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            currStage = stage;

        } catch (IOException io) {
            io.printStackTrace();
        }
    }


}
