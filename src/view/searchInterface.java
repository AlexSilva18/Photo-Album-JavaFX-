package view;

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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.*;

import static view.nonAdminInterface.album_list;

public class searchInterface implements Initializable {
    @FXML
    private ListView list_view;
    @FXML
    private Button create_album;
    @FXML
    private TextField album_field;
    @FXML
    private Button exit_search;
    @FXML
    private Button open_image;
    @FXML
    private TextArea album_details;

    HashMap<String, HashMap<String, ArrayList<Photo>>> tempHash;
    ArrayList<Photo> searchedPhotos;
    public ObservableList<Photo> obs_list = FXCollections.observableArrayList();


    public void createAlbum(ActionEvent actionEvent) {
        if (album_field.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("No Album Name was inserted");
            alert.setContentText("Please provide an album name");
            alert.showAndWait();
            return;
        }
        Album album = new Album(album_field.getText().trim().toLowerCase());
        for (Album a : nonAdminInterface.album_list){
            if (album.getName().equals(album)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText("Duplicate Album");
                alert.setContentText("album has not been created");
                alert.showAndWait();
                return;
            }
        }
        nonAdminInterface.album_list.add(album);
        nonAdminInterface.currUser.setHashAlbum(album.getName());
        nonAdminInterface.currUser.photoAlbums.get(nonAdminInterface.currUser.getUsername()).put(
                album.getName(), searchedPhotos);
        album_field.clear();
    }

    public void openImage(ActionEvent actionEvent) {
    }
    public void getData(){
//      nonAdminInterface.startDate;
//      nonAdminInterface.endDate;
//      nonAdminInterface.tagType;
//      nonAdminInterface.tagValue;
//
//      HashMap<String, ArrayList<Photo>> tempAlbums = tempHash.get(newUser.getUsername());
//      Iterator it2 = tempAlbums.entrySet().iterator();
//      while (it2.hasNext()) {
//          HashMap.Entry pair2 = (HashMap.Entry) it2.next();
//          //System.out.println("key: " + pair2.getKey());
//          //System.out.println("values: " + pair2.getValue());
//          //newUser.setHashAlbum((String)pair2.getKey());
//          newUser.photoAlbums.get(newUser.getUsername())
//                  .put((String) pair2.getKey(), // album names
//                          (ArrayList<Photo>) pair2.getValue() // album arraylist photos
//                  );
//
//      }
  }
    public void exitSearch(ActionEvent actionEvent) {
        Photos.switchScenes("../view/nonAdminUI.fxml", exit_search);
//        try {
//            Photos photo = new Photos();
//
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("nonAdminUI.fxml"));
//            loader.setLocation(getClass().getResource("nonAdminUI.fxml"));
//            Stage stage = (Stage) exit_search.getScene().getWindow();
//            Scene scene = new Scene(loader.load());
//            stage.setScene(scene);
////	        if (currUser.equals("admin"))
//        } catch (IOException io) {
//            io.printStackTrace();
//        }
        //add logic to save work below
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tempHash = new HashMap<>();
        searchedPhotos = new ArrayList<>();
        for (User u : adminInterface.user_list) {
            //System.out.println("users: " + u.getUsername());
            //System.out.println("Albums: " + u.getPhotoAlbums());
            tempHash.put(u.getUsername(), u.getHashUser());
        }
        //System.out.println(currUser.getPhotoAlbums());
        System.out.println(nonAdminInterface.tagVal);
        System.out.println(nonAdminInterface.tagName);
        System.out.println(nonAdminInterface.start);
        System.out.println(nonAdminInterface.end);
        int dateFlag = 1;
        int tagFlag = 0;
        if (nonAdminInterface.tagVal == null && nonAdminInterface.tagName == null)
            tagFlag = 1;

        Date startdate = null;
        Date enddate = null;
        Calendar startCalendar = null;
        Calendar endCalendar = null;
        if (nonAdminInterface.start != null && nonAdminInterface.end != null) {
            startdate = Date.from(nonAdminInterface.start.atStartOfDay(ZoneId.systemDefault()).toInstant());
            enddate = Date.from(nonAdminInterface.end.atStartOfDay(ZoneId.systemDefault()).toInstant());
            startCalendar = Calendar.getInstance();
            startCalendar.setTime(startdate);
            endCalendar = Calendar.getInstance();
            endCalendar.setTime(enddate);
            dateFlag = 0;
        }

        Iterator it = tempHash.entrySet().iterator();
        while(it.hasNext()) {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            //System.out.println("key: " + pair.getKey());
            User newUser = new User((String) pair.getKey());

            HashMap<String, ArrayList<Photo>> tempAlbums = tempHash.get(newUser.getUsername());
            Iterator it2 = tempAlbums.entrySet().iterator();
            while (it2.hasNext()) {
                HashMap.Entry pair2 = (HashMap.Entry) it2.next();
//                newUser.photoAlbums.get(newUser.getUsername())
//                        .put((String) pair2.getKey(), // album names
//                                (ArrayList<Photo>) pair2.getValue() // album arraylist photos
//                        );
                //ArrayList<Photo> tempPhotos = tempHash.get(newUser.getUsername()).get(pair2.getKey());
                ArrayList<Photo> tempPhotos = (ArrayList<Photo>)pair2.getValue();
                for (Photo p : tempPhotos){
                    if (tagFlag == 0) {
                        if (p.getTags().containsKey(nonAdminInterface.tagName)) {
                            for (String tags : p.getTags().get(nonAdminInterface.tagName)) {
                                if (tags.equals(nonAdminInterface.tagVal))
                                    searchedPhotos.add(p);
                            }
                        }
                    }
                    if (dateFlag == 0) {
                        if (startdate.compareTo(p.getDate().getTime()) < 0 && enddate.compareTo(p.getDate().getTime()) > 0) {
                            searchedPhotos.add(p);
                        }
                    }
                }
            }

            //newUser.getCurrAlbum().setNumPhotos(newUser.getHashPhotos(newUser.getCurrAlbum().getName()).size());
        }
        obs_list.clear();
        obs_list.addAll(searchedPhotos);
        list_view.getItems().setAll(obs_list);
    }
}
