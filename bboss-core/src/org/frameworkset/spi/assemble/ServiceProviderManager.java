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
package org.frameworkset.spi.assemble;

import com.frameworkset.util.ParserError;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.CallContext;
import org.frameworkset.spi.Lifecycle;
import org.frameworkset.spi.assemble.RefID.Index;
import org.frameworkset.spi.assemble.callback.AssembleCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

/**
 * 
 * 
 * <p>
 * Title: ServiceProviderManager.java
 * </p>
 * 
 * <p>
 * Description: 服务提供者管理类
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * bboss workgroup
 * </p>
 * 
 * @Date Aug 13, 2008 9:27:36 AM
 * @author biaoping.yin
 * @version 1.0
 */
public class ServiceProviderManager implements AOPValueHandler{
    private static Logger log = LoggerFactory.getLogger(ServiceProviderManager.class);
    /**
     * 属性引用的反向索引集合
     */
    
    public Map<String,Set> reverseAttrRefids = new LinkedHashMap<String,Set>();
    
    /**
     * 服务引用的反向索引集合
     */
    public Map<String,Set> reverseServiceRefids = new LinkedHashMap<String,Set>();
    /**
     * 属性引用标识的前缀
     */
    public static final String ATTRIBUTE_PREFIX = "attr:";
    /**
     * 服务引用标识的前缀，如果引用标识中不带前缀时默认为服务引用
     */
    public static final String SERVICE_PREFIX = "service:";

//    private static ServiceProviderManager serviceProviderManager;
    public static String defaultConfigFile = "manager-provider.xml";
    
    public static String seriralConfigFile = "org/frameworkset/soa/serialconf.xml";

    private Map<String,Pro> properties = new LinkedHashMap<String,Pro>();
    public String getKeysString(){
    	StringBuilder stringBuilder = new StringBuilder();
		getKeysString(  stringBuilder );
    	return stringBuilder.toString();
	}

	public void getKeysString(StringBuilder stringBuilder ){
    	boolean first = true;
		if(properties != null && properties.size() > 0){
			for(String key:properties.keySet()){
				if(!first)
					stringBuilder.append(",");
				else
					first = false;
				stringBuilder.append(key);
			}
		}
	}

    /**
     * 对已经分析过的配置文件进行记录，避免重复分析配置文件
     */
    private Map parsedList;
    private static final Object trace = new Object();

    /**
     * 管理服务配置文件跟踪器列表,顶级文件 List<LinkConfigFile>
     */
    private List<LinkConfigFile> traceFiles;
    
     
    /**
     * 根据id建立的providerManager索引表 Map<managerid,ProviderManagerInfo>
     */
    private Map managers;

    private ProviderManagerInfo defaultProviderManagerInfo;

    /**
     * 需要导入的管理服务配置文件列表，这些文件的路径必须相对于 config-manager.xml文件的地址，例如：
     * 如果config-manager.xml文件的存放目录为d:/cms/WEB-INF/classes/
     * managerimport的file属性设置为hnds/hnds-dingshui.xml,那么该导入文件的真实地址为
     * d:/cms/WEB-INF/classes/hnds/hnds-dingshui.xml Map<path,LinkConfigFile>
     */
    private Map managerimports;
    private BaseApplicationContext applicationContext;
//    static {
//    	ServiceProviderManager temp = new ServiceProviderManager();
//    	temp.init();
//    	serviceProviderManager = temp;
//    }
//
//    public static ServiceProviderManager getInstance() {
//
//        return serviceProviderManager;
//    }
    
//    public void init()
//    {
////    	 try {
////
////             load(defaultConfigFile, null);
////
////         }
////
////         catch (Exception e) {
////             e.printStackTrace();
////             log.error("Load [" + defaultConfigFile + "]失败", e);
////         }
//    	this.init(defaultConfigFile);
//    }
    
    public void init(String configfile)
    {
    	init(AssembleCallback.classpathprex, "", configfile);
//    	 try {
//    		 List<ManagerImport> mis = AssembleUtil.getManagerImports(AssembleCallback.classpathprex, "", configfile);
//    		 for(int i = 0; i < mis.size(); i ++)
//    		 {
//    			 load(mis.get(i), null);
//    		 }
//    		 
//
//         }
//
//         catch (Exception e) {
//             e.printStackTrace();
//             log.error("Load [" + defaultConfigFile + "]失败", e);
//         }
    }
    
    
    public void init(String docbaseType,String docbase,String configfile)
    {
    	init(docbaseType,docbase,configfile,true);
    }
    
    public void init(String docbaseType,String docbase,InputStream instream)
    {
    	init(docbaseType,docbase,instream,true);
    }
    
