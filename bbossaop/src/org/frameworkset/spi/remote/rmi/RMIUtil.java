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

package org.frameworkset.spi.remote.rmi;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.frameworkset.spi.SOAFileApplicationContext;
import org.frameworkset.spi.assemble.ProMap;
import org.frameworkset.util.ResourceUtils;

/**
 * <p>Title: RMIUtil.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2011-5-18 下午03:01:08
 * @author biaoping.yin
 * @version 1.0
 */
public class RMIUtil {
	public static boolean enved = false;
	private static Logger log = Logger.getLogger(RMIUtil.class);
	private static java.util.concurrent.locks.ReentrantLock evnlock = new ReentrantLock();
	private static long connectiontimeout = 60 * 1000;
	private static boolean connectiontimeoutsetted = false;
	
	private static void initConnectionTimeout()
	{
		
				
		SOAFileApplicationContext context = null;
		try
		{
			URL file = ResourceUtils.getFileURL("org/frameworkset/spi/manager-rpc-rmi.xml");
			context = new SOAFileApplicationContext (file,"UTF-8");
			ProMap map = context.getMapProperty("rpc.protocol.rmi.params");
			if(map == null)
			{
				
			}
			else
			{
				connectiontimeout = map.getLong("connection.timeout", 60*1000);
			}
			
			
		}
		catch (Exception e)
		{
			connectiontimeout = 60*1000;
		}
		
		finally
		{
			if(context != null)
				context.destroy(true);
		}
		
	}
	public static void setEnv()
	{
		if(enved )
			return;
		else
		{
			evnlock.lock();
			try
			{
				if(enved )
					return;
				System
				.setProperty(
						"java.security.policy", getPolicy()); //$NON-NLS-1$
				
		
				try {
					//System.setSecurityManager(new RMISecurityManager());
				
					enved = true;	
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			finally
			{
				evnlock.unlock();
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
		if (!file.endsWith(".jar") && !file.endsWith(".zip")) {
			try {
				return (new File(URLDecoder.decode(location.getFile(), "UTF-8")))
						;
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
							.substring(6), "UTF-8"));
				} else {
					path = new File(URLDecoder.decode(location.toExternalForm()
							.substring(5), "UTF-8"));
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
	public static String getPolicy() {
		URL location = (RMIServer.class).getProtectionDomain().getCodeSource()
				.getLocation();
		File appDir = computeApplicationDir(location, new File("."));
		if(appDir.isFile())
		{
//			File configurationFile = new File(appDir, "rmi.server.policy");
			String appDir_ = "jar:file:/"+ appDir.getAbsolutePath() +"!/rmi.server.policy";
			//jar:file:/.../.../xxx.jar!/.../.../zzz.class
			
			System.out.println("appDir_:"+appDir_);
			return appDir_;
			
		}
		else
		{
			File configurationFile = new File(appDir, "rmi.server.policy");
			return configurationFile.getAbsolutePath();
		}
	}
	private static final Map<String,Object> porttraces = new HashMap<String,Object> ();
	private static final Object traceobject = new Object();
	
	public static void startPort(int PORT)
	{
		try {
			String key = PORT + "";
			if(porttraces.containsKey(PORT + ""))
				return;
			RMIUtil.setEnv();
			LocateRegistry.createRegistry(PORT);
			porttraces.put(key, traceobject);
		} catch (Exception e) {
			log.warn(e);
		}
	}
	
	public static boolean rebindservice(String address,Remote rpcService)
	{
		try {
			RMIUtil.setEnv();
			Naming.rebind(address,  rpcService);
			return true;
		} catch (RemoteException e) {
//			System.out.println("address="+address);
//			e.printStackTrace();
			log.error("address="+address,e);
		} catch (MalformedURLException e) {
//			System.out.println("MalformedURLException address="+address);
//			e.printStackTrace();
			log.error("MalformedURLException address="+address,e);
		}
		 catch (Exception e) {
//			 System.out.println("MalformedURLException address="+address);
//			 e.printStackTrace();
				log.error("MalformedURLException address="+address,e);
			}
		 return false;
	}
	
	public static boolean  unbindService(String serviceaddress)
	{
		try {
			RMIUtil.setEnv();
			Naming.unbind(serviceaddress);
			return true;
		} catch (RemoteException e) {
			log.error("serviceaddress="+serviceaddress,e);
		} catch (MalformedURLException e) {
			log.error("serviceaddress="+serviceaddress,e);
		} catch (NotBoundException e) {
			log.error("serviceaddress="+serviceaddress,e);
		}
		catch (Exception e) {
			log.error("serviceaddress="+serviceaddress,e);
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T lookupService(String address,Class<T> serviceType)
	{
		try {
			RMIUtil.setEnv();
			if(!connectiontimeoutsetted)
			{
				initConnectionTimeout();
				System.setProperty("sun.rmi.transport.connectionTimeout",connectiontimeout + "");
				connectiontimeoutsetted = true;
			}
			return (T)Naming.lookup(address);
		} catch (MalformedURLException e) {
			log.error(e.getMessage(),e);
		} catch (RemoteException e) {
			log.error(e.getMessage(),e);
		} catch (NotBoundException e) {
			log.error(e.getMessage(),e);
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}
	
	public static Object lookupService(String address)
	{
		try {
			RMIUtil.setEnv();
			if(!connectiontimeoutsetted)
			{
				initConnectionTimeout();
				System.setProperty("sun.rmi.transport.connectionTimeout",connectiontimeout + "");
				connectiontimeoutsetted = true;
			}
			return Naming.lookup(address);
		} catch (MalformedURLException e) {
			log.error(e.getMessage(),e);
		} catch (RemoteException e) {
			log.error(e.getMessage(),e);
		} catch (NotBoundException e) {
			log.error(e.getMessage(),e);
		}
		catch (Exception e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}
	
}
