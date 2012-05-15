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
package org.frameworkset.mvc;

import java.io.File;
import java.io.IOException;

import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.multipart.IgnoreFieldNameMultipartFile;

/**
 * <p> FileController.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2012-5-14 ÏÂÎç3:25:35
 * @author biaoping.yin
 * @version 1.0
 */
public class FileController {
	public @ResponseBody String upload(IgnoreFieldNameMultipartFile[] filedata,String testparam) throws IllegalStateException, IOException
	{
		if(filedata != null)
		{
			filedata[0].transferTo(new File("d:/tst.png"));
		}
		return "{\"err\":\"\",\"msg\":\"tst.png\"}";
	}
	
	public @ResponseBody String uploadwithbean(FileBean filedata,String testparam) throws IllegalStateException, IOException
	{
		if(filedata != null)
		{
			filedata.getFile().transferTo(new File("d:/tst.png"));
		}
		return "{\"err\":\"\",\"msg\":\"tst.png\"}";
	}
	
	public static void main(String[] args)
	{
		String dispoString = "attachment; name=\"filedata\"; filename=\"TempPlanDiagram.png\"";
		 int iFindStart = dispoString.indexOf(" filename=\"") + 11;  
         int iFindEnd = dispoString.indexOf("\"", iFindStart);  
         String sFileName = dispoString.substring(iFindStart, iFindEnd);
         iFindStart = dispoString.indexOf(" name=\"") + 7;  
         iFindEnd = dispoString.indexOf("\"", iFindStart);  
         String sFieldName = dispoString.substring(iFindStart, iFindEnd);
	}

}
