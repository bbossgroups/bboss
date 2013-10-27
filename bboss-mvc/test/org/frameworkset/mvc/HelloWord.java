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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frameworkset.util.annotations.AssertDToken;
import org.frameworkset.util.annotations.MapKey;
import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.util.StringUtil;

/**
 * <p>
 * HelloWord.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2009
 * </p>
 * 
 * @Date 2011-6-4
 * @author biaoping.yin
 * @version 1.0
 */
public class HelloWord
{
	public String listmap(ModelMap model)
	{
		//将所有的key放到nameList中
		List<String> nameList = new ArrayList<String>();
		nameList.add("handlerModel");
		nameList.add("applyUnionModel");
		nameList.add("billLoanModel");
		nameList.add("loanPayModel");
		nameList.add("budgetModel");
		nameList.add("outgoModel");
		nameList.add("billItemModel");
		nameList.add("billAttachment");
		nameList.add("billSapModel");
		//构造每个key对应的List<ExampleBean>数据并放到Map<String,List<ExampleBean>> billDataMap变量中
		Map<String,List<ExampleBean>> billDataMap = new HashMap<String,List<ExampleBean>>();
		List<ExampleBean> datas = new ArrayList<ExampleBean>();//定义List<ExampleBean>集合，为了示例的简单，每个集合中只放一个ExampleBean类型对象
		ExampleBean bean = new ExampleBean();
		bean.setName("handlerModel");
		bean.setSex("男");
		datas.add(bean);
		billDataMap.put("handlerModel",datas);//put数据到map中
		datas = new ArrayList<ExampleBean>();
		bean = new ExampleBean();
		bean.setName("applyUnionModel");
		bean.setSex("女");
		datas.add(bean);
		billDataMap.put("applyUnionModel",datas);//put数据到map中
		datas = new ArrayList<ExampleBean>();
		bean = new ExampleBean();
		bean.setName("billLoanModel");
		bean.setSex("男");
		datas.add(bean);
		billDataMap.put("billLoanModel",datas);//put数据到map中
		datas = new ArrayList<ExampleBean>();
		bean = new ExampleBean();
		bean.setName("loanPayModel");
		bean.setSex("女");
		datas.add(bean);
		billDataMap.put("loanPayModel",datas);//put数据到map中
		datas = new ArrayList<ExampleBean>();
		bean = new ExampleBean();
		bean.setName("budgetModel");
		bean.setSex("男");
		datas.add(bean);
		billDataMap.put("budgetModel",datas);//put数据到map中
		datas = new ArrayList<ExampleBean>();
		bean = new ExampleBean();
		bean.setName("outgoModel");
		bean.setSex("女");
		datas.add(bean);
		billDataMap.put("outgoModel",datas);//put数据到map中
		datas = new ArrayList<ExampleBean>();
		bean = new ExampleBean();
		bean.setName("billItemModel");
		bean.setSex("女");
		datas.add(bean);
		billDataMap.put("billItemModel",datas);//put数据到map中
		datas = new ArrayList<ExampleBean>();
		bean = new ExampleBean();
		bean.setName("billAttachment");
		bean.setSex("男");
		datas.add(bean);
		billDataMap.put("billAttachment",datas);//put数据到map中
		datas = new ArrayList<ExampleBean>();
		bean = new ExampleBean();
		bean.setName("billSapModel");
		bean.setSex("未知");
		datas.add(bean);
		billDataMap.put("billSapModel",datas);//put数据到map中	
		model.addAttribute("nameList", nameList);//将名称列表放到控制器数据容器中	
		model.addAttribute("billDataMap", billDataMap);//将map数据放到控制器数据容器中
		return "path:listmap";//跳转到数据展示页面
	}
	@AssertDToken
	public String sayHelloNumber(@RequestParam(name = "name") int ynum,
			ModelMap model)
	{

		if (ynum != 0)
		{
			model.addAttribute("serverHelloNumber", "幸运数字为[" + ynum + "]！");
		}
		else
			model.addAttribute("serverHelloNumber", "幸运数字为[" + ynum
					+ "]！，好像有点不对哦。");

		return "path:sayHello";
	}

