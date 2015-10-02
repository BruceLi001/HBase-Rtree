package Lib;

import Lib.Rectangle;

public class Data {
	public int key;
	public Rectangle mbr;
	public String rowindex;
	public String value;
	
	public Data (int key, Rectangle mbr, String rowindex, String value) {
  	   this.key = key;
  	   this.mbr = mbr;
  	   this.rowindex = rowindex;
  	   this.value = value;
    }
     
     public Data() {
  	   
     }
     
     public Rectangle getMbr () {
  	   return this.mbr;
     }
     
     public void setMbr (Rectangle mbr) {
  	   this.mbr = mbr;
     }
     
     public int getKey () {
  	   return this.key;
     }
     
     public void setKey (int key) {
  	   this.key = key;
     }
     
     public String getIndex () {
  	   return this.rowindex;
     }
}