    public void init(String docbaseType, String docbase, String configfile,
			URL file)
	{

		try {
	   		 
	   		 {
	   			 this.loadFromURL(file);
	   		 }
   		 

        }

        catch (Exception e) {
//            e.printStackTrace();
			if(log.isErrorEnabled())
				log.error(new StringBuilder().append("Load [" ).append( defaultConfigFile ).append( "]失败").toString(), e);
        }
		
	}
    protected ManagerImportWrapper sortManagerImports(List<ManagerImport> mis)
    {
    	ManagerImportWrapper mi = new ManagerImportWrapper();
    	mi.setImports(mis);
    	return mi;
    }
    public void init(String docbaseType,String docbase,String configfile,boolean isfile)
    {
    	 try {
    		 if(this.applicationContext.isfile())
    		 {
	    		 List<ManagerImport> mis = AssembleUtil.getManagerImports(docbaseType, docbase, configfile);
	    		 ManagerImportWrapper mi = sortManagerImports(mis);
	    		 mis = mi.getImports();
	    		 for(int i = 0; i < mis.size(); i ++)
	    		 {
	    			 load(mis.get(i), mi.getParent());
	    		 }
    		 }
    		 else
    		 {
    			 this.loadFromContent(configfile);
    		 }
    		 

         }

         catch (Exception e) {

			 if(log.isErrorEnabled())
				 log.error(new StringBuilder().append("Load [" ).append( configfile ).append( "] failed").toString(), e);
         }
    }
    
    public void init(String docbaseType,String docbase,InputStream instream,boolean isfile)
    {
    	 try {
//    		 if(this.applicationContext.isfile())
//    		 {
//	    		 List<ManagerImport> mis = AssembleUtil.getManagerImports(docbaseType, docbase, configfile);
//	    		 for(int i = 0; i < mis.size(); i ++)
//	    		 {
//	    			 load(mis.get(i), null);
//	    		 }
//    		 }
//    		 else
    		 {
    			 this.parseXML(instream);
    		 }
    		 

         }

         catch (Exception e) {
//             e.printStackTrace();
			 if(log.isErrorEnabled())
             	log.error(new StringBuilder().append("Load [" ).append( defaultConfigFile ).append( "]失败").toString(), e);
         }
    }

    public ServiceProviderManager(BaseApplicationContext applicationContext) {
        parsedList = new LinkedHashMap();
        managers = new LinkedHashMap();
        managerimports = new LinkedHashMap();
        traceFiles = new ArrayList();
        this.applicationContext = applicationContext;
    }
    private boolean serial;
	public boolean isSerial() {
		return serial;
	}

	public void setSerial(boolean serial) {
		this.serial = serial;
	}
    private String charset = "UTF-8";
    public ServiceProviderManager(BaseApplicationContext applicationContext,String charset) {
        parsedList = new LinkedHashMap();
        managers = new LinkedHashMap();
        managerimports = new LinkedHashMap();
        traceFiles = new ArrayList();
        this.applicationContext = applicationContext;
        this.charset = charset;
    }

    /**
     * 解析并加载imports配置文件列表中的包含的管理服务，parentFile是指导入这些配置文件的配置文件，便于系统 进行调试和跟踪
     * 
     * @param imports
     * @param parentFile
     */
    public void batchLoad(List imports, LinkConfigFile parentFile) {

        for (int i = 0; imports != null && i < imports.size(); i++) {
            ManagerImport mi = (ManagerImport)imports.get(i);
            List<ManagerImport> resolvermis = AssembleUtil.getManagerImports(mi);
            for(int j = 0; j < resolvermis.size(); j ++)
            {
	            try {
	            	ManagerImport resolvermi = (ManagerImport)resolvermis.get(j);
	                this.load(resolvermi, parentFile);
	            } catch (Exception e) {
	            	e.printStackTrace();

	            }
            }
        }
    }

    private boolean isParsered(String configfile) {
        return this.parsedList.containsKey(configfile);
    }

    public void load(ManagerImport managerImport, LinkConfigFile parentFile) {
        if (isParsered(managerImport.getFile()))
            return;
		parsedList.put(managerImport.getFile(), trace);
        parseXML( managerImport, parentFile);

    }
    
    public void loadFromContent(String content) {
        
        parseXML( content);
        

    }
 public void loadFromURL(URL content) {
        
        parseXML( content);
        

    }
	private PropertiesContainer propertiesContainer = null;

	public PropertiesContainer getPropertiesContainer() {
		return propertiesContainer;
	}

