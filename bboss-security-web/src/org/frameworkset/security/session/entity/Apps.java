package org.frameworkset.security.session.entity;

import java.util.List;

import org.frameworkset.security.session.statics.AttributeInfo;
import org.frameworkset.security.session.statics.SessionAPP;

public class Apps {
	private List<SessionAPP> apps;
	private AttributeInfo[] extendAttributes;
	public Apps() {
		// TODO Auto-generated constructor stub
	}
	public List<SessionAPP> getApps() {
		return apps;
	}
	public void setApps(List<SessionAPP> apps) {
		this.apps = apps;
	}
	public AttributeInfo[] getExtendAttributes() {
		return extendAttributes;
	}
	public void setExtendAttributes(AttributeInfo[] extendAttributes) {
		this.extendAttributes = extendAttributes;
	}

}
