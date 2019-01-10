package view;

import application.Album;
import application.Photo;
import application.Photos;
import application.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Popup;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;

public class photoViewInterface implements Initializable, Serializable {
    @FXML
    private BorderPane pane;
    @FXML
    private Button move_image;
    @FXML
    private Button addTag;
    @FXML
    private Button saveChanges;
    @FXML
    private Button editCaption;
    @FXML
    private Button cancelButton;
    @FXML
    private Button cancelButton1;
    @FXML
    private Button saveChanges1;
    @FXML
    private Button removeTag;

    @FXML
    private TextArea photoCaption;
    @FXML
    private TextField tagName;
    @FXML
    private TextField tagValue;
    @FXML
    private TextArea tagDisp;
    
//    @FXML
//    private Button delete_image;
//    @FXML
//    private Button add_image;
    @FXML
    private Button exit_photos;
    @FXML
    private Label photoDate;
//    @FXML
//    private Button view_image;
    @FXML
    private Button copy_image;
    @FXML
    private ListView<Photo> list_view;
    @FXML
    private ImageView imgView;

    private static Photo displayedImage;
    private static Photo fullScreenImage;
    private static int exitFlag = 0;
    private static int deleteFlag =0;
    public static ArrayList<Photo> photo_list = new ArrayList<>();
    private ObservableList<Photo> photoObsList = FXCollections.observableArrayList();
    private nonAdminInterface nonAdminObj = new nonAdminInterface();
    private User currUser = nonAdminInterface.currUser;

    private boolean isCap =false;
    private boolean isRemoveTag = false;
    private boolean isAddTag = false;
    protected HashMap<String, ArrayList<Photo>> stockPhotos;
    login logObj = new login();

    public void editTags(){
    	isRemoveTag=true;
    	tagName.setEditable(true);
    	tagValue.setEditable(true);
    	saveChanges1.setVisible(true);
    	saveChanges1.setDisable(false);
    	cancelButton1.setVisible(true);
    	cancelButton1.setDisable(false);
    }
    public void addTag(){
    	isAddTag = true;
    	tagName.setEditable(true);
    	tagValue.setEditable(true);
    	saveChanges1.setVisible(true);
    	saveChanges1.setDisable(false);
    	cancelButton1.setVisible(true);
    	cancelButton1.setDisable(false);
    }
    public void editCaption(){
    	isCap=true;
    	photoCaption.setEditable(true);
    	saveChanges.setVisible(true);
    	saveChanges.setDisable(false);
    	cancelButton.setVisible(true);
    	cancelButton.setDisable(false);
    }
    
