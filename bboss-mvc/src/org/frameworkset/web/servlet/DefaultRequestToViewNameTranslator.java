/**
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
package org.frameworkset.web.servlet;

import javax.servlet.http.HttpServletRequest;

import org.frameworkset.util.Assert;
import org.frameworkset.web.util.UrlPathHelper;

import com.frameworkset.util.StringUtil;

/**
 * <p>Title: DefaultRequestToViewNameTranslator.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008-2010</p>
 * @Date 2010-10-2
 * @author biaoping.yin
 * @version 1.0
 */
public class DefaultRequestToViewNameTranslator  implements RequestToViewNameTranslator {

	private static final String SLASH = "/";


	private String prefix = "";

	private String suffix = "";

	private String separator = SLASH;

	private boolean stripLeadingSlash = true;

	private boolean stripExtension = true;

	private UrlPathHelper urlPathHelper = new UrlPathHelper();


	/**
	 * Set the prefix to prepend to generated view names.
	 * @param prefix the prefix to prepend to generated view names
	 */
	public void setPrefix(String prefix) {
		this.prefix = (prefix != null ? prefix : "");
	}

	/**
	 * Set the suffix to append to generated view names.
	 * @param suffix the suffix to append to generated view names
	 */
	public void setSuffix(String suffix) {
		this.suffix = (suffix != null ? suffix : "");
	}

	/**
	 * Set the value that will replace '<code>/</code>' as the separator
	 * in the view name. The default behavior simply leaves '<code>/</code>'
	 * as the separator.
	 * @param separator the desired separator value
	 */
	public void setSeparator(String separator) {
		this.separator = separator;
	}

	/**
	 * Set whether or not leading slashes should be stripped from the URI when
	 * generating the view name. Default is "true".
	 * @param stripLeadingSlash <code>true</code> if leading slashes are to be stripped
	 */
	public void setStripLeadingSlash(boolean stripLeadingSlash) {
		this.stripLeadingSlash = stripLeadingSlash;
	}

	/**
	 * Set whether or not file extensions should be stripped from the URI when
	 * generating the view name. Default is "true".
	 * @param stripExtension <code>true</code> if file extensions should be stripped
	 */
	public void setStripExtension(boolean stripExtension) {
		this.stripExtension = stripExtension;
	}

	/**
	 * Set if URL lookup should always use the full path within the current servlet
	 * context. Else, the path within the current servlet mapping is used
	 * if applicable (i.e. in the case of a ".../*" servlet mapping in web.xml).
	 * Default is "false".
	 * @param alwaysUseFullPath <code>true</code> if URL lookup should always use the full path
	 * @see UrlPathHelper#setAlwaysUseFullPath
	 */
	public void setAlwaysUseFullPath(boolean alwaysUseFullPath) {
		this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
	}

	/**
	 * Set if the context path and request URI should be URL-decoded.
	 * Both are returned <i>undecoded</i> by the Servlet API,
	 * in contrast to the servlet path.
	 * <p>Uses either the request encoding or the default encoding according
	 * to the Servlet spec (ISO-8859-1).
	 * @see UrlPathHelper#setUrlDecode
	 */
	public void setUrlDecode(boolean urlDecode) {
		this.urlPathHelper.setUrlDecode(urlDecode);
	}

	/**
	 * Set the {@link UrlPathHelper} to use for
	 * the resolution of lookup paths.
	 * <p>Use this to override the default UrlPathHelper with a custom subclass,
	 * or to share common UrlPathHelper settings across multiple web components.
	 * @param urlPathHelper the desired helper
	 * @throws IllegalArgumentException if the supplied UrlPathHelper is <code>null</code>
	 */
	public void setUrlPathHelper(UrlPathHelper urlPathHelper) {
		Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
		this.urlPathHelper = urlPathHelper;
	}


	/**
	 * Translates the request URI of the incoming {@link HttpServletRequest}
	 * into the view name based on the configured parameters.
	 * @see UrlPathHelper#getLookupPathForRequest
	 * @see #transformPath
	 */
	public String getViewName(HttpServletRequest request) {
		String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
		return (this.prefix + transformPath(lookupPath) + this.suffix);
	}

	/**
	 * Transform the request URI (in the context of the webapp) stripping
	 * slashes and extensions, and replacing the separator as required.
	 * @param lookupPath the lookup path for the current request,
	 * as determined by the UrlPathHelper
	 * @return the transformed path, with slashes and extensions stripped
	 * if desired
	 */
	protected String transformPath(String lookupPath) {
		String path = lookupPath;
		if (this.stripLeadingSlash && path.startsWith(SLASH)) {
			path = path.substring(1);
		}
		if (this.stripExtension) {
			path = StringUtil.stripFilenameExtension(path);
		}
		if (!SLASH.equals(this.separator)) {
			path = StringUtil.replace(path, SLASH, this.separator);
		}
		return path;
	}

}
