package bboss.org.apache.velocity.runtime.parser.node;

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

import org.apache.commons.lang3.StringUtils;
import bboss.org.apache.velocity.exception.VelocityException;
import bboss.org.apache.velocity.util.introspection.Introspector;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Returns the value of a public field when executed.
 */
public class PublicFieldExecutor extends AbstractExecutor
{
    private final Introspector introspector;

    /**
     * Field to be accessed
     */
    private Field field = null;

    /**
     * @param log
     * @param introspector
     * @param clazz
     * @param property
     * @since 1.5
     */
    public PublicFieldExecutor(final Logger log, final Introspector introspector,
            final Class<?> clazz, final String property)
    {
        this.log = log;
        this.introspector = introspector;

        // Don't allow passing in the empty string or null because
        // it will either fail with a StringIndexOutOfBounds error
        // or the introspector will get confused.
        if (StringUtils.isNotEmpty(property))
        {
            discover(clazz, property);
        }
    }

    @Override
    public boolean isAlive() {
        return getField() != null;
    }

    /**
     * @return The current field.
     */
    public Field getField()
    {
        return field;
    }

    /**
     * @param field
     */
    protected void setField(final Field field)
    {
        this.field = field;
    }

    /**
     * @return The current introspector.
     * @since 1.5
     */
    protected Introspector getIntrospector()
    {
        return this.introspector;
    }

    /**
     * @param clazz
     * @param property
     */
    protected void discover(final Class<?> clazz, final String property)
    {
        try
        {
            setField(introspector.getField(clazz, property));
        }
        /*
         * pass through application level runtime exceptions
         */
        catch( RuntimeException e )
        {
            throw e;
        }
        catch(Exception e)
        {
            String msg = "Exception while looking for public field '" + property + "'";
            log.error(msg, e);
            throw new VelocityException(msg, e);
        }
    }

    /**
     * @see bboss.org.apache.velocity.runtime.parser.node.AbstractExecutor#execute(java.lang.Object)
     */
    @Override
    public Object execute(Object o)
        throws IllegalAccessException,  InvocationTargetException
    {
        return isAlive() ? getField().get(o) : null;
    }
}
