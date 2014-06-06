package org.frameworkset.security.session.statics;

import java.util.List;
import java.util.Map;

public interface SessionStaticManager {

	/**
	 * 获取应用概览列表数据
	 * 
	 * @return 2014年6月5日
	 */
	List<SessionAPP> getSessionAPP();

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
	
	void removeAllSession(String appKey);

}
