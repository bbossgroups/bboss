/*
 *  Copyright 2008-2010 biaoping.yin
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

package org.frameworkset.http;

import org.frameworkset.spi.editor.AbstractEditorInf;

import com.frameworkset.util.StringUtil;

/**
 * {@link java.beans.PropertyEditor Editor} for {@link MediaType}
 * descriptors, to automatically convert <code>String</code> specifications
 * (e.g. <code>"text/html"</code>) to <code>MediaType</code> properties.
 *
 * @author Juergen Hoeller
 * @since 3.0
 * @see MediaType
 */
public class MediaTypeEditor extends AbstractEditorInf<MediaType> {

	
	public MediaType getValueFromString(String text) {
		if (StringUtil.hasText(text)) {
			return (MediaType.parseMediaType(text));
		}
		else {
			return null;
		}
	}
	
	public MediaType getValueFromObject(Object text) {
		String t = String.valueOf(text);
		if (StringUtil.hasText(t)) {
			return (MediaType.parseMediaType(t));
		}
		else {
			return null;
		}
	}

	

}
