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
package org.frameworkset.web.servlet.handler;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;
import org.frameworkset.http.HttpEntity;
import org.frameworkset.http.HttpHeaders;
import org.frameworkset.http.HttpInputMessage;
import org.frameworkset.http.HttpOutputMessage;
import org.frameworkset.http.MediaType;
import org.frameworkset.http.ResponseEntity;
import org.frameworkset.http.ServerHttpResponse;
import org.frameworkset.http.ServletServerHttpRequest;
import org.frameworkset.http.ServletServerHttpResponse;
import org.frameworkset.http.converter.HttpMessageConverter;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.spi.support.validate.BindingResult;
import org.frameworkset.spi.support.validate.ValidationUtils;
import org.frameworkset.spi.support.validate.Validator;
import org.frameworkset.util.AntPathMatcher;
import org.frameworkset.util.ClassUtil;
import org.frameworkset.util.ClassUtil.PropertieDescription;
import org.frameworkset.util.ClassUtils;
import org.frameworkset.util.Conventions;
import org.frameworkset.util.GenericTypeResolver;
import org.frameworkset.util.LinkedMultiValueMap;
import org.frameworkset.util.MethodParameter;
import org.frameworkset.util.MultiValueMap;
import org.frameworkset.util.ParameterUtil;
import org.frameworkset.util.PathMatcher;
import org.frameworkset.util.ReflectionUtils;
import org.frameworkset.util.annotations.AnnotationUtils;
import org.frameworkset.util.annotations.Attribute;
import org.frameworkset.util.annotations.AttributeScope;
import org.frameworkset.util.annotations.CookieValue;
import org.frameworkset.util.annotations.DataBind;
import org.frameworkset.util.annotations.HandlerMapping;
import org.frameworkset.util.annotations.HttpMethod;
import org.frameworkset.util.annotations.MapKey;
import org.frameworkset.util.annotations.MethodData;
import org.frameworkset.util.annotations.MethodInfo;
import org.frameworkset.util.annotations.ModelAttribute;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.PathVariable;
import org.frameworkset.util.annotations.RequestBody;
import org.frameworkset.util.annotations.RequestHeader;
import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.util.annotations.Scope;
import org.frameworkset.util.annotations.ValueConstants;
import org.frameworkset.util.annotations.wraper.AttributeWraper;
import org.frameworkset.util.annotations.wraper.CookieValueWraper;
import org.frameworkset.util.annotations.wraper.PathVariableWraper;
import org.frameworkset.util.annotations.wraper.RequestBodyWraper;
import org.frameworkset.util.annotations.wraper.RequestHeaderWraper;
import org.frameworkset.util.annotations.wraper.RequestParamWraper;
import org.frameworkset.util.annotations.wraper.ResponseBodyWraper;
import org.frameworkset.web.HttpMediaTypeNotAcceptableException;
import org.frameworkset.web.HttpMediaTypeNotSupportedException;
import org.frameworkset.web.HttpSessionRequiredException;
import org.frameworkset.web.bind.MissingServletRequestParameterException;
import org.frameworkset.web.bind.ServletRequestDataBinder;
import org.frameworkset.web.bind.WebDataBinder.CallHolder;
import org.frameworkset.web.multipart.IgnoreFieldNameMultipartFile;
import org.frameworkset.web.multipart.MultipartFile;
import org.frameworkset.web.multipart.MultipartHttpServletRequest;
import org.frameworkset.web.servlet.ModelAndView;
import org.frameworkset.web.servlet.ModelMap;
import org.frameworkset.web.servlet.handler.annotations.ExcludeMethod;
import org.frameworkset.web.servlet.handler.annotations.HandlerMethodInvoker;
import org.frameworkset.web.servlet.handler.annotations.HandlerMethodResolver;
import org.frameworkset.web.servlet.handler.annotations.ServletAnnotationMappingUtils;
import org.frameworkset.web.servlet.mvc.MethodNameResolver;
import org.frameworkset.web.servlet.mvc.NoSuchRequestHandlingMethodException;
import org.frameworkset.web.servlet.mvc.ServletWebRequest;
import org.frameworkset.web.servlet.mvc.mutiaction.InternalPathMethodNameResolver;
import org.frameworkset.web.servlet.support.RequestContext;
import org.frameworkset.web.servlet.view.AbstractUrlBasedView;
import org.frameworkset.web.servlet.view.UrlBasedViewResolver;
import org.frameworkset.web.servlet.view.View;
import org.frameworkset.web.util.UrlPathHelper;
import org.frameworkset.web.util.WebUtils;

import com.frameworkset.util.ArrayEditorInf;
import com.frameworkset.util.BeanUtils;
import com.frameworkset.util.EditorInf;
import com.frameworkset.util.StringUtil;
import com.frameworkset.util.ValueObjectUtil;

