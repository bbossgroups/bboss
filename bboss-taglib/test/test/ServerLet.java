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
package test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thoughtworks.xstream.XStream;

/**
 * <p>Title: ServerLet.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-5
 * @author biaoping.yin
 * @version 1.0
 */
public class ServerLet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		InputStream instream = req.getInputStream();
		 ByteArrayOutputStream
		 buffer = new ByteArrayOutputStream();
	        try {
	            byte[] tmp = new byte[4096];
	            int l;
	            while((l = instream.read(tmp)) != -1) {
	                buffer.write(tmp, 0, l);
	            }
	        } finally {
	            instream.close();
	        }
	        PrintWriter out = resp.getWriter();
	        String ret = "<html>";
//			out.write("<html>");
	        ret = ret + new String( buffer.toByteArray()) + "</html>";
	        out.write(ret);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		InputStream instream = req.getInputStream();
		 ByteArrayOutputStream
		 buffer = new ByteArrayOutputStream();
	        try {
	            byte[] tmp = new byte[4096];
	            int l;
	            while((l = instream.read(tmp)) != -1) {
	                buffer.write(tmp, 0, l);
	            }
	        } finally {
	            instream.close();
	        }
//	        PrintWriter out = resp.getWriter();
	        String ret = "<html>";
////			out.write("<html>");
	        ret = ret + new String( buffer.toByteArray()) + "</html>";
//	        out.write(ret);
	        org.frameworkset.spi.remote.http.TestBean bean = new org.frameworkset.spi.remote.http.TestBean();
	        bean.setName(ret);
	        bean.setId("testid");
//	        response.reset();      
            response.setContentType("text/xml; charset=UTF-8");   
            XStream steam = new XStream();
            steam.toXML(bean, response.getWriter());
//           response.setHeader("Content-Length",  + "");  
           
//           response.getWriter().write(ret);      
           
//           ObjectOutputStream oot = new ObjectOutputStream(os);
////           oot.writeInt(100);
//           oot.write(ret.getBytes());
//           oot.flush();
//           oot.close();

	       
//		resp.setContentType(CONTENT_TYPE);
		
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		super.init(config);
	}

//	@Override
//	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//			throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		super.doGet(req, resp);
//	}
//
//	@Override
//	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
//			throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		super.doPost(req, resp);
//	}



}
