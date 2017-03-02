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
package org.frameworkset.persitent.util;

import java.net.URL;

import org.frameworkset.spi.SOAFileApplicationContext;
import org.frameworkset.spi.assemble.ServiceProviderManager;

public class SQLSOAFileApplicationContext extends SOAFileApplicationContext {

	public SQLSOAFileApplicationContext(String file) {
		super(file);
		// TODO Auto-generated constructor stub
	}

	public SQLSOAFileApplicationContext(URL file, String path) {
		super(file, path);
		// TODO Auto-generated constructor stub
	}

	public SQLSOAFileApplicationContext(String file, String charset) {
		super(file, charset);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected ServiceProviderManager _getServiceProviderManager() {
		// TODO Auto-generated method stub
		return new SQLServiceProviderManager(this);
	}
	
	protected ServiceProviderManager _getServiceProviderManagerWithCharset(String charset)
	{
		return new SQLServiceProviderManager(this,charset); 
	}

}
