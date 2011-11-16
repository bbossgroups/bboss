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

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.frameworkset.web.listener.JarUtil;

/**
 * <p>Title: BaseAppServerClassLoader.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-11-19
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class BaseAppServerClassLoader implements CustomClassLoader {
	private ClassLoader classLoader;
	
	
	

	
	
	public void initClassLoader(ClassLoader classLoader) {
		
		this.classLoader = classLoader;
		
	}
	
	public void addRepository(String[] repositories) {
		if (repositories == null || repositories.length == 0)
			return;	
		for (String reppath : repositories) {
			loadJars(reppath);
			System.out.println("add lib[" + reppath + "] to ClassLoader["
					+ classLoader + "]");
		}
	}
	
	protected abstract void addRepository(ClassLoader classLoader,String jarfile) throws Exception;

	private void loadJars( String file) {
		File jar = new File(file);
		if (!jar.exists())
			return;
		if (jar.isFile())
			try {
//				classLoader.addRepository("file:///" + jar.getCanonicalPath());
				addRepository( classLoader,"file:///" + jar.getCanonicalPath());
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else {
			File[] jarfiles = jar.listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					if (pathname.isFile()) {
						String name = pathname.getName();
						return JarUtil.isJarFile(name);
					}
					else
					{
						String name = pathname.getName();
						return !JarUtil.isExecludeJarDir(name) ;
					}
				}
			});

			if (jarfiles == null || jarfiles.length == 0)
				return;
			for (File jarfile : jarfiles) {

				if (jarfile.isFile()) {
					try {
//						classLoader.addRepository("file:///"
//								+ jarfile.getCanonicalPath());
						addRepository( classLoader,"file:///" + jarfile.getCanonicalPath());
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					try {
						loadJars( jarfile.getCanonicalPath());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
	}

}
