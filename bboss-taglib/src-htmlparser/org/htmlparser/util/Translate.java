// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/util/Translate.java,v $
// $Author: derrickoswald $
// $Date: 2004/07/31 16:42:33 $
// $Revision: 1.46 $
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

package org.htmlparser.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.htmlparser.util.sort.Sort;

/**
 * Extended character entity reference.
 * Handles kernels within other strings, just for lookup purposes.
 */
class CharacterReferenceEx extends CharacterReference
{
    /**
     * The starting point in the string.
     */
    protected int mStart;

    /**
     * The ending point in the string.
     */
    protected int mEnd;

    /**
     * Zero args constructor.
     * This object is only ever used after setting the kernel, start and end.
     */
    public CharacterReferenceEx ()
    {
        super ("", 0);
    }

    /**
     * Set the starting point of the kernel.
     */
    public void setStart (int start)
    {
        mStart = start;
    }

    /**
     * Set the supposed ending point.
     * This only specifies an upper bound on the kernel length.
     */
    public void setEnd (int end)
    {
        mEnd = end;
    }

    /**
     * Get this CharacterReference's kernel.
     * @return The kernel in the equivalent character entity reference.
     */
    public String getKernel ()
    {
        return (mKernel.substring (mStart, mEnd));
    }

    //
    // Ordered interface
    //

    /**
     * Compare one reference to another.
     * @see org.htmlparser.util.sort.Ordered
     */
    public int compare (Object that)
    {
        CharacterReference r;
        String kernel;
        int length;
        int ret;

        ret = 0;
        r = (CharacterReference)that;
        kernel = r.getKernel ();
        length = kernel.length ();
        for (int i = mStart, j = 0; i < mEnd; i++, j++)
        {
            if (j >= length)
            {
                ret = 1;
                break;
            }
            ret = mKernel.charAt (i) - kernel.charAt (j);
            if (0 != ret)
                break;
        }

        return (ret);
    }
}

/**
 * Translate numeric character references and character entity references to unicode characters.
 * Based on tables found at <a href="http://www.w3.org/TR/REC-html40/sgml/entities.html">
 * http://www.w3.org/TR/REC-html40/sgml/entities.html</a>
 * <p>Typical usage:
 * <pre>
 *      String s = Translate.decode (getTextFromHtmlPage ());
 * </pre>
 * or
 * <pre>
 *      String s = "&lt;HTML&gt;" + Translate.encode (getArbitraryText ()) + "&lt;/HTML&gt;";
 * </pre>
 */
public class Translate
{
    /**
     * If this member is set <code>true</code>, decoding of streams is
     * done line by line in order to reduce the maximum memory required.
     */
    static public boolean DECODE_LINE_BY_LINE = false;

    /**
     * If this member is set <code>true</code>, encoding of numeric character
     * references uses hexadecimal digits, i.e. &amp;#x25CB;, instead of decimal
     * digits.
     */
    static public boolean ENCODE_HEXADECIMAL = false;

