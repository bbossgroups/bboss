package org.frameworkset.util.beans;


public class NotReadablePropertyException  extends InvalidPropertyException {

	/**
	 * Create a new NotReadablePropertyException.
	 * @param beanClass the offending bean class
	 * @param propertyName the offending property
	 */
	public NotReadablePropertyException(Class beanClass, String propertyName) {
		super(beanClass, propertyName,
				"Bean property '" + propertyName + "' is not readable or has an invalid getter method: " +
				"Does the return type of the getter match the parameter type of the setter?");
	}

	/**
	 * Create a new NotReadablePropertyException.
	 * @param beanClass the offending bean class
	 * @param propertyName the offending property
	 * @param msg the detail message
	 */
	public NotReadablePropertyException(Class beanClass, String propertyName, String msg) {
		super(beanClass, propertyName, msg);
	}

}
