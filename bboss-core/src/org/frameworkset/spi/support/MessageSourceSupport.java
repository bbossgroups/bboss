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

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Title: MessageSourceSupport.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2010-9-24 下午05:05:18
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class MessageSourceSupport {
	/** Logger available to subclasses */
	protected static final Logger logger = LoggerFactory
			.getLogger(MessageSourceSupport.class);

	private boolean alwaysUseMessageFormat = false;

	/**
	 * Cache to hold already generated MessageFormats per message. Used for
	 * passed-in default messages. MessageFormats for resolved codes are cached
	 * on a specific basis in subclasses.
	 */
	private final Map cachedMessageFormats = new ConcurrentHashMap();

	/**
	 * Set whether to always apply the MessageFormat rules, parsing even
	 * messages without arguments.
	 * <p>
	 * Default is "false": Messages without arguments are by default returned
	 * as-is, without parsing them through MessageFormat. Set this to "true" to
	 * enforce MessageFormat for all messages, expecting all message texts to be
	 * written with MessageFormat escaping.
	 * <p>
	 * For example, MessageFormat expects a single quote to be escaped as "''".
	 * If your message texts are all written with such escaping, even when not
	 * defining argument placeholders, you need to set this flag to "true".
	 * Else, only message texts with actual arguments are supposed to be written
	 * with MessageFormat escaping.
	 * 
	 * @see java.text.MessageFormat
	 */
	public void setAlwaysUseMessageFormat(boolean alwaysUseMessageFormat) {
		this.alwaysUseMessageFormat = alwaysUseMessageFormat;
	}

	/**
	 * Return whether to always apply the MessageFormat rules, parsing even
	 * messages without arguments.
	 */
	protected boolean isAlwaysUseMessageFormat() {
		return this.alwaysUseMessageFormat;
	}

	/**
	 * Format the given message String, using cached MessageFormats. By default
	 * invoked for passed-in default messages, to resolve any argument
	 * placeholders found in them.
	 * 
	 * @param msg
	 *            the message to format
	 * @param args
	 *            array of arguments that will be filled in for params within
	 *            the message, or <code>null</code> if none.
	 * @param locale
	 *            the Locale used for formatting
	 * @return the formatted message (with resolved arguments)
	 */
	protected String formatMessage(String msg, Object[] args, Locale locale) {
		if (msg == null
				|| (!this.alwaysUseMessageFormat && (args == null || args.length == 0))) {
			return msg;
		}
		MessageFormat messageFormat = (MessageFormat) this.cachedMessageFormats.get(msg);
		if (messageFormat == null)
		{
			synchronized (this.cachedMessageFormats) {
				messageFormat = (MessageFormat) this.cachedMessageFormats.get(msg);
				if (messageFormat == null) {
					messageFormat = createMessageFormat(msg, locale);
					this.cachedMessageFormats.put(msg, messageFormat);
				}
			}
		}
		synchronized (messageFormat) {
			return messageFormat.format(resolveArguments(args, locale));
		}
	}
	

	/**
	 * Create a MessageFormat for the given message and Locale.
	 * 
	 * @param msg
	 *            the message to create a MessageFormat for
	 * @param locale
	 *            the Locale to create a MessageFormat for
	 * @return the MessageFormat instance
	 */
	protected static MessageFormat createMessageFormat(String msg, Locale locale) {
		if (logger.isDebugEnabled()) {
			logger.debug("Creating MessageFormat for pattern [" + msg
					+ "] and locale '" + locale + "'");
		}
		return new MessageFormat((msg != null ? msg : ""), locale);
	}

	/**
	 * Template method for resolving argument objects.
	 * <p>
	 * The default implementation simply returns the given argument array as-is.
	 * Can be overridden in subclasses in order to resolve special argument
	 * types.
	 * 
	 * @param args
	 *            the original argument array
	 * @param locale
	 *            the Locale to resolve against
	 * @return the resolved argument array
	 */
	protected Object[] resolveArguments(Object[] args, Locale locale) {
		return args;
	}

	/**
	 * Render the given default message String. The default message is passed in
	 * as specified by the caller and can be rendered into a fully formatted
	 * default message shown to the user.
	 * <p>
	 * The default implementation passes the String to
	 * <code>formatMessage</code>, resolving any argument placeholders found in
	 * them. Subclasses may override this method to plug in custom processing of
	 * default messages.
	 * 
	 * @param defaultMessage
	 *            the passed-in default message String
	 * @param args
	 *            array of arguments that will be filled in for params within
	 *            the message, or <code>null</code> if none.
	 * @param locale
	 *            the Locale used for formatting
	 * @return the rendered default message (with resolved arguments)
	 * @see #formatMessage(String, Object[], java.util.Locale)
	 */
	protected String renderDefaultMessage(String defaultMessage, Object[] args,
			Locale locale) {
		return formatMessage(defaultMessage, args, locale);
	}

}