    /**
     * Table mapping entity reference kernel to character.
     * This is sorted by kernel when the class is loaded.
     */
    protected static final CharacterReference[] mCharacterReferences =
    {
        // Portions © International Organization for Standardization 1986
        // Permission to copy in any form is granted for use with
        // conforming SGML systems and applications as defined in
        // ISO 8879, provided this notice is included in all copies.
        // Character entity set. Typical invocation:
        // <!ENTITY % HTMLlat1 PUBLIC
        // "-//W3C//ENTITIES Latin 1//EN//HTML">
        // %HTMLlat1;
        new CharacterReference ("nbsp",     '\u00a0'), // no-break space = non-breaking space, U+00A0 ISOnum
        new CharacterReference ("iexcl",    '\u00a1'), // inverted exclamation mark, U+00A1 ISOnum
        new CharacterReference ("cent",     '\u00a2'), // cent sign, U+00A2 ISOnum
        new CharacterReference ("pound",    '\u00a3'), // pound sign, U+00A3 ISOnum
        new CharacterReference ("curren",   '\u00a4'), // currency sign, U+00A4 ISOnum
        new CharacterReference ("yen",      '\u00a5'), // yen sign = yuan sign, U+00A5 ISOnum
        new CharacterReference ("brvbar",   '\u00a6'), // broken bar = broken vertical bar, U+00A6 ISOnum
        new CharacterReference ("sect",     '\u00a7'), // section sign, U+00A7 ISOnum
        new CharacterReference ("uml",      '\u00a8'), // diaeresis = spacing diaeresis, U+00A8 ISOdia
        new CharacterReference ("copy",     '\u00a9'), // copyright sign, U+00A9 ISOnum
        new CharacterReference ("ordf",     '\u00aa'), // feminine ordinal indicator, U+00AA ISOnum
        new CharacterReference ("laquo",    '\u00ab'), // left-pointing double angle quotation mark = left pointing guillemet, U+00AB ISOnum
        new CharacterReference ("not",      '\u00ac'), // not sign, U+00AC ISOnum
        new CharacterReference ("shy",      '\u00ad'), // soft hyphen = discretionary hyphen, U+00AD ISOnum
        new CharacterReference ("reg",      '\u00ae'), // registered sign = registered trade mark sign, U+00AE ISOnum
        new CharacterReference ("macr",     '\u00af'), // macron = spacing macron = overline = APL overbar, U+00AF ISOdia
        new CharacterReference ("deg",      '\u00b0'), // degree sign, U+00B0 ISOnum
        new CharacterReference ("plusmn",   '\u00b1'), // plus-minus sign = plus-or-minus sign, U+00B1 ISOnum
        new CharacterReference ("sup2",     '\u00b2'), // superscript two = superscript digit two = squared, U+00B2 ISOnum
        new CharacterReference ("sup3",     '\u00b3'), // superscript three = superscript digit three = cubed, U+00B3 ISOnum
        new CharacterReference ("acute",    '\u00b4'), // acute accent = spacing acute, U+00B4 ISOdia
        new CharacterReference ("micro",    '\u00b5'), // micro sign, U+00B5 ISOnum
        new CharacterReference ("para",     '\u00b6'), // pilcrow sign = paragraph sign, U+00B6 ISOnum
        new CharacterReference ("middot",   '\u00b7'), // middle dot = Georgian comma = Greek middle dot, U+00B7 ISOnum
        new CharacterReference ("cedil",    '\u00b8'), // cedilla = spacing cedilla, U+00B8 ISOdia
        new CharacterReference ("sup1",     '\u00b9'), // superscript one = superscript digit one, U+00B9 ISOnum
        new CharacterReference ("ordm",     '\u00ba'), // masculine ordinal indicator, U+00BA ISOnum
        new CharacterReference ("raquo",    '\u00bb'), // right-pointing double angle quotation mark = right pointing guillemet, U+00BB ISOnum
        new CharacterReference ("frac14",   '\u00bc'), // vulgar fraction one quarter = fraction one quarter, U+00BC ISOnum
        new CharacterReference ("frac12",   '\u00bd'), // vulgar fraction one half = fraction one half, U+00BD ISOnum
        new CharacterReference ("frac34",   '\u00be'), // vulgar fraction three quarters = fraction three quarters, U+00BE ISOnum
        new CharacterReference ("iquest",   '\u00bf'), // inverted question mark = turned question mark, U+00BF ISOnum
        new CharacterReference ("Agrave",   '\u00c0'), // latin capital letter A with grave = latin capital letter A grave, U+00C0 ISOlat1
        new CharacterReference ("Aacute",   '\u00c1'), // latin capital letter A with acute, U+00C1 ISOlat1
        new CharacterReference ("Acirc",    '\u00c2'), // latin capital letter A with circumflex, U+00C2 ISOlat1
        new CharacterReference ("Atilde",   '\u00c3'), // latin capital letter A with tilde, U+00C3 ISOlat1
        new CharacterReference ("Auml",     '\u00c4'), // latin capital letter A with diaeresis, U+00C4 ISOlat1
        new CharacterReference ("Aring",    '\u00c5'), // latin capital letter A with ring above = latin capital letter A ring, U+00C5 ISOlat1
        new CharacterReference ("AElig",    '\u00c6'), // latin capital letter AE = latin capital ligature AE, U+00C6 ISOlat1
        new CharacterReference ("Ccedil",   '\u00c7'), // latin capital letter C with cedilla, U+00C7 ISOlat1
        new CharacterReference ("Egrave",   '\u00c8'), // latin capital letter E with grave, U+00C8 ISOlat1
        new CharacterReference ("Eacute",   '\u00c9'), // latin capital letter E with acute, U+00C9 ISOlat1
        new CharacterReference ("Ecirc",    '\u00ca'), // latin capital letter E with circumflex, U+00CA ISOlat1
        new CharacterReference ("Euml",     '\u00cb'), // latin capital letter E with diaeresis, U+00CB ISOlat1
        new CharacterReference ("Igrave",   '\u00cc'), // latin capital letter I with grave, U+00CC ISOlat1
        new CharacterReference ("Iacute",   '\u00cd'), // latin capital letter I with acute, U+00CD ISOlat1
        new CharacterReference ("Icirc",    '\u00ce'), // latin capital letter I with circumflex, U+00CE ISOlat1
        new CharacterReference ("Iuml",     '\u00cf'), // latin capital letter I with diaeresis, U+00CF ISOlat1
        new CharacterReference ("ETH",      '\u00d0'), // latin capital letter ETH, U+00D0 ISOlat1
        new CharacterReference ("Ntilde",   '\u00d1'), // latin capital letter N with tilde, U+00D1 ISOlat1
        new CharacterReference ("Ograve",   '\u00d2'), // latin capital letter O with grave, U+00D2 ISOlat1
        new CharacterReference ("Oacute",   '\u00d3'), // latin capital letter O with acute, U+00D3 ISOlat1
        new CharacterReference ("Ocirc",    '\u00d4'), // latin capital letter O with circumflex, U+00D4 ISOlat1
        new CharacterReference ("Otilde",   '\u00d5'), // latin capital letter O with tilde, U+00D5 ISOlat1
        new CharacterReference ("Ouml",     '\u00d6'), // latin capital letter O with diaeresis, U+00D6 ISOlat1
        new CharacterReference ("times",    '\u00d7'), // multiplication sign, U+00D7 ISOnum
        new CharacterReference ("Oslash",   '\u00d8'), // latin capital letter O with stroke = latin capital letter O slash, U+00D8 ISOlat1
        new CharacterReference ("Ugrave",   '\u00d9'), // latin capital letter U with grave, U+00D9 ISOlat1
        new CharacterReference ("Uacute",   '\u00da'), // latin capital letter U with acute, U+00DA ISOlat1
        new CharacterReference ("Ucirc",    '\u00db'), // latin capital letter U with circumflex, U+00DB ISOlat1
        new CharacterReference ("Uuml",     '\u00dc'), // latin capital letter U with diaeresis, U+00DC ISOlat1
        new CharacterReference ("Yacute",   '\u00dd'), // latin capital letter Y with acute, U+00DD ISOlat1
        new CharacterReference ("THORN",    '\u00de'), // latin capital letter THORN, U+00DE ISOlat1
        new CharacterReference ("szlig",    '\u00df'), // latin small letter sharp s = ess-zed, U+00DF ISOlat1
        new CharacterReference ("agrave",   '\u00e0'), // latin small letter a with grave = latin small letter a grave, U+00E0 ISOlat1
        new CharacterReference ("aacute",   '\u00e1'), // latin small letter a with acute, U+00E1 ISOlat1
        new CharacterReference ("acirc",    '\u00e2'), // latin small letter a with circumflex, U+00E2 ISOlat1
        new CharacterReference ("atilde",   '\u00e3'), // latin small letter a with tilde, U+00E3 ISOlat1
        new CharacterReference ("auml",     '\u00e4'), // latin small letter a with diaeresis, U+00E4 ISOlat1
        new CharacterReference ("aring",    '\u00e5'), // latin small letter a with ring above = latin small letter a ring, U+00E5 ISOlat1
        new CharacterReference ("aelig",    '\u00e6'), // latin small letter ae = latin small ligature ae, U+00E6 ISOlat1
        new CharacterReference ("ccedil",   '\u00e7'), // latin small letter c with cedilla, U+00E7 ISOlat1
        new CharacterReference ("egrave",   '\u00e8'), // latin small letter e with grave, U+00E8 ISOlat1
        new CharacterReference ("eacute",   '\u00e9'), // latin small letter e with acute, U+00E9 ISOlat1
        new CharacterReference ("ecirc",    '\u00ea'), // latin small letter e with circumflex, U+00EA ISOlat1
        new CharacterReference ("euml",     '\u00eb'), // latin small letter e with diaeresis, U+00EB ISOlat1
        new CharacterReference ("igrave",   '\u00ec'), // latin small letter i with grave, U+00EC ISOlat1
        new CharacterReference ("iacute",   '\u00ed'), // latin small letter i with acute, U+00ED ISOlat1
        new CharacterReference ("icirc",    '\u00ee'), // latin small letter i with circumflex, U+00EE ISOlat1
        new CharacterReference ("iuml",     '\u00ef'), // latin small letter i with diaeresis, U+00EF ISOlat1
        new CharacterReference ("eth",      '\u00f0'), // latin small letter eth, U+00F0 ISOlat1
        new CharacterReference ("ntilde",   '\u00f1'), // latin small letter n with tilde, U+00F1 ISOlat1
        new CharacterReference ("ograve",   '\u00f2'), // latin small letter o with grave, U+00F2 ISOlat1
        new CharacterReference ("oacute",   '\u00f3'), // latin small letter o with acute, U+00F3 ISOlat1
        new CharacterReference ("ocirc",    '\u00f4'), // latin small letter o with circumflex, U+00F4 ISOlat1
        new CharacterReference ("otilde",   '\u00f5'), // latin small letter o with tilde, U+00F5 ISOlat1
        new CharacterReference ("ouml",     '\u00f6'), // latin small letter o with diaeresis, U+00F6 ISOlat1
        new CharacterReference ("divide",   '\u00f7'), // division sign, U+00F7 ISOnum
        new CharacterReference ("oslash",   '\u00f8'), // latin small letter o with stroke, = latin small letter o slash, U+00F8 ISOlat1
        new CharacterReference ("ugrave",   '\u00f9'), // latin small letter u with grave, U+00F9 ISOlat1
        new CharacterReference ("uacute",   '\u00fa'), // latin small letter u with acute, U+00FA ISOlat1
        new CharacterReference ("ucirc",    '\u00fb'), // latin small letter u with circumflex, U+00FB ISOlat1
        new CharacterReference ("uuml",     '\u00fc'), // latin small letter u with diaeresis, U+00FC ISOlat1
        new CharacterReference ("yacute",   '\u00fd'), // latin small letter y with acute, U+00FD ISOlat1
        new CharacterReference ("thorn",    '\u00fe'), // latin small letter thorn, U+00FE ISOlat1
        new CharacterReference ("yuml",     '\u00ff'), // latin small letter y with diaeresis, U+00FF ISOlat1
        // Mathematical, Greek and Symbolic characters for HTML
        // Character entity set. Typical invocation:
        // <!ENTITY % HTMLsymbol PUBLIC
        // "-//W3C//ENTITIES Symbols//EN//HTML">
        // %HTMLsymbol;
        // Portions © International Organization for Standardization 1986:
        // Permission to copy in any form is granted for use with
        // conforming SGML systems and applications as defined in
        // ISO 8879, provided this notice is included in all copies.
        // Relevant ISO entity set is given unless names are newly introduced.
        // New names (i.e., not in ISO 8879 list) do not clash with any
        // existing ISO 8879 entity names. ISO 10646 character numbers
        // are given for each character, in hex. CDATA values are decimal
        // conversions of the ISO 10646 values and refer to the document
        // character set. Names are ISO 10646 names.
        // Latin Extended-B
        new CharacterReference ("fnof",     '\u0192'), // latin small f with hook = function = florin, U+0192 ISOtech
        // Greek
        new CharacterReference ("Alpha",    '\u0391'), // greek capital letter alpha, U+0391
        new CharacterReference ("Beta",     '\u0392'), // greek capital letter beta, U+0392
        new CharacterReference ("Gamma",    '\u0393'), // greek capital letter gamma, U+0393 ISOgrk3
        new CharacterReference ("Delta",    '\u0394'), // greek capital letter delta, U+0394 ISOgrk3
        new CharacterReference ("Epsilon",  '\u0395'), // greek capital letter epsilon, U+0395
        new CharacterReference ("Zeta",     '\u0396'), // greek capital letter zeta, U+0396
        new CharacterReference ("Eta",      '\u0397'), // greek capital letter eta, U+0397
        new CharacterReference ("Theta",    '\u0398'), // greek capital letter theta, U+0398 ISOgrk3
        new CharacterReference ("Iota",     '\u0399'), // greek capital letter iota, U+0399
        new CharacterReference ("Kappa",    '\u039a'), // greek capital letter kappa, U+039A
        new CharacterReference ("Lambda",   '\u039b'), // greek capital letter lambda, U+039B ISOgrk3
        new CharacterReference ("Mu",       '\u039c'), // greek capital letter mu, U+039C
        new CharacterReference ("Nu",       '\u039d'), // greek capital letter nu, U+039D
        new CharacterReference ("Xi",       '\u039e'), // greek capital letter xi, U+039E ISOgrk3
        new CharacterReference ("Omicron",  '\u039f'), // greek capital letter omicron, U+039F
        new CharacterReference ("Pi",       '\u03a0'), // greek capital letter pi, U+03A0 ISOgrk3
        new CharacterReference ("Rho",      '\u03a1'), // greek capital letter rho, U+03A1
        // there is no Sigmaf, and no U+03A2 character either
        new CharacterReference ("Sigma",    '\u03a3'), // greek capital letter sigma, U+03A3 ISOgrk3
        new CharacterReference ("Tau",      '\u03a4'), // greek capital letter tau, U+03A4
        new CharacterReference ("Upsilon",  '\u03a5'), // greek capital letter upsilon, U+03A5 ISOgrk3
        new CharacterReference ("Phi",      '\u03a6'), // greek capital letter phi, U+03A6 ISOgrk3
        new CharacterReference ("Chi",      '\u03a7'), // greek capital letter chi, U+03A7
        new CharacterReference ("Psi",      '\u03a8'), // greek capital letter psi, U+03A8 ISOgrk3
        new CharacterReference ("Omega",    '\u03a9'), // greek capital letter omega, U+03A9 ISOgrk3
        new CharacterReference ("alpha",    '\u03b1'), // greek small letter alpha, U+03B1 ISOgrk3
        new CharacterReference ("beta",     '\u03b2'), // greek small letter beta, U+03B2 ISOgrk3
        new CharacterReference ("gamma",    '\u03b3'), // greek small letter gamma, U+03B3 ISOgrk3
        new CharacterReference ("delta",    '\u03b4'), // greek small letter delta, U+03B4 ISOgrk3
        new CharacterReference ("epsilon",  '\u03b5'), // greek small letter epsilon, U+03B5 ISOgrk3
        new CharacterReference ("zeta",     '\u03b6'), // greek small letter zeta, U+03B6 ISOgrk3
        new CharacterReference ("eta",      '\u03b7'), // greek small letter eta, U+03B7 ISOgrk3
        new CharacterReference ("theta",    '\u03b8'), // greek small letter theta, U+03B8 ISOgrk3
        new CharacterReference ("iota",     '\u03b9'), // greek small letter iota, U+03B9 ISOgrk3
        new CharacterReference ("kappa",    '\u03ba'), // greek small letter kappa, U+03BA ISOgrk3
        new CharacterReference ("lambda",   '\u03bb'), // greek small letter lambda, U+03BB ISOgrk3
        new CharacterReference ("mu",       '\u03bc'), // greek small letter mu, U+03BC ISOgrk3
        new CharacterReference ("nu",       '\u03bd'), // greek small letter nu, U+03BD ISOgrk3
        new CharacterReference ("xi",       '\u03be'), // greek small letter xi, U+03BE ISOgrk3
        new CharacterReference ("omicron",  '\u03bf'), // greek small letter omicron, U+03BF NEW
        new CharacterReference ("pi",       '\u03c0'), // greek small letter pi, U+03C0 ISOgrk3
        new CharacterReference ("rho",      '\u03c1'), // greek small letter rho, U+03C1 ISOgrk3
        new CharacterReference ("sigmaf",   '\u03c2'), // greek small letter final sigma, U+03C2 ISOgrk3
        new CharacterReference ("sigma",    '\u03c3'), // greek small letter sigma, U+03C3 ISOgrk3
        new CharacterReference ("tau",      '\u03c4'), // greek small letter tau, U+03C4 ISOgrk3
        new CharacterReference ("upsilon",  '\u03c5'), // greek small letter upsilon, U+03C5 ISOgrk3
        new CharacterReference ("phi",      '\u03c6'), // greek small letter phi, U+03C6 ISOgrk3
        new CharacterReference ("chi",      '\u03c7'), // greek small letter chi, U+03C7 ISOgrk3
        new CharacterReference ("psi",      '\u03c8'), // greek small letter psi, U+03C8 ISOgrk3
        new CharacterReference ("omega",    '\u03c9'), // greek small letter omega, U+03C9 ISOgrk3
        new CharacterReference ("thetasym", '\u03d1'), // greek small letter theta symbol, U+03D1 NEW
        new CharacterReference ("upsih",    '\u03d2'), // greek upsilon with hook symbol, U+03D2 NEW
        new CharacterReference ("piv",      '\u03d6'), // greek pi symbol, U+03D6 ISOgrk3
        // General Punctuation
        new CharacterReference ("bull",     '\u2022'), // bullet = black small circle, U+2022 ISOpub
        // bullet is NOT the same as bullet operator, U+2219
        new CharacterReference ("hellip",   '\u2026'), // horizontal ellipsis = three dot leader, U+2026 ISOpub
        new CharacterReference ("prime",    '\u2032'), // prime = minutes = feet, U+2032 ISOtech
        new CharacterReference ("Prime",    '\u2033'), // double prime = seconds = inches, U+2033 ISOtech
        new CharacterReference ("oline",    '\u203e'), // overline = spacing overscore, U+203E NEW
        new CharacterReference ("frasl",    '\u2044'), // fraction slash, U+2044 NEW
        // Letterlike Symbols
        new CharacterReference ("weierp",   '\u2118'), // script capital P = power set = Weierstrass p, U+2118 ISOamso
        new CharacterReference ("image",    '\u2111'), // blackletter capital I = imaginary part, U+2111 ISOamso
        new CharacterReference ("real",     '\u211c'), // blackletter capital R = real part symbol, U+211C ISOamso
        new CharacterReference ("trade",    '\u2122'), // trade mark sign, U+2122 ISOnum
        new CharacterReference ("alefsym",  '\u2135'), // alef symbol = first transfinite cardinal, U+2135 NEW
        // alef symbol is NOT the same as hebrew letter alef,
        // U+05D0 although the same glyph could be used to depict both characters
        // Arrows
        new CharacterReference ("larr",     '\u2190'), // leftwards arrow, U+2190 ISOnum
        new CharacterReference ("uarr",     '\u2191'), // upwards arrow, U+2191 ISOnum
        new CharacterReference ("rarr",     '\u2192'), // rightwards arrow, U+2192 ISOnum
        new CharacterReference ("darr",     '\u2193'), // downwards arrow, U+2193 ISOnum
        new CharacterReference ("harr",     '\u2194'), // left right arrow, U+2194 ISOamsa
        new CharacterReference ("crarr",    '\u21b5'), // downwards arrow with corner leftwards = carriage return, U+21B5 NEW
        new CharacterReference ("lArr",     '\u21d0'), // leftwards double arrow, U+21D0 ISOtech
        // ISO 10646 does not say that lArr is the same as the 'is implied by' arrow
        // but also does not have any other character for that function. So ? lArr can
        // be used for 'is implied by' as ISOtech suggests
        new CharacterReference ("uArr",     '\u21d1'), // upwards double arrow, U+21D1 ISOamsa
        new CharacterReference ("rArr",     '\u21d2'), // rightwards double arrow, U+21D2 ISOtech
        // ISO 10646 does not say this is the 'implies' character but does not have 
        // another character with this function so ?
        // rArr can be used for 'implies' as ISOtech suggests
        new CharacterReference ("dArr",     '\u21d3'), // downwards double arrow, U+21D3 ISOamsa
        new CharacterReference ("hArr",     '\u21d4'), // left right double arrow, U+21D4 ISOamsa
        // Mathematical Operators
        new CharacterReference ("forall",   '\u2200'), // for all, U+2200 ISOtech
        new CharacterReference ("part",     '\u2202'), // partial differential, U+2202 ISOtech
        new CharacterReference ("exist",    '\u2203'), // there exists, U+2203 ISOtech
        new CharacterReference ("empty",    '\u2205'), // empty set = null set = diameter, U+2205 ISOamso
        new CharacterReference ("nabla",    '\u2207'), // nabla = backward difference, U+2207 ISOtech
        new CharacterReference ("isin",     '\u2208'), // element of, U+2208 ISOtech
        new CharacterReference ("notin",    '\u2209'), // not an element of, U+2209 ISOtech
        new CharacterReference ("ni",       '\u220b'), // contains as member, U+220B ISOtech
        // should there be a more memorable name than 'ni'?
        new CharacterReference ("prod",     '\u220f'), // n-ary product = product sign, U+220F ISOamsb
        // prod is NOT the same character as U+03A0 'greek capital letter pi' though
        // the same glyph might be used for both
        new CharacterReference ("sum",      '\u2211'), // n-ary sumation, U+2211 ISOamsb
        // sum is NOT the same character as U+03A3 'greek capital letter sigma'
        // though the same glyph might be used for both
        new CharacterReference ("minus",    '\u2212'), // minus sign, U+2212 ISOtech
        new CharacterReference ("lowast",   '\u2217'), // asterisk operator, U+2217 ISOtech
        new CharacterReference ("radic",    '\u221a'), // square root = radical sign, U+221A ISOtech
        new CharacterReference ("prop",     '\u221d'), // proportional to, U+221D ISOtech
        new CharacterReference ("infin",    '\u221e'), // infinity, U+221E ISOtech
        new CharacterReference ("ang",      '\u2220'), // angle, U+2220 ISOamso
        new CharacterReference ("and",      '\u2227'), // logical and = wedge, U+2227 ISOtech
        new CharacterReference ("or",       '\u2228'), // logical or = vee, U+2228 ISOtech
        new CharacterReference ("cap",      '\u2229'), // intersection = cap, U+2229 ISOtech
        new CharacterReference ("cup",      '\u222a'), // union = cup, U+222A ISOtech
        new CharacterReference ("int",      '\u222b'), // integral, U+222B ISOtech
        new CharacterReference ("there4",   '\u2234'), // therefore, U+2234 ISOtech
        new CharacterReference ("sim",      '\u223c'), // tilde operator = varies with = similar to, U+223C ISOtech
        // tilde operator is NOT the same character as the tilde, U+007E,
        // although the same glyph might be used to represent both
        new CharacterReference ("cong",     '\u2245'), // approximately equal to, U+2245 ISOtech
        new CharacterReference ("asymp",    '\u2248'), // almost equal to = asymptotic to, U+2248 ISOamsr
        new CharacterReference ("ne",       '\u2260'), // not equal to, U+2260 ISOtech
        new CharacterReference ("equiv",    '\u2261'), // identical to, U+2261 ISOtech
        new CharacterReference ("le",       '\u2264'), // less-than or equal to, U+2264 ISOtech
        new CharacterReference ("ge",       '\u2265'), // greater-than or equal to, U+2265 ISOtech
        new CharacterReference ("sub",      '\u2282'), // subset of, U+2282 ISOtech
        new CharacterReference ("sup",      '\u2283'), // superset of, U+2283 ISOtech
        // note that nsup, 'not a superset of, U+2283' is not covered by the Symbol 
        // font encoding and is not included. Should it be, for symmetry?
        // It is in ISOamsn
        new CharacterReference ("nsub",     '\u2284'), // not a subset of, U+2284 ISOamsn
        new CharacterReference ("sube",     '\u2286'), // subset of or equal to, U+2286 ISOtech
        new CharacterReference ("supe",     '\u2287'), // superset of or equal to, U+2287 ISOtech
        new CharacterReference ("oplus",    '\u2295'), // circled plus = direct sum, U+2295 ISOamsb
        new CharacterReference ("otimes",   '\u2297'), // circled times = vector product, U+2297 ISOamsb
        new CharacterReference ("perp",     '\u22a5'), // up tack = orthogonal to = perpendicular, U+22A5 ISOtech
        new CharacterReference ("sdot",     '\u22c5'), // dot operator, U+22C5 ISOamsb
        // dot operator is NOT the same character as U+00B7 middle dot
        // Miscellaneous Technical
        new CharacterReference ("lceil",    '\u2308'), // left ceiling = apl upstile, U+2308 ISOamsc
        new CharacterReference ("rceil",    '\u2309'), // right ceiling, U+2309 ISOamsc
        new CharacterReference ("lfloor",   '\u230a'), // left floor = apl downstile, U+230A ISOamsc
        new CharacterReference ("rfloor",   '\u230b'), // right floor, U+230B ISOamsc
        new CharacterReference ("lang",     '\u2329'), // left-pointing angle bracket = bra, U+2329 ISOtech
        // lang is NOT the same character as U+003C 'less than' 
        // or U+2039 'single left-pointing angle quotation mark'
        new CharacterReference ("rang",     '\u232a'), // right-pointing angle bracket = ket, U+232A ISOtech
        // rang is NOT the same character as U+003E 'greater than' 
        // or U+203A 'single right-pointing angle quotation mark'
        // Geometric Shapes
        new CharacterReference ("loz",      '\u25ca'), // lozenge, U+25CA ISOpub
        // Miscellaneous Symbols
        new CharacterReference ("spades",   '\u2660'), // black spade suit, U+2660 ISOpub
        // black here seems to mean filled as opposed to hollow
        new CharacterReference ("clubs",    '\u2663'), // black club suit = shamrock, U+2663 ISOpub
        new CharacterReference ("hearts",   '\u2665'), // black heart suit = valentine, U+2665 ISOpub
        new CharacterReference ("diams",    '\u2666'), // black diamond suit, U+2666 ISOpub
        // Special characters for HTML
        // Character entity set. Typical invocation:
        // <!ENTITY % HTMLspecial PUBLIC
        // "-//W3C//ENTITIES Special//EN//HTML">
        // %HTMLspecial;
        // Portions © International Organization for Standardization 1986:
        // Permission to copy in any form is granted for use with
        // conforming SGML systems and applications as defined in
        // ISO 8879, provided this notice is included in all copies.
        // Relevant ISO entity set is given unless names are newly introduced.
        // New names (i.e., not in ISO 8879 list) do not clash with any
        // existing ISO 8879 entity names. ISO 10646 character numbers
        // are given for each character, in hex. CDATA values are decimal
        // conversions of the ISO 10646 values and refer to the document
        // character set. Names are ISO 10646 names.
        // C0 Controls and Basic Latin
        new CharacterReference ("quot",     '\u0022'), // quotation mark = APL quote, U+0022 ISOnum
        new CharacterReference ("amp",      '\u0026'), // ampersand, U+0026 ISOnum
        new CharacterReference ("lt",       '\u003c'), // less-than sign, U+003C ISOnum
        new CharacterReference ("gt",       '\u003e'), // greater-than sign, U+003E ISOnum
        // Latin Extended-A
        new CharacterReference ("OElig",    '\u0152'), // latin capital ligature OE, U+0152 ISOlat2
        new CharacterReference ("oelig",    '\u0153'), // latin small ligature oe, U+0153 ISOlat2
        // ligature is a misnomer, this is a separate character in some languages
        new CharacterReference ("Scaron",   '\u0160'), // latin capital letter S with caron, U+0160 ISOlat2
        new CharacterReference ("scaron",   '\u0161'), // latin small letter s with caron, U+0161 ISOlat2
        new CharacterReference ("Yuml",     '\u0178'), // latin capital letter Y with diaeresis, U+0178 ISOlat2
        // Spacing Modifier Letters
        new CharacterReference ("circ",     '\u02c6'), // modifier letter circumflex accent, U+02C6 ISOpub
        new CharacterReference ("tilde",    '\u02dc'), // small tilde, U+02DC ISOdia
        // General Punctuation
        new CharacterReference ("ensp",     '\u2002'), // en space, U+2002 ISOpub
        new CharacterReference ("emsp",     '\u2003'), // em space, U+2003 ISOpub
        new CharacterReference ("thinsp",   '\u2009'), // thin space, U+2009 ISOpub
        new CharacterReference ("zwnj",     '\u200c'), // zero width non-joiner, U+200C NEW RFC 2070
        new CharacterReference ("zwj",      '\u200d'), // zero width joiner, U+200D NEW RFC 2070
        new CharacterReference ("lrm",      '\u200e'), // left-to-right mark, U+200E NEW RFC 2070
        new CharacterReference ("rlm",      '\u200f'), // right-to-left mark, U+200F NEW RFC 2070
        new CharacterReference ("ndash",    '\u2013'), // en dash, U+2013 ISOpub
        new CharacterReference ("mdash",    '\u2014'), // em dash, U+2014 ISOpub
        new CharacterReference ("lsquo",    '\u2018'), // left single quotation mark, U+2018 ISOnum
        new CharacterReference ("rsquo",    '\u2019'), // right single quotation mark, U+2019 ISOnum
        new CharacterReference ("sbquo",    '\u201a'), // single low-9 quotation mark, U+201A NEW
        new CharacterReference ("ldquo",    '\u201c'), // left double quotation mark, U+201C ISOnum
        new CharacterReference ("rdquo",    '\u201d'), // right double quotation mark, U+201D ISOnum
        new CharacterReference ("bdquo",    '\u201e'), // double low-9 quotation mark, U+201E NEW
        new CharacterReference ("dagger",   '\u2020'), // dagger, U+2020 ISOpub
        new CharacterReference ("Dagger",   '\u2021'), // double dagger, U+2021 ISOpub
        new CharacterReference ("permil",   '\u2030'), // per mille sign, U+2030 ISOtech
        new CharacterReference ("lsaquo",   '\u2039'), // single left-pointing angle quotation mark, U+2039 ISO proposed
        // lsaquo is proposed but not yet ISO standardized
        new CharacterReference ("rsaquo",   '\u203a'), // single right-pointing angle quotation mark, U+203A ISO proposed
        // rsaquo is proposed but not yet ISO standardized
        new CharacterReference ("euro",     '\u20ac'), // euro sign, U+20AC NEW
    };

