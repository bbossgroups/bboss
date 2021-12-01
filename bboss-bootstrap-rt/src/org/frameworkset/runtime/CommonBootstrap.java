package org.frameworkset.runtime;
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

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2020</p>
 * @Date 2021/11/27 17:21
 * @author biaoping.yin
 * @version 1.0
 */
public class CommonBootstrap implements CommonBootstrapInf{
	private static Logger logger = LoggerFactory.getLogger(CommonBootstrap.class);

	@Override
	public void startup(String propertfile,ClassLoader cl,String mainclass,File appDir,String args[],URL classpathEntries[]){
		try {

			if (mainclass == null) {
				logger.error("Invalid main-class and exit.");
				System.exit(1);
			}

			logger.info("Startup {} use config file:{}", mainclass,propertfile);
			// Object instance = mainClass.newInstance();
			// startup(String[] serverinfo,String plugins[])

			StringBuilder classes = new StringBuilder();
			classes.append("Load resources:\r\n");
			for (int i = 0; i < classpathEntries.length; i++) {
				URL url = classpathEntries[i];
				classes.append("	["+i+"]"+url + "\r\n");
			}
			logger.info(classes.toString());
			Class mainClass = cl.loadClass(mainclass);
			try {

				Method setAppdir = mainClass.getMethod("setAppdir", new Class[]{File.class});
				if (setAppdir != null) {
					setAppdir.invoke(null, new Object[]{appDir});
				}
			} catch (Exception e) {
				logger.warn("Ignore set Appdir variable for " + mainclass + ":" + e.getMessage());
			}
			Method method = mainClass.getMethod("main", new Class[]{String[].class});
			method.invoke(null, new Object[]{args});
			logger.info("Started {} success.",mainclass);
		}
		catch (Exception e){
			logger.error("Start "+ mainclass + " failed:",e);
			System.exit(1);
		}
		catch (Throwable e){
			logger.error("Start "+ mainclass + " failed:",e);
			System.exit(1);
		}
	}
	@Override
	public void shutdown(String propertfile,File appDir,int shutdownLevel,URL classpathEntries[]) {

		logger.info("Shutdown use config file:{}", propertfile);
		StringBuilder classes = new StringBuilder();
		classes.append("Load resources:\r\n");
		for (int i = 0; i < classpathEntries.length; i++) {
			URL url = classpathEntries[i];
			classes.append("	["+i+"]"+url + "\r\n");
		}
		logger.info(classes.toString());
		String pidname = CommonLauncher.getProperty("pidfile", "pid");
		File pid = pidname.startsWith("/")?new File(pidname):new File(appDir, pidname);
		List<String> pids = new ArrayList<String>();
		FileReader read = null;
		try {
			if (!pid.exists()) {
				logger.warn("进程号文件" + pid.getAbsolutePath() + "不存在。。。。");
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
			logger.warn("",e);
		} finally {
			if (read != null)
				try {
					read.close();
				} catch (IOException e) {
					logger.warn("",e);
				}
		}

		if(pids.size() > 0){
			try {
				killproc(pids,  shutdownLevel);
				checkProccessesStopped(pids);
			} catch (IOException e) {
				logger.warn("",e);
			} catch (InterruptedException e) {
				logger.warn("",e);
			}
		}
		else{
			logger.info("没有需要关闭的进程信息.");
		}
		pid.delete();

		logger.info("shutdown end.");

	}

	private void getError(Process process,String processId){
		ByteArrayOutputStream baos = null;
		InputStream os = null;
		try {

			baos = new ByteArrayOutputStream();
			os = process.getErrorStream();
			byte[] b = new byte[256];
			while (os.read(b) > 0) {
				baos.write(b);
				logger.info("b:"+b);
			}
			baos.flush();
			String s = baos.toString();

			logger.info("Check Processor {} Exist result string:{},flag {} ",processId,s);


		} catch (IOException e) {
			logger.error(processId, e);
		} finally {
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {

				}
			}
			if(baos != null){
				try {
					baos.close();
				} catch (IOException e) {

				}
			}

		}
	}

