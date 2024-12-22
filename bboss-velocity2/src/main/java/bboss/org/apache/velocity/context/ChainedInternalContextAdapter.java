package bboss.org.apache.velocity.context;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import bboss.org.apache.velocity.Template;
import bboss.org.apache.velocity.app.event.EventCartridge;
import bboss.org.apache.velocity.runtime.resource.Resource;
import bboss.org.apache.velocity.util.introspection.IntrospectionCacheData;

import java.util.List;

/**
 * This is an abstract internal-use-only context implementation to be
 * used as a subclass for other internal-use-only contexts that wrap
 * other internal-use-only contexts.
 *
 * We use this context to make it easier to chain an existing context
 * as part of a new context implementation.  It just delegates everything
 * to the inner/parent context. Subclasses then only need to override
 * the methods relevant to them.
 *
 * @author Nathan Bubna
 * @version $Id: ChainedInternalContextAdapter.java 685724 2008-08-13 23:12:12Z nbubna $
 * @since 1.6
 */
public abstract class ChainedInternalContextAdapter implements InternalContextAdapter
{
    /** the parent context */
    protected InternalContextAdapter wrappedContext = null;

    /**
     * CTOR, wraps an ICA
     * @param inner context
     */
    public ChainedInternalContextAdapter(InternalContextAdapter inner)
    {
        wrappedContext = inner;
    }

    /**
     * Return the inner / user context.
     * @return The inner / user context.
     */
    @Override
    public Context getInternalUserContext()
    {
        return wrappedContext.getInternalUserContext();
    }

    /**
     * @see bboss.org.apache.velocity.context.InternalWrapperContext#getBaseContext()
     */
    @Override
    public InternalContextAdapter getBaseContext()
    {
        return wrappedContext.getBaseContext();
    }

    /**
     * Retrieves from parent context.
     *
     * @param key name of item to get
     * @return  stored object or null
     */
    @Override
    public Object get(String key)
    {
        return wrappedContext.get(key);
    }

    /**
     * Put method also stores values in parent context
     *
     * @param key name of item to set
     * @param value object to set to key
     * @return old stored object
     */
    @Override
    public Object put(String key, Object value)
    {
        /*
         * just put in the local context
         */
        return wrappedContext.put(key, value);
    }

    /**
     * @see bboss.org.apache.velocity.context.Context#containsKey(java.lang.String)
     */
    @Override
    public boolean containsKey(String key)
    {
        return wrappedContext.containsKey(key);
    }

    /**
     * @see bboss.org.apache.velocity.context.Context#getKeys()
     */
    @Override
    public String[] getKeys()
    {
        return wrappedContext.getKeys();
    }

    /**
     * @see bboss.org.apache.velocity.context.Context#remove(java.lang.String)
     */
    @Override
    public Object remove(String key)
    {
        return wrappedContext.remove(key);
    }

    /**
     * @see bboss.org.apache.velocity.context.InternalHousekeepingContext#pushCurrentTemplateName(java.lang.String)
     */
    @Override
    public void pushCurrentTemplateName(String s)
    {
        wrappedContext.pushCurrentTemplateName(s);
    }

    /**
     * @see bboss.org.apache.velocity.context.InternalHousekeepingContext#popCurrentTemplateName()
     */
    @Override
    public void popCurrentTemplateName()
    {
        wrappedContext.popCurrentTemplateName();
    }

    /**
     * @see bboss.org.apache.velocity.context.InternalHousekeepingContext#getCurrentTemplateName()
     */
    @Override
    public String getCurrentTemplateName()
    {
        return wrappedContext.getCurrentTemplateName();
    }

    /**
     * @see bboss.org.apache.velocity.context.InternalHousekeepingContext#getTemplateNameStack()
     */
    @Override
    public String[] getTemplateNameStack()
    {
        return wrappedContext.getTemplateNameStack();
    }

    /**
     * @see bboss.org.apache.velocity.context.InternalHousekeepingContext#pushCurrentMacroName(java.lang.String)
     */
    @Override
    public void pushCurrentMacroName(String s)
    {
        wrappedContext.pushCurrentMacroName(s);
    }

    /**
     * @see bboss.org.apache.velocity.context.InternalHousekeepingContext#popCurrentMacroName()
     */
    @Override
    public void popCurrentMacroName()
    {
        wrappedContext.popCurrentMacroName();
    }

    /**
     * @see bboss.org.apache.velocity.context.InternalHousekeepingContext#getCurrentMacroName()
     */
    @Override
    public String getCurrentMacroName()
    {
        return wrappedContext.getCurrentMacroName();
    }

    /**
     * @see bboss.org.apache.velocity.context.InternalHousekeepingContext#getCurrentMacroCallDepth()
     */
    @Override
    public int getCurrentMacroCallDepth()
    {
        return wrappedContext.getCurrentMacroCallDepth();
    }

    /**
     * @see bboss.org.apache.velocity.context.InternalHousekeepingContext#getMacroNameStack()
     */
    @Override
    public String[] getMacroNameStack()
    {
        return wrappedContext.getMacroNameStack();
    }

    /**
     * @see bboss.org.apache.velocity.context.InternalHousekeepingContext#icacheGet(java.lang.Object)
     */
    @Override
    public IntrospectionCacheData icacheGet(Object key)
    {
        return wrappedContext.icacheGet(key);
    }

    /**
     * @see bboss.org.apache.velocity.context.InternalHousekeepingContext#icachePut(java.lang.Object, org.apache.velocity.util.introspection.IntrospectionCacheData)
     */
    @Override
    public void icachePut(Object key, IntrospectionCacheData o)
    {
        wrappedContext.icachePut(key, o);
    }

    /**
     * @see bboss.org.apache.velocity.context.InternalHousekeepingContext#setMacroLibraries(List)
     */
    @Override
    public void setMacroLibraries(List<Template> macroLibraries)
    {
        wrappedContext.setMacroLibraries(macroLibraries);
    }

    /**
     * @see bboss.org.apache.velocity.context.InternalHousekeepingContext#getMacroLibraries()
     */
    @Override
    public List<Template> getMacroLibraries()
    {
        return wrappedContext.getMacroLibraries();
    }

    /**
     * @see bboss.org.apache.velocity.context.InternalEventContext#attachEventCartridge(org.apache.velocity.app.event.EventCartridge)
     */
    @Override
    public EventCartridge attachEventCartridge(EventCartridge ec)
    {
        return wrappedContext.attachEventCartridge(ec);
    }

    /**
     * @see bboss.org.apache.velocity.context.InternalEventContext#getEventCartridge()
     */
    @Override
    public EventCartridge getEventCartridge()
    {
        return wrappedContext.getEventCartridge();
    }


    /**
     * @see bboss.org.apache.velocity.context.InternalHousekeepingContext#setCurrentResource(org.apache.velocity.runtime.resource.Resource)
     */
    @Override
    public void setCurrentResource(Resource r)
    {
        wrappedContext.setCurrentResource(r);
    }

    /**
     * @see bboss.org.apache.velocity.context.InternalHousekeepingContext#getCurrentResource()
     */
    @Override
    public Resource getCurrentResource()
    {
        return wrappedContext.getCurrentResource();
    }
}
