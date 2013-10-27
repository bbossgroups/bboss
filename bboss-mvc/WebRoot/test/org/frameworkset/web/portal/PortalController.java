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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frameworkset.util.annotations.ResponseBody;


/**
 * <p>PortalController.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 
 * @Date 2011-6-19
 * @author biaoping.yin
 * @version 1.0
 */
public class PortalController
{
	public String portal()
	{
		return "path:portalidx";
	}
	
	public @ResponseBody(datatype="json") GouWuChe datagrid_data()
	{
		GouWuChe container = new GouWuChe();
		container.setTotal(28);
		List<Productor> rows = new ArrayList<Productor>();
		for(int i = 0; i < 28; i ++)
			rows.add( buildProductor(i));
		container.setRows(rows);
		
		
		container.setFooter(buildFooterProductor());
		//footer.put("Average", 18);
		return container;
	}
	
	private Productor buildProductor(int i)
	{
		Productor p = new Productor();
		p.setProductid("FI-SW-0" + i);
		p.setUnitcost(10.00 + i);
		p.setStatus("P");
		p.setListprice(16.50 + i);
		if(i % 3 == 0)
			p.setAttr1("桃子");
		else if(i % 3 == 1)
			p.setAttr1("火龙果");
		if(i % 3 == 2)
			p.setAttr1("芒果");	
		p.setItemid("EST-" + i);
		return p;
	}
	
	private List<Productor> buildFooterProductor()
	{
		Productor p = new Productor();
		p.setProductid("Average:" );
		p.setUnitcost(19.80);
		
		p.setListprice(60.40);
		
		
		List<Productor> footer = new ArrayList<Productor>();
		footer.add(p);
		p = new Productor();
		p.setProductid("Total:" );
		p.setUnitcost(198.00);
		
		p.setListprice(604.00);
		footer.add(p);
		return footer;
	}
}