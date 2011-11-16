package org.frameworkset.web.servlet.launcher;

import java.io.File;
import java.lang.reflect.Method;

//import com.apusic.web.container.ReloadableClassLoader;

public class ApusicWebappClassLoader extends BaseAppServerClassLoader {

	@Override
	protected void addRepository(ClassLoader classLoader, String jarfile)
			throws Exception {
		Method method = classLoader.getClass().getDeclaredMethod("addFile", File.class);
//		 ReloadableClassLoader classLoader_ = (ReloadableClassLoader)classLoader;
		method.invoke(classLoader, new File(jarfile));
//		 classLoader_.addFile(new File(jarfile));
		 System.out.println("load custom Jars from Location:" + jarfile);

	}
	
	public boolean validate(ClassLoader classLoader) {
		try {
			if(classLoader.getClass().getName().equals("com.apusic.web.container.ReloadableClassLoader"))
				return true;
			
//			ReloadableClassLoader classLoader_ = (ReloadableClassLoader)classLoader;
			return false;
		} catch (Exception e) {
			return false;
		}
	}

}
