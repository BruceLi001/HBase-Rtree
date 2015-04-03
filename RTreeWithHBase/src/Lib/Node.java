package Lib;

public abstract class Node {
       public static final int MAX = 100;
       public static final int MIN = 50;
	   
       public int num;
       public Node parent;
       //public Node[] childArray;
       public int level;
       public int childindex;
       public Rectangle mbr;
       public int key;
       
       public Rectangle[] mbrArray = new Rectangle[MAX];
       public int[] keyArray = new int[MAX]; //to store the key of each element in this node.
       public Node[] childArray = new Node[MAX];
	   public DataItem[] dataArray = new DataItem[MAX];
       
       //to have the number of elements in this node
      public int getNum () {
    	   return this.num;
       }
       
       //to have the rectangles in this node
       public Rectangle[] getMbrArray(){
    	   return this.mbrArray;
       }
       
     
       
       public void setMbr (Rectangle mbr) {
    	   this.mbr = mbr;
       }
       
       
       public void setOneMbr (int index, Rectangle mbr) {
    	   this.getMbrArray()[index] = mbr;
       }
       
       public void setMbrArray (Rectangle[] mbrArray) {
    	   this.mbrArray = mbrArray;
       }
       
       public void setOneKey (int index, int key) {
    	  // System.out.println(index);
    	   this.keyArray[index] = key;
       }
       
       public int[] getKeyArray () {
    	   return this.keyArray;
       }
       
       public void setKeyArray (int[] keyArray) {
    	   this.keyArray = keyArray;
       }
       
       //have the key of this node
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
       
       
       public void setOneChild (int index, Node child) {
    	   //System.out.println(index);
    	   this.childArray[index] = child;
       }
       
       public Node[] getchildArray () {
    	   return this.childArray;
       }
       
       public void setchildArray (Node[] childArray) {
    	   this.childArray = childArray;
       }
       
       public boolean isFull () {
    	   return num == MAX;
       }
       
       public boolean isLeaf () {
    	   return childArray == null;
       }
       
       public void setchildIndex (int index) {
    	   //System.out.println(index);
    	   this.childindex = index;
       }
       
       public Node getWhichChild (int index) {
    	   for (int i = 0; i < num; i++) {
    		   if (i == index) {
    			   return childArray[i];
    		   }
    	   }
    	   return null;
       }
       
       public Node getTheChild (Node child) {
    	   for (int i = 0; i < num; i++) {
    		   if (this.childArray[i] == child) {
    			   return child;
    		   }
    	   }
    	   return null;
       }
       
       public void setWhichChild (int index, Node child) {
    	      for (int i = 0; i < num; i++) {
    	    	  if (i == index) {
    	    	     childArray[i] = child;	  
    	    	  }
    	      }
    	      //return null;
       }
       
       public int getIndexChild (Node child) {
    	   for (int i = 0; i < num; i++) {
    		   if (childArray[i] == child) {
    			  // System.out.println(child);
    			   return i;
    		   }
    	   }
    	   return -1;
       }
       
       public boolean isRoot () {
    	   //System.out.println(1);
    	   return parent == null;
    	   
       }
       
       public Rectangle getMbr() {
   		   Rectangle mbr = mbrArray[0];
   		   for (int i = 1; i < num; i++) {
   			   mbr = Rectangle.merge(mbr, mbrArray[i]);
   		   }
   		   return mbr;
   	   }
       
       public abstract DataItem[] getDataArray ();
       
//       public static void main (String[] args) {
//    	   Node node = new Node();
//    	   node.setLevel(5);
//    	   System.out.println(node.level);
//       }
      
}
