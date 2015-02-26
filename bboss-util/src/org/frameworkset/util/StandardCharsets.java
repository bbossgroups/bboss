package org.frameworkset.util;

import java.nio.charset.Charset;

public class StandardCharsets {
	 private StandardCharsets() {
	        throw new AssertionError("No java.nio.charset.StandardCharsets instances for you!");
	    }
	    /**
	     * Seven-bit ASCII, a.k.a. ISO646-US, a.k.a. the Basic Latin block of the
	     * Unicode character set
	     */
	    public static final Charset US_ASCII = Charset.forName("US-ASCII");
	    /**
	     * ISO Latin Alphabet No. 1, a.k.a. ISO-LATIN-1
	     */
	    public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
	    /**
	     * Eight-bit UCS Transformation Format
	     */
	    public static final Charset UTF_8 = Charset.forName("UTF-8");
	    /**
	     * Sixteen-bit UCS Transformation Format, big-endian byte order
	     */
	    public static final Charset UTF_16BE = Charset.forName("UTF-16BE");
	    /**
	     * Sixteen-bit UCS Transformation Format, little-endian byte order
	     */
	    public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
	    /**
	     * Sixteen-bit UCS Transformation Format, byte order identified by an
	     * optional byte-order mark
	     */
	    public static final Charset UTF_16 = Charset.forName("UTF-16");
}
