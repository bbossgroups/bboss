//
//  BaseHttpUnitTestCase.java
//  taglibs
//
//  Created by tditchen on 3/13/05.
//  Copyright 2005 __MyCompanyName__. All rights reserved.
//

package org.ditchnet.httpunit;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.w3c.dom.*;
import org.ditchnet.xml.dom.DomUtils;
import org.ditchnet.test.DitchBaseTestCase;


public abstract class BaseHttpUnitTestCase extends DitchBaseTestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run( suite() );
	}
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Base class for HttpUnitTestCases");
		suite.addTestSuite( BaseHttpUnitTestCase.class );
		return suite;
	}
	
	DomUtils domUtils;
	
	public BaseHttpUnitTestCase(String name) {
		super(name);
	}
	
/*	protected Element getFirstAncestorByClassName(final Node target,
												  final String className) {
		return DomUtils.getFirstAncestorByClassName(target,className);
	}
	
	protected Element getFirstChildByClassName(final Node target,
											   final String className) {
		return DomUtils.getFirstChildByClassName(target,className);
	}
*/	
}
