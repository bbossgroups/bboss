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
package org.tigris.subversion.svnant.selectors;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.types.selectors.BaseExtendSelector;
import org.tigris.subversion.svnant.ISvnAntProjectComponent;
import org.tigris.subversion.svnant.SvnAntException;
import org.tigris.subversion.svnant.SvnTask;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.utils.StringUtils;

/**
 * This is an abstract class that implements all functionality shared
 * between all file selectors in svn-ant. In particular, it implements
 * the handling of the common parameters: javahl and svnkit. It provides
 * the logic to select the approrpriate client adapter. Finally, it implements
 * the method required by all Ant selectors (isSelected) and redirects
 * the control flow to a subclass implementation while providing the
 * appropriate client adapter.  
 * 
 * @author Jean-Pierre Fiset <a href="mailto:jp@fiset.ca">jp@fiset.ca</a>
 *
 */
public abstract class BaseSvnSelector extends BaseExtendSelector implements ISvnAntProjectComponent {

    private boolean javahl = true;
    
    private boolean svnkit = true;

    /**
     * 'failonerror' property for file selector.
     */
	private boolean failonerror = true;

    /* (non-Javadoc)
	 * @see org.tigris.subversion.svnant.ISvnAntProjectComponent#getJavahl()
	 */
	public boolean getJavahl() {
		return javahl;
	}

	/* (non-Javadoc)
	 * @see org.tigris.subversion.svnant.ISvnAntProjectComponent#getSvnKit()
	 */
	public boolean getSvnKit() {
		return svnkit;
	}

	/* (non-Javadoc)
	 * @see org.tigris.subversion.svnant.ISvnAntProjectComponent#getProjectComponent()
	 */
	public ProjectComponent getProjectComponent() {
		return this;
	}

    /**
     * Accessor method to 'javahl' property. If reset (false),
     * JavaHL is not used.
     * @param javahl_ New value for javahl property.
     */
    public void setJavahl(boolean javahl_) {
        javahl = javahl_;
    }

    /**
     * Accessor method to 'svnkit' property. If reset (false),
     * SVNKit is not used.
     * @param svnkit_ New value for svnkit property.
     */
    public void setSvnkit(boolean svnkit_) {
        svnkit = svnkit_;
    }
    
	/**
	 * @param failonerror the failonerror to set
	 */
	public void setFailonerror(boolean failonerror) {
		this.failonerror = failonerror;
	}

	final public boolean isSelected(File basedir_, String filename_, File file_) throws BuildException {
		
		String[] nameSegments = StringUtils.split(getClass().getName(), ".");
		String className = nameSegments[nameSegments.length -1]; 

		try {
			return isSelected(SvnTask.getClientAdapter(this), basedir_, filename_, file_);
		} catch (SvnAntException ex) {
			if (this.failonerror) {
				log("selector " + className + " failed !", Project.MSG_INFO);
				throw new BuildException(ex.getMessage(), ex.getCause());
			} else {
				log("selector " + className + " failed :" + ex.getLocalizedMessage(), Project.MSG_ERR);
				return false;
			}
		}
	}
	
	/**
	 * Method that needs to be reimplemented by each subclass. It is equivalent to 'isSelected',
	 * inherited from BaseExtendSelector, with the exception that a SVN client adaptor is provided. 
	 * @param svnClient_ The SVN client that should be used to perform repository access
	 * @param basedir_ A java.io.File object for the base directory
	 * @param filename_ The name of the file to check
	 * @param file_ A File object for this filename
	 * @exception SvnAntException if an error occurs
	 * @return Returns true if the file should be selected. Otherwise, false. 
	 */
	abstract public boolean isSelected(ISVNClientAdapter svnClient_, File basedir_, String filename_, File file_) throws SvnAntException;
	
}
