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
package com.frameworkset.common.poolman.monitor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AbandonedTraceExt implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String stackid;
	private String label;
	private long createTime;
	private String stackInfo;
	
	private String dburl;
	private AbandonedTraceExt parent;	
	private List<AbandonedTraceExt> traces = new ArrayList<AbandonedTraceExt>();
	
	public AbandonedTraceExt(String id)
	{
		this.stackid = id;
	}
	
	public String getStackid() {
		return stackid;
	}
	public void setStackid(String stackid) {
		this.stackid = stackid;
	}
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getStackInfo() {
		return stackInfo;
	}
	public void setStackInfo(String stackInfo) {
		this.stackInfo = stackInfo;
	}
	public AbandonedTraceExt getParent() {
		return parent;
	}
	public void setParent(AbandonedTraceExt parent) {
		this.parent = parent;
	}
	public List<AbandonedTraceExt> getTraces() {
		return traces;
	}
	public void setTraces(List<AbandonedTraceExt> traces) {
		this.traces = traces;
	}
	public void addTrace(AbandonedTraceExt trace)
	{
		if(trace!=null)
		{
			traces.add(trace);
		}
	}

	public String getDburl() {
		return dburl;
	}

	public void setDburl(String dburl) {
		this.dburl = dburl;
	}
}
