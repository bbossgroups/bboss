package org.frameworkset.security.session.statics;

import java.util.List;
import java.util.Map;

public class NullSessionStaticManagerImpl implements SessionStaticManager {

	public NullSessionStaticManagerImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<SessionAPP> getSessionAPP() {
		// TODO Auto-generated method stub
		return null;
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
	public void removeAllSession(String appKey) {
		// TODO Auto-generated method stub

	}

}
