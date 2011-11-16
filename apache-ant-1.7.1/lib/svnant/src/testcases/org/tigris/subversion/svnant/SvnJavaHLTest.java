package org.tigris.subversion.svnant;

import org.tigris.subversion.svnclientadapter.SVNClientAdapterFactory;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.javahl.JhlClientAdapterFactory;

public class SvnJavaHLTest extends SvnTest {

    public SvnJavaHLTest(String name) {
        super(name);
    }
    
    static {
        try {
            JhlClientAdapterFactory.setup();
        } catch (SVNClientException e) {
            // if an exception is thrown, javahl is not available or 
            // already registered ...
        	throw new RuntimeException("Cannot load JavaHL binding :", e);
        }
    }

	protected boolean isJavaHLTest()
	{
		return true;
	}
	protected boolean isSVNKitTest()
	{
		return false;
	}

    public void setUp() {
    	super.setUp();

    	svnClient = SVNClientAdapterFactory.createSVNClient(JhlClientAdapterFactory.JAVAHL_CLIENT);
    }

    /* (non-Javadoc)
     * @see org.apache.tools.ant.BuildFileTest#executeTarget(java.lang.String)
     */
    protected void executeTarget(String targetName) {
    	project.setProperty("javahl", "true");
    	project.setProperty("svnkit", "false");
        assertPropertyEquals("javahl", "true");
        assertPropertyEquals("svnkit", "false");
    	super.executeTarget(targetName);
    }

}