	private void parseXML(ManagerImport managerImport, LinkConfigFile parentFile) {

    	String configFile = managerImport.getRealPath();
        /* CHANGED TO USE JAXP */

        String url = configFile;
        boolean isfile = false;

		try {
        	if(managerImport.isClasspathBase())
        	{
	            URL confURL = ServiceProviderManager.class.getClassLoader().getResource(configFile);
	            if (confURL == null)
	                confURL = ServiceProviderManager.class.getClassLoader().getResource("/" + configFile);
	
	            if (confURL == null)
	                confURL = getTCL().getResource(configFile);
	            if (confURL == null)
	                confURL = getTCL().getResource("/" + configFile);
	            if (confURL == null)
	                confURL = ClassLoader.getSystemResource(configFile);
	            if (confURL == null)
	                confURL = ClassLoader.getSystemResource("/" + configFile);
	
	            if (confURL == null) {
	                url = System.getProperty("user.dir");
	                url += "/" + configFile;
	                File f = new File(url);
	                if(!f.exists());
	                {
	                	url = configFile;
	                	
	                	
	                }
	                isfile = true;
	                	
	            } else {
	                url = confURL.toString();
	            }
        	}
        	else
        	{
        		url = managerImport.getRealPath();
        	}
            LinkConfigFile linkconfigFile = new LinkConfigFile(url, managerImport.getFile(), parentFile);

            ProviderParser handler = _buildProviderParser(url,linkconfigFile);//new ProviderParser(this.getApplicationContext(),url, linkconfigFile);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
            if(managerImport.isClasspathBase())
            {
            	if(!isfile) {
					parser.parse(url, handler);

				}
            	else
            	{
            		File f = new File(url);
            		if(f.exists())
            			parser.parse(new File(url), handler);
            		else
            		{
            			if(log.isDebugEnabled())
            			 log.debug(new StringBuilder().append("Ignore load IOC xml file[" ).append( url).append( "]:file do not exist.").toString());
            		}
            	}
            }
            else
            {
            	File f = new File(url);
        		if(f.exists()) {
					parser.parse(new File(url), handler);

				}
        		else
        		{
        			if(log.isDebugEnabled())
        				log.debug(new StringBuilder().append("Ignore load IOC xml file[" ).append( url).append( "]:file do not exist.").toString());
        		}
            }
			propertiesContainer  = handler.getConfigPropertiesFile();
            this.addMangers(handler.getManagers());
            this.addProperties(handler.getProperties());
            if (parentFile == null || parentFile.isMultiRoot())
                this.traceFiles.add(linkconfigFile);
            else
                parentFile.addLinkConfigFile(linkconfigFile);
            linkconfigFile.setMgrServices(handler.getManagers());
            linkconfigFile.setProperties(handler.getProperties());

            linkconfigFile.setConfigPropertiesFile(propertiesContainer);
            this.managerimports.put(linkconfigFile.getIdentity(), linkconfigFile);
            if(handler.getMangerimports() != null && handler.getMangerimports().size() > 0)
            	this.batchLoad(handler.getMangerimports(), linkconfigFile);

        } catch (Exception e) {
        	if(configFile.equals(defaultConfigFile) || configFile.equals(seriralConfigFile))
        	{
//        		log.debug("从文件[" + configFile+ "]装载管理服务失败，请检查文件是否存在，或者是否被正确定义。");
				String message = 	new StringBuilder().append("从文件[" ).append( configFile).append( "]装载管理服务失败，请检查文件是否存在，或者是否被正确定义。").toString();
				if(log.isDebugEnabled())
					log.debug(message,e);
				if(applicationContext != null) {
					ParserError parserError = new ParserError();
					parserError.setMessage(message);
					parserError.setException(e);
					this.applicationContext.setParserError(parserError);
				}
        	}
        	else if (parentFile == null) {
				String message = 	new StringBuilder().append("从文件[" ).append( url ).append( "]装载管理服务失败，请检查文件是否存在，或者是否被正确定义。").toString();
				if(log.isErrorEnabled())
					log.error(message,e);
				if(applicationContext != null) {
					ParserError parserError = new ParserError();
					parserError.setMessage(message);
					parserError.setException(e);
					this.applicationContext.setParserError(parserError);
				}
                throw new RuntimeException(message,e);
            } else {
				String message = 	new StringBuilder().append("从文件[" ).append( parentFile ).append( "@" ).append( url ).append( "]装载管理服务失败，请检查文件是否存在，或者是否被正确定义。").toString();
				if(log.isErrorEnabled())
					log.error(message,e);
				if(applicationContext != null) {
					ParserError parserError = new ParserError();
					parserError.setMessage(message);
					parserError.setException(e);
					this.applicationContext.setParserError(parserError);
				}
				throw new RuntimeException(message,e);
//            	 throw new RuntimeException("从文件[" + parentFile + "@" + url + "]装载管理服务失败，请检查文件是否存在，或者是否被正确定义。",e);
            }
            
        }
		finally {
			if(propertiesContainer != null){
				propertiesContainer.afterLoaded(applicationContext);
			}
		}

    }
    
    
    private void parseXML(String content) {
    	InputSource is  = null;
    	
    	Reader reader = null;
    		try
    		{
            ProviderParser handler = _buildProviderParser();
            handler.setSerial(this.serial);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setValidating(false);
            
            SAXParser parser = factory.newSAXParser();        
//            sr = new ByteArrayInputStream(content.getBytes(this.charset));
//            parser.parse(sr, handler); 
            reader = new StringReader(content);
            is = new InputSource(reader);
            
            parser.parse(is, handler);
            
            this.addProperties(handler.getProperties());
            handler = null;
        } catch (Exception e) {
//            log.error("解析报文内容出错[" + content + "]，请检查报文是否被正确定义。",e);
				String message = 	new StringBuilder().append("解析报文内容出错[" ).append( content ).append( "]，请检查报文是否被正确定义。").toString();
				if(log.isErrorEnabled())
					log.error(message,e);
				if(applicationContext != null) {
					ParserError parserError = new ParserError();
					parserError.setMessage(message);
					parserError.setException(e);
					this.applicationContext.setParserError(parserError);
				}
        }
        finally
        {
        	if(reader != null) {
				try {
					reader.close();
					reader = null;
				} catch (Exception e2) {
					
				}
			}
        	if(is != null) {
				is = null;
			}
        }

    }
    
