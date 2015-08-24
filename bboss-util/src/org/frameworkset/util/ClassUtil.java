/**
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
package org.frameworkset.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.frameworkset.soa.annotation.ExcludeField;
import org.frameworkset.util.annotations.Attribute;
import org.frameworkset.util.annotations.CookieValue;
import org.frameworkset.util.annotations.DataBind;
import org.frameworkset.util.annotations.IgnoreBind;
import org.frameworkset.util.annotations.MapKey;
import org.frameworkset.util.annotations.PathVariable;
import org.frameworkset.util.annotations.RequestBody;
import org.frameworkset.util.annotations.RequestHeader;
import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.util.annotations.wraper.AttributeWraper;
import org.frameworkset.util.annotations.wraper.ColumnWraper;
import org.frameworkset.util.annotations.wraper.CookieValueWraper;
import org.frameworkset.util.annotations.wraper.PathVariableWraper;
import org.frameworkset.util.annotations.wraper.RequestBodyWraper;
import org.frameworkset.util.annotations.wraper.RequestHeaderWraper;
import org.frameworkset.util.annotations.wraper.RequestParamWraper;

import com.frameworkset.orm.annotation.Column;
import com.frameworkset.orm.annotation.IgnoreORMapping;
import com.frameworkset.orm.annotation.PrimaryKey;
import com.frameworkset.util.BeanUtils;
import com.frameworkset.util.EditorInf;
import com.frameworkset.util.ValueObjectUtil;


/**
 * <p>ClassUtil.java</p>
 * <p> Description: 需要注意
 * boolean 类型属性的get/set方法的生成方式
 * 已经boolean变量的命名方式，不要在前面添加is前缀，不要命名成isXXXX，这样处理会有问题的
 * </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-9-6
 * @author biaoping.yin
 * @version 1.0
 */
public class ClassUtil
{
	
	private static final Logger log = Logger.getLogger(ClassUtil.class);
	private static final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
	public static void destroy()
	{
		if(classInfos != null)
		{
			classInfos.clear();
			classInfos=null;
		}
	}
	public static ParameterNameDiscoverer getParameterNameDiscoverer()
	{
		return parameterNameDiscoverer;
	}
	
	public static class Var
	{
		private boolean isvar ;
		private int position;
		private String name;
		public int getPosition() {
			return position;
		}
		public void setPosition(int position) {
			this.position = position;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public boolean isIsvar() {
			return isvar;
		}
		public void setIsvar(boolean isvar) {
			this.isvar = isvar;
		}
	}
	public final static EditorInf getParamEditor(RequestParamWraper param)
	{
		if(param == null)
			return null;
		
		String editor = param.editor();					
		if(editor != null && !editor.equals(""))
		{
			return (EditorInf) BeanUtils.instantiateClass(editor);
		}
		
		return null;
	}
	public final static RequestParamWraper getWriterMethodRequestParam(Method writeMethod)
	{
		if(writeMethod == null)
			return null;
		Annotation[] annotations = writeMethod.getAnnotations();
		if(annotations == null || annotations.length == 0)
			return null;
		for(Annotation annotation:annotations)
		{
			if(annotation instanceof RequestParam)
			{
				RequestParam param = (RequestParam)annotation;
				return new RequestParamWraper(param);
			}
		}
		return null;
	}
	public static class PropertieDescription
	{
		private Class propertyType;		
		private Method writeMethod;
		
		private RequestParamWraper  writeMethodRequestParam;
		private EditorInf writeMethodEditor;
		private Method readMethod;
		private Field field;
		private String name;
		
		private boolean canwrite = false;
		private boolean canread = false;
		private boolean canseriable = true;
		private Class[] writeMethodPropertyGenericType;
		private Class propertyGenericType;
		private Annotation[] annotations;
		private String requestParamName;
		private String origineRequestParamName;
		private boolean namevariabled = false;
		private RequestParamWraper requestParam ;
		private AttributeWraper attribute;
		private PrimaryKey pk;
		private ColumnWraper column;
		private CookieValueWraper cookie;
		private RequestHeaderWraper header;
		private PathVariableWraper pathVariable;
		private RequestBodyWraper requestBody;
		private DataBind dataBind;
		private IgnoreBind ignoreBind;
		private IgnoreORMapping ignoreORMapping;
		private MapKey mapkey;
		public IgnoreBind getIgnoreBind() {
			return ignoreBind;
		}

