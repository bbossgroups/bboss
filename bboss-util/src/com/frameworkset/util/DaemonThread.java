package com.frameworkset.util;

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
	static class WrapperResourceInit implements ResourceInitial,UUIDResource{
		private ResourceInitial resourceInitial;
		private String fileName;
		public WrapperResourceInit(ResourceInitial resourceInitial,String fileName){
			this.resourceInitial = resourceInitial;
			this.fileName = fileName;
		}

		@Override
		public void reinit() {
			resourceInitial.reinit();
		}

		@Override
		public String getUUID() {
			if(resourceInitial instanceof UUIDResource)
				return ((UUIDResource)resourceInitial).getUUID();
			return this.fileName;
		}
	}
	static class FileBean
	{
		 public FileBean(File file,
						 WrapperResourceInit init) {
			super();
			this.file = file;
			this.inits = new ArrayList<WrapperResourceInit>();
			inits.add(init);
			this.oldModifiedTime =  file.lastModified();
			
		}
		 private boolean removeflag = false;
		 private File file;
//		 private long lastModifiedTime;
		 private long oldModifiedTime; 
		 private List<WrapperResourceInit> inits;
		/**
		 * @return the file
		 */
		public File getFile() {
			return file;
		}
		/**
		 * @param file the file to set
		 */
		public void setFile(File file) {
			this.file = file;
		}

		/**
		 * @return the oldModifiedTime
		 */
		public long getOldModifiedTime() {
			return oldModifiedTime;
		}
		/**
		 * @param oldModifiedTime the oldModifiedTime to set
		 */
		public void setOldModifiedTime(long oldModifiedTime) {
			this.oldModifiedTime = oldModifiedTime;
		}
		
		
		public  boolean checkChanged()
		{
			if(file == null)
				return false;
			 boolean exist = file.exists(); 
	         if(!exist)
	         {
	         	//init.destroy();
	         	return false;
	         }
	         long lastModifiedTime = file.lastModified();
	         if(this.oldModifiedTime != lastModifiedTime)
	         {

	             return true;
	         }
	         else
	         {
	        	 return false;
	         }
		}
		
		/**
		 * 1 resolved java.util.ConcurrentModificationException by biaoping.yin on 2015.03.09
		 */
		public synchronized void reinit()
		{
			 //System.out.println("Reload changed file：" + file.getAbsolutePath());
			if(log.isDebugEnabled()) {
				log.debug("Reload resources in changed file：" + file.getAbsolutePath());
			}
			long lastModifiedTime = file.lastModified();
			if(lastModifiedTime == this.oldModifiedTime)
				return;
			else {
				//检查文件是否已经读写完毕，通过文件长度来判断（不一定准确）
				long length = file.length();
				long last = length;
				do {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						break;
					}
					length = file.length();
					if (last == length) {
						lastModifiedTime = file.lastModified();
						break;

					} else {
						last = length;
					}
				}
				while(true);
			}
			this.oldModifiedTime = lastModifiedTime;

			for(int i = 0; inits != null && i < this.inits.size(); i++)
			{
				WrapperResourceInit init = inits.get(i);
				try {
					init.reinit();
					if(log.isDebugEnabled()) {
						log.debug("Reload changed resource file " + init.getUUID()+"@"+ file.getAbsolutePath() + " sucessed.");
					}
				} catch (Exception e) {
					if(log.isDebugEnabled())
						log.debug("Reload changed resource  file " + init.getUUID()+"@"+ file.getAbsolutePath() + " failed:" ,e);
				}
			}


		}
		//end 1 resolved java.util.ConcurrentModificationException by biaoping.yin on 2015.03.09 
		public boolean isRemoveflag() {
			return removeflag;
		}
		public void setRemoveflag(boolean removeflag) {
			this.removeflag = removeflag;
		}
		public void addResourceInit(WrapperResourceInit init) {
			if(!this.contain(init))
				this.inits.add(init);
			
		}
		
		private boolean contain(ResourceInitial init)
		{
			
			if(init instanceof UUIDResource)
			{
				String uuid = ((UUIDResource)init).getUUID();
				for(int i = 0; i < this.inits.size(); i ++)
				{
					ResourceInitial initOld = this.inits.get(i);
					if(initOld instanceof UUIDResource)
					{
						if(((UUIDResource)initOld).getUUID().equals(uuid))
							return true;
					}
				}
				return false;
			}
			else {

					return true;//兼容旧版本,没有实现UUIDResource接口的初始化资源初始化接口只允许存在一个ResourceInitial

			}
		}


	}
    private static Logger log = LoggerFactory.getLogger(DaemonThread.class);

//    private long lastModifiedTime;
//    private long oldModifiedTime;
    private long refresh_interval = 5000;
    private Map<String,FileBean> files = new HashMap<String,FileBean>();
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
    	if(this.stopped )
    		return;
    	File file = new File(fileName);
        if(!file.exists())
        {
        	URL confURL = ResourceInitial.class.getClassLoader().getResource(fileName);
        	if(confURL != null)
        		file = new File(confURL.getPath() );
        }
        addFile(file,fileName,init);
