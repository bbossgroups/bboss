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
import java.util.Stack;
import java.util.Vector;

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;
import org.tigris.subversion.svnant.SvnAntException;
import org.tigris.subversion.svnant.SvnAntValidationException;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.utils.SVNStatusUtils;

/**
 * svn Add. Add a file, a directory or a set of files to repository
 * @author Cédric Chabanois 
 *         <a href="mailto:cchabanois@ifrance.com">cchabanois@ifrance.com</a>
 *
 */
public class Add extends SvnCommand {
    /** file to add to the repository */
    private File file = null;

    /** filesets to add to the repository */
    private Vector filesets = new Vector();

    /** do not fail when file or directory to add is not found */
    private boolean failonerror = false;

    /** directory to add to the repository */
    private File dir = null;

    /** add recursively ? (only for dir attribute) */
    private boolean recurse = true;

    /** check directories already under version control during add ? (only for dir attribute) */
    private boolean force = false;

    public void execute() throws SvnAntException {

        // deal with the single file
        if (file != null) {
            svnAddFile(file);
        }

        // deal with a directory
        if (dir != null) {
            svnAddDir(dir, recurse, force);
        }

        // deal with filesets
        if (filesets.size() > 0) {
            for (int i = 0; i < filesets.size(); i++) {
                FileSet fs = (FileSet) filesets.elementAt(i);
                svnAddFileSet(fs);
            }
        }

    }

    /**
     * Ensure we have a consistent and legal set of attributes
     */
    protected void validateAttributes() throws SvnAntValidationException {

        if ((file == null) && (dir == null) && (filesets.size() == 0))
            throw new SvnAntValidationException("file, url or fileset must be set");
    }

    /**
     * add a file to the repository
     * @param svnClient
     * @param aFile
     * @throws SvnAntException
     */
    private void svnAddFile(File aFile) throws SvnAntException {
        if (aFile.exists()) {
            if (aFile.isDirectory()) {
                logWarning(
                    "Directory "
                        + aFile.getAbsolutePath()
                        + " cannot be added using the file attribute.  "
                        + "Use dir instead.");
            } else {
                try {
                    svnClient.addFile(aFile);
                } catch (Exception e) {
                    throw new SvnAntException(
                        "Can't add file "
                            + aFile.getAbsolutePath()
                            + " to repository",
                        e);
                }
            }
        } else {
            String message =
                "Warning: Could not find file "
                    + aFile.getAbsolutePath()
                    + " to add to the repository.";
            if (!failonerror) {
            	logWarning(message);
            } else {
                throw new SvnAntException(message);
            }
        }
    }

    /**
     * add a directory to the repository
     * @param svnClient
     * @param aDir
     * @param recursive
     * @param force
     * @throws SvnAntException
     */
    private void svnAddDir(File aDir, boolean recursive, boolean force) throws SvnAntException {
        if (aDir.exists()) {
            if (!aDir.isDirectory()) {
                logWarning(
                    "File "
                        + aDir.getAbsolutePath()
                        + " cannot be added using the dir attribute.  "
                        + "Use file instead.");
            } else {

                try {
                    svnClient.addDirectory(aDir, recursive, force);
                } catch (Exception e) {
                    throw new SvnAntException(
                        "Can't add directory "
                            + aDir.getAbsolutePath()
                            + " to repository",
                        e);
                }
            }
        } else {
            String message =
                "Warning: Could not find directory "
                    + aDir.getAbsolutePath()
                    + " to add to the repository.";
            if (!failonerror) {
                logWarning(message);
            } else {
                throw new SvnAntException(message);
            }
        }

    }

    /**
     * add the file (or directory) to the repository, including any necessary parent directories
     * @param svnClient
     * @param aFile
     * @param baseDir
     * @throws SvnAntException
     */
    private void svnAddFileWithDirs(File aFile, File baseDir)
        throws SvnAntException {

        Stack dirs = new Stack();
        File currentDir = aFile.getParentFile();
        
        try {
			// don't add the file if already added ...
			if (SVNStatusUtils.isManaged(svnClient.getSingleStatus(aFile)))
			    return;
			
			// determine directories to add to repository			
			while ((currentDir != null)
			    && (!SVNStatusUtils.isManaged(svnClient.getSingleStatus(currentDir)))
			    && (!currentDir.equals(baseDir))) {
			    dirs.push(currentDir);
			    currentDir = currentDir.getParentFile();
			}
		} catch (SVNClientException e) {
			throw new SvnAntException("Cannot get status of file or directory",e);
		}

        // add them to the repository
        while (dirs.size() > 0) {
            currentDir = (File) dirs.pop();
            try {
                svnClient.addFile(currentDir);
            } catch (Exception e) {
                throw new SvnAntException(
                    "Cannot add directory "
                        + currentDir.getAbsolutePath()
                        + " to repository",
                    e);
            }
        }

        // now add the file ...
        try {
            svnClient.addFile(aFile);
        } catch (Exception e) {
            throw new SvnAntException(
                "Can't add file " + aFile.getAbsolutePath() + " to repository",
                e);

        }
    }

    /**
     * add a fileset (both dirs and files) to the repository
     * @param svnClient
     * @param fs
     * @throws SvnAntException
     */
    private void svnAddFileSet(FileSet fs) throws SvnAntException {
        DirectoryScanner ds = fs.getDirectoryScanner(getProject());
        File baseDir = fs.getDir(getProject()); // base dir
        String[] files = ds.getIncludedFiles();
        String[] dirs = ds.getIncludedDirectories();

        // first : we add directories to the repository
        for (int i = 0; i < dirs.length; i++) {
            svnAddFileWithDirs(new File(baseDir, dirs[i]), baseDir);
        }

        // then we add files
        for (int i = 0; i < files.length; i++) {
            svnAddFileWithDirs(new File(baseDir, files[i]), baseDir);
        }
    }

	/**
	 * set file to add to repository
	 * @param file
	 */
    public void setFile(File file) {
        this.file = file;
    }

	/**
	 * set the directory to add to the repository
	 * @param dir
	 */
    public void setDir(File dir) {
        this.dir = dir;
    }

	/**
	 * if set, directory will be added recursively (see setDir)
	 * @param recurse
	 */
    public void setRecurse(boolean recurse) {
        this.recurse = recurse;
    }

	/**
	 * if set, directory will be checked for new content even if already managed by subversion (see setDir)
	 * @param force
	 */
    public void setForce(boolean force) {
        this.force = force;
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
