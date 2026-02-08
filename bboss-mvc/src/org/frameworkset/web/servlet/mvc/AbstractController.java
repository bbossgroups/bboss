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
package org.frameworkset.web.servlet.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;

import org.frameworkset.spi.BaseApplicationContext;
import org.frameworkset.spi.BeanNameAware;
import org.frameworkset.spi.InitializingBean;
import org.frameworkset.spi.assemble.Pro;
import org.frameworkset.util.annotations.HttpMethod;
import org.frameworkset.web.servlet.Controller;
import org.frameworkset.web.servlet.HandlerExecutionChain;
import org.frameworkset.web.servlet.ModelAndView;
import org.frameworkset.http.HttpMethodsContainer;
import org.frameworkset.web.servlet.handler.annotations.ExcludeMethod;
import org.frameworkset.web.servlet.support.WebContentGenerator;
import org.frameworkset.web.util.WebUtils;

/**
 * <p>Title: AbstractController.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-10-6
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class AbstractController  extends WebContentGenerator implements Controller, BeanNameAware, InitializingBean {

	private boolean synchronizeOnSession = false;
    private String beanName;
    private HttpMethodsContainer supportedMethods;
    /**
     * Set the name of the bean in the bean factory that created this bean.
     * <p>Invoked after population of normal bean properties but before an
     * init callback such as {@link InitializingBean#afterPropertiesSet()}
     * or a custom init-method.
     * @param name the name of the bean in the factory.
     * Note that this name is the actual bean name used in the factory, which may
     * differ from the originally specified name: in particular for inner bean
     * names, the actual bean name might have been made unique through appending
     * "#..." suffixes. Use the  BeanFactoryUtils#originalBeanName(String)
     * method to extract the original bean name (without suffix), if desired.
     */
    public void setBeanName(String name){
        this.beanName = name;
    }

    public HttpMethodsContainer getSupportedMethods() {
        return supportedMethods;
    }

    /**
	 * Set if controller execution should be synchronized on the session,
	 * to serialize parallel invocations from the same client.
	 * <p>More specifically, the execution of the <code>handleRequestInternal</code>
	 * method will get synchronized if this flag is "true". The best available
	 * session mutex will be used for the synchronization; ideally, this will
	 * be a mutex exposed by HttpSessionMutexListener.
	 * <p>The session mutex is guaranteed to be the same object during
	 * the entire lifetime of the session, available under the key defined
	 * by the <code>SESSION_MUTEX_ATTRIBUTE</code> constant. It serves as a
	 * safe reference to synchronize on for locking on the current session.
	 * <p>In many cases, the HttpSession reference itself is a safe mutex
	 * as well, since it will always be the same object reference for the
	 * same active logical session. However, this is not guaranteed across
	 * different servlet containers; the only 100% safe way is a session mutex.

	 */
	public final void setSynchronizeOnSession(boolean synchronizeOnSession) {
		this.synchronizeOnSession = synchronizeOnSession;
	}

	/**
	 * Return whether controller execution should be synchronized on the session.
	 */
	public final boolean isSynchronizeOnSession() {
		return this.synchronizeOnSession;
	}
    /**
     * Invoked by a BeanFactory after it has set all bean properties supplied
     * (and satisfied BeanFactoryAware and ApplicationContextAware).
     * <p>This method allows the bean instance to perform initialization only
     * possible when all bean properties have been set and to throw an
     * exception in the event of misconfiguration.
     * @throws Exception in the event of misconfiguration (such
     * as failure to set an essential property) or if initialization fails.
     */
    public void afterPropertiesSet() throws Exception{
        supportedMethods = new HttpMethodsContainer();
        BaseApplicationContext applicationContext = getApplicationContext();
        String gloableHttpMethods = applicationContext.getProperty("gloableHttpMethods");
        Pro pro = applicationContext.getProBean(this.beanName);
        String httpMethods = pro.getStringExtendAttribute("httpMethods");
        HttpMethod[] gloableMethods = null;
        HttpMethod[] methods = null;
       
        if(gloableHttpMethods != null && gloableHttpMethods.trim().length() > 0){
            gloableMethods = HttpMethod.resolveHttpMethods(gloableHttpMethods.trim());
        }
        if(httpMethods != null && httpMethods.trim().length() > 0){
            methods = HttpMethod.resolveHttpMethods(httpMethods.trim());
        }
        if(methods ==  null && methods.length == 0){
            methods = gloableMethods;
        }
        if(methods != null && methods.length > 0){
            
            supportedMethods.setHttpMethods(methods);
            
        }
    }


	@ExcludeMethod
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response,PageContext pageContext)
			throws Exception {

		// Delegate to WebContentGenerator for checking and preparing.
		checkAndPrepare(request, response, this instanceof LastModified,this.supportedMethods);

		// Execute handleRequestInternal in synchronized block if required.
		if (this.synchronizeOnSession) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				Object mutex = WebUtils.getSessionMutex(session);
				synchronized (mutex) {
					return handleRequestInternal(request, response,pageContext,null);
				}
			}
		}
		
		return handleRequestInternal(request, response,pageContext,null);
	}

	@ExcludeMethod
	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response,PageContext pageContext,HandlerExecutionChain handlerExecutionChain)
			throws Exception {

		// Delegate to WebContentGenerator for checking and preparing.
		checkAndPrepare(request, response, this instanceof LastModified,this.getSupportedMethods());

		// Execute handleRequestInternal in synchronized block if required.
		if (this.synchronizeOnSession) {
			HttpSession session = request.getSession(false);
			if (session != null) {
				Object mutex = WebUtils.getSessionMutex(session);
				synchronized (mutex) {
					return handleRequestInternal(request, response,pageContext,handlerExecutionChain);
				}
			}
		}

		return handleRequestInternal(request, response,pageContext,handlerExecutionChain);
	}

	/**
	 * Template method. Subclasses must implement this.
	 * The contract is the same as for <code>handleRequest</code>.
	 * @see #handleRequest
	 */
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response, PageContext pageContext,HandlerExecutionChain handlerExecutionChain)
	    throws Exception{
		return handleRequestInternal(  request,   response,   pageContext);
	}

	/**
	 * Template method. Subclasses must implement this.
	 * The contract is the same as for <code>handleRequest</code>.
	 * @see #handleRequest
	 */
	protected abstract ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response, PageContext pageContext)
			throws Exception;



}