    protected ProviderParser _buildProviderParser()
    {
    	return new ProviderParser(this.getApplicationContext());
    }
    
    protected ProviderParser _buildProviderParser(String url,LinkConfigFile linkconfigFile)
    {
    	return new ProviderParser(this.getApplicationContext(),url, linkconfigFile);
    }
    private void parseXML(InputStream in) {
//    	InputStream in = null;
//    	ByteArrayInputStream sr = null;
    		try
    		{
            ProviderParser handler = _buildProviderParser();
            handler.setSerial(this.serial);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setValidating(false);
            
            SAXParser parser = factory.newSAXParser();        
//            sr = new ByteArrayInputStream(content.getBytes());
//            in = new java.io.BufferedInputStream(sr);
//            parser.parse(in, handler);    
            parser.parse(in, handler);    
            
            this.addProperties(handler.getProperties());
            handler = null;
        } catch (Exception e) {
//            log.error("解析报文内容出错[" + in + "]，请检查报文是否被正确定义。",e);
				String message = 	new StringBuilder().append("解析报文内容出错[" ).append( in ).append( "]，请检查报文是否被正确定义。").toString();
				if(log.isErrorEnabled())
					log.error(message,e);
				if(applicationContext != null) {
					ParserError parserError = new ParserError();
					parserError.setMessage(message);
					parserError.setException(e);
					this.applicationContext.setParserError(parserError);
				}
        }
        finally
        {
//        	if(sr != null) {
//				try {
//					sr.close();
//				} catch (Exception e2) {
//					
//				}
//			}
        	if(in != null) {
				try {
					in.close();
				} catch (Exception e2) {
					
				}
			}
        }

    }
    

    private void parseXML(URL contentFile) {
//    	InputStream in = null;
//    	ByteArrayInputStream sr = null;
		String xmlFilePath = contentFile.toString();
		try
		{
            ProviderParser handler = _buildProviderParser();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setValidating(false);
            
            SAXParser parser = factory.newSAXParser();
        
//            in = new java.io.BufferedInputStream(new java.io.FileInputStream(contentFile));
            parser.parse(xmlFilePath, handler);
            
            this.addProperties(handler.getProperties());
            handler = null;
        } catch (Exception e) {
    		String message = 	new StringBuilder().append("解析报文内容出错[" ).append( xmlFilePath ).append( "]，请检查报文是否被正确定义。").toString();
    		if(log.isErrorEnabled())
            	log.error(message,e);
			if(applicationContext != null) {
				ParserError parserError = new ParserError();
				parserError.setMessage(message);
				parserError.setException(e);
				this.applicationContext.setParserError(parserError);
			}
        }
        finally
        {
        	
        }

    }

    public void addTopLevel(LinkConfigFile linkconfigFile) {
        this.traceFiles.add(linkconfigFile);
        this.managerimports.put(linkconfigFile.getIdentity(), linkconfigFile);
    }

    private static ClassLoader getTCL() throws IllegalAccessException, InvocationTargetException {
        Method method = null;
        try {
            method = (java.lang.Thread.class).getMethod("getContextClassLoader");
        } catch (NoSuchMethodException e) {
            return null;
        }
        return (ClassLoader)method.invoke(Thread.currentThread());
    }

