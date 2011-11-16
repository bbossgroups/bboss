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
package org.frameworkset.web.listener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.frameworkset.web.servlet.PropertiesDummy;
import org.frameworkset.web.servlet.launcher.CustomClassLoader;
import org.frameworkset.web.servlet.launcher.CustomJarsLauncher;

/**
 * <p>
 * Title: ListenerUtil.java
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
 * @Date 2011-1-5
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class JarUtil {
	private static final Properties defaultStrategies;

	/**
	 * Name of the class path resource (relative to the DispatcherServlet class)
	 * that defines DispatcherServlet's default strategy names.
	 */
	private static final String DEFAULT_STRATEGIES_PATH = "DispatcherServlet.properties";
	private static String[] jarFiles = new String[] { ".jar", ".dll", ".lib",
			".so", ".zip" };
	private static String[] execludeJarDirs = new String[] { ".svn" };
	private static String docbase;
	private static String customJarsLocation = "/WEB-INF/lib/ext";
	private static String[] appServerClassLoader = new String[] {
			"org.frameworkset.web.servlet.launcher.TomcatWebAppClassLoader",
			"org.frameworkset.web.servlet.launcher.ApusicWebappClassLoader",
			"org.frameworkset.web.servlet.launcher.WAS70WebappClassLoader",
			"org.frameworkset.web.servlet.launcher.ResinWebAppClassLoader",
			"org.frameworkset.web.servlet.launcher.WeblogicWebAppClassLoader" };

	static {

		// Load default strategy implementations from properties file.
		// This is currently strictly internal and not meant to be customized
		// by application developers.
		try {

			defaultStrategies = fillProperties();
			initAppServerClassLoader();
			initDocbase();
			String[] jarFiles_ = null;
			String jarFile = (String) defaultStrategies
					.getProperty("org.frameworkset.web.servlet.launcher.jarFiles");
			if (jarFile != null && !jarFile.trim().equals("")) {
				jarFile = jarFile.trim();
				jarFiles_ = jarFile.split(",");
				if (jarFiles_.length > 0) {
					for (int i = 0; i < jarFiles_.length; i++) {
						jarFiles_[i] = jarFiles_[i].trim();
					}
					jarFiles = jarFiles_;
				}
			}

			String execludeJarDir = (String) defaultStrategies
					.getProperty("org.frameworkset.web.servlet.launcher.execludejarDirs");
			if (execludeJarDir != null && !execludeJarDir.trim().equals("")) {
				execludeJarDir = execludeJarDir.trim();
				String[] execludeJarDirs_ = execludeJarDir.split(",");

				if (execludeJarDirs_.length > 0) {
					for (int i = 0; i < execludeJarDirs_.length; i++) {
						execludeJarDirs_[i] = execludeJarDirs_[i].trim();
					}
					execludeJarDirs = execludeJarDirs_;
				}

			}

		} catch (IOException ex) {
			throw new IllegalStateException(
					"Could not load 'DispatcherServlet.properties': "
							+ ex.getMessage());
		}
	}

	private static void initDocbase() {
		docbase = defaultStrategies
				.getProperty("org.frameworkset.web.servlet.launcher.docbase");
		System.out
				.println("get docbase from [org.frameworkset.web.servlet.launcher.docbase]: "
						+ docbase);
		if (docbase == null || docbase.trim().equals("")) {
			URL location = (JarUtil.class).getProtectionDomain()
					.getCodeSource().getLocation();
			File appDir = computeApplicationDir(location, new File("."));
			docbase = appDir.getParentFile().getPath();
			System.out
					.println("Evalute docbase from Class context: " + docbase);
		} else {
			docbase = docbase.trim();
			File file = new File(docbase);
			if (!file.exists()) {
				System.out.println("Config docbase does not exist: " + docbase
						+ ", Will Get from Class context.");
				URL location = (JarUtil.class).getProtectionDomain()
						.getCodeSource().getLocation();
				File appDir = computeApplicationDir(location, new File("."));
				docbase = appDir.getParentFile().getPath();
				System.out.println("Evalute docbase from Class context: "
						+ docbase);
			}
		}

		// org.frameworkset.web.servlet.launcher.docbase
		customJarsLocation = defaultStrategies
				.getProperty("org.frameworkset.web.servlet.launcher.customJarsLocation");
		System.out
				.println("Get customJarsLocation from [org.frameworkset.web.servlet.launcher.customJarsLocation]: "
						+ customJarsLocation);
		if (customJarsLocation == null || customJarsLocation.trim().equals("")) {
			System.out
					.println("Get customJarsLocation from [org.frameworkset.web.servlet.launcher.customJarsLocation]: "
							+ customJarsLocation
							+ " default customJarsLocation[/WEB-INF/lib/ext] will be used.");
			customJarsLocation = "/WEB-INF/lib/ext";
		} else {
			customJarsLocation = customJarsLocation.trim();
		}

	}

	private static void initAppServerClassLoader() {
		String appServerClassLoader_ = defaultStrategies
				.getProperty("org.frameworkset.web.servlet.launcher.CustomClassLoader");

		if (appServerClassLoader_ != null) {
			System.out
					.println("AppServerClassLoaders:" + appServerClassLoader_);
			String temp[] = appServerClassLoader_.split(",");
			for (int i = 0; i < temp.length; i++) {
				temp[i] = temp[i].trim();
			}
			if (temp.length > 0) {
				appServerClassLoader = temp;
			}

		}

	}

	private static File computeApplicationDir(URL location, File defaultDir) {
		if (location == null) {
			System.out
					.println("Warning: Cannot locate the program directory. Assuming default.");
			return defaultDir;
		}
		if (!"file".equalsIgnoreCase(location.getProtocol())) {
			System.out
					.println("Warning: Unrecognized location type. Assuming default.");
			return new File(".");
		}
		String file = location.getFile();
		// System.out.println("file: .>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
		// + file);
		if (!file.endsWith(".jar") && !file.endsWith(".zip")) {
			try {
				// return (new File(URLDecoder.decode(location.getFile(),
				// "UTF-8"))).getParentFile().getParentFile();
				return (new File(URLDecoder.decode(location.getFile(), "UTF-8")))
						.getParentFile().getParentFile().getParentFile()
						.getParentFile().getParentFile().getParentFile();
			} catch (UnsupportedEncodingException e) {

			}

			System.out
					.println("Warning: Unrecognized location type. Assuming default.");
			return new File(location.getFile());
		} else {

			try {
				File path = null;// new
									// File(URLDecoder.decode(location.toExternalForm().substring(6),
									// "UTF-8")).getParentFile();
				if (!isLinux()) {
					path = new File(URLDecoder.decode(location.toExternalForm()
							.substring(6), "UTF-8")).getParentFile()
							.getParentFile();
				} else {
					path = new File(URLDecoder.decode(location.toExternalForm()
							.substring(5), "UTF-8")).getParentFile()
							.getParentFile();
				}
				// System.out.println("path: " + path.getAbsolutePath());
				// System.out.println("location: " + location.getPath());
				// System.out.println("external from location: " +
				// URLDecoder.decode(location.toExternalForm().substring(6),
				// "UTF-8"));
				// System.out.println("external from location + 6: " +
				// URLDecoder.decode(location.toExternalForm(), "UTF-8"));

				return path;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out
				.println("Warning: Unrecognized location type. Assuming default.");
		return new File(location.getFile());
	}

	public static boolean isJarFile(String name) {
		for (int i = 0; i < jarFiles.length; i++) {
			if (name.endsWith(jarFiles[i]))
				return true;
		}
		return false;
	}

	public static boolean isExecludeJarDir(String name) {
		for (int i = 0; i < execludeJarDirs.length; i++) {
			if (name.equals(execludeJarDirs[i]))
				return true;
		}
		return false;
	}

	/**
	 * determine the OS name
	 * 
	 * @return The name of the OS
	 */
	public static final String getOS() {
		return System.getProperty("os.name");
	}

	/**
	 * @return True if the OS is a Windows derivate.
	 */
	public static final boolean isWindows() {
		return getOS().startsWith("Windows");
	}

	/**
	 * @return True if the OS is a Linux derivate.
	 */
	public static final boolean isLinux() {
		return getOS().startsWith("Linux");
	}

	private static boolean loaded = false;

	public static boolean loadCustomJars() {
		if (loaded)
			return loaded;
		try {
			// Exception e = new
			// Exception("JarLoadServlet>>>>>>>>>>>>>>>>>>>>>>>>>");
			// e.printStackTrace();
			// String customJarsLocation =
			// arg0.getServletContext().getInitParameter("customJarsLocation");
			System.out.println("Load custom Jars from Location:"
					+ customJarsLocation
					+ " to "
					+ JarUtil.class.getClassLoader().getClass()
							.getCanonicalName());
			CustomClassLoader classLoader = getDefaultStrategy(JarUtil.class
					.getClassLoader());
			// classLoader.initClassLoader(JarUtil.class.getClassLoader());
			CustomJarsLauncher launcher = new CustomJarsLauncher();
			launcher.loadFromRepository(classLoader, docbase,
					customJarsLocation);
			Logger log = Logger.getLogger(JarUtil.class);

			return loaded;
		} catch (Exception e) {
			e.printStackTrace();
			return loaded;
		} finally {
			loaded = true;
		}
	}

	/**
	 * Return the default strategy object for the given strategy interface.
	 * <p>
	 * The default implementation delegates to {@link #getDefaultStrategies},
	 * expecting a single object in the list.
	 * 
	 * @param context
	 *            the current WebApplicationContext
	 * @param strategyInterface
	 *            the strategy interface
	 * @return the corresponding strategy object
	 * 
	 * @see #getDefaultStrategies
	 */
	protected static CustomClassLoader getDefaultStrategy(
			ClassLoader classLoader) throws Exception {
		List<CustomClassLoader> strategies = getDefaultStrategies(classLoader);
		if (strategies.size() <= 0) {
			throw new Exception(
					"DispatcherServlet needs exactly 1 strategy for interface ["
							+ CustomClassLoader.class.getName() + "]");
		}
		return strategies.get(0);
	}

	public static InputStream getInputStream(Class clazz) throws IOException {
		InputStream is = null;

		is = clazz.getResourceAsStream(DEFAULT_STRATEGIES_PATH);

		if (is == null) {
			throw new FileNotFoundException(DEFAULT_STRATEGIES_PATH
					+ " cannot be opened because it does not exist");
		}
		return is;
	}

	/**
	 * Create a List of default strategy objects for the given strategy
	 * interface.
	 * <p>
	 * The default implementation uses the "DispatcherServlet.properties" file
	 * (in the same package as the DispatcherServlet class) to determine the
	 * class names. It instantiates the strategy objects through the context's
	 * BeanFactory.
	 * 
	 * @param context
	 *            the current WebApplicationContext
	 * @param strategyInterface
	 *            the strategy interface
	 * @return the List of corresponding strategy objects
	 */
	protected static List<CustomClassLoader> getDefaultStrategies(
			ClassLoader classLoader) {

		List<CustomClassLoader> strategies = null;

		if (appServerClassLoader != null) {
			String[] classNames = appServerClassLoader;
			// System.out.println(classNames);
			strategies = new ArrayList(classNames.length);
			for (int i = 0; i < classNames.length; i++) {
				String className = classNames[i];

				try {
					System.out.println("Use CustomClassLoader:" + className);
					Class clazz = Class.forName(className);

					CustomClassLoader strategy = (CustomClassLoader) clazz
							.newInstance();

					if (strategy.validate(classLoader)) {

						strategy.initClassLoader(classLoader);
						strategies.add(strategy);
						return strategies;
					}

				} catch (ClassNotFoundException ex) {
					// ex.printStackTrace();
					continue;
				} catch (LinkageError ex) {
					// ex.printStackTrace();
					continue;
				} catch (InstantiationException ex) {
					// ex.printStackTrace();
					continue;
				} catch (IllegalAccessException ex) {
					// ex.printStackTrace();
					continue;
				} catch (Exception ex) {
					// ex.printStackTrace();
					continue;
				}
			}
			strategies = Collections.EMPTY_LIST;
		} else {
			strategies = Collections.EMPTY_LIST;
		}
		return strategies;
	}

	/**
	 * Fill the given properties from the given resource.
	 * 
	 * @param props
	 *            the Properties instance to fill
	 * @param resource
	 *            the resource to load from
	 * @throws IOException
	 *             if loading failed
	 */
	public static Properties fillProperties() throws IOException {
		InputStream is = getInputStream(PropertiesDummy.class);
		try {
			Properties props = new Properties();
			props.load(is);
			return props;

		} finally {
			is.close();
		}
	}

}
