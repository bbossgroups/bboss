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

/**
 * <p>Title: MessageSourceResolvable.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-9-24 下午04:53:29
 * @author biaoping.yin
 * @version 1.0
 */
public interface MessageSourceResolvable {
	/**
	 * Return the codes to be used to resolve this message, in the order that
	 * they should get tried. The last code will therefore be the default one.
	 * @return a String array of codes which are associated with this message
	 */
	public String[] getCodes();

	/**
	 * Return the array of arguments to be used to resolve this message.
	 * @return an array of objects to be used as parameters to replace
	 * placeholders within the message text
	 * @see java.text.MessageFormat
	 */
	public Object[] getArguments();

	/**
	 * Return the default message to be used to resolve this message.
	 * @return the default message, or <code>null</code> if no default
	 */
	public String getDefaultMessage();

}
