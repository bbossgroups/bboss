package org.frameworkset.runtime;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * <p>
 * Title: Launcher.java
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
 * @Date
 * @author biaoping.yin
 * @version 1.0
 */
public class CommonLauncher {

	private static String publiclibdir = "/lib";

	private static String resourcesdir = "/resources";

	private static String classes = "/classes";
	private static String webclasses = "/WebRoot/WEB-INF/classes";

	private static String weblib = "/WebRoot/WEB-INF/lib";

	private static String propertfile = "/config.properties";
	public static String commonBootstrapClass = "org.frameworkset.runtime.CommonBootstrap";
	public static String mainclass = "org.frameworkset.persistent.db.DBInit";
	private static Properties properts;
	private static String[] args;
	private static String extlibs[];
	private static String extresources[];
	private static List<URL> alljars;
	private static List<String> resourceFilters;
	/**
	 * 需要过滤的资源文件或者目录
	 */
	private static String[] excludes;
	private static File appDir;
	private static boolean shutdown = false;
	private static int shutdownLevel = 9;
	private static boolean restart = false;


	public static String getProperty(String pro) {
		return getProperty(pro, true);
	}

	public static String getProperty(String pro, String defaultValue) {
		return getProperty(pro, defaultValue, true);
	}

	public static String getProperty(String pro, boolean trim) {
		return getProperty(pro, null,trim);
	}


	/**
	 * 先从配置文件获取属性，如果配置文件中没有，则从系统jvm变量中取，如果系统变量中没有，则采用默认值
	 * @param propertyName
	 * @param defaultValue
	 * @return
	 */
	public static Integer getIntProperty(String propertyName,String defaultValue){
		String property = getProperty(  propertyName,  defaultValue);
		if(property == null)
			return null;
		int p = Integer.parseInt(property);
		return p;
	}
	/**
	 * 先从配置文件获取属性，如果配置文件中没有，则从系统jvm变量中取，如果系统变量中没有，则采用默认值
	 * @param propertyName
	 * @return
	 */
	public static Integer getIntProperty(String propertyName){
		String property = getProperty(  propertyName);
		if(property == null)
			return null;
		int p = Integer.parseInt(property);
		return p;
	}
	/**
	 * 先从配置文件获取属性，如果配置文件中没有，则从系统jvm变量中取，如果系统变量中没有，则采用默认值
	 * @param propertyName
	 * @param defaultValue
	 * @return
	 */
	public static Integer getIntProperty(String propertyName,Integer defaultValue){
		String property = getProperty(  propertyName);
		if(property == null)
			return defaultValue;
		int p = Integer.parseInt(property);
		return p;
	}

	public static Boolean getBooleanAttribute(String name) {
		String property = getProperty(  name);
		if(property == null)
			return null;
		Boolean p = Boolean.parseBoolean(property.toLowerCase());
		return p;


	}

	public static Boolean getBooleanAttribute(String name, Boolean defaultValue) {
		String property = getProperty(  name);
		if(property == null)
			return defaultValue;
		Boolean p = Boolean.parseBoolean(property.toLowerCase());
		return p;


	}

	public static Long getLongAttribute(String name) {
		String property = getProperty(  name);
		if(property == null)
			return null;
		Long p = Long.parseLong(property);
		return p;


	}

	public static Long getLongAttribute(String name, Long defaultValue) {
		String property = getProperty(  name);
		if(property == null)
			return defaultValue;
		Long p = Long.parseLong(property);
		return p;


	}

	public static Short getShortAttribute(String name) {
		String property = getProperty(  name);
		if(property == null)
			return null;
		Short p = Short.parseShort(property);
		return p;


	}

	public static Short getShortAttribute(String name, Short defaultValue) {
		String property = getProperty(  name);
		if(property == null)
			return defaultValue;
		Short p = Short.parseShort(property);
		return p;


	}

	/**
	 * 先从配置文件获取属性，如果配置文件中没有，则从系统jvm变量中取，如果系统变量中没有，则采用默认值
	 * @param propertyName
	 * @param defaultValue
	 * @return
	 */
	public static  String getProperty(String propertyName,String defaultValue, boolean trim){
		defaultValue = System.getProperty(propertyName,defaultValue);
		String value = null;
		if (properts != null)
			value = (String) properts.get(propertyName);
		if (value == null)
			return defaultValue;
		else {
			if (trim)
				value = value.trim();
		}
		return value;

	}

