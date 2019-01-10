package application;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

// with serializable we can store all the content (albums and photos) of @photoAlbums into a photoFile to export and import
public class Album implements Serializable {
	protected String name;
	//protected String description;
    protected int numPhotos;
    protected String startDate;
    protected String endDate;
    protected static String currAlbum;
	protected ArrayList<Photo> photoPaths;
	

    // (must be in album Interface if i'm not mistaken)


	public Album(){};

//	public Album album (String name, String description,ArrayList<File> photoPaths){
//		this.name=name;
//		this.description=description;
//		this.photoPaths=photoPaths;
//		return this;
//	}
    public Album (String name){
        this.name = name;
    }
    public Album (String name, int numPhotos, String startDate, String endDate){
        this.name = name;
        this.numPhotos = numPhotos;
        this.startDate = startDate;
        this.endDate = endDate;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumPhotos(int numPhotos){ this.numPhotos = numPhotos;
	}
	public int getNumPhotos(){ return this.numPhotos;
	}

	public void setStartDate(String startDate){
		this.startDate = startDate;
		}
	public String getStartDate(){ 
		return this.startDate;
		}

	public String getEndDate(){ 
		return this.endDate;
		}
    public void setEndDate(String endDate){
    	this.endDate = endDate;
    	}

	public void setCurrAlbum(String currAlbum){this.currAlbum = currAlbum;}
    public String getCurrentAlbum(){ return this.currAlbum;}

//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}

    @Override
    public String toString(){
        return this.getName() + "\t" + this.getNumPhotos() + "\t" +  this.getEndDate() + "\t" +  this.getStartDate();
        //return this.getName();
    }
}
