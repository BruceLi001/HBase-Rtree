package Tree;

import java.util.ArrayList;
import java.util.List;

import Lib.DataItem;
import Lib.Node;
import Lib.Rectangle;
import Lib.DataItem;

public class RTree {
	 public Node root;
	 List<LeafNode> listLeaf = new ArrayList<LeafNode>();
	 public static final int MAX = 10;
     public static final int MIN = 5;
	 
	 public RTree () {
		 this.root = new LeafNode();
	 }
	 
	 public DataItem insertData (DataItem data) {
		   // System.out.println(data.mbr); 
		    LeafNode leaf = chooseLeaf ((Node)root, data.getMbr()); //to select the area needs least enlargement;
		   // System.out.println(leaf);
		    //System.out.println(root.mbr);
		    insertLeaf(leaf, data);
		    return data;
	 }
	 
	 public LeafNode chooseLeaf (Node start, Rectangle mbr) {
		    if (start.isLeaf()) {
		    	//System.out.println(start);
//		    	if (root.parent != null) {
//		    	System.out.println(root.parent.num);
//		    	}
		    	//System.out.println(root);
		    	//System.out.println(start.parent);
		    	return (LeafNode) start;
		    }
		    //System.out.println(start.level);
		    //System.out.println(start.childArray[0].mbr);
		    //System.out.println(start.num);
		    //System.out.println(1);
		    //initialize the variables
		    int bestchoice = 0;
//		    if (start.parent != null) {
//		       System.out.println(start.parent.num);
//		    }
		    //System.out.println(start.getMbrArray()[0]);
		    //System.out.println(start.isLeaf());
		    //System.out.println(start.getMbrArray()[1]);
		    int areaCurr = start.getMbrArray()[bestchoice].getArea();
		    Rectangle tmp = start.getMbrArray()[bestchoice];
		    int areaInc = Rectangle.merge(tmp, mbr).getArea()
		    		      - tmp.getArea();
		    
		    for (int i = 0; i < start.getNum(); i++) {
		    	Rectangle tmptmp = start.getMbrArray()[i];
		    	//System.out.println(1);
		    	//Rectangle tmptmp = start.getchildArray()[i].getMbr();
		    	int tmpArea = tmptmp.getArea();
		    	int tmpAreaInc = Rectangle.merge(tmptmp, mbr).getArea()
		    			         - tmptmp.getArea();
		    	
		    	if (tmpAreaInc < areaInc) {
		    		areaInc = tmpAreaInc;
		    		bestchoice = i;
		    		areaCurr = tmpArea;
		    	}
		    	
		    	if (tmpAreaInc == areaInc) {
		    		areaInc = tmpAreaInc;
		    		if (areaCurr < tmpArea) {
  	    			  bestchoice = i;
  	    			  areaInc = tmpAreaInc;
  	    			  areaCurr = tmpArea;
  	    		  }
		    	}
		    }
		    //System.out.println(start.getchildArray()[bestchoice].mbrArray[0]);
		    LeafNode bestleaf = chooseLeaf (((NonLeafNode)start).getchildArray()[bestchoice], mbr);
  	        return bestleaf;
	 }
	 
