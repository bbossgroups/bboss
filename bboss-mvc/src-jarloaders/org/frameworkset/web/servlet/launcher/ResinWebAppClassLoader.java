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
 * Title: ResinWebAppClassLoader
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
public class ResinWebAppClassLoader extends BaseAppServerClassLoader {

	protected  void addRepository(ClassLoader classLoader,String jarfile) throws Exception
	{
//		EnvironmentClassLoader classLoader_ = (EnvironmentClassLoader) classLoader;
		Method appendToClassPathForInstrumentation = classLoader.getClass().getDeclaredMethod("appendToClassPathForInstrumentation", String.class);
		appendToClassPathForInstrumentation.invoke(classLoader, jarfile.substring("file:///".length()));
//		classLoader_.appendToClassPathForInstrumentation(jarfile.substring("file:///".length()));
//		classLoader_.addURL(new FilePath(jarfile.substring("file:///".length())));
//		classLoader_.addRepository(jarfile);
		System.out.println("load custom Jars from Location:" + jarfile);
	}

	public boolean validate(ClassLoader classLoader) {
		try {
//			com.caucho.loader.EnvironmentClassLoader
			if(classLoader.getClass().getName().equals("com.caucho.loader.EnvironmentClassLoader"))
				return true;
			return false;
		} catch (Exception e) {
			
			return false;
		}
	}
}
