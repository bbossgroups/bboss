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
import java.util.Date;

import org.apache.tools.ant.Project;
import org.tigris.subversion.svnant.SvnAntException;
import org.tigris.subversion.svnant.SvnAntValidationException;
import org.tigris.subversion.svnclientadapter.ISVNStatus;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNUrl;

/**
 * get the status of a file or a directory
 */
public class Status extends SvnCommand {

	private File path = null;
	private String textStatusProperty = null;
	private String propStatusProperty = null;
	private String revisionProperty = null;
	private String lastChangedRevisionProperty = null;
	private String lastChangedDateProperty = null;
	private String lastCommitAuthorProperty = null;
	private String urlProperty = null;
	
	/* (non-Javadoc)
	 * @see org.tigris.subversion.svnant.SvnCommand#execute(org.tigris.subversion.svnclientadapter.ISVNClientAdapter)
	 */
	public void execute() throws SvnAntException {
		
		Project aProject = getProject();
		try {
			ISVNStatus status = svnClient.getSingleStatus(path);
			
			if (textStatusProperty != null) {
				aProject.setProperty(textStatusProperty, status.getTextStatus().toString());
			}
			
			if (propStatusProperty != null) {
				aProject.setProperty(propStatusProperty, status.getPropStatus().toString());
			}
			
			if (revisionProperty != null) {
				String revision;
				if( null == status.getRevision() ) {
					revision = "-1";
				} else {
					revision = status.getRevision().toString();
				}
				aProject.setProperty(revisionProperty, revision);
			}
			if (lastChangedRevisionProperty != null) {
				String lastChangedRevision;
				if (status.getLastChangedRevision() == null) {
					lastChangedRevision = "";
				} else {
					lastChangedRevision = status.getLastChangedRevision().toString();
				}
				aProject.setProperty(lastChangedRevisionProperty, lastChangedRevision);
			}
			if (lastCommitAuthorProperty != null) {
				String lastCommitAuthor = status.getLastCommitAuthor();
				if (lastCommitAuthor == null) {
					lastCommitAuthor = "";
				}
				aProject.setProperty(lastCommitAuthorProperty,lastCommitAuthor);
			}
			if (lastChangedDateProperty != null) {
				Date lastChangedDate = status.getLastChangedDate();
				if (lastChangedDate == null) {
					aProject.setProperty(lastChangedDateProperty, "");
				} else {
					aProject.setProperty(lastChangedDateProperty, getDateStringFor(lastChangedDate));
				}
				
			}
			if (urlProperty != null) {
				SVNUrl statusUrl = status.getUrl();
				aProject.setProperty(urlProperty, (statusUrl != null) ? statusUrl.toString() : "");
			}
			
		} catch (SVNClientException e) {
			throw new SvnAntException("Can't get status of "+path, e);
		}

	}

	/**
	 * Ensure we have a consistent and legal set of attributes
	 */
	protected void validateAttributes() throws SvnAntValidationException {
        if (path == null) {
        	throw new SvnAntValidationException("path attribute must be set");
        }
	}	
	
	/**
	 * @param path The path to set.
	 */
	public void setPath(File path) {
		this.path = path;
	}

	/**
	 * @param textStatusProperty The textStatusProperty to set.
	 */
	public void setTextStatusProperty(String textStatusProperty) {
		this.textStatusProperty = textStatusProperty;
	}
	
	/**
	 * @param propStatusProperty The propStatusProperty to set.
	 */
	public void setPropStatusProperty(String propStatusProperty) {
		this.propStatusProperty = propStatusProperty;
	}
	
	/**
	 * @param revisionProperty The revisionProperty to set.
	 */
	public void setRevisionProperty(String revisionProperty) {
		this.revisionProperty = revisionProperty;
	}
	
	/**
	 * @param lastChangedRevisionProperty The lastChangedRevisionProperty to set.
	 */
	public void setLastChangedRevisionProperty(
			String lastChangedRevisionProperty) {
		this.lastChangedRevisionProperty = lastChangedRevisionProperty;
	}
	
	/**
	 * @param lastCommitAuthorProperty The lastCommitAuthor to set.
	 */
	public void setLastCommitAuthorProperty(String lastCommitAuthorProperty) {
		this.lastCommitAuthorProperty = lastCommitAuthorProperty;
	}

	/**
	 * @param lastChangedDateProperty The lastChangedDateProperty to set.
	 */
	public void setLastChangedDateProperty(String lastChangedDateProperty) {
		this.lastChangedDateProperty = lastChangedDateProperty;
	}

	/**
	 * @param urlProperty The url to set.
	 */
	public void setUrlProperty(String urlProperty) {
		this.urlProperty = urlProperty;
	}

}
