// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Rogers George
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/filters/CssSelectorNodeFilter.java,v $
// $Author: derrickoswald $
// $Date: 2005/05/15 11:49:04 $
// $Revision: 1.6 $
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
import org.htmlparser.Tag;
import org.htmlparser.util.NodeList;

/**
 * A NodeFilter that accepts nodes based on whether they match a CSS2 selector.
 * Refer to <a href="http://www.w3.org/TR/REC-CSS2/selector.html">
 * http://www.w3.org/TR/REC-CSS2/selector.html</a> for syntax.
 * <p>
 * Todo: more thorough testing, any relevant pseudo-classes, css3 features
 */
public class CssSelectorNodeFilter implements NodeFilter
{
    /**
     * Regular expression to split the selector into tokens.
     */
    private static Pattern tokens =
        Pattern.compile("("
            + "/\\*.*?\\*/"             // comments
            + ") | ("
            + "   \".*?[^\"]\""   // double quoted string
            + " | \'.*?[^\']\'"   // single quoted string
            + " | \"\" | \'\' "     // empty quoted string
            + ") | ("
            + " [\\~\\*\\$\\^]? = " // attrib-val relations
            + ") | ("
            + " [a-zA-Z_\\*](?:[a-zA-Z0-9_-]|\\\\.)* " // bare name
            + ") | \\s*("
            + " [+>~\\s] "        // combinators
            + ")\\s* | ("
            + " [\\.\\[\\]\\#\\:)(] "       // class/id/attr/param delims
            + ") | ("
            + " [\\,] "                     // comma
            + ") | ( . )"                   // everything else (bogus)
            ,
            Pattern.CASE_INSENSITIVE
            | Pattern.DOTALL
            | Pattern.COMMENTS);

    /**
     * Comment token type.
     */
    private static final int COMMENT = 1;

    /**
     * quoted string token type.
     */
    private static final int QUOTEDSTRING = 2;

    /**
     * Relation token type.
     */
    private static final int RELATION = 3;

    /**
     * Name token type.
     */
    private static final int NAME = 4;

    /**
     * Combinator token type.
     */
    private static final int COMBINATOR = 5;

    /**
     * Delimiter token type.
     */
    private static final int DELIM = 6;

    /**
     * Comma token type.
     */
    private static final int COMMA = 7;

    private NodeFilter therule;

    private Matcher m = null;
    private int tokentype = 0;
    private String token = null;

    /**
     * Create a Cascading Style Sheet node filter.
     * @param selector The selector expression.
     */
    public CssSelectorNodeFilter(String selector)
    {
        m = tokens.matcher (selector);
        if (nextToken ())
            therule = parse ();
    }

    /**
     * Accept nodes that match the selector expression.
     * @param node The node to check.
     * @return <code>true</code> if the node matches,
     * <code>false</code> otherwise.
     */
    public boolean accept (Node node)
    {
        return (therule.accept (node));
    }

    private boolean nextToken ()
    {
        if (m != null && m.find ())
            for (int i = 1; i < m.groupCount (); i++)
                if (null != m.group (i))
                {
                    tokentype = i;
                    token = m.group (i);
                    return true;
                }
        tokentype = 0;
        token = null;
        return (false);
    }

    private NodeFilter parse ()
    {
        NodeFilter ret;
        
        ret = null;
        do
        {
            switch (tokentype)
            {
                case COMMENT:
                case NAME:
                case DELIM:
                    if (ret == null)
                        ret = parseSimple ();
                    else
                        ret = new AndFilter (ret, parseSimple ());
                    break;
                case COMBINATOR:
                    switch (token.charAt (0))
                    {
                        case '+':
                            ret = new AdjacentFilter (ret);
                            break;
                        case '>':
                            ret = new HasParentFilter (ret);
                            break;
                        default: // whitespace
                            ret = new HasAncestorFilter (ret);
                    }
                    nextToken ();
                    break;
                case COMMA:
                    ret = new OrFilter (ret, parse ());
                    nextToken ();
                    break;
            }
        }
        while (token != null);

        return (ret);
    }