		/**
		 * if (field.isAnnotationPresent(RequestBody.class)
				|| field.isAnnotationPresent(DataBind.class)
				|| field.isAnnotationPresent(PathVariable.class)
				|| field.isAnnotationPresent(RequestParam.class)
				|| field.isAnnotationPresent(Attribute.class)
				|| field.isAnnotationPresent(CookieValue.class)
				|| field.isAnnotationPresent(RequestHeader.class))
		 * @return
		 */
		public RequestParamWraper getRequestParam() {
			return requestParam;
		}

		public boolean isNamevariabled() {
			return namevariabled;
		}

		public String getOrigineRequestParamName() {
			return origineRequestParamName;
		}

		/**
		 * 参数名称由常量和变量部分组成，变量var中包含了变量对应的request参数名称和变量在整个参数名称中所处的位置
		 */
		private List<Var> requestParamNameToken;
		
		
		private boolean oldAccessible = false;
		private String uperName;
		public PropertieDescription(Class propertyType,Field field, Method writeMethod,Method readMethod,
				String name)
		{
			super();
			this.propertyType = propertyType;
			this.writeMethod = writeMethod;
			this.readMethod = readMethod;
			if(this.readMethod != null && !ReflectionUtils.isAccessible(readMethod))
			{
				this.readMethod = null;
			}
			
			if(this.writeMethod != null && !ReflectionUtils.isAccessible(writeMethod))
			{
				this.writeMethod = null;
			}
			this.name = name;
			if(name != null)
				this.uperName = name.toUpperCase();
			this.field = field;
			
			
			if(this.field != null)
				oldAccessible = this.field.isAccessible();
			if((writeMethod == null || this.readMethod == null))
			{
				if(this.field != null)
				{
					 int mode = this.field.getModifiers();
					 if( !Modifier.isStatic(mode))
					 {
						 if(!Modifier.isPublic(mode))
						 {
							 this.field.setAccessible(true);
							 
						 }
						 if(!Modifier.isFinal(mode))
							 canwrite = true;
						 canread = true;
						
					 }
					 if(Modifier.isPublic(mode))
					 {
						 canread = true;
						 if(!Modifier.isFinal(mode))
						 {
							 canwrite = true;
						 }
					 }
					
				}
			}
			
			if(!canread && readMethod != null)
				this.canread = true;
			
			if(!canwrite && writeMethod != null)
				this.canwrite = true;
			if(this.field != null )
			{
				 int mode = this.field.getModifiers();
				 if( Modifier.isFinal(mode) 
							|| Modifier.isStatic(mode) 
							|| Modifier.isTransient(mode) 
							|| findAnnotation(ExcludeField.class) != null)
				 {
					 canseriable = false;
				 }
			}
			
			if(canseriable && (readMethod == null || writeMethod == null) && this.field == null)
			{
				canseriable = false;
			}
			
			if(this.writeMethod != null)
			{
				this.writeMethodPropertyGenericType = ClassUtils.getPropertyGenericTypes(writeMethod);
			}
			else if(field != null)
			{
				this.writeMethodPropertyGenericType =  ClassUtils.genericTypes(this.field);
			}
			if(this.writeMethod != null)
			{
				writeMethodRequestParam = getWriterMethodRequestParam( writeMethod);
				try {
					this.writeMethodEditor = getParamEditor(writeMethodRequestParam);
				} catch (Exception e) {
					log.error("get writeMethodEditor error!", e);
				}
			}
			if(this.writeMethod != null)
			{
				this.propertyGenericType = ClassUtils.getPropertyGenericType(this.writeMethod);
			}
			else if(this.field != null)
			{
				this.propertyGenericType = ClassUtils.genericType(this.field);
			}
			if(this.field != null)
			{
				annotations = this.field.getAnnotations();
				initParam(this.field.getAnnotations());
			}
			
				
		}
		