/**
 * <p>
 * Title: HandlerUtils.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2008
 * </p>
 * 
 * @Date 2010-11-7
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class HandlerUtils {
	protected static final Logger logger = Logger.getLogger(HandlerUtils.class);
	/** Default command name used for binding command objects: "command" */
	public static final String DEFAULT_COMMAND_NAME = "command";
	public static final String USE_MVC_DENCODE_KEY = "org.frameworkset.web.servlet.handler.HandlerUtils.USE_MVC_DENCODE_KEY";
	public static final Boolean TRUE = new Boolean(true);
	public static final PathMatcher pathMatcher = new AntPathMatcher();
	private static Method assertTokenMethod ;
	static
	{
		try {
			Class clazz = Class.forName("org.frameworkset.web.token.TokenFilter");
			assertTokenMethod = clazz.getMethod("assertDToken", ServletRequest.class,
					ServletResponse.class, MethodData.class);
		} catch (ClassNotFoundException e) {
			logger.info("get assertDToken method from org.frameworkset.web.token.TokenFilter failed:",e);
		} catch (NoSuchMethodException e) {
			logger.info("get assertDToken method from org.frameworkset.web.token.TokenFilter failed:",e);
		} catch (Exception e) {
			logger.info("get assertDToken method from org.frameworkset.web.token.TokenFilter failed:",e);
		}
		
	}

	public static boolean isExcludehandleMethod(Class<?> handlerType,
			Method method) {
		String methodName = method.getName();

		if (methodName.equals("notifyAll") || methodName.equals("notify"))
			return true;
		if (methodName.equals("wait") || methodName.equals("clone")
				|| methodName.equals("equals") || methodName.equals("hashCode")
				|| methodName.equals("getClass"))
			return true;
		Class<?>[] parameterTypes = method.getParameterTypes();
		Class<?> returnType = method.getReturnType();
		if (String.class.equals(returnType)) {

			if (parameterTypes.length == 0 && (methodName.equals("toString")))
				return true;

		}
		boolean isExcludehandleMethod = method
				.getAnnotation(ExcludeMethod.class) != null;
		if (isExcludehandleMethod)
			return true;
		Field[] fields = ClassUtil.getDeclaredFields(handlerType);// 判读方法是否为字段的get/set方法，如果是则方法不是控制器方法

		for (int i = 0; fields != null && i < fields.length; i++) {
			Field f = fields[i];
			String fname = f.getName();
			fname = String.valueOf(fname.charAt(0)).toUpperCase()
					+ fname.substring(1);

			if (methodName.equals("set" + fname)) {
				if (parameterTypes != null && parameterTypes.length == 1) {
					if (f.getType().isAssignableFrom(
							method.getParameterTypes()[0])) {
						return true;
					}
				}
			} else if ((methodName.equals("get" + fname) || methodName
					.equals("is" + fname))) {
				if (parameterTypes == null || parameterTypes.length == 0)
					return true;

			}
		}
		return false;

	}

	/**
	 * Is the supplied method a valid handler method?
	 * <p>
	 * Does not consider <code>Controller.handleRequest</code> itself as handler
	 * method (to avoid potential stack overflow).
	 */
	public static boolean isHandlerMethod(Class<?> handlerType, Method method) {
		if (Modifier.isStatic(method.getModifiers()))
			return false;
		boolean ishandleMethod = containHandleAnnotations(method);
		if (ishandleMethod)
			return true;

		boolean isExcludehandleMethod = isExcludehandleMethod(handlerType,
				method);
		if (isExcludehandleMethod)
			return false;
		boolean flag = containParamAnnotations(method);
		if (flag)
			return true;
		Class[] parameterTypes = method.getParameterTypes();
		String methodName = method.getName();
		if (("handleRequest".equals(methodName) && parameterTypes.length == 3))
			// || parameterTypes.length == 0)
			return false;

		Class returnType = method.getReturnType();

		if (ModelAndView.class.equals(returnType)
				|| Map.class.equals(returnType)
				|| String.class.equals(returnType)
				|| void.class.equals(returnType)) {
			flag = true;
			// for (int i = 0; i < parameterTypes.length; i++) {
			// flag = (HttpServletRequest.class
			// .isAssignableFrom(parameterTypes[i])
			// || HttpServletResponse.class
			// .isAssignableFrom(parameterTypes[i])
			// || PageContext.class
			// .isAssignableFrom(parameterTypes[i]) || ModelMap.class
			// .isAssignableFrom(parameterTypes[i]));
			// if (flag == false)
			// break;
			//
			// }
			return flag;
		}
		return false;
	}

	public static boolean containHandleAnnotations(Method method) {
		return method.getAnnotation(HandlerMapping.class) != null;
	}

	public static boolean containParamAnnotations(Method method) {
		ResponseBody by = method.getAnnotation(ResponseBody.class);
		if (by != null)
			return true;
		Annotation[][] paramAnnotations = method.getParameterAnnotations();
		if (paramAnnotations.length == 0)
			return false;

		for (Annotation[] annotations : paramAnnotations) {
			if (annotations.length > 0) {
				boolean ret = annotations[0] instanceof RequestParam
						|| annotations[0] instanceof Attribute
						|| annotations[0] instanceof DataBind
						|| annotations[0] instanceof PathVariable
						|| annotations[0] instanceof CookieValue
						|| annotations[0] instanceof RequestBody
						|| annotations[0] instanceof RequestHeader
						|| annotations[0] instanceof PagerParam;
				if (ret)
					return true;

			}
		}

		return false;
	}

	private static Object evaluateStringParam(RequestParamWraper requestParam,
			HttpServletRequest request, String requestParamName, Class type,
			EditorInf editor) {
		Object paramValue = null;

		String decodeCharset = requestParam.decodeCharset();
		String charset = requestParam.charset();
		String convertcharset = requestParam.convertcharset();
		if (decodeCharset != null){
			request.setAttribute(USE_MVC_DENCODE_KEY, TRUE);
		}
		String[] values = request.getParameterValues(requestParamName);
		request.setAttribute(USE_MVC_DENCODE_KEY, null);
		if (values != null) {

			if (!type.isArray() && !List.class.isAssignableFrom(type)
					&& !Set.class.isAssignableFrom(type)) {

				if (values.length > 0) {
					if (editor == null) {

						// String value_ = values[0];
						// if(decodeCharset != null)
						// {
						// try {
						// paramValue = URLDecoder.decode(value_,decodeCharset);
						// } catch (Exception e) {
						// logger.error(e);
						// paramValue = value_;
						// }
						// }
						// else if(charset != null && convertcharset != null)
						// {
						// try {
						// paramValue = new String(value_.getBytes(charset),
						// convertcharset);
						// } catch (Exception e) {
						// logger.error(e);
						// paramValue = value_;
						// }
						// }
						// else
						// {
						// paramValue = value_;
						// }
						paramValue = HandlerUtils._getRequestData(values,
								decodeCharset, charset, convertcharset);
					} else if (editor instanceof ArrayEditorInf) {
						paramValue = HandlerUtils._getRequestDatas(values,
								decodeCharset, charset, convertcharset);
					} else {
						paramValue = HandlerUtils._getRequestData(values,
								decodeCharset, charset, convertcharset);
					}

				}
			} else {
				// paramValue = values;
				// if(decodeCharset != null)
				// {
				// String[] values_ = new String[values.length];
				//
				// for(int i = 0; i < values_.length; i ++)
				// {
				// try {
				// values_[i] = URLDecoder.decode(values[i],decodeCharset);
				// } catch (Exception e) {
				// logger.error(e);
				// values_[i] = values[i];
				// }
				// }
				// paramValue = values_;
				//
				// }
				// else if(charset != null && convertcharset != null)
				// {
				// String[] values_ = new String[values.length];
				//
				// for(int i = 0; i < values_.length; i ++)
				// {
				// try {
				// values_[i] = new String(values[i].getBytes(charset),
				// convertcharset);
				// } catch (Exception e) {
				// logger.error(e);
				// values_[i] = values[i];
				// }
				// }
				// paramValue = values_;
				// }
				// else
				// {
				// paramValue = values;
				// }
				if (editor == null) {
					paramValue = HandlerUtils._getRequestDatas(values,
							decodeCharset, charset, convertcharset);
				} else if (editor instanceof ArrayEditorInf) {
					paramValue = HandlerUtils._getRequestDatas(values,
							decodeCharset, charset, convertcharset);
				} else {
					paramValue = HandlerUtils._getRequestData(values,
							decodeCharset, charset, convertcharset);
				}

			}
		}

		return paramValue;
	}

	private static Object evaluatePrimaryStringParam(
			HttpServletRequest request, String requestParamName, Class type,
			EditorInf editor) {
		Object paramValue = null;
		String[] values = request.getParameterValues(requestParamName);
		request.setAttribute(USE_MVC_DENCODE_KEY, null);
		if (values != null) {

			if (!type.isArray() && !List.class.isAssignableFrom(type)
					&& !Set.class.isAssignableFrom(type)) {
				if (values.length > 0) {
					// paramValue = values[0];
					String value_ = values[0];
					paramValue = value_;
				}
			} else {
				paramValue = values;
			}
		}

		return paramValue;
	}

	private static Object evaluateMultipartFileParam(RequestParamWraper requestParam,
			HttpServletRequest request, String requestParamName, Class type) {
		Object paramValue = null;
		if (request instanceof MultipartHttpServletRequest) {

			MultipartFile[] values = null;
			if (!HandlerUtils.isIgnoreFieldNameMultipartFile(type)) {
				values = ((MultipartHttpServletRequest) request)
						.getFiles(requestParamName);
			} else {
				values = ((MultipartHttpServletRequest) request)
						.getFirstFieldFiles();
			}

			if (values != null) {

				if (!type.isArray() && !List.class.isAssignableFrom(type)
						&& !Set.class.isAssignableFrom(type)) {
					if (values.length > 0) {

						MultipartFile value_ = values[0];

						paramValue = value_;

					}
				} else {

					paramValue = values;
				}
			}
		} else {
			logger.debug("Evaluate Multipart File failed: request is not a MultipartHttpServletRequest.");
		}

		return paramValue;
	}

	private static Object evaluateMultipartFileParamWithNoName(
			HttpServletRequest request, Class type) {
		Object paramValue = null;
		if (request instanceof MultipartHttpServletRequest) {
			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			MultipartFile[] values = multipartRequest.getFirstFieldFiles();
			if (values != null) {

				if (!type.isArray()) {
					if (values.length > 0) {
						MultipartFile value_ = values[0];
						paramValue = value_;

					}
				} else {
					paramValue = values;

				}
			}
			// Iterator<String> filenames = multipartRequest.getFileNames();
			// if(filenames == null)
			// return null;
			// while(filenames.hasNext())
			// {
			// MultipartFile[] values =
			// multipartRequest.getFiles(filenames.next());
			//
			// if (values != null) {
			//
			//
			// if (!type.isArray() )
			// {
			// if( values.length > 0)
			// {
			// MultipartFile value_ = values[0];
			// paramValue = value_;
			// break;
			// }
			// }
			// else
			// {
			// paramValue = values;
			// break;
			// }
			// }
			// }
		} else {
			logger.debug("Evaluate Multipart File failed: request is not a MultipartHttpServletRequest.");
		}

		return paramValue;
	}

	private static Object evaluateMethodArg(MethodParameter methodParameter_,
			HttpServletRequest request, HttpServletResponse response,
			PageContext pageContext, MethodData handlerMethod, ModelMap model,
			Map pathVarDatas, Validator[] validators,
			HttpMessageConverter[] messageConverters, Class type,
			MethodInfo methodInfo, int position) throws Exception {
		List<MethodParameter> methodParameters = methodParameter_
				.getMultiAnnotationParams();
		if (methodParameters != null && methodParameters.size() > 0) {
			return _evaluateMethodArg(methodParameter_, request, response,
					pageContext, handlerMethod, model, pathVarDatas,
					validators, messageConverters, type,methodInfo);
		} else {
			Object paramValue = null;
			if (List.class.isAssignableFrom(type)) {// 如果是列表数据集
				List command = new ArrayList();
				Class ct = methodInfo.getGenericParameterType(position);// 获取元素类型
//				if (ct == null) {
//					model.getErrors().rejectValue(
//							methodParameter_.getRequestParameterName(),
//							"evaluateAnnotationsValue.error",
//							"没有获取到集合参数对象类型,请检查是否指定了集合泛型："
//									+ methodInfo.getMethod().getName());
//					paramValue = ValueObjectUtil.getDefaultValue(type);
//				} else 
				{
					String paramname = methodParameter_.getRequestParameterName();
					bind(request, response, pageContext, handlerMethod, model,
							command, ct, validators, messageConverters, paramname);
					paramValue = command;
				}
			} else if (Set.class.isAssignableFrom(type)) {// 如果是Set数据集
				Set command = new TreeSet();
				String paramname = methodParameter_.getRequestParameterName();
				Class ct = methodInfo.getGenericParameterType(position);// 获取元素类型
//				if (ct == null) {
//					model.getErrors().rejectValue(
//							methodParameter_.getRequestParameterName(),
//							"evaluateAnnotationsValue.error",
//							"没有获取到集合参数对象类型,请检查是否指定了集合泛型或者通过注解指定绑定变量："
//									+ methodInfo.getMethod().getName());
//					paramValue = ValueObjectUtil.getDefaultValue(type);
//				} else 
				{
					bind(request, response, pageContext, handlerMethod, model,
							command, ct, validators, messageConverters, paramname );
					paramValue = command;
				}
			} else if (isMultipartFile(type)) {

				paramValue = evaluateMultipartFileParamWithNoName(request, type);

			} else {
				Object command = newCommandObject(type);
				bind(request, response, pageContext, handlerMethod, model,
						command, validators, messageConverters);
				paramValue = command;
			}
			if (paramValue == null) {
				paramValue = ValueObjectUtil.getDefaultValue(type);
			}
			return paramValue;
		}
	}

	private static Object _evaluateMethodArg(MethodParameter methodParameter_,
			HttpServletRequest request, HttpServletResponse response,
			PageContext pageContext, MethodData handlerMethod, ModelMap model,
			Map pathVarDatas, Validator[] validators,
			HttpMessageConverter[] messageConverters, Class type,MethodInfo methodInfo)
			throws Exception {
		Object paramValue = null;
		Object defaultValue = null;
		boolean userEditor = true;
		EditorInf editor = null;
		boolean isrequired = false;

		List<MethodParameter> methodParameters = methodParameter_
				.getMultiAnnotationParams();
		String dateformat = null;

		for (MethodParameter methodParameter : methodParameters) {
			String requestParamName = ParameterUtil.getParameterName(methodParameter,  request, 0);
			defaultValue = methodParameter.getDefaultValue();
			editor = methodParameter.getEditor();
			if (!isrequired)
				isrequired = methodParameter.isRequired();
			if (methodParameter.getDataBindScope() == Scope.REQUEST_PARAM) {
				RequestParamWraper requestParam = methodParameter.getRequestParam();
				if (!isMultipartFile(type)) {
					dateformat = requestParam.dateformat();
//					if (dateformat.equals(ValueConstants.DEFAULT_NONE))
//						dateformat = null;
					paramValue = evaluateStringParam(requestParam, request,
							requestParamName, type, editor);
				} else {
					paramValue = evaluateMultipartFileParam(requestParam,
							request, requestParamName, type);
				}
			} else if (methodParameter.getDataBindScope() == Scope.REQUEST_ATTRIBUTE) {
				paramValue = request.getAttribute(requestParamName);

			} else if (methodParameter.getDataBindScope() == Scope.SESSION_ATTRIBUTE) {
				HttpSession session = request.getSession(false);
				if (session != null)
					paramValue = session.getAttribute(requestParamName);

			} else if (methodParameter.getDataBindScope() == Scope.PATHVARIABLE) {
				if (methodParameter.getPathVariable() != null) {
					dateformat = methodParameter.getPathVariable().dateformat();

				}
				if (pathVarDatas != null) {
					if (methodParameter.getPathVariable() != null) {
						String decodeCharset = methodParameter
								.getPathVariable().decodeCharset();
						String charset = methodParameter.getPathVariable()
								.charset();
						String convertcharset = methodParameter
								.getPathVariable().convertcharset();


						String paramValue_ = (String) pathVarDatas
								.get(requestParamName);
						if (decodeCharset != null) {
							try {
								paramValue = URLDecoder.decode(paramValue_,
										decodeCharset);
							} catch (Exception e) {
								logger.error(e);
								paramValue = paramValue_;
							}
						} else if (charset != null && convertcharset != null) {
							try {
								paramValue = new String(
										paramValue_.getBytes(charset),
										convertcharset);
							} catch (Exception e) {
								logger.error(e);
								paramValue = paramValue_;
							}
						} else {
							paramValue = paramValue_;
						}
					} else {
						paramValue = pathVarDatas.get(requestParamName);
					}

				}

			} else if (methodParameter.getDataBindScope() == Scope.PAGECONTEXT_APPLICATION_SCOPE) {
				if (methodParameter.getAttribute() != null) {
					dateformat = methodParameter.getAttribute().dateformat();

				}
				paramValue = pageContext.getAttribute(requestParamName,
						PageContext.APPLICATION_SCOPE);

			} else if (methodParameter.getDataBindScope() == Scope.PAGECONTEXT_PAGE_SCOPE) {
				if (methodParameter.getAttribute() != null) {
					dateformat = methodParameter.getAttribute().dateformat();

				}
				paramValue = pageContext.getAttribute(requestParamName,
						PageContext.PAGE_SCOPE);

			} else if (methodParameter.getDataBindScope() == Scope.PAGECONTEXT_REQUEST_SCOPE) {
				if (methodParameter.getAttribute() != null) {
					dateformat = methodParameter.getAttribute().dateformat();

				}
				paramValue = pageContext.getAttribute(requestParamName,
						PageContext.REQUEST_SCOPE);

			} else if (methodParameter.getDataBindScope() == Scope.PAGECONTEXT_SESSION_SCOPE) {
				if (methodParameter.getAttribute() != null) {
					dateformat = methodParameter.getAttribute().dateformat();

				}
				paramValue = pageContext.getAttribute(requestParamName,
						PageContext.SESSION_SCOPE);

			} else if (methodParameter.getDataBindScope() == Scope.COOKIE) {
				if (methodParameter.getCookieValue() != null) {
					dateformat = methodParameter.getCookieValue().dateformat();

				}
				paramValue = resolveCookieValue(methodParameter, request);
				// userEditor = false;

			} else if (methodParameter.getDataBindScope() == Scope.PAGER_PARAM) {
				paramValue = resolvePagerParam(methodParameter, request);

			} else if (methodParameter.getDataBindScope() == Scope.MAP_PARAM) {
				paramValue = resolvePagerParam(methodParameter, request);

			} else if (methodParameter.getDataBindScope() == Scope.REQUEST_HEADER) {
				if (methodParameter.getRequestHeader() != null) {
					dateformat = methodParameter.getRequestHeader()
							.dateformat();

				}
				paramValue = resolveRequestHeader(methodParameter, request);
			} else if (methodParameter.getDataBindScope() == Scope.REQUEST_BODY) {
				paramValue = resolveRequestBody(methodParameter, request,
						messageConverters);
				if (editor == null)
					userEditor = false;
				else
					userEditor = true;

			} else if (methodParameter.isDataBeanBind()) {
				Object command = newCommandObject(type);
				bind(request, response, pageContext, handlerMethod, model,
						command, validators, messageConverters);
				paramValue = command;
				userEditor = false;

			}
			// else {
			// Object command = newCommandObject(type);
			// bind(request, response, pageContext, handlerMethod, model,
			// command, validators, messageConverters);
			// paramValue = command;
			// userEditor = false;
			//
			// }
			if (paramValue == null && defaultValue != null)
				paramValue = defaultValue;
			if (paramValue != null) {
				try {
					if (userEditor) {
						if (editor == null)
						{
							if(!ValueObjectUtil.isCollectionType(type))
							{
								paramValue = ValueObjectUtil.typeCast(paramValue,
										type, dateformat);
							}
							else
							{
								Class elementType = methodInfo.getGenericParameterType(methodParameter_.getParameterIndex());
								paramValue = ValueObjectUtil.typeCastCollection(paramValue, type, elementType, dateformat);
							}
						}
						else
							paramValue = ValueObjectUtil.typeCast(paramValue,
									editor);
					}
					break;
				} catch (Exception e) {

					Exception error = raiseMissingParameterException(
							requestParamName, type, paramValue, e);
					model.getErrors().rejectValue(requestParamName,
							"ValueObjectUtil.typeCast.error",
							String.valueOf(paramValue), type,
							error.getMessage());
					return ValueObjectUtil.getDefaultValue(type);
				}
			} else {
				// if(isrequired)
				// break;
			}
		}
		if (paramValue == null) {
			if (isrequired) {
				String paramName = null;
				if (methodParameters.size() > 1) {
					StringBuffer buffer = new StringBuffer();
					boolean flag = false;
					for (MethodParameter methodParameter : methodParameters) {
						if (flag)
							buffer.append(",")
									.append(methodParameter
											.getRequestParameterName())
									.append(":")
									.append(methodParameter.getDataBindScope());

						else {
							buffer.append(",")
									.append(methodParameter
											.getRequestParameterName())
									.append(":")
									.append(methodParameter.getDataBindScope());
							flag = true;
						}

					}
					paramName = buffer.toString();

				} else {

					paramName = methodParameters.get(0)
							.getRequestParameterName();
				}
				Exception e = raiseMissingParameterException(paramName, type);
				model.getErrors().rejectValue(paramName, "value.required.null",
						e.getMessage());
				return ValueObjectUtil.getDefaultValue(type);
			} else {
				paramValue = ValueObjectUtil.getDefaultValue(type);
			}
		}
		return paramValue;
	}

	/**
	 * 处理基础数据类型绑定，附件如何考虑呢，如果没有注解修饰 最好也是按照基础类型的方式来处理
	 * 
	 * @param methodParameter_
	 * @param request
	 * @param response
	 * @param pageContext
	 * @param handlerMethod
	 * @param model
	 * @param pathVarDatas
	 * @param validators
	 * @param messageConverters
	 * @param type
	 * @return
	 * @throws Exception
	 */
	private static Object evaluatePrimaryTypeMethodArg(
			MethodParameter methodParameter, HttpServletRequest request,
			HttpServletResponse response, PageContext pageContext,
			MethodData handlerMethod, ModelMap model, Map pathVarDatas,
			Validator[] validators, HttpMessageConverter[] messageConverters,
			Class type) throws Exception {
		Object paramValue = null;

		String requestParamName = methodParameter.getRequestParameterName();
		EditorInf editor = methodParameter.getEditor();
		if (!isMultipartFile(type)) {

			paramValue = evaluatePrimaryStringParam(request, requestParamName,
					type, editor);
		} else {
			paramValue = evaluateMultipartFileParam(null, request,
					requestParamName, type);
		}

		if (paramValue == null) {

			paramValue = ValueObjectUtil.getDefaultValue(type);

		} else {
			try {
				if (editor == null) {
					paramValue = ValueObjectUtil.typeCast(paramValue, type,
							(String) null);
				} else {
					paramValue = ValueObjectUtil.typeCast(paramValue, editor);
				}
			} catch (Exception e) {

				Exception error = raiseMissingParameterException(
						requestParamName, type, paramValue, e);
				model.getErrors().rejectValue(requestParamName,
						"ValueObjectUtil.typeCast.error",
						String.valueOf(paramValue), type, error.getMessage());
				return ValueObjectUtil.getDefaultValue(type);
			}
		}
		return paramValue;
	}

	/**
	 * 计算分页参数的值
	 * 
	 * @param methodParameter
	 * @param request
	 * @return
	 */
	private static Object resolvePagerParam(MethodParameter methodParameter,
			HttpServletRequest request) {
		Object paramValue = null;
		String name = methodParameter.getRequestParameterName();
		if (name.equals(PagerParam.PAGE_SIZE)) {
			// 获取页面size
			String cookieid = RequestContext.getPagerSizeCookieID(request,
					methodParameter.getParamNamePrefix());
			int defaultSize = RequestContext.getPagerSize(request,
					methodParameter.getDefaultValue(), cookieid);
			AbstractUrlHandlerMapping
					.exposeAttribute(
							org.frameworkset.web.servlet.HandlerMapping.PAGER_PAGESIZE_FLAG_ATTRIBUTE,
							defaultSize, request);

			AbstractUrlHandlerMapping
					.exposeAttribute(
							org.frameworkset.web.servlet.HandlerMapping.PAGER_COOKIEID_ATTRIBUTE,
							cookieid, request);
			AbstractUrlHandlerMapping
					.exposeAttribute(
							org.frameworkset.web.servlet.HandlerMapping.PAGER_CUSTOM_PAGESIZE_ATTRIBUTE,
							RequestContext.getCustomPageSize(methodParameter
									.getDefaultValue()), request);
			// String baseUri = RequestContext.getHandlerMappingPath(request);
			// String cookieid = methodParameter.getParamNamePrefix() == null ?
			// PagerDataSet.COOKIE_PREFIX + baseUri :
			// PagerDataSet.COOKIE_PREFIX + baseUri + "|"
			// +methodParameter.getParamNamePrefix();
			// int default_ = 10;
			// if(methodParameter.getDefaultValue().equals(ValueConstants.DEFAULT_NONE)
			// )
			// default_ = 10;
			// else {
			// try {
			// default_ = Integer.parseInt(String.valueOf(methodParameter
			// .getDefaultValue()));
			// } catch (Exception e) {
			// // TODO: handle exception
			// }
			// }
			// int defaultSize =
			// PagerDataSet.consumeCookie(cookieid,default_,request,null);

			paramValue = defaultSize;
		} else {
			String[] values = request.getParameterValues(name);
			if (values != null) {
				paramValue = values[0];
			} else {
				if (methodParameter.getDefaultValue() != null)
					paramValue = methodParameter.getDefaultValue();
			}
			if (name.endsWith("." + PagerParam.OFFSET) && paramValue != null) {
				try {
					long offset = Long.parseLong((String) paramValue);
					if (offset < 0)
						paramValue = "0";
				} catch (Exception e) {
					paramValue = "0";
				}

			}

		}
		return paramValue;
	}

	public static Object[] buildMethodCallArgs(HttpServletRequest request,
			HttpServletResponse response, PageContext pageContext,
			MethodData handlerMethod, ModelMap model, Validator[] validators,
			HttpMessageConverter[] messageConverters) throws Exception {
		MethodInfo methodInfo = handlerMethod.getMethodInfo();
		Method method = methodInfo.getMethod();
		Class[] methodParamTypes = method.getParameterTypes();
		Map pathVarDatas = handlerMethod.getPathVariableDatas();
		if (methodParamTypes.length == 0) {
			return new Object[0];
		}
		Object params[] = new Object[methodParamTypes.length];
		for (int i = 0; i < params.length; i++) {
			Class type = methodParamTypes[i];
			Object paramValue = null;
			MethodParameter methodParameter = methodInfo.getMethodParameter(i);
			if (HttpSession.class.isAssignableFrom(type)) {
				HttpSession session = request.getSession(false);
				if (session == null) {
					throw new HttpSessionRequiredException(
							"Pre-existing session required for handler method '"
									+ method.getName() + "'");
				}
				paramValue = session;
				// userEditor = false;

			} else if (HttpServletRequest.class.isAssignableFrom(type)) {
				paramValue = request;
				// userEditor = false;
			} else if (javax.servlet.http.HttpServletResponse.class
					.isAssignableFrom(type))

			{
				paramValue = response;
				// userEditor = false;
			} else if (PageContext.class.isAssignableFrom(type)) {
				paramValue = pageContext;
				// userEditor = false;
			} else if (ModelMap.class.isAssignableFrom(type)) {
				paramValue = model;
				// userEditor = false;
			} else if (Map.class.isAssignableFrom(type)) {
				MapKey mapKey = methodParameter.getMapKey();
				if (methodParameter == null || mapKey == null) {
					paramValue = buildParameterMaps(request);
				}
				else if (mapKey != null) 
				{
					if(!mapKey.value().equals(""))
					{
						Map command = new HashMap();
						Class[] ct = methodInfo.getGenericParameterTypes(i);// 获取元素类型
						if (ct == null) {
							model.getErrors().rejectValue(
									methodParameter.getRequestParameterName(),
									"evaluateAnnotationsValue.error",
									"没有获取到集合参数对象类型,请检查控制器方法：" + method.getName()
											+ "是否指定了集合泛型参数。");
							paramValue = ValueObjectUtil.getDefaultValue(type);
						} else {
							bind(request, response, pageContext, handlerMethod,
									model, command, ct[0], ct[1], methodParameter
											.getMapKey().value(), validators,
									messageConverters);
							paramValue = command;
						}
					}
					else
					{
						paramValue = buildParameterMaps(request,mapKey.pattern());
					}
				}
			} else if (methodParameter != null) {
				if (!methodParameter.isPrimaryType()) {
					paramValue = evaluateMethodArg(methodParameter, request,
							response, pageContext, handlerMethod, model,
							pathVarDatas, validators, messageConverters, type,
							methodInfo, i);
				} else {
					paramValue = evaluatePrimaryTypeMethodArg(methodParameter,
							request, response, pageContext, handlerMethod,
							model, pathVarDatas, validators, messageConverters,
							type);
				}
				params[i] = paramValue;
				continue;
			}

			// else if (List.class.isAssignableFrom(type)) {//如果是列表数据集
			// List command = new ArrayList();
			// Class ct = methodInfo.getGenericParameterType(i);//获取元素类型
			// if(ct == null)
			// {
			// model.getErrors().rejectValue(methodParameter.getRequestParameterName(),
			// "evaluateAnnotationsValue.error","没有获取到集合参数对象类型,请检查是否指定了集合泛型：" +
			// method.getName());
			// paramValue = ValueObjectUtil.getDefaultValue(type);
			// }
			// else
			// {
			// bind(request, response, pageContext, handlerMethod, model,
			// command, ct,validators, messageConverters);
			// paramValue = command;
			// }
			// }
			// else if (Set.class.isAssignableFrom(type)) {//如果是Set数据集
			// Set command = new TreeSet();
			//
			// Class ct = methodInfo.getGenericParameterType(i);//获取元素类型
			// if(ct == null)
			// {
			// model.getErrors().rejectValue(methodParameter.getRequestParameterName(),
			// "evaluateAnnotationsValue.error","没有获取到集合参数对象类型,请检查是否指定了集合泛型或者通过注解指定绑定变量："
			// + method.getName());
			// paramValue = ValueObjectUtil.getDefaultValue(type);
			// }
			// else
			// {
			// bind(request, response, pageContext, handlerMethod, model,
			// command, ct,validators, messageConverters);
			// paramValue = command;
			// }
			// }
			// else if(isMultipartFile(type))
			// {
			//
			// paramValue = evaluateMultipartFileParamWithNoName( request,
			// type);
			//
			// }
			// else {
			// Object command = newCommandObject(type);
			// bind(request, response, pageContext, handlerMethod, model,
			// command, validators, messageConverters);
			// paramValue = command;
			// }
			if (paramValue == null) {
				paramValue = ValueObjectUtil.getDefaultValue(type);
			}
			params[i] = paramValue;
		}
		return params;

	}

	public static Map buildParameterMaps(HttpServletRequest request) {
		Map map = new HashMap(request.getParameterMap().size());
		Enumeration<String> enums = request.getParameterNames();
		while (enums.hasMoreElements()) {
			String key = enums.nextElement();
			String[] parameters = request.getParameterValues(key);
			if (parameters != null) {
				if (parameters.length == 1)
					map.put(key, parameters[0]);
				else if (parameters.length > 1)
					map.put(key, parameters);

			}
		}
		return map;

	}
	
	public static Map buildParameterMaps(HttpServletRequest request,String namepattern) {
		Map map = new HashMap(request.getParameterMap().size());
		Enumeration<String> enums = request.getParameterNames();
		while (enums.hasMoreElements()) {
			String key = enums.nextElement();
			if(!pathMatcher.match(namepattern, key))
				continue;
			String[] parameters = request.getParameterValues(key);
			if (parameters != null) {
				if (parameters.length == 1)
					map.put(key, parameters[0]);
				else if (parameters.length > 1)
					map.put(key, parameters);

			}
		}
		return map;

	}
	
	
	
	// public static Object[] buildMethodCallArgs(HttpServletRequest request,
	// HttpServletResponse response, PageContext pageContext,
	// MethodData handlerMethod, ModelMap model, Validator[] validators,
	// HttpMessageConverter[] messageConverters) throws Exception {
	// MethodInfo methodInfo = handlerMethod.getMethodInfo();
	// Method method = methodInfo.getMethod();
	// Class[] methodParamTypes = method.getParameterTypes();
	// Map pathVarDatas = handlerMethod.getPathVariableDatas();
	// if (methodParamTypes.length == 0) {
	// return new Object[0];
	// }
	// Object params[] = new Object[methodParamTypes.length];
	// for (int i = 0; i < params.length; i++) {
	// Class type = methodParamTypes[i];
	// Object paramValue = null;
	// MethodParameter methodParameter = methodInfo.getMethodParameter(i);
	// String paramName = methodParameter != null
	// ?methodParameter.getRequestParameterName():"";
	// boolean userEditor = true;
	// if (HttpSession.class.isAssignableFrom(type)) {
	// HttpSession session = request.getSession(false);
	// if (session == null) {
	// throw new HttpSessionRequiredException(
	// "Pre-existing session required for handler method '"
	// + method.getName() + "'");
	// }
	// paramValue = session;
	// // userEditor = false;
	//
	// } else if (HttpServletRequest.class.isAssignableFrom(type))
	//
	// {
	// paramValue = request;
	// // userEditor = false;
	// } else if (javax.servlet.http.HttpServletResponse.class
	// .isAssignableFrom(type))
	//
	// {
	// paramValue = response;
	// // userEditor = false;
	// } else if (PageContext.class.isAssignableFrom(type)) {
	// paramValue = pageContext;
	// // userEditor = false;
	// } else if (ModelMap.class.isAssignableFrom(type)) {
	// paramValue = model;
	// // userEditor = false;
	// } else if (methodParameter != null) {
	// String requestParamName = methodParameter
	// .getRequestParameterName();
	//
	// if (methodParameter.getDataBindScope() == Scope.REQUEST_PARAM) {
	// String[] values = request
	// .getParameterValues(requestParamName);
	// if (values != null) {
	// if (values.length == 1)
	// paramValue = values[0];
	// else
	// paramValue = values;
	// }
	// } else if (methodParameter.getDataBindScope() == Scope.REQUEST_ATTRIBUTE)
	// {
	// paramValue = request.getAttribute(requestParamName);
	// } else if (methodParameter.getDataBindScope() == Scope.SESSION_ATTRIBUTE)
	// {
	// HttpSession session = request.getSession(false);
	// if (session != null)
	// paramValue = session.getAttribute(requestParamName);
	// } else if (methodParameter.getDataBindScope() == Scope.PATHVARIABLE) {
	// if (pathVarDatas != null)
	// paramValue = pathVarDatas.get(requestParamName);
	// } else if (methodParameter.getDataBindScope() ==
	// Scope.PAGECONTEXT_APPLICATION_SCOPE) {
	//
	// paramValue = pageContext.getAttribute(requestParamName,
	// PageContext.APPLICATION_SCOPE);
	// } else if (methodParameter.getDataBindScope() ==
	// Scope.PAGECONTEXT_PAGE_SCOPE) {
	//
	// paramValue = pageContext.getAttribute(requestParamName,
	// PageContext.PAGE_SCOPE);
	// } else if (methodParameter.getDataBindScope() ==
	// Scope.PAGECONTEXT_REQUEST_SCOPE) {
	//
	// paramValue = pageContext.getAttribute(requestParamName,
	// PageContext.REQUEST_SCOPE);
	// } else if (methodParameter.getDataBindScope() ==
	// Scope.PAGECONTEXT_SESSION_SCOPE) {
	//
	// paramValue = pageContext.getAttribute(requestParamName,
	// PageContext.SESSION_SCOPE);
	// } else if (methodParameter.getDataBindScope() == Scope.COOKIE) {
	//
	// paramValue = resolveCookieValue(methodParameter, request);
	// userEditor = false;
	// } else if (methodParameter.getDataBindScope() == Scope.REQUEST_HEADER) {
	//
	// paramValue = resolveRequestHeader(methodParameter, request);
	// } else if (methodParameter.getDataBindScope() == Scope.REQUEST_BODY) {
	// paramValue = resolveRequestBody(methodParameter, request,
	// messageConverters);
	// userEditor = false;
	// } else if (methodParameter.isDataBeanBind()) {
	// Object command = newCommandObject(type);
	// bind(request, response, pageContext, handlerMethod, model,
	// command, validators, messageConverters);
	// paramValue = command;
	// userEditor = false;
	// } else {
	// Object command = newCommandObject(type);
	// bind(request, response, pageContext, handlerMethod, model,
	// command, validators, messageConverters);
	// paramValue = command;
	// userEditor = false;
	// }
	// if (userEditor) {
	// EditorInf editor = methodInfo.getEditor(i);
	//
	// try {
	// if (editor == null)
	// paramValue = ValueObjectUtil.typeCast(paramValue,
	// type);
	// else
	// paramValue = ValueObjectUtil.typeCast(paramValue,
	// editor);
	// } catch (Exception e) {
	// throw raiseMissingParameterException(paramName, type,paramValue, e);
	// }
	// }
	//
	// } else {
	// Object command = newCommandObject(type);
	// bind(request, response, pageContext, handlerMethod, model,
	// command, validators, messageConverters);
	// paramValue = command;
	// }
	//
	// if (paramValue == null) {
	//
	// if (methodParameter.getDefaultValue() == null) {
	// if (methodParameter.isRequired())
	// throw raiseMissingParameterException(paramName, type);
	// else {
	// paramValue = ValueObjectUtil.getDefaultValue(type);
	// }
	// } else {
	// if (userEditor) {
	// EditorInf editor = methodInfo.getEditor(i);
	//
	// try {
	// if (editor == null)
	// paramValue = ValueObjectUtil
	// .typeCast(methodParameter
	// .getDefaultValue(), type);
	// else
	// paramValue = ValueObjectUtil.typeCast(
	// methodParameter.getDefaultValue(),
	// editor);
	// } catch (Exception e) {
	// throw raiseMissingParameterException(paramName,
	// type, paramValue,e);
	// }
	// }
	// }
	//
	// }
	// params[i] = paramValue;
	// }
	// return params;
	//
	// }

	/**
	 * Resolves the given {@link RequestBody @RequestBody} annotation.
	 */
	protected static Object resolveRequestBody(MethodParameter methodParam,
			HttpServletRequest webRequest,
			HttpMessageConverter[] messageConverters) throws Exception {

		return resolveRequestBody(methodParam.getParameterType(),
				methodParam.getRequestParameterName(), webRequest,
				messageConverters,  methodParam.getRequestBody());
	}

	/**
	 * Resolves the given {@link RequestBody @RequestBody} annotation.
	 */
	protected static Object resolveRequestBody(Class paramType,
			String paramName, HttpServletRequest webRequest,
			HttpMessageConverter[] messageConverters,RequestBodyWraper requestBody) throws Exception {
		return readWithMessageConverters(paramType, paramName,
				createHttpInputMessage(webRequest), messageConverters,   requestBody);
		// return readWithMessageConverters(methodParam,
		// createHttpInputMessage(webRequest), methodParam
		// .getParameterType(), messageConverters);
	}

	/**
	 * Template method for creating a new HttpInputMessage instance.
	 * <p>
	 * The default implementation creates a standard
	 * {@link ServletServerHttpRequest}. This can be overridden for custom
	 * {@code HttpInputMessage} implementations
	 * 
	 * @param servletRequest
	 *            current HTTP request
	 * @return the HttpInputMessage instance to use
	 * @throws Exception
	 *             in case of errors
	 */
	protected static HttpInputMessage createHttpInputMessage(
			HttpServletRequest servletRequest) throws Exception {
		return new ServletServerHttpRequest(servletRequest);
	}

	/**
	 * Template method for creating a new HttpOuputMessage instance.
	 * <p>
	 * The default implementation creates a standard
	 * {@link ServletServerHttpResponse}. This can be overridden for custom
	 * {@code HttpOutputMessage} implementations
	 * 
	 * @param servletResponse
	 *            current HTTP response
	 * @return the HttpInputMessage instance to use
	 * @throws Exception
	 *             in case of errors
	 */
	protected static HttpOutputMessage createHttpOutputMessage(
			HttpServletResponse servletResponse) throws Exception {
		return new ServletServerHttpResponse(servletResponse);
	}

	// private HttpEntity resolveHttpEntityRequest(MethodParameter methodParam,
	// NativeWebRequest webRequest)
	// throws Exception {
	//
	// HttpInputMessage inputMessage = createHttpInputMessage(webRequest);
	// Class<?> paramType = getHttpEntityType(methodParam);
	// Object body = readWithMessageConverters(methodParam, inputMessage,
	// paramType);
	// return new HttpEntity<Object>(body, inputMessage.getHeaders());
	// }

	// private static Object readWithMessageConverters(
	// MethodParameter methodParam, HttpInputMessage inputMessage,
	// Class paramType, HttpMessageConverter[] messageConverters)
	// throws Exception {
	//
	// // MediaType contentType = inputMessage.getHeaders().getContentType();
	// // if (contentType == null) {
	// // StringBuilder builder = new StringBuilder(ClassUtils
	// // .getShortName(methodParam.getParameterType()));
	// // String paramName = methodParam.getRequestParameterName();
	// // if (paramName != null) {
	// // builder.append(' ');
	// // builder.append(paramName);
	// // }
	// // throw new HttpMediaTypeNotSupportedException(
	// // "Cannot extract parameter (" + builder.toString()
	// // + "): no Content-Type found");
	// // }
	// //
	// // List<MediaType> allSupportedMediaTypes = new ArrayList<MediaType>();
	// // if (messageConverters != null) {
	// // for (HttpMessageConverter<?> messageConverter : messageConverters) {
	// // allSupportedMediaTypes.addAll(messageConverter
	// // .getSupportedMediaTypes());
	// // if (messageConverter.canRead(paramType, contentType)) {
	// // if (logger.isDebugEnabled()) {
	// // logger.debug("Reading [" + paramType.getName()
	// // + "] as \"" + contentType + "\" using ["
	// // + messageConverter + "]");
	// // }
	// // return messageConverter.read(paramType, inputMessage);
	// // }
	// // }
	// // }
	// // throw new HttpMediaTypeNotSupportedException(contentType,
	// // allSupportedMediaTypes);
	//
	// return
	// readWithMessageConverters(methodParam.getParameterType(),methodParam.getRequestParameterName(),
	// inputMessage,
	// messageConverters);
	// }

	private static Object readWithMessageConverters(Class paramType,
			String paramName, HttpInputMessage inputMessage,
			HttpMessageConverter[] messageConverters,RequestBodyWraper requestBody) throws Exception {

		MediaType contentType = inputMessage.getHeaders().getContentType();
		if (contentType == null) {
			StringBuilder builder = new StringBuilder(
					ClassUtils.getShortName(paramType));
			// String paramName = methodParam.getRequestParameterName();
			if (paramName != null) {
				builder.append(' ');
				builder.append(paramName);
			}
			throw new HttpMediaTypeNotSupportedException(
					"Cannot extract parameter (" + builder.toString()
							+ "): no Content-Type found");
		}

//		List<MediaType> allSupportedMediaTypes = new ArrayList<MediaType>();
		HttpMessageConverter defaultmessageConverter = null;
		if (messageConverters != null) {
			for (HttpMessageConverter<?> messageConverter : messageConverters) {
//				allSupportedMediaTypes.addAll(messageConverter
//						.getSupportedMediaTypes());
				if(messageConverter.isdefault())
				{
					defaultmessageConverter = messageConverter;
				}
				if (messageConverter.canRead(requestBody.getDatatype())) {
					if (logger.isDebugEnabled()) {
						logger.debug("Reading [" + paramType.getName()
								+ "] as \"" + contentType + "\" using ["
								+ messageConverter + "]");
					}
					return messageConverter.read(paramType, inputMessage);
				}
			}
		}
		if(defaultmessageConverter != null)
		{
			return defaultmessageConverter.read(paramType, inputMessage);
		}
		
			
		throw new java.lang.IllegalAccessException("RequestBody resolve failed:No messageConverter found.Please check the field or method parameter "+paramName+"'s annotation is been setted correct.");
	}

	private static Object resolveCookieValue(MethodParameter methodParam,
			HttpServletRequest webRequest) throws Exception {
		return resolveCookieValue(methodParam.getParameterType(),
				methodParam.getRequestParameterName(), webRequest);
		// Class<?> paramType = methodParam.getParameterType();
		//
		// Cookie cookieValue = WebUtils.getCookie(webRequest,
		// methodParam.getRequestParameterName());
		// if (Cookie.class.isAssignableFrom(paramType)) {
		// return cookieValue;
		// }
		// else if (cookieValue != null) {
		// // return UrlPathHelper.decodeRequestString(webRequest,
		// cookieValue.getValue());
		// Object ret = cookieValue.getValue();
		//
		// EditorInf editor = methodParam.getEditor();
		//
		// if (editor == null)
		// ret = ValueObjectUtil.typeCast(ret, paramType);
		// else
		// ret = ValueObjectUtil.typeCast(ret,
		// editor);
		// return ret;
		//
		// }
		// else {
		// if (methodParam.getDefaultValue() != null) {
		// return methodParam.getDefaultValue();
		// }
		// else if (methodParam.isRequired()) {
		// throw
		// raiseMissingCookieException(methodParam.getRequestParameterName(),
		// paramType);
		// }
		// else
		// {
		// return null;
		// }
		// }
		// // if (cookieValue == null) {
		// // if (methodParam.getDefaultValue() != null) {
		// // return methodParam.getDefaultValue();
		// // }
		// // else if (methodParam.isRequired()) {
		// // raiseMissingCookieException(methodParam.getRequestParameterName(),
		// paramType);
		// // }
		// //
		// // }
		// // WebDataBinder binder = createBinder(webRequest, null, cookieName);
		// // initBinder(handlerForInitBinderCall, cookieName, binder,
		// webRequest);
		// // return binder.convertIfNecessary(cookieValue, paramType,
		// methodParam);
	}

	private static Object resolveCookieValue(Class<?> paramType,
			String paramname, HttpServletRequest webRequest) throws Exception {

		Cookie cookieValue = WebUtils.getCookie(webRequest, paramname);
		if (Cookie.class.isAssignableFrom(paramType)) {
			return cookieValue;
		} else if (cookieValue != null) {
			// return UrlPathHelper.decodeRequestString(webRequest,
			// cookieValue.getValue());
			Object ret = cookieValue.getValue();

			// try {
			// if (editor == null)
			// ret = ValueObjectUtil.typeCast(ret, paramType);
			// else
			// ret = ValueObjectUtil.typeCast(ret, editor);
			// return ret;
			// } catch (Exception e) {
			// throw raiseMissingParameterException(paramname, paramType,ret,
			// e);
			// }
			return ret;

		} else {
			// if (defaultValue != null) {
			// // return defaultValue;
			// Object ret = null;
			// try {
			// if (editor == null)
			// ret = ValueObjectUtil.typeCast(ret, paramType);
			// else
			// ret = ValueObjectUtil.typeCast(ret, editor);
			// return ret;
			// } catch (Exception e) {
			// throw raiseMissingParameterException(paramname, paramType,ret,
			// e);
			// }
			// } else if (required) {
			// // throw raiseMissingCookieException(paramname, paramType);
			// } else {
			// return ValueObjectUtil.getDefaultValue(paramType);
			// }
			return null;
		}
		// if (cookieValue == null) {
		// if (methodParam.getDefaultValue() != null) {
		// return methodParam.getDefaultValue();
		// }
		// else if (methodParam.isRequired()) {
		// raiseMissingCookieException(methodParam.getRequestParameterName(),
		// paramType);
		// }
		//
		// }
		// WebDataBinder binder = createBinder(webRequest, null, cookieName);
		// initBinder(handlerForInitBinderCall, cookieName, binder, webRequest);
		// return binder.convertIfNecessary(cookieValue, paramType,
		// methodParam);
	}

	private static Object resolveRequestHeader(MethodParameter methodParam,
			HttpServletRequest webRequest) throws Exception {

		// Class<?> paramType = methodParam.getParameterType();
		// String headerName = methodParam.getRequestParameterName();
		// if (Map.class.isAssignableFrom(paramType)) {
		// return resolveRequestHeaderMap((Class<? extends Map>) paramType,
		// webRequest);
		// }
		//
		// Object headerValue = null;
		// Enumeration<String> headerValues = webRequest.getHeaders(headerName);
		// if (headerValues != null) {
		// List<String> result = new ArrayList<String>();
		// for (Enumeration<String> iterator_ = webRequest
		// .getHeaders(headerName); iterator_.hasMoreElements();) {
		// result.add(iterator_.nextElement());
		// }
		// headerValue = (result.size() == 1 ? result.get(0) : result
		// .toArray());
		// return headerValue;
		// }
		// if (headerValue == null || headerValues == null) {
		// if (methodParam.getDefaultValue() != null
		// && methodParam.getDefaultValue() != ValueConstants.DEFAULT_NONE) {
		// headerValue = methodParam.getDefaultValue();
		// return headerValue;
		// } else if (methodParam.isRequired()) {
		// throw raiseMissingHeaderException(headerName, paramType);
		// } else
		// return null;
		// // headerValue = checkValue(headerName, headerValue, paramType);
		// }
		// return null;

		return resolveRequestHeader(methodParam.getParameterType(),
				methodParam.getRequestParameterName(), webRequest);

		// WebDataBinder binder = createBinder(webRequest, null, headerName);
		// initBinder(handlerForInitBinderCall, headerName, binder, webRequest);
		// return binder.convertIfNecessary(headerValue, paramType,
		// methodParam);
	}

	private static Object resolveRequestHeader(Class<?> paramType,
			String headerName, HttpServletRequest webRequest) throws Exception {

		Object headerValue = null;
		if (Map.class.isAssignableFrom(paramType)) {
			headerValue = resolveRequestHeaderMap(
					(Class<? extends Map>) paramType, webRequest);
		}

		Enumeration<String> headerValues = webRequest.getHeaders(headerName);
		if (headerValues != null) {
			List<String> result = new ArrayList<String>();
			for (Enumeration<String> iterator_ = webRequest
					.getHeaders(headerName); iterator_.hasMoreElements();) {
				result.add(iterator_.nextElement());
			}
			headerValue = (result.size() == 1 ? result.get(0) : result
					.toArray());
			// return headerValue;
		}
		// if (headerValue == null ) {
		// if (defaultValue != null) {
		// headerValue = defaultValue;
		// return headerValue;
		// } else if (required) {
		// throw raiseMissingHeaderException(headerName, paramType);
		// } else
		// {
		// return ValueObjectUtil.getDefaultValue(paramType);
		// }
		// // headerValue = checkValue(headerName, headerValue, paramType);
		// }
		return headerValue;

		// WebDataBinder binder = createBinder(webRequest, null, headerName);
		// initBinder(handlerForInitBinderCall, headerName, binder, webRequest);
		// return binder.convertIfNecessary(headerValue, paramType,
		// methodParam);
	}

	private static Map resolveRequestHeaderMap(Class<? extends Map> mapType,
			HttpServletRequest webRequest) {
		if (MultiValueMap.class.isAssignableFrom(mapType)) {
			MultiValueMap<String, String> result;
			if (HttpHeaders.class.isAssignableFrom(mapType)) {
				result = new HttpHeaders();
			} else {
				result = new LinkedMultiValueMap<String, String>();
			}
			for (Enumeration<String> iterator = webRequest.getHeaderNames(); iterator
					.hasMoreElements();) {
				String headerName = iterator.nextElement();

				for (Enumeration<String> iterator_ = webRequest
						.getHeaders(headerName); iterator_.hasMoreElements();) {
					result.add(headerName, iterator_.nextElement());
				}
			}
			return result;
		} else {
			Map<String, String> result = new LinkedHashMap<String, String>();
			for (Enumeration<String> iterator = webRequest.getHeaderNames(); iterator
					.hasMoreElements();) {
				String headerName = iterator.nextElement();
				String headerValue = webRequest.getHeader(headerName);
				result.put(headerName, headerValue);
			}
			return result;
		}
	}

	protected static Exception raiseMissingHeaderException(String headerName,
			Class paramType) throws Exception {
		return new IllegalStateException("Missing header '" + headerName
				+ "' of type [" + paramType.getName() + "]");
	}

	protected static Exception raiseMissingCookieException(String cookieName,
			Class paramType) {
		return new IllegalStateException("Missing cookie value '" + cookieName
				+ "' of type [" + paramType.getName() + "]");
	}

	private static boolean hasParameterAnnotation(PropertieDescription field) {
		
		if (field.getRequestBody() != null
				|| field.getDataBind() != null
				|| field.getPathVariable() != null//.isAnnotationPresent(PathVariable.class)
				|| field.getRequestParam() != null//isAnnotationPresent(RequestParam.class)
				|| field.getAttribute() != null//isAnnotationPresent(Attribute.class)
				|| field.getCookie() != null//isAnnotationPresent(CookieValue.class)
				|| field.getHeader() != null)

			return true;
		return false;
	}

