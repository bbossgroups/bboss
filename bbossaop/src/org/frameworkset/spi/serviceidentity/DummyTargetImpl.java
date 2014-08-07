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

package org.frameworkset.spi.serviceidentity;

import java.util.List;

import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.Target;

/**
 * <p>Title: DummyTargetImpl.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-5-11 11:25:01
 * @author biaoping.yin
 * @version 1.0
 */
public class DummyTargetImpl implements Target{

	public String getFirstNode() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getNextNode() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getStringTargets() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<RPCAddress> getTargets() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isAll() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSelf() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean ismuticast() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isunicast() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean protocol_ejb() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean protocol_http() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean protocol_jgroup() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean protocol_jms() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean protocol_mina() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean protocol_netty() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean protocol_rest() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean protocol_rmi() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean protocol_webservice() {
		// TODO Auto-generated method stub
		return false;
	}

}
