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

import bboss.org.apache.velocity.exception.VelocityException;
import bboss.org.apache.velocity.runtime.RuntimeConstants;
import bboss.org.apache.velocity.runtime.RuntimeServices;
import bboss.org.apache.velocity.runtime.parser.node.AbstractExecutor;
import bboss.org.apache.velocity.runtime.parser.node.BooleanPropertyExecutor;
import bboss.org.apache.velocity.runtime.parser.node.GetExecutor;
import bboss.org.apache.velocity.runtime.parser.node.MapGetExecutor;
import bboss.org.apache.velocity.runtime.parser.node.MapSetExecutor;
import bboss.org.apache.velocity.runtime.parser.node.PropertyExecutor;
import bboss.org.apache.velocity.runtime.parser.node.PutExecutor;
import bboss.org.apache.velocity.runtime.parser.node.SetExecutor;
import bboss.org.apache.velocity.runtime.parser.node.SetPropertyExecutor;
import bboss.org.apache.velocity.util.ArrayIterator;
import bboss.org.apache.velocity.util.ArrayListWrapper;
import bboss.org.apache.velocity.util.ClassUtils;
import bboss.org.apache.velocity.util.EnumerationIterator;
import bboss.org.apache.velocity.util.RuntimeServicesAware;

import org.slf4j.Logger;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/**
 *  Implementation of Uberspect to provide the default introspective
 *  functionality of Velocity
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @author <a href="mailto:henning@apache.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 */
public class UberspectImpl implements Uberspect, RuntimeServicesAware
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
     * the conversion handler
     */
    protected TypeConversionHandler conversionHandler;

    /**
     * runtime services
     */
    protected RuntimeServices rsvc;

    /**
     *  init - generates the Introspector. As the setup code
     *  makes sure that the log gets set before this is called,
     *  we can initialize the Introspector using the log object.
     */
    @Override
    public void init()
    {
        introspector = new Introspector(log, conversionHandler);
    }

    public TypeConversionHandler getConversionHandler()
    {
        return conversionHandler;
    }

    /**
     * sets the runtime services
     * @param rs runtime services
     */
    @Override
    @SuppressWarnings("deprecation")
    public void setRuntimeServices(RuntimeServices rs)
    {
        rsvc = rs;
        log = rsvc.getLog("introspection");

        Object conversionHandlerInstance = rs.getProperty(RuntimeConstants.CONVERSION_HANDLER_INSTANCE);
        if (conversionHandlerInstance == null)
        {
            String conversionHandlerClass = rs.getString(RuntimeConstants.CONVERSION_HANDLER_CLASS);
            if (conversionHandlerClass != null && !conversionHandlerClass.equals("none"))
            {
                try
                {
                    conversionHandlerInstance = ClassUtils.getNewInstance(conversionHandlerClass);
                }
                catch (ClassNotFoundException cnfe )
                {
                    String err = "The specified class for ConversionHandler (" + conversionHandlerClass
                        + ") does not exist or is not accessible to the current classloader.";
                    log.error(err);
                    throw new VelocityException(err, cnfe);
                }
                catch (InstantiationException ie)
                {
                    throw new VelocityException("Could not instantiate class '" + conversionHandlerClass + "'", ie);
                }
                catch (IllegalAccessException ae)
                {
                    throw new VelocityException("Cannot access class '" + conversionHandlerClass + "'", ae);
                }
            }
        }

        if (conversionHandlerInstance != null)
        {
            if (conversionHandlerInstance instanceof ConversionHandler)
            {
                log.warn("The ConversionHandler interface is deprecated - see the TypeConversionHandler interface");
                final ConversionHandler ch = (ConversionHandler)conversionHandlerInstance;
                conversionHandler = new TypeConversionHandler()
                {
                    @Override
                    public boolean isExplicitlyConvertible(Type formal, Class<?> actual, boolean possibleVarArg)
                    {
                        Class<?> formalClass = IntrospectionUtils.getTypeClass(formal);
                        if (formalClass != null) return ch.isExplicitlyConvertible(formalClass, actual, possibleVarArg);
                        else return false;
                    }

                    @Override
                    public Converter<?> getNeededConverter(Type formal, Class<?> actual)
                    {
                        Class<?> formalClass = IntrospectionUtils.getTypeClass(formal);
                        if (formalClass != null) return ch.getNeededConverter(formalClass, actual);
                        else return null;
                    }

                    @Override
                    public void addConverter(Type formal, Class<?> actual, Converter<?> converter)
                    {
                        Class<?> formalClass = IntrospectionUtils.getTypeClass(formal);
                        if (formalClass != null) ch.addConverter(formalClass, actual, converter);
                        else throw new UnsupportedOperationException("This conversion handler doesn't know how to handle Type: " + formal.getTypeName());
                    }
                };
            }
            else if (!(conversionHandlerInstance instanceof TypeConversionHandler))
            {
                String err = "The specified class or provided instance for the conversion handler (" + conversionHandlerInstance.getClass().getName()
                        + ") does not implement " + TypeConversionHandler.class.getName()
                        + "; Velocity is not initialized correctly.";

                log.error(err);
                throw new VelocityException(err, null, rsvc.getLogContext().getStackTrace());
            }
            else
            {
                conversionHandler = (TypeConversionHandler)conversionHandlerInstance;
            }
        }
    }

    /**
     *  Sets the runtime logger - this must be called before anything
     *  else.
     *
     * @param log The logger instance to use.
     * @since 1.5
     * @deprecated logger is now set by default to the namespace logger "velocity.rendering".
     */
    public void setLog(Logger log)
    {
        this.log = log;
    }

    /**
     *  To support iterative objects used in a <code>#foreach()</code>
     *  loop.
     *
     * @param obj The iterative object.
     * @param i Info about the object's location.
     * @return An {@link Iterator} object.
     */
    @Override
    public Iterator getIterator(Object obj, Info i)
    {
        if (obj.getClass().isArray())
        {
            return new ArrayIterator(obj);
        }
        else if (obj instanceof Iterable)
        {
            return ((Iterable) obj).iterator();
        }
        else if (obj instanceof Map)
        {
            return ((Map) obj).values().iterator();
        }
        else if (obj instanceof Iterator)
        {
            log.debug("The iterative object in the #foreach() loop at {}" +
                      " is of type java.util.Iterator.  Because " +
                      "it is not resettable, if used in more than once it " +
                      "may lead to unexpected results.", i);
            return ((Iterator) obj);
        }
        else if (obj instanceof Enumeration)
        {
            log.debug("The iterative object in the #foreach() loop at {}" +
                      " is of type java.util.Enumeration.  Because " +
                      "it is not resettable, if used in more than once it " +
                      "may lead to unexpected results.", i);
            return new EnumerationIterator((Enumeration) obj);
        }
        else
        {
            // look for an iterator() method to support the JDK5 Iterable
            // interface or any user tools/DTOs that want to work in
            // foreach without implementing the Collection interface
            Class<?> type = obj.getClass();
            try
            {
                Method iter = type.getMethod("iterator");
                Class<?> returns = iter.getReturnType();
                if (Iterator.class.isAssignableFrom(returns))
                {
                    try
                    {
                        return (Iterator)iter.invoke(obj);
                    }
                    catch (IllegalAccessException e)
                    {
                        // Cannot invoke this method, just give up
                    }
                    catch (Exception e)
                    {
                        throw new VelocityException("Error invoking the method 'iterator' on class '"
                            + obj.getClass().getName() +"'", e, rsvc.getLogContext().getStackTrace());
                    }
                }
                else
                {
                    log.debug("iterator() method of reference in #foreach loop at " +
                              "{} does not return a true Iterator.", i);
                }
            }
            catch (NoSuchMethodException nsme)
            {
                // eat this one, but let all other exceptions thru
            }
        }

        /*  we have no clue what this is  */
        log.debug("Could not determine type of iterator in #foreach loop at {}", i);

        return null;
    }

    /**
     *  Method
     * @param obj
     * @param methodName
     * @param args
     * @param i
     * @return A Velocity Method.
     */
    @Override
    public VelMethod getMethod(Object obj, String methodName, Object[] args, Info i)
    {
        if (obj == null)
        {
            return null;
        }

        Method m = introspector.getMethod(obj.getClass(), methodName, args);
        if (m != null)
        {
            return new VelMethodImpl(m, false, getNeededConverters(m.getGenericParameterTypes(), args));
        }

        Class<?> cls = obj.getClass();
        // if it's an array
        if (cls.isArray())
        {
            // check for support via our array->list wrapper
            m = introspector.getMethod(ArrayListWrapper.class, methodName, args);
            if (m != null)
            {
                // and create a method that knows to wrap the value
                // before invoking the method
                return new VelMethodImpl(m, true, getNeededConverters(m.getGenericParameterTypes(), args));
            }
        }
        // watch for classes, to allow calling their static methods (VELOCITY-102)
        else if (cls == Class.class)
        {
            m = introspector.getMethod((Class<?>)obj, methodName, args);
            if (m != null)
            {
                return new VelMethodImpl(m, false, getNeededConverters(m.getGenericParameterTypes(), args));
            }
        }
        return null;
    }

    /**
     * get the list of needed converters to adapt passed argument types to method types
     * @return null if not conversion needed, otherwise an array containing needed converters
     */
    private Converter<?>[] getNeededConverters(Type[] expected, Object[] provided)
    {
        if (conversionHandler == null) return null;
        // var args are not handled here - CB TODO
        int n = Math.min(expected.length, provided.length);
        Converter<?>[] converters = null;
        for (int i = 0; i < n; ++i)
        {
            Object arg = provided[i];
            if (arg == null) continue;
            Converter<?> converter = conversionHandler.getNeededConverter(expected[i], arg.getClass());
            if (converter != null)
            {
                if (converters == null)
                {
                    converters = new Converter[expected.length];
                }
                converters[i] = converter;
            }
        }
        return converters;
    }

    /**
     * Property  getter
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

        /*
         *  first try for a getFoo() type of property
         *  (also getfoo() )
         */
        AbstractExecutor executor = new PropertyExecutor(log, introspector, claz, identifier);

        /*
         * Let's see if we are a map...
         */
        if (!executor.isAlive())
        {
            executor = new MapGetExecutor(log, obj, identifier);
        }

        /*
         *  if that didn't work, look for get("foo")
         */

        if (!executor.isAlive())
        {
            executor = new GetExecutor(log, introspector, claz, identifier);
        }

        /*
         *  finally, look for boolean isFoo()
         */

        if (!executor.isAlive())
        {
            executor = new BooleanPropertyExecutor(log, introspector, claz,
                    identifier);
        }

        /*
         * and idem on an array
         */
        if (!executor.isAlive() && obj.getClass().isArray())
        {
            executor = new BooleanPropertyExecutor(log, introspector, ArrayListWrapper.class,
                    identifier, true);
        }

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
    public VelPropertySet getPropertySet(Object obj, String identifier,
                                         Object arg, Info i)
    {
        if (obj == null)
        {
            return null;
        }

        Class<?> claz = obj.getClass();

        /*
         *  first try for a setFoo() type of property
         *  (also setfoo() )
         */
        SetExecutor executor = new SetPropertyExecutor(log, introspector, claz, identifier, arg);

        /*
         * Let's see if we are a map...
         */
        if (!executor.isAlive())  {
            executor = new MapSetExecutor(log, claz, identifier);
        }

        /*
         *  if that didn't work, look for put("foo", arg)
         */

        if (!executor.isAlive())
        {
            executor = new PutExecutor(log, introspector, claz, arg, identifier);
        }

        return (executor.isAlive()) ? new VelSetterImpl(executor) : null;
    }

    /**
     *  Implementation of VelMethod
     */
    public static class VelMethodImpl implements VelMethod
    {
        final Method method;
        Boolean isVarArg;
        boolean wrapArray;
        Converter<?> converters[];

        /**
         * @param m
         */
        public VelMethodImpl(Method m)
        {
            this(m, false, null);
        }

        /**
         * @param method
         * @param wrapArray
         * @since 1.6
         */
        public VelMethodImpl(Method method, boolean wrapArray)
        {
            this(method, wrapArray, null);
        }

        /**
         * @param method
         * @param wrapArray
         * @param converters
         * @since 2.0
         */
        public VelMethodImpl(Method method, boolean wrapArray, Converter<?>[] converters)
        {
            this.method = method;
            this.wrapArray = wrapArray;
            this.converters = converters;
        }

        private VelMethodImpl()
        {
            method = null;
        }

        /**
         * @param o
         * @param actual
         * @return invocation result
         * @see VelMethod#invoke(java.lang.Object, java.lang.Object[])
         */
        @Override
        public Object invoke(Object o, Object[] actual)
            throws IllegalAccessException, InvocationTargetException
        {
            // if we're pretending an array is a list...
            if (wrapArray)
            {
                o = new ArrayListWrapper(o);
            }

            if (isVarArg())
            {
                Class<?>[] formal = method.getParameterTypes();
                int index = formal.length - 1;
                if (actual.length >= index)
                {
                    Class<?> type = formal[index].getComponentType();
                    actual = handleVarArg(type, index, actual);
                }
            }

            if (converters != null)
            {
                // some converters may throw an ArithmeticException
                // which we want to wrap into an IllegalArgumentException
                try
                {
                    for (int i = 0; i < actual.length; ++i)
                    {
                        if (converters[i] != null)
                        {
                            actual[i] = converters[i].convert(actual[i]);
                        }
                    }
                }
                catch (ArithmeticException ae)
                {
                    throw new IllegalArgumentException(ae);
                }
            }

            // call extension point invocation
            return doInvoke(o, actual);
        }

        /**
         * Offers an extension point for subclasses (in alternate Uberspects)
         * to alter the invocation after any array wrapping or varargs handling
         * has already been completed.
         * @param o target object
         * @param actual arguments
         * @return invocation result
         * @throws IllegalAccessException
         * @throws InvocationTargetException
         * @since 1.6
         */
        protected Object doInvoke(Object o, Object[] actual)
            throws IllegalAccessException, InvocationTargetException
        {
            return method.invoke(o, actual);
        }

        /**
         * @return true if this method can accept a variable number of arguments
         * @since 1.6
         */
        public boolean isVarArg()
        {
            if (isVarArg == null)
            {
                Class<?>[] formal = method.getParameterTypes();
                if (formal.length == 0)
                {
                    this.isVarArg = Boolean.FALSE;
                }
                else
                {
                    Class<?> last = formal[formal.length - 1];
                    // if the last arg is an array, then
                    // we consider this a varargs method
                    this.isVarArg = last.isArray();
                }
            }
            return isVarArg;
        }

        /**
         * @param type The vararg class type (aka component type
         *             of the expected array arg)
         * @param index The index of the vararg in the method declaration
         *              (This will always be one less than the number of
         *               expected arguments.)
         * @param actual The actual parameters being passed to this method
         * @return The actual parameters adjusted for the varargs in order
         *          to fit the method declaration.
         */
        private Object[] handleVarArg(final Class<?> type,
                                      final int index,
                                      Object[] actual)
        {
            // if no values are being passed into the vararg
            if (actual.length == index)
            {
                // copy existing args to new array
                Object[] newActual = new Object[actual.length + 1];
                System.arraycopy(actual, 0, newActual, 0, actual.length);
                // create an empty array of the expected type
                newActual[index] = Array.newInstance(type, 0);
                actual = newActual;
            }
            // if one value is being passed into the vararg
            else if (actual.length == index + 1 && actual[index] != null)
            {
                // make sure the last arg is an array of the expected type
                Class<?> argClass = actual[index].getClass();
                if (!argClass.isArray() && IntrospectionUtils.isMethodInvocationConvertible(type, argClass, false))
                {
                    // create a 1-length array to hold and replace the last param
                    Object lastActual = Array.newInstance(type, 1);
                    Array.set(lastActual, 0, actual[index]);
                    actual[index] = lastActual;
                }
            }
            // if multiple values are being passed into the vararg
            else if (actual.length > index + 1)
            {
                // put the last and extra actual in an array of the expected type
                int size = actual.length - index;
                Object lastActual = Array.newInstance(type, size);
                for (int i = 0; i < size; i++)
                {
                    Array.set(lastActual, i, actual[index + i]);
                }

                // put all into a new actual array of the appropriate size
                Object[] newActual = new Object[index + 1];
                System.arraycopy(actual, 0, newActual, 0, index);
                newActual[index] = lastActual;

                // replace the old actual array
                actual = newActual;
            }
            return actual;
        }

        /**
         * @see bboss.org.apache.velocity.util.introspection.VelMethod#isCacheable()
         */
        @Override
        public boolean isCacheable()
        {
            return true;
        }

        /**
         * @see bboss.org.apache.velocity.util.introspection.VelMethod#getMethodName()
         */
        @Override
        public String getMethodName()
        {
            return method.getName();
        }

        /**
         * @see bboss.org.apache.velocity.util.introspection.VelMethod#getMethod()
         */
        @Override
        public Method getMethod()
        {
            return method;
        }

        /**
         * @see bboss.org.apache.velocity.util.introspection.VelMethod#getReturnType()
         */
        @Override
        public Class<?> getReturnType()
        {
            return method.getReturnType();
        }
    }

    /**
     *
     *
     */
    public static class VelGetterImpl implements VelPropertyGet
    {
        final AbstractExecutor getExecutor;

        /**
         * @param exec
         */
        public VelGetterImpl(AbstractExecutor exec)
        {
            getExecutor = exec;
        }

        private VelGetterImpl()
        {
            getExecutor = null;
        }

        /**
         * @see bboss.org.apache.velocity.util.introspection.VelPropertyGet#invoke(java.lang.Object)
         */
        @Override
        public Object invoke(Object o)
            throws IllegalAccessException, InvocationTargetException
        {
            return getExecutor.execute(o);
        }

        /**
         * @see bboss.org.apache.velocity.util.introspection.VelPropertyGet#isCacheable()
         */
        @Override
        public boolean isCacheable()
        {
            return true;
        }

        /**
         * @see bboss.org.apache.velocity.util.introspection.VelPropertyGet#getMethodName()
         */
        @Override
        public String getMethodName()
        {
            return getExecutor.isAlive() ? getExecutor.getMethod().getName() : null;
        }
    }

    /**
     *
     */
    public static class VelSetterImpl implements VelPropertySet
    {
        private final SetExecutor setExecutor;

        /**
         * @param setExecutor
         */
        public VelSetterImpl(final SetExecutor setExecutor)
        {
            this.setExecutor = setExecutor;
        }

        private VelSetterImpl()
        {
            setExecutor = null;
        }

        /**
         * Invoke the found Set Executor.
         *
         * @param o is the Object to invoke it on.
         * @param value in the Value to set.
         * @return The resulting Object.
         */
        @Override
        public Object invoke(final Object o, final Object value)
            throws IllegalAccessException, InvocationTargetException
        {
            return setExecutor.execute(o, value);
        }

        /**
         * @see bboss.org.apache.velocity.util.introspection.VelPropertySet#isCacheable()
         */
        @Override
        public boolean isCacheable()
        {
            return true;
        }

        /**
         * @see bboss.org.apache.velocity.util.introspection.VelPropertySet#getMethodName()
         */
        @Override
        public String getMethodName()
        {
            return setExecutor.isAlive() ? setExecutor.getMethod().getName() : null;
        }
    }
}
