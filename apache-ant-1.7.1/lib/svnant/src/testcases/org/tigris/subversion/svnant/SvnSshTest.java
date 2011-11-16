/*
 * Created on 26 févr. 2004
 */
package org.tigris.subversion.svnant;

import org.apache.tools.ant.BuildFileTest;

/**
 * to run this test, you should use keychain (or ssh-agent) otherwise the password will be asked many times 
 * 
 * @author Cédric Chabanois 
 *         <a href="mailto:cchabanois@ifrance.com">cchabanois@ifrance.com</a>
 *
 */
public class SvnSshTest extends BuildFileTest {

	public SvnSshTest(String name) {
		super(name);
	}

	public void setUp() {
		configureProject("test/svnssh/build.xml");
	}

	public void tearDown()
	{
		System.out.print(getLog());
	}

	public void testSvnservePasswdSucceed() throws Exception {
		executeTarget("testPasswdSucceed");
	}

}
