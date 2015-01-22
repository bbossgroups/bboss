package org.frameworkset.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frameworkset.util.annotations.AnnotationUtils;
import org.junit.Test;

import com.frameworkset.util.SimpleStringUtil;

public class PathDataTest {
	@Test
	public void test()
	{
		List<String> datas = AnnotationUtils.parserPathdata("/rest//people//");
		System.out.println();
		//第一级路径是不能作为变量的
		String path = "/rest/{a}/people/{b}/{c}";
		int len = path.length();
		int index = path.indexOf('/');
		List<Integer> poses = new ArrayList<Integer>();
		List<String> variables = new ArrayList<String>();
		int count = -1;
		while(index != -1)
		{		
			if(index == len - 1)
				break;
			count ++;
			if(path.charAt(index+1) == '{')
			{
				poses.add(count);
				int endps = path.indexOf("}",index + 1);
				variables.add(path.substring(index + 1 + 1, endps));
			}
			index = path.indexOf("/", index + 1);			
		}
		String[] pathVariables = null;
		Integer[] pathVariablePositions = null;
		if(poses.size() > 0)
		{
			pathVariables = SimpleStringUtil.toStringArray(variables);
			pathVariablePositions = SimpleStringUtil.toIntArray(poses);
		}
		Map retdatas = new HashMap();
		 
		for(int i = 0; i < pathVariablePositions.length; i ++)
		{
			int pos = pathVariablePositions[i].intValue();
			String key = pathVariables[i];
			String value = datas.get(pos);
			retdatas.put(key, value);
		}
		 
		System.out.println();
	}
	
	
	@Test
	public void testValue()
	{
		List<String> datas = AnnotationUtils.parserPathdata("/rest/sunshine/people/2_6_204/yinbp");
		System.out.println();
		//第一级路径是不能作为变量的
		String path = "/rest/{a}/people/{b}/{c}";
		int len = path.length();
		int index = path.indexOf('/');
		List<Integer> poses = new ArrayList<Integer>();
		List<String> variables = new ArrayList<String>();
		int count = -1;
		while(index != -1)
		{		
			if(index == len - 1)
				break;
			count ++;
			if(path.charAt(index+1) == '{')
			{
				poses.add(count);
				int endps = path.indexOf("}",index + 1);
				variables.add(path.substring(index + 1 + 1, endps));
			}
			index = path.indexOf("/", index + 1);			
		}
		String[] pathVariables = null;
		Integer[] pathVariablePositions = null;
		if(poses.size() > 0)
		{
			pathVariables = SimpleStringUtil.toStringArray(variables);
			pathVariablePositions = SimpleStringUtil.toIntArray(poses);
		}
		Map retdatas = new HashMap();
		 
		for(int i = 0; i < pathVariablePositions.length; i ++)
		{
			int pos = pathVariablePositions[i].intValue();
			String key = pathVariables[i];
			String value = datas.get(pos);
			retdatas.put(key, value);
		}
		 
		System.out.println();
	}
	
	
	private void parserVariables()
	{
		
		String path = "/rest/{a}/people/{b}/{c}";
		int len = path.length();
		int index = path.indexOf('/');
		List<Integer> poses = new ArrayList<Integer>();
		List<String> variables = new ArrayList<String>();
		int count = -1;
		while(index != -1)
		{		
			if(index == len - 1)
				break;
			count ++;
			if(path.charAt(index+1) == '{')
			{
				poses.add(count);
				int endps = path.indexOf("}",index + 1);
				variables.add(path.substring(index + 1 + 1, endps));
			}
			index = path.indexOf("/", index + 1);			
		}
		
		if(poses.size() > 0)
		{
			String[] pathVariables = SimpleStringUtil.toStringArray(variables);
			Integer[] pathVariablePositions = SimpleStringUtil.toIntArray(poses);
		}
		
			
		
		
	}
}
