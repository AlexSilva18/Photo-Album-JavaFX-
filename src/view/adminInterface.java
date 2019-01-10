 package view;
 
 import application.*;
 import javafx.collections.FXCollections;
 import javafx.collections.ObservableList;
 import javafx.fxml.FXML;
 import javafx.fxml.FXMLLoader;
 import javafx.fxml.Initializable;
 import javafx.scene.Scene;
 import javafx.event.ActionEvent;
 import javafx.scene.control.*;
 import javafx.event.EventHandler;
 import javafx.scene.input.MouseEvent;
 import javafx.stage.FileChooser;
 import javafx.stage.Stage;
 import application.User;
 
 import java.io.*;
 import java.lang.reflect.Array;
 import java.net.URL;
 import java.nio.file.Files;
 import java.util.*;

 public class adminInterface  implements Initializable {
 
     @FXML
     private TextField user_field;
     @FXML
     private Button my_albums;
     @FXML
     private Button logout;
     @FXML
     private ListView<application.User> list_view;
     boolean appendFile = false;
     private static final String inputDir = "input";
     private static final String inputFile = "input.dat";
     static User tempUser;

     private ObservableList<application.User> obs_list = FXCollections.observableArrayList();
     public static ArrayList<application.User> user_list = new ArrayList<>();
 
     private boolean adminFlag = false, stockFlag = false;
     // private String currUser;
     // private User currentUser;
 
     public adminInterface() {
 
     }
 
     @Override
     public void initialize(URL location, ResourceBundle resources) {
         //currUser = currentUser.getCurrentUser();
         obs_list.clear();
         obs_list.addAll(user_list);
         list_view.getItems().setAll(obs_list);
     }
 
     public void addUser() {
 
         if (user_field.getText().isEmpty()) {
             Alert alert = new Alert(Alert.AlertType.WARNING);
             alert.setTitle("Error");
             alert.setHeaderText("You Must Enter A Username");
             alert.setContentText("Please enter a username");
             alert.showAndWait();
             return;
         }
 
         String user = user_field.getText().trim().toLowerCase();
         for (application.User u : user_list) {
             if (u.getUsername().compareTo(user) == 0) {
                 Alert alert = new Alert(Alert.AlertType.WARNING);
                 alert.setTitle("Error");
                 alert.setHeaderText("Account Already Exists");
                 alert.setContentText("Please try another username");
                 alert.showAndWait();
                 user_field.clear();
                 return;
             }
         }
         application.User newUser = new application.User(user);
         user_list.add(newUser);
         obs_list.clear();
         obs_list.addAll(user_list);
         list_view.getItems().setAll(obs_list);
 
 
         user_field.clear();
     }
 
     public void deleteUser() {
         // checks for valid selection
         if (nonAdminInterface.checkInvalidSelection(list_view))
             return;
         application.User delUser = list_view.getSelectionModel().getSelectedItem();
         if (delUser.getUsername().equals("admin")) {
             Alert alert = new Alert(Alert.AlertType.WARNING);
             alert.setTitle("Error");
             alert.setHeaderText("Cannot Delete Admin Account");
             alert.setContentText("Invalid attempt to delete admin");
             alert.showAndWait();
             return;
         }
         int index = user_list.indexOf(delUser);
 
         user_list.remove(index);
         obs_list.remove(delUser);
         obs_list.addAll(user_list);
         list_view.getItems().remove(index);
         //list_view.getSelectionModel().select(index);
         //user_field.clear();
     }
 
     public void goToMyAlbums(ActionEvent actionEvent) {
         Photos.switchScenes("../view/nonAdminUI.fxml", my_albums);
     }
 

 
     public void logout() {
         Photos.switchScenes("../view/login.fxml", logout);
     }
 
     public void getUsersFromFile() {
         HashMap<String, HashMap<String, ArrayList<Photo>>> tempHash = new HashMap<>();
         try{
             File dataFile = new File("./input/input.dat");
             if(!dataFile.exists()){
                 File data = new File("./input/input.dat");
                 data.createNewFile();
//                 System.out.println("new .dat created");
             }
             else{
                 ObjectInputStream inStream = new ObjectInputStream(
                         new FileInputStream("./input/input.dat"));
                 tempHash = (HashMap<String, HashMap<String, ArrayList<Photo>>>)inStream.readObject();
                 inStream.close();
//                 System.out.println("inserted into hashMap");
             }
         }
         catch(Exception e){
             e.printStackTrace();
         }

         //System.out.println("HashMap: " + tempHash);

         Iterator it = tempHash.entrySet().iterator();
         while(it.hasNext()) {
             HashMap.Entry pair = (HashMap.Entry) it.next();
             //System.out.println("key: " + pair.getKey());
             User newUser = new User((String) pair.getKey());

             if (newUser.getUsername().equals("admin"))
                 adminFlag = true;
             if (newUser.getUsername().equals("stock"))
                 stockFlag = true;
             // check if any fields of the List are empty (no album or field were provided)
             user_list.add(newUser);
             HashMap<String, ArrayList<Photo>> tempAlbums = tempHash.get(newUser.getUsername());
             Iterator it2 = tempAlbums.entrySet().iterator();
             while (it2.hasNext()) {
                 HashMap.Entry pair2 = (HashMap.Entry) it2.next();
                 //System.out.println("key: " + pair2.getKey());
                 //System.out.println("values: " + pair2.getValue());
                 //newUser.setHashAlbum((String)pair2.getKey());
                 newUser.photoAlbums.get(newUser.getUsername())
                         .put((String) pair2.getKey(), // album names
                                 (ArrayList<Photo>) pair2.getValue() // album arraylist photos
                         );
             }

             //newUser.getCurrAlbum().setNumPhotos(newUser.getHashPhotos(newUser.getCurrAlbum().getName()).size());
         }
         
         
         if (!adminFlag) {
             User adminUser = new User("admin");
             user_list.add(adminUser);
             //System.out.println("adminUser: " + adminUser.getPhotoAlbums());
         }
         if(!stockFlag){
             User stockUser = new User("stock");
             user_list.add(stockUser);
             stockUser.setHashAlbum("stock");

             for (int i = 1; i < 6; i++){
                 String filePath = "./stock/stock" + Integer.toString(i) + ".jpg";
                 File stockFile = new File(filePath);
                 Calendar date = null;
                 HashMap<String,ArrayList<String>> tagsList = new HashMap<String,ArrayList<String>>() ;
                 Photo photo = new Photo(stockFile, stockFile.getName(), stockFile.getAbsolutePath(),"These are stock photos.",tagsList,date.getInstance());
                 stockUser.addHashPhotos("stock", photo);
             }

         }
     }

     public static void saveInput(){
         HashMap<String, HashMap<String, ArrayList<Photo>>> tempHash = new HashMap<>();

         for (User a : user_list) {
             tempHash.put(a.getUsername(), a.getHashUser());
         }
//         System.out.println("tempHash: " + tempHash);
         try{
             File inputFile = new File(inputDir);
             if (!inputFile.exists()){
                 inputFile.mkdir();
             }

             ObjectOutputStream outStream = new ObjectOutputStream(
                     new FileOutputStream("./input/input.dat")
             );
             outStream.writeObject(tempHash);
             outStream.close();
//             System.out.println("INPUT SAVED");
         } catch (Exception e) {
             e.printStackTrace();
         }
     }
 
 
 }