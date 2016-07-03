// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/beans/StringBean.java,v $
// $Author: derrickoswald $
// $Date: 2005/05/15 11:49:03 $
// $Revision: 1.44 $
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
//

package org.htmlparser.beans;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.net.URLConnection;

import org.htmlparser.Parser;
import org.htmlparser.Text;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.Tag;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.EncodingChangeException;
import org.htmlparser.util.Translate;
import org.htmlparser.visitors.NodeVisitor;

/**
 * Extract strings from a URL.
 * <p>Text within &lt;SCRIPT&gt;&lt;/SCRIPT&gt; tags is removed.</p>
 * <p>The text within &lt;PRE&gt;&lt;/PRE&gt; tags is not altered.</p>
 * <p>The property <code>Strings</code>, which is the output property is null
 * until a URL is set. So a typical usage is:</p>
 * <pre>
 *     StringBean sb = new StringBean ();
 *     sb.setLinks (false);
 *     sb.setReplaceNonBreakingSpaces (true);
 *     sb.setCollapse (true);
 *     sb.setURL ("http://www.netbeans.org"); // the HTTP is performed here
 *     String s = sb.getStrings ();
 * </pre>
 * You can also use the StringBean as a NodeVisitor on your own parser,
 * in which case you have to refetch your page if you change one of the
 * properties because it resets the Strings property:</p>
 * <pre>
 *     StringBean sb = new StringBean ();
 *     Parser parser = new Parser ("http://cbc.ca");
 *     parser.visitAllNodesWith (sb);
 *     String s = sb.getStrings ();
 *     sb.setLinks (true);
 *     parser.reset ();
 *     parser.visitAllNodesWith (sb);
 *     String sl = sb.getStrings ();
 * </pre>
 * According to Nick Burch, who contributed the patch, this is handy if you
 * don't want StringBean to wander off and get the content itself, either
 * because you already have it, it's not on a website etc.
 */
public class StringBean extends NodeVisitor implements Serializable
{
    /**
     * Property name in event where the URL contents changes.
     */
    public static final String PROP_STRINGS_PROPERTY = "strings";

    /**
     * Property name in event where the 'embed links' state changes.
     */
    public static final String PROP_LINKS_PROPERTY = "links";

    /**
     * Property name in event where the URL changes.
     */
    public static final String PROP_URL_PROPERTY = "URL";

    /**
     * Property name in event where the 'replace non-breaking spaces'
     * state changes.
     */
    public static final String PROP_REPLACE_SPACE_PROPERTY =
        "replaceNonBreakingSpaces";

    /**
     * Property name in event where the 'collapse whitespace' state changes.
     */
    public static final String PROP_COLLAPSE_PROPERTY = "collapse";

    /**
     * Property name in event where the connection changes.
     */
    public static final String PROP_CONNECTION_PROPERTY = "connection";

    /**
     * A newline.
     */
    private static final String NEWLINE = System.getProperty ("line.separator");

    /**
     * The length of the NEWLINE.
     */
    private static final int NEWLINE_SIZE = NEWLINE.length ();

    /**
     * Bound property support.
     */
    protected PropertyChangeSupport mPropertySupport;

    /**
     * The parser used to extract strings.
     */
    protected Parser mParser;

    /**
     * The strings extracted from the URL.
     */
    protected String mStrings;

    /**
     * If <code>true</code> the link URLs are embedded in the text output.
     */
    protected boolean mLinks;

    /**
     * If <code>true</code> regular space characters are substituted for
     * non-breaking spaces in the text output.
     */
    protected boolean mReplaceSpace;

    /**
     * If <code>true</code> sequences of whitespace characters are replaced
     * with a single space character.
     */
    protected boolean mCollapse;

    /**
     * The buffer text is stored in while traversing the HTML.
     */
    protected StringBuffer mBuffer;

    /**
     * Set <code>true</code> when traversing a SCRIPT tag.
     */
    protected boolean mIsScript;

    /**
     * Set <code>true</code> when traversing a PRE tag.
     */
    protected boolean mIsPre;

    /**
     * Set <code>true</code> when traversing a STYLE tag.
     */
    protected boolean mIsStyle;

   /**
     * Create a StringBean object.
     * Default property values are set to 'do the right thing':
     * <p><code>Links</code> is set <code>false</code> so text appears like a
     * browser would display it, albeit without the colour or underline clues
     * normally associated with a link.</p>
     * <p><code>ReplaceNonBreakingSpaces</code> is set <code>true</code>, so
     * that printing the text works, but the extra information regarding these
     * formatting marks is available if you set it false.</p>
     * <p><code>Collapse</code> is set <code>true</code>, so text appears
     * compact like a browser would display it.</p>
     */
    public StringBean ()
    {
        super (true, true);
        mPropertySupport = new PropertyChangeSupport (this);
        mParser = new Parser ();
        mStrings = null;
        mLinks = false;
        mReplaceSpace = true;
        mCollapse = true;
        mBuffer = new StringBuffer (4096);
        mIsScript = false;
        mIsPre = false;
        mIsStyle = false;
    }