		private void initParam(Annotation[] annotations)
		{
			if(annotations == null || annotations.length == 0)
				return;
			for(int i = 0; i < annotations.length; i ++)
			{
				Annotation a = annotations[i];
				if(a instanceof RequestParam)
				{
					requestParam = new RequestParamWraper((RequestParam)a);
					if(requestParam.name() == null || requestParam.name().equals(""))
					{
						this.requestParamName = name;
					}
					else
					{
						String name = requestParam.name();
						this.origineRequestParamName = name;
						int vstart = name.indexOf("${");
						if(vstart  < 0)
						{
							this.requestParamName = name;
						}
						else
						{
							this.namevariabled = true;
							this.requestParamNameToken = ParameterUtil.evalVars(vstart, name);
							
							
						}
					}
					break;
					
				}
				else if(a instanceof PrimaryKey)
				{
					pk = (PrimaryKey)a;
				}
				else if(a instanceof Column )
				{
					column = new ColumnWraper((Column )a);
				}
				else if(a instanceof Attribute )
				{
					attribute = new AttributeWraper((Attribute )a);
				}
				else if(a instanceof RequestHeader )
				{
					header = new RequestHeaderWraper((RequestHeader )a);
				}
				else if(a instanceof PathVariable )
				{
					pathVariable = new PathVariableWraper((PathVariable )a);
				}
				else if(a instanceof DataBind )
				{
					dataBind = ((DataBind )a);
				}
				else if(a instanceof RequestBody )
				{
					requestBody = new RequestBodyWraper(((RequestBody )a),propertyType);
				}
				else if(a instanceof CookieValue )
				{
					cookie = new CookieValueWraper ((CookieValue )a);
				}
				else if(a instanceof IgnoreBind)
				{
					this.ignoreBind = (IgnoreBind)a;
				}
				else if(a instanceof IgnoreORMapping)
				{
					this.ignoreORMapping = (IgnoreORMapping)a;
				}
				else if(a instanceof MapKey)
				{
					this.mapkey = (MapKey)a;
				}
			}
		}
		
		public Class[] getPropertyGenericTypes()
		{
//			if(this.writeMethod != null)
//			{
//				return ClassUtils.getPropertyGenericTypes(writeMethod);
//			}
//			else
//			{
//				return ClassUtils.genericTypes(this.field);
//			}
			return writeMethodPropertyGenericType;
		}
		
		public Class getPropertyGenericType()
		{
//			if(this.writeMethod != null)
//			{
//				return ClassUtils.getPropertyGenericType(writeMethod);
//			}
//			else
//			{
//				return ClassUtils.genericType(this.field);
//			}
			return this.propertyGenericType;
		}
		
		public <T extends Annotation> T findAnnotation(Class<T> type)
		{
			if(this.field != null)
				return (T)this.field.getAnnotation(type);
			return null;
		}
		
//		public Annotation[] findAnnotations()
//		{
////			if(this.field != null)
////				return this.field.getAnnotations();
////			return null;
//			return annotations;
//		}
		
		public boolean canread()
		{
			return canread;
		}
		
		public boolean canwrite()
		{
			return canwrite;
		}
		
		public boolean canseriable()
		{
			return canseriable;
		}
		
		
		
		
		public Object getValue(Object po) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
		{
			if(po == null)
				return null;
			if(this.readMethod != null)
				return this.readMethod.invoke(po);
			else if(this.field != null)
				return this.field.get(po);
			throw new IllegalAccessException("Get value for property["+this.name+"] failed:get Method or field not exist");
			
		}
		public void setValue(Object po,Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
		{
			if(po == null)
				return ;
			if(this.writeMethod != null)
			{
				this.writeMethod.invoke(po,value);
				
			}
			else if(this.field != null)
			{
				this.field.set(po,value);
			}
			else
			{
				throw new IllegalAccessException("Set value for property["+this.name+"] failed: set Method or field not exist.");
			}
			
		}
		
		
		public Class getPropertyType(){
			return propertyType;
		}
		public Method getWriteMethod(){
			return this.writeMethod;
		}
		
		
		public String getName(){
			return this.name;
		}

		
		public Method getReadMethod()
		{
		
			return readMethod;
		}

		public Field getField() {
			return field;
		}

		public void setWriteMethod(Method writeMethod) {
			this.writeMethod = writeMethod;
		}

		public void setReadMethod(Method readMethod) {
			this.readMethod = readMethod;
		}

		public String getRequestParamName() {
			return requestParamName;
		}

//		public Annotation[] getAnnotations() {
//			return annotations;
//		}

		public List<Var> getRequestParamNameToken() {
			return requestParamNameToken;
		}

		public PrimaryKey getPk() {
			return pk;
		}

		public ColumnWraper getColumn() {
			return column;
		}

		public String getUperName() {
			return uperName;
		}

		public Annotation[] getAnnotations() {
			return annotations;
		}

		public CookieValueWraper getCookie() {
			return cookie;
		}

		public RequestHeaderWraper getHeader() {
			return header;
		}

		public AttributeWraper getAttribute() {
			return attribute;
		}

		public RequestParamWraper getWriteMethodRequestParam() {
			return writeMethodRequestParam;
		}

		public EditorInf getWriteMethodEditor() {
			return writeMethodEditor;
		}

		public PathVariableWraper getPathVariable() {
			return pathVariable;
		}

		public DataBind getDataBind() {
			return dataBind;
		}

		public RequestBodyWraper getRequestBody() {
			return requestBody;
		}

		public MapKey getMapkey() {
			return mapkey;
		}

		public IgnoreORMapping getIgnoreORMapping() {
			return ignoreORMapping;
		}

	}
	public static class ClassInfo
	{
		/**
		 * declaredFields保存了类clazz以及父类中的所有属性字段定义，如果子类中和父类变量
		 * 重名，则安顺包含在数组中，这种情况是不允许的必须过滤掉，也就是说子类中有了和父类中相同签名的方法，则自动过滤掉
		 */
	    private volatile transient Field[] declaredFields;
	    
//	    private volatile transient Map<String ,PropertieDescription> propertyDescriptors;
	    private volatile transient List<PropertieDescription> propertyDescriptors;
	    /**
		 * declaredMethods保存了类clazz以及父类中的所有public方法定义，如果子类中和父类方法定义
		 * 像同(抽象方法实现，过载等等)，则安顺序包含在数组中，这种情况是不允许的必须过滤掉，
		 * 也就是说子类中有了和父类中相同签名的方法，则自动过滤掉
		 */
	    private volatile transient Method[] declaredMethods;
	    