    public ProviderManagerInfo getProviderManagerInfo(String providerManagerType) {
        return (ProviderManagerInfo)this.managers.get(providerManagerType);

    }

    public void addMangers(Map managers) {
        if (managers != null && managers.size() > 0)
            this.managers.putAll(managers);

    }

    public void addProperties(Map<String,Pro> properties) {
        if (properties != null && properties.size() > 0)
            this.properties.putAll(properties);

    }

    public ProviderManagerInfo getDefaultProviderManagerInfo() {
        return defaultProviderManagerInfo;
    }

    public void setDefaultProviderManagerInfo(ProviderManagerInfo defaultProviderManagerInfo) {
        this.defaultProviderManagerInfo = defaultProviderManagerInfo;
    }

    private Map mutuxMangers;

    public Map getManagers() {
        if (mutuxMangers == null)
            mutuxMangers = Collections.unmodifiableMap(managers);
        return mutuxMangers;
    }

    public void addProviderManagerInfo(ProviderManagerInfo provider) {
        this.managers.put(provider.getId(), provider);
    }

    /**
     * 返回一级配置文件清单
     * 
     * @return
     */
    public List getTraceFiles() {
        return traceFiles;
    }

    /**
     * 返回给定标识的管理服务配置文件信息
     * 
     * @param id
     * @return
     */
    public LinkConfigFile getLinkConfigFile(String id) {
        return (LinkConfigFile)this.managerimports.get(id);
    }
    
    public Set<String> getPropertyKeys()
    {
    	return this.properties.keySet();
    }

    public boolean isEmpty(){
    	return this.properties == null || this.properties.size() == 0;
	}

    public String getProperty(String name) {
        Pro pro = this.properties.get(name);
        if(pro == null)
            return null;
        String value = (String)pro.getValue();
//        if(value == null)
//            throw new AssembleException("配置文件没有指定属性[" + name + "]！");
        return value;
    }
    
    public Object getObjectProperty(String name) {
        return getObjectProperty(name,null);
//        if(value == null)
//            throw new AssembleException("配置文件没有指定属性[" + name + "]！");
        
    }
    
    public Object getObjectProperty(String name,Object defaultValue) {
        Pro pro = this.properties.get(name);
        if(pro == null)
        {
            log.debug(new StringBuilder().append("配置文件[").append(applicationContext.getConfigfile() ).append("]中没有指定属性[" ).append( name ).append( "]！返回缺省值" ).append( defaultValue).toString());
            return defaultValue;
        }
        
        return pro.getObject(defaultValue);
            
    }
    
    public ProSet getSetProperty(String name) {
        Pro value = this.properties.get(name);
        if (value == null) {
            log.debug(new StringBuilder().append("配置文件[").append(applicationContext.getConfigfile() ).append("]中没有指定属性[" ).append( name ).append( "]！").toString());
            return null;
        }
       return value.getSet(); 
//        if(value == null)
//            throw new AssembleException("配置文件没有指定属性[" + name + "]！");
        
    }
    
    public ProSet getSetProperty(String name,ProSet defaultValue) {
        Pro value = this.properties.get(name);
        if (value == null) {
            log.debug(new StringBuilder().append("配置文件[").append(applicationContext.getConfigfile() ).append("]中没有指定属性[" ).append( name ).append( "]！").toString());
            return defaultValue;
        }
       return value.getSet(defaultValue);
//        if(value == null)
//            throw new AssembleException("配置文件没有指定属性[" + name + "]！");
        
    }
    
    public ProList getListProperty(String name) {
        Pro value = this.properties.get(name);
        if (value == null) {
            
            log.debug(new StringBuilder().append("配置文件[").append(applicationContext.getConfigfile() ).append("]中没有指定属性[" ).append( name ).append( "]！").toString());
            return null;
        }
       return value.getList();
//        if(value == null)
//            throw new AssembleException("配置文件没有指定属性[" + name + "]！");
        
    }
    
    public ProList getListProperty(String name,ProList defaultValue) {
        Pro value = this.properties.get(name);
        if (value == null) {
            log.debug(new StringBuilder().append("配置文件[").append(applicationContext.getConfigfile() ).append("]中没有指定属性[" ).append( name ).append( "]！").toString());
            return defaultValue;
        }
       return value.getList(defaultValue);
//        if(value == null)
//            throw new AssembleException("配置文件没有指定属性[" + name + "]！");
        
    }
    
    
    public ProMap getMapProperty(String name) {
        Pro value = this.properties.get(name);
        if (value == null) {
            log.debug(new StringBuilder().append("配置文件[").append(applicationContext.getConfigfile() ).append("]中没有指定属性[" ).append( name ).append( "]！").toString());
            return null;
        }
       return value.getMap();        
//        if(value == null)
//            throw new AssembleException("配置文件没有指定属性[" + name + "]！");
        
    }
    
