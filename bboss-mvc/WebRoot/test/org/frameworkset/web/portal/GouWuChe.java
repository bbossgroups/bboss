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
package org.frameworkset.web.portal;

import java.util.List;



/**
 * <p>BeanContainer.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-6-19
 * @author biaoping.yin
 * @version 1.0
 */
public class GouWuChe
{
	private long total;
	private List<Productor> rows;
	private List<Productor> footer;
	
	public long getTotal()
	{
	
		return total;
	}
	
	public void setTotal(long total)
	{
	
		this.total = total;
	}
	
	public List<Productor> getRows()
	{
	
		return rows;
	}
	
	public void setRows(List<Productor> rows)
	{
	
		this.rows = rows;
	}

	
	public List<Productor> getFooter()
	{
	
		return footer;
	}

	
	public void setFooter(List<Productor> footer)
	{
	
		this.footer = footer;
	}

	
	

}
