// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/lexer/InputStreamSource.java,v $
// $Author: derrickoswald $
// $Date: 2005/05/15 11:49:04 $
// $Revision: 1.7 $
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;

import org.htmlparser.util.EncodingChangeException;
import org.htmlparser.util.ParserException;

/**
 * A source of characters based on an InputStream such as from a URLConnection.
 */
public class InputStreamSource
    extends
        Source
{
    /**
     * An initial buffer size.
     * Has a default value of {@value}.
     */
    public static int BUFFER_SIZE = 16384;

    /**
     * The stream of bytes.
     * Set to <code>null</code> when the source is closed.
     */
    protected transient InputStream mStream;

    /**
     * The character set in use.
     */
    protected String mEncoding;

    /**
     * The converter from bytes to characters.
     */
    protected transient InputStreamReader mReader;

    /**
     * The characters read so far.
     */
    protected char[] mBuffer;

    /**
     * The number of valid bytes in the buffer.
     */
    protected int mLevel;

    /**
     * The offset of the next byte returned by read().
     */
    protected int mOffset;

    /**
     * The bookmark.
     */
    protected int mMark;

    /**
     * Create a source of characters using the default character set.
     * @param stream The stream of bytes to use.
     * @exception UnsupportedEncodingException If the default character set
     * is unsupported.
     */
    public InputStreamSource (InputStream stream)
        throws
            UnsupportedEncodingException
    {
        this (stream, null, BUFFER_SIZE);
    }

    /**
     * Create a source of characters.
     * @param stream The stream of bytes to use.
     * @param charset The character set used in encoding the stream.
     * @exception UnsupportedEncodingException If the character set
     * is unsupported.
     */
    public InputStreamSource (InputStream stream, String charset)
        throws
            UnsupportedEncodingException
    {
        this (stream, charset, BUFFER_SIZE);
    }

    /**
     * Create a source of characters.
     * @param stream The stream of bytes to use.
     * @param charset The character set used in encoding the stream.
     * @param size The initial character buffer size.
     * @exception UnsupportedEncodingException If the character set
     * is unsupported.
     */
    public InputStreamSource (InputStream stream, String charset, int size)
        throws
            UnsupportedEncodingException
    {
        if (null == stream)
            stream = new Stream (null);
        else
            // bug #1044707 mark()/reset() issues
            if (!stream.markSupported ())
                // wrap the stream so we can reset
                stream = new Stream (stream);
            // else
                // just because mark is supported doesn't guarantee
                // proper reset operation; there is no call to mark
                // in this code, so if reset misbehaves there is an
                // appropriate message in setEncoding() to suggest
                // wraping it in a Stream.
                // This was deemed better than an attempt to call
                // reset at this point just to check if we would
                // succeed later, or to call mark with an arbitrary
                // lookahead size
        mStream = stream;
        if (null == charset)
        {
            mReader = new InputStreamReader (stream);
            mEncoding = mReader.getEncoding ();
        }
        else
        {
            mEncoding = charset;
            mReader = new InputStreamReader (stream, charset);
        }
        mBuffer = new char[size];
        mLevel = 0;
        mOffset = 0;
        mMark = -1;
    }

    //
    // Serialization support
    //

    /**
     * Serialization support.
     * @param out Where to write this object.
     * @exception IOException If serialization has a problem.
     */
    private void writeObject (ObjectOutputStream out)
        throws
            IOException
    {
        int offset;
        char[] buffer;

        if (null != mStream)
        {
            // remember the offset, drain the input stream, restore the offset
            offset = mOffset;
            buffer = new char[4096];
            while (EOF != read (buffer))
                ;
            mOffset = offset;
        }

        out.defaultWriteObject ();
    }

    /**
     * Deserialization support.
     * @param in Where to read this object from.
     * @exception IOException If deserialization has a problem.
     */
    private void readObject (ObjectInputStream in)
        throws
            IOException,
            ClassNotFoundException
    {
        in.defaultReadObject ();
        if (null != mBuffer) // buffer is null when destroy's been called
            // pretend we're open, mStream goes null when exhausted
            mStream = new ByteArrayInputStream (new byte[0]);
    }

    /**
     * Get the input stream being used.
     * @return The current input stream.
     */
    public InputStream getStream ()
    {
        return (mStream);
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
     * Begins reading from the source with the given character set.
     * If the current encoding is the same as the requested encoding,
     * this method is a no-op. Otherwise any subsequent characters read from
     * this page will have been decoded using the given character set.<p>
     * Some magic happens here to obtain this result if characters have already
     * been consumed from this source.
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
        String encoding;
        InputStream stream;
        char[] buffer;
        int offset;
        char[] new_chars;

        encoding = getEncoding ();
        if (!encoding.equalsIgnoreCase (character_set))
        {
            stream = getStream ();
            try
            {
                buffer = mBuffer;
                offset = mOffset;
                stream.reset ();
                try
                {
                    mEncoding = character_set;
                    mReader = new InputStreamReader (stream, character_set);
                    mBuffer = new char[mBuffer.length];
                    mLevel = 0;
                    mOffset = 0;
                    mMark = -1;
                    if (0 != offset)
                    {
                        new_chars = new char[offset];
                        if (offset != read (new_chars))
                            throw new ParserException ("reset stream failed");
                        for (int i = 0; i < offset; i++)
                            if (new_chars[i] != buffer[i])
                                throw new EncodingChangeException ("character mismatch (new: "
                                + new_chars[i]
                                + " [0x"
                                + Integer.toString (new_chars[i], 16)
                                + "] != old: "
                                + " [0x"
                                + Integer.toString (buffer[i], 16)
                                + buffer[i]
                                + "]) for encoding change from "
                                + encoding
                                + " to "
                                + character_set
                                + " at character offset "
                                + i);
                    }
                }
                catch (IOException ioe)
                {
                    throw new ParserException (ioe.getMessage (), ioe);
                }
            }
            catch (IOException ioe)
            {   // bug #1044707 mark()/reset() issues
                throw new ParserException ("Stream reset failed ("
                    + ioe.getMessage ()
                    + "), try wrapping it with a org.htmlparser.lexer.Stream",
                    ioe);
            }
        }
    }

    /**
     * Fetch more characters from the underlying reader.
     * Has no effect if the underlying reader has been drained.
     * @param min The minimum to read.
     * @exception IOException If the underlying reader read() throws one.
     */
    protected void fill (int min)
        throws
            IOException
    {
        char[] buffer;
        int size;
        int read;

        if (null != mReader) // mReader goes null when it's been sucked dry
        {
            size = mBuffer.length - mLevel; // available space
            if (size < min) // oops, better get some buffer space
            {
                // unknown length... keep doubling
                size = mBuffer.length * 2;
                read = mLevel + min;
                if (size < read) // or satisfy min, whichever is greater
                    size = read;
                else
                    min = size - mLevel; // read the max
                buffer = new char[size];
            }
            else
            {
                buffer = mBuffer;
                min = size;
            }

            // read into the end of the 'new' buffer
            read = mReader.read (buffer, mLevel, min);
            if (EOF == read)
            {
                mReader.close ();
                mReader = null;
            }
            else
            {
                if (mBuffer != buffer)
                {   // copy the bytes previously read
                    System.arraycopy (mBuffer, 0, buffer, 0, mLevel);
                    mBuffer = buffer;
                }
                mLevel += read;
            }
            // todo, should repeat on read shorter than original min
        }
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
     * This method will block until a character is available,
     * an I/O error occurs, or the end of the stream is reached.
     * @return The character read, as an integer in the range 0 to 65535
     * (<tt>0x00-0xffff</tt>), or {@link #EOF EOF} if the end of the stream has
     * been reached
     * @exception IOException If an I/O error occurs.
     */
    public int read () throws IOException
    {
        int ret;

        if (mLevel - mOffset < 1)
        {
            if (null == mStream)
                throw new IOException ("source is closed");
            fill (1);
            if (mOffset >= mLevel)
                ret = EOF;
            else
                ret = mBuffer[mOffset++];
        }
        else
            ret = mBuffer[mOffset++];

        return (ret);
    }

    /**
     * Read characters into a portion of an array.  This method will block
     * until some input is available, an I/O error occurs, or the end of the
     * stream is reached.
     * @param cbuf Destination buffer
     * @param off Offset at which to start storing characters
     * @param len Maximum number of characters to read
     * @return The number of characters read, or {@link #EOF EOF} if the end of
     * the stream has been reached
     * @exception IOException If an I/O error occurs.
     */
    public int read (char[] cbuf, int off, int len) throws IOException
    {
        int ret;

        if (null == mStream)
            throw new IOException ("source is closed");
        if ((null == cbuf) || (0 > off) || (0 > len))
            throw new IOException ("illegal argument read ("
                + ((null == cbuf) ? "null" : "cbuf")
                + ", " + off + ", " + len + ")");
        if (mLevel - mOffset < len)
            fill (len - (mLevel - mOffset)); // minimum to satisfy this request
        if (mOffset >= mLevel)
            ret = EOF;
        else
        {
            ret = Math.min (mLevel - mOffset, len);
            System.arraycopy (mBuffer, mOffset, cbuf, off, ret);
            mOffset += ret;
        }

        return (ret);
    }

    /**
     * Read characters into an array.
     * This method will block until some input is available, an I/O error occurs,
     * or the end of the stream is reached.
     * @param cbuf Destination buffer.
     * @return The number of characters read, or {@link #EOF EOF} if the end of
     * the stream has been reached.
     * @exception IOException If an I/O error occurs.
     */
    public int read (char[] cbuf) throws IOException
    {
        return (read (cbuf, 0, cbuf.length));
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
        if (null == mStream)
            throw new IllegalStateException ("source is closed");
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
     * @exception IOException If the source is closed.
     *
     */
    public void mark (int readAheadLimit) throws IOException
    {
        if (null == mStream)
            throw new IOException ("source is closed");
        mMark = mOffset;
    }

    /**
     * Tell whether this source is ready to be read.
     * @return <code>true</code> if the next read() is guaranteed not to block
     * for input, <code>false</code> otherwise.
     * Note that returning false does not guarantee that the next read will block.
     * @exception IOException If the source is closed.
     */
    public boolean ready () throws IOException
    {
        if (null == mStream)
            throw new IOException ("source is closed");
        return (mOffset < mLevel);
    }

    /**
     * Skip characters.
     * This method will block until some characters are available,
     * an I/O error occurs, or the end of the stream is reached.
     * <em>Note: n is treated as an int</em>
     * @param n The number of characters to skip.
     * @return The number of characters actually skipped
     * @exception IllegalArgumentException If <code>n</code> is negative.
     * @exception IOException If an I/O error occurs.
     */
    public long skip (long n)
        throws
            IOException,
            IllegalArgumentException
    {
        long ret;

        if (null == mStream)
            throw new IOException ("source is closed");
        if (0 > n)
            throw new IllegalArgumentException ("cannot skip backwards");
        else
        {
            if (mLevel - mOffset < n)
                fill ((int)(n - (mLevel - mOffset))); // minimum to satisfy this request
            if (mOffset >= mLevel)
                ret = EOF;
            else
            {
                ret = Math.min (mLevel - mOffset, n);
                mOffset += ret;
            }
        }

        return (ret);
    }

    //
    // Methods not in your Daddy's Reader
    //

    /**
     * Undo the read of a single character.
     * @exception IOException If the source is closed or no characters have
     * been read.
     */
    public void unread () throws IOException
    {
        if (null == mStream)
            throw new IOException ("source is closed");
        if (0 < mOffset)
            mOffset--;
        else
            throw new IOException ("can't unread no characters");
    }

    /**
     * Retrieve a character again.
     * @param offset The offset of the character.
     * @return The character at <code>offset</code>.
     * @exception IOException If the offset is beyond {@link #offset()} or the
     * source is closed.
     */
    public char getCharacter (int offset) throws IOException
    {
        char ret;

        if (null == mStream)
            throw new IOException ("source is closed");
        if (offset >= mBuffer.length)
            throw new IOException ("illegal read ahead");
        else
            ret = mBuffer[offset];
        
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
     * @exception IOException If the start or end is beyond {@link #offset()}
     * or the source is closed.
     */
    public void getCharacters (char[] array, int offset, int start, int end) throws IOException
    {
        if (null == mStream)
            throw new IOException ("source is closed");
        System.arraycopy (mBuffer, start, array, offset, end - start);
    }
    
    /**
     * Retrieve a string.
     * @param offset The offset of the first character.
     * @param length The number of characters to retrieve.
     * @return A string containing the <code>length</code> characters at <code>offset</code>.
     * @exception IOException If the offset or (offset + length) is beyond
     * {@link #offset()} or the source is closed.
     */
    public String getString (int offset, int length) throws IOException
    {
        String ret;

        if (null == mStream)
            throw new IOException ("source is closed");
        if (offset + length >= mBuffer.length)
            throw new IOException ("illegal read ahead");
        else
            ret = new String (mBuffer, offset, length);
        
        return (ret);
    }

    /**
     * Append characters already read into a <code>StringBuffer</code>.
     * @param buffer The buffer to append to.
     * @param offset The offset of the first character.
     * @param length The number of characters to retrieve.
     * @exception IOException If the offset or (offset + length) is beyond
     * {@link #offset()} or the source is closed.
     */
    public void getCharacters (StringBuffer buffer, int offset, int length) throws IOException
    {
        if (null == mStream)
            throw new IOException ("source is closed");
        buffer.append (mBuffer, offset, length);
    }

    /**
     * Close the source.
     * Once a source has been closed, further {@link #read() read},
     * {@link #ready ready}, {@link #mark mark}, {@link #reset reset},
     * {@link #skip skip}, {@link #unread unread},
     * {@link #getCharacter getCharacter} or {@link #getString getString}
     * invocations will throw an IOException.
     * Closing a previously-closed source, however, has no effect.
     * @exception IOException If an I/O error occurs
     */
    public void destroy () throws IOException
    {
        mStream = null;
        if (null != mReader)
            mReader.close ();
        mReader = null;
        mBuffer = null;
        mLevel = 0;
        mOffset = 0;
        mMark = -1;
    }

    /**
     * Get the position (in characters).
     * @return The number of characters that have already been read, or
     * {@link #EOF EOF} if the source is closed.
     */
    public int offset ()
    {
        int ret;

        if (null == mStream)
            ret = EOF;
        else
            ret = mOffset;

        return (ret);
    }

    /**
     * Get the number of available characters.
     * @return The number of characters that can be read without blocking or
     * zero if the source is closed.
     */
    public int available ()
    {
        int ret;

        if (null == mStream)
            ret = 0;
        else
            ret = mLevel - mOffset;

        return (ret);
    }
}
