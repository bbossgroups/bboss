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
package org.frameworkset.util.annotations;


/**
 * <p>Title: ValueConstants.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-11-22
 * @author biaoping.yin
 * @version 1.0
 */
public interface ValueConstants {

	/**
	 * Constant defining a value for no default - as a replacement for
	 * <code>null</code> which we cannot use in annotation attributes.
	 * <p>This is an artificial arrangement of 16 unicode characters,
	 * with its sole purpose being to never match user-declared values.
	 * @see RequestParam#defaultValue()
	 * @see RequestHeader#defaultValue()
	 * @see CookieValue#defaultValue()
	 */
	String DEFAULT_NONE = "\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n";
	public static final String datatype_string = "String"; 
	public static final String datatype_file = "file"; 
	public static final String datatype_bytearray = "bytearray"; 
	public static final String datatype_rss = "rss"; 
	public static final String datatype_atom = "atom"; 
	public static final String datatype_json = "json"; 
	public static final String datatype_jsonp = "jsonp";
	public static final String datatype_xml = "xml";
	public static final String datatype_word = "word";
}
