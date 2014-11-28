package org.frameworkset.security.session.statics;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface SessionStaticManager {

	/**
	 * 获取应用概览列表数据
	 * 
	 * @return 2014年6月5日
	 */
	List<SessionAPP> getSessionAPP();
	List<SessionAPP> getSessionAPP(HttpServletRequest request);
	/**
	 * 判断用户是有使用app的session管理权限
	 * @param app 
	 * @param currentapp
	 * @return
	 */
	public boolean hasMonitorPermission(String app,HttpServletRequest request);
	public boolean hasDeleteAppPermission(String app,HttpServletRequest request);
	
	/**
	 * 判断应用是否有查询会话权限，除了总控应用可以看所有会话外，其他的应用只能看当前应用的会话数据
	 * @param app 
	 * @param currentapp
	 * @return
	 */
	public boolean hasMonitorPermission(String app,String currentapp);
	/**
	 * 
	 * @param params
	 *            appkey, sessionid,host,referip,createtime_start,
	 *            createtime_end(开始时间和结束时间)
	 * @param start
	 * @param end
	 * @return
	 */
	List<SessionInfo> getAllSessionInfos(Map queryParams, int row, int page)
			throws Exception;

	SessionInfo getSessionInfo(String appKey, String sessionid);

	void removeSessionInfo(String appKey, String sessionid);

	void removeSessionInfos(String appKey, String[] sessionid);
	
	void removeAllSession(String appKey,String currentappkey,String currentsessionid);
	boolean deleteApp(String app) throws Exception;
	boolean isMonitorAll();

}
