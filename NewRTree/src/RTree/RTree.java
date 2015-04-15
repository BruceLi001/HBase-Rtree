package RTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import Lib.Node;
import Lib.Rectangle;
import Lib.DataItem;
import RTree.LeafNode;
import RTree.NonLeafNode;
import RTree.RTree;

public class RTree {
       public Node root;
       List<LeafNode> listLeaf = new ArrayList<LeafNode>();
  	   public static final int MAX = 100;
       public static final int MIN = 50;
       
       //to initialize R tree random in my data set
       public RTree () {
    	   this.root = new LeafNode();
    	   //RTree tree = new RTree();
      	   for (int i = 0; i < 100000; i++) {
     	    	this.insertData(new DataItem(i, 
     	    			new Rectangle(i, i + 1, i + 1, i + 2), "row"+i));
     	     }
      	   //System.out.println(1);
      	 
       }
       
       public RTree (String Name) {
    	   this.root = new LeafNode();
       }
       
       //to build R tree in any data set.
       public void RTreeTable (String Name, ArrayList<DataItem>attrNames, ArrayList<String>idxAttrbutes) 
    		   throws IOException {    	   
    	   Configuration conf = HBaseConfiguration.create();
     	   Connection connection = ConnectionFactory.createConnection(conf);
     	   HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
     	   //HBaseAdmin admin = new HBaseAdmin(conf);
     	   TableName tablename = TableName.valueOf(Name);
     	   HTableDescriptor tableDescriptor = new HTableDescriptor(
     	    		                           TableName.valueOf(Name));
     	   tableDescriptor.addFamily(new HColumnDescriptor("fam1"));
     	   tableDescriptor.addFamily(new HColumnDescriptor("fam2"));
     	   admin.createTable(tableDescriptor);
     	   Table table = connection.getTable(tablename);
     	   for (int i = 0; i < attrNames.size(); i++) {
     		   //System.out.println(attrNames.get(i));
     		   this.insertData(attrNames.get(i));
     		   //System.out.println(idxAttrbutes.get(i));
     	   }
     	   
     	   for (int i = 0; i < idxAttrbutes.size() - 1; i++) {
     		   byte[] row = Bytes.toBytes(idxAttrbutes.get(i)); 
 	           byte[] family = Bytes.toBytes("fam1");
 	           //System.out.println(idxAttrbutes.get(i));
 	           byte[] qualifier = Bytes.toBytes("qual2");
  	           byte[] value = Bytes.toBytes(idxAttrbutes.get(i + 1));
  	           if (idxAttrbutes.get(i).contains("row")) {
 	               table.put(new Put(row).addColumn(family, qualifier, value));
  	           }
     	   }
       }
       
       public DataItem insertData (DataItem data) {
    	   //System.out.println(data.getMbr());
    	   LeafNode leaf = chooseLeaf ((Node)root, data.getMbr());
    	   if (!leaf.isFull()) {
 	    	  leaf.insertData(data);
 	    	  adjustTree(null, leaf);
 	    	  return data;
 	      }
 		  LeafNode newleaf = leaf.split(data);
 	      adjustTree (newleaf, leaf);
    	  //System.out.println(root.getChildArray()[0].getMbr().overlap(new Rectangle(10,11,10,11)));
    	  return data;
       }
       
