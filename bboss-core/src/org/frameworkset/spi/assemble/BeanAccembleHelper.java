package org.frameworkset.spi.assemble;

import java.beans.IntrospectionException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.frameworkset.spi.ApplicationContextAware;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.BeanClassLoaderAware;
import org.frameworkset.spi.BeanInfoAware;
import org.frameworkset.spi.BeanNameAware;
import org.frameworkset.spi.CallContext;
import org.frameworkset.spi.DisposableBean;
import org.frameworkset.spi.InitializingBean;
import org.frameworkset.spi.LocalCallContextImpl;
import org.frameworkset.spi.ResourceLoaderAware;
import org.frameworkset.spi.SPIException;
import org.frameworkset.spi.support.ApplicationObjectSupport;
import org.frameworkset.spi.support.MessageSourceAware;
import org.frameworkset.util.ClassUtil;
import org.frameworkset.util.ClassUtil.ClassInfo;
import org.frameworkset.util.ClassUtil.PropertieDescription;

import com.frameworkset.spi.assemble.BeanInstanceException;
import com.frameworkset.spi.assemble.CurrentlyInCreationException;
import com.frameworkset.util.BeanUtils;
import com.frameworkset.util.EditorInf;
import com.frameworkset.util.NoSupportTypeCastException;
import com.frameworkset.util.ValueObjectUtil;

/**
 * 
 * <p>
 * Title: BeanAccembleHelper.java
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
 * @Date 2009-9-19 下午11:01:02
 * @author biaoping.yin
 * @version 1.0
 */
public class BeanAccembleHelper<V> {
	private static Logger log = Logger.getLogger(BeanAccembleHelper.class);

	private Class<?>[] constructParamTypes = null;

	private Constructor<V> constructor = null;
	
	public static class LoopObject
	{
		Object obj;
		public LoopObject()
		{
//			this.obj = obj;
		}
		public Object getObj() {
			return obj;
		}
		public void setObj(Object obj) {
			this.obj = obj;
		}
		
		
		
	}

	/**
	 * 获取属性的引用值
	 * 
	 * @param property
	 * @param context
	 * @param defaultValue
	 * @return
	 */
	public Object getRefValue(Pro property, CallContext callcontext,
			Object defaultValue) {
		if (callcontext != null )
		{
//			if(callcontext.isSOAApplication())
//			{
//				
//			}
//			else
			{
				Context context = callcontext.getLoopContext();
				
//				if (context != null && context.isLoopIOC(property.getRefid())) {
//					throw new CurrentlyInCreationException(
//							"loop inject constructor error. the inject context path is ["
//									+ context + ">" + property.getRefid()
//									+ "],请检查配置文件是否配置正确[" + property.getConfigFile()
//									+ "]");
//				}
				LoopObject lo = new LoopObject();
				boolean loopobject = context != null?context.isLoopIOC(property.getRefid(),lo):false;
				if (loopobject && lo.getObj() == null) {
					throw new CurrentlyInCreationException(
							"loop inject constructor error. the inject context path is ["
									+ context + ">" + property.getRefid()
									+ "],请检查配置文件是否配置正确[" + property.getConfigFile()
									+ "]");
				}
				if(lo.getObj() != null)
					return lo.getObj();
			}
		}
			
		
		if (!property.isServiceRef()) {//识别新的模式
			/**
			 * 需要对refid进行内部识别的处理
			 * test1->test2->test3
			 * 
			 */
			Object ret = null;
			if(property.getRefidLink() == null)
				ret = property.getApplicationContext().getBeanObject(
			                                        					callcontext, property.getRefid());
			else
				ret = property.getApplicationContext().getBeanObjectFromRefID(
    					callcontext, property.getRefidLink(),property.getRefid(),null);
			if (ret == null)
				return defaultValue;
			if (property.getClazz() != null && !property.getClazz().equals("")) {
				try {					
					ret = ValueObjectUtil.typeCast(ret, ret.getClass(),
							property.getType());
					return ret;
				} catch (NumberFormatException e) {
					throw new CurrentlyInCreationException(
							"providerManagerInfo[" + property.getName()
									+ "],请检查配置文件是否配置正确["
									+ property.getConfigFile() + "]", e);
				} catch (IllegalArgumentException e) {
					throw new CurrentlyInCreationException(
							"providerManagerInfo[" + property.getName()
									+ "],请检查配置文件是否配置正确["
									+ property.getConfigFile() + "]", e);
				} catch (NoSupportTypeCastException e) {
					throw new CurrentlyInCreationException(
							"providerManagerInfo[" + property.getName()
									+ "],请检查配置文件是否配置正确["
									+ property.getConfigFile() + "]", e);
				} catch (BeanInstanceException e) {
					throw e;
				} catch (Exception e) {
					throw new CurrentlyInCreationException(
							"providerManagerInfo[" + property.getName()
									+ "],请检查配置文件是否配置正确["
									+ property.getConfigFile() + "]", e);
				}
			} else {
				return ret;
			}
			
		} else {
			Object ret = property.getApplicationContext().getProvider(
			                                      					callcontext, property.getRefid(), property.getReftype());
  			if (ret == null)
  				return defaultValue;
  			return ret;
			                                      			
			
		}
	}
	
	/**
	 * 获取bean创建工厂对象
	 * 
	 * @param property
	 * @param context
	 * @param defaultValue
	 * @return
	 */
	public Object getFactoryRefValue(Pro property,String factoryname, CallContext callcontext,
			Object defaultValue) {
		Context context = null;
		if (callcontext != null)
			context = callcontext.getLoopContext();
//		Object loopobject = context != null?context.isLoopIOC(factoryname):null;
//		if (context != null && context.isLoopIOC(factoryname)) {
//			throw new CurrentlyInCreationException(
//					"loop inject constructor error. the inject context path is ["
//							+ context + ">" + property.getRefid()
//							+ "],请检查配置文件是否配置正确[" + property.getConfigFile()
//							+ "]");
//		}
		LoopObject lo = new LoopObject();
		boolean loopobject = context != null?context.isLoopIOC(factoryname,lo):false;
		if (loopobject && lo.getObj() == null) {
			throw new CurrentlyInCreationException(
					"loop inject constructor error. the inject context path is ["
							+ context + ">" + factoryname
							+ "],请检查配置文件是否配置正确[" + property.getConfigFile()
							+ "]");
		}
		if(lo.getObj() != null)
			return lo.getObj();
		
		Object ret = property.getApplicationContext().getBeanObject(
				callcontext, factoryname);
		if (ret == null)
		{
			return defaultValue;
		
		} else {
			return ret;
		}
		
	}

	@SuppressWarnings("unchecked")
	public Class<?>[] getParamsTypes(List<Pro> params) {
		if (constructParamTypes != null)
			return constructParamTypes;
		synchronized (this) {
			if (constructParamTypes != null)
				return constructParamTypes;
			if (params == null || params.size() == 0)
				return new Class<?>[0];
			constructParamTypes = new Class<?>[params.size()];
			int i = 0;
			for (Pro pro : params) {

				if (pro.getType() == null)
					return null;
				constructParamTypes[i] = pro.getType();

				i++;
			}
		}
		return constructParamTypes;
	}

