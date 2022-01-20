package org.frameworkset.bulk;
/**
 * Copyright 2020 bboss
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2022/1/19 15:20
 * @author biaoping.yin
 * @version 1.0
 */
public class CommonBulkData {
	public static final int INSERT = 0;
	public static final int UPDATE = 1;
	public static final int DELETE = 2;



	/**
	 * 0 - insert
	 * 1 - update
	 * 2 - delete
	 */
	private int type;
	private Object data;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
