package org.tigris.subversion.svnant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.Target;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.UnknownElement;
import org.tigris.subversion.svnclientadapter.ISVNAnnotations;
import org.tigris.subversion.svnclientadapter.ISVNClientAdapter;
import org.tigris.subversion.svnclientadapter.ISVNDirEntry;
import org.tigris.subversion.svnclientadapter.ISVNInfo;
import org.tigris.subversion.svnclientadapter.ISVNLogMessage;
import org.tigris.subversion.svnclientadapter.ISVNNotifyListener;
import org.tigris.subversion.svnclientadapter.ISVNProperty;
import org.tigris.subversion.svnclientadapter.ISVNStatus;
import org.tigris.subversion.svnclientadapter.SVNClientException;
import org.tigris.subversion.svnclientadapter.SVNNodeKind;
import org.tigris.subversion.svnclientadapter.SVNRevision;
import org.tigris.subversion.svnclientadapter.SVNScheduleKind;
import org.tigris.subversion.svnclientadapter.SVNStatusKind;
import org.tigris.subversion.svnclientadapter.SVNUrl;
import org.tigris.subversion.svnclientadapter.utils.SVNStatusUtils;

/**
 * Set the property <code>javahl</code> and/or <code>svnkit</code> to
 * <code>true</code> in any of the test/<i>[dir]</i>/build.properties
 * files to run the test cases using the JavaHL or SVNKit clients (as
 * opposed to only the command-line client).
 *
 * @author Cédric Chabanois 
 *         <a href="mailto:cchabanois@ifrance.com">cchabanois@ifrance.com</a>
 * @author Jeremy Whitlock
 * <a href="mailto:jwhitlock@collab.net">jwhitlock@collab.net</a>
 * @author Daniel Rall
 */
public abstract class SvnTest extends BuildFileTest {

	protected ISVNClientAdapter svnClient;
	protected static final String WORKINGCOPY_DIR = "test/svn/workingcopy";
	protected static final String WORKINGCOPY2_DIR = "test/svn/workingcopy2";
	protected static final String TEST_DIR = "test/svn/test";

    public SvnTest(String name) {
        super(name);
    }
    
    public void setUp() {
        configureProject("test/svn/build.xml");
    }

	public void tearDown()
	{
		System.out.print(getLog());
	}

	protected abstract boolean isJavaHLTest(); 
	protected abstract boolean isSVNKitTest(); 
	
    public void testCheckout() throws SVNClientException {
        executeTarget("testCheckout");

		assertEquals(1,svnClient.getSingleStatus(new File("test/svn/coHEAD/checkoutTest/readme.txt")).getLastChangedRevision().getNumber());
    }
    

	public void testList() throws SVNClientException,MalformedURLException {
       executeTarget("testList");
	   
       // first using a SVNUrl
       String urlRepos = getProject().getProperty("urlRepos");
	   ISVNDirEntry[] list = svnClient.getList(new SVNUrl(urlRepos+"/listTest"),SVNRevision.HEAD,false);
	   assertTrue(list.length > 0);
	   
	   // using a File
	   list = svnClient.getList(new File(WORKINGCOPY_DIR+"/listTest"),SVNRevision.BASE,false);
	   assertTrue(list.length > 0);
	   
	   // there is no BASE for listTest because it was added and committed but
	   // it needs to be updated before there is a BASE for it
	   // this is not what I expected ...
	   list = svnClient.getList(new File(WORKINGCOPY_DIR),SVNRevision.BASE,false);
	   assertTrue(list.length == 0);
	}


    public void testLog() throws SVNClientException {
        executeTarget("testLog");
//		String urlRepos = getProject().getProperty("urlRepos");
		ISVNLogMessage[] messages = svnClient.getLogMessages(new File(WORKINGCOPY_DIR+"/logTest/file1.txt"),new SVNRevision.Number(0),SVNRevision.HEAD);
        assertTrue(messages.length > 0);
		assertEquals("logTest directory added to repository",messages[0].getMessage());
        assertEquals(5,messages[0].getChangedPaths().length);
        assertEquals('A',messages[0].getChangedPaths()[0].getAction());
    }


    public void testAddCommit() throws SVNClientException {
       executeTarget("testAddCommit");
	   assertTrue(svnClient.getSingleStatus(new File(WORKINGCOPY_DIR+"/addCommitTest/file0.add")).getLastChangedRevision().getNumber() > 0);
    }
    
    public void testCleanup() throws SVNClientException {
       executeTarget("testCleanup");
       assertTrue(!new File(WORKINGCOPY_DIR+"/.svn/lock").exists());
	}
     

    public void testCopy() throws SVNClientException {
    	executeTarget("testCopy");
		assertTrue(svnClient.getSingleStatus(new File(WORKINGCOPY_DIR+"/copyTest/copy1")).getLastChangedRevision().getNumber() > 0);
    } 

	public void testDelete() {
		executeTarget("testDelete");
		assertFalse(new File(WORKINGCOPY_DIR+"/deleteTest/deleteFromWorkingCopy/file0.del").exists());
		assertTrue(new File(WORKINGCOPY_DIR+"/deleteTest/deleteFromWorkingCopy/donotdel.txt").exists());
	}

