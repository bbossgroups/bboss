package org.frameworkset.runtime;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
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
public class CommonLauncher
{
    

    

    private static String publiclibdir = "/lib";
    
    private static String resourcesdir = "/resources";    
    
    private static String classes = "/classes";    
    private static String webclasses = "/WebRoot/WEB-INF/classes";
    
    private static String weblib = "/WebRoot/WEB-INF/lib";
    
    private static String propertfile = "/config.properties";
    
    


    public static String mainclass = "org.frameworkset.persistent.db.DBInit";
    private static Properties properts;
    private static String[] args;
    private static String extlibs[];
    private static String extresources[];
    private static File appDir;
    
    private static  List<URL> alljars;
    public static String getProperty(String pro)
    {
    	return getProperty(pro,true);
    }
    
    public static String getProperty(String pro,String defaultValue)
    {
    	return getProperty(pro,defaultValue,true);
    }
    
    public static String getProperty(String pro,boolean trim)
    {
    	String value = null;
    	if(properts != null)
    		value = (String)properts.get(pro);
    	if(value != null &&trim)
    		value = value.trim();
    	return value;
    }
    
    public static String getProperty(String pro,String defaultValue,boolean trim)
    {
    	String value = null;
    	if(properts != null)
    		value = (String)properts.get(pro);
    	if(value == null)
    		return defaultValue;
    	else
    	{
    		if(trim)
    			value = value.trim();
    	}
    	return value;
    }
    private static void loadConfig(File appDir) throws IOException
    {
    	System.out.println("appDir:"+appDir);
    	 File propertiesFile = new File(appDir, propertfile);
         InputStream in = new FileInputStream(propertiesFile);
        properts = new Properties(); 
        properts.load(in);
        mainclass = properts.getProperty("mainclass");
        String extlib = properts.getProperty("extlibs") ;
        if(extlib != null)
        {
        	extlibs = extlib.split(";");
        	for(int i = 0;  i < extlibs.length; i++)
        	{
        		extlibs[i] = extlibs[i].trim();
        	}
        }
        String extresources_ = properts.getProperty("extresources") ;
        if(extresources_ != null)
        {
        	extresources = extresources_.split(";");
        	for(int i = 0;  i < extresources.length; i++)
        	{
        		extresources[i] = extresources[i].trim();
        	}
        }
        
        if(mainclass == null || mainclass.trim().length() == 0)
        {
        	throw new java.lang.IllegalArgumentException("配置文件config.properties 中没有正确设置mainclass属性.");
        }
        else
        {
        	mainclass = mainclass.trim();
        	System.out.println("use mainclass:"+mainclass);
        }
    }
    public static void run(String[] args) throws SecurityException, IllegalArgumentException,
            ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException
    {
    	CommonLauncher.args = args;
        URL location = (CommonLauncher.class).getProtectionDomain().getCodeSource().getLocation();
        appDir = computeApplicationDir(location, new File("."));
       
        File lib = new File(appDir, publiclibdir);
        
        
        File resourcesFile = new File(appDir, resourcesdir);
        
        File classesFile = new File(appDir, classes);
        
        File webclassesFile = new File(appDir, webclasses);
        
        
        File weblibFile = new File(appDir, weblib);
       
        loadConfig( appDir);
        loadPlugins(lib, resourcesFile,  classesFile,webclassesFile,weblibFile);
        
        
        URL classpathEntries[] = (URL[]) alljars.toArray(new URL[alljars.size()]);
        ClassLoader cl = new URLClassLoader(classpathEntries);
        Thread.currentThread().setContextClassLoader(cl);
        
        if (mainclass == null)
        {
            System.out.println("Invalid main-class entry, cannot proceed.");
            System.exit(1);
        }
        Class mainClass = cl.loadClass(mainclass);

//        Object instance = mainClass.newInstance();
        //startup(String[] serverinfo,String plugins[])
        
        for (int i = 0; i < classpathEntries.length; i++)
        {
            URL url = classpathEntries[i];
            System.out.println("ClassPath[" + i + "] = " + url);
        }
        try {
			Method setAppdir = mainClass.getMethod("setAppdir", new Class[] {File.class});
			if(setAppdir != null)
			{
				setAppdir.invoke(null, new Object[] {appDir});
			}
		} catch (Exception e) {
			System.out.println("ignore set Appdir variable for "+mainclass+":"+e.getMessage());
		}
        Method method = mainClass.getMethod("main", new Class[] {String[].class});
        method.invoke(null, new Object[] {args});
    }