    //
    // internals
    //

    /**
     * Appends a newline to the buffer if there isn't one there already.
     * Except if the buffer is empty.
     */
    protected void carriageReturn ()
    {
        int length;

        length = mBuffer.length ();
        if ((0 != length) // don't append newlines to the beginning of a buffer
            && ((NEWLINE_SIZE <= length) // not enough chars to hold a NEWLINE
            && (!mBuffer.substring (
                length - NEWLINE_SIZE, length).equals (NEWLINE))))
            mBuffer.append (NEWLINE);
    }

    /**
     * Add the given text collapsing whitespace.
     * Use a little finite state machine:
     * <pre>
     * state 0: whitepace was last emitted character
     * state 1: in whitespace
     * state 2: in word
     * A whitespace character moves us to state 1 and any other character
     * moves us to state 2, except that state 0 stays in state 0 until
     * a non-whitespace and going from whitespace to word we emit a space
     * before the character:
     *    input:     whitespace   other-character
     * state\next
     *    0               0             2
     *    1               1        space then 2
     *    2               1             2
     * </pre>
     * @param buffer The buffer to append to.
     * @param string The string to append.
     */
    protected void collapse (StringBuffer buffer, String string)
    {
        int chars;
        int length;
        int state;
        char character;

        chars = string.length ();
        if (0 != chars)
        {
            length = buffer.length ();
            state = ((0 == length)
                || (buffer.charAt (length - 1) == ' ')
                || ((NEWLINE_SIZE <= length)
                    && buffer.substring (
                        length - NEWLINE_SIZE, length).equals (NEWLINE)))
                ? 0 : 1;
            for (int i = 0; i < chars; i++)
            {
                character = string.charAt (i);
                switch (character)
                {
                    // see HTML specification section 9.1 White space
                    // http://www.w3.org/TR/html4/struct/text.html#h-9.1
                    case '\u0020':
                    case '\u0009':
                    case '\u000C':
                    case '\u200B':
                    case '\r':
                    case '\n':
                        if (0 != state)
                            state = 1;
                        break;
                    default:
                        if (1 == state)
                            buffer.append (' ');
                        state = 2;
                        buffer.append (character);
                }
            }
        }
    }

    /**
     * Extract the text from a page.
     * @return The textual contents of the page.
     * @exception ParserException If a parse error occurs.
     */
    protected String extractStrings ()
        throws
            ParserException
    {
        String ret;

        mParser.visitAllNodesWith (this);
        ret = mBuffer.toString ();
        mBuffer = new StringBuffer(4096);

        return (ret);
    }

    /**
     * Assign the <code>Strings</code> property, firing the property change.
     * @param strings The new value of the <code>Strings</code> property.
     */
    protected void updateStrings (String strings)
    {
        String oldValue;

        if ((null == mStrings) || !mStrings.equals (strings))
        {
            oldValue = mStrings;
            mStrings = strings;
            mPropertySupport.firePropertyChange (
                PROP_STRINGS_PROPERTY, oldValue, strings);
        }
    }

    /**
     * Fetch the URL contents.
     * Only do work if there is a valid parser with it's URL set.
     */
    protected void setStrings ()
    {
        if (null != getURL ())
            try
            {
                try
                {
                    mParser.visitAllNodesWith (this);
                    updateStrings (mBuffer.toString ());
                }
                finally
                {
                    mBuffer = new StringBuffer (4096);
                }
            }
            catch (EncodingChangeException ece)
            {
                mIsPre = false;
                mIsScript = false;
                mIsStyle = false;
                try
                {   // try again with the encoding now in force
                    mParser.reset ();
                    mBuffer = new StringBuffer (4096);
                    mParser.visitAllNodesWith (this);
                    updateStrings (mBuffer.toString ());
                }
                catch (ParserException pe)
                {
                    updateStrings (pe.toString ());
                }
                finally
                {
                    mBuffer = new StringBuffer (4096);
                }
             }
            catch (ParserException pe)
            {
                updateStrings (pe.toString ());
            }
        else
        {
            // reset in case this StringBean is used as a visitor
            // on another parser, not it's own
            mStrings = null;
            mBuffer = new StringBuffer (4096);
        }
    }

