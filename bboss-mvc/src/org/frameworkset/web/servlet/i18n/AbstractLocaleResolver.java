package org.frameworkset.web.servlet.i18n;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.web.servlet.LocaleResolver;

import com.frameworkset.util.SimpleStringUtil;
import com.frameworkset.util.StringUtil;

public abstract class AbstractLocaleResolver implements LocaleResolver {

	protected Locale defaultLocal = Locale.SIMPLIFIED_CHINESE;
	protected String defaultLanguage = "zh_CN";
	
	public String getDefaultLanguage() {
		return defaultLanguage;
	}

	public void setDefaultLanguage(String defaultLanguage) {
		if(!StringUtil.isEmpty(defaultLanguage))
		{
			this.defaultLanguage = defaultLanguage;
			Map<String,Locale> locales = SimpleStringUtil.getAllLocales();		
			defaultLocal = locales.get(defaultLanguage);
			if(defaultLocal == null)
				throw new java.lang.IllegalArgumentException("未知语言:"+defaultLanguage);
		}
	}

	public Locale getDefaultLocal() {
		return defaultLocal;
	}

	public void setDefaultLocal(Locale defaultLocal) {
		this.defaultLocal = defaultLocal;
	}

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
