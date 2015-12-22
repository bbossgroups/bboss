package org.frameworkset.security.session.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.frameworkset.security.session.domain.CrossDomain;
import org.frameworkset.security.session.entity.Apps;
import org.frameworkset.security.session.entity.SessionCondition;
import org.frameworkset.security.session.entity.SessionInfoBean;
import org.frameworkset.security.session.impl.SessionHelper;
import org.frameworkset.security.session.service.SessionManagerService;
import org.frameworkset.security.session.statics.AttributeInfo;
import org.frameworkset.security.session.statics.SessionAPP;
import org.frameworkset.security.session.statics.SessionConfig;
import org.frameworkset.soa.ObjectSerializable;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

public class SessionManagerAction {

	private SessionManagerService sessionService;
	private static Logger log = Logger.getLogger(SessionManagerAction.class);
	public String sessionManager(String appkey,ModelMap model) {
		if(!StringUtil.isEmpty(appkey))
		{
			SessionConfig sessionConfig = SessionHelper .getSessionConfig(appkey);
			if(sessionConfig != null && sessionConfig.getExtendAttributeInfos() != null)		
			{
				
				model.addAttribute("sessionConfig", sessionConfig);
				if(  sessionConfig.getExtendAttributeInfos() != null)			
					model.addAttribute("monitorAttributes", sessionConfig.getExtendAttributeInfos());
			}
		}
		return "path:sessionManager";
	}
	
	public @ResponseBody String deleteApp(String appkey,HttpServletRequest request)
	{
		if(StringUtil.isEmpty(appkey))
		{
			return "对不起，请选择要删除应用!";
		}
		String currentAppkey = SessionHelper.getAppKey(request);
		if(appkey.equals(currentAppkey))
		{
			return "对不起，不能删除本系统对应的应用"+currentAppkey+"!";
		}
		if(SessionHelper.hasDeleteAppPermission(appkey, request))
		{
			try {
				SessionHelper.deleteApp(appkey);
				return "success";
			} catch (Exception e) {
				log.error("删除应用失败：", e);
				return "删除应用失败："+e.getMessage();
				
			}
		}
		else
		{
			return "对不起，没有删除应用"+appkey+"的权限!";
		}
	}

	
	
	/**
	 * 分页获取session数据
	 * 
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param pagesize
	 * @param task
	 * @param model
	 * @return 2014年6月4日
	 */
	public String querySessionData(
			@PagerParam(name = PagerParam.SORT, defaultvalue = "") String sortKey,
			@PagerParam(name = PagerParam.DESC, defaultvalue = "false") boolean desc,
			@PagerParam(name = PagerParam.OFFSET) int offset,
			@PagerParam(name = PagerParam.PAGE_SIZE, defaultvalue = "10") int pagesize,
			SessionCondition condition, ModelMap model,HttpServletRequest request)  throws Exception{

		try {
			if(StringUtil.isEmpty(condition.getAppkey()))
			{
				return "path:sessionList";
			}
				
			if(!SessionHelper.hasMonitorPermission(condition.getAppkey(), request))
			{
				model.addAttribute("message","对不起，没有查询应用"+condition.getAppkey()+"的session数据的权限");
			}
			else
			{
				SessionConfig sessionConfig = SessionHelper.getSessionConfig(condition.getAppkey());
				if(sessionConfig != null)
					condition.setExtendAttributes(SessionHelper.parserExtendAttributes(request,sessionConfig));
				// 分页获取session数据
				ListInfo AllSessions = sessionService.querySessionDataForPage(
						sessionConfig,condition, offset, pagesize);
				HttpSession session = request.getSession(false);
				if(session != null)
				{
					model.addAttribute("currentsessionid", session.getId());
				}
				model.addAttribute("sessionList", AllSessions);
				if(sessionConfig != null)
				{					
					AttributeInfo[] monitorAttributeArray = sessionConfig.getExtendAttributeInfos();
//					model.addAttribute("sessionConfig", sessionConfig);
					if(monitorAttributeArray != null)			
						model.addAttribute("monitorAttributes", monitorAttributeArray);
				}
			}

			return "path:sessionList";

		} catch (Exception e) {
			throw e;
		}

	}

	/**
	 * 获取应用概览列表
	 * 
	 * @param appKey
	 * @return 2014年6月5日
	 */
	public @ResponseBody(datatype = "json")
	Apps getAppSessionData(String appKey,boolean loadextendattrs,HttpServletRequest request)  throws Exception{

		try {
			Apps apps = new Apps();
			List<SessionAPP> appSessionList = sessionService
					.queryAppSessionData(appKey,  request);
			apps.setApps(appSessionList);
			
			if(loadextendattrs && StringUtil.isNotEmpty(appKey))
			{
				SessionConfig config = SessionHelper.getSessionConfig(appKey);
				if(config != null)
					apps.setExtendAttributes(config.getExtendAttributeInfos());
			}
			return apps;

		} catch (Exception e) {
			throw e;
		}
	}
	
	
//	/**
//	 * 获取appKey对应的应用的session统计数据
//	 * 
//	 * @param appKey
//	 * @return 2014年6月5日
//	 */
//	public @ResponseBody(datatype = "json")
//	SessionAPP getSingleSessionAPP(String appKey)  throws Exception{
//
//		try {
//			SessionAPP appSessionList = sessionService
//					.getSingleSessionAPP(appKey);
//
//			return appSessionList;
//
//		} catch (Exception e) {
//			throw e;
//		}
//	}
	
