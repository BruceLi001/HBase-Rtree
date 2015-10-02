package RTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Lib.Rectangle;
import Lib.DataItem;
import Lib.Node;



public class RTreeKnn {
	  double Distweight;
	  double Rateweight;
	  double Cuisineweight;
	  double Priceweight;
	  double nearestDist = Double.MAX_VALUE;
	  double dist = Double.MIN_VALUE;
	  RTree tree;
	  Node start;
	  DataItem query;
	  DataItem Nearest = new DataItem();
	  List<Node> branchList = new ArrayList<Node>();
	  List<DataItem> result = new ArrayList<DataItem>();
	  ArrayList<DataItem> finalresult = new ArrayList<DataItem>();
	  
	  
	  public RTreeKnn (double DistWeight, double RateWeight, double PriceWeight) {
		 this.Distweight = DistWeight;
		 this.Rateweight = RateWeight;
		 this.Priceweight = PriceWeight;
	  }
	  
      public List<DataItem> KNNSearch (Node start, DataItem data, RTree tree) {
    	  //int Nearest = Integer.MAX_VALUE;
    	  //System.out.println(start);
    	  //System.out.println(start.isLeaf());
    	  this.tree = tree;
    	  if (start.isLeaf()) {
    		  for (int i = 0; i < start.getNum(); i++) {
    			  dist = objectDist(data, start.getDataArray()[i]);
    			  //System.out.println(dist == nearestDist);
    			  if (dist <= nearestDist) {
    				  //System.out.println(nearestDist);
    				  nearestDist = dist;
    				  Nearest = start.getDataArray()[i];
    			  }
    		  }
    		  //System.out.println(Nearest);
    		  result.add(Nearest);
//    		  for (int j = 0; j < result.size(); j++) {
//				  if (result.get(j) != Nearest || result.size() == 0) {
//			  		result.add(Nearest);
//				  }else if (result.get(j) == Nearest) {
//					  result.remove(j);
//				  }
//			  } 
    	  }else {
    		  genBranchList(data, start, branchList);
    		  sortBranchList(branchList);
    		  int last = 0;
    		  last = DownpruneBranchList(start, data, result, branchList);
    		  
    		  for (int i = 0; i < last; i++) {
    			   start = branchList.get(i);
    			   for (int j = 0; j < start.getNum(); j++) {
    				   Node newNode = start.getChildArray()[j];
    			   //System.out.println(newNode.getMaxLati() - newNode.getMinLati());
    				   KNNSearch(newNode, data, tree);
    			   //System.out.println(1);
    				   last = UppruneBranchList(start, data, result, branchList);
    			  }
    		  }
    	  }
    	  
    	  sortResult(result);
    	  
//    	  for (int j = 1; j < result.size(); j++) {
//    		 if (!finalresult.contains(result.get(j))) {
//    			 finalresult.add(result.get(j));
//    		 }
//    		 
//    	  }
    	  
    	  for (int j = 1; j < result.size(); j++) {
     		 if (result.get(j) == result.get(j - 1)) {
     			 result.remove(j);
     		 }
     		 
     	  }
    	
    	  return result;
    	 
      }

	

//	private double objectDist(DataItem querypoint, DataItem datapoint) {
//		    
//		    double longDiff = Math.abs(querypoint.longitude - datapoint.longitude) 
//		    		          / (Math.max(querypoint.longitude, datapoint.longitude) 
//		    		        		  - Math.min(querypoint.longitude, datapoint.longitude));
//		   
//		    double latiDiff = Math.abs(querypoint.latitude - datapoint.latitude) 
//  		                    / (Math.max(querypoint.latitude, datapoint.latitude) 
//		        		      - Math.min(querypoint.latitude, datapoint.latitude));
//		    
//		    double realDistDiff = Math.sqrt((longDiff * 3600 * 0.186) * (longDiff * 3600 * 0.186) 
//		    		            + (latiDiff * 3600 * 0.161) * (latiDiff * 3600 * 0.161));
//		    
//		    double RateDiff = Math.abs(querypoint.rate - datapoint.rate) 
//  		                   / (Math.max(querypoint.rate, datapoint.rate) 
//		        		  - Math.min(querypoint.rate, datapoint.rate));
//		    
//		    double priceDiff = Math.abs(querypoint.price - datapoint.price) 
//  		                    / (Math.max(querypoint.price, datapoint.price) 
//		        		      - Math.min(querypoint.price, datapoint.price));
//		    
//		    double Dist = this.Distweight * ((longDiff + latiDiff) / 2) + this.Rateweight * RateDiff
//		    		      + this.Priceweight * priceDiff;
//		   
//		    return Dist;
//	}
      
