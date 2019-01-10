package application;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class User implements Serializable {

	protected String username;
    protected Album currAlbum; // stores the album that is currently opened
	protected static String currentUser; // stores the user that is currently logged in
	protected ArrayList<User> users;


	/** stores all users, each users album, and all photos and each album
     */
    public HashMap<String, HashMap<String, ArrayList<Photo>>> photoAlbums;
	

	public User(){};

    public User(String username){
        this.username = username;
        //this.photoAlbums = new HashMap<String, HashMap<String, ArrayList<Photo>>>() {{
        this.photoAlbums = new HashMap() {{
            put(username, new HashMap<>());
        }};
        //  setHashUser();
    }

	public User user(String username, String password, ArrayList<Album> albums) {
		this.username=username;
		//this.password=password;
		//this.albums=albums;
		return this;		
	}

//	public ArrayList<Album> getAlbums() {
//		return albums;
//	}
//
//	public void setAlbums(ArrayList<Album> albums) {
//		this.albums = albums;
//	}

    public void addToUserList(){
        users.add(this);
    }

	public String getUsername() {
		return username;
	}

	public void setCurrAlbum(Album album){
        this.currAlbum = album;
    }

    public Album getCurrAlbum(){
        return currAlbum;
    }

	public void setUsername(String username) {
		this.username = username;
	}

     /** sets photoAlbum HashMap
     * @param photoAlbums
     */
    public void setPhotoAlbums(HashMap<String, HashMap<String, ArrayList<Photo>>> photoAlbums) {
	    this.photoAlbums = photoAlbums;
    }


    /** adds new user to the hashTable
     */
    public void setHashUser(String username){
        photoAlbums.put(username, new HashMap<>());
    }


    /** retrieves the user in the hashTable
     * hashMap holds all users
     * @return
     */
    public HashMap<String, ArrayList<Photo>> getHashUser(){
        return photoAlbums.get(this.username);
    }

    /** add album to hashTable with empty Photo List
     * gets logged in user to find the specific album
     * @param albumName
     */
    public void setHashAlbum(String albumName){
        HashMap<String, ArrayList<Photo>> temp = new HashMap<>();
        temp = this.photoAlbums.get(this.username);
        temp.put(albumName, new ArrayList<>());
        photoAlbums.put(this.username, temp);
    }

    /** add album to hashTable with empty Photo List
     * gets logged in user to find the specific album
     * @param albumName
     */
    public void renameHashAlbum(String albumName, String removeKey){
    	
        HashMap<String, ArrayList<Photo>> temp = new HashMap<>();
        temp = this.photoAlbums.get(this.username);
        ArrayList<Photo> tempPhotos = photoAlbums.get(this.username).get(removeKey);
        System.out.println(tempPhotos.toString());
        temp.remove(removeKey);
        System.out.println(tempPhotos.toString());
        temp.put(albumName, new ArrayList<>());
        photoAlbums.put(this.username, temp);
        
        photoAlbums.get(this.username).put(albumName, tempPhotos);
        System.out.println(this.photoAlbums.values().toString());
    }

    /** retrieves the users albums in the HashMap
     * returns of Set containing all the albums
     * @return
     */
    public Set<String> getHashAlbum(){
        HashMap<String, ArrayList<Photo>> temp = new HashMap<>();
        temp = this.photoAlbums.get(this.username);
        return temp.keySet();
    }

    /** delete album from HashTable
     * @param albumName
     */
    public void delHashAlbum(String albumName){
        photoAlbums.get(this.username).remove(albumName);
    }

    /** Add photo to HashTable
     * @param album
     * @param photo
     */
    public void addHashPhotos(String album, Photo photo){
        ArrayList<Photo> tempPhotos = photoAlbums.get(this.username).get(album);
        tempPhotos.add(photo);
        photoAlbums.get(this.username).put(album, tempPhotos);
    }

    /** Delete photo from HashTable
     * @param album
     * @param photo
     */
    public void delHashPhoto(String album, Photo photo){
        photoAlbums.get(this.username).get(album).remove(photo);
    }

    /** return the users photos from a single album
     * @param album
     * @return
     */
    public ArrayList<Photo> getHashPhotos(String album){
        return photoAlbums.get(this.username).get(album);
    }

    /** return all Users albums
     * @return
     */
    public HashMap<String, HashMap<String, ArrayList<Photo>>> getPhotoAlbums() {
        return photoAlbums;
    }


    /** sets the current user logged in
     * @param currentUser
     */
	public void setCurrUser(String currentUser) {
        this.currentUser = currentUser;
    }

    /** retrieves the current logged in user
     * @return
     */
	public String getCurrUser() {return this.currentUser;}



    /** Display Username only in Admin tool Listview
     * @return
     */
    @Override
    public String toString(){
        return this.getUsername();
    }

}
