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


/**
 * <p>Productor.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-6-19
 * @author biaoping.yin
 * @version 1.0
 */
public class Productor
{
	private String productid;
	private double unitcost;
	private String status;
	private double listprice;
	private String attr1;
	private String itemid;
	
	public String getProductid()
	{
	
		return productid;
	}
	
	public void setProductid(String productid)
	{
	
		this.productid = productid;
	}

	
	public double getUnitcost()
	{
	
		return unitcost;
	}

	
	public void setUnitcost(double unitcost)
	{
	
		this.unitcost = unitcost;
	}

	
	public String getStatus()
	{
	
		return status;
	}

	
	public void setStatus(String status)
	{
	
		this.status = status;
	}

	
	public double getListprice()
	{
	
		return listprice;
	}

	
	public void setListprice(double listprice)
	{
	
		this.listprice = listprice;
	}

	
	

	
	public String getItemid()
	{
	
		return itemid;
	}

	
	public void setItemid(String itemid)
	{
	
		this.itemid = itemid;
	}

	
	public String getAttr1()
	{
	
		return attr1;
	}

	
	public void setAttr1(String attr1)
	{
	
		this.attr1 = attr1;
	}
}
