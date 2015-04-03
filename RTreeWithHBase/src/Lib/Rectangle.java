package Lib;

public class Rectangle {
       int xMin;
       int yMin;
       int xMax;
       int yMax;
       
       public Rectangle () {
    	   
       }
       
       public Rectangle (int xMin, int yMin, int xMax, int yMax) {
    	   this.xMin = xMin;
    	   this.yMin = yMin;
    	   this.xMax = xMax;
    	   this.yMax = yMax;
       }
       
       public void setxMin (int xMin) {
    	   this.xMin = xMin;
       }
       
       public void setxMax (int xMax) {
    	   this.xMax = xMax;
       }
       
       public void setyMin (int yMin) {
    	   this.yMin = yMin;
       }
       
       public void setyMax (int yMax) {
    	   this.yMax = yMax;
       }
       
       public int getxMin () {
    	   return xMin;
       }
       
       public int getxMax () {
    	   return xMax;
       }
       
       public int getyMin () {
    	   return yMin;
       }
       
       public int getyMax () {
    	   return yMax;
       }
       
       public boolean contain (Rectangle rect) {
    	   if (xMin <= rect.getxMin() && xMax >= rect.getxMax() 
    			   && yMin <= rect.getyMin() && yMax >= rect.getyMax()) {
    		   return true;
    	   }
    	   return false;
       }
       
       public boolean overlap (Rectangle rect) {
    	   if (!(xMax <= rect.getxMin() || xMin >= rect.getxMin())) {
    		   if (!(yMin >= rect.getyMax() || yMax <= rect.getyMin())) {
    			   return true;
    		   }
    	   }
    	   return false;
       }
       
       public static Rectangle merge (Rectangle rect1, Rectangle rect2) {
    	   Rectangle newrect = new Rectangle();
    	   
    	   if (rect1.getxMin() < rect2.getxMin()) {
    		   newrect.setxMin(rect1.getxMin());
    	   }else {
    		   newrect.setxMin(rect2.getxMin());
    	   }
    	   
    	   if (rect1.getxMax() > rect2.getxMax()) {
    		   newrect.setxMax(rect1.getxMax());
    	   }else {
    		   newrect.setxMax(rect2.getxMax());
    	   }
    	   
    	   if (rect1.getyMin() < rect2.getyMin()) {
    		   newrect.setyMin(rect1.getyMin());
    	   }else {
    		   newrect.setyMin(rect2.getyMin());
    	   }
    	   
    	   if (rect1.getyMax() > rect2.getyMax()) {
    		   newrect.setyMax(rect1.getyMax());
    	   }else {
    		   newrect.setyMax(rect2.getyMax());;
    	   }
    	   return newrect;
       }
       
       public int getArea () {
    	   return (xMax - xMin) * (yMax - yMin);
       }
       
//       public static void main(String[] args) {
//    	   //System.out.println(new Rectangle(1,1,4,4).contain(new Rectangle(1,2,3,4)));
//    	   Rectangle rect = Rectangle.merge(new Rectangle(1,1,1,1), new Rectangle(2,3,3,4));
//    	   System.out.println(rect.getxMax());
//       }
       
      
}