	public void testExport() {
		executeTarget("testExport");
	}
	
	public void testImport() {
		executeTarget("testImport");
	} 

	
	public void testMkdir() throws SVNClientException {
		executeTarget("testMkdir");
		assertTrue(svnClient.getSingleStatus(new File(WORKINGCOPY_DIR+"/mkdirTest/testMkdir2")).getLastChangedRevision().getNumber() > 0);
    } 
	
	public void testMove() throws SVNClientException {
		executeTarget("testMove");
		assertTrue(svnClient.getSingleStatus(new File(WORKINGCOPY_DIR+"/moveTest/dir1Renamed")).getLastChangedRevision().getNumber() > 0);
	}
   
    public void testProp() throws SVNClientException {
        executeTarget("testProp");
        File file = new File(WORKINGCOPY_DIR+"/propTest/file.png");
        ISVNProperty propData = svnClient.propertyGet(file,"svn:mime-type");
        assertTrue(propData != null);
        assertEquals("image/png",propData.getValue());
        assertEquals(file.getAbsoluteFile(),propData.getFile());
        
        propData = svnClient.propertyGet(file,"myPicture");
        assertTrue(propData != null);
        assertEquals(170,propData.getData().length);

        propData = svnClient.propertyGet(new File(WORKINGCOPY_DIR+"/propTest/file.png"),"myProperty");
        assertTrue(propData == null);

		ISVNProperty[] properties = svnClient.getProperties(file);
		assertTrue(properties.length == 2);
		
		properties = svnClient.getProperties(new File(WORKINGCOPY_DIR+"/propTest"));
		assertEquals(0,properties.length);
        
        assertEquals("image/png",getProject().getProperty("propTest.mimeType"));
        assertNull(getProject().getProperty("propTestUrlBeforeCommit.mimeType"));
        assertEquals("image/png",getProject().getProperty("propTestUrlAfterCommit.mimeType"));
		file = new File(WORKINGCOPY_DIR+"/propTest/icon2.gif");
        assertTrue(file.exists());
    }

    public void testPropgetInvalidProp() throws SVNClientException {
        executeTarget("testPropgetInvalidProp");
        
        String prop = project.getProperty("propgetInvalidProp.mime");
        assertNull(prop);
    }

    public void testDiff() {
        executeTarget("testDiff");
        File patchFile = new File(WORKINGCOPY_DIR+"/diffTest/patch.txt");
        assertTrue(patchFile.exists());
        assertTrue(patchFile.length() > 0);
    }
    
