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
package com.frameworkset.common;

import java.util.Date;

/**
 * 
 * <p>Title: TestNewface.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date Nov 4, 2008 2:51:17 PM
 * @author biaoping.yin
 * @version 1.0
 */
public class TestNewface {
	String OWNER          ;
	String  OBJECT_NAME    ;
	String   SUBOBJECT_NAME   ;
	int   OBJECT_ID     ;
	int  DATA_OBJECT_ID  ;
	String   OBJECT_TYPE     ;
	Date  CREATED          ;
	Date LAST_DDL_TIME    ;
	String  TIMESTAMP        ;
	String  STATUS         ;
	String  TEMPORARY      ;
	String   GENERATED     ;
	String   SECONDARY    ;
	public String getOWNER() {
		return OWNER;
	}
	public void setOWNER(String owner) {
		OWNER = owner;
	}
	public String getOBJECT_NAME() {
		return OBJECT_NAME;
	}
	public void setOBJECT_NAME(String object_name) {
		OBJECT_NAME = object_name;
	}
	public String getSUBOBJECT_NAME() {
		return SUBOBJECT_NAME;
	}
	public void setSUBOBJECT_NAME(String subobject_name) {
		SUBOBJECT_NAME = subobject_name;
	}
	public int getOBJECT_ID() {
		return OBJECT_ID;
	}
	public void setOBJECT_ID(int object_id) {
		OBJECT_ID = object_id;
	}
	public int getDATA_OBJECT_ID() {
		return DATA_OBJECT_ID;
	}
	public void setDATA_OBJECT_ID(int data_object_id) {
		DATA_OBJECT_ID = data_object_id;
	}
	public String getOBJECT_TYPE() {
		return OBJECT_TYPE;
	}
	public void setOBJECT_TYPE(String object_type) {
		OBJECT_TYPE = object_type;
	}
	public Date getCREATED() {
		return CREATED;
	}
	public void setCREATED(Date created) {
		CREATED = created;
	}
	public Date getLAST_DDL_TIME() {
		return LAST_DDL_TIME;
	}
	public void setLAST_DDL_TIME(Date last_ddl_time) {
		LAST_DDL_TIME = last_ddl_time;
	}
	public String getTIMESTAMP() {
		return TIMESTAMP;
	}
	public void setTIMESTAMP(String timestamp) {
		TIMESTAMP = timestamp;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String status) {
		STATUS = status;
	}
	public String getTEMPORARY() {
		return TEMPORARY;
	}
	public void setTEMPORARY(String temporary) {
		TEMPORARY = temporary;
	}
	public String getGENERATED() {
		return GENERATED;
	}
	public void setGENERATED(String generated) {
		GENERATED = generated;
	}
	public String getSECONDARY() {
		return SECONDARY;
	}
	public void setSECONDARY(String secondary) {
		SECONDARY = secondary;
	}
	
	public String toString()
	{
		StringBuffer ret = new StringBuffer();
		ret.append("[OWNER=").append(OWNER).append("]")
		.append("[OBJECT_NAME=").append(OBJECT_NAME).append("]")
		.append("[SUBOBJECT_NAME=").append(SUBOBJECT_NAME).append("]")
		.append("[OBJECT_ID=").append(OBJECT_ID).append("]")
		.append("[DATA_OBJECT_ID=").append(DATA_OBJECT_ID).append("]")
		.append("[OBJECT_TYPE=").append(OBJECT_TYPE).append("]")
		.append("[CREATED=").append(CREATED).append("]")
		.append("[LAST_DDL_TIME=").append(LAST_DDL_TIME).append("]")
		.append("[TIMESTAMP=").append(TIMESTAMP).append("]")
		.append("[STATUS=").append(STATUS).append("]")
		.append("[TEMPORARY=").append(TEMPORARY).append("]")
		.append("[GENERATED=").append(GENERATED).append("]")
		.append("[SECONDARY=").append(SECONDARY).append("]");
		return ret.toString();
	}
}