       public LeafNode chooseLeaf (Node start, Rectangle mbr) {
    	   if (start.isLeaf()) {
    		   return (LeafNode)start;
    	   }
    	   
    	   int bestchoice = 0;
    	   //traverse the elements in the start node
    	   int areaCurr = start.getMbrArray()[bestchoice].getArea();
    	   Rectangle tmp = start.getMbrArray()[bestchoice];
    	   int areaInc = Rectangle.merge(tmp, mbr).getArea() - areaCurr;
    	   
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
		    		//areaInc = tmpAreaInc;
		    		if (areaCurr > tmpArea) {
 	    			  areaInc = tmpAreaInc;
 	    			  bestchoice = i;
 	    			  areaCurr = tmpArea;
 	    		  }
		    	}
		    }   
		    return chooseLeaf (((NonLeafNode)start).getChildArray()[bestchoice], mbr);
       }
       
       public void adjustTree (Node newNode, Node oldNode) {
    	     if (oldNode.isRoot()) {
 		       if (newNode == null) {
 		    	   return;
 		       }else { //we have splitted a new node
 		    	  NonLeafNode newroot = new NonLeafNode();
 		    	  newroot.setLevel(oldNode.getLevel() + 1);
 		    	  newroot.insertNode(oldNode);
 		    	  newroot.insertNode(newNode);
 		    	  oldNode.setParent(newroot);
 		    	  newNode.setParent(newroot);
 		    	  root = newroot;
 		    	  return;
 		       }
 		    }
    	    NonLeafNode oldparent = (NonLeafNode) oldNode.getParent();
//    	    Node En = oldparent.getChildArray()[oldNode.childindex];
//		    for (int i = 0; i < En.getNum(); i++) {
//		    	
//		       if (!oldNode.isLeaf()) {
//		    	   //System.out.println(oldNode.getMbrArray()[i]);
//		    	   En.mbr = Rectangle.merge(En.getMbrArray()[i], oldNode.getMbrArray()[i]);
//		       }else {
//		    	   En.mbr = Rectangle.merge(En.getMbrArray()[i], oldNode.getMbr());
//		       }
//		    }
//		    oldNode.mbr = En.getMbr();
		    oldparent.getMbrArray()[oldNode.childindex] = oldNode.getMbr();
		   // oldparent.setMbr(En.getMbr());
		    //System.out.println(oldNode.getMbr().getxMax());
		    
		    if (newNode == null) {
		    	//System.out.println(oldparent);
		    	adjustTree(null, oldparent);
		    }else {
		    	if (oldparent.isFull()) {
		    		NonLeafNode newoldparent = oldparent.split(newNode);
		    		adjustTree(newoldparent, oldparent);
		    	}else {
		    		oldparent.insertNode(newNode);
		    		newNode.setParent(oldparent);
		    		adjustTree(null, oldparent);
		    	}
		    }
       }
       
       public Node insertNode (Node node) {
    	      NonLeafNode nodeToInsert = chooseNode(root, node.getMbr());
    	      if (!node.isFull()) {
    	    	  node.insertNode(nodeToInsert);
    	    	  adjustTree(null, node);
    	    	  return nodeToInsert;
    	      }
    	      
    	      //if the leaf is overflow after we insert, we should adjustTree
    	      NonLeafNode newnode = (NonLeafNode) node.split(nodeToInsert);
    	      adjustTree (newnode, node);
    	      return nodeToInsert;
       }
       
       public NonLeafNode chooseNode (Node start, Rectangle mbr) {
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
	        return chooseNode (((NonLeafNode)start).getChildArray()[bestchoice], mbr);
       }
       
       public Rectangle deleteData (Rectangle mbr) {
 	      //LeafNode leafToDelete = new LeafNode();
 	      //System.out.println(root);
 	      LeafNode leafToDelete = FindLeaf(root, mbr);
 	      //System.out.println(leafToDelete);
 	      if (deleteFromLeaf(leafToDelete, mbr)) {
 	    	  this.condenseTree(leafToDelete); //to check whether there is enough elements after deletion
 	    	  if (root.getChildArray() != null) {
 		    	  if (root.getChildArray().length == 1) {
 		    		  root = root.getChildArray()[0];
 		    		  return mbr;
 		    	  }
 	    	  }
 	    	  //System.out.println(1);
 	    	  return mbr;
 	      }
 	      
 	      return null;
      } 
       
       public DataItem RemoveDataItem (Rectangle mbr) {
    	  boolean flag = false;
    	  DataItem removeData = new DataItem();
  	      LeafNode leafToDelete = FindLeaf(root, mbr);
  	      //System.out.println(1);
  	      if (leafToDelete != null) {
  	    	  flag = true;
  	    	  //System.out.println(1);
  	      }
  	      if (flag) {
  	          removeData = (DataItem) this.Query(mbr).get(0);
  	          //System.out.println(1);
  	          //return removeData;
  	      }
  	      //System.out.println(removeData);
  	      if (deleteFromLeaf(leafToDelete, mbr)) {
  	    	  //DataItem result = (DataItem) this.Query(mbr);
  	    	  this.condenseTree(leafToDelete); //to check whether there is enough elements after deletion
  	    	  if (root.getChildArray() != null) {
  		    	  if (root.getChildArray().length == 1) {
  		    		  root = root.getChildArray()[0];
  		    		  return removeData;
  		    	  }
  	    	  }
  	    	  return removeData;
  	      }
  	      
  	      return null;
       } 
       
       public DataItem RetriveDataItem (Rectangle mbr) {
    	      return this.Query(mbr).get(0);
       }
       
       public LeafNode FindLeaf (Node start, Rectangle mbr) {
 	      if (start.isLeaf()) {
 	    	  for (int i = 0; i < start.getNum(); i++) {
 	    		 //System.out.println(start.getMbr().overlap(mbr));
 	    		  if (start.getMbrArray()[i].contain(mbr)   
 	    				  || start.getMbrArray()[i].overlap(mbr)
 	    				  || start.getMbrArray()[i] == mbr
 	    				  || start.getMbr().contain(mbr)
 	    				  || start.getMbr().overlap(mbr)
 	    				  || start.getMbr() == mbr) {
 	    			  //System.out.println(start == null); 
 	    			 // System.out.println(1);
 	    			  return (LeafNode)start;
 	    		  }
 	    	  }
 	      } //search from root to leaf
 	      
 	    
 	      for (int i = 0 ; i < start.getNum(); i++) {
 	    	      if (start.getMbrArray()[i].overlap(mbr)
 	    	    		  || start.getMbrArray()[i] == mbr){
 	    			  return FindLeaf(((NonLeafNode)start).getChildArray()[i], mbr);
 	    		  }else if (start.getMbrArray()[i].contain(mbr)
 	    				  || start.getMbrArray()[i] == mbr) {
 	    			  return FindLeaf(((NonLeafNode)start).getChildArray()[i], mbr);
 	    		  }
 	       }
 	      //System.out.println(1); 
 	      return null;
      }
       
      public boolean deleteFromLeaf (LeafNode leaf, Rectangle mbr) {
   	    for (int i = 0; i < leaf.getNum(); i++) {
//   	    	if (leaf.getMbrArray()[i].contain(mbr) || 
//	    		    leaf.getMbrArray()[i].overlap(mbr) || 
//	    		    leaf.getMbrArray()[i] == mbr) {
   	    		leaf.getDataArray()[i] = null;
   	    		leaf.getMbrArray()[i] = null;
   	    		//leaf.getKeyArray()[i] = (Integer) null;
   	    		leaf.getKeyArray()[i] = -1;
   	    		leaf.num--;
   	    		return true;
//   	    	}

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
   	    		//TO-DO
   	    	}
   	    	return;
   	    }
   	    //System.out.println(1);
   	    if (LeafToDelete.getNum() < MIN) {
   	    	((NonLeafNode)parent).setOneChild(((NonLeafNode)parent).getIndexChild(LeafToDelete), null);
   	        for (int i = 0; i < LeafToDelete.getNum(); i++) {
   	        	if (LeafToDelete.getDataArray() != null) {
       	    		storedelete.add(LeafToDelete.getDataArray()[i]);
       	    	}else {
       	    		storedeleteNode.add(((NonLeafNode)LeafToDelete).getChildArray()[i]);
       	    	}
   	        }
   	        condenseTree(parent);
   	    }else {
   	    	condenseTree(parent);
   	    	
   	    }    
     }
      

      public List<DataItem> Query (Rectangle mbr) {
     	    List<DataItem> queryResult = new ArrayList<DataItem>();
     	    //System.out.println(mbr);
     	    if (root.getMbr().contain(mbr)) {
     	      for (int i = 0; i < root.getNum(); i++) {
     	    	if (root.getChildArray() == null) {
     	    		overlapsearchhelp(root, mbr, queryResult);
     	    	}else{
     	    	  if (root.getChildArray()[i].getMbr().overlap(mbr)) {
     	             overlapsearchhelp(root.getChildArray()[i], mbr, queryResult);
     	          }else if (root.getChildArray()[i].getMbr().contain(mbr)) {
     	        	 containsearchhelp(root.getChildArray()[i], mbr, queryResult);
     	          }
     	    	}
     	      }
     	    }
     	    return queryResult;
      }
      
      public void overlapsearchhelp (Node start, Rectangle mbr, List<DataItem> list) {
    	  if (start.isLeaf()) {
	  	    	for (int i = 0; i < start.getNum(); i++) {
	  	    		if (start.getMbrArray()[i].contain(mbr)
	  	    				|| start.getMbrArray()[i] == mbr) {
	  	    			list.add(start.getDataArray()[i]);
	  	    		}else if (start.getMbrArray()[i].overlap(mbr)) {
	  	    			if (start.getDataArray()[i].getMbr() == mbr) {
	  	    				list.add(start.getDataArray()[i]);
	  	    			}else {
	  	    				overlapsearchhelp(((NonLeafNode)start.parent).getChildArray()[i + 1], mbr, list);
	  	    			}
	  	    		}
	  	    	  }
	  	    	  return;
    	 }
    	  
      	 for (int i = 0; i < start.getNum(); i++) {
		    //System.out.println(start.getMbr().overlap(mbr));
	    	if (start.getMbrArray()[i].contain(mbr)) {
	    		overlapsearchhelp(((NonLeafNode)start).getChildArray()[i], mbr, list);
	    	}else if (start.getMbrArray()[i].overlap(mbr)) {
	    		overlapsearchhelp(((NonLeafNode)start).getChildArray()[i], mbr, list);
	    	}
	    }
      }
      
      public void containsearchhelp (Node start, Rectangle mbr, List<DataItem> list) {
    	  if (start.isLeaf()) {
	  	    	for (int i = 0; i < start.getNum(); i++) {
	  	    		if (start.getMbrArray()[i].contain(mbr)
	  	    				|| start.getMbrArray()[i] == mbr
	  	    				|| start.getMbr().contain(mbr)
	  	    				|| start.getMbr() == mbr) {
	  	    			list.add(start.getDataArray()[i]);
	  	    		}else if (start.getMbrArray()[i].overlap(mbr)
	  	    				  || start.getMbr().overlap(mbr)
	  	    				  || start.getMbr() == mbr) {
	  	    			if (start.getDataArray()[i].getMbr() == mbr
	  	    					|| start.getMbr() == mbr) {
	  	    				list.add(start.getDataArray()[i]);
	  	    			}else {
	  	    				containsearchhelp(((NonLeafNode)start.parent).getChildArray()[i + 1], mbr, list);
	  	    			}
	  	    		}
	  	    	  }
	  	    	  return;
    	 }
    	  
      	 for (int i = 0; i < start.getNum(); i++) {
		    //System.out.println(start.getMbr().overlap(mbr));
	    	if (start.getMbrArray()[i].contain(mbr)) {
	    		containsearchhelp(((NonLeafNode)start).getChildArray()[i], mbr, list);
	    	}else if (start.getMbrArray()[i].overlap(mbr)) {
	    		containsearchhelp(((NonLeafNode)start).getChildArray()[i], mbr, list);
	    	}
	    }
      }
