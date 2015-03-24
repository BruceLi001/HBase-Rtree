import Lib.Rectangle;

public abstract class Node {
    private Node parent;
    protected int num; //to count the number of records in this node
    protected Rectangle mbrArray[] = new Rectangle [MAX];
    public static final int MAX = 1000;
    public static final int MIN = 500;
    
    public abstract boolean isLeaf();
    
    public int getNum () {
    	return num;
    }
    
    public boolean isFull () {
    	return num == MAX;
    }
    
    public Rectangle[] getMbrArray () {
    	return mbrArray;
    }
    
    public Rectangle getMbr () {
    	Rectangle mbr = mbrArray[0];
    	for (int i = 0; i < num; i++) {
    		mbr = Rectangle.merge(mbr, mbrArray[i]);
    	}
    	return mbr;
    }
}
