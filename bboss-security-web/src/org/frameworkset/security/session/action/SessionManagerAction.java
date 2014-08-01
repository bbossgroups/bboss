package org.frameworkset.security.session.action;

import java.util.List;

import org.frameworkset.security.session.entity.SessionCondition;
import org.frameworkset.security.session.entity.SessionInfoBean;
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
			SessionCondition condition, ModelMap model)  throws Exception{

		try {
			// 分页获取session数据
			ListInfo sessionList = sessionService.querySessionDataForPage(
					condition, offset, pagesize);

			model.addAttribute("sessionList", sessionList);

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
	List<SessionAPP> getAppSessionData(String appKey)  throws Exception{

		try {
			List<SessionAPP> appSessionList = sessionService
					.queryAppSessionData(appKey);

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
	String delSessions(String appkey, String sessionids, ModelMap model) {
		try {

			sessionService.delSession(appkey, sessionids);

			return "success";
		} catch (Exception e) {
			return "fail" + e.getMessage();
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
	String delAllSessions(String appkey, ModelMap model) {
		try {

			sessionService.delAllSessions(appkey);

			return "success";
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
			ModelMap model)  throws Exception{
		try {

			SessionInfoBean sessionInfo = sessionService.getSessionInfo(appkey,
					sessionid);

			model.addAttribute("sessionInfo", sessionInfo);

			return "path:viewSessionInfo";

		} catch (Exception e) {
			
			throw e;
		}
	}

}
