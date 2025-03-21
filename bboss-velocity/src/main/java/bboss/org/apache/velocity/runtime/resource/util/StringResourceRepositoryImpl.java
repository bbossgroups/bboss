package bboss.org.apache.velocity.runtime.resource.util;

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

import bboss.org.apache.velocity.runtime.RuntimeConstants;
import bboss.org.apache.velocity.runtime.resource.Resource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of StringResourceRepository.
 * Uses a HashMap for storage
 *
 * @author <a href="mailto:eelco.hillenius@openedge.nl">Eelco Hillenius</a>
 * @author <a href="mailto:henning@apache.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 * @since 1.5
 */
public class StringResourceRepositoryImpl implements StringResourceRepository
{
    /**
     * mem store
     */
    protected Map<String, StringResource> resources = Collections.synchronizedMap(new HashMap<>());

    /**
     * Current Repository encoding.
     */
    private String encoding = RuntimeConstants.ENCODING_DEFAULT;

    /**
     * @see StringResourceRepository#getStringResource(java.lang.String)
     */
    @Override
    public StringResource getStringResource(final String name)
    {
        return resources.get(name);
    }

    /**
     * @see StringResourceRepository#putStringResource(java.lang.String, java.lang.String)
     */
    @Override
    public void putStringResource(final String name, final String body)
    {
        resources.put(name, new StringResource(body, getEncoding()));
    }

    /**
     * @see StringResourceRepository#putStringResource(java.lang.String, java.lang.String, java.lang.String)
     * @since 1.6
     */
    @Override
    public void putStringResource(final String name, final String body, final String encoding)
    {
        resources.put(name, new StringResource(body, encoding));
    }

    /**
     * @see StringResourceRepository#removeStringResource(java.lang.String)
     */
    @Override
    public void removeStringResource(final String name)
    {
        resources.remove(name);
    }

    /**
     * @see bboss.org.apache.velocity.runtime.resource.util.StringResourceRepository#getEncoding()
     */
    @Override
    public String getEncoding()
    {
        return encoding;
    }

    /**
     * @see bboss.org.apache.velocity.runtime.resource.util.StringResourceRepository#setEncoding(java.lang.String)
     */
    @Override
    public void setEncoding(final String encoding)
    {
        this.encoding = encoding;
    }
}
