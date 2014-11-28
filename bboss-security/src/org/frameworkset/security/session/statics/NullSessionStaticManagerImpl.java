package org.frameworkset.security.session.statics;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class NullSessionStaticManagerImpl implements SessionStaticManager {

	public NullSessionStaticManagerImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<SessionAPP> getSessionAPP() {
		// TODO Auto-generated method stub
		return null;
	}
	public List<SessionAPP> getSessionAPP(HttpServletRequest request)
	{
		return null;
	}
	public boolean hasMonitorPermission(String app,HttpServletRequest request)
	{
		return false;
	}
	public boolean hasMonitorPermission(String app,String currentapp)
	{
		return false;
	}
	@Override
	public List<SessionInfo> getAllSessionInfos(Map queryParams, int row,
			int page) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SessionInfo getSessionInfo(String appKey, String sessionid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeSessionInfo(String appKey, String sessionid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeSessionInfos(String appKey, String[] sessionid) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeAllSession(String appKey,String currentappkey,String currentsessionid) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean hasDeleteAppPermission(String app, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteApp(String app) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMonitorAll() {
		// TODO Auto-generated method stub
		return false;
	}

}
