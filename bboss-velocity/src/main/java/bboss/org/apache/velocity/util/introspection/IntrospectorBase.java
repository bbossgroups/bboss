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

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Lookup a a Method object for a particular class given the name of a method
 * and its parameters.
 *
 * The first time the Introspector sees a
 * class it creates a class method map for the
 * class in question. Basically the class method map
 * is a Hashtable where Method objects are keyed by a
 * concatenation of the method name and the names of
 * classes that make up the parameters.
 *
 * For example, a method with the following signature:
 *
 * public void method(String a, StringBuffer b)
 *
 * would be mapped by the key:
 *
 * "method" + "java.lang.String" + "java.lang.StringBuffer"
 *
 * This mapping is performed for all the methods in a class
 * and stored for.
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:bob@werken.com">Bob McWhirter</a>
 * @author <a href="mailto:szegedia@freemail.hu">Attila Szegedi</a>
 * @author <a href="mailto:paulo.gaspar@krankikom.de">Paulo Gaspar</a>
 * @author <a href="mailto:henning@apache.org">Henning P. Schmiedehausen</a>
 * @author <a href="mailto:cdauth@cdauth.eu">Candid Dauth</a>
 * @version $Id$
 */
public abstract class IntrospectorBase
{
    /** Class logger */
    protected final Logger log;

    /** The Introspector Cache */
    private final IntrospectorCache introspectorCache;

    /**
     * C'tor.
     * @param log logger
     * @param conversionHandler conversion handler
     */
    protected IntrospectorBase(final Logger log, final TypeConversionHandler conversionHandler)
    {
        this.log = log;
        introspectorCache = new IntrospectorCache(log, conversionHandler);
    }

    /**
     * Gets the method defined by <code>name</code> and
     * <code>params</code> for the Class <code>c</code>.
     *
     * @param c Class in which the method search is taking place
     * @param name Name of the method being searched for
     * @param params An array of Objects (not Classes) that describe the
     *               the parameters
     *
     * @return The desired Method object.
     * @throws NullPointerException When the parameters passed in can not be used for introspection because null.
     * @throws MethodMap.AmbiguousException When the method map contains more than one match for the requested signature.
     */
    public Method getMethod(final Class<?> c, final String name, final Object[] params)
            throws MethodMap.AmbiguousException
    {
        IntrospectorCache ic = getIntrospectorCache();

        ClassMap classMap = ic.get(Validate.notNull(c, "class object is null!"));
        if (classMap == null)
        {
            classMap = ic.put(c);
        }

        return classMap.findMethod(name, Validate.notNull(params, "params object is null!"));
    }

    /**
     * Gets the field defined by <code>name</code>.
     *
     * @param c Class in which the method search is taking place
     * @param name Name of the field being searched for
     *
     * @return The desired Field object.
     * @throws IllegalArgumentException When the parameters passed in can not be used for introspection.
     */
    public Field getField(final Class<?> c, final String name)
            throws IllegalArgumentException
    {
        IntrospectorCache ic = getIntrospectorCache();

        ClassFieldMap classFieldMap = ic.getFieldMap(Validate.notNull(c, "class object is null!"));
        if (classFieldMap == null)
        {
            ic.put(c);
            classFieldMap = ic.getFieldMap(c);
        }

        return classFieldMap.findField(name);
    }

    /**
     * Return the internal IntrospectorCache object.
     *
     * @return The internal IntrospectorCache object.
     * @since 1.5
     */
    protected IntrospectorCache getIntrospectorCache()
    {
        return introspectorCache;
    }

}
