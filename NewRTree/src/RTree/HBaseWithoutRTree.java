package RTree;

import Lib.Rectangle;
import Lib.Node;
import Lib.DataItem;
import Lib.Data;
import RTree.RTree;
import RTree.LeafNode;
import RTree.NonLeafNode;

import java.io.IOException;
import java.util.ArrayList;

import javax.print.DocFlavor.STRING;

import org.apache.commons.collections.bag.TreeBag;
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
//import org.jruby.org.objectweb.asm.tree.IntInsnNode;

import org.apache.htrace.fasterxml.jackson.annotation.JsonFormat.Value;
import org.jruby.org.objectweb.asm.tree.IntInsnNode;

import Lib.DataItem;

public class HBaseWithoutRTree {
//       public static void main (String[] args) throws IOException {
//    	   long starttime = System.currentTimeMillis();
//    	   HBaseWithoutRTree test = new HBaseWithoutRTree();
//    	   Configuration conf = HBaseConfiguration.create();
//     	   Connection connection = ConnectionFactory.createConnection(conf);
//     	   TableName tablename = TableName.valueOf("testquery");
// 	       HTableDescriptor tableDescriptor = new HTableDescriptor(
//           TableName.valueOf("testquery"));
//           tableDescriptor.addFamily(new HColumnDescriptor("fam1"));
//           tableDescriptor.addFamily(new HColumnDescriptor("fam2"));
//           HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
//           Table table = connection.getTable(tablename);
//           //admin.createTable(tableDescriptor);
//           ArrayList<DataItem> attr = new ArrayList<DataItem>();
//           ArrayList<String> id = new ArrayList<String>();
//           for (int i = 0; i < 10; i++) {
//               id.add("row" + i);
//               id.add("value" + i);
//           }
//           RTree tree = new RTree();
//           for (int i = 0; i < 10; i++) {
//        	   attr.add(new DataItem(i, 
//    	    			new Rectangle(i, i + 1, i, i + 1), "row"+i));
//        	  // System.out.println(attr);
//           }
//          
//          // test.RTreeTable("testinterface", attr, id);
////           test.insertData(new DataItem(0, 
////    	    			new Rectangle(0, 1, 0, 1), "row0"), new Data(0, 
////    	    	    			new Rectangle(0, 1, 0, 1), "row0", "value0"), "testinterface", tree);
//           System.out.println(test.RetriveDataItem(new Rectangle(0,1,0,1)).rowindex);
//           //test.containmentQuery(lower, upper, "testquery");
//           //Table table = connection.getTable(tablename);
////           for (int i = 0; i < 1000000; i++) {
////        		   byte[] row = Bytes.toBytes("row" + i); 
////       	           byte[] family = Bytes.toBytes("fam1");
////       	           byte[] qualifier = Bytes.toBytes("qual2");
////        	       byte[] value = Bytes.toBytes("value" + i);
////        	       table.put(new Put(row).addColumn(family, qualifier, value));
////        	       
////           }
////           byte[] family = Bytes.toBytes("fam1");
////   		   byte[] qualifier = Bytes.toBytes("qual2");
////           for (int i = 0; i < 1000000; i++) {
////        	   
////        	   Get get = new Get(Bytes.toBytes("row" + i));
////   	 	       get.addFamily(Bytes.toBytes("fam1"));
////   	 	       get.addColumn(Bytes.toBytes("fam1"), Bytes.toBytes("qual2"));
////   	           Result result2 = table.get(get);
////               String row = Bytes.toString(result2.getRow());
////   	           String specificValue = Bytes.toString(
////   	    		                result2.getValue(Bytes.toBytes("fam1"), 
////   	    		                Bytes.toBytes("qual2")));
////   	          // System.out.println(specificValue);
////        	  if (specificValue != null) {
////   	           if (specificValue.equals("value1000")) {
////        		   System.out.println(specificValue);
////       	           Delete delete = new Delete(Bytes.toBytes("row" + i));
////    		       delete.addColumn(family, qualifier);
////    		       table.delete(delete);
////        	   }
////        	  }
////           }
//     	   long endtime = System.currentTimeMillis();
//     	   System.out.println(endtime - starttime);
//       }
       
	   //to build RTree and insert data into HBase by using the RTreeTable() in RTree.java
       public String RTreeTable (String Name, ArrayList<DataItem> attrNames, ArrayList<String> idAttrs) throws IOException {
    	     RTree tree = new RTree(Name);
    	     tree.RTreeTable(Name, attrNames, idAttrs);
    	     return Name;
       }
       
