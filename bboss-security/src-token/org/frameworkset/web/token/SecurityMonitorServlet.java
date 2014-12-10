package org.frameworkset.web.token;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.Logger;
import org.frameworkset.security.session.impl.SessionHelper;

/**
 * 独立的token生命周期管理服务，在启用统一的mongodb存储token和ticket情况下有用
 * @author yinbp
 *
 */
public class SecurityMonitorServlet extends HttpServlet {
	private static Logger log = Logger.getLogger(SecurityMonitorServlet.class);
	@Override
	public void init(ServletConfig config) throws ServletException {
		
		super.init(config);
		try {
			
			 TokenHelper.getTokenService();
			
		} catch (Throwable e) {
			log.warn("",e);
		}
		
		try {
			
			SessionHelper.getSessionManager();
			
		} catch (Throwable e) {
			log.warn("",e);
		}
	}

}
