/*
 * Created on 26 févr. 2004
 */
package org.tigris.subversion.svnant;

import org.apache.tools.ant.BuildFileTest;

/**
 * 
 * @author Cédric Chabanois 
 *         <a href="mailto:cchabanois@ifrance.com">cchabanois@ifrance.com</a>
 *
 */
public class SvnserveTest extends BuildFileTest {

	public SvnserveTest(String name) {
		super(name);
	}

	public void setUp() {
		configureProject("test/svnserve/build.xml");
	}

	public void tearDown()
	{
		System.out.print(getLog());
	}

	public void testSvnservePasswdSucceed() throws Exception {
		executeTarget("testSvnservePasswdSucceed");
	}

	public void testSvnservePasswdFail() throws Exception {
		try {
			executeTarget("testSvnservePasswdFail");
			fail(); // it should have failed as an incorrect password has been given
		} catch (Exception e) {
			
		}
	}


}