	private boolean checkProcessorExist(String processId){

			boolean flag = false;
			Process process = null;
			String command = "";
			ByteArrayOutputStream baos = null;
			InputStream os = null;
			try {
				if (OSInfo.isWindows()) {
					command ="cmd /c tasklist  /FI \"PID eq " + processId + "\"";
					logger.info("Check Processor {} exist command {} ",processId,command);
					process = Runtime.getRuntime().exec(command);
				} else  {
					command = "ps -ef | awk '{print $2}'| grep -w " + processId;
					logger.info("Check Processor {} exist command {} ",processId,command);
					String[] cmd = {"/bin/sh", "-c", command};

					process = Runtime.getRuntime().exec(cmd);
				}


//				getError(process,processId);
				baos = new ByteArrayOutputStream();
				os = process.getInputStream();
				byte[] b = new byte[256];
				while (os.read(b) > 0) {
					baos.write(b);

				}
				baos.flush();
				String s = baos.toString();

				flag = s.trim().equals(processId);
				logger.info("Check Processor {} Exist result string:{},flag {} ",processId,s,flag);


			} catch (IOException e) {
				logger.error(processId, e);
			} finally {
				if(os != null){
					try {
						os.close();
					} catch (IOException e) {

					}
				}
				if(baos != null){
					try {
						baos.close();
					} catch (IOException e) {

					}
				}
				if (process != null) {
					process.destroy();
				}
			}
			return flag;

	}
	/**
	 * 检查进程是否已经关闭
	 * @param pids
	 */
	private void checkProccessesStopped(final List<String> pids){
		Thread check = new Thread( new Runnable() {
			@Override
			public void run() {
				for(String processId: pids) {

					boolean exist = true;
					do {

						exist = checkProcessorExist(processId);
						logger.info("Process {} shutdowning ....exist {}",processId,exist);
						try {
							sleep(1000);
						} catch (InterruptedException e) {
							exist = false;
							break;
						}
					}while(exist);
					logger.info("Process {} shutdown completed.",processId);
				}
			}
		},"checkProccessesStopped");
		check.start();
		try {
			check.join();
		}
		catch (InterruptedException e){

		}

	}

	public void killproc(List<String> pids,int shutdownLevel) throws IOException, InterruptedException{
		Process proc = null;

		if (OSInfo.isWindows()) {
			StringBuilder builder = new StringBuilder();
			builder.append("TASKKILL /F");
			for(int i = 0; i < pids.size(); i ++){
				builder.append(" /PID ").append(pids.get(i));
			}
			builder.append(" /T");
			String cmd = builder.toString();
			logger.info("Execute "+cmd);
			proc = Runtime.getRuntime().exec(cmd);
//			${dbinitpath}

		}
		else
		{

			StringBuilder builder = new StringBuilder();
			if(shutdownLevel == -1)
				builder.append("kill ");
			else
				builder.append("kill -").append(shutdownLevel);
			for(int i = 0; i < pids.size(); i ++){
				builder.append(" ").append(pids.get(i));
			}
			String cmd = builder.toString();
			logger.info("Execute "+cmd);
			proc = Runtime.getRuntime().exec(cmd);
		}
		StreamGobbler error = new StreamGobbler( proc.getErrorStream(),"INFO");

		StreamGobbler normal = new StreamGobbler( proc.getInputStream(),"NORMAL");
		error.start();
		normal.start();

		int exitVal = proc.waitFor();
	}

	@Override
	public void restart(String propertfile,ClassLoader cl,String mainclass,File appDir,int shutdownLevel ,URL classpathEntries[],String args[]){

		logger.info("Restart use config file:{}", propertfile);
		shutdown(  propertfile,  appDir,  shutdownLevel,  classpathEntries);
//		try {
//			//关闭服务后，停顿2秒
//			if(shutdownLevel != 9)
//				Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			logger.warn("",e);
//			return;
//		}
		startup(  propertfile,cl,  mainclass,  appDir,  args,  classpathEntries);
	}


}
