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
 * <p>Title: DelegatingMessageSource.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-10-10 下午07:09:37
 * @author biaoping.yin
 * @version 1.0
 */
public class DelegatingMessageSource  extends MessageSourceSupport implements HierarchicalMessageSource {

	private MessageSource parentMessageSource;


	public void setParentMessageSource(MessageSource parent) {
		this.parentMessageSource = parent;
	}

	public MessageSource getParentMessageSource() {
		return this.parentMessageSource;
	}


	public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
		if (this.parentMessageSource != null) {
			return this.parentMessageSource.getMessage(code, args, defaultMessage, locale);
		}
		else {
			return renderDefaultMessage(defaultMessage, args, locale);
		}
	}

	public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
		if (this.parentMessageSource != null) {
			return this.parentMessageSource.getMessage(code, args, locale);
		}
		else {
			throw new NoSuchMessageException(code, locale);
		}
	}

	public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
		if (this.parentMessageSource != null) {
			return this.parentMessageSource.getMessage(resolvable, locale);
		}
		else {
			if (resolvable.getDefaultMessage() != null) {
				return renderDefaultMessage(resolvable.getDefaultMessage(), resolvable.getArguments(), locale);
			}
			String[] codes = resolvable.getCodes();
			String code = (codes != null && codes.length > 0 ? codes[0] : null);
			throw new NoSuchMessageException(code, locale);
		}
	}

	public String getMessage(String code) throws NoSuchMessageException {
		if (this.parentMessageSource != null) {
			return this.parentMessageSource.getMessage(code);
		}
		else {
			throw new NoSuchMessageException(code);
		}
	}

	public String getMessage(String code, Locale locale)
			throws NoSuchMessageException {
		if (this.parentMessageSource != null) {
			return this.parentMessageSource.getMessage(code,locale);
		}
		else {
			throw new NoSuchMessageException(code,locale);
		}
	}


}
