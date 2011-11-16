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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;

import org.tigris.subversion.svnant.SvnAntException;
import org.tigris.subversion.svnant.SvnAntValidationException;
import org.tigris.subversion.svnclientadapter.ISVNLogMessage;
import org.tigris.subversion.svnclientadapter.SVNRevision;
import org.tigris.subversion.svnclientadapter.SVNUrl;

/**
 * svn log. 
 * @author Martin Letenay
 */
public class Log extends SvnCommand {
	
	/** destination file. */ 
	private File destFile = null;

	/** url */
	private SVNUrl url = null;
	
    /** the path */
    private File path = null;
    
    /** the stop-on-copy flag */
    private boolean stopOnCopy = true;

    /** the --xml flag */
    private boolean asXml = true;
    
    /** the --limit */
    private long limit = 0;
    
	/** start revision */
	private SVNRevision startRevision = SVNRevision.HEAD;

	/** stop revision */
	private SVNRevision stopRevision = new SVNRevision.Number(1);

	public void execute() throws SvnAntException {

        ISVNLogMessage[] logMessages = null;
		try {
            if (path != null) {
            	logMessages = svnClient.getLogMessages(path, startRevision, stopRevision, stopOnCopy, false, limit);
            } else {
            	logMessages = svnClient.getLogMessages(url, startRevision, startRevision, stopRevision, stopOnCopy, false, limit);
            }
            writeLogMessages(logMessages);
		} catch (Exception e) {
			throw new SvnAntException("Can't get the log messages for the path or url", e);
		} 
	}

