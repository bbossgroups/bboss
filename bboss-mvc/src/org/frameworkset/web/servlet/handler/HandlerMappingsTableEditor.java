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

import java.util.ArrayList;
import java.util.List;

import org.frameworkset.spi.assemble.ProList;
import org.frameworkset.spi.editor.AbstractEditorInf;
import org.frameworkset.web.servlet.HandlerMapping;

/**
 * <p>Title: HandlerMappingsTableEditor.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-31
 * @author biaoping.yin
 * @version 1.0
 */
public class HandlerMappingsTableEditor extends AbstractEditorInf<List<HandlerMapping>> {

	public List<HandlerMapping> getValueFromObject(Object fromValue) {
		
		ProList proList = (ProList)fromValue;
		if(proList == null || proList.size() == 0)
			return null;
		List<HandlerMapping> handlerMappings = new ArrayList<HandlerMapping>();
		for(int i = 0; proList != null && i < proList.size(); i++)
		{
			handlerMappings.add((HandlerMapping)proList.getObject(i));
		}
		return handlerMappings;
	}

	public List<HandlerMapping> getValueFromString(String fromValue) {
		if(fromValue == null || fromValue.length() == 0)
			return null;
		String[] hms = fromValue.split(",");
		List<HandlerMapping> handlerMappings = new ArrayList<HandlerMapping>();
		for(int i = 0; i < hms.length; i ++)
		{
			handlerMappings.add((HandlerMapping)this.getApplicationContext().createBean(hms[i]));
		}
		
		return handlerMappings;
	}

}
