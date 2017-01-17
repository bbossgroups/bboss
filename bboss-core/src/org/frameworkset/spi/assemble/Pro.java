package org.frameworkset.spi.assemble;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.frameworkset.soa.SerialFactory.MagicClass;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.CallContext;
import org.frameworkset.spi.Lifecycle;
import org.frameworkset.spi.async.annotation.Async;
import org.frameworkset.util.tokenizer.TextGrammarParser;
import org.frameworkset.util.tokenizer.TextGrammarParser.GrammarToken;

import com.frameworkset.orm.annotation.RollbackExceptions;
import com.frameworkset.orm.annotation.Transaction;
import com.frameworkset.orm.annotation.TransactionType;
import com.frameworkset.spi.assemble.BeanInstanceException;
import com.frameworkset.util.EditorInf;
import com.frameworkset.util.SimpleStringUtil;
import com.frameworkset.util.ValueObjectUtil;

/**
 * 
 * <p>
 * Title: Pro.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2009-9-19 下午11:01:17
 * @author biaoping.yin
 * @version 1.0
 */
public class Pro extends BaseTXManager implements Comparable, BeanInf {
	/** 定义property节点的固定属性 */
	public static String[] fixattrs = new String[] { "name", "n","label", "value","v",
			"class","cs", "refid", "singlable", "init-method", "destroy-method",
			"factory-bean", "factory-class", "factory-method","iocplugin" };
	
	public static final String REF_TOKEN = "->";
	
	/**
	 * 内置的属性前缀标识
	 */
	public static String[] fixattrprefics = new String[] { "f:", "path:", "ws:","s:","rmi:"};
	public static String soa_type_attribute = "s:t";
	public static String soa_type_null_attribute = "s:nvl";
	private BaseApplicationContext applicationContext;
	private String factory_bean;
	private String factory_class;
	private String factory_method;
	/**
	 * 指定元素的ioc依赖注入插件
	 * 插件必须实现接口
	 * org.frameworkset.spi.assemble.plugin.IocPlugin<T,R>
	 */
	protected String iocplugin;
	

	private Map<String, String> mvcpaths;
	private Map<String, String> WSAttributes;
	private Map<String, String> RMIAttributes;
	protected String configFile;

	private Map<String, String> SOAAttributes;
	

	public static final String COMPONENT_BEAN = "bean";
	public static final String COMPONENT_OBJECT = "java.lang.Object";
	public static final String COMPONENT_OBJECT_SHORTNAME = "Object";
	
	public static final String COMPONENT_STRING = "java.lang.String";
	public static final String COMPONENT_STRING_SHORTNAME = "String";
	public static final String COMPONENT_CLASS = "Class";

	// public final static String id_mask = "#~#";
	//    
	// /**
	// * 属性全局唯一标识
	// * id 一级标识
	// * containid#~#id 容器中的标识+变量的标识，如果变量的标识不存在，则以容器的标识为准
	// *
	// */
	// private String uuid;
	//    
	// public String getUuid()
	// {
	//	
	// return uuid;
	// }
	//
	//
	//	
	// public void setUuid(String uuid)
	// {
	// if(uuid != null)
	// this.uuid = uuid;
	// else
	// this.uuid = this.name;
	//		
	// }
	protected static Logger log = Logger.getLogger(Pro.class);
	private String name;
	protected boolean bean = false;
	private boolean singlable = true;

	private boolean isfreeze = false;
	/**
	 * 序列化/反序列化指定对象的序列化机制
	 */
	private String magicNumber = null;
	/**
	 * init-method，destroy-
	 * method两个属性分别对应aop框架提供的两个InitializingBean和DisposableBean
	 * 实现的方法，如果组件已经实现了InitializingBean就不需要指定init-method属性
	 * 如果组件实现了DisposableBean接口就不需要指定destroy-method属性
	 */

	/**
	 * bean销毁方法，单列模式时使用
	 */
	private String destroyMethod = null;

	/**
	 * bean初始化方法
	 */
	private String initMethod = null;
	
	/**
	 * 是否允许将组建发布为远程服务
	 */
	private boolean enablerpc = false;

