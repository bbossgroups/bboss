package org.frameworkset.util.shutdown;
/**
 * Copyright 2020 bboss
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2022/1/19 15:53
 * @author biaoping.yin
 * @version 1.0
 */
public class ShutdownUtil {

	static class PriorComparator implements Comparator<WrapperRunnable> {

		public int compare(WrapperRunnable o1, WrapperRunnable o2) {
			if(o1.getProir() > o2.getProir())
				return 1;
			else if(o1.getProir() == o2.getProir())
			{
				return 0;
			}
			else
				return -1;

		}

	}
	public static class WrapperRunnable implements Runnable
	{
		private final Runnable executor;
		private int proir;
		WrapperRunnable(Runnable executor,int proir)
		{
			this.executor = executor;
			this.proir = proir;
		}
		private boolean executed = false;
		public void run()
		{
			if(executed)
				return;

			this.executor.run();
			executed = true;
		}
		public int getProir() {
			return proir;
		}
		public void setProir(int proir) {
			this.proir = proir;
		}

	}

	public static List<WrapperRunnable> getShutdownHooks(){
		return shutdownHooks;
	}
	private static final Logger log = LoggerFactory.getLogger(ShutdownUtil.class);
	private static List<WrapperRunnable> shutdownHooks = new ArrayList<WrapperRunnable>();
	private static synchronized void addShutdownHook_(WrapperRunnable destroyVMHook)
	{
		shutdownHooks.add(destroyVMHook);
		Collections.sort(shutdownHooks, priorComparator);

	}
	/**
	 * 添加系统中停止时的回调程序
	 *
	 * @param destroyVMHook
	 */
	public static void addShutdownHook(Runnable destroyVMHook,int proir) {
		try {
			// use reflection and catch the Exception to allow PoolMan to work
			// with 1.2 VM's
			destroyVMHook = new WrapperRunnable(destroyVMHook,proir);
			addShutdownHook_((WrapperRunnable)destroyVMHook);
//			Class r = Runtime.getRuntime().getClass();
//			java.lang.reflect.Method m = r.getDeclaredMethod("addShutdownHook",
//					new Class[] { Thread.class });
//			m.invoke(Runtime.getRuntime(), new Object[] { new Thread(
//					destroyVMHook) });
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 添加系统中停止时的回调程序
	 *
	 * @param destroyVMHook
	 */
	public static void addShutdownHook(Runnable destroyVMHook) {
		addShutdownHook(destroyVMHook,-1);
	}
	static PriorComparator priorComparator = new PriorComparator();
	private static Thread shutdownHook;
	static
	{
		try {
			Class r = Runtime.getRuntime().getClass();
			java.lang.reflect.Method m = r.getDeclaredMethod("addShutdownHook",
					Thread.class);
			shutdownHook  = new Thread(
					new Runnable(){

						public void run() {
							shutdown();

						}

					});
			m.invoke(Runtime.getRuntime(), shutdownHook);
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	/**
	 * invoke shutdown hooks by programs when application is undeployed.
	 */
	public static  void shutdown()
	{

			try
			{
				if(shutdownHooks != null)
				{

					for(int i = shutdownHooks.size()-1; i >= 0; i --)
					{
						try {

							WrapperRunnable destroyVMHook = shutdownHooks.get(i);
							destroyVMHook.run();
							Thread.sleep(1000);
						} catch (InterruptedException e) {

						}
						catch (Exception e) {
							if(log.isWarnEnabled()){
								log.warn("",e);
							}
						}
					}
					shutdownHooks.clear();
					shutdownHooks = null;
				}



			}
			catch(Exception e)
			{
				e.printStackTrace();
//				log.warn("",e);
			}
			catch(Throwable e)
			{
//				log.warn("",e);
				e.printStackTrace();
			}
			finally
			{
//				if(shutdownHook != null)
//					Runtime.getRuntime().removeShutdownHook(shutdownHook);
			}

		}


}