       //should first new an R tree
       public String overlapQuery (ArrayList<Integer>lower, ArrayList<Integer>upper, String Name) throws IOException {
    	      RTree tree = new RTree();
    	      String index = null;
    	      ArrayList<DataItem> result = new ArrayList<DataItem>();
    	      //index = tree.Query(mbr).get(0).rowindex;
    	      Configuration conf = HBaseConfiguration.create();
        	  Connection connection = ConnectionFactory.createConnection(conf);
        	  TableName tablename = TableName.valueOf(Name);
    	      HTableDescriptor tableDescriptor = new HTableDescriptor(
              TableName.valueOf(Name));
              tableDescriptor.addFamily(new HColumnDescriptor("fam1"));
              tableDescriptor.addFamily(new HColumnDescriptor("fam2"));
              HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
              Table table = connection.getTable(tablename);
              for (int i = 0; i < lower.size() - 1; i++) {
            	  result = (ArrayList<DataItem>) tree.Query(new Rectangle(lower.get(i), lower.get(i + 1),
                          upper.get(i), upper.get(i + 1)));
            	     if (result.size() != 0) {
	            	  index = result.get(0).rowindex;
	                  Get get = new Get(Bytes.toBytes(index));
	      	 	      get.addFamily(Bytes.toBytes("fam1"));
	      	 	      get.addColumn(Bytes.toBytes("fam1"), Bytes.toBytes("qual2"));
	      	          Result result2 = table.get(get);
	                  String row = Bytes.toString(result2.getRow());
	      	          String specificValue = Bytes.toString(
	      	    		                result2.getValue(Bytes.toBytes("fam1"), 
	      	    		                Bytes.toBytes("qual2")));
	      	          System.out.println(specificValue);
            	     }
              }
      	      return "done";
    	      
       }
       
       //should first new an R tree
       public String containmentQuery (ArrayList<Integer>lower, ArrayList<Integer>upper, String Name) throws IOException {
    	   RTree tree = new RTree();
 	       String index = null;
 	       //index = tree.Query(mbr).get(0).rowindex;
 	       ArrayList<DataItem> result = new ArrayList<DataItem>();
 	       Configuration conf = HBaseConfiguration.create();
     	   Connection connection = ConnectionFactory.createConnection(conf);
     	   TableName tablename = TableName.valueOf(Name);
 	       HTableDescriptor tableDescriptor = new HTableDescriptor(
           TableName.valueOf(Name));
           tableDescriptor.addFamily(new HColumnDescriptor("fam1"));
           tableDescriptor.addFamily(new HColumnDescriptor("fam2"));
           HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
           Table table = connection.getTable(tablename);
           for (int i = 0; i < lower.size() - 1; i++) {
         	  result = (ArrayList<DataItem>) tree.Query(new Rectangle(lower.get(i), lower.get(i + 1),
                       upper.get(i), upper.get(i + 1)));
         	     if (result.size() != 0) {
	            	  index = result.get(0).rowindex;
	                  Get get = new Get(Bytes.toBytes(index));
	      	 	      get.addFamily(Bytes.toBytes("fam1"));
	      	 	      get.addColumn(Bytes.toBytes("fam1"), Bytes.toBytes("qual2"));
	      	          Result result2 = table.get(get);
	                  String row = Bytes.toString(result2.getRow());
	      	          String specificValue = Bytes.toString(
	      	    		                result2.getValue(Bytes.toBytes("fam1"), 
	      	    		                Bytes.toBytes("qual2")));
	      	          System.out.println(specificValue);
         	     }
           }
   	      return "done";
       }
       
       //should first new an R tree;
       public DataItem RetriveDataItem (Rectangle mbr) {
    	      RTree tree = new RTree(); //this is to initialize R tree 
    	                                //if you would like a more specific object
    	                                //should use RTreeTable();
    	      DataItem result = tree.RetriveDataItem(mbr);
    	      return result;
       }
       
       //this is to insert into HBase without building R tree
       public void insertHbase (String Name) throws IOException {
    	   Configuration conf = HBaseConfiguration.create();
     	   Connection connection = ConnectionFactory.createConnection(conf);
     	   HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
     	   TableName tablename = TableName.valueOf(Name);
     	   HTableDescriptor tableDescriptor = new HTableDescriptor(
                  TableName.valueOf(Name));
		   tableDescriptor.addFamily(new HColumnDescriptor("fam1"));
		   tableDescriptor.addFamily(new HColumnDescriptor("fam2"));
		   admin.createTable(tableDescriptor);
     	   Table table = connection.getTable(tablename);
    	   for (int i = 0; i < 1000; i++) {
    		   byte[] row = Bytes.toBytes("row" + i); 
   	           byte[] family = Bytes.toBytes("fam1");
   	           byte[] qualifier = Bytes.toBytes("qual2");
    	       byte[] value = Bytes.toBytes("value" + i);
   	           table.put(new Put(row).addColumn(family, qualifier, value));
    	   }
       }
       
