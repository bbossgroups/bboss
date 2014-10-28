package com.frameworkset.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestStringUtil {
	@org.junit.Test
	public void testJsontoObject()
	{
		String ddd = "{'docId':true,'docname':'aaaa'}";
		Map data = SimpleStringUtil.json2Object(ddd, HashMap.class);
		System.out.println(data);
		
	}
	@org.junit.Test
	public void testArrayToenum()
	{
		String[] datas = new String[] {"1","2"};
		Enumeration enums = SimpleStringUtil.arryToenum(datas);
		while(enums.hasMoreElements())
		{
			System.out.println(enums.nextElement());
		}
		
	}
	 
	@org.junit.Test
	public void testObjecttoString()
	{
		String ddd = "{'docId':true,'docname':'aaaa'}";		
		System.out.println(SimpleStringUtil.tostring(ddd));
		
		System.out.println(SimpleStringUtil.tostring(new String[]{"aa","bb"}));
		List dd = new ArrayList();
		dd.add(1);
		dd.add(2);
		System.out.println(SimpleStringUtil.tostring(new Object[]{"aa",null,dd}));
		System.out.println(SimpleStringUtil.tostring(null));
	}
	
}
