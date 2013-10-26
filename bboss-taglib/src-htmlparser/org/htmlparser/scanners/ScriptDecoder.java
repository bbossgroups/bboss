// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/scanners/ScriptDecoder.java,v $
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

package org.htmlparser.scanners;

import org.htmlparser.lexer.Cursor;
import org.htmlparser.lexer.Page;
import org.htmlparser.util.ParserException;

/**
 * Decode script.
 * Script obfuscated by the <A href="http://www.microsoft.com/downloads/details.aspx?FamilyId=E7877F67-C447-4873-B1B0-21F0626A6329&displaylang=en" target="_parent">Windows Script Encoder</A>
 * provided by Microsoft, is converted to plaintext. This code is based loosely
 * on example code provided by MrBrownstone with changes by Joe Steele, see
 * <A href="http://www.virtualconspiracy.com/download/scrdec14.c" target="_parent">scrdec14.c</A>.
 */
public class ScriptDecoder
{
    /**
     * Termination state.
     */
    public static final int STATE_DONE = 0;

    /**
     * State on entry.
     */
    public static final int STATE_INITIAL = 1;

    /**
     * State while reading the encoded length.
     */
    protected static final int STATE_LENGTH = 2;

    /**
     * State when reading up to decoded text.
     */
    protected static final int STATE_PREFIX = 3;

    /**
     * State while decoding.
     */
    protected static final int STATE_DECODE = 4;

    /**
     * State when reading an escape sequence.
     */
    protected static final int STATE_ESCAPE = 5;

    /**
     * State when reading the checksum.
     */
    protected static final int STATE_CHECKSUM = 6;

    /**
     * State while exiting.
     */
    protected static final int STATE_FINAL = 7;

    /**
     * The state to enter when decrypting is complete.
     * If this is STATE_DONE, the decryption will return with any characters
     * following the encoded text still unconsumed.
     * Otherwise, if this is STATE_INITIAL, the input will be exhausted and
     * all following characters will be contained in the return value
     * of the <code>Decode()</code> method.
     */
    public static int LAST_STATE = STATE_DONE;

    /**
     * Table of lookup choice.
     * The decoding cycles between three flavours determined
     * by this sequence of 64 choices, corresponding to the
     * first dimension of the lookup table.
     */
    protected static byte mEncodingIndex[] =
    {
        1, 2, 0, 1, 2, 0, 2, 0, 0, 2, 0, 2, 1, 0, 2, 0, 
        1, 0, 2, 0, 1, 1, 2, 0, 0, 2, 1, 0, 2, 0, 0, 2, 
        1, 1, 0, 2, 0, 2, 0, 1, 0, 1, 1, 2, 0, 1, 0, 2, 
        1, 0, 2, 0, 1, 1, 2, 0, 0, 1, 1, 2, 0, 1, 0, 2,
    };

    /**
     * Two dimensional lookup table.
     * The decoding uses this table to determine the plaintext for
     * characters that aren't mEscaped.
     */
    protected static char mLookupTable[][] =
    {
        {
            '{', 
            '2',  '0',  '!',  ')',  '[',  '8',  '3',  '=', 
            'X',  ':',  '5',  'e',  '9', '\\',  'V',  's', 
            'f',  'N',  'E',  'k',  'b',  'Y',  'x',  '^', 
            '}',  'J',  'm',  'q',    0,  '`',    0,  'S', 
              0,  'B', '\'',  'H',  'r',  'u',  '1',  '7', 
            'M',  'R',  '"',  'T',  'j',  'G',  'd',  '-', 
            ' ',  '',  '.',  'L',  ']',  '~',  'l',  'o', 
            'y',  't',  'C',  '&',  'v',  '%',  '$',  '+', 
            '(',  '#',  'A',  '4', '\t',  '*',  'D',  '?', 
            'w',  ';',  'U',  'i',  'a',  'c',  'P',  'g', 
            'Q',  'I',  'O',  'F',  'h',  '|',  '6',  'p', 
            'n',  'z',  '/',  '_',  'K',  'Z',  ',',  'W', 
        },
        {
            'W', 
            '.',  'G',  'z',  'V',  'B',  'j',  '/',  '&', 
            'I',  'A',  '4',  '2',  '[',  'v',  'r',  'C', 
            '8',  '9',  'p',  'E',  'h',  'q',  'O', '\t', 
            'b',  'D',  '#',  'u',    0,  '~',    0,  '^', 
              0,  'w',  'J',  'a',  ']',  '"',  'K',  'o', 
            'N',  ';',  'L',  'P',  'g',  '*',  '}',  't', 
            'T',  '+',  '-',  ',',  '0',  'n',  'k',  'f', 
            '5',  '%',  '!',  'd',  'M',  'R',  'c',  '?', 
            '{',  'x',  ')',  '(',  's',  'Y',  '3',  '', 
            'm',  'U',  'S',  '|',  ':',  '_',  'e',  'F', 
            'X',  '1',  'i',  'l',  'Z',  'H', '\'', '\\', 
            '=',  '$',  'y',  '7',  '`',  'Q',  ' ',  '6', 
        },
        {
            'n', 
            '-',  'u',  'R',  '`',  'q',  '^',  'I', '\\', 
            'b',  '}',  ')',  '6',  ' ',  '|',  'z',  '', 
            'k',  'c',  '3',  '+',  'h',  'Q',  'f',  'v', 
            '1',  'd',  'T',  'C',    0,  ':',    0,  '~', 
              0,  'E',  ',',  '*',  't', '\'',  '7',  'D', 
            'y',  'Y',  '/',  'o',  '&',  'r',  'j',  '9', 
            '{',  '?',  '8',  'w',  'g',  'S',  'G',  '4', 
            'x',  ']',  '0',  '#',  'Z',  '[',  'l',  'H', 
            'U',  'p',  'i',  '.',  'L',  '!',  '$',  'N', 
            'P', '\t',  'V',  's',  '5',  'a',  'K',  'X', 
            ';',  'W',  '"',  'm',  'M',  '%',  '(',  'F', 
            'J',  '2',  'A',  '=',  '_',  'O',  'B',  'e', 
        },
    };

