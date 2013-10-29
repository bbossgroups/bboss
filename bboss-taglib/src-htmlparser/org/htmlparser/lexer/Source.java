// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/lexer/Source.java,v $
// $Author: derrickoswald $
// $Date: 2005/05/15 11:49:04 $
// $Revision: 1.20 $
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
import java.io.Reader;
import java.io.Serializable;

import org.htmlparser.util.ParserException;

/**
 * A buffered source of characters.
 * A Source is very similar to a Reader, like:
 * <pre>
 * new InputStreamReader (connection.getInputStream (), charset)
 * </pre>
 * It differs from the above, in three ways:
 * <ul>
 * <li>the fetching of bytes may be asynchronous</li>
 * <li>the character set may be changed, which resets the input stream</li>
 * <li>characters may be requested more than once, so in general they
 * will be buffered</li>
 * </ul>
 */
public abstract class Source
    extends
        Reader
    implements
        Serializable
{
    /**
     * Return value when the source is exhausted.
     * Has a value of {@value}.
     */
    public static final int EOF = -1;

    /**
     * Get the encoding being used to convert characters.
     * @return The current encoding.
     */
    public abstract String getEncoding ();

    /**
     * Set the encoding to the given character set.
     * If the current encoding is the same as the requested encoding,
     * this method is a no-op. Otherwise any subsequent characters read from
     * this source will have been decoded using the given character set.<p>
     * If characters have already been consumed from this source, it is expected
     * that an exception will be thrown if the characters read so far would
     * be different if the encoding being set was used from the start.
     * @param character_set The character set to use to convert characters.
     * @exception ParserException If a character mismatch occurs between
     * characters already provided and those that would have been returned
     * had the new character set been in effect from the beginning. An
     * exception is also thrown if the character set is not recognized.
     */
    public abstract void setEncoding (String character_set)
        throws
            ParserException;

    //
    // Reader overrides
    //

    /**
     * Does nothing.
     * It's supposed to close the source, but use {@link #destroy} instead.
     * @exception IOException <em>not used</em>
     * @see #destroy
     */
    public abstract void close () throws IOException;

    /**
     * Read a single character.
     * This method will block until a character is available,
     * an I/O error occurs, or the source is exhausted.
     * @return The character read, as an integer in the range 0 to 65535
     * (<tt>0x00-0xffff</tt>), or {@link #EOF} if the source is exhausted.
     * @exception IOException If an I/O error occurs.
     */
    public abstract int read () throws IOException;

    /**
     * Read characters into a portion of an array.  This method will block
     * until some input is available, an I/O error occurs, or the source is
     * exhausted.
     * @param cbuf Destination buffer
     * @param off Offset at which to start storing characters
     * @param len Maximum number of characters to read
     * @return The number of characters read, or {@link #EOF} if the source is
     * exhausted.
     * @exception IOException If an I/O error occurs.
     */
    public abstract int read (char[] cbuf, int off, int len) throws IOException;

    /**
     * Read characters into an array.
     * This method will block until some input is available, an I/O error occurs,
     * or the source is exhausted.
     * @param cbuf Destination buffer.
     * @return The number of characters read, or {@link #EOF} if the source is
     * exhausted.
     * @exception IOException If an I/O error occurs.
     */
    public abstract int read (char[] cbuf) throws IOException;

    /**
     * Tell whether this source is ready to be read.
     * @return <code>true</code> if the next read() is guaranteed not to block
     * for input, <code>false</code> otherwise.
     * Note that returning false does not guarantee that the next read will block.
     * @exception IOException If an I/O error occurs.
     */
    public abstract boolean ready () throws IOException;

    /**
     * Reset the source.
     * Repositions the read point to begin at zero.
     */
    public abstract void reset ();

    /**
     * Tell whether this source supports the mark() operation.
     * @return <code>true</code> if and only if this source supports the mark
     * operation.
     */
    public abstract boolean markSupported ();

    /**
     * Mark the present position.
     * Subsequent calls to {@link #reset}
     * will attempt to reposition the source to this point.  Not all
     * sources support the mark() operation.
     * @param readAheadLimit The minimum number of characters that can be read
     * before this mark becomes invalid.
     * @exception IOException If an I/O error occurs.
     */
    public abstract void mark (int readAheadLimit) throws IOException;

    /**
     * Skip characters.
     * This method will block until some characters are available,
     * an I/O error occurs, or the source is exhausted.
     * <em>Note: n is treated as an int</em>
     * @param n The number of characters to skip.
     * @return The number of characters actually skipped
     * @exception IOException If an I/O error occurs.
     */
    public abstract long skip (long n) throws IOException;

    //
    // Methods not in your Daddy's Reader
    //

    /**
     * Undo the read of a single character.
     * @exception IOException If the source is closed or no characters have
     * been read.
     */
    public abstract void unread () throws IOException;

    /**
     * Retrieve a character again.
     * @param offset The offset of the character.
     * @return The character at <code>offset</code>.
     * @exception IOException If the source is closed or the offset is beyond
     * {@link #offset()}.
     */
    public abstract char getCharacter (int offset) throws IOException;

    /**
     * Retrieve characters again.
     * @param array The array of characters.
     * @param offset The starting position in the array where characters are to be placed.
     * @param start The starting position, zero based.
     * @param end The ending position
     * (exclusive, i.e. the character at the ending position is not included),
     * zero based.
     * @exception IOException If the source is closed or the start or end is
     * beyond {@link #offset()}.
     */
    public abstract void getCharacters (char[] array, int offset, int start, int end) throws IOException;

    /**
     * Retrieve a string comprised of characters already read.
     * @param offset The offset of the first character.
     * @param length The number of characters to retrieve.
     * @return A string containing the <code>length</code> characters at <code>offset</code>.
     * @exception IOException If the source is closed.
     */
    public abstract String getString (int offset, int length) throws IOException;

    /**
     * Append characters already read into a <code>StringBuffer</code>.
     * @param buffer The buffer to append to.
     * @param offset The offset of the first character.
     * @param length The number of characters to retrieve.
     * @exception IOException If the source is closed or the offset or
     * (offset + length) is beyond {@link #offset()}.
     */
    public abstract void getCharacters (StringBuffer buffer, int offset, int length) throws IOException;

    /**
     * Close the source.
     * Once a source has been closed, further {@link #read() read},
     * {@link #ready ready}, {@link #mark mark}, {@link #reset reset},
     * {@link #skip skip}, {@link #unread unread},
     * {@link #getCharacter getCharacter} or {@link #getString getString}
     * invocations will throw an IOException.
     * Closing a previously-closed source, however, has no effect.
     * @exception IOException If an I/O error occurs.
     */
    public abstract void destroy () throws IOException;

    /**
     * Get the position (in characters).
     * @return The number of characters that have already been read, or
     * {@link #EOF} if the source is closed.
     */
    public abstract int offset ();

    /**
     * Get the number of available characters.
     * @return The number of characters that can be read without blocking.
     */
    public abstract int available ();
}