    public ProProperties getProProperties(String name) {
        Pro value = this.properties.get(name);
        if (value == null) {
            log.debug(new StringBuilder().append("配置文件[").append(applicationContext.getConfigfile() ).append("]中没有指定属性[" ).append( name ).append( "]！").toString());
            return null;
        }
       return value.getProProperties();        
//        if(value == null)
//            throw new AssembleException("配置文件没有指定属性[" + name + "]！");
        
    }
    
    public ProMap getMapProperty(String name,ProMap defaultValue) {
        Pro value = this.properties.get(name);
        if (value == null) {
            log.debug(new StringBuilder().append("配置文件[").append(applicationContext.getConfigfile() ).append("]中没有指定属性[" ).append( name ).append( "]！").toString());
            return defaultValue;
        }
       return value.getMap();
    }
    
    
    public ProArray getArrayProperty(String name) {
        Pro value = this.properties.get(name);
        if (value == null) {
            log.debug(new StringBuilder().append("配置文件[").append(applicationContext.getConfigfile() ).append("]中没有指定属性[" ).append( name ).append( "]！").toString());
            return null;
        }
       return value.getArray();        
//        if(value == null)
//            throw new AssembleException("配置文件没有指定属性[" + name + "]！");
        
    }
    
    public ProArray getArrayProperty(String name,ProArray defaultValue) {
        Pro value = this.properties.get(name);
        if (value == null) {
            log.debug(new StringBuilder().append("配置文件[").append(applicationContext.getConfigfile() ).append("]中没有指定属性[" ).append( name ).append( "]！").toString());
            return defaultValue;
        }
       return value.getArray(defaultValue);
    }

    public int getIntProperty(String name) {
        Pro value = this.properties.get(name);
        if (value == null) {
            throw new AssembleException(new StringBuilder().append("配置文件[").append(applicationContext.getConfigfile() ).append("]中没有指定属性[" ).append( name ).append( "]！").toString());
        }
       return value.getInt();
        
        
            
        
    }
    
    public long getLongProperty(String name) {
        Pro value = this.properties.get(name);
        if (value == null) {
            throw new AssembleException(new StringBuilder().append("配置文件[").append(applicationContext.getConfigfile() ).append("]中没有指定属性[" ).append( name ).append( "]！").toString());
        }
       return value.getLong();
        
        
            
        
    }
    
    public long getLongProperty(String name,long defaultvalue) {
        Pro value = this.properties.get(name);
        if (value == null) {
            return defaultvalue;
        }
       return value.getLong(defaultvalue);
        
        
            
        
    }

    public boolean getBooleanProperty(String name) {
        Pro value = this.properties.get(name);
        if (value == null) {
            throw new AssembleException(new StringBuilder().append("配置文件[").append(applicationContext.getConfigfile() ).append("]中没有指定属性[" ).append( name ).append( "]！").toString());
        }
       return value.getBoolean();
    }
    
    public Pro getPropertyBean(String name)
    {
        Pro pro = this.properties.get(name);
        if(pro == null){
        	if(log.isDebugEnabled())
            	log.debug(new StringBuilder().append("配置文件[").append(applicationContext.getConfigfile() ).append("]中没有指定属性[" ).append( name ).append( "]！").toString());
            return null;
        }
        return pro;
    }
    
    public String[] getBeanNamesForType(Class<Lifecycle> class1, boolean includeNonSingletons, boolean allowEagerInit) {
		// TODO Auto-generated method stub
		Set<Entry<String,Pro>> entrySet = this.properties.entrySet();
		Iterator<Entry<String, Pro>> iterator = entrySet.iterator();
		List<String> names = new ArrayList<String>();
		while(iterator.hasNext())
		{
			Entry<String, Pro> entry = iterator.next();
			Pro pro = entry.getValue();
			if(pro.isType(class1))
			{
				names.add(entry.getKey());
			}
				
		}
		return names.toArray(new String[names.size()]);
	}
    
    public String[] getDependenciesForBean(String beanName) {
		// TODO Auto-generated method stub
		return null;
	}
	public String[] getDependentBeans(String beanName) {
		// TODO Auto-generated method stub
		return null;
	}
	 
	public boolean isFactoryBean(String beanNameToRegister) {
		Pro pro = this.getPropertyBean(beanNameToRegister);
		if(pro != null)
			return pro.isFactoryBean();
		return false;
	}
	 
	public Class<?> getType(String beanNameToCheck) {
		// TODO Auto-generated method stub
		Pro pro = this.getPropertyBean(beanNameToCheck);
		if(pro != null)
			return pro.getType();
		return null;
	}
    
