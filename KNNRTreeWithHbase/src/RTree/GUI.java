package RTree;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import Lib.DataItem;
import Lib.Rectangle;



public class GUI extends JFrame implements ActionListener{
	private static JButton button;
	private JPanel panel;
	public final String DBName = "testGUI"; //to use this database name
	public JComboBox comboBox_cuisine;
	public JComboBox comboBox_dist;
	public JComboBox comboBox_price; 
	public JComboBox comboBox_rate; 
	public JComboBox comboBox_dist_range;
	public JComboBox comboBox_price_range;
	public JComboBox comboBox_rate_range; 
	public JTextField txt = new JTextField();
	public JTextField txtDB = new JTextField();
	//public JTextArea resultArea = new JTextArea(1300,100);
	public JTextArea resultArea = new JTextArea(100,100);;
	
	public Dialog d;
	public Label lab;
	public Button okBut;
	
	JScrollPane jsp;
	
	//build RTree before initializing the GUI
	RTree tree = new RTree("Tree");
   
	public void initFrame(String message,int x,int y,int z,int g) throws IOException{
		//Frame f = new Frame(message);
		
		 //when first run the application, we should generate random data sets
		 //or use your own data set.
		 //we should both insert data into RTree and HBase.
		 //decided to use RTreeTalbe in project1
		
		 ArrayList<DataItem> attr = new ArrayList<DataItem>();
		 ArrayList<String> id = new ArrayList<String>();
		 ArrayList<Double> rate_array = new ArrayList<Double>();
		 ArrayList<String> cuisine_array = new ArrayList<String>();
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
	      
	    tree.RTreeTable(DBName, attr, id);
	    
		this.setLayout(new BorderLayout());
    	this.setBounds(x,y,z,g);
		this.setTitle(message);
		button = new JButton("Search");
		button.addActionListener(this);
		panel = new JPanel();
		panel.add(button);
		//f.add(panel);
		txt = new JTextField("Input your address", 10);
		txtDB = new JTextField("DBName", 5);
		panel.add(txt);
		
		this.add(panel);
		this.setVisible(true);
		this.setResizable(true);  
		
		JLabel label_cuisine = new JLabel("Cuisine:");  
        panel.add(label_cuisine);  
		
        //to set cuisine type
        comboBox_cuisine = new JComboBox();  
        comboBox_cuisine.addItem(" "); 
        comboBox_cuisine.addItem("Mars");
        comboBox_cuisine.addItem("Susi");  
        comboBox_cuisine.addItem("Chinese");  
        comboBox_cuisine.addItem("American"); 
        comboBox_cuisine.addItem("Italian");
        comboBox_cuisine.addItem("Spanish");
        comboBox_cuisine.addItem("Barbecue");
        comboBox_cuisine.addItem("Mediterranean");
        comboBox_cuisine.addItem("Mexico");
        comboBox_cuisine.addItem("French");
        comboBox_cuisine.addItem("Seafood");
        comboBox_cuisine.addItem("Indian");
        comboBox_cuisine.addItem("Steak");
        comboBox_cuisine.addItem("Belgian");
        comboBox_cuisine.addItem("German");
        comboBox_cuisine.addItem("Russia");
        comboBox_cuisine.addItem("Brazilian");
        comboBox_cuisine.addItem("Whatever");
        panel.add(comboBox_cuisine);
        
        JLabel label_distweight=new JLabel("Distance:");  
        panel.add(label_distweight);  
        //JComboBox comboBox_dist = new JComboBox(); 
        
        //distance weights
        comboBox_dist = new JComboBox();
        comboBox_dist.addItem("Unimportant");  
        comboBox_dist.addItem("Normal");  
        comboBox_dist.addItem("Important");  
        //comboBox_dist.addItemListener(this);
        comboBox_dist.addActionListener(this);
        //comboBox_dist.setName("Dist");
        panel.add(comboBox_dist); 
        
        //distance preference
        comboBox_dist_range = new JComboBox();
        comboBox_dist_range.addItem("1 mile");  
        comboBox_dist_range.addItem("1 - 3 miles");  
        comboBox_dist_range.addItem("3 - 5 miles");  
        //comboBox_dist_range.addItemListener(this);
        comboBox_dist_range.addActionListener(this);
        panel.add(comboBox_dist_range); 
        
        JLabel label_priceweight = new JLabel("Price:");  
        panel.add(label_priceweight);   
        
        //price weights
        comboBox_price = new JComboBox();  
        comboBox_price.addItem("Unimportant");  
        comboBox_price.addItem("Normal");  
        comboBox_price.addItem("Important");  
        panel.add(comboBox_price);
        
        //price preferences
        comboBox_price_range = new JComboBox();
        comboBox_price_range.addItem("$5-$10");  
        comboBox_price_range.addItem("$10-$30");  
        comboBox_price_range.addItem("$30-$50");  
        comboBox_price_range.addItem("$50-$80"); 
        comboBox_price_range.addItem("$80-$100");
        comboBox_price_range.addItem("$100 above");
        panel.add(comboBox_price_range);
        
        //rate weights
        JLabel label_rateweight = new JLabel("Rate:");  
        panel.add(label_rateweight);  
        comboBox_rate = new JComboBox();  
        comboBox_rate.addItem("Unimportant");  
        comboBox_rate.addItem("Normal");  
        comboBox_rate.addItem("Important"); 
        panel.add(comboBox_rate);
        
        //rate preferences
        comboBox_rate_range = new JComboBox();  
        comboBox_rate_range.addItem("1 - 3 stars");  
        comboBox_rate_range.addItem("3 - 4 stars");  
        comboBox_rate_range.addItem("4 - 5 stars"); 
        comboBox_rate_range.addItem("2 - 5 stars"); 
        panel.add(comboBox_rate_range);
        
        
       
       
        resultArea.setLineWrap(true);
        
       
        
        panel.add(resultArea);
        //this.getContentPane().add(resultArea);
        
        this.setVisible(true);  
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
	
	public static void main(String[] args) throws IOException {
		long start = System.currentTimeMillis();
		new GUI().initFrame("Food!",150,300,1400,300);
		//itemStateChanged(ItemEvent e);
		long end = System.currentTimeMillis();
		//System.out.println(end - start);
		
	}
	
	public void result (String result, int x, int y, int z, int g) {
		JFrame frame = new JFrame();
		frame.setBounds(x, y, z, g);
		List<Integer> res = new ArrayList<Integer>();
		
		for (int i = 0; i < 20; i++) {
			res.add(i);
		}
		
		JPanel result_panel = new JPanel();
		JTextArea result_txt = new JTextArea(res.toString(),100,200);
		result_panel.add(result_txt);
		frame.add(result_panel);
		frame.setVisible(true);
		frame.setResizable(true);
	    frame.setDefaultCloseOperation(this.DISPOSE_ON_CLOSE);
	    
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e){
		// TODO Auto-generated method stub
		String tmp = "";
		String lon = "";
		String lat = "";
		String cuisine;
		double longitude;
		double latitude;
		double distweight = 0;
		double priceweight = 0;
		double rateweight = 0;
		double dist_restrict_left = 0;
		double dist_restrict_right = 0;
		double rate_restrict = 0;
		List<Integer> price_restrict = new ArrayList<Integer>();
		
		//to see whether the action is the button or not
		if (e.getSource() == button) {
			String[] string = txt.getText().split(" ");
			for (int i = 0; i < string.length; i++) {
				tmp += string[i];
			}
			String str = getLocation("http://maps.googleapis.com/maps/api/geocode/xml?"
					+ "address=" + tmp);
			String[] tmptmp = str.split(" ");
			for (String i : tmptmp) {
				if (i.startsWith("<lat>")) {
					lat = i.replaceAll("<lat>", "");
					lat = lat.replaceAll("</lat>", "");
				}
				if (i.startsWith("<lng>")) {
					lon = i.replaceAll("<lng>", "");
					lon = lon.replaceAll("</lng>", "");
				}
				
			}
			
			longitude = Double.parseDouble(lon);
			latitude = Double.parseDouble(lat);
			

			boolean flag = true;
			
//			DBName = txtDB.getText();
			
			//to test whether the weights user inputed same or not
			if (comboBox_dist.getSelectedItem().equals(comboBox_price.getSelectedItem()) 
					&& comboBox_dist.getSelectedItem().equals(comboBox_rate.getSelectedItem())) {
				JOptionPane.showMessageDialog(this,
					       "Please select different weight", "Warning",
					       JOptionPane.ERROR_MESSAGE);
				flag = false;
			}else {
			
				if (comboBox_price.getSelectedItem().equals(comboBox_rate.getSelectedItem())) {
					JOptionPane.showMessageDialog(null,
						       "Please select different weight", "Warning",
						       JOptionPane.INFORMATION_MESSAGE);
					flag = false;
				}
				
				if (comboBox_rate.getSelectedItem().equals(comboBox_dist.getSelectedItem())) {
					JOptionPane.showMessageDialog(null,
						       "Please select different weight", "Warning", 
						       JOptionPane.INFORMATION_MESSAGE);
					flag = false;
				}
				

				if (comboBox_price.getSelectedItem().equals(comboBox_dist.getSelectedItem())) {
					JOptionPane.showMessageDialog(null,
						       "Please select different weight", "Warning", 
						       JOptionPane.INFORMATION_MESSAGE);
					flag = false;
				}
				
			}
			
			
			//to give specific weights and constraints according to the inputs
			
			//distance inputs
			if (comboBox_dist.getSelectedItem().equals("Unimportant")) {
				distweight = 0.1;
			}else if (comboBox_dist.getSelectedItem().equals("Normal")) {
				distweight = 0.3;
			}else if (comboBox_dist.getSelectedItem().equals("Important")) {
				distweight = 0.6;
			}
			
			if (comboBox_dist_range.getSelectedItem().equals("1 mile")) {
				dist_restrict_left = 0;
				dist_restrict_right = 1;
			}else if (comboBox_dist_range.getSelectedItem().equals("1 - 3 miles")) {
				dist_restrict_left = 1;
				dist_restrict_right = 3;
			}else if (comboBox_dist_range.getSelectedItem().equals("3 - 5 miles")) {
				dist_restrict_left = 3;
				dist_restrict_right = 5;
			}
			
			//price inputs
			if (comboBox_price_range.getSelectedItem().equals("$5-$10")) {
				price_restrict.add(5);
				price_restrict.add(7);
				price_restrict.add(10);
			}else if(comboBox_price_range.getSelectedItem().equals("$10-$30")) {
				//price_restrict.add(10);
				price_restrict.add(15);
				price_restrict.add(20);
				price_restrict.add(25);
				price_restrict.add(30);
			}else if (comboBox_price_range.getSelectedItem().equals("$30-$50")) {
				//price_restrict.add(30);
				price_restrict.add(35);
				price_restrict.add(40);
				price_restrict.add(45);
				price_restrict.add(50);
			}else if (comboBox_price_range.getSelectedItem().equals("$50-$80")) {
				//price_restrict.add(50);
				price_restrict.add(55);
				price_restrict.add(60);
				price_restrict.add(65);
				price_restrict.add(70);
				price_restrict.add(75);
				price_restrict.add(80);
			}else if (comboBox_price_range.getSelectedItem().equals("$80-$100")) {
				//price_restrict.add(80);
				price_restrict.add(85);
				price_restrict.add(90);
				price_restrict.add(95);
				price_restrict.add(100);
			}else if (comboBox_price_range.getSelectedItem().equals("$100 above")) {
				price_restrict.add(100);
				price_restrict.add(150);
				price_restrict.add(180);
				price_restrict.add(200);
				price_restrict.add(250);
				price_restrict.add(280);
				price_restrict.add(300);
			}
			
			//price inputs
			if (comboBox_price.getSelectedItem().equals("Unimportant")) {
				priceweight = 0.1;
			}else if (comboBox_price.getSelectedItem().equals("Normal")) {
				priceweight = 0.3;
			}else if (comboBox_price.getSelectedItem().equals("Important")) {
				priceweight = 0.6;
			}
			
			//rate inputs
			if (comboBox_rate_range.getSelectedItem().equals("1 - 3 stars")) {
				rate_restrict = 2;
			}else if (comboBox_rate_range.getSelectedItem().equals("3 - 4 stars")) {
				rate_restrict = 3.5;
			}else if (comboBox_rate_range.getSelectedItem().equals("4 - 5 stars")) {
				rate_restrict = 4.5;
			}else if (comboBox_rate_range.getSelectedItem().equals("2 - 5 stars")) {
				rate_restrict = 3;
			}
			
			
			if (comboBox_rate.getSelectedItem().equals("Unimportant")) {
				rateweight = 0.1;
			}else if (comboBox_rate.getSelectedItem().equals("Normal")) {
				rateweight = 0.3;
			}else if (comboBox_rate.getSelectedItem().equals("Important")) {
				rateweight = 0.6;
			}
			
			cuisine = (String) comboBox_cuisine.getSelectedItem();
			
			if (flag) {
				
				RTreeKnn test = new RTreeKnn(distweight, rateweight, priceweight);
				List<ArrayList<DataItem>> queryresult = new ArrayList<ArrayList<DataItem>>();
				List<DataItem> querytmp = new ArrayList<DataItem>();
				DataItem querypoint = new DataItem();
				
				for (int i = 0; i < price_restrict.size(); i++) {
					//System.out.println(1);
					int price = price_restrict.get(i);
					querypoint = new DataItem(
							  new Rectangle(longitude,latitude,longitude,latitude), 
							  price, rate_restrict, cuisine, longitude, latitude);
					querytmp = test.KNNSearch(tree.root, new DataItem(
						  new Rectangle(longitude,latitude,longitude,latitude), 
						  price, rate_restrict, cuisine, longitude, latitude), tree);
					queryresult.add((ArrayList<DataItem>) querytmp);
				}
				
				DataItem object = new DataItem();
				
				for (int i = 0; i < queryresult.size(); i++) {
					for (int j = 0; j < queryresult.get(i).size(); j++) {
				     object = queryresult.get(i).get(j);
				     if (!cuisine.equals("Whatever")) {
					     if (!object.cuisine.equals(cuisine)) {
					    	 queryresult.get(i).remove(object);
					     }
				     }
//				     if (!(object.RealDist < dist_restrict_right
//				    		 && object.RealDist > dist_restrict_left)) {
//				    	 queryresult.get(i).remove(object);
//				    	// System.out.println(dist_restrict_right);
//				     }
					}
				}
				
				String info = "No results fetched, please re-arrage"
						+ "your constraints";
				
				resultArea.setText("");
				Configuration conf = HBaseConfiguration.create();
	        	Connection connection;
	        	String restaurant_info = "";
	        	
				
				for (int i = 0; i < queryresult.size(); i++) {
					if (queryresult.get(i) == null || queryresult.get(i).size() == 0) {
						
						resultArea.setText("No results fetched, please re-arrage"
								+ " your constraints");
						
					}
		
					for (int j = 0; j < queryresult.get(i).size(); j++) {
						object = queryresult.get(i).get(j);
						
//						resultArea.append(object.RealDist + "miles" + "\n" + object.restaurantAddr +  " " + 
//								object.restaurantName + "\n" + object.cuisine + " " + "$" + object.price
//								+ " " + object.rate + "stars" + "\n");
						try {
							restaurant_info = connectHbase(object.rowindex);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
						resultArea.append(object.RealDist + " miles"+ "\n" +
								restaurant_info + "\n");
					}
					
				}
			}
			
		}
		
		
	}
	
	public static String getLocation(String strurl){
        String str = "";
		try {
			URL url = new URL(strurl);
	        URLConnection conn = url.openConnection();
	        HttpURLConnection http = (HttpURLConnection)conn;
	        http.connect();
	        InputStream in = http.getInputStream();
	        BufferedReader br = new BufferedReader(new InputStreamReader(in,"GBK"));
	        String s = null;
	        while((s = br.readLine()) != null) {
	        	str += s;
	        }
	        br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return str;
	}
	
	public String connectHbase (String index) throws IOException {
		Configuration conf = HBaseConfiguration.create();
  	    Connection connection = ConnectionFactory.createConnection(conf);
  	    TableName tablename = TableName.valueOf(DBName);
	    HTableDescriptor tableDescriptor = new HTableDescriptor(
        TableName.valueOf(DBName));
        tableDescriptor.addFamily(new HColumnDescriptor("fam1"));
        tableDescriptor.addFamily(new HColumnDescriptor("fam2"));
        HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
        Table table = connection.getTable(tablename);
        Get get = new Get(Bytes.toBytes(index));
 	    get.addFamily(Bytes.toBytes("fam1"));
 	    get.addColumn(Bytes.toBytes("fam1"), Bytes.toBytes("qual2"));
        Result result2 = table.get(get);
        String row = Bytes.toString(result2.getRow());
        String specificValue = Bytes.toString(
    		                result2.getValue(Bytes.toBytes("fam1"), 
    		                Bytes.toBytes("qual2")));
		return specificValue;
	}

	
}