	// @SuppressWarnings("unchecked")
	// public Constructor<V> getConstructor(Class<V> clazz, Class[] params)
	// {
	// try
	// {
	// if (params == null || params.length == 0)
	// {
	// return null;
	//
	// }
	//
	// // Class[] params_ = this.getParamsTypes(params);
	// Constructor<V> constructor = null;
	// // if (params_ != null)
	// constructor = clazz.getConstructor(params);
	//            
	// return constructor;
	// }
	// catch (NoSuchMethodException e)
	// {
	// Constructor<V>[] constructors = clazz.getConstructors();
	// if (constructors == null || constructors.length == 0)
	// throw new
	// CurrentlyInCreationException("Inject constructor error: no construction
	// define in the "
	// + clazz + ",请检查配置文件是否配置正确,参数个数是否正确.");
	// int l = constructors.length;
	// int size = params.length;
	// Class[] types = null;
	// Constructor fault_ = null;
	// for (int i = 0; i < l; i++)
	// {
	// Constructor temp = constructors[i];
	// types = temp.getParameterTypes();
	// if (types != null && types.length == size)
	// {
	// if(fault_ == null)
	// fault_ = temp;
	// if(isSameTypes(types,params))
	// return constructor = temp;
	// }
	//                
	// }
	// if(fault_ != null)
	// return fault_;
	// throw new CurrentlyInCreationException(
	// "Inject constructor error: Parameters with construction defined in the "
	// + clazz
	// +
	// " is not matched with the config paramenters .请检查配置文件是否配置正确,参数个数是否正确.");
	//
	//                        
	//            
	// // TODO Auto-generated catch block
	// // throw new BeanInstanceException("Inject constructor error:"
	// +clazz.getName(),e);
	// }
	// catch (Exception e)
	// {
	// // TODO Auto-generated catch block
	// throw new BeanInstanceException("Inject constructor error:"
	// +clazz.getName(),e);
	// }
	//
	//        
	// }

	// /**
	// *
	// * @param types 构造函数的参数类型
	// * @param params 外部传入的形式参数类型
	// * @return
	// */
	// private boolean isSameTypes(Class[] types, Class[] params)
	// {
	//        
	// if(types.length != params.length)
	// return false;
	// for(int i = 0; i < params.length; i ++)
	// {
	//            
	//            
	// // if(!ValueObjectUtil.isSameType(type, toType))
	// if(!ValueObjectUtil.isSameType(params[i], types[i]))
	// {
	// return false;
	// }
	//            
	// }
	// return true;
	// }

	public static Object[] getValue2ndTypes(List<Pro> params, CallContext context) {
		if (params == null || params.size() == 0) {
			return null;
		}

		Object[] valuetypes = new Object[2];
		Object[] values = new Object[params.size()];
		Class[] types = new Class[params.size()];
		valuetypes = new Object[] { values, types };
		Context currentLoopContext = context !=null ?context.getLoopContext():null;
		for (int i = 0; i < params.size(); i++) {
			Pro param = params.get(i);
			Object refvalue = null;
			if (param.getClazz() != null && !param.getClazz().equals("")) {
				try {
					types[i] = getClass(param.getClazz());
				} catch (Exception e) {
				}
			}
			try
			{
//				refvalue = param.getTrueValue(context);
				refvalue = param.getApplicationContext().proxyObject(param, 
						param.getTrueValue(context), 
						param.getXpath());
			}
			finally
			{
				if(context !=null)
					context.setLoopContext(currentLoopContext);
			}

			if (refvalue != null) {
				values[i] = refvalue;
				if (types[i] == null) {
					types[i] = refvalue.getClass();
				}
			} else {
				if (types[i] == null) {
					types[i] = Object.class;
				}
			}

			

		}
		return valuetypes;
	}
	
	

	/**
	 * 
	 * @param providerManagerInfo
	 * @param context
	 * @param ignoreconstruction 工厂模式实例化工厂实例时需要忽略构造函数，因为构造函数是作为工厂方法创建组件实例的参数
	 * @return
	 */
	private V initbean(BeanInf providerManagerInfo, CallContext context,boolean ignoreconstruction) {
		try {
			
			Class<V> cls = null;
			ClassInfo classInfo = null;
			if(!ignoreconstruction)
			{
				cls = providerManagerInfo.getBeanClass();
				classInfo = ClassUtil.getClassInfo(cls);
			}
			else
			{
				cls = providerManagerInfo.getFactoryClass();
				classInfo = ClassUtil.getClassInfo(cls);
				return (V)context.getLoopContext().setCurrentObj(classInfo.getDefaultConstruction().newInstance());
			}
			
			if (providerManagerInfo.getConstruction() == null
					|| providerManagerInfo.getConstructorParams() == null
					|| providerManagerInfo.getConstructorParams().size() == 0 ) {
				
				return (V)context.getLoopContext().setCurrentObj(classInfo.getDefaultConstruction().newInstance());
			}
			List<Pro> params = providerManagerInfo.getConstructorParams();
			Object[] valuetypes = getValue2ndTypes(params, context);
			Object[] values = (Object[]) valuetypes[0];
			Class[] types = (Class[]) valuetypes[1];
			if (constructor == null) {
				synchronized (this) {
					if (constructor == null) {
						constructor = ValueObjectUtil.getConstructor(cls,
								types, values);
					}
				}
			}

			Class<?>[] parameterTypes = constructor.getParameterTypes();
			
			for (int i = 0; i < parameterTypes.length; i++) {

				Object refvalue = values[i];
				if(refvalue != null)
				{
					if (!parameterTypes[i].isInstance(refvalue)) {
						if (refvalue != null) {
							refvalue = ValueObjectUtil.typeCast(refvalue, refvalue
									.getClass(), parameterTypes[i]);
							values[i] = refvalue;
						}
	
					} else {
	
					}
				}
				else
				{
					values[i] = ValueObjectUtil.getDefaultValue(parameterTypes[i]);
				}
					

			}

			

			return (V)context.getLoopContext().setCurrentObj(constructor.newInstance(values));
		} catch (InstantiationException e) {
			throw new BeanInstanceException("providerManagerInfo["
					+ providerManagerInfo.getName() + "],请检查配置文件是否配置正确["
					+ providerManagerInfo.getConfigFile() + "]", e);
		} catch (IllegalAccessException e) {
			throw new BeanInstanceException("providerManagerInfo["
					+ providerManagerInfo.getName() + "],请检查配置文件是否配置正确["
					+ providerManagerInfo.getConfigFile() + "]", e);
		}
		catch(CurrentlyInCreationException e)
		{
			throw e;
		}
		catch(BeanInstanceException e)
		{
			throw e;
		} catch (Exception e) {
			throw new BeanInstanceException("providerManagerInfo["
					+ providerManagerInfo.getName() + "],请检查配置文件是否配置正确["
					+ providerManagerInfo.getConfigFile() + "]", e);
		}
	}

	public static Class<?> getClass(String type) throws ClassNotFoundException {
		if (type == null)
			return null;
		if (type.equals("int"))
			return int.class;
		else if (type.equals("string") || type.equals("String"))
			return String.class;
		else if (type.equals("boolean"))
			return boolean.class;
		else if (type.equals("double"))
			return double.class;
		else if (type.equals("float"))
			return float.class;
		else if (type.equals("short"))
			return short.class;
		else if (type.equals("char"))
			return char.class;
		else if (type.equals("long"))
			return long.class;
		else if (type.equals("Long"))
			return Long.class;

		else if (type.equals("Boolean"))
			return Boolean.class;
		else if (type.equals("Double"))
			return Double.class;
		else if (type.equals("Float"))
			return Float.class;
		else if (type.equals("Short"))
			return Short.class;
		else if (type.equals("Char") || type.equals("Character")
				|| type.equals("character"))
			return Character.class;
		Class<?> Type = Class.forName(type);
		return Type;
	}

	// private V initbean(Pro<V> providerManagerInfo, Context context)
	// {
	// try
	// {
	// Class<V> cls = providerManagerInfo.getType();
	// if (providerManagerInfo.getConstruction() == null ||
	// providerManagerInfo.getConstructorParams() == null
	// || providerManagerInfo.getConstructorParams().size() == 0)
	// {
	// return cls.newInstance();
	// }
	//
	// List<Pro> params = providerManagerInfo.getConstructorParams();
	// Class<?>[] parameterTypes = this.getParamsTypes(params);
	// Constructor<V> constructor = cls.getConstructor(parameterTypes);
	// // constructor.newInstance(initargs);
	//
	// Object[] values = new Object[parameterTypes.length];
	//
	// for (int i = 0; i < parameterTypes.length; i++)
	// {
	// Pro param = params.get(i);
	// Object refvalue = null;
	// if (param.isBean())
	// {
	// refvalue = BaseSPIManager.getBeanObject(context, param);
	// }
	// else if (param.isRefereced())
	// {
	// refvalue = BaseSPIManager.getBeanObject(context, param);
	// }
	// else
	// {
	// refvalue = param.getObject();
	// }
	// values[i] = refvalue;
	//
	// }
	// return constructor.newInstance(values);
	// }
	// catch (InstantiationException e)
	// {
	// throw new BeanInstanceException(e);
	// }
	// catch (IllegalAccessException e)
	// {
	// throw new BeanInstanceException(e);
	// }
	//
	// catch (Exception e)
	// {
	// throw new BeanInstanceException(e);
	// }
	// }

