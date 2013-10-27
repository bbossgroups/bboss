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

package org.frameworkset.task;

import org.quartz.Calendar;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * <p>Title: CalendarBuilderUtil.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2013-1-23 上午11:10:31
 * @author biaoping.yin
 * @version 1.0
 */
public class CalendarBuilderUtil {

	private final static String header = "import java.util.GregorianCalendar;import org.quartz.Calendar;import org.quartz.impl.calendar.*;";
	public static Calendar calendarBuilder(String buildscript) throws Exception {
		Interpreter interpreter = new Interpreter();
		StringBuilder excuteCode = new StringBuilder();
		//判断是否有自定义导入的类
		if(header != null && !"".equals(header)){
			excuteCode.append(header);
		}
		excuteCode.append(buildscript);
		try {
			
			
			return (Calendar)interpreter.eval(excuteCode.toString());
			
		} catch (EvalError e) {
			throw e;
		} catch (Exception e) {
			
			throw e;
		}
		
	}

}
