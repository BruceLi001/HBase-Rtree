package Lib;

public class DataItem {
       public int key;
       public Rectangle mbr;
       public String rowindex;
       
       public DataItem (int key, Rectangle mbr, String rowindex) {
    	   this.key = key;
    	   this.mbr = mbr;
    	   this.rowindex = rowindex;
       }
       
       public DataItem () {
    	   
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
