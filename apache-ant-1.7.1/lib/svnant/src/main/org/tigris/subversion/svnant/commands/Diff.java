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
 * svn Diff.   
 * display the differences between two paths. (Unified format)
 * 
 * @author Cédric Chabanois 
 *         <a href="mailto:cchabanois@ifrance.com">cchabanois@ifrance.com</a>
 *
 */
public class Diff extends SvnCommand {
    private SVNUrl oldUrl = null;
    private SVNUrl newUrl = null;
    private File oldPath = null;
    private File newPath = null;
    private SVNRevision oldTargetRevision = null;
    private SVNRevision newTargetRevision = null;
    private File outFile = new File("patch");
    private boolean recurse = true; 

	/* (non-Javadoc)
	 * @see org.tigris.subversion.svnant.SvnCommand#execute(org.tigris.subversion.svnclientadapter.SVNClientAdapter)
	 */
	public void execute() throws SvnAntException {

        try {
            if (oldUrl != null)
                svnClient.diff(oldUrl, oldTargetRevision,
                               newUrl, newTargetRevision,
                               outFile, recurse);
            else
                svnClient.diff(oldPath, oldTargetRevision,
                               newPath, newTargetRevision,
                               outFile, recurse);            
        } catch (SVNClientException e) {
            throw new SvnAntException("Can't get the differences",e);
        }
	}

    /**
     * Ensure we have a consistent and legal set of attributes
     */
    protected void validateAttributes() throws SvnAntValidationException {
        if (oldUrl != null) {
            if ((oldPath != null) || (newPath != null))
                throw new SvnAntValidationException("paths cannot be with urls when diffing");
        }
        else
        {
            if ((oldUrl != null) || (newUrl != null))
                throw new SvnAntValidationException("paths cannot be with urls when diffing");
        }
    }

	/**
	 * @param file
	 */
	public void setNewPath(File file) {
		newPath = file;
	}

	/**
	 * @param revision
	 */
	public void setNewTargetRevision(String revision) {
		this.newTargetRevision = getRevisionFrom(revision);
	}

	/**
	 * @param url
	 */
	public void setNewUrl(SVNUrl url) {
		newUrl = url;
	}

	/**
	 * @param file
	 */
	public void setOldPath(File file) {
		oldPath = file;
	}

	/**
	 * @param revision
	 */
	public void setOldTargetRevision(String revision) {
		this.oldTargetRevision = getRevisionFrom(revision);
	}

	/**
	 * @param url
	 */
	public void setOldUrl(SVNUrl url) {
		oldUrl = url;
	}

	/**
	 * @param file
	 */
	public void setOutFile(File file) {
		outFile = file;
	}

	/**
	 * @param b
	 */
	public void setRecurse(boolean b) {
		recurse = b;
	}

}