	    private volatile transient Constructor defaultConstruction;
	    private volatile transient Constructor[] constructions;

	    private  Class<?> clazz;
	    private List<Class> superClasses; 
	    /**
	     * 识别class是否是基本数据类型或者基本数据类型数组
	     */
	    private boolean primary;
	    /**
	     * 识别class是否是基本数据类型
	     */
	    private boolean baseprimary;
	    public Constructor getConstructor(Class... paramTypes )
	    {
	    	try {
	    		if(paramTypes == null || paramTypes.length == 0)
	    			return this.defaultConstruction;
	    		for(int i = 0; constructions != null && i < this.constructions.length; i ++)
	    		{
	    			Constructor c = constructions[i];
	    			Class[] pt = c.getParameterTypes();
	    			if(pt.length == paramTypes.length)
	    			{
	    				int j = 0;
	    				for(; j < pt.length; j ++)
	    				{
	    					if(pt[j] != paramTypes[j])
	    						break;
	    				}
	    				if(j == pt.length)
	    					return c;
	    					
	    			}
	    		}
//				Constructor c = this.clazz.getDeclaredConstructor(paramTypes);
				
				//ReflectionUtils.makeAccessible(c);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return null;
	    }
	    public String getName()
	    {
	    	return this.clazz.getName();
	    }
	    private boolean cglib = false;
	    @SuppressWarnings("unchecked")
		private  ClassInfo(Class clazz){
	    	//处理cglib代理类，还原原始类型信息
	    	String name = clazz.getName();
	    	int idx = name.indexOf("$$EnhancerByCGLIB$$") ;
	    	if(idx < 0)
	    	{
	    		this.clazz = clazz;
	    	}
	    	else
	    	{
	    		this.clazz = clazz.getSuperclass();
	    		cglib = true;
	    	}
	    	try {
				defaultConstruction  = this.clazz.getDeclaredConstructor();
				if(defaultConstruction != null)
					ReflectionUtils.makeAccessible(defaultConstruction);
				
			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
			} 
	    	try {
				
				constructions = this.clazz.getDeclaredConstructors();
				for(int i = 0; constructions != null && i < constructions.length;i ++)
				{
					ReflectionUtils.makeAccessible(constructions[i]);
				}
			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
			} 
	    	this.init();
	    }
	    
	    public void setPropertyValue(Object obj,String property, Object value)
	    {
	    	try {
				this.getPropertyDescriptor(property).setValue(obj, value);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e.getTargetException());
			}
		    catch (Exception e) {
				throw new RuntimeException(e);
			}
	    }
	    
	    public Object getPropertyValue(Object obj,String property)
	    {
	    	try {
				return this.getPropertyDescriptor(property).getValue(obj);
	    	} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e.getTargetException());
			}
		    catch (Exception e) {
				throw new RuntimeException(e);
			}
	    }
	    private static final Field[] NULL = new Field[0];
	    private static final Method[] NULL_M = new Method[0];

		private static final List<PropertieDescription>	NULL_P	= new ArrayList<PropertieDescription>();
	 
