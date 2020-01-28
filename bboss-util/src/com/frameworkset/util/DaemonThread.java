package com.frameworkset.util;

import com.frameworkset.daemon.FileBean;
import com.frameworkset.daemon.ResourceNameSpace;
import com.frameworkset.daemon.WrapperResourceInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
public class DaemonThread extends java.lang.Thread 
{


    private static Logger log = LoggerFactory.getLogger(DaemonThread.class);

//    private long lastModifiedTime;
//    private long oldModifiedTime;
    private long refresh_interval = 5000;
    private Map<String, FileBean> files = new HashMap<String,FileBean>();
	private Map<String, ResourceNameSpace> triggers = new HashMap<String,ResourceNameSpace>();

	//    private ResourceInitial init;
    private boolean started = false;
    private boolean stopped = false;
    public DaemonThread(String fileName,ResourceInitial init)
    {
    	this(fileName,5000,init);
    }
    
    public DaemonThread(long refresh_interval,String name)
    {
    	super(name);
    	this.refresh_interval = refresh_interval;
    	 this.setDaemon(true);
    }
    
    public void addFile(String fileName,ResourceInitial init)
    {
    	if(this.stopped || fileName == null)
    		return;
    	try {
			File file = new File(fileName);
			if (!file.exists()) {
				URL confURL = ResourceInitial.class.getClassLoader().getResource(fileName);
				if (confURL != null)
					file = new File(confURL.getPath());
			}
			addFile(file, fileName, init);
		}
		catch (Exception e){
			if (log.isErrorEnabled())
				log.error("addFile file to monitor Thread failed,fileName:"+fileName ,e);
		}
//    	this.files.add(new FileBean(file,init));
//    	 log.debug("Add file " + file.getAbsolutePath() + " to damon thread that moniting changed files.");
    }
    private FileBean containFile(File file,String fileName)
    {
    	if(this.files == null || files.size() <= 0 )
    		return null;
    	if(file == null)
    		return null;
    	FileBean f = files.get(file.getAbsolutePath());
    	if(f == null)
    		return null;
    	if(f.isRemoveflag())
			f.setRemoveflag(false);
    	return f;
    	
    }

	/**
	 *
	 * @param file 触发文件更新的文件句柄
	 * @param fileName  实际的文件，有可能和file不一致（file可能为jar或者其他合法的压缩包文件，包含多个需要刷新的file）
	 * @param init
	 */
    public  void addFile(File file,String fileName,ResourceInitial init)
    {   
    	if(this.stopped || file == null )
    		return;
    	if( !file.exists())
    	{
    		if(log.isInfoEnabled() )
    			log.info(fileName+"@"+file.getAbsolutePath()+" 对应的文件不存在，忽略修改检测.");
    		return;
    	}
    	try {
			synchronized (lock) {
				FileBean f = this.containFile(file, fileName);
				if (f == null) {
					this.files.put(file.getAbsolutePath(), new FileBean(file, new WrapperResourceInit(init, fileName)));
//	    		log.debug(file.getAbsolutePath() + " has been monitored,ignore this operation.");
//	    		return;
				} else {
					f.addResourceInit(new WrapperResourceInit(init, fileName));
				}
				if (log.isInfoEnabled())
					log.info("Add file " + fileName + "@" + file.getAbsolutePath() + " to monitor thread that moniting changed files.");

			}
		}
    	catch (Exception e){
			if (log.isErrorEnabled())
				log.error("Add file " + fileName + "@" + file.getAbsolutePath() + " to monitor thread that moniting changed files failed:",e);
		}
    }
	/**
	 * determine the OS name
	 *
	 * @return The name of the OS
	 */
	public static final String getOS() {
		String osname = System.getProperty("os.name");
		return osname;
	}
	public static Boolean isWindow = null;
	/**
	 * @return True if the OS is a Windows derivate.
	 */
	public static final boolean isWindows() {
		if(isWindow != null)
			return isWindow.booleanValue();
		synchronized (DaemonThread.class){
			if(isWindow != null)
				return isWindow.booleanValue();
			isWindow = getOS().startsWith("Windows");
		}
		return isWindow;
	}
	/**
	 *
	 * @param file 触发文件更新的文件句柄
	 * @param init
	 */
	public  void addFile(File file,ResourceInitial init)
	{
		if(this.stopped || file == null)
			return;
		String fileName = file.getAbsolutePath();
		addFile(  file, fileName, init);
	}

