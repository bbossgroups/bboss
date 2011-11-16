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
package com.frameworkset.orm.engine.model;

/*
 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;

import com.frameworkset.orm.engine.EngineException;

/**
 * A name generation factory.
 *
 * @author <a href="mailto:dlr@finemaltcoding.com>Daniel Rall</a>
 * @version $Id: NameFactory.java,v 1.6 2004/02/22 06:27:19 jmcnally Exp $
 */
public class NameFactory implements Serializable
{
    /**
     * The fully qualified class name of the Java name generator.
     */
    public static final String JAVA_GENERATOR =
        JavaNameGenerator.class.getName();

    /**
     * The fully qualified class name of the constraint name generator.
     */
    public static final String CONSTRAINT_GENERATOR =
        ConstraintNameGenerator.class.getName();

    /**
     * The single instance of this class.
     */
    private static NameFactory instance = new NameFactory();

    /**
     * The cache of <code>NameGenerator</code> algorithms in use for
     * name generation, keyed by fully qualified class name.
     */
    private Hashtable algorithms;

    /**
     * Creates a new instance with storage for algorithm implementations.
     */
    protected NameFactory()
    {
        algorithms = new Hashtable(5);
    }

    /**
     * Factory method which retrieves an instance of the named generator.
     *
     * @param name The fully qualified class name of the name
     *        generation algorithm to retrieve.
     * @return A name generator
     */
    protected NameGenerator getAlgorithm(String name)
            throws EngineException
    {
        synchronized (algorithms)
        {
            NameGenerator algorithm = (NameGenerator) algorithms.get(name);
            if (algorithm == null)
            {
                try
                {
                    algorithm =
                        (NameGenerator) Class.forName(name).newInstance();
                }
                catch (InstantiationException e)
                {
                    throw new EngineException("Unable to instantiate class " 
                            + name + ": Make sure it's in your run-time classpath", e);
                }
                catch (Exception e)
                {
                    throw new EngineException(e);
                }
                algorithms.put(name, algorithm);
            }
            return algorithm;
        }
    }

    /**
     * Given a list of <code>String</code> objects, implements an
     * algorithm which produces a name.
     *
     * @param algorithmName The fully qualified class name of the
     * {@link com.frameworkset.orm.engine.model.NameGenerator}
     * implementation to use to generate names.
     * @param inputs Inputs used to generate a name.
     * @return The generated name.
     * @throws EngineException an exception
     */
    public static String generateName(String algorithmName, List inputs)
        throws EngineException
    {
        NameGenerator algorithm = instance.getAlgorithm(algorithmName);
        return algorithm.generateName(inputs);
    }
}