    /**
     * Refetch the URL contents.
     * Only need to worry if there is already a valid parser and it's
     * been spent fetching the string contents.
     */
    private void resetStrings ()
    {
        if (null != mStrings)
            try
            {
                mParser.setURL (getURL ());
                setStrings ();
            }
            catch (ParserException pe)
            {
                updateStrings (pe.toString ());
            }
    }

    //
    // Property change support.
    //

    /**
     * Add a PropertyChangeListener to the listener list.
     * The listener is registered for all properties.
     * @param listener The PropertyChangeListener to be added.
     */
    public void addPropertyChangeListener (PropertyChangeListener listener)
    {
        mPropertySupport.addPropertyChangeListener (listener);
    }

    /**
     * Remove a PropertyChangeListener from the listener list.
     * This removes a registered PropertyChangeListener.
     * @param listener The PropertyChangeListener to be removed.
     */
    public void removePropertyChangeListener (PropertyChangeListener listener)
    {
        mPropertySupport.removePropertyChangeListener (listener);
    }

    //
    // Properties
    //

    /**
     * Return the textual contents of the URL.
     * This is the primary output of the bean.
     * @return The user visible (what would be seen in a browser) text.
     */
    public String getStrings ()
    {
        if (null == mStrings)
        if (0 == mBuffer.length ())
            setStrings ();
        else
            updateStrings (mBuffer.toString ());

        return (mStrings);
    }

    /**
     * Get the current 'include links' state.
     * @return <code>true</code> if link text is included in the text extracted
     * from the URL, <code>false</code> otherwise.
     */
    public boolean getLinks ()
    {
        return (mLinks);
    }

    /**
     * Set the 'include links' state.
     * If the setting is changed after the URL has been set, the text from the
     * URL will be reacquired, which is possibly expensive.
     * @param links Use <code>true</code> if link text is to be included in the
     * text extracted from the URL, <code>false</code> otherwise.
     */
    public void setLinks (boolean links)
    {
        boolean oldValue = mLinks;
        if (oldValue != links)
        {
            mLinks = links;
            mPropertySupport.firePropertyChange (
                PROP_LINKS_PROPERTY, oldValue, links);
            resetStrings ();
        }
    }

    /**
     * Get the current URL.
     * @return The URL from which text has been extracted, or <code>null</code>
     * if this property has not been set yet.
     */
    public String getURL ()
    {
         return ((null != mParser) ? mParser.getURL () : null);
    }

    /**
     * Set the URL to extract strings from.
     * The text from the URL will be fetched, which may be expensive, so this
     * property should be set last.
     * @param url The URL that text should be fetched from.
     */
    public void setURL (String url)
    {
        String old;
        URLConnection conn;

        old = getURL ();
        conn = getConnection ();
        if (((null == old) && (null != url)) || ((null != old)
            && !old.equals (url)))
        {
            try
            {
                if (null == mParser)
                    mParser = new Parser (url);
                else
                    mParser.setURL (url);
                mPropertySupport.firePropertyChange (
                    PROP_URL_PROPERTY, old, getURL ());
                mPropertySupport.firePropertyChange (
                    PROP_CONNECTION_PROPERTY, conn, mParser.getConnection ());
                setStrings ();
            }
            catch (ParserException pe)
            {
                updateStrings (pe.toString ());
            }
        }
    }

    /**
     * Get the current 'replace non breaking spaces' state.
     * @return <code>true</code> if non-breaking spaces (character '&#92;u00a0',
     * numeric character reference &amp;#160; or character entity
     * reference &amp;nbsp;) are to be replaced with normal
     * spaces (character '&#92;u0020').
     */
    public boolean getReplaceNonBreakingSpaces ()
    {
        return (mReplaceSpace);
    }

    /**
     * Set the 'replace non breaking spaces' state.
     * If the setting is changed after the URL has been set, the text from the
     * URL will be reacquired, which is possibly expensive.
     * @param replace <code>true</code> if non-breaking spaces
     * (character '&#92;u00a0', numeric character reference &amp;#160;
     * or character entity reference &amp;nbsp;) are to be replaced with normal
     * spaces (character '&#92;u0020').
     */
    public void setReplaceNonBreakingSpaces (boolean replace)
    {
        boolean oldValue = mReplaceSpace;
        if (oldValue != replace)
        {
            mReplaceSpace = replace;
            mPropertySupport.firePropertyChange (PROP_REPLACE_SPACE_PROPERTY,
                oldValue, replace);
            resetStrings ();
        }
    }

