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
package org.frameworkset.spi.assemble;

/**
 * 
 * 
 * <p>Title: RollbackException.java</p>
 *
 * <p>Description: 事务性方法事务回滚异常定义</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>bboss workgroup</p>
 * @Date Aug 7, 2008 10:05:17 AM
 * @author biaoping.yin,尹标平
 * @version 1.0
 */
public class RollbackException {
	public static final int TYPE_IMPLEMENTS = 0;
	public static final int TYPE_INSTANCEOF = 1;
	
	private String exceptionName;
	private Class exceptionClass;
	
	private int exceptionType ;

	public String getExceptionName() {
		return exceptionName;
	}

	public void setExceptionName(String exceptionName) {
		this.exceptionName = exceptionName;
		try
		{
			this.exceptionClass = Class.forName(this.exceptionName);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public Class getExceptionClass() {
		return exceptionClass;
	}

	public void setExceptionClass(Class exceptionClass) {
		this.exceptionClass = exceptionClass;
	}

	public int getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(String exceptionType) {
		if(exceptionType == null)
		{
			exceptionType = "INSTANCEOF";
		}
		if(exceptionType.equals("IMPLEMENTS"))
		{
			this.exceptionType = TYPE_IMPLEMENTS;
		}
		else if(exceptionType.equals("INSTANCEOF"))
		{
			this.exceptionType = TYPE_INSTANCEOF;
		}
		else
		{
			this.exceptionType = TYPE_INSTANCEOF;
		}
			
	}

}