    /**
     * The dividing point between a simple table lookup and a binary search.
     * Characters below the break point are stored in a sparse array allowing
     * direct index lookup.
     */
    protected static final int BREAKPOINT = 0x100;

    /**
     * List of references sorted by character.
     * The first part of this array, up to <code>BREAKPOINT</code> is stored
     * in a direct translational table, indexing into the table with a character
     * yields the reference. The second part is dense and sorted by character,
     * suitable for binary lookup.
     */
    protected static final CharacterReference[] mCharacterList;

    static
    {
        int index;
        CharacterReference item;
        int character;

        // count below the break point
        index = 0;
        for (int i = 0; i < mCharacterReferences.length; i++)
            if (mCharacterReferences[i].getCharacter () < BREAKPOINT)
                index++;
        // allocate enough for the linear table and remainder
        mCharacterList = new CharacterReference[BREAKPOINT + mCharacterReferences.length - index];
        index = BREAKPOINT;
        for (int i = 0; i < mCharacterReferences.length; i++)
        {
            item = mCharacterReferences[i];
            character = mCharacterReferences[i].getCharacter ();
            if (character < BREAKPOINT)
                mCharacterList[character] = item;
            else
            {
                // use a linear search and insertion sort, done only once
                int x = BREAKPOINT;
                while (x < index)
                    if (mCharacterList[x].getCharacter () > character)
                        break;
                    else
                        x++;
                int y = index - 1;
                while (y >= x)
                {
                    mCharacterList[y + 1] = mCharacterList[y];
                    y--;
                }
                mCharacterList[x] = item;
                index++;
            }
        }
        // reorder the original array into kernel order
        Sort.QuickSort (mCharacterReferences);
    }

