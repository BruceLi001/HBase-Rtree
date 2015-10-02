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
	public double MinDist;
	
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
    	 //System.out.println(data);
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
	      
	     
	      double spaceMax = Rectangle.merge(seed1.getMbr(), seed2.getMbr()).getArea() 
	    		         - seed1.getMbr().getArea()
	    		         - seed2.getMbr().getArea();
	      //double loop traverse the list to find the seeds
	      for (int i = 0; i < list.size(); i++) {
	    	  for (int j = i + 1; j < list.size(); j++) {
	    		   double space = Rectangle.merge(list.get(i).getMbr(), list.get(j).getMbr()).getArea() 
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
    	 double areaIncMax = 0;
    	 DataItem next = null;
    	 for (DataItem element : list) {
    		  double areaInc1 = Rectangle.merge(element.getMbr(), leafmbr).getArea()
				         - leafmbr.getArea();
			  double areaInc2 = Rectangle.merge(element.getMbr(), newleafmbr).getArea()
					         - leafmbr.getArea();
			  double areaIncDiff = Math.abs(areaInc1 - areaInc2);
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
    		     double areaIncLeaf = Rectangle.merge(next.getMbr(), leafmbr).getArea() 
    		    		           - leafmbr.getArea();
    		     double areaIncNew = Rectangle.merge(next.getMbr(), leafmbr).getArea()
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

	@Override
	public double getMaxPrice() {
		double max = Double.MIN_VALUE;
		for (int i = 0; i < this.getNum(); i++) {
			if (max < dataArray[i].price) {
				max = dataArray[i].price;
			}
		}
		return max;
	}

	@Override
	public double getMinPrice() {
		double min = Double.MAX_VALUE;
		for (int i = 0; i < this.getNum(); i++) {
			if (min > dataArray[i].price) {
				min = dataArray[i].price;
			}
		}
		return min;
	}

	@Override
	public void setMinDist(double MinDist) {
		// TODO Auto-generated method stub
		this.MinDist = MinDist;
	}

	@Override
	public double getMaxLong() {
		double max = Double.MIN_VALUE;
		for (int i = 0; i < this.getNum(); i++) {
			if (max < dataArray[i].longitude) {
				max = dataArray[i].longitude;
			}
		}
		
		return max;
	}

	@Override
	public double getMinLong() {
		double min = Double.MAX_VALUE;
		for (int i = 0; i < this.getNum(); i++) {
			//System.out.println(dataArray[i].longitude);
			//System.out.println(dataArray[i]);
			if (min > dataArray[i].longitude) {
				min = dataArray[i].longitude;
			}
		}
		return min;
	}

	@Override
	public double getMaxLati() {
		double max = Double.MIN_VALUE;
		for (int i = 0; i < this.getNum(); i++) {
			if (max < dataArray[i].latitude) {
				max = dataArray[i].latitude;
			}
		}
		return max;
	}

	@Override
	public double getMinLati() {
		double min = Double.MAX_VALUE;
		for (int i = 0; i < this.getNum(); i++) {
			if (min > dataArray[i].latitude) {
				min = dataArray[i].latitude;
			}
		}
		return min;
	}

	@Override
	public double getMaxRate() {
		double max = Double.MIN_VALUE;
		for (int i = 0; i < this.getNum(); i++) {
			if (max < dataArray[i].rate) {
				max = dataArray[i].rate;
			}
		}
		return max;
	}

	@Override
	public double getMinRate() {
		double min = Double.MAX_VALUE;
		for (int i = 0; i < this.getNum(); i++) {
			if (min > dataArray[i].rate) {
				min = dataArray[i].rate;
			}
		}
		return min;
	}
       
}
