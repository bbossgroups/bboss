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
package org.frameworkset.mvc;

import java.util.List;


/**
 * <p>Addresses.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-6-20
 * @author biaoping.yin
 * @version 1.0
 */
public class Addresses
{
	private long total;
	private List<Address> rows;
	
	public long getTotal()
	{
	
		return total;
	}
	
	public void setTotal(long total)
	{
	
		this.total = total;
	}
	
	public List<Address> getRows()
	{
	
		return rows;
	}
	
	public void setRows(List<Address> rows)
	{
	
		this.rows = rows;
	}
}
