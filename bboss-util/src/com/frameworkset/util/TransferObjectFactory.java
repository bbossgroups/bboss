/*****************************************************************************
 *                                                                           *
 *  This file is part of the tna framework distribution.                     *
 *  Documentation and updates may be get from  biaoping.yin the author of    *
 *  this framework							     							 *
 *                                                                           *
 *  Sun Public License Notice:                                               *
 *                                                                           *
 *  The contents of this file are subject to the Sun Public License Version  *
 *  1.0 (the "License"); you may not use this file except in compliance with *
 *  the License. A copy of the License is available at http://www.sun.com    *
 *                                                                           *
 *  The Original Code is tag. The Initial Developer of the Original          *
 *  Code is biaoping yin. Portions created by biaoping yin are Copyright     *
 *  (C) 2000.  All Rights Reserved.                                          *
 *                                                                           *
 *  GNU Public License Notice:                                               *
 *                                                                           *
 *  Alternatively, the contents of this file may be used under the terms of  *
 *  the GNU Lesser General Public License (the "LGPL"), in which case the    *
 *  provisions of LGPL are applicable instead of those above. If you wish to *
 *  allow use of your version of this file only under the  terms of the LGPL *
 *  and not to allow others to use your version of this file under the SPL,  *
 *  indicate your decision by deleting the provisions above and replace      *
 *  them with the notice and other provisions required by the LGPL.  If you  *
 *  do not delete the provisions above, a recipient may use your version of  *
 *  this file under either the SPL or the LGPL.                              *
 *                                                                           *
 *  biaoping.yin (yin-bp@163.com)                                            *
 *                                                                           *
 *****************************************************************************/

package com.frameworkset.util;
import java.beans.Introspector;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.frameworkset.util.ClassUtil;
import org.frameworkset.util.ClassUtil.ClassInfo;
import org.frameworkset.util.ClassUtil.PropertieDescription;
import org.frameworkset.util.beans.PropertyAccessException;


/**
 * 实现对象之间属性值的复制功能，map与对象之间属性值的拷贝，hashTable/properties与对象之间属性值的拷贝
 * @author biaoping.yin
 * @version 1.0
 */
public class TransferObjectFactory 
{
	private static final Logger log = Logger.getLogger(TransferObjectFactory.class);

