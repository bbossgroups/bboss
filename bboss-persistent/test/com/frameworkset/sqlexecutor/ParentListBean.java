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
package com.frameworkset.sqlexecutor;

import com.frameworkset.orm.annotation.PrimaryKey;


public class ParentListBean
{
//	@PrimaryKey(pkname="ListBean",auto=true)
	private int id ;
	private String fieldName;
//	@Column(name="fileNamezzz",dataformat="格式转换" ,type="clob")
	private String fieldLable;
	private String fieldType;
	private String sortorder;
	private boolean isprimaryKey;
	private boolean required;
	private int fieldLength;
	private int isvalidated;
	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * @param fieldName the fieldName to set
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	/**
	 * @return the fieldLable
	 */
	public String getFieldLable() {
		return fieldLable;
	}
	/**
	 * @param fieldLable the fieldLable to set
	 */
	public void setFieldLable(String fieldLable) {
		this.fieldLable = fieldLable;
	}
	/**
	 * @return the fieldType
	 */
	public String getFieldType() {
		return fieldType;
	}
	/**
	 * @param fieldType the fieldType to set
	 */
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}
	
	/**
	 * @return the isprimaryKey
	 */
	public boolean isIsprimaryKey() {
		return isprimaryKey;
	}
	/**
	 * @param isprimaryKey the isprimaryKey to set
	 */
	public void setIsprimaryKey(boolean isprimaryKey) {
		this.isprimaryKey = isprimaryKey;
	}
	/**
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}
	/**
	 * @param required the required to set
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}
	/**
	 * @return the fieldLength
	 */
	public int getFieldLength() {
		return fieldLength;
	}
	/**
	 * @param fieldLength the fieldLength to set
	 */
	public void setFieldLength(int fieldLength) {
		this.fieldLength = fieldLength;
	}
	/**
	 * @return the isvalidated
	 */
	public int getIsvalidated() {
		return isvalidated;
	}
	/**
	 * @param isvalidated the isvalidated to set
	 */
	public void setIsvalidated(int isvalidated) {
		this.isvalidated = isvalidated;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the sortorder
	 */
	public String getSortorder() {
		return sortorder;
	}
	/**
	 * @param sortorder the sortorder to set
	 */
	public void setSortorder(String sortorder) {
		this.sortorder = sortorder;
	}
	
}
