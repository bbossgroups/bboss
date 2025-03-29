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

import com.frameworkset.common.poolman.PoolManConstants;
import com.frameworkset.util.SimpleStringUtil;
import org.frameworkset.spi.assemble.PropertiesContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;

/**
 * SAXParser used by Configurator to parse the
 * poolman.xml file. It returns Collections of
 * generic pool properties, JDBC pool properties,
 * and admin-agent properties.
 */
public class ConfigParser extends DefaultHandler{
	private static Logger log = LoggerFactory.getLogger(ConfigParser.class) ;
    private ArrayList dbProps;
    private ArrayList genericProps;
    private Properties adminProps;
    private boolean jmxManagement = PoolManConstants.DEFAULT_USE_JMX;
    private String currentSet;
    private String currentName;
    private StringBuilder currentValue = new StringBuilder();
    private String file;
    private String[] filterdbname = null;
    private String interceptor = "com.frameworkset.common.poolman.interceptor.DummyInterceptor";
    private String currentdbtype ;
    private String dbnamespace;

	public String getRefreshinterval() {
		return refreshinterval;
	}

	private String refreshinterval;


    protected PropertiesContainer configPropertiesFile;
    protected String sqlMappingDir;
    /**
     * 用户自定义的适配器
     */
    private Map<String,String> adaptors = new HashMap<String,String>(); 
    public ConfigParser(String file,String dbnamespace,String[] filterdbname) {
        this.dbProps = new ArrayList();
        this.genericProps = new ArrayList();
        this.file = file;
        this.filterdbname = filterdbname;
        this.dbnamespace = dbnamespace;
    }

	public String getSqlMappingDir() {
		return sqlMappingDir;
	}

	public ArrayList getDataSourceProperties() {
    	if(this.filterdbname != null && filterdbname.length > 0)
    	{
    		this.dbProps.clear();
    		if(this.filterpros != null && this.filterpros.size() > 0)
    			this.dbProps.addAll(this.filterpros);
    	}
        return this.dbProps;
    }

    public ArrayList getGenericProperties() {
        return this.genericProps;
    }

    public Properties getAdminProps() {
        return this.adminProps;
    }

    public boolean isManagementJMX() {
        return this.jmxManagement;
    }
    