     private double objectDist(DataItem querypoint, DataItem datapoint) {
		    
		    double longDiff = Math.abs(querypoint.longitude - datapoint.longitude);
		    		          
		   
		    double latiDiff = Math.abs(querypoint.latitude - datapoint.latitude);
		                   
		    
		    double realDistDiff = Math.sqrt((longDiff * 3600 * 0.0186) * (longDiff * 3600 * 0.0186) 
		    		            + (latiDiff * 3600 * 0.0161) * (latiDiff * 3600 * 0.0161));
		    
		    datapoint.setRealDist(realDistDiff);
		    
		    double RateDiff = Math.abs(querypoint.rate - datapoint.rate);
		                  
		    
		    double priceDiff = Math.abs(querypoint.price - datapoint.price); 
		                    
		    
		    double Dist = this.Distweight * ((longDiff + latiDiff) / 2) + this.Rateweight * RateDiff
		    		      + this.Priceweight * priceDiff;
		    
		    tree.root.getMaxLong();
		    
		    return Dist;
	}
    
    
	
	public double RectangleDist(DataItem querypoint, Node dataNode) {
		double longDiff = Math.abs(querypoint.getPrice() - dataNode.getMaxPrice()) 
		          / (Math.max(querypoint.getPrice(), dataNode.getMaxPrice()) 
		        		  - Math.min(querypoint.getPrice(), dataNode.getMinPrice()));
		double latiDiff = 0;
		double RateDiff = 0;
		double priceDiff = 0;
		double Dist = this.Distweight * (longDiff + latiDiff) + this.Rateweight * RateDiff
				      + this.Priceweight * priceDiff;
		return Dist;
	}
	
	public void genBranchList (DataItem data, Node start, List<Node> branchList) {
	 
		 for (int i = 0; i < start.getNum(); i++) {
			
			double MinDist = this.getMinDist(data, start.getChildArray()[i]);
			//System.out.println(MinDist);
			start.getChildArray()[i].setMinDist(MinDist);
			branchList.add(start.getChildArray()[i]);
			//genBranchList(data, ((NonLeafNode)start).getChildArray()[i], branchList);
			
		}
	   
	}
	
	private void sortBranchList(List<Node> branchList) {
		// TODO Auto-generated method stub
		 Comparator<Node> comp = new Comparator<Node>()  
				    {  
				        public int compare(Node first, Node second)  
				        {  
				            return (int) (first.MinDist - second.MinDist);
				        }  
				    };  
        Collections.sort(branchList, comp);
        //System.out.println(branchList.get(0).getMinLong());
	}
	
	//sort the list according to minDist
	private void sortResult(List<DataItem> result) {
		// TODO Auto-generated method stub
		 Comparator<DataItem> comp = new Comparator<DataItem>()  
				    {  
				        public int compare(DataItem first, DataItem second)  
				        {  
				            return (int) (first.MinDist - second.MinDist);
				        }  
				    };  
        Collections.sort(result, comp);
        //System.out.println(branchList.get(0).getMinLong());
	}
	
	//to down prune the branchList
	public int DownpruneBranchList(Node start, DataItem data,
			List<DataItem> nearest, List<Node> branchList) {
		for (int i = 0; i < branchList.size(); i++) {
			for (int j = 0; j < i; j++) {
				double R1_Min_Dist = getMinDist(data, branchList.get(i));
				double R2_Dist = getMinMaxDist(data, branchList.get(j));
				//System.out.println(branchList.get(j));
				//System.out.println(R2_Dist);
				if (R1_Min_Dist > R2_Dist) {
					branchList.remove(i);
				}
			}
			for (int k = 0; k < nearest.size(); k++) {
				double O_Dist = objectDist(data, nearest.get(k));
				double R_Min_Max_Dist = getMinMaxDist(data, branchList.get(i));
				if (O_Dist > R_Min_Max_Dist) {
					nearest.remove(k);
				}
			}
		}
		
//		for (int i = 0; i < branchList.size(); i++) {
//			double R_Dist = getMinMaxDist(data, branchList.get(i));
//			double O_Dist = objectDist(data, nearest);
//			if (R_Dist < O_Dist) {
//				//branchList.remove(i);
//				nearest = null;
//			}
//		}
		//System.out.println(branchList.size());
		return branchList.size();
		
	}
	
