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
package org.frameworkset.web.servlet;

import javax.servlet.ServletException;

import org.frameworkset.util.Assert;

/**
 * <p>Title: ModelAndViewDefiningException.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008-2010</p>
 * @Date 2010-10-2
 * @author biaoping.yin
 * @version 1.0
 */
public class ModelAndViewDefiningException  extends ServletException {

	private ModelAndView modelAndView;


	/**
	 * Create new ModelAndViewDefiningException with the given ModelAndView,
	 * typically representing a specific error page.
	 * @param modelAndView ModelAndView with view to forward to and model to expose
	 */
	public ModelAndViewDefiningException(ModelAndView modelAndView) {
		Assert.notNull(modelAndView, "ModelAndView must not be null in ModelAndViewDefiningException");
		this.modelAndView = modelAndView;
	}

	/**
	 * Return the ModelAndView that this exception contains for forwarding to.
	 */
	public ModelAndView getModelAndView() {
		return modelAndView;
	}

}
