package org.frameworkset.util.beans;


public class NotWritablePropertyException  extends InvalidPropertyException {

	private String[] possibleMatches = null;


	/**
	 * Create a new NotWritablePropertyException.
	 * @param beanClass the offending bean class
	 * @param propertyName the offending property name
	 */
	public NotWritablePropertyException(Class beanClass, String propertyName) {
		super(beanClass, propertyName,
				"Bean property '" + propertyName + "' is not writable or has an invalid setter method: " +
				"Does the return type of the getter match the parameter type of the setter?");
	}

	/**
	 * Create a new NotWritablePropertyException.
	 * @param beanClass the offending bean class
	 * @param propertyName the offending property name
	 * @param msg the detail message
	 */
	public NotWritablePropertyException(Class beanClass, String propertyName, String msg) {
		super(beanClass, propertyName, msg);
	}

	/**
	 * Create a new NotWritablePropertyException.
	 * @param beanClass the offending bean class
	 * @param propertyName the offending property name
	 * @param msg the detail message
	 * @param cause the root cause
	 */
	public NotWritablePropertyException(Class beanClass, String propertyName, String msg, Throwable cause) {
		super(beanClass, propertyName, msg, cause);
	}

	/**
	 * Create a new NotWritablePropertyException.
	 * @param beanClass the offending bean class
	 * @param propertyName the offending property name
	 * @param msg the detail message
	 * @param possibleMatches suggestions for actual bean property names
	 * that closely match the invalid property name
	 */
	public NotWritablePropertyException(Class beanClass, String propertyName, String msg, String[] possibleMatches) {
		super(beanClass, propertyName, msg);
		this.possibleMatches = possibleMatches;
	}


	/**
	 * Return suggestions for actual bean property names that closely match
	 * the invalid property name, if any.
	 */
	public String[] getPossibleMatches() {
		return possibleMatches;
	}

}
