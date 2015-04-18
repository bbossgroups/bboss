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

import java.sql.Timestamp;
import com.frameworkset.orm.annotation.Column;
import com.frameworkset.orm.annotation.PrimaryKey;

/**
 * <p>
 * Title: Gencode
 * </p>
 * <p>
 * Description: 代码生成管理服务实体类
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
public class Gencode implements java.io.Serializable {
	@PrimaryKey
	private String id;
	private String author;
	private String company;
	@Column(type = "clob")
	private String controlparams;
	private Timestamp createtime;
	private String dbname;
	@Column(type = "clob")
	private String fieldinfos;
	private String tablename;
	private Timestamp updatetime;

	public Gencode() {
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthor() {
		return author;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompany() {
		return company;
	}

	public void setControlparams(String controlparams) {
		this.controlparams = controlparams;
	}

	public String getControlparams() {
		return controlparams;
	}

	public void setCreatetime(Timestamp createtime) {
		this.createtime = createtime;
	}

	public Timestamp getCreatetime() {
		return createtime;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getDbname() {
		return dbname;
	}

	public void setFieldinfos(String fieldinfos) {
		this.fieldinfos = fieldinfos;
	}

	public String getFieldinfos() {
		return fieldinfos;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getTablename() {
		return tablename;
	}

	public void setUpdatetime(Timestamp updatetime) {
		this.updatetime = updatetime;
	}

	public Timestamp getUpdatetime() {
		return updatetime;
	}
}