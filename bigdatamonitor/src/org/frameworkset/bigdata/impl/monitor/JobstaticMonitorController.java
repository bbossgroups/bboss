package org.frameworkset.bigdata.impl.monitor;

import java.util.List;

import org.apache.log4j.Logger;
import org.frameworkset.bigdata.imp.Imp;
import org.frameworkset.bigdata.imp.monitor.SpecialMonitorObject;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.util.StringUtil;

public class JobstaticMonitorController {
	private static Logger log = Logger.getLogger(JobstaticMonitorController.class);
	public String index(String job,ModelMap model)
	{
		SpecialMonitorObject specialMonitorObject = Imp.getImpStaticManager().getSpecialMonitorObject(job);
		model.addAttribute("jobInfo", specialMonitorObject);
		
		
		
		 
		 model.addAttribute("adminNode", Imp.getImpStaticManager().getLocalNode());
		 List<String> allnodes = Imp.getImpStaticManager().getAllDataNodeString();
		 model.addAttribute("allDataNodes", allnodes);
		
		return "path:index";
	}
	
	public @ResponseBody String synJobStatus()
	{
		Imp.getImpStaticManager().synJobStatus();
		return "success";
	}
	
	
	public @ResponseBody String executeJob(String job)
	{
		if(StringUtil.isEmpty(job))
			return "没有要执行的作业";
		try {
			Imp.executeJob(job);
		} catch (Exception e) {
			log.error("执行作业失败："+job, e);
			return StringUtil.exceptionToString(e);
		}
		return "success";
	}
	
	/**
	 * 创建并提交一个新作业
	 * @param jobdef
	 * @return
	 */
	public @ResponseBody String submitNewJob(String jobdef)
	{
		if(StringUtil.isEmpty(jobdef))
			return "没有提交要执行的作业";
		try {
			String msg = Imp.submitNewJob(jobdef);
			return msg;
		} catch (Exception e) {
			log.error("提交执行作业失败："+jobdef, e);
			return StringUtil.exceptionToString(e);
		}
		
	}

}