    /**
     * Private constructor.
     * This class is fully static and thread safe.
     */
    private Translate ()
    {
    }

    /**
     * Binary search for a reference.
     * @param array The array of <code>CharacterReference</code> objects.
     * @param ref The character to search for.
     * @param lo The lower index within which to look.
     * @param hi The upper index within which to look.
     * @return The index at which reference was found or is to be inserted.
     */
    protected static int lookup (CharacterReference[] array, char ref, int lo, int hi)
    {   int num;
        int mid;
        int half;
        int result;
        int ret;

        ret = -1;

        num = (hi - lo) + 1;
        while ((-1 == ret) && (lo <= hi))
        {
            half = num / 2;
            mid = lo + ((0 != (num & 1)) ? half : half - 1);
            result = ref - array[mid].getCharacter ();
            if (0 == result)
                ret = mid;
            else if (0 > result)
            {
                hi = mid - 1;
                num = ((0 != (num & 1)) ? half : half - 1);
            }
            else
            {
                lo = mid + 1;
                num = half;
            }
        }
        if (-1 == ret)
            ret = lo;

        return (ret);
    }

    /**
     * Look up a reference by character.
     * Use a combination of direct table lookup and binary search to find
     * the reference corresponding to the character.
     * @param character The character to be looked up.
     * @return The entity reference for that character or <code>null</code>.
     */
    public static CharacterReference lookup (char character)
    {
        int index;
        CharacterReference ret;

        if (character < BREAKPOINT)
            ret = mCharacterList[character];
        else
        {
            index = lookup (mCharacterList, character, BREAKPOINT, mCharacterList.length - 1);
            if (index < mCharacterList.length)
            {
                ret = mCharacterList[index];
                if (character != ret.getCharacter ())
                    ret = null;
            }
            else
                ret = null;
        }
        
        return (ret);
    }