    /**
     * 
     * 
     * @param deploydir
     * @throws MalformedURLException
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InstantiationException 
     */
    public static void loadPlugins(File lib,File resourcesFile,File classesFile ,
    
    File webclassesFile ,
    
    
    File weblibFile ) throws MalformedURLException,
            ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException,
            IllegalAccessException, InvocationTargetException, InstantiationException
    {
       

        List<URL> allpublicjars = new ArrayList<URL>();
        System.out.println(lib.getAbsolutePath());
        // 
        loadSubdirJars(lib, allpublicjars);
        if(weblibFile.exists())
        	loadSubdirJars(weblibFile, allpublicjars);
       if(extlibs != null && extlibs.length > 0)
       {
    	   for(String ext:extlibs)
    	   {
    		   File elib = new File(appDir,ext);
    		   loadSubdirJars(elib, allpublicjars);
    	   }
       }
        
       
       alljars = new ArrayList<URL>();
        
        alljars.addAll(allpublicjars);

        
        if(extresources != null && extresources.length > 0)
        {
     	   for(String resource:extresources)
     	   {
     		   File elib = new File(appDir,resource);
     		  alljars.add(elib.toURI().toURL());
     	   }
        }
        alljars.add(resourcesFile.toURI().toURL());
        
        if(classesFile.exists())
        	alljars.add(classesFile.toURI().toURL());
        
        if(webclassesFile.exists())
        	alljars.add(webclassesFile.toURI().toURL());
        
    }

    private static void loadSubdirJars(File file, List<URL> alljars) throws MalformedURLException
    {
        
        File[] jarfiles = file.listFiles(new FileFilter()
        {
            public boolean accept(File pathname)
            {
                if (pathname.isFile())
                {
                    String name = pathname.getName();
                    return name.endsWith(".jar") || name.endsWith(".zip") || name.endsWith(".dll") || name.endsWith(".lib") || name.endsWith(".sigar_shellrc") || name.endsWith(".sl") || name.endsWith(".so") || name.endsWith(".dylib");
                }
                else return true;
            }
        });
        
        if(jarfiles == null || jarfiles.length == 0)
            return;
        for (File jarfile : jarfiles)
        {
            
            if (jarfile.isFile())
            {
                alljars.add(jarfile.toURI().toURL());
            }
            else
            {
                loadSubdirJars(jarfile, alljars);
            }
        }
    }

   
    public static void main(String[] args) throws SecurityException, IllegalArgumentException,
            ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, IOException
    {
    	StringBuilder buidler = new StringBuilder();
		for(int i =0 ;args != null && i < args.length; i++)
			buidler.append(args[i]).append(" ");
		System.out.println("laucher args:"+buidler);
        run(args);
        
        
    }
    
    

