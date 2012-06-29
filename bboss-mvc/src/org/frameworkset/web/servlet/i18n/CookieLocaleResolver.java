package org.frameworkset.web.servlet.i18n;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.web.servlet.LocaleResolver;

import com.frameworkset.util.SimpleStringUtil;

public class CookieLocaleResolver implements LocaleResolver{
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
		throw new UnsupportedOperationException(
				"Cannot change HTTP accept header - use a different locale resolution strategy");
		
	}

	public String getCookielocalkey() {
		return cookielocalkey;
	}

	public void setCookielocalkey(String cookielocalkey) {
		this.cookielocalkey = cookielocalkey;
	}

}
