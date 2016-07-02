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

/**
 *	@author Todd Ditchendorf
 *	@since 2005-03-11
 *
 */

package org.ditchnet.jsp.util;

import java.io.IOException;
import java.io.Writer;
import java.io.StringWriter;
import org.ditchnet.xml.Xml;

/**
 *	A JSP-specific XML writing utility class meant to be used as an output 
 *	buffer in a JSP tag handler subclass of 
 *	{@link javax.servlet.jsp.tagext.SimpleTag}. This class should not be viewed
 *	as a general purpose XML writing utility class as several of it's methods 
 *	make assumptions about the formatting of
 *	the output that make it's use in a JSP tag handler class more natural. 
 *	Note that this class is not thread safe.
 *
 *	@author Todd Ditchendorf
 *	@version 0.8
 */
public final class JspResponseWriter {

	private static final String LT 				= "<";
	private static final String GT 				= ">";
	private static final String COLON 			= ":";
	private static final String SPACE 			= " ";
	private static final String EQUALS 			= "=";
	private static final String TAB_STOP 		= "\t";
	private static final String LINE_BREAK 		= "\n";
	private static final String SINGLE_QUOTE 	= "'";
	private static final String DOUBLE_QUOTE 	= "\"";
	private static final String OPEN_COMMENT 	= "<!-- ";
	private static final String CLOSE_COMMENT 	= " -->";
	private static final String FORWARD_SLASH 	= "/";

	private StringWriter out = new StringWriter();

	private boolean prettyPrinting = true;

	private int tagDepth;	
	private volatile boolean elementTagIsOpen;
	private volatile boolean elementIsEmpty;

	/**
	 *	Defualt constructor -- Sets pretty printing to 'on'.
	 *
	 */
	public JspResponseWriter() {
		this(true);
	}
	
	/**
	 *	Allows instanciating this writer with pretty printing turned off.
	 */
	public JspResponseWriter(final boolean prettyPrinting) {
		setPrettyPrinting(prettyPrinting);
	}

	/**
	 *	
	 */
	public StringBuffer getBuffer() {
		return out.getBuffer();
	}
	
	public Writer getWriter() {
		return out;
	}
	
	/**
	 *	Set this writer to include line breaks and tabstops in the output
	 *	for easier human-readable markup.
	 */
	public void setPrettyPrinting(final boolean prettyPrinting) {
		this.prettyPrinting = prettyPrinting;
	}
	
	/**
	 *	Check for pretty printing setting.
	 */
	public boolean isPrettyPrinting() {
		return prettyPrinting;
	}
	
	private void prettyPrint() {
		if (!isPrettyPrinting()) {
			return;
		}
		lineBreak();
		for (int i = 0; i < tagDepth; i++) {
			tabStop();
		}
		tagDepth++;
	}
	
	public void tabStop() {
		closeOpenTagIfNecessary();
		out.write(TAB_STOP);
		elementIsEmpty = false;
	}
	
	/**
	 *	Start an XML element with a qualified name, up to and including the
     * element name.  Once this method has been called, clients can
     * call the <code>writeAttribute()</code> or
     * methods to add attributes and
     * corresponding values.  The starting element will be closed
     * (that is, the trailing '>' character added)
     * on any subsequent call to <code>startElement()</code>,
     * <code>comment()</code>, lineBreak(),
     * <code>text()</code>, <code>endElement()</code></p>
     */
	public void startElement(Xml.Tag qName) {
		_startElement(qName.toString());
	}
	
	/**
	 *	Start an XML element with a namespace prefix and a localName.
	 */
	public void startElement(String prefix, Xml.Tag localName) {
		_startElement(prefix + COLON + localName);
	}
	
	private void _startElement(final String name) {
		closeOpenTagIfNecessary();
		prettyPrint();
		out.write(LT);
		out.write(name);
		elementTagIsOpen = true;
		elementIsEmpty = true;
	}
	
	/**
	 *	End an XML element with a qualified name.
	 */	
	public void endElement(Xml.Tag qName) {
		_endElement(qName.toString());
	}
	
