package com.frameworkset.util;

import java.util.HashMap;
import java.util.Map;

public class TestStringUtil {
	@org.junit.Test
	public void testJsontoObject()
	{
		String ddd = "{'docId':true,'docname':'aaaa'}";
		Map data = SimpleStringUtil.json2Object(ddd, HashMap.class);
		System.out.println(data);
		
	}
}
