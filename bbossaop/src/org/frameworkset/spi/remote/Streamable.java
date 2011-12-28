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

package org.frameworkset.spi.remote;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * <p>Title: Streamable.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-10-11 ÉÏÎç11:33:20
 * @author biaoping.yin
 * @version 1.0
 */
public interface Streamable
{
	/** Write the entire state of the current object (including superclasses) to outstream.
     * Note that the output stream <em>must not</em> be closed */
    void writeTo(DataOutputStream out) throws IOException;

    /** Read the state of the current object (including superclasses) from instream
     * Note that the input stream <em>must not</em> be closed */
    void readFrom(DataInputStream in) throws IOException, IllegalAccessException, InstantiationException;
}
