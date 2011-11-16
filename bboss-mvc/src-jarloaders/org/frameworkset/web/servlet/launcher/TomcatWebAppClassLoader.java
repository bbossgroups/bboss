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
package org.frameworkset.web.servlet.launcher;

import java.lang.reflect.Method;

/**
 * <p>
 * Title: TomcatWebAppClassLoader.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2008
 * </p>
 * 
 * @Date 2010-11-19
 * @author biaoping.yin
 * @version 1.0
 */
public class TomcatWebAppClassLoader extends BaseAppServerClassLoader {
//	public void addRepository(String[] repositories) {
//		if (repositories == null || repositories.length == 0)
//			return;
//		WebappClassLoader classLoader_ = (WebappClassLoader) super.classLoader;
//		for (String reppath : repositories) {
//			loadJars(classLoader_, reppath);
//			System.out.println("add lib[" + reppath + "] to ClassLoader["
//					+ classLoader + "]");
//		}
//	}
//
//	private void loadJars(WebappClassLoader classLoader, String file) {
//		File jar = new File(file);
//		if (!jar.exists())
//			return;
//		if (jar.isFile())
//			try {
//				classLoader.addRepository("file:///" + jar.getCanonicalPath());
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		else {
//			File[] jarfiles = jar.listFiles(new FileFilter() {
//				public boolean accept(File pathname) {
//					if (pathname.isFile()) {
//						String name = pathname.getName();
//						return ListenerUtil.isJarFile(name);
//					}
//					else
//					{
//						String name = pathname.getName();
//						return !ListenerUtil.isExecludeJarDir(name) ;
//					}
//				}
//			});
//
//			if (jarfiles == null || jarfiles.length == 0)
//				return;
//			for (File jarfile : jarfiles) {
//
//				if (jarfile.isFile()) {
//					try {
//						classLoader.addRepository("file:///"
//								+ jarfile.getCanonicalPath());
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				} else {
//					try {
//						loadJars(classLoader, jarfile.getCanonicalPath());
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//		}
//		System.out.println("add lib[" + file + "] to ClassLoader["
//				+ classLoader + "]");
//	}
	protected  void addRepository(ClassLoader classLoader,String jarfile) throws Exception
	{
		
//		WebappClassLoader classLoader_ = (WebappClassLoader) classLoader;
		Method addRepository = classLoader.getClass().getDeclaredMethod("addRepository", String.class);
//		classLoader_.addRepository(jarfile);
		addRepository.invoke(classLoader, jarfile);
		System.out.println("load custom Jars from Location:" + jarfile);
	}

	public boolean validate(ClassLoader classLoader) {
		try {
			if(classLoader.getClass().getName().equals("org.apache.catalina.loader.WebappClassLoader"))
			{
				return true;
			}
//			WebappClassLoader classLoader_ = (WebappClassLoader) classLoader;
			return false;
		} catch (Exception e) {
			return false;
		}
	}
}