//      public void searchhelp (Node start, Rectangle mbr, List<DataItem> list) {
//     	    //System.out.println(start.getNum());
//     	
//     	 if (start.isLeaf()) {
//     		//System.out.println(start);
//  	    	for (int i = 0; i < start.getNum(); i++) {
//  	    		if (start.getMbrArray()[i].contain(mbr)
//  	    				|| start.getMbrArray()[i] == mbr) {
//  	    			list.add(start.getDataArray()[i]);
//  	    		}else if (start.getMbrArray()[i].overlap(mbr)) {
//  	    			if (start.getDataArray()[i].getMbr() == mbr) {
//  	    				list.add(start.getDataArray()[i]);
//  	    			}else {
//  	    				searchhelp(((NonLeafNode)start.parent).getChildArray()[i + 1], mbr, list);
//  	    			}
//  	    		}
//  	    	  }
//  	    	  return;
//     	 }
//     	
//     	 for (int i = 0; i < start.getNum(); i++) {
//     		    //System.out.println(start.getMbr().overlap(mbr));
//     	    	if (start.getMbrArray()[i].contain(mbr)) {
//     	    		searchhelp(((NonLeafNode)start).getChildArray()[i], mbr, list);
//     	    	}else if (start.getMbrArray()[i].overlap(mbr)) {
//     	    		searchhelp(((NonLeafNode)start).getChildArray()[i], mbr, list);
//     	    	}
//     	    }
//     	
//     }
//      public void searchhelp (Node start, Rectangle mbr, List<DataItem> list) {
//	   	    //System.out.println(start.getNum());
//	   	
//	   	 if (start.isLeaf()) {
//	   		//System.out.println(start);
//		    	for (int i = 0; i < start.getNum(); i++) {
//		    		if (start.getMbrArray()[i].contain(mbr)
//		    				|| start.getMbrArray()[i] == mbr) {
//		    			list.add(start.getDataArray()[i]);
//		    		}else if (start.getMbrArray()[i].overlap(mbr)) {
//		    			if (start.getDataArray()[i].getMbr() == mbr) {
//		    				list.add(start.getDataArray()[i]);
//		    			}
////		    			}else {
////		    				searchhelp(((NonLeafNode)start.parent).getChildArray()[i + 1], mbr, list);
////		    			}
//		    		}
//		    	  }
//		    	  return;
//	   	 }
//	   	
//	   	 for (int i = 0; i < start.getNum(); i++) {
//	   		    //System.out.println(start.getMbr().overlap(mbr));
//	   	    	if (start.getMbrArray()[i].contain(mbr)) {
//	   	    		searchhelp(((NonLeafNode)start).getChildArray()[i], mbr, list);
//	   	    	}else if (start.getMbrArray()[i].overlap(mbr)) {
//	   	    		searchhelp(((NonLeafNode)start).getChildArray()[i], mbr, list);
//	   	    	}
//	   	    }
//	   	
//	   }
      
   
    
    
      