    public void testKeywords() throws FileNotFoundException, IOException {
        executeTarget("testKeywords");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(WORKINGCOPY_DIR+"/keywordsTest/file.txt"))); 
        assertEquals("$LastChangedRevision: 1 $",reader.readLine());
        assertEquals("$Author: cedric $",reader.readLine());
        assertEquals("$Id$",reader.readLine());
        reader.close();
    }

    public void testUpdate() {
        executeTarget("testUpdate");
    }

    public void testRevert() throws FileNotFoundException, IOException {
        executeTarget("testRevert");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(WORKINGCOPY_DIR+"/revertTest/file.txt"))); 
        assertEquals("first version",reader.readLine());
        reader.close();
    }

    public void testCat() throws FileNotFoundException, IOException {
        executeTarget("testCat");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(WORKINGCOPY_DIR+"/catTest/filecat.txt"))); 
        assertEquals("first line",reader.readLine());        
        assertEquals("second line",reader.readLine());
        reader.close();
    }

    public void testListener() throws Exception {
        final Set addSet = new HashSet();
        final Set commitSet = new HashSet();
        final Set[] currentSet = new Set[] { null };

        ISVNNotifyListener listener = new ISVNNotifyListener() {
            public void setCommand(int command) {
                if (command == ISVNNotifyListener.Command.ADD) {
                    currentSet[0] = addSet;
                } else {
                    currentSet[0] = commitSet;
                }
            }
            
            public void logCommandLine(String commandLine) {}
            public void logMessage(String message) {}
            public void logError(String message) {}
            public void logRevision(long revision, String path) {}
            public void logCompleted(String message) {}
  
            public void onNotify(File path, SVNNodeKind kind) {
                currentSet[0].add(path);
            }
        };
        
        Target target = (Target)project.getTargets().get("testListener");
        // first task is "copy", second one is "svn"
        Task task = target.getTasks()[1];
        SvnTask svnTask;
        if (task instanceof UnknownElement) {
            // not sure how ant works but it seems to work using that
            UnknownElement unknownElement = (UnknownElement)task;
            unknownElement.maybeConfigure();
            svnTask = (SvnTask)unknownElement.getTask(); 
        }else {
            svnTask = (SvnTask)task;
        }
        svnTask.setJavahl(isJavaHLTest());
        svnTask.setSvnkit(isSVNKitTest());
        
        svnTask.addNotifyListener(listener);
        executeTarget("testListener");
        
        assertEquals(6,addSet.size()); // 6 for add and 6 for commit        
        assertEquals(6,commitSet.size()); // 6 for add and 6 for commit
        
        assertTrue(addSet.contains(new File(WORKINGCOPY_DIR+"/listenerTest").getCanonicalFile()));
        assertTrue(addSet.contains(new File(WORKINGCOPY_DIR+"/listenerTest/dir1").getCanonicalFile()));
        assertTrue(addSet.contains(new File(WORKINGCOPY_DIR+"/listenerTest/dir1/file3.txt").getCanonicalFile()));        
        assertTrue(addSet.contains(new File(WORKINGCOPY_DIR+"/listenerTest/dir1/file4.txt").getCanonicalFile()));
        assertTrue(addSet.contains(new File(WORKINGCOPY_DIR+"/listenerTest/file1.txt").getCanonicalFile()));
        assertTrue(addSet.contains(new File(WORKINGCOPY_DIR+"/listenerTest/file2.txt").getCanonicalFile()));

        assertTrue(commitSet.contains(new File(WORKINGCOPY_DIR+"/listenerTest").getCanonicalFile()));
        assertTrue(commitSet.contains(new File(WORKINGCOPY_DIR+"/listenerTest/dir1").getCanonicalFile()));
        assertTrue(commitSet.contains(new File(WORKINGCOPY_DIR+"/listenerTest/dir1/file3.txt").getCanonicalFile()));        
        assertTrue(commitSet.contains(new File(WORKINGCOPY_DIR+"/listenerTest/dir1/file4.txt").getCanonicalFile()));
        assertTrue(commitSet.contains(new File(WORKINGCOPY_DIR+"/listenerTest/file1.txt").getCanonicalFile()));
        assertTrue(commitSet.contains(new File(WORKINGCOPY_DIR+"/listenerTest/file2.txt").getCanonicalFile()));
        
    }

    protected void assertTextStatus(ISVNStatus status, SVNStatusKind statusKind) {
        assertEquals(status.getTextStatus(), statusKind);
    }
    
    protected void assertNotTextStatus(ISVNStatus status, SVNStatusKind statusKind) {
        assertFalse(status.getTextStatus().equals(statusKind));
    }

    protected void assertManaged(ISVNStatus status) {
        assertTrue(SVNStatusUtils.isManaged(status));
    }
    
    protected void assertNotManaged(ISVNStatus status) {
        assertFalse(SVNStatusUtils.isManaged(status));
    }    
    
    protected void assertHasRemote(ISVNStatus status) {
        assertTrue(SVNStatusUtils.hasRemote(status));
    }
    
    public void testIgnore() throws Exception {
        executeTarget("testIgnore");
        assertTextStatus(svnClient.getSingleStatus(new File(WORKINGCOPY_DIR+"/ignoreTest/fileToIgnore.txt")),SVNStatusKind.IGNORED);
        assertTextStatus(svnClient.getSingleStatus(new File(WORKINGCOPY_DIR+"/ignoreTest/dir1/file1.ignore")),SVNStatusKind.IGNORED);
        assertNotTextStatus(svnClient.getSingleStatus(new File(WORKINGCOPY_DIR+"/ignoreTest/dir1/file2.donotignore")),SVNStatusKind.IGNORED);
        assertTextStatus(svnClient.getSingleStatus(new File(WORKINGCOPY_DIR+"/ignoreTest/dir1/dir2/file3.ignore")),SVNStatusKind.IGNORED);        
    }

    public void testSingleStatus() throws Exception {
        executeTarget("testStatus");
        assertTextStatus(svnClient.getSingleStatus(new File(WORKINGCOPY_DIR+"/statusTest/added.txt")), SVNStatusKind.ADDED);
        
        // a resource that does not exist is a non managed resource
        assertNotManaged(svnClient.getSingleStatus(new File(WORKINGCOPY_DIR+"/statusTest/fileThatDoesNotExist.txt")));
        
        // same test but in a directory that is not versioned
        assertNotManaged(svnClient.getSingleStatus(new File(WORKINGCOPY_DIR+"/statusTest/nonManaged.dir/fileThatDoesNotExist.txt")));
        
        assertTextStatus(svnClient.getSingleStatus(new File(WORKINGCOPY_DIR+"/statusTest/ignored.txt")), SVNStatusKind.IGNORED);    
        
        assertHasRemote(svnClient.getSingleStatus(new File(WORKINGCOPY_DIR+"/statusTest/committed.txt")));        
        
        assertTextStatus(svnClient.getSingleStatus(new File(WORKINGCOPY_DIR+"/statusTest/deleted.txt")), SVNStatusKind.DELETED);

        assertEquals("added",getProject().getProperty("a_testStatus.textStatus"));
        assertEquals("non-svn",getProject().getProperty("a_testStatus.propStatus"));
        SVNRevision.Number lastCommit = (SVNRevision.Number)SVNRevision.getRevision(getProject().getProperty("a_testStatus.lastCommitRevision"));
        assertEquals(null,lastCommit);
        
        SVNRevision.Number revision = (SVNRevision.Number)SVNRevision.getRevision(getProject().getProperty("a_testStatus.revision"));
        assertEquals(0,revision.getNumber());
        assertNotNull(getProject().getProperty("a_testStatus.lastCommitAuthor"));
        
    }
    
    public void testStatus() throws Exception {  
      
		executeTarget("testStatus");
		
        File file = new File(WORKINGCOPY_DIR+"/statusTest/added.txt");
		ISVNInfo info = svnClient.getInfo(file);
		assertEquals(SVNNodeKind.FILE,info.getNodeKind());
        assertNull(info.getLastChangedRevision());
        assertEquals(SVNScheduleKind.ADD,info.getSchedule());
        assertEquals(file.getCanonicalFile(),info.getFile().getCanonicalFile());
        
        // make sure that the top most directory is said to be versionned. It is in a directory where there is no
        // .svn directory but it is versionned however. 
        file = new File(WORKINGCOPY_DIR+"/statusTest/nonManaged.dir/statusTest");
        info = svnClient.getInfo(file);
        assertEquals(SVNNodeKind.DIR,info.getNodeKind());

        ISVNStatus[] statuses;  
        // getStatus(File, boolean) does not have the same result with command line interface
        // and svnjavahl for now. svnjavahl does not return ignored files for now 
        statuses = svnClient.getStatus(new File(WORKINGCOPY_DIR+"/statusTest"),false,true);
        // let's verify we don't forget some files (ignored ones for example)
//TODO some test are disabled ?       
//        assertEquals(8,statuses.length);
        
//        statuses = svnClient.getStatus(new File(WORKINGCOPY_DIR+"/statusTest"),true);
//        assertEquals(9,statuses.length);

//        statuses = svnClient.getStatus(new File(WORKINGCOPY_DIR+"/statusTest/nonManaged.dir").getCanonicalFile(),false);
//        assertEquals(1, statuses.length);

        
        statuses = svnClient.getStatus( new File[] {
            new File(WORKINGCOPY_DIR+"/statusTest/added.txt"),
            new File(WORKINGCOPY_DIR+"/statusTest/managedDir/added in managed dir.txt"),
            new File(WORKINGCOPY_DIR+"/statusTest/nonManaged.dir"),
            new File("nonExistingFile"),
            new File(WORKINGCOPY_DIR+"/statusTest/ignored.txt"),
            new File(WORKINGCOPY_DIR+"/statusTest/nonManaged.dir/statusTest")
            }
        );
        assertEquals(6,statuses.length);
        assertEquals(new File(WORKINGCOPY_DIR+"/statusTest/added.txt").getCanonicalFile(), statuses[0].getFile());
        assertTextStatus(statuses[0], SVNStatusKind.ADDED);
        
        assertEquals(new File(WORKINGCOPY_DIR+"/statusTest/managedDir/added in managed dir.txt").getAbsoluteFile(), statuses[1].getFile());        
        assertManaged(statuses[1]);
        assertEquals(SVNNodeKind.FILE,statuses[1].getNodeKind());
        
        assertNotManaged(statuses[2]);
        assertEquals(SVNNodeKind.UNKNOWN,statuses[2].getNodeKind());

        assertNotManaged(statuses[3]);
        assertEquals(SVNNodeKind.UNKNOWN,statuses[3].getNodeKind());
        
        assertTextStatus(statuses[4], SVNStatusKind.IGNORED);
        assertEquals(SVNNodeKind.UNKNOWN,statuses[4].getNodeKind()); // an ignored resource is a not versioned one, so its resource kind is UNKNOWN
        
        // make sure that the top most directory is said to be versionned. It is in a directory where there is no
        // .svn directory but it is versionned however. 
        assertManaged(statuses[5]);
        assertNotNull(statuses[5].getUrl());
        
  
        // this test does not pass with command line interface : there is a problem with long
        // usernames
//      TODO some test are disabled ?       
//        statuses = svnClient.getStatus(new File(WORKINGCOPY_DIR+"/statusTest/longUserName.dir"),true,true);
//        assertEquals(2, statuses.length);
//        assertEquals(new File(WORKINGCOPY_DIR+"/statusTest/longUserName.dir").getAbsoluteFile(), statuses[0].getFile());
        
        ISVNStatus status = svnClient.getSingleStatus(new File(WORKINGCOPY_DIR+"/statusTest/committed.txt"));

        assertEquals(status.getTextStatus().toString(), getProject().getProperty("testStatus.textStatus"));
        assertEquals(status.getPropStatus().toString(), getProject().getProperty("testStatus.propStatus"));
        assertEquals(status.getLastChangedRevision().toString(), getProject().getProperty("testStatus.lastCommitRevision"));
        assertEquals(status.getRevision().toString(), getProject().getProperty("testStatus.revision"));
        assertEquals(status.getLastCommitAuthor(), getProject().getProperty("testStatus.lastCommitAuthor"));
        assertEquals(new SimpleDateFormat("yyyy/MM/dd HH:mm").format(status.getLastChangedDate()), getProject().getProperty("testStatus.lastChangedDate"));
        assertEquals(status.getUrl().toString(), getProject().getProperty("testStatus.url"));
    }

    public void testWcVersion() throws Exception {  
        
		executeTarget("testStatus");

        ISVNStatus status = svnClient.getSingleStatus(new File(WORKINGCOPY_DIR+"/statusTest"));

        assertEquals(status.getUrl().toString(), getProject().getProperty("wc.repository.url"));
//        assertEquals("", getProject().getProperty("wc.repository.path"));
        assertEquals("4", getProject().getProperty("wc.revision.max"));
        assertEquals("4M", getProject().getProperty("wc.revision.max-with-flags"));
        assertEquals("4", getProject().getProperty("wc.committed.max"));
        assertEquals("4M", getProject().getProperty("wc.committed.max-with-flags"));
        assertEquals("true", getProject().getProperty("wc.modified"));
        assertNull(getProject().getProperty("wc.mixed"));
    }

    public void testStatusUnmanaged() throws Exception {  
		executeTarget("testStatusUnmanaged");
		
		assertEquals(project.getProperty("unmanaged1.textStatus"), "unversioned");
		assertEquals(project.getProperty("unmanaged1.propStatus"), "non-svn");
		assertEquals(project.getProperty("unmanaged1.lastCommitRevision"), "");
		assertEquals(project.getProperty("unmanaged1.revision"), "-1");
		assertEquals(project.getProperty("unmanaged1.lastCommitAuthor"), "");
		assertEquals(project.getProperty("unmanaged1.url"), "");

		assertEquals(project.getProperty("unmanagedDir.textStatus"), "unversioned");
		assertEquals(project.getProperty("unmanagedDir.propStatus"), "non-svn");
		assertEquals(project.getProperty("unmanagedDir.lastCommitRevision"), "");
		assertEquals(project.getProperty("unmanagedDir.revision"), "-1");
		assertEquals(project.getProperty("unmanagedDir.lastCommitAuthor"), "");
		assertEquals(project.getProperty("unmanagedDir.url"), "");

		assertEquals(project.getProperty("unmanaged2.textStatus"), "unversioned");
		assertEquals(project.getProperty("unmanaged2.propStatus"), "non-svn");
		assertEquals(project.getProperty("unmanaged2.lastCommitRevision"), "");
		assertEquals(project.getProperty("unmanaged2.revision"), "-1");
		assertEquals(project.getProperty("unmanaged2.lastCommitAuthor"), "");
		assertEquals(project.getProperty("unmanaged2.url"), "");
    }
    
    /**
     * Test the info command.
     */
    public void testInfo() throws Exception {
        expectBuildException("testInfoNoAttributes",
                             "Dir or file must be set.");
        expectBuildException("testInfoBadFile",
                             "fakefile.txt:  (Not a versioned resource)");
        
        executeTarget("testInfoDirectory");
        
        String[] propNames = new String[] {
            "svn.info.path",
            "svn.info.url",
            "svn.info.repouuid",
            "svn.info.rev",
            "svn.info.nodekind",
            "svn.info.schedule",
            "svn.info.author",
            "svn.info.lastRev",
            "svn.info.lastDate"
        };
        
        for (int i = 0; i < propNames.length; i++) {
            assertPropertySet(propNames[i], true);
        }
        
        propNames = new String[] {
            "svn.info.name",
            "svn.info.lastTextUpdate",
            "svn.info.lastPropUpdate",
            "svn.info.checksum"
        };
        
        // Property shouldn't be set for a directory.
        for (int i = 0; i < propNames.length; i++) {
            assertPropertyUnset(propNames[i]);
        }
			
        executeTarget("testInfoFile");
        
        propNames = new String[] {
            "svn.info.path",
            "svn.info.name",
            "svn.info.url",
            "svn.info.repouuid",
            "svn.info.rev",
            "svn.info.nodekind",
            "svn.info.schedule",
            "svn.info.author",
            "svn.info.lastRev",
            "svn.info.lastDate",
            "svn.info.lastTextUpdate",
            "svn.info.lastPropUpdate",
            "svn.info.checksum"
        };
        
        for (int i = 0; i < propNames.length; i++) {
            assertPropertySet(propNames[i], true);
        }
			
        executeTarget("testInfoCustomPrefix");
        assertPropertySet("wc.info.path", true);
        
        executeTarget("testInfoURL");

        propNames = new String[] {
        		"svn.info.url",
        		"svn.info.repouuid",
        		"svn.info.rev",
        		"svn.info.nodekind",
        		"svn.info.author",
        		"svn.info.lastRev",
        		"svn.info.lastDate"
        };

        for (int i = 0; i < propNames.length; i++) {
        	assertPropertySet(propNames[i], true);
        }
    }

	public void testEntry() throws Exception {
		executeTarget("testEntry");
		
		// first using a SVNUrl
		String urlRepos = getProject().getProperty("urlRepos");
		ISVNDirEntry dirEntry = svnClient.getDirEntry(new SVNUrl(urlRepos+"/entryTest/"),SVNRevision.HEAD);
		assertNotNull(dirEntry);
		assertEquals(SVNNodeKind.DIR,dirEntry.getNodeKind());
		assertEquals("entryTest",dirEntry.getPath());
		
		// using a File
		dirEntry = svnClient.getDirEntry(new File(WORKINGCOPY_DIR+"/entryTest/dir1"),SVNRevision.BASE);
		assertNotNull(dirEntry);
		assertEquals(SVNNodeKind.DIR,dirEntry.getNodeKind());
		
		// this does not work for now because working copy dir needs to be updated
		// before
//		TODO some test are disabled ?       
//		dirEntry = svnClient.getDirEntry(new File(WORKINGCOPY_DIR+"/entryTest/"),SVNRevision.BASE);
//		assertNotNull(dirEntry);
//		assertEquals(SVNNodeKind.DIR,dirEntry.getNodeKind());
//		assertEquals("entryTest",dirEntry.getPath());		
	}

	public void testResolve() throws Exception {
		executeTarget("testResolve");
		File file = new File(WORKINGCOPY_DIR+"/resolveTest/file.txt");
		ISVNStatus status = svnClient.getSingleStatus(file);
		assertTrue(status.getTextStatus() == SVNStatusKind.CONFLICTED);
		svnClient.resolved(file);
		status = svnClient.getSingleStatus(file);
		assertTrue(status.getTextStatus() == SVNStatusKind.MODIFIED);
	}

	public void testAnnotate() throws Exception {
		executeTarget("testAnnotate");
		File file = new File(WORKINGCOPY_DIR+"/annotateTest/file.txt");
		ISVNAnnotations annotations = svnClient.annotate(file,new SVNRevision.Number(2),new SVNRevision.Number(3));
		assertEquals(3,annotations.numberOfLines());
		assertEquals(0,annotations.getRevision(0));
		assertEquals("user1",annotations.getAuthor(1));
		assertEquals(2,annotations.getRevision(1));
		assertEquals("line 2",annotations.getLine(1));
		assertEquals("user2",annotations.getAuthor(2));
		assertEquals(3,annotations.getRevision(2));
		assertEquals("line 3",annotations.getLine(2));
		
		InputStream is = annotations.getInputStream();
		byte[] bytes = new byte[is.available()];
		is.read(bytes);
		assertEquals("line 1\nline 2\nline 3", new String(bytes));
		
	}
    
	
