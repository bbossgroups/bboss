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

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.management.Attribute;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.management.RuntimeMBeanException;
import javax.management.RuntimeOperationsException;

import com.frameworkset.common.poolman.PoolManConstants;
import com.frameworkset.common.poolman.util.SQLManager;

public class JMXPoolDeployer extends BaseTableManager implements PoolManDeployer, Runnable,Serializable {

    private static MBeanServer server;

    /**
     * added paramater server which is getted from application server such
     * as tomcat,weblogic,websphere etc.
     */
    public JMXPoolDeployer(MBeanServer server) {
        super();
        if(server != null)
        {
            this.server = server;
        }
        // add VM shutdown event handler
        try {
            // use reflection and catch the Exception to allow PoolMan to work with 1.2 VM's
            Class r = Runtime.getRuntime().getClass();
            java.lang.reflect.Method m =
                    r.getDeclaredMethod("addShutdownHook", new Class[]{Thread.class});
            m.invoke(Runtime.getRuntime(), new Object[]{new Thread(this)});
        } catch (Exception e) {
        }
    }

    /** This Runnable is not started until a VM shutdown event is fired. */
    public void run() {
        try {
        	super.updateTableInfo();
            // Stop Pools
            ObjectName queryName = new ObjectName("*:*");
            Set allservices = server.queryNames(queryName, null);
            for (Iterator iter = allservices.iterator(); iter.hasNext();) {
                ObjectName objn = (ObjectName) iter.next();
                try {
                    server.invoke(objn, "stop", null, null);
                } catch (Exception re1) {
                }
            }

            // Externalize State
            // TO DO...

        } catch (Exception e) {
            System.out.println("Unable to properly shutdown: " + e);
        }
    }

