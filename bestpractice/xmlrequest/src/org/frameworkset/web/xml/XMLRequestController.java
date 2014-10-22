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
package org.frameworkset.web.xml;

import org.frameworkset.util.annotations.RequestBody;
import org.frameworkset.util.annotations.ResponseBody;


/**
 * <p>XMLRequestController.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-6-22
 * @author biaoping.yin
 * @version 1.0
 */
public class XMLRequestController
{
	public static class XMLBean 
	{
		private String id;
		private String data;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getData() {
			return data;
		}
		public void setData(String data) {
			this.data = data;
		}
	}
	public @ResponseBody(datatype="xml") XMLBean echoxml(@RequestBody(datatype="xml") XMLBean xml)
	{
//		
//		XMLBean XMLBean = new XMLBean();
//		XMLBean.setData(xml);
//		XMLBean.setId(StringUtil.getUUID());
		xml.setData("Response:"+xml.getData());
		return xml;
	}
	
	public @ResponseBody(datatype="json") XMLBean echojson(@RequestBody(datatype="json") XMLBean xml)
	{
//		
//		XMLBean XMLBean = new XMLBean();
//		XMLBean.setData(xml);
//		XMLBean.setId(StringUtil.getUUID());
		xml.setData("Response:"+xml.getData());
		return xml;
	}
	public String index()
	{
		return "path:index";
	}
}
