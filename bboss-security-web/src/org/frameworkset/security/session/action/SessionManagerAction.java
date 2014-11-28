package org.frameworkset.security.session.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.frameworkset.security.session.entity.SessionCondition;
import org.frameworkset.security.session.entity.SessionInfoBean;
import org.frameworkset.security.session.impl.SessionHelper;
import org.frameworkset.security.session.service.SessionManagerService;
import org.frameworkset.security.session.statics.SessionAPP;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

public class SessionManagerAction {

	private SessionManagerService sessionService;
	private static Logger log = Logger.getLogger(SessionManagerAction.class);
	public String sessionManager() {

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
				model.addAttribute("message","对不起，没有查询应用"+condition.getAppkey()+"session数据的权限");
			}
			else
			{
				// 分页获取session数据
				ListInfo sessionList = sessionService.querySessionDataForPage(
						condition, offset, pagesize);
				HttpSession session = request.getSession(false);
				if(session != null)
				{
					model.addAttribute("currentsessionid", session.getId());
				}
				model.addAttribute("sessionList", sessionList);
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
	List<SessionAPP> getAppSessionData(String appKey,HttpServletRequest request)  throws Exception{

		try {
			List<SessionAPP> appSessionList = sessionService
					.queryAppSessionData(appKey,  request);

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
				return "对不起，没有删除应用"+appkey+"session数据的权限";
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
				return "对不起，没有删除应用"+appkey+"session数据的权限";
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
				model.addAttribute("message","对不起，没有查看应用"+appkey+"session数据的权限");
			}
			else
			{
				SessionInfoBean sessionInfo = sessionService.getSessionInfo(appkey,
						sessionid);
	
				model.addAttribute("sessionInfo", sessionInfo);
			}

			return "path:viewSessionInfo";

		} catch (Exception e) {
			
			model.addAttribute("message","对不起，"+appkey+"session数据["+sessionid+"]不存在");
			return "path:viewSessionInfo";
		}
	}

}
