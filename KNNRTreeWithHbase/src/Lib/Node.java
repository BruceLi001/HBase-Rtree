package Lib;

public abstract class Node {
       public static final int MAX = 100;
       public static final int MIN = 50;
       
       public int num;
       public Node parent;
       public int level;
       public int childindex;
       public Rectangle mbr;
       public int key;
       public Rectangle[] mbrArray = new Rectangle[MAX];
       public double MinDist;
       
       
       public boolean isRoot() {
    	   return parent == null;
       }
       
       public abstract boolean isLeaf ();
       
       public abstract DataItem[] getDataArray ();
       
       public abstract Node[] getChildArray();
       
       public abstract Node split(Node node);
       
       public abstract boolean insertNode(Node node);
       
       public abstract void setMinDist(double MinDist);
       
       public abstract double getMaxPrice();
       
       public abstract double getMinPrice();
       
       public abstract double getMaxLong();
       
       public abstract double getMinLong();
       
       public abstract double getMaxLati();
       
       public abstract double getMinLati();
       
       public abstract double getMaxRate();
       
       public abstract double getMinRate();
       
       public boolean isFull () {
    	   return num == MAX;
       }
       
       public long getNum () {
    	   return this.num;
       }
       
       public void setNum (int num) {
    	   this.num = num;
       }
       
       public Rectangle[] getMbrArray () {
    	   return mbrArray;
       }
       
       public void setMbrArray (Rectangle[] mbrArray) {
    	   this.mbrArray = mbrArray;
       }
       
       public int getKey () {
    	   return this.key;
       }
       
       public void setKey (int key) {
    	   this.key = key;
       }
       
       public int getLevel () {
    	   return this.level;
       }
       
       public void setLevel (int level) {
    	   this.level = level;
       }
       
       public Node getParent () {
    	   return this.parent;
       }
       
       public void setParent (Node parent) {
    	   this.parent = parent;
       }
       
       public Rectangle getMbr () {
    	   Rectangle rect = mbrArray[0];
    	   for (int i = 0; i < num; i++) {
    		   rect = Rectangle.merge(rect, mbrArray[i]);
    	   }
    	   return rect;
       }
       
      
}
