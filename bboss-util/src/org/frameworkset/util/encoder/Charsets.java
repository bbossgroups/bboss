/**
 * 
 */
package org.frameworkset.util.encoder;

import java.nio.charset.Charset;

/**
 * @author yinbp
 *
 * @Date:2016-11-18 12:13:56
 */
public class Charsets {

	 //
    // This class should only contain Charset instances for required encodings. This guarantees that it will load
    // correctly and without delay on all Java platforms.
    //

    /**
     * Returns the given Charset or the default Charset if the given Charset is null.
     *
     * @param charset
     *            A charset or null.
     * @return the given Charset or the default Charset if the given Charset is null
     */
    public static Charset toCharset(final Charset charset) {
        return charset == null ? Charset.defaultCharset() : charset;
    }

    /**
     * Returns a Charset for the named charset. If the name is null, return the default Charset.
     *
     * @param charset
     *            The name of the requested charset, may be null.
     * @return a Charset for the named charset
     * @throws java.nio.charset.UnsupportedCharsetException
     *             If the named charset is unavailable
     */
    public static Charset toCharset(final String charset) {
        return charset == null ? Charset.defaultCharset() : Charset.forName(charset);
    }

    /**
     * CharEncodingISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1.
     * <p>
     * Every implementation of the Java platform is required to support this character encoding.
     *
     * @see <a href="http://docs.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @deprecated Use Java 7's {@link java.nio.charset.StandardCharsets}
     */
    @Deprecated
    public static final Charset ISO_8859_1 = Charset.forName(CharEncoding.ISO_8859_1);

    /**
     * Seven-bit ASCII, also known as ISO646-US, also known as the Basic Latin block of the Unicode character set.
     * <p>
     * Every implementation of the Java platform is required to support this character encoding.
     *
     * @see <a href="http://docs.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @deprecated Use Java 7's {@link java.nio.charset.StandardCharsets}
     */
    @Deprecated
    public static final Charset US_ASCII = Charset.forName(CharEncoding.US_ASCII);

    /**
     * Sixteen-bit Unicode Transformation Format, The byte order specified by a mandatory initial byte-order mark
     * (either order accepted on input, big-endian used on output)
     * <p>
     * Every implementation of the Java platform is required to support this character encoding.
     *
     * @see <a href="http://docs.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @deprecated Use Java 7's {@link java.nio.charset.StandardCharsets}
     */
    @Deprecated
    public static final Charset UTF_16 = Charset.forName(CharEncoding.UTF_16);

    /**
     * Sixteen-bit Unicode Transformation Format, big-endian byte order.
     * <p>
     * Every implementation of the Java platform is required to support this character encoding.
     *
     * @see <a href="http://docs.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @deprecated Use Java 7's {@link java.nio.charset.StandardCharsets}
     */
    @Deprecated
    public static final Charset UTF_16BE = Charset.forName(CharEncoding.UTF_16BE);

    /**
     * Sixteen-bit Unicode Transformation Format, little-endian byte order.
     * <p>
     * Every implementation of the Java platform is required to support this character encoding.
     *
     * @see <a href="http://docs.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @deprecated Use Java 7's {@link java.nio.charset.StandardCharsets}
     */
    @Deprecated
    public static final Charset UTF_16LE = Charset.forName(CharEncoding.UTF_16LE);

    /**
     * Eight-bit Unicode Transformation Format.
     * <p>
     * Every implementation of the Java platform is required to support this character encoding.
     *
     * @see <a href="http://docs.oracle.com/javase/6/docs/api/java/nio/charset/Charset.html">Standard charsets</a>
     * @deprecated Use Java 7's {@link java.nio.charset.StandardCharsets}
     */
    @Deprecated
    public static final Charset UTF_8 = Charset.forName(CharEncoding.UTF_8);
}