    public void startElement(String s1, String s2,String name,Attributes attributes) {

        currentValue.delete(0, currentValue.length());
        this.currentName = name;
        
    		
        if (name.toLowerCase().equals("datasource")) {
            this.currentSet = "datasource";
            Properties properties = new Properties();
            String external = SimpleStringUtil.replaceNull(attributes.getValue("external"),"false");
            properties.put("external",external);
            dbProps.add(properties);
        }
        else if(name.equals("config"))
        {
        	if(this.configPropertiesFile == null)
        		configPropertiesFile = new PropertiesContainer();
        	String file = attributes.getValue("file");
        	if(file != null)
        		this.configPropertiesFile.addConfigPropertiesFile(file);
        }

        else if (name.toLowerCase().equals("objectpool")) {
            this.currentSet = "generic";
            genericProps.add(new Properties());
        }
        else if (name.toLowerCase().equals("admin-agent")) {
            this.currentSet = "admin-agent";
            this.adminProps = new Properties();
        }
        else if (name.toLowerCase().equals("management-mode")) {
            this.currentSet = "management-mode";
        }
        else if (name.equals("poolman") || name.equals("MLET")) {
            this.currentSet = "ignorable";
        }
        else if (name.toLowerCase().equals("jndi_principal")) {
            this.currentSet = "jndi_principal";
        }
        else if (name.equals("jndi_credentials") ) {
            this.currentSet = "jndi_credentials";
        }
        else if (name.toLowerCase().equals("adaptor")) {
            this.currentSet = "adaptor";
            this.currentdbtype = attributes.getValue("dbtype");
        }
       
        else if(!name.equals("dbname") && !name.equals("loadmetadata") &&         
        		!name.equals("jndiName") &&                
        		!name.equals("driver") &&                  
        		!name.equals("url") &&                 
        		!name.equals("username") &&               
        		!name.equals("password") &&               
        		!name.equals("txIsolationLevel") &&        
        		!name.equals("nativeResults") &&        
        		!name.equals("poolPreparedStatements") &&  
        		!name.equals("initialConnections") &&     
        		!name.equals("minimumSize") &&              
        		!name.equals("maximumSize") &&              
        		!name.equals("maximumSoft") &&              
        		!name.equals("removeAbandoned") &&       
        		!name.equals("userTimeout") &&             
        		!name.equals("logAbandoned") &&          
        		!name.equals("readOnly") &&
                !name.equals("timeBetweenEvictionRunsMillis") &&
                !name.equals("numTestsPerEvictionRun") &&
                !name.equals("minEvictableIdleTimeMilli") &&
        		!name.equals("skimmerFrequency") &&      
        		!name.equals("connectionTimeout") &&       
        		!name.equals("shrinkBy") &&        
        		!name.equals("testWhileidle") &&          
        		!name.equals("keygenerate") &&       
        		!name.equals("maxWait") &&                
        		!name.equals("validationQuery")&&
        		!name.equals("autoprimarykey")&&
        		!name.equals("cachequerymetadata")&&
        		!name.equals("showsql") &&
        		!name.equals("externaljndiName")&&
        		!name.equals("enablejta") &&
        		!name.equals("usepool") &&
        		!name.equals("encryptdbinfo") &&
        		!name.equals("datasourceFile") && !name.equals("queryfetchsize")&&!name.equals("config")
				&& !name.equals("needtableinfo") && !name.equals("refreshinterval") && !name.equals("dbInfoEncryptClass")  && !name.equals("columnNameMapping") && !name.equals("sqlMappingDir"))
            
        {
        	if(log.isDebugEnabled())
        		log.debug("解析文件时[" + this.file + "]遇到元素[" + name + "]，忽略处理。");
        }

    }

    public void characters(char[] ch, int start, int length) {
        currentValue.append(ch, start, length);
    }
    
