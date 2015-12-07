package org.frameworkset.security.session.impl;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

@SuppressWarnings("deprecation")
public class SimpleHttpSessionContext implements HttpSessionContext {

	public SimpleHttpSessionContext() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Enumeration getIds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpSession getSession(String sessionid) {
		// TODO Auto-generated method stub
		return null;
	}

}
