package org.frameworkset.web.servlet.launcher;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.web.listener.JarUtil;


public class JarLoadServlet  extends HttpServlet {
	static boolean loaded = JarUtil.loadCustomJars();
//	static {
//		// Load default strategy implementations from properties file.
//		// This is currently strictly internal and not meant to be customized
//		// by application developers.
//		try {
////			System.setErr(new PrintStream(new File("d:/rserr.log")));
////			System.setOut(new PrintStream(new File("d:/rsout.log")));
//			System.out.println("JarUtil.loadCustomJars();");
//				JarUtil.loadCustomJars();
//			
//		}
//		catch (Exception ex) {
//			throw new IllegalStateException("Could not load 'DispatcherServlet.properties': " + ex.getMessage());
//		}
//	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}





	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPut(req, resp);
	}







	



	public void init(ServletConfig arg0) throws ServletException {
		System.out.println("JarUtil.loadCustomJars()public void init(ServletConfig arg0) throws ServletException {;");
		
	}

	
}