	 public void insertLeaf (LeafNode leaf, DataItem data) {
	      //if there is still room in this leaf. We could insert directly
	      if (!leaf.isFull()) {
	    	  leaf.insertData(data);
	    	 // System.out.println(leaf.isFull());
	    	 // System.out.println(leaf.childArray);
	    	  adjustTree(null, leaf);
	    	  return;
	      }
	      //System.out.println(leaf);
	      //if the leaf is overflow after we insert, we should adjustTree
	      LeafNode newleaf = leaf.split(data);
	      adjustTree (newleaf, leaf);
	     // return;
     }
	 
	 
	 public void adjustTree (Node newNode, Node oldNode) {
		    if (oldNode.isRoot()) {
		      if (newNode == null) {
		    	return;
		      }else { //we have splitted a new node
		    	  root = new NonLeafNode();
		    	  root.setLevel(oldNode.getLevel() + 1);
		    	 // System.out.println(oldNode);
		    	  ((NonLeafNode)root).insertNode(oldNode);
		    	  ((NonLeafNode)root).insertNode(newNode);
		    	  oldNode.setParent(root);
		    	  newNode.setParent(root);
		    	  root.setOneChild(0, oldNode);
		    	  root.setOneChild(1, newNode);
		    	  //this.root = root;
		    	  //root.insertNode(oldNode);
		    	  //root.insertNode(newNode);
		    	 // System.out.println(newNode);
		    	  return;
		      }
		    }
		    
		    //System.out.println(oldNode.parent);
		    //oldNode.setParent(NonLeafNode oldparent);
		    NonLeafNode oldparent = (NonLeafNode) oldNode.getParent();
		   // System.out.println(oldparent.childArray);
		    //NonLeafNode oldparent = new NonLeafNode();
		    //oldNode.setParent(oldparent);
		    //System.out.println(oldparent.num);
//		    for (int i = 0; i < oldparent.num; i++) {
//		      if (oldparent.childArray[i] == oldNode) {
//		    	System.out.println(oldparent.childArray[i]);
//		    	//System.out.println(oldNode.childindex);
//		      }else {
//		    	 // oldparent.childArray[i] = oldNode;
//		    	  System.out.println(oldNode.num);
//		      }
//		    }
//		    //oldNode.setOneChild(index, child);
//		    if (newNode != null) {
//		    	newNode.setParent(oldparent);
//		    }
		    //System.out.println(oldparent.childArray[1]);
		    //System.out.println(newNode);
		    //System.out.println(oldNode.getNum());
		    //System.out.println(oldparent.getKey());
		    //System.out.println(oldparent.getIndexChild(oldNode));
		    //oldparent.setWhicChild()[oldparent.getIndexChild(oldNode)] = oldNode;
//		    for (Node element : oldparent.childArray) {
//		    	if (element == oldNode) {
//		    		System.out.println(oldparent.num);
//		    		System.out.println("coll");
//		    	}
//		    }
//		    
//		    for (int i = 0; i < oldparent.num; i++) {
//		    	if (i == oldparent.getIndexChild(oldNode)) {
//		    		System.out.println("not coll");
//		    	}
//		    }
		    
		    oldparent.setOneChild(oldparent.getIndexChild(oldNode), oldNode);
		    //oldparent.getMbrArray()[oldparent.getIndexChild(oldNode)] = oldNode.getMbr();
		    oldparent.setOneMbr(oldparent.getIndexChild(oldNode), oldNode.getMbr());
		    //oldparent.getKeyArray()[oldparent.getIndexChild(oldNode)] = oldNode.getKey();
		    oldparent.setOneKey(oldparent.getIndexChild(oldNode), oldNode.getKey());
		    Node En = oldparent.getchildArray()[oldparent.getIndexChild(oldNode)];
		    for (int i = 0; i < En.getNum(); i++) {
		    	En.mbr = Rectangle.merge(En.getMbrArray()[i], oldNode.getMbrArray()[i]);
		    }
		    
//		    if (newNode != null) {
//		    	if (oldparent.isFull()) {
//		    		NonLeafNode newoldparent = oldparent.split(newNode);
//		    		adjustTree(newoldparent, oldparent);
//		    	}else {
//		    		//System.out.println(oldparent.getIndexChild(oldNode));
//		    		oldparent.insertNode(newNode);
//		    		newNode.setParent(oldparent);
//		    		//oldparent.getchildArray()[oldparent.getIndexChild(newNode)] = newNode;
//		    		oldparent.setOneChild(oldparent.getIndexChild(oldNode), newNode);
//		    		
//		    		//System.out.println(oldparent.getIndexChild(oldNode));
//		    		oldparent.setOneKey(oldparent.getIndexChild(newNode), newNode.getKey());
//		    		oldparent.setOneMbr(oldparent.getIndexChild(newNode), newNode.getMbr());
//		    		adjustTree(null, oldparent);
//		    	}
//		    }
		    
		    if (newNode == null) {
		    	adjustTree(null, oldparent);
		    }else {
		    	if (oldparent.isFull()) {
		    		//System.out.println(1);
		    		NonLeafNode newoldparent = oldparent.split(newNode);
		    		//System.out.println(oldparent);
		    		adjustTree(newoldparent, oldparent);
		    	}else {
		    		//System.out.println(oldparent.getIndexChild(oldNode));
		    		oldparent.insertNode(newNode);
		    		newNode.setParent(oldparent);
		    		//oldparent.getchildArray()[oldparent.getIndexChild(newNode)] = newNode;
		    		oldparent.setOneChild(oldparent.getIndexChild(oldNode), newNode);
		    		
		    		//System.out.println(oldparent.getIndexChild(oldNode));
		    		oldparent.setOneKey(oldparent.getIndexChild(newNode), newNode.getKey());
		    		oldparent.setOneMbr(oldparent.getIndexChild(newNode), newNode.getMbr());
		    		adjustTree(null, oldparent);
		    	}
		    }
		    
	 }
	 
	 
	 public NonLeafNode insertNode (Node node) {
		    NonLeafNode leaf = chooseNode (root, node.getMbr()); //to select the area needs least enlargement;
		    insertNode(leaf, node);
		    return (NonLeafNode) node;
	 }
	 
