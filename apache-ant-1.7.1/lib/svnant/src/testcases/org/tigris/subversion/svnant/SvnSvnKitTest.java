package org.tigris.subversion.svnant;

import org.tigris.subversion.svnclientadapter.SVNClientAdapterFactory;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.svnkit.SvnKitClientAdapterFactory;

public class SvnSvnKitTest extends SvnTest {

    public SvnSvnKitTest(String name) {
        super(name);
    }

    static {
        try {
        	SvnKitClientAdapterFactory.setup();
        } catch (SVNClientException e) {
            // if an exception is thrown, SVNKit is not available or 
            // already registered ...
        	throw new RuntimeException("Cannot load SVNKit binding :", e);
        }
    }

	protected boolean isJavaHLTest()
	{
		return false;
	}
	protected boolean isSVNKitTest()
	{
		return true;
	}

    public void setUp() {
    	super.setUp();

    	svnClient = SVNClientAdapterFactory.createSVNClient(SvnKitClientAdapterFactory.SVNKIT_CLIENT);
    }

    /* (non-Javadoc)
     * @see org.apache.tools.ant.BuildFileTest#executeTarget(java.lang.String)
     */
    protected void executeTarget(String targetName) {
    	project.setProperty("javahl", "false");
    	project.setProperty("svnkit", "true");
        assertPropertyEquals("javahl", "false");
        assertPropertyEquals("svnkit", "true");
    	super.executeTarget(targetName);
    }

}