	public String sayHelloString(@RequestParam(name = "name") String yourname,
			ModelMap model)
	{

		if (yourname != null && !"".equals(yourname))
			model.addAttribute("sayHelloString", "服务器向您[" + yourname + "]问好！");
		else
			model.addAttribute("sayHelloString", "请输入您的名字！");
		return "path:sayHello";
	}
	
	public String sayHelloStringVar(@RequestParam(name = "name${id}") String yourname,
			ModelMap model)
	{

		if (yourname != null && !"".equals(yourname))
			model.addAttribute("sayHelloStringVar", "服务器向您[" + yourname + "]问好！");
		else
			model.addAttribute("sayHelloStringVar", "请输入您的名字！");
		return "path:sayHello";
	}

	public String sayHelloTime(
			@RequestParam(name = "d12", dateformat = "yyyy-MM-dd") java.util.Date d12,
			@RequestParam(name = "stringdate", dateformat = "yyyy-MM-dd") java.sql.Date stringdate,
			@RequestParam(name = "stringdatetimestamp", dateformat = "yyyy-MM-dd HH/mm/ss") java.sql.Timestamp stringdatetimestamp,
			@RequestParam(name = "stringdatetimestamp") String stringdatetimestamp_,
			ModelMap model)
	{

		model.put("utilDate", d12);
		model.put("sqlDate", stringdate);
		model.put("sqlTimestamp", stringdatetimestamp);
		return "path:sayHello";

	}
	public String dataListBeanBind(List<DateBean> dates,ModelMap model)
	{
		model.put("dataListBeanBind", dates);
		return "path:sayHello";
	}
	public String sayHelloTimes(
			@RequestParam(name = "d12s", dateformat = "yyyy-MM-dd") java.util.Date[] d12,
			@RequestParam(name = "stringdates", dateformat = "yyyy-MM-dd") java.sql.Date[] stringdate,
			@RequestParam(name = "stringdatetimestamps", dateformat = "yyyy-MM-dd HH/mm/ss") java.sql.Timestamp[] stringdatetimestamp,
			@RequestParam(name = "stringdatetimestamps") String[] stringdatetimestamp_,
			ModelMap model)
	{

		if (d12 != null)
		{
			model.put("utilDates", d12[0]);
			model.put("sqlDates", stringdate[0]);
			model.put("sqlTimestamps", stringdatetimestamp[0]);
		}
		return "path:sayHello";

	}

	public String sayHelloBean(ExampleBean yourname, ExampleBean yourname1,ModelMap model)
	{

		if (yourname.getName() != null && !"".equals(yourname.getName()))
			model.addAttribute("serverHelloBean", yourname);
		else
			;

		return "path:sayHello";
	}
	
	public String sayHelloEditorBean(EditorExampleBean yourname,ModelMap model)
	{

		if (yourname.getName() != null && yourname.getName().length > 0)
			model.addAttribute("serverHelloBean", StringUtil.arrayToDelimitedString(yourname.getName(), ","));
		else
			;

		return "path:sayHello";
	}
	
	public String sayHelloEditorBeans(List<EditorExampleBean> yournames,ModelMap model)
	{
		if(yournames != null && yournames.size() > 0)
		{
			StringBuffer ret = new StringBuffer();
			for(EditorExampleBean yourname:yournames)
			{
				if (yourname.getName() != null && yourname.getName().length > 0)
					ret.append(StringUtil.arrayToDelimitedString(yourname.getName(), ",")).append("<br/>");			
				
			}
			model.addAttribute("serverHelloBean", ret.toString());
		}

		return "path:sayHello";
	}
	public String sayHelloEditor(@RequestParam(editor="org.frameworkset.mvc.Editor") String[] name,ModelMap model)
	{

		if (name != null && name.length > 0)
			model.addAttribute("serverHelloBean", StringUtil.arrayToDelimitedString(name, ","));
		else
			;

		return "path:sayHello";
	}
	public String sayHelloListEditor(@RequestParam(editor="org.frameworkset.mvc.ListEditor") List<String> name,ModelMap model)
	{

		if (name != null && name.size() > 0)
			model.addAttribute("serverHelloBean", name);
		else
			;

		return "path:sayHello";
	}
	public String sayHelloEditors(@RequestParam(editor="org.frameworkset.mvc.ListStringArrayEditor") List<String[]> name,ModelMap model)
	{
		if(name != null && name.size() > 0)
		{
			StringBuffer ret = new StringBuffer();
			for(String[] yourname:name)
			{
				if (yourname != null && yourname.length > 0)
					ret.append(StringUtil.arrayToDelimitedString(yourname, ",")).append("<br/>");			
				
			}
			model.addAttribute("serverHelloBean", ret.toString());
		}

		return "path:sayHello";
	}

