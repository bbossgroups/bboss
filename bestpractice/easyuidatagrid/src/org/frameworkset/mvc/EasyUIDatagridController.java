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

import java.util.ArrayList;
import java.util.List;

import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.util.annotations.ResponseBody;


/**
 * <p>PortalController.java</p>
 * <p> Description: </p>
 * <p> bboss workgroup </p>
 * <p> Copyright (c) 2009 </p>
 * 本文介绍bbossgroups mvc结合jquery easyui datagrid的一个简单案例：

1.通过bbossgroups 控制器方法返回对象，然后mvc框架将对象转换为json对象返回给jquery easyui datagrid展示
2.jquery easyui datagrid通过指定对应控制器方法的请求url来获取datagrid的数据，然后展示出来

下面看控制器的实现：
[code="java"][/code]
 * @Date 2011-6-19
 * @author biaoping.yin
 * @version 1.0
 */
public class EasyUIDatagridController
{
	private static List<Address> addresses ;
	static 
	{
		addresses = new ArrayList<Address>();
		for(int i = 0; i < 10000; i ++)
		{
			Address address = new Address();
			address.setAddr("地址-" + i);
			address.setCode("代码-" + i);
			address.setCol4("col4 数据-" + i);
			address.setName("名称" + i);
			addresses.add(address);
		}
	}
	public String index()
	{
		return "path:portal";
	}
	
	public @ResponseBody(datatype="json") Addresses datagrid_data_pagine(@RequestParam(name="page",defaultvalue="1") int page,@RequestParam(name="rows",defaultvalue="10") int rows)
	{
		return pagerList(addresses,(page - 1)*rows,rows);
		
	}
	public @ResponseBody(datatype="json") GouWuChe datagrid_data_footer()
	{
		GouWuChe container = new GouWuChe();
		container.setTotal(28);
		List<Productor> rows = new ArrayList<Productor>();
		for(int i = 0; i < 28; i ++)
			rows.add( buildProductor(i));
		container.setRows(rows);
		container.setFooter(buildFooterProductor());
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
	/**
	 * 对列表进行分页操作,数据源是一个列表
	 * @param datas 列表数据
	 * @param offset 获取数据的起始位置
	 * @param pageItems 获取数据的条数
	 * @return ListInfo 对分页数据和总记录条数的封装类
	 */

	private static Addresses pagerList(List datas,int offset,int pageItems)
	{
		if(datas == null)
			return null;
		List list = new ArrayList();
		if(offset >= datas.size())
		{
		    int temp = datas.size() % pageItems;
		    offset = datas.size() - temp;
		}
		for(int i = offset; i < datas.size() && i < offset + pageItems; i ++)
		{
		    list.add(datas.get(i));
		}
		Addresses address = new Addresses();
		address.setRows(list);
		address.setTotal(datas.size());
		return address;
	}
}