    public void saveChanges(){    	
        if (photoCaption.getText() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("No Caption has been entered");
            alert.setContentText("Please enter a caption");
            alert.showAndWait();
            return;
        }
        //Save caption changes
        if(isCap){
        	//get the text and set the current photos caption to it       	
	    	list_view.getSelectionModel().getSelectedItem().setCaption(photoCaption.getText().trim());	    	
	    	isCap=false;
	    	saveChanges.setVisible(false);
	    	saveChanges.setDisable(true);
	    	cancelButton.setVisible(false);
	    	cancelButton.setDisable(true);
        }
        
        //save tag changes
        if(isRemoveTag){
        	Photo selectedPhoto = list_view.getSelectionModel().getSelectedItem();
            int index = photo_list.indexOf(selectedPhoto);
            ArrayList<Photo> photos =currUser.getHashPhotos(currUser.getCurrAlbum().getName());
            Photo ph = photos.get(index);
        	ph.removeTag(ph.getTags(),tagName.getText(), tagValue.getText());             	
        	//System.out.println(ph.getTags().toString());
        	tagDisp.setText(selectedPhoto.getTags().toString());
        	isRemoveTag=false;
        	tagName.setEditable(false);
        	tagValue.setEditable(false);
	        tagName.clear();
        	tagValue.clear();
	    	photoCaption.setEditable(false);
	    	saveChanges1.setVisible(false);
	    	saveChanges1.setDisable(true);
	    	cancelButton1.setVisible(false);
	    	cancelButton1.setDisable(true);
        }
        if(isAddTag){
        	
        	//get current selected item and add tag to current selected items tags
        	Photo selectedPhoto = list_view.getSelectionModel().getSelectedItem();
            int index = photo_list.indexOf(selectedPhoto);
            ArrayList<Photo> photos =currUser.getHashPhotos(currUser.getCurrAlbum().getName());
            Photo ph = photos.get(index);
        	ph.addTag(ph.getTags(),tagName.getText(), tagValue.getText());
        	//System.out.println(ph.getTags().toString());
        	tagDisp.setText(selectedPhoto.getTags().toString());
        	list_view.refresh();
        	isAddTag=false;
        	tagName.setEditable(false);
        	tagValue.setEditable(false);
	        tagName.clear();
        	tagValue.clear();
	    	saveChanges1.setVisible(false);
	    	saveChanges1.setDisable(true);
	    	cancelButton1.setVisible(false);
	    	cancelButton1.setDisable(true);
        }
    }
    public void cancel(){
    	if(isCap){
	    	photoCaption.setEditable(false);
	    	saveChanges.setVisible(false);
	    	saveChanges.setDisable(true);
	    	cancelButton.setVisible(false);
	    	cancelButton.setDisable(true);
	    	isCap=false;
    	}  	
    	if(isRemoveTag){
    		tagName.setEditable(true);
        	tagValue.setEditable(true);
        	tagName.clear();
        	tagValue.clear();
	    	saveChanges1.setVisible(false);
	    	saveChanges1.setDisable(true);
	    	cancelButton1.setVisible(false);
	    	cancelButton1.setDisable(true);
	    	isRemoveTag=false;
    	}
    	if(isAddTag){
    		tagName.setEditable(true);
        	tagValue.setEditable(true);
        	tagName.clear();
        	tagValue.clear();
	    	saveChanges1.setVisible(false);
	    	saveChanges1.setDisable(true);
	    	cancelButton1.setVisible(false);
	    	cancelButton1.setDisable(true);
	    	isAddTag=false;
    	}
    }

    public void addImage() {
        FileChooser fileGetter = new FileChooser();
        FileChooser.ExtensionFilter jpg = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter png = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileGetter.getExtensionFilters().addAll(jpg,png);
        File file = fileGetter.showOpenDialog(null);

        if (file != null){
            for (Photo p : photo_list){
                //System.out.println("array: " + p.getPhotoName() + "newFile: " + file.getName().split("\\.")[0]);
                if (p.getPhotoName().equals(file.getName())){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Error");
                    alert.setHeaderText("Photo Already Added");
                    alert.setContentText("Please select a new photo");
                    alert.showAndWait();
                    return;
                }
            }
            Calendar date=null;
            HashMap<String,ArrayList<String>> tagsList = new HashMap<String,ArrayList<String>>();
            Photo newPhoto = new Photo(file, file.getName(), file.getAbsolutePath(),"",tagsList,date.getInstance());
            photo_list.add(newPhoto);
            currUser.addHashPhotos(currUser.getCurrAlbum().getName(), newPhoto);
            photoObsList.clear();
            photoObsList.addAll(photo_list);
            list_view.getItems().setAll(photoObsList);

            //photoObsList.setAll()


        }
        else
            System.out.println("Unable to add photo");
    }

    public void deleteImage() {
        if (nonAdminInterface.checkInvalidSelection(list_view))
            return;
        Photo selectedPhoto = list_view.getSelectionModel().getSelectedItem();
        int index = photo_list.indexOf(selectedPhoto);
        photo_list.remove(index);
        photoObsList.remove(selectedPhoto);
        photoObsList.addAll(photo_list);
        list_view.getItems().remove(index);
        currUser.delHashPhoto(currUser.getCurrAlbum().getName(), selectedPhoto);
        displayedImage = null;
        updateSlideShow();
    }