	public void addFile(URL fileURL,String fileName,ResourceInitial init)
	{
		if(this.stopped )
			return;
		if(fileName == null){
			log.info("Ignore addFile Null file to change monitor Thread:fileName:"+fileName + ",fileURL:"+fileURL);
			return;
		}
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				if (fileURL != null) {
					String fileUrl = fileURL.getPath();
					if (log.isInfoEnabled()) {
						log.info("Use out package file URL to monitor: " + fileUrl);
					}
					file = new File(fileUrl);
					if (!file.exists()) {
						int idx = fileUrl.indexOf("!");
						if (idx > 0) {
							fileUrl = fileUrl.substring(0, idx);
							idx = fileUrl.indexOf("file:");
							if (idx == -1) {
								idx = fileUrl.indexOf("file:");
							}
							if (idx >= 0) {
								if (isWindows()) {
									fileUrl = fileUrl.substring(idx + 6);
								} else {
									fileUrl = fileUrl.substring(idx + 5);
								}
							}
							file = new File(fileUrl);
						}
					}
				}
			}
			addFile(file, fileName, init);
		}
		catch (Exception e){
			if (log.isErrorEnabled())
				log.error("addFile file to monitor Thread failed,fileName:"+fileName + ",fileURL:"+fileURL ,e);
		}
	}


	public void addResource(ResourceNameSpace resourceNameSpace,ResourceInitial init)
	{
		if(this.stopped )
			return;
		if(resourceNameSpace == null){
			log.info("Ignore addResource Null ResourceNameSpace to  monitor change Thread.");
			return;
		}

		try {
			synchronized (lock) {
				ResourceNameSpace f = this.triggers.get(resourceNameSpace.getNameSpace());

				if (f == null) {
					this.triggers.put(resourceNameSpace.getNameSpace(), resourceNameSpace);
					f = resourceNameSpace;
				}

				f.addResourceInit(new WrapperResourceInit(init, resourceNameSpace.getNameSpace()));

				if (log.isInfoEnabled())
					log.info("Add ResourceInitial[" + resourceNameSpace.getNameSpace() + "] to thread that moniting file change .");

			}
		}
		catch (Exception e){
			if (log.isErrorEnabled())
				log.error("Add ResourceInitial[" + resourceNameSpace.getNameSpace() + "] to thread that moniting file change failed:",e);
		}

	}
    public  void removeFile(String filepath)
    {   
    	File file = new File(filepath);
        if(!file.exists())
        {
        	URL confURL = ResourceInitial.class.getClassLoader().getResource(filepath);
        	if(confURL != null)
        		file = new File(confURL.getPath() );
        }
    	removeFile( file);
    }
    public  void removeFile(File file)
    {   
    	
    	synchronized(lock)
    	{
    		if(this.files == null || files.size() <= 0 )
        		return ;
        	if(file == null)
        		return ;
        	FileBean f = files.get(file.getAbsolutePath());
        	if(f != null)
        	{
        		f.setRemoveflag(true);
        		if(log.isInfoEnabled())
        			log.info("marked  file " + file.getAbsolutePath() + " to be removed from monitor thread that moniting changed files.");
        	}
//        	for(FileBean f:files)
//        	{
//        		if(f.getFile() != null && f.getFile().getAbsolutePath().equals(file.getAbsolutePath()))
//        		{
////        			files.remove(f);
//        			f.setRemoveflag(true);
//        		}
//        	}
	    	 
    	}
    }
    
    
    
    public DaemonThread(String fileName,long refresh_interval,ResourceInitial init)
    {
    	super(fileName);
    	this.refresh_interval = refresh_interval > 0 ?refresh_interval:10000;
        File file = new File(fileName);
        if(!file.exists())
        {
        	URL confURL = ResourceInitial.class.getClassLoader().getResource(fileName);
        	if(confURL != null)
        		file = new File(confURL.getPath() );
        }
//        this.oldModifiedTime = file.lastModified();
        
//        this.init = init;
        if(!file.exists())
    	{
    		if(log.isInfoEnabled())
    			log.info(file.getAbsolutePath()+" 对应的文件不存在，忽略修改检测.");
    		return;
    	}
        this.files.put(file.getAbsolutePath(),new FileBean(file,new WrapperResourceInit(init,file.getAbsolutePath())));
        if(log.isInfoEnabled())
        	log.info("Add file " + file.getAbsolutePath() + " to monitor thread that moniting changed files.");
        this.setDaemon(true);
    }
    private Object lock = new Object();
    public DaemonThread(File file,ResourceInitial init)
    {

       
//        this.oldModifiedTime = file.lastModified();
//        this.file = file;
//        this.init = init;
//    	this.files.add(new FileBean(file,init));
    	 this.files.put(file.getAbsolutePath(),new FileBean(file,new WrapperResourceInit(init,file.getAbsolutePath())));
		if(log.isInfoEnabled())
			log.info("Add file " + file.getAbsolutePath() + " to monitor thread that moniting changed files.");
        this.setDaemon(true);
    }


    public boolean started()
    {
        return started;
    }
    private void triggerFiles(){
		List<FileBean> changedFiles = new ArrayList<FileBean>();

		synchronized(lock)
		{
			//modified by biaoping.yin on 2015.03.09:忽略扫描异常，如果出现异常继续扫描
			try
			{
				Iterator<Entry<String, FileBean>> entries = files.entrySet().iterator();
				while(entries.hasNext())
				{
					if(stopped)
						break;
					Entry<String, FileBean> entry = entries.next();
					FileBean f = entry.getValue();

					if(f.isRemoveflag())
						continue;
					try
					{


						if(log.isTraceEnabled()){
							String filePath = f.getFile() != null?f.getFile().getAbsolutePath():"";

							log.trace("Thread["+this.getName()+"] Check file["+filePath+"] .");
						}
						if(f.checkChanged()){
							if(log.isInfoEnabled()){
								String filePath = f.getFile() != null?f.getFile().getAbsolutePath():"";

								log.info("Thread["+this.getName()+"] Monitor file["+filePath+"] changed.");
							}
							changedFiles.add(f);
						}
					}
					catch(Exception e)
					{
						if(log.isInfoEnabled()) {
							String filePath = f.getFile() != null ? f.getFile().getAbsolutePath() : "null";

							String tname = this.getName() != null ? this.getName() : "null";
							log.info("Thread[" + tname + "] Monitor changed file[" + filePath + "] exception:", e);
						}
					}
				}


			}
			catch(Exception e)
			{
				if(log.isInfoEnabled())
					log.info("Thread["+this.getName()+"] Monitor changed files exception:",e);
			}
		}

		if(changedFiles.size() > 0)
		{
			for(FileBean f:changedFiles)
			{
				if(stopped)
					break;
				try
				{
					f.reinit();
				}
				catch(Exception e)
				{
					if(log.isInfoEnabled()) {
						String filePath = f.getFile() != null ? f.getFile().getAbsolutePath() : "null";


						String tname = this.getName() != null ? this.getName() : "null";
						log.info("Thread[" + tname + "] Reinit Monitor changed file[" + filePath + "] exception:", e);
					}
				}
			}
		}
	}
    private void triggerResources(){
		List<ResourceNameSpace> changedFiles = new ArrayList<ResourceNameSpace>();

		synchronized(lock)
		{
			//modified by biaoping.yin on 2015.03.09:忽略扫描异常，如果出现异常继续扫描
			try
			{
				Iterator<Entry<String, ResourceNameSpace>> entries = triggers.entrySet().iterator();
				while(entries.hasNext())
				{
					if(stopped)
						break;
					Entry<String, ResourceNameSpace> entry = entries.next();
					ResourceNameSpace f = entry.getValue();
					String filePath = f.getNameSpace();

					try
					{


						if(log.isTraceEnabled()){


							log.trace("Thread["+this.getName()+"] Check resource["+filePath+"] .");
						}
						if(f.checkChanged()){
							if(log.isInfoEnabled()){

								log.info("Thread["+this.getName()+"] resource["+filePath+"] changed.");
							}
							changedFiles.add(f);
						}
					}
					catch(Exception e)
					{
						if(log.isInfoEnabled()) {

							String tname = this.getName() != null ? this.getName() : "null";
							log.info("Thread[" + tname + "] Monitor changed resource[" + filePath + "] exception:", e);
						}
					}
				}


			}
			catch(Exception e)
			{
				if(log.isInfoEnabled())
					log.info("Thread["+this.getName()+"] Monitor changed files exception:",e);
			}
		}

		if(changedFiles.size() > 0)
		{
			for(ResourceNameSpace f:changedFiles)
			{
				if(stopped)
					break;
				String filePath = f.getNameSpace();
				try
				{
					f.reinit();
				}
				catch(Exception e)
				{
					if(log.isInfoEnabled()) {


						String tname = this.getName() != null ? this.getName() : "null";
						log.info("Thread[" + tname + "] Reinit Monitor changed resource[" + filePath + "] exception:", e);
					}
				}
			}
		}
	}
	private boolean hasFiles(){
    	return files != null && files.size() > 0;
	}
	private boolean hasTriggers(){
		return triggers != null && triggers.size() > 0;
	}

	public void run()
    {
		if(log.isInfoEnabled())
			log.info("Start check files is changed or not.if some files is changed,the resources of these files will be refreshed use ResourceInit interface.");
        started = true;
        while(true)
        {
        	if(stopped)
        		break;
            //System.out.println("Check module file's last modified time.");
            try {
                sleep(refresh_interval);
            } catch (InterruptedException ex) {
//                notifyAll();
            	break;
            }
            if(!hasFiles() && !hasTriggers()){
            	if(log.isTraceEnabled()) {
					String tname = this.getName() != null ? this.getName() : "null";
					log.trace("Thread[" + tname + "] Ignore Monitor change resources : No resources to monitor.");
				}
            	continue;
            }
            if(hasFiles())
				triggerFiles();
            if(hasTriggers()){
				triggerResources();
			}
            

        }
    }
    
    public void stopped()
    {
    	if(this.stopped)
    		return;
    	this.stopped = true;
    	synchronized(lock){
    		if(files != null)
	       	 {
	       		 files.clear();
//	       		 files = null;
	       	 }
    		if(triggers != null){
    			triggers.clear();
			}
    		lock.notifyAll();
    	}
//    	synchronized(this)
//    	{
//    		this.notifyAll();
//    	}
//    	 
    	 
    }
    
    
}
