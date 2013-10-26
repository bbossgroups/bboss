// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/lexer/Stream.java,v $
// $Author: derrickoswald $
// $Date: 2005/05/15 11:49:04 $
// $Revision: 1.14 $
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
import java.io.InputStream;

/**
 * Provides for asynchronous fetching from a stream.
 *
 */
public class Stream extends InputStream implements Runnable
{
    /**
     * The number of calls to fill.
     * Note: to be removed.
     */
    public int fills = 0;

    /**
     * The number of reallocations.
     * Note: to be removed.
     */
    public int reallocations = 0;

    /**
     * The number of synchronous (blocking) fills.
     * Note: to be removed.
     */
    public int synchronous = 0;

    /**
     * An initial buffer size.
     */
    protected static final int BUFFER_SIZE = 4096;

    /**
     * Return value when no more characters are left.
     */
    protected static final int EOF = -1;

    /**
     * The underlying stream.
     */
    protected volatile InputStream mIn;

    /**
     * The bytes read so far.
     */
    public volatile byte[] mBuffer;

    /**
     * The number of valid bytes in the buffer.
     */
    public volatile int mLevel;

    /**
     * The offset of the next byte returned by read().
     */
    protected int mOffset;

    /**
     * The content length from the HTTP header.
     */
    protected int mContentLength;

    /**
     * The bookmark.
     */
    protected int mMark;

    /**
     * Construct a stream with no assumptions about the number of bytes available.
     * @param in The input stream to use.
     */
    public Stream (InputStream in)
    {
        this (in, 0);
    }

    /**
     * Construct a stream to read the given number of bytes.
     * @param in The input stream to use.
     * @param bytes The maximum number of bytes to read.
     * This should be set to the ContentLength from the HTTP header.
     * A negative or zero value indicates an unknown number of bytes.
     */
    public Stream (InputStream in, int bytes)
    {
        mIn = in;
        mBuffer = null;
        mLevel = 0;
        mOffset = 0;
        mContentLength = bytes < 0 ? 0 : bytes;
        mMark = -1;
    }

    /**
     * Fetch more bytes from the underlying stream.
     * Has no effect if the underlying stream has been drained.
     * @param force If <code>true</code>, an attempt is made to read from the
     * underlying stream, even if bytes are available, If <code>false</code>,
     * a read of the underlying stream will not occur if there are already
     * bytes available.
     * @return <code>true</code> if not at the end of the input stream.
     * @exception IOException If the underlying stream read() or available() throws one.
     */
    protected synchronized boolean fill (boolean force)
        throws
            IOException
    {
        int size;
        byte[] buffer;
        int read;
        boolean ret;

        ret = false;

        if (null != mIn) // mIn goes null when it's been sucked dry
        {
            if (!force)
            {   // check for change of state while waiting on the monitor in a synchronous call
                if (0 != available ())
                    return (true);
                synchronous++;
            }

            // get some buffer space
            if (0 == mContentLength)
            {   // unknown content length... keep doubling
                if (null == mBuffer)
                {
                    mBuffer = new byte[Math.max (BUFFER_SIZE, mIn.available ())];
                    buffer = mBuffer;
                }
                else
                {
                    if (mBuffer.length - mLevel < BUFFER_SIZE / 2)
                        buffer = new byte[Math.max (mBuffer.length * 2, mBuffer.length + mIn.available ())];
                    else
                        buffer = mBuffer;
                }
                size = buffer.length - mLevel;
            }
            else
            {   // known content length... allocate once
                size = mContentLength - mLevel;
                if (null == mBuffer)
                    mBuffer =  new byte[size];
                buffer = mBuffer;
            }

            // read into the end of the 'new' buffer
            read = mIn.read (buffer, mLevel, size);
            if (-1 == read)
            {
                mIn.close ();
                mIn = null;
            }
            else
            {
                if (mBuffer != buffer)
                {   // copy the bytes previously read
                    System.arraycopy (mBuffer, 0, buffer, 0, mLevel);
                    mBuffer = buffer;
                    reallocations++;
                }
                mLevel += read;
                if ((0 != mContentLength) && (mLevel == mContentLength))
                {
                    mIn.close ();
                    mIn = null;
                }
                ret = true;
                fills++;
            }
        }

        return (ret);
    }

    //
    // Runnable interface
    //

    /**
     * Continually read the underlying stream untill exhausted.
     * @see java.lang.Thread#run()
     */
    public void run ()
    {
        boolean filled;

        do
        {   // keep hammering the socket with no delay, it's metered upstream
            try
            {
                filled = fill (true);
            }
            catch (IOException ioe)
            {
                ioe.printStackTrace ();
                // exit the thread if there is a problem,
                // let the synchronous reader find out about it
                filled = false;
            }
        }
        while (filled);
    }

    //
    // InputStream overrides
    //

