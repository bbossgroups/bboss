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

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;
import org.tigris.subversion.svnant.SvnAntException;
import org.tigris.subversion.svnant.SvnAntValidationException;
import org.tigris.subversion.svnclientadapter.SVNClientException;

/**
 * svn Revert. Restore pristine working copy file (undo most local edits)
 * @author Cédric Chabanois 
 *         <a href="mailto:cchabanois@ifrance.com">cchabanois@ifrance.com</a>
 *
 */
public class Revert extends SvnCommand {
	
	/** file to revert */
	private File file = null;
	
	/** dir to revert */
	private File dir = null;
    
    /** descend recursively */
    private boolean recurse = false;
	
	/** filesets to revert */
	private Vector filesets = new Vector();	
	
	public void execute() throws SvnAntException {
		
		if (file != null)
			revertFile(file,false);
			
		if (dir != null)
			revertFile(dir,recurse);
			
		// deal with filesets
		if (filesets.size() > 0) {
			for (int i = 0; i < filesets.size(); i++) {
				FileSet fs = (FileSet) filesets.elementAt(i);
				revertFileSet(fs);
			}
		}
	}

	/**
	 * Ensure we have a consistent and legal set of attributes
	 */
	protected void validateAttributes() throws SvnAntValidationException {
        if (file != null) {
            if (dir != null)
                throw new SvnAntValidationException("Don't use both file and dir attribute");
            if (filesets.size() > 0)
                throw new SvnAntValidationException("Don't use both file attribute and filesets");
        }
        else
        if (dir != null) {
            if (filesets.size() > 0)
                throw new SvnAntValidationException("Don't use both file attribute and filesets");            
        }
	}

	/**
	 * Revert file or directory
     *
	 * @param aFile
	 * @param force
	 * @throws SvnAntException
	 */
	private void revertFile(File aFile, boolean doRecurse) throws SvnAntException {
		try {
            svnClient.revert(aFile, doRecurse);
		} catch (SVNClientException e) {
			throw new SvnAntException("Cannot revert file or directory "+aFile.getAbsolutePath(),e);
		}
	}

	/**
	 * revert a fileset (both dirs and files)
	 * @param svnClient
	 * @param fs
	 * @throws SvnAntException
	 */
	private void revertFileSet(FileSet fs) throws SvnAntException {
		DirectoryScanner ds = fs.getDirectoryScanner(getProject());
		File baseDir = fs.getDir(getProject()); // base dir
		String[] files = ds.getIncludedFiles();
		String[] dirs = ds.getIncludedDirectories();
        File[] filesAndDirs = new File[files.length+dirs.length];
        int j = 0;

		for (int i = 0; i < dirs.length; i++) {
            filesAndDirs[j] = new File(baseDir, dirs[i]);
            j++;
		}
		for (int i = 0; i < files.length; i++) {
            filesAndDirs[j] = new File(baseDir, files[i]);
            j++;
		}
        
        try {
            for (int i = 0; i < filesAndDirs.length;i++) {
                svnClient.revert(filesAndDirs[i],false);
            }
        } catch (SVNClientException e) {
            logError("Cannot revert file " + file.getAbsolutePath());
        }
	}

	/**
	 * set file to revert
	 * @param file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * set directory to revert
	 * @param dir
	 */
	public void setDir(File dir) {
		this.dir = dir;
	}
	
	/**
	 * Set the recurse flag
	 * @param recurse
	 */
	public void setRecurse(boolean recurse) {
		this.recurse = recurse;
	}

	/**
	 * Adds a set of files to add
	 * @param set
	 */
	public void addFileset(FileSet set) {
		filesets.addElement(set);
	}	
	
	/**
	 * Adds a set of files to add
	 * @param set
	 */
	public void add(FileSet set) {
		filesets.addElement(set);
	}	
	
}
