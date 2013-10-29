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
package org.frameworkset.web.date;

import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.web.servlet.ModelMap;

/**
 * <p>Title: DateConvertController.java</p> 
 * <p>Description: 日期转换实例</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-4-30
 * @author biaoping.yin
 * @version 1.0
 */
public class DateConvertController implements TestInf{
	
	public String converStringToDate(@RequestParam(name="d12",dateformat="yyyy-MM-dd") java.util.Date d12,
									 @RequestParam(name="stringdate",dateformat="yyyy-MM-dd") java.sql.Date stringdate,
									 @RequestParam(name="stringdatetimestamp",dateformat="yyyy-MM-dd HH/mm/ss") java.sql.Timestamp stringdatetimestamp,
									 @RequestParam(name="stringdatetimestamp") String stringdatetimestamp_,
			ModelMap model)
	{
		model.put("java.util.Date", d12);
		model.put("java.sql.Date", stringdate);
		model.put("java.sql.Timestamp", stringdatetimestamp);
		return "path:convertok";
		
	}
	public String dateconvert()
	{
		return "path:convertin";
	}

}
