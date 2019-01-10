package view;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import application.Album;
import application.Photo;
import application.Photos;
import application.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class nonAdminInterface implements Initializable {
    @FXML
    private TextArea numAlbumPhotos;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private Button admin_tools;
    @FXML
	private Button logoutButton;
	@FXML
	private Button saveButton;
	@FXML
	private Button renameButton;
	@FXML
	private Button cancelButton;
	@FXML
	private Button searchButton;
	@FXML
	private Button createButton;
	@FXML
	private Button openButton;
	@FXML
	private Button deleteButton;
	@FXML
	private TextField tagType;
	@FXML
	private TextField tagValue;
	@FXML
	private TextField albumName;
	@FXML
	private Label albumLabel;
	@FXML
	private Label numPhotoLabel;
	@FXML
	private Label dateRangeLabel;
//	@FXML
//	private TextArea albumDetail;
	@FXML
	public ListView<Album> albumList;
	public static LocalDate start;
	public static LocalDate end;
	public static String tagName;
	public static String tagVal;

	private boolean isCreate = false;
	private boolean isRename = false;
	
    //login loginObj = new login();
    public ObservableList<Album> obs_list = FXCollections.observableArrayList();
    public static ArrayList<Album> album_list = new ArrayList<>(); // stores albums to be displayed in listview
    protected HashMap<String, HashMap<String, ArrayList<Photo>>> photoAlbums; //Stores Album Name with the List of photos

    Album currAlbum = new Album(); // variable to store the album to be opened
    login logObj = new login(); // variable to hold current logged in User for admin tool access
    adminInterface admin = new adminInterface();
    public static User currUser = null;

	public void logout(){
        album_list.clear();
	    obs_list.clear();
        //obs_list.removeAll(album_list);
        albumList.getItems().clear();

        Photos.switchScenes("../view/login.fxml", logoutButton);
	}
	
	//commit changes to album
	public void saveChanges(){
		//logic to save goes here
        toggleButtons(true);
		saveButton.setVisible(false);
		saveButton.setDisable(true);
		cancelButton.setVisible(false);
		cancelButton.setDisable(true);
		renameButton.setVisible(true);
		renameButton.setDisable(false);

		// check if all fields have input
//        if (albumName.getText().isEmpty() || numAlbumPhotos.getText().isEmpty() ||
//                startDateRange.getValue() == null || endDateRange.getValue() == null){
        if (albumName.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("You Must Enter All Fields to Create Album");
            alert.setContentText("Please complete all fields");
            alert.showAndWait();
            clearFields();
            return;
        }

        String album = albumName.getText().trim().toLowerCase();
        for (Album a : album_list){
            if (a.getName().equals(album)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Duplicate Album");
                alert.setContentText("album has not been created");
                alert.showAndWait();
                clearFields();
                return;
            }
        }

        if(isCreate){
	        Album newAlbum = new Album(album);
	        album_list.add(newAlbum);
	        currUser.setHashAlbum(album);
	        obs_list.clear();
	        obs_list.addAll(album_list);
	        albumList.getItems().setAll(obs_list);
	     	isCreate = false;        
        }
        
        if(isRename){      
	        int selectedIndex = albumList.getSelectionModel().getSelectedIndex();
	        
	        Album selectedAlbum = albumList.getItems().get(selectedIndex);
	        System.out.println(selectedAlbum.getName());
	        
	        Album currAlbum = new Album();
	        
	        currAlbum.setName(albumName.getText().trim().toLowerCase());
	        
	        albumList.getItems().clear();
	        obs_list.clear();
	        
	        currUser.renameHashAlbum(currAlbum.getName(),selectedAlbum.getName());
	        //System.out.println(currUser.photoAlbums.values().toString());

	        albumList.getItems().remove(selectedAlbum);
	        album_list.remove(selectedAlbum);
	        obs_list.remove(selectedAlbum);
	        obs_list.clear();
	        album_list.add(currAlbum);
	        //obs_list.add(currAlbum);
            obs_list.setAll(album_list);

	        albumList.getItems().clear();	
	        albumList.getItems().setAll(obs_list);
	        albumList.refresh();
	        isRename = false;
        }
        toggleButtons(true);
        clearFields();
	}

	// rename an album
	public void rename(){
        if (checkInvalidSelection(albumList)){
            return;
        }
        isRename =true;
        toggleButtons(false);
		int selectedIndex = albumList.getSelectionModel().getSelectedIndex();
		albumName.setText(""+obs_list.get(selectedIndex).getName());

        // remaining logic to go here

	}
	
	//cancel an action button
	public void cancel(){
		isRename =false;
		isCreate=false;
		//clear any text fields as needed here

		saveButton.setVisible(false);
		saveButton.setDisable(true);
		cancelButton.setVisible(false);
		cancelButton.setDisable(true);
		renameButton.setVisible(true);
		renameButton.setDisable(false);
        toggleButtons(true);

		clearFields();
	}

	//search for a photo
	public void search(){
		//search logic to go here, then open searchUI
		//startDate.getChronology();
        //endDate.getChronology();
        if (startDate.getValue() != null && endDate.getValue() != null &&
                !tagType.getText().isEmpty() && !tagValue.getText().isEmpty()){
            start = startDate.getValue();
            end = endDate.getValue();
            tagName = tagType.getText();
            tagVal = tagValue.getText();
        }

        else if (startDate.getValue() != null && endDate.getValue() != null){
            start = startDate.getValue();
            end = endDate.getValue();
        }

        else if (!tagType.getText().isEmpty() && !tagValue.getText().isEmpty()){
            tagName = tagType.getText();
            tagVal = tagValue.getText();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Search Options");
            alert.setContentText("Please search by date or by tag or both");
            alert.showAndWait();
            return;

        }

//
//        System.out.println("name: " + tagName);
//        System.out.println("value: " + tagVal);
        // switch scenes to searchInterface
        Photos.switchScenes("../view/searchUI.fxml", searchButton);

	}
	
	//create new album
	public void create(){
		if(saveButton.isVisible()){//if someone is making changes force them to either cancel or commit
			return;
		}
		isCreate = true;

		toggleButtons(false);
        clearFields();
	}

    /** Open album and move to photoView
     */
	public void open(){
		if(saveButton.isVisible()){//if someone is making changes force them to either cancel or commit
			return;
		}
		// check if there was an album selected to open
        if (checkInvalidSelection(albumList))
            return;

        // stores the selected album to be used in the photoInterface
        Album selectedAlbum = albumList.getSelectionModel().getSelectedItem();
        currUser.setCurrAlbum(selectedAlbum);

        // switch scenes to photoViewInterface
        Photos.switchScenes("../view/photoView.fxml", openButton);
	}

    /** runs open() on mouse double click
     * @param mouseEvent
     */
    public void runOpen(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            open();
        }
    }

    /** delete an album
     * gets index of the album located in the album list
     * removes it from the list, HashMap, the obsList and listView
     */
	public void delete(){
		if(saveButton.isVisible()){//if someone is making changes force them to either cancel or commit
			return;
		}
        //  get selected album and its index in the ArrayList
        Album delAlbum = albumList.getSelectionModel().getSelectedItem();
        int index = album_list.indexOf(delAlbum);

        // Updates Listview
        album_list.remove(index);
        currUser.delHashAlbum(delAlbum.getName());
        obs_list.remove(delAlbum);
        obs_list.addAll(album_list);
        albumList.getItems().remove(index);
		//delete album logic here
	}

    public void backToAdminTools(ActionEvent actionEvent) {
        Photos.switchScenes("../view/adminUI.fxml", admin_tools);
    }
 
    /** initializes the album GUI
     *  gets the current user logged in
     *  gets the albums that are stored in the hashMap for that user
     *  adds them to the list of Albums to be displayed in the listView
     *  if admin, adds button to the GUI to back to the Admin Tools
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        start = null;
        end = null;
        tagVal =null;
        tagName = null;
        // iterate through the list of users to find the current logged in user
        for (User u : adminInterface.user_list) {
//            System.out.println("users: " + u);
            if (u.getUsername().equals(logObj.currUser.getCurrUser())) {
                //System.out.println(u.getUsername());
                currUser = u;
            }
        }
//        System.out.println("username: " + currUser.getUsername());
//        System.out.println("currUserHash: " + currUser.getHashUser());

        if (currUser.getHashAlbum() != null) {
            // get users albums
            Set<String> albums = currUser.getHashAlbum();
//            System.out.println("albums: " + albums.toString());
            // populate albumList and check for duplicates
            outerLoop:
            for (String albumName : albums) {
                for (Album a : album_list) {
                    if (a.getName().equals(albumName)) {
                        //System.out.println("duplicate");
                        continue outerLoop;
                    }

                }
                Album tempAlbum = new Album(albumName);
                tempAlbum.setNumPhotos(currUser.getHashPhotos(tempAlbum.getName()).size());
                Photo oldest = currUser.getHashPhotos(tempAlbum.getName()).get(0);
                Photo newest = currUser.getHashPhotos(tempAlbum.getName()).get(0);
                for(Photo ps:currUser.getHashPhotos(tempAlbum.getName())){
                	if(oldest.getDate().compareTo(ps.getDate())<0){
                		oldest=ps;
                	}
                	if(newest.getDate().compareTo(ps.getDate())>0){
                		newest=ps;
                	}       		
                }
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");            
                tempAlbum.setEndDate((sdf.format(newest.getPhotoFile().lastModified())));
                tempAlbum.setStartDate((sdf.format(oldest.getPhotoFile().lastModified())));
                album_list.add(tempAlbum);
            }
            // update listView
            obs_list.clear();
            obs_list.addAll(album_list);
            albumList.getItems().setAll(obs_list);
            albumList.refresh();
        }

	    // checks if logged in user is admin, if so display admin tool button
        if (logObj.currUser.getCurrUser().equals("admin")) {
            admin_tools.setVisible(true);
            admin_tools.setDisable(false);
        }
        else {
            admin_tools.setVisible(false);
            admin_tools.setDisable(true);
        }
        obs_list.clear();
        obs_list.addAll(album_list);
        albumList.getItems().setAll(obs_list);
        albumList.refresh();
    }


    /** clears all input fields
     * @return
     */
    private void clearFields(){
        albumName.clear();
//        numAlbumPhotos.clear();

    }

    /** checks if an item has been selected before any action (button) action
     * gets selected item from listView
     * @param listView
     * @return true if no item was selected
     */
    static boolean checkInvalidSelection(ListView listView){
        if (listView.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("No Item Has Been Selected");
            alert.setContentText("Please select an item from the list");
            alert.showAndWait();
            return true;
        }
        return false;
    }

    /** toggles buttons when create, rename, save changes, and cancel are used
     * if flag is true disables the buttons
     * else enables the buttons
     * @param flag
     */
    private void toggleButtons(boolean flag){
        // disable
        if (flag){
            albumLabel.setVisible(false);
//            numPhotoLabel.setVisible(false);
            //dateRangeLabel.setVisible(false);

            albumName.setVisible(false);
            albumName.setDisable(true);

//            numAlbumPhotos.setVisible(false);
  //          numAlbumPhotos.setDisable(true);
        }
        // enable
        else {
            albumLabel.setVisible(true);
//            numPhotoLabel.setVisible(true);
  //          dateRangeLabel.setVisible(true);

            albumName.setVisible(true);
            albumName.setDisable(false);
//            numAlbumPhotos.setVisible(true);
  //          numAlbumPhotos.setDisable(false);


            saveButton.setVisible(true);
            saveButton.setDisable(false);
            cancelButton.setVisible(true);
            cancelButton.setDisable(false);
            renameButton.setVisible(false);
            renameButton.setDisable(true);
        }
    }

}
