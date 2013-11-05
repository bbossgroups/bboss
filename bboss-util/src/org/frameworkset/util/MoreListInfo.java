package org.frameworkset.util;

import java.util.ArrayList;
import java.util.List;

public class MoreListInfo {
	private boolean hasmore;
	
	
    /**
     * 实际从数据库查询到的记录数，这个数字可能和应用层调用getSize()方法
     * 得到的数据不一致，因为应用程序可能会修改数据集中的数据（增加或者删除记录）
     * 所以more查询时计算是否达到运算的最后一条记录时需要使用resultSize
     */
    private int resultSize;
 
    
   


   /**
    * 分批取出当前页的记录集合
    */
    private List datas;
    
   
  

  
   /**
    * Access method for the datas property.
    *
    * @return   the current value of the datas property
    */
   public List getDatas()
   {
       return datas;
   }

   /**
    * Sets the value of the datas property.
    *
    * @param aDatas the new value of the datas property
    */
   public void setDatas(List aDatas)
   {
       datas = aDatas;
   }

   

    /**
     * 获取最终结果集中的当页记录数
     * @return
     */
    public int getSize()
    {
    	return this.datas != null ?this.datas.size():0;    	
    }	
	/**
	 * 获取原始查询数据库得到的当页记录数据
	 * @return the resultSize
	 */
	public int getResultSize() {
		return resultSize;
	}

	public boolean isHasmore() {
		return hasmore;
	}




	public MoreListInfo(boolean hasmore, int resultSize, List datas) {
		super();
		this.hasmore = hasmore;
		this.resultSize = resultSize;
		this.datas = datas;
	}
	
	public MoreListInfo() {
		super();
		this.hasmore = false;
		this.resultSize = 0;
		this.datas = new ArrayList();
	}

}
