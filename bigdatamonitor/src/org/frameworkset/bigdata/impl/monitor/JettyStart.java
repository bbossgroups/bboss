package org.frameworkset.bigdata.impl.monitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.frameworkset.bigdata.imp.HDFSUploadData;
import org.frameworkset.bigdata.imp.HDFSUploadEventHandler;
import org.frameworkset.bigdata.imp.Imp;
import org.frameworkset.event.EventType;
import org.frameworkset.event.NotifiableFactory;
import org.frameworkset.runtime.CommonLauncher;
import org.xml.sax.SAXException;

public class JettyStart {
	private static Logger log = Logger.getLogger(JettyStart.class);
	public JettyStart() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		String adminNode = System.getProperty("adminNode");
		if(adminNode != null && adminNode.equals("false"))
		{
			NotifiableFactory.getNotifiable().addListener(new HDFSUploadEventHandler(), HDFSUploadData.hdfsuploadevent);
			List<EventType> monitorEventTypes = new ArrayList<EventType>();
			monitorEventTypes.add(HDFSUploadData.hdfs_upload_monitor_request_commond);
			monitorEventTypes.add(HDFSUploadData.hdfs_upload_monitor_response_commond);
			NotifiableFactory.getNotifiable().addListener(Imp.getImpStaticManager(), monitorEventTypes);
			org.frameworkset.remote.EventUtils.init();
			log.info("初始化分布式事件模块完毕！");
		}
		else
		{
			try {
				// 服务器的监听端口
				String port = CommonLauncher.getProperty("port", "8080");
				if (port.equals(""))
					port = "8080";
				String contextPath = CommonLauncher.getProperty("context",
						"bigdata");
				if (contextPath.equals(""))
					contextPath = "bigdata";
	
				int p = Integer.parseInt(port);
				Server server = new Server(p);
				// 关联一个已经存在的上下文
				WebAppContext context = new WebAppContext();
				// 设置描述符位置
				context.setDescriptor("./WebRoot/WEB-INF/web.xml");
				// 设置Web内容上下文路径
				context.setResourceBase("./WebRoot");
				// 设置上下文路径
				context.setContextPath("/" + contextPath);
				context.setParentLoaderPriority(true);
				ContextHandlerCollection contexts = new ContextHandlerCollection();
				contexts.setHandlers(new Handler[] { context });
	
				server.setHandler(contexts);
	
				// server.setHandler(context);
				// 启动
				server.start();
				server.join();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		

	}
	
	public static void setAppdir(File appdir) {
		Imp.setAppdir(appdir);
	}

}