	public static void injectProperties(Object bean,
			Map<String, Pro> globalparams) throws IntrospectionException {
		injectProperties(bean, globalparams, null);
	}

	public static void injectCommonProperties(Object bean,
			Map<String, Object> globalparams) throws IntrospectionException {
		injectCommonProperties(bean, globalparams, null);
	}

	public static void injectCommonProperties(Object bean,
			Map<String, Object> globalparams,
			Map<String, Object> persistentparams) throws IntrospectionException {
//		BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
//		PropertyDescriptor[] attributes = beanInfo.getPropertyDescriptors();
		ClassInfo beanInfo = ClassUtil.getClassInfo(bean.getClass());
		List<PropertieDescription> attributes = beanInfo.getPropertyDescriptors();
		if (globalparams == null || globalparams.size() == 0) {
			return;
		}
		boolean persistentparams_ = persistentparams != null
				&& persistentparams.size() > 0;
		Set sets = globalparams.keySet();
		Iterator<String> it = sets.iterator();
		while (it.hasNext()) {
			String key = it.next();
			Object pvalue = globalparams.get(key);

			String filedName = key;
			try {

				if (persistentparams_) {
					pvalue = persistentparams.get(key);
				}

				boolean flag = false;
				for (int n = 0; attributes != null && n < attributes.size(); n++) {

					// get bean attribute name
					PropertieDescription propertyDescriptor = attributes.get(n);
					String attrName = propertyDescriptor.getName();

					if (filedName.equals(attrName)) {
						flag = true;

						Class type = propertyDescriptor.getPropertyType();

						// create attribute value of correct type
						Object value = null;

						value = ValueObjectUtil.typeCast(pvalue, pvalue
								.getClass(), type);

						// PropertyEditor editor =
						// PropertyEditorManager.findEditor(type);
						// editor.setAsText(ref.getValue());
						// Object value = editor.getValue();
//						Method wm = propertyDescriptor.getWriteMethod();

						try {
							if(propertyDescriptor.canwrite())
							{
								propertyDescriptor.setValue(bean, value);
								break;
							}
							else
							{
								log.warn("设置组件["+bean.getClass()+"]属性失败：Does not exist a writer method for field ["
										+ propertyDescriptor.getName() +"] ,请检查类定义文件是否正确设置了该字段的set方法，或者字段名称是否指定正确。");
								flag = false;
								break;
							}
//							if(wm == null)
//							{
//								log.warn("设置组件["+bean.getClass()+"]属性失败：Does not exist a writer method for field ["
//										+ propertyDescriptor.getName() +"] ,请检查类定义文件是否正确设置了该字段的set方法，或者字段名称是否指定正确。");
//								flag = false;
//								break;
//							}
//							wm.invoke(bean, new Object[] { value });
//							break;
						} catch (IllegalArgumentException e) {
							throw new CurrentlyInCreationException(
									"providerManagerInfo[" + bean.getClass()
											+ "]", e);
						} catch (IllegalAccessException e) {
							throw new CurrentlyInCreationException(
									"providerManagerInfo[" + bean.getClass()
											+ "]", e);
						}
						catch(CurrentlyInCreationException e)
						{
							throw e;
						}
						catch(InvocationTargetException e)
						{
							throw new CurrentlyInCreationException(e.getTargetException());
						}
						catch (Exception e) {
							throw new CurrentlyInCreationException(
									"providerManagerInfo[" + bean.getClass()
											+ "]", e);
						}
						// Object value = editor.getValue();
						// set attribute value on bean

					}
				}

				if (!flag) // 引用字段名称在provider中没有定义
				{
					// System.out.println("引用字段[" + filedName + "]在provider[" +
					// bean.getClass() + "]中没有定义");
//					log.warn("引用字段[" + filedName + "]在组件["
//							+ bean.getClass() + "]中没有定义");
					Exception e = new Exception("引用字段[" + filedName + "]在组件["
							+ bean.getClass() + "]中没有定义");
					log.warn("", e);
				}
			} 
			catch(CurrentlyInCreationException e)
			{
				throw e;
			}
			catch(BeanInstanceException e)
			{
				throw e;
			}
			
			catch (NumberFormatException e) {
				throw new CurrentlyInCreationException("injectProperties["
						+ bean.getClass() + "]", e);
			} catch (IllegalArgumentException e) {
				throw new CurrentlyInCreationException("injectProperties["
						+ bean.getClass() + "]", e);
			} catch (NoSupportTypeCastException e) {
				throw new CurrentlyInCreationException("injectProperties["
						+ bean.getClass() + "]", e);
			} catch (Exception e) {
				throw new CurrentlyInCreationException("injectProperties["
						+ bean.getClass() + "]", e);
			}

		}

	}

