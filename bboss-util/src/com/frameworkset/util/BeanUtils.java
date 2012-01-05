/*
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
package com.frameworkset.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.log4j.Logger;
import org.frameworkset.util.Assert;
import org.frameworkset.util.ClassUtil;
import org.frameworkset.util.ClassUtil.ClassInfo;
import org.frameworkset.util.ClassUtils;
import org.frameworkset.util.MethodParameter;
import org.frameworkset.util.ReflectionUtils;

/**
 * <p>Title: BeanUtils.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-29
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class BeanUtils {
	private static final Logger logger = Logger.getLogger(BeanUtils.class);

	private static final Map unknownEditorTypes = Collections.synchronizedMap(new WeakHashMap());


//	/**
//	 * Convenience method to instantiate a class using its no-arg constructor.
//	 * As this method doesn't try to load classes by name, it should avoid
//	 * class-loading issues.
//	 * <p>Note that this method tries to set the constructor accessible
//	 * if given a non-accessible (that is, non-public) constructor.
//	 * @param clazz class to instantiate
//	 * @return the new instance
//	 * @throws BeanInstantiationException if the bean cannot be instantiated
//	 */
//	public static Object instantiateClass(Class<?> clazz) throws BeanInstantiationException {
//		Assert.notNull(clazz, "Class must not be null");
//		if (clazz.isInterface()) {
//			throw new BeanInstantiationException(clazz, "Specified class is an interface");
//		}
//		try {
//			return instantiateClass(clazz.getDeclaredConstructor((Class[]) null), null);
//		}
//		catch (NoSuchMethodException ex) {
//			throw new BeanInstantiationException(clazz, "No default constructor found", ex);
//		}
//	}
	
	/**
	 * Convenience method to instantiate a class using its no-arg constructor.
	 * As this method doesn't try to load classes by name, it should avoid
	 * class-loading issues.
	 * <p>Note that this method tries to set the constructor accessible
	 * if given a non-accessible (that is, non-public) constructor.
	 * @param clazz class to instantiate
	 * @return the new instance
	 * @throws BeanInstantiationException if the bean cannot be instantiated
	 */
	public static <T> T instantiateClass(Class<T> clazz) throws BeanInstantiationException {
		Assert.notNull(clazz, "Class must not be null");
		if (clazz.isInterface()) {
			throw new BeanInstantiationException(clazz, "Specified class is an interface");
		}
		try {
			ClassInfo classInfo = ClassUtil.getClassInfo(clazz);
			return instantiateClass(clazz,classInfo.getDefaultConstruction(), null);
		}
		catch (NoSuchMethodException ex) {
			throw new BeanInstantiationException(clazz, "No default constructor found", ex);
		}
	}
	
	/**
	 * Convenience method to instantiate a class using its no-arg constructor.
	 * As this method doesn't try to load classes by name, it should avoid
	 * class-loading issues.
	 * <p>Note that this method tries to set the constructor accessible
	 * if given a non-accessible (that is, non-public) constructor.
	 * @param clazz class to instantiate
	 * @return the new instance
	 * @throws BeanInstantiationException if the bean cannot be instantiated
	 */
	public static Object instantiateClass(String clazz) throws BeanInstantiationException {
		Assert.notNull(clazz, "Class must not be null");
		Class claxx;
		try {
			claxx = Class.forName(clazz );
		} catch (ClassNotFoundException e) {
			throw new BeanInstantiationException(clazz, "Specified class not found", e);
		}
		return instantiateClass(claxx);
		
		
	}

	/**
	 * Convenience method to instantiate a class using the given constructor.
	 * As this method doesn't try to load classes by name, it should avoid
	 * class-loading issues.
	 * <p>Note that this method tries to set the constructor accessible
	 * if given a non-accessible (that is, non-public) constructor.
	 * @param ctor the constructor to instantiate
	 * @param args the constructor arguments to apply
	 * @return the new instance
	 * @throws BeanInstantiationException if the bean cannot be instantiated
	 */
	public static Object instantiateClass(Constructor ctor, Object[] args) throws BeanInstantiationException {
		Assert.notNull(ctor, "Constructor must not be null");
		try {
			ReflectionUtils.makeAccessible(ctor);
			return ctor.newInstance(args);
		}
		catch (InstantiationException ex) {
			throw new BeanInstantiationException(ctor.getDeclaringClass(),
					"Is it an abstract class?", ex);
		}
		catch (IllegalAccessException ex) {
			throw new BeanInstantiationException(ctor.getDeclaringClass(),
					"Has the class definition changed? Is the constructor accessible?", ex);
		}
		catch (IllegalArgumentException ex) {
			throw new BeanInstantiationException(ctor.getDeclaringClass(),
					"Illegal arguments for constructor", ex);
		}
		catch (InvocationTargetException ex) {
			throw new BeanInstantiationException(ctor.getDeclaringClass(),
					"Constructor threw exception", ex.getTargetException());
		}
	}
	
	/**
	 * Convenience method to instantiate a class using the given constructor.
	 * As this method doesn't try to load classes by name, it should avoid
	 * class-loading issues.
	 * <p>Note that this method tries to set the constructor accessible
	 * if given a non-accessible (that is, non-public) constructor.
	 * @param ctor the constructor to instantiate
	 * @param args the constructor arguments to apply
	 * @return the new instance
	 * @throws BeanInstantiationException if the bean cannot be instantiated
	 */
	public static <T> T instantiateClass(Class<T> type,Constructor ctor, Object[] args) throws BeanInstantiationException {
		Assert.notNull(ctor, "Constructor must not be null");
		try {
//			ReflectionUtils.makeAccessible(ctor);
			return (T)ctor.newInstance(args);
		}
		catch (InstantiationException ex) {
			throw new BeanInstantiationException(ctor.getDeclaringClass(),
					"Is it an abstract class?", ex);
		}
		catch (IllegalAccessException ex) {
			throw new BeanInstantiationException(ctor.getDeclaringClass(),
					"Has the class definition changed? Is the constructor accessible?", ex);
		}
		catch (IllegalArgumentException ex) {
			throw new BeanInstantiationException(ctor.getDeclaringClass(),
					"Illegal arguments for constructor", ex);
		}
		catch (InvocationTargetException ex) {
			throw new BeanInstantiationException(ctor.getDeclaringClass(),
					"Constructor threw exception", ex.getTargetException());
		}
	}

	/**
	 * Find a method with the given method name and the given parameter types,
	 * declared on the given class or one of its superclasses. Prefers public methods,
	 * but will return a protected, package access, or private method too.
	 * <p>Checks <code>Class.getMethod</code> first, falling back to
	 * <code>findDeclaredMethod</code>. This allows to find public methods
	 * without issues even in environments with restricted Java security settings.
	 * @param clazz the class to check
	 * @param methodName the name of the method to find
	 * @param paramTypes the parameter types of the method to find
	 * @return the Method object, or <code>null</code> if not found
	 * @see java.lang.Class#getMethod
	 * @see #findDeclaredMethod
	 */
	public static Method findMethod(Class clazz, String methodName, Class[] paramTypes) {
		try {
			return clazz.getMethod(methodName, paramTypes);
		}
		catch (NoSuchMethodException ex) {
			return findDeclaredMethod(clazz, methodName, paramTypes);
		}
	}

	/**
	 * Find a method with the given method name and the given parameter types,
	 * declared on the given class or one of its superclasses. Will return a public,
	 * protected, package access, or private method.
	 * <p>Checks <code>Class.getDeclaredMethod</code>, cascading upwards to all superclasses.
	 * @param clazz the class to check
	 * @param methodName the name of the method to find
	 * @param paramTypes the parameter types of the method to find
	 * @return the Method object, or <code>null</code> if not found
	 * @see java.lang.Class#getDeclaredMethod
	 */
	public static Method findDeclaredMethod(Class clazz, String methodName, Class[] paramTypes) {
		try {
			return clazz.getDeclaredMethod(methodName, paramTypes);
		}
		catch (NoSuchMethodException ex) {
			if (clazz.getSuperclass() != null) {
				return findDeclaredMethod(clazz.getSuperclass(), methodName, paramTypes);
			}
			return null;
		}
	}

	/**
	 * Find a method with the given method name and minimal parameters (best case: none),
	 * declared on the given class or one of its superclasses. Prefers public methods,
	 * but will return a protected, package access, or private method too.
	 * <p>Checks <code>Class.getMethods</code> first, falling back to
	 * <code>findDeclaredMethodWithMinimalParameters</code>. This allows to find public
	 * methods without issues even in environments with restricted Java security settings.
	 * @param clazz the class to check
	 * @param methodName the name of the method to find
	 * @return the Method object, or <code>null</code> if not found
	 * @throws IllegalArgumentException if methods of the given name were found but
	 * could not be resolved to a unique method with minimal parameters
	 * @see java.lang.Class#getMethods
	 * @see #findDeclaredMethodWithMinimalParameters
	 */
	public static Method findMethodWithMinimalParameters(Class clazz, String methodName)
			throws IllegalArgumentException {

		Method targetMethod = doFindMethodWithMinimalParameters(clazz.getDeclaredMethods(), methodName);
		if (targetMethod == null) {
			return findDeclaredMethodWithMinimalParameters(clazz, methodName);
		}
		return targetMethod;
	}

	/**
	 * Find a method with the given method name and minimal parameters (best case: none),
	 * declared on the given class or one of its superclasses. Will return a public,
	 * protected, package access, or private method.
	 * <p>Checks <code>Class.getDeclaredMethods</code>, cascading upwards to all superclasses.
	 * @param clazz the class to check
	 * @param methodName the name of the method to find
	 * @return the Method object, or <code>null</code> if not found
	 * @throws IllegalArgumentException if methods of the given name were found but
	 * could not be resolved to a unique method with minimal parameters
	 * @see java.lang.Class#getDeclaredMethods
	 */
	public static Method findDeclaredMethodWithMinimalParameters(Class clazz, String methodName)
			throws IllegalArgumentException {

		Method targetMethod = doFindMethodWithMinimalParameters(clazz.getDeclaredMethods(), methodName);
		if (targetMethod == null && clazz.getSuperclass() != null) {
			return findDeclaredMethodWithMinimalParameters(clazz.getSuperclass(), methodName);
		}
		return targetMethod;
	}

	/**
	 * Find a method with the given method name and minimal parameters (best case: none)
	 * in the given list of methods.
	 * @param methods the methods to check
	 * @param methodName the name of the method to find
	 * @return the Method object, or <code>null</code> if not found
	 * @throws IllegalArgumentException if methods of the given name were found but
	 * could not be resolved to a unique method with minimal parameters
	 */
	private static Method doFindMethodWithMinimalParameters(Method[] methods, String methodName)
			throws IllegalArgumentException {

		Method targetMethod = null;
		int numMethodsFoundWithCurrentMinimumArgs = 0;
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().equals(methodName)) {
				int numParams = methods[i].getParameterTypes().length;
				if (targetMethod == null ||
						numParams < targetMethod.getParameterTypes().length) {
					targetMethod = methods[i];
					numMethodsFoundWithCurrentMinimumArgs = 1;
				}
				else {
					if (targetMethod.getParameterTypes().length == numParams) {
						// Additional candidate with same length.
						numMethodsFoundWithCurrentMinimumArgs++;
					}
				}
			}
		}
		if (numMethodsFoundWithCurrentMinimumArgs > 1) {
			throw new IllegalArgumentException("Cannot resolve method '" + methodName +
					"' to a unique method. Attempted to resolve to overloaded method with " +
					"the least number of parameters, but there were " +
					numMethodsFoundWithCurrentMinimumArgs + " candidates.");
		}
		return targetMethod;
	}

	
	
	

	/**
	 * Check if the given type represents a "simple" property:
	 * a primitive, a String or other CharSequence, a Number, a Date,
	 * a URI, a URL, a Locale, a Class, or a corresponding array.
	 * <p>Used to determine properties to check for a "simple" dependency-check.
	 * @param clazz the type to check
	 * @return whether the given type represents a "simple" property
	
	 */
	public static boolean isSimpleProperty(Class clazz) {
		Assert.notNull(clazz, "Class must not be null");
		return isSimpleValueType(clazz) || (clazz.isArray() && isSimpleValueType(clazz.getComponentType()));
	}

	/**
	 * Check if the given type represents a "simple" value type:
	 * a primitive, a String or other CharSequence, a Number, a Date,
	 * a URI, a URL, a Locale or a Class.
	 * @param clazz the type to check
	 * @return whether the given type represents a "simple" value type
	 */
	public static boolean isSimpleValueType(Class clazz) {
		return ClassUtils.isPrimitiveOrWrapper(clazz) || CharSequence.class.isAssignableFrom(clazz) ||
				Number.class.isAssignableFrom(clazz) || Date.class.isAssignableFrom(clazz) ||
				clazz.equals(URI.class) || clazz.equals(URL.class) ||
				clazz.equals(Locale.class) || clazz.equals(Class.class);
	}

	/**
	 * Determine if the given target type is assignable from the given value
	 * type, assuming setting by reflection. Considers primitive wrapper
	 * classes as assignable to the corresponding primitive types.
	 * @param targetType the target type
	 * @param valueType the value type that should be assigned to the target type
	 * @return if the target type is assignable from the value type
	    ClassUtils#isAssignable(Class, Class)
	 */
	public static boolean isAssignable(Class targetType, Class valueType) {
		return ClassUtils.isAssignable(targetType, valueType);
	}

	/**
	 * Determine if the given type is assignable from the given value,
	 * assuming setting by reflection. Considers primitive wrapper classes
	 * as assignable to the corresponding primitive types.
	 * @param type the target type
	 * @param value the value that should be assigned to the type
	 * @return if the type is assignable from the value
	 * @deprecated as of Bboss 2.0, in favor of <code>ClassUtils.isAssignableValue</code>
	 * @see ClassUtils#isAssignableValue(Class, Object)
	 */
	public static boolean isAssignable(Class type, Object value) {
		return ClassUtils.isAssignableValue(type, value);
	}

	/**
	 * Obtain a new MethodParameter object for the write method of the
	 * specified property.
	 * @param pd the PropertyDescriptor for the property
	 * @return a corresponding MethodParameter object
	 */
	public static MethodParameter getWriteMethodParameter(PropertyDescriptor pd) {
		{
			return new MethodParameter(pd.getWriteMethod(), 0);
		}
	}
	

}
