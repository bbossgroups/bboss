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

// PoolMan Classes

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.frameworkset.spi.BaseApplicationContext;

import com.frameworkset.common.poolman.util.JDBCPool;
import com.frameworkset.common.poolman.util.JDBCPoolMetaData;
import com.frameworkset.common.poolman.util.SQLManager;

public class LocalPoolDeployer extends BaseTableManager implements PoolManDeployer,Serializable {
	public static boolean addShutdownHook = false;
	public static void shutdownHandle()
	{
		if(addShutdownHook)
			return;
		// add VM shutdown event handler
        try {
        	 
            // use reflection and catch the Exception to allow PoolMan to work with 1.2 VM's
            BaseApplicationContext.addShutdownHook(new Runnable(){

				public void run() {
					 try {
						 updateTableInfo();
				            SQLManager.destroy();
				           // GenericPoolManager.getInstance().destroyPools();
				        } catch (Exception e) {
				            System.out.println("Unable to properly shutdown: " + e);
				        }
					
				}
            	
            },1000);
            addShutdownHook = true;
        } catch (Exception e) {
        	addShutdownHook = true;
        }
	}
	
    public void deployConfiguration(PoolManConfiguration config) throws Exception {

        startDataSources(config.getDataSources());
//        startGenericPools(config.getGenericPools());

        // Note: there is no admin for the non-JMX PoolMan

        // add VM shutdown event handler
        shutdownHandle();
    }
    
    public void deployConfiguration(PoolManConfiguration config,String dbname) throws Exception {

        startDataSources(config.getDataSources());
//        startGenericPools(config.getGenericPools());

        
    }
    
	public void deployConfiguration(PoolManConfiguration config, Map values)
	throws Exception {
// TODO Auto-generated method stub
		startDataSource(config.getDataSources(),values);
		shutdownHandle();
	}

//    public void run() {
//        try {
//            // get SQLManager and closeAllResources
//        	
//            SQLManager.destroy();
//           // GenericPoolManager.getInstance().destroyPools();
//        } catch (Exception e) {
//            System.out.println("Unable to properly shutdown: " + e);
//        }
//    }

    private void startDataSources(ArrayList datasources) throws Exception {

        if (datasources == null)
            return;

        for (Iterator iter = datasources.iterator(); iter.hasNext();) {

            // Get each set of datasource entries
            Properties dbprops = (Properties) iter.next();

            // create the metadata object
            JDBCPoolMetaData metadata = new JDBCPoolMetaData();

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
			SQLManager.getInstance().createPool(metadata);
            //jpool.log("PoolMan Local Pool Deployer: Created JDBC Connection Pool named: " + metadata.getDbname());

        }
    }
    
    
    private void startDataSource(ArrayList datasources,Map<String,String> values) throws Exception {

        if (datasources == null )
            return;
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

            BeanInfo beanInfo = Introspector.getBeanInfo(metadata.getClass());

            // set attributes based on properties

            PropertyDescriptor[] attributes = beanInfo.getPropertyDescriptors();
            for (int n = 0; n < attributes.length; n++) {

                // get bean attribute name
                String attrName = attributes[n].getName();
                
                if (dbprops.containsKey(attrName.toLowerCase())) {

                    // get value in props
                    String propsVal = null;
                    if(!useTemplate)
                    {
                    	propsVal = dbprops.getProperty(attrName.toLowerCase());
                    }
                    else
                    {
                    	propsVal = values.get(attrName.toLowerCase());
                    	if(propsVal == null)
                    		propsVal = dbprops.getProperty(attrName.toLowerCase());
                    		
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
            JDBCPool jpool = SQLManager.getInstance().createPool(metadata);
            //jpool.log("PoolMan Local Pool Deployer: Created JDBC Connection Pool named: " + metadata.getDbname());
        }
       
    }



//    private void startGenericPools(ArrayList genericObjects) throws Exception {
//
//        if (genericObjects == null)
//            return;
//
//        for (Iterator iter = genericObjects.iterator(); iter.hasNext();) {
//
//            // Get each set of pool entries
//            Properties props = (Properties) iter.next();
//
//            GenericPoolMetaData metadata = new GenericPoolMetaData();
//
//            // set attributes based on properties
//
//            BeanInfo beanInfo = Introspector.getBeanInfo(metadata.getClass());
//
//            // set attributes based on properties
//
//            PropertyDescriptor[] attributes = beanInfo.getPropertyDescriptors();
//            for (int n = 0; n < attributes.length; n++) {
//
//                // get attribute name
//                String attrName = attributes[n].getName();
//
//                if (props.containsKey(attrName.toLowerCase())) {
//
//                    // get value in props
//                    String propsVal = props.getProperty(attrName.toLowerCase());
//
//                    Class type = attributes[n].getPropertyType();
//                    // create attribute value of correct type
//                    PropertyEditor editor = PropertyEditorManager.findEditor(type);
//                    editor.setAsText(propsVal);
//                    Object value = editor.getValue();
//
//                    attributes[n].getWriteMethod().invoke(metadata, new Object[]{value});
//                }
//            }
//
//            GenericPool gpool = GenericPoolManager.getInstance().createPool(metadata);
//            gpool.log("PoolMan Local Pool Deployer: Created Object Pool Named: " + metadata.getName());
//        }
//    }

}

