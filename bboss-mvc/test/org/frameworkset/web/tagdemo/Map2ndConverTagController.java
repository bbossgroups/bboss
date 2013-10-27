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
package org.frameworkset.web.tagdemo;

import java.util.HashMap;
import java.util.Map;

import org.frameworkset.web.servlet.ModelMap;

import test.TestBean;


/**
 * <p>Map2ndConverTagController.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-10-12
 * @author biaoping.yin
 * @version 1.0
 */
public class Map2ndConverTagController
{
	
	public String testmap(ModelMap model)
	{
		TestBean bean = null;
		Map<String,TestBean> mapbeans = new HashMap<String,TestBean>();
		bean = new TestBean();
		bean.setId("uuid");
		bean.setName("多多");
		mapbeans.put(bean.getId(),bean);
		
		bean = new TestBean();
		bean.setId("uuid1");
		bean.setName("多多1");
		mapbeans.put(bean.getId(),bean);
		bean = new TestBean();
		bean.setId("uuid2");
		bean.setName("多多2");
		mapbeans.put(bean.getId(),bean);
		model.addAttribute("mapbeans",mapbeans);
		
		Map<String,String> mapstrings = new HashMap<String,String>();
		mapstrings.put("id1","多多1");
		mapstrings.put("id2","多多2");
		mapstrings.put("id3","多多3");
		mapstrings.put("id4","多多4");
		model.addAttribute("mapstrings",mapstrings);
		
		org.frameworkset.web.demo.User user = new org.frameworkset.web.demo.User();
		user.setUserId(1);
		user.setUserName("duoduo");
		java.util.Map beanmapdata = new HashMap();
		beanmapdata.put("duoduo","多多");
		model.addAttribute("beanmapdata",beanmapdata);
		model.addAttribute("beandata",user);
		return "path:testmap";
		
	}
}
