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
import org.tigris.subversion.svnclientadapter.SVNRevision;

/**
 * svn Update. Bring changes from the repository into the working copy.
 * @author Cédric Chabanois 
 *         <a href="mailto:cchabanois@ifrance.com">cchabanois@ifrance.com</a>
 *
 */
public class Update extends SvnCommand {
	/** file to update */
	private File file = null;
	
	/** dir to update */
	private File dir = null;
	
	/** filesets to update */
	private Vector filesets = new Vector();	
	
	private SVNRevision revision = SVNRevision.HEAD;
	
	private boolean recurse = true;

	public void execute() throws SvnAntException {
		
		if (file != null)
		{
			try {
				svnClient.update(file, revision, false);
			} catch (SVNClientException e) {
				throw new SvnAntException("Cannot update file "+file.getAbsolutePath(),e);
			}
		}
			
		if (dir != null) {
			try {
				svnClient.update(dir, revision, recurse);
			} catch (SVNClientException e) {
				throw new SvnAntException("Cannot update dir "+dir.getAbsolutePath(),e);
			}			
		}
		
		// deal with filesets
		if (filesets.size() > 0) {
			for (int i = 0; i < filesets.size(); i++) {
				FileSet fs = (FileSet) filesets.elementAt(i);
				updateFileSet(fs);
			}
		}
	}

	/**
	 * Ensure we have a consistent and legal set of attributes
	 */
	protected void validateAttributes() throws SvnAntValidationException {
		if ((file == null) && (dir == null) && (filesets.size() == 0))
			throw new SvnAntValidationException("file, url or fileset must be set");         
		if (revision == null)
			throw SvnAntValidationException.createInvalidRevisionException();
	}

	/**
	 * updates a fileset (both dirs and files)
	 * @param svnClient
	 * @param fs
	 * @throws SvnAntException
	 */
	private void updateFileSet(FileSet fs) throws SvnAntException {
		DirectoryScanner ds = fs.getDirectoryScanner(getProject());
		File baseDir = fs.getDir(getProject()); // base dir
		String[] files = ds.getIncludedFiles();
		String[] dirs = ds.getIncludedDirectories();

		// first : we update directories
		for (int i = 0; i < dirs.length; i++) {
			File aDir = new File(baseDir, dirs[i]);
			try {
				svnClient.update(aDir,revision,false);
			} catch (SVNClientException e) {
				logError("Cannot update directory " + aDir.getAbsolutePath());
			}
		}

		// then we update files
		for (int i = 0; i < files.length; i++) {
			File aFile = new File(baseDir, files[i]);
			try {
				svnClient.update(aFile,revision,false);
			} catch (SVNClientException e) {
				logError("Cannot update file " + aFile.getAbsolutePath());
			}
		}
	}

	/**
	 * set the file to update
	 * @param file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * set the directory to update
	 * @param dir
	 */
	public void setDir(File dir) {
		this.dir = dir;
	}
	
	/**
	 * if set, directory will be updated recursively 
	 * @param recurse
	 */
	public void setRecurse(boolean recurse) {
		this.recurse = recurse;
	}
	
	/**
	 * Sets the revision
	 * 
	 * @param revision
	 */
	public void setRevision(String revision) {
		this.revision = getRevisionFrom(revision);
	}

	/**
	 * Adds a set of files to update
	 * @param set
	 */
	public void addFileset(FileSet set) {
		filesets.addElement(set);
	}	
	
	/**
	 * Adds a set of files to update
	 * @param set
	 */
	public void add(FileSet set) {
		filesets.addElement(set);
	}	
}