    private static File computeApplicationDir(URL location, File defaultDir)
    {
        if (location == null)
        {
            System.out.println("Warning: Cannot locate the program directory. Assuming default.");
            return defaultDir;
        }
        if (!"file".equalsIgnoreCase(location.getProtocol()))
        {
            System.out.println("Warning: Unrecognized location type. Assuming default.");
            return new File(".");
        }
        String file = location.getFile();
        if (!file.endsWith(".jar") && !file.endsWith(".zip"))
        {
            try
            {
                return (new File(URLDecoder.decode(location.getFile(), "UTF-8"))).getParentFile();
            }
            catch (UnsupportedEncodingException e)
            {

            }

            System.out.println("Warning: Unrecognized location type. Assuming default.");
            return new File(location.getFile());
        }
        else
        {
            
            try
            {
                File path = null;//new File(URLDecoder.decode(location.toExternalForm().substring(6), "UTF-8")).getParentFile();
                if(!CommonLauncher.isLinux() && !CommonLauncher.isOSX())
                {
                	path = new File(URLDecoder.decode(location.toExternalForm().substring(6), "UTF-8")).getParentFile();
                }
                else
                {
                	path = new File(URLDecoder.decode(location.toExternalForm().substring(5), "UTF-8")).getParentFile();
                }
//                System.out.println("path: " + path.getAbsolutePath());
//                System.out.println("location: " + location.getPath());
//                System.out.println("external from location: " + URLDecoder.decode(location.toExternalForm().substring(6), "UTF-8"));
//                System.out.println("external from location + 6: " + URLDecoder.decode(location.toExternalForm(), "UTF-8"));
                
                return path;
            }
            catch (UnsupportedEncodingException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        System.out.println("Warning: Unrecognized location type. Assuming default.");
        return new File(location.getFile());
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

	/**
	 * @return True if the OS is an OSX derivate.
	 */
	public static final boolean isOSX() {
		return getOS().toUpperCase().contains("OS X");
	}

	/**
	 * Determine the hostname of the machine Kettle is running on
	 * 
	 * @return The hostname
	 */
	public static final String getHostname() {
		String lastHostname = "localhost";
		try {
			Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface nwi = en.nextElement();
				Enumeration<InetAddress> ip = nwi.getInetAddresses();

				while (ip.hasMoreElements()) {
					InetAddress in = (InetAddress) ip.nextElement();
					lastHostname = in.getHostName();
					// System.out.println("  ip address bound : "+in.getHostAddress());
					// System.out.println("  hostname         : "+in.getHostName());
					// System.out.println("  Cann.hostname    : "+in.getCanonicalHostName());
					// System.out.println("  ip string        : "+in.toString());
					if (!lastHostname.equalsIgnoreCase("localhost")
							&& !(lastHostname.indexOf(':') >= 0)) {
						return lastHostname;
					}
				}
			}
		} catch (SocketException e) {

		}

		return lastHostname;
	}

	/**
	 * Determins the IP address of the machine Kettle is running on.
	 * 
	 * @return The IP address
	 */
	public static final String getIPAddress() throws Exception {
		Enumeration<NetworkInterface> enumInterfaces = NetworkInterface
				.getNetworkInterfaces();
		while (enumInterfaces.hasMoreElements()) {
			NetworkInterface nwi = (NetworkInterface) enumInterfaces
					.nextElement();
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
	public static final String getIPAddress(String networkInterfaceName)
			throws SocketException {
		NetworkInterface networkInterface = NetworkInterface
				.getByName(networkInterfaceName);
		Enumeration<InetAddress> ipAddresses = networkInterface
				.getInetAddresses();
		while (ipAddresses.hasMoreElements()) {
			InetAddress inetAddress = (InetAddress) ipAddresses.nextElement();
			if (!inetAddress.isLoopbackAddress()
					&& inetAddress.toString().indexOf(":") < 0) {
				String hostname = inetAddress.getHostAddress();
				return hostname;
			}
		}
		return null;
	}

	/**
	 * Tries to determine the MAC address of the machine Kettle is running on.
	 * 
	 * @return The MAC address.
	 */
	public static final String getMACAddress() throws Exception {
		String ip = getIPAddress();
		String mac = "none";
		String os = getOS();
		String s = "";

		// System.out.println("os = "+os+", ip="+ip);

		if (os.equalsIgnoreCase("Windows NT")
				|| os.equalsIgnoreCase("Windows 2000")
				|| os.equalsIgnoreCase("Windows XP")
				|| os.equalsIgnoreCase("Windows 95")
				|| os.equalsIgnoreCase("Windows 98")
				|| os.equalsIgnoreCase("Windows Me")
				|| os.startsWith("Windows")) {
			try {
				// System.out.println("EXEC> nbtstat -a "+ip);

				Process p = Runtime.getRuntime().exec("nbtstat -a " + ip);

				// read the standard output of the command
				BufferedReader stdInput = new BufferedReader(
						new InputStreamReader(p.getInputStream()));

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
		} else if (os.equalsIgnoreCase("Linux")) {
			try {
				Process p = Runtime.getRuntime().exec("/sbin/ifconfig -a");

				// read the standard output of the command
				BufferedReader stdInput = new BufferedReader(
						new InputStreamReader(p.getInputStream()));

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
		} else if (os.equalsIgnoreCase("Solaris")) {
			try {
				Process p = Runtime.getRuntime().exec("/usr/sbin/ifconfig -a");

				// read the standard output of the command
				BufferedReader stdInput = new BufferedReader(
						new InputStreamReader(p.getInputStream()));

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
		} else if (os.equalsIgnoreCase("HP-UX")) {
			try {
				Process p = Runtime.getRuntime().exec("/usr/sbin/lanscan -a");

				// read the standard output of the command
				BufferedReader stdInput = new BufferedReader(
						new InputStreamReader(p.getInputStream()));

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
	 * is considered a space in Kettle if it is a space, a tab, a newline or a
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
	 * @param str
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
	 * @param str
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
			return rightPad(new StringBuffer(), limit);
		else
			return rightPad(new StringBuffer(ret), limit);
	}

	/**
	 * Right pad a StringBuffer: adds spaces to a string until a certain length.
	 * If the length is smaller then the limit specified, the String is
	 * truncated.
	 * 
	 * @param ret
	 *            The StringBuffer to pad
	 * @param limit
	 *            The desired length of the padded string.
	 * @return The padded String.
	 */
	public static final String rightPad(StringBuffer ret, int limit) {
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
}