	private static void initExcludeResources(){
		List<String> filters = new ArrayList<String>();
		if(excludes != null && excludes.length > 0){
			for(String exclude:excludes){
				File file = new File(exclude);
				if(file.exists()){
					try {
						filters.add(file.getCanonicalPath());
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			resourceFilters = filters;
		}
	}
	private static boolean isFilter(File file){
		if(resourceFilters == null || resourceFilters.size() == 0){
			return false;
		}
		try {
			String path = file.getCanonicalPath();
			for(String filterPath:resourceFilters){
				if(filterPath.equals(path)){
					System.out.println("Ignore load file["+path+"]");
					return true;
				}
			}
			return false;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static void loadConfig(File appDir) throws IOException {
		System.out.println("appDir:" + appDir);
		InputStream in = null;
		Reader read = null;
		try {
			File propertiesFile = new File(appDir, propertfile);
			in = new FileInputStream(propertiesFile);
			read = new InputStreamReader(in, "UTF-8");
			System.out.println("Load properties from file:" + propertiesFile);
			properts = new Properties();
			properts.load(read);
			mainclass = properts.getProperty("mainclass");
			System.out.println("Configed main class:" + mainclass);
			//如果mainclass是变量定义格式，则从系统属性和系统环境变量中解析mainclass
			mainclass = EnvirmentUtil.parserMainClass(mainclass);
			if (mainclass == null || mainclass.trim().length() == 0)
				throw new java.lang.IllegalArgumentException("配置文件config.properties 中没有正确设置mainclass属性.");
			System.out.println("Use mainclass:" + mainclass);
			String extlib = properts.getProperty("extlibs");

			if (extlib != null) {
				extlibs = extlib.split(";");
				for (int i = 0; i < extlibs.length; i++) {
					extlibs[i] = extlibs[i].trim();
				}
			}
			String excludes_ = properts.getProperty("excludes");
			System.out.println("Exclude Resources:"+excludes_);
			if(excludes_ != null && !excludes_.equals("")){
				excludes = excludes_.split(";");
				for (int i = 0; i < excludes.length; i++) {
					excludes[i] = excludes[i].trim();
				}
				initExcludeResources();
			}
			String extresources_ = properts.getProperty("extresources");
			if (extresources_ != null) {
				extresources = extresources_.split(";");
				for (int i = 0; i < extresources.length; i++) {
					extresources[i] = extresources[i].trim();
				}
			}


		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if (read != null)
				try {
					read.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

		}
	}
	
	public static void run(String[] args)
			throws SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
		CommonLauncher.args = args;
		URL location = (CommonLauncher.class).getProtectionDomain().getCodeSource().getLocation();
		System.out.println("os info:" + OSInfo.getOS());
		appDir = computeApplicationDir(location, new File("."));
		loadConfig(appDir);
		if (!shutdown && !restart) {
			startup();
		} else if(shutdown){
			shutdown();
		}
		else if(restart){
			restart();
//			shutdown();
//			try {
//				//关闭服务后，停顿2秒
//				if(shutdownLevel != 9)
//					Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//				return;
//			}
//			startup();
		}

	}
	private static void restart() throws NoSuchMethodException, IllegalAccessException, InstantiationException, ClassNotFoundException, InvocationTargetException, MalformedURLException {
		loadEnvironment();
		URL classpathEntries[] = (URL[]) alljars.toArray(new URL[alljars.size()]);
		ClassLoader cl = new URLClassLoader(classpathEntries);
		Thread.currentThread().setContextClassLoader(cl);
		CommonBootstrapInf commonBootstrapInf = getCommonBootstrap(cl);
		commonBootstrapInf.restart(  propertfile,cl,mainclass,appDir,shutdownLevel,classpathEntries,args);
	}

	private static void shutdown() throws IllegalAccessException, InstantiationException, ClassNotFoundException,
			NoSuchMethodException, MalformedURLException, InvocationTargetException {
		loadEnvironment();
		URL classpathEntries[] = (URL[]) alljars.toArray(new URL[alljars.size()]);
		ClassLoader cl = new URLClassLoader(classpathEntries);
		Thread.currentThread().setContextClassLoader(cl);
		CommonBootstrapInf commonBootstrapInf = getCommonBootstrap(cl);
		commonBootstrapInf.shutdown(  propertfile,appDir,shutdownLevel,classpathEntries);
		/**
		System.out.println("shutdown start ....");
		String pidname = getProperty("pidfile", "pid");
		File pid = pidname.startsWith("/")?new File(pidname):new File(appDir, pidname);
		List<String> pids = new ArrayList<String>();
		FileReader read = null;
		try {
			if (!pid.exists()) {
				System.out.println("进程号文件" + pid.getAbsolutePath() + "不存在。。。。");
				return;
			}

			read = new FileReader(pid);
			BufferedReader in = null;
			String s = null;
			try {
				in = new BufferedReader(read);
				while ((s = in.readLine()) != null) {
					s = s.trim();
					if(!s.equals("") && !s.equals("\n"))
						pids.add(s);
				}

			} finally {
				if (in != null)
					in.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (read != null)
				try {
					read.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		if(pids.size() > 0){
			try {
				killproc(pids);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else{
			System.out.println("没有需要关闭的进程信息.");
		}
		pid.delete();
		
		System.out.println("shutdown end.");
		 */

	}


	private static void loadEnvironment() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException, MalformedURLException, ClassNotFoundException {
		File lib = new File(appDir, publiclibdir);

		File resourcesFile = new File(appDir, resourcesdir);

		File classesFile = new File(appDir, classes);

		File webclassesFile = new File(appDir, webclasses);

		File weblibFile = new File(appDir, weblib);

		loadPlugins(lib, resourcesFile, classesFile, webclassesFile, weblibFile);
	}
	private static CommonBootstrapInf getCommonBootstrap(ClassLoader cl) throws ClassNotFoundException,
			IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
		Class commonBootstrapclass = cl.loadClass(commonBootstrapClass);
		CommonBootstrapInf commonBootstrapInf = (CommonBootstrapInf) commonBootstrapclass.newInstance();
		return commonBootstrapInf;
	}
	private static void startup()
			throws MalformedURLException, ClassNotFoundException, SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {
		genPIDFile();
		loadEnvironment();

		URL classpathEntries[] = (URL[]) alljars.toArray(new URL[alljars.size()]);
		ClassLoader cl = new URLClassLoader(classpathEntries);
		Thread.currentThread().setContextClassLoader(cl);
		CommonBootstrapInf commonBootstrapInf = getCommonBootstrap(cl);
		commonBootstrapInf.startup(propertfile,cl,mainclass,appDir,args,classpathEntries);
		/**
		if (mainclass == null) {
			System.out.println("Invalid main-class entry, cannot proceed.");
			System.exit(1);
		}




		// Object instance = mainClass.newInstance();
		// startup(String[] serverinfo,String plugins[])

		for (int i = 0; i < classpathEntries.length; i++) {
			URL url = classpathEntries[i];
			System.out.println("ClassPath[" + i + "] = " + url);
		}
		Class mainClass = cl.loadClass(mainclass);
		try {

			Method setAppdir = mainClass.getMethod("setAppdir", new Class[] { File.class });
			if (setAppdir != null) {
				setAppdir.invoke(null, new Object[] { appDir });
			}
		} catch (Exception e) {
			System.out.println("ignore set Appdir variable for " + mainclass + ":" + e.getMessage());
		}
		Method method = mainClass.getMethod("main", new Class[] { String[].class });
		method.invoke(null, new Object[] { args });
		System.out.println("started success.");
		 */
	}

	private static void genPIDFile() {
		String pidname = getProperty("pidfile", "pid");
		File pid = pidname.startsWith("/")?new File(pidname):new File(appDir, pidname);

		FileWriter writer = null;
		try {
			if (pid.exists()) {

			} else {
				pid.createNewFile();
			}
			writer = new FileWriter(pid, true);
			writer.write(getProcessID());
			writer.write("\n");
			writer.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}

	/**
	 *
	 * @param lib
	 * @param resourcesFile
	 * @param classesFile
	 * @param webclassesFile
	 * @param weblibFile
	 * @throws MalformedURLException
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 */
	public static void loadPlugins(File lib, File resourcesFile, File classesFile,

			File webclassesFile,

			File weblibFile)
			throws MalformedURLException, ClassNotFoundException, SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException {

		List<URL> allpublicjars = new ArrayList<URL>();
		System.out.println(lib.getAbsolutePath());
		//
		loadSubdirJars(lib, allpublicjars,false);
		if (weblibFile.exists())
			loadSubdirJars(weblibFile, allpublicjars,false);
		if (extlibs != null && extlibs.length > 0) {
			for (String ext : extlibs) {
				File elib = new File(appDir, ext);
				loadSubdirJars(elib, allpublicjars,false);
			}
		}

		alljars = new ArrayList<URL>();

		alljars.addAll(allpublicjars);

		if (extresources != null && extresources.length > 0) {
			for (String resource : extresources) {
				File elib = new File(appDir, resource);
				if(!isFilter(elib))
					alljars.add(elib.toURI().toURL());
			}
		}
		alljars.add(resourcesFile.toURI().toURL());

		if (classesFile.exists())
			alljars.add(classesFile.toURI().toURL());

		if (webclassesFile.exists())
			alljars.add(webclassesFile.toURI().toURL());

	}

	private static void loadSubdirJars(File file, List<URL> alljars,boolean rootFilter) throws MalformedURLException {
		if(rootFilter && isFilter(file))
			return;
		if (file.isFile()) {

			alljars.add(file.toURI().toURL());
		} else {
			File[] jarFiles = file.listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					if(isFilter(pathname))
						return false;
					if (pathname.isFile()) {
						String name = pathname.getName();
//						return name.endsWith(".jar") || name.endsWith(".zip") || name.endsWith(".dll")
//								|| name.endsWith(".lib") || name.endsWith(".sigar_shellrc") || name.endsWith(".sl")
//								|| name.endsWith(".so") || name.endsWith(".dylib");
						return true;
					} else
						return true;
				}
			});

			if (jarFiles == null || jarFiles.length == 0)
				return;
			for (File jarFile : jarFiles) {

				if (jarFile.isFile()) {
					alljars.add(jarFile.toURI().toURL());
				} else {
					loadSubdirJars(jarFile, alljars,false);
				}
			}
		}
	}

	

	public static void main(String[] args)
			throws SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
		System.out.println("PID:"+getProcessID());
		StringBuilder buidler = new StringBuilder();

		for (int i = 0; args != null && i < args.length; i++) {
			if (args[i].startsWith("--conf=")) {
				propertfile = args[i].substring("--conf=".length());
				if (!propertfile.startsWith("/"))
					propertfile = "/" + propertfile;
			} else if (args[i].equals("stop")) {
				shutdown = true;
			}
			else if (args[i].equals("restart")) {
				restart = true;
			}
			else if (args[i].startsWith("--shutdownLevel=")) {
				String level = args[i].substring("--shutdownLevel=".length());
				try {
					if(level.equals("C"))
						shutdownLevel = -1;
					else 
						shutdownLevel = Integer.parseInt(level);
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("shutdownLevel 必须制定定为2 9 或者C:args["+args[i]+"],level["+level+"]");
				}
				restart = true;
			}
			
			buidler.append(args[i]).append(" ");
		}
		
		System.out.println("laucher args:" + buidler);
		System.out.println("use config file:" + propertfile);
		run(args);
		
	}

	private static File computeApplicationDir(URL location, File defaultDir) {

		if (location == null) {
			System.out.println("Warning: Cannot locate the program directory. Assuming default.");
			return defaultDir;
		}
		if (!"file".equalsIgnoreCase(location.getProtocol())) {
			System.out.println("Warning: Unrecognized location type. Assuming default.");
			return new File(".");
		}
		String file = location.getFile();
		if (!file.endsWith(".jar") && !file.endsWith(".zip")) {
			try {
				return (new File(URLDecoder.decode(location.getFile(), "UTF-8"))).getParentFile();
			} catch (UnsupportedEncodingException e) {

			}

			System.out.println("Warning: Unrecognized location type. Assuming default.");
			return new File(location.getFile());
		} else {

			try {
				File path = null;// new
									// File(URLDecoder.decode(location.toExternalForm().substring(6),
									// "UTF-8")).getParentFile();
				// if(!CommonLauncher.isLinux() && !CommonLauncher.isOSX())
				if (OSInfo.isWindows()) {
					path = new File(URLDecoder.decode(location.toExternalForm().substring(6), "UTF-8")).getParentFile();
				} else {
					path = new File(URLDecoder.decode(location.toExternalForm().substring(5), "UTF-8")).getParentFile();
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

		System.out.println("Warning: Unrecognized location type. Assuming default.");
		return new File(location.getFile());
	}


	/**
	 * Determine the hostname of the machine  is running on
	 * 
	 * @return The hostname
	 */
	public static final String getHostname() {
		String lastHostname = "localhost";
		try {
			Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface nwi = en.nextElement();
				Enumeration<InetAddress> ip = nwi.getInetAddresses();

				while (ip.hasMoreElements()) {
					InetAddress in = (InetAddress) ip.nextElement();
					lastHostname = in.getHostName();
					// System.out.println(" ip address bound :
					// "+in.getHostAddress());
					// System.out.println(" hostname : "+in.getHostName());
					// System.out.println(" Cann.hostname :
					// "+in.getCanonicalHostName());
					// System.out.println(" ip string : "+in.toString());
					if (!lastHostname.equalsIgnoreCase("localhost") && !(lastHostname.indexOf(':') >= 0)) {
						return lastHostname;
					}
				}
			}
		} catch (SocketException e) {

		}

		return lastHostname;
	}

	/**
	 * Determins the IP address of the machine is running on.
	 * 
	 * @return The IP address
	 */
	public static final String getIPAddress() throws Exception {
		Enumeration<NetworkInterface> enumInterfaces = NetworkInterface.getNetworkInterfaces();
		while (enumInterfaces.hasMoreElements()) {
			NetworkInterface nwi = (NetworkInterface) enumInterfaces.nextElement();
			Enumeration<InetAddress> ip = nwi.getInetAddresses();
			while (ip.hasMoreElements()) {
				InetAddress in = (InetAddress) ip.nextElement();
				if (!in.isLoopbackAddress() && in.toString().indexOf(":") < 0) {
					return in.getHostAddress();
				}
			}
		}
		return "127.0.0.1";
	}

	/**
	 * Get the primary IP address tied to a network interface (excluding
	 * loop-back etc)
	 * 
	 * @param networkInterfaceName
	 *            the name of the network interface to interrogate
	 * @return null if the network interface or address wasn't found.
	 * 
	 * @throws SocketException
	 *             in case of a security or network error
	 */
	public static final String getIPAddress(String networkInterfaceName) throws SocketException {
		NetworkInterface networkInterface = NetworkInterface.getByName(networkInterfaceName);
		Enumeration<InetAddress> ipAddresses = networkInterface.getInetAddresses();
		while (ipAddresses.hasMoreElements()) {
			InetAddress inetAddress = (InetAddress) ipAddresses.nextElement();
			if (!inetAddress.isLoopbackAddress() && inetAddress.toString().indexOf(":") < 0) {
				String hostname = inetAddress.getHostAddress();
				return hostname;
			}
		}
		return null;
	}

	/**
	 * Tries to determine the MAC address of the machine  is running on.
	 * 
	 * @return The MAC address.
	 */
	public static final String getMACAddress() throws Exception {
		String ip = getIPAddress();
		String mac = "none";
		String os = OSInfo.getOS();
		String s = "";

		// System.out.println("os = "+os+", ip="+ip);

		if (OSInfo.isWindows()) {
			try {
				// System.out.println("EXEC> nbtstat -a "+ip);

				Process p = Runtime.getRuntime().exec("nbtstat -a " + ip);

				// read the standard output of the command
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

				while (!procDone(p)) {
					while ((s = stdInput.readLine()) != null) {
						// System.out.println("NBTSTAT> "+s);
						if (s.indexOf("MAC") >= 0) {
							int idx = s.indexOf('=');
							mac = s.substring(idx + 2);
						}
					}
				}
				stdInput.close();
			} catch (Exception e) {

			}
		} else if (OSInfo.isLinux()) {
			try {
				Process p = Runtime.getRuntime().exec("/sbin/ifconfig -a");

				// read the standard output of the command
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

				while (!procDone(p)) {
					while ((s = stdInput.readLine()) != null) {
						int idx = s.indexOf("HWaddr");
						if (idx >= 0) {
							mac = s.substring(idx + 7);
						}
					}
				}
				stdInput.close();
			} catch (Exception e) {

			}
		} else if (OSInfo.isSolaris()) {
			try {
				Process p = Runtime.getRuntime().exec("/usr/sbin/ifconfig -a");

				// read the standard output of the command
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

				while (!procDone(p)) {
					while ((s = stdInput.readLine()) != null) {
						int idx = s.indexOf("ether");
						if (idx >= 0) {
							mac = s.substring(idx + 6);
						}
					}
				}
				stdInput.close();
			} catch (Exception e) {

			}
		} else if (OSInfo.isHPUX()) {
			try {
				Process p = Runtime.getRuntime().exec("/usr/sbin/lanscan -a");

				// read the standard output of the command
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

				while (!procDone(p)) {
					while ((s = stdInput.readLine()) != null) {
						if (s.indexOf("MAC") >= 0) {
							int idx = s.indexOf("0x");
							mac = s.substring(idx + 2);
						}
					}
				}
				stdInput.close();
			} catch (Exception e) {

			}
		}

		return trim(mac);
	}

	private static final boolean procDone(Process p) {
		try {
			p.exitValue();
			return true;
		} catch (IllegalThreadStateException e) {
			return false;
		}
	}

	/**
	 * Determines whether or not a character is considered a space. A character
	 * is considered a space in  if it is a space, a tab, a newline or a
	 * cariage return.
	 * 
	 * @param c
	 *            The character to verify if it is a space.
	 * @return true if the character is a space. false otherwise.
	 */
	public static final boolean isSpace(char c) {
		return c == ' ' || c == '\t' || c == '\r' || c == '\n';
	}

	/**
	 * Left trim: remove spaces to the left of a String.
	 * 
	 * @param source
	 *            The String to left trim
	 * @return The left trimmed String
	 */
	public static String ltrim(String source) {
		if (source == null)
			return null;
		int from = 0;
		while (from < source.length() && isSpace(source.charAt(from)))
			from++;

		return source.substring(from);
	}

	/**
	 * Right trim: remove spaces to the right of a string
	 * 
	 * @param source
	 *            The string to right trim
	 * @return The trimmed string.
	 */
	public static String rtrim(String source) {
		if (source == null)
			return null;

		int max = source.length();
		while (max > 0 && isSpace(source.charAt(max - 1)))
			max--;

		return source.substring(0, max);
	}

	/**
	 * Trims a string: removes the leading and trailing spaces of a String.
	 * 
	 * @param str
	 *            The string to trim
	 * @return The trimmed string.
	 */
	public static final String trim(String str) {
		if (str == null)
			return null;

		int max = str.length() - 1;
		int min = 0;

		while (min <= max && isSpace(str.charAt(min)))
			min++;
		while (max >= 0 && isSpace(str.charAt(max)))
			max--;

		if (max < min)
			return "";

		return str.substring(min, max + 1);
	}

	/**
	 * Right pad a string: adds spaces to a string until a certain length. If
	 * the length is smaller then the limit specified, the String is truncated.
	 * 
	 * @param ret
	 *            The string to pad
	 * @param limit
	 *            The desired length of the padded string.
	 * @return The padded String.
	 */
	public static final String rightPad(String ret, int limit) {
		if (ret == null)
			return rightPad(new StringBuilder(), limit);
		else
			return rightPad(new StringBuilder(ret), limit);
	}

	/**
	 * Right pad a StringBuffer: adds spaces to a string until a certain length.
	 * If the length is smaller then the limit specified, the String is
	 * truncated.
	 * 
	 * @param ret
	 *            The StringBuilder to pad
	 * @param limit
	 *            The desired length of the padded string.
	 * @return The padded String.
	 */
	public static final String rightPad(StringBuilder ret, int limit) {
		int len = ret.length();
		int l;

		if (len > limit) {
			ret.setLength(limit);
		} else {
			for (l = len; l < limit; l++)
				ret.append(' ');
		}
		return ret.toString();
	}

	public static boolean isEmpty(String string) {
		return string == null || string.length() == 0;
	}

	public static final String getProcessID() {
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		// System.out.println(runtimeMXBean.getName());
		return runtimeMXBean.getName().split("@")[0];
	}
}
