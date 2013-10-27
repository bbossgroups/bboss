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

package org.frameworkset.netty;

import static org.jboss.netty.channel.Channels.pipeline;

import javax.net.ssl.SSLEngine;

import org.frameworkset.spi.assemble.ProMap;
import org.frameworkset.spi.remote.Util;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.handler.codec.serialization.ObjectDecoder;
import org.jboss.netty.handler.codec.serialization.ObjectEncoder;
import org.jboss.netty.handler.ssl.SslHandler;


/**
 * <p>Title: NettyChannelPipelineFactory.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-4-20 下午03:18:49
 * @author biaoping.yin
 * @version 1.0
 */
public class NettyChannelPipelineFactory implements ChannelPipelineFactory
{
	public static final int maxFramgeLength_ = 20971520;
	public static final int estimatedLength_ = 512;
	private int maxFramgeLength = maxFramgeLength_;
	private int estimatedLength = estimatedLength_;
	public NettyChannelPipelineFactory(int maxFramgeLength,int estimatedLength)
	{
		this.maxFramgeLength = maxFramgeLength;
		this.estimatedLength = estimatedLength;
	}
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = pipeline();

        // Add SSL handler first to encrypt and decrypt everything.
        // In this example, we use a bogus certificate in the server side
        // and accept any invalid certificates in the client side.
        // You will need something more complicated to identify both
        // and server in the real world.
        //
        // Read SecureChatSslContextFactory
        // if you need client certificate authentication.
        
        /**
         * 增加ssl的技术支持
         */
        ProMap commons = Util.defaultContext.getMapProperty("rpc.protocol.netty.params");
        boolean enablessl = commons.getBoolean("enablessl",false);
        SSLEngine eg = null;
        if(enablessl)
        {
            eg = NettyClinentTransport.buildSSLEngine(false);
        }
       
        if(eg != null)
            pipeline.addFirst("ssl", new SslHandler(eg));
        
        ChannelUpstreamHandler sh = null;
        sh = (ChannelUpstreamHandler)Util.defaultContext.getBeanObject(Util.rpc_netty_RPCServerIoHandler); 
        pipeline.addLast("decoder", new ObjectDecoder(maxFramgeLength));
        pipeline.addLast("encoder", new ObjectEncoder(estimatedLength));
        pipeline.addLast("handler", sh);
        return pipeline;
    }
}