    private NodeFilter parseSimple()
    {
        boolean done = false;
        NodeFilter ret = null;

        if (token != null)
            do
            {
                switch (tokentype)
                {
                    case COMMENT:
                        nextToken();
                        break;
                    case NAME:
                        if ("*".equals(token))
                            ret = new YesFilter();
                        else if (ret == null)
                            ret = new TagNameFilter(unescape(token));
                        else
                            ret = new AndFilter(ret, new TagNameFilter(unescape(token)));
                        nextToken();
                        break;
                    case DELIM:
                        switch (token.charAt(0))
                        {
                            case '.':
                                nextToken();
                                if (tokentype != NAME)
                                    throw new IllegalArgumentException("Syntax error at " + token);
                                if (ret == null)
                                    ret = new HasAttributeFilter("class", unescape(token));
                                else
                                    ret
                                    = new AndFilter(ret, new HasAttributeFilter("class", unescape(token)));
                                break;
                            case '#':
                                nextToken();
                                if (tokentype != NAME)
                                    throw new IllegalArgumentException("Syntax error at " + token);
                                if (ret == null)
                                    ret = new HasAttributeFilter("id", unescape(token));
                                else
                                    ret = new AndFilter(ret, new HasAttributeFilter("id", unescape(token)));
                                break;
                            case ':':
                                nextToken();
                                if (ret == null)
                                    ret = parsePseudoClass();
                                else
                                    ret = new AndFilter(ret, parsePseudoClass());
                                break;
                            case '[':
                                nextToken();
                                if (ret == null)
                                    ret = parseAttributeExp();
                                else
                                    ret = new AndFilter(ret, parseAttributeExp());
                                break;
                        }
                        nextToken();
                        break;
                    default:
                        done = true;
                }
            }
            while (!done && token != null);
        return ret;
    }

    private NodeFilter parsePseudoClass()
    {
        throw new IllegalArgumentException("pseudoclasses not implemented yet");
    }

    private NodeFilter parseAttributeExp()
    {
        NodeFilter ret = null;
        if (tokentype == NAME)
        {
            String attrib = token;
            nextToken();
            if ("]".equals(token))
                ret = new HasAttributeFilter(unescape(attrib));
            else if (tokentype == RELATION)
            {
                String val = null, rel = token;
                nextToken();
                if (tokentype == QUOTEDSTRING)
                    val = unescape(token.substring(1, token.length() - 1));
                else if (tokentype == NAME)
                    val = unescape(token);
                if ("~=".equals(rel) && val != null)
                    ret = new AttribMatchFilter(unescape(attrib),
                        "\\b"
                        + val.replaceAll("([^a-zA-Z0-9])", "\\\\$1")
                        + "\\b");
                else if ("=".equals(rel) && val != null)
                    ret = new HasAttributeFilter(attrib, val);
            }
        }
        if (ret == null)
            throw new IllegalArgumentException("Syntax error at " + token + tokentype);

        nextToken();
        return ret;
    }

    /**
     * Replace escape sequences in a string.
     * @param escaped The string to examine.
     * @return The argument with escape sequences replaced by their
     * equivalent character.
     */
    public static String unescape(String escaped)
    {
        StringBuffer result = new StringBuffer(escaped.length());
        Matcher m = Pattern.compile("\\\\(?:([a-fA-F0-9]{2,6})|(.))").matcher(
                        escaped);
        while (m.find())
        {
            if (m.group(1) != null)
                m.appendReplacement(result,
                    String.valueOf((char)Integer.parseInt(m.group(1), 16)));
            else if (m.group(2) != null)
                m.appendReplacement(result, m.group(2));
        }
        m.appendTail(result);

        return result.toString();
    }

    private static class HasAncestorFilter implements NodeFilter
    {
        private NodeFilter atest;

        public HasAncestorFilter(NodeFilter n)
        {
            atest = n;
        }

        public boolean accept(Node n)
        {
            while (n != null)
            {
                n = n.getParent();
                if (atest.accept(n))
                    return true;
            }
            return false;
        }
    }

    private static class AdjacentFilter implements NodeFilter
    {
        private NodeFilter sibtest;

        public AdjacentFilter(NodeFilter n)
        {
            sibtest = n;
        }

        public boolean accept(Node n)
        {
            if (n.getParent() != null)
            {
                NodeList l = n.getParent().getChildren();
                for (int i = 0; i < l.size(); i++)
                    if (l.elementAt(i) == n && i > 0)
                        return (sibtest.accept(l.elementAt(i - 1)));
            }
            return false;
        }
    }

    private static class YesFilter implements NodeFilter
    {
        public boolean accept(Node n)
        {return true;}
    }

    private static class AttribMatchFilter implements NodeFilter
    {
        private Pattern rel;
        private String attrib;

        public AttribMatchFilter(String attrib, String regex)
        {
            rel = Pattern.compile(regex);
            this.attrib = attrib;
        }

        public boolean accept(Node node)
        {
            if (node instanceof Tag && ((Tag)node).getAttribute(attrib) != null)
                if (rel != null
                        && !rel.matcher(((Tag)node).getAttribute(attrib)).find())
                    return false;
                else
                    return true;
            else
                return false;
        }
    }
}