    /**
     * Look up a reference by kernel.
     * Use a binary search on the ordered list of known references.
     * Since the binary search returns the position at which a new item should
     * be inserted, we check the references earlier in the list if there is
     * a failure.
     * @param key A character reference with the kernel set to the string
     * to be found. It need not be truncated at the exact end of the reference.
     */
    protected static CharacterReference lookup (CharacterReference key)
    {
        String string;
        int index;
        String kernel;
        char character;
        CharacterReference test;
        CharacterReference ret;

        // Care should be taken here because some entity references are
        // prefixes of others, i.e.:
        // \u2209[notin] \u00ac[not]
        // \u00ba[ordm] \u2228[or]
        // \u03d6[piv] \u03c0[pi]
        // \u00b3[sup3] \u2283[sup]
        ret = null;
        index = Sort.bsearch (mCharacterReferences, key);
        string = key.getKernel ();
        if (index < mCharacterReferences.length)
        {
            ret = mCharacterReferences[index];
            kernel = ret.getKernel ();
            if (!string.regionMatches (
                0,
                kernel,
                0,
                kernel.length ()))
            {   // not exact, check references starting with same character
                // to see if a subset matches
                ret = null;
            }
        }
        if (null == ret)
        {
            character = string.charAt (0);
            while (--index >= 0)
            {
                test = mCharacterReferences[index];
                kernel = test.getKernel ();
                if (character == kernel.charAt (0))
                {
                    if (string.regionMatches (
                        0,
                        kernel,
                        0,
                        kernel.length ()))
                    {
                        ret = test;
                        break;
                    }
                }
                else
                    break;
            }
        }
        
        return (ret);
    }

