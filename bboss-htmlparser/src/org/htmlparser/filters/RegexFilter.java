// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2003 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/filters/RegexFilter.java,v $
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

package org.htmlparser.filters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Text;

/**
 * This filter accepts all string nodes matching a regular expression.
 * Because this searches {@link org.htmlparser.Text Text} nodes. it is
 * only useful for finding small fragments of text, where it is
 * unlikely to be broken up by a tag. To find large fragments of text
 * you should convert the page to plain text with something like the
 * {@link org.htmlparser.beans.StringBean StringBean} and then apply
 * the regular expression.
 * <p>
 * For example, to look for dates use:
 * <pre>
 *   (19|20)\d\d([- \\/.](0[1-9]|1[012])[- \\/.](0[1-9]|[12][0-9]|3[01]))?
 * </pre>
 * as in:
 * <pre>
 * Parser parser = new Parser ("http://cbc.ca");
 * RegexFilter filter = new RegexFilter ("(19|20)\\d\\d([- \\\\/.](0[1-9]|1[012])[- \\\\/.](0[1-9]|[12][0-9]|3[01]))?");
 * NodeIterator iterator = parser.extractAllNodesThatMatch (filter).elements ();
 * </pre>
 * which matches a date in yyyy-mm-dd format between 1900-01-01 and 2099-12-31,
 * with a choice of five separators, either a dash, a space, either kind of
 * slash or a period.
 * The year is matched by (19|20)\d\d which uses alternation to allow the
 * either 19 or 20 as the first two digits. The round brackets are mandatory.
 * The month is matched by 0[1-9]|1[012], again enclosed by round brackets
 * to keep the two options together. By using character classes, the first
 * option matches a number between 01 and 09, and the second
 * matches 10, 11 or 12.
 * The last part of the regex consists of three options. The first matches
 * the numbers 01 through 09, the second 10 through 29, and the third matches 30 or 31.
 * The day and month are optional, but must occur together because of the ()?
 * bracketing after the year.
 */
public class RegexFilter implements NodeFilter
{
    /**
     * Use match() matching strategy.
     */
    public static final int MATCH = 1;

    /**
     * Use lookingAt() match strategy.
     */
    public static final int LOOKINGAT = 2;

    /**
     * Use find() match strategy.
     */
    public static final int FIND = 3;

    /**
     * The regular expression to search for.
     */
    protected String mPatternString;

    /**
     * The compiled regular expression to search for.
     */
    protected Pattern mPattern;

    /**
     * The match strategy.
     * @see #RegexFilter(String, int)
     */
    protected int mStrategy;

    /**
     * Creates a new instance of RegexFilter that accepts string nodes matching
     * the regular expression ".*" using the FIND strategy.
     */
    public RegexFilter ()
    {
        this (".*", FIND);
    }

    /**
     * Creates a new instance of RegexFilter that accepts string nodes matching
     * a regular expression using the FIND strategy.
     * @param pattern The pattern to search for.
     */
    public RegexFilter (String pattern)
    {
        this (pattern, FIND);
    }

    /**
     * Creates a new instance of RegexFilter that accepts string nodes matching
     * a regular expression.
     * @param pattern The pattern to search for.
     * @param strategy The type of match:
     * <ol>
     * <li>{@link #MATCH} use matches() method: attempts to match
     * the entire input sequence against the pattern</li>
     * <li>{@link #LOOKINGAT} use lookingAt() method: attempts to match
     * the input sequence, starting at the beginning, against the pattern</li>
     * <li>{@link #FIND} use find() method: scans the input sequence looking
     * for the next subsequence that matches the pattern</li>
     * </ol>
     */
    public RegexFilter (String pattern, int strategy)
    {
        setPattern (pattern);
        setStrategy (strategy);
    }

    /**
     * Get the search pattern.
     * @return Returns the pattern.
     */
    public String getPattern ()
    {
        return (mPatternString);
    }

    /**
     * Set the search pattern.
     * @param pattern The pattern to set.
     */
    public void setPattern (String pattern)
    {
        mPatternString = pattern;
        mPattern = Pattern.compile (pattern);
    }

    /**
     * Get the search strategy.
     * @return Returns the strategy.
     */
    public int getStrategy ()
    {
        return (mStrategy);
    }

    /**
     * Set the search pattern.
     * @param strategy The strategy to use. One of MATCH, LOOKINGAT or FIND.
     */
    public void setStrategy (int strategy)
    {
        if ((strategy != MATCH) && (strategy != LOOKINGAT)
            && (strategy != FIND))
            throw new IllegalArgumentException ("illegal strategy ("
                + strategy + ")");
        mStrategy = strategy;
    }

    /**
     * Accept string nodes that match the regular expression.
     * @param node The node to check.
     * @return <code>true</code> if the regular expression matches the
     * text of the node, <code>false</code> otherwise.
     */
    public boolean accept (Node node)
    {
        String string;
        Matcher matcher;
        boolean ret;

        ret = false;
        if (node instanceof Text)
        {
            string = ((Text)node).getText ();
            matcher = mPattern.matcher (string);
            switch (mStrategy)
            {
                case MATCH:
                    ret = matcher.matches ();
                    break;
                case LOOKINGAT:
                    ret = matcher.lookingAt ();
                    break;
                case FIND:
                default:
                    ret = matcher.find ();
                    break;
            }
        }

        return (ret);
    }
}
