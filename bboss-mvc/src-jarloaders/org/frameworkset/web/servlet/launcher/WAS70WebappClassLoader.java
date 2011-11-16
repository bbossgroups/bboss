package org.frameworkset.web.servlet.launcher;

import java.lang.reflect.Method;

public class WAS70WebappClassLoader extends BaseAppServerClassLoader {

	@Override
	protected void addRepository(ClassLoader classLoader, String jarfile)
			throws Exception {
		Method addPaths = classLoader.getClass().getDeclaredMethod("addPaths", new Class[]{String[].class});
		addPaths.invoke(classLoader,new Object[]{ new String[]{jarfile}});
//		CompoundClassLoader classLoader_ = (CompoundClassLoader)classLoader;
//		classLoader_.addPaths(new String[]{jarfile});

	}

	public boolean validate(ClassLoader classLoader) {
		try
		{
			if(classLoader.getClass().getName().equals("com.ibm.ws.classloader.CompoundClassLoader"))
				return true;
			return false;
//			CompoundClassLoader classLoader_ = (CompoundClassLoader)classLoader;
//			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}

}
