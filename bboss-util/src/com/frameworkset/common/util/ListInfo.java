package com.frameworkset.common.util;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;

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

    /**获取数据总数*/
    private long totalSize;
    /***/
    private Serializable object;


   /**
    * 分批取出当前页的记录集合
    */
    private List datas;
    private Hashtable[] dbDatas;

    public ListInfo()
   {

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
   public Hashtable[] getDbDatas() {
       return dbDatas;
   }
   /**
    * @param dbDatas The dbDatas to set.
    */
   public void setDbDatas(Hashtable[] dbDatas) {
       this.dbDatas = dbDatas;
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



    public void setObject(Serializable object) {
        this.object = object;
    }

    public void setShowAll(boolean showAll) {
        this.showAll = showAll;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }
}