	    private Object declaredMethodLock = new Object();
	    /**
	     * 获取类的公共方法数组，包括类以及父类的public方法
	     * @return
	     */
	    public Method[] getDeclaredMethods()
		{
	    	if(declaredMethods != null)
	    	{
	    		if(declaredMethods == NULL_M)
    				return null;
	    		return declaredMethods;
	    	}
	    	synchronized(declaredMethodLock)
	    	{
	    		if(declaredMethods != null)
		    	{
		    		if(declaredMethods == NULL_M)
	    				return null;
		    		return declaredMethods;
		    	}
		    	Method[] retmethods = null;
				try
				{		    				
					retmethods = getRecursiveDeclaredMehtods();		    			
				}
				catch(Exception e)
				{
					log.error(e.getMessage(),e);
				}
				if(retmethods == null)
					declaredMethods = NULL_M;
				else
					declaredMethods = retmethods;
	    	}
	    	if(declaredMethods == NULL_M)
    				return null;
    		return declaredMethods;
    		
		
		}
	    
	    private void init()
	    {
	    	
	    	this.primary = ValueObjectUtil.isPrimaryType(clazz);
	    	this.baseprimary = ValueObjectUtil.isBasePrimaryType(clazz);
	    	//初始化所有父类信息：
	    	if(superClasses == null)
	    	{
		    	superClasses = new ArrayList<Class> ();
		    	Class superclass = this.clazz.getSuperclass();
		    	while(superclass != null )
		    	{
		    		if(superclass == java.lang.Object.class)
		    			break;
		    		this.superClasses.add(superclass);
		    		superclass = superclass.getSuperclass();
		    	}
	    	}

			if(declaredFields == null)
    		{
				Field[] retfs = null;
    			try
    			{		    				
    				retfs = getRecursiveDeclaredFileds();
    				
    			}
    			catch(Exception e)
    			{
    				log.error(e.getMessage(),e);
//		    				declaredFields =NULL;
    			}
    			List<PropertieDescription> retpropertyDescriptors = null;
    			try
    			{		    				
    				retpropertyDescriptors = initBeaninfo(retfs);	 
    				
    			}
    			catch(Exception e)
    			{
    				log.error(e.getMessage(),e);
    				retpropertyDescriptors = NULL_P;
    			}
    			
    			this.propertyDescriptors = retpropertyDescriptors;
    			
    			if(retfs == null)
					declaredFields = NULL;
				else
					declaredFields = retfs;
    			
    		}
    		
	    	
	    	
	    }
	    public Field[] getDeclaredFields()
	    {
//	    	init();
    		if(declaredFields == NULL)
    			return null;
    		return declaredFields;    			
	    }
	    
	    public List<PropertieDescription> getPropertyDescriptors()
	    {
	    	return propertyDescriptors ;
	    }
	    /**
	     * 根据方法名称和方法参数类型判断是否是同一个方法
	     * @param method
	     * @param other
	     * @return
	     */
	    private boolean issamemethod(Method method,Method other)
	    {
	    	if(!method.getName().equals(other.getName()))
    			return false;
    		Class[] parameterTypes = method.getParameterTypes();
    		Class[] otherparameterTypes = other.getParameterTypes();
    		if((parameterTypes == null || parameterTypes.length == 0)  
    				&& (otherparameterTypes == null || otherparameterTypes.length == 0))
    			return true;
    		if(parameterTypes == null)
    		{
    			return false;
    		}
    		
    		if(otherparameterTypes == null)
    		{
    			return false;
    		}
    		
    		if(parameterTypes.length != otherparameterTypes.length)
    			return false;
    		for(int i = 0; i < parameterTypes.length; i ++)
    		{
    			if(parameterTypes[i] != otherparameterTypes[i])
    				return false;
    		}
    		return true;
	    }
	    private boolean containMethod(List<Method> lfs,Method method)
	    {
	    	if(lfs == null || lfs.size() == 0)
	    		return false;
	    	for(Method other:lfs)
	    	{
	    		if(issamemethod(method,other))
	    			return true;
	    	}
	    	return false;
	    }
	    
	    
	    
	    
	    private Method[] getRecursiveDeclaredMehtods()
	    {
	    	Method[] methods = null;
	    	List<Method> lfs = new ArrayList<Method>();
	    	Method m;
	    	Class clazz_super = clazz;
	    	do
	    	{
		    	try
		    	{
		    		methods = clazz_super.getMethods();	
		    		if(methods != null && methods.length > 0)
		    		{
		    			for(Method f:methods)
		    			{
		    				if(!containMethod(lfs,f))//过滤重载方法和抽象方法，或者接口方法
		    					lfs.add(f);
			    		}
		    		}
			    		
		    		clazz_super = clazz_super.getSuperclass();
		    		if(clazz_super == null || clazz_super == Object.class)
		    		{
		    			break;
		    		}
		    		
		    	}
		    	catch(Exception e)
		    	{
		    		clazz_super = clazz_super.getSuperclass();
		    		if(clazz_super == null)
		    			break;
		    	}
	    	}
	    	while(true);
	    	if(lfs.size() > 0)
	    	{
	    		methods = new Method[lfs.size()];
		    	for(int i = 0; i < lfs.size(); i ++)
		    	{
		    		methods[i] = lfs.get(i);
		    	}
	    	}
	    	return methods;
	    	
	    }
	    
