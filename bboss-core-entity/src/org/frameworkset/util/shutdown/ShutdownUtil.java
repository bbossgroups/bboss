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
        List<WrapperRunnable> shutdownHooks_ = null;
        synchronized (ShutdownUtil.class){
            if(shutdownHooks != null){
                shutdownHooks_ = new ArrayList<>(shutdownHooks.size());
                shutdownHooks_.addAll(shutdownHooks);

            }
        }
        if(shutdownHooks_ == null){
            shutdownHooks_ = new ArrayList<>(0);
        }
		return shutdownHooks_;
	}
	private static final Logger log = LoggerFactory.getLogger(ShutdownUtil.class);
	private static List<WrapperRunnable> shutdownHooks = new ArrayList<WrapperRunnable>();
	private static synchronized void addShutdownHook_(WrapperRunnable destroyVMHook)
	{
        if(shutdownHooks != null) {
            shutdownHooks.add(destroyVMHook);
            Collections.sort(shutdownHooks, priorComparator);
        }

	}
	/**
	 * 首先从配置文件中查找属性值，然后从jvm系统熟悉和系统环境变量中查找属性值
	 * @param property
	 * @return
	 */
	public static String getSystemEnvProperty(String property)
	{

//			Properties pros = System.getProperties();
		//Get value from jvm system propeties,just like -Dproperty=value
		String value =System.getProperty(property);
		
		if(value == null) {
			//Get value from os env ,just like property=value in user profile
			value = System.getenv(property);
		}
		return value;
	}

	/**
	 * 首先从配置文件中查找属性值，然后从jvm系统熟悉和系统环境变量中查找属性值
	 * @param property
	 * @return
	 */
	public static String getSystemEnvProperty(String property,String defaultValue)
	{
		String value = getSystemEnvProperty( property);

		return value != null? value:defaultValue;
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
			log.warn("addShutdownHook failed:",e);
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
			String enableShutdownHook = ShutdownUtil.getSystemEnvProperty("enableShutdownHook","false");
			//只有在启用自动关闭的情况下，才可以在jvm退出时自动关闭和释放资源，否则需要手动调用ShutdownUtil.shutdown()方法释放资源
			if(enableShutdownHook.equals("true")) {
				Class r = Runtime.getRuntime().getClass();
				java.lang.reflect.Method m = r.getDeclaredMethod("addShutdownHook",
						Thread.class);
				shutdownHook = new Thread(
						new Runnable() {

							public void run() {
								shutdown();

							}

						});
				m.invoke(Runtime.getRuntime(), shutdownHook);
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	/**
	 * invoke shutdown hooks by programs when application is undeployed.
	 */
	public static  void shutdown()
	{
        List<WrapperRunnable> shutdownHooks_ = null;
        synchronized (ShutdownUtil.class){
            if(shutdownHooks != null){
                shutdownHooks_ = new ArrayList<>(shutdownHooks.size());
                shutdownHooks_.addAll(shutdownHooks);
                shutdownHooks.clear();
                shutdownHooks = null;
            }
        }
        try
        {
            if(shutdownHooks_ != null)
            {

                for(int i = shutdownHooks_.size()-1; i >= 0; i --)
                {
                    try {

                        WrapperRunnable destroyVMHook = shutdownHooks_.get(i);
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

            }



        }
        catch(Exception e)
        {
            log.warn("",e);
        }
        catch(Throwable e)
        {
            log.warn("",e);
        }

    }


}
