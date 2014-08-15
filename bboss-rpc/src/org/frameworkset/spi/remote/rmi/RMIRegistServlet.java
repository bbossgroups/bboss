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

package org.frameworkset.spi.remote.rmi;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * <p>Title: RMIRegistServlet.java</p> 
 * <p>Description: 在web应用程序中启动所有rmi服务的servlet
 * 改servlet的启动顺序必须在mvc框架的servlet后面启动，否则
 * 会导致mvc框架中配置的rmi服务无法加载
 * </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-5-20 上午09:49:40
 * @author biaoping.yin
 * @version 1.0
 */
public class RMIRegistServlet extends HttpServlet {

	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		
		super.init(config);
		org.frameworkset.spi.remote.rmi.RMIAssemble.loadAllRMIService();
	}

}
