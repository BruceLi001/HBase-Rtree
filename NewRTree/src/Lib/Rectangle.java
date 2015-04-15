package Lib;

public class Rectangle {
     
	 int xMin;
	 int xMax;
	 int yMin;
	 int yMax;
	 
	 public Rectangle (int xMin, int yMin, int xMax, int yMax) {
    	   this.xMin = xMin;
    	   this.yMin = yMin;
    	   this.xMax = xMax;
    	   this.yMax = yMax;
     }
	 
	 public Rectangle () {
		 
	 }
	 
	 public int getxMin () {
		 return this.xMin;
	 }
	 
	 public int getyMin () {
		 return this.yMin;
	 }
	 
	 public int getxMax () {
		 return this.xMax;
	 }
	 
	 public int getyMax () {
		 return this.yMax;
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
	 
	 public int getArea () {
		 return (xMax - xMin) * (yMax - yMin);
	 }
	 
	 public boolean contain (Rectangle rect) {
		 if (this.xMin <= rect.getxMin() && this.xMax >= rect.getxMax()
			&& this.yMin <= rect.getyMin() && this.yMax >= rect.getyMax()) {
			 return true;
		 }
		 return false;
	 }
	 
	 public boolean overlap (Rectangle rect) {
		 if (this.xMax <= rect.getxMin() || this.xMin >= rect.getxMax())
		 {
			 if (this.yMin >= rect.getyMax() || this.yMax <= rect.getyMin()) {
			     return false;
			 }
		 }
		 return true;
	 }
	 
	 public static Rectangle merge (Rectangle rect1, Rectangle rect2) {
  	   Rectangle newrect = new Rectangle();
  	   //System.out.println(rect2.getxMax());
  	   if (rect1.getxMin() <= rect2.getxMin()) {
  		   newrect.setxMin(rect1.getxMin());
  	   }else {
  		   newrect.setxMin(rect2.getxMin());
  	   }
  	   
  	   if (rect1.getxMax() >= rect2.getxMax()) {
  		   newrect.setxMax(rect1.getxMax());
  	   }else {
  		   newrect.setxMax(rect2.getxMax());
  	   }
  	   
  	   if (rect1.getyMin() <= rect2.getyMin()) {
  		   newrect.setyMin(rect1.getyMin());
  	   }else {
  		   newrect.setyMin(rect2.getyMin());
  	   }
  	   
  	   if (rect1.getyMax() >= rect2.getyMax()) {
  		   newrect.setyMax(rect1.getyMax());
  	   }else {
  		   newrect.setyMax(rect2.getyMax());;
  	   }
  	   return newrect;
     }
	 
//	 public static void main (String[] args) {
//		 Rectangle rect = new Rectangle(1,1,2,2);
//		 Rectangle rect1 = new Rectangle(0,1,0,1);
//		 System.out.println(rect.overlap(rect1));
//	 }
     
}