	    private boolean issamefield(Field field,Field other)
	    {
	    	if(!field.getName().equals(other.getName()))
	    	{
	    		return false;
	    	}
	    	
	    	if(field.getType() != other.getType())
	    		return false;
	    	return true;
	    }
	    private boolean containField(List<Field> lfs,Field field)
	    {
	    	if(lfs == null || lfs.size() == 0)
	    		return false;
	    	for(Field other:lfs)
	    	{
	    		if(issamefield(field,other))
	    			return true;
	    	}
	    	return false;
	    }
	    private Field[] getRecursiveDeclaredFileds()
	    {
	    	Field[] fields = null;
	    	List<Field> lfs = new ArrayList<Field>();
	    	Class clazz_super = clazz;
	    	do
	    	{
		    	try
		    	{
		    		fields = clazz_super.getDeclaredFields();
		    		if(fields != null && fields.length > 0)
		    		{
		    			for(Field f:fields)
			    		{
		    				if(!containField(lfs,f))
		    					lfs.add(f);
			    		}
		    		}
		    		clazz_super = clazz_super.getSuperclass();
		    		if(clazz_super == null)
		    		{
		    			break;
		    		}
		    		
		    	}
		    	catch(Exception e)
		    	{
		    		clazz_super = clazz_super.getSuperclass();
		    		if(clazz_super == null)
		    			break;
		    	}
	    	}
	    	while(true);
	    	if(lfs.size() > 0)
	    	{
	    		fields = new Field[lfs.size()];
		    	for(int i = 0; i < lfs.size(); i ++)
		    	{
		    		fields[i] = lfs.get(i);
		    	}
	    	}
	    	return fields;
	    	
	    }
	    
	    public Method getDeclaredMethod(String name)
		{

	    	Method[] ret = getDeclaredMethods();
    	    if(ret == null)
    	    	return null;
    	    for(Method f:ret)
    	    {
    	    	if(f.getName().equals(name))
    	    		return f;
    	    }
    	    return null;
			
		}
	    public Field getDeclaredField(String name)
	    {
	    	    Field[] ret = this.getDeclaredFields();
	    	    if(ret == null)
	    	    	return null;
	    	    for(Field f:ret)
	    	    {
	    	    	if(f.getName().equals(name))
	    	    		return f;
	    	    }
	    	    return null;
	    	
	    		
	    }
	    
	    private Field getDeclaredField(Field[] declaredFields,String name,Class type)
	    {
//	    	    Field[] declaredFields = this.getDeclaredFields();
	    	    if(declaredFields == null)
	    	    	return null;
//	    	    for(Field f:declaredFields)
	    	    for(int i = declaredFields.length - 1; i >=0; i --)
	    	    {
	    	    	Field f = declaredFields[i];
	    	    	
	    	    	if(f.getName().equals(name) )
	    	    	{
	    	    		if( f.getType() == type )
	    	    			return f;
	    	    		
	    	    		
	    	    		 
	    	    	}
	    	    }
	    	    return null;
	    	
	    		
	    }
	    
	    
	    
	    private List<Field> copyFields(Field[] declaredFields)
	    {
	    	if(declaredFields == null || declaredFields.length == 0)
	    		return null;
	    	List<Field> copys = new ArrayList<Field>(declaredFields.length);
	    	for(int i =0;i < declaredFields.length; i++)
	    	{
	    		copys.add(declaredFields[i]);
	    	}
	    	return copys;
	    }
	    /**
	     * 如果包含名称为name的字段，由于该字段在BeanInfo中已经存在，则将该字段从fileds副本中移除，以便将
	     * 最后剩下的字段生成get/set方法
	     * @param name
	     * @param fields
	     * @return
	     */
	    private Field containFieldAndRemove(String name,List<Field> fields)
		{
	    	
			for(int i = 0; fields != null && i < fields.size(); i ++)
			{
				Field p = fields.get(i);
				if(p.getName().equals(name))
				{
					fields.remove(i);
					i --;
					return p;
				}
			}
			return null;
		}
	    
