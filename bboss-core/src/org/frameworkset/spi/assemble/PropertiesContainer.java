package org.frameworkset.spi.assemble;

import com.frameworkset.util.SimpleStringUtil;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.assemble.plugin.PropertiesFilePlugin;
import org.frameworkset.util.io.ClassPathResource;
import org.frameworkset.util.tokenizer.TextGrammarParser;
import org.frameworkset.util.tokenizer.TextGrammarParser.GrammarToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class PropertiesContainer implements GetProperties{
    protected List<String> configPropertiesFiles;
    protected Properties allProperties ;
    protected Properties sonAndParentProperties ;
    private static Logger log = LoggerFactory.getLogger(PropertiesContainer.class);
    public PropertiesContainer(){

	}
    public void addConfigPropertiesFile(String configPropertiesFile,LinkConfigFile linkfile)
    {
    	if(configPropertiesFiles == null)
    	{
    		configPropertiesFiles = new ArrayList<String>();
    		
    	}
    	if(allProperties  == null)
    		allProperties = new Properties();

    	String[] configPropertiesFiles = configPropertiesFile.split(",");//属性文件可以配置多个，每个用逗号分隔
		for(String file_:configPropertiesFiles) {
			this.configPropertiesFiles.add(file_);
			evalfile(file_, linkfile);
		}
    	if(linkfile != null)
    		loopback(linkfile);
    	
    }

    public void addAll(Map properties){
		if(configPropertiesFiles == null)
		{
			configPropertiesFiles = new ArrayList<String>();

		}
		if(allProperties  == null)
			allProperties = new Properties();
		if (properties != null && properties.size() > 0) {
			allProperties.putAll(properties);
		}

	}

	public String getExternalProperty(String property)
	{

		return getPropertyFromSelf2ndSons(property);
	}
	public String getExternalProperty(String property,String defaultValue)
	{
		String value = getPropertyFromSelf2ndSons(property);

		if(value != null)
			return value;
		else
			return defaultValue;
	}

	public void addConfigPropertiesFromPlugin(String configPropertiesPlugin, LinkConfigFile linkfile, BaseApplicationContext applicationContext)
	{

		if(configPropertiesFiles == null)
		{
			configPropertiesFiles = new ArrayList<String>();

		}
		if(allProperties  == null)
			allProperties = new Properties();

		try {
			Class clazz = Class.forName(configPropertiesPlugin.trim());
			synchronized (PropertiesFilePlugin.class) {
				PropertiesFilePlugin propertiesFilePlugin = (PropertiesFilePlugin) clazz.newInstance();
				try {
					if (propertiesFilePlugin.getInitType() != 1) {
						String configPropertiesFile = propertiesFilePlugin.getFiles(applicationContext);
						if (SimpleStringUtil.isNotEmpty(configPropertiesFile)) {
							loadPropertiesFromFiles(configPropertiesFile, linkfile);
						}
					} else {
						Map configProperties = propertiesFilePlugin.getConfigProperties(applicationContext);
						if (configProperties != null && configProperties.size() > 0) {
							allProperties.putAll(configProperties);
						}
					}
				} finally {
					propertiesFilePlugin.restore();
				}
			}
			if(linkfile != null)
				loopback(linkfile);
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	private void loadPropertiesFromFiles(String configPropertiesFile,LinkConfigFile linkfile ){
		String[] configPropertiesFiles = configPropertiesFile.split(",");//属性文件可以配置多个，每个用逗号分隔
		for(String file_:configPropertiesFiles) {
			this.configPropertiesFiles.add(file_);
			evalfile(file_, linkfile);
		}
	}



	/**
	 * 计算值中存在的变量的值，首先从外部属性文件中获取变量值，如果没有对应的值，再从ioc对于配置文件中获取，如果都没有获取到，看看有没有默认值，如果
	 * 有默认值，则采用默认值
	 * @param value
	 * @param providerParser
	 * @return
	 */
	public String evalValue(String value,ProviderParser providerParser)
	{
		
		if(SimpleStringUtil.isEmpty(value))
			return value;
		String varpre = null;
		String varend = null;
		boolean findVariableFromSelf = false;//持久层sql配置会设置为true
		if(providerParser != null){
			BaseApplicationContext context = providerParser.getApplicationContext();	
			if(context != null){
				varpre = context.getServiceProviderManager().getVarpre();
				varend = context.getServiceProviderManager().getVarend();
				findVariableFromSelf = context.getServiceProviderManager().findVariableFromSelf();
			}
		}
		if(varpre == null)
			varpre = "${";
		if(varend == null)
			varend = "}";
			
		List<GrammarToken> tokens = TextGrammarParser.parser(value, varpre, varend.charAt(0));
		StringBuilder re = new StringBuilder();
		for(int i = 0; tokens != null && i < tokens.size(); i ++)
		{
			GrammarToken token = tokens.get(i);
			if(token.texttoken())
				re.append(token.getText());
			else
			{
				
				String varvalue = this.getProperty(token.getText());
				if(varvalue == null){
					Pro p = providerParser._getProperty(token.getText());
					if(p != null){
						varvalue = (String)p.getValue();
					}
				}
				if(varvalue != null){
					re.append(varvalue);
				}
				else
				{
					
					if(token.getDefaultValue() != null)
						re.append(token.getDefaultValue());
					else
						re.append(varpre).append(token.getText()).append(varend);
				}
			}
		}
		return re.toString();
		
	}
	public String escapeValue(String value, ProviderParser providerParser) {
		if(SimpleStringUtil.isEmpty(value))
			return value;
		String escapePre = null;
		String escapeEnd = null;

		String escapeRNPre = null;
		String escapeRNEnd = null;
		ServiceProviderManager serviceProviderManager = null;
		if(providerParser != null){
			BaseApplicationContext context = providerParser.getApplicationContext();
			if(context != null){
				serviceProviderManager = context.getServiceProviderManager();
				escapePre = serviceProviderManager.getEscapePre();
				escapeEnd = serviceProviderManager.getEscapeEnd();
				escapeRNPre = serviceProviderManager.getEscapeRNPre();
				escapeRNEnd = serviceProviderManager.getEscapeRNEnd();
			}
		}
		boolean escape = !(SimpleStringUtil.isEmpty(escapeEnd ) || SimpleStringUtil.isEmpty(escapePre ));
		boolean escapeRN = !(SimpleStringUtil.isEmpty(escapeRNPre ) || SimpleStringUtil.isEmpty(escapeRNEnd ));

		if(escapeRN){
			List<GrammarToken> tokens = TextGrammarParser.parser(value, escapeRNPre, escapeRNEnd);
			StringBuilder re = new StringBuilder();
			for (int i = 0; tokens != null && i < tokens.size(); i++) {
				GrammarToken token = tokens.get(i);
				if (token.texttoken())
					re.append(token.getText());
				else {
					re.append("\"");
					serviceProviderManager.escapeRN(token.getText(), re);
					re.append("\"");
				}
			}
			value = re.toString();
		}
		if(escape) {
			List<GrammarToken> tokens = TextGrammarParser.parser(value, escapePre, escapeEnd);
			StringBuilder re = new StringBuilder();
			for (int i = 0; tokens != null && i < tokens.size(); i++) {
				GrammarToken token = tokens.get(i);
				if (token.texttoken())
					re.append(token.getText());
				else {
					re.append("\"");
					serviceProviderManager.escapeValue(token.getText(), re);
					re.append("\"");
				}
			}
			value = re.toString();
		}
		return value;

	}
    public void addConfigPropertiesFile(String configPropertiesFile)
    {
    	addConfigPropertiesFile(  configPropertiesFile,null);
    	
    }
    private void loopback(LinkConfigFile linkfile)
    {
    	linkfile.loopback(this);
    }
    private void evalfile(String configPropertiesFile,LinkConfigFile linkfile)
    {
    	Properties properties = new java.util.Properties();
    	
    	InputStream input = null;
		Reader read = null;
    	try
    	{
    		
    		if(!configPropertiesFile.startsWith("file:"))
    		{
		    	ClassPathResource  resource = new ClassPathResource(configPropertiesFile);

		    	try{
					input = resource.getInputStream();
		    		if(log.isDebugEnabled())
		    			log.debug("load config Properties File :"+resource.getURL());
		    	}
		    	catch(Exception e){
		    		if(linkfile == null)
						log.warn(new StringBuilder().append("Load config Properties File failed:")
												.append(configPropertiesFile)
												.append(" in ")
												.append(" cannot be opened because it does not exist,Ignored load.").toString());
		    		else {
						StringBuilder builder = new StringBuilder();
						builder.append("Load config Properties File failed:")
								.append(configPropertiesFile)
								.append(" in ");
						linkfile.toString(builder);
						builder.append(" cannot be opened because it does not exist,Ignored load.");
						log.warn(builder.toString());
					}
		    	}
    		}
    		else
    		{
    			String _configPropertiesFile = configPropertiesFile.substring("file:".length());
    			File configFile = new File(_configPropertiesFile);

    			if(configFile.exists()) {

					input = new FileInputStream(configFile);
					if (log.isDebugEnabled())
						log.debug("load config Properties File :" + _configPropertiesFile);
				}
				else
				{
					if(linkfile == null)
						log.warn(new StringBuilder().append("Load config Properties File failed:")
								.append(configPropertiesFile)
								.append(" in ")
								.append(" cannot be opened because it does not exist,Ignored load.").toString());
					else {
						StringBuilder builder = new StringBuilder();
						builder.append("Load config Properties File failed:")
								.append(configPropertiesFile)
								.append(" in ");
						linkfile.toString(builder);
						builder.append(" cannot be opened because it does not exist,Ignored load.");
						log.warn(builder.toString());
					}
				}
    		}
    		if(input != null) {
				read = new InputStreamReader(input, "UTF-8");
				properties.load(read);
			}
	    	allProperties.putAll(properties);
	    
    	}
    	catch(Exception e)
    	{
    		log.error("load config Properties File failed:",e);
    	}
    	finally
    	{
    		if(input != null)
				try {
					input.close();
				} catch (IOException e) {
					 
				}
			if(read != null)
				try {
					read.close();
				} catch (IOException e) {

				}
    	}
    }
    
    public void mergeParentConfigProperties(PropertiesContainer parent)
    {
    	if(parent == this)
    		return;
    	if(allProperties  == null)
    		allProperties = new Properties();
    	allProperties.putAll(parent.getAllProperties());
    }
    
    public void mergeSonConfigProperties(PropertiesContainer son)
    {
//    	if(son == this)
//    		return;
    	if(sonAndParentProperties  == null)
    		sonAndParentProperties = new Properties();
    	if(son.getAllProperties() != null)
    		sonAndParentProperties.putAll(son.getAllProperties());
    }
    public Map<? extends Object, ? extends Object> getAllProperties() {
		// TODO Auto-generated method stub
		return this.allProperties;
	}
	public String getProperty(String property)
    {
    	if(allProperties == null)
    		return null;
    	return allProperties.getProperty(property);
    }
	
	public String getPropertyFromSelf2ndSons(String property)
    {
    	String value = null;
    	if(sonAndParentProperties == null)
			value = null;
    	else
			value = sonAndParentProperties.getProperty(property);
		if(value == null)
			value = getProperty( property);
		return value;
    }
    
    public int size()
    {
    	if(allProperties == null)
    		return 0;
    	return allProperties.size();
    }
    
    public static void main(String[] args)
    {
    	String _configPropertiesFile = "file:/opt/local/xxx.propertis".substring("file:".length());
    	System.out.println(_configPropertiesFile);
    }


}
