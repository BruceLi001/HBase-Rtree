package Tree;

import java.util.ArrayList;
import java.util.List;

import Lib.Rectangle;
import Lib.Node;
import Lib.DataItem;;

public class LeafNode extends Node{
       
	   //this.childArray = null;
	   public Node[] childArray = null;
	   //ArrayList<DataItem> dataInNode = new ArrayList<DataItem>();
	   public DataItem[] dataArray = new DataItem[MAX];
	   public int childindex;
	   //public int num = 0;
	   
	   public LeafNode () {
    	   
       }
	   
	   public boolean isLeaf() {
		   return true;
	   }
	   
	   public DataItem[] getDataArray () {
		   return this.dataArray;
	   }
	   
	   public void setDataArray (DataItem[] dataArray) {
		   this.dataArray = dataArray;
	   }
	   
	   public DataItem updateData (int index, DataItem data) {
		   for (int i = 0; i < num; i++) {
			   if (i == index) {
				   dataArray[i] = data;
			   } 
		   }
		   return data;
	   }
	   public boolean insertData (DataItem data) {
		      if (isFull()) {
		    	  return false;
		      }
		      
		      
		      keyArray[num] = data.getKey();
		      mbrArray[num] = data.getMbr();
		      //System.out.println(data.getMbr().getxMax());
		      //System.out.println(this.getMbr().getxMax());
		      //System.out.println(data.mbr.getxMax() - data.mbr.getxMin());
		      //System.out.println(num);
		      //System.out.println(mbrArray[num]);
		      dataArray[num] = data;
//		      for (int i = 0; i < num; i++) {
//		    	  this.mbr = Rectangle.merge(mbrArray[i], data.getMbr());
//		      }
		      num++;
		      //System.out.println(mbrArray[1]);
		      
		      //System.out.println(mbr);
		      return true;
		      
	   }
	   
	   
	   public List<DataItem> PickSeeds (DataItem data) {
		      List<DataItem> list = new ArrayList<DataItem>();
		      List<DataItem> seeds = new ArrayList<DataItem>(); //store the results
		      
		      
		      for (int i = 0; i < MAX; i++) {
		    	  list.add(dataArray[i]);
		    	  //System.out.println(list);
		    	  //System.out.println(dataArray[i]);  
		      }
		      
		      //System.out.println(list.get(9).mbr);
		      DataItem seed1 = list.get(0);
		      DataItem seed2 = list.get(1);
		      
		      //System.out.println(list);
		      //System.out.println(list);
		      //System.out.println(seed1.getMbr().getArea());
		      //System.out.println(seed1);
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
	   
	   //to split when num == max
	   public LeafNode split (DataItem data) {
		      LeafNode newleaf = new LeafNode();
		      //System.out.println(this);
		      //retrive all the data in this node and clean it
		      List<DataItem> list = new ArrayList<DataItem>();
		      for (int i = 0; i < MAX; i++) {
		    	  list.add(dataArray[i]);
		    	 // System.out.println(dataArray[i]);
		      }
		      //System.out.println(dataArray);
		      List<DataItem> seeds = new ArrayList<DataItem>();
		      seeds = PickSeeds(data);
		      //System.out.println(seeds);
		      DataItem seed1 = seeds.get(0);
		      DataItem seed2 = seeds.get(1);
		      clean();
		      
		      
		      
		      //then insert the seeds into the two nodes;
		      this.insertData(seed1);
		      newleaf.insertData(seed2);
		      list.remove(seed1);
		      list.remove(seed2);
		      
		     
		      Rectangle leafMbr = seed1.getMbr();
		      Rectangle newleafMbr = seed2.getMbr();
		      //System.out.println(leafMbr);
		      //System.out.println(list);
		      //then select the nearest data to each seed
		      while (list != null && list.size() != 0) {
		    	  
		    	  int areaIncMax = 0;
//		    	  /DataItem element;
		    	  //our goal is to find the dataitem to make the area
		    	  //increment of both seeds the biggest. This means we have found the 
		    	  //nearest dataitem to insert.
		    	  
		    	//once we have reached enough numbers of a node: num = MAX + 1 - MIN
		    	 //we should insert the rest of the list into the other node
		    	  if (num == MAX + 1 - MIN) {
		    		  for (DataItem dataitem : list) {
		    			  newleaf.insertData(dataitem);
		    		  }
		    		  return newleaf;
		    	  }
		    	  
		    	  if (newleaf.num == MAX + 1 - MIN) {
		    		  for (DataItem dataitem : list) {
		    			  this.insertData(dataitem);
		    		  }
		    		  return newleaf;
		    	  }
		    	  
		    	  DataItem toInsert = null;
		    	  boolean insertToNew = false;
		    	  for (DataItem element : list) {
		    		  int areaInc1 = Rectangle.merge(element.getMbr(), leafMbr).getArea()
		    				         - leafMbr.getArea();
		    		  int areaInc2 = Rectangle.merge(element.getMbr(), newleafMbr).getArea()
		    				         - newleafMbr.getArea();
		    		  int areaIncDiff = Math.abs(areaInc1 - areaInc2);
		    		  if (areaIncDiff >= areaIncMax) {
		    			  areaIncMax = areaIncDiff;
		    			  //insertToNew = false;
		    			  toInsert = element;
		    			  if (areaInc1 > areaInc2) {
		    				  insertToNew = true;
		    			  }else {
		    				  insertToNew = false;
		    			  }
		    			  
		    		  }
		    	  }
		    	  if (insertToNew) {
	    			  newleaf.insertData(toInsert);
	    			  //update the rectangle
	    			  newleafMbr = Rectangle.merge(newleafMbr, toInsert.getMbr());
	    			  //System.out.println(toInsert);
	    		  }else {
	    			  insertData(toInsert);
	    			  //System.out.println(toInsert);
	    			  //System.out.println(this);
	    			 // System.out.println(isFull());
	    			  leafMbr = Rectangle.merge(leafMbr, toInsert.getMbr());
	    		  }
		    	  
		    	  list.remove(toInsert);
		    	  
		    	
		    }
		      
		      //System.out.println(newleaf);
		      //System.out.println(this);
		      //System.out.println(newleaf);
		      return newleaf;
	   }
	   
	   public void clean () {
		   for (int i = 0; i < num; i++) {
			   mbrArray[i] = null;
			   keyArray[i] = -1;
			   dataArray[i] = null;
		   }
		   num = 0;
	   }


	public void condenseTree() {
		// TODO Auto-generated method stub
		
	}
	   
	   
	  
//	   public boolean isLeaf () {
//		   return true;
//	   }
//	   public static void main (String[] args) {
//		   LeafNode leaf = new LeafNode();
//		   for(int i = 0; i < 11; i++){
//		   System.out.println(leaf.insertData(new DataItem(3, new Rectangle(1,1,2,2), "col")));
//		   System.out.println(leaf.num);
//		   }
//	   }

}
