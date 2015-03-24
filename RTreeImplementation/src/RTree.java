import java.util.ArrayList;
import java.util.List;

import Lib.Rectangle;
import Lib.DataItem;

public class RTree {
      private LeafNode root = new LeafNode();
      
      //to choose which node to insert we do it recursively
      private LeafNode chooseLeaf (Node start, Rectangle mbr) {
    	  if (start.isLeaf()) {
    		  return (LeafNode) start;
    	  }
    	  
    	  //then we go on to choose the rectangle that has the
    	  //least enlargement
    	  //we should have an index for each element in a node
    	  int bestchoice = 0; // to represent each rectangle
    	  int areaCurrent = start.getMbrArray()[bestchoice].getArea();
    	  //this is to calculate which rectangle has the minimum enlargement
    	  //after the insertion
    	  int areaInc = Rectangle.merge(start.getMbrArray()[bestchoice], mbr).getArea();
    	  for (int i = 0; i < start.getNum(); i++) {
    		  int areaCurrTmp = start.getMbrArray()[i].getArea();
    		  int areaIncTmp = Rectangle.merge(start.getMbrArray()[i], mbr).getArea()
    				           - areaCurrTmp;
    		  if (areaIncTmp > areaInc) {
    			  bestchoice = i;
    			  areaInc = areaIncTmp;
    			  areaCurrent = areaCurrTmp;
    		  }
    	  }
    	  
    	  return (LeafNode)(start.getMbrArray()[bestchoice]);
      }
      
      private void insertToLeaf (LeafNode leaf, DataItem dataitem) {
    	  if (!leaf.isFull()) {
    		  leaf.insertRecord(dataitem);
    		  adjustTree(leaf, null);
    		  return;
    	  }
    	  
    	  LeafNode newLeaf = leaf.split(dataitem);
    	  adjustTree(leaf, newLeaf);
      }
      
      //after splitting we should adjust the whole tree
      private void adjustTree (Node oldNode, Node newNode) {
    	  NonLeafNode parent;
    	  //when it reaches the root, we should consider whether
    	  //there is new node or not
    	  if (parent == null) {
    		  if (newNode == null) {
    			  return;
    		  }else {
    			  
    		  }
    		  
    	  }
      }
      
      public List<DataItem> Search (Rectangle rect) {
    	     List<DataItem> result = new ArrayList<DataItem>();
    	     searchhelp(root, rect, result);
    	     return result;
      }
      
      public void searchhelp (Node start, Rectangle rect, List<DataItem> result) {
    	  if(start.isLeaf()) {
  			for (int i = 0; i < start.getNum(); i++) {
  				if(start.getMbrArray()[i].overlap(range)) {
  					list.add(new Record(start.getMbrArray()[i], ((LeafNode)start).getObjArray()[i]));
  				}
  			}
  			return;
  		}
  		
  		//目录矩形节点，一一比较是否有重叠,有重叠就s继续进入索引子空间搜索
  		for(int i=0; i<start.getNum(); i++) {
  			if(start.getMbrArray()[i].overlap(range)) {
  				recursionSearch(((InternalNode)start).getChildArray()[i], range, list);                                                                                                                                                             
  			}
  		}
      }
      
      public void delete () {
    	  
      }
      
      public void condensTree () {
    	  
      }
}
