package org.frameworkset.spi.annotations.domain;

import java.io.Serializable;

import org.frameworkset.util.annotations.PathVariable;
import org.frameworkset.util.annotations.RequestParam;


public class Person implements Serializable {
	@PathVariable(value="id")
	@RequestParam(name="id") 
	private Long id;
	
	@RequestParam(name = "name", decodeCharset = "UTF-8")
	private String name;
	
	
	private String title;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
