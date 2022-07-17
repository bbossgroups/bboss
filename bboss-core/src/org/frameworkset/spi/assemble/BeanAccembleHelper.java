package org.frameworkset.spi.assemble;

import com.frameworkset.spi.assemble.BeanInstanceException;
import com.frameworkset.spi.assemble.CurrentlyInCreationException;
import com.frameworkset.util.BeanUtils;
import com.frameworkset.util.EditorInf;
import com.frameworkset.util.NoSupportTypeCastException;
import com.frameworkset.util.ValueObjectUtil;
import org.frameworkset.spi.*;
import org.frameworkset.spi.assemble.plugin.IocPlugin;
import org.frameworkset.spi.support.ApplicationObjectSupport;
import org.frameworkset.spi.support.MessageSourceAware;
import org.frameworkset.util.ClassUtil;
import org.frameworkset.util.ClassUtil.ClassInfo;
import org.frameworkset.util.ClassUtil.PropertieDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
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
 * @author biaoping.yin
 * @version 1.0
 * @Date 2009-9-19 下午11:01:02
 */
public class BeanAccembleHelper {
	private static Logger log = LoggerFactory.getLogger(BeanAccembleHelper.class);

	private Class<?>[] constructParamTypes = null;

	private Constructor constructor = null;

	public static Object[] getValue2ndTypes(List<Pro> params, CallContext context) {
		if (params == null || params.size() == 0) {
			return null;
		}

		Object[] valuetypes = new Object[2];
		Object[] values = new Object[params.size()];
		Class[] types = new Class[params.size()];
		valuetypes = new Object[]{values, types};
		Context currentLoopContext = context != null ? context.getLoopContext() : null;
		for (int i = 0; i < params.size(); i++) {
			Pro param = params.get(i);
			Object refvalue = null;
			if (param.getClazz() != null && !param.getClazz().equals("")) {
				try {
					types[i] = getClass(param.getClazz());
				} catch (Exception e) {
				}
			}
			try {
//				refvalue = param.getTrueValue(context);
				refvalue = param.getApplicationContext().proxyObject(param,
						param.getTrueValue(context),
						param.getXpath());
			} finally {
				if (context != null)
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
							if (propertyDescriptor.canwrite()) {
								propertyDescriptor.setValue(bean, value);
								break;
							} else {
								if (log.isWarnEnabled()) {
									log.warn(new StringBuilder().append("设置组件[")
											.append(bean.getClass())
											.append("]属性失败：Does not exist a writer method for field ["
											)
											.append(propertyDescriptor.getName())
											.append("] ,请检查类定义文件是否正确设置了该字段的set方法，或者字段名称是否指定正确。").toString());
								}
								flag = false;
								break;
							}

						} catch (IllegalArgumentException e) {
							throw new CurrentlyInCreationException(
									new StringBuilder().append("providerManagerInfo[")
											.append(bean.getClass()
											)
											.append("]").toString(), e);
						} catch (IllegalAccessException e) {
							throw new CurrentlyInCreationException(
									new StringBuilder().append("providerManagerInfo[")
											.append(bean.getClass()
											)
											.append("]").toString(), e);
						} catch (CurrentlyInCreationException e) {
							throw e;
						} catch (InvocationTargetException e) {
							throw new CurrentlyInCreationException(e.getTargetException());
						} catch (Exception e) {
							throw new CurrentlyInCreationException(
									new StringBuilder().append("providerManagerInfo[")
											.append(bean.getClass()
											)
											.append("]").toString(), e);
						}
						// Object value = editor.getValue();
						// set attribute value on bean

					}
				}