//	/**
//	 * 指定了多个注解类型的属性，可以选择性地从不同的注解方式获取属性的值
//	 * 
//	 * @param writeMethod
//	 * @param annotations
//	 * @param pathVarDatas
//	 * @param request
//	 * @param pageContext
//	 * @param handlerMethod
//	 * @param model
//	 * @param type
//	 * @return
//	 * @throws Exception
//	 */
//	private static Object evaluateArrayPositionAnnotationsValue(
//			Annotation[] annotations, Map pathVarDatas,
//			HttpServletRequest request, String name, PageContext pageContext,
//			MethodData handlerMethod, ModelMap model, Class type, Object value)
//			throws Exception {
//
//		boolean required = false;
//		EditorInf editor = null;
//		boolean useEditor = true;
//		// boolean touched = false;
//		Object defaultValue = null;
//		String dateformat = null;
//		for (Annotation anno : annotations) {
//
//			if (anno instanceof PathVariable) {
//
//				useEditor = false;
//			} else if (anno instanceof RequestParam) {
//				RequestParam param = (RequestParam) anno;
//				if (!isMultipartFile(type)) {
//					dateformat = param.dateformat();
//					if (dateformat.equals(ValueConstants.DEFAULT_NONE))
//						dateformat = null;
//					// String decodeCharset = param.decodeCharset();
//					// String charset = param.charset();
//					// String convertcharset = param.convertcharset();
//					// if(decodeCharset.equals(ValueConstants.DEFAULT_NONE))
//					// {
//					// decodeCharset = null;
//					// }
//					// else
//					// {
//					// request.setAttribute(USE_MVC_DENCODE_KEY, TRUE);
//					// }
//					// if(charset.equals(ValueConstants.DEFAULT_NONE))
//					// {
//					// charset = null;
//					// }
//					// if(convertcharset.equals(ValueConstants.DEFAULT_NONE))
//					// {
//					// convertcharset = null;
//					// }
//					//
//					// request.setAttribute(USE_MVC_DENCODE_KEY, null);
//
//					if (param.editor() != null && !param.editor().equals(""))
//						editor = (EditorInf) BeanUtils.instantiateClass(param
//								.editor());
//					if (!required)
//						required = param.required();
//					defaultValue = param.defaultvalue();
//
//				} else {
//
//				}
//
//				useEditor = true;
//
//			} else if (anno instanceof Attribute) {
//
//				useEditor = false;
//
//			} else if (anno instanceof CookieValue) {
//
//				useEditor = false;
//
//			} else if (anno instanceof RequestHeader) {
//
//				useEditor = false;
//
//			}
//			if (defaultValue != null
//					&& defaultValue.equals(ValueConstants.DEFAULT_NONE))
//				defaultValue = null;
//			if (value == null)
//				value = defaultValue;
//			if (value != null)
//				break;
//			dateformat = null;
//		}
//
//		if (useEditor) {
//			try {
//				if (editor == null)
//					value = ValueObjectUtil.typeCast(value, type, dateformat);
//				else
//					value = ValueObjectUtil.typeCast(value, editor);
//			} catch (Exception e) {
//				Exception error = raiseMissingParameterException(name, type,
//						value, e);
//				model.getErrors().rejectValue(name,
//						"ValueObjectUtil.typeCast.error",
//						String.valueOf(value), type, error.getMessage());
//				return ValueObjectUtil.getDefaultValue(type);
//			}
//
//		}
//		if (value == null && required) {
//			Exception e = raiseMissingParameterException(name, type);
//			model.getErrors().rejectValue(name, "value.required.null",
//					e.getMessage());
//			return ValueObjectUtil.getDefaultValue(type);
//
//		}
//		return value;
//	}
	/**
	 * 单独获取名称为变量表达式的参数的值
	 * @param property
	 * @param request
	 * @param name
	 * @param model
	 * @param type
	 * @param holder
	 * @param elementType
	 * @return
	 * @throws Exception
	 */
	private static Object getNamedParameterValue(PropertieDescription property,
			 HttpServletRequest request, String name, ModelMap model,
			Class type, CallHolder holder,Class elementType) throws Exception
	{
		boolean required = false;
		EditorInf editor = null;
		boolean useEditor = true;
		int curpostion = holder.getPosition();
		Object defaultValue = null;
		String dateformat = null;
		Object value = null;
		RequestParamWraper param = property.getRequestParam();
		if (!isMultipartFile(type)) {
			dateformat = param.dateformat();
			String decodeCharset = param.decodeCharset();
			String charset = param.charset();
			String convertcharset = param.convertcharset();
			if (decodeCharset != null) {
				request.setAttribute(USE_MVC_DENCODE_KEY, TRUE);
			}

			request.setAttribute(USE_MVC_DENCODE_KEY, null);
			if (param.editor() != null && !param.editor().equals(""))
				editor = (EditorInf) BeanUtils.instantiateClass(param
						.editor());
			String paramName = ParameterUtil.getParameterName(property, name, request, curpostion);
//			String[] values = !param.name().equals("") ? request
//					.getParameterValues(param.name()) : request
//					.getParameterValues(name);
			String[] values = request.getParameterValues(paramName);
			// boolean needAddData = holder.needAddData();
			value = getRequestData(values, holder, type, decodeCharset,
					charset, convertcharset, editor,property.isNamevariabled());

			if (!required)
				required = param.required();
			defaultValue = param.defaultvalue();
			
		} else {
			MultipartFile[] values = null;
			String paramName = ParameterUtil.getParameterName(property, name, request, curpostion);
			if (!HandlerUtils.isIgnoreFieldNameMultipartFile(type)) {
//				values = !param.name().equals("") ? ((MultipartHttpServletRequest) request)
//						.getFiles(param.name())
//						: ((MultipartHttpServletRequest) request)
//								.getFiles(name);
				values = ((MultipartHttpServletRequest) request).getFiles(paramName);

			} else {
				values = ((MultipartHttpServletRequest) request)
						.getFirstFieldFiles();
			}
			value = getRequestData(values, holder, type,property.isNamevariabled());
			if (param.editor() != null && !param.editor().equals(""))
				editor = (EditorInf) BeanUtils.instantiateClass(param
						.editor());
			if (!required)
				required = param.required();
			
		}
		if (value == null)
			value = defaultValue;
		if (useEditor) {
			try {
				if (editor == null)
				{
					if(!ValueObjectUtil.isCollectionType(type))
					{
						value = ValueObjectUtil.typeCast(value, type, dateformat);
					}
					else
					{						
						value = ValueObjectUtil.typeCastCollection(value, type, elementType, dateformat);	
					}
				}
				else
					value = ValueObjectUtil.typeCast(value, editor);
			} catch (Exception e) {
				Exception error = raiseMissingParameterException(name, type,
						value, e);
				model.getErrors().rejectValue(name,
						"ValueObjectUtil.typeCast.error",
						String.valueOf(value), type, error.getMessage());
				return ValueObjectUtil.getDefaultValue(type);
			}

		}
		
		if (value == null && required) {
			Exception e = raiseMissingParameterException(name, type);
			model.getErrors().rejectValue(name, "value.required.null",
					e.getMessage());
			return ValueObjectUtil.getDefaultValue(type);

		}
		
		return value;
	}

	/**
	 * 指定了多个注解类型的属性，可以选择性地从不同的注解方式获取属性的值
	 * 
	 * @param writeMethod
	 * @param annotations
	 * @param pathVarDatas
	 * @param request
	 * @param pageContext
	 * @param handlerMethod
	 * @param model
	 * @param type
	 * @return
	 * @throws Exception
	 */
	private static Object evaluateAnnotationsValue(PropertieDescription property,
			Map pathVarDatas, HttpServletRequest request, String name,
			PageContext pageContext, MethodData handlerMethod, ModelMap model,
			Class type, CallHolder holder,Class elementType) throws Exception {
		Object value = null;
		boolean required = false;
		EditorInf editor = null;
		boolean useEditor = true;
		boolean touched = false;
		Object defaultValue = null;
		String dateformat = null;
		Annotation[] annotations = property.getAnnotations();
//		for (Annotation anno : annotations) 
		

			
			if ( property.getRequestParam() != null) {
				
				RequestParamWraper param = property.getRequestParam();
				if (!isMultipartFile(type)) {
					dateformat = param.dateformat();
					String decodeCharset = param.decodeCharset();
					String charset = param.charset();
					String convertcharset = param.convertcharset();
					if (decodeCharset != null){
						request.setAttribute(USE_MVC_DENCODE_KEY, TRUE);
					}

					request.setAttribute(USE_MVC_DENCODE_KEY, null);
					if (param.editor() != null && !param.editor().equals(""))
						editor = (EditorInf) BeanUtils.instantiateClass(param
								.editor());
					String paramName = ParameterUtil.getParameterName(property, name, request, 0);
//					String[] values = !param.name().equals("") ? request
//							.getParameterValues(param.name()) : request
//							.getParameterValues(name);
					String[] values = request.getParameterValues(paramName);
					// boolean needAddData = holder.needAddData();
					value = getRequestData(values, holder, type, decodeCharset,
							charset, convertcharset, editor,property.isNamevariabled());

					if (!required)
						required = param.required();
					defaultValue = param.defaultvalue();
					if (!property.isNamevariabled() && holder.needAddData() ) {
						holder.addData(name, values, true, editor, required,
								defaultValue, dateformat);
					}
				} else {
					MultipartFile[] values = null;
					String paramName = ParameterUtil.getParameterName(property, name, request, 0);
					if (!HandlerUtils.isIgnoreFieldNameMultipartFile(type)) {
//						values = !param.name().equals("") ? ((MultipartHttpServletRequest) request)
//								.getFiles(param.name())
//								: ((MultipartHttpServletRequest) request)
//										.getFiles(name);
						values = ((MultipartHttpServletRequest) request).getFiles(paramName);

					} else {
						values = ((MultipartHttpServletRequest) request)
								.getFirstFieldFiles();
					}
					value = getRequestData(values, holder, type,property.isNamevariabled());
					if (param.editor() != null && !param.editor().equals(""))
						editor = (EditorInf) BeanUtils.instantiateClass(param
								.editor());
					if (!required)
						required = param.required();
					if (!property.isNamevariabled() && holder.needAddData()) {
						holder.addData(name, values, true, editor, required);
					}
				}
				touched = true;
				useEditor = true;

			} 
			if (value == null && property.getPathVariable() != null) {
				dateformat = null;
				PathVariableWraper param = property.getPathVariable();
				if (pathVarDatas != null) {
					if (!param.value().equals(""))
						value = pathVarDatas.get(param.value());
					else
						value = pathVarDatas.get(name);
				}
				if (param.editor() != null && !param.editor().equals(""))
					editor = (EditorInf) BeanUtils.instantiateClass(param
							.editor());
				defaultValue = param.defaultvalue();
				dateformat = param.dateformat();
				useEditor = true;
			}
			
			if (value == null && property.getAttribute() != null) {
				dateformat = null;
				AttributeWraper param = property.getAttribute();
				String paramName = !param.name().equals("") ? param.name()
						: name;
				if (!required)
					required = param.required();

				if (param.scope() == AttributeScope.PAGECONTEXT_APPLICATION_SCOPE)
					value = pageContext.getAttribute(paramName,
							PageContext.APPLICATION_SCOPE);

				else if (param.scope() == AttributeScope.PAGECONTEXT_PAGE_SCOPE) {
					value = pageContext.getAttribute(paramName,
							PageContext.PAGE_SCOPE);

				} else if (param.scope() == AttributeScope.PAGECONTEXT_REQUEST_SCOPE) {
					value = pageContext.getAttribute(paramName,
							PageContext.REQUEST_SCOPE);

				} else if (param.scope() == AttributeScope.PAGECONTEXT_SESSION_SCOPE) {
					value = pageContext.getAttribute(paramName,
							PageContext.SESSION_SCOPE);
				} else if (param.scope() == AttributeScope.REQUEST_ATTRIBUTE) {
					value = request.getAttribute(paramName);
				} else if (param.scope() == AttributeScope.SESSION_ATTRIBUTE) {
					HttpSession session = request.getSession(false);
					if (session != null)
						value = session.getAttribute(paramName);
				} else if (param.scope() == AttributeScope.MODEL_ATTRIBUTE) {

					if (model != null)
						value = model.get(paramName);

				}
				dateformat = param.dateformat();
				if (param.editor() != null && !param.editor().equals(""))
					editor = (EditorInf) BeanUtils.instantiateClass(param
							.editor());
				defaultValue = param.defaultvalue();

				useEditor = true;

			}
			if (value == null && property.getCookie() != null) {
				dateformat = null;
				CookieValueWraper param = property.getCookie();
				dateformat = param.dateformat();
				if (!required)
					required = param.required();
				if (param.editor() != null && !param.editor().equals(""))
					editor = (EditorInf) BeanUtils.instantiateClass(param
							.editor());
				defaultValue = param.defaultvalue();
				String paramName = !param.name().equals("") ? param.name()
						: name;
				value = resolveCookieValue(type, paramName, request);
				useEditor = true;

			}
			if (value == null && property.getHeader() != null) {
				dateformat = null;

				// value = resolveRequestHeader(methodParameter, request);
				RequestHeaderWraper param =  property.getHeader();
				dateformat = param.dateformat();
				
				required = param.required();
				if (param.editor() != null && !param.editor().equals(""))
					editor = (EditorInf) BeanUtils.instantiateClass(param
							.editor());
				defaultValue = param.defaultvalue();

				// resolveRequestHeader(Class<?> paramType,String
				// headerName,Object defaultValue,
				// HttpServletRequest webRequest,boolean required)
				String paramName = !param.name().equals("") ? param.name()
						: name;
				value = resolveRequestHeader(type, paramName, request);
				useEditor = true;

			}
			if (value == null)
				value = defaultValue;
		

		if (!property.isNamevariabled() && !touched && holder.needAddData()) {
			holder.addData(name, value, editor, required, defaultValue);
		}

		if (useEditor) {
			try {
				if (editor == null)
				{
					if(!ValueObjectUtil.isCollectionType(type))
					{
						value = ValueObjectUtil.typeCast(value, type, dateformat);
					}
					else
					{						
						value = ValueObjectUtil.typeCastCollection(value, type, elementType, dateformat);	
					}
				}
				else
					value = ValueObjectUtil.typeCast(value, editor);
			} catch (Exception e) {
				Exception error = raiseMissingParameterException(name, type,
						value, e);
				model.getErrors().rejectValue(name,
						"ValueObjectUtil.typeCast.error",
						String.valueOf(value), type, error.getMessage());
				return ValueObjectUtil.getDefaultValue(type);
			}

		}
		if (value == null && required) {
			Exception e = raiseMissingParameterException(name, type);
			model.getErrors().rejectValue(name, "value.required.null",
					e.getMessage());
			return ValueObjectUtil.getDefaultValue(type);

		}
		return value;
	}

	/**
	 * 多文件附件上传参数获取
	 * 
	 * @param values
	 * @param holder
	 * @param type
	 * @param decodeCharset
	 * @param charset
	 * @param convertcharset
	 * @return
	 */
	public static Object getRequestData(MultipartFile values[],
			CallHolder holder, Class type,boolean isNamevariabled) {
		Object value = null;
		if (values != null) {
			if (!isNamevariabled && holder.isCollection()) {
				if (holder.getPosition() == 0)
					holder.setCounts(values.length);
				if (values.length > 0) {
					if (holder.getPosition() < values.length) {

						MultipartFile value_ = values[holder.getPosition()];
						value = value_;

					}

				}
			} else {
				if (!type.isArray() && !List.class.isAssignableFrom(type)
						&& !Set.class.isAssignableFrom(type)) {
					if (values.length > 0) {
						// value = values[0];
						MultipartFile value_ = values[0];
						value = value_;

					}
				} else {
					value = values;
				}
			}

		}
		return value;
	}

	/**
	 * 普通Request参数处理
	 * 
	 * @param values
	 * @param holder
	 * @param type
	 * @param decodeCharset
	 * @param charset
	 * @param convertcharset
	 * @return
	 */
	public static Object getRequestData(String values[], CallHolder holder,
			Class type, String decodeCharset, String charset,
			String convertcharset, EditorInf editor,boolean namevariabled) {
		Object value = null;
		if (values != null) {
			if (!namevariabled && holder.isCollection()) {
				if (holder.getPosition() == 0)
					holder.setCounts(values.length);
				if (values.length > 0) {
					if (holder.getPosition() < values.length) {

						String value_ = values[holder.getPosition()];
						if (decodeCharset != null) {
							try {
								value = URLDecoder
										.decode(value_, decodeCharset);
							} catch (Exception e) {
								logger.error(e);
								value = value_;
							}
						} else if (charset != null && convertcharset != null) {
							try {
								value = new String(value_.getBytes(charset),
										convertcharset);
							} catch (Exception e) {
								logger.error(e);
								value = value_;
							}
						} else {
							value = value_;
						}
					}
					// else
					// value = values[holder.getPosition()];
				}
			} else {
				if (!type.isArray() && !List.class.isAssignableFrom(type)
						&& !Set.class.isAssignableFrom(type)) {
					if (values.length > 0) {
						if (editor == null) {
							value = _getRequestData(values, decodeCharset,
									charset, convertcharset);
						} else if (editor instanceof ArrayEditorInf) {
							if (values.length > 0) {
								value = _getRequestDatas(values, decodeCharset,
										charset, convertcharset);
							} else {
								value = values;
							}
						} else {
							value = _getRequestData(values, decodeCharset,
									charset, convertcharset);
						}
					}
				} else {
					if (values.length > 0) {
						if (editor == null)// 默认返回多条值
						{
							value = _getRequestDatas(values, decodeCharset,
									charset, convertcharset);
						} else if (editor instanceof ArrayEditorInf)// ArrayEditorInf，返回多条值
						{
							value = _getRequestDatas(values, decodeCharset,
									charset, convertcharset);
						} else// EditorInf，只返回第一条值
						{
							value = _getRequestData(values, decodeCharset,
									charset, convertcharset);
						}

					} else {
						value = values;
					}

				}
			}

		}
		return value;
	}

	private static Object _getRequestDatas(String values[],
			String decodeCharset, String charset, String convertcharset) {
		Object value = null;
		if (decodeCharset != null) {
			String[] values_ = new String[values.length];

			for (int i = 0; i < values_.length; i++) {
				try {
					values_[i] = URLDecoder.decode(values[i], decodeCharset);
				} catch (Exception e) {
					logger.error("URLDecoder.decode(" + values[i] + ","
							+ decodeCharset + ") error:", e);
					values_[i] = values[i];
				}
			}
			value = values_;

		} else if (charset != null && convertcharset != null) {
			String[] values_ = new String[values.length];

			for (int i = 0; i < values_.length; i++) {
				try {
					values_[i] = new String(values[i].getBytes(charset),
							convertcharset);
				} catch (Exception e) {
					logger.error("new String(" + values[i] + ".getBytes("
							+ charset + "), " + convertcharset + ") error:", e);
					values_[i] = values[i];
				}
			}
			value = values_;
		} else {
			value = values;
		}
		return value;
	}

	private static Object _getRequestData(String values[],
			String decodeCharset, String charset, String convertcharset) {
		Object value = null;
		String value_ = values[0];
		if (decodeCharset != null) {
			try {
				value = URLDecoder.decode(value_, decodeCharset);
			} catch (Exception e) {
				logger.error("URLDecoder.decode(" + value_ + ","
						+ decodeCharset + ") error:", e);
				value = value_;
			}
		} else if (charset != null && convertcharset != null) {
			try {
				value = new String(value_.getBytes(charset), convertcharset);
			} catch (Exception e) {
				logger.error("new String(value_.getBytes(" + charset + "), "
						+ convertcharset + ") error:", e);
				value = value_;
			}
		} else {
			value = value_;
		}
		return value;
	}

	private static Object buildArrayPositionData(PropertieDescription property,
			HttpServletRequest request, HttpServletResponse response,
			PageContext pageContext, MethodData handlerMethod, ModelMap model,
			HttpMessageConverter[] messageConverters, CallHolder holder,
			Class objectType, Map pathVarDatas) throws Exception {
		String name = property.getName();
		Object value = holder.getData(name);
		boolean required = false;

		EditorInf editor = null;
		boolean useEditor = true;
		SimpleDateFormat dateformat = null;
		if (holder.isArray(name)) {
			useEditor = true;
			editor = holder.getEditor(name);
			required = holder.isRequired(name);
			dateformat = holder.getDateformat(name);
		}

		Field field = property.getField();
		Class type = property.getPropertyType();
		if (field == null) {
			return null;
		}

		// if (HttpSession.class.isAssignableFrom(type)) {
		//
		//
		//
		// }
		// else if (HttpServletRequest.class.isAssignableFrom(type))
		// {
		//
		// } else if (javax.servlet.http.HttpServletResponse.class
		// .isAssignableFrom(type))
		// {
		//
		// } else if (PageContext.class.isAssignableFrom(type)) {
		//
		// } else if (ModelMap.class.isAssignableFrom(type)) {
		//
		// }
		// else if(Map.class.isAssignableFrom(type))
		// {
		//
		// }
		// else if (field.getAnnotations() == null
		// || field.getAnnotations().length == 0 ||
		// !hasParameterAnnotation(field)) {
		//
		// }
		//
		// else if (field.isAnnotationPresent(RequestBody.class)) {
		//
		// }
		// else if (field.isAnnotationPresent(DataBind.class)) {
		//
		// }
		// else
		// {
		// Annotation[] annotations = field.getAnnotations();
		// try
		// {
		// value = evaluateArrayPositionAnnotationsValue( annotations,
		// pathVarDatas, request, name,
		// pageContext, handlerMethod, model, type,value);
		//
		// return value;
		// }
		// catch(Exception e)
		// {
		// model.getErrors().rejectValue(name,
		// "evaluateAnnotationsValue.error",e.getMessage());
		// return ValueObjectUtil.getDefaultValue(type);
		// }
		// }
		if (useEditor) {
			try {
				if (editor == null)
					value = ValueObjectUtil.typeCastWithDateformat(value, type,
							dateformat);
				else
					value = ValueObjectUtil.typeCast(value, editor);
			} catch (Exception e) {
				Exception error = raiseMissingParameterException(name, type,
						value, e);
				model.getErrors().rejectValue(name,
						"ValueObjectUtil.typeCast.error",
						String.valueOf(value), type, error.getMessage());
				return ValueObjectUtil.getDefaultValue(type);
			}

		}
		if (value == null && required) {
			Exception e = raiseMissingParameterException(name, type);
			model.getErrors().rejectValue(name, "value.required.null",
					e.getMessage());
			return ValueObjectUtil.getDefaultValue(type);
		}
		return value;
	}

	public static Object buildPropertyValue(PropertieDescription property,
			HttpServletRequest request, HttpServletResponse response,
			PageContext pageContext, MethodData handlerMethod, ModelMap model,
			HttpMessageConverter[] messageConverters, CallHolder holder,
			Class objectType) throws Exception {
		MethodInfo methodInfo = handlerMethod.getMethodInfo();
		Map pathVarDatas = handlerMethod.getPathVariableDatas();
		String name = property.getName();

		Class type = property.getPropertyType();

		boolean required = false;
		// Object pvalue = completeVO.get(name);
		Object value = null;
		EditorInf editor = null;
		boolean useEditor = true;
		if (holder.isCollection() && holder.getPosition() > 0) {
			// value = holder.getData(name);
			// if(holder.isArray(name))
			// {
			// useEditor = true;
			// editor = holder.getEditor(name);
			// required = holder.isRequired(name);
			// }
			if(!property.isNamevariabled())
			{
				// 解决问题：List<Bean>中如果bean有日期类型并且指定了日期格式，对应多条记录情况下格式化日期报错的问题			
				value = buildArrayPositionData(property, request, response,
						pageContext, handlerMethod, model, messageConverters,
						holder, objectType, pathVarDatas);
			}
			else
			{
				/**
				 * Class ct = ValueObjectUtil.isCollectionType(type)?property.getPropertyGenericType():null;// 获取元素类型
					value = evaluateAnnotationsValue(property, pathVarDatas,
							request, name, pageContext, handlerMethod, model,
							type, holder,ct);
					useEditor = false;
					return value;
				 */
				Class ct = ValueObjectUtil.isCollectionType(type)?property.getPropertyGenericType():null;// 获取元素类型
				value = getNamedParameterValue( property,
						   request,  name,  model,
						 type,  holder, ct) ;
			}
			return value;
		} else {

			Field field = property.getField();
			if (field == null) {
				return null;
			}

			if (HttpSession.class.isAssignableFrom(type)) {
				HttpSession session = request.getSession(false);
				if (session == null) {
					throw new HttpSessionRequiredException(
							"Pre-existing session required for write method '"
									+ property.getName() + "." + name + "'");
				}
				value = session;
				if (holder.needAddData()) {
					holder.addData(name, value);
				}
				useEditor = false;

			} else if (HttpServletRequest.class.isAssignableFrom(type)) {
				value = request;
				useEditor = false;
				if (holder.needAddData()) {
					holder.addData(name, value);
				}
			} else if (javax.servlet.http.HttpServletResponse.class
					.isAssignableFrom(type)) {
				value = response;
				if (holder.needAddData()) {
					holder.addData(name, value);
				}
				useEditor = false;
			} else if (PageContext.class.isAssignableFrom(type)) {
				value = pageContext;
				if (holder.needAddData()) {
					holder.addData(name, value);
				}
				useEditor = false;
			} else if (ModelMap.class.isAssignableFrom(type)) {
				value = model;
				if (holder.needAddData()) {
					holder.addData(name, value);
				}
				useEditor = false;
			} else if (Map.class.isAssignableFrom(type)) {
				MapKey mapKey = null;
				if (property != null)
					mapKey = property.getMapkey();
				if (mapKey == null) {
					value = buildParameterMaps(request);
				}
				else 
				{
					if(!mapKey.value().equals(""))
					{
						Map command = new HashMap();
	
						Class[] ct = property.getPropertyGenericTypes();// 获取元素类型
						if (ct == null) {
							model.getErrors().rejectValue(
									name,
									"evaluateAnnotationsValue.error",
									"没有获取到集合对象类型,请检查属性" + property.getName()
											+ "或者属性set方法是否指定了集合泛型.");
							return ValueObjectUtil.getDefaultValue(type);
						}
						// MapKey mapKey = field.getAnnotation(MapKey.class);
						bind(request, response, pageContext, handlerMethod, model,
								command, ct[0], ct[1], mapKey.value(), null,
								messageConverters);
						value = command;
						if (holder.needAddData()) {
							holder.addData(name, value);
						}
					}
					else
					{
						value = buildParameterMaps(request,mapKey.pattern());
					}
				}
				useEditor = false;
			} else if (!hasParameterAnnotation(property)) {
				if (List.class.isAssignableFrom(type)) {// 如果是列表数据集
					List command = new ArrayList();
					Class ct = property.getPropertyGenericType();// 获取元素类型
//					if (ct == null) {
//						model.getErrors().rejectValue(
//								name,
//								"evaluateAnnotationsValue.error",
//								"没有获取到集合对象类型,请检查属性是否指定了集合泛型："
//										+ property.getName());
//						return ValueObjectUtil.getDefaultValue(type);
//					}
					bind(request, response, pageContext, handlerMethod, model,
							command, ct, null, messageConverters,name);
					value = command;
					if (holder.needAddData()) {
						holder.addData(name, value);
					}
					useEditor = false;
				} else if (Set.class.isAssignableFrom(type)) {// 如果是Set数据集
					Set command = new TreeSet();
					Class ct = property.getPropertyGenericType();// 获取元素类型
//					if (ct == null) {
//						model.getErrors().rejectValue(
//								name,
//								"evaluateAnnotationsValue.error",
//								"没有获取到集合对象类型,请检查是否指定了集合泛型："
//										+ property.getName());
//						return ValueObjectUtil.getDefaultValue(type);
//					}
					bind(request, response, pageContext, handlerMethod, model,
							command, ct, null, messageConverters,name);
					value = command;
					if (holder.needAddData()) {
						holder.addData(name, value);
					}
					useEditor = false;
				} else {
					if (!isMultipartFile(type)) {
						String[] values = request.getParameterValues(name);
						value = getRequestData(values, holder, type, null,
								null, null, null,false);
						if (holder.needAddData()) {
							holder.addData(name, values, true, null, false);
						}
					} else {
						if (request instanceof MultipartHttpServletRequest) {
							if (!HandlerUtils
									.isIgnoreFieldNameMultipartFile(type)) {
								MultipartFile[] values = ((MultipartHttpServletRequest) request)
										.getFiles(name);
								value = getRequestData(values, holder, type,false);
								if (holder.needAddData()) {
									holder.addData(name, values, true, null,
											false);
								}
							} else {
								MultipartFile[] values = ((MultipartHttpServletRequest) request)
										.getFirstFieldFiles();
								value = getRequestData(values, holder, type,false);
								if (holder.needAddData()) {
									holder.addData(name, values, true, null,
											false);
								}
							}
						} else {
							logger.warn("EvaluateMultipartFileParamWithNoName for type["
									+ type.getCanonicalName()
									+ "] fail: form is not a multipart form,please check you form config.");
						}
					}
				}
			}

//			else if (field.isAnnotationPresent(RequestBody.class)) {
			else if (property.getRequestBody() != null) {
				value = resolveRequestBody(type, name, request,
						messageConverters,property.getRequestBody());
				if (holder.needAddData()) {
					holder.addData(name, value);
				}
				useEditor = false;
			} else if (field.isAnnotationPresent(DataBind.class)) {
				Object command = newCommandObject(type);
				bind(request, response, pageContext, handlerMethod, model,
						command, null, messageConverters);
				value = command;
				if (holder.needAddData()) {
					holder.addData(name, value);
				}
				useEditor = false;
			} else {
				Annotation[] annotations = property.getAnnotations();
				try {
					Class ct = ValueObjectUtil.isCollectionType(type)?property.getPropertyGenericType():null;// 获取元素类型
					value = evaluateAnnotationsValue(property, pathVarDatas,
							request, name, pageContext, handlerMethod, model,
							type, holder,ct);
					useEditor = false;
					return value;
				} catch (Exception e) {
					model.getErrors().rejectValue(name,
							"evaluateAnnotationsValue.error", e.getMessage());
					return ValueObjectUtil.getDefaultValue(type);
				}
			}

		}

		if (useEditor) {
			try {
				if (editor == null)
				{
					
					value = ValueObjectUtil.typeCast(value, type);
					
				}
				else
					value = ValueObjectUtil.typeCast(value, editor);
			} catch (Exception e) {
				Exception error = raiseMissingParameterException(name, type,
						value, e);
				model.getErrors().rejectValue(name,
						"ValueObjectUtil.typeCast.error",
						String.valueOf(value), type, error.getMessage());
				return ValueObjectUtil.getDefaultValue(type);
			}

		}
		if (value == null && required) {
			Exception e = raiseMissingParameterException(name, type);
			model.getErrors().rejectValue(name, "value.required.null",
					e.getMessage());
			return ValueObjectUtil.getDefaultValue(type);
		}
		return value;

	}

	public static boolean isMultipartFile(Class type) {
		return MultipartFile.class.isAssignableFrom(type)
				|| MultipartFile[].class.isAssignableFrom(type);
	}

	// !IgnoreFieldNameMultipartFile.class.isAssignableFrom(type) &&
	// !IgnoreFieldNameMultipartFile[].class.isAssignableFrom(type)
	public static boolean isIgnoreFieldNameMultipartFile(Class type) {
		return IgnoreFieldNameMultipartFile.class.isAssignableFrom(type)
				|| IgnoreFieldNameMultipartFile[].class.isAssignableFrom(type);
	}

	/**
	 * Bind request parameters onto the given command bean
	 * 
	 * @param request
	 *            request from which parameters will be bound
	 * @param command
	 *            command object, that must be a JavaBean
	 * @throws Exception
	 *             in case of invalid state or arguments
	 */
	public static void bind(HttpServletRequest request,
			HttpServletResponse response, PageContext pageContext,
			MethodData handlerMethod, ModelMap model, Object command,
			Validator[] validators, HttpMessageConverter[] messageConverters)
			throws Exception {
		logger.debug("Binding request parameters onto MultiActionController command");
		ServletRequestDataBinder binder = createBinder(request, command,
				(BindingResult) model.getErrors());

		binder.bind(request, response, pageContext, handlerMethod, model,
				messageConverters);
		if (validators != null) {
			for (int i = 0; i < validators.length; i++) {
				if (validators[i].supports(command.getClass())) {
					ValidationUtils.invokeValidator(validators[i], command,
							binder.getBindingResult());
				}
			}
		}
		binder.closeNoCatch();
	}

	/**
	 * Bind request parameters onto the given command bean
	 * 
	 * @param request
	 *            request from which parameters will be bound
	 * @param command
	 *            command object, that must be a JavaBean
	 * @throws Exception
	 *             in case of invalid state or arguments
	 */
	public static void bind(HttpServletRequest request,
			HttpServletResponse response, PageContext pageContext,
			MethodData handlerMethod, ModelMap model, Collection command,
			Class objectType, Validator[] validators,
			HttpMessageConverter[] messageConverters,String paramname) throws Exception {
		logger.debug("Binding request parameters onto  Controller Parameter Object.");
		ServletRequestDataBinder binder = createBinder(request, command,
				objectType, (BindingResult) model.getErrors(), paramname);

		binder.bind(request, response, pageContext, handlerMethod, model,
				messageConverters);
		if (validators != null) {
			for (int i = 0; i < validators.length; i++) {
				if (validators[i].supports(command.getClass())) {
					ValidationUtils.invokeValidator(validators[i], command,
							binder.getBindingResult());
				}
			}
		}
		binder.closeNoCatch();
	}

	/**
	 * Bind request parameters onto the given command bean
	 * 
	 * @param request
	 *            request from which parameters will be bound
	 * @param command
	 *            command object, that must be a JavaBean
	 * @throws Exception
	 *             in case of invalid state or arguments
	 */
	public static void bind(HttpServletRequest request,
			HttpServletResponse response, PageContext pageContext,
			MethodData handlerMethod, ModelMap model, Map command,
			Class mapkeytype, Class objectType, String mapkeyName,
			Validator[] validators, HttpMessageConverter[] messageConverters)
			throws Exception {
		logger.debug("Binding request parameters onto  Controller Parameter Object.");
		ServletRequestDataBinder binder = createBinder(request, command,
				mapkeytype, objectType, mapkeyName,
				(BindingResult) model.getErrors());

		binder.bind(request, response, pageContext, handlerMethod, model,
				messageConverters);
		if (validators != null) {
			for (int i = 0; i < validators.length; i++) {
				if (validators[i].supports(command.getClass())) {
					ValidationUtils.invokeValidator(validators[i], command,
							binder.getBindingResult());
				}
			}
		}
		binder.closeNoCatch();
	}

	/**
	 * Create a new binder instance for the given command and request.
	 * <p>
	 * Called by <code>bind</code>. Can be overridden to plug in custom
	 * ServletRequestDataBinder subclasses.
	 * <p>
	 * The default implementation creates a standard ServletRequestDataBinder,
	 * and invokes <code>initBinder</code>. Note that <code>initBinder</code>
	 * will not be invoked if you override this method!
	 * 
	 * @param request
	 *            current HTTP request
	 * @param command
	 *            the command to bind onto
	 * @return the new binder instance
	 * @throws Exception
	 *             in case of invalid state or arguments
	 * @see #bind
	 * @see #initBinder
	 */
	public static ServletRequestDataBinder createBinder(
			HttpServletRequest request, Object command,
			BindingResult bindingResult) throws Exception {
		ServletRequestDataBinder binder = new ServletRequestDataBinder(command,
				getCommandName(command));
		binder.setBindingResult(bindingResult);
		// bindingResult.pushNestedPath(command.getClass().getName());
		// initBinder(request, binder);
		return binder;
	}

	public static ServletRequestDataBinder createBinder(
			HttpServletRequest request, Collection command, Class objecttype,
			BindingResult bindingResult,String paramname) throws Exception {
		ServletRequestDataBinder binder = new ServletRequestDataBinder(command,
				getCommandName(command), objecttype, paramname);
		binder.setBindingResult(bindingResult);
		// bindingResult.pushNestedPath(command.getClass().getName());
		// initBinder(request, binder);
		return binder;
	}

	public static ServletRequestDataBinder createBinder(
			HttpServletRequest request, Map command, Class mapkeytype,
			Class objectType, String mapkeyName, BindingResult bindingResult)
			throws Exception {
		// Map command, String commandName,
		// Class mapkeytype,Class objectType,String mapkeyName
		ServletRequestDataBinder binder = new ServletRequestDataBinder(command,
				getCommandName(command), mapkeytype, objectType, mapkeyName);
		binder.setBindingResult(bindingResult);
		// bindingResult.pushNestedPath(command.getClass().getName());
		// initBinder(request, binder);
		return binder;
	}

	/**
	 * Return the command name to use for the given command object.
	 * <p>
	 * Default is "command".
	 * 
	 * @param command
	 *            the command object
	 * @return the command name to use
	 * @see #DEFAULT_COMMAND_NAME
	 */
	public static String getCommandName(Object command) {
		return DEFAULT_COMMAND_NAME;
	}

	/**
	 * Create a new command object of the given class.
	 * <p>
	 * This implementation uses <code>BeanUtils.instantiateClass</code>, so
	 * commands need to have public no-arg constructors. Subclasses can override
	 * this implementation if desired.
	 * 
	 * @throws Exception
	 *             if the command object could not be instantiated
	 * @see BeanUtils#instantiateClass(Class)
	 */
	public static Object newCommandObject(Class clazz) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("Creating new command of class [" + clazz.getName()
					+ "]");
		}

		return BeanUtils.instantiateClass(clazz);
	}

	public static Exception raiseMissingParameterException(String paramName,
			Class paramType) throws Exception {
		return new IllegalStateException("Missing parameter '" + paramName
				+ "' of type [" + paramType.getName() + "]");
	}

	public static Exception raiseMissingParameterException(String paramName,
			Class paramType, Object paramValue, Throwable e) throws Exception {
		return new IllegalStateException("Parameter '" + paramName
				+ "' of type [" + paramType.getName() + "],Error value is ["
				+ paramValue + "],reason is[" + e.getMessage() + "]");
	}

	/**
	 * Checks for presence of the {@link HandlerMapping} annotation on the
	 * handler class and on any of its methods.
	 */
	@SuppressWarnings("unchecked")
	public static String[] determineUrlsForHandler(
			BaseApplicationContext context, String beanName, Map cachedMappings) {

		Pro beaninfos = context.getProBean(beanName);
		if (!beaninfos.isBean())
			return null;
		final Class<?> handlerType = beaninfos.getBeanClass();
		if (handlerType == null)
			return null;
		// ListableBeanFactory bf = (context instanceof
		// ConfigurableApplicationContext ?
		// ((ConfigurableApplicationContext) context).getBeanFactory() :
		// context);
		// GenericBeanFactoryAccessor bfa = new GenericBeanFactoryAccessor(bf);
		boolean iscontroller = beanName != null && beanName.startsWith("/");

		String[] paths = null;
		HandlerMapping mapping = null;
		if (iscontroller)// 路径对应url地址直接解析路径
		{
			paths = beanName.split(",");

		} else// 否则判断组件是否使用了HandlerMapping注解和Controller注解
		{
			// 获取类级url和controller映射关系
			mapping = AnnotationUtils.findAnnotation(handlerType,
					HandlerMapping.class);
			if (mapping != null) {
				// @HandlerMapping found at type level
				if (cachedMappings != null)
					cachedMappings.put(handlerType, mapping);
				paths = mapping.value();
			}
			// else if (AnnotationUtils.findAnnotation(handlerType,
			// Controller.class) == null) {
			// return null;
			// }
		}

		Set<String> urls = new LinkedHashSet<String>();

		if (paths != null && paths.length > 0) {
			final Set<Method> handlerMethods = new LinkedHashSet<Method>();
			Method[] methods = handlerType.getMethods();
			for (Method method : methods) {
				if (HandlerUtils.isHandlerMethod(handlerType, method)) {

					handlerMethods.add(ClassUtils.getMostSpecificMethod(method,
							handlerType));
				}
			}
			// @HandlerMapping specifies paths at type level
			for (String path : paths) {

				// if(mapping.restful())
				{
					addUrlsForRestfulPath(urls, path, handlerMethods);
				}
				// else
				// {
				// addUrlsForPath(urls, path);
				// }
			}

			return StringUtil.toStringArray(urls);
		} else {
			// actual paths specified by @HandlerMapping at method level
			// 对应设置了controller注解的控制器，要求里面的url处理方法必须设置HandleMaping注解并且制定相应的url否则忽略相应的方法
			return determineUrlsForHandlerMethods(handlerType);
		}
		// }
		// else if (AnnotationUtils.findAnnotation(handlerType,
		// Controller.class) != null) {
		// // @HandlerMapping to be introspected at method level
		// return determineUrlsForHandlerMethods(handlerType);
		// }
		// else {
		// return null;
		// }
	}

	/**
	 * Add URLs and/or URL patterns for the given path.
	 * 
	 * @param urls
	 *            the Set of URLs for the current bean
	 * @param path
	 *            the currently introspected path
	 */
	protected static void addUrlsForRestfulPath(Set<String> urls, String path,
			Set<Method> handlermethods) {
		Iterator<Method> methods = handlermethods.iterator();
		boolean added = false;
		while (methods.hasNext()) {
			if (!added)
				added = true;
			urls.addAll(getRestfulUrl(path, methods.next()));
		}
		if (!added)
			urls.add(path);
	}

	/**
	 * Derive URL mappings from the handler's method-level mappings.
	 * 
	 * @param handlerType
	 *            the handler type to introspect
	 * @return the array of mapped URLs
	 */
	protected static String[] determineUrlsForHandlerMethods(
			Class<?> handlerType) {
		final Set<String> urls = new LinkedHashSet<String>();
		ReflectionUtils.doWithMethods(handlerType,
				new ReflectionUtils.MethodCallback() {
					public void doWith(Method method) {
						HandlerMapping mapping = method
								.getAnnotation(HandlerMapping.class);
						if (mapping != null) {
							String[] mappedPaths = mapping.value();
							for (int i = 0; i < mappedPaths.length; i++) {
								addUrlsForPath(urls, mappedPaths[i]);
							}
						}
					}
				});
		if (urls.size() > 0)
			return StringUtil.toStringArray(urls);
		return null;
	}

	/**
	 * Add URLs and/or URL patterns for the given path.
	 * 
	 * @param urls
	 *            the Set of URLs for the current bean
	 * @param path
	 *            the currently introspected path
	 */
	protected static void addUrlsForPath(Set<String> urls, String path) {
		// Iterator<Method> methods = handlermethods.iterator();
		// while(methods.hasNext())
		// {
		// urls.addAll(this.getUrl(path, restful, methods.next()));
		// urls.add(path); 20110511注释掉，没有考虑restful风格地址
		// if (this.useDefaultSuffixPattern && path.indexOf('.') == -1) {
		// urls.add(path + ".*");
		// }
		// }
		urls.add(getRestfulUrl(path));
	}

	protected static String getRestfulUrl(String methodpath) {

//		String mappedPath = methodpath;
//		StringBuffer pathUrl = new StringBuffer();
//
//		String tmp[] = mappedPath.split("/");
//		for (int i = 1; i < tmp.length; i++) {
//			if (tmp[i].startsWith("{"))
//				pathUrl.append("/*");
//			else {
//				pathUrl.append("/").append(tmp[i]);
//			}
//
//		}
//		return pathUrl.toString();
		return MethodInfo.buildPathPattern(methodpath);

	}

	// protected int convert(HttpMethod HttpMethod)
	// {
	//
	// }
	protected static Set<String> getRestfulUrl(String path, Method method) {

//		String url = path;
//		Set<String> urls = new LinkedHashSet<String>();
//		HandlerMapping mapping = method.getAnnotation(HandlerMapping.class);
//		// MethodInfo methodInfo = new MethodInfo(method);
//		if (mapping != null) {
//			String[] mappedPaths = mapping.value();
//			if (mappedPaths != null && mappedPaths.length > 0) {
//				String mappedPath = mappedPaths[0];
//				StringBuffer pathUrl = new StringBuffer();
//				pathUrl.append(url);
//				String tmp[] = mappedPath.split("/");
//				for (int i = 1; i < tmp.length; i++) {
//					if (tmp[i].startsWith("{"))
//						pathUrl.append("/*");
//					else {
//						pathUrl.append("/").append(tmp[i]);
//					}
//
//				}
//				urls.add(pathUrl.toString());
//				pathUrl = null;
//			} else {
//				urls.add(url);
//			}
//		} else {
//			urls.add(url);
//		}
//		return urls;
		
		String url = path;
		Set<String> urls = new LinkedHashSet<String>();
		HandlerMapping mapping = method.getAnnotation(HandlerMapping.class);
		// MethodInfo methodInfo = new MethodInfo(method);
		if (mapping != null) {
			String[] mappedPaths = mapping.value();
			if (mappedPaths != null && mappedPaths.length > 0) {
				String mappedPath = mappedPaths[0];
//				StringBuffer pathUrl = new StringBuffer();
//				pathUrl.append(url);
//				String tmp[] = mappedPath.split("/");
//				for (int i = 1; i < tmp.length; i++) {
//					if (tmp[i].startsWith("{"))
//						pathUrl.append("/*");
//					else {
//						pathUrl.append("/").append(tmp[i]);
//					}
//
//				}
				urls.add(MethodInfo.buildPathPattern(url,mappedPath));
//				pathUrl = null;
			} else {
				urls.add(url);
			}
		} else {
			urls.add(url);
		}
		return urls;

	}

	public static ModelAndView invokeHandlerMethod(HttpServletRequest request,
			HttpServletResponse response, PageContext pageContext,
			HandlerMeta handler, ServletHandlerMethodResolver methodResolver,
			HttpMessageConverter[] messageConverters) throws Exception {

		try {
			// ServletHandlerMethodResolver methodResolver =
			// getMethodResolver(handler.getClass(),methodResolverCache,urlPathHelper,pathMatcher,methodNameResolver);
			MethodData handlerMethod = methodResolver
					.resolveHandlerMethod(request);
			if(assertTokenMethod != null)
			{
//				assertDToken(request, response, handlerMethod);
				assertTokenMethod.invoke(null, request, response, handlerMethod);
			}
			ServletHandlerMethodInvoker methodInvoker = new ServletHandlerMethodInvoker(
					methodResolver, messageConverters);
			ServletWebRequest webRequest = new ServletWebRequest(request,
					response);
			ModelMap implicitModel = new ModelMap();

			Object result = methodInvoker.invokeHandlerMethod(handlerMethod,
					handler, request, response, pageContext, implicitModel);
			ModelAndView mav = methodInvoker.getModelAndView(
					handlerMethod.getMethodInfo(), handler, result,
					implicitModel, webRequest);
			// methodInvoker.updateModelAttributes(
			// handler, (mav != null ? mav.getModel() : null), implicitModel,
			// webRequest);
			return mav;
		} catch (PathURLNotSetException ex) {
			return handleNoSuchRequestHandlingMethod(ex, request, response);
		} catch (NoSuchRequestHandlingMethodException ex) {
			return handleNoSuchRequestHandlingMethod(ex, request, response);
		}

	}

