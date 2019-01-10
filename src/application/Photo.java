package application;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Photo implements Serializable {
    protected String name;
    protected HashMap<String,ArrayList<String>> tags = new HashMap<String,ArrayList<String>>();
    protected Calendar date; // make sure milliseconds are set to zero e.g.  cal.set(Calendar.MILLISECOND,0);
    protected File photoFile;
    protected String filePath;
    protected String caption;

	public Photo(String name){
        this.name = name;
    }
    public Photo(File file, String name, String filePath,String caption,HashMap<String,ArrayList<String>> tags,Calendar date ){
        this.photoFile = file;
        this.name = name;
        this.filePath = filePath;
        this.date=date;
        this.caption=caption;
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        StringTokenizer strtok =new StringTokenizer(sdf.format(file.lastModified()),"/: ");
        String month = strtok.nextToken();
        String day =strtok.nextToken();
        String year =strtok.nextToken();
        String hour =strtok.nextToken();
        String min =strtok.nextToken();
        if(strtok.hasMoreTokens()){
        	String sec =strtok.nextToken();
        	
            date.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(min),Integer.parseInt(sec));
            date.set(Calendar.MILLISECOND,0);;
        }
        else{
        	date.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(min));
        	date.set(Calendar.MILLISECOND,0);;
        }
        
        
    }

    public HashMap<String, ArrayList<String>> getTags() {   	
    	return tags;
	}
      
	public void setTags(HashMap<String, ArrayList<String>> tags) {	
		this.tags = tags;
	}
	
	public void removeTag(HashMap<String, ArrayList<String>> tags,String key, String tagVal){
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(tagVal);
		if(tags.isEmpty()){
			return;
		}
			 if(tags.containsKey(key) && tags.get(key).contains(tagVal)){
				tags.get(key).remove(tagVal);	
				if(tags.get(key).size()==0){//remove the key if it has no list
					tags.remove(key);

				}
			}		
		else{
			return;
		}
	}
	
	public void addTag(HashMap<String, ArrayList<String>> tags,String key, String tagVal){
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(tagVal);
		
		if(!tags.isEmpty()){
			if(tags.containsKey(key)){				
				tags.get(key).add(tagVal);	
				return;
			}
			else{
				tags.put(key, temp);
				return;
			}
		}
		else{
			tags.put(key, temp);
			return;

		}
	}
	
	
	public Calendar getDate() {
		return date;
	}
	public void setDate(Calendar date) {
		this.date = date;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	public void setPhotoName(String name){
        this.name = name;
    }

    public String getPhotoName() {
    	return this.name;
    }

    public File getPhotoFile(){ 
    	return this.photoFile;
    }

    public String getFilePath(){ 
    	return this.filePath;
    }

    private String trimmedName(){
        return this.name.split("\\.")[0];
    }

    @Override
    public String toString(){
        return trimmedName();
    }
}
