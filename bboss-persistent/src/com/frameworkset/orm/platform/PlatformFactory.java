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
package com.frameworkset.orm.platform;

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

import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * Factory class responsible to create Platform objects that
 * define RDBMS platform specific behaviour.
 *
 * @author Thomas Mahler
 * @author <a href="mailto:mpoeschl@marmot.at">Martin Poeschl</a>
 * @version $Id: PlatformFactory.java,v 1.2 2004/02/22 06:27:19 jmcnally Exp $
 */
public class PlatformFactory
{
    private static HashMap platforms = new HashMap();
    private static Logger log = Logger.getLogger(PlatformFactory.class);

    /**
     * Returns the Platform for a platform name.
     *
     * @param dbms name of the platform
     */
    public static Platform getPlatformFor(String dbms)
    {
        Platform result = null;
        String platformName = null;

        result = (Platform) getPlatforms().get(dbms);
        if (result == null)
        {
            try
            {
                platformName = getClassnameFor(dbms);
                Class platformClass = Class.forName(platformName);
                result = (Platform) platformClass.newInstance();

            }
            catch (Throwable t)
            {
                log.warn("problems with platform " + platformName
                        + ": " + t.getMessage());
                log.warn("Torque will use PlatformDefaultImpl instead");

                result = new PlatformDefaultImpl();
            }
            getPlatforms().put(dbms, result); // cache the Platform
        }
        return result;
    }

    /**
     * compute the name of the concrete Class representing the Platform
     * specified by <code>platform</code>
     *
     * @param platform the name of the platform as specified in the repository
     */
    private static String getClassnameFor(String platform)
    {
        String pf = "Default";
        if (platform != null)
        {
            pf = platform;
        }
        return "com.frameworkset.orm.platform.Platform" + pf.substring(0, 1).toUpperCase() + pf.substring(1) + "Impl";
    }

    /**
     * Gets the platforms.
     *
     * @return Returns a HashMap
     */
    private static HashMap getPlatforms()
    {
        return platforms;
    }
}
