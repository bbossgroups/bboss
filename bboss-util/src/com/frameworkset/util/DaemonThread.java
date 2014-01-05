package com.frameworkset.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

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
			this.init = init;
			this.oldModifiedTime =  file.lastModified();
			
		}
		 private File file;
		 private long lastModifiedTime;
		 private long oldModifiedTime; 
		 private ResourceInitial init;
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
		/**
		 * @return the init
		 */
		public ResourceInitial getInit() {
			return init;
		}
		/**
		 * @param init the init to set
		 */
		public void setInit(ResourceInitial init) {
			this.init = init;
		}
		
		public void checkChanged()
		{
			if(file == null)
				return ;
			 boolean exist = file.exists(); 
	         if(!exist)
	         {
	         	//init.destroy();
	         	return;
	         }
	         this.lastModifiedTime = file.lastModified();
	         if(this.oldModifiedTime != this.lastModifiedTime)
	         {
	             //System.out.println("Reload changed file：" + file.getAbsolutePath());
	             log.debug("Reload changed file：" + file.getAbsolutePath());
	             this.oldModifiedTime = this.lastModifiedTime;
	             try {
					init.reinit();
					log.debug("Reload changed file " + file.getAbsolutePath() + " sucessed." );
				} catch (Exception e) {
					log.debug("Reload changed file " + file.getAbsolutePath() + " failed:" ,e);
				} 
	             
	         }
		}
		 
		 
	}
    private static Logger log = Logger.getLogger(DaemonThread.class);

//    private long lastModifiedTime;
//    private long oldModifiedTime;
    private long refresh_interval = 5000;
    private List<FileBean> files = new ArrayList<FileBean>();
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
    private boolean containFile(File file)
    {
    	if(this.files == null || files.size() <= 0 )
    		return false;
    	if(file == null)
    		return false;
    	for(FileBean f:files)
    	{
    		if(f.getFile() != null && f.getFile().getAbsolutePath().equals(file.getAbsolutePath()))
    			return true;
    	}
    	return false;
    }
    public void addFile(File file,ResourceInitial init)
    {   
    	if(this.containFile(file))
    	{
    		log.debug(file.getAbsolutePath() + " has been monitored,ignore this operation.");
    		return;
    	}
    	this.files.add(new FileBean(file,init));
    	 log.debug("Add file " + file.getAbsolutePath() + " to damon thread which moniting file modified.");
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
        this.files.add(new FileBean(file,init));
        log.debug("Add file " + file.getAbsolutePath() + " to damon thread which moniting file modified.");
        this.setDaemon(true);
    }

    public DaemonThread(File file,ResourceInitial init)
    {

       
//        this.oldModifiedTime = file.lastModified();
//        this.file = file;
//        this.init = init;
    	this.files.add(new FileBean(file,init));
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
            for(FileBean f:files)
            {
            	if(stopped)
            		break;
            	f.checkChanged();
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
    	synchronized(this)
    	{
    		this.notifyAll();
    	}
    	 if(files != null)
    	 {
    		 files.clear();
    		 files = null;
    	 }
    	 
    }
    
    
}
