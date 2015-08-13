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
package com.frameworkset.common.poolman.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.spi.assemble.Pro;

import com.frameworkset.common.poolman.monitor.AbandonedTraceExt;
import com.frameworkset.common.poolman.sql.PoolManDataSource;
import com.frameworkset.orm.transaction.TXDataSource;

/**
 * 
 * <p>
 * Title: DatasourceUtil.java
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * @Date 2012-7-31 上午9:51:27
 * @author biaoping.yin
 * @version 1.0
 */
public class DatasourceUtil {
	public static final String DATASOURCE_BEAN_NAME = "datasource";
	private static Logger log = Logger.getLogger(DatasourceUtil.class);

	public static DataSource getDataSource(String sourcefile) {
		BaseApplicationContext context = DefaultApplicationContext
				.getApplicationContext(sourcefile);
		return context.getTBeanObject(DATASOURCE_BEAN_NAME, DataSource.class);
	}

	public static Map<String, Object> getDataSourceParameters(String sourcefile) {
		BaseApplicationContext context = DefaultApplicationContext
				.getApplicationContext(sourcefile);
		Pro pro = context.getProBean(DATASOURCE_BEAN_NAME);
		List<Pro> list = pro.getReferences();
		Map<String, Object> parameters = new HashMap<String, Object>();
		for (int i = 0; list != null && i < list.size(); i++) {
			Pro param = list.get(i);
			parameters.put(param.getName(), param.getValue());

		}
		return parameters;
	}

	private static DataSource getSRCDataSource(TXDataSource ds) {
		return ds.getSRCDataSource();
	}

