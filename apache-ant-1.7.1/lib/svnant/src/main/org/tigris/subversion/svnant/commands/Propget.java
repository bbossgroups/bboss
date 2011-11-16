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
import java.io.FileOutputStream;
import java.io.IOException;

import org.tigris.subversion.svnant.SvnAntException;
import org.tigris.subversion.svnant.SvnAntValidationException;
import org.tigris.subversion.svnclientadapter.ISVNProperty;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNUrl;

/**
 * svn propget. Get a property
 * @author Cédric Chabanois 
 *         <a href="mailto:cchabanois@ifrance.com">cchabanois@ifrance.com</a>
 *
 */
public class Propget extends SvnCommand {
    
    // path of the resource from which we want to get the property
    private File path;

    // url of the resource from which we want to get the property
    private SVNUrl url;

    // the name of the svn property we want to get
    private String name;
    
    // the ant property we want to set with the value of the svn property 
    private String property;
    
    // file to which we want to write the property to
    private File file;
    
	/* (non-Javadoc)
	 * @see org.tigris.subversion.svnant.SvnCommand#execute(org.tigris.subversion.svnclientadapter.ISVNClientAdapter)
	 */
	public void execute() throws SvnAntException {

        ISVNProperty svnProperty;
        try {
        	if (path != null) {
        		svnProperty = svnClient.propertyGet(path,name);
        	} else {
        		svnProperty = svnClient.propertyGet(url,name);
        	}
        } catch (SVNClientException e) {
            throw new SvnAntException("Can't get property "+name, e);
        }
        
        if (property != null && svnProperty != null) {
            getProject().setProperty(property, svnProperty.getValue());
        }
        
        if (file != null) {
            FileOutputStream os = null;
            try {
            	os = new FileOutputStream(file);
            	if (svnProperty != null) {
            		os.write(svnProperty.getData());
            	}            	
            } catch (IOException e) {
                throw new SvnAntException("Can't write property value to file "+file.toString(), e);
            } finally {
                if (os != null) {
                	try {
                		os.close();
                    } catch (IOException e) {
                    	//An exception during close. Just ignore.
                    }
                }
            }
        }   
	}

    /**
     * Ensure we have a consistent and legal set of attributes
     */
    protected void validateAttributes() throws SvnAntValidationException {
        if (((path == null) && (url == null))
                || ((path != null) && (url != null)))
            throw new SvnAntValidationException("path attribute or url attribute must be set");
        if (name == null)
            throw new SvnAntValidationException("svnPropertyName attribute must be set");
        if ((property == null) && (file == null))
            throw new SvnAntValidationException("property or file attribute must be set");
    }
    
	/**
	 * @param file The file to set.
	 */
	public void setFile(File file) {
		this.file = file;
	}
	/**
	 * @param path The path to set.
	 */
	public void setPath(File path) {
		this.path = path;
	}
	/**
	 * @param url The url to set.
	 */
	public void setUrl(SVNUrl url) {
		this.url = url;
	}
	/**
	 * @param property The property to set.
	 */
	public void setProperty(String property) {
		this.property = property;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
}