	/**
	* Use a HashMap to cache class information for
	* Transfer Object classes
	*/
	private static HashMap classDataInfo = new HashMap();
	/**
	* Create a Transfer Object for the given object. The
	* given object must be an EJB Implementation and have
	* a superclass that acts as the class for the entity's
	* Transfer Object. Only the fields defined in this
	* superclass are copied in to the Transfer Object.
	*/
	public static Serializable createTransferObject(
		Object ejb,
		String whichTOType,
		String completeTOType)
	{

		try
		{
			// Get the class data for the complete
			// Transfer Object type
			ClassData cData = getClassData(completeTOType);
			// Get class data for the requested TO type
			ClassData voCData = getClassData(whichTOType);
			// Create the Transfer Object of the requested
			// Transfer Object type...
			Object whichTO = Class.forName(whichTOType).newInstance();
			// get the TO fields for the requested TO
			// from the ClassData for the requested TO
			Field[] voFields = voCData.arrFields;
			// get all fields for the complete TO
			// from the ClassData for complete TO
			Field[] beanFields = cData.arrFields;
			if (voFields == null
				|| voFields.length == 0
				|| beanFields == null
				|| beanFields.length == 0)
			{
				return (Serializable) createTransferObject(ejb, whichTO);
			}
			// copy the common fields from the complete TO
			// to the fields of the requested TO
			for (int i = 0; i < voFields.length; i++)
			{
				try
				{
					String voFieldName = voFields[i].getName();
					for (int j = 0; j < beanFields.length; j++)
					{
						// if the field names are same, copy value
						if (voFieldName.equals(beanFields[j].getName()))
						{
							// Copy value from matching field
							// from the bean instance into the new
							// Transfer Object created earlier
							voFields[i].set(whichTO, beanFields[j].get(ejb));
							break;
						}
					}
				}
				catch (Exception e)
				{
					// handle exceptions that may be thrown
					// by the reflection methods...
				}
			}
			// return the requested Transfer Object
			return (java.io.Serializable) whichTO;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			// Handle all exceptions here...
		}
		return null;
	}
//	/**
//	 * added by biaoping.yin 2005.8.13
//	 * 将map中包含的属性值复制到对象中,对应属性的名称和类型必须一致
//	 * @param completeVO 有属性值的map对象
//	 * @param whichToVO 空对象
//	 * @return Object
//	 */
//	public static Object createTransferObject(
//		Map completeVO,
//		Object whichToVO)
//	{
//		if (completeVO == null || whichToVO == null)
//			return null;
//		Method[] wMethods = whichToVO.getClass().getMethods();
//		//Class cClazz = completeVO.getClass();
//		if (wMethods == null || wMethods.length == 0)
//		{
//			return whichToVO;
//		}
//		for (int i = 0; i < wMethods.length; i++)
//		{
//			String wmName = wMethods[i].getName();
//			if (!wmName.startsWith("set"))
//			{
//				continue;
//			}
//			String wmPrex = wmName.substring(0, 3);
//			String wfield = wmName.substring(3);
//			Object value = null;
//			//Method cMethod = null;
//			try
//			{
//				value = completeVO.get(wfield);
////				cMethod =
////					cClazz.getMethod(
////						ValueObjectUtil.getMethodName(wfield),
////						null);
//				if(value == null)
//				{
//					log.info(
//						new StringBuffer("warning:[")
//							.append(wfield)
//							.append(" not found in hashmap")
//							.append("]!")
//							.toString());
//					continue;
//				}
//				Class retType = value.getClass();
//				Class paramType = wMethods[i].getParameterTypes()[0];
//
//				if (retType == paramType)
//				{
//					ValueObjectUtil.invoke(
//						whichToVO,
//						wMethods[i],
//						new Object[] {
//							 value});
//					//wMethods[i].invoke(whichToVO,new Object[] {cMethod.invoke(
//					//completeVO, null)});
//				}
//				else
//				{
//					try
//					{
//						Object obj =
//							ValueObjectUtil.typeCast(
//								value,
//								retType,
//								paramType);
//						ValueObjectUtil.invoke(
//							whichToVO,
//							wMethods[i],
//							new Object[] { obj });
//					}
//					catch (NoSupportTypeCastException e)
//					{
//						throw e;
//						//System.out.println("warning ParamTypeNotMatchException:<" + cMethod + "><" + wMethods[i] + "> return value type not match the param type!" );
//					}
//					//System.out.println("warning ParamTypeNotMatchException:<" + cMethod + "><" + wMethods[i] + "> return value type not match the param type!" );
//				}
//			}
//			catch (NullPointerException e)
//			{
//				log.info(
//					new StringBuffer("warning NullPointerException:[")
//						.append(wfield)
//						.append("] not found in [map]!")
//						.toString());
//				continue;
//			}
//			catch (NoSuchMethodException e)
//			{
//				log.info(
//					new StringBuffer("warning NoSuchMethodException:[")
//						.append(e.getMessage())
//						.append("]")
//						.toString());
//				//e.printStackTrace();
//				continue;
//			}
//			catch (SecurityException securitye)
//			{
//				log.info(
//					new StringBuffer("warning SecurityException:[")
//						.append(securitye.getMessage())
//						.append("]")
//						.toString());
//				//securitye.printStackTrace();
//				continue;
//			}
//			catch (NoSupportTypeCastException castException)
//			{
//				log.info(
//					new StringBuffer("warning NoSupportTypeCastException:[")
//							 .append(wfield).append("][")
//							 .append(wMethods[i])
//							 .append("] return value type not match the param type!")
//							 .toString());
//				continue;
//			}
//			catch (NumberFormatException e)
//			{
//				log.info(
//					new StringBuffer("warning NumberFormatException:[")
//							 .append(e.getMessage())
//							 .append("]")
//							 .toString());
//				//e.printStackTrace();
//				continue;
//			}
//			catch (IllegalArgumentException e)
//			{
//				log.info(
//					new StringBuffer("warning IllegalArgumentException:[")
//							 .append(e.getMessage())
//							 .append("]")
//							 .toString());
//				//e.printStackTrace();
//				continue;
//			}
//			catch (Exception e)
//			{
//				log.info(
//					new StringBuffer("warning Exception:[")
//							 .append(e.getMessage())
//							 .append("]")
//							 .toString());
//				//e.printStackTrace();
//				continue;
//			}
//		}
//		return whichToVO;
//	}
	
