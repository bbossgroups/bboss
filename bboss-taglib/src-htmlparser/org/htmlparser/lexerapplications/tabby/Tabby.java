// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2003 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/lexerapplications/tabby/Tabby.java,v $
// $Author: derrickoswald $
// $Date: 2005/03/13 14:51:44 $
// $Revision: 1.3 $
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

package org.htmlparser.lexerapplications.tabby;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.htmlparser.lexer.Cursor;
import org.htmlparser.lexer.Page;

/**
 * Replace tabs with spaces.
 * Convert tabs to the correct number of spaces according to a tabstop,
 * change DOS \r\n line endings to Unix \n form, and remove trailing whitespace
 */
public class Tabby
{
    /**
     * The default tab stop spacing.
     */
    private static final int DEFAULT_TABSTOP = 4;

    /**
     * The file filter to apply.
     */
    protected Filter mFilter;

    /**
     * The replacement tab stop size.
     */
    protected int mTabsize;

    /**
     * Creates a new instance of Tabby with no file filter and a tab stop of 4.
     */
    public Tabby ()
    {
        mFilter = null;
        mTabsize = DEFAULT_TABSTOP;
    }

    /**
     * Creates a new instance of Tabby using the given regular expression and
     * a tab stop of 4.
     * @param filter The regular expression to apply to the files searched.
     */
    public Tabby (final String filter)
    {
        this ();
        mFilter = new Filter (filter);
    }

    /** Creates a new instance of Tabby.
     * @param filter The regular expression to apply to the files searched.
     * @param tabsize The tab stop setting.
     * @exception IllegalArgumentException If tabsize is not a positive number.
     */
    public Tabby (final String filter, final int tabsize)
        throws
            IllegalArgumentException
    {
        this (filter);
        if (0 >= tabsize)
            throw new IllegalArgumentException ("tab size cannot be negative");
        mTabsize = tabsize;
    }

    /**
     * Process the file or directory.
     * @param file The file to process.
     */
    protected void process (final File file)
    {
        File[] files;

        if (file.isDirectory ())
        {
            files = file.listFiles (mFilter);
            for (int i = 0; i < files.length; i++)
                process (files[i]);
        }
        else
            edit (file);
    }

    /**
     * Process the file or directory.
     * @param file The file to edit.
     */
    protected void edit (final File file)
    {
        FileInputStream in;
        Page page;
        Cursor cursor;
        int position;
        int expected;
        boolean modified;
        char ch;
        int last;
        StringBuffer buffer;
        FileOutputStream out;

        try
        {
            in = new FileInputStream (file);
            buffer = new StringBuffer (in.available ());
            try
            {
                page = new Page (in, null);
                cursor = new Cursor (page, 0);
                position = 0;
                modified = false;
                expected = 0;
                last = -1;
                while (Page.EOF != (ch = page.getCharacter (cursor)))
                {
                    if (++expected != cursor.getPosition ())
                    {
                        modified = true;
                        expected = cursor.getPosition ();
                    }
                    if ('\t' == ch)
                    {
                        do
                        {
                            buffer.append (' ');
                            position++;
                        }
                        while (0 != (position % mTabsize));
                        modified = true;
                    }
                    else if ('\n' == ch)
                    {
                        // check for whitespace on the end of the line
                        if (last + 1 != position)
                        {
                            // remove trailing whitespace
                            last = buffer.length () - (position - last - 1);
                            buffer.setLength (last);
                            modified = true;
                        }
                        buffer.append (ch);
                        position = 0;
                        last = -1;
                    }
                    else
                    {
                        buffer.append (ch);
                        if (!Character.isWhitespace (ch))
                            last = position;
                        position++;
                    }
                }
            }
            finally
            {
                in.close ();
            }
            if (modified)
            {
                System.out.println (file.getAbsolutePath ());
                out = new FileOutputStream (file);
                out.write (buffer.toString ().getBytes (Page.DEFAULT_CHARSET));
                out.close ();
            }
        }
        catch (Exception e)
        {
            System.out.println (e);
        }
    }

