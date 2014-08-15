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
package org.frameworkset.log;



/**
 * <p> 通用日志记录接口 </p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2009-8-9
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class Logger implements BaseLogger
{
	public abstract void logBasic(String log);
	public abstract void logDebug(String log);
	public abstract void logDetailed(String log);
	public abstract void logError(String log);
	public abstract void logError(String log, Throwable e);
	public abstract void logMinimal(String log);
	public abstract void logRowlevel(String log);
	
	public abstract void logBasic(String subject,String log);
        public abstract void logDebug(String subject,String log);
        public abstract void logDetailed(String subject,String log);
        public abstract void logError(String subject,String log);
        public abstract void logError(String subject,String log, Throwable e);
        public abstract void logMinimal(String subject,String log);
        public abstract void logRowlevel(String subject,String log);
	
}