       //for this method, should input an RTree object first
       public Data insertData (DataItem dataitem, Data data, String Name, RTree tree) throws IOException {
    	   
    	   Configuration conf = HBaseConfiguration.create();
    	   Connection connection = ConnectionFactory.createConnection(conf);
    	   HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
    	      //HBaseAdmin admin = new HBaseAdmin(conf);
    	   TableName tablename = TableName.valueOf(Name);
    	   HTableDescriptor tableDescriptor = new HTableDescriptor(
    	    		                           TableName.valueOf(Name));
    	   tableDescriptor.addFamily(new HColumnDescriptor("fam1"));
    	   tableDescriptor.addFamily(new HColumnDescriptor("fam2"));
    	   LeafNode leaf = tree.chooseLeaf ((Node)tree.root, data.getMbr());
    	   Table table = connection.getTable(tablename);
    	   if (!leaf.isFull()) {
 	    	  leaf.insertData(dataitem);
 	    	  tree.adjustTree(null, leaf);
 	    	  byte[] row = Bytes.toBytes(data.rowindex); 
 	          byte[] family = Bytes.toBytes("fam1");
 	          byte[] qualifier = Bytes.toBytes("qual2");
 	          byte[] value = Bytes.toBytes(data.value);
 	          table.put(new Put(row).addColumn(family, qualifier, value));
 	    	  return data;
 	      }
 		  LeafNode newleaf = leaf.split(dataitem);
 	      tree.adjustTree (newleaf, leaf);
   	      //admin.createTable(tableDescriptor);
 	      byte[] row = Bytes.toBytes(data.rowindex); 
          byte[] family = Bytes.toBytes("fam1");
          byte[] qualifier = Bytes.toBytes("qual2");
          byte[] value = Bytes.toBytes(data.value);
          table.put(new Put(row).addColumn(family, qualifier, value));
    	  //System.out.println(root.getChildArray()[0].getMbr().overlap(new Rectangle(10,11,10,11)));
    	  return data;
       }
       
       //also this method should have an R tree object first
       public DataItem RemoveDataItem (Rectangle mbr, String Name, RTree tree) throws IOException {
    	  Configuration conf = HBaseConfiguration.create();
    	  Connection connection = ConnectionFactory.createConnection(conf);
    	  HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
    	      //HBaseAdmin admin = new HBaseAdmin(conf);
    	  TableName tablename = TableName.valueOf(Name);
    	  HTableDescriptor tableDescriptor = new HTableDescriptor(
    	    		                           TableName.valueOf(Name));
    	  tableDescriptor.addFamily(new HColumnDescriptor("fam1"));
    	  tableDescriptor.addFamily(new HColumnDescriptor("fam2"));
    	  Table table = connection.getTable(tablename);
    	  boolean flag = false;
     	  DataItem removeData = new DataItem();
   	      LeafNode leafToDelete = tree.FindLeaf(tree.root, mbr);
   	      //System.out.println(1);
   	      if (leafToDelete != null) {
   	    	  flag = true;
   	      }
   	      if (flag) {
   	          removeData = (DataItem) tree.Query(mbr).get(0);
   	          byte[] row = Bytes.toBytes(removeData.rowindex);
			  byte[] family = Bytes.toBytes("fam1");
			  byte[] qualifier = Bytes.toBytes("qual2");
  	          Delete delete = new Delete(row);
		      delete.addColumn(family, qualifier);
		      table.delete(delete);
   	      }
   	      //System.out.println(removeData);
   	      if (tree.deleteFromLeaf(leafToDelete, mbr)) {
   	    	  tree.condenseTree(leafToDelete); //to check whether there is enough elements after deletion
   	    	  if (tree.root.getChildArray() != null) {
   		    	  if (tree.root.getChildArray().length == 1) {
   		    		  tree.root = tree.root.getChildArray()[0];
   		    		  return removeData;
   		    	  }
   	    	  }
   	    	  return removeData;
   	      }
   	      
   	      return null;
        } 
       
       
       
      
}
