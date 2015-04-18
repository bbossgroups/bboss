/**
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

package org.frameworkset.gencode.web.entity;

/**
 * <p>
 * Title: GencodeCondition
 * </p>
 * <p>
 * Description: 代码生成管理查询条件实体类
 * </p>
 * <p>
 * bboss
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2015-04-18 20:44:21
 * @author yinbp
 * @version v1.0
 */
public class GencodeCondition implements java.io.Serializable {
	private String tablename;
	private String author;
	private String sortKey;
	private boolean sortDesc;

	public GencodeCondition() {
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getTablename() {
		return tablename;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthor() {
		return author;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}

	public String getSortKey() {
		return sortKey;
	}

	public void setSortDesc(boolean sortDesc) {
		this.sortDesc = sortDesc;
	}

	public boolean getSortDesc() {
		return sortDesc;
	}
}