//	private static void assertDToken(ServletRequest request,
//			ServletResponse response, MethodData handlerMethod)
//			throws IOException {
//		if (handlerMethod.getMethodInfo().isRequiredDToken()) {
//			
//			if (!TokenHelper.isEnableToken())
//				return;
//			TokenHelper.doDTokencheck(request, response);
//			// if(!memTokenManager.assertDTokenSetted(request))
//			// {
//			// if(request instanceof HttpServletRequest)
//			// {
//			// memTokenManager.sendRedirect((HttpServletRequest)
//			// request,(HttpServletResponse) response);
//			// }
//			// else
//			// {
//			// throw new DTokenValidateFailedException();
//			// }
//			// }
//		}
//		else if (handlerMethod.getMethodInfo().isRequireTicket())
//		{			
//			TokenHelper.doTicketcheck(request, response);
//		}
//
//	}

	/**
	 * Handle the case where no request handler method was found.
	 * <p>
	 * The default implementation logs a warning and sends an HTTP 404 error.
	 * Alternatively, a fallback view could be chosen, or the
	 * NoSuchRequestHandlingMethodException could be rethrown as-is.
	 * 
	 * @param ex
	 *            the NoSuchRequestHandlingMethodException to be handled
	 * @param request
	 *            current HTTP request
	 * @param response
	 *            current HTTP response
	 * @return a ModelAndView to render, or <code>null</code> if handled
	 *         directly
	 * @throws Exception
	 *             an Exception that should be thrown as result of the servlet
	 *             request
	 */
	public static ModelAndView handleNoSuchRequestHandlingMethod(
			PathURLNotSetException ex, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		pageNotFoundLogger.warn(ex.getMessage());
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
		return null;
	}

	public static ModelAndView handleNoSuchRequestHandlingMethod(
			NoSuchRequestHandlingMethodException ex,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		pageNotFoundLogger.warn(ex.getMessage());
		response.sendError(HttpServletResponse.SC_NOT_FOUND);
		return null;
	}

	/**
	 * Log category to use when no mapped handler is found for a request.
	 * 
	 * @see #pageNotFoundLogger
	 */
	public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.frameworkset.web.servlet.PageNotFound";

	/**
	 * Additional logger to use when no mapped handler is found for a request.
	 * 
	 * @see #PAGE_NOT_FOUND_LOG_CATEGORY
	 */
	protected final static Logger pageNotFoundLogger = Logger
			.getLogger(PAGE_NOT_FOUND_LOG_CATEGORY);

	// protected static final Log pageNotFoundLogger = LogFactory
	// .getLog(PAGE_NOT_FOUND_LOG_CATEGORY);

	public static class ServletHandlerMethodResolver extends
			HandlerMethodResolver {
		private UrlPathHelper urlPathHelper = new UrlPathHelper();
		private MethodNameResolver methodNameResolver = new InternalPathMethodNameResolver();
		private PathMatcher pathMatcher = new AntPathMatcher();

		public ServletHandlerMethodResolver(Class<?> handlerType,
				UrlPathHelper urlPathHelper, PathMatcher pathMatcher,
				MethodNameResolver methodNameResolver) {
			super(handlerType);
			this.urlPathHelper = urlPathHelper;
			this.methodNameResolver = methodNameResolver;
			this.pathMatcher = pathMatcher;
		}

		public ServletHandlerMethodResolver(Class<?> handlerType,
				UrlPathHelper urlPathHelper, PathMatcher pathMatcher,
				MethodNameResolver methodNameResolver, String baseurls[]) {
			super(handlerType, baseurls);
			this.urlPathHelper = urlPathHelper;
			this.methodNameResolver = methodNameResolver;
			this.pathMatcher = pathMatcher;
		}

		public MethodData resolveHandlerMethod(HttpServletRequest request)
				throws ServletException {
			String lookupPath = urlPathHelper.getLookupPathForRequest(request);
			Map<HandlerMappingInfo, MethodInfo> targetHandlerMethods = new LinkedHashMap<HandlerMappingInfo, MethodInfo>();
			Map<HandlerMappingInfo, String> targetPathMatches = new LinkedHashMap<HandlerMappingInfo, String>();

			String resolvedMethodName = methodNameResolver
					.getHandlerMethodName(request);
			Set<MethodInfo> handlerMethods = getHandlerMethods();
			for (MethodInfo handlerMethod : handlerMethods) {

				HandlerMapping mapping = handlerMethod.getMethodMapping();
				if (mapping == null) {
					if (resolvedMethodName.equals(handlerMethod.getMethod()
							.getName())) {
						// String path_ = (String) request
						// .getAttribute(org.frameworkset.web.servlet.HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
						String path_ = RequestContext
								.getHandlerMappingPath(request);
						Map pathdatas = AnnotationUtils.resolvePathDatas(
								handlerMethod, path_);

						MethodData methodData = new MethodData(handlerMethod,
								pathdatas);

						return methodData;
					}
					continue;
				}
				HandlerMappingInfo mappingInfo = new HandlerMappingInfo();
				mappingInfo.paths = handlerMethod.getPathPattern();
				if (!hasTypeLevelMapping()
						|| !Arrays.equals(mapping.method(),
								getTypeLevelMapping().method())) {
					mappingInfo.methods = mapping.method();
				}
				if (!hasTypeLevelMapping()
						|| !Arrays.equals(mapping.params(),
								getTypeLevelMapping().params())) {
					mappingInfo.params = mapping.params();
				}
				boolean match = false;
				if (handlerMethod.getPathPattern() != null
						&& handlerMethod.getPathPattern().length > 0) {
					for (String mappedPath : handlerMethod.getPathPattern()) {
						if (isPathMatch(mappedPath, lookupPath)) {
							if (checkParameters(mappingInfo, request)) {
								match = true;
								targetPathMatches.put(mappingInfo, mappedPath);
							} else {
								break;
							}
						}
					}
				} else {
					// No paths specified: parameter match sufficient.
					match = checkParameters(mappingInfo, request);
					// if (match && mappingInfo.methods.length == 0 &&
					// mappingInfo.params.length == 0 &&
					// resolvedMethodName != null &&
					// !resolvedMethodName.equals(handlerMethod.getMethod().getName()))
					// {
					// match = false;
					// }
				}
				if (match) {
					MethodInfo oldMappedMethod = targetHandlerMethods.put(
							mappingInfo, handlerMethod);
					if (oldMappedMethod != null)
						throw new IllegalStateException(
								"Ambiguous handler methods mapped for HTTP path '"
										+ lookupPath
										+ "': {"
										+ oldMappedMethod
										+ ", "
										+ handlerMethod
										+ "}. If you intend to handle the same path in multiple methods, then factor "
										+ "them out into a dedicated handler class with that path mapped at the type level!");
					// if (oldMappedMethod != null && oldMappedMethod !=
					// handlerMethod.getMethod()) {
					// if (methodNameResolver != null &&
					// mappingInfo.paths.length == 0) {
					// if
					// (!oldMappedMethod.getName().equals(handlerMethod.getMethod().getName()))
					// {
					// if (resolvedMethodName == null) {
					// resolvedMethodName =
					// methodNameResolver.getHandlerMethodName(request);
					// }
					// if
					// (!resolvedMethodName.equals(oldMappedMethod.getName())) {
					// oldMappedMethod = null;
					// }
					// if
					// (!resolvedMethodName.equals(handlerMethod.getMethod().getName()))
					// {
					// if (oldMappedMethod != null) {
					// targetHandlerMethods.put(mappingInfo, oldMappedMethod);
					// oldMappedMethod = null;
					// }
					// else {
					// targetHandlerMethods.remove(mappingInfo);
					// }
					// }
					// }
					// }
					// if (oldMappedMethod != null) {
					// throw new
					// IllegalStateException("Ambiguous handler methods mapped for HTTP path '"
					// +
					// lookupPath + "': {" + oldMappedMethod + ", " +
					// handlerMethod +
					// "}. If you intend to handle the same path in multiple methods, then factor "
					// +
					// "them out into a dedicated handler class with that path mapped at the type level!");
					// }
					// }
				}
			}
			if (targetHandlerMethods.size() == 1) {
				MethodInfo handlerMethod = targetHandlerMethods.values()
						.iterator().next();
				// String path_ = (String) request
				// .getAttribute(org.frameworkset.web.servlet.HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
				String path_ = (String) request
						.getAttribute(org.frameworkset.web.servlet.HandlerMapping.HANDLER_MAPPING_PATH_ATTRIBUTE);
				Map pathdatas = AnnotationUtils.resolvePathDatas(handlerMethod,
						path_);

				MethodData methodData = new MethodData(handlerMethod, pathdatas);

				return methodData;
				// return targetHandlerMethods.values().iterator().next();
			} else if (!targetHandlerMethods.isEmpty()) {
				HandlerMappingInfo bestMappingMatch = null;
				String bestPathMatch = null;
				for (HandlerMappingInfo mapping : targetHandlerMethods.keySet()) {
					String mappedPath = targetPathMatches.get(mapping);
					if (bestMappingMatch == null) {
						bestMappingMatch = mapping;
						bestPathMatch = mappedPath;
					} else {
						if (isBetterPathMatch(mappedPath, bestPathMatch,
								lookupPath)
								|| (!isBetterPathMatch(bestPathMatch,
										mappedPath, lookupPath) && (isBetterMethodMatch(
										mapping, bestMappingMatch) || (!isBetterMethodMatch(
										bestMappingMatch, mapping) && isBetterParamMatch(
										mapping, bestMappingMatch))))) {
							bestMappingMatch = mapping;
							bestPathMatch = mappedPath;
						}
					}
				}
				MethodInfo handlerMethod = targetHandlerMethods
						.get(bestMappingMatch);
				// String path_ = (String) request
				// .getAttribute(org.frameworkset.web.servlet.HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
				String path_ = (String) request
						.getAttribute(org.frameworkset.web.servlet.HandlerMapping.HANDLER_MAPPING_PATH_ATTRIBUTE);
				Map pathdatas = AnnotationUtils.resolvePathDatas(handlerMethod,
						path_);

				MethodData methodData = new MethodData(handlerMethod, pathdatas);

				return methodData;
				// return targetHandlerMethods.get(bestMappingMatch);
			} else {
				throw new NoSuchRequestHandlingMethodException(lookupPath,
						request.getMethod(), request.getParameterMap());
			}
		}

		private boolean isPathMatch(String mappedPath, String lookupPath) {
			if (mappedPath.equals(lookupPath)
					|| pathMatcher.match(mappedPath, lookupPath)) {
				return true;
			}
			boolean hasSuffix = (mappedPath.indexOf('.') != -1);
			if (!hasSuffix && pathMatcher.match(mappedPath + ".*", lookupPath)) {
				return true;
			}
			return (!mappedPath.startsWith("/") && (lookupPath
					.endsWith(mappedPath)
					|| pathMatcher.match("/**/" + mappedPath, lookupPath) || (!hasSuffix && pathMatcher
					.match("/**/" + mappedPath + ".*", lookupPath))));
		}

		private boolean checkParameters(HandlerMappingInfo mapping,
				HttpServletRequest request) {
			return ServletAnnotationMappingUtils.checkRequestMethod(
					mapping.methods, request)
					&& ServletAnnotationMappingUtils.checkParameters(
							mapping.params, request);
		}

		private boolean isBetterPathMatch(String mappedPath,
				String mappedPathToCompare, String lookupPath) {
			return (mappedPath != null && (mappedPathToCompare == null
					|| mappedPathToCompare.length() < mappedPath.length() || (mappedPath
					.equals(lookupPath) && !mappedPathToCompare
					.equals(lookupPath))));
		}

		private boolean isBetterMethodMatch(HandlerMappingInfo mapping,
				HandlerMappingInfo mappingToCompare) {
			return (mappingToCompare.methods.length == 0 && mapping.methods.length > 0);
		}

		private boolean isBetterParamMatch(HandlerMappingInfo mapping,
				HandlerMappingInfo mappingToCompare) {
			return (mappingToCompare.params.length < mapping.params.length);
		}
	}

	public static class ServletHandlerMethodInvoker extends
			HandlerMethodInvoker {

		private boolean responseArgumentUsed = false;

		public ServletHandlerMethodInvoker(HandlerMethodResolver resolver,
				HttpMessageConverter[] messageConverters) {
			super(messageConverters, resolver
			// null, null, null, null
			);
		}

		@Override
		protected void raiseMissingParameterException(String paramName,
				Class paramType) throws Exception {
			throw new MissingServletRequestParameterException(paramName,
					paramType.getName());
		}

		@Override
		protected void raiseSessionRequiredException(String message)
				throws Exception {
			throw new HttpSessionRequiredException(message);
		}

		// @Override
		// protected void doBind(NativeWebRequest webRequest, WebDataBinder
		// binder, boolean failOnErrors)
		// throws Exception {
		//
		// ServletRequestDataBinder servletBinder = (ServletRequestDataBinder)
		// binder;
		// servletBinder.bind((ServletRequest) webRequest.getNativeRequest());
		// if (failOnErrors) {
		// servletBinder.closeNoCatch();
		// }
		// }

		@SuppressWarnings("unchecked")
		public ModelAndView getModelAndView(MethodInfo handlerMethod,
				HandlerMeta handlerMeta, Object returnValue,
				ModelMap implicitModel, ServletWebRequest webRequest)
				throws Exception {

			if (handlerMethod.isResponseBody()) {
				handleResponseBody(handlerMethod.getResponsebodyAnno(),returnValue, webRequest );
				return null;
			} else if (returnValue instanceof ModelAndView) {
				ModelAndView mav = (ModelAndView) returnValue;
				if (mav.getView() != null
						&& mav.getView() instanceof AbstractUrlBasedView) {
					// 处理path:类型路径
					AbstractUrlBasedView view = (AbstractUrlBasedView) mav
							.getView();
					String url = view.getUrl();
					if (UrlBasedViewResolver.isPathVariable(url)) {
						url = handlerMeta.getUrlPath(url, handlerMethod
								.getMethod().getName(), handlerMeta
								.getHandler(), webRequest.getRequest());
						view.setUrl(url);
					}
				} else if (UrlBasedViewResolver.isPathVariable(mav
						.getViewName())) {
					mav.setViewName(handlerMeta.getUrlPath(mav.getViewName(),
							handlerMethod.getMethod().getName(),
							handlerMeta.getHandler(), webRequest.getRequest()));
				}
				mav.getModelMap().mergeAttributes(implicitModel);
				return mav;
			} else if (returnValue instanceof ModelMap) {
				return new ModelAndView().addAllObjects(implicitModel)
						.addAllObjects(((ModelMap) returnValue));
			} else if (returnValue instanceof Map) {
				return new ModelAndView().addAllObjects(implicitModel)
						.addAllObjects((Map) returnValue);
			} else if (returnValue instanceof View) {

				if (returnValue instanceof AbstractUrlBasedView) {
					// 处理path:类型路径
					AbstractUrlBasedView view = (AbstractUrlBasedView) returnValue;
					String url = view.getUrl();
					if (UrlBasedViewResolver.isPathVariable(url)) {
						url = handlerMeta.getUrlPath(url, handlerMethod
								.getMethod().getName(), handlerMeta
								.getHandler(), webRequest.getRequest());
						view.setUrl(url);
					}

					return new ModelAndView(view).addAllObjects(implicitModel);

				} else {
					return new ModelAndView((View) returnValue)
							.addAllObjects(implicitModel);
				}
			} else if (returnValue instanceof String) {

				String viewName = (String) returnValue;
				if (UrlBasedViewResolver.isPathVariable(viewName)) {
					return new ModelAndView(handlerMeta.getUrlPath(viewName,
							handlerMethod.getMethod().getName(),
							handlerMeta.getHandler(), webRequest.getRequest()))
							.addAllObjects(implicitModel);
				} else {
					return new ModelAndView((String) returnValue)
							.addAllObjects(implicitModel);
				}
			}

			else if (returnValue instanceof HttpEntity) {
				handleHttpEntityResponse(handlerMethod.getResponsebodyAnno(),(HttpEntity<?>) returnValue,
						webRequest);
				return null;
			} else if (returnValue == null) {
				return null;
				// Either returned null or was 'void' return.
				// if (this.responseArgumentUsed || webRequest.isNotModified())
				// {
				// return null;
				// }
				// else {
				// // Assuming view name translation...
				// return new ModelAndView().addAllObjects(implicitModel);
				// }
			} else if (!BeanUtils.isSimpleProperty(returnValue.getClass())) {
				// Assume a single model attribute...
				ModelAttribute attr = AnnotationUtils.findAnnotation(
						handlerMethod.getMethod(), ModelAttribute.class);
				String attrName = (attr != null ? attr.name() : "");
				ModelAndView mav = new ModelAndView()
						.addAllObjects(implicitModel);
				if ("".equals(attrName)) {
					Class resolvedType = GenericTypeResolver.resolveReturnType(
							handlerMethod.getMethod(), handlerMeta.getHandler()
									.getClass());
					attrName = Conventions.getVariableNameForReturnType(
							handlerMethod.getMethod(), resolvedType,
							returnValue);
				}
				return mav.addObject(attrName, returnValue);
			} else {
				throw new IllegalArgumentException(
						"Invalid handler method return value: " + returnValue);
			}
		}

		private void handleResponseBody(ResponseBodyWraper responsebodyAnno,Object returnValue,
				ServletWebRequest webRequest )
				throws Exception {
			if (returnValue == null) {
				return;
			}
			HttpInputMessage inputMessage = HandlerUtils
					.createHttpInputMessage(webRequest.getRequest());
			HttpOutputMessage outputMessage = HandlerUtils
					.createHttpOutputMessage(webRequest.getResponse());
			writeWithMessageConverters(  responsebodyAnno,returnValue, inputMessage,
					outputMessage  );
		}

		private void handleHttpEntityResponse(ResponseBodyWraper responsebodyAnno,HttpEntity<?> responseEntity,
				ServletWebRequest webRequest) throws Exception {
			if (responseEntity == null) {
				return;
			}
			HttpInputMessage inputMessage = HandlerUtils
					.createHttpInputMessage(webRequest.getRequest());
			HttpOutputMessage outputMessage = HandlerUtils
					.createHttpOutputMessage(webRequest.getResponse());
			if (responseEntity instanceof ResponseEntity
					&& outputMessage instanceof ServerHttpResponse) {
				((ServerHttpResponse) outputMessage)
						.setStatusCode(((ResponseEntity) responseEntity)
								.getStatusCode());
			}
			HttpHeaders entityHeaders = responseEntity.getHeaders();
			if (!entityHeaders.isEmpty()) {
				outputMessage.getHeaders().putAll(entityHeaders);
			}
			Object body = responseEntity.getBody();
			if (body != null) {
				writeWithMessageConverters(  responsebodyAnno,body, inputMessage, outputMessage 
						 );
			} else {
				// flush headers
				outputMessage.getBody();
			}
		}

		@SuppressWarnings("unchecked")
		private void writeWithMessageConverters(ResponseBodyWraper responsebodyAnno,Object returnValue,
				HttpInputMessage inputMessage, HttpOutputMessage outputMessage
				) throws IOException,
				HttpMediaTypeNotAcceptableException {
			HttpMessageConverter defaultMessageConverter = null;
			String datatype = responsebodyAnno.datatype();
			MediaType responseMediaType = responsebodyAnno.getResponseMediaType();
			if(responsebodyAnno.isEval())
			{
				List<MediaType> acceptedMediaTypes = inputMessage.getHeaders()
						.getAccept();
				for(int i = 0; acceptedMediaTypes != null && i < acceptedMediaTypes.size();i ++)
				{
					MediaType mediaType = acceptedMediaTypes.get(i);
					if(mediaType.isJson())
					{
						responseMediaType = HttpMessageConverter.jsonmediatypes[0];
						datatype = ValueConstants.datatype_json;
						break;
					}
					else if(mediaType.isJsonp())
					{
						responseMediaType = HttpMessageConverter.jsonmediatypes[0];
						datatype = ValueConstants.datatype_jsonp;
						break;
					}
					
				}
			}
			
			Class<?> returnValueType = returnValue.getClass();
			List<MediaType> allSupportedMediaTypes = new ArrayList<MediaType>();
			if (getMessageConverters() != null) {
				
				
					for (HttpMessageConverter messageConverter : getMessageConverters()) {
						if(defaultMessageConverter == null && messageConverter.isdefault())
							defaultMessageConverter = messageConverter;
						if (messageConverter.canWrite(datatype))
						
//						if (messageConverter.canWrite(returnValueType,
//								acceptedMediaType)) 
						{
							messageConverter.write(returnValue,
									responseMediaType, outputMessage,
									inputMessage );
							// if (logger.isDebugEnabled()) {
							// MediaType contentType = outputMessage
							// .getHeaders().getContentType();
							// if (contentType == null) {
							// contentType = acceptedMediaType;
							// }
							// logger
							// .debug("Written [" + returnValue
							// + "] as \"" + contentType
							// + "\" using ["
							// + messageConverter + "]");
							// }
							this.responseArgumentUsed = true;
							return;
						}
						
					}
				
				if(defaultMessageConverter != null)
				{
					defaultMessageConverter.write(returnValue,
							defaultMessageConverter.getDefaultAcceptedMediaType(), outputMessage,
							inputMessage );
					this.responseArgumentUsed = true;
					return;
				}
			}
			
			
			
			
//			List<MediaType> acceptedMediaTypes = inputMessage.getHeaders()
//					.getAccept();
//			boolean usecustomMediaTypeByMethod = false;
//			if (acceptedMediaTypes.isEmpty()) {
//				if (responseMediaType == null)
//					acceptedMediaTypes = Collections
//							.singletonList(MediaType.ALL);
//				else
//					acceptedMediaTypes = Collections
//							.singletonList(responseMediaType);
//
//			} else {
//				if (responseMediaType != null) {
//					acceptedMediaTypes.clear();
//					acceptedMediaTypes.add(responseMediaType);
//					usecustomMediaTypeByMethod = true;
//				} else {
//					MediaType.sortByQualityValue(acceptedMediaTypes);
//				}
//			}
//
//			Class<?> returnValueType = returnValue.getClass();
//			List<MediaType> allSupportedMediaTypes = new ArrayList<MediaType>();
//			if (getMessageConverters() != null) {
//				
//				for (MediaType acceptedMediaType : acceptedMediaTypes) {
//					for (HttpMessageConverter messageConverter : getMessageConverters()) {
//						if(defaultMessageConverter == null && messageConverter.isdefault())
//							defaultMessageConverter = messageConverter;
//						if (messageConverter.canWrite(returnValueType,
//								acceptedMediaType)) {
//							messageConverter.write(returnValue,
//									acceptedMediaType, outputMessage,
//									inputMessage, usecustomMediaTypeByMethod);
//							// if (logger.isDebugEnabled()) {
//							// MediaType contentType = outputMessage
//							// .getHeaders().getContentType();
//							// if (contentType == null) {
//							// contentType = acceptedMediaType;
//							// }
//							// logger
//							// .debug("Written [" + returnValue
//							// + "] as \"" + contentType
//							// + "\" using ["
//							// + messageConverter + "]");
//							// }
//							this.responseArgumentUsed = true;
//							return;
//						}
//						
//					}
//				}
//				if(defaultMessageConverter != null)
//				{
//					defaultMessageConverter.write(returnValue,
//							defaultMessageConverter.getDefaultAcceptedMediaType(), outputMessage,
//							inputMessage, usecustomMediaTypeByMethod);
//					this.responseArgumentUsed = true;
//					return;
//				}
//				for (HttpMessageConverter messageConverter : messageConverters) {
//					if (messageConverter.getSupportedMediaTypes() != null
//							&& messageConverter.getSupportedMediaTypes().size() > 0) {
//						allSupportedMediaTypes.addAll(messageConverter
//								.getSupportedMediaTypes());
//					}
//				}
//			}
			throw new HttpMediaTypeNotAcceptableException(
					allSupportedMediaTypes);
			
			
		}

		// protected void doBind(NativeWebRequest webRequest,
		// WebDataBinder binder, boolean failOnErrors) throws Exception {
		//
		// WebRequestDataBinder requestBinder = (WebRequestDataBinder) binder;
		// requestBinder.bind(webRequest);
		// if (failOnErrors) {
		// requestBinder.closeNoCatch();
		// }
		// }

	}

	private static class HandlerMappingInfo {

		public String[] paths = new String[0];

		public HttpMethod[] methods = new HttpMethod[0];

		public String[] params = new String[0];

		public boolean equals(Object obj) {
			HandlerMappingInfo other = (HandlerMappingInfo) obj;
			return (Arrays.equals(this.paths, other.paths)
					&& Arrays.equals(this.methods, other.methods) && Arrays
						.equals(this.params, other.params));
		}

		public int hashCode() {
			return (Arrays.hashCode(this.paths) * 29
					+ Arrays.hashCode(this.methods) * 31 + Arrays
						.hashCode(this.params));
		}
	}

}
