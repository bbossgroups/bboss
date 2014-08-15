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
package org.frameworkset.spi.support.validate;

import org.frameworkset.spi.support.DefaultMessageSourceResolvable;
import org.frameworkset.util.Assert;

/**
 * <p>Title: ObjectError.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-28
 * @author biaoping.yin
 * @version 1.0
 */
public class ObjectError  extends DefaultMessageSourceResolvable {

	private final String objectName;
	


	/**
	 * Create a new instance of the ObjectError class.
	 * @param objectName the name of the affected object
	 * @param defaultMessage the default message to be used to resolve this message
	 */
	public ObjectError(String objectName, String defaultMessage) {
		this(objectName, null, null, defaultMessage);
	}

	/**
	 * Create a new instance of the ObjectError class.
	 * @param objectName the name of the affected object
	 * @param codes the codes to be used to resolve this message
	 * @param arguments	the array of arguments to be used to resolve this message
	 * @param defaultMessage the default message to be used to resolve this message
	 */
	public ObjectError(String objectName, String[] codes, Object[] arguments, String defaultMessage) {
		super(codes, arguments, defaultMessage);
		Assert.notNull(objectName, "Object name must not be null");
		this.objectName = objectName;
	}


	/**
	 * Return the name of the affected object.
	 */
	public String getObjectName() {
		return this.objectName;
	}


	public String toString() {
		return "Error in object '" + this.objectName + "': " + resolvableToString();
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(getClass().equals(other.getClass())) || !super.equals(other)) {
			return false;
		}
		ObjectError otherError = (ObjectError) other;
		return getObjectName().equals(otherError.getObjectName());
	}

	public int hashCode() {
		return super.hashCode() * 29 + getObjectName().hashCode();
	}

	

}
