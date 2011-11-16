package org.tigris.subversion.svnant.commands;

import java.io.File;

import org.tigris.subversion.svnant.SvnAntException;
import org.tigris.subversion.svnant.SvnAntValidationException;
import org.tigris.subversion.svnclientadapter.SVNClientException;

public class Cleanup extends SvnCommand {

    /** directory to cleanup */
    private File path = null;
    
    private boolean failonerror = true;
    
	@Override
	public void execute() throws SvnAntException {
        if (path != null) {
        	if (path.exists()){
	        	try {
					svnClient.cleanup(path);
				} catch (SVNClientException e) {
					if (failonerror){
	                throw new SvnAntException(
	                        "Can't cleanup directory " + path.getAbsolutePath(),
	                        e);
					}
				}
        	}else{
        		if (failonerror){
        			throw new SvnAntException(
                        "Directory doesn't exist " + path.getAbsolutePath());
        		}
        	}
        }

	}

	/**
	 * Sets the destination directory; required 
	 * @param path destination directory for cleanup.
	 */
	public void setDir(File path) {
		this.path = path;
	}

	@Override
	protected void validateAttributes() throws SvnAntValidationException {
        if (path == null) 
            throw new SvnAntValidationException("dir must be set");
	}

	/**
	 * @param failonerror the failonerror to set
	 */
	public void setFailonerror(boolean failonerror) {
		this.failonerror = failonerror;
	}

}
