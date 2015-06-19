package org.frameworkset.bigdata.impl.monitor;

import java.util.Map;

import org.frameworkset.bigdata.imp.Imp;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.event.IocLifeCycleEventListener;

public class BigdataIocLifeCycleEventListener implements
		IocLifeCycleEventListener {
	private Map<String,String> params;
	@Override
	public void afterstart(BaseApplicationContext ioccontext) {
		

	}

	@Override
	public void beforestart() {
		if(params != null)
		{
			String adminasdatanode = params.get("adminasdatanode");
			if(adminasdatanode != null && adminasdatanode.equals("true"))
				Imp.startAdminNode(true);
			else
				Imp.startAdminNode(false);
		}
		else
		{
			Imp.startAdminNode(false);
		}

	}

	@Override
	public void init(Map<String,String> params) {
		this.params = params;
		
	}

}
