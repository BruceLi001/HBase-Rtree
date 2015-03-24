package Lib;

public class DataItem {
	private int key; //to represent the id
//    private int x;
//    private int y;
//    private String value;
    private Rectangle mbr; // to represent the object
    public DataItem () {
    	
    }
    
    public  DataItem (Rectangle mbr, int key) {
    	this.mbr = mbr;
    	this.key = key;
    }
    
    public Rectangle getMbr () {
    	return mbr;
    }
    
    public void setMbr (Rectangle mbr) {
    	this.mbr = mbr;
    }
    
    public int getKey () {
    	return key;
    }
    
    public void setKey (int key) {
    	this.key = key;
    } 
}
