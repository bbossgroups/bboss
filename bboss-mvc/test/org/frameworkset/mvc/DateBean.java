package org.frameworkset.mvc;

import org.frameworkset.util.annotations.RequestParam;

public class DateBean {
	private @RequestParam(name = "d12s", dateformat = "yyyy-MM-dd") 
	java.util.Date d12;
	private @RequestParam(name = "d13s", dateformat = "yyyy-MM-dd") 
	java.util.Date d13;
	private String id;
	

}