	public static void injectProperties(Object bean,
			Map<String, Pro> globalparams, Map<String, Pro> persistentparams)
			throws IntrospectionException {
//		BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
//		PropertyDescriptor[] attributes = beanInfo.getPropertyDescriptors();
		ClassInfo beanInfo = ClassUtil.getClassInfo(bean.getClass());
		List<PropertieDescription> attributes = beanInfo.getPropertyDescriptors();
		if (globalparams == null || globalparams.size() == 0) {
			return;
		}
		boolean persistentparams_ = persistentparams != null
				&& persistentparams.size() > 0;
		Set sets = globalparams.keySet();
		Iterator<String> it = sets.iterator();
		while (it.hasNext()) {
			String key = it.next();
			Pro pro = globalparams.get(key);
			String clazz = pro.getClazz();
			String filedName = pro.getName();
			try {
				Class ptype = BeanAccembleHelper.getClass(clazz);

				Object pvalue = pro.getObject();
				EditorInf editor = pro.getEditorInf();
				if (persistentparams_) {
					Pro newValue = persistentparams.get(pro.getName());
					if (newValue != null) {
						if (newValue.getEditorInf() == null && editor != null) {
							pvalue = editor.getValueFromObject(newValue
									.getObject());
						} else {
							pvalue = newValue.getObject();
						}
					}

				}

				if (ptype != null && editor == null)
					pvalue = ValueObjectUtil.typeCast(pvalue,
							pvalue.getClass(), ptype);

				boolean flag = false;
				for (int n = 0; n < attributes.size(); n++) {

					// get bean attribute name
					PropertieDescription propertyDescriptor = attributes.get(n);
					String attrName = propertyDescriptor.getName();

					if (filedName.equals(attrName)) {
						flag = true;

						Class type = propertyDescriptor.getPropertyType();

						// create attribute value of correct type
						Object value = null;
						if (editor == null) {
							value = ValueObjectUtil.typeCast(pvalue, pvalue
									.getClass(), type);
						} else {
							value = pvalue;
							// value = ValueObjectUtil.typeCast(pvalue,
							// pro.getEditorInf());
						}
						// PropertyEditor editor =
						// PropertyEditorManager.findEditor(type);
						// editor.setAsText(ref.getValue());
						// Object value = editor.getValue();
						

						try {
							if(propertyDescriptor.canwrite())
							{
//								Method wm = propertyDescriptor.getWriteMethod();
//								if(wm == null)
//								{
//									log.warn("初始化组件["+bean.getClass()+"]失败：Does not exist a writer method for field ["
//											+ propertyDescriptor.getName() +"] ,请检查类定义文件是否正确设置了该字段的set方法，或者字段名称是否指定正确。");
//									flag = false;
//									break;
//								}
//								wm.invoke(bean, new Object[] { value });
								propertyDescriptor.setValue(bean, value);
								break;
							}
							else
							{
								log.warn("初始化组件["+bean.getClass()+"]失败：Does not exist a writer method for field ["
										+ propertyDescriptor.getName() +"] ,请检查类定义文件是否正确设置了该字段的set方法，或者字段名称是否指定正确。");
								flag = false;
								break;
							}
							
						} catch (IllegalArgumentException e) {
							throw new CurrentlyInCreationException(
									pro.getName()+"@" + pro.getConfigFile() + "，providerManagerInfo[" + bean.getClass()
											+ "]", e);
						} 
						catch(CurrentlyInCreationException e)
						{
							throw e;
						}
						catch(InvocationTargetException e)
						{
							throw new CurrentlyInCreationException(e.getTargetException());
						}
						catch (IllegalAccessException e) {
							throw new CurrentlyInCreationException(
									pro.getName()+"@" + pro.getConfigFile() + "，providerManagerInfo[" + bean.getClass()
											+ "]", e);
						} 
						catch (Exception e) {
							throw new CurrentlyInCreationException(
									pro.getName()+"@" + pro.getConfigFile() + "，providerManagerInfo[" + bean.getClass()
											+ "]", e);
						}
						// Object value = editor.getValue();
						// set attribute value on bean

					}
				}

				if (!flag) // 引用字段名称在provider中没有定义
				{
					// System.out.println("引用字段[" + filedName + "]在provider[" +
					// bean.getClass() + "]中没有定义");
//					log.warn("引用字段[" + filedName + "]在provider["
//							+ bean.getClass() + "]中没有定义");
					Exception e = new Exception("引用字段[" + filedName + "]在组件["
							+ bean.getClass() + "]中没有定义,请检查配置文件是否配置正确["
											+ pro
													.getConfigFile() + "]");
					log.warn("", e);
				}
			}
			catch(CurrentlyInCreationException e)
			{
				throw e;
			}
			catch(BeanInstanceException e)
			{
				throw e;
			}catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				throw new CurrentlyInCreationException(pro.getName()+"@" + pro.getConfigFile() + "，injectProperties["
						+ bean.getClass() + "]", e);
			} catch (NumberFormatException e) {
				throw new CurrentlyInCreationException(pro.getName()+"@" + pro.getConfigFile() + "，injectProperties["
						+ bean.getClass() + "]", e);
			} catch (IllegalArgumentException e) {
				throw new CurrentlyInCreationException(pro.getName()+"@" + pro.getConfigFile() + "，injectProperties["
						+ bean.getClass() + "]", e);
			} catch (NoSupportTypeCastException e) {
				throw new CurrentlyInCreationException(pro.getName()+"@" + pro.getConfigFile() + "，injectProperties["
						+ bean.getClass() + "]", e);
			} catch (Exception e) {
				throw new CurrentlyInCreationException(pro.getName()+"@" + pro.getConfigFile() + "，injectProperties["
						+ bean.getClass() + "]", e);
			}

		}

	}
	/**
	 * 通过工厂方法获取组件实例
	 * @param providerManagerInfo
	 * @param callcontext
	 * @return
	 */
	private V getBeanFromFactory(BeanInf providerManagerInfo, CallContext callcontext)
	{
		Pro pro = (Pro)providerManagerInfo;
		String factoryMethod = pro.getFactory_method();
		if(factoryMethod == null || factoryMethod.equals(""))
			throw new CurrentlyInCreationException(pro.getName()+"@" + pro.getConfigFile() + "，没有指定工厂创建方法。请检查[factory-method]属性配置是否正确。");
		if(pro.getFactory_bean() != null)//使用工厂bean创建组件实例
		{
			//获取工厂对象
			String beanname = providerManagerInfo.getFactory_bean();
			Object factory = this.getFactoryRefValue(pro, beanname, callcontext, null);
			if(factory == null)
			{
				throw new CurrentlyInCreationException(pro.getName()+"@" + pro.getConfigFile() + "，没有找到对象工厂["+beanname+"]。请检查[factory-bean]属性配置是否正确。");
			}
			//通过工厂对象创建组件实例
			//1.构建工厂创建路径上下文，防止工厂对象在创建组件实例时产生循环依赖
			Context context = null;
			if (callcontext == null)
				callcontext = new LocalCallContextImpl(providerManagerInfo
						.getApplicationContext());
			if (callcontext.getLoopContext() == null) {
				context = new Context(providerManagerInfo.getXpath());
				callcontext.setLoopContext(context);
			} else {
				context = new Context(callcontext.getLoopContext(),
						providerManagerInfo.getXpath());
				callcontext.setLoopContext(context);
			}
			//2.创建组件实例
			V bean  = creatorBeanByFactoryBean(pro, factory,factoryMethod,callcontext);
			context.setCurrentObj(bean);
			return bean;
		}
		else//使用工厂类静态方法创建组件实例
		{
			String factoryClass = pro.getFactory_class();
			try {
				Class cls = Class.forName(factoryClass);
				//通过工厂对象创建组件实例
				//1.构建工厂创建路径上下文，防止工厂对象在创建组件实例时产生循环依赖
				Context context = null;
				if (callcontext == null)
					callcontext = new LocalCallContextImpl(providerManagerInfo
							.getApplicationContext());
				if (callcontext.getLoopContext() == null) {
					context = new Context(providerManagerInfo.getXpath());
					callcontext.setLoopContext(context);
				} else {
					context = new Context(callcontext.getLoopContext(),
							providerManagerInfo.getXpath());
					callcontext.setLoopContext(context);
				}
				V bean  = creatorBeanByFactoryClass(pro, cls, factoryMethod,callcontext);
//				context.getLoopContext().setCurrentObj(bean);
				return bean;
			}
			catch(CurrentlyInCreationException e)
			{
				throw e;
			}
			catch(BeanInstanceException e)
			{
				throw e;
			}
			catch (Exception e) {
				throw new CurrentlyInCreationException(pro.getName()+"@" + pro.getConfigFile() + "，没有找到对象工厂["+factoryClass+"]类。请检查[factory-class]属性配置是否正确。",e);
			}
			
			
		}
	}
	
	/**
	 * 通过工厂对象创建组件实例
	 * @param providerManagerInfo
	 * @param factory
	 * @param context
	 * @return
	 */
	private V creatorBeanByFactoryBean(Pro  providerManagerInfo, Object factory,String factoryMethod,CallContext context)
	{
		try {
			Class cls = (Class) factory.getClass();
			
			if (providerManagerInfo.getConstruction() == null
					|| providerManagerInfo.getConstructorParams() == null
					|| providerManagerInfo.getConstructorParams().size() == 0) {
				Method method = cls.getMethod(factoryMethod,null);
				if(method == null)
				{
					throw new CurrentlyInCreationException(providerManagerInfo.getName()+"@" + providerManagerInfo.getConfigFile() + ",对象工厂["+factory.getClass().getName()+"]没有定义"+factoryMethod+"方法。请检查[factory-method]属性配置是否正确。");
				}
				/**
				 * 本地线程技术，存放CallContext上下文
				 * 如果有旧的还需要保存旧的callContext
				 */
//				return (V) method.invoke(factory, null);
				return invokerMethod(  providerManagerInfo,factory,method,null,context);
				
				/**
				 * 清理本地线程
				 * 如果有旧的callContext，还需要恢复旧的上下文
				 */
			}
			List<Pro> params = providerManagerInfo.getConstructorParams();
			Object[] valuetypes = getValue2ndTypes(params, context);
			Object[] values = (Object[]) valuetypes[0];
			Class[] types = (Class[]) valuetypes[1];
			Method method = getMethod(providerManagerInfo, cls, factoryMethod, types, values, false);
					

			Class<?>[] parameterTypes = method.getParameterTypes();

			for (int i = 0; i < parameterTypes.length; i++) {
				Object refvalue = values[i];
				if (!parameterTypes[i].isInstance(refvalue)) {
					if (refvalue != null) {
						refvalue = ValueObjectUtil.typeCast(refvalue, refvalue
								.getClass(), parameterTypes[i]);
						values[i] = refvalue;
					}
				} else {

				}

			}
			/**
			 * 采用本地线程技术存放CallContext上下文
			 * 如果有旧的还需要保存旧的callContext
			 */
			
//			V bean =  (V)method.invoke(factory,values);
			V bean = invokerMethod(  providerManagerInfo,factory,method,values,context);
			return bean;
			
			/**
			 * 清理本地线程
			 * 如果有旧的callContext，还需要恢复旧的上下文
			 */
			
			
		}  catch (IllegalAccessException e) {
			throw new BeanInstanceException("providerManagerInfo["
					+ providerManagerInfo.getName() + "],请检查配置文件是否配置正确["
					+ providerManagerInfo.getConfigFile() + "]", e);
		}
		catch(CurrentlyInCreationException e)
		{
			throw e;
		}
		catch(InvocationTargetException e)
		{
			throw new BeanInstanceException(e.getTargetException());
		}
		catch (BeanInstanceException e) {
			throw e;
		} catch (Exception e) {
			throw new BeanInstanceException("providerManagerInfo["
					+ providerManagerInfo.getName() + "],请检查配置文件是否配置正确["
					+ providerManagerInfo.getConfigFile() + "]", e);
		}
	}
	
	
	/**
	 * 通过工厂对象创建组件实例
	 * @param providerManagerInfo
	 * @param factory
	 * @param context
	 * @return
	 */
	public static  MethodInvoker creatorMethodInvokerByBean( Pro providerManagerInfo, String beanName,String method)
	{
		try {
			Object instance  = providerManagerInfo.getApplicationContext().getBeanObject(beanName);
			Class cls = instance.getClass();
			
			if (providerManagerInfo.getConstruction() == null
					|| providerManagerInfo.getConstructorParams() == null
					|| providerManagerInfo.getConstructorParams().size() == 0) {
				Method method_ = cls.getMethod(method,null);
				if(method == null)
				{
					throw new CurrentlyInCreationException(providerManagerInfo.getName()+"@" 
							+ providerManagerInfo.getConfigFile() + ",对象工厂["+cls.getName()+"]没有定义"+method+"方法。请检查[factory-method]属性配置是否正确。");
				}
				
				/**
				 * 本地线程技术，存放CallContext上下文
				 * 如果有旧的还需要保存旧的callContext
				 */
//				return (V) method.invoke(factory, null);
				return new MethodInvoker(false, instance, null, method_,providerManagerInfo);
				
				/**
				 * 清理本地线程
				 * 如果有旧的callContext，还需要恢复旧的上下文
				 */
			}
			List<Pro> params = providerManagerInfo.getConstructorParams();
			Object[] valuetypes = getValue2ndTypes(params, null);
			Object[] values = (Object[]) valuetypes[0];
			Class[] types = (Class[]) valuetypes[1];
			Method method_ = getMethod(providerManagerInfo, cls, method, types, values, false);
					

			Class<?>[] parameterTypes = method_.getParameterTypes();

			for (int i = 0; i < parameterTypes.length; i++) {
				Object refvalue = values[i];
				if (!parameterTypes[i].isInstance(refvalue)) {
					if (refvalue != null) {
						refvalue = ValueObjectUtil.typeCast(refvalue, refvalue
								.getClass(), parameterTypes[i]);
						values[i] = refvalue;
					}
				} else {

				}

			}
	

			return new MethodInvoker(false, instance, values, method_,providerManagerInfo);
			
			
			
		} 
		catch(CurrentlyInCreationException e)
		{
			throw e;
		}
		
		catch (BeanInstanceException e) {
			throw e;
		} catch (Exception e) {
			throw new BeanInstanceException("providerManagerInfo["
					+ providerManagerInfo.getName() + "],请检查配置文件是否配置正确["
					+ providerManagerInfo.getConfigFile() + "]", e);
		}
	}
	
	private V invokerMethod(Pro  providerManagerInfo,Object bean,Method method,Object[] params,CallContext context) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		try {
			/**
			 * 本地线程技术，存放CallContext上下文
			 * 如果有旧的还需要保存旧的callContext
			 */
			return (V) method.invoke(bean, params);
		} 
		finally
		{
			/**
			 * 清理本地线程
			 * 如果有旧的callContext，还需要恢复旧的上下文
			 */
		}
		
		
		
	}
	
	
	/**
	 * 通过工厂类创建组件实例,工厂方法如果是静态的直接调用静态方法创建所需组件；
	 * 如果是非静态的安照普通组件处理，即先获取工厂对象实例，然后再创建组件
	 * @param providerManagerInfo
	 * @param factory
	 * @param context
	 * @return
	 */
	private V creatorBeanByFactoryClass(Pro  providerManagerInfo, Class factoryClass,String factoryMethod,CallContext context)
	{
		try {
			Class cls = factoryClass;
			
			if (providerManagerInfo.getConstruction() == null
					|| providerManagerInfo.getConstructorParams() == null
					|| providerManagerInfo.getConstructorParams().size() == 0) {
				Method method = cls.getMethod(factoryMethod,null);
//				method.
				if(method == null)
				{
					throw new CurrentlyInCreationException(providerManagerInfo.getName()+"@" + providerManagerInfo.getConfigFile() + ",对象工厂["+factoryClass.getName()+"]没有定义"+factoryMethod+"方法。请检查[factory-method]属性配置是否正确。");
				}
				try {
					/**
					 * 本地线程技术，存放CallContext上下文
					 * 如果有旧的还需要保存旧的callContext
					 */
//					return (V) method.invoke(null);

					 int mode = method.getModifiers();
					 if( Modifier.isStatic(mode))//静态方法
						 return this.invokerMethod(providerManagerInfo, null, method, null, context);
					 else//非静态方法
					 {
						 Object factoryInf = getBeanFromClass( providerManagerInfo,  context,true);//先获取工厂类实例
						 return this.invokerMethod(providerManagerInfo, factoryInf, method, null, context);//然后调用工厂类实例方法创建组件实例
					 }
				}
				catch(CurrentlyInCreationException e)
				{
					throw e;
				}
				catch(InvocationTargetException e)
				{
					throw new BeanInstanceException(e.getTargetException());
				}
				catch (Exception e) {
					throw new CurrentlyInCreationException(providerManagerInfo.getName()+"@" + providerManagerInfo.getConfigFile() + ",对象工厂["+factoryClass.getName()+"]没有定义"+factoryMethod+"方法,或者是非静态方法。请检查[factory-method]属性配置是否正确。",e);
				}
				
				
				/**
				 * 清理本地线程
				 * 如果有旧的callContext，还需要恢复旧的上下文
				 */
			}
			List<Pro> params = providerManagerInfo.getConstructorParams();
			Object[] valuetypes = getValue2ndTypes(params, context);
			Object[] values = (Object[]) valuetypes[0];
			Class[] types = (Class[]) valuetypes[1];
			Method method = getMethod(providerManagerInfo, cls, factoryMethod, types, values, false);
					

			Class<?>[] parameterTypes = method.getParameterTypes();

			for (int i = 0; i < parameterTypes.length; i++) {

				Object refvalue = values[i];
				if (!parameterTypes[i].isInstance(refvalue)) {
					if (refvalue != null) {
						refvalue = ValueObjectUtil.typeCast(refvalue, refvalue
								.getClass(), parameterTypes[i]);
						values[i] = refvalue;
					}

				} else {

				}

			}
			/**
			 * 采用本地线程技术存放CallContext上下文
			 * 如果有旧的还需要保存旧的callContext
			 */
			
//			V bean =  (V)method.invoke(null,values);
			 int mode = method.getModifiers();
			 if( Modifier.isStatic(mode))//静态方法
			 {
				 return invokerMethod(providerManagerInfo, null, method, values, context);
			 }
			 else //非静态方法
			 {
				 Object factoryInf = getBeanFromClass( providerManagerInfo,  context,true);//先获取工厂类实例
				 return invokerMethod(providerManagerInfo, factoryInf, method, values, context);//然后调用工厂类实例方法创建组件实例
			 }
			
			/**
			 * 清理本地线程
			 * 如果有旧的callContext，还需要恢复旧的上下文
			 */
			
			
		}  catch (IllegalAccessException e) {
			throw new BeanInstanceException("providerManagerInfo["
					+ providerManagerInfo.getName() + "],请检查配置文件是否配置正确["
					+ providerManagerInfo.getConfigFile() + "]", e);
		} catch (BeanInstanceException e) {
			throw e;
		} 
		catch(CurrentlyInCreationException e)
		{
			throw e;
		}
		catch(InvocationTargetException e)
		{
			throw new BeanInstanceException(e.getTargetException());
		}
		catch (Exception e) {
			throw new BeanInstanceException("providerManagerInfo["
					+ providerManagerInfo.getName() + "],请检查配置文件是否配置正确["
					+ providerManagerInfo.getConfigFile() + "]", e);
		}
	}
	
	/**
	 * 通过工厂对象创建组件实例
	 * @param providerManagerInfo
	 * @param factory
	 * @param context
	 * @return
	 */
	public static MethodInvoker creatorMethodInvokerByClass(Pro  providerManagerInfo, String beanClass,String method)
	{
		try {
			Class cls = Class.forName(beanClass);
			Object instance = BeanUtils.instantiateClass(cls);
			
			if (providerManagerInfo.getConstruction() == null
					|| providerManagerInfo.getConstructorParams() == null
					|| providerManagerInfo.getConstructorParams().size() == 0) {
				Method method_ = cls.getMethod(method,null);
//				method.
				if(method == null)
				{
					throw new CurrentlyInCreationException(providerManagerInfo.getName()+"@" + providerManagerInfo.getConfigFile() 
							+ ",对象工厂["+beanClass+"]没有定义"+method+"方法。请检查[factory-method]属性配置是否正确。");
				}
				try {
					/**
					 * 本地线程技术，存放CallContext上下文
					 * 如果有旧的还需要保存旧的callContext
					 */
//					return (V) method.invoke(null);
					return new MethodInvoker(true, instance, null, method_,providerManagerInfo);
				}
				catch(CurrentlyInCreationException e)
				{
					throw e;
				}
				
				catch (Exception e) {
					throw new CurrentlyInCreationException(providerManagerInfo.getName()+"@" + providerManagerInfo.getConfigFile() 
							+ ",对象工厂["+beanClass+"]没有定义"+method+"方法,或者是非静态方法。请检查[factory-method]属性配置是否正确。",e);
				}
				
				
				/**
				 * 清理本地线程
				 * 如果有旧的callContext，还需要恢复旧的上下文
				 */
			}
			List<Pro> params = providerManagerInfo.getConstructorParams();
			Object[] valuetypes = getValue2ndTypes(params, null);
			Object[] values = (Object[]) valuetypes[0];
			Class[] types = (Class[]) valuetypes[1];
			Method method_ = getMethod(providerManagerInfo, cls, method, types, values, false);
					

			Class<?>[] parameterTypes = method_.getParameterTypes();

			for (int i = 0; i < parameterTypes.length; i++) {

				Object refvalue = values[i];
				if (!parameterTypes[i].isInstance(refvalue)) {
					if (refvalue != null) {
						refvalue = ValueObjectUtil.typeCast(refvalue, refvalue
								.getClass(), parameterTypes[i]);
						values[i] = refvalue;
					}

				} else {

				}

			}
			
			return new MethodInvoker(true, instance, values, method_,providerManagerInfo);
			
			
		}catch (BeanInstanceException e) {
			throw e;
		} 
		catch(CurrentlyInCreationException e)
		{
			throw e;
		}
		
		catch (Exception e) {
			throw new BeanInstanceException("providerManagerInfo["
					+ providerManagerInfo.getName() + "],请检查配置文件是否配置正确["
					+ providerManagerInfo.getConfigFile() + "]", e);
		}
	}
	
	/**
     * 根据参数类型params_，获取clazz的构造函数，paramArgs为参数的值，如果synTypes为true方法会
     * 通过参数的值对参数类型进行校正 当符合params_类型的构造函数有多个时，返回最开始匹配上的构造函数，但是当synTypes为true时，
     * 就会返回严格符合paramArgs值类型对应的构造函数 paramArgs值的类型也会作为获取构造函数的辅助条件，
     * 
     * @param clazz
     * @param params_
     * @param paramArgs
     * @param synTypes
     * @return
     */
    public static Method getMethod(Pro pro,Class clazz,String methodName, Class[] params_, Object[] paramArgs, boolean synTypes)
    {
        if (params_ == null || params_.length == 0)
        {
            return null;

        }
        Class[] params = null;
        if (synTypes)
            params = ValueObjectUtil.synParamTypes(params_, paramArgs);
        else
        	params = params_;
        try
        {

        	Method method = clazz.getMethod(methodName,params);
            return method;
        }
        catch (NoSuchMethodException e)
        {
        	Method[] constructors = clazz.getMethods();
            if (constructors == null || constructors.length == 0)
                throw new CurrentlyInCreationException("Inject constructor error: no construction define in the "
                        + clazz + ",请检查配置文件是否配置正确,参数个数是否正确.");
            int l = constructors.length;
            int size = params.length;
            Class[] types = null;
            Method fault_ = null;
            for (int i = 0; i < l; i++)
            {
            	Method temp = constructors[i];
            	if(!temp.getName().equals(methodName))
            		continue;
                types = temp.getParameterTypes();
                if (types != null && types.length == size)
                {
                    if (fault_ == null)
                        fault_ = temp;
                    if (ValueObjectUtil.isSameTypes(types, params, paramArgs))
                        return temp;
                }

            }
            if (fault_ != null)
                return fault_;
            StringBuffer msg = new StringBuffer();
            
            msg.append(pro.getName()+"@" 
            		+ pro.getConfigFile() + "， 对象工厂["
            		+ clazz+"]没有定义"
            		+ methodName);
            msg.append("(");
            for(int i = 0; i < params.length; i ++)
            {
            	if(i > 0 && i < params.length - 1)
            		msg.append(",");
            	msg.append(params[i].getCanonicalName());
            	
            }
            
            msg.append(")方法。请检查[factory-method]属性配置是否正确。.");
            		
            throw new CurrentlyInCreationException(msg.toString());

            // TODO Auto-generated catch block
            // throw new BeanInstanceException("Inject constructor error:"
            // +clazz.getName(),e);
        }
        catch(CurrentlyInCreationException e)
        {
        	throw e;
        }
        catch (Exception e)
        {
        	StringBuffer msg = new StringBuffer();
            
            msg.append(pro.getName()+"@" 
            		+ pro.getConfigFile() + "， 对象工厂["
            		+ clazz+"]没有定义"
            		+ methodName);
            msg.append("(");
            for(int i = 0; i < params.length; i ++)
            {
            	if(i > 0 && i < params.length - 1)
            		msg.append(",");
            	msg.append(params[i].getCanonicalName());
            	
            }
            
            msg.append(")方法。请检查[factory-method]属性配置是否正确。.");
            		
            throw new CurrentlyInCreationException(msg.toString(),e);
        }

    }
    