    /**
     * 根据引用的维度获取其对应的Pro对象
     * @param refid
     * @return
     */
    public Pro getInnerPropertyBean(RefID refid,String strrefid)
    {	
//    	String name = refid.getName();
    	Pro pro = null;
//    	if(strrefid != null && strrefid.equals("test1->testarray[0]->test2"))
//    	{
//    		System.out.println();
//    	}
    	boolean firsted = true;
    	Pro temp = null;
    	do
    	{
    		if(firsted)
    		{
    			pro = this.properties.get(refid.getName());
    			firsted = false;
    		}
    		else
    		{
    			if(pro.isRefereced())//如果任然是一个引用关系，继续查找到实际的对象pro，然后在继续下一个环节的引用关系查找
    			{
    				pro = getInnerPropertyBean(pro.getRefidLink(),pro.getRefid());
    			}
    			List<Pro> refs = pro.getReferences();
    			pro = null;
    			for(int i = 0; i < refs.size(); i ++)
    			{
    				temp = refs.get(i);
    				if(temp.getName().equals(refid.getName()))
					{
    					pro = temp;
    					break;
					}
    			}
    			if(pro == null)
    			{
    				if(log.isDebugEnabled())
    				 	log.debug(new StringBuilder().append("配置文件[").append(applicationContext.getConfigfile() ).append("]中没有指定属性[" ).append( strrefid ).append( "]！").toString());
    		         return null;
    			}
    		}
    		
	        List<Index> indexs = refid.getIndexs();
	        if(indexs != null && indexs.size() > 0)//内部对象：构造器，list/array/set
	        {
	        	for(int i = 0;i < indexs.size(); i ++)
	        	{
	        		Index index = indexs.get(i);
	        		if(!index.isInconstruction())
	        		{
	        			if(index.getInt_idx() >= 0)
	        			{
	        				if(pro.isList())
	        					pro = pro.getList().getPro(index.getInt_idx());
	        				else if(pro.isArray())
	        					pro = pro.getArray().getPro(index.getInt_idx());
	        				
	        				else 
	        					pro = pro.getSet().getPro(index.getInt_idx());	
	        			}
	        			else
	        			{
	        				pro = pro.getMap().getPro(index.getString_idx());	
	        			}
	        		}
	        		else
	        		{
	        			pro = (Pro)pro.getConstructorParams().get(index.getInt_idx());
	        		}
	        		
        			if(pro.isRefereced() && indexs.size() > 1)
        			{
        				pro = getInnerPropertyBean(pro.getRefidLink(),pro.getRefid());
        			}
	        		
	        	}
	        }
	        else//属性引用
	        {
	        	
	        }
	       
	        refid = refid.getNext();
    	}while(refid != null);
	        
        
        if(pro == null){
			if(log.isDebugEnabled())
				log.debug(new StringBuilder().append("配置文件[").append(applicationContext.getConfigfile() ).append("]中没有指定属性[" ).append( strrefid ).append( "]！").toString());
            return null;
        }
        return pro;
    }

    public String getProperty(String name, String defaultValue) {
        String value = getProperty(name);
        if (value == null) {
			if(log.isDebugEnabled())
				log.debug(new StringBuilder().append("配置文件[").append(applicationContext.getConfigfile() ).append("]中没有指定属性[" ).append( name ).append( "]！返回缺省值" ).append( defaultValue).toString());
            return defaultValue;
        }
        return value;
    }

    public int getIntProperty(String name, int defaultValue) {
        Pro value = this.properties.get(name);
        if (value == null) {
			if(log.isDebugEnabled())
				log.debug(new StringBuilder().append("配置文件[").append(applicationContext.getConfigfile() ).append("]中没有指定属性[" ).append( name ).append( "]！").toString());
            return defaultValue;
        }
       return value.getInt(defaultValue);
        
    }

    public boolean getBooleanProperty(String name, boolean defaultValue) {
        Pro value = this.properties.get(name);
        if (value == null) {
			if(log.isDebugEnabled())
				log.debug(new StringBuilder().append("配置文件[").append(applicationContext.getConfigfile() ).append("]中没有指定属性[" ).append( name ).append( "]！").toString());
            return defaultValue;
        }
       return value.getBoolean();
    }
//    getBeanObject
    public Object getBeanObject(CallContext context, String name, Object defaultValue)
    {
        
        Pro pro = this.properties.get(name);
        if(pro == null)
        {
			if(log.isDebugEnabled())
				log.debug(new StringBuilder().append("配置文件[").append(applicationContext.getConfigfile() ).append("]中没有指定属性[" ).append( name ).append( "]！返回缺省值" ).append( defaultValue).toString());
            return defaultValue;
        }
        
        return pro.getBeanObject(context,defaultValue);
    }

