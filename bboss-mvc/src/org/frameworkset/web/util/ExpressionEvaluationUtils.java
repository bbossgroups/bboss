/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.frameworkset.web.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.Expression;

import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;
import org.frameworkset.util.Assert;
import org.frameworkset.util.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Title: ExpressionEvaluationUtils.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-10
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class ExpressionEvaluationUtils {
	/**
	 * JSP 2.0 expression cache parameter at the servlet context level
	 * (i.e. a context-param in <code>web.xml</code>): "cacheJspExpressions".
	 */
	public static final String EXPRESSION_CACHE_CONTEXT_PARAM = "cacheJspExpressions";

	public static final String EXPRESSION_PREFIX = "${";

	public static final String EXPRESSION_SUFFIX = "}";


	private static final String EXPRESSION_CACHE_FLAG_CONTEXT_ATTR =
			ExpressionEvaluationUtils.class.getName() + ".CACHE_JSP_EXPRESSIONS";

	private static final String EXPRESSION_CACHE_MAP_CONTEXT_ATTR =
			ExpressionEvaluationUtils.class.getName() + ".JSP_EXPRESSION_CACHE";

	private static final String JSP_20_CLASS_NAME =
			"javax.servlet.jsp.el.ExpressionEvaluator";

	private static final String JAKARTA_JSTL_CLASS_NAME =
			"org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager";

	protected final static Logger logger = LoggerFactory.getLogger(ExpressionEvaluationUtils.class);
//	private static final Log logger = LogFactory.getLog(ExpressionEvaluationUtils.class);

	private static ExpressionEvaluationHelper helper;


	static {
		ClassLoader cl = ExpressionEvaluationUtils.class.getClassLoader();
		if (ClassUtils.isPresent(JSP_20_CLASS_NAME, cl)) {
			logger.debug("Found JSP 2.0 ExpressionEvaluator");
			if (ClassUtils.isPresent(JAKARTA_JSTL_CLASS_NAME, cl)) {
				logger.debug("Found Jakarta JSTL ExpressionEvaluatorManager");
				helper = new Jsp20ExpressionEvaluationHelper(new JakartaExpressionEvaluationHelper());
			}
			else {
				helper = new Jsp20ExpressionEvaluationHelper(new NoExpressionEvaluationHelper());
			}
		}
		else if (ClassUtils.isPresent(JAKARTA_JSTL_CLASS_NAME, cl)) {
			logger.debug("Found Jakarta JSTL ExpressionEvaluatorManager");
			helper = new JakartaExpressionEvaluationHelper();
		}
		else {
			logger.debug("JSP expression evaluation not available");
			helper = new NoExpressionEvaluationHelper();
		}
	}


	/**
	 * Check if the given expression value is an EL expression.
	 * @param value the expression to check
	 * @return <code>true</code> if the expression is an EL expression,
	 * <code>false</code> otherwise
	 */
	public static boolean isExpressionLanguage(String value) {
		return (value != null && value.indexOf(EXPRESSION_PREFIX) != -1);
	}

	/**
	 * Evaluate the given expression (be it EL or a literal String value)
	 * to an Object of a given type,
	 * @param attrName name of the attribute (typically a JSP tag attribute)
	 * @param attrValue value of the attribute
	 * @param resultClass class that the result should have (String, Integer, Boolean)
	 * @param pageContext current JSP PageContext
	 * @return the result of the evaluation
	 * @throws JspException in case of parsing errors, also in case of type mismatch
	 * if the passed-in literal value is not an EL expression and not assignable to
	 * the result class
	 */
	public static Object evaluate(String attrName, String attrValue, Class resultClass, PageContext pageContext)
	    throws JspException {

		if (isExpressionLanguage(attrValue)) {
			return doEvaluate(attrName, attrValue, resultClass, pageContext);
		}
		else if (attrValue != null && resultClass != null && !resultClass.isInstance(attrValue)) {
			throw new JspException("Attribute value \"" + attrValue + "\" is neither a JSP EL expression nor " +
					"assignable to result class [" + resultClass.getName() + "]");
		}
		else {
			return attrValue;
		}
	}

	/**
	 * Evaluate the given expression (be it EL or a literal String value) to an Object.
	 * @param attrName name of the attribute (typically a JSP tag attribute)
	 * @param attrValue value of the attribute
	 * @param pageContext current JSP PageContext
	 * @return the result of the evaluation
	 * @throws JspException in case of parsing errors
	 */
	public static Object evaluate(String attrName, String attrValue, PageContext pageContext)
	    throws JspException {

		if (isExpressionLanguage(attrValue)) {
			return doEvaluate(attrName, attrValue, Object.class, pageContext);
		}
		else {
			return attrValue;
		}
	}

	/**
	 * Evaluate the given expression (be it EL or a literal String value) to a String.
	 * @param attrName name of the attribute (typically a JSP tag attribute)
	 * @param attrValue value of the attribute
	 * @param pageContext current JSP PageContext
	 * @return the result of the evaluation
	 * @throws JspException in case of parsing errors
	 */
	public static String evaluateString(String attrName, String attrValue, PageContext pageContext)
	    throws JspException {

		if (isExpressionLanguage(attrValue)) {
			return (String) doEvaluate(attrName, attrValue, String.class, pageContext);
		}
		else {
			return attrValue;
		}
	}

	/**
	 * Evaluate the given expression (be it EL or a literal String value) to an integer.
	 * @param attrName name of the attribute (typically a JSP tag attribute)
	 * @param attrValue value of the attribute
	 * @param pageContext current JSP PageContext
	 * @return the result of the evaluation
	 * @throws JspException in case of parsing errors
	 */
	public static int evaluateInteger(String attrName, String attrValue, PageContext pageContext)
			throws JspException {

		if (isExpressionLanguage(attrValue)) {
			return ((Integer) doEvaluate(attrName, attrValue, Integer.class, pageContext)).intValue();
		}
		else {
			return Integer.parseInt(attrValue);
		}
	}

	/**
	 * Evaluate the given expression (be it EL or a literal String value) to a boolean.
	 * @param attrName name of the attribute (typically a JSP tag attribute)
	 * @param attrValue value of the attribute
	 * @param pageContext current JSP PageContext
	 * @return the result of the evaluation
	 * @throws JspException in case of parsing errors
	 */
	public static boolean evaluateBoolean(String attrName, String attrValue, PageContext pageContext)
	    throws JspException {

		if (isExpressionLanguage(attrValue)) {
			return ((Boolean) doEvaluate(attrName, attrValue, Boolean.class, pageContext)).booleanValue();
		}
		else {
			return Boolean.valueOf(attrValue).booleanValue();
		}
	}


	/**
	 * Actually evaluate the given expression (be it EL or a literal String value)
	 * to an Object of a given type. Supports concatenated expressions,
	 * for example: "${var1}text${var2}"
	 * @param attrName name of the attribute
	 * @param attrValue value of the attribute
	 * @param resultClass class that the result should have
	 * @param pageContext current JSP PageContext
	 * @return the result of the evaluation
	 * @throws JspException in case of parsing errors
	 */
	private static Object doEvaluate(String attrName, String attrValue, Class resultClass, PageContext pageContext)
	    throws JspException {

		Assert.notNull(attrValue, "Attribute value must not be null");
		Assert.notNull(resultClass, "Result class must not be null");
		Assert.notNull(pageContext, "PageContext must not be null");

		if (resultClass.isAssignableFrom(String.class)) {
			StringBuilder resultValue = null;
			int exprPrefixIndex = -1;
			int exprSuffixIndex = 0;
			do {
				exprPrefixIndex = attrValue.indexOf(EXPRESSION_PREFIX, exprSuffixIndex);
				if (exprPrefixIndex != -1) {
					int prevExprSuffixIndex = exprSuffixIndex;
					exprSuffixIndex = attrValue.indexOf(EXPRESSION_SUFFIX, exprPrefixIndex + EXPRESSION_PREFIX.length());
					String expr = null;
					if (exprSuffixIndex != -1) {
						exprSuffixIndex += EXPRESSION_SUFFIX.length();
						expr = attrValue.substring(exprPrefixIndex, exprSuffixIndex);
					}
					else {
						expr = attrValue.substring(exprPrefixIndex);
					}
					if (expr.length() == attrValue.length()) {
						// A single expression without static prefix or suffix ->
						// parse it with the specified result class rather than String.
						return helper.evaluate(attrName, attrValue, resultClass, pageContext);
					}
					else {
						// We actually need to concatenate partial expressions into a String.
						if (resultValue == null) {
							resultValue = new StringBuilder();
						}
						resultValue.append(attrValue.substring(prevExprSuffixIndex, exprPrefixIndex));
						resultValue.append(helper.evaluate(attrName, expr, String.class, pageContext));
					}
				}
				else {
					if (resultValue == null) {
						resultValue = new StringBuilder();
					}
					resultValue.append(attrValue.substring(exprSuffixIndex));
				}
			}
			while (exprPrefixIndex != -1 && exprSuffixIndex != -1);
			return resultValue.toString();
		}

		else {
			return helper.evaluate(attrName, attrValue, resultClass, pageContext);
		}
	}

	/**
	 * Determine whether JSP 2.0 expressions are supposed to be cached
	 * and return the corresponding cache Map, or <code>null</code> if
	 * caching is not enabled.
	 * @param pageContext current JSP PageContext
	 * @return the cache Map, or <code>null</code> if caching is disabled
	 */
	private static Map getJspExpressionCache(PageContext pageContext) {
		ServletContext servletContext = pageContext.getServletContext();
		Map cacheMap = (Map) servletContext.getAttribute(EXPRESSION_CACHE_MAP_CONTEXT_ATTR);
		if (cacheMap == null) {
			Boolean cacheFlag = (Boolean) servletContext.getAttribute(EXPRESSION_CACHE_FLAG_CONTEXT_ATTR);
			if (cacheFlag == null) {
				cacheFlag = Boolean.valueOf(servletContext.getInitParameter(EXPRESSION_CACHE_CONTEXT_PARAM));
				servletContext.setAttribute(EXPRESSION_CACHE_FLAG_CONTEXT_ATTR, cacheFlag);
			}
			if (cacheFlag.booleanValue()) {
				cacheMap = new ConcurrentHashMap();
				servletContext.setAttribute(EXPRESSION_CACHE_MAP_CONTEXT_ATTR, cacheMap);
			}
		}
		return cacheMap;
	}


	/**
	 * Internal interface for evaluating a JSP EL expression.
	 */
	private static interface ExpressionEvaluationHelper {

		public Object evaluate(String attrName, String attrValue, Class resultClass, PageContext pageContext)
				throws JspException;
	}


	/**
	 * Fallback ExpressionEvaluationHelper:
	 * always throws an exception in case of an actual EL expression.
	 */
	private static class NoExpressionEvaluationHelper implements ExpressionEvaluationHelper {

		public Object evaluate(String attrName, String attrValue, Class resultClass, PageContext pageContext)
				throws JspException {

			throw new JspException(
					"Neither JSP 2.0 nor Jakarta JSTL available - cannot parse JSP EL expression \"" + attrValue + "\"");
		}
	}


	/**
	 * Actual invocation of the Jakarta ExpressionEvaluatorManager.
	 * In separate inner class to avoid runtime dependency on Jakarta's
	 * JSTL implementation, for evaluation of non-EL expressions.
	 */
	private static class JakartaExpressionEvaluationHelper implements ExpressionEvaluationHelper {

		public Object evaluate(String attrName, String attrValue, Class resultClass, PageContext pageContext)
		    throws JspException {

			return ExpressionEvaluatorManager.evaluate(attrName, attrValue, resultClass, pageContext);
		}
	}


	/**
	 * Actual invocation of the JSP 2.0 ExpressionEvaluator.
	 * In separate inner class to avoid runtime dependency on JSP 2.0,
	 * for evaluation of non-EL expressions.
	 */
	private static class Jsp20ExpressionEvaluationHelper implements ExpressionEvaluationHelper {

		private final ExpressionEvaluationHelper fallback;

		private boolean fallbackNecessary = false;

		public Jsp20ExpressionEvaluationHelper(ExpressionEvaluationHelper fallback) {
			this.fallback = fallback;
		}

		public Object evaluate(String attrName, String attrValue, Class resultClass, PageContext pageContext)
				throws JspException {

			if (isFallbackNecessary()) {
				return this.fallback.evaluate(attrName, attrValue, resultClass, pageContext);
			}

			try {
				Map expressionCache = getJspExpressionCache(pageContext);
				if (expressionCache != null) {
					// We are supposed to explicitly create and cache JSP Expression objects.
					ExpressionCacheKey cacheKey = new ExpressionCacheKey(attrValue, resultClass);
					Expression expr = (Expression) expressionCache.get(cacheKey);
					if (expr == null) {
						expr = pageContext.getExpressionEvaluator().parseExpression(attrValue, resultClass, null);
						expressionCache.put(cacheKey, expr);
					}
					return expr.evaluate(pageContext.getVariableResolver());
				}
				else {
					// We're simply calling the JSP 2.0 evaluate method straight away.
					return pageContext.getExpressionEvaluator().evaluate(
							attrValue, resultClass, pageContext.getVariableResolver(), null);
				}
			}
			catch (ELException ex) {
				throw new JspException("Parsing of JSP EL expression \"" + attrValue + "\" failed", ex);
			}
			catch (LinkageError err) {
				logger.debug("JSP 2.0 ExpressionEvaluator API present but not implemented - using fallback", err);
				setFallbackNecessary();
				return this.fallback.evaluate(attrName, attrValue, resultClass, pageContext);
			}
		}

		private synchronized boolean isFallbackNecessary() {
			return this.fallbackNecessary;
		}

		private synchronized void setFallbackNecessary() {
			this.fallbackNecessary = true;
		}
	}


	/**
	 * Cache key class for JSP 2.0 Expression objects.
	 */
	private static class ExpressionCacheKey {

		private final String value;
		private final Class resultClass;
		private final int hashCode;

		public ExpressionCacheKey(String value, Class resultClass) {
			this.value = value;
			this.resultClass = resultClass;
			this.hashCode = this.value.hashCode() * 29 + this.resultClass.hashCode();
		}

		public boolean equals(Object obj) {
			if (!(obj instanceof ExpressionCacheKey)) {
				return false;
			}
			ExpressionCacheKey other = (ExpressionCacheKey) obj;
			return (this.value.equals(other.value) && this.resultClass.equals(other.resultClass));
		}

		public int hashCode() {
			return this.hashCode;
		}
	}

}