//    private Object getSpecialObject(Pro providerManagerInfo, CallContext callcontext)
//    {
//    	Class<V> cls = providerManagerInfo.getBeanClass();
//    	if(StackTraceElement.class.isAssignableFrom(cls) )
//		{
//    		/**<property name="className" soa:type="String"><![CDATA[org.frameworkset.soa.SOAApplicationContextTest]]></property>
//			<property name="fileName" soa:type="String"><![CDATA[SOAApplicationContextTest.java]]></property>
//			<property name="lineNumber" soa:type="int" value="113" />
//			<property name="methodName" soa:type="String"><![CDATA[bytearraybeantoxml]]></property>
//			<property name="nativeMethod" soa:type="boolean" value="false" />*/
//    		
//    		providerManagerInfo.get
//		}
//    	if(Throwable.class.isAssignableFrom(cls) )
//		{
//			
//		}
//    }
    
    private V getBeanFromClass(BeanInf providerManagerInfo, CallContext callcontext)
    {
    	return  getBeanFromClass( providerManagerInfo,  callcontext,false);
    }
	
	private V getBeanFromClass(BeanInf providerManagerInfo, CallContext callcontext,boolean ignoreconstruction)
	{
		V instance = null;
		try {
			
			Class<V> cls = null;
			if(!ignoreconstruction)
				cls = providerManagerInfo.getBeanClass();
			else
				cls = providerManagerInfo.getFactoryClass();
			
			instance = initbean(providerManagerInfo, callcontext,ignoreconstruction);

//			BeanInfo beanInfo = Introspector.getBeanInfo(cls);
//			PropertyDescriptor[] attributes = beanInfo.getPropertyDescriptors();

			List<Pro> refs = providerManagerInfo.getReferences();
			ClassInfo classInfo = ClassUtil.getClassInfo(cls);
			if (refs != null && refs.size() > 0) {
				//这里需要增加引用Pro的调用上下文
				Context currentLoopContext = callcontext != null?callcontext.getLoopContext():null;
				for (int i = 0; i < refs.size(); i++) {
					Pro ref = refs.get(i);
					
					String filedName = ref.getName();
					Object refvalue = null;
					try
					{
//						if(ref.getXpath() != null)
							refvalue = providerManagerInfo.getApplicationContext().proxyObject(ref, 
									ref.getTrueValue(callcontext), 
									ref.getXpath());
					}
					finally
					{
						if(callcontext != null)
							callcontext.setLoopContext(currentLoopContext);
					}				

					// get bean attribute name
					PropertieDescription propertyDescriptor = classInfo.getPropertyDescriptor(filedName);
					

					if (propertyDescriptor != null) {
						

						Class type = propertyDescriptor.getPropertyType();

						// create attribute value of correct type
						
						Object value = refvalue != null?ValueObjectUtil.typeCast(refvalue,
								refvalue.getClass(), type):ValueObjectUtil.getDefaultValue(type);
						
//						Method wm = propertyDescriptor.getWriteMethod();

						try {
							if(propertyDescriptor.canwrite())
							{
								propertyDescriptor.setValue(instance, value);
								continue;
							}
							else
							{
								log.warn("设置组件["+providerManagerInfo.getName()+"]属性失败：Does not exist a writer method for field ["
										+ propertyDescriptor.getName() +"] in class ["+instance.getClass().getCanonicalName()+"],请检查类定义文件是否正确设置了该字段的set方法，或者字段名称是否指定正确。");
								
								continue;
							}
//							if(wm == null)
//							{
//								log.warn("设置组件["+providerManagerInfo.getName()+"]属性失败：Does not exist a writer method for field ["
//										+ propertyDescriptor.getName() +"] in class ["+instance.getClass().getCanonicalName()+"],请检查类定义文件是否正确设置了该字段的set方法，或者字段名称是否指定正确。");
//								
//								continue;
//							}
//							wm.invoke(instance, new Object[] { value });
//							continue;
						} catch (IllegalArgumentException e) {
							throw new CurrentlyInCreationException(
									"providerManagerInfo["
											+ providerManagerInfo.getName()
											+ "],请检查配置文件是否配置正确["
											+ providerManagerInfo
													.getConfigFile() + "]",
									e);
						} catch (IllegalAccessException e) {
							throw new CurrentlyInCreationException(
									"providerManagerInfo["
											+ providerManagerInfo.getName()
											+ "],请检查配置文件是否配置正确["
											+ providerManagerInfo
													.getConfigFile() + "]",
									e);
						} catch (InvocationTargetException e) {
							throw new CurrentlyInCreationException(
									"providerManagerInfo["
											+ providerManagerInfo.getName()
											+ "],请检查配置文件是否配置正确["
											+ providerManagerInfo
													.getConfigFile() + "]",
									e);
						}
						catch(CurrentlyInCreationException e)
						{
							throw e;
						}
						catch(BeanInstanceException e)
						{
							throw e;
						}
						
						catch (Exception e) {
//								e.printStackTrace();
							throw new CurrentlyInCreationException(
									"providerManagerInfo["
											+ providerManagerInfo.getName()
											+ "],请检查配置文件是否配置正确["
											+ providerManagerInfo
													.getConfigFile() + "]",
									e);
						}
						

					}
					else // 引用字段名称在provider中没有定义
					{
						
//						log.warn("引用字段[" + filedName + "]在provider["
//								+ instance.getClass() + "]中没有定义");
						Exception e = new Exception("引用字段[" + filedName + "]在组件["
								+ instance.getClass() + "]中没有定义,请检查配置文件是否配置正确["
											+ providerManagerInfo
													.getConfigFile() + "]");
						log.warn("", e);
					}
				}
			}
		} catch (NumberFormatException e) {
			throw new CurrentlyInCreationException("providerManagerInfo["
					+ providerManagerInfo.getName() + "],请检查配置文件是否配置正确["
					+ providerManagerInfo.getConfigFile() + "]", e);
		} catch (IllegalArgumentException e) {
			throw new CurrentlyInCreationException("providerManagerInfo["
					+ providerManagerInfo.getName() + "],请检查配置文件是否配置正确["
					+ providerManagerInfo.getConfigFile() + "]", e);
		} catch (NoSupportTypeCastException e) {
			throw new CurrentlyInCreationException("providerManagerInfo["
					+ providerManagerInfo.getName() + "],请检查配置文件是否配置正确["
					+ providerManagerInfo.getConfigFile() + "]", e);
		} catch (BeanInstanceException e) {
			throw e;
		} catch (SPIException e) {
			throw e;
		} catch (CurrentlyInCreationException e) {
			throw e;
		}

		catch (Exception e) {
			throw new CurrentlyInCreationException("providerManagerInfo["
					+ providerManagerInfo.getName() + "],请检查配置文件是否配置正确["
					+ providerManagerInfo.getConfigFile() + "]" , e);
		}
		providerManagerInfo.getApplicationContext().initBean(instance,
				providerManagerInfo);
		
		return instance;
	}
	@SuppressWarnings("unchecked")
	public V getBean(BeanInf providerManagerInfo, CallContext callcontext) {
		Context context = null;
		if (callcontext == null)
			callcontext = new LocalCallContextImpl(providerManagerInfo
					.getApplicationContext());
//		if(callcontext.isSOAApplication())
//		{
//			return getBeanFromClass( providerManagerInfo,  callcontext);
//		}
//		else
		{
			if (callcontext.getLoopContext() == null) {
				context = new Context(providerManagerInfo.getXpath());
				callcontext.setLoopContext(context);
			} else {
//				context = new Context(callcontext.getLoopContext(),
//						providerManagerInfo.getName());
				if(providerManagerInfo.getXpath() != null)
					context = new Context(callcontext.getLoopContext(),
							providerManagerInfo.getXpath());
				else
				{
					context = new Context(callcontext.getLoopContext(),
							providerManagerInfo.getName());
				}
				callcontext.setLoopContext(context);
			}
			if(providerManagerInfo.getFactory_bean() == null && providerManagerInfo.getFactory_class() == null)
			{
				return getBeanFromClass( providerManagerInfo,  callcontext);
				
			}
			else
			{
				return getBeanFromFactory(providerManagerInfo, callcontext);
			}
		}
		
		
	}

	public static void initBean(Object bean, BeanInf providerManagerInfo,
			BaseApplicationContext context) throws BeanInstanceException {
		if(bean instanceof BeanClassLoaderAware)
		{
			((BeanClassLoaderAware)bean).setBeanClassLoader(context.getClassLoader());
		}
		if (bean instanceof ApplicationObjectSupport) {
			((ApplicationObjectSupport) bean).setApplicationContext(context);

		} else if (bean instanceof ApplicationContextAware) {
			((ApplicationContextAware) bean).setApplicationContext(context);
		}

		if(bean instanceof BeanNameAware)
		{
			if(providerManagerInfo != null)
				((BeanNameAware) bean).setBeanName(providerManagerInfo.getName());
		}
		if(bean instanceof BeanInfoAware)
		{
			if(providerManagerInfo != null && providerManagerInfo instanceof Pro)
				((BeanInfoAware) bean).setBeaninfo((Pro)providerManagerInfo);
		}
		if (bean instanceof MessageSourceAware) {
			((MessageSourceAware) bean).setMessageSource(context);
		}
		if (bean instanceof ResourceLoaderAware) {
			((ResourceLoaderAware) bean).setResourceLoader(context);
		}
		
		afterPropertiesSet(bean, providerManagerInfo, context);
		registDestroy(bean, providerManagerInfo, context);

	}
	public static void initBean(Object bean, String beanname,
			BaseApplicationContext context) throws BeanInstanceException {		
		if (bean instanceof ApplicationObjectSupport) {
			((ApplicationObjectSupport) bean).setApplicationContext(context);

		} else if (bean instanceof ApplicationContextAware) {
			((ApplicationContextAware) bean).setApplicationContext(context);
		}
		if(bean instanceof BeanNameAware)
		{
			
				((BeanNameAware) bean).setBeanName(beanname);
		}
		if (bean instanceof MessageSourceAware) {
			((MessageSourceAware) bean).setMessageSource(context);
		}
		if (bean instanceof ResourceLoaderAware) {
			((ResourceLoaderAware) bean).setResourceLoader(context);
		}
		

		afterPropertiesSet(bean, null, context);
		registDestroy(bean, null, context);

	}

	public static void registDestroy(Object instance,
			BeanInf providerManagerInfo, BaseApplicationContext context) {
		if (providerManagerInfo != null && !providerManagerInfo.isSinglable())
			return;
		if (instance != null && instance instanceof DisposableBean) {
			context.registDisableBean((DisposableBean) instance);
		} else if (providerManagerInfo != null
				&& providerManagerInfo.getDestroyMethod() != null
				&& !providerManagerInfo.getDestroyMethod().equals("")) {
			context.registDestroyMethod(providerManagerInfo.getDestroyMethod(),
					instance);
		}
	}

	public static void afterPropertiesSet(Object instance,
			BeanInf providerManagerInfo, BaseApplicationContext context)
			throws BeanInstanceException {
		if (instance == null)
			return;
		if (instance instanceof InitializingBean) {
			InitializingBean init = (InitializingBean) instance;
			try {
				init.afterPropertiesSet();
			} catch (BeanInstanceException e) {
				throw e;
			} catch (Exception e) {
				throw new BeanInstanceException(e);
			}
		} else if (providerManagerInfo != null
				&& providerManagerInfo.getInitMethod() != null
				&& !providerManagerInfo.getInitMethod().equals("")) {
			try {
				Method m = instance.getClass().getDeclaredMethod(
						providerManagerInfo.getInitMethod());
				m.invoke(instance, new Object[0]);
			} catch (SecurityException e) {
				throw new BeanInstanceException("InitializingBean["
						+ providerManagerInfo.getName()
						+ "] afterPropertiesSet 失败,请检查配置文件是否配置正确["
						+ providerManagerInfo.getConfigFile() + "]", e);
			} catch (NoSuchMethodException e) {
				throw new BeanInstanceException("InitializingBean["
						+ providerManagerInfo.getName()
						+ "] afterPropertiesSet 失败,请检查配置文件是否配置正确["
						+ providerManagerInfo.getConfigFile() + "]", e);
			}
			catch(CurrentlyInCreationException e)
			{
				throw e;
			}
			catch(InvocationTargetException e)
			{
				throw new BeanInstanceException(e.getTargetException());
			}
			catch (BeanInstanceException e) {
				throw e;
			} catch (Exception e) {
				throw new BeanInstanceException("InitializingBean["
						+ providerManagerInfo.getName()
						+ "] afterPropertiesSet 失败,请检查配置文件是否配置正确["
						+ providerManagerInfo.getConfigFile() + "]", e);
			}
		}

	}

	// public Object getBean(SecurityProviderInfo providerManagerInfo, Context
	// context_)
	// {
	// Object instance = null;
	// try
	// {
	// Class cls = providerManagerInfo.getProviderClass_();
	// Context context = null;
	// if (context_ == null)
	// {
	// context = new
	// Context(providerManagerInfo.getProviderManagerInfo().getId());
	// }
	// else
	// {
	// context = new Context(context_,
	// providerManagerInfo.getProviderManagerInfo().getId());
	// }
	// instance = initbean(providerManagerInfo, context);
	// BeanInfo beanInfo = Introspector.getBeanInfo(cls);
	// PropertyDescriptor[] attributes = beanInfo.getPropertyDescriptors();
	//
	// List<Pro> refs =
	// providerManagerInfo.getProviderManagerInfo().getReferences();
	// if (refs != null && refs.size() > 0)
	// {
	//
	// for (int i = 0; i < refs.size(); i++)
	// {
	// Pro ref = refs.get(i);
	// boolean flag = false;
	// String filedName = ref.getName();
	//
	// // Object refvalue = ref.getObject(context);
	// Object refvalue = null;
	// if (ref.isBean())
	// {
	// refvalue = BaseSPIManager.getBeanObject(context, ref);
	// }
	// else if (ref.isRefereced())
	// {
	// refvalue = BaseSPIManager.getBeanObject(context, ref);
	// }
	// else
	// {
	// refvalue = ref.getObject();
	// }
	// for (int n = 0; n < attributes.length; n++)
	// {
	//
	// // get bean attribute name
	// PropertyDescriptor propertyDescriptor = attributes[n];
	// String attrName = propertyDescriptor.getName();
	//
	// if (filedName.equals(attrName))
	// {
	// flag = true;
	//
	// Class type = propertyDescriptor.getPropertyType();
	//
	// // create attribute value of correct type
	// Object value = ValueObjectUtil.typeCast(refvalue, refvalue.getClass(),
	// type);
	// // PropertyEditor editor =
	// // PropertyEditorManager.findEditor(type);
	// // editor.setAsText(ref.getValue());
	// // Object value = editor.getValue();
	// Method wm = propertyDescriptor.getWriteMethod();
	//
	// try
	// {
	// wm.invoke(instance, new Object[] { value });
	// }
	// catch (IllegalArgumentException e)
	// {
	// throw new CurrentlyInCreationException(e);
	// }
	// catch (IllegalAccessException e)
	// {
	// throw new CurrentlyInCreationException(e);
	// }
	// catch (InvocationTargetException e)
	// {
	// throw new CurrentlyInCreationException(e);
	// }
	// // Object value = editor.getValue();
	// // set attribute value on bean
	//
	// }
	// }
	//
	// if (!flag) // 引用字段名称在provider中没有定义
	// {
	// System.out.println("引用字段[" + filedName + "]在provider[" +
	// instance.getClass() + "]中没有定义");
	// log.warn("引用字段[" + filedName + "]在provider[" + instance.getClass() +
	// "]中没有定义");
	// }
	// }
	// }
	// }
	// catch (IntrospectionException e1)
	// {
	// throw new CurrentlyInCreationException(e1);
	// }
	// catch (NumberFormatException e)
	// {
	// throw new CurrentlyInCreationException(e);
	// }
	// catch (IllegalArgumentException e)
	// {
	// throw new CurrentlyInCreationException(e);
	// }
	// catch (NoSupportTypeCastException e)
	// {
	// throw new CurrentlyInCreationException(e);
	// }
	// // catch (InstantiationException e)
	// // {
	// // throw new CurrentlyInCreationException(e);
	// // }
	// // catch (IllegalAccessException e)
	// // {
	// // throw new CurrentlyInCreationException(e);
	// // }
	//
	// catch (BeanInstanceException e)
	// {
	// throw e;
	// }
	// catch (SPIException e)
	// {
	// throw e;
	// }
	// catch (CurrentlyInCreationException e)
	// {
	// throw e;
	// }
	//
	// catch (Exception e)
	// {
	// throw new CurrentlyInCreationException(e);
	// }
	// return instance;
	// }
}