	//to up prune the branchList
	public int UppruneBranchList(Node start, DataItem data, List<DataItem> nearestlist,
			List<Node> branchList) {
		for (int i = 0; i < branchList.size(); i++) {
			double R_Dist = getMinDist(data, branchList.get(i));
			for (int j = 0; j < nearestlist.size(); j++) {
				DataItem nearest = nearestlist.get(j);
				double O_Dist = objectDist(data, nearest);
				nearest.MinDist = O_Dist;
				//System.out.println(R_Dist == O_Dist);
				if (R_Dist > O_Dist) {
					//System.out.println(branchList.size());
					//System.out.println(i > branchList.size());
					if (branchList.get(i) != null) {
					   branchList.remove(i);
					}
				}
			}
		}
		//System.out.println(nearest.getRate());
		return branchList.size();
	}
	
//	public double getMinDist (DataItem querypoint, Node dataNode) {
//		   if (dataNode.getMbr().contain(querypoint.getMbr())) {
//			   return 0;
//		   }else {
//			   double longDiff = Math.abs(querypoint.longitude - dataNode.getMinLong())
//					           / (Math.max(querypoint.longitude, dataNode.getMinLong())
//					        		   - Math.min(querypoint.longitude, dataNode.getMinLong()));
//			   
//			   double latiDiff = Math.abs(querypoint.latitude - dataNode.getMinLati())
//					            / (Math.max(querypoint.latitude, dataNode.getMinLati())
//					            		- Math.min(querypoint.latitude, dataNode.getMinLati()));
//			   
//			   double realDistDiff = Math.sqrt((longDiff * 3600 * 0.186) * (longDiff * 3600 * 0.186) 
//   		            + (latiDiff * 3600 * 0.161) * (latiDiff * 3600 * 0.161));
//			   
//			   double rateDiff = Math.abs(querypoint.rate - dataNode.getMinRate())
//					            / (Math.max(querypoint.rate, dataNode.getMinRate())
//					            		- Math.min(querypoint.rate, dataNode.getMinRate()));
//			   
//			   double priceDiff = Math.abs(querypoint.price - dataNode.getMinPrice())
//					              / (Math.max(querypoint.price, dataNode.getMinRate())
//					            		  - Math.min(querypoint.price, dataNode.getMinPrice()));
//			   
//			   double Dist = this.Distweight * ((longDiff + latiDiff) / 2) + this.Rateweight * rateDiff
//		    		      + this.Priceweight * priceDiff;
//			   //System.out.println(Dist);
//			   return Dist;
//		   }
//		   
//	}
	
	//to test a number in a range or not
	public boolean rangeInDefined (double num, double min, double max) {
		   return Math.max(min, num) == Math.min(num, max);
	}
	
	public static double getRealDiff (DataItem querypoint, DataItem data) {
		   double longDiff = Math.abs(querypoint.longitude - data.longitude);
		   double latiDiff = Math.abs(querypoint.latitude - data.latitude);
		   
		   
		   double realDistDiff = Math.sqrt((longDiff * 3600 * 0.0186) * (longDiff * 3600 * 0.0186) 
  		            + (latiDiff * 3600 * 0.0161) * (latiDiff * 3600 * 0.0161));
		   data.setRealDist(realDistDiff);
		   return realDistDiff;
	}
	
//	public double getMinDist (DataItem querypoint, Node dataNode) {
//		   
//		   double longDiff = 0;
//		   double latiDiff = 0;
//		   double rateDiff = 0;
//		   double priceDiff = 0;
//		   double realDiff = 0;
//		   
//		   if (dataNode.getMbr().contain(querypoint.getMbr()) && 
//				   this.rangeInDefined(querypoint.getPrice(), dataNode.getMinPrice(), dataNode.getMaxPrice())
//				   && this.rangeInDefined(querypoint.getRate(), dataNode.getMinRate(), dataNode.getMaxRate())) {
//			   return 0;
//		   }
//		   //do the projection on each dimesion
//		   if (this.rangeInDefined(querypoint.getLong(), dataNode.getMinLong(), 
//				   dataNode.getMaxLong()) ) {
//			   longDiff = 0;
//		   }else if (querypoint.getLong() < dataNode.getMinLong()) {
//			   longDiff = Math.abs(querypoint.getLong() - dataNode.getMinLong())
//					   / (Math.max(querypoint.getLong(), dataNode.getMinLong()) 
//							   - Math.min(querypoint.getLong(), dataNode.getMinLong()));
//		   }else if(querypoint.getLong() > dataNode.getMaxLong()) {
//			   longDiff = Math.abs(querypoint.getLong() - dataNode.getMaxLong())
//					   / (Math.max(querypoint.getLong(), dataNode.getMaxLong())
//							   - Math.min(querypoint.getLong(), dataNode.getMaxLong()));
//		   }
//		   //latitude dimension
//		   if (this.rangeInDefined(querypoint.getLong(), dataNode.getMinLati(), 
//				   dataNode.getMaxLati())) {
//			   latiDiff = 0;
//		   }else if (querypoint.getLati() < dataNode.getMinLati()) {
//			   latiDiff = Math.abs(querypoint.getLati() - dataNode.getMinLati())
//					   / (Math.max(querypoint.getLati(), dataNode.getMinLati())
//							   - Math.min(querypoint.getLati(), dataNode.getMaxLati()));
//		   }else if (querypoint.getLati() > dataNode.getMaxLati()) {
//			   latiDiff = Math.abs(querypoint.getLati() - dataNode.getMaxLati())
//					   / (Math.max(querypoint.getLati(), dataNode.getMaxLati())
//							   - Math.min(querypoint.getLati(), dataNode.getMaxLati()));
//		   }
//		   //rate dimension
//		   if (this.rangeInDefined(querypoint.getRate(), dataNode.getMinRate(), 
//				   dataNode.getMaxRate())) {
//			   rateDiff = 0;
//		   }else if (querypoint.getRate() < dataNode.getMinRate()) {
//			   rateDiff = Math.abs(querypoint.getRate() - dataNode.getMinRate())
//					   / (Math.max(querypoint.getRate(), dataNode.getMinRate())
//							   - (Math.min(querypoint.getRate(), dataNode.getMinRate())));
//		   }else if (querypoint.getRate() > dataNode.getMaxRate()) {
//			   rateDiff = Math.abs(querypoint.getRate() - dataNode.getMaxRate())
//					   / (Math.max(querypoint.getRate(), dataNode.getMaxRate())
//							   - Math.min(querypoint.getRate(), dataNode.getMaxRate()));
//		   }
//		   //price dimension
//		   if (this.rangeInDefined(querypoint.getRate(), dataNode.getMinPrice(), 
//				   dataNode.getMaxPrice())) {
//			   priceDiff = 0;
//		   }else if (querypoint.getPrice() < dataNode.getMinPrice()) {
//			   priceDiff = Math.abs(querypoint.getPrice() - dataNode.getMinPrice())
//					   / (Math.max(querypoint.getPrice(), dataNode.getMinPrice())
//							   - Math.min(querypoint.getPrice(), dataNode.getMinPrice()));
//		   }else if (querypoint.getPrice() > dataNode.getMaxPrice()) {
//			   priceDiff = Math.abs(querypoint.getPrice() - dataNode.getMaxPrice())
//					   / (Math.max(querypoint.getPrice(), dataNode.getMinPrice())
//							   - Math.min(querypoint.getPrice(), dataNode.getMaxPrice()));
//		   }
//		   //return d-dimensional result
//		   double Dist = this.Distweight * ((longDiff + latiDiff) / 2) + this.Rateweight * rateDiff
//	    		      + this.Priceweight * priceDiff;
//		   return Dist;
//	}
	
