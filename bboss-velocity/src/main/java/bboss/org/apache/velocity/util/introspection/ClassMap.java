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

import org.slf4j.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A cache of introspection information for a specific class instance.
 * Keys {@link java.lang.reflect.Method} objects by a concatenation of the
 * method name and the names of classes that make up the parameters.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:bob@werken.com">Bob McWhirter</a>
 * @author <a href="mailto:szegedia@freemail.hu">Attila Szegedi</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @author <a href="mailto:henning@apache.org">Henning P. Schmiedehausen</a>
 * @author Nathan Bubna
 * @author <a href="mailto:claude.brisson@gmail.com">Claude Brisson</a>
 * @version $Id$
 */
public class ClassMap
{
    /** Set true if you want to debug the reflection code */
    private static final boolean debugReflection = false;

    /** Class logger */
    private final Logger log;

    /**
     * Class passed into the constructor used to as
     * the basis for the Method map.
     */
    private final Class<?> clazz;

    private final MethodCache methodCache;

    /**
     * Standard constructor
     * @param clazz The class for which this ClassMap gets constructed.
     * @param log logger
     */
    public ClassMap(final Class<?> clazz, final Logger log)
    {
        this(clazz, log, null);
    }

    /**
     * Standard constructor
     * @param clazz The class for which this ClassMap gets constructed.
     * @param log logger
     * @param conversionHandler conversion handler
     * @since 2.0
     */
    public ClassMap(final Class<?> clazz, final Logger log, final TypeConversionHandler conversionHandler)
    {
        this.clazz = clazz;
        this.log = log;

        if (debugReflection)
        {
            log.debug("=================================================================");
            log.debug("== Class: {}", clazz);
        }

        methodCache = createMethodCache(conversionHandler);

        if (debugReflection)
        {
            log.debug("=================================================================");
        }
    }

    /**
     * Returns the class object whose methods are cached by this map.
     *
     * @return The class object whose methods are cached by this map.
     */
    public Class<?> getCachedClass()
    {
        return clazz;
    }

    /**
     * Find a Method using the method name and parameter objects.
     *
     * @param name The method name to look up.
     * @param params An array of parameters for the method.
     * @return A Method object representing the method to invoke or null.
     * @throws MethodMap.AmbiguousException When more than one method is a match for the parameters.
     */
    public Method findMethod(final String name, final Object[] params)
            throws MethodMap.AmbiguousException
    {
        return methodCache.get(name, params);
    }

    /**
     * Populate the Map of direct hits. These
     * are taken from all the public methods
     * that our class, its parents and their implemented interfaces provide.
     */
    private MethodCache createMethodCache(TypeConversionHandler conversionHandler)
    {
        MethodCache methodCache = new MethodCache(log, conversionHandler);
    //
    // Looks through all elements in the class hierarchy. This one is bottom-first (i.e. we start
    // with the actual declaring class and its interfaces and then move up (superclass etc.) until we
    // hit java.lang.Object. That is important because it will give us the methods of the declaring class
    // which might in turn be abstract further up the tree.
    //
    // We also ignore all SecurityExceptions that might happen due to SecurityManager restrictions (prominently
    // hit with Tomcat 5.5).
    //
    // We can also omit all that complicated getPublic, getAccessible and upcast logic that the class map had up
    // until Velocity 1.4. As we always reflect all elements of the tree (that's what we have a cache for), we will
    // hit the public elements sooner or later because we reflect all the public elements anyway.
    //
        // Ah, the miracles of Java for(;;) ...
        for (Class<?> classToReflect = getCachedClass(); classToReflect != null ; classToReflect = classToReflect.getSuperclass())
        {
            if (Modifier.isPublic(classToReflect.getModifiers()))
            {
                populateMethodCacheWith(methodCache, classToReflect);
            }
            Class<?> [] interfaces = classToReflect.getInterfaces();
            for (Class<?> anInterface : interfaces)
            {
                populateMethodCacheWithInterface(methodCache, anInterface);
            }
        }
        // return the already initialized cache
        return methodCache;
    }

    /* recurses up interface heirarchy to get all super interfaces (VELOCITY-689) */
    private void populateMethodCacheWithInterface(MethodCache methodCache, Class<?> iface)
    {
        if (Modifier.isPublic(iface.getModifiers()))
        {
            populateMethodCacheWith(methodCache, iface);
        }
        Class<?>[] supers = iface.getInterfaces();
        for (Class<?> aSuper : supers)
        {
            populateMethodCacheWithInterface(methodCache, aSuper);
        }
    }

    private void populateMethodCacheWith(MethodCache methodCache, Class<?> classToReflect)
    {
        if (debugReflection)
        {
            log.debug("Reflecting {}", classToReflect);
        }

        try
        {
            Method[] methods = classToReflect.getDeclaredMethods();
            for (Method method : methods)
            {
                int modifiers = method.getModifiers();
                if (Modifier.isPublic(modifiers))
                {
                    method = MethodMap.getAccessibleMethodDeclaration(method);
                    methodCache.put(method);
                }
            }
        }
        catch (SecurityException se) // Everybody feels better with...
        {
            log.debug("While accessing methods of {}:", classToReflect, se);
        }
    }

