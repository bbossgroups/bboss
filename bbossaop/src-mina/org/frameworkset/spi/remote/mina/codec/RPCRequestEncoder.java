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

package org.frameworkset.spi.remote.mina.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.frameworkset.spi.remote.RPCRequest;


/**
 * <p>Title: RPCRequestEncoder.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-10-7 下午05:33:22
 * @author biaoping.yin
 * @version 1.0
 */
public class RPCRequestEncoder implements ProtocolEncoder 
{

	public void dispose(IoSession session) throws Exception
	{

		// TODO Auto-generated method stub
		
	}

	public void encode(IoSession session, Object message,
			ProtocolEncoderOutput out) throws Exception
	{

		RPCRequest request = (RPCRequest) message;
        IoBuffer buffer = IoBuffer.allocate(12, false);
        buffer.setAutoExpand(true);
//        buffer.putInt(request.getWidth());
//        buffer.putInt(request.getHeight());
//        buffer.putInt(request.getNumberOfCharacters());
        buffer.putObject(request);
        buffer.flip();
        out.write(buffer);
		
	}

	

}