				if (!flag) // 引用字段名称在provider中没有定义
				{

					if (log.isWarnEnabled()) {

						Exception e = new Exception(new StringBuilder().append("引用字段[")
								.append(filedName)
								.append("]在组件["
								)
								.append(bean.getClass())
								.append("]中没有定义").toString());
						log.warn("", e);
					}
				}
			} catch (CurrentlyInCreationException e) {
				throw e;
			} catch (BeanInstanceException e) {
				throw e;
			} catch (NumberFormatException e) {
				throw new CurrentlyInCreationException(new StringBuilder().append("injectProperties["
				)
						.append(bean.getClass())
						.append("]").toString(), e);
			} catch (IllegalArgumentException e) {
				throw new CurrentlyInCreationException(new StringBuilder().append("injectProperties["
				)
						.append(bean.getClass())
						.append("]").toString(), e);
			} catch (NoSupportTypeCastException e) {
				throw new CurrentlyInCreationException(new StringBuilder().append("injectProperties["
				)
						.append(bean.getClass())
						.append("]").toString(), e);
			} catch (Exception e) {
				throw new CurrentlyInCreationException(new StringBuilder().append("injectProperties["
				)
						.append(bean.getClass())
						.append("]").toString(), e);
			}

		}

	}

	public static void injectProperties(Object bean,
										Map<String, Pro> globalparams, Map<String, Pro> persistentparams)
			throws IntrospectionException {

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
							if (propertyDescriptor.canwrite()) {

								propertyDescriptor.setValue(bean, value);
								break;
							} else {
								if (log.isWarnEnabled()) {
									log.warn(new StringBuilder().append("初始化组件[")
											.append(bean.getClass())
											.append("]失败：Does not exist a writer method for field ["
											)
											.append(propertyDescriptor.getName())
											.append("] ,请检查类定义文件是否正确设置了该字段的set方法，或者字段名称是否指定正确。").toString());
								}
								flag = false;
								break;
							}

						} catch (IllegalArgumentException e) {
							throw new CurrentlyInCreationException(
									new StringBuilder().append(pro.getName())
											.append("@")
											.append(pro.getConfigFile())
											.append("，providerManagerInfo[")
											.append(bean.getClass()
											)
											.append("]").toString(), e);
						} catch (CurrentlyInCreationException e) {
							throw e;
						} catch (InvocationTargetException e) {
							throw new CurrentlyInCreationException(e.getTargetException());
						} catch (IllegalAccessException e) {
							throw new CurrentlyInCreationException(
									new StringBuilder().append(pro.getName())
											.append("@")
											.append(pro.getConfigFile())
											.append("，providerManagerInfo[")
											.append(bean.getClass()
											)
											.append("]").toString(), e);
						} catch (Exception e) {
							throw new CurrentlyInCreationException(
									new StringBuilder().append(pro.getName())
											.append("@")
											.append(pro.getConfigFile())
											.append("，providerManagerInfo[")
											.append(bean.getClass()
											)
											.append("]").toString(), e);
						}
						// Object value = editor.getValue();
						// set attribute value on bean

					}
				}

				if (!flag) // 引用字段名称在provider中没有定义
				{

					if (log.isWarnEnabled()) {

						Exception e = new Exception(new StringBuilder().append("引用字段[")
								.append(filedName)
								.append("]在组件["
								)
								.append(bean.getClass())
								.append("]中没有定义,请检查配置文件是否配置正确["
								)
								.append(pro
										.getConfigFile())
								.append("]").toString());
						log.warn("", e);
					}
				}
			} catch (CurrentlyInCreationException e) {
				throw e;
			} catch (BeanInstanceException e) {
				throw e;
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				throw new CurrentlyInCreationException(new StringBuilder().append(pro.getName())
						.append("@")
						.append(pro.getConfigFile())
						.append("，injectProperties["
						)
						.append(bean.getClass())
						.append("]").toString(), e);
			} catch (NumberFormatException e) {
				throw new CurrentlyInCreationException(new StringBuilder().append(pro.getName())
						.append("@")
						.append(pro.getConfigFile())
						.append("，injectProperties["
						)
						.append(bean.getClass())
						.append("]").toString(), e);
			} catch (IllegalArgumentException e) {
				throw new CurrentlyInCreationException(new StringBuilder().append(pro.getName())
						.append("@")
						.append(pro.getConfigFile())
						.append("，injectProperties["
						)
						.append(bean.getClass())
						.append("]").toString(), e);
			} catch (NoSupportTypeCastException e) {
				throw new CurrentlyInCreationException(new StringBuilder().append(pro.getName())
						.append("@")
						.append(pro.getConfigFile())
						.append("，injectProperties["
						)
						.append(bean.getClass())
						.append("]").toString(), e);
			} catch (Exception e) {
				throw new CurrentlyInCreationException(new StringBuilder().append(pro.getName())
						.append("@")
						.append(pro.getConfigFile())
						.append("，injectProperties["
						)
						.append(bean.getClass())
						.append("]").toString(), e);
			}

		}

	}

	/**
	 * 通过工厂对象创建组件实例
	 *
	 * @param providerManagerInfo
	 * @return
	 */
	public static MethodInvoker creatorMethodInvokerByBean(Pro providerManagerInfo, String beanName, String method) {
		try {
			Object instance = providerManagerInfo.getApplicationContext().getBeanObject(beanName);
			Class cls = instance.getClass();

			if (providerManagerInfo.getConstruction() == null
					|| providerManagerInfo.getConstructorParams() == null
					|| providerManagerInfo.getConstructorParams().size() == 0) {
				Method method_ = cls.getMethod(method);
				if (method == null) {
					throw new CurrentlyInCreationException(new StringBuilder().append(providerManagerInfo.getName())
							.append("@"
							)
							.append(providerManagerInfo.getConfigFile())
							.append(",对象工厂[")
							.append(cls.getName())
							.append("]没有定义")
							.append(method)
							.append("方法。请检查[factory-method]属性配置是否正确。").toString());
				}

				/**
				 * 本地线程技术，存放CallContext上下文
				 * 如果有旧的还需要保存旧的callContext
				 */
//				return  method.invoke(factory, null);
				return new MethodInvoker(false, instance, null, method_, providerManagerInfo);

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


			return new MethodInvoker(false, instance, values, method_, providerManagerInfo);


		} catch (CurrentlyInCreationException e) {
			throw e;
		} catch (BeanInstanceException e) {
			throw e;
		} catch (Exception e) {
			throw new BeanInstanceException(new StringBuilder().append("providerManagerInfo["
			)
					.append(providerManagerInfo.getName())
					.append("],请检查配置文件是否配置正确["
					)
					.append(providerManagerInfo.getConfigFile())
					.append("]").toString(), e);
		}
	}

	/**
	 * 通过工厂对象创建组件实例
	 *
	 * @param instance
	 * @return
	 */
	public static MethodInvoker creatorMethodInvokerByBean(Object instance, String method) {
		Class cls = instance.getClass();
		ClassInfo classInfo = ClassUtil.getClassInfo(cls);
		try {



			 Method method_ = classInfo.getDeclaredMethod(method);


			return new MethodInvoker(false, instance, null, method_, null);


		}  catch (Exception e) {
			throw new MethodInvokerException("Call method "+method+" on instance " + classInfo.getName() +" failed:",e);
		}
	}

	/**
	 * 通过工厂对象创建组件实例
	 *
	 * @param providerManagerInfo
	 * @return
	 */
	public static MethodInvoker creatorMethodInvokerByClass(Pro providerManagerInfo, String beanClass, String method) {
		try {
			Class cls = Class.forName(beanClass);
			Object instance = BeanUtils.instantiateClass(cls);

			if (providerManagerInfo.getConstruction() == null
					|| providerManagerInfo.getConstructorParams() == null
					|| providerManagerInfo.getConstructorParams().size() == 0) {
				Method method_ = cls.getMethod(method);
//				method.
				if (method == null) {
					throw new CurrentlyInCreationException(new StringBuilder().append(providerManagerInfo.getName())
							.append("@")
							.append(providerManagerInfo.getConfigFile())
							.append(",对象工厂[")
							.append(beanClass)
							.append("]没有定义")
							.append(method)
							.append("方法。请检查[factory-method]属性配置是否正确。").toString());
				}
				try {
					/**
					 * 本地线程技术，存放CallContext上下文
					 * 如果有旧的还需要保存旧的callContext
					 */
//					return  method.invoke(null);
					return new MethodInvoker(true, instance, null, method_, providerManagerInfo);
				} catch (CurrentlyInCreationException e) {
					throw e;
				} catch (Exception e) {
					throw new CurrentlyInCreationException(new StringBuilder().append(providerManagerInfo.getName())
							.append("@")
							.append(providerManagerInfo.getConfigFile())
							.append(",对象工厂[")
							.append(beanClass)
							.append("]没有定义")
							.append(method)
							.append("方法,或者是非静态方法。请检查[factory-method]属性配置是否正确。").toString(), e);
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

			return new MethodInvoker(true, instance, values, method_, providerManagerInfo);


		} catch (BeanInstanceException e) {
			throw e;
		} catch (CurrentlyInCreationException e) {
			throw e;
		} catch (Exception e) {
			throw new BeanInstanceException(new StringBuilder().append("providerManagerInfo[")
					.append(providerManagerInfo.getName())
					.append("],请检查配置文件是否配置正确[")
					.append(providerManagerInfo.getConfigFile())
					.append("]").toString(), e);
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
	public static Method getMethod(Pro pro, Class clazz, String methodName, Class[] params_, Object[] paramArgs, boolean synTypes) {
		if (params_ == null || params_.length == 0) {
			return null;

		}
		Class[] params = null;
		if (synTypes)
			params = ValueObjectUtil.synParamTypes(params_, paramArgs);
		else
			params = params_;
		try {

			Method method = clazz.getMethod(methodName, params);
			return method;
		} catch (NoSuchMethodException e) {
			Method[] constructors = clazz.getMethods();
			if (constructors == null || constructors.length == 0)
				throw new CurrentlyInCreationException(new StringBuilder().append("Inject constructor error: no construction define in the ")
						.append(clazz)
						.append(",请检查配置文件是否配置正确,参数个数是否正确.").toString());
			int l = constructors.length;
			int size = params.length;
			Class[] types = null;
			Method fault_ = null;
			for (int i = 0; i < l; i++) {
				Method temp = constructors[i];
				if (!temp.getName().equals(methodName))
					continue;
				types = temp.getParameterTypes();
				if (types != null && types.length == size) {
					if (fault_ == null)
						fault_ = temp;
					if (ValueObjectUtil.isSameTypes(types, params, paramArgs))
						return temp;
				}

			}
			if (fault_ != null)
				return fault_;
			StringBuilder msg = new StringBuilder();

			msg.append(pro.getName())
					.append("@"
					)
					.append(pro.getConfigFile())
					.append("， 对象工厂["
					)
					.append(clazz)
					.append("]没有定义"
					)
					.append(methodName);
			msg.append("(");
			for (int i = 0; i < params.length; i++) {
				if (i > 0 && i < params.length - 1)
					msg.append(",");
				msg.append(params[i].getCanonicalName());

			}

			msg.append(")方法。请检查[factory-method]属性配置是否正确。.");

			throw new CurrentlyInCreationException(msg.toString());

			// TODO Auto-generated catch block
			// throw new BeanInstanceException("Inject constructor error:"
			// +clazz.getName(),e);
		} catch (CurrentlyInCreationException e) {
			throw e;
		} catch (Exception e) {
			StringBuilder msg = new StringBuilder();

			msg.append(pro.getName())
					.append("@"
					)
					.append(pro.getConfigFile())
					.append("， 对象工厂["
					)
					.append(clazz)
					.append("]没有定义"
					)
					.append(methodName);
			msg.append("(");
			for (int i = 0; i < params.length; i++) {
				if (i > 0 && i < params.length - 1)
					msg.append(",");
				msg.append(params[i].getCanonicalName());

			}

			msg.append(")方法。请检查[factory-method]属性配置是否正确。.");

			throw new CurrentlyInCreationException(msg.toString(), e);
		}

	}

	public static void initBean(Object bean, BeanInf providerManagerInfo,
								BaseApplicationContext context) throws BeanInstanceException {
		if (bean instanceof BeanClassLoaderAware) {
			((BeanClassLoaderAware) bean).setBeanClassLoader(context.getClassLoader());
		}
		if (bean instanceof ApplicationObjectSupport) {
			((ApplicationObjectSupport) bean).setApplicationContext(context);

		} else if (bean instanceof ApplicationContextAware) {
			((ApplicationContextAware) bean).setApplicationContext(context);
		}

		if (bean instanceof BeanNameAware) {
			if (providerManagerInfo != null)
				((BeanNameAware) bean).setBeanName(providerManagerInfo.getName());
		}
		if (bean instanceof BeanInfoAware) {
			if (providerManagerInfo != null && providerManagerInfo instanceof Pro)
				((BeanInfoAware) bean).setBeaninfo((Pro) providerManagerInfo);
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
		if (bean instanceof BeanNameAware) {

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
				ClassInfo iclass = ClassUtil.getClassInfo(instance.getClass());
				Method m = iclass.getDeclaredMethod(providerManagerInfo.getInitMethod());
				if (m == null) {
					throw new BeanInstanceException(new StringBuilder().append("InitializingBean[")
							.append(providerManagerInfo.getName())
							.append("] afterPropertiesSet 失败:NoSuchMethodException[")
							.append(providerManagerInfo.getInitMethod())
							.append("]，请检查配置文件是否配置正确[")
							.append(providerManagerInfo.getConfigFile())
							.append("]").toString());
				}
//				Method m = instance.getClass().getDeclaredMethod(
//						providerManagerInfo.getInitMethod());
				m.invoke(instance, new Object[0]);
			} catch (SecurityException e) {
				throw new BeanInstanceException(new StringBuilder().append("InitializingBean[")
						.append(providerManagerInfo.getName())
						.append("] afterPropertiesSet 失败,请检查配置文件是否配置正确[")
						.append(providerManagerInfo.getConfigFile())
						.append("]").toString(), e);
			} catch (CurrentlyInCreationException e) {
				throw e;
			} catch (InvocationTargetException e) {
				throw new BeanInstanceException(new StringBuilder().append("InitializingBean[")
						.append(providerManagerInfo.getName())
						.append("] afterPropertiesSet 失败,请检查配置文件是否配置正确[")
						.append(providerManagerInfo.getConfigFile())
						.append("]").toString(), e.getTargetException());
			} catch (BeanInstanceException e) {
				throw e;
			} catch (Exception e) {
				throw new BeanInstanceException(new StringBuilder().append("InitializingBean[")
						.append(providerManagerInfo.getName())
						.append("] afterPropertiesSet 失败,请检查配置文件是否配置正确[")
						.append(providerManagerInfo.getConfigFile())
						.append("]").toString(), e);
			}
		}

	}

	/**
	 * 获取属性的引用值
	 *
	 * @param property
	 * @param callcontext
	 * @param defaultValue
	 * @return
	 */
	public Object getRefValue(Pro property, CallContext callcontext,
							  Object defaultValue) {
		if (callcontext != null) {

			{
				Context context = callcontext.getLoopContext();


				LoopObject lo = new LoopObject();
				boolean loopobject = context != null ? context.isLoopIOC(property.getRefid(), lo) : false;
				if (loopobject && lo.getObj() == null) {
					throw new CurrentlyInCreationException(new StringBuilder().append(
							"loop inject constructor error. the inject context path is [")
							.append(context)
							.append(">")
							.append(property.getRefid()
							)
							.append("],请检查配置文件是否配置正确[")
							.append(property.getConfigFile()
							)
							.append("]").toString());
				}
				if (lo.getObj() != null)
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
			if (property.getRefidLink() == null)
				ret = property.getApplicationContext().getBeanObject(
						callcontext, property.getRefid());
			else
				ret = property.getApplicationContext().getBeanObjectFromRefID(
						callcontext, property.getRefidLink(), property.getRefid(), null);
			if (ret == null)
				return defaultValue;
			if (property.getClazz() != null && !property.getClazz().equals("")) {
				try {
					ret = ValueObjectUtil.typeCast(ret, ret.getClass(),
							property.getType());
					return ret;
				} catch (NumberFormatException e) {
					throw new CurrentlyInCreationException(
							new StringBuilder().append("providerManagerInfo[")
									.append(property.getName())
									.append("],请检查配置文件是否配置正确[")
									.append(property.getConfigFile())
									.append("]").toString(), e);
				} catch (IllegalArgumentException e) {
					throw new CurrentlyInCreationException(
							new StringBuilder().append("providerManagerInfo[")
									.append(property.getName()
									)
									.append("],请检查配置文件是否配置正确["
									)
									.append(property.getConfigFile())
									.append("]").toString(), e);
				} catch (NoSupportTypeCastException e) {
					throw new CurrentlyInCreationException(
							new StringBuilder().append("providerManagerInfo[")
									.append(property.getName()
									)
									.append("],请检查配置文件是否配置正确["
									)
									.append(property.getConfigFile())
									.append("]").toString(), e);
				} catch (BeanInstanceException e) {
					throw e;
				} catch (Exception e) {
					throw new CurrentlyInCreationException(
							new StringBuilder().append("providerManagerInfo[")
									.append(property.getName()
									)
									.append("],请检查配置文件是否配置正确["
									)
									.append(property.getConfigFile())
									.append("]").toString(), e);
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
	 * @param defaultValue
	 * @return
	 */
	public Object getFactoryRefValue(Pro property, String factoryname, CallContext callcontext,
									 Object defaultValue) {
		Context context = null;
		if (callcontext != null)
			context = callcontext.getLoopContext();

		LoopObject lo = new LoopObject();
		boolean loopobject = context != null ? context.isLoopIOC(factoryname, lo) : false;
		if (loopobject && lo.getObj() == null) {
			throw new CurrentlyInCreationException(
					new StringBuilder().append("loop inject constructor error. the inject context path is ["
					)
							.append(context)
							.append(">")
							.append(factoryname
							)
							.append("],请检查配置文件是否配置正确[")
							.append(property.getConfigFile()
							)
							.append("]").toString());
		}
		if (lo.getObj() != null)
			return lo.getObj();

		Object ret = property.getApplicationContext().getBeanObject(
				callcontext, factoryname);
		if (ret == null) {
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

	/**
	 * @param providerManagerInfo
	 * @param context
	 * @param ignoreconstruction  工厂模式实例化工厂实例时需要忽略构造函数，因为构造函数是作为工厂方法创建组件实例的参数
	 * @return
	 */
	private Object initbean(BeanInf providerManagerInfo, CallContext context, boolean ignoreconstruction) {
		try {

			Class cls = null;
			ClassInfo classInfo = null;
			if (!ignoreconstruction) {
				cls = providerManagerInfo.getBeanClass();
				classInfo = ClassUtil.getClassInfo(cls);
			} else {
				if (providerManagerInfo.getIocplugin() == null)
					cls = providerManagerInfo.getFactoryClass();
				else
					cls = providerManagerInfo.getIocpluginClass();
				classInfo = ClassUtil.getClassInfo(cls);
				return context.getLoopContext().setCurrentObj(classInfo.getDefaultConstruction().newInstance());
			}

			if (providerManagerInfo.getConstruction() == null
					|| providerManagerInfo.getConstructorParams() == null
					|| providerManagerInfo.getConstructorParams().size() == 0) {

				return context.getLoopContext().setCurrentObj(classInfo.getDefaultConstruction().newInstance());
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
				if (refvalue != null) {
					if (!parameterTypes[i].isInstance(refvalue)) {
						if (refvalue != null) {
							refvalue = ValueObjectUtil.typeCast(refvalue, refvalue
									.getClass(), parameterTypes[i]);
							values[i] = refvalue;
						}

					} else {

					}
				} else {
					values[i] = ValueObjectUtil.getDefaultValue(parameterTypes[i]);
				}


			}


			return context.getLoopContext().setCurrentObj(constructor.newInstance(values));
		} catch (InstantiationException e) {
			throw new BeanInstanceException(new StringBuilder().append("providerManagerInfo["
			)
					.append(providerManagerInfo.getName())
					.append("],请检查配置文件是否配置正确["
					)
					.append(providerManagerInfo.getConfigFile())
					.append("]").toString(), e);
		} catch (IllegalAccessException e) {
			throw new BeanInstanceException(new StringBuilder().append("providerManagerInfo["
			)
					.append(providerManagerInfo.getName())
					.append("],请检查配置文件是否配置正确["
					)
					.append(providerManagerInfo.getConfigFile())
					.append("]").toString(), e);
		} catch (CurrentlyInCreationException e) {
			throw e;
		} catch (BeanInstanceException e) {
			throw e;
		} catch (Exception e) {
			throw new BeanInstanceException(new StringBuilder().append("providerManagerInfo["
			)
					.append(providerManagerInfo.getName())
					.append("],请检查配置文件是否配置正确["
					)
					.append(providerManagerInfo.getConfigFile())
					.append("]").toString(), e);
		}
	}

	/**
	 * 通过工厂方法获取组件实例
	 *
	 * @param providerManagerInfo
	 * @param callcontext
	 * @return
	 */
	private Object getBeanFromFactory(BeanInf providerManagerInfo, CallContext callcontext) {
		Pro pro = (Pro) providerManagerInfo;
		String factoryMethod = pro.getFactory_method();
		if (factoryMethod == null || factoryMethod.equals(""))
			throw new CurrentlyInCreationException(new StringBuilder().append(pro.getName())
					.append("@")
					.append(pro.getConfigFile())
					.append("，没有指定工厂创建方法。请检查[factory-method]属性配置是否正确。").toString());
		if (pro.getFactory_bean() != null)//使用工厂bean创建组件实例
		{
			//获取工厂对象
			Context cloopcontext = callcontext.getLoopContext();
			Object factory = null;
			try {
				String beanname = providerManagerInfo.getFactory_bean();
				factory = this.getFactoryRefValue(pro, beanname, callcontext, null);
				if (factory == null) {
					throw new CurrentlyInCreationException(new StringBuilder().append(pro.getName())
							.append("@")
							.append(pro.getConfigFile())
							.append("，没有找到对象工厂[")
							.append(beanname)
							.append("]。请检查[factory-bean]属性配置是否正确。").toString());
				}
//				//通过工厂对象创建组件实例
//				//1.构建工厂创建路径上下文，防止工厂对象在创建组件实例时产生循环依赖
//
//				if (callcontext == null)
//					callcontext = new LocalCallContextImpl(providerManagerInfo
//							.getApplicationContext());
//				if (callcontext.getLoopContext() == null) {
//					context = new Context(providerManagerInfo.getXpath());
//					callcontext.setLoopContext(context);
//				} else {
//					context = new Context(callcontext.getLoopContext(),
//							providerManagerInfo.getXpath());
//					callcontext.setLoopContext(context);
//				}
			} finally {
				if (cloopcontext != null)
					callcontext.setLoopContext(cloopcontext);
			}
			//2.创建组件实例
			Object bean = creatorBeanByFactoryBean(pro, factory, factoryMethod, callcontext);
			cloopcontext.setCurrentObj(bean);
			return bean;
		} else//使用工厂类静态方法创建组件实例
		{
			String factoryClass = pro.getFactory_class();
			try {
				Class cls = pro.getFactoryClass();
//				//通过工厂对象创建组件实例
//				//1.构建工厂创建路径上下文，防止工厂对象在创建组件实例时产生循环依赖
//				Context context = null;
//				if (callcontext == null)
//					callcontext = new LocalCallContextImpl(providerManagerInfo
//							.getApplicationContext());
//				if (callcontext.getLoopContext() == null) {
//					context = new Context(providerManagerInfo.getXpath());
//					callcontext.setLoopContext(context);
//				} else {
//					context = new Context(callcontext.getLoopContext(),
//							providerManagerInfo.getXpath());
//					callcontext.setLoopContext(context);
//				}
				Object bean = creatorBeanByFactoryClass(pro, cls, factoryMethod, callcontext);
				callcontext.getLoopContext().setCurrentObj(bean);
				return bean;
			} catch (CurrentlyInCreationException e) {
				throw e;
			} catch (BeanInstanceException e) {
				throw e;
			} catch (Exception e) {
				throw new CurrentlyInCreationException(new StringBuilder().append(pro.getName())
						.append("@")
						.append(pro.getConfigFile())
						.append("，没有找到对象工厂[")
						.append(factoryClass)
						.append("]类。请检查[factory-class]属性配置是否正确。").toString(), e);
			}


		}
	}

	/**
	 * 通过工厂方法获取组件实例
	 *
	 * @param providerManagerInfo
	 * @param callcontext
	 * @return
	 */
	private Object getBeanFromIOCPlugin(BeanInf providerManagerInfo, CallContext callcontext) {
		Pro pro = (Pro) providerManagerInfo;

		{

			try {
				Class cls = pro.getIocpluginClass();
				//通过工厂对象创建组件实例
				//1.构建工厂创建路径上下文，防止工厂对象在创建组件实例时产生循环依赖
//				Context context = null;
//				if (callcontext == null)
//					callcontext = new LocalCallContextImpl(providerManagerInfo
//							.getApplicationContext());
//				if (callcontext.getLoopContext() == null) {
//					context = new Context(providerManagerInfo.getXpath());
//					callcontext.setLoopContext(context);
//				} else {
//					context = new Context(callcontext.getLoopContext(),
//							providerManagerInfo.getXpath());
//					callcontext.setLoopContext(context);
//				}
				Object bean = this.creatorBeanByIocPluginClass(pro, cls, callcontext);
				callcontext.getLoopContext().setCurrentObj(bean);
				return bean;
			} catch (CurrentlyInCreationException e) {
				throw e;
			} catch (BeanInstanceException e) {
				throw e;
			} catch (Exception e) {
				throw new CurrentlyInCreationException(new StringBuilder().append(pro.getName())
						.append("@")
						.append(pro.getConfigFile())
						.append("，没有找到对象工厂[")
						.append(pro.getIocplugin())
						.append("]类。请检查[factory-class]属性配置是否正确。").toString(), e);
			}


		}
	}

	/**
	 * 通过工厂对象创建组件实例
	 *
	 * @param providerManagerInfo
	 * @param factory
	 * @param context
	 * @return
	 */
	private Object creatorBeanByFactoryBean(Pro providerManagerInfo, Object factory, String factoryMethod, CallContext context) {
		try {
			Class cls = (Class) factory.getClass();

			if (providerManagerInfo.getConstruction() == null
					|| providerManagerInfo.getConstructorParams() == null
					|| providerManagerInfo.getConstructorParams().size() == 0) {
				Method method = cls.getMethod(factoryMethod);
				if (method == null) {
					throw new CurrentlyInCreationException(new StringBuilder().append(providerManagerInfo.getName())
							.append("@")
							.append(providerManagerInfo.getConfigFile())
							.append(",对象工厂[")
							.append(factory.getClass().getName())
							.append("]没有定义")
							.append(factoryMethod)
							.append("方法。请检查[factory-method]属性配置是否正确。").toString());
				}
				/**
				 * 本地线程技术，存放CallContext上下文
				 * 如果有旧的还需要保存旧的callContext
				 */
//				return  method.invoke(factory, null);
				return invokerMethod(providerManagerInfo, factory, method, null, context);

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

//			Object bean =  method.invoke(factory,values);
			Object bean = invokerMethod(providerManagerInfo, factory, method, values, context);
			return bean;

			/**
			 * 清理本地线程
			 * 如果有旧的callContext，还需要恢复旧的上下文
			 */


		} catch (IllegalAccessException e) {
			throw new BeanInstanceException(new StringBuilder().append("providerManagerInfo["
			)
					.append(providerManagerInfo.getName())
					.append("],请检查配置文件是否配置正确["
					)
					.append(providerManagerInfo.getConfigFile())
					.append("]").toString(), e);
		} catch (CurrentlyInCreationException e) {
			throw e;
		} catch (InvocationTargetException e) {
			throw new BeanInstanceException(e.getTargetException());
		} catch (BeanInstanceException e) {
			throw e;
		} catch (Exception e) {
			throw new BeanInstanceException(new StringBuilder().append("providerManagerInfo["
			)
					.append(providerManagerInfo.getName())
					.append("],请检查配置文件是否配置正确["
					)
					.append(providerManagerInfo.getConfigFile())
					.append("]").toString(), e);
		}
	}

	private Object invokerMethod(Pro providerManagerInfo, Object bean, Method method, Object[] params, CallContext context) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		try {
			/**
			 * 本地线程技术，存放CallContext上下文
			 * 如果有旧的还需要保存旧的callContext
			 */
			return method.invoke(bean, params);
		} finally {
			/**
			 * 清理本地线程
			 * 如果有旧的callContext，还需要恢复旧的上下文
			 */
		}


	}

	/**
	 * 通过工厂类创建组件实例,工厂方法如果是静态的直接调用静态方法创建所需组件；
	 * 如果是非静态的安照普通组件处理，即先获取工厂对象实例，然后再创建组件
	 *
	 * @param providerManagerInfo
	 * @param context
	 * @return
	 */
	private Object creatorBeanByFactoryClass(Pro providerManagerInfo, Class factoryClass, String factoryMethod, CallContext context) {
		try {
			Class cls = factoryClass;

			if (providerManagerInfo.getConstruction() == null
					|| providerManagerInfo.getConstructorParams() == null
					|| providerManagerInfo.getConstructorParams().size() == 0) {
				Method method = cls.getMethod(factoryMethod);
//				method.
				if (method == null) {
					throw new CurrentlyInCreationException(new StringBuilder().append(providerManagerInfo.getName())
							.append("@")
							.append(providerManagerInfo.getConfigFile())
							.append(",对象工厂[")
							.append(factoryClass.getName())
							.append("]没有定义")
							.append(factoryMethod)
							.append("方法。请检查[factory-method]属性配置是否正确。").toString());
				}
				try {
					/**
					 * 本地线程技术，存放CallContext上下文
					 * 如果有旧的还需要保存旧的callContext
					 */
//					return  method.invoke(null);

					int mode = method.getModifiers();
					if (Modifier.isStatic(mode))//静态方法
						return this.invokerMethod(providerManagerInfo, null, method, null, context);
					else//非静态方法
					{
						Object factoryInf = getBeanFromClass(providerManagerInfo, context, true);//先获取工厂类实例
						return this.invokerMethod(providerManagerInfo, factoryInf, method, null, context);//然后调用工厂类实例方法创建组件实例
					}
				} catch (CurrentlyInCreationException e) {
					throw e;
				} catch (InvocationTargetException e) {
					throw new BeanInstanceException(e.getTargetException());
				} catch (Exception e) {
					throw new CurrentlyInCreationException(new StringBuilder().append(providerManagerInfo.getName())
							.append("@")
							.append(providerManagerInfo.getConfigFile())
							.append(",对象工厂[")
							.append(factoryClass.getName())
							.append("]没有定义")
							.append(factoryMethod)
							.append("方法,或者是非静态方法。请检查[factory-method]属性配置是否正确。").toString(), e);
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

//			Object bean =  method.invoke(null,values);
			int mode = method.getModifiers();
			if (Modifier.isStatic(mode))//静态方法
			{
				return invokerMethod(providerManagerInfo, null, method, values, context);
			} else //非静态方法
			{
				Object factoryInf = getBeanFromClass(providerManagerInfo, context, true);//先获取工厂类实例
				return invokerMethod(providerManagerInfo, factoryInf, method, values, context);//然后调用工厂类实例方法创建组件实例
			}

			/**
			 * 清理本地线程
			 * 如果有旧的callContext，还需要恢复旧的上下文
			 */


		} catch (IllegalAccessException e) {
			throw new BeanInstanceException(new StringBuilder().append("providerManagerInfo["
			)
					.append(providerManagerInfo.getName())
					.append("],请检查配置文件是否配置正确["
					)
					.append(providerManagerInfo.getConfigFile())
					.append("]").toString(), e);
		} catch (BeanInstanceException e) {
			throw e;
		} catch (CurrentlyInCreationException e) {
			throw e;
		} catch (InvocationTargetException e) {
			throw new BeanInstanceException(e.getTargetException());
		} catch (Exception e) {
			throw new BeanInstanceException(new StringBuilder().append("providerManagerInfo["
			)
					.append(providerManagerInfo.getName())
					.append("],请检查配置文件是否配置正确["
					)
					.append(providerManagerInfo.getConfigFile())
					.append("]").toString(), e);
		}
	}

	/**
	 * 通过ioc插件类创建组件实例
	 *
	 * @param providerManagerInfo
	 * @param context
	 * @return
	 */
	private Object creatorBeanByIocPluginClass(Pro providerManagerInfo, Class iocpluginClass, CallContext context) {


		try {
			/**
			 * 本地线程技术，存放CallContext上下文
			 * 如果有旧的还需要保存旧的callContext
			 */
//					return  method.invoke(null);


			IocPlugin factoryInf = (IocPlugin) getBeanFromClass(providerManagerInfo, context, true);//先获取工厂类实例

			return factoryInf.ioc(providerManagerInfo.getIocinputData(), context);


		} catch (CurrentlyInCreationException e) {
			throw e;
		} catch (Exception e) {
			throw new CurrentlyInCreationException(new StringBuilder().append(providerManagerInfo.getName())
					.append("@")
					.append(providerManagerInfo.getConfigFile())
					.append(",插件[")
					.append(iocpluginClass.getName())
					.append("]解析组件异常。请检查[iocplugin]属性配置是否正确。").toString(), e);
		}


	}

	private Object getBeanFromClass(BeanInf providerManagerInfo, CallContext callcontext) {
		return getBeanFromClass(providerManagerInfo, callcontext, false);
	}

	private Object getBeanFromClass(BeanInf providerManagerInfo, CallContext callcontext, boolean ignoreconstruction) {
		Object instance = null;
		try {

			Class<Object> cls = null;
			if (!ignoreconstruction)
				cls = providerManagerInfo.getBeanClass();
			else {
				if (providerManagerInfo.getIocplugin() == null)
					cls = providerManagerInfo.getFactoryClass();
				else
					cls = providerManagerInfo.getIocpluginClass();
			}


			instance = initbean(providerManagerInfo, callcontext, ignoreconstruction);

//			BeanInfo beanInfo = Introspector.getBeanInfo(cls);
//			PropertyDescriptor[] attributes = beanInfo.getPropertyDescriptors();

			List<Pro> refs = providerManagerInfo.getReferences();
			ClassInfo classInfo = ClassUtil.getClassInfo(cls);
			if (refs != null && refs.size() > 0) {
				//这里需要增加引用Pro的调用上下文
				Context currentLoopContext = callcontext != null ? callcontext.getLoopContext() : null;
				for (int i = 0; i < refs.size(); i++) {
					Pro ref = refs.get(i);

					String filedName = ref.getName();
					Object refvalue = null;
					try {
//						if(ref.getXpath() != null)
						refvalue = providerManagerInfo.getApplicationContext().proxyObject(ref,
								ref.getTrueValue(callcontext),
								ref.getXpath());
					} finally {
						if (callcontext != null)
							callcontext.setLoopContext(currentLoopContext);
					}

					// get bean attribute name
					PropertieDescription propertyDescriptor = classInfo.getPropertyDescriptor(filedName);


					if (propertyDescriptor != null) {


						Class type = propertyDescriptor.getPropertyType();

						// create attribute value of correct type

						Object value = refvalue != null ? ValueObjectUtil.typeCast(refvalue,
								refvalue.getClass(), type) : ValueObjectUtil.getDefaultValue(type);

//						Method wm = propertyDescriptor.getWriteMethod();

						try {
							if (propertyDescriptor.canwrite()) {
								propertyDescriptor.setValue(instance, value);
								continue;
							} else {
								if (log.isWarnEnabled()) {
									log.warn(new StringBuilder().append("设置组件[")
											.append(providerManagerInfo.getName())
											.append("]属性失败：Does not exist a writer method for field [")
											.append(propertyDescriptor.getName())
											.append("] in class [")
											.append(instance.getClass().getCanonicalName())
											.append("],请检查类定义文件是否正确设置了该字段的set方法，或者字段名称是否指定正确。").toString());
								}
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
									new StringBuilder().append("providerManagerInfo[")
											.append(providerManagerInfo.getName())
											.append("],请检查配置文件是否配置正确[")
											.append(providerManagerInfo
													.getConfigFile())
											.append("]").toString(),
									e);
						} catch (IllegalAccessException e) {
							throw new CurrentlyInCreationException(
									new StringBuilder().append("providerManagerInfo[")
											.append(providerManagerInfo.getName())
											.append("],请检查配置文件是否配置正确[")
											.append(providerManagerInfo
													.getConfigFile())
											.append("]").toString(),
									e);
						} catch (InvocationTargetException e) {
							throw new CurrentlyInCreationException(
									new StringBuilder().append("providerManagerInfo[")
											.append(providerManagerInfo.getName())
											.append("],请检查配置文件是否配置正确[")
											.append(providerManagerInfo
													.getConfigFile())
											.append("]").toString(),
									e);
						} catch (CurrentlyInCreationException e) {
							throw e;
						} catch (BeanInstanceException e) {
							throw e;
						} catch (Exception e) {
//								e.printStackTrace();
							throw new CurrentlyInCreationException(
									new StringBuilder().append("providerManagerInfo[")
											.append(providerManagerInfo.getName())
											.append("],请检查配置文件是否配置正确[")
											.append(providerManagerInfo
													.getConfigFile())
											.append("]").toString(),
									e);
						}


					} else // 引用字段名称在provider中没有定义
					{


						if (log.isWarnEnabled()) {

							Exception e = new CurrentlyInCreationException(new StringBuilder().append("引用字段[")
									.append(filedName)
									.append("]在组件[")
									.append(instance.getClass())
									.append("]中没有定义,请检查配置文件是否配置正确[")
									.append(providerManagerInfo
											.getConfigFile())
									.append("]").toString());
							log.warn("", e);
						}
					}
				}
			}
		} catch (NumberFormatException e) {
			throw new CurrentlyInCreationException(new StringBuilder().append("providerManagerInfo[")
					.append(providerManagerInfo.getName())
					.append("],请检查配置文件是否配置正确[")
					.append(providerManagerInfo.getConfigFile())
					.append("]").toString(), e);
		} catch (IllegalArgumentException e) {
			throw new CurrentlyInCreationException(new StringBuilder().append("providerManagerInfo[")
					.append(providerManagerInfo.getName())
					.append("],请检查配置文件是否配置正确[")
					.append(providerManagerInfo.getConfigFile())
					.append("]").toString(), e);
		} catch (NoSupportTypeCastException e) {
			throw new CurrentlyInCreationException(new StringBuilder().append("providerManagerInfo[")
					.append(providerManagerInfo.getName())
					.append("],请检查配置文件是否配置正确[")
					.append(providerManagerInfo.getConfigFile())
					.append("]").toString(), e);
		} catch (BeanInstanceException e) {
			throw e;
		} catch (SPIException e) {
			throw e;
		} catch (CurrentlyInCreationException e) {
			throw e;
		} catch (Exception e) {
			throw new CurrentlyInCreationException(new StringBuilder().append("providerManagerInfo[")
					.append(providerManagerInfo.getName())
					.append("],请检查配置文件是否配置正确[")
					.append(providerManagerInfo.getConfigFile())
					.append("]").toString(), e);
		}
		providerManagerInfo.getApplicationContext().initBean(instance,
				providerManagerInfo);

		return instance;
	}

	@SuppressWarnings("unchecked")
	public Object getBean(BeanInf providerManagerInfo, CallContext callcontext) {
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
				if (providerManagerInfo.getXpath() != null)
					context = new Context(callcontext.getLoopContext(),
							providerManagerInfo.getXpath());
				else {
					context = new Context(callcontext.getLoopContext(),
							providerManagerInfo.getName());
				}
				callcontext.setLoopContext(context);
			}
			if (providerManagerInfo.getFactory_bean() == null && providerManagerInfo.getFactory_class() == null) {
				if (providerManagerInfo.getIocplugin() == null)
					return getBeanFromClass(providerManagerInfo, callcontext);
				else
					return getBeanFromIOCPlugin(providerManagerInfo, callcontext);

			} else {
				return getBeanFromFactory(providerManagerInfo, callcontext);
			}
		}


	}

	public static class LoopObject {
		Object obj;

		public LoopObject() {
//			this.obj = obj;
		}

		public Object getObj() {
			return obj;
		}

		public void setObj(Object obj) {
			this.obj = obj;
		}


	}


}