    /**
     * Implement a file filter.
     */
    class Filter implements FileFilter
    {
        /**
         * The compiled expression.
         */
        protected Pattern mExpression;

        /**
         * Create a file filter from the regular expression.
         * @param expression The <a href="http://java.sun.com/j2se/1.4.2/docs/api/java/util/regex/Pattern.html#sum">regular expression</a>.
         * A useful regular expression is ".*\.java" which accepts all
         * .java files.
         * @exception IllegalArgumentException If the expression is
         * <code>null</code>.
         * @exception PatternSyntaxException If the expression is not a valid
         * regular expression.
         */
        public Filter (final String expression)
            throws
                PatternSyntaxException
        {
            if (null == expression)
                throw new IllegalArgumentException (
                    "filter expression cannot be null");
            mExpression = Pattern.compile (expression);
        }

        //
        // FileFilter interface
        //

        /**
         * Tests whether or not the file should be included in a pathname list.
         * @param pathname The abstract pathname to be tested.
         * @return <code>true</code> if and only if <code>pathname</code>
         * should be included.
         */
        public boolean accept (final File pathname)
        {
            Matcher matcher;
            boolean ret;

            // match directories
            if (pathname.isDirectory ())
                ret = true;
            else
            {
                matcher = mExpression.matcher (pathname.getAbsolutePath ());
                ret = matcher.matches ();
            }

            return (ret);
        }
    }

    /**
     * Run Tabby on a file or directory.
     * @param args The command line arguments.
     * <PRE>
     * args[0] The file or directory to work on.
     * args[1] Optional, the regular expression to use as a file filter
     * args[2] Optional, the tab stop setting (integer).
     * </PRE>
     */
    public static void main (final String[] args)
    {
        Tabby tabby;
        File file;

        if (0 == args.length)
            System.out.println (
                  "usage: Tabby (<directory>|<file>)"
                + " [file-match regexp] [tabsize]");
        else
        {
            if (2 < args.length)
                tabby = new Tabby (args[1], Integer.parseInt (args[2]));
            else
                if (1 < args.length)
                    tabby = new Tabby (args[1]);
                else
                    tabby = new Tabby ();
            file = new File (args[0]);
            tabby.process (file);
        }
    }
}

/*
 * Revision Control Modification History
 *
 * $Log: Tabby.java,v $
 * Revision 1.3  2005/03/13 14:51:44  derrickoswald
 * Bug #1121401 No Parsing with yahoo!
 * By default nio.charset.CharsetDecoder replaces characters it cannot
 * represent in the current encoding with zero, which was the value
 * returned by the page when the Stream reached EOF.
 * This changes the Page return value to (char)Source.EOF (-1) when
 * the end of stream is encountered.
 *
 * Revision 1.2  2004/07/31 16:42:34  derrickoswald
 * Remove unused variables and other fixes exposed by turning on compiler warnings.
 *
 * Revision 1.1  2003/09/10 03:38:26  derrickoswald
 * Add style checking target to ant build script:
 *     ant checkstyle
 * It uses a jar from http://checkstyle.sourceforge.net which is dropped in the lib directory.
 * The rules are in the file htmlparser_checks.xml in the src directory.
 *
 * Added lexerapplications package with Tabby as the first app. It performs whitespace manipulation
 * on source files to follow the style rules. This reduced the number of style violations to roughly 14,000.
 *
 * There are a few issues with the style checker that need to be resolved before it should be taken too seriously.
 * For example:
 * It thinks all method arguments should be final, even if they are modified by the code (which the compiler frowns on).
 * It complains about long lines, even when there is no possibility of wrapping the line, i.e. a URL in a comment
 * that's more than 80 characters long.
 * It considers all naked integers as 'magic numbers', even when they are obvious, i.e. the 4 corners of a box.
 * It complains about whitespace following braces, even in array initializers, i.e. X[][] = { {a, b} { } }
 *
 * But it points out some really interesting things, even if you don't agree with the style guidelines,
 * so it's worth a look.
 *
 *
 */