	 public NonLeafNode chooseNode (Node start, Rectangle mbr) {
//		    if (start.isLeaf()) {
//		    	return (LeafNode) start;
//		    }
		    
		    //initialize the varibales
		    int bestchoice = 0;
		    int areaCurr = start.getMbrArray()[bestchoice].getArea();
		    Rectangle tmp = start.getMbrArray()[bestchoice];
		    int areaInc = Rectangle.merge(tmp, mbr).getArea()
		    		      - tmp.getArea();
		    
		    for (int i = 0; i < start.getNum(); i++) {
		    	Rectangle tmptmp = start.getMbrArray()[i];
		    	int tmpArea = tmptmp.getArea();
		    	int tmpAreaInc = Rectangle.merge(tmptmp, mbr).getArea()
		    			         - tmptmp.getArea();
		    	
		    	if (tmpAreaInc < areaInc) {
		    		areaInc = tmpAreaInc;
		    		bestchoice = i;
		    		areaCurr = tmpArea;
		    	}
		    	
		    	if (tmpAreaInc == areaInc) {
		    		areaInc = tmpAreaInc;
		    		if (areaCurr < tmpArea) {
	    			  bestchoice = i;
	    			  areaInc = tmpAreaInc;
	    			  areaCurr = tmpArea;
	    		  }
		    	}
		    }
		    NonLeafNode bestleaf = chooseNode (start.getchildArray()[bestchoice], mbr);
	        return bestleaf;
	 }
	 
	 public void insertNode (NonLeafNode node, Node insertNode) {
	      //if there is still room in this leaf. We could insert directly
	      if (!node.isFull()) {
	    	  node.insertNode(insertNode);
	    	  adjustTree(null, node);
	    	  return;
	      }
	      
	      //if the leaf is overflow after we insert, we should adjustTree
	      NonLeafNode newnode = node.split(insertNode);
	      adjustTree (newnode, node);
    }
	 
	 public Rectangle deleteData (Rectangle mbr) {
	      LeafNode leafToDelete = new LeafNode();
	      //System.out.println(root);
	      leafToDelete = FindLeaf(root, mbr);
	      //System.out.println(leafToDelete);
	     // System.out.println(root.getchildArray()[0]);
	      if (deleteFromLeaf(leafToDelete, mbr)) {
	    	  this.condenseTree(leafToDelete); //to check whether there is enough elements after deletion
	    	  if (root.getchildArray().length == 1) {
	    		  root = (LeafNode) root.getchildArray()[0];
	    		  return mbr;
	    	  }
	    	  //System.out.println(1);
	    	  return mbr;
	      }
	      
	      return null;
     } 
  
