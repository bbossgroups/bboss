package org.frameworkset.web.servlet.i18n;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frameworkset.util.SimpleStringUtil;
import com.frameworkset.util.StringUtil;

public class CookieLocaleResolver extends AbstractLocaleResolver{
	public static final String COOKIE_LOCAL_KEY = "org.frameworkset.web.servlet.i18n.COOKIE_LOCAL_KEY";
	private String cookielocalkey = COOKIE_LOCAL_KEY;
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		Locale locale = null;
		if(cookies != null)
		{
			Map<String,Locale> locales = SimpleStringUtil.getAllLocales();			
			for (Cookie temp : cookies) {
				if(cookielocalkey.equals(temp.getName()))
				{
					locale = locales.get(temp.getValue());
					break;
				}
				
			}
		}
		if(locale == null)
			return request.getLocale();
		return locale;
	}

	@Override
	public void setLocale(HttpServletRequest request,
			HttpServletResponse response, Locale locale) {
//		throw new UnsupportedOperationException(
//				"Cannot change HTTP accept header - use a different locale resolution strategy");
		if(locale == null)
			locale = request.getLocale();
		String language = String.valueOf(locale);
		StringUtil.addCookieValue(request, response, cookielocalkey, language);
	}

	public String getCookielocalkey() {
		return cookielocalkey;
	}

	@Override
	public void setLocale(HttpServletRequest request,
			HttpServletResponse response, String locale) {
		if(locale == null)
			locale = String.valueOf(request.getLocale());
		
		StringUtil.addCookieValue(request, response, cookielocalkey, locale);

	}
	public void setCookielocalkey(String cookielocalkey) {
		this.cookielocalkey = cookielocalkey;
	}

}
