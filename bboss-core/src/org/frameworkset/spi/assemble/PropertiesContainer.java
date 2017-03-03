package org.frameworkset.spi.assemble;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.util.io.ClassPathResource;
import org.frameworkset.util.tokenizer.TextGrammarParser;
import org.frameworkset.util.tokenizer.TextGrammarParser.GrammarToken;

import com.frameworkset.util.SimpleStringUtil;

public class PropertiesContainer {
    protected List<String> configPropertiesFiles;
    protected Properties allProperties ;
    protected Properties sonAndParentProperties ;
    private static Logger log = Logger.getLogger(PropertiesContainer.class);
    public void addConfigPropertiesFile(String configPropertiesFile,LinkConfigFile linkfile)
    {
    	if(configPropertiesFiles == null)
    	{
    		configPropertiesFiles = new ArrayList<String>();
    		
    	}
    	if(allProperties  == null)
    		allProperties = new Properties();
    	this.configPropertiesFiles.add(configPropertiesFile);
    	evalfile(configPropertiesFile);
    	if(linkfile != null)
    		loopback(linkfile);
    	
    }
    
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
    public void addConfigPropertiesFile(String configPropertiesFile)
    {
    	addConfigPropertiesFile(  configPropertiesFile,null);
    	
    }
    private void loopback(LinkConfigFile linkfile)
    {
    	linkfile.loopback(this);
    }
    private void evalfile(String configPropertiesFile)
    {
    	Properties properties = new java.util.Properties();
    	
    	InputStream input = null;
    	try
    	{
    		
    		if(!configPropertiesFile.startsWith("file:"))
    		{
		    	ClassPathResource  resource = new ClassPathResource(configPropertiesFile);
		    	input = resource.getInputStream();
		    	try{
		    		if(log.isDebugEnabled())
		    			log.debug("load config Properties File :"+resource.getURL());
		    	}
		    	catch(Exception e){
		    		
		    	}
    		}
    		else
    		{
    			String _configPropertiesFile = configPropertiesFile.substring("file:".length());
    			input = new FileInputStream(new File(_configPropertiesFile));
    			if(log.isDebugEnabled())
	    			log.debug("load config Properties File :"+_configPropertiesFile);
    		}
	    	properties.load(input);
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
    	if(sonAndParentProperties == null)
    		return null;
    	return sonAndParentProperties.getProperty(property);
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
