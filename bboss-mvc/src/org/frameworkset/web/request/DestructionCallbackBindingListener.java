package org.frameworkset.web.request;

import java.io.Serializable;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.frameworkset.web.servlet.context.RequestAttributes;
import org.frameworkset.web.servlet.context.ServletRequestAttributes;

/**
 * Adapter that implements the Servlet HttpSessionBindingListener interface,
 * wrapping a session destruction callback.
 *
 * @author Juergen Hoeller
 * @since 3.0
 * @see RequestAttributes#registerDestructionCallback
 * @see ServletRequestAttributes#registerSessionDestructionCallback
 */
@SuppressWarnings("serial")
public class DestructionCallbackBindingListener implements HttpSessionBindingListener, Serializable {

	private final Runnable destructionCallback;


	/**
	 * Create a new DestructionCallbackBindingListener for the given callback.
	 * @param destructionCallback the Runnable to execute when this listener
	 * object gets unbound from the session
	 */
	public DestructionCallbackBindingListener(Runnable destructionCallback) {
		this.destructionCallback = destructionCallback;
	}


	@Override
	public void valueBound(HttpSessionBindingEvent event) {
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		this.destructionCallback.run();
	}

}
