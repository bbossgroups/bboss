package org.frameworkset.spi;

/**
 * A common interface defining methods for start/stop lifecycle control.
 * The typical use case for this is to control asynchronous processing.
 * <b>NOTE: This interface does not imply specific auto-startup semantics.
 * Consider implementing {@link SmartLifecycle} for that purpose.</b>
 *
 * <p>Can be implemented by both components (typically a bboss bean defined in a
 * bboss context) and containers  (typically a bboss {@link ApplicationContext}
 * itself). Containers will propagate start/stop signals to all components that
 * apply within each container, e.g. for a stop/restart scenario at runtime.
 *
 * <p>Can be used for direct invocations or for management operations via JMX.
 * In the latter case, the  
 * will typically be defined with an
 * ,
 * restricting the visibility of activity-controlled components to the Lifecycle
 * interface.
 *
 * <p>Note that the Lifecycle interface is only supported on <b>top-level singleton
 * beans</b>. On any other component, the Lifecycle interface will remain undetected
 * and hence ignored. Also, note that the extended {@link SmartLifecycle} interface
 * provides integration with the application context's startup and shutdown phases.
 *
 * @author Juergen Hoeller
 * @since 2.0
 * @see SmartLifecycle

 */
public interface Lifecycle {

	/**
	 * Start this component.
	 * <p>Should not throw an exception if the component is already running.
	 * <p>In the case of a container, this will propagate the start signal to all
	 * components that apply.
	 * @see SmartLifecycle#isAutoStartup()
	 */
	default void start(){
        
    }

    default void start(String path){
        start();
    }

    /**
	 * Stop this component, typically in a synchronous fashion, such that the component is
	 * fully stopped upon return of this method. Consider implementing {@link SmartLifecycle}
	 * and its {@code stop(Runnable)} variant when asynchronous stop behavior is necessary.
	 * <p>Note that this stop notification is not guaranteed to come before destruction: On
	 * regular shutdown, {@code Lifecycle} beans will first receive a stop notification before
	 * the general destruction callbacks are being propagated; however, on hot refresh during a
	 * context's lifetime or on aborted refresh attempts, only destroy methods will be called.
	 * <p>Should not throw an exception if the component isn't started yet.
	 * <p>In the case of a container, this will propagate the stop signal to all components
	 * that apply.
	 * @see SmartLifecycle#stop(Runnable)
	 * @see DisposableBean#destroy()
	 */
	void stop();

	/**
	 * Check whether this component is currently running.
	 * <p>In the case of a container, this will return {@code true} only if <i>all</i>
	 * components that apply are currently running.
	 * @return whether the component is currently running
	 */
	boolean isRunning();
}