    /**
     * Look up a reference by kernel.
     * Use a binary search on the ordered list of known references.
     * <em>This is not very efficient, use {@link org.htmlparser.util.Translate#lookup(org.htmlparser.util.CharacterReference) lookup(CharacterReference)}
     * instead.</em>
     * @param kernel The string to lookup, i.e. "amp".
     * @param start The starting point in the string of the kernel.
     * @param end The ending point in the string of the kernel.
     * This should be the index of the semicolon if it exists, or failing that,
     * at least an index past the last character of the kernel.
     * @return The reference that matches the given string, or <code>null</code>
     * if it wasn't found.
     */
    public static CharacterReference lookup (String kernel, int start, int end)
    {
        CharacterReferenceEx probe;
        
        probe = new CharacterReferenceEx ();
        probe.setKernel (kernel);
        probe.setStart (start);
        probe.setEnd (end);

        return (lookup (probe));
    }

    /**
     * Convert a reference to a unicode character.
     * Convert a single numeric character reference or character entity reference
     * to a unicode character.
     * @param string The string to convert. Of the form &xxxx; or &amp;#xxxx; with
     * or without the leading ampersand or trailing semi-colon.
     * @param start The starting pooint in the string to look for a character reference.
     * @param end The ending point in the string to stop looking for a character reference.
     * @return The converted character or ' ' (zero) if the string is an
     * invalid reference.
     * @deprecated Use {@link #decode(String) decode}.
     */
    public static char convertToChar (String string, int start, int end)
    {
        return (decode (string.substring (start, end)).charAt (0));
    }

