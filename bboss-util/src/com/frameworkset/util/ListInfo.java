package com.frameworkset.util;

import java.io.Serializable;
import java.util.List;

import org.frameworkset.util.MoreListInfo;

/**
 * <p>Title: ListInfo</p>
 *
 * <p>Description:
 *    封装分页信息
 * </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class ListInfo implements Serializable{
    /**是否显示所有的数据*/
    private boolean showAll = false;
    /*
    * 显示的数据字段名称
	 */
	public static final int DEFAULT_MAX_ITEMS = Integer.MAX_VALUE,
			DEFAULT_MAX_PAGE_ITEMS = 10, DEFAULT_MAX_INDEX_PAGES = 10;
    
    private int maxPageItems = DEFAULT_MAX_PAGE_ITEMS;

    /**获取数据总数*/
    private long totalSize;
    /**
     * 实际从数据库查询到的记录数，这个数字可能和应用层调用getSize()方法
     * 得到的数据不一致，因为应用程序可能会修改数据集中的数据（增加或者删除记录）
     * 所以more查询时计算是否达到运算的最后一条记录时需要使用resultSize
     */
    private int resultSize;
    /***/
    private Serializable object;
    
    /**
     * 判断数据集合是否是直接从dbutil中获取
     * 为true时表示直接从dbutil中获取到的数据
     */
    private boolean isdbdata = false;


   /**
    * 分批取出当前页的记录集合
    */
    private List datas;
    private Object[] dbDatas;
    /**
	 * more分页查询，不会计算总记录数，如果没有记录那么返回的ListInfo的datas的size为0,
	 * 提升性能，同时前台标签库也会做响应的调整
	 */
    private boolean more = false;
   
    
    /**
	 * @return the more
	 */
	public boolean isMore() {
		return more;
	}
	/**
	 * @param more the more to set
	 */
	public void setMore(boolean more) {
		this.more = more;
	}
    public ListInfo()
   {

   }
   
    /**
     * added by biaoping.yin on 20080728
     * @return
     */
   public Object getObjectDatas()
   {
	   if(!this.isdbdata())
	   {
		   return this.datas;
	   }
	   else
	   {
		   return this.dbDatas;
	   }
   }
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

   public String toString()
   {
       return "ListInfo:"+ this.getClass() + "\r\ndatas:" + datas + "\r\ntotalSize:" + totalSize;
   }
   /**
    * @return Returns the dbDatas.
    */
   public Object[] getArrayDatas() {
       return dbDatas;
   }
   /**
    * @param dbDatas The dbDatas to set.
    */
   public void setArrayDatas(Object[] dbDatas) {
       this.dbDatas = dbDatas;
       this.isdbdata = true;
   }
   
   public boolean isdbdata()
   {
	   return this.isdbdata;
   }

    public Serializable getObject() {
        return object;
    }

    public boolean isShowAll() {
        return showAll;
    }


    public long getTotalSize() {
        return totalSize;
    }
    /**
     * 获取最终结果集中的当页记录数
     * @return
     */
    public int getSize()
    {
    	if(!isdbdata())
    	{
    		return this.datas != null ?this.datas.size():0;
    		
    	}
    	else
    	{
    		return this.dbDatas != null ?this.dbDatas.length:0;
    	}
    }
    
    



    public void setObject(Serializable object) {
        this.object = object;
    }

    public void setShowAll(boolean showAll) {
        this.showAll = showAll;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

	/**
	 * @return the maxPageItems
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}

	/**
	 * @param maxPageItems the maxPageItems to set
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}
	/**
	 * 获取原始查询数据库得到的当页记录数据
	 * @return the resultSize
	 */
	public int getResultSize() {
		return resultSize;
	}
	/**
	 * @param resultSize the resultSize to set
	 */
	public void setResultSize(int resultSize) {
		this.resultSize = resultSize;
	}
	
	public static MoreListInfo buildMoreListInfo(ListInfo listInfo)
	{
		MoreListInfo moreList = new MoreListInfo(listInfo.getResultSize() == listInfo.getMaxPageItems(),listInfo.getResultSize(),listInfo.getDatas());
		return moreList;
	}
	
	public MoreListInfo getMoreListInfo()
	{
//		MoreListInfo moreList = new MoreListInfo(listInfo.getResultSize() < listInfo.getMaxPageItems(),listInfo.getResultSize(),listInfo.getDatas());
		return buildMoreListInfo(this);
	}
}