    /**
     * Reads the next byte of data from the input stream. The value byte is
     * returned as an <code>int</code> in the range <code>0</code> to
     * <code>255</code>. If no byte is available because the end of the stream
     * has been reached, the value <code>-1</code> is returned. This method
     * blocks until input data is available, the end of the stream is detected,
     * or an exception is thrown.
     * @return  The next byte of data, or <code>-1</code> if the end of the
     * stream is reached.
     * @exception IOException If an I/O error occurs.
     */
    public int read () throws IOException
    {
        int ret;

        // The following is unsynchronized code.
        // Some would argue that unsynchronized access isn't thread safe
        // but I think I can rationalize it in this case...
        // The two volatile members are mLevel and mBuffer (besides mIn).
        // If (mOffset >= mLevel) turns false after the test, fill is
        // superflously called, but it's synchronized and figures it out.
        // (mOffset < mLevel) only goes more true by the operation of the
        // background thread, it increases the value of mLevel
        // and volatile int access is atomic.
        // If mBuffer changes by the operation of the background thread,
        // the array pointed to can only be bigger than the previous buffer,
        // and hence no array bounds exception can be raised.
        if (0 == available ())
            fill (false);
        if (0 != available ())
            ret = mBuffer[mOffset++] & 0xff;
        else
            ret = EOF;

        return (ret);
    }

    /**
     * Returns the number of bytes that can be read (or skipped over) from
     * this input stream without blocking by the next caller of a method for
     * this input stream.  The next caller might be the same thread or or
     * another thread.
     * @return The number of bytes that can be read from this input stream
     * without blocking.
     * @exception IOException If an I/O error occurs.
     */
    public int available () throws IOException
    {
        return (mLevel - mOffset);
    }

    /**
     * Closes this input stream and releases any system resources associated
     * with the stream.
     * @exception IOException If an I/O error occurs.
     */
    public synchronized void close () throws IOException
    {
        if (null != mIn)
        {
            mIn.close ();
            mIn = null;
        }
        mBuffer = null;
        mLevel = 0;
        mOffset = 0;
        mContentLength =0;
        mMark = -1;
    }

    /**
     * Repositions this stream to the position at the time the
     * <code>mark</code> method was last called on this input stream.
     *
     * <p> The general contract of <code>reset</code> is:
     *
     * <p><ul>
     *
     * <li> If the method <code>markSupported</code> returns
     * <code>true</code>, then:
     *
     *     <ul><li> If the method <code>mark</code> has not been called since
     *     the stream was created, or the number of bytes read from the stream
     *     since <code>mark</code> was last called is larger than the argument
     *     to <code>mark</code> at that last call, then an
     *     <code>IOException</code> might be thrown.
     *
     *     <li> If such an <code>IOException</code> is not thrown, then the
     *     stream is reset to a state such that all the bytes read since the
     *     most recent call to <code>mark</code> (or since the start of the
     *     file, if <code>mark</code> has not been called) will be resupplied
     *     to subsequent callers of the <code>read</code> method, followed by
     *     any bytes that otherwise would have been the next input data as of
     *     the time of the call to <code>reset</code>. </ul>
     *
     * <li> If the method <code>markSupported</code> returns
     * <code>false</code>, then:
     *
     *     <ul><li> The call to <code>reset</code> may throw an
     *     <code>IOException</code>.
     *
     *     <li> If an <code>IOException</code> is not thrown, then the stream
     *     is reset to a fixed state that depends on the particular type of the
     *     input stream and how it was created. The bytes that will be supplied
     *     to subsequent callers of the <code>read</code> method depend on the
     *     particular type of the input stream. </ul></ul>
     *
     * @exception IOException <em>Never thrown. Just for subclassers.</em>
     * @see java.io.InputStream#mark(int)
     * @see java.io.IOException
     *
     */
    public void reset () throws IOException
    {
        if (-1 != mMark)
            mOffset = mMark;
        else
            mOffset = 0;
    }

    /**
     * Tests if this input stream supports the <code>mark</code> and
     * <code>reset</code> methods. Whether or not <code>mark</code> and
     * <code>reset</code> are supported is an invariant property of a
     * particular input stream instance. The <code>markSupported</code> method
     * of <code>InputStream</code> returns <code>false</code>.
     *
     * @return <code>true</code>.
     * @see java.io.InputStream#mark(int)
     * @see java.io.InputStream#reset()
     *
     */
    public boolean markSupported ()
    {
        return (true);
    }

    /**
     * Marks the current position in this input stream. A subsequent call to
     * the <code>reset</code> method repositions this stream at the last marked
     * position so that subsequent reads re-read the same bytes.
     *
     * <p> The <code>readlimit</code> arguments tells this input stream to
     * allow that many bytes to be read before the mark position gets
     * invalidated.
     *
     * <p> The general contract of <code>mark</code> is that, if the method
     * <code>markSupported</code> returns <code>true</code>, the stream somehow
     * remembers all the bytes read after the call to <code>mark</code> and
     * stands ready to supply those same bytes again if and whenever the method
     * <code>reset</code> is called.  However, the stream is not required to
     * remember any data at all if more than <code>readlimit</code> bytes are
     * read from the stream before <code>reset</code> is called.
     *
     * @param readlimit <em>Not used.</em>
     * @see java.io.InputStream#reset()
     *
     */
    public void mark (int readlimit)
    {
        mMark = mOffset;
    }
}
