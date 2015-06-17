package org.frameworkset.spi.event;

import java.util.Map;

import org.frameworkset.spi.BaseApplicationContext;

/**
 * 
 * @author biaoping.yin
 *
 */
public interface IocLifeCycleEventListener {
	void init(Map<String,String> params);
	void beforestart();
	void afterstart(BaseApplicationContext context);

}
