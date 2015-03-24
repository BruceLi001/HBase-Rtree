import Lib.Rectangle;;

public class DataItem {
     private int key;
     private int x;
     private int y;
     private String value;
     private Rectangle Mbr;
     
     public void setX (int x) {
    	 this.x = x;
     }
     
     public void setY (int y) {
    	 this.y = y;
     }
     
     public void setKey (int key) {
    	 this.key = key;
     }
     
     public Rectangle setMBR (int x, int y) {
    	   Rectangle Mbr = new Rectangle(x, x, y, y);
    	   return Mbr;
     }
     
     public Rectangle getMBR () {
    	 return Mbr;
     }
}
