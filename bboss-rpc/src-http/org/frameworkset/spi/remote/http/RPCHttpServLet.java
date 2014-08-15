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
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.soa.ObjectSerializable;
import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.remote.BaseRPCIOHandler;
import org.frameworkset.spi.remote.RPCMessage;

//import com.thoughtworks.xstream.XStream;

/**
 * <p>
 * Title: ServerLet.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2008
 * </p>
 * 
 * @Date 2010-9-5
 * @author biaoping.yin
 * @version 1.0
 */
public class RPCHttpServLet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	BaseRPCIOHandler ioHandler = (BaseRPCIOHandler) ApplicationContext
			.getApplicationContext().getBeanObject(
					"rpc.application.server.RPCServerIoHandler");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		InputStream instream = req.getInputStream();

//		XStream stream = new XStream();
		
		RPCMessage ret = null;
		try {
			RPCMessage message = (RPCMessage) ObjectSerializable.toBean(instream, RPCMessage.class);
			ret = ioHandler.messageReceived(message);
			resp.reset();
			resp.setContentType(BBossHttp.XML_TEXT_TYPE);

			PrintWriter out = resp.getWriter();
			ObjectSerializable.toXML(ret, out);
			out.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(req, response);

	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
	}

	// @Override
	// protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	// throws ServletException, IOException {
	// // TODO Auto-generated method stub
	// super.doGet(req, resp);
	// }
	//
	// @Override
	// protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	// throws ServletException, IOException {
	// // TODO Auto-generated method stub
	// super.doPost(req, resp);
	// }

}
