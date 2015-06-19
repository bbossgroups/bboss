package com.sany.bigdata.imp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.frameworkset.event.EventType;
import org.frameworkset.event.NotifiableFactory;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.DefaultApplicationContext;
import org.frameworkset.spi.SOAApplicationContext;
import org.frameworkset.util.annotations.ResponseBody;

import com.frameworkset.util.SimpleStringUtil;
import com.sany.bigdata.imp.monitor.ImpStaticManager;

public class Imp {
	private static Logger log = Logger.getLogger(Imp.class);
	private static Map<String,String> parserParams(String params_)
	{
		Map<String,String> params = new HashMap<String,String>();
		String[] temp = params_.split("\\+");
		for(String p:temp)
		{
			String[] p_ = p.split("=");
			params.put(p_[0], p_[1]);
		}
		return params;
	}
	private static ImpStaticManager impStaticManager = new ImpStaticManager();
	public static ImpStaticManager getImpStaticManager()
	
	{
		return impStaticManager;
	}
	public static void executeJob(String job) throws Exception
	{
		HDFSUploadData HDFSUploadData = new HDFSUploadData();
		HDFSUploadData.uploadData(job);
	}
	public static void startAdminNode(boolean adminasdatanode)
	{
		//监听器监听的事件消息可以是本地事件，可以是远程本地消息，也可以是远程消息
		//如果不指定eventtypes则监听所有类型的事件消息
		impStaticManager.setAdminasdatanode(adminasdatanode);
		NotifiableFactory.getNotifiable().addListener(new HDFSUploadEventHandler(), HDFSUploadData.hdfsuploadevent);
		List<EventType> monitorEventTypes = new ArrayList<EventType>();
		monitorEventTypes.add(HDFSUploadData.hdfs_upload_monitor_request_commond);
		monitorEventTypes.add(HDFSUploadData.hdfs_upload_monitor_response_commond);
		NotifiableFactory.getNotifiable().addListener(impStaticManager, monitorEventTypes);
		org.frameworkset.remote.EventUtils.init();
		log.info("初始化分布式事件模块完毕！");
		log.info("adminNode:true");




	}
	public static void main(String[] args) throws Exception
	{
		//监听器监听的事件消息可以是本地事件，可以是远程本地消息，也可以是远程消息
				//如果不指定eventtypes则监听所有类型的事件消息
		String deleteNode = System.getProperty("deleteNode");
		if(deleteNode != null && !deleteNode.trim().equals(""))
		{
			log.info("deleteNode:"+deleteNode);
		}
		else
		{
			
			NotifiableFactory.getNotifiable().addListener(new HDFSUploadEventHandler(), HDFSUploadData.hdfsuploadevent);
			List<EventType> monitorEventTypes = new ArrayList<EventType>();
			monitorEventTypes.add(HDFSUploadData.hdfs_upload_monitor_request_commond);
			monitorEventTypes.add(HDFSUploadData.hdfs_upload_monitor_response_commond);
			NotifiableFactory.getNotifiable().addListener(impStaticManager, monitorEventTypes);
			org.frameworkset.remote.EventUtils.init();
			log.info("初始化分布式事件模块完毕！");
		}
		String adminNode = System.getProperty("adminNode");
		
		
//		String adminNode = "true";
//		String jobname_="test";
		
		log.info("adminNode:"+adminNode);
		StringBuilder buidler = new StringBuilder();
		for(int i =0 ;args != null && i < args.length; i++)
			 buidler.append(args[i]).append(" ");
		log.info("executor args:"+buidler);
		
		
		if(adminNode != null && adminNode.equals("true"))
		{
			if(args == null || args.length == 0)
			{
				log.info("没有指定需要执行的作业名称，请在resources/task.xml文件中查找对应的作业名称，多个请用逗号分隔。参数语法：jobs=testpagine|datanode=true，多个参数请用|分隔");
				return;
			}
				
			String params_=args[0];
			Map<String,String> params =  parserParams(params_);
			String jobname_ = params.get("jobs");
			String[] jobnames = jobname_.split(",");
			if(jobnames == null  || jobnames.length == 0)
			{
				log.info("delete jobs hdfs files 完毕:没有指定要执行的作业");
				return ;
			}
			log.info("execute jobs:"+jobname_);
			boolean datanode = false;
			String datanode_ = params.get("datanode");
			if(datanode_ != null && datanode_.equals("true"))
			{
				datanode = true;
			}
			impStaticManager.setAdminasdatanode(datanode);
			if(datanode)
				log.info("管理节点作为数据处理节点,将会参与数据处理作业：datanode=true");
			else
				log.info("管理节点不可以作为数据处理节点,不会参与数据处理作业：datanode=false");
			HDFSUploadData HDFSUploadData = new HDFSUploadData();
		 
			for(final String job:jobnames)
			{
//				if(jobnames.length > 1)
//				{
//					new Thread(new Runnable(){
//						public void run()
//						{
//							try {
//								HDFSUploadData.uploadData(job,isdatanode);
//							} catch (Exception e) {
//								log.error("任务["+job+"]执行失败：", e);
//							}
//						}
//					},"任务["+job+"]执行线程").start();
//					
//				}
//				else
				{
					HDFSUploadData.uploadData(job);
				}
				
			}
		}
		else if(deleteNode != null && !deleteNode.trim().equals(""))
		{
			String params_=args[0];
			Map<String,String> params =  parserParams(params_);
		 
			String jobname_ = params.get("jobs");
			String[] jobnames = jobname_.split(",");
			if(jobnames == null  || jobnames.length == 0)
			{
				log.info("delete jobs hdfs files 完毕:没有指定要执行的作业");
				return ;
			}
			log.info("delete jobs hdfs files 开始:"+jobname_);
			
			
			
			
			HDFSUploadData HDFSUploadData = new HDFSUploadData();
			for(String job:jobnames)
				HDFSUploadData.deleteData(job);
			
			log.info("delete jobs hdfs files 完毕:"+jobname_);
		}
	}
	
	/**
	 * 创建并提交一个新作业
	 * @param jobdef
	 * @return
	 */
	public static String submitNewJob(String jobdef)
	{
		BaseApplicationContext ioccontext = new SOAApplicationContext(jobdef);
		List<String> jobs = getConfigTasks(ioccontext);
		StringBuilder builder = new StringBuilder();
		if(jobs != null && jobs.size() > 0)
		{
			for(int i = 0; i < jobs.size(); i++)
			{
				String jobname = jobs.get(i);
				HDFSUploadData HDFSUploadData = new HDFSUploadData();
				try {
					
					HDFSUploadData.uploadData(ioccontext,jobname);
				} catch (Exception e) {
					log.error(jobname + " 作业执行失败：",e);
					builder.append(SimpleStringUtil.exceptionToString(e));
				}
			}
		}
		if(builder.length() > 0)
			return builder.toString();
		else
			return "success";
		
		
	}
	
	
	public static List<String> getConfigTasks(BaseApplicationContext context)
	{
		 if(context == null )
			 return null;
		List<String> tasks = new ArrayList<String>();
		
		tasks.addAll(context.getPropertyKeys());
		return tasks;
	}

}