    private List<Properties> filterpros = null;
    private boolean contain(String newdbname)
    {
    	if(filterpros == null || filterpros.size() == 0)
    		return false;
    	for(Properties p:filterpros)
    	{
    		String dbname = (String)p.get("dbname");
    		if(dbname.equals(newdbname))
    			return true;
    	}
    	return false;
    	
    }
    public void endElement(String s1,String s2,String name) {
        if (this.currentSet.equals("datasource")) {
            Properties p = (Properties) dbProps.get(dbProps.size() - 1);
            if(name.toLowerCase().equals("dbname"))
            {
            	String dbname = currentValue.toString().trim();
            	String temp = dbname;
            	if(this.dbnamespace != null && !this.dbnamespace.equals(""))
	   			 {
            		dbname = dbnamespace + ":" + currentValue.toString().trim();
	   			 }
            	p.put(name.toLowerCase(), dbname);
            	
            	if(this.filterdbname !=null && this.filterdbname.length > 0)
            	{
	            	for(String _dbname:filterdbname)
	            	{
	            		if(temp.equals(_dbname) )
	            		{
	            			if(filterpros == null)
	            				filterpros = new ArrayList<Properties>();
	            			filterpros.add(p);
	            			break;
	            		}
	            	}
            	}
            	
            }
            else
            {
            	String value = null;
            	if(configPropertiesFile != null)
            		value = this.configPropertiesFile.evalValue((List)null,currentValue.toString().trim(),null);
            	else
            	{
            		value = currentValue.toString().trim();
            	}
            	if(!name.equals("password"))
            		p.put(name.toLowerCase(), value.trim());
            	else {
                    if(name.equals("skimmerFrequency"))
                    {
                        log.warn("skimmerFrequency is Deprecated,use timeBetweenEvictionRunsMillis.");
                        name = "timeBetweenEvictionRunsMillis";
                    }
                    else if(name.equals("connectionTimeout"))
                    {
                        log.warn("connectionTimeout is Deprecated,use minEvictableIdleTimeMilli.");
                        name = "minEvictableIdleTimeMilli";
                    }
                    else if(name.equals("shrinkBy"))
                    {
                        log.warn("shrinkBy is Deprecated,use numTestsPerEvictionRun.");
                        name = "numTestsPerEvictionRun";
                    } 
                    p.put(name.toLowerCase(), value);
                }
            }
        }
        else if (this.currentSet.equals("generic")) {
        	
            Properties p = (Properties) genericProps.get(genericProps.size() - 1);
            p.put(name.toLowerCase(), currentValue.toString().trim());
           
        }
        else if (this.currentSet.equals("admin-agent")) {
            adminProps.setProperty(name.toLowerCase(), currentValue.toString().trim());
        }
        else if (this.currentSet.equals("management-mode")) {
            if (currentValue.toString().toLowerCase().trim().equals("jmx"))
                this.jmxManagement = true;
        }
        else if (this.currentSet.equals("jndi_principal")) {
            if (currentValue.toString().trim().length() > 0)
            	PoolManConfiguration.jndi_principal = currentValue.toString();
        }
        else if (this.currentSet.equals("jndi_credentials")) {
        	if (currentValue.toString().trim().length() > 0)
            	PoolManConfiguration.jndi_credentials = currentValue.toString();
        }
        
        else if (this.currentSet.equals("adaptor")) {
        	if (currentValue.toString().trim().length() > 0)
        	{
        		if(currentdbtype == null || currentdbtype.equals(""))
        		{
        			if(log.isInfoEnabled())
						log.info("ignoe adaptor["+currentValue+"],没有指定dbtype。");
        		}
        		else
        		{
        			this.adaptors.put(this.currentdbtype, currentValue.toString().trim());
        			currentdbtype = null;
        		}
        		
        	}
        }
        else if(name.equals("needtableinfo")){
        	PoolManConfiguration.needtableinfo = currentValue.toString().trim().equals("true");
        }
        else if(name.equals("refreshinterval")){
        	String refresh = currentValue.toString().trim();
        	try {
				long temp = Long.parseLong(refresh);
				PoolManConfiguration.setRefresh_interval(temp);
			}
        	catch (Exception e){
				if(log.isInfoEnabled())
					log.info("refreshinterval:"+refreshinterval + "必须是long值",e);
			}
		}
//		else if(name.equals("dbInfoEncryptClass")){
//			String dbInfoEncryptclass = currentValue.toString().trim();
//			 if(!dbInfoEncryptclass.equals(""))
//				PoolManConfiguration.setDbInfoEncryptclass(dbInfoEncryptclass);
//
//		}
		else if(name.equals("sqlMappingDir")){
			String sqlMappingDir = currentValue.toString().trim();
			if(!sqlMappingDir.equals(""))
				PoolManConfiguration.setSqlMappingDir(sqlMappingDir);

		}

		else if(name.equals("columnNameMapping")){
			String columnNameMapping = currentValue.toString().trim();
			try {
			if(!columnNameMapping.equals("") ) {
				boolean c = Boolean.parseBoolean(columnNameMapping);
				PoolManConfiguration.setColumnNameMapping(c);
			}
			}
			catch (Exception e){
				if(log.isInfoEnabled())
					log.info("columnNameMapping:"+columnNameMapping + "必须是boolean值",e);
			}
		}

        
        this.currentValue.delete(0, this.currentValue.length());
    }

   

	/**
	 * @return the adaptors
	 */
	public Map<String, String> getAdaptors() {
		return adaptors;
	}



	/**
	 * @param adaptors the adaptors to set
	 */
	public void setAdaptors(Map<String, String> adaptors) {
		this.adaptors = adaptors;
	}



	/**
	 * @return the interceptor
	 */
	public String getInterceptor() {
		return interceptor;
	}



	/**
	 * @param interceptor the interceptor to set
	 */
	public void setInterceptor(String interceptor) {
		this.interceptor = interceptor;
	}
}





