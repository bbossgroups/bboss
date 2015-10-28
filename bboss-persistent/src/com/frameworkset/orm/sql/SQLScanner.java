/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.  
 */
package com.frameworkset.orm.sql;

/*

 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple Scanner implementation that scans an
 * sql file into usable tokens.  Used by SQLToAppData.
 *
 * @author <a href="mailto:leon@opticode.co.za">Leon Messerschmidt</a>
 * @author <a href="mailto:jon@latchkey.com">Jon S. Stevens</a>
 * @author <a href="mailto:andyhot@di.uoa.gr">Andreas Andreou</a>
 * @version $Id: SQLScanner.java,v 1.4 2004/02/22 06:27:20 jmcnally Exp $
 */
public class SQLScanner
{
    /** white spaces */
    private static final String WHITE = "\f\r\t\n ";
    /** alphabetic characters */
    private static final String ALFA
            = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    /** numbers */
    private static final String NUMER = "0123456789";
    /** alphanumeric */
    private static final String ALFANUM = ALFA + NUMER;
    /** special characters */
    private static final String SPECIAL = ";(),'";
    /** comment */
    private static final char COMMENT_POUND = '#';
    /** comment */
    private static final char COMMENT_SLASH = '/';
    /** comment */
    private static final char COMMENT_STAR = '*';
    /** comment */
    private static final char COMMENT_DASH = '-';

    /** the input reader */
    private Reader in;
    /** character */
    private int chr;
    /** token */
    private String token;
    /** list of tokens */
    private List tokens;
    /** line */
    private int line;
    /** column */
    private int col;

    /**
     * Creates a new scanner with no Reader
     */
    public SQLScanner()
    {
        this(null);
    }

    /**
     * Creates a new scanner with an Input Reader
     *
     * @param input the input reader
     */
    public SQLScanner(Reader input)
    {
        setInput(input);
    }

    /**
     * Set the Input
     *
     * @param input the input reader
     */
    public void setInput(Reader input)
    {
        in = input;
    }


    /**
     * Reads the next character and increments the line and column counters.
     *
     * @throws IOException If an I/O error occurs
     */
    private void readChar() throws IOException
    {
        boolean wasLine = (char) chr == '\r';
        chr = in.read();
        System.out.println("readchar:" + (char)chr);
        if ((char) chr == '\n' || (char) chr == '\r' || (char) chr == '\f')
        {
            col = 0;
            if (!wasLine || (char) chr != '\n')
            {
                line++;
            }
        }
        else
        {
            col++;
        }
    }

    /**
     * Scans an identifier.
     *
     * @throws IOException If an I/O error occurs
     */
    private void scanIdentifier () throws IOException
    {
        token = "";
        char c = (char) chr;
        while (chr != -1 && WHITE.indexOf(c) == -1 && SPECIAL.indexOf(c) == -1)
        {
            token = token + (char) chr;
            readChar();
            c = (char) chr;
        }
        int start = col - token.length();
        System.out.println("token:" + token);
        tokens.add(new Token(token, line, start));
    }

    /**
     * Scans an identifier which had started with the negative sign.
     *
     * @throws IOException If an I/O error occurs
     */
    private void scanNegativeIdentifier () throws IOException
    {
        token = "-";
        char c = (char) chr;
        while (chr != -1 && WHITE.indexOf(c) == -1 && SPECIAL.indexOf(c) == -1)
        {
            token = token + (char) chr;
            readChar();
            c = (char) chr;
        }
        int start = col - token.length();
        tokens.add(new Token(token, line, start));
    }

    /**
     * Scan the input Reader and returns a list of tokens.
     *
     * @return a list of tokens
     * @throws IOException If an I/O error occurs
     */
    public List scan () throws IOException
    {
        line = 1;
        col = 0;
        boolean inComment = false;
        boolean inCommentSlashStar = false;
        boolean inCommentDash = false;

        boolean inNegative;

        tokens = new ArrayList();
        readChar();
        while (chr != -1)
        {
            char c = (char) chr;
            System.out.println(c);
            inNegative = false;

            if (c == COMMENT_DASH)
            {
                readChar();
                if ((char) chr == COMMENT_DASH)
                {
                    inCommentDash = true;
                }
                else
                {
                    inNegative = true;
                    c = (char) chr;
                }
            }

            if (inCommentDash)
            {
                if (c == '\n' || c == '\r')
                {
                    inCommentDash = false;
                }
                readChar();
            }
            else if (c == COMMENT_POUND)
            {
                inComment = true;
                readChar();
            }
            else if (c == COMMENT_SLASH)
            {
                readChar();
                if ((char) chr == COMMENT_STAR)
                {
                    inCommentSlashStar = true;
                }
            }
            else if (inComment || inCommentSlashStar)
            {
                if (c == '*')
                {
                    readChar();
                    if ((char) chr == COMMENT_SLASH)
                    {
                        inCommentSlashStar = false;
                    }
                }
                else if (c == '\n' || c == '\r')
                {
                    inComment = false;
                }
                readChar();
            }
            else if (ALFANUM.indexOf(c) >= 0)
            {
                if (inNegative)
                {
                    scanNegativeIdentifier();
                }
                else
                {
                    scanIdentifier();
                }
            }
            else if (SPECIAL.indexOf(c) >= 0)
            {
                tokens.add(new Token("" + c, line, col));
                readChar();
            }
            else
            {
                readChar();
            }
        }
        return tokens;
    }

    public static void main(String[] args)
    {
        String sql = "select a.name,b.tile from aa a,bb b";
        java.io.ByteArrayInputStream in = new ByteArrayInputStream(sql.getBytes());
        BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(in));
        SQLScanner scan = new SQLScanner();
        scan.setInput(reader);
        try
        {
            List list = scan.scan();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
