package org.frameworkset.web.servlet.i18n;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.web.servlet.LocaleResolver;

public abstract class AbstractLocaleResolver implements LocaleResolver {

	
	@Override
	public void setLocale(HttpServletRequest request,
			HttpServletResponse response, Locale locale) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLocale(HttpServletRequest request,
			HttpServletResponse response, String locale) {
		// TODO Auto-generated method stub

	}
	
	public String resolveLocaleCode(HttpServletRequest request)
	{
		Locale locale = this.resolveLocale(request);
		return String.valueOf(locale);
	}

}