    /**
     * The base 64 decoding table.
     * This array determines the value of decoded base 64 elements.
     */
    protected static int mDigits[];
    static
    {
        mDigits = new int[0x7b];
        for (int i = 0; i < 26; i++)
        {
            mDigits['A' + i] = i;
            mDigits['a' + i] = i + 26;
        }
        for (int i = 0; i < 10; i++)
            mDigits['0' + i] = i + 52;
        mDigits[0x2b] = '>';
        mDigits[0x2f] = '?';
    }

    /**
     * The leader.
     * The prefix to the encoded script is #@~^nnnnnn== where the n are the
     * length digits in base64.
     */
    protected static char mLeader[] =
    { 
        '#',
        '@',
        '~',
        '^',
    };

    /**
     * The prefix.
     * The prfix separates the encoded text from the length.
     */
    protected static char mPrefix[] =
    { 
        '=',
        '=',
    };

    /**
     * The trailer.
     * The suffix to the encoded script is nnnnnn==^#~@ where the n are the
     * checksum digits in base64. These characters are the part after the checksum.
     */
    protected static char mTrailer[] =
    { 
        '=',
        '=',
        '^',
        '#',
        '~',
        '@',
    };

    /**
     * Escape sequence characters.
     */
    protected static char mEscapes[] =
    {
        '#',
        '&',
        '!',
        '*',
        '$',
    };

    /**
     * The escaped characters corresponding to the each escape sequence.
     */
    protected static char mEscaped[] = //"\r\n<>@";
    {
        '\r',
        '\n',
        '<',
        '>',
        '@',
    };

    /**
     * Extract the base 64 encoded number.
     * This is a very limited subset of base 64 encoded characters.
     * Six characters are expected. These are translated into a single long
     * value. For a more complete base 64 codec see for example the base64
     * package of <A href="http://sourceforge.net/projects/iharder/" target="_parent">iHarder.net</A>
     * @param p Six base 64 encoded digits.
     * @return The value of the decoded number.
     */
    protected static long decodeBase64 (char[] p)
    {
        long ret;
        
        ret = 0;

        ret +=  (mDigits[p[0]] << 2);
        ret +=  (mDigits[p[1]] >> 4);
        ret +=  (mDigits[p[1]] & 0xf) << 12;
        ret += ((mDigits[p[2]] >> 2) << 8); 
        ret += ((mDigits[p[2]] & 0x3) << 22);
        ret +=  (mDigits[p[3]] << 16);
        ret += ((mDigits[p[4]] << 2) << 24);
        ret += ((mDigits[p[5]] >> 4) << 24);

        return (ret);
    }