	public Pro(BaseApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public Pro() {

	}

	public String getInitMethod() {

		return initMethod;
	}

	public void setInitMethod(String initMethod) {

		this.initMethod = initMethod;
	}

	public String getDestroyMethod() {

		return destroyMethod;
	}

	public void setDestroyMethod(String destroyMethod) {

		this.destroyMethod = destroyMethod;
	}

	public void freeze() {
		this.isfreeze = true;
	}

	private boolean isFreeze() {

		return this.isfreeze;
	}

	
	protected void modify() {
		if (this.isFreeze())
		{
			StringBuilder error = new StringBuilder();
			error.append("组件").append(this.getName() != null?this.getName():"").append("@").append(this.configFile).append("已被冻结,不能修改!");
			throw new CannotModifyException(error.toString());
		}
	}

	public String toString() {
		if (this.getValue() != null) {
			return this.getValue().toString();
		}

		if (this.isBean()) {
			// Object val = this.getBean();
			if (this.clazz != null)
				return clazz;
			return super.toString();
		} else if (this.isRefereced()) {
			// Object val = this.getRefValue(null, null);
			if (this.refid != null)
				return refid;
			return super.toString();
		}

		return super.toString();
	}

	public static boolean isFixAttribute(String name) {
		for (String _name : fixattrs) {
			if (_name.equals(name))
				return true;
		}
		return false;
	}

	/**
	 * 扩展属性集
	 */
	private Map<String, Object> extendsAttributes = new HashMap<String, Object>();

	public Map<String, Object> getExtendsAttributes() {
		return extendsAttributes;
	}

	public void setExtendsAttributes(Map<String, Object> extendsAttributes) {
		this.extendsAttributes.clear();
		this.extendsAttributes.putAll(extendsAttributes);
	}

	public boolean isSinglable() {
		return singlable;
	}

	public void setSinglable(boolean singlable) {
		this.singlable = singlable;
	}

	protected Class cls = null;
	protected void _buildType() throws ClassNotFoundException
	{
		synchronized (this) {
			if (cls == null)
				cls = BeanAccembleHelper.getClass(clazz);
		}
	}

	public Class getType() {

		try {

			if (cls != null)
				return cls;
			if (clazz == null || clazz.equals(""))
				return null;
			 
			_buildType();
			return cls;
		}

		catch (ClassNotFoundException e) {
			StringBuilder error = new StringBuilder();
			error.append("获取组件").append(this.getName()).append("@").append(this.configFile).append("类型失败：");
			throw new BeanInstanceException(error.toString(),e);
		} catch (Throwable e) {
			StringBuilder error = new StringBuilder();
			error.append("获取组件").append(this.getName()).append("@").append(this.configFile).append("类型失败：");
			throw new BeanInstanceException(error.toString(),e);
		}

	}

	protected Object beaninstance;
	protected BeanAccembleHelper accember = new BeanAccembleHelper();

	public Object getBean() {
		return getBean((CallContext) null,true);
	}
	
	public boolean useiocplugin()
	{
		return this.iocplugin != null;
	}
	
	protected void _buildBean(CallContext context,boolean convertcontainer)
	{
		synchronized (this) {
			if (beaninstance == null)
			{
				_initBean(context, convertcontainer);
				
			}
		}
	}

	/**
	 * 本方法返回原始的bean组件
	 * @param context
	 * @return
	 */
	public Object getBean(CallContext context,boolean convertcontainer) {		
		if (this.isSinglable()) // 单列模式
		{
			if(!convertcontainer)
				return value;
			if (beaninstance == null) {
				_buildBean(context,convertcontainer);
				return beaninstance;
			} else {
				return beaninstance;
			}
		} else {
			if (this.isBean()) {
				Object retvalue = null;
				if(value != null)
				{
					retvalue = processValue(context, convertcontainer);
					if(magicclass != null && magicclass.getPreserialObject() != null)
					{
						retvalue = magicclass.getPreserialObject().posthandle(retvalue);
					}
				}
				else
				{
					retvalue = accember.getBean(this, context);
					if(magicclass != null && magicclass.getPreserialObject() != null)
					{
						retvalue = magicclass.getPreserialObject().posthandle(retvalue);
					}
				}
				return retvalue ;
			} else {
				return this.getTrueValue(context);
			}
			// return accember.getBean(this,context);

		}

	}
	/**
	 * 容器类型配置元数据处理
	 * @return
	 */
	protected Object processValue(CallContext context,boolean convertcontainer)
	{
		if(value == null)
			return null;
		Object retvalue = null;
		
		if(magicclass != null)
		{			
			if(magicclass.getSerailObject() != null)
			{
				retvalue = magicclass.getSerailObject().deserialize((String)value);
				if(magicclass.getPreserialObject() != null)
				{
					retvalue = magicclass.getPreserialObject().posthandle(retvalue);
				}
				return retvalue;
			}
			
			
		}
		if (!convertcontainer) {//如果不需要将容器转换为实际类型那么直接返回对应的值
			return value;
		
		} 
		else if (value instanceof ProList) {
//			String soatype = this.getSOAAttribute(soa_type_attribute);
			String soatype = this.getClazz();
			if(soatype == null)
				soatype = this.getSOAAttribute(soa_type_attribute);
			if(soatype != null)
			{
				try {
					Class clazz = ValueObjectUtil.getClass(soatype);
					retvalue = ((ProList) value).getComponentList(clazz,context);
					if(magicclass != null && magicclass.getPreserialObject() != null)
					{
						retvalue = magicclass.getPreserialObject().posthandle(retvalue);
					}
				} catch (ClassNotFoundException e) {
					StringBuilder error = new StringBuilder();
					error.append("初始化List组件").append(this.getName() != null?this.getName():"").append("@").append(this.configFile).append("失败：");
					throw new BeanInstanceException(error.toString(),e);
				}
				catch (Throwable e) {
					StringBuilder error = new StringBuilder();
					error.append("初始化List组件").append(this.getName() != null?this.getName():"").append("@").append(this.configFile).append("失败：");
					throw new BeanInstanceException(error.toString(),e);
				}
//				retvalue = ((ProList) value).getComponentList();
			}
			else
			{
				retvalue = ((ProList) value).getComponentList(ArrayList.class,context);
				if(magicclass != null && magicclass.getPreserialObject() != null)
				{
					retvalue = magicclass.getPreserialObject().posthandle(retvalue);
				}
			}
		} 
		else if (value instanceof ProMap) {
//			String soatype = this.getSOAAttribute(soa_type_attribute);
			String soatype = this.getClazz();
			if(soatype == null)
				soatype = this.getSOAAttribute(soa_type_attribute);
			if(soatype != null)
			{
				try {
					Class clazz = ValueObjectUtil.getClass(soatype);
					retvalue = ((ProMap) value).getComponentMap(clazz,context);
					if(magicclass != null && magicclass.getPreserialObject() != null)
					{
						retvalue = magicclass.getPreserialObject().posthandle(retvalue);
					}
				} catch (ClassNotFoundException e) {
					StringBuilder error = new StringBuilder();
					error.append("初始化Map组件").append(this.getName() != null?this.getName():"").append("@").append(this.configFile).append("失败：");
					throw new BeanInstanceException(error.toString(),e);
				}
				catch (Throwable e) {
					StringBuilder error = new StringBuilder();
					error.append("初始化Map组件").append(this.getName() != null?this.getName():"").append("@").append(this.configFile).append("失败：");
					throw new BeanInstanceException(error.toString(),e);
				}
			}
			else
			{
				retvalue = ((ProMap) value).getComponentMap(HashMap.class,context);
				if(magicclass != null && magicclass.getPreserialObject() != null)
				{
					retvalue = magicclass.getPreserialObject().posthandle(retvalue);
				}
			}
			
		} else if (value instanceof ProSet) {
//			String soatype = this.getSOAAttribute(soa_type_attribute);
			String soatype = this.getClazz();
			if(soatype == null)
				soatype = this.getSOAAttribute(soa_type_attribute);
			if(soatype != null)
			{
				try {
					Class clazz = ValueObjectUtil.getClass(soatype);
					retvalue = ((ProSet) value).getComponentSet(clazz,context);
					if(magicclass != null && magicclass.getPreserialObject() != null)
					{
						retvalue = magicclass.getPreserialObject().posthandle(retvalue);
					}
				} catch (ClassNotFoundException e) {
					StringBuilder error = new StringBuilder();
					error.append("初始化Set组件").append(this.getName() != null?this.getName():"").append("@").append(this.configFile).append("失败：");
					throw new BeanInstanceException(error.toString(),e);
				}
				catch (Throwable e) {
					StringBuilder error = new StringBuilder();
					error.append("初始化Set组件").append(this.getName() != null?this.getName():"").append("@").append(this.configFile).append("失败：");
					throw new BeanInstanceException(error.toString(),e);
				}
//				retvalue = ((ProList) value).getComponentList();
			}
			else
			{
				retvalue = ((ProSet) value).getComponentSet(TreeSet.class,context);
				if(magicclass != null && magicclass.getPreserialObject() != null)
				{
					retvalue = magicclass.getPreserialObject().posthandle(retvalue);
				}
			}
			
		} else if (value instanceof ProArray) {
			retvalue = ((ProArray) value).getComponentArray(context);
			if(magicclass != null && magicclass.getPreserialObject() != null)
			{
				retvalue = magicclass.getPreserialObject().posthandle(retvalue);
			}
		} 
		else {
//			if (this.clazz != null) {
//				retvalue = accember.getBean(this, context);
//				if(magicclass != null && magicclass.getPreserialObject() != null)
//				{
//					retvalue = magicclass.getPreserialObject().posthandle(retvalue);
//				}
//			} else 
			{
				retvalue = value;
			}
		}
		
		return retvalue;
	}
	
	/**
	 * 本方法带ioc功能的bean组件
	 * @param context
	 * @return
	 */
	public Object getProxyBean(CallContext context) {
		return getApplicationContext().proxyObject(this, 
				this.getBean(context,true), 
				this.getXpath());

	}

	public <T> T getBeanFromType(Class<T> type) {
		return getBean(null, type);

	}
	protected void _initTBean(CallContext context, Class type)
	{
		synchronized (this) {
			if (beaninstance == null)
			{
				
				if (this.isBean()) {
					beaninstance = accember.getBean(this, context);
				} else {
					beaninstance = this.getTrueValue(context);
				}
			}
			
		}
	}
	public <T> T getBean(CallContext context, Class<T> type) {
		if (this.isSinglable()) // 单列模式
		{
			if (beaninstance == null) {
				_initTBean(  context,   type);
				return (T) beaninstance;
			} else {
				return (T) beaninstance;
			}
		} else {
			if (this.isBean()) {
				return (T) accember.getBean(this, context);
			} else {
				return (T) this.getTrueValue(context);
			}
			// return accember.getBean(this,context);

		}

	}

	public boolean isBean() {
		return bean;
	}

	public void setBean(boolean bean) {
		modify();
		this.bean = bean;

	}

	public boolean isRefereced() {
		return this.refid != null && !this.refid.equals("");
	}
	
	protected Object value;
	private String xpath;

	/**
	 * attr:test1->test2
	 * attr:test1[0]
	 * attr:test1[key]
	 * 
	 */
	private String refid;
	
	private RefID refidLink;
	
	

	/**
	 * 值类型
	 */
	protected String clazz;
	private String description;

	/**
	 * 属性编辑器信息
	 */
	private Editor editor; 

	public boolean innerNode()
	{
		return refidLink != null && refidLink.getNext() != null;
	}
	public void setEditor(Editor editor) {
		modify();
		this.editor = editor;

	}
	
	public String getEditorString()
	{
		if (this.editor != null)
			return this.editor.getEditor();
		return null;
	}

	public EditorInf getEditorInf() throws Exception {
		if (this.editor != null)
			return this.editor.getEditorInf(this.applicationContext);
		return null;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	private String label;
	private boolean isserviceRef = false;

	// /**
	// * 对应的字段名称
	// */
	// private String fieldname;

	public boolean isAttributeRef() {
		return !this.isserviceRef;
	}

	public boolean isServiceRef() {
		return this.isserviceRef;
	}

	public String getRefid() {
		return refid;
	}
	
	

	public void setRefid(String refid) {
		modify();
		if (refid == null || refid.equals(""))
			return;
		isserviceRef = refid.startsWith(ServiceProviderManager.SERVICE_PREFIX);
		if (!isserviceRef) {

			if (refid.startsWith(ServiceProviderManager.ATTRIBUTE_PREFIX)) {
				this.refid = refid
						.substring(ServiceProviderManager.ATTRIBUTE_PREFIX
								.length());
				this.refidLink = RefIDUtil.parsedRefID(this.refid);
				
			} else {
				this.refid = refid;
				isserviceRef = true;
			}
		} else {
			this.refid = refid.substring(ServiceProviderManager.SERVICE_PREFIX
					.length());
		}
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		modify();
		this.name = name;
	}

	public Object getValue() {
		return value;
	}
	public static String evalValue(String value,PropertiesContainer configPropertiesFile)
	{
		return configPropertiesFile.evalValue(value);
//		if(SimpleStringUtil.isEmpty(value))
//			return value;
//		List<GrammarToken> tokens = TextGrammarParser.parser(value, "${", '}');
//		StringBuilder re = new StringBuilder();
//		for(int i = 0; tokens != null && i < tokens.size(); i ++)
//		{
//			GrammarToken token = tokens.get(i);
//			if(token.texttoken())
//				re.append(token.getText());
//			else
//			{
//				String varvalue = configPropertiesFile.getProperty(token.getText());
//				if(varvalue != null)
//					re.append(varvalue);
//				else
//				{
//					re.append("${").append(token.getText()).append("}");
//				}
//			}
//		}
//		return re.toString();
		
	}
	public void setValue(String value,PropertiesContainer configPropertiesFile) {
		modify();
		if(configPropertiesFile != null && configPropertiesFile.size() > 0)
		{
			
			this.value = evalValue(  value,  configPropertiesFile);
		}
		else
		{
			this.value = value;
		}
	}
	public void setValue(String value) {
		modify();
		 
		this.value = value;
		 
	}
	public void setCollectionValue(Object value) {
		modify();
		this.value = value;
	}

	private boolean isMap = false;

	private boolean isArray = false;

	public boolean isMap() {

		return isMap;
	}

	public void setMap(boolean isMap) {

		this.isMap = isMap;
	}

	public boolean isList() {

		return isList;
	}

	public boolean isArray() {

		return isArray;
	}

	public void setList(boolean isList) {

		this.isList = isList;
	}

	public void setArray(boolean isArray) {

		this.isArray = isArray;
	}

	public boolean isSet() {

		return isSet;
	}

	public void setSet(boolean isSet) {

		this.isSet = isSet;
	}

	private boolean isList = false;
	private boolean isSet = false;

	public String getClazz() {
		return clazz;
	}

	public void setClazz(String clazz) {
		modify();
		this.clazz = clazz;
	}

	public int getInt() {
		Object value = this.getTrueValue();
		if (value == null)
			return 0;

		try {
			return Integer.parseInt(value.toString());
		} catch (Exception e) {
			throw new AssembleException("属性[" + name + "]不是整型数据！value=" + value);
		}

	}

	public long getLong() {
		Object value = this.getTrueValue();
		if (value == null)
			return 0;

		try {
			return Long.parseLong(value.toString());
		} catch (Exception e) {
			throw new AssembleException("属性[" + name + "]不是整型数据！value=" + value);
		}

	}

	public Pro getPro() {
		Object value = this.getTrueValue();
		return (Pro) value;
	}

	public int getInt(int defaultValue) {
		Object value = this.getTrueValue();
		if (value == null || value.equals(""))
			return defaultValue;
		try {
			return Integer.parseInt(value.toString());
		} catch (Exception e) {
			throw new AssembleException("属性[" + name + "]不是整型数据！value=" + value);
		}
	}

	public long getLong(long defaultValue) {
		Object value = this.getTrueValue();
		if (value == null || value.equals(""))
			return defaultValue;
		try {
			return Long.parseLong(value.toString());
		} catch (Exception e) {
			throw new AssembleException("属性[" + name + "]不是整型数据！value=" + value);
		}
	}

	public boolean getBoolean() {
		Object value = this.getTrueValue();
		if (value == null) {
			throw new AssembleException("配置文件没有指定属性[" + name + "]的值。");
		}
		String t = value.toString();
		if (t.equals("")
				|| (!t.equalsIgnoreCase("true") && !t.equalsIgnoreCase("false"))) {
			throw new AssembleException("配置文件指定属性[" + name
					+ "]的值不是boolean值：value=" + value);
		}

		try {
			boolean ivalue = Boolean.parseBoolean(t);
			return ivalue;

		} catch (Exception e) {
			throw new AssembleException("属性[" + name + "]不是布尔型数据！value="
					+ value);
		}

	}

	public boolean getBoolean(boolean defaultValue) {
		Object value = this.getTrueValue();
		if (value == null) {
			log.info("配置文件没有指定属性[" + name + "]的值。");
			return defaultValue;
		}
		String t = value.toString();
		if (t.equals("")
				|| (!t.equalsIgnoreCase("true") && !t.equalsIgnoreCase("false"))) {
			throw new AssembleException("配置文件指定属性[" + name
					+ "]的值不是boolean值：value=" + value);
		}
		try {
			boolean ivalue = Boolean.parseBoolean(t);
			return ivalue;

		} catch (Exception e) {
			throw new AssembleException("属性[" + name + "]不是布尔型数据！value="
					+ value);
		}

	}

	public String getString() {
		Object value = this.getTrueValue();
		if (value == null)
			return null;

		return value.toString();
	}

	public String getString(String defaultValue) {
		Object ret = getTrueValue(null);
		if (ret == null)
			return defaultValue;

		return ret.toString();
	}

	public ProMap getMap(ProMap defaultValue) {
		Object ret = getTrueValueWithoutEditor(null, null, false);
		if (ret == null)
			return defaultValue;

		try {
			return (ProMap) ret;
		} catch (Exception e) {
			log.warn("",e);
			return defaultValue;
		}
	}

	public Object getTrueValue() {

		return getTrueValue(null);

	}

	public Object getTrueValue(CallContext context) {
		return getTrueValue(context, null);
	}
	
	/**
	 * 本方法返回带ioc功能的值
	 * @param context
	 * @return
	 */
	public Object getProxyTrueValue(CallContext context) {
//		return getTrueValue(context, null);
		return getApplicationContext().proxyObject(this, 
				getTrueValue( context) , 
				this.getXpath());

	}

	public Object getTrueValue(CallContext context, Object defaultValue) {
		return getTrueValue(context, defaultValue, true);

	}
	
	public boolean isSOAByteArray(String soa_type)
	{
//		String soa_type = this.getSOAAttribute(soa_type_attribute);
		
		if(soa_type != null && (soa_type.equals("byte[]") || soa_type.equals("File")))
		{
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param context
	 * @param defaultValue
	 * @param convertcontainer
	 * @return
	 */
	public Object getTrueValue(CallContext context, Object defaultValue,
			boolean convertcontainer) {
		return getTrueValue_(context, defaultValue,
		 		  			convertcontainer,true);
//		Object retvalue = null;
//		if (value != null) {
//			if (!convertcontainer) {//如果不需要将容器转换为实际类型那么直接返回对应的值
//				retvalue = value;
//			} else if (value instanceof ProList) {
//				String soatype = this.getSOAAttribute(soa_type_attribute);
//				if(soatype != null)
//				{
//					try {
//						Class clazz = ValueObjectUtil.getClass(soatype);
//						retvalue = ((ProList) value).getComponentList(clazz);
//					} catch (ClassNotFoundException e) {
//						throw new BeanInstanceException(e);
//					}
////					retvalue = ((ProList) value).getComponentList();
//				}
//				else
//				{
//					retvalue = ((ProList) value).getComponentList(ArrayList.class);
//				}
//			} else if (value instanceof ProMap) {
//				String soatype = this.getSOAAttribute(soa_type_attribute);
//				if(soatype != null)
//				{
//					try {
//						Class clazz = ValueObjectUtil.getClass(soatype);
//						retvalue = ((ProMap) value).getComponentMap(clazz);
//					} catch (ClassNotFoundException e) {
//						throw new BeanInstanceException(e);
//					}
//				}
//				else
//				{
//					retvalue = ((ProMap) value).getComponentMap(HashMap.class);
//				}
//				
//			} else if (value instanceof ProSet) {
//				String soatype = this.getSOAAttribute(soa_type_attribute);
//				if(soatype != null)
//				{
//					try {
//						Class clazz = ValueObjectUtil.getClass(soatype);
//						retvalue = ((ProSet) value).getComponentSet(clazz);
//					} catch (ClassNotFoundException e) {
//						throw new BeanInstanceException(e);
//					}
////					retvalue = ((ProList) value).getComponentList();
//				}
//				else
//				{
//					retvalue = ((ProSet) value).getComponentSet(TreeSet.class);
//				}
//				
//			} else if (value instanceof ProArray) {
//				retvalue = ((ProArray) value).getComponentArray();
//			} else {
//				String soatype = this.getSOAAttribute(soa_type_attribute);
//				if(soatype == null)
//				{
//					retvalue = value;
//				}
//				else
//				{
//					if(this.isSOAByteArray(soatype))
//					{
//						try {
//							retvalue = ValueObjectUtil.byteArrayDecoder((String)value);
//						} catch (Exception e) {
//							throw new BeanInstanceException(e);
//						}
//					}
//					else
//					{
//						try {
//							retvalue = ValueObjectUtil.typeCast(value, ValueObjectUtil.getClass(soatype));
//						} catch (Exception e) {
//							throw new BeanInstanceException(e);
//						}
//					}
//				}
//			}
//		}
//		else if (bean) {
//			if (convertcontainer) 
//			{
//				Object ret = this.getBean(context);
//				retvalue = ret != null ? ret : defaultValue;
//				
//			}
//			else
//			{
//				retvalue = defaultValue;
//			}
//			
//		} 
//		else if (this.isRefereced()) {
//			retvalue = this.getRefValue(context, defaultValue);
//		} else {
//			if (this.clazz != null) {
//				try {
//					if (!convertcontainer) 
//					{
//						retvalue = defaultValue;
//					}
//					else
//					{
//						retvalue = Class.forName(this.clazz).newInstance();
//					}
//				} catch (InstantiationException e) {
//					throw new BeanInstanceException(e);
//				} catch (IllegalAccessException e) {
//					throw new BeanInstanceException(e);
//				} catch (ClassNotFoundException e) {
//					throw new BeanInstanceException(e);
//				}
//			} else {
//				retvalue = defaultValue;
//			}
//		}
//
//		try {
//			EditorInf editor = getEditorInf();
//			if (editor != null) {
//				retvalue = editor.getValueFromObject(retvalue);
//			}
//		} catch (CurrentlyInCreationException e) {
//			throw e;
//		} catch (Exception e) {
//			throw new BeanInstanceException(e);
//		}
//		return retvalue;

	}
	
	protected MagicClass magicclass = null;
	
	/**
	 * 
	 * @param context
	 * @param defaultValue
	 * @param convertcontainer
	 * @return
	 */
	private Object getTrueValue_(CallContext context, Object defaultValue,
			boolean convertcontainer,boolean useeditor) {
//		MagicClass magicclass = null;
//		if(this.magicclass != null)
//		{
//			magicclass = SerialFactory.getSerialFactory().getMagicClassByMagicNumber(magicNumber);
//			if(magicclass == null)
//			{
//				throw new BeanInstanceException("反序列化数据异常:magicNumber " +magicNumber+"不存在。检查resources/org/frameworkset/soa/serialconf.xml中是否配置正确!");
//			}
//		}
		Object retvalue = null;
		if (bean) {
//			if (convertcontainer) 
			{
				Object ret = this.getBean(context,convertcontainer);
				retvalue = ret != null ? ret : defaultValue;
				
			}
//			else
//			{
//				retvalue = defaultValue;
//			}
			
		} 
		else if (this.isRefereced()) {
			retvalue = this.getRefValue(context, defaultValue);
		} 
//		else {
//			if (this.clazz != null) {
//				try {
//					if (!convertcontainer) 
//					{
//						retvalue = defaultValue;
//					}
//					else
//					{
//						retvalue = Class.forName(this.clazz).newInstance();
//					}
//				} catch (InstantiationException e) {
//					throw new BeanInstanceException(e);
//				} catch (IllegalAccessException e) {
//					throw new BeanInstanceException(e);
//				} catch (ClassNotFoundException e) {
//					throw new BeanInstanceException(e);
//				}
//			} else {
//				retvalue = defaultValue;
//			}
//		}
		else if (value != null) {
//			retvalue = value;
//			if(magicclass != null)
//			{
//				
//				if(magicclass.getSerailObject() != null)
//				{
//					retvalue = magicclass.getSerailObject().deserialize((String)value);
//				}
//				else
//				{
//					retvalue = value;
//				}
//				return retvalue;
//			}
//			else if (!convertcontainer) {//如果不需要将容器转换为实际类型那么直接返回对应的值
//				retvalue = value;
//			} else if (value instanceof ProList) {
//				String soatype = this.getSOAAttribute(soa_type_attribute);
//				if(soatype != null)
//				{
//					try {
//						Class clazz = ValueObjectUtil.getClass(soatype);
//						retvalue = ((ProList) value).getComponentList(clazz,context);
//					} catch (ClassNotFoundException e) {
//						throw new BeanInstanceException(e);
//					}
////					retvalue = ((ProList) value).getComponentList();
//				}
//				else
//				{
//					retvalue = ((ProList) value).getComponentList(ArrayList.class,context);
//				}
//			} else if (value instanceof ProMap) {
//				String soatype = this.getSOAAttribute(soa_type_attribute);
//				if(soatype != null)
//				{
//					try {
//						Class clazz = ValueObjectUtil.getClass(soatype);
//						retvalue = ((ProMap) value).getComponentMap(clazz,context);
//					} catch (ClassNotFoundException e) {
//						throw new BeanInstanceException(e);
//					}
//				}
//				else
//				{
//					retvalue = ((ProMap) value).getComponentMap(HashMap.class,context);
//				}
//				
//			} else if (value instanceof ProSet) {
//				String soatype = this.getSOAAttribute(soa_type_attribute);
//				if(soatype != null)
//				{
//					try {
//						Class clazz = ValueObjectUtil.getClass(soatype);
//						retvalue = ((ProSet) value).getComponentSet(clazz,context);
//					} catch (ClassNotFoundException e) {
//						throw new BeanInstanceException(e);
//					}
////					retvalue = ((ProList) value).getComponentList();
//				}
//				else
//				{
//					retvalue = ((ProSet) value).getComponentSet(TreeSet.class,context);
//				}
//				
//			} else if (value instanceof ProArray) {
//				retvalue = ((ProArray) value).getComponentArray(context);
//			} 
			
//			else 
//			{
				String soatype = this.getSOAAttribute(soa_type_attribute);
				if(soatype == null)
				{
					retvalue = value;
				}
				else
				{
					if(this.isSOAByteArray(soatype))
					{
						try {
							retvalue = ValueObjectUtil.byteArrayDecoder((String)value);
						}catch (Throwable e) {
							//InvocationTargetException e;
							StringBuilder error = new StringBuilder();
							error.append("获取组件值").append(this.getName() != null?this.getName():"").append("@").append(this.configFile).append("失败：");
							throw new BeanInstanceException(error.toString(),e);
							 
						}
					}
					else
					{
						try {
							retvalue = ValueObjectUtil.typeCast(value, ValueObjectUtil.getClass(soatype));
						}
						
						catch (Throwable e) {
							 
							StringBuilder error = new StringBuilder();
							error.append("获取组件值").append(this.getName() != null?this.getName():"").append("@").append(this.configFile).append("失败：");
							throw new BeanInstanceException(error.toString(),e);
							 
						}
					}
				}
//			}
		}
		else {
			retvalue = defaultValue;
		}
		
		

		if(useeditor)
		{
			try {
				EditorInf editor = getEditorInf();
				if (editor != null) {
					retvalue = editor.getValueFromObject(retvalue);
				}
			} catch (CurrentlyInCreationException e) {
				throw e;
			} 
			catch (Throwable e) {
				//InvocationTargetException e;
				StringBuilder error = new StringBuilder();
				error.append("获取组件值").append(this.getName() != null?this.getName():"").append("@").append(this.configFile).append("失败：");
				throw new BeanInstanceException(error.toString(),e);
				 
			}
		}
		return retvalue;

	}
	
	/**
	 * 
	 * @param context
	 * @param defaultValue
	 * @param convertcontainer
	 * @return
	 */
	public Object getTrueValueWithoutEditor(CallContext context, Object defaultValue,
			boolean convertcontainer) {
		
		return getTrueValue_(context, defaultValue,
		  			convertcontainer,false);
//		Object retvalue = null;
//		if (value != null) {
//			if (!convertcontainer) {//如果不需要将容器转换为实际类型那么直接返回对应的值
//				retvalue = value;
//			} else if (value instanceof ProList) {
//				retvalue = ((ProList) value).getComponentList();
//			} else if (value instanceof ProMap) {
////				retvalue = ((ProMap) value).getComponentMap();
//				String soatype = this.getSOAAttribute(soa_type_attribute);
//				if(soatype != null)
//				{
//					try {
//						Class clazz = ValueObjectUtil.getClass(soatype);
//						retvalue = ((ProMap) value).getComponentMap(clazz);
//					} catch (ClassNotFoundException e) {
//						throw new BeanInstanceException(e);
//					}
//				}
//				else
//				{
//					retvalue = ((ProMap) value).getComponentMap(HashMap.class);
//				}
//			} else if (value instanceof ProSet) {
//				retvalue = ((ProSet) value).getComponentSet();
//			} else if (value instanceof ProArray) {
//				retvalue = ((ProArray) value).getComponentArray();
//			} else {
//				String soatype = this.getSOAAttribute(soa_type_attribute);
//				if(soatype == null)
//				{
//					retvalue = value;
//				}
//				else
//				{
//					if(this.isSOAByteArray(soatype))
//					{
//						try {
//							retvalue = ValueObjectUtil.byteArrayDecoder((String)value);
//						} catch (Exception e) {
//							throw new BeanInstanceException(e);
//						}
//					}
//					else
//					{
//						try {
//							retvalue = ValueObjectUtil.typeCast(value, ValueObjectUtil.getClass(soatype));
//						} catch (Exception e) {
//							throw new BeanInstanceException(e);
//						}
//					}
//				}
//			}
//		}
//
//		else if (this.isRefereced()) {
//			retvalue = this.getRefValue(context, defaultValue);
//		} else if (this.isBean()) {
//			if (!convertcontainer) 
//			{
//				retvalue = defaultValue;
//			}
//			else
//			{
//				Object ret = this.getBean(context);
//				retvalue = ret != null ? ret : defaultValue;
//			}
//			// if(ret != null)
//			// return ret;
//			// return defaultValue;
//		} else {
//			if (this.clazz != null) {
//				try {
//					if (!convertcontainer) 
//					{
//						retvalue = defaultValue;
//					}
//					else
//					{
//						retvalue = Class.forName(this.clazz).newInstance();
//					}
//				} catch (InstantiationException e) {
//					throw new BeanInstanceException(e);
//				} catch (IllegalAccessException e) {
//					throw new BeanInstanceException(e);
//				} catch (ClassNotFoundException e) {
//					throw new BeanInstanceException(e);
//				}
//			} else {
//				retvalue = defaultValue;
//			}
//		}
//
////		try {
////			EditorInf editor = getEditorInf();
////			if (editor != null) {
////				retvalue = editor.getValueFromObject(retvalue);
////			}
////		} catch (CurrentlyInCreationException e) {
////			throw e;
////		} catch (Exception e) {
////			throw new BeanInstanceException(e);
////		}
//		return retvalue;

	}

	// Object trueValue;
	public ProMap getMap() {

		return getMap(null);
	}

	public ProList getList(ProList defaultValue) {
		Object ret = getTrueValueWithoutEditor(null, null, false);
		if (ret == null)
			return defaultValue;

		try {
			return (ProList) ret;
		} catch (Exception e) {
			log.warn("",e);
			return defaultValue;
		}
	}

	public ProList getList() {

		return getList(null);
	}

	public ProArray getArray() {

		return getArray(null);
	}

	public ProArray getArray(ProArray defaultValue) {

		Object ret = getTrueValueWithoutEditor(null, null, false);
		if (ret == null)
			return defaultValue;

		try {
			return (ProArray) ret;
		} catch (Exception e) {
			log.warn("",e);
			if (defaultValue == null) {
				try {
					return (ProArray) this.value;
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
			return defaultValue;
		}
	}

	public ProSet getSet(ProSet defaultValue) {
		Object ret = getTrueValueWithoutEditor(null, null, false);
		if (ret == null)
			return defaultValue;

		try {
			return (ProSet) ret;
		} catch (Exception e) {
			log.warn("",e);
			return defaultValue;
		}
	}

	public ProSet getSet() {

		return getSet(null);
	}

	public Object getBeanObject() {
		return getBeanObject((CallContext) null);
	}

	public Object getBeanObject(Object defaultValue) {
		return getBeanObject(null, defaultValue);
	}

	public Object getBeanObject(CallContext context) {
		return getBeanObject(context, null);
	}

	/**
	 * 属性参数
	 */
	private List<Pro> references = new ArrayList<Pro>();

	public List<Pro> getReferences() {
		// TODO Auto-generated method stub
		return references;
	}

	/**
	 * 构造函数参数
	 */
	// private List<Pro> constructorParams = new ArrayList<Pro>();
	public List<Pro> getConstructorParams() {
		// TODO Auto-generated method stub
		return this.construction.getParams();
	}

	// public void addConstructorParam(Pro pro)
	// {
	// this.constructorParams.add(pro);
	// }

	private Construction construction;

	public Construction getConstruction() {

		return construction;
	}

	public void addConstructor(Construction construction) {
		this.construction = construction;
	}

	public void addReferenceParam(Pro pro) {
		this.references.add(pro);
	}

	private String reftype;

	public void setReftype(String reftype) {
		modify();
		this.reftype = reftype;
	}

	public String getReftype() {
		// TODO Auto-generated method stub
		return reftype;
	}

	public Object getBeanObject(CallContext context, Object defaultValue) {
		// if(!this.isBean())
		// {
		// if(value == null)
		// {
		// if(this.refid == null || this.refid.equals(""))
		// return defaultValue;
		// else
		// {
		// return this.getRefValue(context,defaultValue);
		// }
		// }
		//            
		// return value;
		// }
		// else
		// {
		// return this.getBean(context);
		// }
		return this.getTrueValue(context, defaultValue);
	}

	public Object getObject(Object defaultValue) {
		if (this.value != null)
			return value;
		Object value = this.getTrueValue();

		return value != null ? value : defaultValue;
		// if(!this.isBean())
		// {
		// if(value == null)
		// {
		// if(this.refid == null || this.refid.equals(""))
		// return defaultValue;
		// else
		// {
		// return this.getRefValue(context,defaultValue);
		// }
		// }
		//            
		// return value;
		// }
		// else
		// {
		// return this.getBean(context);
		// }
	}

	public Object getObject() {
		return this.getObject(null);
		// if(!this.isBean())
		// {
		// if(value == null)
		// {
		// if(this.refid == null || this.refid.equals(""))
		// return defaultValue;
		// else
		// {
		// return this.getRefValue(context,defaultValue);
		// }
		// }
		//            
		// return value;
		// }
		// else
		// {
		// return this.getBean(context);
		// }
	}

	public Object getRefValue(CallContext context, Object defaultValue) {
		if (this.isSinglable()) // 单列模式
		{
			if (beaninstance == null) {
				_buildRefValue(  context,   defaultValue);
				return beaninstance;
			} else {
				return beaninstance;
			}
		} else {
			return accember.getRefValue(this, context, defaultValue);
		}
	}
	protected void _buildRefValue(CallContext context, Object defaultValue)
	{
		synchronized (this) {
			if (beaninstance == null)
			{
				
				beaninstance = accember.getRefValue(this, context,
						defaultValue);
			}
			
		}
	}

	public Class getBeanClass() {
		// TODO Auto-generated method stub
		return this.getType();
	}
	protected Class factoryClass;
	 protected void _buildFactoryClass() throws ClassNotFoundException
	 {
		 synchronized (this) {
				if (factoryClass == null)
					
				// if(clazz.equals("int") )
				// return cls = int.class;
				// if(clazz.equals("integer") )
				// return cls = Integer.class;
				// if(clazz.equals("float") )
				// return cls = float.class;
				// if(clazz.equals("double") )
				// return cls = double.class;
				// if(clazz.equals("short") )
				// return cls = short.class;
				// if(clazz.equals("char") )
				// return cls = char.class;
				// if(clazz.equals("string") )
				// return cls = String.class;
				// if(clazz.equals("boolean") )
				// return cls = boolean.class;

				factoryClass = BeanAccembleHelper.getClass(this.getFactory_class());
			}
	 }
	 public Class getFactoryClass()
	 {
		 try {

				if (factoryClass != null)
					return factoryClass;
				if (factory_class == null || factory_class.equals(""))
					return null;
				// return cls = String.class;
				_buildFactoryClass();
				return factoryClass;
			}

			catch (ClassNotFoundException e) {
				StringBuilder error = new StringBuilder();
				error.append("初始化工厂组件").append(this.getName() != null?this.getName():"").append("@").append(this.configFile).append("失败：");
				throw new BeanInstanceException(error.toString(),e);
			} catch (Exception e) {
				StringBuilder error = new StringBuilder();
				error.append("初始化工厂组件").append(this.getName() != null?this.getName():"").append("@").append(this.configFile).append("失败：");
				throw new BeanInstanceException(error.toString(),e);
			}
	 }

	public int compareTo(Object o) {
		if(o == null)
			return 1;
		
		return this.hashCode() > o.hashCode()? 1:-1;
	}

	

	public String getConfigFile() {
		// TODO Auto-generated method stub
		return configFile;
	}

	public void setConfigFile(String configFile) {
		modify();
		this.configFile = configFile;

	}

	public boolean getBooleanExtendAttribute(String name) {
		// TODO Auto-generated method stub
		Object ret = null;
		if (getExtendsAttributes() != null) {
			ret = this.getExtendsAttributes().get(name);
			if (ret != null)
				return Boolean.parseBoolean(ret.toString().toLowerCase());
			return false;
		} else {
//			log.info("Get Extend Attribute failed:name = " + name
//					+ " not exist in property[" + this.getName() + "]");
			return false;
		}

	}

	public boolean getBooleanExtendAttribute(String name, boolean defaultValue) {
		// TODO Auto-generated method stub
		Object ret = null;
		if (getExtendsAttributes() != null) {
			ret = this.getExtendsAttributes().get(name);
			if (ret != null)
				return Boolean.parseBoolean(ret.toString().toLowerCase());
//			log.info("Get Extend Attribute failed:name = " + name
//					+ " not exist in property[" + this.getName()
//					+ "] return default value: " + defaultValue);
			else
				return defaultValue;
		} else {
//			log.info("Get Extend Attribute failed:name = " + name
//					+ " not exist in property[" + this.getName()
//					+ "] return default value: " + defaultValue);
			return defaultValue;
		}

	}

	public String getStringExtendAttribute(String name) {
		// TODO Auto-generated method stub
		return (String) this.getExtendsAttributes().get(name);
	}

	public String getStringExtendAttribute(String name, String defaultValue) {
		String value = (String) this.getExtendsAttributes().get(name);
		return value != null ? value : defaultValue;
	}

	public Object getExtendAttribute(String name) {
		// TODO Auto-generated method stub
		if (getExtendsAttributes() != null)
			return this.getExtendsAttributes().get(name);
		else {
//			log.info("Get Extend Attribute failed:name = " + name
//					+ " not exist in property[" + this.getName() + "]");
			return null;
		}

	}

	public Object getExtendAttribute(String name, Object defaultValue) {
		// TODO Auto-generated method stub
		if (getExtendsAttributes() != null) {
			Object value = this.getExtendsAttributes().get(name);
			return value != null ? value : defaultValue;
		} else {
//			log.info("Get Extend Attribute failed:name = " + name
//					+ " not exist in property[" + this.getName() + "]");
			return defaultValue;
		}

	}

	public int getIntExtendAttribute(String name) {
		// TODO Auto-generated method stub
		Object ret = null;
		if (getExtendsAttributes() != null) {
			ret = this.getExtendsAttributes().get(name);
			if (ret != null)
				return Integer.parseInt(ret.toString());
			return -1;
		} else {
//			log.info("Get Extend Attribute failed:name = " + name
//					+ " not exist in property[" + this.getName() + "]");
			return -1;
		}

	}

	public int getIntExtendAttribute(String name, int defaultValue) {
		// TODO Auto-generated method stub
		Object ret = null;
		if (getExtendsAttributes() != null) {
			ret = this.getExtendsAttributes().get(name);
			if (ret != null)
				return Integer.parseInt(ret.toString());
			else 
				return defaultValue;
		} else {
//			log.info("Get Extend Attribute failed:name = " + name
//					+ " not exist in property[" + this.getName() + "]");
			return defaultValue;
		}

	}

	public long getLongExtendAttribute(String name) {
		// TODO Auto-generated method stub
		Object ret = null;
		if (getExtendsAttributes() != null) {
			ret = this.getExtendsAttributes().get(name);
			if (ret != null)
				return Long.parseLong(ret.toString());
			return -1;
		} else {
//			log.info("Get Extend Attribute failed:name = " + name
//					+ " not exist in property[" + this.getName() + "]");
			return -1;
		}
	}

	public long getLongExtendAttribute(String name, long defaultValue) {
		// TODO Auto-generated method stub
		Object ret = null;
		if (getExtendsAttributes() != null) {
			ret = this.getExtendsAttributes().get(name);
			if (ret != null)
				return Long.parseLong(ret.toString());
			else
				return defaultValue;
		} else {
//			log.info("Get Extend Attribute failed:name = " + name
//					+ " not exist in property[" + this.getName() + "]");
			return defaultValue;
		}
	}

	

	@Override
	protected void initTransactions() {
		if (!this.isBean())
			return;
		try {
			Class class_ = Class.forName(this.clazz);
			Method[] methods = class_.getDeclaredMethods();
			if (methods == null || methods.length == 0)
				return;
			if (txs == null)
				txs = new Transactions();
			for (Method method : methods) {
				boolean txFlag = method.isAnnotationPresent(Transaction.class);
				TransactionType txtype = null;
				if (txFlag) {
					Transaction tx = method.getAnnotation(Transaction.class);
					txtype = tx.value();

				} else
//					return;
					continue;
				txFlag = method.isAnnotationPresent(RollbackExceptions.class);
				String[] rollbacksexceptions = null;
				if (txFlag) {
					RollbackExceptions tx = method
							.getAnnotation(RollbackExceptions.class);
					rollbacksexceptions = tx.value();

				}
				txs.addTransactionMethod(new SynchronizedMethod(method, txtype,
						rollbacksexceptions));

			}

		} catch (ClassNotFoundException e) {
			StringBuilder error = new StringBuilder();
			error.append("初始化组件").append(this.getName() != null?this.getName():"").append("@").append(this.configFile).append("事务配置失败：");
			throw new BeanInstanceException(error.toString(),e);
		}
		catch (Throwable e) {
			StringBuilder error = new StringBuilder();
			error.append("初始化组件").append(this.getName() != null?this.getName():"").append("@").append(this.configFile).append("事务配置失败：");
			throw new BeanInstanceException(error.toString(),e);
		}

	}
	

	public BaseApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public String getFactory_bean() {
		return factory_bean;
	}

	public void setFactory_bean(String factoryBean) {
		this.modify();
		factory_bean = factoryBean;
	}

	public String getFactory_class() {
		return factory_class;
	}

	public void setFactory_class(String factoryClass) {
		this.modify();
		factory_class = factoryClass;
	}

	public String getFactory_method() {
		return factory_method;
	}

	public void setFactory_method(String factoryMethod) {
		this.modify();
		factory_method = factoryMethod;
	}

	/**
	 * @return the mvcpaths
	 */
	public Map<String, String> getMvcpaths() {
		return mvcpaths;
	}

	/**
	 * @param mvcpaths
	 *            the mvcpaths to set
	 */
	public void setMvcpaths(Map<String, String> mvcpaths) {
		this.modify();
		this.mvcpaths = mvcpaths;
	}

	/**
	 * 加载异步调用的方法信息
	 */
	protected void initAsyncMethods() {
		if (!this.isBean())
			return;

		try {
			Class class_ = Class.forName(this.clazz);
			Method[] methods = class_.getDeclaredMethods();
			if (methods == null || methods.length == 0)
				return;
			if (this.asyncMethods == null)
				asyncMethods = new AOPMethods();
			for (Method method : methods) {
				boolean txFlag = method.isAnnotationPresent(Async.class);
				Async tx = null;
				if (txFlag) {
					tx = method.getAnnotation(Async.class);
					asyncMethods.addTransactionMethod(new SynchronizedMethod(method, tx));
				} else
					continue;
				

			}
		} catch (ClassNotFoundException e) {
			
			StringBuilder error = new StringBuilder();
			error.append("初始化组件").append(this.getName() != null?this.getName():"").append("@").append(this.configFile).append("同步方法配置失败：");
			throw new BeanInstanceException(error.toString(),e);
		}
		catch (Throwable e) {
			StringBuilder error = new StringBuilder();
			error.append("初始化组件").append(this.getName() != null?this.getName():"").append("@").append(this.configFile).append("同步方法配置失败：");
			throw new BeanInstanceException(error.toString(),e);
		}

	}

	public String[] getStringArray() {
		Object value = this.getTrueValue();
		if (value == null)
			return null;

		try {
			return (String[])value;
		} catch (Exception e) {
			throw new AssembleException(new StringBuilder().append("属性[" ).append( name ).append("@").append(this.configFile).append("]不是String[]型数据！value=").append( value).toString());
		}
	}

	public String[] getStringArray(String[] defaultValues) {
		Object value = this.getTrueValue();
		if (value == null || value.equals(""))
			return defaultValues;
		try {
			return (String[])value;
		} catch (Exception e) {
			throw new AssembleException(new StringBuilder().append("属性[" ).append( name ).append("@").append(this.configFile).append("]不是String[]型数据！value=").append( value).toString());
		}
	}

	public void setWSAttributes(Map<String, String> wSAttributes) {
		modify();
		this.WSAttributes = wSAttributes;
		
	}
	
	public String getWSAttribute(String WSAttribute) {
		if(isWSService())
		{
			return this.WSAttributes.get(WSAttribute);
		}
		return null;
		
	}
	
	public Map getWSAttributes() {
		if(isWSService())
		{
			return this.WSAttributes;
		}
		return null;
		
	}
	
	public String getRMIAttribute(String RMIAttribute) {
		if(isRMIService())
		{
			return this.RMIAttributes.get(RMIAttribute);
		}
		return null;
		
	}
	
	public boolean isWSService()
	{
		return this.WSAttributes != null && this.WSAttributes.size() > 0;
	}
	
	public boolean isRMIService()
	{
		return this.RMIAttributes != null && this.RMIAttributes.size() > 0;
	}

	public void setSOAAttributes(Map<String, String> sOAAttributes) {
		modify();
		this.SOAAttributes = sOAAttributes;
	}
	
	public String getSOAAttribute(String SOAAttribute)
	{
		if(this.SOAAttributes != null && this.SOAAttributes.size() > 0)
		{
			return this.SOAAttributes.get(SOAAttribute)
			;
		}
		return null;
	}
	
	public Map getSOAAttributes()
	{
		if(this.SOAAttributes != null && this.SOAAttributes.size() > 0)
		{
			return this.SOAAttributes
			;
		}
		return null;
	}

	/**
	 * @return the rMIAttributes
	 */
	public Map<String, String> getRMIAttributes() {
		return RMIAttributes;
	}

	/**
	 * @param rMIAttributes the rMIAttributes to set
	 */
	public void setRMIAttributes(Map<String, String> rMIAttributes) {
		modify();
		RMIAttributes = rMIAttributes;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public RefID getRefidLink() {
		return refidLink;
	}

	public String getMagicNumber() {
		return magicNumber;
	}

	public void setMagicNumber(String magicNumber) {
		this.magicNumber = magicNumber;
	}

	public MagicClass getMagicclass() {
		return magicclass;
	}

	public void setMagicclass(MagicClass magicclass) {
		this.magicclass = magicclass;
	}

	public String getIocplugin() {
		return iocplugin;
	}

	public void setIocplugin(String iocplugin) {
		modify();
		this.iocplugin = iocplugin;
	}
	
	
	 protected Class iocpluginClass;
	 public static final int pro_type = 0;
	 public static final int promap_type = 1;
	 public static final int prolist_type = 2;
	 public static final int proarray_type = 3;
	 public static final int proset_type = 4;
	 protected int iocinputtype = pro_type;
	 protected Object iocinputData ;
	 
	 protected void _initIocpluginClass() throws ClassNotFoundException
	 {		 
			 				 
			iocpluginClass = Class.forName(iocplugin);
			Class inputtype = firstgenericTypes(iocpluginClass);
			if(inputtype != null)
			{
				if(inputtype == Pro.class)
				{
					this.iocinputtype =  pro_type;
					iocinputData = this;
				}
				else if(inputtype == ProMap.class)
				{
					this.iocinputtype =  promap_type;
					iocinputData = this.getMap();
				}
				else if(inputtype == ProList.class)
				{
					this.iocinputtype =  prolist_type;
					iocinputData = this.getList();
				}
				else if(inputtype == ProArray.class)
				{
					this.iocinputtype =  proarray_type;
					iocinputData = this.getArray();
				}
				else if(inputtype == ProSet.class)
				{
					this.iocinputtype =  proset_type;
					iocinputData = this.getSet();
				}
				else
				{
					String error = new StringBuilder().append("iocplugin[").append(iocplugin).append("]@").append(this.configFile).append("'s first genericType[").append(inputtype.getCanonicalName()).append("] is not support by bboss ioc,please change iocplugin class defined.").toString(); 
					log.error(error);
					throw new java.lang.IllegalArgumentException(error);
				}
				
			}
			else
			{
				iocinputData = this;
			}
		 
	 }
	 
	 protected void _initBean(CallContext context,boolean convertcontainer)
		{		
			if (bean) {
				if(useiocplugin())//pro
				{
					Object _beaninstance = accember.getBean(this, context);
					if(magicclass != null && magicclass.getPreserialObject() != null)
					{
						_beaninstance = magicclass.getPreserialObject().posthandle(_beaninstance);
					}
					beaninstance = _beaninstance;
				}
				else if(value != null)
				{							
					Object _beaninstance = processValue(context, convertcontainer);
					if(magicclass != null && magicclass.getPreserialObject() != null)
					{
						_beaninstance = magicclass.getPreserialObject().posthandle(_beaninstance);
					}
					beaninstance = _beaninstance;							 
				}
				else
				{
					Object _beaninstance = accember.getBean(this, context);
					if(magicclass != null && magicclass.getPreserialObject() != null)
					{
						_beaninstance = magicclass.getPreserialObject().posthandle(_beaninstance);
					}
					beaninstance = _beaninstance;
				}
			} else {
				beaninstance = this.getTrueValue(context);
			}		
		}
	 protected void _buildIocpluginClass() throws ClassNotFoundException
	 {
		 synchronized (this) {
			if (iocpluginClass == null)
			{				 
				_initIocpluginClass();
			}
		 }
	 }
	 public Class getIocpluginClass()
	 {
		 try {

				if (iocpluginClass != null)
					return iocpluginClass;
				if (this.iocplugin == null || iocplugin.equals(""))
					return null;
				
				_buildIocpluginClass();
				return iocpluginClass;
			}

			catch (ClassNotFoundException e) {
				StringBuilder error = new StringBuilder();
				error.append("初始化iocpluginClass").append(this.getName() != null?this.getName():"").append("@").append(this.configFile).append("失败：");
				throw new BeanInstanceException(error.toString(),e);
			} catch (Throwable e) {
				StringBuilder error = new StringBuilder();
				error.append("初始化iocpluginClass").append(this.getName() != null?this.getName():"").append("@").append(this.configFile).append("失败：");
				throw new BeanInstanceException(error.toString(),e);
			}
	 } 
	 
	 public static Class firstgenericTypes(Class clazz)
		{
			if (clazz == null )
			{
				return null;
			}
			Type[] types = clazz.getTypeParameters();
			if(types == null || types.length == 0)
				types = clazz.getGenericInterfaces();
			if(types == null || types.length == 0)
				return null;
			// Class[] pts = method.getParameterTypes();
			Type type = types[0];
			if (type == null )
			{
				return null;
			}

			
			if (type instanceof ParameterizedType)
			{

				Type[] ptypes = ((ParameterizedType) type)
						.getActualTypeArguments();

				
				
				return (Class)ptypes[0];

			}
			
			return null;

		}

	public int getIocinputtype() {
		return iocinputtype;
	}

	public Object getIocinputData() {
		return iocinputData;
	}

	public boolean isEnablerpc() {
		return enablerpc;
	}

	public void setEnablerpc(boolean enablerpc) {
		this.enablerpc = enablerpc;
	}
	
	
	public String[] getDependenciesForBean( ) {
		// TODO Auto-generated method stub
		return null;
	}
	public String[] getDependentBeans( ) {
		// TODO Auto-generated method stub
		return null;
	}
	 
	public boolean isFactoryBean( ) {
		
		return getFactory_bean() != null || getFactory_class() != null;
		 
	}

	public boolean isType(Class<Lifecycle> class1) {
		if(!isFactoryBean( ) )
		{
			if(this.getType() != null)
			{
				return class1.isAssignableFrom(getType());
			}
		}
		return false;
	}
	 
	 
	

}
