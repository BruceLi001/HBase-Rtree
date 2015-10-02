package RTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.print.DocFlavor.STRING;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.DoubleWritable;

import Lib.DataItem;
import Lib.Rectangle;

public class InsertHbase {
	   public InsertHbase (String Name) throws IOException {
		    ArrayList<DataItem> attr = new ArrayList<DataItem>();
		    ArrayList<String> id = new ArrayList<String>();
		    ArrayList<Double> rate_array = new ArrayList<Double>();
		    ArrayList<String> cuisine_array = new ArrayList<String>();
		    
		    Configuration conf = HBaseConfiguration.create();
			Connection connection = ConnectionFactory.createConnection(conf);
			TableName tablename = TableName.valueOf(Name);
		    HTableDescriptor tableDescriptor = new HTableDescriptor(
		    TableName.valueOf(Name));
		    tableDescriptor.addFamily(new HColumnDescriptor("fam1"));
		    tableDescriptor.addFamily(new HColumnDescriptor("fam2"));
		    HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
		    Table table = connection.getTable(tablename);
		    admin.createTable(tableDescriptor);
		    //Table table = connection.getTable(tablename);
//	           for (int i = 0; i < 1000000; i++) {
//	        		   byte[] row = Bytes.toBytes("row" + i); 
//	       	           byte[] family = Bytes.toBytes("fam1");
//	       	           byte[] qualifier = Bytes.toBytes("qual2");
//	        	       byte[] value = Bytes.toBytes("value" + i);
//	        	       table.put(new Put(row).addColumn(family, qualifier, value));
//	        	       
//	           }
		      Random random = new Random();
	    	  Random ran = new Random();
	    	  Random rand = new Random();
	    	  
	    	  rate_array.add(0.0);
	    	  rate_array.add(0.5);
	    	  rate_array.add(1.0);
	    	  rate_array.add(1.5);
	    	  rate_array.add(2.0);
	    	  rate_array.add(2.5);
	    	  rate_array.add(3.0);
	    	  rate_array.add(3.5);
	    	  rate_array.add(4.0);
	    	  rate_array.add(4.5);
	    	  rate_array.add(5.0);
	      	  
	    	  cuisine_array.add("Susi");
	      	  cuisine_array.add("Chinese");
	      	  cuisine_array.add("American");
	      	  cuisine_array.add("Italian");
	      	  cuisine_array.add("Spanish");
	      	  cuisine_array.add("Mexico");
	      	  cuisine_array.add("Barbecue");
	      	  cuisine_array.add("Mediterranean");
	      	  cuisine_array.add("French");
	      	  cuisine_array.add("Seafood");
	      	  cuisine_array.add("Indian");
	      	  cuisine_array.add("Steak");
	      	  cuisine_array.add("Belgian");
	      	  cuisine_array.add("Russia");
	      	  cuisine_array.add("Brazilian");
	      	  cuisine_array.add("German");
		    
		    for (int i = 0; i < 10000; i++) {
		    	double a = new Double((double) i);
		    	DataItem object = new DataItem(i, 
	      				  new Rectangle(-(77.092 + a / 100000), 38.895 + a / 100000, 
	      						  -(77.098 + a / 100000), 38.895 + a / 100000),
	      				  random.nextInt(310), rate_array.get(ran.nextInt(11)), 
	      				  cuisine_array.get(rand.nextInt(16)),-(77.092 + a / 100000), 
	      				  38.895 + a / 100000,
	      				  "row" + i, "Resturant" + i, i + "Street");
		    	attr.add(object);
		    	id.add("row" + i);
		    	id.add(object.restaurantAddr + " " + object.restaurantName
		    			+ " " + 
		    			object.cuisine +  " " + object.price + " " 
		    			+ object.rate);
		    }
		    
		    for (int i = 0; i < id.size() - 1; i++) {
	      		  double a = new Double((double) i);
	      		  byte[] row = Bytes.toBytes(id.get(i));
	      		  byte[] family = Bytes.toBytes("fam1");
     	          byte[] qualifier = Bytes.toBytes("qual2");
     	          byte[] value = Bytes.toBytes(id.get(i + 1));
     	          
     	          if (id.get(i).contains("row")) {
     	          	table.put(new Put(row).addColumn(family, qualifier, value
     	        		  ));
     	          }
	      	  }
	   }
	   
	   public static void main (String[] args) throws IOException {
		   InsertHbase test = new InsertHbase("testGUI");
	   }
}
