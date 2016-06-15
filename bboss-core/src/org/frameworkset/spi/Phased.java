package org.frameworkset.spi;

/**
 * Interface for objects that may participate in a phased
 * process such as lifecycle management.
 *
 * @author Mark Fisher
 * @since 3.0
 * @see SmartLifecycle
 */
public interface Phased {

	/**
	 * Return the phase value of this object.
	 */
	int getPhase();

}