	/**
	 *	End an XML element with a namespace prefix and a localName.
	 */
	public void endElement(String prefix, Xml.Tag localName) {
		_endElement(prefix + COLON + localName);
	}
	
	private void _endElement(final String name) {
		if (elementIsEmpty) {
			out.write(SPACE);
			out.write(FORWARD_SLASH);
			out.write(GT);
			elementIsEmpty = false;
		} else {
			if (isPrettyPrinting()) { 
				lineBreak(); 
			}
			out.write(LT);
			out.write(FORWARD_SLASH);
			out.write(name);
			out.write(GT);
		}
		elementTagIsOpen = false;
		tagDepth--;
	}
	
	/**
	 *	This method exists for the convenience of the JSP author in manually
	 *	controlling the pretty printing of the buffered output.
	 */
	public void lineBreak() {
		closeOpenTagIfNecessary();
		out.write(LINE_BREAK);
		elementIsEmpty = false;
	}
	
	/**
	 *	Write an attribute with qualified name and corresponding value.
	 *	This method may only
	 *	be called after a call to <code>startElement()</code>, and before the
	 *	element has been closed.
	 *
	 *	@exception <code>NullPointerException</code>
	 *	@exception <code>IllegalStateException</code> if no element tag is
	 *			currently open
	 */
	public void attribute(Xml.Attr qName, String value) {
		_attribute(qName.toString(),value);
	}
	
	/**
	 *	Write an attribute with this namespace prefix, and local name 
	 *	corresponding values. This method may only
	 *	be called after a call to <code>startElement()</code>, and before the
	 *	element has been closed.
	 *
	 *	@exception <code>NullPointerException</code>
	 *	@exception <code>IllegalStateException</code> if no element tag is
	 *			currently open
	 */
	public void attribute(String prefix, Xml.Attr localName, String value) {
		_attribute(prefix + localName.toString(), value);
	}
	
	private void _attribute(String qName, String value) {
		if (!elementTagIsOpen) {
			throw new IllegalStateException("Attempting to write attribute " +
											" with no open element tag");
		}
		if (null == qName) {
			throw new NullPointerException("Attempting to write attribute " +
										   " with no name");
		}
		out.write(SPACE);
		out.write(qName);
		out.write(EQUALS);
		out.write(DOUBLE_QUOTE);
		out.write(value);
		out.write(DOUBLE_QUOTE);
	}
	
	/**
     * <p>Write an object, after converting it to a String (if necessary),
     * and after performing any escaping appropriate for the markup language
     * being rendered.  If there is an open element that has been created
     * by a call to <code>startElement()</code>, that element will be closed
     * first.</p>
     *
     * @param text Text to be written
     * 
     * @exception NullPointerException if <code>text</code>
     *  is <code>null</code>
	 */
	public void text(Object text) {
		if (null == text) {
			throw new NullPointerException("Attempting to write null text");
		}
		closeOpenTagIfNecessary();
		out.write(text.toString());
		elementIsEmpty = false;
	}
	
    /**
     * <p>Write a comment containing the specified text, after converting
     * that text to a String (if necessary), and after performing any escaping
     * appropriate for the markup language being rendered.  If there is
     * an open element that has been created by a call to
     * <code>startElement()</code>, that element will be closed first.</p>
     *
     * @param comment Text content of the comment
     *
     * @exception NullPointerException if <code>comment</code>
     *  is <code>null</code>
     */
	public void comment(Object comment) {
		if (null == comment) {
			throw new NullPointerException("Attempting to write null comment");
		}
		closeOpenTagIfNecessary();
		out.write(OPEN_COMMENT);
		out.write(comment.toString());
		out.write(CLOSE_COMMENT);
	}
	
	private void closeOpenTagIfNecessary() {
		if (elementTagIsOpen) {
			out.write(GT);
			elementTagIsOpen = false;
		}
	}
	
	public void close()
	{
		if(this.out != null)
		{
			try {
				out.close();
			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}
	}

}

