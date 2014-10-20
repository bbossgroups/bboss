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

package org.frameworkset.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frameworkset.util.ClassUtil.Var;
import org.frameworkset.util.annotations.MapKey;
import org.frameworkset.util.annotations.Scope;
import org.frameworkset.util.annotations.wraper.AttributeWraper;
import org.frameworkset.util.annotations.wraper.CookieValueWraper;
import org.frameworkset.util.annotations.wraper.PagerParamWraper;
import org.frameworkset.util.annotations.wraper.PathVariableWraper;
import org.frameworkset.util.annotations.wraper.RequestBodyWraper;
import org.frameworkset.util.annotations.wraper.RequestHeaderWraper;
import org.frameworkset.util.annotations.wraper.RequestParamWraper;

import com.frameworkset.util.EditorInf;

/**
 * <p>
 * Title: MethodParameter.java
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
 * @Date 2010-10-13 下午08:58:46
 * @author biaoping.yin
 * @version 1.0
 */
public class MethodParameter {
	private static final Method methodParameterAnnotationsMethod = ClassUtils
			.getMethodIfAvailable(Method.class, "getParameterAnnotations",
					new Class[0]);

	private static final Method constructorParameterAnnotationsMethod = ClassUtils
			.getMethodIfAvailable(Constructor.class, "getParameterAnnotations",
					new Class[0]);
	
	

	private Method method;

	private EditorInf editor;

	private Constructor constructor;
	

	private final int parameterIndex;

	private Class parameterType;

	private Object[] parameterAnnotations;

	private ParameterNameDiscoverer parameterNameDiscoverer;
	private boolean isrequestbody;

	/**
	 * 请求参数名称
	 */
	private String parameterName;
	
	private String origineRequestParamName;
	public void setOrigineRequestParamName(String origineRequestParamName) {
		this.origineRequestParamName = origineRequestParamName;
	}

	private boolean namevariabled = false;
	

