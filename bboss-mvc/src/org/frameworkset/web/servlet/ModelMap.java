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
package org.frameworkset.web.servlet;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.frameworkset.spi.support.validate.Errors;
import org.frameworkset.util.Assert;
import org.frameworkset.util.Conventions;
import org.frameworkset.web.bind.BindingResultImpl;

/**
 * <p>Title: ModelMap.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-26
 * @author biaoping.yin
 * @version 1.0
 */
public class ModelMap extends LinkedHashMap {
	private Errors errors;
	
//	private ListInfo pageDatas;
	
	/**
	 * Construct a new, empty <code>ModelMap</code>.
	 */
	public ModelMap() {
	}

	/**
	 * Construct a new <code>ModelMap</code> containing the supplied attribute
	 * under the supplied name.
	 * @see #addAttribute(String, Object)
	 */
	public ModelMap(String attributeName, Object attributeValue) {
		addAttribute(attributeName, attributeValue);
	}

	/**
	 * Construct a new <code>ModelMap</code> containing the supplied attribute.
	 * Uses attribute name generation to generate the key for the supplied model
	 * object.
	 * @see #addAttribute(Object)
	 */
	public ModelMap(Object attributeValue) {
		addAttribute(attributeValue);
	}


	/**
	 * Add the supplied attribute under the supplied name.
	 * @param attributeName the name of the model attribute (never <code>null</code>)
	 * @param attributeValue the model attribute value (can be <code>null</code>)
	 */
	public ModelMap addAttribute(String attributeName, Object attributeValue) {
		Assert.notNull(attributeName, "Model attribute name must not be null");
		put(attributeName, attributeValue);
		return this;
	}

	/**
	 * Add the supplied attribute to this <code>Map</code> using a
	 * {@link Conventions#getVariableName generated name}.
	 * <p/><emphasis>Note: Empty {@link Collection Collections} are not added to
	 * the model when using this method because we cannot correctly determine
	 * the true convention name. View code should check for <code>null</code> rather
	 * than for empty collections as is already done by JSTL tags.</emphasis>
	 * @param attributeValue the model attribute value (never <code>null</code>)
	 */
	public ModelMap addAttribute(Object attributeValue) {
		Assert.notNull(attributeValue, "Model object must not be null");
		if (attributeValue instanceof Collection && ((Collection) attributeValue).isEmpty()) {
			return this;
		}
		return addAttribute(Conventions.getVariableName(attributeValue), attributeValue);
	}

	/**
	 * Copy all attributes in the supplied <code>Collection</code> into this
	 * <code>Map</code>, using attribute name generation for each element.
	 * @see #addAttribute(Object)
	 */
	public ModelMap addAllAttributes(Collection attributeValues) {
		if (attributeValues != null) {
			for (Iterator it = attributeValues.iterator(); it.hasNext();) {
				addAttribute(it.next());
			}
		}
		return this;
	}

	/**
	 * Copy all attributes in the supplied <code>Map</code> into this <code>Map</code>.
	 * @see #addAttribute(String, Object)
	 */
	public ModelMap addAllAttributes(Map attributes) {
		if (attributes != null) {
			putAll(attributes);
		}
		return this;
	}

	/**
	 * Copy all attributes in the supplied <code>Map</code> into this <code>Map</code>,
	 * with existing objects of the same name taking precedence (i.e. not getting
	 * replaced).
	 */
	public ModelMap mergeAttributes(Map attributes) {
		if (attributes != null) {
			for (Iterator it = attributes.keySet().iterator(); it.hasNext();) {
				Object key = it.next();
				if (!containsKey(key)) {
					put(key, attributes.get(key));
				}
			}
		}
		
		if(attributes instanceof ModelMap)
		{
			addErrors((ModelMap)attributes);
		}
		return this;
	}

	/**
	 * Does this model contain an attribute of the given name?
	 * @param attributeName the name of the model attribute (never <code>null</code>)
	 * @return whether this model contains a corresponding attribute
	 */
	public boolean containsAttribute(String attributeName) {
		return containsKey(attributeName);
	}


	/**
	 * @deprecated as of Bboss 2.5, in favor of {@link #addAttribute(String, Object)}
	 */
	public ModelMap addObject(String modelName, Object modelObject) {
		return addAttribute(modelName, modelObject);
	}

	/**
	 * @deprecated as of Bboss 2.5, in favor of {@link #addAttribute(Object)}
	 */
	public ModelMap addObject(Object modelObject) {
		return addAttribute(modelObject);
	}

	/**
	 * @deprecated as of Bboss 2.5, in favor of {@link #addAllAttributes(Collection)}
	 */
	public ModelMap addAllObjects(Collection objects) {
		return addAllAttributes(objects);
	}

	/**
	 * @deprecated as of Bboss 2.5, in favor of {@link #addAllAttributes(Map)}
	 */
	public ModelMap addAllObjects(Map objects) {
		return addAllAttributes(objects);
	} 
	
	public boolean hasErrors()
	{
		return errors != null && errors.hasErrors();
	}

	public Errors getErrors() {
		if(errors == null)
			errors = new BindingResultImpl(null);
		return errors;
	}

	/**
	 * @param modelMap
	 */
	public void addErrors(ModelMap modelMap) {
		if(modelMap != null && modelMap.hasErrors())
			this.getErrors().addAllErrors(modelMap.getErrors());
		
		
	}
	
	/**
	 * @param modelMap
	 */
	public void addErrors(Errors errors) {
		if(errors != null)
			this.getErrors().addAllErrors(errors);
		
		
	}

//	/**
//	 * @return the pageDatas
//	 */
//	public ListInfo getPageDatas() {
//		return pageDatas;
//	}
//
//	/**
//	 * @param pageDatas the pageDatas to set
//	 */
//	public void setPageDatas(ListInfo pageDatas) {
//		this.pageDatas = pageDatas;
//	}
//	
	
	
	

}
