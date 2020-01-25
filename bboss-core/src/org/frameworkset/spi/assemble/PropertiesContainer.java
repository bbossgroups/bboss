package org.frameworkset.spi.assemble;

import com.frameworkset.util.SimpleStringUtil;
import com.frameworkset.util.ValueCastUtil;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.assemble.plugin.PropertiesFilePlugin;
import org.frameworkset.spi.support.EnvUtil;
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
    protected Map<Object,Object> allProperties ;
    protected Map<Object,Object> sonAndParentProperties ;
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
		//解析属性值中的环境变量
		Map evaledProperties = EnvUtil.evalEnvVariable(allProperties);
		if(evaledProperties != null){
			allProperties.putAll(evaledProperties);
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

	public Object getExternalObjectProperty(String property)
	{

		return getExternalObjectProperty(  property,null);
	}
	public Object getExternalObjectProperty(String property,Object defaultValue)
	{
		Object value = null;
		if(sonAndParentProperties == null)
			value = null;
		else {
			value = sonAndParentProperties.get(property);
		}
		if(value == null)
			value = getObjectProperty( property);
		return value;
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
					Map evaledProperties = EnvUtil.evalEnvVariable(allProperties);
					if(evaledProperties != null){
						allProperties.putAll(evaledProperties);
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
	public String evalValue(List<String> parentLinks,String value,ProviderParser providerParser)
	{
		
		if(SimpleStringUtil.isEmpty(value))
			return value;
//		String varpre = null;
//		String varend = null;
//		boolean findVariableFromSelf = false;//持久层sql配置会设置为true
		AOPValueHandler valueHandler = null;
		ValueContainer valueContainer = providerParser;
		if(providerParser != null){
			BaseApplicationContext context = providerParser.getApplicationContext();	
			if(context != null){
//				varpre = context.getServiceProviderManager().getVarpre();
//				varend = context.getServiceProviderManager().getVarend();
//				findVariableFromSelf = context.getServiceProviderManager().findVariableFromSelf();
				valueHandler = context.getServiceProviderManager();
			}
		}

		return evalValue( parentLinks,value,  valueHandler, valueContainer);
//		if(varpre == null)
//			varpre = "${";
//		if(varend == null)
//			varend = "}";
//
//		List<GrammarToken> tokens = TextGrammarParser.parser(value, varpre, varend.charAt(0));
//		StringBuilder re = new StringBuilder();
//		for(int i = 0; tokens != null && i < tokens.size(); i ++)
//		{
//			GrammarToken token = tokens.get(i);
//			if(token.texttoken())
//				re.append(token.getText());
//			else
//			{
//
//				String varvalue = this.getProperty(token.getText());
//				if(varvalue == null){
//					Pro p = providerParser._getRealProperty(token.getText());
//					if(p != null){
//						varvalue = (String)providerParser.getRealPropertyValue(p);
//					}
//				}
//				if(varvalue != null){
//					re.append(varvalue);
//				}
//				else
//				{
//
//					if(token.getDefaultValue() != null)
//						re.append(token.getDefaultValue());
//					else
//						re.append(varpre).append(token.getText()).append(varend);
//				}
//			}
//		}
//		return re.toString();
		
	}
	public void checkLoopNode(String name,List<String> parentLinks){
		if(parentLinks == null){
			return;
		}
		boolean looped = false;
		for(int i = 0;  i < parentLinks.size(); i ++){
			String node = parentLinks.get(i);
			if(node.equals(name)){
				looped = true;
				break;
			}
		}
		if(looped){
			StringBuilder msg = new StringBuilder();
			msg.append("Loop macro reference: ");
			for(int i = 0;  parentLinks != null && i < parentLinks.size(); i ++){
				String node = parentLinks.get(i);
				msg.append(node).append("->");
			}
			msg.append(name);
			throw new MacroParserException(msg.toString());
		}
		parentLinks.add(name);
	}
	/**
	 * 计算值中存在的变量的值，首先从外部属性文件中获取变量值，如果没有对应的值，再从ioc对于配置文件中获取，如果都没有获取到，看看有没有默认值，如果
	 * 有默认值，则采用默认值
	 * @param value
	 * @param valueHandler
	 * @return
	 */
	public String evalValue(List<String> parentLinks,String value, AOPValueHandler valueHandler,ValueContainer valueContainer)
	{

		if(SimpleStringUtil.isEmpty(value))
			return value;
		String varpre = null;
		String varend = null;
		boolean findVariableFromSelf = false;//持久层sql配置会设置为true
		if(valueHandler != null){
			varpre = valueHandler.getVarpre();
			varend = valueHandler.getVarend();
			findVariableFromSelf = valueHandler.findVariableFromSelf();
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
					/**
					Pro p = valueHandler._getRealProperty(token.getText());
					if(p != null){
						varvalue = (String)valueHandler.getRealPropertyValue(p);
					}
					 */
					checkLoopNode(token.getText(), parentLinks);
					varvalue = valueContainer.getMacroVariableValue( parentLinks,token.getText());
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
//		String escapePre = null;
//		String escapeEnd = null;
//
//		String escapeRNPre = null;
//		String escapeRNEnd = null;
		AOPValueHandler serviceProviderManager = null;
		if(providerParser != null){
			BaseApplicationContext context = providerParser.getApplicationContext();
			if(context != null){
				serviceProviderManager = context.getServiceProviderManager();
//				escapePre = serviceProviderManager.getEscapePre();
//				escapeEnd = serviceProviderManager.getEscapeEnd();
//				escapeRNPre = serviceProviderManager.getEscapeRNPre();
//				escapeRNEnd = serviceProviderManager.getEscapeRNEnd();
			}
		}
		return escapeValue( value, serviceProviderManager);
//		boolean escape = !SimpleStringUtil.isEmpty(escapeEnd ) || SimpleStringUtil.isEmpty(escapePre ));
//		boolean escapeRN = !(SimpleStringUtil.isEmpty(escapeRNPre ) || SimpleStringUtil.isEmpty(escapeRNEnd ));
//
//		if(escapeRN){
//			List<GrammarToken> tokens = TextGrammarParser.parser(value, escapeRNPre, escapeRNEnd);
//			StringBuilder re = new StringBuilder();
//			for (int i = 0; tokens != null && i < tokens.size(); i++) {
//				GrammarToken token = tokens.get(i);
//				if (token.texttoken())
//					re.append(token.getText());
//				else {
//					re.append("\"");
//					serviceProviderManager.escapeRN(token.getText(), re);
//					re.append("\"");
//				}
//			}
//			value = re.toString();
//		}
//		if(escape) {
//			List<GrammarToken> tokens = TextGrammarParser.parser(value, escapePre, escapeEnd);
//			StringBuilder re = new StringBuilder();
//			for (int i = 0; tokens != null && i < tokens.size(); i++) {
//				GrammarToken token = tokens.get(i);
//				if (token.texttoken())
//					re.append(token.getText());
//				else {
//					re.append("\"");
//					serviceProviderManager.escapeValue(token.getText(), re);
//					re.append("\"");
//				}
//			}
//			value = re.toString();
//		}
//		return value;

	}

	public String escapeValue(String value, AOPValueHandler valueHandler) {
		if(SimpleStringUtil.isEmpty(value))
			return value;
		String escapePre = null;
		String escapeEnd = null;

		String escapeRNPre = null;
		String escapeRNEnd = null;
		if(valueHandler != null){
			escapePre = valueHandler.getEscapePre();
			escapeEnd = valueHandler.getEscapeEnd();
			escapeRNPre = valueHandler.getEscapeRNPre();
			escapeRNEnd = valueHandler.getEscapeRNEnd();
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
					valueHandler.escapeRN(token.getText(), re);
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
					valueHandler.escapeValue(token.getText(), re);
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
		    		if(log.isInfoEnabled())
		    			log.info("load config Properties File :"+resource.getURL());
		    	}
		    	catch(Exception e){
		    		if(linkfile == null) {
		    			if(log.isInfoEnabled()) {
							log.info(new StringBuilder().append("load config Properties File :")
									.append(configPropertiesFile)
									.append(" does not exist,Ignore load.").toString());
						}
					}
		    		else {
		    			if(log.isInfoEnabled()) {
							StringBuilder builder = new StringBuilder();
							builder.append("load config Properties File :")
									.append(configPropertiesFile)
									.append(" in ");
							linkfile.toString(builder);
							builder.append(" does not exist,Ignore load.");
							log.info(builder.toString());
						}
					}
		    	}
    		}
    		else
    		{
    			String _configPropertiesFile = configPropertiesFile.substring("file:".length());
    			File configFile = new File(_configPropertiesFile);

    			if(configFile.exists()) {

					input = new FileInputStream(configFile);
					if (log.isInfoEnabled())
						log.info("load config Properties File :" + _configPropertiesFile);
				}
				else
				{
					if(linkfile == null)
						if(log.isInfoEnabled()) {
							log.info(new StringBuilder().append("load config Properties File :")
									.append(configPropertiesFile)
									.append(" does not exist,Ignore load.").toString());
						}
					else {
							if(log.isInfoEnabled()) {
								StringBuilder builder = new StringBuilder();
								builder.append("load config Properties File :")
										.append(configPropertiesFile)
										.append(" in ");
								linkfile.toString(builder);
								builder.append(" does not exist,Ignore load.");
								log.info(builder.toString());
							}
					}
				}
    		}
    		if(input != null) {
				read = new InputStreamReader(input, "UTF-8");
				properties.load(read);
			}
			if(!properties.isEmpty()) {
				allProperties.putAll(properties);
//				Iterator<Map.Entry<Object, Object>> temp = properties.entrySet().iterator();
//				StringBuilder builder = new StringBuilder();
//				while(temp.hasNext()) {
//					Map.Entry<Object, Object> entry = temp.next();
//					String key = (String)entry.getKey();
//					try {
//						EnvUtil.getSystemEnv(builder, key, null, properties);
//						allProperties.put(key, builder.toString());
//						builder.setLength(0);
//					}
//					catch (Throwable e){
//						if(log.isWarnEnabled()){
//							log.warn("",e);
//						}
//					}
//
//				}
			}
	    
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
    public Map<Object,Object> getAllProperties() {
		// TODO Auto-generated method stub
		return this.allProperties;
	}
	public String getProperty(String property)
    {
    	if(allProperties == null)
    		return null;
    	Object value = allProperties.get(property);
    	if(value == null)
    		return null;
    	return value instanceof String?(String)value:String.valueOf(value);

    }

	public Object getObjectProperty(String property)
	{
		if(allProperties == null)
			return null;
		Object value = allProperties.get(property);
		return value;

	}

	/**
	 * 首先从配置文件中查找属性值，然后从jvm系统熟悉和系统环境变量中查找属性值
	 * @param property
	 * @return
	 */
	public String getSystemEnvProperty(String property)
	{
		String value = getProperty(  property);

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

	/**
	 * 首先从配置文件中查找属性值，然后从jvm系统熟悉和系统环境变量中查找属性值
	 * @param property
	 * @return
	 */
	public String getSystemEnvProperty(String property,String defaultValue)
	{
		String value = getProperty(  property);

		if(value == null){ //Get value from jvm system propeties,just like -Dproperty=value
//			Properties pros = System.getProperties();
			value =System.getProperty(property);
			if(value == null) {
				//Get value from os env ,just like property=value in user profile
				value = System.getenv(property);

			}
		}
		return value != null? value:defaultValue;
	}
	public Boolean getBooleanSystemEnvProperty(String property)
	{

		String value = getSystemEnvProperty(  property);
		if(value == null)
			return null;
		if(value.equals("true")){
			return true;
		}
		return false;
	}
	public boolean getBooleanSystemEnvProperty(String property,boolean defaultValue)
	{

		String value = getSystemEnvProperty(  property);
		if(value == null)
			return defaultValue;
		if(value.equals("true")){
			return true;
		}
		return false;
	}


	public boolean getBooleanProperty(String property,boolean defaultValue)
	{
		if(allProperties == null)
			return defaultValue;
		Object value = allProperties.get(property);
		return ValueCastUtil.toBoolean(value,defaultValue);
	}


	public int getIntSystemEnvProperty(String property,int defaultValue) {
		String value = getSystemEnvProperty(  property);
		if(value == null)
			return defaultValue;
		try {
			return Integer.parseInt(value);
		}
		catch (Exception e){
			throw new java.lang.IllegalArgumentException(new StringBuilder()
					.append("getIntSystemEnvProperty failed:").append(property)
					.append("=").append(value).toString());
		}

	}

	public Integer getIntSystemEnvProperty(String property) {
		String value = getSystemEnvProperty(  property);
		if(value == null)
			return null;
		try {
			return Integer.parseInt(value);
		}
		catch (Exception e){
			throw new java.lang.IllegalArgumentException(new StringBuilder()
					.append("getIntSystemEnvProperty failed:").append(property)
					.append("=").append(value).toString());
		}

	}

	public int getIntProperty(String property,int defaultValue) {
		if(allProperties == null)
			return defaultValue;
		Object value = allProperties.get(property);

		try {
			return ValueCastUtil.toInt(value,defaultValue);

		}
		catch (Exception e){
			throw new java.lang.IllegalArgumentException(new StringBuilder()
					.append("getIntProperty failed:").append(property)
					.append("=").append(value).toString());
		}

	}


	public long getLongSystemEnvProperty(String property,long defaultValue) {
		String value = getSystemEnvProperty(  property);
		if(value == null)
			return defaultValue;
		try {
			return Long.parseLong(value);
		}
		catch (Exception e){
			throw new java.lang.IllegalArgumentException(new StringBuilder()
					.append("getLongSystemEnvProperty failed:").append(property)
					.append("=").append(value).toString());
		}

	}

	public Long getLongSystemEnvProperty(String property) {
		String value = getSystemEnvProperty(  property);
		if(value == null)
			return null;
		try {
			return Long.parseLong(value);
		}
		catch (Exception e){
			throw new java.lang.IllegalArgumentException(new StringBuilder()
					.append("getLongSystemEnvProperty failed:").append(property)
					.append("=").append(value).toString());
		}

	}

	public long getLongProperty(String property,long defaultValue) {
		if(allProperties == null)
			return defaultValue;
		Object value = allProperties.get(property);

		try {
			return ValueCastUtil.toLong(value,defaultValue);
		}
		catch (Exception e){
			throw new java.lang.IllegalArgumentException(new StringBuilder()
					.append("getLongProperty failed:").append(property)
					.append("=").append(value).toString());
		}

	}




	public String getPropertyFromSelf2ndSons(String property)
    {
    	Object value = null;
    	if(sonAndParentProperties == null)
			value = null;
    	else {
			value = sonAndParentProperties.get(property);
		}
		if(value == null)
			value = getObjectProperty( property);
		return ValueCastUtil.toString(value,null);
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