    /**
     * Convert a reference to a unicode character.
     * Convert a single numeric character reference or character entity reference
     * to a unicode character.
     * @param string The string to convert. Of the form &xxxx; or &amp;#xxxx; with
     * or without the leading ampersand or trailing semi-colon.
     * @return The converted character or ' ' (zero) if the string is an
     * invalid reference.
     * @deprecated Use {@link #decode(String) decode}.
     */
    public static char convertToChar (String string)
    {
        return (decode (string).charAt (0));
    }

    /**
     * Decode a string containing references.
     * Change all numeric character reference and character entity references
     * to unicode characters.
     * @param string The string to translate.
     */
    public static String decode (String string)
    {
        CharacterReferenceEx key;
        int amp;
        int index;
        int length;
        StringBuffer buffer;
        char character;
        int number;
        int radix;
        int i;
        int semi;
        boolean done;
        CharacterReference item;
        String ret;

        if (-1 == (amp = string.indexOf ('&')))
            ret = string;
        else
        {
            key = null;
            index = 0;
            length = string.length ();
            buffer = new StringBuffer (length);
            do
            {
                // equivalent to buffer.append (string.substring (index, amp));
                // but without the allocation of a new String
                while (index < amp)
                    buffer.append (string.charAt (index++));
                
                index++;
                if (index < length)
                {
                    character = string.charAt (index);
                    if ('#' == character)
                    {
                        // numeric character reference
                        index++;
                        number = 0;
                        radix = 0;
                        i = index;
                        done = false;
                        while ((i < length) && !done)
                        {
                            character = string.charAt (i);
                            switch (character)
                            {
                                case '0':
                                case '1':
                                case '2':
                                case '3':
                                case '4':
                                case '5':
                                case '6':
                                case '7':
                                case '8':
                                case '9':
                                    if (0 == radix)
                                        radix = 10;
                                    number = number * radix + (character - '0');
                                    break;
                                case 'A':
                                case 'B':
                                case 'C':
                                case 'D':
                                case 'E':
                                case 'F':
                                    if (16 == radix)
                                        number = number * radix + (character - 'A' + 10);
                                    else
                                        done = true;
                                    break;
                                case 'a':
                                case 'b':
                                case 'c':
                                case 'd':
                                case 'e':
                                case 'f':
                                    if (16 == radix)
                                        number = number * radix + (character - 'a' + 10);
                                    else
                                        done = true;
                                    break;
                                case 'x':
                                case 'X':
                                    if (0 == radix)
                                        radix = 16;
                                    else
                                        done = true;
                                    break;
                                case ';':
                                    done = true;
                                    i++;
                                    break;
                                default:
                                    done = true;
                                    break;
                            }
                            if (!done)
                                i++;
                        }
                        if (0 != number)
                        {
                            buffer.append ((char)number);
                            index = i;
                            amp = index;
                        }
                        
                    }
                    else if (Character.isLetter (character)) // really can't start with a digit eh...
                    {
                        // character entity reference
                        i = index + 1;
                        done = false;
                        semi = length;
                        while ((i < length) && !done)
                        {
                            character = string.charAt (i);
                            if (';' == character)
                            {
                                done = true;
                                semi = i;
                                i++;
                            }
                            else if (Character.isLetterOrDigit (character))
                                i++;
                            else
                            {
                                done = true;
                                semi = i;
                            }
                        }
                        // new CharacterReference (string.substring (index, semi), 0);
                        if (null == key)
                            key = new CharacterReferenceEx ();
                        key.setKernel (string);
                        key.setStart (index);
                        key.setEnd (semi);
                        item = lookup (key);
                        if (null != item)
                        {
                            buffer.append ((char)item.getCharacter ());
                            index += item.getKernel ().length ();
                            if ((index < length) && (';' == string.charAt (index)))
                                index++;
                            amp = index;
                        }
                    }
                    else
                    {
                        // need do nothing here, the ampersand will be consumed below
                    }
                }
                // gather up unconsumed characters
                while (amp < index)
                    buffer.append (string.charAt (amp++));
            }
            while ((index < length) && (-1 != (amp = string.indexOf ('&', index))));
            // equivalent to buffer.append (string.substring (index));
            // but without the allocation of a new String
            while (index < length)
                buffer.append (string.charAt (index++));
            ret = buffer.toString ();
        }

        return (ret);
    }

