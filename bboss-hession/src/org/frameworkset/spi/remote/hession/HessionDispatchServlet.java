/**
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
package org.frameworkset.spi.remote.hession;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * HessionDispatchServlet.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2009
 * </p>
 * 
 * @Date 2013-2-21 下午4:53:04
 * @author biaoping.yin
 * @version 1.0
 */
public class HessionDispatchServlet extends GenericServlet {
	private HessianHandlerFactory hessianHandlerFactory;

	public HessionDispatchServlet() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		if (!req.getMethod().equals("POST")) {
			res.setStatus(500, "Hessian Requires POST");
			PrintWriter out = res.getWriter();

			res.setContentType("text/html");
			out.println("<h1>Hessian Requires POST</h1>");

			return;
		}

		try {
			AbstractHessionHandler handler = this.hessianHandlerFactory
					.getHessionHandler(req, res);
			if(handler != null)
				handler.invoke(req, res);
		}
		
		catch (RuntimeException e) {
			throw e;
		} 
		catch (Exception e) {
			throw new ServletException(e);
		}
		catch (Throwable e) {
			throw new ServletException(e);
		}

	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.hessianHandlerFactory = new HessianHandlerFactory();
	}

}
