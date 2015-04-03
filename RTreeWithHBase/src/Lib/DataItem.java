package Lib;

import java.util.ArrayList;

public class DataItem {
       public int key;
       public Rectangle mbr;
       public String rowindex;
       //public ArrayList<String> data;
       
       public DataItem (int key, Rectangle mbr, String rowindex) {
    	   this.key = key;
    	   this.mbr = mbr;
    	   this.rowindex = rowindex;
       }
       
       public DataItem () {
    	   
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
       
       public String getRowIndex () {
    	   return rowindex;
       }
       
       public void setRowIndex (String rowIndex) {
    	   this.rowindex = rowIndex;
       }
       
//       public static void main (String[] args) {
//    	   DataItem data = new DataItem(1, new Rectangle(1,1,2,2), "col");
//    	   System.out.println(data.getRowIndex());
//       }
}
