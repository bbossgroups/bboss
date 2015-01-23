package org.frameworkset.spi.annotations.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.frameworkset.spi.annotations.domain.Person;
import org.frameworkset.util.annotations.HandlerMapping;
import org.frameworkset.util.annotations.HttpMethod;
import org.frameworkset.util.annotations.PathVariable;
import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.web.servlet.ModelMap;

/**
 * 
 * <p>Title: UserInfoController.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-24
 * @author biaoping.yin
 * @version 1.0
 */

@HandlerMapping(value="/rest/people")
public class UserInfoController{

	private static List<Person> list=new ArrayList<Person>();
	
	public static List<Person> getPersons()
	{
		return list;
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
		person1.setTitle("Who");
		list.add(person1);
	}
	//http://localhost:8080/bboss-mvc/rest/people
	@HandlerMapping(method = HttpMethod.GET)
	public String getAll(ModelMap model) {
		System.out.println("=============> Get list");
		model.addAttribute("list",list);
		return "restful/people";
		// return new People(personDao.getPeople());
	}
	//http://localhost:8080/bboss-mvc/rest/people/1
	@HandlerMapping(value = "/{id}", method = HttpMethod.GET)
	public String getPerson(@PathVariable("id") Long personId,ModelMap model) {
		System.out.println("=========> Get one person");
		Person person;
		List list2=new ArrayList<Person>();
		for (int i = 0; i < list.size(); i++) {
			person=(Person) list.get(i);
			if(person.getId().equals(personId)){
				list2.add(person);
			}			
		}
		model.addAttribute("list",list2);
		return "restful/people";
	}

/*	@HandlerMapping(methodHttpMethodhod.POST)
	public String savePerson(HttpServletRequest request,HttpServletResponse response,Model model) {
		Person person=new Person();
		person.setId(new Long(request.getParameter("people_id")));
		person.setName(request.getParameter("people_name"));
		person.setTitle(request.getParameter("people_title"));
		list.add(person);
		model.addAttribute("list",list);
		return "people";
	}
*/
	@HandlerMapping(method = HttpMethod.POST)
	public String savePerson(Person person,ModelMap model) {
		boolean hasErrors = model.hasErrors();
		if(hasErrors)
		{
			model.addAttribute("list",list);
			
		}
		else
		{
			list.add(person);
			model.addAttribute("list",list);
		}
		
		return "restful/people";
	}
	
	public String testNameMethod(HttpServletRequest request,HttpServletResponse response,ModelMap model)
	{
//		list.add(person);
		System.out.println("testNameMethod");
		model.addAttribute("list",list);
		return "restful/people";
	}
	
	@HandlerMapping(value = "/{action}/{id}", method = {HttpMethod.DELETE,HttpMethod.GET,HttpMethod.POST})
	public String deletePerson(@PathVariable("action") String action,@PathVariable("id") Long personId,ModelMap model) {
		System.out.println("action:" + action);
		Person person;
		for (int i = 0; i < list.size(); i++) {
			person=(Person) list.get(i);
			if(person.getId().equals(personId)){
				list.remove(i);
			}			
		}
		model.addAttribute("list",list);
		return "restful/people";
	}
	
	@HandlerMapping(value = "/{id}", method = HttpMethod.DELETE)
	public String deletePerson(@PathVariable("id") Long personId,ModelMap model) {
		Person person;
		for (int i = 0; i < list.size(); i++) {
			person=(Person) list.get(i);
			if(person.getId().equals(personId)){
				list.remove(i);
			}			
		}
		model.addAttribute("list",list);
		return "restful/people";
	}
	
	@HandlerMapping(value = "/{id}", method = HttpMethod.PUT)
	public String updatePerson(@RequestParam(name="id") 
								@PathVariable("id") Long personId,Person person,ModelMap model) throws Exception {
		Person person2;
		for (int i = 0; i < list.size(); i++) {
			person2=(Person) list.get(i);
			if(person2.getId().equals(personId)){
				person2.setId(person.getId());
				person2.setName(person.getName());
				person2.setTitle(person.getTitle());
				break;
			}			
		}	
		model.addAttribute("list",list);
		if(true)
			throw new Exception("这是一个错误异常处理程序的实例方法");
		else
			return "restful/people";
	}
	
	/**
	 * 异常处理程序，每个控制器可以定义多个特定异常的处理方法
	 * @param request
	 * @param response
	 * @param context
	 * @param e
	 * @param model
	 * @return
	 */
	public String exceptionHandler(HttpServletRequest request ,HttpServletResponse response,PageContext context,Exception e,ModelMap model)
	{
		e.printStackTrace();
		return "restful/people";
	}
}
