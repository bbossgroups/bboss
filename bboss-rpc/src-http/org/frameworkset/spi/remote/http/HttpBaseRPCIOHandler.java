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

package org.frameworkset.spi.remote.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.protocol.HttpContext;
import org.frameworkset.soa.ObjectSerializable;
import org.frameworkset.spi.remote.BaseFutureCall;
import org.frameworkset.spi.remote.BaseRPCIOHandler;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.RPCMessage;
import org.frameworkset.spi.remote.RequestHandler;

//import com.thoughtworks.xstream.XStream;

/**
 * <p>Title: HttpBaseRPCIOHandler.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-9-12 ÉÏÎç10:02:00
 * @author biaoping.yin
 * @version 1.0
 */
public class HttpBaseRPCIOHandler extends BaseRPCIOHandler implements org.apache.http.protocol.HttpRequestHandler{

	public HttpBaseRPCIOHandler(String name, RequestHandler handler) {
		super(name, handler, null);

	}

	@Override
	protected BaseFutureCall buildBaseFutureCall(RPCMessage srcmsg,
			RPCAddress address) {

		return new HttpFuture( srcmsg,  address, this);
	}
	
	public RPCAddress getLocalAddress()
    {

		return HttpServer.getHttpServer().getLocalAddress();
    }

	public void handle(HttpRequest request, HttpResponse response,
			HttpContext context) throws HttpException, IOException {
//		InputStream instream = request.getInputStream();
//		 ByteArrayOutputStream
//		 buffer = new ByteArrayOutputStream();
//	        try {
//	            byte[] tmp = new byte[4096];
//	            int l;
//	            while((l = instream.read(tmp)) != -1) {
//	                buffer.write(tmp, 0, l);
//	            }
//	        } finally {
//	            instream.close();
//	        }
		String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
        if (!method.equals("GET") && !method.equals("HEAD") && !method.equals("POST")) {
            throw new MethodNotSupportedException(method + " method not supported");
        }

       
           	
          
			try {
				 HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();

	            InputStream instream = entity.getContent();
//	            XStream stream = new XStream();
	            RPCMessage message = (RPCMessage) ObjectSerializable.toBean(instream, RPCMessage.class);   
				RPCMessage ret = super.messageReceived(message);
				 String ret_str = ObjectSerializable.toXML(ret);
		            response.addHeader("Content-Type", "text/xml;charset=UTF-8");    

		              response.setEntity(new NStringEntity(ret_str));
			} catch (Exception e) {
				throw new HttpException(e.getMessage());
			}
           
     

	       
		
	}

}
