package Tree;

import java.util.ArrayList;
import java.util.List;

import Lib.DataItem;
import Lib.Node;
import Lib.Node;
import Lib.Rectangle;

public class NonLeafNode extends Node{
       //nonleafnode class is similar to leaf node because we only 
	   //replace the data with child node
	   public Node[] childArray = new Node[MAX];
	   public DataItem[] dataArray = null;
	   private Node toInsert;
	  
	   public NonLeafNode () {
		   
	   }
	   
	   public boolean isLeaf() {
		   return false;
	   }
	   
	   public DataItem[] getDataArray () {
              return null;		   
	   }
	   
	   public Node[] getChildArray () {
		   return this.childArray;
	   }
	   
	   public void setChildArray (Node[] childArray) {
		   this.childArray = childArray;
	   }
	   
	   public void updateChild (int index, Node child) {
		   this.childArray[index] = child;
	   }
	   
	   public boolean insertNode (Node newnode) {
		    if (isFull()) {
		    	return false;
		    }
		    
		    childArray[num] = newnode;
		    //System.out.println(newnode.num);
		   // System.out.println(newnode.getMbr());
		    mbrArray[num] = newnode.getMbr();
		    //System.out.println(newnode.mbr);
		    keyArray[num] = newnode.getKey();
		    //newnode.setchildIndex(num);
//		    for (int i = 0; i < num; i++) {
//		    	  this.mbr = Rectangle.merge(mbrArray[i], newnode.getMbr());
//		      }
		    num++;
		   
		    return true;
	   }
	   
	   public List<Node> PickSeeds (Node data) {
		      List<Node> list = new ArrayList<Node>();
		      List<Node> seeds = new ArrayList<Node>(); //store the results
		      
		      
		      for (int i = 0; i < MAX; i++) {
		    	  list.add(childArray[i]);
		    	  
		      }
		      
		      Node seed1 = list.get(0);
		      Node seed2 = list.get(1);
		      
		      int spaceMax = Rectangle.merge(seed1.getMbr(), seed2.getMbr()).getArea() - seed1.getMbr().getArea()
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
	   
	   //to split when num == max
	   public NonLeafNode split (Node node) {
		      NonLeafNode newNode = new NonLeafNode();
		      //retrive all the data in this node and clean it
		      List<Node> list = new ArrayList<Node>();
		      for (int i = 0; i < MAX; i++) {
		    	  list.add(childArray[i]);
		      }
		      
		      List<Node> seeds = new ArrayList<Node>();
		      seeds = PickSeeds(node);
		      clean();
		      
		      Node seed1 = seeds.get(0);
		      Node seed2 = seeds.get(1);
		      
		      //then insert the seeds into the two nodes;
		      this.insertNode(seed1);
		      newNode.insertNode(seed2);
		      list.remove(seed1);
		      list.remove(seed2);
		      
		     
		      Rectangle NodeMbr = seed1.getMbr();
		      Rectangle newNodeMbr = seed2.getMbr();
		      //then select the nearest data to each seed
		      while (list != null && list.size() != 0) {
		    	  
		    	  int areaIncMax = 0;
//		    	  /Node element;
		    	  //our goal is to find the Node to make the area
		    	  //increment of both seeds the biggest. This means we have found the 
		    	  //nearest Node to insert.
		    	  
		    	//once we have reached enough numbers of a node: num = MAX + 1 - MIN
		    	 //we should insert the rest of the list into the other node
		    	  if (num == MAX + 1 - MIN) {
		    		  for (Node Node : list) {
		    			  newNode.insertNode(Node);
		    		  }
		    		  return newNode;
		    	  }
		    	  
		    	  if (newNode.num == MAX + 1 - MIN) {
		    		  for (Node Node : list) {
		    			  this.insertNode(Node);
		    		  }
		    		  return newNode;
		    	  }
		    	  
		    	  toInsert = null;
		    	  boolean insertToNew = false;
		    	  for (Node element : list) {
		    		  int areaInc1 = Rectangle.merge(element.getMbr(), NodeMbr).getArea()
		    				         - NodeMbr.getArea();
		    		  int areaInc2 = Rectangle.merge(element.getMbr(), newNodeMbr).getArea()
		    				         - newNodeMbr.getArea();
		    		  int areaIncDiff = Math.abs(areaInc1 - areaInc2);
		    		  if (areaIncDiff >= areaIncMax) {
		    			  areaIncMax = areaIncDiff;
		    			  //insertToNew = false;
		    			  //Node toInsert = element;
		    			  toInsert = element;
		    			  if (areaInc1 > areaInc2) {
		    				  insertToNew = true;
		    			  }
		    			  if (areaInc1 < areaInc2) {
		    				  insertToNew = false;
		    			  }
		    		  }
		    	  }
		    	  if (insertToNew) {
	    			  newNode.insertNode(toInsert);
	    			  //update the rectangle
	    			 // System.out.println(1);
	    			  newNodeMbr = Rectangle.merge(newNodeMbr, toInsert.getMbr());
	    		  }else {
	    			  this.insertNode(toInsert);
	    			  //System.out.println(toInsert);
	    			  //System.out.println(NodeMbr);
	    			  NodeMbr = Rectangle.merge(NodeMbr, toInsert.getMbr());
	    		  }
		    	  
		    	  list.remove(toInsert);
		    	  
		    	
		    }
		      
		    
		      return newNode;
	   }
	   
	   public void clean () {
		   for (int i = 0; i < num; i++) {
			   mbrArray[i] = null;
			   //keyArray[i] = (Integer) null;
			   keyArray[i] = -1;
			   childArray[i] = null;
		   }
	   }
	  
}
