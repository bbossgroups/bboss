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

import java.security.Permission;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;
import org.frameworkset.spi.ApplicationContext;
import org.frameworkset.spi.assemble.ProMap;
import org.frameworkset.spi.remote.BaseRPCIOHandler;
import org.frameworkset.spi.remote.RPCAddress;
import org.frameworkset.spi.remote.Target;
import org.frameworkset.spi.remote.Util;



/**
 * <p>
 * Title: RMIServer.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2010-6-16 下午07:12:45
 * @author biaoping.yin
 * @version 1.0
 */
public class RMIServer {

	public int PORT = 1099;
	private static Logger log = Logger.getLogger(RMIServer.class);

	private String permission = null;

	private long CONNECT_TIMEOUT = 30;

	public long getCONNECT_TIMEOUT() {
		return CONNECT_TIMEOUT;
	}

	private boolean started = false;
	private RPCAddress localAddress;
	private String ip = "127.0.0.1";

	public boolean started() {
		return this.started;
	}

	public boolean validateAddress(RPCAddress address) {
		// 首先判断地址是否在地址范围中

		// return ClinentTransport.validateAddress(address);
		// fix rpc
		return true;
	}

	private static RMIServer server;

	public static String iohandler_name = "rpc.rmi.RPCServerIoHandler";

	public static BaseRPCIOHandler getBaseRPCIOHandler() {
		return (BaseRPCIOHandler) Util.defaultContext
				.getBeanObject(iohandler_name);
	}

	public static RMIServer getRMIServer() {
		if (server != null)
			return server;
		synchronized (RMIServer.class) {
			if (server != null)
				return server;
			server = (RMIServer)Util.defaultContext.getBeanObject("rpc.rmi.server");
		}

		return server;
	}

	private static class ShutDownRMIServer implements Runnable {
		RMIServer server;

		ShutDownRMIServer(RMIServer server) {
			this.server = server;
		}

		public void run() {
			server.stop();

		}

	}

	public RPCAddress getLocalAddress() {
		return this.localAddress;
	}

	public ProMap conparams = null;

	public String address;
	
	private String serveruuid = "default";

	public RMIServer(ProMap conparams) {
		this.conparams = conparams;
		CONNECT_TIMEOUT = conparams.getInt("connection.timeout", 15) * 1000;
		String ip = conparams.getString("connection.bind.ip");
		if (ip != null)
			this.ip = ip;
		PORT = conparams.getInt("connection.bind.port");
		this.serveruuid = conparams.getString("server_uuid","default"); 
		this.localAddress = new RPCAddress(this.ip, PORT,
				Target.BROADCAST_TYPE_RMI);
		this.permission = ip + ":" + PORT;
//		address = "//" + ip + ":" + PORT + "/" + serveruuid + "/rpcService";
		address = "//" + ip + ":" + PORT + "/rpcService";
	}

	Thread task;
	private java.util.concurrent.locks.ReentrantLock lock = new ReentrantLock();
	Exception e;

	
	
