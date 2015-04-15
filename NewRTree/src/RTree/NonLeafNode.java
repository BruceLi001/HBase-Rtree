package RTree;

import java.util.ArrayList;
import java.util.List;

import Lib.DataItem;
import Lib.Rectangle;
import Lib.Node;
import RTree.LeafNode;
import RTree.NonLeafNode;

public class NonLeafNode extends Node{
       public Node[] childArray = new Node[MAX];
       public int[] keyArray = new int[MAX];
       public DataItem[] dataArray = null;
       public int childindex;
       
       public NonLeafNode () {
    	   
       }
       
		@Override
		public boolean isLeaf() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public DataItem[] getDataArray() {
			// TODO Auto-generated method stub
			return null;
		}
	
		@Override
		public Node[] getChildArray() {
			// TODO Auto-generated method stub
			return childArray;
		}
		
		public boolean isFull () {
 			return num == MAX;
		}
		
	    public void setChildArray (Node[] childArray) {
		   this.childArray = childArray;
	    }
	   
	    public void updateChild (int index, Node child) {
		   this.childArray[index] = child;
	    }
	    
	    public int getIndexChild (Node child) {
	    	   for (int i = 0; i < num; i++) {
	    		  if (!child.isLeaf()) {
	    		   if (this.childArray[i] == (NonLeafNode)child) {
	    			  // System.out.println(child);
	    			   return i;
	    		   }
	    		  }
	    		  if (child.isLeaf()) {
	    			  if (this.childArray[i] == (LeafNode)child) {
	        			  //System.out.println(child);
	        			   return i;
	        		   }
	    		  }
	    	   }
	    	   return -1;
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
       
       public void setchildIndex (int index) {
    	   //System.out.println(index);
    	   this.childindex = index;
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
    	   this.getKeyArray()[index] = key;
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
    	   this.childArray[index] = (NonLeafNode)child;
       }
       
       public boolean insertNode (Node newnode) {
		    if (isFull()) {
		    	return false;
		    }
		    if (!newnode.isLeaf()) {
		        ((NonLeafNode)newnode).setchildIndex(num);
		    }else {
		    	((LeafNode)newnode).setchildIndex(num);
		    }
		    childArray[num] = newnode;
		    mbrArray[num] = newnode.getMbr();
		    keyArray[num] = newnode.getKey();
		    //System.out.printlnchildArray[0]);
		    this.num = num + 1;
		    return true;
	   }
       
       //to distinguish the insertion of inserting at the end of an array 
       //and the insertion after splitting
       public boolean insertNewNode (Node newnode) {
       	 for (int i = 0; i < MAX; i++) {
       		 childArray[i] = null;
       		 mbrArray[i] = null;
       		 keyArray[i] = -1;
       	 }
       	 num = 0;
       	 childArray[num] = newnode;
       	 mbrArray[num] = newnode.getMbr();
       	 keyArray[num] = newnode.getKey();
       	 num = num + 1;
       	 return true;
       }
       
       public List<Node> PickSeeds (Node node) {
 	      List<Node> list = new ArrayList<Node>();
 	      List<Node> seeds = new ArrayList<Node>(); //store the results
 	      
 	      
 	      for (int i = 0; i < MAX; i++) {
 	    	  list.add(childArray[i]);
 	    	 
 	      }
 	      
 	     
 	      Node seed1 = list.get(0);
 	      Node seed2 = list.get(1);
 	      
 	     
 	      int spaceMax = Rectangle.merge(seed1.getMbr(), seed2.getMbr()).getArea() 
 	    		         - seed1.getMbr().getArea()
 	    		         - seed2.getMbr().getArea();
 	      //double loop traverse the list to find the seeds
 	      for (int i = 0; i < list.size(); i++) {
 	    	  for (int j = i + 1; j < list.size(); j++) {
 	    		   int space = Rectangle.merge(list.get(i).getMbr(), list.get(j).getMbr()).getArea() 
 	    				     - list.get(i).getMbr().getArea() - list.get(j).getMbr().getArea();
 	    		   if (space > spaceMax) {
 	    			   spaceMax = space;
 	    			   seed1 = list.get(i);
 	    			   seed2 = list.get(j);
 	    		   }
 	    	  }
 	    	}
 	        seeds.add(seed1);
 	        seeds.add(seed2);
 	        return seeds;
     }
     
     public Node PickNext (List<Node> list, Rectangle Nodembr, Rectangle newNodembr) {
     	 int areaIncMax = 0;
     	 Node next = null;
     	 for (Node element : list) {
     		  int areaInc1 = Rectangle.merge(element.getMbr(), Nodembr).getArea()
 				         - newNodembr.getArea();
 			  int areaInc2 = Rectangle.merge(element.getMbr(), newNodembr).getArea()
 					         - Nodembr.getArea();
 			  int areaIncDiff = Math.abs(areaInc1 - areaInc2);
 			  if (areaIncDiff >= areaIncMax) {
     			  areaIncMax = areaIncDiff;
     			  next = element;
     		  }
     	   }
     	 return next;
     }
     
     //to split when num == MAX
     public NonLeafNode split (Node node) {
     	   NonLeafNode newNode = new NonLeafNode();
     	   List<Node> list = new ArrayList<Node>();
     	   for (int i = 0; i < MAX; i++) {
     		   list.add(childArray[i]);
     	   }
     	       	   
     	   List<Node> seeds = new ArrayList<Node>();
     	   seeds = PickSeeds(node);
     	   Node seed1 = seeds.get(0);
     	   Node seed2 = seeds.get(1);
     	   
     	   
     	   
     	   this.insertNewNode(seed1); //to call a new method as this insertion is not the
     	                             //insertion happened when the node is not full.
     	   newNode.insertNewNode(seed2);
     	   list.remove(seed1);
     	   list.remove(seed2);
     	   
     	   //initialize the two rectangles of seed1 and seed2.
     	   Rectangle Nodembr = seed1.getMbr();
     	   Rectangle newNodembr = seed2.getMbr();
     	   
     	   
     	   Node next;
     	   while (list != null && list.size() != 0) {
	     		  if (num == MAX + 1 - MIN) {
	        		   for (Node element : list) {
	    	    			  newNode.insertNode(element);
	    	    		  }
	    	    		  return newNode;
	        	   }
	        	   
	        	   if (newNode.num == MAX + 1 - MIN) {
	        		   for (Node element : list) {
	    	    			  this.insertNode(element);
	    	    		  }
	    	    		  return newNode;
	        	   }
        	   
     		     next = PickNext(list, Nodembr, newNodembr);
     		     int areaIncLeaf = Rectangle.merge(next.getMbr(), Nodembr).getArea() 
     		    		           - Nodembr.getArea();
     		     int areaIncNew = Rectangle.merge(next.getMbr(), Nodembr).getArea()
     		    		          - newNodembr.getArea();
     		     if (areaIncLeaf > areaIncNew) {
     		    	 newNode.insertNode(next);
     		     }else {
     		    	 this.insertNode(next);
     		     }
     	   }
     	   return newNode;
     }

	
     
}
