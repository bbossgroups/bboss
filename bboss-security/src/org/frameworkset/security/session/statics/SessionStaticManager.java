package org.frameworkset.security.session.statics;

import java.util.List;
import java.util.Map;

public interface SessionStaticManager {

	List<SessionAPP> getSessionAPP();
	List<SessionInfo> getSessionInfos(String appkey,long start,long end);
	/**
	 * 
	 * @param params appkey,hostid,referip,createtime(开始时间和结束时间)
	 * @param start
	 * @param end
	 * @return
	 */
	List<SessionInfo> getAllSessionInfos(Map params,long start,long end);
	SessionInfo getSessionInfo(String sessionid);
	void removeSessionInfo(String sessionid);
	void removeSessionInfos(String[] sessionid);
	void removeAppSessionInfo(String appkey);

}