    /**
     * This is the cache to store and look up the method information.
     *
     * @author <a href="mailto:henning@apache.org">Henning P. Schmiedehausen</a>
     * @version $Id$
     */
    private static final class MethodCache
    {
        private static final Object CACHE_MISS = new Object();

        private static final String NULL_ARG = Object.class.getName();

        private static final Map<Class<?>, String> convertPrimitives = new HashMap();

        static
        {
            convertPrimitives.put(Boolean.TYPE,   Boolean.class.getName());
            convertPrimitives.put(Byte.TYPE,      Byte.class.getName());
            convertPrimitives.put(Character.TYPE, Character.class.getName());
            convertPrimitives.put(Double.TYPE,    Double.class.getName());
            convertPrimitives.put(Float.TYPE,     Float.class.getName());
            convertPrimitives.put(Integer.TYPE,   Integer.class.getName());
            convertPrimitives.put(Long.TYPE,      Long.class.getName());
            convertPrimitives.put(Short.TYPE,     Short.class.getName());
        }

    	/** Class logger */
        private final Logger log;

        /**
         * Cache of Methods, or CACHE_MISS, keyed by method
         * name and actual arguments used to find it.
         */
        private final Map<Object, Object> cache = new ConcurrentHashMap<>();

        /** Map of methods that are searchable according to method parameters to find a match */
        private final MethodMap methodMap;

        private MethodCache(Logger log, TypeConversionHandler conversionHandler)
        {
            this.log = log;
            methodMap = new MethodMap(conversionHandler);
        }

        /**
         * Find a Method using the method name and parameter objects.
         *
         * Look in the methodMap for an entry.  If found,
         * it'll either be a CACHE_MISS, in which case we
         * simply give up, or it'll be a Method, in which
         * case, we return it.
         *
         * If nothing is found, then we must actually go
         * and introspect the method from the MethodMap.
         *
         * @param name The method name to look up.
         * @param params An array of parameters for the method.
         * @return A Method object representing the method to invoke or null.
         * @throws MethodMap.AmbiguousException When more than one method is a match for the parameters.
         */
        public Method get(final String name, final Object [] params)
                throws MethodMap.AmbiguousException
        {
            String methodKey = makeMethodKey(name, params);

            Object cacheEntry = cache.get(methodKey);
            if (cacheEntry == CACHE_MISS)
            {
                // We looked this up before and failed.
                return null;
            }

            if (cacheEntry == null)
            {
                try
                {
                    // That one is expensive...
                    cacheEntry = methodMap.find(name, params);
                }
                catch(MethodMap.AmbiguousException ae)
                {
                    /*
                     *  that's a miss :-)
                     */
                    cache.put(methodKey, CACHE_MISS);
                    throw ae;
                }

                cache.put(methodKey,
                        (cacheEntry != null) ? cacheEntry : CACHE_MISS);
            }

            // Yes, this might just be null.
            return (Method) cacheEntry;
        }

        private void put(Method method)
        {
            String methodKey = makeMethodKey(method);

            // We don't overwrite methods because we fill the
            // cache from defined class towards java.lang.Object
            // and that would cause overridden methods to appear
            // as if they were not overridden.
            if (cache.get(methodKey) == null)
            {
                cache.put(methodKey, method);
                methodMap.add(method);
                if (debugReflection)
                {
                    log.debug("Adding {}", method);
                }
            }
        }

        /**
         * Make a methodKey for the given method using
         * the concatenation of the name and the
         * types of the method parameters.
         *
         * @param method to be stored as key
         * @return key for ClassMap
         */
        private String makeMethodKey(final Method method)
        {
            Class<?>[] parameterTypes = method.getParameterTypes();
            int args = parameterTypes.length;
            if (args == 0)
            {
                return method.getName();
            }

            StringBuilder methodKey = new StringBuilder((args+1)*16).append(method.getName());

            for (Class<?> parameterType : parameterTypes)
            {
                /*
                 * If the argument type is primitive then we want
                 * to convert our primitive type signature to the
                 * corresponding Object type so introspection for
                 * methods with primitive types will work correctly.
                 *
                 * The lookup map (convertPrimitives) contains all eight
                 * primitives (boolean, byte, char, double, float, int, long, short)
                 * known to Java. So it should never return null for the key passed in.
                 */
                if (parameterType.isPrimitive())
                {
                    methodKey.append((String) convertPrimitives.get(parameterType));
                } else
                {
                    methodKey.append(parameterType.getName());
                }
            }

            return methodKey.toString();
        }

        private String makeMethodKey(String method, Object[] params)
        {
            int args = params.length;
            if (args == 0)
            {
                return method;
            }

            StringBuilder methodKey = new StringBuilder((args+1)*16).append(method);

            for (Object arg : params)
            {
                if (arg == null)
                {
                    methodKey.append(NULL_ARG);
                }
                else
                {
                    methodKey.append(arg.getClass().getName());
                }
            }

            return methodKey.toString();
        }
    }
}
