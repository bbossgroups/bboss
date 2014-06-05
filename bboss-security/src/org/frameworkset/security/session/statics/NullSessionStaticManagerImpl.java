package org.frameworkset.security.session.statics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NullSessionStaticManagerImpl implements SessionStaticManager{

	public NullSessionStaticManagerImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<SessionAPP> getSessionAPP() {
		// TODO Auto-generated method stub
		return new ArrayList<SessionAPP>();
	}

	@Override
	public List<SessionInfo> getSessionInfos(String appkey, long start, long end) {
		// TODO Auto-generated method stub
		return new ArrayList<SessionInfo>();
	}

	@Override
	public List<SessionInfo> getAllSessionInfos(Map params, long start, long end) {
		return new ArrayList<SessionInfo>();
	}

	@Override
	public SessionInfo getSessionInfo(String sessionid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeSessionInfo(String sessionid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeSessionInfos(String[] sessionid) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeAppSessionInfo(String appkey) {
		// TODO Auto-generated method stub
		
	}

}