	/**
	 * added by biaoping.yin 2005.8.13
	 * 将map中包含的属性值复制到对象中,对应属性的名称和类型必须一致
	 * @param completeVO 有属性值的map对象
	 * @param whichToVO 空对象
	 * @return Object
	 */
	public static Object createTransferObject(
		Map completeVO,
		Object whichToVO)
	{
		if (completeVO == null || whichToVO == null)
			return null;
		
		ClassInfo beanInfo = null;
		try {
			beanInfo = ClassUtil.getClassInfo(whichToVO.getClass());
		} catch (Exception e) {
			throw new PropertyAccessException(new PropertyChangeEvent(whichToVO, "",
				     null, null),"获取bean 信息失败",e);
		}
		 List<PropertieDescription> attributes = beanInfo.getPropertyDescriptors();
		Set keys = completeVO.entrySet();
		Iterator keyItr = keys.iterator();
		while(keyItr.hasNext())
		{
			Map.Entry entry = (Map.Entry)keyItr.next();
			String name = String.valueOf(entry.getKey());
			Object pvalue = entry.getValue();
			for(PropertieDescription property :attributes)
			{
				if(name.equals(property.getName()))
				{
					Class type = property.getPropertyType();
//					Object pvalue = completeVO.get(name);
					
					Object value = null;
					Method writeMethod = property.getWriteMethod();
					 
//					if (editor == null) 
					if(pvalue != null)
					{
						EditorInf editor = property.getWriteMethodEditor();
						if(editor == null)
							value = ValueObjectUtil.typeCast(pvalue, pvalue
									.getClass(), type);
						else
						{
							value = ValueObjectUtil.typeCast(pvalue, editor);
						}
					}
					
					try {
						writeMethod.invoke(whichToVO, new Object[]{value});
						break;
					} catch (IllegalArgumentException e) {
						throw new PropertyAccessException(new PropertyChangeEvent(whichToVO, name,
							     null, value),"设置属性失败",e);
					} catch (IllegalAccessException e) {
						throw new PropertyAccessException(new PropertyChangeEvent(whichToVO, name,
							     null, value),"设置属性失败",e);
					} catch (InvocationTargetException e) {
						throw new PropertyAccessException(new PropertyChangeEvent(whichToVO, name,
							     null, value),"设置属性失败",e);
					}
//					else 
//					{
//						value = pvalue;
//						// value = ValueObjectUtil.typeCast(pvalue,
//						// pro.getEditorInf());
//					}
				}
			}
		}
		
		

		
		return whichToVO;
	}
//	public final static EditorInf getParamEditor(Method writeMethod)
//	{
//		if(writeMethod == null)
//			return null;
//		Annotation[] annotations = writeMethod.getAnnotations();
//		if(annotations == null || annotations.length == 0)
//			return null;
//		for(Annotation annotation:annotations)
//		{
//			if(annotation instanceof RequestParam)
//			{
//				RequestParam param = (RequestParam)annotation;
//				String editor = param.editor();					
//				if(editor != null && !editor.equals(""))
//				{
//					return (EditorInf) BeanUtils.instantiateClass(editor);
//				}
//			}
//		}
//		return null;
//	}
	
