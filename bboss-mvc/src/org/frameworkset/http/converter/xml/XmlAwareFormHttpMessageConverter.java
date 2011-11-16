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

package org.frameworkset.http.converter.xml;

import org.frameworkset.http.converter.FormHttpMessageConverter;

/**
 * Extension of {@link converter.FormHttpMessageConverter},
 * adding support for XML-based parts through a {@link SourceHttpMessageConverter}.
 *
 * @author Juergen Hoeller
 * @since 3.0.3
 */
public class XmlAwareFormHttpMessageConverter extends FormHttpMessageConverter {

	public XmlAwareFormHttpMessageConverter() {
		super();
		addPartConverter(new SourceHttpMessageConverter());
	}

}
