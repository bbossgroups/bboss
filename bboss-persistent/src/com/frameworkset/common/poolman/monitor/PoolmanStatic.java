/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.frameworkset.common.poolman.monitor;

import com.frameworkset.common.poolman.util.JDBCPoolMetaData;


public class PoolmanStatic extends JDBCPoolMetaData{
	/**
	 * 最小连接数
	 */
//    private int minconnections;
    /**
     * 最大连接数
     */
//    private int maxconnections;
    /**
     * 当前空闲连接数
     */
    private int idleconnections;
    /**
     * 当前使用连接数
     */
    private int activeconnections;
    
    /**
     * 使用高峰值
     */
    private int heapconnections;
   
    /**
     * poolman dbname
     */
    private String dbname;
    
    /* 启动时间
	 */
   private long startTime;
   /**
    * 停止时间
    */
   private long stopTime;
   /**
    * 状态
    */
   private String status;
   /**
    * 持续运行时间
    */
   private long continousTime;
   
   private boolean usepool = true;
  
   /**
    * 检测sql语句
    */
//   private String validationQuery;
   /**
    * 数据库驱动
    */
//   private String driver;
   /**
    * 是否外部数据源
    */
//   private boolean external = PoolManConstants.EXTERNAL;
   /**
    * 是否外部数据源
    */
//   private String externaljndiName;
   /**
    * jndi名称
    */
//   private String JNDIName;
 
   
//   public String getValidationQuery() {
//		return validationQuery;
//	}
//	public void setValidationQuery(String validationQuery) {
//		this.validationQuery = validationQuery;
//	}
//	public String getDriver() {
//		return driver;
//	}
//	public void setDriver(String driver) {
//		this.driver = driver;
//	}
//	public boolean isExternal() {
//		return external;
//	}
//	public void setExternal(boolean external) {
//		this.external = external;
//	}
//	public String getExternaljndiName() {
//		return externaljndiName;
//	}
//	public void setExternaljndiName(String externaljndiName) {
//		this.externaljndiName = externaljndiName;
//	}
//	public String getJNDIName() {
//		return JNDIName;
//	}
//	public void setJNDIName(String jNDIName) {
//		JNDIName = jNDIName;
//	}
	public long getStartTime()
   {
       return startTime;
   }
   public void setStartTime(long startTime)
   {
       this.startTime = startTime;
   }
   public long getStopTime()
   {
       return stopTime;
   }
   public void setStopTime(long stopTime)
   {
       this.stopTime = stopTime;
   }
   public String getStatus()
   {
       return status;
   }
   public void setStatus(String status)
   {
       this.status = status.toUpperCase();
   }

   public long getContinousTime()
   {
       return continousTime;
   }
   public void setContinousTime(long continousTime)
   {
       this.continousTime = continousTime;
   }
  
    
    
//    public int getMinconnections()
//    {
//        return minconnections;
//    }
//    public void setMinconnections(int minconnections)
//    {
//        this.minconnections = minconnections;
//    }
//    public int getMaxconnections()
//    {
//        return maxconnections;
//    }
//    public void setMaxconnections(int maxconnections)
//    {
//        this.maxconnections = maxconnections;
//    }
    public int getIdleconnections()
    {
        return idleconnections;
    }
    public void setIdleconnections(int idleconnections)
    {
        this.idleconnections = idleconnections;
    }
    public int getActiveconnections()
    {
        return activeconnections;
    }
    public void setActiveconnections(int activeconnections)
    {
        this.activeconnections = activeconnections;
    }
    public int getHeapconnections()
    {
        return heapconnections;
    }
    public void setHeapconnections(int heapconnections)
    {
        this.heapconnections = heapconnections;
    }
    public String toString()
    {
        StringBuffer ret = new StringBuffer(100);
        ret.append(super.toString());
        ret.append("dbname:").append(this.getDbname()).append(",")
        .append("minconnections:").append(this.getMaximumSize()).append(",")
        .append("maxconnections:").append(this.getMaximumSize()).append(",")
        .append("idleconnections:").append(idleconnections).append(",")
        .append("activeconnections:").append(activeconnections).append(",")
        .append("heapconnections:").append(heapconnections)
        .append("\r\n");
        return ret.toString();
                
    
    }
    public String getDbname()
    {
        return dbname;
    }
    public void setDbname(String dbname)
    {
        this.dbname = dbname;
    }
	/**
	 * @return the usepool
	 */
	public boolean isUsepool() {
		return usepool;
	}
	/**
	 * @param usepool the usepool to set
	 */
	public void setUsepool(boolean usepool) {
		this.usepool = usepool;
	}

}