	public double getMinDist (DataItem querypoint, Node dataNode) {
		   
		   double longDiff = 0;
		   double latiDiff = 0;
		   double rateDiff = 0;
		   double priceDiff = 0;
		   double realDiff = 0;
		   
		   if (dataNode.getMbr().contain(querypoint.getMbr()) && 
				   this.rangeInDefined(querypoint.getPrice(), dataNode.getMinPrice(), dataNode.getMaxPrice())
				   && this.rangeInDefined(querypoint.getRate(), dataNode.getMinRate(), dataNode.getMaxRate())) {
			   return 0;
		   }
		   //do the projection on each dimesion
		   if (this.rangeInDefined(querypoint.getLong(), dataNode.getMinLong(), 
				   dataNode.getMaxLong()) ) {
			   longDiff = 0;
		   }else if (querypoint.getLong() < dataNode.getMinLong()) {
			   longDiff = Math.abs(querypoint.getLong() - dataNode.getMinLong());
					 
					  
		   }else if(querypoint.getLong() > dataNode.getMaxLong()) {
			   longDiff = Math.abs(querypoint.getLong() - dataNode.getMaxLong());
					  
		   }
		   
		   //latitude dimension
		   if (this.rangeInDefined(querypoint.getLong(), dataNode.getMinLati(), 
				   dataNode.getMaxLati())) {
			   latiDiff = 0;
		   }else if (querypoint.getLati() < dataNode.getMinLati()) {
			   latiDiff = Math.abs(querypoint.getLati() - dataNode.getMinLati());
					  
					   
		   }else if (querypoint.getLati() > dataNode.getMaxLati()) {
			   latiDiff = Math.abs(querypoint.getLati() - dataNode.getMaxLati());
					  
					
		   }
		   //rate dimension
		   if (this.rangeInDefined(querypoint.getRate(), dataNode.getMinRate(), 
				   dataNode.getMaxRate())) {
			   rateDiff = 0;
		   }else if (querypoint.getRate() < dataNode.getMinRate()) {
			   rateDiff = Math.abs(querypoint.getRate() - dataNode.getMinRate());
					   
					   
		   }else if (querypoint.getRate() > dataNode.getMaxRate()) {
			   rateDiff = Math.abs(querypoint.getRate() - dataNode.getMaxRate());
					  
		   }
		   //price dimension
		   if (this.rangeInDefined(querypoint.getRate(), dataNode.getMinPrice(), 
				   dataNode.getMaxPrice())) {
			   priceDiff = 0;
		   }else if (querypoint.getPrice() < dataNode.getMinPrice()) {
			   priceDiff = Math.abs(querypoint.getPrice() - dataNode.getMinPrice());
					  
					 
		   }else if (querypoint.getPrice() > dataNode.getMaxPrice()) {
			   priceDiff = Math.abs(querypoint.getPrice() - dataNode.getMaxPrice());
					  
					 
		   }
		   //return d-dimensional result
		   double Dist = this.Distweight * ((longDiff + latiDiff) / 2) + this.Rateweight * rateDiff
	    		      + this.Priceweight * priceDiff;
		   return Dist;
	}
	
//	public double getMinMaxDist (DataItem querypoint, Node dataNode) {
//		 double longDiff = Math.abs(querypoint.longitude - dataNode.getMaxLong())
//		           / (Math.max(querypoint.longitude, dataNode.getMaxLong())
//		        		   - Math.min(querypoint.longitude, dataNode.getMaxLong()));
//        // System.out.println(dataNode.getMbr().getxMin());
//         double latiDiff = Math.abs(querypoint.latitude - dataNode.getMaxLati())
//		            / (Math.max(querypoint.latitude, dataNode.getMaxLati())
//		            		- Math.min(querypoint.latitude, dataNode.getMaxLati()));
// 
//         double realDistDiff = Math.sqrt((longDiff * 3600 * 0.186) * (longDiff * 3600 * 0.186) 
//                    + (latiDiff * 3600 * 0.161) * (latiDiff * 3600 * 0.161));
// 
//         double rateDiff = Math.abs(querypoint.rate - dataNode.getMaxRate())
//		            / (Math.max(querypoint.rate, dataNode.getMaxRate())
//		            		- Math.min(querypoint.rate, dataNode.getMaxRate()));
// 
//         double priceDiff = Math.abs(querypoint.price - dataNode.getMaxPrice())
//		              / (Math.max(querypoint.price, dataNode.getMaxRate())
//		            		  - Math.min(querypoint.price, dataNode.getMaxPrice()));
// 
//         double Dist = this.Distweight * ((longDiff + latiDiff) / 2) + this.Rateweight * rateDiff
//		            + this.Priceweight * priceDiff;
//         //System.out.println(realDistDiff);
//         return Dist;
//	}
	
	
	//follow the d-dimensional formual in the KNN paper
	public double getMinMaxDist (DataItem querypoint, Node dataNode) {
		double rm;
		double rM;
		double longTmp;
		double latiTmp;
		double rateTmp;
		double priceTmp;
		double distTmp;
		double longDiff;
		double latiDiff;
		double rateDiff;
		double priceDiff;
		List<Double> dist = new ArrayList<Double>();
		
		
		if (querypoint.getLong() <= (dataNode.getMaxLong() + dataNode.getMinLong()) / 2) {
			longTmp = dataNode.getMinLong();
			if (querypoint.getLati() >= (dataNode.getMaxLati() + dataNode.getMinLati()) / 2) {
				latiTmp = dataNode.getMinLati();
			}else {
				latiTmp = dataNode.getMaxLati();
			}
			
			if (querypoint.getRate() >= (dataNode.getMaxRate() + dataNode.getMinRate()) / 2) {
				rateTmp = dataNode.getMinRate();
			}else {
				rateTmp = dataNode.getMaxRate();
			}
			
			if (querypoint.getPrice() >= (dataNode.getMaxPrice() + dataNode.getMinPrice()) / 2) {
				priceTmp = dataNode.getMinPrice();
			}else {
				priceTmp = dataNode.getMaxPrice();
			}
			
//			longDiff = Math.abs(querypoint.getLong() - longTmp)
//					   / (Math.max(querypoint.getLong(), longTmp) 
//							   - Math.min(querypoint.getLong(), longTmp));
//			
//			latiDiff = Math.abs(querypoint.getLati() - latiTmp)
//					   / (Math.max(querypoint.getLati(), latiTmp)
//							   - Math.min(querypoint.getLati(), latiTmp));
//			 
//			rateDiff = Math.abs(querypoint.getRate() - rateTmp)
//					   / (Math.max(querypoint.getRate(), rateTmp)
//							   - Math.min(querypoint.getRate(), rateTmp));
//			
//			priceDiff = Math.abs(querypoint.getPrice() - priceTmp)
//					   / (Math.max(querypoint.getPrice(), priceTmp)
//							   - Math.min(querypoint.getPrice(), priceTmp));
			longDiff = Math.abs(querypoint.getLong() - longTmp);
				
					  
			
			latiDiff = Math.abs(querypoint.getLati() - latiTmp);
				
					  
			 
			rateDiff = Math.abs(querypoint.getRate() - rateTmp);
					
					  
			
			priceDiff = Math.abs(querypoint.getPrice() - priceTmp);
					
					
			 
			distTmp = this.Distweight * ((longDiff + latiDiff) / 2) + this.Rateweight * rateDiff
					+ this.Priceweight * priceDiff;
			
			dist.add(distTmp);
		}else {
			longTmp = dataNode.getMaxLong();
			if (querypoint.getLati() >= (dataNode.getMaxLati() + dataNode.getMinLati()) / 2) {
				latiTmp = dataNode.getMinLati();
			}else {
				latiTmp = dataNode.getMaxLati();
			}
			
			if (querypoint.getRate() >= (dataNode.getMaxRate() + dataNode.getMinRate()) / 2) {
				rateTmp = dataNode.getMinRate();
			}else {
				rateTmp = dataNode.getMaxRate();
			}
			
			if (querypoint.getPrice() >= (dataNode.getMaxPrice() + dataNode.getMinPrice()) / 2) {
				priceTmp = dataNode.getMinPrice();
			}else {
				priceTmp = dataNode.getMaxPrice();
			}
			
//			longDiff = Math.abs(querypoint.getLong() - longTmp)
//					   / (Math.max(querypoint.getLong(), longTmp) 
//							   - Math.min(querypoint.getLong(), longTmp));
//			
//			latiDiff = Math.abs(querypoint.getLati() - latiTmp)
//					   / (Math.max(querypoint.getLati(), latiTmp)
//							   - Math.min(querypoint.getLati(), latiTmp));
//			 
//			rateDiff = Math.abs(querypoint.getRate() - rateTmp)
//					   / (Math.max(querypoint.getRate(), rateTmp)
//							   - Math.min(querypoint.getRate(), rateTmp));
//			
//			 priceDiff = Math.abs(querypoint.getPrice() - priceTmp)
//					   / (Math.max(querypoint.getPrice(), priceTmp)
//							   - Math.min(querypoint.getPrice(), priceTmp));
			longDiff = Math.abs(querypoint.getLong() - longTmp);
					
			latiDiff = Math.abs(querypoint.getLati() - latiTmp);
					
			rateDiff = Math.abs(querypoint.getRate() - rateTmp);
				
			priceDiff = Math.abs(querypoint.getPrice() - priceTmp);
					
					  
			distTmp = this.Distweight * ((longDiff + latiDiff) / 2) + this.Rateweight * rateDiff
					+ this.Priceweight * priceDiff;
			
			dist.add(distTmp);
		}
		
		if (querypoint.getLati() <= (dataNode.getMaxLati() + dataNode.getMinLati()) / 2) {
			latiTmp = dataNode.getMinLati();
			if (querypoint.getLong() >= (dataNode.getMaxLong() + dataNode.getMinLong()) / 2) {
				longTmp = dataNode.getMinLong();
			}else {
				longTmp = dataNode.getMaxLong();
			}
			
			if (querypoint.getRate() >= (dataNode.getMaxRate() + dataNode.getMinRate()) / 2) {
				rateTmp = dataNode.getMinRate();
			}else {
				rateTmp = dataNode.getMaxRate();
			}
			
			if (querypoint.getPrice() >= (dataNode.getMaxPrice() + dataNode.getMinPrice()) / 2) {
				priceTmp = dataNode.getMinPrice();
			}else {
				priceTmp = dataNode.getMaxPrice();
			}
			
//			longDiff = Math.abs(querypoint.getLong() - longTmp)
//					   / (Math.max(querypoint.getLong(), longTmp) 
//							   - Math.min(querypoint.getLong(), longTmp));
//			
//			latiDiff = Math.abs(querypoint.getLati() - latiTmp)
//					   / (Math.max(querypoint.getLati(), latiTmp)
//							   - Math.min(querypoint.getLati(), latiTmp));
//			 
//			rateDiff = Math.abs(querypoint.getRate() - rateTmp)
//					   / (Math.max(querypoint.getRate(), rateTmp)
//							   - Math.min(querypoint.getRate(), rateTmp));
//			
//			priceDiff = Math.abs(querypoint.getPrice() - priceTmp)
//					   / (Math.max(querypoint.getPrice(), priceTmp)
//							   - Math.min(querypoint.getPrice(), priceTmp));
			longDiff = Math.abs(querypoint.getLong() - longTmp);
					
			
			latiDiff = Math.abs(querypoint.getLati() - latiTmp);
					
					   
			rateDiff = Math.abs(querypoint.getRate() - rateTmp);
				
			
			priceDiff = Math.abs(querypoint.getPrice() - priceTmp);
				
					  
			distTmp = this.Distweight * ((longDiff + latiDiff) / 2) + this.Rateweight * rateDiff
					+ this.Priceweight * priceDiff;
			
			dist.add(distTmp);
		}else {
			latiTmp = dataNode.getMaxLati();
			if (querypoint.getLong() >= (dataNode.getMaxLong() + dataNode.getMinLong()) / 2) {
				longTmp = dataNode.getMinLong();
			}else {
				longTmp = dataNode.getMaxLong();
			}
			
			if (querypoint.getRate() >= (dataNode.getMaxRate() + dataNode.getMinRate()) / 2) {
				rateTmp = dataNode.getMinRate();
			}else {
				rateTmp = dataNode.getMaxRate();
			}
			
			if (querypoint.getPrice() >= (dataNode.getMaxPrice() + dataNode.getMinPrice()) / 2) {
				priceTmp = dataNode.getMinPrice();
			}else {
				priceTmp = dataNode.getMaxPrice();
			}
			
//			longDiff = Math.abs(querypoint.getLong() - longTmp)
//					   / (Math.max(querypoint.getLong(), longTmp) 
//							   - Math.min(querypoint.getLong(), longTmp));
//			
//			latiDiff = Math.abs(querypoint.getLati() - latiTmp)
//					   / (Math.max(querypoint.getLati(), latiTmp)
//							   - Math.min(querypoint.getLati(), latiTmp));
//			 
//			rateDiff = Math.abs(querypoint.getRate() - rateTmp)
//					   / (Math.max(querypoint.getRate(), rateTmp)
//							   - Math.min(querypoint.getRate(), rateTmp));
//			
//			priceDiff = Math.abs(querypoint.getPrice() - priceTmp)
//					   / (Math.max(querypoint.getPrice(), priceTmp)
//							   - Math.min(querypoint.getPrice(), priceTmp));
			longDiff = Math.abs(querypoint.getLong() - longTmp);
					
			
			latiDiff = Math.abs(querypoint.getLati() - latiTmp);
					
			 
			rateDiff = Math.abs(querypoint.getRate() - rateTmp);
				
			
			priceDiff = Math.abs(querypoint.getPrice() - priceTmp);
					   
			
			distTmp = this.Distweight * ((longDiff + latiDiff) / 2) + this.Rateweight * rateDiff
					+ this.Priceweight * priceDiff;
			
			dist.add(distTmp);
		}
		
		if (querypoint.getPrice() <= (dataNode.getMaxPrice() + dataNode.getMinPrice()) / 2) {
			priceTmp = dataNode.getMinPrice();
			if (querypoint.getLong() >= (dataNode.getMaxLong() + dataNode.getMinLong()) / 2) {
				longTmp = dataNode.getMinLong();
			}else {
				longTmp = dataNode.getMaxLong();
			}
			
			if (querypoint.getLati() >= (dataNode.getMaxLati() + dataNode.getMinLati()) / 2) {
				latiTmp = dataNode.getMinLati();
			}else {
				latiTmp = dataNode.getMaxLati();
			}
			
			if (querypoint.getRate() >= (dataNode.getMaxRate() + dataNode.getMinRate()) / 2) {
				rateTmp = dataNode.getMinRate();
			}else {
				rateTmp = dataNode.getMaxRate();
			}
			
		}else {
			priceTmp = dataNode.getMaxPrice();
			if (querypoint.getLong() >= (dataNode.getMaxLong() + dataNode.getMinLong()) / 2) {
				longTmp = dataNode.getMinLong();
			}else {
				longTmp = dataNode.getMaxLong();
			}
			
			if (querypoint.getLati() >= (dataNode.getMaxLati() + dataNode.getMinLati()) / 2) {
				latiTmp = dataNode.getMinLati();
			}else {
				latiTmp = dataNode.getMaxLati();
			}
			
			if (querypoint.getRate() >= (dataNode.getMaxRate() + dataNode.getMinRate()) / 2) {
				rateTmp = dataNode.getMinRate();
			}else {
				rateTmp = dataNode.getMaxRate();
			}
//			longDiff = Math.abs(querypoint.getLong() - longTmp)
//					   / (Math.max(querypoint.getLong(), longTmp) 
//							   - Math.min(querypoint.getLong(), longTmp));
//			
//			latiDiff = Math.abs(querypoint.getLati() - latiTmp)
//					   / (Math.max(querypoint.getLati(), latiTmp)
//							   - Math.min(querypoint.getLati(), latiTmp));
//			 
//			rateDiff = Math.abs(querypoint.getRate() - rateTmp)
//					   / (Math.max(querypoint.getRate(), rateTmp)
//							   - Math.min(querypoint.getRate(), rateTmp));
//			
//			priceDiff = Math.abs(querypoint.getPrice() - priceTmp)
//					   / (Math.max(querypoint.getPrice(), priceTmp)
//							   - Math.min(querypoint.getPrice(), priceTmp));
			longDiff = Math.abs(querypoint.getLong() - longTmp);
					
			
			latiDiff = Math.abs(querypoint.getLati() - latiTmp);
				
			 
			rateDiff = Math.abs(querypoint.getRate() - rateTmp);
					
			
			priceDiff = Math.abs(querypoint.getPrice() - priceTmp);
					
			
			distTmp = this.Distweight * ((longDiff + latiDiff) / 2) + this.Rateweight * rateDiff
					+ this.Priceweight * priceDiff;
			
			dist.add(distTmp);
		}
		
		if (querypoint.getRate() <= (dataNode.getMaxRate() + dataNode.getMinRate()) / 2) {
			rateTmp = dataNode.getMinRate();
			if (querypoint.getLong() >= (dataNode.getMaxLong() + dataNode.getMinLong()) / 2) {
				longTmp = dataNode.getMinLong();
			}else {
				longTmp = dataNode.getMaxLong();
			}
			
			if (querypoint.getLati() >= (dataNode.getMaxLati() + dataNode.getMinLati()) / 2) {
				latiTmp = dataNode.getMinLati();
			}else {
				latiTmp = dataNode.getMaxLati();
			}
			
			if (querypoint.getPrice() >= (dataNode.getMaxPrice() + dataNode.getMinPrice()) / 2) {
				rateTmp = dataNode.getMinPrice();
			}else {
				rateTmp = dataNode.getMaxPrice();
			}
		}else {
			rateTmp = dataNode.getMaxRate();
			if (querypoint.getLong() >= (dataNode.getMaxLong() + dataNode.getMinLong()) / 2) {
				longTmp = dataNode.getMinLong();
			}else {
				longTmp = dataNode.getMaxLong();
			}
			
			if (querypoint.getLati() >= (dataNode.getMaxLati() + dataNode.getMinLati()) / 2) {
				latiTmp = dataNode.getMinLati();
			}else {
				latiTmp = dataNode.getMaxLati();
			}
			
			if (querypoint.getPrice() >= (dataNode.getMaxPrice() + dataNode.getMinPrice()) / 2) {
				rateTmp = dataNode.getMinPrice();
			}else {
				rateTmp = dataNode.getMaxPrice();
			}
//			longDiff = Math.abs(querypoint.getLong() - longTmp)
//					   / (Math.max(querypoint.getLong(), longTmp) 
//							   - Math.min(querypoint.getLong(), longTmp));
//			
//			latiDiff = Math.abs(querypoint.getLati() - latiTmp)
//					   / (Math.max(querypoint.getLati(), latiTmp)
//							   - Math.min(querypoint.getLati(), latiTmp));
//			 
//			rateDiff = Math.abs(querypoint.getRate() - rateTmp)
//					   / (Math.max(querypoint.getRate(), rateTmp)
//							   - Math.min(querypoint.getRate(), rateTmp));
//			
//			priceDiff = Math.abs(querypoint.getPrice() - priceTmp)
//					   / (Math.max(querypoint.getPrice(), priceTmp)
//							   - Math.min(querypoint.getPrice(), priceTmp));
			longDiff = Math.abs(querypoint.getLong() - longTmp);
				
			
			latiDiff = Math.abs(querypoint.getLati() - latiTmp);
				
			 
			rateDiff = Math.abs(querypoint.getRate() - rateTmp);
					
			
			priceDiff = Math.abs(querypoint.getPrice() - priceTmp);
					
			
			distTmp = this.Distweight * ((longDiff + latiDiff) / 2) + this.Rateweight * rateDiff
					+ this.Priceweight * priceDiff;
			
			dist.add(distTmp);
		}
		
		double minMaxDist = Double.MIN_VALUE;
		for (int i = 0; i < dist.size(); i++) {
			if (minMaxDist < dist.get(i)) {
				minMaxDist = dist.get(i);
			}
		}
		return minMaxDist;
	}

	public static void main (String[] args) {
		  long start = System.currentTimeMillis();
		  RTree tree = new RTree();
		  RTreeKnn test = new RTreeKnn(0.3,0.2,0.5);
		  List<DataItem> result = new ArrayList<DataItem>();
		  //System.out.println(tree.root.getChildArray()[1].getDataArray()[1].getMbr().getxMax());
		  //System.out.println(((NonLeafNode)tree.root).childArray[0].level);
		  result = test.KNNSearch(tree.root, new DataItem(
				  new Rectangle(-78,39,-78,39), 50, 4, "Japannee", -78, 39
				  ), tree);
		  for (int i = 0; i < result.size(); i++) {
			  System.out.println(result.get(i).getLati());
		  }
		  long end = System.currentTimeMillis();
		  System.out.println(end - start);
		 
	  }
}
