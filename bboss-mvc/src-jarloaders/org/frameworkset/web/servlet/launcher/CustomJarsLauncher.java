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

/**
 * <p>
 * Title: CustomJarsLauncher.java
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
public class CustomJarsLauncher {
	public void loadFromRepository(CustomClassLoader classLoader,
			String docbase, String customJarsLocation) {
		if (customJarsLocation == null
				|| customJarsLocation.trim().length() == 0)
			return;
		String[] repositories = customJarsLocation.split(";");
		int i = 0;
		for (String path : repositories) {
			repositories[i] = getRealPath(docbase, path.trim());
			i++;
		}
		classLoader.addRepository(repositories);
	}

	public static String getRealPath(String contextPath, String path) {
		if (contextPath == null || contextPath.equals("")) {
			// System.out.println("StringUtil.getRealPath() contextPath:"
			// + contextPath);
			return path == null ? "" : path;
		}
		if (path == null || path.equals("")) {
			return contextPath;
		}
		contextPath = contextPath.replace('\\', '/');
		path = path.replace('\\', '/');
		if (path.startsWith("/")) {
			if (!contextPath.endsWith("/"))
				return contextPath + path;
			else {
				return contextPath.substring(0, contextPath.length() - 1)
						+ path;
			}
		} else {
			if (!contextPath.endsWith("/"))
				return contextPath + "/" + path;
			else {
				return contextPath + path;
			}
		}
	}
}