    /**
     * Decode script encoded by the Microsoft obfuscator.
     * @param page The source for encoded text.
     * @param cursor The position at which to start decoding.
     * This is advanced to the end of the encoded text.
     * @return The plaintext.
     * @exception ParserException If an error is discovered while decoding.
     */
    public static String Decode (Page page, Cursor cursor)
        throws
            ParserException
    {
        int state;
        int substate_initial;
        int substate_length;
        int substate_prefix;
        int substate_checksum;
        int substate_final;
        long checksum;
        long length;
        char buffer[];
        buffer = new char[6];
        int index;
        char character;
        int input_character;
        boolean found;
        StringBuffer ret;
        
        ret = new StringBuffer (1024);

        state = STATE_INITIAL;
        substate_initial = 0;
        substate_length = 0;
        substate_prefix = 0;
        substate_checksum = 0;
        substate_final = 0;
        length = 0L;
        checksum = 0L;
        index = 0;
        while (STATE_DONE != state)
        {
            input_character = page.getCharacter (cursor);
            character = (char)input_character;
            if (Page.EOF == input_character)
            {
                if (   (STATE_INITIAL != state)
                    || (0 != substate_initial)
                    || (0 != substate_length)
                    || (0 != substate_prefix)
                    || (0 != substate_checksum)
                    || (0 != substate_final))
                    throw new ParserException ("illegal state for exit");
                state = STATE_DONE;
            }
            else
                switch (state)
                {
                    case STATE_INITIAL:
                        if (character == mLeader[substate_initial])
                        {
                            substate_initial++;
                            if (substate_initial == mLeader.length)
                            {
                                substate_initial = 0;
                                state = STATE_LENGTH;
                            }
                        }
                        else
                        {
                            // oops, flush
                            for (int k = 0; 0 < substate_initial; k++)
                            {
                                ret.append (mLeader[k++]);
                                substate_initial--;
                            }
                            ret.append (character);
                        }
                        break;

                    case STATE_LENGTH:
                        buffer[substate_length] = character;
                        substate_length++;
                        if (substate_length >= buffer.length)
                        {
                            length = decodeBase64 (buffer);
                            if (0 > length)
                                throw new ParserException ("illegal length: " + length);
                            substate_length = 0;
                            state = STATE_PREFIX;
                        }
                        break;

                    case STATE_PREFIX:
                        if (character == mPrefix[substate_prefix])
                            substate_prefix++;
                        else
                            throw new ParserException ("illegal character encountered: " + (int)character + " ('" + character + "')");
                        if (substate_prefix >= mPrefix.length)
                        {
                            substate_prefix = 0;
                            state = STATE_DECODE;
                        }
                        break;

                    case STATE_DECODE:
                        if ('@' == character)
                            state = STATE_ESCAPE;
                        else
                        {
                            if (input_character < 0x80)
                            {
                                if (input_character == '\t')
                                    input_character = 0;
                                else if (input_character >= ' ')
                                    input_character -= ' ' - 1;
                                else
                                    throw new ParserException ("illegal encoded character: " + input_character + " ('" + character + "')");
                                char ch = mLookupTable[mEncodingIndex[index % 64]][input_character];
                                ret.append (ch);
                                checksum += ch;
                                index++;
                            }
                            else
                                ret.append (character);
                        }
                        length--;
                        if (0 == length)
                        {
                            index = 0;
                            state = STATE_CHECKSUM;
                        }
                        break;

                    case STATE_ESCAPE:
                        found = false;
                        for (int i = 0; i < mEscapes.length; i++)
                            if (character == mEscapes[i])
                            {
                                found = true;
                                character = mEscaped[i];
                            }
                        if (!found)
                            throw new ParserException ("unexpected escape character: " + (int)character + " ('" + character + "')");
                        ret.append (character);
                        checksum += character;
                        index++;
                        state = STATE_DECODE;
                        length--;
                        if (0 == length)
                        {
                            index = 0;
                            state = STATE_CHECKSUM;
                        }
                        break;

                    case STATE_CHECKSUM:
                        buffer[substate_checksum] = character;
                        substate_checksum++;
                        if (substate_checksum >= buffer.length)
                        {
                            long check = decodeBase64 (buffer);
                            if (check != checksum)
                                throw new ParserException ("incorrect checksum, expected " + check + ", calculated " + checksum);
                            checksum = 0;
                            substate_checksum = 0;
                            state = STATE_FINAL;
                        }
                        break;

                    case STATE_FINAL:
                        if (character == mTrailer[substate_final])
                            substate_final++;
                        else
                            throw new ParserException ("illegal character encountered: " + (int)character + " ('" + character + "')");
                        if (substate_final >= mTrailer.length)
                        {
                            substate_final = 0;
                            state = LAST_STATE;
                        }
                        break;
                    default:
                        throw new ParserException ("invalid state: " + state);
                }
        }

        return (ret.toString ());
    }

//    /**
//     * Example mainline for decrypting script.
//     * Change a file with encrypted script into one without.
//     * <em>WARNING: This does not preserve DOS type line endings.</em>
//     * @param args Command line arguments. Two file names, input and output.
//     * Optionally, the character set to use as a third argument.
//     * @exception IOException If the input file doesn't exist, or the output
//     * file cannot be created.
//     * @exception ParserException If there is a decryption problem.
//     */
//    public static void main (String[] args)
//         throws
//            IOException,
//            ParserException
//    {
//        String charset;
//        FileInputStream in;
//        Page page;
//        Cursor cursor;
//        String string;
//        int ret;
//        
//        if (args.length < 2)
//        {
//            System.out.println ("Usage: java org.htmlparser.scanners.ScriptDecoder <infile> <outfile> [charset]");
//            ret = 1;
//        }
//        else
//        {
//            if (2 < args.length)
//                charset = args[2];
//            else
//                charset = "ISO-8859-1";
//            in = new FileInputStream (args[0]);
//            page = new Page (in, charset);
//            cursor = new Cursor (page, 0);
//            ScriptDecoder.LAST_STATE = STATE_INITIAL;
//            string = ScriptDecoder.Decode (page, cursor);
//            in.close ();
//            
//            FileOutputStream outfile = new FileOutputStream (args[1]);
//            outfile.write (string.getBytes (charset));
//            outfile.close ();
//            ret = (0 != string.length ()) ? 0 : 1;
//        }
//        
//        System.exit (ret);
//    }
}
