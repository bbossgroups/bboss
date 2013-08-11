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
package org.frameworkset.web.bind;

import java.util.Collection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.frameworkset.http.converter.HttpMessageConverter;
import org.frameworkset.util.annotations.MethodData;
import org.frameworkset.web.servlet.ModelMap;

/**
 * <p>Title: ServletRequestDataBinder.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-15
 * @author biaoping.yin
 * @version 1.0
 */
public class ServletRequestDataBinder  extends WebDataBinder {

	/**
	 * Create a new ServletRequestDataBinder instance, with default object name.
	 * @param target the target object to bind onto (or <code>null</code>
	 * if the binder is just used to convert a plain parameter value)
	 * @see #DEFAULT_OBJECT_NAME
	 */
	public ServletRequestDataBinder(Object target) {
		super(target);
	}

	/**
	 * Create a new ServletRequestDataBinder instance.
	 * @param target the target object to bind onto (or <code>null</code>
	 * if the binder is just used to convert a plain parameter value)
	 * @param objectName the name of the target object
	 */
	public ServletRequestDataBinder(Object target, String objectName) {
		super(target, objectName);
	}


	public ServletRequestDataBinder(Collection command, String commandName,
			Class objecttype,String paramname) {
		super(command, commandName,objecttype, paramname);
	}
	//Class mapkeytype,Class objectType,String mapkeyName,
	public ServletRequestDataBinder(Map command, String commandName,
			Class mapkeytype,Class objectType,String mapkeyName) {
		//Map target, String mapKeyName,Class mapKeyType,String objectName,Class objectType
		super(command,mapkeyName , mapkeytype,commandName, objectType );
	}

	/**
	 * Bind the parameters of the given request to this binder's target,
	 * also binding multipart files in case of a multipart request.
	 * <p>This call can create field errors, representing basic binding
	 * errors like a required field (code "required"), or type mismatch
	 * between value and bean property (code "typeMismatch").
	 * <p>Multipart files are bound via their parameter name, just like normal
	 * HTTP parameters: i.e. "uploadedFile" to an "uploadedFile" bean property,
	 * invoking a "setUploadedFile" setter method.
	 * <p>The type of the target property for a multipart file can be MultipartFile,
	 * byte[], or String. The latter two receive the contents of the uploaded file;
	 * all metadata like original file name, content type, etc are lost in those cases.
	 * @param request request with parameters to bind (can be multipart)
	 * @throws Exception 
	 * 
	 * @see #bindMultipartFiles
	 * 
	 */
	public void bind(HttpServletRequest request,HttpServletResponse response,PageContext pageContext,
			MethodData handlerMethod,ModelMap model,HttpMessageConverter[] messageConverters) throws Exception {
//		Map mpvs = new RequestMap(request);
//		if (request instanceof MultipartHttpServletRequest) {
//			MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
//			bindMultipartFiles(multipartRequest.getFileMap(), mpvs);
//		}
		doBind( request,response,pageContext,
				handlerMethod,model, messageConverters);
	}

	/**
	 * Treats errors as fatal.
	 * <p>Use this method only if it's an error if the input isn't valid.
	 * This might be appropriate if all input is from dropdowns, for example.
	 * @throws ServletRequestBindingException subclass of ServletException on any binding problem
	 */
	public void closeNoCatch() throws ServletRequestBindingException {
		//Fixme
		if (getBindingResult() != null && getBindingResult().hasErrors()) {
//			throw new ServletRequestBindingException(
//					"Errors binding onto object '" + getBindingResult().getObjectName() + "'",
//					new BindException(getBindingResult()));
		}
	}
	
	 

}
