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
package org.frameworkset.web.http.converter.json;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.frameworkset.http.converter.json.JsonDataWrapper;
import org.frameworkset.spi.annotations.domain.Person;
import org.frameworkset.util.annotations.HandlerMapping;
import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.handler.annotations.Controller;

/**
 * <p>Title: JsonController.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-12-10
 * @author biaoping.yin
 * @version 1.0
 */
@Controller
public class JsonController {
	private static List<Person> list=new ArrayList<Person>();
	
	public static List<Person> getPersons(String qtype ,String value)
	{
		if(qtype.equals(""))
			return list;
		List<Person> datas = new ArrayList<Person>();
		if(value.equals(""))
			return list;
		for(Person p:list)
		{
			if(qtype.equals("name"))
			{
				if(p.getName() != null && p.getName().contains(value))
					datas.add(p);
			}
			else if(qtype.equals("title"))
			{
				if(p.getTitle() != null && p.getTitle().contains(value))
					datas.add(p);
			}
		}
		return datas;
	}
	
	public static void addPerson(String id,String title,String name)
	{
		Person person = new Person();
		person.setId(Long.parseLong(id));
		person.setName(title);
		person.setTitle(name);
		list.add(0,person);
	}
	static{
		Person person = new Person();
		person.setId(new Long(1));
		person.setName("He Jing");
		person.setTitle("None");
		list.add(person);
		Person person1 = new Person();
		person1.setId(new Long(2));
		person1.setName("Kevin");
		person1.setTitle("Who中文");
		list.add(person1);
		
		for(int i = 3; i < 10000; i ++)
		{
			Person person_ = new Person();
			person_.setId(new Long(i));
			person_.setName("Kevin_" + i);
			person_.setTitle("Who am I?");
			list.add(person_);
		}
	}
	
	 //http://localhost:8080/bboss-mvc/json/ajax.html
	public @ResponseBody  JsonDataWrapper<Person> ajax(HttpServletRequest request,
													@RequestParam(name="page",defaultvalue="1") int page,
													@RequestParam(name="rp",defaultvalue="10") int pagesize,
													@RequestParam(name="sortname",defaultvalue="id")  String sortname,
													@RequestParam(name="sortorder",defaultvalue="asc")  String sortorder)    
	    throws Exception {   
		String qtype = request.getParameter("qtype");
		String name = request.getParameter("query");
	    JsonDataWrapper<Person> jsonDataWrapper = this.getPaginatedGridData( page,pagesize,qtype ,name);   
	    return jsonDataWrapper;   
	}  
	//http://localhost:8080/bboss-mvc/json/showjson.html
	@HandlerMapping
	public String showjson()
	{
		return "json/json";
	}
	
	private  JsonDataWrapper<Person> getPaginatedGridData(int page,int pagesize,String qtype ,	String value)
	{
		return (JsonDataWrapper<Person>)JsonDataWrapper.pagerList(getPersons(qtype ,value), page, pagesize);
	}

	

}
