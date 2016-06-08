package org.frameworkset.web.socket.handler;

import org.frameworkset.spi.ApplicationContextAware;
import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.util.Assert;

import com.frameworkset.util.BeanUtils;

public class BeanCreatingHandlerProvider <T> implements ApplicationContextAware {

	private final Class<? extends T> handlerType;

	private BaseApplicationContext beanFactory;


	public BeanCreatingHandlerProvider(Class<? extends T> handlerType) {
		Assert.notNull(handlerType, "handlerType must not be null");
		this.handlerType = handlerType;
	}


	@Override
	public void setApplicationContext(BaseApplicationContext beanFactory) {
		if (beanFactory instanceof BaseApplicationContext) {
			this.beanFactory = (BaseApplicationContext) beanFactory;
		}
	}

	public void destroy(T handler) {
		if (this.beanFactory != null) {
			this.beanFactory.destroyBean(handler);
		}
	}


	public Class<? extends T> getHandlerType() {
		return this.handlerType;
	}

	public T getHandler() {
		if (this.beanFactory != null) {
			return this.beanFactory.createBean(this.handlerType);
		}
		else {
			return BeanUtils.instantiate(this.handlerType);
		}
	}

	@Override
	public String toString() {
		return "BeanCreatingHandlerProvider[handlerType=" + this.handlerType + "]";
	}

 

}
