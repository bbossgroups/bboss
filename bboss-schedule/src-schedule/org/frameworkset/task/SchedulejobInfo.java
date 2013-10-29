package org.frameworkset.task;

import java.util.HashMap;
import java.util.Map;

import org.frameworkset.spi.assemble.Pro;

/**
 * 
 * <p>Title: SchedulejobInfo.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-3-26 下午02:04:10
 * @author biaoping.yin,gao.tang
 * @version 1.0
 */
public class SchedulejobInfo implements java.io.Serializable {
	private ScheduleServiceInfo parent;
	private Pro jobPro;
	private String name;
	private String id;
	private String clazz;
	private boolean used = true;
	private boolean shouldRecover = false;
	
	/**
	 * 作业执行组件及其执行的方法参数
	 * 如果指定的bean和method属性，那么clazz对应的executejob就不需要指定，二者选一
	 */
	private String beanName ;
	private String beanClass ;
	private String method;
	private String cronb_time ;
	private org.frameworkset.spi.assemble.Construction methodConstruction;
	/**
	 * Map<String,String>
	 */
	private Map parameters = new HashMap();
	public String getClazz() {
		return clazz;
	}
	
	public boolean isMethodInvokerJob()
	{
		return (this.beanName != null || this.beanClass != null) && this.method != null; 
	}
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}
	public String getCronb_time() {
		return cronb_time;
	}
	public void setCronb_time(String cronb_time) {
		this.cronb_time = cronb_time;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isUsed() {
		return used;
	}
	public void setUsed(boolean used) {
		this.used = used;
	}
	
	public void addParameter(String name,Object value)
	{
		this.parameters.put(name,value);
		
	}
	
	public Object getParameter(String name)
	{
		return (String)parameters.get(name);		
	}
	
	public Object getParameter(String name,Object defaultValue)
	{
		String ret = (String)parameters.get(name);
		return ret != null?ret:defaultValue;		
	}
	public ScheduleServiceInfo getParent() {
		return parent;
	}
	public ScheduleServiceInfo getScheduleServiceInfo() {
		return parent;
	}
	public void setParent(ScheduleServiceInfo parent) {
		this.parent = parent;
	}
	public Map getParameters() {
		return parameters;
	}
	/**
	 * @return the shouldRecover
	 */
	public boolean isShouldRecover() {
		return shouldRecover;
	}
	/**
	 * @param shouldRecover the shouldRecover to set
	 */
	public void setShouldRecover(boolean shouldRecover) {
		this.shouldRecover = shouldRecover;
	}
	protected String getBeanName() {
		return beanName;
	}
	protected void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	protected String getMethod() {
		return method;
	}
	protected void setMethod(String method) {
		this.method = method;
	}
	protected String getBeanClass() {
		return beanClass;
	}
	protected void setBeanClass(String beanClass) {
		this.beanClass = beanClass;
	}
	protected org.frameworkset.spi.assemble.Construction getMethodConstruction() {
		return methodConstruction;
	}
	protected void setMethodConstruction(
			org.frameworkset.spi.assemble.Construction methodConstruction) {
		this.methodConstruction = methodConstruction;
	}

	public Pro getJobPro() {
		return jobPro;
	}

	protected void setJobPro(Pro jobPro) {
		this.jobPro = jobPro;
	}

}