	    public  Class<?> getClazz()
	    {
	    	return this.clazz;
	    }
	    
	    private boolean containFieldInPropertyDescriptors(List<PropertieDescription> propertyDescriptors,Field field)
	    {
	    	if(propertyDescriptors == null || propertyDescriptors.size() == 0)
	    		return false;
	    	for(PropertieDescription p:propertyDescriptors)
	    	{
	    		if(p.getName().equalsIgnoreCase(field.getName()))
	    		{
	    			return true;
	    		}
	    	}
	    	return false;
	    }
	    
	    private void buildFieldPropertieDescriptions(List<PropertieDescription> propertyDescriptors,Field[] declaredFields)
	    {
	    	for(int i = 0; i < declaredFields.length  ;  i++)
			{
				Field f = declaredFields[i];
				if(containFieldInPropertyDescriptors(propertyDescriptors,f))
					continue;
				propertyDescriptors.add(buildPropertieDescription( f));
			}
	    }
	    
	    private void buildFieldPropertieDescriptions(List<PropertieDescription> propertyDescriptors,List<Field> declaredFields)
	    {
	    	for(int i = 0; i < declaredFields.size()  ;  i++)
			{
				Field f = declaredFields.get(i);
				if(containFieldInPropertyDescriptors(propertyDescriptors,f))
					continue;
				propertyDescriptors.add(buildPropertieDescription( f));
			}
	    }
	    private List<PropertieDescription> initBeaninfo(Field[] declaredFields)
	    {
	    	List<PropertieDescription> propertyDescriptors = null;
	    	
	    	BeanInfo beanInfo = null;
			try
			{
				beanInfo = Introspector.getBeanInfo(this.clazz);
				
				PropertyDescriptor[] attributes = beanInfo.getPropertyDescriptors();
//				List<PropertieDescription> asm = new ArrayList<PropertieDescription>();
				if(attributes == null || attributes.length == 0 ||
						(attributes.length == 1 && attributes[0]
								.getName()
								.equals("class")))
				{
					if(declaredFields == null || declaredFields.length == 0)
					{
						propertyDescriptors = NULL_P;
					}
					else
					{
						propertyDescriptors = new ArrayList<PropertieDescription>(declaredFields.length);
						buildFieldPropertieDescriptions( propertyDescriptors,declaredFields);

					}
					return propertyDescriptors;
				}
				else
				{
					List<Field> copyFields = copyFields(declaredFields);
					propertyDescriptors = new ArrayList<PropertieDescription>();
					
					for(int i = 0;  i < attributes.length; i ++)
					{		
						PropertyDescriptor attr = attributes[i];
						if(attr.getName().equals("class"))
							continue;
						PropertieDescription p = buildPropertieDescription(declaredFields, copyFields,attr);
						if(p != null)
						propertyDescriptors.add( p);
					}
					
					if(copyFields != null && copyFields.size() > 0)
					{
						List<PropertieDescription> propertyDescriptors_ = new ArrayList<PropertieDescription>(declaredFields.length);
						buildFieldPropertieDescriptions( propertyDescriptors_,copyFields);
						propertyDescriptors.addAll(propertyDescriptors_);

					}
//					if(asm.size() > 0)
//					{
//						this.clazz = AsmUtil.addGETSETMethodForClass(asm, this.clazz);
//					}
				}
				
				
				
			}
			catch (Exception e)
			{
				propertyDescriptors = NULL_P;
				log.error("Init Beaninfo[" + clazz.getName() + "] failed:",e);
			}
			return propertyDescriptors;
			
	    }
	    
