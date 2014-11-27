package org.frameworkset.security.session.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.frameworkset.security.session.entity.SessionCondition;
import org.frameworkset.security.session.entity.SessionInfoBean;
import org.frameworkset.security.session.impl.SessionHelper;
import org.frameworkset.security.session.service.SessionManagerService;
import org.frameworkset.security.session.statics.SessionAPP;
import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.util.ListInfo;

public class SessionManagerAction {

	private SessionManagerService sessionService;

	public String sessionManager() {

		return "path:sessionManager";
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
			if(!SessionHelper.hasMonitorPermission(condition.getAppkey(), request))
			{
				model.addAttribute("message","对不起，没有查询应用"+condition.getAppkey()+"session数据的权限");
			}
			else
			{
				// 分页获取session数据
				ListInfo sessionList = sessionService.querySessionDataForPage(
						condition, offset, pagesize);
	
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
			else
			{
				sessionService.delSession(appkey, sessionids);
	
				return "success";
			}
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
				sessionService.delAllSessions(appkey);
	
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
