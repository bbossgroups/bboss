/*
 * The contents of this file are subject to the GNU Lesser General Public
 * License Version 2.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.gnu.org/copyleft/lesser.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Developer:
 * Todd Ditchendorf, todd@ditchnet.org
 *
 */

package org.ditchnet.xml.dom;

import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.ditchnet.test.DitchBaseTestCase;
import org.ditchnet.xml.Xhtml;


public class DomUtilsTest extends DitchBaseTestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run( suite() );
	}
	
	public static Test suite() {
		TestSuite suite = new TestSuite(
			"Tests for DOM utility algorithms contained in DomUtils class");
		suite.addTestSuite( DomUtilsTest.class );
		return suite;
	}
	
	final static String RES_FILE_SYSTEM_ID 
		= "testResources/domUtilsTestDoc.xml";

	final static String DOC_BODY_ID					= "doc-body";
	final static String MAIN_HEADER_ID				= "main-header";
	final static String HEADER_CLASS_NAME			= "header";
	final static String MAIN_LIST_ID				= "main-list";
	final static String FIRST_LIST_ITEM_ID			= "first-list-item";
	final static String FIRST_SUB_LIST_ID			= "first-sub-list";
	final static String CLASS_BREAKER_ID			= "class-breaker";
	final static String LIST_CLASS_NAME				= "list";
	final static String LIST_ITEM_CLASS_NAME		= "list-item";
	final static String ANOTHER_CLASS_NAME			= "another-class";
	final static String DEPTH_FIRST_ID				= "depth-first";
	final static String BREADTH_FIRST_ID			= "breadth-first";
	final static String DEPTH_BREADTH_CLASS_NAME	= "depth-breadth";
	
	Document doc;
	
	public DomUtilsTest(String name) {
		super(name);
	}
	
	public void setUp() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.parse(
			new InputSource( RES_FILE_SYSTEM_ID ));
	}
	
	public void testResDocumentExists() {
		Element docEl = doc.getDocumentElement();
		assertEquals(Xhtml.Tag.HTML.toString(),
					 docEl.getTagName());
	}
	
	public void testHasId() {
		Element mainHeader = doc.getElementById( MAIN_HEADER_ID );
		assertTrue(DomUtils.hasId(mainHeader,MAIN_HEADER_ID));
		assertFalse(DomUtils.hasId(mainHeader," " + MAIN_HEADER_ID));
	}
	
	public void testHasClassName() {
		Element firstSubList = doc.getElementById( FIRST_SUB_LIST_ID );
		assertEquals(Xhtml.Tag.UL.toString(),
					 firstSubList.getNodeName());
		assertTrue("The first sub list should have 'list' as a class name",
				   DomUtils.hasClassName(firstSubList,LIST_CLASS_NAME));
		assertTrue("The first sub list should have 'another-class as a class name",
				   DomUtils.hasClassName(firstSubList,ANOTHER_CLASS_NAME));
		assertTrue(DomUtils.hasClassName(firstSubList,ANOTHER_CLASS_NAME+" "));
		assertTrue(DomUtils.hasClassName(firstSubList," "+ANOTHER_CLASS_NAME+" "));
		assertTrue(DomUtils.hasClassName(firstSubList," "+ANOTHER_CLASS_NAME));
		
		Element li = doc.getElementById( MAIN_LIST_ID );
		assertTrue(DomUtils.hasClassName(li, LIST_CLASS_NAME ));
		assertTrue(DomUtils.hasClassName(li, ANOTHER_CLASS_NAME ));
		assertFalse(DomUtils.hasClassName(li,ANOTHER_CLASS_NAME+ANOTHER_CLASS_NAME));
		
		li = doc.getElementById(CLASS_BREAKER_ID);
		assertFalse(DomUtils.hasClassName(li,LIST_ITEM_CLASS_NAME));
	}
	
	public void testIsElementNode() {
		Element html = doc.getDocumentElement();
		assertTrue(DomUtils.isElementNode(html));
		assertFalse(DomUtils.isElementNode(html.getFirstChild()));
		Node title = doc.getElementsByTagName(Xhtml.Tag.TITLE.toString()).item(0);
		assertTrue(DomUtils.isElementNode(title));
		assertFalse(DomUtils.isElementNode(title.getFirstChild()));
	}
	
	public void testGetFirstAncestorByClassName() {
		Element firstListItem = doc.getElementById( FIRST_LIST_ITEM_ID );
		assertEquals(Xhtml.Tag.LI.toString(),
					 firstListItem.getTagName());
		Element mainList = DomUtils.getFirstAncestorByClassName(
														firstListItem,
														LIST_CLASS_NAME );
		assertEquals(Xhtml.Tag.UL.toString(),
					 mainList.getTagName());
		assertTrue(DomUtils.hasId( mainList, MAIN_LIST_ID ));
	}
	
	public void testGetFirstAncestorOrSelfByClassName() {
		Element ul = doc.getElementById( FIRST_SUB_LIST_ID );
		Element target = DomUtils.getFirstAncestorOrSelfByClassName(
													ul,
													LIST_CLASS_NAME);
		assertSame(ul,target);
		assertTrue(DomUtils.hasId(ul,FIRST_SUB_LIST_ID));
		
		Element li = doc.getElementById( FIRST_LIST_ITEM_ID );
		target = DomUtils.getFirstAncestorOrSelfByClassName(
													li,
													LIST_CLASS_NAME);
		assertNotSame(li,target);
		assertTrue(DomUtils.hasId(ul,FIRST_SUB_LIST_ID));
	}
	
	public void testGetFirstChildByClassName() {
		Element body = doc.getElementById( DOC_BODY_ID );
		Element h1 = DomUtils.getFirstChildByClassName(body,HEADER_CLASS_NAME);
		assertTrue(DomUtils.hasId(h1,MAIN_HEADER_ID));
		assertTrue(DomUtils.hasClassName(h1,HEADER_CLASS_NAME));
		Element mainList = doc.getElementById( MAIN_LIST_ID );
		Element li = 
			DomUtils.getFirstChildByClassName(mainList,LIST_ITEM_CLASS_NAME);
		assertTrue(DomUtils.hasId(li,FIRST_LIST_ITEM_ID));
	}
	
	public void testGetFirstDescendantByClassNameBreadthFirst() {
		Element html = doc.getDocumentElement();
		Element h1 = DomUtils.getFirstDescendantByClassNameBreadthFirst(
															html,
															HEADER_CLASS_NAME);
		assertTrue(DomUtils.hasId(h1,MAIN_HEADER_ID));
		assertTrue(DomUtils.hasClassName(h1,HEADER_CLASS_NAME));
		assertFalse(DomUtils.hasClassName(h1,"header-"));
		
		Element body = doc.getElementById( DOC_BODY_ID );
		h1 = DomUtils.getFirstDescendantByClassNameBreadthFirst(
															body,
															HEADER_CLASS_NAME);
		assertTrue(DomUtils.hasId(h1,MAIN_HEADER_ID));
		assertTrue(DomUtils.hasClassName(h1,HEADER_CLASS_NAME));
		assertFalse(DomUtils.hasClassName(h1,"-header"));
		assertFalse(DomUtils.hasClassName(h1,"headerheader"));
		
		Element li = DomUtils.getFirstDescendantByClassNameBreadthFirst(
													body,
													DEPTH_BREADTH_CLASS_NAME );
		assertTrue(DomUtils.hasId(li,BREADTH_FIRST_ID));
	}

	public void testGetFirstDescendantByClassNameDepthFirst() {
		Element html = doc.getDocumentElement();
		Element h1 = DomUtils.getFirstDescendantByClassNameDepthFirst(
													html,
													HEADER_CLASS_NAME);
		assertTrue(DomUtils.hasId(h1,MAIN_HEADER_ID));
		assertTrue(DomUtils.hasClassName(h1,HEADER_CLASS_NAME));
		assertFalse(DomUtils.hasClassName(h1,"header-"));
		
		Element body = doc.getElementById( DOC_BODY_ID );
		h1 = DomUtils.getFirstDescendantByClassNameDepthFirst(
													body,
													HEADER_CLASS_NAME);
		assertTrue(DomUtils.hasId(h1,MAIN_HEADER_ID));
		assertTrue(DomUtils.hasClassName(h1,HEADER_CLASS_NAME));
		assertFalse(DomUtils.hasClassName(h1,"-header"));
		assertFalse(DomUtils.hasClassName(h1,"headerheader"));
		
		Element li = DomUtils.getFirstDescendantByClassNameDepthFirst(
													body,
													DEPTH_BREADTH_CLASS_NAME );
		assertTrue(DomUtils.hasId(li,DEPTH_FIRST_ID));
	}
	
	public void testGetChildrenByClassName() {
		Element ul = doc.getElementById( MAIN_LIST_ID );
		List lis = DomUtils.getChildrenByClassName(ul,LIST_ITEM_CLASS_NAME);
		assertEquals(3,lis.size());
	}
	
	public void testGetDescendantsByClassName() {
		Element ul = doc.getElementById( FIRST_SUB_LIST_ID );
		List lis = DomUtils.getDescendantsByClassName(ul,LIST_ITEM_CLASS_NAME);
		assertEquals(1,lis.size());
		ul = doc.getElementById( MAIN_LIST_ID );
		lis = DomUtils.getDescendantsByClassName(ul,LIST_ITEM_CLASS_NAME);
		assertEquals(5,lis.size());
	}
}


/***************************

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>DomUtils Test Document</title>
</head>
<body id="doc-body" class="test-body another-class">
<h1 id="main-header" class="header">DomUtils Test Document</h1>
<ul id="main-list" class="list another-class">
<li id="first-list-item" class="list-item">List Item!
<ul id="first-sub-list" class="list another-class">
<li id="depth-first" class="list-item sub-list-item depth-breadth">Nested List Item!</li>
</ul>
</li>
<li id="breadth-first" class="list-item depth-breadth"></li>
<li id="class-breaker" class="no-list-item">
<li class="list-item"></li>
</li>
<li class="list-item"></li>
</ul>
</body>
</html>

****************************/
