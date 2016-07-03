// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/lexer/StringSource.java,v $
// $Author: derrickoswald $
// $Date: 2005/05/15 11:49:04 $
// $Revision: 1.4 $
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

import java.io.IOException;
import org.htmlparser.util.ParserException;

/**
 * A source of characters based on a String.
 */
public class StringSource
    extends
        Source
{
    /**
     * The source of characters.
     */
    protected String mString;

    /**
     * The current offset into the string.
     */
    protected int mOffset;

    /**
     * The encoding to report.
     * Only used by {@link #getEncoding}.
     */
    protected String mEncoding;

    /**
     * The bookmark.
     */
    protected int mMark;

    /**
     * Construct a source using the provided string.
     * Until it is set, the encoding will be reported as ISO-8859-1.
     * @param string The source of characters.
     */
    public StringSource (String string)
    {
        this (string, "ISO-8859-1");
    }

    /**
     * Construct a source using the provided string and encoding.
     * The encoding is only used by {@link #getEncoding}.
     * @param string The source of characters.
     * @param character_set The encoding to report.
     */
    public StringSource (String string, String character_set)
    {
        mString = (null == string) ? "" : string;
        mOffset = 0;
        mEncoding = character_set;
        mMark = -1;
    }

    /**
     * Get the encoding being used to convert characters.
     * @return The current encoding.
     */
    public String getEncoding ()
    {
        return (mEncoding);
    }

    /**
     * Set the encoding to the given character set.
     * This simply sets the encoding reported by {@link #getEncoding}.
     * @param character_set The character set to use to convert characters.
     * @exception ParserException <em>Not thrown</em>.
     */
    public void setEncoding (String character_set)
        throws
            ParserException
    {
        mEncoding = character_set;
    }

    //
    // Reader overrides
    //

    /**
     * Does nothing.
     * It's supposed to close the source, but use destroy() instead.
     * @exception IOException <em>not used</em>
     * @see #destroy
     */
    public void close () throws IOException
    {
    }

    /**
     * Read a single character.
     * @return The character read, as an integer in the range 0 to 65535
     * (<tt>0x00-0xffff</tt>), or {@link #EOF EOF} if the source is exhausted.
     * @exception IOException If an I/O error occurs.
     */
    public int read () throws IOException
    {
        int ret;

        if (null == mString)
            throw new IOException ("source is closed");
        else if (mOffset >= mString.length ())
            ret = EOF;
        else
        {
            ret = mString.charAt (mOffset);
            mOffset++;
        }

        return (ret);
    }

    /**
     * Read characters into a portion of an array.
     * @param cbuf Destination buffer
     * @param off Offset at which to start storing characters
     * @param len Maximum number of characters to read
     * @return The number of characters read, or {@link #EOF EOF} if the source
     * is exhausted.
     * @exception IOException If an I/O error occurs.
     */
    public int read (char[] cbuf, int off, int len) throws IOException
    {
        int length;
        int ret;

        if (null == mString)
            throw new IOException ("source is closed");
        else
        {
            length = mString.length ();
            if (mOffset >= length)
                ret = EOF;
            else
            {
                if (len > length - mOffset)
                    len = length - mOffset;
                mString.getChars (mOffset, mOffset + len, cbuf, off);
                mOffset += len;
                ret = len;
            }
        }

        return (ret);
    }

    /**
     * Read characters into an array.
     * @param cbuf Destination buffer.
     * @return The number of characters read, or {@link #EOF EOF} if the source
     * is exhausted.
     * @exception IOException If an I/O error occurs.
     */

    public int read (char[] cbuf) throws IOException
    {
        return (read (cbuf, 0, cbuf.length));
    }

    /**
     * Tell whether this source is ready to be read.
     * @return Equivalent to a non-zero {@link #available()}, i.e. there are
     * still more characters to read.
     * @exception IOException Thrown if the source is closed.
     */
    public boolean ready () throws IOException
    {
        if (null == mString)
            throw new IOException ("source is closed");
        return (mOffset < mString.length ());
    }

    /**
     * Reset the source.
     * Repositions the read point to begin at zero.
     * @exception IllegalStateException If the source has been closed.
     */
    public void reset ()
        throws
            IllegalStateException
    {
        if (null == mString)
            throw new IllegalStateException ("source is closed");
        else
            if (-1 != mMark)
                mOffset = mMark;
            else
                mOffset = 0;
    }

    /**
     * Tell whether this source supports the mark() operation.
     * @return <code>true</code>.
     */
    public boolean markSupported ()
    {
        return (true);
    }

    /**
     * Mark the present position in the source.
     * Subsequent calls to {@link #reset()}
     * will attempt to reposition the source to this point.
     * @param  readAheadLimit <em>Not used.</em>
     * @exception IOException Thrown if the source is closed.
     *
     */
    public void mark (int readAheadLimit) throws IOException
    {
        if (null == mString)
            throw new IOException ("source is closed");
        mMark = mOffset;
    }

    /**
     * Skip characters.
     * <em>Note: n is treated as an int</em>
     * @param n The number of characters to skip.
     * @return The number of characters actually skipped
     * @exception IllegalArgumentException If <code>n</code> is negative.
     * @exception IOException If the source is closed.
     */
    public long skip (long n)
        throws
            IOException,
            IllegalArgumentException
    {
        int length;
        long ret;

        if (null == mString)
            throw new IOException ("source is closed");
        if (0 > n)
            throw new IllegalArgumentException ("cannot skip backwards");
        else
        {
            length = mString.length ();
            if (mOffset >= length)
                n = 0L;
            else if (n > length - mOffset)
                n = length - mOffset;
            mOffset += n;
            ret = n;
        }

        return (ret);
    }

    //
    // Methods not in your Daddy's Reader
    //

    /**
     * Undo the read of a single character.
     * @exception IOException If no characters have been read or the source is closed.
     */
    public void unread () throws IOException
    {
        if (null == mString)
            throw new IOException ("source is closed");
        else if (mOffset <= 0)
            throw new IOException ("can't unread no characters");
        else
            mOffset--;
    }

    /**
     * Retrieve a character again.
     * @param offset The offset of the character.
     * @return The character at <code>offset</code>.
     * @exception IOException If the source is closed or an attempt is made to
     * read beyond {@link #offset()}.
     */
    public char getCharacter (int offset) throws IOException
    {
        char ret;

        if (null == mString)
            throw new IOException ("source is closed");
        else if (offset >= mOffset)
            throw new IOException ("read beyond current offset");
        else
            ret = mString.charAt (offset);

        return (ret);
    }

    /**
     * Retrieve characters again.
     * @param array The array of characters.
     * @param offset The starting position in the array where characters are to be placed.
     * @param start The starting position, zero based.
     * @param end The ending position
     * (exclusive, i.e. the character at the ending position is not included),
     * zero based.
     * @exception IOException If the source is closed or an attempt is made to
     * read beyond {@link #offset()}.
     */
    public void getCharacters (char[] array, int offset, int start, int end) throws IOException
    {
        if (null == mString)
            throw new IOException ("source is closed");
        else
        {
            if (end > mOffset)
                throw new IOException ("read beyond current offset");
            else
                mString.getChars (start, end, array, offset);
        }
    }

    /**
     * Retrieve a string comprised of characters already read.
     * Asking for characters ahead of {@link #offset()} will throw an exception.
     * @param offset The offset of the first character.
     * @param length The number of characters to retrieve.
     * @return A string containing the <code>length</code> characters at <code>offset</code>.
     * @exception IOException If the source is closed or an attempt is made to
     * read beyond {@link #offset()}.
     */
    public String getString (int offset, int length) throws IOException
    {
        String ret;

        if (null == mString)
            throw new IOException ("source is closed");
        else
        {
            if (offset + length > mOffset)
                throw new IOException ("read beyond end of string");
            else
                ret = mString.substring (offset, offset + length);
        }

        return (ret);
    }

    /**
     * Append characters already read into a <code>StringBuffer</code>.
     * Asking for characters ahead of {@link #offset()} will throw an exception.
     * @param buffer The buffer to append to.
     * @param offset The offset of the first character.
     * @param length The number of characters to retrieve.
     * @exception IOException If the source is closed or an attempt is made to
     * read beyond {@link #offset()}.
     */
    public void getCharacters (StringBuffer buffer, int offset, int length) throws IOException
    {
        if (null == mString)
            throw new IOException ("source is closed");
        else
        {
            if (offset + length > mOffset)
                throw new IOException ("read beyond end of string");
            else
                buffer.append (mString.substring (offset, offset + length));
        }
    }

    /**
     * Close the source.
     * Once a source has been closed, further {@link #read() read},
     * {@link #ready ready}, {@link #mark mark}, {@link #reset reset},
     * {@link #skip skip}, {@link #unread unread},
     * {@link #getCharacter getCharacter} or {@link #getString getString}
     * invocations will throw an IOException.
     * Closing a previously-closed source, however, has no effect.
     * @exception IOException <em>Not thrown</em>
     */
    public void destroy () throws IOException
    {
        mString = null;
    }

    /**
     * Get the position (in characters).
     * @return The number of characters that have already been read, or
     * {@link #EOF EOF} if the source is closed.
     */
    public int offset ()
    {
        int ret;

        if (null == mString)
            ret = EOF;
        else
            ret = mOffset;

        return (ret);
    }

    /**
     * Get the number of available characters.
     * @return The number of characters that can be read or zero if the source
     * is closed.
     */
    public int available ()
    {
        int ret;

        if (null == mString)
            ret = 0;
        else
            ret = mString.length () - mOffset;

        return (ret);
    }
}
