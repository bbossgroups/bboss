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
package com.frameworkset.common.poolman;

import java.util.LinkedList;
import java.util.List;


/**
 * 
 * <p>Title: CallableParams.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date Dec 7, 2008 9:08:06 PM
 * @author biaoping.yin
 * @version 1.0
 */
public class CallableParams extends Params
{
	List callParams;
	List outParams;
	int call_type = CallableDBUtil.CALL_PROCEDURE;
	public CallableParams()
	{
		super();
		callParams = new LinkedList();
		outParams = new LinkedList();
	}
	public void clear()
	{
		super.clear();
		this.call_type = CallableDBUtil.CALL_PROCEDURE;
		this.callParams = null;
		this.outParams = null;
	}
}
