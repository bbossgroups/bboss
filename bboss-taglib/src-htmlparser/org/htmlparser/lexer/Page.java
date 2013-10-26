// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/lexer/Page.java,v $
// $Author: derrickoswald $
// $Date: 2005/05/15 11:49:04 $
// $Revision: 1.50 $
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

package org.htmlparser.lexer;

import java.io.InputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

import org.htmlparser.http.ConnectionManager;
import org.htmlparser.util.ParserException;

/**
 * Represents the contents of an HTML page.
 * Contains the source of characters and an index of positions of line
 * separators (actually the first character position on the next line).
 */
public class Page
    implements
        Serializable
{
    /**
     * The default charset.
     * This should be <code>{@value}</code>,
     * see RFC 2616 (http://www.ietf.org/rfc/rfc2616.txt?number=2616)
     * section 3.7.1
     * <p>Another alias is "8859_1".
     */
    public static final String DEFAULT_CHARSET = "ISO-8859-1";

    /**
     * The default content type.
     * In the absence of alternate information, assume html content ({@value}).
     */
    public static final String DEFAULT_CONTENT_TYPE = "text/html";

    /**
     * Character value when the page is exhausted.
     * Has a value of {@value}.
     */
    public static final char EOF = (char)Source.EOF;

    /**
     * The URL this page is coming from.
     * Cached value of <code>getConnection().toExternalForm()</code> or
     * <code>setUrl()</code>.
     */
    protected String mUrl;

    /**
     * The base URL for this page.
     */
    protected String mBaseUrl;

    /**
     * The source of characters.
     */
    protected Source mSource;

    /**
     * Character positions of the first character in each line.
     */
    protected PageIndex mIndex;

    /**
     * The connection this page is coming from or <code>null</code>.
     */
    protected transient URLConnection mConnection;

    /**
     * Connection control (proxy, cookies, authorization).
     */
    protected static ConnectionManager mConnectionManager =
        new ConnectionManager ();

    /**
     * Construct an empty page.
     */
    public Page ()
    {
        this ("");
    }

    /**
     * Construct a page reading from a URL connection.
     * @param connection A fully conditioned connection. The connect()
     * method will be called so it need not be connected yet.
     * @exception ParserException An exception object wrapping a number of
     * possible error conditions, some of which are outlined below.
     * <li>IOException If an i/o exception occurs creating the
     * source.</li>
     * <li>UnsupportedEncodingException if the character set specified in the
     * HTTP header is not supported.</li>
     */
    public Page (URLConnection connection) throws ParserException
    {
        if (null == connection)
            throw new IllegalArgumentException ("connection cannot be null");
        setConnection (connection);
        mBaseUrl = null;
    }

    /**
     * Construct a page from a stream encoded with the given charset.
     * @param stream The source of bytes.
     * @param charset The encoding used.
     * If null, defaults to the <code>DEFAULT_CHARSET</code>.
     * @exception UnsupportedEncodingException If the given charset
     * is not supported.
     */
    public Page (InputStream stream, String charset)
        throws
            UnsupportedEncodingException
    {
        if (null == stream)
            throw new IllegalArgumentException ("stream cannot be null");
        if (null == charset)
            charset = DEFAULT_CHARSET;
        mSource = new InputStreamSource (stream, charset);
        mIndex = new PageIndex (this);
        mConnection = null;
        mUrl = null;
        mBaseUrl = null;
    }

    /**
     * Construct a page from the given string.
     * @param text The HTML text.
     * @param charset <em>Optional</em>. The character set encoding that will
     * be reported by {@link #getEncoding}. If charset is <code>null</code>
     * the default character set is used.
     */
    public Page (String text, String charset)
    {
        if (null == text)
            throw new IllegalArgumentException ("text cannot be null");
        if (null == charset)
            charset = DEFAULT_CHARSET;
        mSource = new StringSource (text, charset);
        mIndex = new PageIndex (this);
        mConnection = null;
        mUrl = null;
        mBaseUrl = null;
    }

    /**
     * Construct a page from the given string.
     * The page will report that it is using an encoding of
     * {@link #DEFAULT_CHARSET}.
     * @param text The HTML text.
     */
    public Page (String text)
    {
        this (text, null);
    }

    /**
     * Construct a page from a source.
     * @param source The source of characters.
     */
    public Page (Source source)
    {
        if (null == source)
            throw new IllegalArgumentException ("source cannot be null");
        mSource = source;
        mIndex = new PageIndex (this);
        mConnection = null;
        mUrl = null;
        mBaseUrl = null;
    }

    //
    // static methods
    //

    /**
     * Get the connection manager all Parsers use.
     * @return The connection manager.
     */
    public static ConnectionManager getConnectionManager ()
    {
        return (mConnectionManager);
    }

    /**
     * Set the connection manager to use.
     * @param manager The new connection manager.
     */
    public static void setConnectionManager (ConnectionManager manager)
    {
        mConnectionManager = manager;
    }

    /**
     * Get a CharacterSet name corresponding to a charset parameter.
     * @param content A text line of the form:
     * <pre>
     * text/html; charset=Shift_JIS
     * </pre>
     * which is applicable both to the HTTP header field Content-Type and
     * the meta tag http-equiv="Content-Type".
     * Note this method also handles non-compliant quoted charset directives
     * such as:
     * <pre>
     * text/html; charset="UTF-8"
     * </pre>
     * and
     * <pre>
     * text/html; charset='UTF-8'
     * </pre>
     * @return The character set name to use when reading the input stream.
     * For JDKs that have the Charset class this is qualified by passing
     * the name to findCharset() to render it into canonical form.
     * If the charset parameter is not found in the given string, the default
     * character set is returned.
     * @see #findCharset
     * @see #DEFAULT_CHARSET
     */
    public static String getCharset (String content)
    {
        final String CHARSET_STRING = "charset";
        int index;
        String ret;

        ret = DEFAULT_CHARSET;
        if (null != content)
        {
            index = content.indexOf (CHARSET_STRING);

            if (index != -1)
            {
                content = content.substring (index +
                    CHARSET_STRING.length ()).trim ();
                if (content.startsWith ("="))
                {
                    content = content.substring (1).trim ();
                    index = content.indexOf (";");
                    if (index != -1)
                        content = content.substring (0, index);

                    //remove any double quotes from around charset string
                    if (content.startsWith ("\"") && content.endsWith ("\"")
                        && (1 < content.length ()))
                        content = content.substring (1, content.length () - 1);

                    //remove any single quote from around charset string
                    if (content.startsWith ("'") && content.endsWith ("'")
                        && (1 < content.length ()))
                        content = content.substring (1, content.length () - 1);

                    ret = findCharset (content, ret);

                    // Charset names are not case-sensitive;
                    // that is, case is always ignored when comparing
                    // charset names.
//                    if (!ret.equalsIgnoreCase (content))
//                    {
//                        System.out.println (
//                            "detected charset \""
//                            + content
//                            + "\", using \""
//                            + ret
//                            + "\"");
//                    }
                }
            }
        }

        return (ret);
    }

    /**
     * Lookup a character set name.
     * <em>Vacuous for JVM's without <code>java.nio.charset</code>.</em>
     * This uses reflection so the code will still run under prior JDK's but
     * in that case the default is always returned.
     * @param name The name to look up. One of the aliases for a character set.
     * @param fallback The name to return if the lookup fails.
     * @return The character set name.
     */
    public static String findCharset (String name, String fallback)
    {
        String ret;

        try
        {
            Class cls;
            Method method;
            Object object;

            cls = Class.forName ("java.nio.charset.Charset");
            method = cls.getMethod ("forName", new Class[] { String.class });
            object = method.invoke (null, new Object[] { name });
            method = cls.getMethod ("name", new Class[] { });
            object = method.invoke (object, new Object[] { });
            ret = (String)object;
        }
        catch (ClassNotFoundException cnfe)
        {
            // for reflection exceptions, assume the name is correct
            ret = name;
        }
        catch (NoSuchMethodException nsme)
        {
            // for reflection exceptions, assume the name is correct
            ret = name;
        }
        catch (IllegalAccessException ia)
        {
            // for reflection exceptions, assume the name is correct
            ret = name;
        }
        catch (InvocationTargetException ita)
        {
            // java.nio.charset.IllegalCharsetNameException
            // and java.nio.charset.UnsupportedCharsetException
            // return the default
            ret = fallback;
            System.out.println (
                "unable to determine cannonical charset name for "
                + name
                + " - using "
                + fallback);
        }

        return (ret);
    }

    //
    // Serialization support
    //

    /**
     * Serialize the page.
     * There are two modes to serializing a page based on the connected state.
     * If connected, the URL and the current offset is saved, while if
     * disconnected, the underling source is saved.
     * @param out The object stream to store this object in.
     * @exception IOException If there is a serialization problem.
     */
    private void writeObject (ObjectOutputStream out)
        throws
            IOException
    {
        String href;
        Source source;
        PageIndex index;

        // two cases, reading from a URL and not
        if (null != getConnection ())
        {
            out.writeBoolean (true);
            out.writeInt (mSource.offset ()); // need to preread this much
            href = getUrl ();
            out.writeObject (href);
            setUrl (getConnection ().getURL ().toExternalForm ());
            source = getSource ();
            mSource = null; // don't serialize the source if we can avoid it
            index = mIndex;
            mIndex = null; // will get recreated; valid for the new page anyway?
            out.defaultWriteObject ();
            mSource = source;
            mIndex = index;
        }
        else
        {
            out.writeBoolean (false);
            href = getUrl ();
            out.writeObject (href);
            setUrl (null); // don't try and read a bogus URL
            out.defaultWriteObject ();
            setUrl (href);
        }
    }

    /**
     * Deserialize the page.
     * For details see <code>writeObject()</code>.
     * @param in The object stream to decode.
     * @exception IOException If there is a deserialization problem with
     * the stream.
     * @exception ClassNotFoundException If the deserialized class can't be
     * located with the current classpath and class loader.
     */
    private void readObject (ObjectInputStream in)
        throws
            IOException,
            ClassNotFoundException
    {
        boolean fromurl;
        int offset;
        String href;
        URL url;
        Cursor cursor;

        fromurl = in.readBoolean ();
        if (fromurl)
        {
            offset = in.readInt ();
            href = (String)in.readObject ();
            in.defaultReadObject ();
            // open the URL
            if (null != getUrl ())
            {
                url = new URL (getUrl ());
                try
                {
                    setConnection (url.openConnection ());
                }
                catch (ParserException pe)
                {
                    throw new IOException (pe.getMessage ());
                }
            }
            cursor = new Cursor (this, 0);
            for (int i = 0; i < offset; i++)
                try
                {
                    getCharacter (cursor);
                }
                catch (ParserException pe)
                {
                    throw new IOException (pe.getMessage ());
                }
            setUrl (href);
        }
        else
        {
            href = (String)in.readObject ();
            in.defaultReadObject ();
            setUrl (href);
        }
    }

    /**
     * Reset the page by resetting the source of characters.
     */
    public void reset ()
    {
        getSource ().reset ();
        mIndex = new PageIndex (this); // todo: is this really necessary?
    }

    /**
     * Close the page by destroying the source of characters.
     * @exception IOException If destroying the source encounters an error.
     */
    public void close () throws IOException
    {
        if (null != getSource ())
            getSource ().destroy ();
    }

    /**
     * Clean up this page, releasing resources.
     * Calls <code>close()</code>.
     * @exception Throwable if <code>close()</code> throws an
     * <code>IOException</code>.
     */
    protected void finalize ()
        throws
            Throwable
    {
        close ();
    }

    /**
     * Get the connection, if any.
     * @return The connection object for this page, or null if this page
     * is built from a stream or a string.
     */
    public URLConnection getConnection ()
    {
        return (mConnection);
    }

    /**
     * Set the URLConnection to be used by this page.
     * Starts reading from the given connection.
     * This also resets the current url.
     * @param connection The connection to use.
     * It will be connected by this method.
     * @exception ParserException If the <code>connect()</code> method fails,
     * or an I/O error occurs opening the input stream or the character set
     * designated in the HTTP header is unsupported.
     */
    public void setConnection (URLConnection connection)
        throws
            ParserException
    {
        Stream stream;
        String type;
        String charset;
        String contentEncoding;

        mConnection = connection;
        try
        {
            getConnection ().connect ();
        }
        catch (UnknownHostException uhe)
        {
            throw new ParserException ("Connect to "
                + mConnection.getURL ().toExternalForm () + " failed.", uhe);
        }
        catch (IOException ioe)
        {
            throw new ParserException ("Exception connecting to "
                + mConnection.getURL ().toExternalForm ()
                + " (" + ioe.getMessage () + ").", ioe);
        }
        type = getContentType ();
        charset = getCharset (type);
        try
        {
            contentEncoding = connection.getContentEncoding();
            if ((null != contentEncoding)
                && (-1 != contentEncoding.indexOf ("gzip")))
            {
                stream = new Stream (new GZIPInputStream (
                    getConnection ().getInputStream ()));
            }
            else if ((null != contentEncoding)
                && (-1 != contentEncoding.indexOf ("deflate")))
            {
                stream = new Stream (new InflaterInputStream (
                    getConnection ().getInputStream ()));
            }
            else
            {
                stream = new Stream (getConnection ().getInputStream ());
            }

            try
            {
                mSource = new InputStreamSource (stream, charset);
            }
            catch (UnsupportedEncodingException uee)
            {
//                StringBuffer msg;
//
//                msg = new StringBuffer (1024);
//                msg.append (getConnection ().getURL ().toExternalForm ());
//                msg.append (" has an encoding (");
//                msg.append (charset);
//                msg.append (") which is not supported, using ");
//                msg.append (DEFAULT_CHARSET);
//                System.out.println (msg.toString ());
                charset = DEFAULT_CHARSET;
                mSource = new InputStreamSource (stream, charset);
            }
        }
        catch (IOException ioe)
        {
            throw new ParserException ("Exception getting input stream from "
                + mConnection.getURL ().toExternalForm ()
                + " (" + ioe.getMessage () + ").", ioe);
        }
        mUrl = connection.getURL ().toExternalForm ();
        mIndex = new PageIndex (this);
    }

    /**
     * Get the URL for this page.
     * This is only available if the page has a connection
     * (<code>getConnection()</code> returns non-null), or the document base has
     * been set via a call to <code>setUrl()</code>.
     * @return The url for the connection, or <code>null</code> if there is
     * no conenction or the document base has not been set.
     */
    public String getUrl ()
    {
        return (mUrl);
    }

    /**
     * Set the URL for this page.
     * This doesn't affect the contents of the page, just the interpretation
     * of relative links from this point forward.
     * @param url The new URL.
     */
    public void setUrl (String url)
    {
        mUrl = url;
    }

    /**
     * Gets the baseUrl.
     * @return The base URL for this page, or <code>null</code> if not set.
     */
    public String getBaseUrl ()
    {
        return (mBaseUrl);
    }

    /**
     * Sets the baseUrl.
     * @param url The base url for this page.
     */
    public void setBaseUrl (String url)
    {
        mBaseUrl = url;
    }

    /**
     * Get the source this page is reading from.
     * @return The current source.
     */
    public Source getSource ()
    {
        return (mSource);
    }

    /**
     * Try and extract the content type from the HTTP header.
     * @return The content type.
     */
    public String getContentType ()
    {
        URLConnection connection;
        String content;
        String ret;

        ret = DEFAULT_CONTENT_TYPE;
        connection = getConnection ();
        if (null != connection)
        {
            content = connection.getContentType ();
            if (null != content)
                ret = content;
        }

        return (ret);
    }

    /**
     * Read the character at the given cursor position.
     * The cursor position can be only behind or equal to the
     * current source position.
     * Returns end of lines (EOL) as \n, by converting \r and \r\n to \n,
     * and updates the end-of-line index accordingly
     * Advances the cursor position by one (or two in the \r\n case).
     * @param cursor The position to read at.
     * @return The character at that position, and modifies the cursor to
     * prepare for the next read. If the source is exhausted a zero is returned.
     * @exception ParserException If an IOException on the underlying source
     * occurs, or an attemp is made to read characters in the future (the
     * cursor position is ahead of the underlying stream)
     */
    public char getCharacter (Cursor cursor)
        throws
            ParserException
    {
        int i;
        char ret;

        i = cursor.getPosition ();
        if (mSource.offset () < i)
            // hmmm, we could skip ahead, but then what about the EOL index
            throw new ParserException (
                "attempt to read future characters from source "
                + i + " > " + mSource.offset ());
        else if (mSource.offset () == i)
            try
            {
                i = mSource.read ();
                if (Source.EOF == i)
                    ret = EOF;
                else
                {
                    ret = (char)i;
                    cursor.advance ();
                }
            }
            catch (IOException ioe)
            {
                throw new ParserException (
                    "problem reading a character at position "
                    + cursor.getPosition (), ioe);
            }
        else
        {
            // historic read
            try
            {
                ret = mSource.getCharacter (i);
            }
            catch (IOException ioe)
            {
                throw new ParserException (
                    "can't read a character at position "
                    + i, ioe);
            }
            cursor.advance ();
        }

        // handle \r
        if ('\r' == ret)
        {   // switch to single character EOL
            ret = '\n';

            // check for a \n in the next position
            if (mSource.offset () == cursor.getPosition ())
                try
                {
                    i = mSource.read ();
                    if (Source.EOF == i)
                    {
                        // do nothing
                    }
                    else if ('\n' == (char)i)
                        cursor.advance ();
                    else
                        try
                        {
                            mSource.unread ();
                        }
                        catch (IOException ioe)
                        {
                            throw new ParserException (
                                "can't unread a character at position "
                                + cursor.getPosition (), ioe);
                        }
                }
                catch (IOException ioe)
                {
                    throw new ParserException (
                        "problem reading a character at position "
                        + cursor.getPosition (), ioe);
                }
            else
                try
                {
                    if ('\n' == mSource.getCharacter (cursor.getPosition ()))
                        cursor.advance ();
                }
                catch (IOException ioe)
                {
                    throw new ParserException (
                        "can't read a character at position "
                        + cursor.getPosition (), ioe);
                }
        }
        if ('\n' == ret)
            // update the EOL index in any case
            mIndex.add (cursor);

        return (ret);
    }

    /**
     * Get the current encoding being used.
     * @return The encoding used to convert characters.
     */
    public String getEncoding ()
    {
        return (getSource ().getEncoding ());
    }

    /**
     * Begins reading from the source with the given character set.
     * If the current encoding is the same as the requested encoding,
     * this method is a no-op. Otherwise any subsequent characters read from
     * this page will have been decoded using the given character set.<p>
     * Some magic happens here to obtain this result if characters have already
     * been consumed from this page.
     * Since a Reader cannot be dynamically altered to use a different character
     * set, the underlying stream is reset, a new Source is constructed
     * and a comparison made of the characters read so far with the newly
     * read characters up to the current position.
     * If a difference is encountered, or some other problem occurs,
     * an exception is thrown.
     * @param character_set The character set to use to convert bytes into
     * characters.
     * @exception ParserException If a character mismatch occurs between
     * characters already provided and those that would have been returned
     * had the new character set been in effect from the beginning. An
     * exception is also thrown if the underlying stream won't put up with
     * these shenanigans.
     */
    public void setEncoding (String character_set)
        throws
            ParserException
    {
        getSource ().setEncoding (character_set);
    }

    /**
     * Build a URL from the link and base provided.
     * @return An absolute URL.
     * @param link The (relative) URI.
     * @param base The base URL of the page, either from the &lt;BASE&gt; tag
     * or, if none, the URL the page is being fetched from.
     * @exception MalformedURLException If creating the URL fails.
     */
    public URL constructUrl (String link, String base)
        throws MalformedURLException
    {
        String path;
        boolean modified;
        boolean absolute;
        int index;
        URL url; // constructed URL combining relative link and base

        url = new URL (new URL (base), link);
        path = url.getFile ();
        modified = false;
        absolute = link.startsWith ("/");
        if (!absolute)
        {   // we prefer to fix incorrect relative links
            // this doesn't fix them all, just the ones at the start
            while (path.startsWith ("/."))
            {
                if (path.startsWith ("/../"))
                {
                    path = path.substring (3);
                    modified = true;
                }
                else if (path.startsWith ("/./") || path.startsWith("/."))
                {
                    path = path.substring (2);
                    modified = true;
                }
                else
                    break;
            }
        }
        // fix backslashes
        while (-1 != (index = path.indexOf ("/\\")))
        {
            path = path.substring (0, index + 1) + path.substring (index + 2);
            modified = true;
        }
        if (modified)
            url = new URL (url, path);

        return (url);
    }

    /**
     * Create an absolute URL from a relative link.
     * @param link The reslative portion of a URL.
     * @return The fully qualified URL or the original link if it was absolute
     * already or a failure occured.
     */
    public String getAbsoluteURL (String link)
    {
        String base;
        URL url;
        String ret;

        if ((null == link) || ("".equals (link)))
            ret = "";
        else
            try
            {
                base =  getBaseUrl ();
                if (null == base)
                    base = getUrl ();
                if (null == base)
                    ret = link;
                else
                {
                    url = constructUrl (link, base);
                    ret = url.toExternalForm ();
                }
            }
            catch (MalformedURLException murle)
            {
                ret = link;
            }

        return (ret);
    }

    /**
     * Get the line number for a cursor.
     * @param cursor The character offset into the page.
     * @return The line number the character is in.
     */
    public int row (Cursor cursor)
    {
        return (mIndex.row (cursor));
    }

    /**
     * Get the line number for a cursor.
     * @param position The character offset into the page.
     * @return The line number the character is in.
     */
    public int row (int position)
    {
        return (mIndex.row (position));
    }

    /**
     * Get the column number for a cursor.
     * @param cursor The character offset into the page.
     * @return The character offset into the line this cursor is on.
     */
    public int column (Cursor cursor)
    {
        return (mIndex.column (cursor));
    }

    /**
     * Get the column number for a cursor.
     * @param position The character offset into the page.
     * @return The character offset into the line this cursor is on.
     */
    public int column (int position)
    {
        return (mIndex.column (position));
    }

    /**
     * Get the text identified by the given limits.
     * @param start The starting position, zero based.
     * @param end The ending position
     * (exclusive, i.e. the character at the ending position is not included),
     * zero based.
     * @return The text from <code>start</code> to <code>end</code>.
     * @see #getText(StringBuffer, int, int)
     * @exception IllegalArgumentException If an attempt is made to get
     * characters ahead of the current source offset (character position).
     */
    public String getText (int start, int end)
        throws
            IllegalArgumentException
    {
        String ret;

        try
        {
            ret = mSource.getString (start, end - start);
        }
        catch (IOException ioe)
        {
            throw new IllegalArgumentException (
                "can't get the "
                + (end - start)
                + "characters at position "
                + start
                + " - "
                + ioe.getMessage ());
        }

        return (ret);
    }

    /**
     * Put the text identified by the given limits into the given buffer.
     * @param buffer The accumulator for the characters.
     * @param start The starting position, zero based.
     * @param end The ending position
     * (exclusive, i.e. the character at the ending position is not included),
     * zero based.
     * @exception IllegalArgumentException If an attempt is made to get
     * characters ahead of the current source offset (character position).
     */
    public void getText (StringBuffer buffer, int start, int end)
        throws
            IllegalArgumentException
    {
        int length;

        if ((mSource.offset () < start) || (mSource.offset () < end))
            throw new IllegalArgumentException (
                "attempt to extract future characters from source"
                + start + "|" + end + " > " + mSource.offset ());
        if (end < start)
        {
            length = end;
            end = start;
            start = length;
        }
        length = end - start;
        try
        {
            mSource.getCharacters (buffer, start, length);
        }
        catch (IOException ioe)
        {
            throw new IllegalArgumentException (
                "can't get the "
                + (end - start)
                + "characters at position "
                + start
                + " - "
                + ioe.getMessage ());
        }
    }

    /**
     * Get all text read so far from the source.
     * @return The text from the source.
     * @see #getText(StringBuffer)
     */
    public String getText ()
    {
        return (getText (0, mSource.offset ()));
    }

    /**
     * Put all text read so far from the source into the given buffer.
     * @param buffer The accumulator for the characters.
     * @see #getText(StringBuffer,int,int)
     */
    public void getText (StringBuffer buffer)
    {
        getText (buffer, 0, mSource.offset ());
    }

    /**
     * Put the text identified by the given limits into the given array at the specified offset.
     * @param array The array of characters.
     * @param offset The starting position in the array where characters are to be placed.
     * @param start The starting position, zero based.
     * @param end The ending position
     * (exclusive, i.e. the character at the ending position is not included),
     * zero based.
     * @exception IllegalArgumentException If an attempt is made to get
     * characters ahead of the current source offset (character position).
     */
    public void getText (char[] array, int offset, int start, int end)
        throws
            IllegalArgumentException
    {
        int length;

        if ((mSource.offset () < start) || (mSource.offset () < end))
            throw new IllegalArgumentException ("attempt to extract future characters from source");
        if (end < start)
        {   // swap
            length = end;
            end = start;
            start = length;
        }
        length = end - start;
        try
        {
            mSource.getCharacters (array, offset, start, end);
        }
        catch (IOException ioe)
        {
            throw new IllegalArgumentException (
                "can't get the "
                + (end - start)
                + "characters at position "
                + start
                + " - "
                + ioe.getMessage ());
        }
    }

    /**
     * Get the text line the position of the cursor lies on.
     * @param cursor The position to calculate for.
     * @return The contents of the URL or file corresponding to the line number
     * containg the cursor position.
     */
    public String getLine (Cursor cursor)
    {
        int line;
        int size;
        int start;
        int end;

        line = row (cursor);
        size = mIndex.size ();
        if (line < size)
        {
            start = mIndex.elementAt (line);
            line++;
            if (line <= size)
                end = mIndex.elementAt (line);
            else
                end = mSource.offset ();
        }
        else // current line
        {
            start = mIndex.elementAt (line - 1);
            end = mSource.offset ();
        }
        
            
        return (getText (start,  end));
    }

    /**
     * Get the text line the position of the cursor lies on.
     * @param position The position to calculate for.
     * @return The contents of the URL or file corresponding to the line number
     * containg the cursor position.
     */
    public String getLine (int position)
    {
        return (getLine (new Cursor (this, position)));
    }
    
    /**
     * Display some of this page as a string.
     * @return The last few characters the source read in.
     */
    public String toString ()
    {
        StringBuffer buffer;
        int start;
        String ret;

        if (mSource.offset () > 0)
        {
            buffer = new StringBuffer (43);
            start = mSource.offset () - 40;
            if (0 > start)
                start = 0;
            else
                buffer.append ("...");
            getText (buffer, start, mSource.offset ());
            ret = buffer.toString ();
        }
        else
            ret = super.toString ();
        
        return (ret);
    }
}
