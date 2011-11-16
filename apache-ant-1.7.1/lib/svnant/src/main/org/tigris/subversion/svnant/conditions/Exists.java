/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */ 
package org.tigris.subversion.svnant.conditions;

import java.io.File;
import java.net.MalformedURLException;

import org.apache.tools.ant.Project;
import org.tigris.subversion.svnant.SvnAntException;
import org.tigris.subversion.svnant.SvnTask;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNInfo;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNRevision;
import org.tigris.subversion.svnclientadapter.SVNUrl;

/**
 * Instances of this class are used to test the existence of a repository
 * element. It implements the Condition interface, as defined in ANT and
 * can be used within condition tasks.
 * 
 * This condition is based on logic from the "info" command. If info can be
 * retrieved, then the condition is true. If info fails, then th condition is
 * false.
 * 
 * @author Jean-Pierre Fiset <a href="mailto:jp@fiset.ca">jp@fiset.ca</a>
 *
 */
public class Exists extends SvnCondition {
	
   /**
     * The target to retrieve properties for.
     */
    private String target = null;
	
	public boolean internalEval() throws SvnAntException {
		// Obtain a svnClient according to javahl and svnkit properties
		ISVNClientAdapter svnClient = SvnTask.getClientAdapter(this);

		// Retrieve info for the requested element
		ISVNInfo info = null;
		try {
			File targetAsFile = new File(Project.translatePath(this.target));
			if (targetAsFile.exists()) {
				// Since the target exists locally, assume it's not a URL.
				info = svnClient.getInfo(targetAsFile);
			} else {
				try {
					SVNUrl url = new SVNUrl(this.target);
					info = svnClient.getInfo(url);
				} catch (MalformedURLException malformedURL) {
					// Since we don't have a valid URL with which to
					// contact the repository, assume the target is a
					// local file, even though it doesn't exist locally.
					info = svnClient.getInfo(targetAsFile);
				}
			}
		} catch (SVNClientException e) {
			// Assume that it is not existant
			return false;
		}
		
		// No info -> not in repository
		if( null == info ) {
			return false;
		}
		// No revision -> not in repository
		if ((info.getRevision() == null) || (SVNRevision.INVALID_REVISION.equals(info.getRevision()))) {
        	return false;
        }
		
		// Assume it is...
		return true;
	}

	/**
	 * @return the target
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(String target) {
		this.target = target;
	}

}
