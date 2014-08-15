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




/**
 * <p>Title: Buffer.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2009-10-8 下午11:24:01
 * @author biaoping.yin
 * @version 1.0
 */
public class Buffer
{
	private final byte[] buf;
    private final int offset;
    private final int length;

    public Buffer(byte[] buf, int offset, int length) {
        this.buf=buf;
        this.offset=offset;
        this.length=length;
    }

    public byte[] getBuf() {
        return buf;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

    public Buffer copy() {
        byte[] new_buf=buf != null? new byte[length] : null;
        int new_length=new_buf != null? new_buf.length : 0;
        if(new_buf != null)
            System.arraycopy(buf, offset, new_buf, 0, length);
        return new Buffer(new_buf, 0, new_length);
    }

    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append(length).append(" bytes");
        if(offset > 0)
            sb.append(" (offset=").append(offset).append(")");
        return sb.toString();
    }
}
