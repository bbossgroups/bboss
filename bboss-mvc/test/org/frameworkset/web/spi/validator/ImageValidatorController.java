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
package org.frameworkset.web.spi.validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.frameworkset.util.annotations.Attribute;
import org.frameworkset.util.annotations.AttributeScope;
import org.frameworkset.util.annotations.HandlerMapping;
import org.frameworkset.util.annotations.PathVariable;
import org.frameworkset.util.annotations.HttpMethod;
import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.web.servlet.ModelAndView;

/**
 * <p>Title: ImageValidatorController.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-11-13
 * @author biaoping.yin
 * @version 1.0
 */
@HandlerMapping("/rest/imagevalidator")
public class ImageValidatorController  {
	//http://localhost:8080/bboss-mvc/rest/imagevalidator/abcefg1234/6
	
	@HandlerMapping(value="/{codelist}/{codenum}",method={HttpMethod.GET,HttpMethod.POST})
	public void generateImageCode(
								  @PathVariable("codelist") String codelist,
								  @PathVariable("codenum") int codenum,
								  HttpServletRequest request,HttpServletResponse response)
	{
		String codekey  = "imagecodekey";
		HttpSession session = request.getSession(true);
		RandImgCreater rc = new RandImgCreater(response,codenum,codelist);
		String rand = rc.createRandImage();
		session.setAttribute(codekey,rand);

	}
//	http://localhost:8080/bboss-mvc/rest/imagevalidator
	@HandlerMapping(method={HttpMethod.POST,HttpMethod.GET})
	public ModelAndView checkImageCode(@RequestParam(name="imagecode") String imagecode,
			@Attribute(name="imagecodekey",scope=AttributeScope.SESSION_ATTRIBUTE) String oldcode,
			HttpServletRequest request)
	{
		
	       String message = "";   
	        if(imagecode == null || imagecode.equals(""))   
	            message = "image.validate.emptycode";   
	        else if(oldcode == null)   
	            message = "image.validate.submitted";   
	        else if(!imagecode.equals(oldcode))   
	        {   
	            message = "image.validate.notmatched";   
	        }   
	        else  
	        {   
	            message = "image.validate.ok";   
	        }      
	        if(oldcode != null && request.getSession(false) != null)
	        	request.getSession(false).removeAttribute("imagecodekey");
		ModelAndView view = new ModelAndView("/validate/imagevalidate","message",message);
		return view;

	}
	
	
}