     public LeafNode FindLeaf (Node start, Rectangle mbr) {
    	  //System.out.println(start.num);
    	  //System.out.println(start.getMbrArray()[0].getyMax());
    	  //System.out.println(mbr.getxMin());
	      if (start.isLeaf()) {
	    	  for (int i = 0; i < start.getNum(); i++) {
	    		 // System.out.println(start.getMbrArray()[i].getxMax());
	    		  //System.out.println(start.getMbrArray());
	    		  if (start.getMbrArray()[i].contain(mbr) || 
	    				  start.getMbrArray()[i].overlap(mbr) || 
	    				  start.getMbrArray()[i] == mbr) {
	    			      
	    			  return (LeafNode)start;
	    		  }
	    	  }
	      } //search from root to leaf
	    	  //System.out.println(mbr.getyMin());
	    	  //System.out.println(start.mbr.getxMin());
	      for (int i = 0 ; i < start.getNum(); i++) {
//	    		  if (start.getchildArray()[i].getMbr().overlap(data.getMbr())) {
//	    			  FindLeaf(start.getchildArray()[i], data);
//	    		  }
	    	     
	    		  if (start.getchildArray()[i].getMbr().contain(mbr) 
	    				  || start.getchildArray()[i].getMbr().overlap(mbr)) {
	    			  return FindLeaf(start.getchildArray()[i], mbr);
	    		  }
	       }
	    	  
	      return null;
     }
     
     public boolean deleteFromLeaf (LeafNode leaf, Rectangle mbr) {
    	    //System.out.println(leaf);
    	 
    	    for (int i = 0; i < leaf.getNum(); i++) {
    	    	if (leaf.getMbrArray()[i].contain(mbr) || 
	    		    leaf.getMbrArray()[i].overlap(mbr) || 
	    		    leaf.getMbrArray()[i] == mbr) {
    	    		leaf.getDataArray()[i] = null;
    	    		leaf.getMbrArray()[i] = null;
    	    		//leaf.getKeyArray()[i] = (Integer) null;
    	    		leaf.getKeyArray()[i] = -1;
    	    		leaf.num--;
    	    		return true;
    	    	}

    	    }
    	 
    	    return false;
     }
     
     public void condenseTree (Node LeafToDelete) {
    	    //System.out.println(LeafToDelete.parent);
    	    Node parent = LeafToDelete.getParent();
    	    List<DataItem> storedelete = new ArrayList<DataItem>();
    	    List<Node> storedeleteNode = new ArrayList<Node>();
    	    int level = LeafToDelete.getLevel();
    	    
    	    if (LeafToDelete.isRoot()) {
    	    	for (DataItem element : storedelete) {
    	    		this.insertData(element);
    	    		//System.out.println(1);
    	    	}
    	    	for (Node element : storedeleteNode) {
    	    		 
    	    	}
    	    	return;
    	    }
    	    //System.out.println(1);
    	    if (LeafToDelete.getNum() < MIN) {
    	    	parent.setOneChild(parent.getIndexChild(LeafToDelete), null);
    	        for (int i = 0; i < LeafToDelete.getNum(); i++) {
    	        	if (LeafToDelete.getDataArray() != null) {
        	    		storedelete.add(LeafToDelete.getDataArray()[i]);
        	    	}else {
        	    		storedeleteNode.add(LeafToDelete.getchildArray()[i]);
        	    	}
    	        }
    	        condenseTree(parent);
    	    }else {
    	    	//System.out.println(LeafToDelete);
    	    	//Rectangle.merge(LeafToDelete.getMbr(), parent.getMbr());
    	    	//System.out.println(LeafToDelete);
    	    	//condenseTree(parent);
    	    	return;
    	    }    
     }
   
     public static void main (String[] args) {
    	 RTree testTree = new RTree();
    	
    	 for (int i = 0; i < 100; i++){
    	   //System.out.println(testTree.root);
    	     testTree.insertData(new DataItem(i, new Rectangle(i,i + 1,i,i+1), "col" + i));
    		 //testTree.deleteData(new DataItem(i, new Rectangle(i,i+1,i,i+1), "col" + i));
    	    // System.out.println(testTree.root.getMbr().getxMin());
    	 }
    	 
    	 //for (int i = 0; i < 3; i++) {
    		// testTree.deleteData(new DataItem(i, new Rectangle(i,i+1,i,i+1), "col" + i));
    		 System.out.println(testTree.deleteData(new Rectangle(3,4,3,4)));
    		 //System.out.println(testTree.deleteData(new Rectangle(0,1,0,1)));
    	// }
    	 
    	
    	 
    	// System.out.println(testTree.root.parent.num);
    	 
       }
     
}
