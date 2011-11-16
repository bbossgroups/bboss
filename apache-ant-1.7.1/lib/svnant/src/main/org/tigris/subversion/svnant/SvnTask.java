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
package org.tigris.subversion.svnant;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Task;
import org.tigris.subversion.svnant.commands.Add;
import org.tigris.subversion.svnant.commands.Cat;
import org.tigris.subversion.svnant.commands.Checkout;
import org.tigris.subversion.svnant.commands.Cleanup;
import org.tigris.subversion.svnant.commands.Commit;
import org.tigris.subversion.svnant.commands.Copy;
import org.tigris.subversion.svnant.commands.CreateRepository;
import org.tigris.subversion.svnant.commands.Delete;
import org.tigris.subversion.svnant.commands.Diff;
import org.tigris.subversion.svnant.commands.Export;
import org.tigris.subversion.svnant.commands.Feedback;
import org.tigris.subversion.svnant.commands.Ignore;
import org.tigris.subversion.svnant.commands.Import;
import org.tigris.subversion.svnant.commands.Info;
import org.tigris.subversion.svnant.commands.Keywordsadd;
import org.tigris.subversion.svnant.commands.Keywordsremove;
import org.tigris.subversion.svnant.commands.Keywordsset;
import org.tigris.subversion.svnant.commands.Log;
import org.tigris.subversion.svnant.commands.Mkdir;
import org.tigris.subversion.svnant.commands.Move;
import org.tigris.subversion.svnant.commands.Propdel;
import org.tigris.subversion.svnant.commands.Propget;
import org.tigris.subversion.svnant.commands.Propset;
import org.tigris.subversion.svnant.commands.Revert;
import org.tigris.subversion.svnant.commands.Status;
import org.tigris.subversion.svnant.commands.SvnCommand;
import org.tigris.subversion.svnant.commands.Switch;
import org.tigris.subversion.svnant.commands.Update;
import org.tigris.subversion.svnant.commands.WcVersion;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNNotifyListener;
import org.tigris.subversion.svnclientadapter.SVNClientAdapterFactory;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.commandline.CmdLineClientAdapterFactory;
import org.tigris.subversion.svnclientadapter.javahl.JhlClientAdapterFactory;
import org.tigris.subversion.svnclientadapter.svnkit.SvnKitClientAdapterFactory;

/**
 * Svn Task
 * @author Cédric Chabanois 
 *         <a href="mailto:cchabanois@ifrance.com">cchabanois@ifrance.com</a>
 *
 */
public class SvnTask extends Task implements ISvnAntProjectComponent {

	private static boolean javahlAvailableInitialized = false;
    private static boolean javahlAvailable;
    private static boolean svnKitAvailableInitialized = false;
    private static boolean svnKitAvailable;
    private static boolean commandLineAvailableInitialized = false;
    private static boolean commandLineAvailable;
	
    private String username = null;
    private String password = null;    
    private boolean javahl = true;
    private boolean svnkit = true;
    private String dateFormatter = null;
    private TimeZone dateTimeZone = null;
	private boolean failonerror = true;

    private List commands = new ArrayList();
    private List notifyListeners = new ArrayList();
    
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
	 * @param username the username to set
	 */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
	 * @param password the password to set
	 */
    public void setPassword(String password) {
        this.password = "\"\"".equals(password) ? "" : password;
    }

	/**
     * set javahl to false to use command line interface
     * @param javahl
     */
    public void setJavahl(boolean javahl) {
        this.javahl = javahl;
    }

    /**
     * set svnkit to false to use command line interface
     * @param svnkit
     */
    public void setSvnkit(boolean svnkit) {
        this.svnkit = svnkit;
    }

    /**
     * @return dateFormatter used to parse/format revision dates
     */
    public String getDateFormatter()
    {
    	return dateFormatter != null ? dateFormatter : "MM/dd/yyyy hh:mm a";
    }
    
