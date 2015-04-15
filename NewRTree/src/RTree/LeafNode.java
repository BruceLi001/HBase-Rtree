package RTree;

import java.util.ArrayList;
import java.util.List;

import Lib.Node;
import Lib.Rectangle;
import Lib.DataItem;

public class LeafNode extends Node{
    
	public Node[] childArray = null;
	public DataItem[] dataArray = new DataItem[MAX];
	public int[] keyArray = new int[MAX];
	public int childindex;
	
	public LeafNode () {
		
	}
	
	public void setchildIndex (int index) {
		this.childindex = index;
	}
	
	public int getchildIndex () {
		return this.childindex;
	}
	
	public void setDataArray (DataItem[] dataArray) {
		   this.dataArray = dataArray;
	}
	
	public int[] getKeyArray () {
		return keyArray;
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
    	if (this.isFull()) {
    		return false;
    	}
    	
    	dataArray[num] = data;
    	mbrArray[num] = data.getMbr();
    	keyArray[num] = data.getKey();
    	num = num + 1;
    	return true;
    }
    
    //to distinguish the insertion of inserting at the end of an array 
    //and the insertion after splitting
    public boolean insertNewData (DataItem data) {
    	 for (int i = 0; i < MAX; i++) {
    		 dataArray[i] = null;
    		 mbrArray[i] = null;
    		 keyArray[i] = -1;
    	 }
    	 num = 0;
    	 dataArray[num] = data;
    	 mbrArray[num] = data.getMbr();
    	 keyArray[num] = data.getKey();
    	 num = num + 1;
    	 return true;
    }
    
    public List<DataItem> PickSeeds (DataItem data) {
	      List<DataItem> list = new ArrayList<DataItem>();
	      List<DataItem> seeds = new ArrayList<DataItem>(); //store the results
	      
	      
	      for (int i = 0; i < MAX; i++) {
	    	  list.add(dataArray[i]);
	    	 
	      }
	      
	     
	      DataItem seed1 = list.get(0);
	      DataItem seed2 = list.get(1);
	      
	     
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
    
    public DataItem PickNext (List<DataItem> list, Rectangle leafmbr, Rectangle newleafmbr) {
    	 int areaIncMax = 0;
    	 DataItem next = null;
    	 for (DataItem element : list) {
    		  int areaInc1 = Rectangle.merge(element.getMbr(), leafmbr).getArea()
				         - leafmbr.getArea();
			  int areaInc2 = Rectangle.merge(element.getMbr(), newleafmbr).getArea()
					         - leafmbr.getArea();
			  int areaIncDiff = Math.abs(areaInc1 - areaInc2);
			  if (areaIncDiff >= areaIncMax) {
    			  areaIncMax = areaIncDiff;
    			  next = element;
    		  }
    	   }
    	 return next;
    }
    
    //to split when num == MAX
    public LeafNode split (DataItem data) {
    	   LeafNode newleaf = new LeafNode();
    	   List<DataItem> list = new ArrayList<DataItem>();
    	   for (int i = 0; i < MAX; i++) {
    		   list.add(dataArray[i]);
    	   }
    	       	   
    	   List<DataItem> seeds = new ArrayList<DataItem>();
    	   seeds = PickSeeds(data);
    	   DataItem seed1 = seeds.get(0);
    	   DataItem seed2 = seeds.get(1);
    	   
    	   
    	   
    	   this.insertNewData(seed1); //to call a new method as this insertion is not the
    	                             //insertion happened when the node is not full.
    	   newleaf.insertNewData(seed2);
    	   list.remove(seed1);
    	   list.remove(seed2);
    	   
    	   //initialize the two rectangles of seed1 and seed2.
    	   Rectangle leafmbr = seed1.getMbr();
    	   Rectangle newleafmbr = seed2.getMbr();
    	  
    	   
    	   DataItem next = new DataItem();
    	   while (list != null && list.size() != 0) {
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
    		     next = PickNext(list, leafmbr, newleafmbr);
    		     int areaIncLeaf = Rectangle.merge(next.getMbr(), leafmbr).getArea() 
    		    		           - leafmbr.getArea();
    		     int areaIncNew = Rectangle.merge(next.getMbr(), leafmbr).getArea()
    		    		          - newleafmbr.getArea();
    		     if (areaIncLeaf > areaIncNew) {
    		    	 newleaf.insertData(next);
    		     }else {
    		    	 this.insertData(next);
    		     }
    	   }
    	   return newleaf;
    }
    
    public Rectangle getMbr () {
       Rectangle rect = dataArray[0].getMbr();
   	   for (int i = 0; i < num; i++) {
   		   rect = Rectangle.merge(rect, dataArray[i].getMbr());
   	   }
   	   return rect;
    }
    
	@Override
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public DataItem[] getDataArray() {
		// TODO Auto-generated method stub
		return dataArray;
	}

	@Override
	public Node[] getChildArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Node split(Node node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertNode(Node node) {
		// TODO Auto-generated method stub
		return false;
	}
       
}
