package org.frameworkset.web.demo;

import java.util.List;

public class SiteDemoBean {
	private String name;
	private String description;
	private String cnname;
	private String controllerClass;
	private String controllerClassCharset;
	private String configFile;
	private String configFileCharset;
	private String beanClassCharset;
	private List<FormUrl> formlist;
	private List<String> visturl;
	private List<String> beanClass;
	
	public List<String> getBeanClass() {
		return beanClass;
	}
	public void setBeanClass(List<String> beanClass) {
		this.beanClass = beanClass;
	}
	/**
	 * @return the controllerClass
	 */
	public String getControllerClass() {
		return controllerClass;
	}
	/**
	 * @param controllerClass the controllerClass to set
	 */
	public void setControllerClass(String controllerClass) {
		this.controllerClass = controllerClass;
	}
	/**
	 * @return the configFile
	 */
	public String getConfigFile() {
		return configFile;
	}
	/**
	 * @param configFile the configFile to set
	 */
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	/**
	 * @return the formlist
	 */
	public List<FormUrl> getFormlist() {
		return formlist;
	}
	/**
	 * @param formlist the formlist to set
	 */
	public void setFormlist(List<FormUrl> formlist) {
		this.formlist = formlist;
	}
	/**
	 * @return the visturl
	 */
	public List<String> getVisturl() {
		return visturl;
	}
	/**
	 * @param visturl the visturl to set
	 */
	public void setVisturl(List<String> visturl) {
		this.visturl = visturl;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the cnname
	 */
	public String getCnname() {
		return cnname;
	}
	/**
	 * @param cnname the cnname to set
	 */
	public void setCnname(String cnname) {
		this.cnname = cnname;
	}
	/**
	 * @return the controllerClassCharset
	 */
	public String getControllerClassCharset() {
		return controllerClassCharset;
	}
	/**
	 * @param controllerClassCharset the controllerClassCharset to set
	 */
	public void setControllerClassCharset(String controllerClassCharset) {
		this.controllerClassCharset = controllerClassCharset;
	}
	/**
	 * @return the configFileCharset
	 */
	public String getConfigFileCharset() {
		return configFileCharset;
	}
	/**
	 * @param configFileCharset the configFileCharset to set
	 */
	public void setConfigFileCharset(String configFileCharset) {
		this.configFileCharset = configFileCharset;
	}
	public String getBeanClassCharset() {
		return beanClassCharset;
	}
	public void setBeanClassCharset(String beanClassCharset) {
		this.beanClassCharset = beanClassCharset;
	}
	
	

}
