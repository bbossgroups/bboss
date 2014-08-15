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
 * <p>Title: HierarchicalMessageSource.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-9-24 下午10:48:46
 * @author biaoping.yin
 * @version 1.0
 */
public interface HierarchicalMessageSource extends MessageSource {
	
	/** 
	 * Set the parent that will be used to try to resolve messages
	 * that this object can't resolve.
	 * @param parent the parent MessageSource that will be used to
	 * resolve messages that this object can't resolve.
	 * May be <code>null</code>, in which case no further resolution is possible.
	 */
	void setParentMessageSource(MessageSource parent);

	/**
	 * Return the parent of this MessageSource, or <code>null</code> if none.
	 */
	MessageSource getParentMessageSource();

}