	public String sayHelloBeanList(List<ExampleBean> yourname, ModelMap model)
	{

		model.addAttribute("serverHelloListBean", yourname);

		return "path:sayHello";
	}

	public String sayHelloBeanMap(@RequestParam(name = "name") String yourname,
			@MapKey("name") Map<String, ExampleBean> mapBeans, ModelMap model)
	{

		model.addAttribute("sayHelloBeanMap", mapBeans);
		return "path:sayHello";
	}
	
	
	public String sayHelloStringList( List name, ModelMap model)
	{

		model.addAttribute("sayHelloStringList", name);

		return "path:sayHello";
	}
	
	
	public String sayHelloIntListWithNameMapping(@RequestParam(name = "name") List<Integer> names, ModelMap model)
	{

		model.addAttribute("sayHelloIntListWithNameMapping", names);

		return "path:sayHello";
	}
	
	public String sayHelloIntList(List<Integer> name, ModelMap model)
	{

		model.addAttribute("sayHelloIntList", name);

		return "path:sayHello";
	}
	
	public String sayHelloEnumList(List<SexType> sex, ModelMap model)
	{

		model.addAttribute("sayHelloEnumList", sex);

		return "path:sayHello";
	}

	public String sayHelloStringMap(Map params,
			ModelMap model)
	{

		model.addAttribute("sayHelloStringMap", params);
		return "path:sayHello";
	}
	
	/**
	 * 补充mapkey注解pattern属性的测试用例：
	 * @param params
	 * @param model
	 * @return
	 */
	public String sayHelloStringMapWithFilter(@MapKey(pattern="pre.cc.*") Map params,
			ModelMap model)
	{

		model.addAttribute("sayHelloStringMapWithFilter", params);
		return "path:sayHello";
	}

	public String sayHelloArray(
			@RequestParam(name = "name") String[] yournames, ModelMap model)
	{

		model.addAttribute("serverHelloArray", yournames);
		return "path:sayHello";
	}

	/**
	 * 测试单个字符串向枚举类型值转换
	 * 
	 * @param type
	 * @param response
	 * @throws IOException
	 */
	public @ResponseBody
	String sayHelloEnum(@RequestParam(name = "sex") SexType type)
			throws IOException
	{

		if (type != null)
		{
			if (type == SexType.F)
			{
				return "女";
			}
			else if (type == SexType.M)
			{
				return "男";
			}
			else if (type == SexType.UN)
			{
				return "未知";
			}

		}

		return "未知";
	}

	/**
	 * 测试单个字符串向枚举类型值转换
	 * 
	 * @param type
	 * @param response
	 * @throws IOException
	 */
	@AssertDToken
	public @ResponseBody
	String sayHelloEnums(@RequestParam(name = "sex") SexType[] types)
			throws IOException
	{

		if (types != null)
		{
			if (types[0] == SexType.F)
			{

				return "女";
			}
			else if (types[0] == SexType.M)
			{

				return "男";
			}
			else if (types[0] == SexType.UN)
			{
				return "未知";
			}
		}
		return "未知";
	}

	public String index()
	{

		return "path:sayHello";
	}
	
	
	public String listExampleBean(ListExampleBean listExampleBean,ModelMap model)
	{
		model.addAttribute("listExampleBean", listExampleBean);
		return "path:sayHello";
	}
}
