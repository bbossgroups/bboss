package org.frameworkset.mvc;

import java.util.List;

import org.frameworkset.util.annotations.RequestParam;

public class ListExampleBean {
	private List name;
	private @RequestParam(name="name") List names;
	private List<Integer> age;
	private @RequestParam(name="age") List<Integer> ages; 

}