    /**
     * Get the current 'collapse whitespace' state.
     * If set to <code>true</code> this emulates the operation of browsers
     * in interpretting text where <quote>user agents should collapse input
     * white space sequences when producing output inter-word space</quote>.
     * See HTML specification section 9.1 White space
     * <a href="http://www.w3.org/TR/html4/struct/text.html#h-9.1">
     * http://www.w3.org/TR/html4/struct/text.html#h-9.1</a>.
     * @return <code>true</code> if sequences of whitespace (space '&#92;u0020',
     * tab '&#92;u0009', form feed '&#92;u000C', zero-width space '&#92;u200B',
     * carriage-return '\r' and NEWLINE '\n') are to be replaced with a single
     * space.
     */
    public boolean getCollapse ()
    {
        return (mCollapse);
    }

    /**
     * Set the current 'collapse whitespace' state.
     * If the setting is changed after the URL has been set, the text from the
     * URL will be reacquired, which is possibly expensive.
     * @param collapse If <code>true</code>, sequences of whitespace
     * will be reduced to a single space.
     */
    public void setCollapse (boolean collapse)
    {
        boolean oldValue = mCollapse;
        if (oldValue != collapse)
        {
            mCollapse = collapse;
            mPropertySupport.firePropertyChange (
                    PROP_COLLAPSE_PROPERTY, oldValue, collapse);
            resetStrings ();
        }
    }

    /**
     * Get the current connection.
     * @return The connection that the parser has or <code>null</code> if it
     * hasn't been set or the parser hasn't been constructed yet.
     */
    public URLConnection getConnection ()
    {
        return ((null != mParser) ? mParser.getConnection () : null);
    }

    /**
     * Set the parser's connection.
     * The text from the URL will be fetched, which may be expensive, so this
     * property should be set last.
     * @param connection New value of property Connection.
     */
    public void setConnection (URLConnection connection)
    {
        String url;
        URLConnection conn;

        url = getURL ();
        conn = getConnection ();
        if (((null == conn) && (null != connection))
            || ((null != conn) && !conn.equals (connection)))
        {
            try
            {
                if (null == mParser)
                    mParser = new Parser (connection);
                else
                    mParser.setConnection (connection);
                mPropertySupport.firePropertyChange (
                    PROP_URL_PROPERTY, url, getURL ());
                mPropertySupport.firePropertyChange (
                    PROP_CONNECTION_PROPERTY, conn, mParser.getConnection ());
                setStrings ();
            }
            catch (ParserException pe)
            {
                updateStrings (pe.toString ());
            }
        }
    }

    //
    // NodeVisitor overrides
    //

    /**
     * Appends the text to the output.
     * @param string The text node.
     */
    public void visitStringNode (Text string)
    {
        if (!mIsScript && !mIsStyle)
        {
            String text = string.getText ();
            if (!mIsPre)
            {
                text = Translate.decode (text);
                if (getReplaceNonBreakingSpaces ())
                    text = text.replace ('\u00a0', ' ');
                if (getCollapse ())
                    collapse (mBuffer, text);
                else
                    mBuffer.append (text);
            }
            else
                mBuffer.append (text);
        }
    }

    /**
     * Appends a NEWLINE to the output if the tag breaks flow, and
     * possibly sets the state of the PRE and SCRIPT flags.
     * @param tag The tag to examine.
     */
    public void visitTag (Tag tag)
    {
        String name;

        if (tag instanceof LinkTag)
            if (getLinks ())
            { // appends the link as text between angle brackets to the output.
                mBuffer.append ("<");
                mBuffer.append (((LinkTag)tag).getLink ());
                mBuffer.append (">");
            }
        name = tag.getTagName ();
        if (name.equalsIgnoreCase ("PRE"))
            mIsPre = true;
        else if (name.equalsIgnoreCase ("SCRIPT"))
            mIsScript = true;
        else if (name.equalsIgnoreCase ("STYLE"))
            mIsStyle = true;
        if (tag.breaksFlow ())
            carriageReturn ();
    }

    /**
     * Resets the state of the PRE and SCRIPT flags.
     * @param tag The end tag to process.
     */
    public void visitEndTag (Tag tag)
    {
        String name;

        name = tag.getTagName ();
        if (name.equalsIgnoreCase ("PRE"))
            mIsPre = false;
        else if (name.equalsIgnoreCase ("SCRIPT"))
            mIsScript = false;
        else if (name.equalsIgnoreCase ("STYLE"))
            mIsStyle = false;
    }

    /**
     * Unit test.
     * @param args Pass arg[0] as the URL to process.
     */
    public static void main (String[] args)
    {
        if (0 >= args.length)
            System.out.println ("Usage: java -classpath htmlparser.jar"
                + " org.htmlparser.beans.StringBean <http://whatever_url>");
        else
        {
            StringBean sb = new StringBean ();
            sb.setLinks (false);
            sb.setReplaceNonBreakingSpaces (true);
            sb.setCollapse (true);
            sb.setURL (args[0]);
            System.out.println (sb.getStrings ());
        }
    }
}