	/**
	 * 获取当前请求对应的应用session统计信息
	 * 
	 * @param appKey
	 * @return 2014年6月5日
	 */
	public @ResponseBody(datatype = "json")
	SessionAPP getSingleSessionAPPByRequest(HttpServletRequest request)  throws Exception{

		try {
			SessionAPP appSessionList = sessionService
					.getSingleSessionAPP(request);

			return appSessionList;

		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * 删除应用下的指定session
	 * 
	 * @param sessionid
	 * @param model
	 * @return 2014年6月5日
	 */
	public @ResponseBody
	String delSessions(String appkey, String sessionids, ModelMap model,HttpServletRequest request) {
		try {
			if(!SessionHelper.hasMonitorPermission(appkey, request))
			{
				return "对不起，没有删除应用"+appkey+"的session数据的权限";
			}
			else if(StringUtil.isNotEmpty((String)sessionids))
			{
				String[] sessionids_ = sessionids.split(",");
				HttpSession session = request.getSession(false);
				if(session != null)
				{
					String cursessionid = session.getId();
					for(int i = 0; i < sessionids_.length; i++)
					{
						String sessionid = sessionids_[i];						
						if(cursessionid.equals(sessionid))
						{
							sessionids_[i] = null;
						}
					}
				}
				sessionService.delSession(appkey, sessionids_);
	
				return "success";
			}
			else
				return "没有要删除的会话数据.";
		} catch (Exception e) {
			return "fail:" + e.getMessage();
		}
	}

	/**
	 * 清空应用下的所有session
	 * 
	 * @param sessionid
	 * @param model
	 * @return 2014年6月5日
	 */
	public @ResponseBody
	String delAllSessions(String appkey, ModelMap model,HttpServletRequest request) {
		try {
			if(!SessionHelper.hasMonitorPermission(appkey, request))
			{
				return "对不起，没有删除应用"+appkey+"的session数据的权限";
			}
			else
			{
				HttpSession session = request.getSession(false);
				String sessionid = session != null?session.getId():null;
				String currentappkey = SessionHelper.getAppKey(request);
				sessionService.delAllSessions(appkey,  currentappkey,sessionid);
	
				return "success";
			}
		} catch (Exception e) {
			return "fail" + e.getMessage();
		}
	}

	/**
	 * 流程明细查看跳转
	 * 
	 * @param processKey
	 * @return 2014年5月7日
	 */
	public String viewSessionInfo(String sessionid, String appkey,
			ModelMap model,HttpServletRequest request)  throws Exception{
		try {
			if(!SessionHelper.hasMonitorPermission(appkey, request))
			{
				model.addAttribute("message","对不起，没有查看应用"+appkey+"的session数据的权限");
			}
			else
			{
				SessionInfoBean sessionInfo = sessionService.getSessionInfo(appkey,
						sessionid);
	
				model.addAttribute("sessionInfo", sessionInfo);
			}

			return "path:viewSessionInfo";

		} catch (Exception e) {
			
			model.addAttribute("message","对不起，"+appkey+"的session["+sessionid+"]不存在");
			return "path:viewSessionInfo";
		}
	}
	
	public String viewSessionConfig(  String appkey,
			ModelMap model,HttpServletRequest request)  throws Exception{
		try {
			if(!SessionHelper.hasMonitorPermission(appkey, request))
			{
				model.addAttribute("message","对不起，没有查看应用"+appkey+"的session共享的权限");
			}
			else
			{
				SessionConfig sessionConfig = SessionHelper .getSessionConfig(appkey,true);
				if(sessionConfig != null  )		
				{
					
					model.addAttribute("sessionConfig", sessionConfig);
					if(sessionConfig.getCrossDomain() != null && !sessionConfig.getCrossDomain().equals(""))
					{
						CrossDomain crossDomain = ObjectSerializable.toBean(sessionConfig.getCrossDomain(), CrossDomain.class);
						model.addAttribute("crossDomain", crossDomain);
					}
					 
				}
				else
					model.addAttribute("message", "对不起，没有获取到应用"+appkey+"的session共享配置");
			}

			return "path:viewSessionConfig";

		} catch (Exception e) {
			
			model.addAttribute("message","对不起，获取"+appkey+"的session共享配置失败!");
			return "path:viewSessionConfig";
		}
	}

}