	private void writeLogMessages(ISVNLogMessage[] logMessages) throws SvnAntException
	{		
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile)));
            
            if (asXml) {
            	writeXmlHeader(writer);
            	for (int i = 0; i < logMessages.length; i++) {
					writeLogEntryAsXml(logMessages[i], writer);
				}
            	writeXmlFooter(writer);
            } else {
            	for (int i = 0; i < logMessages.length; i++) {
					writeLogEntryAsPlaintext(logMessages[i], writer);
				}
            }            
		} catch (Exception e) {
			throw new SvnAntException("Can't get the content of the specified file", e);
		} finally {
            if (writer != null) {
                try {
                	writer.close();
                } catch (IOException e) { 
                	//Just ignore, it's exception during stream closing }
                }
            }
		}
	}
	
	/**
	 * Ensure we have a consistent and legal set of attributes
	 */
	protected void validateAttributes() throws SvnAntValidationException {
        if ((url == null) && (path == null))
            throw new SvnAntValidationException("url or path attributes must be set");
		if (destFile == null)
			destFile = new File(getProject().getBaseDir(),
                                url.getLastPathSegment());
		if (startRevision == null)
			throw SvnAntValidationException.createInvalidRevisionException();
		if (stopRevision == null)
			throw SvnAntValidationException.createInvalidRevisionException();
	}

	/**
	 * Sets the URL; required.
	 * @param url The url to set
	 */
	public void setUrl(SVNUrl url) {
		this.url = url;
	}

	/**
	 * set the path of the new directory
	 * @param path
	 */
    public void setPath(File path) {
        this.path = path;
    }

	/**
	 * @param destFile the destFile to set
	 */
	public void setDestFile(File destFile) {
		this.destFile = destFile;
	}
	
	/**
	 * @param startRevision the startRevision to set
	 */
	public void setStartRevision(String startRevision) {
		this.startRevision = getRevisionFrom(startRevision);
	}

	/**
	 * @param stopRevision the stopRevision to set
	 */
	public void setStopRevision(String stopRevision) {
		this.stopRevision = getRevisionFrom(stopRevision);
	}

	/**
	 * @param stopOnCopy the stopOnCopy to set
	 */
	public void setStopOnCopy(boolean stopOnCopy) {
		this.stopOnCopy = stopOnCopy;
	}
	
	/**
	 * @param asXml the asXml to set
	 */
	public void setAsXml(boolean asXml) {
		this.asXml = asXml;
	}

	/**
	 * @param limit the limit to set
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	private void writeLogEntryAsPlaintext(ISVNLogMessage logMessage, BufferedWriter writer) throws IOException
	{
		//Non-verbose
//		------------------------------------------------------------------------
//		r2233 | markphip | 2006-05-22 22:16:34 +0200 (po, 22 V 2006) | 3 lines
//
//		Improve error reporting in Lock/Unlock messages.  It now outputs the erro
//		message that is created by the client adapter, which includes more info s
//		as the user that holds the lock on the file.
//		------------------------------------------------------------------------
//		r2221 | markphip | 2006-05-19 17:29:46 +0200 (pi, 19 V 2006) | 1 line
//
//		JavaSVN 1.0.5
//		------------------------------------------------------------------------
		
		//Verbose
//		------------------------------------------------------------------------
//		r2233 | markphip | 2006-05-22 22:16:34 +0200 (po, 22 V 2006) | 3 lines
//		Changed paths:
//		   M /trunk/subclipse/core/lib/svnClientAdapter.jar
//		   M /trunk/svnClientAdapter/src/main/org/tigris/subversion/svnclientadapter/jav
//		ahl/JhlNotificationHandler.java
//
//		Improve error reporting in Lock/Unlock messages.  It now outputs the error
//		message that is created by the client adapter, which includes more info such
//		as the user that holds the lock on the file.
//		------------------------------------------------------------------------
//		r2221 | markphip | 2006-05-19 17:29:46 +0200 (pi, 19 V 2006) | 1 line
//		Changed paths:
//		   M /branches/1.0.x/subclipse/core/lib/javasvn.jar
//		   M /branches/1.0.x/svnClientAdapter/lib/javasvn.jar
//		   M /trunk/subclipse/core/lib/javasvn.jar
//		   M /trunk/svnClientAdapter/lib/javasvn.jar
//		   M /trunk/www/subclipse/changes.html
//
//		JavaSVN 1.0.5
//		------------------------------------------------------------------------
		
		writer.write("------------------------------------------------------------------------");
		writer.newLine();
		writer.write('r');
		writer.write(logMessage.getRevision().toString());
		writer.write(" | ");
		writer.write(logMessage.getAuthor());
		writer.write(" | ");
		writer.write(logMessage.getDate().toString());
//		writer.write(" | ");
//		writer.write();
//		writer.write(" lines");
//		if (.length > 1) {
//			writer.write('s');
//		}
		writer.newLine();
		writer.newLine();
		writer.write(logMessage.getMessage());
		writer.newLine();
	}

	private void writeLogEntryAsXml(ISVNLogMessage logMessage, BufferedWriter writer) throws IOException
	{
		//Non-verbose
//		<?xml version="1.0" encoding="utf-8"?>
//		<log>
//		<logentry
//		   revision="2233">
//		<author>markphip</author>
//		<date>2006-05-22T20:16:34.198898Z</date>
//		<msg>Improve error reporting in Lock/Unlock messages.  It now outputs the error
//		message that is created by the client adapter, which includes more info such
//		as the user that holds the lock on the file.</msg>
//		</logentry>
//		<logentry
//		   revision="2221">
//		<author>markphip</author>
//		<date>2006-05-19T15:29:46.078330Z</date>
//		<msg>JavaSVN 1.0.5</msg>
//		</logentry>
//		</log>
				
		//Verbose
//		<?xml version="1.0" encoding="utf-8"?>
//		<log>
//		<logentry
//		   revision="2233">
//		<author>markphip</author>
//		<date>2006-05-22T20:16:34.198898Z</date>
//		<paths>
//		<path
//		   action="M">/trunk/svnClientAdapter/src/main/org/tigris/subversion/svnclientad
//		apter/javahl/JhlNotificationHandler.java</path>
//		<path
//		   action="M">/trunk/subclipse/core/lib/svnClientAdapter.jar</path>
//		</paths>
//		<msg>Improve error reporting in Lock/Unlock messages.  It now outputs the error
//		message that is created by the client adapter, which includes more info such
//		as the user that holds the lock on the file.</msg>
//		</logentry>
//		<logentry
//		   revision="2221">
//		<author>markphip</author>
//		<date>2006-05-19T15:29:46.078330Z</date>
//		<paths>
//		<path
//		   action="M">/trunk/svnClientAdapter/lib/javasvn.jar</path>
//		<path
//		   action="M">/branches/1.0.x/subclipse/core/lib/javasvn.jar</path>
//		<path
//		   action="M">/branches/1.0.x/svnClientAdapter/lib/javasvn.jar</path>
//		<path
//		   action="M">/trunk/www/subclipse/changes.html</path>
//		<path
//		   action="M">/trunk/subclipse/core/lib/javasvn.jar</path>
//		</paths>
//		<msg>JavaSVN 1.0.5</msg>
//		</logentry>
//		</log>

		writer.write("<logentry revision=\"");
		writer.write(logMessage.getRevision().toString());
		writer.write("\">");
		writer.newLine();
		writer.write("<author>");
		writer.write(logMessage.getAuthor());
		writer.write("</author>");
		writer.newLine();
		writer.write("<date>");
		writer.write(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(logMessage.getDate()));
		writer.write("</date>");
		writer.newLine();
		writer.write("<msg>");
		writer.write(logMessage.getMessage());
		writer.write("</msg>");
		writer.newLine();
		writer.write("</logentry>");
		writer.newLine();
	}

	private void writeXmlHeader(BufferedWriter writer) throws IOException
	{
		writer.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		writer.newLine();
		writer.write("<log>");
		writer.newLine();
	}

	private void writeXmlFooter(BufferedWriter writer) throws IOException
	{
		writer.write("</log>");		
	}

}
