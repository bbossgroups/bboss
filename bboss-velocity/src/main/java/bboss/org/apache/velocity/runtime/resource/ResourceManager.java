package bboss.org.apache.velocity.runtime.resource;

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

import bboss.org.apache.velocity.exception.ParseErrorException;
import bboss.org.apache.velocity.exception.ResourceNotFoundException;
import bboss.org.apache.velocity.runtime.RuntimeServices;

/**
 * Class to manage the text resource for the Velocity
 * Runtime.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:paulo.gaspar@krankikom.de">Paulo Gaspar</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id$
 */
public interface ResourceManager
{
    /**
     * A template resources.
     */
    int RESOURCE_TEMPLATE = 1;

    /**
     * A static content resource.
     */
    int RESOURCE_CONTENT = 2;

    int RESOURCE_SQL = 3;
    int RESOURCE_ES = 4;

    /**
     * Initialize the ResourceManager.
     * @param rs
     */
    void initialize(RuntimeServices rs);

    /**
     * Gets the named resource.  Returned class type corresponds to specified type
     * (i.e. <code>Template</code> to <code>RESOURCE_TEMPLATE</code>).
     *
     * @param resourceName The name of the resource to retrieve.
     * @param resourceType The type of resource (<code>RESOURCE_TEMPLATE</code>,
     *                     <code>RESOURCE_CONTENT</code>, etc.).
     * @param encoding  The character encoding to use.
     * @return Resource with the template parsed and ready.
     * @throws ResourceNotFoundException if template not found
     *          from any available source.
     * @throws ParseErrorException if template cannot be parsed due
     *          to syntax (or other) error.
     */
    Resource getResource(String resourceName, int resourceType, String encoding)
        throws ResourceNotFoundException, ParseErrorException;
    void initTemplate(Resource template, String  encoding);

    /**
     *  Determines is a template exists, and returns name of the loader that
     *  provides it.  This is a slightly less hokey way to support
     *  the Velocity.templateExists() utility method, which was broken
     *  when per-template encoding was introduced.  We can revisit this.
     *
     *  @param resourceName Name of template or content resource
     *  @return class name of loader than can provide it
     */
    String getLoaderNameForResource(String resourceName);

}
