package org.tigris.subversion.svnant;

import org.tigris.subversion.svnclientadapter.SVNClientAdapterFactory;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.commandline.CmdLineClientAdapterFactory;

public class SvnCmdLineTest extends SvnTest {

    public SvnCmdLineTest(String name) {
        super(name);
    }
    
    static {
        try {
            CmdLineClientAdapterFactory.setup();
        } catch (SVNClientException e) {
            // if an exception is thrown, command line interface is not available or
            // already registered ...                
        	throw new RuntimeException("Cannot load commandline binding :", e);
        }
    }

	protected boolean isJavaHLTest()
	{
		return false;
	}
	protected boolean isSVNKitTest()
	{
		return false;
	}

    public void setUp() {
        super.setUp();

    	svnClient = SVNClientAdapterFactory.createSVNClient(CmdLineClientAdapterFactory.COMMANDLINE_CLIENT);
    }

    /* (non-Javadoc)
     * @see org.apache.tools.ant.BuildFileTest#executeTarget(java.lang.String)
     */
    protected void executeTarget(String targetName) {
    	project.setProperty("javahl", "false");
    	project.setProperty("svnkit", "false");
        assertPropertyEquals("javahl", "false");
        assertPropertyEquals("svnkit", "false");
    	super.executeTarget(targetName);
    }

}