	    private PropertieDescription buildPropertieDescription(Field[] declaredFields,List<Field> copeFields,PropertyDescriptor attr )
	    {
	    	Method wm = attr.getWriteMethod();
	    	Method rm = attr.getReadMethod();
	    	if(rm != null && (rm.getParameterTypes() != null && rm.getParameterTypes().length > 0))
	    	{
	    		rm = null;
	    	}
	    	
	    	if(wm != null && (wm.getParameterTypes() != null && wm.getParameterTypes().length > 1))
	    	{
	    		wm = null;
	    	}
	    	
	    	if(attr.getPropertyType() == null)
	    	{
	    		return null;
	    	}
	    	
	    	
	    	Field field = this.getDeclaredField(declaredFields,attr.getName(),attr.getPropertyType());
	    	
	    	this.containFieldAndRemove(attr.getName(), copeFields) ;
	    	
	    	
	    	PropertieDescription pd = new PropertieDescription(attr.getPropertyType(),
	    			                    field,wm,
	    								rm,attr.getName());
//	    	if(field != null && (wm == null || rm == null))
//	    		asm.add(pd);
	    	
	    	return pd;
	    }
	    
	    private PropertieDescription buildPropertieDescription(Field field )
	    {
//	    	Method wm = null;
//	    	Method rm = null;
	    	PropertieDescription pd = new PropertieDescription(field.getType(),
	    									field,null,
	    									null,field.getName());
//	    	asm.add(pd);
	    	return pd;
	    }
		public PropertieDescription getPropertyDescriptor(String name)
		{
//			this.init();
			if(propertyDescriptors != NULL_P)
			{
				for(int i = 0; i < this.propertyDescriptors.size(); i ++)
				{
					PropertieDescription p = this.propertyDescriptors.get(i);
					if(p.getName().equals(name))
						return p;
				}
    			return null;
			}
    		else
    			return null;
    		
		}

		public Constructor getDefaultConstruction() throws NoSuchMethodException {
			if(this.defaultConstruction != null)
				return defaultConstruction;
			throw new NoSuchMethodException(this.clazz.getName() + " do not define a default construction.");
		}

		public boolean isPrimary() {
			return primary;
		}

		public void setPrimary(boolean primary) {
			this.primary = primary;
		}

		public boolean isBaseprimary() {
			return baseprimary;
		}

		public Constructor[] getConstructions() {
			return constructions;
		}
		public List<Class> getSuperClasses() {
			return superClasses;
		}
		public boolean isCglib() {
			return cglib;
		}

		
		
	}
	
	private static  Map<Class,ClassInfo> classInfos = new HashMap<Class,ClassInfo>();
	private static Object lock = new Object();
	public static Field[] getDeclaredFields(Class clazz) throws SecurityException {
		ClassInfo classinfo = getClassInfo(clazz);
		return classinfo.getDeclaredFields();
       
    }
	
	
	public static Field getDeclaredField(Class clazz,String name) throws SecurityException {
		ClassInfo classinfo = getClassInfo(clazz);
		return classinfo.getDeclaredField(name);
       
    }
	
	public static PropertieDescription getPropertyDescriptor(Class clazz,String name)
	{
		ClassInfo classinfo = getClassInfo(clazz);
		return classinfo.getPropertyDescriptor(name);
	}
	
	public static ClassInfo  getClassInfo(Class clazz)
	{
		ClassInfo classinfo = classInfos.get(clazz);
		if(classinfo != null)
			return classinfo;
		synchronized(lock)
		{
			classinfo = classInfos.get(clazz);
			if(classinfo == null)
			{
				classinfo = new ClassInfo(clazz);
				classInfos.put(clazz, classinfo);
			}
		}
		return classinfo;
	}


	public static Method getDeclaredMethod(Class clazz,String name)
	{
		ClassInfo  csinfo = getClassInfo(clazz);
		if(csinfo == null)
			return null;
		// TODO Auto-generated method stub
		return csinfo.getDeclaredMethod(name);
	}

	
	public static Method[] getDeclaredMethods(Class target)	
	{
		ClassInfo  csinfo = getClassInfo(target);
		return csinfo.getDeclaredMethods();
	}
	
	public static String genJavaName(String tableColumn)
	{
		int idx = tableColumn.indexOf('_');
		if(idx == -1)
			return null;
		String tableColumn_ = tableColumn.toLowerCase();
		int length = tableColumn.length();
		StringBuffer ret = new StringBuffer();
		
		for(int i = 0; i < length; i ++)
		{
			
			char c = tableColumn_.charAt(i);
			if(i < idx)
			{
				ret.append(c);
				
			}
			else if(i > idx)
			{
				if(c == '_')
				{
					idx = i;
				}
				else if(i == idx + 1)
				{
					if(idx != 0)
						ret.append(String.valueOf(c).toUpperCase());
					else
						ret.append(c);
				}
				else 
				{
					ret.append(c);
				}
					
			}
			
		}
		return ret.toString();
	}
	
}
