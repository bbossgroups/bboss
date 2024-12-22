package bboss.org.apache.velocity.util.introspection;

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

import bboss.org.apache.velocity.runtime.RuntimeServices;
import bboss.org.apache.velocity.runtime.parser.node.PublicFieldExecutor;
import bboss.org.apache.velocity.runtime.parser.node.SetPublicFieldExecutor;
import bboss.org.apache.velocity.util.RuntimeServicesAware;
import bboss.org.apache.velocity.util.introspection.UberspectImpl.VelGetterImpl;
import bboss.org.apache.velocity.util.introspection.UberspectImpl.VelSetterImpl;
import org.slf4j.Logger;

import java.util.Iterator;

/**
 * Implementation of Uberspect to additionally provide access to public fields.
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @author <a href="mailto:henning@apache.org">Henning P. Schmiedehausen</a>
 * @author <a href="mailto:cdauth@cdauth.eu">Candid Dauth</a>
 */
public class UberspectPublicFields implements Uberspect, RuntimeServicesAware
{
    /**
     *  Our runtime logger.
     */
    protected Logger log;

    /**
     *  the default Velocity introspector
     */
    protected Introspector introspector;

    /**
     *  init - generates the Introspector. As the setup code
     *  makes sure that the log gets set before this is called,
     *  we can initialize the Introspector using the log object.
     */
    @Override
    public void init()
    {
        introspector = new Introspector(log);
    }

    /**
     * Property getter
     * @param obj
     * @param identifier
     * @param i
     * @return A Velocity Getter Method.
     */
    @Override
    public VelPropertyGet getPropertyGet(Object obj, String identifier, Info i)
    {
        if (obj == null)
        {
            return null;
        }

        Class<?> claz = obj.getClass();

        PublicFieldExecutor executor = new PublicFieldExecutor(log, introspector, claz, identifier);

        return (executor.isAlive()) ? new VelGetterImpl(executor) : null;
    }

    /**
     * Property setter
     * @param obj
     * @param identifier
     * @param arg
     * @param i
     * @return A Velocity Setter method.
     */
    @Override
    public VelPropertySet getPropertySet(Object obj, String identifier, Object arg, Info i)
    {
        if (obj == null)
        {
            return null;
        }

        Class<?> claz = obj.getClass();

        SetPublicFieldExecutor executor = new SetPublicFieldExecutor(log, introspector, claz, identifier, arg);

        return (executor.isAlive()) ? new VelSetterImpl(executor) : null;
    }

    /**
     * @param obj
     * @param info
     * @return iterator
     */
    @Override
    public Iterator getIterator(Object obj, Info info)
    {
        return null;
    }

    /**
     * @param obj
     * @param method
     * @param args
     * @param info
     * @return method wrapper
     */
    @Override
    public VelMethod getMethod(Object obj, String method, Object[] args, Info info)
    {
        return null;
    }

    /**
     * @param rs RuntimeServices object assigned during initialization
     */
    @Override
    public void setRuntimeServices(RuntimeServices rs)
    {
        log = rs.getLog("rendering");
    }
}
