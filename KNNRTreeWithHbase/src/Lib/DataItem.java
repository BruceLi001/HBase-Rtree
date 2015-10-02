package Lib;

public class DataItem {
       public int key;
       public Rectangle mbr;
       public double price;
       public double rate;
       public String cuisine;
       public double longitude;
       public double latitude;
       public String rowindex;
       public String restaurantName;
       public String restaurantAddr;
       public double MinDist;
       public double RealDist;
       
       public DataItem (int key, Rectangle mbr, 
    		            double price, double rate, String cuisine,
    		            double longitude, double latitude,
    		            String rowindex, String Name, String Addr) {
    	   this.key = key;
    	   this.mbr = mbr;
    	   this.price = price;
    	   this.rate = rate;
    	   this.cuisine = cuisine;
    	   this.longitude = longitude;
    	   this.latitude = latitude;
    	   this.rowindex = rowindex;
    	   this.restaurantName = Name;
    	   this.restaurantAddr = Addr;
       }
       
       public DataItem (Rectangle mbr, double price, double rate, String cuisine,
    		            double longitude, double latitude
    		            ) {
    	   this.mbr = mbr;
    	   this.price = price;
    	   this.rate = rate;
    	   this.cuisine = cuisine;
    	   this.longitude = longitude;
    	   this.latitude = latitude;
    	   
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
       
       public double getPrice () {
    	   return this.price;
       }
       
       public double getLong () {
    	   return this.longitude;
       }
       
       public double getLati () {
    	   return this.latitude;
       }
       
       public double getRate () {
    	   return this.rate;
       }
       
       public String getCuisine () {
    	   return this.cuisine;
       }
       
       public String getName () {
    	   return this.restaurantName;
       }
       
       public String getAddr () {
    	   return this.restaurantAddr;
       }
       
       public void setRealDist (double realDist) {
    	   java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");  
    	   df.format(realDist);
    	   this.RealDist = realDist;
       }
}
