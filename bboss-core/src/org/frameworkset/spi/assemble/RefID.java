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

import java.util.List;

/**
 * <p>Title: RefID.java</p> 
 * <p>Description: 保存引用标识结构信息</p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2012-1-2 下午8:12:14
 * @author biaoping.yin
 * @version 1.0
 */
public class RefID {
	public static class Index
	{
//		private Object index;
		private int int_idx = -1;
		private String string_idx;
		
		private boolean inconstruction;
		public Index(int index, boolean inconstruction) {
			super();
			this.int_idx = index;
			this.inconstruction = inconstruction;
		}
		public Index(String index, boolean inconstruction) {
			super();
			this.string_idx = index;
			this.inconstruction = inconstruction;
		}
		
		public boolean isInconstruction() {
			return inconstruction;
		}
		public int getInt_idx() {
			return int_idx;
		}
		public String getString_idx() {
			return string_idx;
		}
	}
	private String name;
	/**
	 * attr:test1->test2
	 * attr:test1[0]
	 * attr:test1[key]
	 * 
	 */
	private RefID parent;
	private RefID next;
//	private String key;
	private List<Index> indexs ;
	

//	private boolean container;
	public RefID getParent() {
		return parent;
	}
	public void setParent(RefID parent) {
		this.parent = parent;
	}
	public RefID getNext() {
		return next;
	}
	public void setNext(RefID next) {
		this.next = next;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List getIndexs() {
		return indexs;
	}
	public void setIndexs(List indexs) {
		this.indexs = indexs;
	}
	
	

	
	
	

}