    public static void increamentMaxTotalConnections(DataSource datasource,int nums)
    {
    	DataSource datasource_ = null;
		if (datasource instanceof TXDataSource) {
			datasource_ = getSRCDataSource((TXDataSource) datasource);
		} else {
			datasource_ = datasource;
		}
		String name = null;
		if (datasource_ instanceof PoolManDataSource) {
			PoolManDataSource temp = (PoolManDataSource) datasource_;
			name = temp.getPoolName();
			datasource_ = temp.getInnerDataSource();
		}
		
		try {
			if (datasource_ != null) {
				Method getMaxTotal = datasource_.getClass().getMethod(
						"getMaxTotal");
				
				Method setMaxTotal = datasource_.getClass().getMethod(
						"setMaxTotal",int.class);
				
				if (getMaxTotal != null && setMaxTotal != null)
				{					 
					int  maxTotal = ((Integer ) getMaxTotal.invoke(datasource_)).intValue();
					int newmaxTotal = maxTotal + nums;
					
					
					setMaxTotal.invoke(datasource_, newmaxTotal);
					if(name != null)
					{
						log.info("Increament MaxTotal Connections from "+maxTotal +" to "+newmaxTotal + " for datasource[" + name+"]");
					}
					
				}
				
			}
		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
    }
	
	public static List<AbandonedTraceExt> getGoodTraceObjects(DataSource datasource) {

		DataSource datasource_ = null;
		if (datasource instanceof TXDataSource) {
			datasource_ = getSRCDataSource((TXDataSource) datasource);
		} else {
			datasource_ = datasource;
		}
		if (datasource_ instanceof PoolManDataSource) {
			PoolManDataSource temp = (PoolManDataSource) datasource_;
			datasource_ = temp.getInnerDataSource();
		}
		try {
			if (datasource_ != null) {
				Method getTraceObjects = datasource_.getClass().getMethod(
						"getGoodTraceObjects");

				if (getTraceObjects == null) {
					
				}
				else
				{
					 
						List<AbandonedTraceExt>  dd = (List<AbandonedTraceExt> ) getTraceObjects.invoke(datasource_);
						return dd;
					 
				}
				
			}
		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		return new ArrayList<AbandonedTraceExt>();

	}

	public static boolean closeDS(DataSource datasource) {
		DataSource datasource_ = null;
		if (datasource instanceof TXDataSource) {
			datasource_ = getSRCDataSource((TXDataSource) datasource);
		} else {
			datasource_ = datasource;
		}
		if (datasource_ != null) {
			try {
				Method close = datasource_.getClass().getMethod("close");
				if (close != null)
				{
					close.invoke(datasource_);
					return true;
				}
				
			} catch (SecurityException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
			} catch (NoSuchMethodException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
			} catch (IllegalArgumentException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
			} catch (IllegalAccessException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
			} catch (InvocationTargetException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
			}

		}
		return false;
	}
	
	/**
	 * 获取当前链接池中空闲的链接数 接口只对内部数据源有用，外部数据源返回-1
	 * 非连接池数据源为实现该功能
	 * @return
	 */
	public static int getNumIdle(DataSource datasource) {
		
		
			
		DataSource datasource_ = null;
		if(datasource instanceof TXDataSource)
		{
			datasource_ =getSRCDataSource((TXDataSource)datasource);
		}
		else
		{
			datasource_ = datasource;
		}
		
		if (datasource_ instanceof PoolManDataSource) {
			PoolManDataSource temp = (PoolManDataSource) datasource_;
			datasource_ = temp.getInnerDataSource();
			
		}
		try {
			if (datasource_ != null) {
				Method getNumIdle = datasource_.getClass().getMethod(
						"_getNumIdle");

				
				if (getNumIdle != null) {
					return (Integer) getNumIdle.invoke(datasource_);
				}
			}
		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		return -1;
		
		
	}
	
	/**
	 * 获取当前链接池中正在使用的链接 接口只对内部数据源有用，外部数据源返回-1
	 * 
	 * @return
	 */
	public static int getNumActive(DataSource datasource) {
		
		
		DataSource datasource_ = null;
		if(datasource instanceof TXDataSource)
		{
			datasource_ =getSRCDataSource((TXDataSource)datasource);
		}
		else
		{
			datasource_ = datasource;
		}
		
		if (datasource_ instanceof PoolManDataSource) {
			PoolManDataSource temp = (PoolManDataSource) datasource_;
			datasource_ = temp.getInnerDataSource();
			
		}

		try {
			if (datasource_ != null) {
				Method getNumActive = datasource_.getClass().getMethod(
						"_getNumActive");

				
				if (getNumActive != null) {
					return (Integer) getNumActive.invoke(datasource_);
				}
			}
		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		return -1;
		
		
	}
	
	
	/**
	 * 获取并发最大使用链接数发生的时间点，记录链接池到目前为止并发使用链接的最大数目的时间，内置dbcp2才有意义， 外部数据源返回-1
	 * 
	 * @return
	 */
	public static long getMaxActiveNumTime(DataSource datasource) {
		
		
		DataSource datasource_ = null;
		if(datasource instanceof TXDataSource)
		{
			datasource_ =getSRCDataSource((TXDataSource)datasource);
		}
		else
		{
			datasource_ = datasource;
		}
		if (datasource_  instanceof PoolManDataSource) {
			PoolManDataSource temp = (PoolManDataSource) datasource_ ;
			datasource_ = temp.getInnerDataSource();
			
		}

		try {
			if (datasource_ != null) {
				Method getMaxActiveNumTime = datasource_.getClass().getMethod(
						"getMaxActiveNumTime");

				
				if (getMaxActiveNumTime != null) {
					return (Long) getMaxActiveNumTime.invoke(datasource_);
				}
			}
		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		return -1;
				
	}
	
	/**
	 * 获取并发最大使用链接数，记录链接池到目前为止并发使用链接的最大数目， 外部数据源返回-1
	 * 
	 * @return
	 */
	public static int getMaxNumActive(DataSource datasource) {
		
		
		DataSource datasource_ = null;
		if(datasource instanceof TXDataSource)
		{
			datasource_ =getSRCDataSource((TXDataSource)datasource);
		}
		else
		{
			datasource_ = datasource;
		}
		if (datasource_  instanceof PoolManDataSource) {
			PoolManDataSource temp = (PoolManDataSource) datasource_ ;
			datasource_ = temp.getInnerDataSource();
			
		}

		try {
			if (datasource_ != null) {
				Method getMaxNumActive = datasource_.getClass().getMethod(
						"getMaxNumActive");

				
				if (getMaxNumActive != null) {
					return (Integer) getMaxNumActive.invoke(datasource_);
				}
			}
		} catch (SecurityException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (NoSuchMethodException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IllegalArgumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		} catch (InvocationTargetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
		return -1;
				
	}

}
