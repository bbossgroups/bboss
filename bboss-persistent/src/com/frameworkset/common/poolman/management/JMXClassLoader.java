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
 *  distributed under the License is distributed on an "AS IS" bboss persistent,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package com.frameworkset.common.poolman.management;

import java.io.Serializable;
import java.net.URL;
import java.net.URLClassLoader;


/**
 * Encountered Class linkage problems in somce ClassLoaders --
 * such as the one used with the Swing and AWT views of JUnit (JUnit's
 * text-based view was fine, since it doesn't use the same CL) and
 * some versions of Tomcat -- that are compounded by extraneous
 * loader context switching in the JMX RI. This ClassLoader does
 * nothing other than insist that MBeans are loaded by a specific
 * loader in order to avoid linkage problems between contexts.
 */
public class JMXClassLoader extends URLClassLoader implements JMXClassLoaderMBean , Serializable{

    private ClassLoader parent;

    public JMXClassLoader(ClassLoader parent) {
        super(new URL[0], parent);
        this.parent = parent;
    }

    public Class loadClass(String name) throws ClassNotFoundException {
        return parent.loadClass(name);
    }

}
