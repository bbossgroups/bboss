/*
 *  Copyright 2008 bbossgroups
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
package org.frameworkset.web.token;

/**
 * <p>Title: NullValidateApplication.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2014年4月23日
 * @author biaoping.yin
 * @version 3.8.0
 */
public class NullValidateApplication implements ValidateApplication {

	@Override
	public boolean checkApp(String appid, String secret) throws TokenException {		
		 AppValidateResult result = validateApp(appid, secret);
		 if(result == null )
			 return true;
		 return result.getResult();
	}
	
	@Override
	public AppValidateResult validateApp(String appid, String secret) throws TokenException {		
		return null;
	}

}
