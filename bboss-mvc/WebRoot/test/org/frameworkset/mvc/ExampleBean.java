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

import java.util.Date;

import org.frameworkset.util.annotations.RequestParam;


/**
 * <p>ExampleBean.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-6-4
 * @author biaoping.yin
 * @version 1.0
 */
public class ExampleBean
{
	private String id;
	private String name = null;
	private String sex = null;
	@RequestParam(name="favovate${id}")
	private int favovate ;
	
	@RequestParam(dateformat="yyyy-MM-dd")
	private Date testDate ;

	
	public String getName()
	{
	
		return name;
	}

	
	public void setName(String name)
	{
	
		this.name = name;
	}


	
	public String getSex()
	{
	
		return sex;
	}


	
	public void setSex(String sex)
	{
	
		this.sex = sex;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public int getFavovate() {
		return favovate;
	}


	public void setFavovate(int favovate) {
		this.favovate = favovate;
	}

}
