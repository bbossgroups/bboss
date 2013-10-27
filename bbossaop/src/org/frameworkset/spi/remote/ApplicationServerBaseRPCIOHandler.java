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

package org.frameworkset.spi.remote;


/**
 * <p>Title: ApplicationServerBaseRPCIOHandler.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-9-15 下午11:00:06
 * @author biaoping.yin
 * @version 1.0
 */
public class ApplicationServerBaseRPCIOHandler extends BaseRPCIOHandler {

	public ApplicationServerBaseRPCIOHandler(String name,
			RequestHandler handler, String localaddress) {
		super(name, handler, localaddress);
		// TODO Auto-generated constructor stub
	}
	public ApplicationServerBaseRPCIOHandler(String name,
			RequestHandler handler) {
		super(name, handler, null);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected BaseFutureCall buildBaseFutureCall(RPCMessage srcmsg,
			RPCAddress address) {
		// TODO Auto-generated method stub
		return null;
	}

}
