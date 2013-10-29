package org.frameworkset.spi.mvc;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.frameworkset.web.servlet.ModelAndView;
import org.frameworkset.web.servlet.mvc.AbstractController;

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

/**
 * <p>Title: DemoController.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-6
 * @author biaoping.yin
 * @version 1.0
 */
public class DemoController extends AbstractController {

	private String viewName = null;
	@Override
	//http://localhost:8080/bboss-mvc/demo.htm
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response,PageContext pageContext) throws Exception {
		List values = new ArrayList();
		for(int i = 0; i < 100; i ++)
		{
			TestObject object = new TestObject();
			object.setId("id" + i);
			object.setName("多多");
			values.add(object);
		}
		return new ModelAndView(this.getViewName(),"testmodel",values);
	}
	public String getViewName() {
		return viewName;
	}
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

}