    /** Load DataSource info from XML and create a Service for each entry set. */
    public void deployConfiguration(PoolManConfiguration config) throws Exception {

        try {

            // server
            if(server == null)
                JMXPoolDeployer.server = MBeanServerFactory.createMBeanServer();

            // classloader
            SQLManager manager = SQLManager.getInstance();
            ObjectName loaderName = new ObjectName("DefaultDomain:service=JMXPatchedClassLoader");
            /**
             * className - The class name of the MBean to be instantiated.
			 * name - The object name of the MBean. May be null.
			 * params - An array containing the parameters of the constructor to be invoked.
			 * signature - An array containing the signature of the constructor to be invoked.
             */
            server.createMBean("com.frameworkset.common.poolman.management.JMXClassLoader", loaderName, new Object[]{ manager.getClass().getClassLoader() }, new String[]{ "java.lang.ClassLoader" });

            ArrayList datasources = config.getDataSources();
            if (datasources != null) {

                for (Iterator iter = datasources.iterator(); iter.hasNext();) {

                    // Get each set of datasource entries
                    Properties dbprops = (Properties) iter.next();

                    // Create a DataSourceServiceMBean using those entries
                    ObjectInstance datasource = addDataSourceService(dbprops, loaderName);

                    // Start the new DataSource MBean
                    try {
                        server.invoke(datasource.getObjectName(), "start", new Object[0], new String[0]);
                    } catch (RuntimeMBeanException re) {
                        Exception e = re.getTargetException();
                        throw e;
                    }
                }

            }

            // start reflective Generic object pools

            ArrayList genericPools = config.getGenericPools();
            if (genericPools != null) {

                for (Iterator iter = genericPools.iterator(); iter.hasNext();) {

                    // Get each set of datasource entries
                    Properties props = (Properties) iter.next();

                    // Create a GenericPoolServiceMBean using those entries
                    ObjectInstance pooledObject = addPooledObjectService(props, loaderName);

                    // Start the new Service
                    try {
                        server.invoke(pooledObject.getObjectName(), "start", new Object[0], new String[0]);
                    } catch (RuntimeMBeanException re) {
                        Exception e = re.getTargetException();
                        System.err.println(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            // start admin agent
            Properties adminProps = config.getAdminProperties();
            if (adminProps != null) {
                try {
                    Class agentType = Class.forName(adminProps.getProperty("class", "com.sun.jdmk.comm.HtmlAdaptorServer"));
                    Object agentInstance = agentType.newInstance();
                    String compositeName = new String(adminProps.getProperty("name", "Adaptor:name=html") + ",port=" + adminProps.getProperty("port", "8082"));
                    ObjectName agentName = new ObjectName(compositeName);
                    server.registerMBean(agentInstance, agentName);
                    java.lang.reflect.Method startMethod = agentType.getMethod("start", null);
                    startMethod.invoke(agentInstance, null);
                } catch (Exception e) {
                    System.err.println("ERROR: Could not create the Admin Agent: " + e);
                    e.printStackTrace();
                }
            }

        } catch (RuntimeOperationsException e) {
            throw e.getTargetException();
        } catch (MBeanException e) {
            throw e.getTargetException();
        } catch (RuntimeMBeanException e) {
            throw e.getTargetException();
        } catch (javax.management.RuntimeErrorException e) {
            throw e.getTargetError();
        } catch (Exception e) {
            throw e;
        }
    }

    private ObjectInstance addDataSourceService(Properties props, ObjectName loaderName) throws Exception {

        String dbname = null;
        try {
            dbname = (String) props.get("dbname");
        } catch (NullPointerException npe) {
            throw new Exception("No name found for database!");
        }

        // instance will have JNDI name of getName() and
        // ObjectName of getObjectName()
        ObjectName objectName = new ObjectName("DefaultDomain:service=PoolManDataSource-" + dbname);

        // class is same for all DataSource services
        String className = PoolManConstants.DATASOURCE_SVC_CLASSNAME;

        // create the instance, this binds it to the MBeanServer
        // with no attributes yet set
        ObjectInstance instance = null;
        try {
            instance = server.createMBean(className, objectName, loaderName);
        } catch (Exception ine) {
            ine.printStackTrace();
            //System.exit(0);
        }

        // here's the new MBean's info
        MBeanInfo mbeanInfo = server.getMBeanInfo(instance.getObjectName());

        // set attributes based on properties

        MBeanAttributeInfo[] attributes = mbeanInfo.getAttributes();
        for (int n = 0; n < attributes.length; n++) {

            // get MBean attribute name
            String attrName = attributes[n].getName();

            if (props.containsKey(attrName.toLowerCase())) {

                // get value in props
                String propsVal = props.getProperty(attrName.toLowerCase());

                // manage data type using java.bean package
                // so far we only need boolean, int, and String
                String datatype = attributes[n].getType();
                Class type;
                if (datatype.equals("int"))
                    type = Integer.TYPE;
                else if (datatype.equals("boolean"))
                    type = Boolean.TYPE;
                else
                    type = Class.forName(datatype);

                // create attribute value of correct type
                PropertyEditor editor = PropertyEditorManager.findEditor(type);
                editor.setAsText(propsVal);
                Object value = editor.getValue();
                Attribute attrValue = new Attribute(attrName, value);

                // set attribute value on mbean
                server.setAttribute(objectName, attrValue);

            }
        }

        return instance;
    }

    private ObjectInstance addPooledObjectService(Properties props, ObjectName loaderName) throws Exception {

        String poolName = null;
        try {
            poolName = (String) props.get("name");
        } catch (NullPointerException npe) {
            throw new Exception("No name found for ObjectPool!");
        }

        ObjectName objectName = new ObjectName("DefaultDomain:service=PoolManObjectPool-" + poolName);

        // class is same for all DataSource services
        String className = PoolManConstants.GENERIC_SVC_CLASSNAME;

        // create the instance, this binds it to the MBeanServer
        // with no attributes yet set (all set to default)
        ObjectInstance instance = server.createMBean(className, objectName, loaderName);

        // here's the new MBean's info
        MBeanInfo mbeanInfo = server.getMBeanInfo(instance.getObjectName());

        // set attributes based on properties

        MBeanAttributeInfo[] attributes = mbeanInfo.getAttributes();
        for (int n = 0; n < attributes.length; n++) {

            // get MBean attribute name
            String attrName = attributes[n].getName();

            if (props.containsKey(attrName.toLowerCase())) {

                // get value in props
                String propsVal = props.getProperty(attrName.toLowerCase());

                // manage data type using java.bean package
                // so far we only need boolean, int, and String
                String datatype = attributes[n].getType();
                Class type;
                if (datatype.equals("int"))
                    type = Integer.TYPE;
                else if (datatype.equals("boolean"))
                    type = Boolean.TYPE;
                else if (datatype.equals("float"))
                    type = Float.TYPE;
                else if (datatype.equals("long"))
                    type = Long.TYPE;
                else if (datatype.equals("double"))
                    type = Double.TYPE;
                else
                    type = Class.forName(datatype);

                // create attribute value of correct type
                PropertyEditor editor = PropertyEditorManager.findEditor(type);
                editor.setAsText(propsVal);
                Object value = editor.getValue();
                Attribute attrValue = new Attribute(attrName, value);

                // set attribute value on mbean
                server.setAttribute(objectName, attrValue);

            }

        }

        return instance;
    }

    public void removeDataSourceService(String dbname) throws Exception {

    }

	public void deployConfiguration(PoolManConfiguration config,
			Map<String, String> values) throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void deployConfiguration(PoolManConfiguration config, String dbname)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}

