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
package org.tigris.subversion.svnant.commands;

import java.io.File;

import org.tigris.subversion.svnant.SvnAntException;
import org.tigris.subversion.svnant.SvnAntValidationException;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNRevision;
import org.tigris.subversion.svnclientadapter.SVNUrl;

/**
 * svn Move. Moves or renames a file
 * @author Cédric Chabanois 
 *         <a href="mailto:cchabanois@ifrance.com">cchabanois@ifrance.com</a>
 *
 */
public class Move extends SvnCommand {
    private File srcPath = null;
    private File destPath = null;
    private SVNUrl srcUrl = null;
    private SVNUrl destUrl = null;
	private String message = null;
    private boolean force = false;

    public void execute() throws SvnAntException {

        if (srcPath != null) {
            try {
                svnClient.move(srcPath, destPath, force);
            } catch (SVNClientException e) {
                throw new SvnAntException("Can't copy", e);
            }
        } else {
            try {
                svnClient.move(srcUrl, destUrl, message, SVNRevision.HEAD);
            } catch (SVNClientException e) {
                throw new SvnAntException("Can't copy", e);
            }
        }

    }

    /**
     * Ensure we have a consistent and legal set of attributes
     */
    protected void validateAttributes() throws SvnAntValidationException {
        if (((srcPath == null) && (srcUrl == null))
            || ((srcPath != null) && (srcUrl != null)))
            throw new SvnAntValidationException("Either srcPath attribute or srcUrl attribute must be set");

        if (srcPath != null) {
            if (destPath == null)
                throw new SvnAntValidationException("destPath attribute must be set when srcPath is set");
            if (destUrl != null)
                throw new SvnAntValidationException("destUrl attribute cannot be used when srcPath is set");
        }

        if (srcUrl != null) {
            if (destUrl == null)
                throw new SvnAntValidationException("destUrl attribute must be set when srcUrl is set");
            if (destPath != null)
                throw new SvnAntValidationException("destPath attribute cannot be used when srcUrl is set");
            if (message == null)
            	throw new SvnAntValidationException("message attribute must be set when srcUrl is set");
        }
    }

	/**
	 * set the path to move from
	 * @param srcPath
	 */
    public void setSrcPath(File srcPath) {
        this.srcPath = srcPath;
    }

	/**
	 * set the path to move to
	 * @param destPath
	 */
    public void setDestPath(File destPath) {
        this.destPath = destPath;
    }

	/**
	 * set the url to move from
	 * @param srcUrl
	 */
    public void setSrcUrl(SVNUrl srcUrl) {
        this.srcUrl = srcUrl;
    }

	/**
	 * set the url to move to
	 * @param destUrl
	 */
    public void setDestUrl(SVNUrl destUrl) {
        this.destUrl = destUrl;
    }
    
    /**
     * set the message for commit when using destUrl
     * @param message
     */
    public void setMessage(String message) {
    	this.message = message;
    }

    /**
     * set the force parameter
     * @param force
     */
    public void setForce(boolean force) {
        this.force = force;
    }

}