    public void viewImage() throws FileNotFoundException {
        if (nonAdminInterface.checkInvalidSelection(list_view))
            return;
        Photo selectedPhoto = list_view.getSelectionModel().getSelectedItem();
        setImageView(selectedPhoto);
        photoCaption.setText(selectedPhoto.getCaption());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");       
        photoDate.setText(sdf.format(selectedPhoto.getPhotoFile().lastModified()));
        tagDisp.setText(selectedPhoto.getTags().toString());
    }

    public void runViewImage(MouseEvent mouseEvent) throws FileNotFoundException {
        if (mouseEvent.getClickCount() == 2) {
            viewImage();
        }
    }


    private void updateSlideShow(){
        int lastItem = photo_list.size() - 1;
        if (lastItem >= 0){
            list_view.getSelectionModel().select(lastItem);
        }
        imgView.setImage(null);
    }

    // get previous image in the arrayList
    public void photoSlidePrevious() {
        int index = 0;
        for (Photo p : photo_list){
            if (p.getPhotoName().equals(displayedImage.getPhotoName())){
                index = photo_list.indexOf(p);
            }
        }
        if(index == 0)
            return;

        list_view.getSelectionModel().select(index - 1);
        Photo prevPhoto = list_view.getSelectionModel().getSelectedItem();
        setImageView(prevPhoto);
        photoCaption.setText(prevPhoto.getCaption());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");       
        photoDate.setText(sdf.format(prevPhoto.getPhotoFile().lastModified()));
        tagDisp.setText(prevPhoto.getTags().toString());
    }

    // get next image in the arrayList
    public void photoSlideNext() {
        int index = 0;
        for (Photo p : photo_list){
            if (p.getPhotoName().equals(displayedImage.getPhotoName())){
                index = photo_list.indexOf(p);
            }
        }
        if(index == (photo_list.size() - 1) )
            return;

        list_view.getSelectionModel().select(index + 1);
        Photo nextPhoto = list_view.getSelectionModel().getSelectedItem();
        setImageView(nextPhoto);
        photoCaption.setText(nextPhoto.getCaption());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");       
        photoDate.setText(sdf.format(nextPhoto.getPhotoFile().lastModified()));
        tagDisp.setText(nextPhoto.getTags().toString());
    }

    // updates ImageView
    private void setImageView(Photo photo){
        displayedImage = photo;
//        FileInputStream inputStream = new FileInputStream(selectedPhoto.getFilePath());
//        Image newImage = new Image(inputStream);
	        Image displayImage = new Image(photo.getPhotoFile().toURI().toString());
	        imgView.setImage(displayImage);

    }

