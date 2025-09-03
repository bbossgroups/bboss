package org.frameworkset.spi;

/**
 * An extension of the {@link Lifecycle} interface for those objects that require to
 * be started upon ApplicationContext refresh and/or shutdown in a particular order.
 * The {@link #isAutoStartup()} return value indicates whether this object should
 * be started at the time of a context refresh. The callback-accepting
 * {@link #stop(Runnable)} method is useful for objects that have an asynchronous
 * shutdown process. Any implementation of this interface <i>must</i> invoke the
 * callback's run() method upon shutdown completion to avoid unnecessary delays
 * in the overall ApplicationContext shutdown.
 *
 * <p>This interface extends {@link Phased}, and the {@link #getPhase()} method's
 * return value indicates the phase within which this Lifecycle component should
 * be started and stopped. The startup process begins with the <i>lowest</i>
 * phase value and ends with the <i>highest</i> phase value (Integer.MIN_VALUE
 * is the lowest possible, and Integer.MAX_VALUE is the highest possible). The
 * shutdown process will apply the reverse order. Any components with the
 * same value will be arbitrarily ordered within the same phase.
 *
 * <p>Example: if component B depends on component A having already started, then
 * component A should have a lower phase value than component B. During the
 * shutdown process, component B would be stopped before component A.
 *
 * <p>Any explicit "depends-on" relationship will take precedence over
 * the phase order such that the dependent bean always starts after its
 * dependency and always stops before its dependency.
 *
 * <p>Any Lifecycle components within the context that do not also implement
 * SmartLifecycle will be treated as if they have a phase value of 0. That
 * way a SmartLifecycle implementation may start before those Lifecycle
 * components if it has a negative phase value, or it may start after
 * those components if it has a positive phase value.
 *
 * <p>Note that, due to the auto-startup support in SmartLifecycle,
 * a SmartLifecycle bean instance will get initialized on startup of the
 * application context in any case. As a consequence, the bean definition
 * lazy-init flag has very limited actual effect on SmartLifecycle beans.
 *
 * @author Mark Fisher
 * @since 3.0
 * @see LifecycleProcessor
 */
public interface SmartLifecycle extends Lifecycle, Phased {

	/**
	 * Returns {@code true} if this {@code Lifecycle} component should get
	 * started automatically by the container at the time that the containing
	 * {@link ApplicationContext} gets refreshed.
	 * <p>A value of {@code false} indicates that the component is intended to
	 * be started through an explicit {@link #start()} call instead, analogous
	 * to a plain {@link Lifecycle} implementation.
	 * @see #start()
	 * @see #getPhase()
	 * @see LifecycleProcessor#onRefresh()
	 */
	boolean isAutoStartup();

	/**
	 * Indicates that a Lifecycle component must stop if it is currently running.
	 * <p>The provided callback is used by the {@link LifecycleProcessor} to support
	 * an ordered, and potentially concurrent, shutdown of all components having a
	 * common shutdown order value. The callback <b>must</b> be executed after
	 * the {@code SmartLifecycle} component does indeed stop.
	 * <p>The {@link LifecycleProcessor} will call <i>only</i> this variant of the
	 * {@code stop} method; i.e. {@link Lifecycle#stop()} will not be called for
	 * {@code SmartLifecycle} implementations unless explicitly delegated to within
	 * the implementation of this method.
	 * @see #stop()
	 * @see #getPhase()
	 */
	void stop(Runnable callback);
}