//     public static void main (String[] args) throws IOException {
//    	long starttime = System.currentTimeMillis();
//  	    RTree tree = new RTree();
////  	    
//  	    Configuration conf = HBaseConfiguration.create();
//  	    Connection connection = ConnectionFactory.createConnection(conf);
//  	    HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
//  	    TableName tablename = TableName.valueOf("testquery");
//  	    HTableDescriptor tableDescriptor = new HTableDescriptor(
//  	    		                           TableName.valueOf("testquery"
//  	    		                           	));
//  	    tableDescriptor.addFamily(new HColumnDescriptor("fam1"));
//  	    tableDescriptor.addFamily(new HColumnDescriptor("fam2"));
//  	    //admin.createTable(tableDescriptor);
//  	    Table table = connection.getTable(tablename);
////  	    for (int i = 0; i < 1000; i++) {
////  	    	tree.insertData(new DataItem(i, new Rectangle(i, i + 1, i + 1, i + 2), "row"+i));
//////  	    	byte[] row = Bytes.toBytes("row" + i); 
//////	        byte[] family = Bytes.toBytes("fam1");
//////	        byte[] qualifier = Bytes.toBytes("qual2");
//////	        byte[] value = Bytes.toBytes("value" + i);
//////	        table.put(new Put(row).addColumn(family, qualifier, value));
////  	    }
////  	
//  	    long medtime = System.currentTimeMillis();
//  	    //System.out.println(medtime - starttime);
//  	    DataItem data = tree.RetriveDataItem(new Rectangle(198,199,199,200));
//  	    
//	  	byte[] row = Bytes.toBytes(data.rowindex);
//		byte[] family = Bytes.toBytes("fam1");
//		byte[] qualifier = Bytes.toBytes("qual2");
//	    Delete delete = new Delete(row);
//	    delete.addColumn(family, qualifier);
//	    table.delete(delete);
//  	    //tree.RemoveDataItem(new Rectangle(10000,10001,10000,10001));
//  	    //DataItem t = removeDataItem(new DataItem());
//  	    ArrayList<Integer> lower = new ArrayList<Integer>();
//  	    ArrayList<Integer> upper = new ArrayList<Integer>();
//  	    for (int i = 10; i < 1000; i++) {
//  	    	lower.add(i);
//  	    	upper.add(i + 1);
//  	    }
////  	    lower.add(99);
////  	    lower.add(99);
////  	    upper.add(100);
////  	    upper.add(100);
//  	    List<DataItem> result = new ArrayList<DataItem>();
//  	    //result = tree.Query(new Rectangle(10002,10003,10002,10003));
//  	    //tree.deleteData(new Rectangle(10000,10001,10000,10001));
//  	    
//  	    for (int i = 0; i < upper.size() - 1; i++) {
//  	    	result = tree.Query(new Rectangle(lower.get(i), lower.get(i + 1),
//  	    			                          upper.get(i), upper.get(i + 1)));
//  	    }
//  	    
//         //System.out.println(result.get(0).rowindex);
//  	     System.out.println(result);
////	  	 Get get = new Get(Bytes.toBytes(result.get(0).rowindex));
////	 	 get.addFamily(Bytes.toBytes("fam1"));
////	 	 get.addColumn(Bytes.toBytes("fam1"), Bytes.toBytes("qual2"));
////	     Result result2 = table.get(get);
////	     String row = Bytes.toString(result2.getRow());
////	     String specificValue = Bytes.toString(
////	    		                result2.getValue(Bytes.toBytes("fam1"), 
////	    		                Bytes.toBytes("qual2")));
////	     System.out.println(specificValue);
//  	    //System.out.println(result.get(0).rowindex);
////	  
//  	    long endtime = System.currentTimeMillis();
//  	    System.out.println(endtime - medtime);
//       
//  	 
//     }
    

       
}