    /**
     * Decode the characters in a string buffer containing references.
     * Change all numeric character reference and character entity references
     * to unicode characters.
     * @param buffer The StringBuffer containing references.
     * @return The decoded string.
     */
    public static String decode (StringBuffer buffer)
    {
        return decode (buffer.toString());
    }

    /**
     * Decode a stream containing references.
     * Change all numeric character reference and character entity references
     * to unicode characters. If <code>DECODE_LINE_BY_LINE</code> is true,
     * the input stream is broken up into lines, terminated by either
     * carriage return or newline, in order to reduce the latency and maximum
     * buffering memory size required.
     * @param in The stream to translate. It is assumed that the input
     * stream is encoded with ISO-8859-1 since the table of character
     * entity references in this class applies only to ISO-8859-1.
     * @param out The stream to write the decoded stream to.
     */
    public static void decode (InputStream in, PrintStream out)
    {
        Reader reader;
        StringBuffer buffer;
        int character;
        String string;
        boolean newlines;

        try
        {
            try
            {
                reader = new BufferedReader (new InputStreamReader (in, "ISO-8859-1"));
            }
            catch (UnsupportedEncodingException use)
            {
                // yeah, like this will happen; OK, assume the default is ISO-8859-1
                reader = new BufferedReader (new InputStreamReader (in));
            }
            buffer = new StringBuffer (1024);
            newlines = false;
            if (DECODE_LINE_BY_LINE)
                while (-1 != (character = reader.read ()))
                {
                    if (('\r' == character) || ('\n' == character))
                    {
                        if (!newlines)
                        {
                            string = decode (buffer.toString ());
                            out.print (string);
                            buffer.setLength (0);
                            newlines = true;
                        }
                        buffer.append ((char)character);
                    }
                    else
                    {
                        if (newlines)
                        {
                            out.print (buffer.toString ());
                            buffer.setLength (0);
                            newlines = false;
                        }
                        buffer.append ((char)character);
                    }
                }
            else
                while (-1 != (character = reader.read ()))
                    buffer.append ((char)character);
            if (0 != buffer.length ())
            {
                if (newlines)
                    out.print (buffer.toString ());
                else
                {
                    string = decode (buffer.toString ());
                    out.print (string);
                }
            }
        }
        catch (IOException ioe)
        {
            out.println ();
            out.println (ioe.getMessage ());
        }
        finally
        {
            out.flush ();
        }
    }

    /**
     * Convert a character to a numeric character reference.
     * Convert a unicode character to a numeric character reference of
     * the form &amp;#xxxx;.
     * @param character The character to convert.
     * @return The converted character.
     * @deprecated Use {@link #encode(int) encode}.
     */
    public static String convertToString (int character)
    {
        return (encode (character));
    }

    /**
     * Convert a character to a numeric character reference.
     * Convert a unicode character to a numeric character reference of
     * the form &amp;#xxxx;.
     * @param character The character to convert.
     * @return The converted character.
     */
    public static String encode (int character)
    {
        StringBuffer ret;

        ret = new StringBuffer (13); /* &#2147483647; */
        ret.append ("&#");
        if (ENCODE_HEXADECIMAL)
        {
            ret.append ("x");
            ret.append (Integer.toHexString (character));
        }
        else
            ret.append (character);
        ret.append (';');

        return (ret.toString ());
    }
    
    /**
     * Encode a string to use references.
     * Change all characters that are not ISO-8859-1 to their numeric character
     * reference or character entity reference.
     * @param string The string to translate.
     * @return The encoded string.
     */
    public static String encode (String string)
    {
        int length;
        char c;
        CharacterReference candidate;
        StringBuffer ret;

        ret = new StringBuffer (string.length () * 6);
        length  = string.length ();
        for (int i = 0; i < length; i++)
        {
            c = string.charAt (i);
            candidate = lookup (c);
            if (null != candidate)
            {
                ret.append ('&');
                ret.append (candidate.getKernel ());
                ret.append (';');
            }
            else if (!(c < 0x007F))
            {
                ret.append ("&#");
                if (ENCODE_HEXADECIMAL)
                {
                    ret.append ("x");
                    ret.append (Integer.toHexString (c));
                }
                else
                    ret.append ((int)c);
                ret.append (';');
            }
            else
                ret.append (c);
        }

        return (ret.toString ());
    }

    /**
     * Encode a stream to use references.
     * Change all characters that are not ISO-8859-1 to their numeric character
     * reference or character entity reference.
     * @param in The stream to translate. It is assumed that the input
     * stream is encoded with ISO-8859-1 since the table of character
     * entity references in this class applies only to ISO-8859-1.
     * @param out The stream to write the decoded stream to.
     */
    public static void encode (InputStream in, PrintStream out)
    {
        Reader reader;
        char c;
        int index;
        CharacterReference candidate;
        PrintWriter output;

        try
        {
            reader = new BufferedReader (new InputStreamReader (in, "ISO-8859-1"));
            output = new PrintWriter (new BufferedWriter (new OutputStreamWriter (out, "ISO-8859-1")));
        }
        catch (UnsupportedEncodingException use)
        {
            // yeah, like this will happen; OK, assume default is ISO-8859-1
            reader = new BufferedReader (new InputStreamReader (in));
            output = new PrintWriter (new BufferedWriter (new OutputStreamWriter (out)));
        }
        try
        {
            while (-1 != (index = reader.read ()))
            {
                c = (char)index;
                candidate = lookup (c);
                if (null != candidate)
                {
                    output.print ('&');
                    output.print (candidate.getKernel ());
                    output.print (';');
                }
                else if (!(c < 0x007F))
                {
                    output.print ("&#");
                    if (ENCODE_HEXADECIMAL)
                    {
                        output.print ("x");
                        output.print (Integer.toHexString (c));
                    }
                    else
                        output.print ((int)c);
                    output.print (';');
                }
                else
                    output.print (c);
            }
        }
        catch (IOException ioe)
        {
            output.println ();
            output.println (ioe.getMessage ());
        }
        finally
        {
            output.flush ();
        }
    }

    /**
     * Numeric character reference and character entity reference to unicode codec.
     * Translate the <code>System.in</code> input into an encoded or decoded
     * stream and send the results to <code>System.out</code>.
     * @param args If arg[0] is <code>-encode</code> perform an encoding on
     * <code>System.in</code>, otherwise perform a decoding.
     */
    public static void main (String[] args)
    {
        boolean encode;

        if (0 < args.length && args[0].equalsIgnoreCase ("-encode"))
            encode = true;
        else
            encode = false;
        if (encode)
            encode (System.in, System.out);
        else
            decode (System.in, System.out);
    }
}