    /**
     * set dateFormatter used to parse/format revision dates
     * @param dateFormatter
     */
    public void setDateFormatter(String dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    /**
     * @return dateTimeZone used to parse/format revision dates
     */
    public TimeZone getDateTimeZone()
    {
    	return dateTimeZone;
    }
    
    /**
     * set dateTimezone used to parse/format revision dates
     * @param dateTimezone
     */
    public void setDateTimezone(String dateTimeZone) {
	    this.dateTimeZone = TimeZone.getTimeZone(dateTimeZone);
    }

    /**
	 * @return the failonerror
	 */
	public boolean isFailonerror() {
		return failonerror;
	}

	/**
	 * @param failonerror the failonerror to set
	 */
	public void setFailonerror(boolean failonerror) {
		this.failonerror = failonerror;
	}

	public void addCheckout(Checkout a) {
        addCommand(a);
    }

    public void addAdd(Add a) {
        addCommand(a);
    }

    public void addCleanup(Cleanup a) {
    	addCommand(a);
    }
    
    public void addCommit(Commit a) {
        addCommand(a);
    }

    public void addCopy(Copy a) {
        addCommand(a);
    }

    public void addDelete(Delete a) {
        addCommand(a);
    }

    public void addExport(Export a) {
        addCommand(a);
    }

    /**
     * Add the info command to the list of commands to execute.
     */
    public void addInfo(Info a) {
        addCommand(a);
    }

    public void addImport(Import a) {
        addCommand(a);
    }
    
    public void addLog(Log a) {
        addCommand(a);
    }

    public void addMkdir(Mkdir a) {
        addCommand(a);
    }

    public void addMove(Move a) {
        addCommand(a);
    }

    public void addUpdate(Update a) {
        addCommand(a);
    }
    
    public void addPropset(Propset a) {
        addCommand(a);
    }
    
    public void addDiff(Diff a) {
        addCommand(a);
    }

    public void addKeywordsSet(Keywordsset a) {
        addCommand(a);
    }
    
    public void addKeywordsAdd(Keywordsadd a) {
        addCommand(a);
    }
    
    public void addKeywordsRemove(Keywordsremove a) {
        addCommand(a);
    }    

    public void addRevert(Revert a) {
        addCommand(a);
    }

    public void addCat(Cat a) {
        addCommand(a);
    }

    public void addPropdel(Propdel a) {
        addCommand(a);
    }
    
    public void addIgnore(Ignore a) {
        addCommand(a);
    }
    
    public void addCreateRepository(CreateRepository a) {
        addCommand(a);
    }
    
    public void addWcVersion(WcVersion a) {
        addCommand(a);
    }

    public void addStatus(Status a) {
    	addCommand(a);
    }
    
    public void addSwitch(Switch a) {
    	addCommand(a);
    }
    
    public void addPropget(Propget a) {
    	addCommand(a);
    }
    

    private void addCommand(SvnCommand cmd)
    {
    	cmd.setTask(this);
    	commands.add(cmd);
    }
    
    public void addNotifyListener(ISVNNotifyListener notifyListener) {
        notifyListeners.add(notifyListener);
    }

    /**
     * check if javahl is available
     * @return true if javahl is available
     */
    static public boolean isJavahlAvailable() {
    	if (javahlAvailableInitialized == false) {
            // we don't initiliaze javahlAvailable in the static field because we
            // don't want the check to occur if javahl is set to false
            try {
                JhlClientAdapterFactory.setup();
            } catch (SVNClientException e) {
                // if an exception is thrown, javahl is not available or 
                // already registered ...
            }
            javahlAvailable = false;
            try {
            	javahlAvailable = SVNClientAdapterFactory.isSVNClientAvailable(JhlClientAdapterFactory.JAVAHL_CLIENT);
            } catch (Exception ex) {
            	//If anything goes wrong ... 
            }            

            javahlAvailableInitialized = true;
        }
        return javahlAvailable;
    }
    
    /**
     * check if SVNKit is available
     * @return true if SVNKit is available
     */
    static public boolean isSVNKitAvailable() {
        if (svnKitAvailableInitialized == false) {
            // we don't initiliaze svnKitAvailable in the static field because we
            // don't want the check to occur if svnkit is set to false
            try {
                SvnKitClientAdapterFactory.setup();
            } catch (SVNClientException e) {
                // if an exception is thrown, SVNKit is not available or 
                // already registered ...
            }
            svnKitAvailable = false;
            try {
            	svnKitAvailable = SVNClientAdapterFactory.isSVNClientAvailable(SvnKitClientAdapterFactory.SVNKIT_CLIENT);
            } catch (Exception ex) {
            	//If anything goes wrong ... 
            }            
            svnKitAvailableInitialized = true;
        }
        return svnKitAvailable;
    }
    
    /**
     * check if command line interface is available
     * @return true if command line interface is available
     */
    static public boolean isCommandLineAvailable() {
        if (commandLineAvailableInitialized == false) {
            try {
                CmdLineClientAdapterFactory.setup();
            } catch (SVNClientException e) {
                // if an exception is thrown, command line interface is not available or
                // already registered ...                
            }
            commandLineAvailable = 
                SVNClientAdapterFactory.isSVNClientAvailable(CmdLineClientAdapterFactory.COMMANDLINE_CLIENT);
            commandLineAvailableInitialized = true;
        }
        return commandLineAvailable;
    }
    
    public void maybeConfigure() throws BuildException
    {
    	super.maybeConfigure();
    }
    
    public void execute() throws BuildException {

        ISVNClientAdapter svnClient = getClientAdapter(this);        

        if (username != null)
            svnClient.setUsername(username);

        if (password != null)
            svnClient.setPassword(password);

        for (int i = 0; i < notifyListeners.size();i++) {
            svnClient.addNotifyListener((ISVNNotifyListener)notifyListeners.get(i));
        }

        for (int i = 0; i < commands.size(); i++) {
            SvnCommand command = (SvnCommand) commands.get(i);
            Feedback feedback = new Feedback(command);
			svnClient.addNotifyListener(feedback);
            command.executeCommand(svnClient);
            svnClient.removeNotifyListener(feedback);
        }
        
    }

	/**
	 * This method returns a SVN client adapter, based on the property set when the file selector
	 * was declared. More specifically, the 'javahl' and 'svnkit' flags are verified, as well as the
	 * availability of JAVAHL ad SVNKit adapters, to decide what flavour to use.
	 * @param component a ISVNAntProjectComponent for which the client adapter is created
	 * @return An instance of SVN client adapter that meets the specified constraints, if any.
	 * @throws BuildException Thrown in a situation where no adapter can fit the constraints.
	 */
	public static ISVNClientAdapter getClientAdapter(ISvnAntProjectComponent component) throws BuildException {
		ISVNClientAdapter svnClient;
        
        if ((component.getJavahl()) && (isJavahlAvailable())) {
            svnClient = SVNClientAdapterFactory.createSVNClient(JhlClientAdapterFactory.JAVAHL_CLIENT);
            component.getProjectComponent().log("Using javahl", Project.MSG_VERBOSE);
        }
        else
        if ((component.getSvnKit()) && isSVNKitAvailable()) {
            svnClient = SVNClientAdapterFactory.createSVNClient(SvnKitClientAdapterFactory.SVNKIT_CLIENT);
            component.getProjectComponent().log("Using svnkit", Project.MSG_VERBOSE);
        }
        else
        if (isCommandLineAvailable()) {
            svnClient = SVNClientAdapterFactory.createSVNClient(CmdLineClientAdapterFactory.COMMANDLINE_CLIENT);
            component.getProjectComponent().log("Using command line", Project.MSG_VERBOSE);
        } 
        else {
            throw new BuildException("Cannot find javahl, svnkit nor command line svn client");
        }
		return svnClient;
	}

}
