package org.frameworkset.spi.remote.http;
/**
 * Copyright 2008 biaoping.yin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/6/19 21:43
 * @author biaoping.yin
 * @version 1.0
 */

import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds all of the variables needed to describe an HTTP connection to a host.
 * This includes remote host name, port and scheme.
 *
 * @since 4.0
 */
@Contract(threading = ThreadingBehavior.IMMUTABLE)
public final class HttpHost implements Cloneable, Serializable {

	private static final long serialVersionUID = -7529410654042457626L;



	protected String hostAddress;
	protected Map<String,Object> attributes;
	protected String routing;



	/**
	 * Creates {@code HttpHost} instance with the default scheme and port and the given hostname.
	 *
	 * @param hostname  the hostname (IP or DNS name)
	 */
	public HttpHost(final String hostname) {
		if (!hostname.startsWith("http://") && !hostname.startsWith("https://")) {
			this.hostAddress = "http://" + hostname;
		}
		else {
			this.hostAddress = hostname;
		}
	}





	/**
	 * Copy constructor for {@link HttpHost HttpHost}.
	 *
	 * @param httphost the HTTP host to copy details from
	 */
	public HttpHost (final HttpHost httphost) {
		super();
		Args.notNull(httphost, "HTTP host");

		this.hostAddress = httphost.hostAddress;
		this.attributes = httphost.attributes;
		this.routing = httphost.routing;
	}




	@Override
	public String toString() {

			return hostAddress;

	}


	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj instanceof HttpHost) {
			final HttpHost that = (HttpHost) obj;
			return this.hostAddress.equals(that.hostAddress);
		}
		return false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = LangUtils.HASH_SEED;
		hash = LangUtils.hashCode(hash, this.hostAddress);

		return hash;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
	public void addAttribute(String key,Object value){
		if(attributes == null){
			attributes = new HashMap<String, Object>();
		}
		attributes.put(key,value);
	}
	public Object getAttribute(String key){
		if(attributes != null) {
			return attributes.get(key);
		}
		else{
			return null;
		}
	}
	public String getRouting(){
		return this.routing;
	}
	public void setRouting(String routing){
		this.routing = routing;
	}
}
