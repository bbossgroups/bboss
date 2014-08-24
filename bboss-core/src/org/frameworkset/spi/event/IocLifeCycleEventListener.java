package org.frameworkset.spi.event;

import org.frameworkset.spi.BaseApplicationContext;

/**
 * 
 * @author biaoping.yin
 *
 */
public interface IocLifeCycleEventListener {
	void beforestart();
	void afterstart(BaseApplicationContext context);

}