	public void start() throws RMIException {
		if (started)
			return;
		else {
			e = null;
			lock.lock();
			try {
				if (started)
					return;
				// Create a class that handles sessions, incoming and outgoing
				// data
				System.out.println("Start rmi server.....");

				task = new Thread(new Runnable() {

					public void run() {
						

						try {
							
							
							RMIUtil.startPort(PORT);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						try {
							RMIRpcService rpcService = new RMIRpcService();
							
							if(RMIUtil.rebindservice(address, rpcService))
							{
								System.out.println("绑定组件地址：" + address);
								System.out.println("RMI监听于IP：" + ip);
								System.out.println("RMI监听于端口：" + PORT);
								started = true;
								ApplicationContext
										.addShutdownHook(new ShutDownRMIServer(
												server));
								synchronized (task) {
									task.wait();
								}
								System.out.println("task exit.");
							}
							else
							{
								throw new Exception("绑定服务失败，绑定组件地址：" + address+"，IP：" + ip+"，端口：" + PORT + ",具体原因请查看系统日志。");
							}
						
							
							
							
						} catch (Exception e1) {
							e = e1;
						}

					}

				});
				task.start();
				while (true) {
					if (!started) {
						if (e != null) {
							throw new RMIException(e);

						} else {
							synchronized (this) {
								wait(1000);
							}
						}
					} else {
						break;
					}
				}

				// System.setSecurityManager(new MySecurityManager());
				// try {
				// RMIRpcService rpcService = new RMIRpcService();
				// LocateRegistry.createRegistry(PORT);
				// Naming.bind("rmi://"+ip+":"+PORT+"/rpcService", rpcService);
				//		      
				// System.out.println("RMI监听于IP："+ip);
				// System.out.println("RMI监听于端口："+PORT);
				// } catch (Exception e) {
				// e.printStackTrace();
				// }
				//				
				// this.started = true;
			} catch (RMIException e) {
				throw e;
			} catch (Exception e) {
				throw new RMIException(e);
			} finally {
				lock.unlock();
			}
		}

	}

	public static void main(String[] args) throws Exception {
		RMIServer.getRMIServer().start();
		// RMIServer.getRMIServer().stop();
		// Class.forName("String");
	}

	public void stop() {
		if (!this.started)
			return;
		log.debug("Stop rmi server [" + address + "] begin.");
		System.out.println("Stop rmi server [" + address + "] begin.");
		if (server == null)
			return;
		// acceptor.unbind();
		
			if(RMIUtil.unbindService(address))
			{
				synchronized (task) {
					task.notifyAll();
				}
			}
		
		this.started = false;
		log.debug("Stop rmi server [" + "" + "] end.");
		System.out.println("Stop rmi server [" + address + "] end.");
	}

	public static class MySecurityManager extends SecurityManager {

		public void checkPermission(Permission perm) {
			// System.out.println("perm:" + perm);
			// System.out.println("perm.getName():" + perm.getName());
			// if(perm.getName().equals(permission)
			// || "getProperty.networkaddress.cache.ttl".equals(perm.getName())
			// || "sun.net.inetaddr.ttl".equals(perm.getName())
			// ||
			// "getProperty.networkaddress.cache.negative.ttl".equals(perm.getName())
			//    						
			// || "sun.rmi.server.logLevel".equals(perm.getName())
			// || "sun.rmi.log.useOld".equals(perm.getName())
			// || "java.util.logging.manager".equals(perm.getName())
			// || "shutdownHooks".equals(perm.getName())
			// || "control".equals(perm.getName())
			// || "java.rmi.server.ignoreStubClasses".equals(perm.getName())
			// || "sun.rmi.client.logCalls".equals(perm.getName())
			// || "java.rmi.server.logCalls".equals(perm.getName())
			// || "sun.rmi.server.exceptionTrace".equals(perm.getName())
			// || "sun.rmi.server.suppressStackTraces".equals(perm.getName())
			// || "java.rmi.server.randomIDs".equals(perm.getName())
			// || "getProperty.security.provider.1".equals(perm.getName())
			// || "getProperty.security.provider.2".equals(perm.getName())
			// || "getProperty.security.provider.3".equals(perm.getName())
			// || perm.getName().startsWith("getProperty.security.provider.")
			// || perm.getName().startsWith("suppressAccessChecks")
			// ||perm.getName().startsWith("com.sun.security.")
			//    						
			//    						
			//    						
			//    						
			//    						
			//    						
			//    						
			//    						
			//    						
			//    						
			//    						
			//    						
			//    						
			// )
			// return ;
			// else
			// {
			// super.checkPermission(perm);
			// }
			return;
			// System.out.println("perm.getActions();:"+perm.getActions());

		}

		public void checkPermission(Permission perm, Object context) {
			// System.out.println("perm:" + perm);
			// System.out.println("context:" + context);
			// if (perm.getName().equals(permission))
			// return;
			// else {
			// super.checkPermission(perm, context);
			// }
		}
	}

	public long getConnectTimeout() {
		
		return this.CONNECT_TIMEOUT;
	}

}
