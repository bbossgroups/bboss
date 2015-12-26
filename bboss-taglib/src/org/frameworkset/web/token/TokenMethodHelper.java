package org.frameworkset.web.token;

import java.lang.reflect.Method;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public abstract class TokenMethodHelper {
	public static Method isEnableToken;
	public static Method doDTokencheck;
//	buildDToken(String elementType, String jsonsplit,
//			HttpServletRequest request, String fid, boolean cache)
	public static Method buildDToken;
	private static final Logger logger = Logger.getLogger(TokenMethodHelper.class);
	static 
	{
		Class clazz = null;
		try {
			clazz = Class.forName("org.frameworkset.web.token.TokenHelper");
			isEnableToken = clazz.getMethod("isEnableToken");
		} catch (ClassNotFoundException e) {
			logger.info("get isEnableToken method from org.frameworkset.web.token.TokenHelper failed:",e);
		} catch (NoSuchMethodException e) {
			logger.info("get isEnableToken method from org.frameworkset.web.token.TokenHelper failed:",e);
		} catch (Exception e) {
			logger.info("get isEnableToken method from org.frameworkset.web.token.TokenHelper failed:",e);
		}
		
		try {
			if(clazz == null)
				clazz = Class.forName("org.frameworkset.web.token.TokenHelper");
			doDTokencheck = clazz.getMethod("doDTokencheck",ServletRequest.class,
					ServletResponse.class);
		} catch (ClassNotFoundException e) {
			logger.info("get doDTokencheck method from org.frameworkset.web.token.TokenHelper failed:",e);
		} catch (NoSuchMethodException e) {
			logger.info("get doDTokencheck method from org.frameworkset.web.token.TokenHelper failed:",e);
		} catch (Exception e) {
			logger.info("get doDTokencheck method from org.frameworkset.web.token.TokenHelper failed:",e);
		}
		try {
			if(clazz == null)
				clazz = Class.forName("org.frameworkset.web.token.TokenHelper");
			buildDToken = clazz.getMethod("buildDToken",String.class, String.class,
					HttpServletRequest.class, String.class, boolean.class);
		} catch (ClassNotFoundException e) {
			logger.info("get buildDToken method from org.frameworkset.web.token.TokenHelper failed:",e);
		} catch (NoSuchMethodException e) {
			logger.info("get buildDToken method from org.frameworkset.web.token.TokenHelper failed:",e);
		} catch (Exception e) {
			logger.info("get buildDToken method from org.frameworkset.web.token.TokenHelper failed:",e);
		}
	}
	public TokenMethodHelper() {
		// TODO Auto-generated constructor stub
	}

}