//    	this.files.add(new FileBean(file,init));
//    	 log.debug("Add file " + file.getAbsolutePath() + " to damon thread which moniting file modified.");
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
    	if(this.stopped )
    		return;
    	if(!file.exists())
    	{
    		if(log.isDebugEnabled())
    			log.debug(fileName+"@"+file.getAbsolutePath()+" 对应的文件不存在，忽略修改检测.");
    		return;
    	}
    	
    	synchronized(lock)
    	{
    		FileBean f = this.containFile(file,fileName);
	    	if(f == null)
	    	{
	    		this.files.put(file.getAbsolutePath(), new FileBean(file,new WrapperResourceInit(init,fileName)));
//	    		log.debug(file.getAbsolutePath() + " has been monitored,ignore this operation.");
//	    		return;
	    	}
	    	else
	    	{
	    		f.addResourceInit(new WrapperResourceInit(init,fileName));
	    	}
	    	if(log.isDebugEnabled())
	    	 	log.debug("Add file " + fileName+"@"+ file.getAbsolutePath() + " to monitor thread which moniting file modified.");
	    	
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
		if(this.stopped )
			return;
		String fileName = file.getAbsolutePath();
		addFile(  file, fileName, init);
	}
	public  void addFile(URL fileURL,String fileName,ResourceInitial init)
	{
		if(this.stopped )
			return;
		File file = new File(fileName);
		if(!file.exists())
		{
			if(fileURL != null) {
				String fileUrl = fileURL.getPath();
				log.debug("Use file URL to monitor: "+fileUrl);
				file = new File(fileUrl);
				if(!file.exists()){
					int idx = fileUrl.indexOf("!");
					if(idx > 0) {
						fileUrl = fileUrl.substring(0, idx);
						idx = fileUrl.indexOf("file:");
						if (idx == -1) {
							idx = fileUrl.indexOf("file:");
						}
						if(idx >= 0) {
							if(isWindows()) {
								fileUrl = fileUrl.substring(idx + 6);
							}
							else{
								fileUrl = fileUrl.substring(idx + 5);
							}
						}
						file = new File(fileUrl);
					}
				}
			}
		}
		addFile(file,fileName,init);
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
        		log.debug("marked  file " + file.getAbsolutePath() + " to be removed from monitor thread which moniting file modified.");
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
    		log.debug(file.getAbsolutePath()+" 对应的文件不存在，忽略修改检测.");
    		return;
    	}
        this.files.put(file.getAbsolutePath(),new FileBean(file,new WrapperResourceInit(init,file.getAbsolutePath())));
        log.debug("Add file " + file.getAbsolutePath() + " to monitor thread which moniting file modified.");
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
    	log.debug("Add file " + file.getAbsolutePath() + " to monitor thread which moniting file modified.");
        this.setDaemon(true);
    }


    public boolean started()
    {
        return started;
    }
    public void run()
    {
    	log.info("Start check files is changed or not.if some files is changed,the resources of these files will be refreshed use ResourceInit interface.");
        started = true;
        for(;;)
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
            if(files == null || files.size() == 0){
            	if(log.isTraceEnabled()) {
					String tname = this.getName() != null ? this.getName() : "null";
					log.trace("Thread[" + tname + "] Ignore Monitor change Files : No file to be monitor.");
				}
            	continue;
            }
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
		            			if(log.isDebugEnabled()){
		            				String filePath = f.getFile() != null?f.getFile().getAbsolutePath():"";

		            				log.debug("Thread["+this.getName()+"] Monitor file["+filePath+"] changed.");
		            			}
		            			changedFiles.add(f);
		            		}
		            	}
		            	catch(Exception e)
		            	{
							if(log.isDebugEnabled()) {
								String filePath = f.getFile() != null ? f.getFile().getAbsolutePath() : "null";

								String tname = this.getName() != null ? this.getName() : "null";
								log.debug("Thread[" + tname + "] Monitor changed file[" + filePath + "] exception:", e);
							}
		            	}
            		}
		            
		            
            	}
            	catch(Exception e)
            	{
            		log.debug("Thread["+this.getName()+"] Monitor changed files exception:",e);
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
	            		if(log.isDebugEnabled()) {
							String filePath = f.getFile() != null ? f.getFile().getAbsolutePath() : "null";


							String tname = this.getName() != null ? this.getName() : "null";
							log.debug("Thread[" + tname + "] Reinit Monitor changed file[" + filePath + "] exception:", e);
						}
	            	}
            	}
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
    		lock.notifyAll();
    	}
//    	synchronized(this)
//    	{
//    		this.notifyAll();
//    	}
//    	 
    	 
    }
    
    
}
