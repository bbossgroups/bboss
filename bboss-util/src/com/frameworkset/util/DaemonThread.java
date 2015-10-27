package com.frameworkset.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.frameworkset.util.DaemonThread.FileBean;

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
	static class FileBean
	{
		 public FileBean(File file, 
				ResourceInitial init) {
			super();
			this.file = file;	
			this.inits = new ArrayList<ResourceInitial>();
			inits.add(init);
			this.oldModifiedTime =  file.lastModified();
			
		}
		 private boolean removeflag = false;
		 private File file;
		 private long lastModifiedTime;
		 private long oldModifiedTime; 
		 private List<ResourceInitial> inits;
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
		 * @return the lastModifiedTime
		 */
		public long getLastModifiedTime() {
			return lastModifiedTime;
		}
		/**
		 * @param lastModifiedTime the lastModifiedTime to set
		 */
		public void setLastModifiedTime(long lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
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
		
		
		public boolean checkChanged()
		{
			if(file == null)
				return false;
			 boolean exist = file.exists(); 
	         if(!exist)
	         {
	         	//init.destroy();
	         	return false;
	         }
	         this.lastModifiedTime = file.lastModified();
	         if(this.oldModifiedTime != this.lastModifiedTime)
	         {
	        	 //begin 1 resolved java.util.ConcurrentModificationException by biaoping.yin on 2015.03.09
//	             //System.out.println("Reload changed file：" + file.getAbsolutePath());
//	             log.debug("Reload changed file：" + file.getAbsolutePath());
//	             this.oldModifiedTime = this.lastModifiedTime;
//	             try {
//					init.reinit();
//					log.debug("Reload changed file " + file.getAbsolutePath() + " sucessed." );
//				} catch (Exception e) {
//					log.debug("Reload changed file " + file.getAbsolutePath() + " failed:" ,e);
//				} 
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
		public void reinit()
		{
			 //System.out.println("Reload changed file：" + file.getAbsolutePath());
            log.debug("Reload changed file：" + file.getAbsolutePath());
            this.oldModifiedTime = file.lastModified();
            try {
            	for(int i = 0; inits != null && i < this.inits.size(); i++)
            	{
            		ResourceInitial init = inits.get(i);
            		try {
						init.reinit();
					} catch (Exception e) {
						log.debug("Reload changed file " + file.getAbsolutePath() + " failed:" ,e);
					}
            	}
            	
				log.debug("Reload changed file " + file.getAbsolutePath() + " sucessed." );
			} catch (Exception e) {
				log.debug("Reload changed file " + file.getAbsolutePath() + " failed:" ,e);
			} 
		}
		//end 1 resolved java.util.ConcurrentModificationException by biaoping.yin on 2015.03.09 
		public boolean isRemoveflag() {
			return removeflag;
		}
		public void setRemoveflag(boolean removeflag) {
			this.removeflag = removeflag;
		}
		public void addResourceInit(ResourceInitial init) {
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
			else
				return true;//兼容旧版本,没有实现UUIDResource接口的初始化资源初始化接口只允许存在一个ResourceInitial
		}
		 
	}
    private static Logger log = Logger.getLogger(DaemonThread.class);

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
    	File file = new File(fileName);
        if(!file.exists())
        {
        	URL confURL = ResourceInitial.class.getClassLoader().getResource(fileName);
        	if(confURL != null)
        		file = new File(confURL.getPath() );
        }
        addFile(file,init);
//    	this.files.add(new FileBean(file,init));
//    	 log.debug("Add file " + file.getAbsolutePath() + " to damon thread which moniting file modified.");
    }
    private FileBean containFile(File file)
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
    public  void addFile(File file,ResourceInitial init)
    {   
    	if(!file.exists())
    	{
    		log.debug(file.getAbsolutePath()+" 对应的文件不存在，忽略修改检测.");
    		return;
    	}
    	synchronized(lock)
    	{
    		FileBean f = this.containFile(file);
	    	if(f == null)
	    	{
	    		this.files.put(file.getAbsolutePath(), new FileBean(file,init));
//	    		log.debug(file.getAbsolutePath() + " has been monitored,ignore this operation.");
//	    		return;
	    	}
	    	else
	    	{
	    		f.addResourceInit(init);
	    	}
	    	 log.debug("Add file " + file.getAbsolutePath() + " to damon thread which moniting file modified.");
	    	
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
        		log.debug("marked  file " + file.getAbsolutePath() + " to be removed from damon thread which moniting file modified.");
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
        this.files.put(file.getAbsolutePath(),new FileBean(file,init));
        log.debug("Add file " + file.getAbsolutePath() + " to damon thread which moniting file modified.");
        this.setDaemon(true);
    }
    private Object lock = new Object();
    public DaemonThread(File file,ResourceInitial init)
    {

       
//        this.oldModifiedTime = file.lastModified();
//        this.file = file;
//        this.init = init;
//    	this.files.add(new FileBean(file,init));
    	 this.files.put(file.getAbsolutePath(),new FileBean(file,init));
    	log.debug("Add file " + file.getAbsolutePath() + " to damon thread which moniting file modified.");
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
            if(files == null || files.size() == 0)
            	continue;
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
		            		if(f.checkChanged())
		            			changedFiles.add(f);
		            	}
		            	catch(Exception e)
		            	{
		            		String filePath = f.getFile() != null?f.getFile().getAbsolutePath():"null";
		            		String tname = this.getName() != null?this.getName():"null";
		            		log.debug("check file["+filePath+"] modified thread["+tname+"]  exception occur:",e);
		            	}
            		}
		            
		            
            	}
            	catch(Exception e)
            	{
            		log.debug("check file modified thread["+this.getName()+"] error occur:",e);
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
	            		String filePath = f.getFile() != null?f.getFile().getAbsolutePath():"null";
	            		String tname = this.getName() != null?this.getName():"null";
	            		log.debug("check file["+filePath+"] modified thread["+tname+"] exception occur:",e);
	            	}
            	}
            }
            
//            boolean exist = file.exists(); 
//            if(!exist)
//            {
//            	//init.destroy();
//            	break;
//            }
//            this.lastModifiedTime = file.lastModified();
//            if(this.oldModifiedTime != this.lastModifiedTime)
//            {
//                //System.out.println("Reload changed file：" + file.getAbsolutePath());
//                log.debug("Reload changed file：" + file.getAbsolutePath());
//                this.oldModifiedTime = this.lastModifiedTime;
//                init.reinit(); 
//                
//            }
        }
    }
    
    public void stopped()
    {
    	this.stopped = true;
    	synchronized(lock){
    		if(files != null)
	       	 {
	       		 files.clear();
	       		 files = null;
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