	public void setNamevariabled(boolean namevariabled) {
		this.namevariabled = namevariabled;
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
	
	public List<Var> getRequestParamNameToken() {
		return requestParamNameToken;
	}

	public void setRequestParamNameToken(List<Var> requestParamNameToken) {
		this.requestParamNameToken = requestParamNameToken;
	}

	private boolean dataBeanBind;
	private Scope requestScope = null;

	private int nestingLevel = 1;

	/** Map from Integer level to Integer type index */
	private Map typeIndexesPerLevel;

	private Map typeVariableMap;
	
	private String paramNamePrefix;
	private RequestParamWraper requestParam;
	private RequestHeaderWraper requestHeader;
	private AttributeWraper attribute;
	private PagerParamWraper pagerParam;
	private RequestBodyWraper requestBody;

	private MapKey	mapKey;
	
	private boolean primaryType;
	
	/**
	 * 标识参数是否被多个不同类型的注解所注解
	 */
	private boolean multiAnnotations = false;
	
	private List<MethodParameter> multiAnnotationParams;

	/**
	 * Create a new MethodParameter for the given method, with nesting level 1.
	 * 
	 * @param method
	 *            the Method to specify a parameter for
	 * @param parameterIndex
	 *            the index of the parameter
	 */
	public MethodParameter(Method method, int parameterIndex) {
		this(method, parameterIndex, 1);
	}

	/**
	 * Create a new MethodParameter for the given method.
	 * 
	 * @param method
	 *            the Method to specify a parameter for
	 * @param parameterIndex
	 *            the index of the parameter (-1 for the method return type; 0
	 *            for the first method parameter, 1 for the second method
	 *            parameter, etc)
	 * @param nestingLevel
	 *            the nesting level of the target type (typically 1; e.g. in
	 *            case of a List of Lists, 1 would indicate the nested List,
	 *            whereas 2 would indicate the element of the nested List)
	 */
	public MethodParameter(Method method, int parameterIndex, int nestingLevel) {
		Assert.notNull(method, "Method must not be null");
		this.method = method;
		this.parameterIndex = parameterIndex;
		this.nestingLevel = nestingLevel;
	}

	/**
	 * Create a new MethodParameter for the given constructor, with nesting
	 * level 1.
	 * 
	 * @param constructor
	 *            the Constructor to specify a parameter for
	 * @param parameterIndex
	 *            the index of the parameter
	 */
	public MethodParameter(Constructor constructor, int parameterIndex) {
		this(constructor, parameterIndex, 1);
	}

	/**
	 * Create a new MethodParameter for the given constructor.
	 * 
	 * @param constructor
	 *            the Constructor to specify a parameter for
	 * @param parameterIndex
	 *            the index of the parameter
	 * @param nestingLevel
	 *            the nesting level of the target type (typically 1; e.g. in
	 *            case of a List of Lists, 1 would indicate the nested List,
	 *            whereas 2 would indicate the element of the nested List)
	 */
	public MethodParameter(Constructor constructor, int parameterIndex,
			int nestingLevel) {
		Assert.notNull(constructor, "Constructor must not be null");
		this.constructor = constructor;
		this.parameterIndex = parameterIndex;
		this.nestingLevel = nestingLevel;
	}

	/**
	 * Copy constructor, resulting in an independent MethodParameter object
	 * based on the same metadata and cache state that the original object was
	 * in.
	 * 
	 * @param original
	 *            the original MethodParameter object to copy from
	 */
	public MethodParameter(MethodParameter original) {
		Assert.notNull(original, "Original must not be null");
		this.method = original.method;
		this.constructor = original.constructor;
		this.parameterIndex = original.parameterIndex;
		this.parameterType = original.parameterType;
		this.parameterAnnotations = original.parameterAnnotations;
		this.typeVariableMap = original.typeVariableMap;
	}

	/**
	 * Return the wrapped Method, if any.
	 * <p>
	 * Note: Either Method or Constructor is available.
	 * 
	 * @return the Method, or <code>null</code> if none
	 */
	public Method getMethod() {
		return this.method;
	}

	/**
	 * Return the wrapped Constructor, if any.
	 * <p>
	 * Note: Either Method or Constructor is available.
	 * 
	 * @return the Constructor, or <code>null</code> if none
	 */
	public Constructor getConstructor() {
		return this.constructor;
	}

	/**
	 * Return the index of the method/constructor parameter.
	 * 
	 * @return the parameter index (never negative)
	 */
	public int getParameterIndex() {
		return this.parameterIndex;
	}

	/**
	 * Set a resolved (generic) parameter type.
	 */
	void setParameterType(Class parameterType) {
		this.parameterType = parameterType;
	}

	/**
	 * Return the type of the method/constructor parameter.
	 * 
	 * @return the parameter type (never <code>null</code>)
	 */
	public Class getParameterType() {
		if (this.parameterType == null) {
			this.parameterType = (this.method != null ? this.method
					.getParameterTypes()[this.parameterIndex]
					: this.constructor.getParameterTypes()[this.parameterIndex]);
		}
		return this.parameterType;
	}

	/**
	 * Return the annotations associated with the method/constructor parameter.
	 * 
	 * @return the parameter annotations, or <code>null</code> if there is no
	 *         annotation support (on JDK < 1.5). The return value is an Object
	 *         array instead of an Annotation array simply for compatibility
	 *         with older JDKs; feel free to cast it to
	 *         <code>Annotation[]</code> on JDK 1.5 or higher.
	 */
	public Object[] getParameterAnnotations() {
		if (this.parameterAnnotations != null) {
			return this.parameterAnnotations;
		}
		if (methodParameterAnnotationsMethod == null) {
			return null;
		}
		Object[][] annotationArray = (this.method != null ? ((Object[][]) ReflectionUtils
				.invokeMethod(methodParameterAnnotationsMethod, this.method))
				: ((Object[][]) ReflectionUtils
						.invokeMethod(constructorParameterAnnotationsMethod,
								this.constructor)));
		this.parameterAnnotations = annotationArray[this.parameterIndex];
		return this.parameterAnnotations;
	}

	/**
	 * Initialize parameter name discovery for this method parameter.
	 * <p>
	 * This method does not actually try to retrieve the parameter name at this
	 * point; it just allows discovery to happen when the application calls
	 * {@link #getParameterName()} (if ever).
	 */
	public void initParameterNameDiscovery(
			ParameterNameDiscoverer parameterNameDiscoverer) {
		this.parameterNameDiscoverer = parameterNameDiscoverer;
	}

	/**
	 * Return the name of the method/constructor parameter.
	 * 
	 * @return the parameter name (may be <code>null</code> if no parameter name
	 *         metadata is contained in the class file or no
	 *         {@link #initParameterNameDiscovery ParameterNameDiscoverer} has
	 *         been set to begin with)
	 */
	public String getMethodParameterName() {
		String parameterName = null;
		if (parameterName == null && this.parameterNameDiscoverer != null) {
			String[] parameterNames = (this.method != null ? this.parameterNameDiscoverer
					.getParameterNames(this.method)
					: this.parameterNameDiscoverer
							.getParameterNames(this.constructor));
			if (parameterNames != null) {
				parameterName = parameterNames[this.parameterIndex];
			}
			this.parameterNameDiscoverer = null;
		}
		return parameterName;
	}
	
	public String getRequestParameterName() {

	
		return this.parameterName;
	}

	/**
	 * Increase this parameter's nesting level.
	 * 
	 * @see #getNestingLevel()
	 */
	public void increaseNestingLevel() {
		this.nestingLevel++;
	}

	/**
	 * Decrease this parameter's nesting level.
	 * 
	 * @see #getNestingLevel()
	 */
	public void decreaseNestingLevel() {
		getTypeIndexesPerLevel().remove(new Integer(this.nestingLevel));
		this.nestingLevel--;
	}

	/**
	 * Return the nesting level of the target type (typically 1; e.g. in case of
	 * a List of Lists, 1 would indicate the nested List, whereas 2 would
	 * indicate the element of the nested List).
	 */
	public int getNestingLevel() {
		return this.nestingLevel;
	}

	/**
	 * Set the type index for the current nesting level.
	 * 
	 * @param typeIndex
	 *            the corresponding type index (or <code>null</code> for the
	 *            default type index)
	 * @see #getNestingLevel()
	 */
	public void setTypeIndexForCurrentLevel(int typeIndex) {
		getTypeIndexesPerLevel().put(new Integer(this.nestingLevel),
				new Integer(typeIndex));
	}

	/**
	 * Return the type index for the current nesting level.
	 * 
	 * @return the corresponding type index, or <code>null</code> if none
	 *         specified (indicating the default type index)
	 * @see #getNestingLevel()
	 */
	public Integer getTypeIndexForCurrentLevel() {
		return getTypeIndexForLevel(this.nestingLevel);
	}

	/**
	 * Return the type index for the specified nesting level.
	 * 
	 * @param nestingLevel
	 *            the nesting level to check
	 * @return the corresponding type index, or <code>null</code> if none
	 *         specified (indicating the default type index)
	 */
	public Integer getTypeIndexForLevel(int nestingLevel) {
		return (Integer) getTypeIndexesPerLevel()
				.get(new Integer(nestingLevel));
	}

	/**
	 * Obtain the (lazily constructed) type-indexes-per-level Map.
	 */
	private Map getTypeIndexesPerLevel() {
		if (this.typeIndexesPerLevel == null) {
			this.typeIndexesPerLevel = new HashMap(4);
		}
		return this.typeIndexesPerLevel;
	}

	/**
	 * Create a new MethodParameter for the given method or constructor.
	 * <p>
	 * This is a convenience constructor for scenarios where a Method or
	 * Constructor reference is treated in a generic fashion.
	 * 
	 * @param methodOrConstructor
	 *            the Method or Constructor to specify a parameter for
	 * @param parameterIndex
	 *            the index of the parameter
	 * @return the corresponding MethodParameter instance
	 */
	public static MethodParameter forMethodOrConstructor(
			Object methodOrConstructor, int parameterIndex) {
		if (methodOrConstructor instanceof Method) {
			return new MethodParameter((Method) methodOrConstructor,
					parameterIndex);
		} else if (methodOrConstructor instanceof Constructor) {
			return new MethodParameter((Constructor) methodOrConstructor,
					parameterIndex);
		} else {
			throw new IllegalArgumentException("Given object ["
					+ methodOrConstructor
					+ "] is neither a Method nor a Constructor");
		}
	}

	public EditorInf getEditor() {
		return editor;
	}

	public void setEditor(EditorInf editor) {
		this.editor = editor;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public void setDataBeanBind(boolean dataBeanBind) {
		this.dataBeanBind = dataBeanBind;

	}
	
	public boolean isDataBeanBind()
	{
		return this.dataBeanBind;
	}

	public void setDataBindScope(Scope requestScope) {
		this.requestScope = requestScope;
	}
	
	public Scope getDataBindScope() {
		return requestScope;
	}
	private boolean required = false;
	public void setRequired(boolean required) {
		this.required = required;
		
	}
	
	public boolean isRequired()
	{
		return this.required;
	}
	
	private Object defaultValue;

	public Object getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}

	public boolean isMultiAnnotations() {
		return multiAnnotations;
	}

	public void setMultiAnnotations(boolean multiAnnotations) {
		this.multiAnnotations = multiAnnotations;
	}

	public List<MethodParameter> getMultiAnnotationParams() {
		return multiAnnotationParams;
	}

	public void setMultiAnnotationParams(
			List<MethodParameter> multiAnnotationParams) {
		if(multiAnnotationParams != null && multiAnnotationParams.size() > 0)
			this.setMultiAnnotations(true);
		this.multiAnnotationParams = multiAnnotationParams;
	}

	public Map getTypeVariableMap() {
		return typeVariableMap;
	}

	protected void setTypeVariableMap(Map typeVariableMap) {
		this.typeVariableMap = typeVariableMap;
	}

	/**
	 * @return the paramNamePrefix
	 */
	public String getParamNamePrefix() {
		return paramNamePrefix;
	}

	/**
	 * @param paramNamePrefix the paramNamePrefix to set
	 */
	public void setParamNamePrefix(String paramNamePrefix) {
		this.paramNamePrefix = paramNamePrefix;
	}

	/**
	 * @return the requestParam
	 */
	public RequestParamWraper getRequestParam() {
		return requestParam;
	}

	/**
	 * @param requestParam the requestParam to set
	 */
	public void setRequestParam(RequestParamWraper requestParam) {
		this.requestParam = requestParam;
	}
	private PathVariableWraper pathVariable;
	public void setPathVariable(PathVariableWraper param) {
		pathVariable = param;
		
	}
	
	private CookieValueWraper cookieValue;
	

	/**
	 * @return the pathVariable
	 */
	public PathVariableWraper getPathVariable() {
		return pathVariable;
	}

	/**
	 * @return the cookieValue
	 */
	public CookieValueWraper getCookieValue() {
		return cookieValue;
	}

	/**
	 * @param cookieValue the cookieValue to set
	 */
	public void setCookieValue(CookieValueWraper cookieValue) {
		this.cookieValue = cookieValue;
	}
//	private AttributeWraper attribute;

	/**
	 * @return the attribute
	 */
	public AttributeWraper getAttribute() {
		return attribute;
	}

	/**
	 * @param attribute the attribute to set
	 */
	public void setAttribute(AttributeWraper attribute) {
		this.attribute = attribute;
	}
	
	

	/**
	 * @return the requestHeader
	 */
	public RequestHeaderWraper getRequestHeader() {
		return requestHeader;
	}

	/**
	 * @param requestHeader the requestHeader to set
	 */
	public void setRequestHeader(RequestHeaderWraper requestHeader) {
		this.requestHeader = requestHeader;
	}

	public void setMapKey(MapKey mapKey)
	{

		this.mapKey = mapKey; 
		
	}

	
	public MapKey getMapKey()
	{
	
		return mapKey;
	}

	public boolean isPrimaryType() {
		return primaryType;
	}

	public void setPrimaryType(boolean isPrimaryType) {
		this.primaryType = isPrimaryType;
	}

	public PagerParamWraper getPagerParam() {
		return pagerParam;
	}

	public void setPagerParam(PagerParamWraper pagerParam) {
		this.pagerParam = pagerParam;
	}

	public boolean isIsrequestbody() {
		return isrequestbody;
	}

	public void setIsrequestbody(boolean isrequestbody) {
		this.isrequestbody = isrequestbody;
	}

	public RequestBodyWraper getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(RequestBodyWraper requestBody) {
		this.requestBody = requestBody;
	}


}
