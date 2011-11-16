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
package org.ditchnet.jsp.util;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import org.ditchnet.test.DitchBaseTestCase;
import org.ditchnet.xml.Xhtml;


public class JspResponseWriterTest extends DitchBaseTestCase {

	JspResponseWriter writer;
	StringBuffer buff;

	public JspResponseWriterTest(String name) { 
		super(name);
	}
	
	public void setUp() {
		writer = new JspResponseWriter();
		buff = writer.getBuffer();
	}
	
	private void dump(String expected,StringBuffer buff) {
		super.dump("expected:"+expected,"\n\nbuff:"+buff.toString());
	}
	
	public void testStartElement() {
		String expected = "\n<div";
		writer.startElement(Xhtml.Tag.DIV);
		//dump(expected,buff);
		assertEquals(expected,buff.toString());
	}
	
	public void testPrettyPrinting() {
		String expected = "\n<html>\n\t<head>\n\t<title>\n\t</title>\n\t" +
							"</head>\n\t</html>";
		writer.startElement(Xhtml.Tag.HTML);
		writer.startElement(Xhtml.Tag.HEAD);
		writer.startElement(Xhtml.Tag.TITLE);
		writer.endElement(Xhtml.Tag.TITLE);
		writer.endElement(Xhtml.Tag.HEAD);
		writer.endElement(Xhtml.Tag.HTML);
	}
	
	public void testFalseArgConstructor() {
		String expected = "<div><img border=\"1\" /></div><!-- " +
							"xul is cool -->";
		JspResponseWriter writer = new JspResponseWriter(false);
		StringBuffer buff = writer.getBuffer();
		writer.startElement(Xhtml.Tag.DIV);
		writer.startElement(Xhtml.Tag.IMG);
		writer.attribute(Xhtml.Attr.BORDER,"1");
		writer.endElement(Xhtml.Tag.IMG);
		writer.endElement(Xhtml.Tag.DIV);
		writer.comment("xul is cool");
		assertEquals(expected,buff.toString());
	}
	
	public void testTrueArgConstructor() {
		String expected = "\n<div>\n\t<img border=\"1\" />\n</div><!-- " +
							"xul is cool -->";
		JspResponseWriter writer = new JspResponseWriter(true);
		StringBuffer buff = writer.getBuffer();
		writer.startElement(Xhtml.Tag.DIV);
		writer.startElement(Xhtml.Tag.IMG);
		writer.attribute(Xhtml.Attr.BORDER,"1");
		writer.endElement(Xhtml.Tag.IMG);
		writer.endElement(Xhtml.Tag.DIV);
		writer.comment("xul is cool");
		//dump(expected,buff);
		assertEquals(expected,buff.toString());
	}	
	
	public void testEndElement() {
		writer.endElement(Xhtml.Tag.SPAN);
		assertEquals(buff.toString(),"\n</span>");
	}
	
	public void testNSStartElement() {
		String expected = "\n<html:body";
		writer.startElement("html",Xhtml.Tag.BODY);
		assertEquals(expected,buff.toString());
	}
	
	public void testNSEndElement() {
		String expected = "\n</xhtml:script>";
		writer.endElement("xhtml",Xhtml.Tag.SCRIPT);
		assertEquals(expected,buff.toString());
	}
	
	public void testLineBreak() {
		String expected = "\n";
		writer.lineBreak();
		assertEquals(expected,buff.toString());
	}
	
	public void testTabStop() {
		String expected = "\t";
		writer.tabStop();
		assertEquals(expected,buff.toString());
	}
	
	public void testAttribute() {
		String expected = "\n<input type=\"button\"";
		writer.startElement(Xhtml.Tag.INPUT);
		writer.attribute(Xhtml.Attr.TYPE,"button");
		assertEquals(expected,buff.toString());
	}
	
	public void testAttributeNS() {
		String expected = "\n<div xml:lang=\"en-uk\">\n</div>";
		writer.startElement(Xhtml.Tag.DIV);
		writer.attribute("xml",Xhtml.Attr.LANG,"en-uk");
		writer.lineBreak();
		writer.endElement(Xhtml.Tag.DIV);
	}
	
	public void testIllegalStateAttribute() {
		try {
			writer.attribute(Xhtml.Attr.TYPE,"text");
			fail("Should have thrown IllegalStateException for illegal " +
					"attribute");
		} catch (IllegalStateException expected) {
			assertTrue(true);
		}
	}
	
	public void testNullAttributeName() {
		writer.startElement(Xhtml.Tag.INPUT);
		try {
			writer.attribute(null,"12");
			fail("Should have thrown NullPointerException for null attribute" +
					" name");
		} catch (NullPointerException expected) {
			assertTrue(true);
		}
	}
	
	public void testText() {
		String expected = "\n<html>hey dude";
		writer.startElement(Xhtml.Tag.HTML);
		writer.text("hey dude");
		assertEquals(expected,buff.toString());
	}
	
	public void testNullText() {
		try {
			writer.text(null);
			fail("Should have thrown NullPointerException for null text");
		} catch (NullPointerException expected) {
			assertTrue(true);
		}
	}

	public void testComment() {
		String expected = "\n<body><!-- cool man -->";
		writer.startElement(Xhtml.Tag.BODY);
		writer.comment("cool man");
		assertEquals(expected,buff.toString());
	}
	
	public void testNullComment() {
		try {
			writer.comment(null);
			fail("Should have thrown NullPointerException for null comment");
		} catch (NullPointerException expected) {
			assertTrue(true);
		}
	}
	
	public void testEmptyElement() {
		String expected = "\n<img alt=\"a picture\" />\n<span>\n</span>";
		writer.startElement(Xhtml.Tag.IMG);
		writer.attribute(Xhtml.Attr.ALT,"a picture");
		writer.endElement(Xhtml.Tag.IMG);
		writer.startElement(Xhtml.Tag.SPAN);
		writer.text("");
		writer.endElement(Xhtml.Tag.SPAN);
		//dump(expected,buff);
		assertEquals(expected,buff.toString());
	}
	
	public void testLineBreakCreatesNonEmptyElement() {
		String expected = "\n<span>\n\n</span>\n<div lang=\"us-en\">\n\n</div>";
		writer.startElement(Xhtml.Tag.SPAN);
		writer.lineBreak();
		writer.endElement(Xhtml.Tag.SPAN);
		writer.startElement(Xhtml.Tag.DIV);
		writer.attribute(Xhtml.Attr.LANG,"us-en");
		writer.lineBreak();
		writer.endElement(Xhtml.Tag.DIV);
		//dump(expected,buff);
		assertEquals(expected,buff.toString());
	}
	
	public void testTabStopCreatesNonEmptyElement() {
		String expected = "\n<span>\t\n</span>";
		writer.startElement(Xhtml.Tag.SPAN);
		writer.tabStop();
		writer.endElement(Xhtml.Tag.SPAN);
		//dump(expected,buff);
		assertEquals(expected,buff.toString());
	}
	
	public static Test suite() {
		return new TestSuite(JspResponseWriterTest.class);
	}

}