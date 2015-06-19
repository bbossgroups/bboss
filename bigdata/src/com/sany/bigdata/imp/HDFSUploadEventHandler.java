package com.sany.bigdata.imp;

import java.util.Map;

import org.apache.log4j.Logger;
import org.frameworkset.event.Event;
import org.frameworkset.event.Listener;
import org.frameworkset.remote.EventUtils;
import org.jgroups.Address;

public class HDFSUploadEventHandler implements Listener<Map<String,TaskConfig>>{

	private static Logger log = Logger.getLogger(HDFSUploadEventHandler.class); 
	@Override
	public void handle(Event<Map<String, TaskConfig>> e) {
		
		
		
		TaskConfig config = null;
		Map<String, TaskConfig> tasksMap = e.getSource();
		if(tasksMap != null && tasksMap.size() > 0)
		{
			
			if(e.isLocal())
			{
				config = tasksMap.get("rundirect");
			}
			else
			{
				Address localAddress = EventUtils.getLocalAddress();
				if(localAddress != null)
				{
					config = tasksMap.get(localAddress.toString());
				}
			}
		}
		if(config != null)
		{
			log.info("receive task:"+config +" begin to execute job.");
			final TaskConfig config_ = config;
			new Thread(new Runnable(){
				public void run()
				{
					ExecutorJob job = new ExecutorJob(); 
					job.execute(config_);
					log.info("Execute job end:"+config_ );
				}
			}).start();
			
			
			
			
		}
		else
		{
			log.info("Execute job end:没有需要本节点处理的 任务.");
		}
		
		
		
	}

}
