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
package org.frameworkset.web.search;

/**
 * <p>Title: DocumentInfo.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-11-26
 * @author biaoping.yin
 * @version 1.0
 */
public class DocumentInfo {
	private String doctitle;
	public String getDoctitle() {
	return doctitle;
	}
	public void setDoctitle(String doctitle) {
	this.doctitle = doctitle;
	}
	public String getSummary() {
	return summary;
	}
	public void setSummary(String summary) {
	this.summary = summary;
	}
	public String getUrl() {
	return url;
	}
	public void setUrl(String url) {
	this.url = url;
	}
	private String summary;
	private String url;
	}
