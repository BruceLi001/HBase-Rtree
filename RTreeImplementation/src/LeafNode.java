import java.util.ArrayList;
import java.util.List;

import Lib.Rectangle;
import Lib.DataItem;

public class LeafNode extends Node{
    private int keyArray[] = new int[MAX]; //this is to memo the lowest level
                                           //the lowest level contains all the objects in R-tree
    public boolean isLeaf () {
    	return true;
    }
    
    public boolean insertRecord (DataItem dataitem) {
    	 if (isFull()) {
    		 return false;
    	 }
    	 
    	 mbrArray[num] = dataitem.getMbr();
    	 keyArray[num] = dataitem.getKey();
    	 num++;
    	 
    	 return true;
    	 
    }
    
    public LeafNode split (DataItem dataitem) {
    	LeafNode newleaf = new LeafNode();
    	
    	//first we retrieve all the records
    	//then we reset all the nodes
    	List<DataItem> list = new ArrayList<DataItem>();
    	list.add(dataitem);
    	for (int i = 0; i < MAX; i++) {
    		list.add(new DataItem(mbrArray[i], keyArray[i]));
    	}
    	reset();
    	
    	//pick the seeds which the total area is the largest
    	DataItem seed1 = list.get(0);
    	DataItem seed2 = list.get(0);
    	int sumAreaMax = Rectangle.merge(seed1.getMbr(), 
    			seed2.getMbr()).getArea();//this is to calculate the area
    	                                  //once we put the two seeds in a group
    	for (int i = 0; i < list.size() - 1; i++) {
    		for (int j = i + 1; j < list.size(); j++) {
    			int sumArea = Rectangle.merge(seed1.getMbr(), 
    	    			seed2.getMbr()).getArea();
    			if (sumArea > sumAreaMax) {
    				sumAreaMax = sumArea;
    				seed1 = list.get(i);
    				seed2 = list.get(j);
    			}
    		}
    	}
    	
    	//After we have two seeds, we should insert the seeds
    	//into the groups and remove them from the list
    	
    	this.insertRecord(seed1);
    	newleaf.insertRecord(seed2);
    	list.remove(seed1);
    	list.remove(seed2);
    	
    	// 4、将剩下的MAX+1-2个项依据面积增量差值最大挑选出优先插入项，再依据最小面积增量原则添加到节点中
    			Rectangle leafMbr = mbrArray[num - 1];
    			Rectangle newLeafMbr = newLeaf.getMbrArray()[newLeaf.getNum() - 1];
    			
    			
    			//循环一次插入一个记录到两个节点中的一个，插入一个之后相应的节点的mbr要更新
    			while (list.size() != 0) {
    				// 当有一个节点的数目达到MAX+1-MIN,为了使另一个节点至少满足MIN个，必须将剩余节点统统插入另一个节点
    				if (num == MAX + 1 - MIN) {
    					for (Record rec : list) {
    						newLeaf.insertRecord(rec);
    					}
    					break;
    				}
    				if (newLeaf.getNum() == MAX + 1 - MIN) {
    					for (Record rec : list) {
    						insertRecord(rec);
    					}
    					break;
    				}

    				// 逐个比对数据项，找出最应当首先插入的数据项
    				double areaIncDiffMax = 0;		//最大面积增量差
    				Record willInsert = null;
    				boolean isNewLeaf = false;		//待插入节点的新旧标记
    				for (Record recordTmp : list) {
    					double areaInc1 = Rectangle.getNewMbr(leafMbr, recordTmp.getMbr()).getArea() - leafMbr.getArea(); 
    					double areaInc2 = Rectangle.getNewMbr(newLeafMbr, recordTmp.getMbr()).getArea() - newLeafMbr.getArea();
    					double areaIncDiff = Math.abs(areaInc1-areaInc2);
    					if(areaIncDiff >= areaIncDiffMax) {
    						areaIncDiffMax = areaIncDiff;
    						willInsert = recordTmp;
    						if(areaInc1 < areaInc2) {
    							isNewLeaf = false;
    						} else {
    							isNewLeaf = true;
    						}
    					}
    				}
    				
    				//插入一个数据项,之后更新这两个节点mbr
    				if(isNewLeaf) {
    					newLeaf.insertRecord(willInsert);
    					newLeafMbr = Rectangle.getNewMbr(newLeafMbr, willInsert.getMbr());
    				} else {
    					insertRecord(willInsert);
    					leafMbr = Rectangle.getNewMbr(leafMbr, willInsert.getMbr());
    				}
    				
    				list.remove(willInsert);
    			}

    			return newLeaf;
    	
    }
    
    public void reset () {
    	for (int i = 0; i < num; i++) {
    		mbrArray[i] = null;
    		keyArray[i] = 0;
    	}
    	num = 0 ;
    }
}



