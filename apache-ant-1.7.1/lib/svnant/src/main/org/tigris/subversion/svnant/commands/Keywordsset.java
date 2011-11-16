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

import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.types.FileSet;
import org.tigris.subversion.svnant.SvnAntException;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNKeywords;

/**
 * set keywords substitution list
 * @author Cédric Chabanois 
 *         <a href="mailto:cchabanois@ifrance.com">cchabanois@ifrance.com</a>
 *
 */
public class Keywordsset extends Keywords {
   
	/* (non-Javadoc)
	 * @see org.tigris.subversion.svnant.SvnCommand#execute(org.tigris.subversion.svnclientadapter.ISVNClientAdapter)
	 */
	public void execute() throws SvnAntException {
        super.execute();        

        if (file != null) {
            try {            
                svnClient.setKeywords(file,keywords,false);
            } catch (SVNClientException e) {
                throw new SvnAntException("Can't set keywords on file "+file.toString(), e);
            }
        }
        else
        if (dir != null) {
            try {            
                svnClient.setKeywords(dir,keywords,recurse);
            } catch (SVNClientException e) {
                throw new SvnAntException("Can't set keywords on directory "+dir.toString(), e);
            }            
        }
        else
        // deal with filesets
        if (filesets.size() > 0) {
            for (int i = 0; i < filesets.size(); i++) {
                FileSet fs = (FileSet) filesets.elementAt(i);
                keywordsSet(fs,keywords);
            }
        }
	}

    /**
     * set keywords on a fileset (both dirs and files)
     * @param svnClient
     * @param fs
     * @throws SvnAntException
     */
    private void keywordsSet(FileSet fs, SVNKeywords theKeywords) throws SvnAntException {
        DirectoryScanner ds = fs.getDirectoryScanner(getProject());
        File baseDir = fs.getDir(getProject()); // base dir
        String[] files = ds.getIncludedFiles();
        
        // we don't need to set keywords on directories
        // we only set keywords on files

        for (int i = 0; i < files.length; i++) {
            File aFile = new File(baseDir, files[i]);
            try {
                svnClient.setKeywords(aFile,theKeywords,false);
            } catch (SVNClientException e) {
                throw new SvnAntException("Can't set keywords on file "+aFile.toString(), e);
            }
        }
    }


    

}