    /**
     *
     * @param mouseEvent gets mouse click
     */
    public void displayFullScreen(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            fullScreenImage = list_view.getSelectionModel().getSelectedItem();
            ImageView tempView = new ImageView();
            tempView = imgView;
            tempView.setPreserveRatio(false);
            tempView.setFitHeight(700);
            tempView.setFitWidth(1000);
            exitFlag = 1;
            BorderPane pane = new BorderPane();
            pane.setPrefSize(1100, 800);
            pane.setCenter(tempView);
            Button goBack = new Button ("X");
            pane.setBottom(goBack);

            BorderPane.setMargin(goBack, new Insets(0,475,20,525));
            //pane.setCenter(goBack);
            goBack.setOnAction(e -> Photos.switchScenes("../view/photoView.fxml", goBack));
            
            Photos.currStage.setScene(new Scene(pane));
            Photos.currStage.show();
        }
    }
    
    public void displayCopyToAlbum() {
    	if(nonAdminObj.checkInvalidSelection(list_view)){
    		return;
    	}
            BorderPane pane = new BorderPane();
            pane.setPrefSize(500, 700);
            BorderPane pane2 = new BorderPane();
            pane2.setPrefSize(500, 700);
            Set<String> albums = currUser.getHashAlbum();
            ListView<String> dispList = new ListView<>();
            ObservableList<String> obs = FXCollections.observableArrayList();
            for(String albumName : albums){
            	obs.add(albumName);
            }
            dispList.getItems().setAll(obs);

            pane.setCenter(dispList);
            Button goBack = new Button ("Cancel");
            
            
            pane.setRight(goBack);
            Button select = new Button ("Select");
            goBack.setPrefWidth(100);
            select.setPrefWidth(100);
            pane.setLeft(select);
            //copy photo to new album
            select.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                	if(nonAdminObj.checkInvalidSelection(dispList)){
                		return;
                	}
                	else if (dispList.getSelectionModel().getSelectedItem().equals(currUser.getCurrAlbum().getName())){
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Error");
                        alert.setHeaderText("Photo already exists!");
                        alert.setContentText("this photo is already contained in this album");
                        alert.showAndWait();
                	    return;
                    }
                	Photo copyThis = list_view.getSelectionModel().getSelectedItem();             	
                	String albumName = dispList.getSelectionModel().getSelectedItem();

                	System.out.println(list_view.getItems().toString()+ "copy");
                	currUser.addHashPhotos(albumName, copyThis);
                	//System.out.println(currUser.getHashAlbum()+"current albums ");
                	

                	if(deleteFlag==1){
                		
            	        deleteImage();
            	        System.out.println(list_view.getItems().toString()+ "after delete");
            	        System.out.println(photo_list.toString()+ "after delete photolist");
            	        deleteFlag=0;
            	        //photoObsList.clear();
            	       // photoObsList.addAll(photo_list);
            	       // list_view.getItems().setAll(photoObsList);
                    }
                	//list_view.refresh();
                	
                	Photos.switchScenes("../view/photoView.fxml", select);
                    return;
                }
            });
            	
            
            goBack.setOnAction(e -> Photos.switchScenes("../view/photoView.fxml", goBack));

            Photos.currStage.setScene(new Scene(pane));
            Photos.currStage.show();
    }
    
    public void displayMoveToAlbum() { 
    	deleteFlag=1;
        displayCopyToAlbum();
        System.out.println(list_view.getItems().toString()+ "before delete");
        
        System.out.println(list_view.getItems().toString()+ "after refresh");
        System.out.println(photo_list.toString()+ "after refresh photolist");
    }
    
    
    public void exitPhotoViewer() {
        photo_list.clear();
        photoObsList.clear();
        //obs_list.removeAll(album_list);
        list_view.getItems().clear();
        Photos.switchScenes("../view/nonAdminUI.fxml", exit_photos);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imgView.setFitHeight(350); // set fixed ImageView size
        pane.setCenter(imgView); // center image in ImageView
        Album currAlbum = nonAdminInterface.currUser.getCurrAlbum();
        currAlbum.setNumPhotos(currUser.getHashPhotos(currAlbum.getName()).size());
        ArrayList<Photo> photos = nonAdminInterface.currUser.getHashPhotos(currAlbum.getName());

        outerLoop:
        for (Photo p: photos) {
            for (Photo ph : photo_list){
                if (p.getPhotoName().equals(ph.getPhotoName())){
                    //System.out.println("duplicate");
                    continue  outerLoop;
                }

            }
            photo_list.add(p);
        }
        photoObsList.clear();
        photoObsList.addAll(photo_list);
        list_view.getItems().setAll(photoObsList);

            // set view when returning from full Screen
        if(exitFlag == 1) {
            photoObsList.clear();
            photoObsList.setAll(photo_list);
            list_view.getItems().setAll(photoObsList);
            int index = photo_list.indexOf(fullScreenImage);
            list_view.getSelectionModel().select(index);
            Photo photo = list_view.getSelectionModel().getSelectedItem();
            setImageView(photo);
            photoCaption.setText(photo.getCaption());
            exitFlag = 0;
        }

        // load photos from this album
        
        
        
        
    }



}