	/**
	 * added by biaoping.yin 2005.8.13
	 * 将Hashtable中包含的属性值复制到对象中,对应属性的名称和类型必须一致
	 * @param completeVO 有属性值的Hashtable对象
	 * @param whichToVO 空对象
	 * @return Object
	 */
	public static Object createTransferObject(
		Hashtable completeVO,
		Object whichToVO)
	{
		if (completeVO == null || whichToVO == null)
			return null;
		Method[] wMethods = whichToVO.getClass().getMethods();
		//Class cClazz = completeVO.getClass();
		if (wMethods == null || wMethods.length == 0)
		{
			return whichToVO;
		}
		for (int i = 0; i < wMethods.length; i++)
		{
			String wmName = wMethods[i].getName();
			if (!wmName.startsWith("set"))
			{
				continue;
			}
			String wmPrex = wmName.substring(0, 3);
			String wfield = wmName.substring(3);
			Object value = null;
			//Method cMethod = null;
			try
			{
				value = completeVO.get(wfield);
//				cMethod =
//					cClazz.getMethod(
//						ValueObjectUtil.getMethodName(wfield),
//						null);
				if(value == null)
				{
					log.info(
						new StringBuffer("warning:[")
							.append(wfield)
							.append(" not found in hashmap")
							.append("]!")
							.toString());
					continue;
				}
				Class retType = value.getClass();
				Class paramType = wMethods[i].getParameterTypes()[0];

				if (retType == paramType)
				{
					ValueObjectUtil.invoke(
						whichToVO,
						wMethods[i],
						new Object[] {
							 value});
					//wMethods[i].invoke(whichToVO,new Object[] {cMethod.invoke(
					//completeVO, null)});
				}
				else
				{
					try
					{
						Object obj =
							ValueObjectUtil.typeCast(
								value,
								retType,
								paramType);
						ValueObjectUtil.invoke(
							whichToVO,
							wMethods[i],
							new Object[] { obj });
					}
					catch (NoSupportTypeCastException e)
					{
						throw e;
						//System.out.println("warning ParamTypeNotMatchException:<" + cMethod + "><" + wMethods[i] + "> return value type not match the param type!" );
					}
					//System.out.println("warning ParamTypeNotMatchException:<" + cMethod + "><" + wMethods[i] + "> return value type not match the param type!" );
				}
			}
			catch (NullPointerException e)
			{
				log.info(
					new StringBuffer("warning NullPointerException:[")
						.append(wfield)
						.append("] not found in [map]!")
						.toString());
				continue;
			}
			catch (NoSuchMethodException e)
			{
				log.info(
					new StringBuffer("warning NoSuchMethodException:[")
						.append(e.getMessage())
						.append("]")
						.toString());
				//e.printStackTrace();
				continue;
			}
			catch (SecurityException securitye)
			{
				log.info(
					new StringBuffer("warning SecurityException:[")
						.append(securitye.getMessage())
						.append("]")
						.toString());
				//securitye.printStackTrace();
				continue;
			}
			catch (NoSupportTypeCastException castException)
			{
				log.info(
					new StringBuffer("warning NoSupportTypeCastException:[")
							 .append(wfield).append("][")
							 .append(wMethods[i])
							 .append("] return value type not match the param type!")
							 .toString());
				continue;
			}
			catch (NumberFormatException e)
			{
				log.info(
					new StringBuffer("warning NumberFormatException:[")
							 .append(e.getMessage())
							 .append("]")
							 .toString());
				//e.printStackTrace();
				continue;
			}
			catch (IllegalArgumentException e)
			{
				log.info(
					new StringBuffer("warning IllegalArgumentException:[")
							 .append(e.getMessage())
							 .append("]")
							 .toString());
				//e.printStackTrace();
				continue;
			}
			catch (Exception e)
			{
				log.info(
					new StringBuffer("warning Exception:[")
							 .append(e.getMessage())
							 .append("]")
							 .toString());
				//e.printStackTrace();
				continue;
			}
		}
		return whichToVO;
	}
	/**
	 * added by biaoping.yin 2004.5.20
	 * 将一个对象属性复制到另一个对象中,对应属性的名称和类型必须一致
	 * @param completeVO 有属性值的对象
	 * @param whichToVO 空对象
	 * @return Object
	 */
	public static Object createTransferObject(
		Object completeVO,
		Object whichToVO)
	{
		if (completeVO == null || whichToVO == null)
			return null;
		Method[] wMethods = whichToVO.getClass().getMethods();
		Class cClazz = completeVO.getClass();
		if (wMethods == null || wMethods.length == 0)
		{
			return whichToVO;
		}
		for (int i = 0; i < wMethods.length; i++)
		{
			String wmName = wMethods[i].getName();
			if (!wmName.startsWith("set"))
			{
				continue;
			}
			String wmPrex = wmName.substring(0, 3);
			String wfield = wmName.substring(3);
			Method cMethod = null;
			try
			{
				try {
					cMethod = cClazz.getMethod(ValueObjectUtil
							.getMethodName(wfield), null);
				} catch (Exception e) {
					// TODO: handle exception
				}
				if(cMethod == null)
				{
					try {
						cMethod = cClazz.getMethod(ValueObjectUtil
								.getBooleanMethodName(wfield), null);
						if(cMethod == null)
						{
							log.info(
							new StringBuffer("warning:[")
								.append(wMethods[i])
								.append("]")
								.append(ValueObjectUtil.getMethodName(wfield))
								.append(" not found in [")
								.append(cClazz)
								.append("]!")
								.toString());
							continue;
						}
					} catch (Exception e) {
							log.info(
							new StringBuffer("warning:[")
								.append(wMethods[i])
								.append("]")
								.append(ValueObjectUtil.getMethodName(wfield))
								.append(" not found in [")
								.append(cClazz)
								.append("]!")
								.toString());
							continue;
						
					}
					
				}
				Class retType = cMethod.getReturnType();
				Class paramType = wMethods[i].getParameterTypes()[0];

				if (retType == paramType)
				{
					ValueObjectUtil.invoke(
						whichToVO,
						wMethods[i],
						new Object[] {
							 ValueObjectUtil.invoke(
								completeVO,
								cMethod,
								null)});
					//wMethods[i].invoke(whichToVO,new Object[] {cMethod.invoke(
					//completeVO, null)});
				}
				else
				{
					try
					{
						Object obj =
							ValueObjectUtil.typeCast(
								ValueObjectUtil.invoke(
									completeVO,
									cMethod,
									null),
								retType,
								paramType);
						ValueObjectUtil.invoke(
							whichToVO,
							wMethods[i],
							new Object[] { obj });
					}
					catch (NoSupportTypeCastException e)
					{
						throw e;
						//System.out.println("warning ParamTypeNotMatchException:<" + cMethod + "><" + wMethods[i] + "> return value type not match the param type!" );
					}
					//System.out.println("warning ParamTypeNotMatchException:<" + cMethod + "><" + wMethods[i] + "> return value type not match the param type!" );
				}
			}
			catch (NullPointerException e)
			{
				log.info(
					new StringBuffer("warning NullPointerException:[")
						.append(wMethods[i])
						.append("] not found in [")
						.append(cClazz)
						.append("]!")
						.toString());
				continue;
			}
			catch (NoSuchMethodException e)
			{
				log.info(
					new StringBuffer("warning NoSuchMethodException:[")
						.append(e.getMessage())
						.append("]")
						.toString());
				//e.printStackTrace();
				continue;
			}
			catch (SecurityException securitye)
			{
				log.info(
					new StringBuffer("warning SecurityException:[")
						.append(securitye.getMessage())
						.append("]")
						.toString());
				//securitye.printStackTrace();
				continue;
			}
			catch (NoSupportTypeCastException castException)
			{
				log.info(
					new StringBuffer("warning NoSupportTypeCastException:[")
							 .append(cMethod).append("][")
							 .append(wMethods[i])
							 .append("] return value type not match the param type!"+ castException.getMessage())
							 .toString(),castException);
				continue;
			}
			catch (NumberFormatException e)
			{
				log.info(
					new StringBuffer("warning NumberFormatException:[")
							 .append(e.getMessage())
							 .append("]")
							 .toString());
				//e.printStackTrace();
				continue;
			}
			catch (IllegalArgumentException e)
			{
				log.info(
					new StringBuffer("warning IllegalArgumentException:[")
							 .append(e.getMessage())
							 .append("]")
							 .toString());
				//e.printStackTrace();
				continue;
			}
			catch (Exception e)
			{
				log.info(
					new StringBuffer("warning Exception:[")
							 .append(e.getMessage())
							 .append("]")
							 .toString());
				//e.printStackTrace();
				continue;
			}
		}
		return whichToVO;
	}
	/**
	* Return a ClassData object that contains the
	* information needed to create
	* a Transfer Object for the given class. This information
	* is only obtained from the
	* class using reflection once, after that it will be
	* obtained from the classDataInfo HashMap.
	*/
	private static ClassData getClassData(String className)
	{
		ClassData cData = (ClassData) classDataInfo.get(className);
		try
		{
			if (cData == null)
			{
				// Get the class of the given object and the
				// Transfer Object to be created
				Field[] arrFields;
				Class ejbTOClass = Class.forName(className);
				// Determine the fields that must be copied
				arrFields = ejbTOClass.getFields();
				cData =
					new ClassData(
						ejbTOClass,
						arrFields,
						ejbTOClass.getMethods());
				classDataInfo.put(className, cData);
			}
		}
		catch (Exception e)
		{
			// handle exceptions here...
		}
		return cData;
	}

	public static void main(String[] args)
	{
		//Logger log = Logger.getLogger(TransferObjectFactory.class);

		log.debug("debug:aaaaa");
		log.info("info:bbbbbbb");
		log.warn("warn:bbbbbbb");
		log.error("error:bbbbbbb");

	}
}
/**
* Inner Class that contains class data for the
* Transfer Object classes
*/
class ClassData
{
	// Transfer Object Class
	public Class clsTransferObject;
	// Transfer Object fields
	public Field[] arrFields;
	//Transfer Object methods
	public Method[] arrMethods;
	// Constructor
	public ClassData(Class cls, Field[] fields, Method[] methods)
	{

		clsTransferObject = cls;
		arrFields = fields;
		arrMethods = methods;
	}
}
