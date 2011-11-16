/**
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
package test.pager;

/**
 * <p>TableInfo.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date Jun 16, 2009
 * @author biaoping.yin
 * @version 1.0
 */
public class TableInfo {
	String TABLE_NAME         ;
	String TABLE_ID_NAME       ;
	int TABLE_ID_INCREMENT  ;
	int TABLE_ID_VALUE      ;
	String TABLE_ID_GENERATOR  ;
	String TABLE_ID_TYPE       ;
	String TABLE_ID_PREFIX     ;
	public String getTABLE_NAME() {
		return TABLE_NAME;
	}
	public void setTABLE_NAME(String table_name) {
		TABLE_NAME = table_name;
	}
	public String getTABLE_ID_NAME() {
		return TABLE_ID_NAME;
	}
	public void setTABLE_ID_NAME(String table_id_name) {
		TABLE_ID_NAME = table_id_name;
	}
	public int getTABLE_ID_INCREMENT() {
		return TABLE_ID_INCREMENT;
	}
	public void setTABLE_ID_INCREMENT(int table_id_increment) {
		TABLE_ID_INCREMENT = table_id_increment;
	}
	public int getTABLE_ID_VALUE() {
		return TABLE_ID_VALUE;
	}
	public void setTABLE_ID_VALUE(int table_id_value) {
		TABLE_ID_VALUE = table_id_value;
	}
	public String getTABLE_ID_GENERATOR() {
		return TABLE_ID_GENERATOR;
	}
	public void setTABLE_ID_GENERATOR(String table_id_generator) {
		TABLE_ID_GENERATOR = table_id_generator;
	}
	public String getTABLE_ID_TYPE() {
		return TABLE_ID_TYPE;
	}
	public void setTABLE_ID_TYPE(String table_id_type) {
		TABLE_ID_TYPE = table_id_type;
	}
	public String getTABLE_ID_PREFIX() {
		return TABLE_ID_PREFIX;
	}
	public void setTABLE_ID_PREFIX(String table_id_prefix) {
		TABLE_ID_PREFIX = table_id_prefix;
	}

}
