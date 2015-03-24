package Lib;
import java.io.Serializable;
import java.util.StringTokenizer;

public class Rectangle {
   private int xMin;
   private int yMin;
   private int xMax;
   private int yMax;
   
   public Rectangle(){
	   
   }
   
   public Rectangle(int xMin, int yMin, int xMax, int yMax) {
	   this.xMin = xMin;
	   this.yMin = yMin;
	   this.xMax = xMax;
	   this.yMax = yMax;
   }
   
   public void setXMin(int xMin) {
	   this.xMin = xMin;
   }
   
   public void setXMax(int xMax) {
	   this.xMax = xMax;
   }
   
   public void setYMin(int yMin) {
	   this.yMin = yMin;
   }
   
   public void setYMax(int yMax) {
	   this.yMax = yMax;
   }
   
   public int getXMin () {
	   return xMin;
   }
   
   public int getXMax () {
	   return xMax;
   }
   
   public int getYMin () {
	   return yMin;
   }
   
   public int getYMax () {
	   return yMax;
   }
   
   public boolean contain (Rectangle rectangle) {
	   if (xMin <= rectangle.getXMin() && xMax >= rectangle.getXMax()
			   && yMin <= rectangle.getYMin() && yMax >= rectangle.getYMax()) {
		   return true;
	   }else {
		   return false;
	   }
   }
   
   public boolean overlap (Rectangle rectangle) {
	   if (! (xMin >= rectangle.xMax || xMax <= rectangle.xMin)) {
		   if (!(yMin >= rectangle.yMax || yMax <= rectangle.yMin)) {
			   return true;
		   }
	   }   
	   
	   return false;
   }
   
   public int getArea () {
	   return (xMax - xMin) * (yMax - yMin);
   }
   
   public static Rectangle merge (Rectangle rect1, Rectangle rect2) {
	   Rectangle mbr = new Rectangle();
	   if (rect1.getXMin() < rect2.getXMin()) {
		   mbr.setXMin(rect1.getXMin());
	   }else {
		   mbr.setXMin(rect2.getXMin());
	   }
	   
	   if (rect1.getYMin() < rect2.getYMin()) {
		   mbr.setYMin(rect1.getYMin());
	   }else {
		   mbr.setYMin(rect2.getYMin());
	   }
	   
	   if (rect1.getXMax() > rect2.getXMax()) {
		   mbr.setXMax(rect1.getXMax());
	   }else {
		   mbr.setXMax(rect2.getXMax());
	   }
	   
	   if (rect1.getYMax() > rect2.getYMax()) {
		   mbr.setYMax(rect1.getYMax());
	   }else {
		   mbr.setYMax(rect2.getYMax());
	   }
       return mbr;
   }
}
