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

package org.frameworkset.spi.assemble;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.frameworkset.util.ValueObjectUtil;

/**
 * <p>
 * Title: ProArray.java
 * </p>
 * <p>
 * Description: ��Ҫ������ioc������ע�빦��
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2011-4-11 ����01:30:37
 * @author biaoping.yin
 * @version 1.0
 */
public class ProArray implements Serializable{
	private static final Logger log = Logger.getLogger(ProArray.class);
	private boolean isfreeze = false;
	/**
	 * ����������ͣ��������������ͣ� bean:������ֱ�ӽ���װ��bean���� String��ProArrayת��ΪString[]����
	 * Pro��Ĭ������Pro������ת��������ָ��editor�༭��
	 */
	private String componentType;
	Pro[] pros;
	List<Pro> pros_temp;

	public Pro addPro(Pro pro) {
		if (pros_temp == null)
			pros_temp = new ArrayList<Pro>();
		pros_temp.add(pro);
		return pro;
	}

	public void freeze() {
		this.isfreeze = true;
		if (pros_temp != null) {
			pros = new Pro[this.pros_temp.size()];
			for (int i = 0; i < pros_temp.size(); i++) {
				pros[i] = this.pros_temp.get(i);
			}
			pros_temp = null;
		}
	}

	private boolean isFreeze() {

		return this.isfreeze;
	}

	private void modify() {
		if (this.isFreeze())
			throw new CannotModifyException();
	}

	/**
	 * @return the componentType
	 */
	public String getComponentType() {
		return componentType;
	}

	/**
	 * @param componentType
	 *            the componentType to set
	 */
	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	public Object getObject(int i, Object defaultValue) {
		Pro value = pros[i];

		if (value == null)
			return defaultValue;

		return value.getObject(defaultValue);
	}

	public int getInt(int i, int defaultValue) {
		Pro value = pros[i];
		if (value == null)
			return defaultValue;
		// int value_ = Integer.parseInt(value.toString());
		return value.getInt();
	}

	public Pro<?> getPro(int i) {
		return pros[i];
	}

	public boolean getBoolean(int i) {
		Pro value = pros[i];
		if (value == null)
			return false;
		// boolean value_ = Boolean.parseBoolean(value.toString());
		return value.getBoolean();
	}

	public boolean getBoolean(int i, boolean defaultValue) {
		Pro value = pros[i];
		if (value == null)
			return defaultValue;
		boolean value_ = value.getBoolean(defaultValue);
		return value_;
	}

	public String getString(int i) {
		Pro value = pros[i];
		if (value == null)
			return null;

		return value.getString();
	}

	public String getString(int i, String defaultValue) {
		Pro value = pros[i];

		if (value == null)
			return defaultValue;

		return value.getString(defaultValue);
	}

	public ProList getList(int i, ProList defaultValue) {
		Pro value = pros[i];

		if (value == null)
			return defaultValue;

		return value.getList(defaultValue);
	}

	public ProList getList(int i) {
		Pro value = pros[i];
		if (value == null)
			return null;

		return value.getList();
	}

	public ProSet getSet(int i, ProSet defaultValue) {
		Pro value = pros[i];

		if (value == null)
			return defaultValue;

		return value.getSet(defaultValue);
	}

	public ProSet getSet(int i) {
		Pro value = pros[i];
		if (value == null)
			return null;
		return value.getSet();
	}

	/**
	 * @return the pros
	 */
	public Pro[] getPros() {
		return pros;
	}

	/**
	 * @param pros
	 *            the pros to set
	 */
	public void setPros(Pro[] pros) {
		modify();
		this.pros = pros;
	}

	private Object componentArray;

	public int size() {
		return this.pros != null ? this.pros.length : 0;
	}

	private Object lock = new Object();

	public Object getComponentArray() {
		if (this.getComponentType() == null)
			return this;

		if (componentArray == null) {
			synchronized (lock) {
				if (componentArray == null) {
//					if (this.size() > 0) 
					{
						if (this.componentType.equalsIgnoreCase(Pro.COMPONENT_BEAN)) {
							Class enumType = this.pros[0].getBeanClass();
							// componentArray = new ArrayList(this.size());
							componentArray = Array.newInstance(enumType, this
									.size());
							int i = 0;
							if(pros != null)
							{
								for (Pro v : this.pros) {
									Array.set(componentArray, i, v.getBean());
									i++;
								}
							}
						} else if (this.componentType
								.equalsIgnoreCase(Pro.COMPONENT_STRING_SHORTNAME) || this.componentType
								.equalsIgnoreCase(Pro.COMPONENT_STRING)) {

							// componentArray = new ArrayList(this.size());
							componentArray = Array.newInstance(String.class,
									this.size());
							int i = 0;
							if(pros != null)
							{
								for (Pro v : this.pros) {
								Array.set(componentArray, i, v.getString());
								i++;
								}
							}
						}
						else if (this.componentType.equalsIgnoreCase(Pro.COMPONENT_OBJECT_SHORTNAME) || this.componentType.equalsIgnoreCase(Pro.COMPONENT_OBJECT)) {
							
							// componentArray = new ArrayList(this.size());
							componentArray = Array.newInstance(Object.class, this
									.size());
							int i = 0;
							if(pros != null)
							{
								for (Pro v : this.pros) {
								Array.set(componentArray, i, v.getBean());
								i++;
								}
							}
						}
						else if (this.componentType.equalsIgnoreCase(Pro.COMPONENT_CLASS) ) {
							
							// componentArray = new ArrayList(this.size());
							componentArray = Array.newInstance(Class.class, this
									.size());
							int i = 0;
							if(pros != null)
							{
								for (Pro v : this.pros) {
								Object value = v.getBean();
								Array.set(componentArray, i, ValueObjectUtil.typeCast(value, Class.class));
								i++;
								}
							}
						}						
						else {
							try {
								componentArray = Array.newInstance(ValueObjectUtil.getClass(componentType), this
										.size());
								int i = 0;
								if(pros != null)
								{
									for (Pro v : this.pros) {
									Array.set(componentArray, i, v.getBean());
									i++;
									}
								}
							} catch (NegativeArraySizeException e) {
								log.error(e);
								componentArray = this;
							} catch (ClassNotFoundException e) {
								log.error(e);
								componentArray = this;
							}
							catch (Exception e) {
								log.error(e);
								componentArray = this;
							}
							
							
						}
					}
//					else {
//						componentArray = this;
//					}
				}
			}
		}

		return componentArray;
	}

}