//    public void testRepositoryRoot() throws Exception {
//        executeTarget("testRepositoryRoot");
//        String urlRepos = getProject().getProperty("urlRepos");
//        SVNUrl url = svnClient.getRepositoryRoot(
//                new SVNUrl(urlRepos+"/entryTest/"));
//        assertEquals(new SVNUrl(urlRepos),url);
//    }

    
    public void testSwitch() throws Exception {
    	executeTarget("testSwitch");
    }

    public void testNormalSelector() throws Exception {
    	executeTarget("testNormalSelector");
    	
    	// Count number of files in test directory
		File dir2 = new File(TEST_DIR);
		assertTrue( dir2.exists() );
		assertTrue( dir2.isDirectory() );
		assertEquals(2, dir2.listFiles().length);
		
		// Verify that the expected files are present
		assertTrue( (new File(dir2, "normal1.txt")).exists() );
		assertTrue( (new File(dir2, "normal2.txt")).exists() );
    }

    public void testAddedSelector() throws Exception {
    	executeTarget("testAddedSelector");
    	
    	// Count number of files in test directory
		File dir2 = new File(TEST_DIR);
		assertTrue( dir2.exists() );
		assertTrue( dir2.isDirectory() );
		assertEquals(2, dir2.listFiles().length);
		
		// Verify that the expected files are present
		assertTrue( (new File(dir2, "added1.txt")).exists() );
		assertTrue( (new File(dir2, "added2.txt")).exists() );
    }

    public void testUnversionedSelector() throws Exception {
    	executeTarget("testUnversionedSelector");
    	
    	// Count number of files in test directory
		File dir2 = new File(TEST_DIR);
		assertTrue( dir2.exists() );
		assertTrue( dir2.isDirectory() );
		assertEquals(2, dir2.listFiles().length);
		
		// Verify that the expected files are present
		assertTrue( (new File(dir2, "unversioned1.txt")).exists() );
		assertTrue( (new File(dir2, "unversioned2.txt")).exists() );
    }

    public void testModifiedSelector() throws Exception {
    	executeTarget("testModifiedSelector");
    	
    	// Count number of files in test directory
		File dir2 = new File(TEST_DIR);
		assertTrue( dir2.exists() );
		assertTrue( dir2.isDirectory() );
		assertEquals(4, dir2.listFiles().length);
		
		// Verify that the expected files are present
		assertTrue( (new File(dir2, "modified1.txt")).exists() );
		assertTrue( (new File(dir2, "modified2.txt")).exists() );
		assertTrue( (new File(dir2, "conflicted1.txt")).exists() );
		assertTrue( (new File(dir2, "conflicted2.txt")).exists() );
    }

    public void testIgnoredSelector() throws Exception {
    	executeTarget("testIgnoredSelector");
    	
    	// Count number of files in test directory
		File dir2 = new File(TEST_DIR);
		assertTrue( dir2.exists() );
		assertTrue( dir2.isDirectory() );
		assertEquals(2, dir2.listFiles().length);
		
		// Verify that the expected files are present
		assertTrue( (new File(dir2, "ignored1.txt")).exists() );
		assertTrue( (new File(dir2, "ignored2.txt")).exists() );
    }

    public void testConflictedSelector() throws Exception {
    	executeTarget("testConflictedSelector");
    	
    	// Count number of files in test directory
		File dir2 = new File(TEST_DIR);
		assertTrue( dir2.exists() );
		assertTrue( dir2.isDirectory() );
		assertEquals(2, dir2.listFiles().length);
		
		// Verify that the expected files are present
		assertTrue( (new File(dir2, "conflicted1.txt")).exists() );
		assertTrue( (new File(dir2, "conflicted2.txt")).exists() );
    }

    public void testReplacedSelector() throws Exception {
    	executeTarget("testReplacedSelector");
    	
    	// Count number of files in test directory
		File dir2 = new File(TEST_DIR);
		assertTrue( dir2.exists() );
		assertTrue( dir2.isDirectory() );
		assertEquals(2, dir2.listFiles().length);
		
		// Verify that the expected files are present
		assertTrue( (new File(dir2, "replaced1.txt")).exists() );
		assertTrue( (new File(dir2, "replaced2.txt")).exists() );
    }

    public void testEmbeddedSelector() throws Exception {
    	executeTarget("testEmbeddedSelector");
    	
    	// Count number of files in test directory
		File dir2 = new File(TEST_DIR);
		assertTrue( dir2.exists() );
		assertTrue( dir2.isDirectory() );
		assertEquals(0, dir2.listFiles().length);
		
    }

    public void testAddSvnFileSet() throws Exception {
    	executeTarget("testAddSvnFileSet");
    	
    	// Count number of files in test directory
		File dir2 = new File(TEST_DIR);
		assertTrue( dir2.exists() );
		assertTrue( dir2.isDirectory() );
		assertEquals(2, dir2.listFiles().length);
		
		// Verify that the expected files are present
		assertTrue( (new File(dir2, "added1.txt")).exists() );
		assertTrue( (new File(dir2, "added2.txt")).exists() );
    }

    public void testCommitSvnFileSet() throws Exception {
    	executeTarget("testCommitSvnFileSet");
    	
    	// Count number of files in test directory
		File dir = new File(TEST_DIR);
		assertTrue( dir.exists() );
		assertTrue( dir.isDirectory() );
		assertEquals(2, dir.listFiles().length);
		
		// Verify that the expected files are present
		assertTrue( (new File(dir, "file2.txt")).exists() );
		assertTrue( (new File(dir, "dir1")).exists() );

		File dir2 = new File(TEST_DIR, "dir1");
		assertTrue( dir2.exists() );
		assertTrue( dir2.isDirectory() );
		assertEquals(1, dir2.listFiles().length);
		assertTrue( (new File(dir2, "file1.txt")).exists() );
    }

    public void testDeleteSvnFileSet() throws Exception {
    	executeTarget("testDeleteSvnFileSet");
    	
    	// Count number of files in test directory
		File dir = new File(WORKINGCOPY2_DIR);
		assertTrue( dir.exists() );
		assertTrue( dir.isDirectory() );
		File[] files = dir.listFiles();
		assertEquals(3, files.length);
		
		// Verify that the expected files are present
		assertTrue( (new File(dir, "normal1.txt")).exists() );
		assertTrue( (new File(dir, ".svn")).exists() );
		assertTrue( (new File(dir, "dir1")).exists() );

		File dir2 = new File(WORKINGCOPY2_DIR, "dir1");
		assertTrue( dir2.exists() );
		assertTrue( dir2.isDirectory() );
		assertEquals(2, dir2.listFiles().length);
		assertTrue( (new File(dir2, "normal2.txt")).exists() );
		assertTrue( (new File(dir2, ".svn")).exists() );
    }

    public void testKeywordsSvnFileSet() throws Exception {
    	executeTarget("testKeywordsSvnFileSet");
    	
    	// Test file1.txt
		File dir = new File(WORKINGCOPY_DIR);
		File file1 = new File(dir, "file1.txt");
		BufferedReader br1 = 
			new BufferedReader(
					new InputStreamReader(
							new FileInputStream(file1)
							)
					);
		String content1 = br1.readLine();
		br1.close();
		assertTrue( content1.matches(".*[0-9]+.*") );

    	// Test file2.txt
		File dir2 = new File(WORKINGCOPY_DIR, "dir1");
		File file2 = new File(dir2, "file2.txt");
		BufferedReader br2 = 
			new BufferedReader(
					new InputStreamReader(
							new FileInputStream(file2)
							)
					);
		String content2 = br2.readLine();
		br2.close();
		assertTrue( content2.matches(".*[0-9]+.*") );
    }

    public void testRevertSvnFileSet() throws Exception {
    	executeTarget("testRevertSvnFileSet");
    	
    	// Test deleted1.txt
		File dir = new File(WORKINGCOPY_DIR);
		File deleted1 = new File(dir, "deleted1.txt");
		assertTrue( deleted1.exists() );

    	// Test deleted2.txt
		File dir2 = new File(WORKINGCOPY_DIR, "dir1");
		File deleted2 = new File(dir2, "deleted2.txt");
		assertTrue( deleted2.exists() );
    }

    public void testUpdateSvnFileSet() throws Exception {
    	executeTarget("testUpdateSvnFileSet");
    	
    	// Test missing1.txt
		File dir = new File(WORKINGCOPY_DIR);
		File missing1 = new File(dir, "missing1.txt");
		assertTrue( missing1.exists() );

    	// Test missing2.txt
		File dir2 = new File(WORKINGCOPY_DIR, "dir1");
		File missing2 = new File(dir2, "missing2.txt");
		assertTrue( missing2.exists() );
    }

    public void testSvnFileSetAsRefId() throws Exception {
    	executeTarget("testSvnFileSetAsRefId");
    	
    	// Count number of files in test directory
		File dir = new File(TEST_DIR);
		assertTrue( dir.exists() );
		assertTrue( dir.isDirectory() );
		assertEquals(2, dir.listFiles().length);
		
		// Verify that the expected files are present
		assertTrue( (new File(dir, "added1.txt")).exists() );
		assertTrue( (new File(dir, "added2.txt")).exists() );
    }

    public void testSvnFileSetIncludes() throws Exception {
    	executeTarget("testSvnFileSetIncludes");
    	
    	// Count number of files in test directory
		File dir = new File(TEST_DIR);
		assertTrue( dir.exists() );
		assertTrue( dir.isDirectory() );
		assertEquals(2, dir.listFiles().length);
		
		// Verify that the expected files are present
		assertTrue( (new File(dir, "file11.txt")).exists() );
		assertTrue( (new File(dir, "dir")).exists() );
		
		dir = new File(dir, "dir");
		assertEquals(1, dir.listFiles().length);
		assertTrue( (new File(dir, "file21.txt")).exists() );
    }

    public void testSvnFileSetExcludes() throws Exception {
    	executeTarget("testSvnFileSetExcludes");
    	
    	// Count number of files in test directory
		File dir = new File(TEST_DIR);
		assertTrue( dir.exists() );
		assertTrue( dir.isDirectory() );
		assertEquals(2, dir.listFiles().length);
		
		// Verify that the expected files are present
		assertTrue( (new File(dir, "file12.txt")).exists() );
		assertTrue( (new File(dir, "dir")).exists() );
		
		dir = new File(dir, "dir");
		assertEquals(1, dir.listFiles().length);
		assertTrue( (new File(dir, "file22.txt")).exists() );
    }

    public void testSvnFileSetNestedInclude() throws Exception {
    	executeTarget("testSvnFileSetNestedInclude");
    	
    	// Count number of files in test directory
		File dir = new File(TEST_DIR);
		assertTrue( dir.exists() );
		assertTrue( dir.isDirectory() );
		assertEquals(2, dir.listFiles().length);
		
		// Verify that the expected files are present
		assertTrue( (new File(dir, "file11.txt")).exists() );
		assertTrue( (new File(dir, "dir")).exists() );
		
		dir = new File(dir, "dir");
		assertEquals(1, dir.listFiles().length);
		assertTrue( (new File(dir, "file21.txt")).exists() );
    }

    public void testSvnFileSetNestedExclude() throws Exception {
    	executeTarget("testSvnFileSetNestedExclude");
    	
    	// Count number of files in test directory
		File dir = new File(TEST_DIR);
		assertTrue( dir.exists() );
		assertTrue( dir.isDirectory() );
		assertEquals(2, dir.listFiles().length);
		
		// Verify that the expected files are present
		assertTrue( (new File(dir, "file12.txt")).exists() );
		assertTrue( (new File(dir, "dir")).exists() );
		
		dir = new File(dir, "dir");
		assertEquals(1, dir.listFiles().length);
		assertTrue( (new File(dir, "file22.txt")).exists() );
    }

    public void testSvnFileSetPatternSet() throws Exception {
    	executeTarget("testSvnFileSetPatternSet");
    	
    	// Count number of files in test directory
		File dir = new File(TEST_DIR);
		assertTrue( dir.exists() );
		assertTrue( dir.isDirectory() );
		assertEquals(2, dir.listFiles().length);
		
		// Verify that the expected files are present
		assertTrue( (new File(dir, "file1.xml")).exists() );
		assertTrue( (new File(dir, "dir1")).exists() );
		
		dir = new File(dir, "dir1");
		assertEquals(1, dir.listFiles().length);
		assertTrue( (new File(dir, "file3.xml")).exists() );
    }
    
    public void testSvnExists() throws Exception {
    	executeTarget("testSvnExists");
    	
    	// Test for expected results
		assertEquals("true", project.getProperty("svnExists.local.checkedin"));
		assertEquals("true", project.getProperty("svnExists.local.added"));
		assertEquals(null, project.getProperty("svnExists.local.private"));
		assertEquals(null, project.getProperty("svnExists.local.inexistant"));
		assertEquals("true", project.getProperty("svnExists.server.checkedin"));
		assertEquals(null, project.getProperty("svnExists.server.added"));
		assertEquals(null, project.getProperty("svnExists.server.private"));
    }
    
    /**
     * Asserts whether an Ant property is set.
     */
    private void assertPropertySet(String propName, boolean expectedSet)
    {
        if (expectedSet) {
            assertNotNull("Property '" + propName + "' should be set",
                          super.project.getProperty(propName));
        }
        else {
            assertNull("Property '" + propName + "' should be null",
                       super.project.getProperty(propName));
        }
    }

    /**
     * This is not actually a test case, but a hook to assure that
     * cleanup is handled after all test cases have run (rather than
     * after <i>each</i> test case, which would take longer).
     */
    public void testCleanupAfterTests() throws Exception {
    	executeTarget("clean");
    }

    public static void main(String[] args) {
        String[] testCaseName = { SvnTest.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
        //		junit.ui.TestRunner.main(testCaseName);
    }

}
