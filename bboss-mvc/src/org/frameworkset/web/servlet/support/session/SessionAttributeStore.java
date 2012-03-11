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
package org.frameworkset.web.servlet.support.session;

import org.frameworkset.web.servlet.mvc.WebRequest;

/**
 * <p>Title: SessionAttributeStore.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-24
 * @author biaoping.yin
 * @version 1.0
 */
public interface SessionAttributeStore {
	
	/**
	 * Store the supplied attribute in the backend session.
	 * <p>Can be called for new attributes as well as for existing attributes.
	 * In the latter case, this signals that the attribute value may have been modified.
	 * @param request the current request
	 * @param attributeName the name of the attribute
	 * @param attributeValue the attribute value to store
	 */
	void storeAttribute(WebRequest request, String attributeName, Object attributeValue);

	/**
	 * Retrieve the specified attribute from the backend session.
	 * <p>This will typically be called with the expectation that the
	 * attribute is already present, with an exception to be thrown
	 * if this method returns <code>null</code>.
	 * @param request the current request
	 * @param attributeName the name of the attribute
	 * @return the current attribute value, or <code>null</code> if none
	 */
	Object retrieveAttribute(WebRequest request, String attributeName);

	/**
	 * Clean up the specified attribute in the backend session.
	 * <p>Indicates that the attribute name will not be used anymore.
	 * @param request the current request
	 * @param attributeName the name of the attribute
	 */
	void cleanupAttribute(WebRequest request, String attributeName);

}
