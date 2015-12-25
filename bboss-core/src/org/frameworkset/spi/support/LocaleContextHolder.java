/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.frameworkset.spi.support;

import java.util.Locale;




/**
 * <p>Title: LocaleContextHolder.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-9-24 下午04:56:17
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class LocaleContextHolder {
	private static final ThreadLocal localeContextHolder = new NamedThreadLocal("Locale context");

	private static final ThreadLocal inheritableLocaleContextHolder =
			new NamedInheritableThreadLocal("Locale context");


	/**
	 * Reset the LocaleContext for the current thread.
	 */
	public static void resetLocaleContext() {
		localeContextHolder.set(null);
		inheritableLocaleContextHolder.set(null);
	}

	/**
	 * Associate the given LocaleContext with the current thread,
	 * <i>not</i> exposing it as inheritable for child threads.
	 * @param localeContext the current LocaleContext, or <code>null</code> to reset
	 * the thread-bound context
	 */
	public static void setLocaleContext(LocaleContext localeContext) {
		setLocaleContext(localeContext, false);
	}

	/**
	 * Associate the given LocaleContext with the current thread.
	 * @param localeContext the current LocaleContext, or <code>null</code> to reset
	 * the thread-bound context
	 * @param inheritable whether to expose the LocaleContext as inheritable
	 * for child threads (using an {@link java.lang.InheritableThreadLocal})
	 */
	public static void setLocaleContext(LocaleContext localeContext, boolean inheritable) {
		if (inheritable) {
			inheritableLocaleContextHolder.set(localeContext);
			localeContextHolder.set(null);
		}
		else {
			localeContextHolder.set(localeContext);
			inheritableLocaleContextHolder.set(null);
		}
	}

	/**
	 * Return the LocaleContext associated with the current thread, if any.
	 * @return the current LocaleContext, or <code>null</code> if none
	 */
	public static LocaleContext getLocaleContext() {
		LocaleContext localeContext = (LocaleContext) localeContextHolder.get();
		if (localeContext == null) {
			localeContext = (LocaleContext) inheritableLocaleContextHolder.get();
		}
		return localeContext;
	}

	/**
	 * Associate the given Locale with the current thread.
	 * <p>Will implicitly create a LocaleContext for the given Locale,
	 * <i>not</i> exposing it as inheritable for child threads.
	 * @param locale the current Locale, or <code>null</code> to reset
	 * the thread-bound context
	 * @see SimpleLocaleContext#SimpleLocaleContext(java.util.Locale)
	 */
	public static void setLocale(Locale locale) {
		setLocale(locale, false);
	}

	/**
	 * Associate the given Locale with the current thread.
	 * <p>Will implicitly create a LocaleContext for the given Locale.
	 * @param locale the current Locale, or <code>null</code> to reset
	 * the thread-bound context
	 * @param inheritable whether to expose the LocaleContext as inheritable
	 * for child threads (using an {@link java.lang.InheritableThreadLocal})
	 * @see SimpleLocaleContext#SimpleLocaleContext(java.util.Locale)
	 */
	public static void setLocale(Locale locale, boolean inheritable) {
		LocaleContext localeContext = (locale != null ? new SimpleLocaleContext(locale) : null);
		setLocaleContext(localeContext, inheritable);
	}

	/**
	 * Return the Locale associated with the current thread, if any,
	 * or the system default Locale else.
	 * @return the current Locale, or the system default Locale if no
	 * specific Locale has been associated with the current thread
	 * @see LocaleContext#getLocale()
	 * @see java.util.Locale#getDefault()
	 */
	public static Locale getLocale() {
		LocaleContext localeContext = getLocaleContext();
		return (localeContext != null ? localeContext.getLocale() : Locale.getDefault());
	}

}