    public Object getBeanObject(CallContext context, Pro providerManagerInfo)
    {
        // TODO Auto-generated method stub
         return getBeanObject(context, providerManagerInfo,null);
    }
    
    
    public Object getBeanObject(CallContext context, Pro providerManagerInfo,Object defaultValue)
    {
        // TODO Auto-generated method stub
         return providerManagerInfo.getBeanObject(context,defaultValue);
    }

    public BaseApplicationContext getApplicationContext()
    {
        return applicationContext;
    }
    private boolean started = true;
	public synchronized void destroy() {
		_destroy();
		
	}
	
	protected void _destroy() {
		if(!started)
			return;
		started = false;
		if(managerimports != null)
			this.managerimports.clear();
		
		if(managerimports != null)
			this.managers.clear();
		if(mutuxMangers != null)
			this.mutuxMangers.clear();
		if(reverseAttrRefids != null)
			this.reverseAttrRefids.clear();
		if(properties != null)
			this.properties.clear();
		if(reverseServiceRefids != null)
			this.reverseServiceRefids.clear();
		if(traceFiles != null)
			this.traceFiles.clear();

		this.serial = false;
		
	}
	
	public boolean containsBean(String themeSourceBeanName) {
		
		return this.properties.containsKey(themeSourceBeanName);
	}

	public String[] getStringArray(String key) {
		
		 Pro value = this.properties.get(key);
	        if (value == null) {
	            throw new AssembleException(new StringBuilder().append("配置文件[").append(applicationContext.getConfigfile() ).append("]中没有指定属性[" ).append( key ).append( "]！").toString());
	        }
	       return value.getStringArray();
	}

	public String[] getStringArray(String key, String[] defaultValues) {
		 Pro value = this.properties.get(key);
	        if (value == null) {
	            return defaultValues;
	        }
	       return value.getStringArray(defaultValues);
	}
	
	public String getExternalProperty(String property)
	{
		String value = null;
		for(int i = 0;traceFiles != null &&  i < this.traceFiles.size();i ++)
		{
			LinkConfigFile f = traceFiles.get(i);
			value = f.getConfigPropertiesFile() != null?f.getConfigPropertiesFile().getPropertyFromSelf2ndSons(property):null;
			if(value != null)
				break;
		}
		return value;
	}
	public String getExternalProperty(String property,String defaultValue)
	{
		String value = null;
		for(int i = 0;traceFiles != null &&  i < this.traceFiles.size();i ++)
		{
			LinkConfigFile f = traceFiles.get(i);
			value = f.getConfigPropertiesFile() != null?f.getConfigPropertiesFile().getPropertyFromSelf2ndSons(property):null;
			if(value != null)
				break;
		}
		if(value != null)
			return value;
		else
			return defaultValue;
	}
	public Map getAllExternalProperties(){
		Map all = new HashMap();
		for(int i = 0;traceFiles != null &&  i < this.traceFiles.size();i ++)
		{
			LinkConfigFile f = traceFiles.get(i);
			Map value = f.getConfigPropertiesFile() != null?f.getConfigPropertiesFile().getAllExternalProperties():null;
			if(value != null)
				all.putAll(value);
		}
		return all;
	}
	public String getSystemEnvProperty(String property){
		String value = getExternalProperty(  property);

		if(value == null){ //Get value from jvm system propeties,just like -Dproperty=value
//			Properties pros = System.getProperties();
			value =System.getProperty(property);
			if(value == null) {
				//Get value from os env ,just like property=value in user profile
				value = System.getenv(property);
			}
		}
		return value;
	}
	public Object getExternalObjectProperty(String property,Object defaultValue)
	{
		String value = null;
		for(int i = 0;traceFiles != null &&  i < this.traceFiles.size();i ++)
		{
			LinkConfigFile f = traceFiles.get(i);
			value = f.getConfigPropertiesFile() != null?f.getConfigPropertiesFile().getPropertyFromSelf2ndSons(property):null;
			if(value != null)
				break;
		}
		if(value != null)
			return value;
		else
			return defaultValue;
	}

 
	public String getVarpre(){
		return "${";
	}
	
	public String getVarend(){
		return "}";
	}
	
	public boolean findVariableFromSelf(){
		return false;
	}
	public String getEscapePre(){
		return null;
	}
	public String getEscapeEnd(){
		return null;
	}
	public void escapeValue(String value,StringBuilder builder){
		builder.append(value);
	}
	public String getEscapeRNPre(){
		return null;
	}
	public String getEscapeRNEnd(){
		return null;
	}
	/**
	 * 处理回车换行符，
	 * @param value
	 * @return
	 */
	public void escapeRN(String value,StringBuilder builder){
		builder.append(value);
	}
}
