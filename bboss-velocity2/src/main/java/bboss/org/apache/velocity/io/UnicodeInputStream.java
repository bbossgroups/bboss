package bboss.org.apache.velocity.io;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

import java.util.Locale;

/**
 * This is an input stream that is unicode BOM aware. This allows you to e.g. read
 * Windows Notepad Unicode files as Velocity templates.
 *
 * It allows you to check the actual encoding of a file by calling {@link #getEncodingFromStream()} on
 * the input stream reader.
 *
 * This class is not thread safe! When more than one thread wants to use an instance of UnicodeInputStream,
 * the caller must provide synchronization.
 *
 * @author <a href="mailto:mailmur@yahoo.com">Aki Nieminen</a>
 * @author <a href="mailto:henning@apache.org">Henning P. Schmiedehausen</a>
 * @version $Id$
 * @since 1.5
 */
public class UnicodeInputStream
    extends InputStream
{

    /** BOM Marker for UTF 8. See http://www.unicode.org/unicode/faq/utf_bom.html */
    public static final UnicodeBOM UTF8_BOM = new UnicodeBOM("UTF-8", new byte [] { (byte)0xef, (byte)0xbb, (byte)0xbf });

    /** BOM Marker for UTF 16, little endian. See http://www.unicode.org/unicode/faq/utf_bom.html */
    public static final UnicodeBOM UTF16LE_BOM = new UnicodeBOM("UTF-16LE", new byte [] { (byte)0xff, (byte)0xfe });

    /** BOM Marker for UTF 16, big endian. See http://www.unicode.org/unicode/faq/utf_bom.html */
    public static final UnicodeBOM UTF16BE_BOM = new UnicodeBOM("UTF-16BE", new byte [] { (byte)0xfe, (byte)0xff });

    /**
     * BOM Marker for UTF 32, little endian. See http://www.unicode.org/unicode/faq/utf_bom.html
     *
     */
    public static final UnicodeBOM UTF32LE_BOM = new UnicodeBOM("UTF-32LE", new byte [] { (byte)0xff, (byte)0xfe, (byte)0x00, (byte)0x00 });

    /**
     * BOM Marker for UTF 32, big endian. See http://www.unicode.org/unicode/faq/utf_bom.html
     *
     */
    public static final UnicodeBOM UTF32BE_BOM = new UnicodeBOM("UTF-32BE", new byte [] { (byte)0x00, (byte)0x00, (byte)0xfe, (byte)0xff });

    /** The maximum amount of bytes to read for a BOM */
    private static final int MAX_BOM_SIZE = 4;

    /** Buffer for BOM reading */
    private byte [] buf = new byte[MAX_BOM_SIZE];

    /** Buffer pointer. */
    private int pos = 0;

    /** The stream encoding as read from the BOM or null. */
    private final String encoding;

    /** True if the BOM itself should be skipped and not read. */
    private final boolean skipBOM;

    private final PushbackInputStream inputStream;

    /**
     * Creates a new UnicodeInputStream object. Skips a BOM which defines the file encoding.
     *
     * @param  inputStream The input stream to use for reading.
     * @throws IllegalStateException
     * @throws IOException
     */
    public UnicodeInputStream(final InputStream inputStream)
            throws IllegalStateException, IOException
    {
        this(inputStream, true);
    }

    /**
     * Creates a new UnicodeInputStream object.
     *
     * @param  inputStream The input stream to use for reading.
     * @param skipBOM If this is set to true, a BOM read from the stream is discarded. This parameter should normally be true.
     * @throws IllegalStateException
     * @throws IOException
     */
    public UnicodeInputStream(final InputStream inputStream, boolean skipBOM)
            throws IllegalStateException, IOException
    {
        super();

        this.skipBOM = skipBOM;
        this.inputStream = new PushbackInputStream(inputStream, MAX_BOM_SIZE);

        try
        {
            this.encoding = readEncoding();
        }
        catch (IOException ioe)
        {
            throw new IllegalStateException("Could not read BOM from Stream", ioe);
        }
    }

    /**
     * Returns true if the input stream discards the BOM.
     *
     * @return  True if the input stream discards the BOM.
     */
    public boolean isSkipBOM()
    {
        return skipBOM;
    }

    /**
     * Read encoding based on BOM.
     *
     * @return  The encoding based on the BOM.
     *
     * @throws  IllegalStateException  When a problem reading the BOM occured.
     */
    public String getEncodingFromStream()
    {
        return encoding;
    }

    /**
     * This method gets the encoding from the stream contents if a BOM exists. If no BOM exists, the encoding
     * is undefined.
     *
     * @return The encoding of this streams contents as decided by the BOM or null if no BOM was found.
     * @throws IOException
     */
    protected String readEncoding()
        throws IOException
    {
        pos = 0;

        UnicodeBOM encoding = null;

        // read first byte.
        if (readByte())
        {
            // Build a list of matches
            //
            // 00 00 FE FF --> UTF 32 BE
            // EF BB BF    --> UTF 8
            // FE FF       --> UTF 16 BE
            // FF FE       --> UTF 16 LE
            // FF FE 00 00 --> UTF 32 LE

            switch (buf[0])
            {
            case (byte)0x00: // UTF32 BE
                encoding = match(UTF32BE_BOM, null);
                break;
            case (byte)0xef: // UTF8
                encoding = match(UTF8_BOM, null);
                break;
            case (byte)0xfe: // UTF16 BE
                encoding = match(UTF16BE_BOM, null);
                break;
            case (byte)0xff: // UTF16/32 LE
                encoding = match(UTF16LE_BOM, null);

                if (encoding != null)
                {
                    encoding = match(UTF32LE_BOM, encoding);
                }
                break;

            default:
                encoding = null;
                break;
            }
        }

        pushback(encoding);

        return (encoding != null) ? encoding.getEncoding() : null;
    }

    private UnicodeBOM match(final UnicodeBOM matchEncoding, final UnicodeBOM noMatchEncoding)
        throws IOException
    {
        byte [] bom = matchEncoding.getBytes();

        for (int i = 0; i < bom.length; i++)
        {
            if (pos <= i) // Byte has not yet been read
            {
                if (!readByte())
                {
                    return noMatchEncoding;
                }
            }

            if (bom[i] != buf[i])
            {
                return noMatchEncoding;
            }
        }

        return matchEncoding;
    }

    private boolean readByte()
            throws IOException
    {
        int res = inputStream.read();
        if (res == -1)
        {
            return false;
        }

        if (pos >= buf.length)
        {
            throw new IOException("BOM read error");
        }

        buf[pos++] = (byte) res;
        return true;
    }

    private void pushback(final UnicodeBOM matchBOM)
        throws IOException
    {
        int count = pos; // By default, all bytes are pushed back.
        int start = 0;

        if (matchBOM != null && skipBOM)
        {
            // We have a match (some bytes are part of the BOM)
            // and we want to skip the BOM. Push back only the bytes
            // after the BOM.
            start = matchBOM.getBytes().length;
            count = (pos - start);

            if (count < 0)
            {
                throw new IllegalStateException("Match has more bytes than available!");
            }
        }

        inputStream.unread(buf, start, count);
    }

    /**
     * @throws IOException
     * @see java.io.InputStream#close()
     */
    @Override
    public void close()
        throws IOException
    {
        inputStream.close();
    }

    /**
     * @throws IOException
     * @see java.io.InputStream#available()
     */
    @Override
    public int available()
        throws IOException
    {
        return inputStream.available();
    }

    /**
     * @param readlimit
     * @see java.io.InputStream#mark(int)
     */
    @Override
    public void mark(final int readlimit)
    {
        inputStream.mark(readlimit);
    }

    /**
     * @return mark supported
     * @see java.io.InputStream#markSupported()
     */
    @Override
    public boolean markSupported()
    {
        return inputStream.markSupported();
    }

    /**
     * @return read char
     * @see java.io.InputStream#read()
     */
    @Override
    public int read()
        throws IOException
    {
        return inputStream.read();
    }

    /**
     * @param b buffer
     * @return read chars count
     * @see java.io.InputStream#read(byte[])
     */
    @Override
    public int read(final byte [] b)
        throws IOException
    {
        return inputStream.read(b);
    }

    /**
     * @param b buffer
     * @param off offset
     * @param len length
     * @return reac char
     * @see java.io.InputStream#read(byte[], int, int)
     */
    @Override
    public int read(final byte [] b, final int off, final int len)
        throws IOException
    {
        return inputStream.read(b, off, len);
    }

    /**
     * @see java.io.InputStream#reset()
     */
    @Override
    public void reset()
        throws IOException
    {
        inputStream.reset();
    }

    /**
     * @param n
     * @return skipped count
     * @see java.io.InputStream#skip(long)
     */
    @Override
    public long skip(final long n)
        throws IOException
    {
        return inputStream.skip(n);
    }


    /**
     * Helper function to compare encodings
     * @param left
     * @param right
     * @return true for same encoding
     */
    public static boolean sameEncoding(String left, String right)
    {
        left = left.toUpperCase(Locale.ROOT).replace("-", "").replace("_","");
        right = right.toUpperCase(Locale.ROOT).replace("-", "").replace("_","");
        return left.equals(right);
    }

    /**
     * Helper class to bundle encoding and BOM marker.
     *
     * @author <a href="mailto:henning@apache.org">Henning P. Schmiedehausen</a>
     * @version $Id$
     */
    static final class UnicodeBOM
    {
        private final String encoding;

        private final byte [] bytes;

        private UnicodeBOM(final String encoding, final byte [] bytes)
        {
            this.encoding = encoding;
            this.bytes = bytes;
        }

        String getEncoding()
        {
            return encoding;
        }

        byte [] getBytes()
        {
            return bytes;
        }
    }
}
