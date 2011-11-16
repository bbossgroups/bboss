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
import java.util.Vector;

import org.apache.tools.ant.types.FileSet;
import org.tigris.subversion.svnant.SvnAntException;
import org.tigris.subversion.svnant.SvnAntValidationException;
import org.tigris.subversion.svnclientadapter.SVNKeywords;

/**
 * ancestor of KeywordSet ...
 * @author Cédric Chabanois 
 *         <a href="mailto:cchabanois@ifrance.com">cchabanois@ifrance.com</a>
 *
 */
public abstract class Keywords extends SvnCommand {
    // the keywords available for substitution
    protected SVNKeywords keywords;

    /** file concerned by keywords substitution */
    protected File file = null;

    /** filesets concerned by keywords substitution */
    protected Vector filesets = new Vector();

    /** directory concerned by keywords substitution */
    protected File dir = null;

    /** set keywords substitution recursively ? (only for dir attribute) */
    protected boolean recurse = true;

    public void execute() throws SvnAntException {
        // we do nothing there but this function is overloaded  
    }

    /**
     * Ensure we have a consistent and legal set of attributes
     */
    protected void validateAttributes() throws SvnAntValidationException {

        if ((file == null) && (dir == null) && (filesets.size() == 0))
            throw new SvnAntValidationException("file, url or fileset must be set");
    }
    
	public void setHeadURL(boolean b) {
		keywords.setHeadUrl(b);
	}

    public void setURL(boolean b) {
        keywords.setHeadUrl(b);
    }

	public void setId(boolean b) {
        keywords.setId(b);
	}

	public void setLastChangedBy(boolean b) {
		keywords.setLastChangedBy(b);
	}

    public void setAuthor(boolean b) {
        keywords.setLastChangedBy(b);
    }

	public void setLastChangedDate(boolean b) {
		keywords.setLastChangedDate(b);
	}

    public void setDate(boolean b) {
        keywords.setLastChangedDate(b);
    }


	public void setLastChangedRevision(boolean b) {
		keywords.setLastChangedRevision(b);
	}
    
    public void setRev(boolean b) {
        keywords.setLastChangedRevision(b);
    }

    /**
     * set file on which to set keywords substitution 
     * @param file
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * set directory on which to set keywords substitution 
     * @param dir
     */
    public void setDir(File dir) {
        this.dir = dir;
    }

    /**
     * if set, keywords substitution will be set recursively
     * @param recurse
     */
    public void setRecurse(boolean recurse) {
        this.recurse = recurse;
    }

    /**
     * Adds a set of files on which to apply keywords substitution
     * @param set
     */
    public void addFileset(FileSet set) {
        filesets.addElement(set);
    }

    /**
     * Adds a set of files on which to apply keywords substitution
     * @param set
     */
    public void add(FileSet set) {
        filesets.addElement(set);
    }

    /**
     * set the keywords
     * @param keywords
     */
    public void setKeywords(SVNKeywords keywords) {
        this.keywords = keywords;
    }

}
