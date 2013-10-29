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
package org.frameworkset.soa.xblink;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * <p>Person.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-10-10
 * @author biaoping.yin
 * @version 1.0
 */
public class Person
{
	private String firstname;   
    private String lastname = "ssss"; 
    private Date birthdate;
    
    private Date updatedate[];
    private PhoneNumber phone;   
    private PhoneNumber fax;
    private Set dataSet;
    private List dataList;
    private Map dataMap;
    private String[] dataArray;
    private EnumData sex;
    
    private String[][] dataDoubleArray ;
	
	public String getFirstname()
	{
	
		return firstname;
	}
	
	public void setFirstname(String firstname)
	{
	
		this.firstname = firstname;
	}
	
	public String getLastname()
	{
	
		return lastname;
	}
	
	public void setLastname(String lastname)
	{
	
		this.lastname = lastname;
	}
	
	public PhoneNumber getPhone()
	{
	
		return phone;
	}
	
	public void setPhone(PhoneNumber phone)
	{
	
		this.phone = phone;
	}
	
	public PhoneNumber getFax()
	{
	
		return fax;
	}
	
	public void setFax(PhoneNumber fax)
	{
	
		this.fax = fax;
	}

	
	public Set getDataSet()
	{
	
		return dataSet;
	}

	
	public void setDataSet(Set dataSet)
	{
	
		this.dataSet = dataSet;
	}

	
	public List getDataList()
	{
	
		return dataList;
	}

	
	public void setDataList(List dataList)
	{
	
		this.dataList = dataList;
	}

	
	public Map getDataMap()
	{
	
		return dataMap;
	}

	
	public void setDataMap(Map dataMap)
	{
	
		this.dataMap = dataMap;
	}

	
	public String[] getDataArray()
	{
	
		return dataArray;
	}

	
	public void setDataArray(String[] dataArray)
	{
	
		this.dataArray = dataArray;
	}

	
	public Date getBirthdate()
	{
	
		return birthdate;
	}

	
	public void setBirthdate(Date birthdate)
	{
	
		this.birthdate = birthdate;
	}

	
	public Date[] getUpdatedate()
	{
	
		return updatedate;
	}

	
	public void setUpdatedate(Date[] updatedate)
	{
	
		this.updatedate = updatedate;
	}

	
	public String[][] getDataDoubleArray()
	{
	
		return dataDoubleArray;
	}

	
	public void setDataDoubleArray(String[][] dataDoubleArray)
	{
	
		this.dataDoubleArray = dataDoubleArray;
	}

	
	public EnumData getSex()
	{
	
		return sex;
	}

	
	public void setSex(EnumData sex)
	{
	
		this.sex = sex;
	} 
}
