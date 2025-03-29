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
 *  distributed under the License is distributed on an "AS IS" bboss persistent,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package com.frameworkset.common.poolman.management;


import com.frameworkset.common.poolman.util.DBStartResult;
import com.frameworkset.common.poolman.util.JDBCPool;
import com.frameworkset.common.poolman.util.JDBCPoolMetaData;
import com.frameworkset.common.poolman.util.SQLManager;
import org.frameworkset.util.shutdown.ShutdownUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class LocalPoolDeployer extends BaseTableManager implements PoolManDeployer,Serializable {
	public static boolean addShutdownHook = false;
	private static Logger logger = LoggerFactory.getLogger(LocalPoolDeployer.class);
	public static void shutdownHandle()
	{
		if(addShutdownHook)
			return;
		// add VM shutdown event handler
        try {
        	 
            // use reflection and catch the Exception to allow PoolMan to work with 1.2 VM's
            ShutdownUtil.addShutdownHook(new Runnable(){

				public void run() {
					 try {
						 updateTableInfo();
				            SQLManager.destroy(false);
				           // GenericPoolManager.getInstance().destroyPools();
				        } catch (Exception e) {
				           logger.warn("Unable to properly shutdown: ", e);
				        }
					
				}
            	
            },Integer.MAX_VALUE - 9);
            addShutdownHook = true;
        } catch (Exception e) {
        	addShutdownHook = true;
        }
	}
	
    public DBStartResult deployConfiguration(PoolManConfiguration config) throws Exception {

		DBStartResult dbStartResult =startDataSources(config.getDataSources(),config.getConnectionProperties());
//        startGenericPools(config.getGenericPools());

        // Note: there is no admin for the non-JMX PoolMan

        // add VM shutdown event handler
        	shutdownHandle();
        return dbStartResult;
    }
    
    public DBStartResult deployConfiguration(PoolManConfiguration config,String dbname) throws Exception {

		DBStartResult dbStartResult = startDataSources(config.getDataSources(),config.getConnectionProperties());
			shutdownHandle();
//        startGenericPools(config.getGenericPools());
		return dbStartResult;
        
    }
    
	public DBStartResult deployConfiguration(PoolManConfiguration config, Map values)
	throws Exception {
// TODO Auto-generated method stub
		DBStartResult dbStartResult = startDataSource(config.getDataSources(),values,config.getConnectionProperties(),config);
			shutdownHandle();
		return dbStartResult;
	}


    private DBStartResult startDataSources(ArrayList datasources,Properties connectionProperties) throws Exception {

        if (datasources == null)
            return null;

		DBStartResult dbStartResult = new DBStartResult();
        for (Iterator iter = datasources.iterator(); iter.hasNext();) {

            // Get each set of datasource entries
            Properties dbprops = (Properties) iter.next();

            // create the metadata object
            JDBCPoolMetaData metadata = new JDBCPoolMetaData();

            metadata.setConnectionProperties(connectionProperties);
            BeanInfo beanInfo = Introspector.getBeanInfo(metadata.getClass());

            // set attributes based on properties

            PropertyDescriptor[] attributes = beanInfo.getPropertyDescriptors();
            for (int n = 0; n < attributes.length; n++) {

                // get bean attribute name
                String attrName = attributes[n].getName();

                if (dbprops.containsKey(attrName.toLowerCase())) {

                    // get value in props
                    String propsVal = dbprops.getProperty(attrName.toLowerCase());

                    Class type = attributes[n].getPropertyType();
//                    System.out.println("props: " + attrName);
//                    System.out.println("propsVal: " + propsVal);
                    // create attribute value of correct type
                    if(type == java.lang.Boolean.class)
                    	type = boolean.class;
                    PropertyEditor editor = PropertyEditorManager.findEditor(type);
                    editor.setAsText(propsVal);
                    Object value = editor.getValue();
                    // set attribute value on bean
                    attributes[n].getWriteMethod().invoke(metadata, new Object[]{value});
                }
            }
            metadata.initDatasourceParameters();
			if(logger.isInfoEnabled()) {
				logger.info(" Created JDBC Connection Pool named {},config:{}",metadata.getName(),metadata.toString());
			}
			JDBCPool jdbcPool = SQLManager.getInstance().createPool(metadata,null);
			if(jdbcPool != null ){
				dbStartResult.addDBStartResult(jdbcPool.getDBName());
			}


        }
        return dbStartResult;
    }
    
    
    private DBStartResult startDataSource(ArrayList datasources,Map<String,String> values,Properties connectionProperties,PoolManConfiguration config) throws Exception {

        if (datasources == null )
            return null;
		DBStartResult dbStartResult = new DBStartResult();
        Properties dbprops = null;
        for (Iterator iter = datasources.iterator(); iter.hasNext();) {

        	dbprops = (Properties) iter.next();
        	boolean useTemplate = false;
            // Get each set of datasource entries
        	if(values != null && values.size() > 0)
        	{
	        	String dbname = (String)values.get("dbname"); 
	        	
	            String temp = (String)dbprops.get("dbname");
	            
	            if(temp != null && dbname != null && (temp.equals(dbname)|| temp.equals("${dbname}")) )
	            {
	            	useTemplate = true;
	            }
        	}
        

            // create the metadata object
            JDBCPoolMetaData metadata = new JDBCPoolMetaData();

            metadata.setConnectionProperties(connectionProperties);
            BeanInfo beanInfo = Introspector.getBeanInfo(metadata.getClass());

            // set attributes based on properties

            PropertyDescriptor[] attributes = beanInfo.getPropertyDescriptors();
            for (int n = 0; n < attributes.length; n++) {

                // get bean attribute name
                String attrName = attributes[n].getName().toLowerCase();
                
                if (dbprops.containsKey(attrName)) {

                    // get value in props
                    String propsVal = null;
                    if(!useTemplate)
                    {
                    	propsVal = dbprops.getProperty(attrName);
                    }
                    else
                    {
                    	propsVal = values.get(attrName);
                    	if(propsVal == null)
                    		propsVal = dbprops.getProperty(attrName);
                    		
                    }

                    Class type = attributes[n].getPropertyType();
//                    System.out.println("props: " + attrName);
//                    System.out.println("propsVal: " + propsVal);
                    // create attribute value of correct type
                    PropertyEditor editor = PropertyEditorManager.findEditor(type);
                    Object value = null;
                    if(type.isAssignableFrom(Boolean.class))
                    {
                    	if(propsVal == null || propsVal.trim().equals(""))
                    	{
                    		
                    	}
                    	else
                    	{
                    		 editor.setAsText(propsVal);
     	                    value = editor.getValue();
                    	}
                    }
                    else
                    {
	                    editor.setAsText(propsVal);
	                    value = editor.getValue();
                    }
                    // set attribute value on bean
                    attributes[n].getWriteMethod().invoke(metadata, new Object[]{value});
                }
            }
            metadata.initDatasourceParameters();
			if(logger.isInfoEnabled()) {
				logger.info(" Created JDBC Connection Pool named {},config:{}",metadata.getName(),metadata.toString());
			}
            JDBCPool jpool = SQLManager.getInstance().createPool(metadata,config!= null?config.getDatasourceConfig():null);
			if(jpool != null){
				dbStartResult.addDBStartResult(jpool.getDBName());
			}
        }
        return dbStartResult;
       
    }

 

}

