// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Somik Raha
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/util/ParserUtils.java,v $
// $Author: derrickoswald $
// $Date: 2005/05/15 11:49:05 $
// $Revision: 1.47 $
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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;


public class ParserUtils
{
    public static String removeChars(String s, char occur) {
        StringBuffer newString = new StringBuffer();
        char ch;
        for (int i = 0; i < s.length(); i++) {
            ch = s.charAt(i);
            if (ch != occur)
                newString.append(ch);
        }
        return newString.toString();
    }

    public static String removeEscapeCharacters(String inputString) {
        inputString = ParserUtils.removeChars(inputString, '\r');
        inputString = ParserUtils.removeChars(inputString, '\n');
        inputString = ParserUtils.removeChars(inputString, '\t');
        return inputString;
    }

    public static String removeTrailingBlanks(String text) {
        char ch = ' ';
        while (ch == ' ') {
            ch = text.charAt(text.length() - 1);
            if (ch == ' ')
                text = text.substring(0, text.length() - 1);
        }
        return text;
    }

    /**
     * Search given node and pick up any objects of given type.
     * @param node The node to search.
     * @param type The class to search for.
     * @return A node array with the matching nodes.
     */
    public static Node[] findTypeInNode(Node node, Class type)
    {
        NodeFilter filter;
        NodeList ret;
        
        ret = new NodeList ();
        filter = new NodeClassFilter (type);
        node.collectInto (ret, filter);

        return (ret.toNodeArray ());
    }

    /**
     * Split the input string considering as string separator
     * all the not numerical characters
     * with the only exception of the characters specified in charsDoNotBeRemoved param.
     * <BR>For example if you call splitButDigits(&quot;&lt;DIV&gt;  +12.5, +3.4 &lt;/DIV&gt;&quot;, &quot;+.&quot;),
     * <BR>you obtain an array of strings {&quot;+12.5&quot;, &quot;+3.4&quot;} as output (1,2,3,4 and 5 are digits and +,. are chars that do not be removed).
     * @param input The string in input.
     * @param charsDoNotBeRemoved The chars that do not be removed.
     * @return The array of strings as output.
    */
    public static String[] splitButDigits (String input, String charsDoNotBeRemoved)
    {
 	
        ArrayList output = new ArrayList();
        int minCapacity = 0;
        StringBuffer str = new StringBuffer();

        boolean charFound = false;
        boolean toBeAdd = false;
        for (int index=0; index<input.length(); index++)
        {    
            charFound=false;
            for (int charsCount=0; charsCount<charsDoNotBeRemoved.length(); charsCount++)
                if (charsDoNotBeRemoved.charAt(charsCount)==input.charAt(index))
                    charFound=true;
            if ((Character.isDigit(input.charAt(index))) || (charFound))
            {
                str.append(input.charAt(index));
                toBeAdd=false;
            }
            else
                if (!toBeAdd)
                    toBeAdd=true;
            // finished to parse one string
            if (toBeAdd && (str.length()!=0)) {
                minCapacity++;
                output.ensureCapacity(minCapacity);
                if (output.add(str.toString()))
                    str = new StringBuffer();
                else
                    minCapacity--;
            }
        }
        // add the last string
        if (str.length()!=0) {
            minCapacity++;
            output.ensureCapacity(minCapacity);
            if (output.add(str.toString()))
                str = new StringBuffer();
            else
                minCapacity--;
        }

        output.trimToSize();
        Object[] outputObj = output.toArray();
        String[] outputStr = new String[output.size()];
        for (int i=0; i<output.size(); i++)
            outputStr[i] = new String((String) outputObj[i]);
        return outputStr;
        
    }
    
    /**
     * Remove from the input string all the not numerical characters
     * with the only exception of the characters specified in charsDoNotBeRemoved param.
     * <BR>For example if you call trimButDigits(&quot;&lt;DIV&gt;  +12.5 &lt;/DIV&gt;&quot;, &quot;+.&quot;),
     * <BR>you obtain a string &quot;+12.5&quot; as output (1,2 and 5 are digits and +,. are chars that do not be removed).
     * <BR>For example if you call trimButDigits(&quot;&lt;DIV&gt;  +1 2 . 5 &lt;/DIV&gt;&quot;, &quot;+.&quot;),
     * <BR>you obtain a string &quot;+12.5&quot; as output (the spaces between 1 and 2, 2 and ., . and 5 are removed).
     * @param input The string in input.
     * @param charsDoNotBeRemoved The chars that do not be removed.
     * @return The string as output.
    */
    public static String trimButDigits (String input, String charsDoNotBeRemoved)
    {
 	
        StringBuffer output = new StringBuffer();

        boolean charFound=false;
        for (int index=0; index<input.length(); index++)
        {    
            charFound=false;
            for (int charsCount=0; charsCount<charsDoNotBeRemoved.length(); charsCount++)
                if (charsDoNotBeRemoved.charAt(charsCount)==input.charAt(index))
                    charFound=true;
            if ((Character.isDigit(input.charAt(index))) || (charFound))
                output.append(input.charAt(index));
        }

        return output.toString();
        
    }
    
    /**
     * Remove from the beginning and the end of the input string all the not numerical characters
     * with the only exception of the characters specified in charsDoNotBeRemoved param.
     * <BR>The removal process removes only chars at the beginning and at the end of the string.
     * <BR>For example if you call trimButDigitsBeginEnd(&quot;&lt;DIV&gt;  +12.5 &lt;/DIV&gt;&quot;, &quot;+.&quot;),
     * <BR>you obtain a string &quot;+12.5&quot; as output (1,2 and 5 are digits and +,. are chars that do not be removed).
     * <BR>For example if you call trimButDigitsBeginEnd(&quot;&lt;DIV&gt;  +1 2 . 5 &lt;/DIV&gt;&quot;, &quot;+.&quot;),
     * <BR>you obtain a string &quot;+1 2 . 5&quot; as output (the spacess inside the string are not removed).
     * @param input - The string in input.
     * @param charsDoNotBeRemoved - The chars that do not be removed.
     * @return The string as output.
    */
    public static String trimButDigitsBeginEnd (String input, String charsDoNotBeRemoved)
    {
 	
        String output = new String();

        int begin=0;
        int end=input.length()-1;
        boolean charFound=false;
        boolean ok=true;
        for (int index=begin; (index<input.length()) && ok; index++)
        {                
            charFound=false;
            for (int charsCount=0; charsCount<charsDoNotBeRemoved.length(); charsCount++)
                if (charsDoNotBeRemoved.charAt(charsCount)==input.charAt(index))
                    charFound=true;
            if ( (Character.isDigit(input.charAt(index))) || (charFound) )
            {
                begin=index;
                ok=false;
            }
        }
        ok=true;
        for (int index=end; (index>=0) && ok; index--)
        {
            charFound=false;
            for (int charsCount=0; charsCount<charsDoNotBeRemoved.length(); charsCount++)
                if (charsDoNotBeRemoved.charAt(charsCount)==input.charAt(index))
                    charFound=true;
            if ( (Character.isDigit(input.charAt(index))) || (charFound) )
            {
                end=index;
                ok=false;
            }
        }
        output=input.substring(begin,end+1);

        return output;
        
    }
    
    /**
     * Split the input string considering as string separator
     * all the spaces and tabs like chars and
     * the chars specified in the input variable charsToBeRemoved.
     * <BR>For example if you call splitSpaces(&quot;&lt;DIV&gt;  +12.5, +3.4 &lt;/DIV&gt;&quot;, &quot;&lt;>DIV/,&quot;),
     * &lt;BR>you obtain an array of strings {&quot;+12.5&quot;, &quot;+3.4&quot;} as output (space chars and &lt;,&gt;,D,I,V,/ and the comma are chars that must be removed).
     * @param input The string in input.
     * @param charsToBeRemoved The chars to be removed.
     * @return The array of strings as output.
    */
    public static String[] splitSpaces (String input, String charsToBeRemoved)
    {
 	
        ArrayList output = new ArrayList();
        int minCapacity = 0;
        StringBuffer str = new StringBuffer();

        boolean charFound = false;
        boolean toBeAdd = false;
        for (int index=0; index<input.length(); index++)
        {    
            charFound=false;
            for (int charsCount=0; charsCount<charsToBeRemoved.length(); charsCount++)
                if (charsToBeRemoved.charAt(charsCount)==input.charAt(index))
                    charFound=true;
            if (!((Character.isWhitespace(input.charAt(index))) || (Character.isSpaceChar(input.charAt(index))) || (charFound)))
            {
                str.append(input.charAt(index));
                toBeAdd=false;
            }
            else
                if (!toBeAdd)
                    toBeAdd=true;
            // finished to parse one string
            if (toBeAdd && (str.length()!=0)) {
                minCapacity++;
                output.ensureCapacity(minCapacity);
                if (output.add(str.toString()))
                    str = new StringBuffer();
                else
                    minCapacity--;
            }
        }
        // add the last string
        if (str.length()!=0) {
            minCapacity++;
            output.ensureCapacity(minCapacity);
            if (output.add(str.toString()))
                str = new StringBuffer();
            else
                minCapacity--;
        }

        output.trimToSize();
        Object[] outputObj = output.toArray();
        String[] outputStr = new String[output.size()];
        for (int i=0; i<output.size(); i++)
            outputStr[i] = new String((String) outputObj[i]);
        return outputStr;
        
    }

    /**
     * Remove from the input string all the spaces and tabs like chars.
     * Remove also the chars specified in the input variable charsToBeRemoved.
     * <BR>For example if you call trimSpaces(&quot;&lt;DIV&gt;  +12.5 &lt;/DIV&gt;&quot;, &quot;&lt;>DIV/&quot;),
     * <BR>you obtain a string &quot;+12.5&quot; as output (space chars and &lt;,&gt;,D,I,V,/ are chars that must be removed).
     * <BR>For example if you call trimSpaces(&quot;&lt;DIV&gt;  Trim All Spaces Also The Ones Inside The String &lt;/DIV&gt;&quot;, &quot;&lt;>DIV/&quot;),
     * <BR>you obtain a string &quot;TrimAllSpacesAlsoTheOnesInsideTheString&quot; as output (all the spaces inside the string are removed).
     * @param input The string in input.
     * @param charsToBeRemoved The chars to be removed.
     * @return The string as output.
    */
    public static String trimSpaces (String input, String charsToBeRemoved)
    {
 	
        StringBuffer output = new StringBuffer();

        boolean charFound=false;
        for (int index=0; index<input.length(); index++)
        {    
            charFound=false;
            for (int charsCount=0; charsCount<charsToBeRemoved.length(); charsCount++)
                if (charsToBeRemoved.charAt(charsCount)==input.charAt(index))
                    charFound=true;
            if (!((Character.isWhitespace(input.charAt(index))) || (Character.isSpaceChar(input.charAt(index))) || (charFound)))
                output.append(input.charAt(index));
        }

        return output.toString();

    }

    /**
     * Remove from the beginning and the end of the input string all the spaces and tabs like chars.
     * Remove also the chars specified in the input variable charsToBeRemoved.
     * <BR>The removal process removes only chars at the beginning and at the end of the string.
     * <BR>For example if you call trimSpacesBeginEnd(&quot;&lt;DIV&gt;  +12.5 &lt;/DIV&gt;&quot;, &quot;&lt;>DIV/&quot;),
     * <BR>you obtain a string &quot;+12.5&quot; as output (space chars and &lt;,&gt;,D,I,V,/ are chars that must be removed).
     * <BR>For example if you call trimSpacesBeginEnd(&quot;&lt;DIV&gt;  Trim all spaces but not the ones inside the string &lt;/DIV&gt;&quot;, &quot;&lt;>DIV/&quot;),
     * <BR>you obtain a string &quot;Trim all spaces but not the ones inside the string&quot; as output (all the spaces inside the string are preserved).
     * @param input The string in input.
     * @param charsToBeRemoved The chars to be removed.
     * @return The string as output.
    */
    public static String trimSpacesBeginEnd (String input, String charsToBeRemoved)
    {
 	
        String output = new String();

        int begin=0;
        int end=input.length()-1;
        boolean charFound=false;
        boolean ok=true;
        for (int index=begin; (index<input.length()) && ok; index++)
        {                
            charFound=false;
            for (int charsCount=0; charsCount<charsToBeRemoved.length(); charsCount++)
                if (charsToBeRemoved.charAt(charsCount)==input.charAt(index))
                    charFound=true;
            if (!( (Character.isWhitespace(input.charAt(index))) || (Character.isSpaceChar(input.charAt(index))) || (charFound) ))
            {
                begin=index;
                ok=false;
            }
        }
        ok=true;
        for (int index=end; (index>=0) && ok; index--)
        {
            charFound=false;
            for (int charsCount=0; charsCount<charsToBeRemoved.length(); charsCount++)
                if (charsToBeRemoved.charAt(charsCount)==input.charAt(index))
                    charFound=true;
            if (!( (Character.isWhitespace(input.charAt(index))) || (Character.isSpaceChar(input.charAt(index))) || (charFound) ))
            {
                end=index;
                ok=false;
            }
        }
        output=input.substring(begin,end+1);

        return output;
        
    }
    
    /**
     * Split the input string considering as string separator
     * all the characters
     * with the only exception of the characters specified in charsDoNotBeRemoved param.
     * <BR>For example if you call splitButChars(&quot;&lt;DIV&gt;  +12.5, +3.4 &lt;/DIV&gt;&quot;, &quot;+.1234567890&quot;),
     * <BR>you obtain an array of strings {&quot;+12.5&quot;, &quot;+3.4&quot;} as output (+,.,1,2,3,4,5,6,7,8,9,0 are chars that do not be removed).
     * @param input The string in input.
     * @param charsDoNotBeRemoved The chars that do not be removed.
     * @return The array of strings as output.
    */
    public static String[] splitButChars (String input, String charsDoNotBeRemoved)
    {
 	
        ArrayList output = new ArrayList();
        int minCapacity = 0;
        StringBuffer str = new StringBuffer();

        boolean charFound = false;
        boolean toBeAdd = false;
        for (int index=0; index<input.length(); index++)
        {    
            charFound=false;
            for (int charsCount=0; charsCount<charsDoNotBeRemoved.length(); charsCount++)
                if (charsDoNotBeRemoved.charAt(charsCount)==input.charAt(index))
                    charFound=true;
            if (charFound)
            {
                str.append(input.charAt(index));
                toBeAdd=false;
            }
            else
                if (!toBeAdd)
                    toBeAdd=true;
            // finished to parse one string
            if (toBeAdd && (str.length()!=0)) {
                minCapacity++;
                output.ensureCapacity(minCapacity);
                if (output.add(str.toString()))
                    str = new StringBuffer();
                else
                    minCapacity--;
            }
        }
        // add the last string
        if (str.length()!=0) {
            minCapacity++;
            output.ensureCapacity(minCapacity);
            if (output.add(str.toString()))
                str = new StringBuffer();
            else
                minCapacity--;
        }

        output.trimToSize();
        Object[] outputObj = output.toArray();
        String[] outputStr = new String[output.size()];
        for (int i=0; i<output.size(); i++)
            outputStr[i] = new String((String) outputObj[i]);
        return outputStr;
        
    }
    
    /**
     * Remove from the input string all the characters
     * with the only exception of the characters specified in charsDoNotBeRemoved param.
     * <BR>For example if you call trimButChars(&quot;&lt;DIV&gt;  +12.5 &lt;/DIV&gt;&quot;, &quot;+.1234567890&quot;),
     * <BR>you obtain a string &quot;+12.5&quot; as output (+,.,1,2,3,4,5,6,7,8,9,0 are chars that do not be removed).
     * <BR>For example if you call trimButChars(&quot;&lt;DIV&gt;  +1 2 . 5 &lt;/DIV&gt;&quot;, &quot;+.1234567890&quot;),
     * <BR>you obtain a string &quot;+12.5&quot; as output (the spaces between 1 and 2, 2 and ., . and 5 are removed).
     * @param input The string in input.
     * @param charsDoNotBeRemoved The chars that do not be removed.
     * @return The string as output.
    */
    public static String trimButChars (String input, String charsDoNotBeRemoved)
    {
 	
        StringBuffer output = new StringBuffer();

        boolean charFound=false;
        for (int index=0; index<input.length(); index++)
        {    
            charFound=false;
            for (int charsCount=0; charsCount<charsDoNotBeRemoved.length(); charsCount++)
                if (charsDoNotBeRemoved.charAt(charsCount)==input.charAt(index))
                    charFound=true;
            if (charFound)
                output.append(input.charAt(index));
        }
        
        return output.toString();
        
    }
    
    /**
     * Remove from the beginning and the end of the input string all the characters
     * with the only exception of the characters specified in charsDoNotBeRemoved param.
     * <BR>The removal process removes only chars at the beginning and at the end of the string.
     * <BR>For example if you call trimButCharsBeginEnd(&quot;&lt;DIV&gt;  +12.5 &lt;/DIV&gt;&quot;, &quot;+.1234567890&quot;),
     * <BR>you obtain a string &quot;+12.5&quot; as output (+,.,1,2,3,4,5,6,7,8,9,0 are chars that do not be removed).
     * <BR>For example if you call trimButCharsBeginEnd(&quot;&lt;DIV&gt;  +1 2 . 5 &lt;/DIV&gt;&quot;, &quot;+.1234567890&quot;),
     * <BR>you obtain a string &quot;+1 2 . 5&quot; as output (the spaces inside the string are not removed).
     * @param input The string in input.
     * @param charsDoNotBeRemoved The chars that do not be removed.
     * @return The string as output.
    */
    public static String trimButCharsBeginEnd (String input, String charsDoNotBeRemoved)
    {
 	
        String output = new String();

        int begin=0;
        int end=input.length()-1;
        boolean charFound=false;
        boolean ok=true;
        for (int index=begin; (index<input.length()) && ok; index++)
        {                
            charFound=false;
            for (int charsCount=0; charsCount<charsDoNotBeRemoved.length(); charsCount++)
                if (charsDoNotBeRemoved.charAt(charsCount)==input.charAt(index))
                    charFound=true;
            if (charFound)
            {
                begin=index;
                ok=false;
            }
        }
        ok=true;
        for (int index=end; (index>=0) && ok; index--)
        {
            charFound=false;
            for (int charsCount=0; charsCount<charsDoNotBeRemoved.length(); charsCount++)
                if (charsDoNotBeRemoved.charAt(charsCount)==input.charAt(index))
                    charFound=true;
            if (charFound)
            {
                end=index;
                ok=false;
            }
        }
        output=input.substring(begin,end+1);

        return output;
        
    }

    /**
     * Split the input string considering as string separator
     * the chars specified in the input variable charsToBeRemoved.
     * <BR>For example if you call splitChars(&quot;&lt;DIV&gt;  +12.5, +3.4 &lt;/DIV&gt;&quot;, &quot; <>DIV/,&quot;),
     * <BR>you obtain an array of strings {&quot;+12.5&quot;, &quot;+3.4&quot;} as output (space chars and &lt;,&gt;,D,I,V,/ and the comma are chars that must be removed).
     * @param input The string in input.
     * @param charsToBeRemoved The chars to be removed.
     * @return The array of strings as output.
    */
    public static String[] splitChars (String input, String charsToBeRemoved)
    {
 	
        ArrayList output = new ArrayList();
        int minCapacity = 0;
        StringBuffer str = new StringBuffer();

        boolean charFound = false;
        boolean toBeAdd = false;
        for (int index=0; index<input.length(); index++)
        {    
            charFound=false;
            for (int charsCount=0; charsCount<charsToBeRemoved.length(); charsCount++)
                if (charsToBeRemoved.charAt(charsCount)==input.charAt(index))
                    charFound=true;
            if (!(charFound))
            {
                str.append(input.charAt(index));
                toBeAdd=false;
            }
            else
                if (!toBeAdd)
                    toBeAdd=true;
            // finished to parse one string
            if (toBeAdd && (str.length()!=0)) {
                minCapacity++;
                output.ensureCapacity(minCapacity);
                if (output.add(str.toString()))
                    str = new StringBuffer();
                else
                    minCapacity--;
            }
        }
        // add the last string
        if (str.length()!=0) {
            minCapacity++;
            output.ensureCapacity(minCapacity);
            if (output.add(str.toString()))
                str = new StringBuffer();
            else
                minCapacity--;
        }

        output.trimToSize();
        Object[] outputObj = output.toArray();
        String[] outputStr = new String[output.size()];
        for (int i=0; i<output.size(); i++)
            outputStr[i] = new String((String) outputObj[i]);
        return outputStr;
        
    }

    /**
     * Remove from the input string all the chars specified in the input variable charsToBeRemoved.
     * <BR>For example if you call trimChars(&quot;&lt;DIV&gt;  +12.5 &lt;/DIV&gt;&quot;, &quot;<>DIV/ &quot;),
     * <BR>you obtain a string &quot;+12.5&quot; as output (&lt;,&gt;,D,I,V,/ and space char are chars that must be removed).
     * <BR>For example if you call trimChars(&quot;&lt;DIV&gt;  Trim All Chars Also The Ones Inside The String &lt;/DIV&gt;&quot;, &quot;<>DIV/ &quot;),
     * <BR>you obtain a string &quot;TrimAllCharsAlsoTheOnesInsideTheString&quot; as output (all the spaces inside the string are removed).
     * @param input The string in input.
     * @param charsToBeRemoved The chars to be removed.
     * @return The string as output.
    */
    public static String trimChars (String input, String charsToBeRemoved)
    {
 	
        StringBuffer output = new StringBuffer();

        boolean charFound=false;
        for (int index=0; index<input.length(); index++)
        {    
            charFound=false;
            for (int charsCount=0; charsCount<charsToBeRemoved.length(); charsCount++)
                if (charsToBeRemoved.charAt(charsCount)==input.charAt(index))
                    charFound=true;
            if (!(charFound))
                output.append(input.charAt(index));
        }

        return output.toString();

    }

    /**
     * Remove from the beginning and the end of the input string all the chars specified in the input variable charsToBeRemoved.
     * <BR>The removal process removes only chars at the beginning and at the end of the string.
     * <BR>For example if you call trimCharsBeginEnd(&quot;&lt;DIV&gt;  +12.5 &lt;/DIV&gt;&quot;, &quot;<>DIV/ &quot;),
     * <BR>you obtain a string &quot;+12.5&quot; as output (' ' is a space char and &lt;,&gt;,D,I,V,/ are chars that must be removed).
     * <BR>For example if you call trimCharsBeginEnd(&quot;&lt;DIV&gt;  Trim all spaces but not the ones inside the string &lt;/DIV&gt;&quot;, &quot;<>DIV/ &quot;),
     * <BR>you obtain a string &quot;Trim all spaces but not the ones inside the string&quot; as output (all the spaces inside the string are preserved).
     * @param input The string in input.
     * @param charsToBeRemoved The chars to be removed.
     * @return The string as output.
    */
    public static String trimCharsBeginEnd (String input, String charsToBeRemoved)
    {
 	
        String output = new String();

        int begin=0;
        int end=input.length()-1;
        boolean charFound=false;
        boolean ok=true;
        for (int index=begin; (index<input.length()) && ok; index++)
        {                
            charFound=false;
            for (int charsCount=0; charsCount<charsToBeRemoved.length(); charsCount++)
                if (charsToBeRemoved.charAt(charsCount)==input.charAt(index))
                    charFound=true;
            if (!(charFound))
            {
                begin=index;
                ok=false;
            }
        }
        ok=true;
        for (int index=end; (index>=0) && ok; index--)
        {
            charFound=false;
            for (int charsCount=0; charsCount<charsToBeRemoved.length(); charsCount++)
                if (charsToBeRemoved.charAt(charsCount)==input.charAt(index))
                    charFound=true;
            if (!(charFound))
            {
                end=index;
                ok=false;
            }
        }
        output=input.substring(begin,end+1);

        return output;
        
    }

    /**
     * Split the input string in a string array,
     * considering the tags as delimiter for splitting.
     * @see ParserUtils#splitTags (String input, String[] tags, boolean recursive, boolean insideTag).
     */
    public static String[] splitTags (String input, String[] tags)
        throws ParserException, UnsupportedEncodingException
    {
        return splitTags (input, tags, true, true);
    }
    
    /**
     * Split the input string in a string array,
     * considering the tags as delimiter for splitting.
     * <BR>For example if you call splitTags(&quot;Begin &lt;DIV&gt;&lt;DIV&gt;  +12.5 &lt;/DIV&gt;&lt;/DIV&gt; ALL OK&quot;, new String[] {&quot;DIV&quot;}),
     * <BR>you obtain a string array {&quot;Begin &quot;, &quot; ALL OK&quot;} as output (splitted &lt;DIV&gt; tags and their content recursively).
     * <BR>For example if you call splitTags(&quot;Begin &lt;DIV&gt;&lt;DIV&gt;  +12.5 &lt;/DIV&gt;&lt;/DIV&gt; ALL OK&quot;, new String[] {&quot;DIV&quot;}, false, false),
     * <BR>you obtain a string array {&quot;Begin &quot;, &quot;&lt;DIV&gt;  +12.5 &lt;/DIV&gt;&quot;, &quot; ALL OK&quot;} as output (splitted &lt;DIV&gt; tags and not their content and no recursively).
     * <BR>For example if you call splitTags(&quot;Begin &lt;DIV&gt;&lt;DIV&gt;  +12.5 &lt;/DIV&gt;&lt;/DIV&gt; ALL OK&quot;, new String[] {&quot;DIV&quot;}, true, false),
     * <BR>you obtain a string array {&quot;Begin &quot;, &quot;  +12.5 &quot;, &quot; ALL OK&quot;} as output (splitted &lt;DIV&gt; tags and not their content recursively).
     * <BR>For example if you call splitTags(&quot;Begin &lt;DIV&gt;&lt;DIV&gt;  +12.5 &lt;/DIV&gt;&lt;/DIV&gt; ALL OK&quot;, new String[] {&quot;DIV&quot;}, false, true),
     * <BR>you obtain a string array {&quot;Begin &quot;, &quot; ALL OK&quot;} as output (splitted &lt;DIV&gt; tags and their content).
     * @param input The string in input.
     * @param tags The tags to be used as splitting delimiter.
     * @param recursive Optional parameter (true if not present), if true delete all the tags recursively.
     * @param insideTag Optional parameter (true if not present), if true delete also the content of the tags.
     * @return The string array containing the strings delimited by tags.
     */
    public static String[] splitTags (String input, String[] tags, boolean recursive, boolean insideTag)
        throws ParserException, UnsupportedEncodingException
    {
 	
        ArrayList outputArrayList = new ArrayList();
        int minCapacity = 0;
        String output = new String();
        String inputModified = new String(input);
        String[] outputStr = new String[] {};
        
        String dummyString = createDummyString (' ', input.length());
        
        // loop inside the different tags to be trimmed
        for (int i=0; i<tags.length; i++)
        {
            
            // loop inside the tags of the same type
            NodeList links = getLinks (inputModified, tags[i], recursive);
            for (int j=0; j<links.size(); j++)
            {
                CompositeTag beginTag = (CompositeTag)links.elementAt(j);
                Tag endTag = beginTag.getEndTag();

                // positions of begin and end tags
                int beginTagBegin = beginTag.getStartPosition ();
                int endTagBegin = beginTag.getEndPosition ();
                int beginTagEnd = endTag.getStartPosition ();
                int endTagEnd = endTag.getEndPosition ();

                if (insideTag)
                {
                    dummyString = modifyDummyString (new String(dummyString), beginTagBegin, endTagEnd);
                }
                else
                {
                    dummyString = modifyDummyString (new String(dummyString), beginTagBegin, endTagBegin);
                    dummyString = modifyDummyString (new String(dummyString), beginTagEnd, endTagEnd);
                }
            }
            for (int k=dummyString.indexOf(' '); (k<dummyString.length()) && (k!=-1);)
            {
                int kNew = dummyString.indexOf('*',k);
                if (kNew!=-1)
                {
                    output = inputModified.substring(k,kNew);
                    k = dummyString.indexOf(' ',kNew);
                    
                    minCapacity++;
                    outputArrayList.ensureCapacity(minCapacity);
                    if (outputArrayList.add(output))
                        output = new String();
                    else
                        minCapacity--;
                }
                else
                {
                    output = inputModified.substring(k,dummyString.length());
                    k = kNew;
                    
                    minCapacity++;
                    outputArrayList.ensureCapacity(minCapacity);
                    if (outputArrayList.add(output))
                        output = new String();
                    else
                        minCapacity--;
                }
            }
            StringBuffer outputStringBuffer = new StringBuffer();
            outputArrayList.trimToSize();
            Object[] outputObj = outputArrayList.toArray();
            outputStr = new String[outputArrayList.size()];
            for (int j=0; j<outputArrayList.size(); j++)
            {
                outputStr[j] = new String((String) outputObj[j]);
                outputStringBuffer.append(outputStr[j]);
            }
            outputArrayList = new ArrayList();
            inputModified = new String(outputStringBuffer.toString());
            dummyString = createDummyString (' ', inputModified.length());
        }
        
        return outputStr;
        
    }
    
    /**
     * Split the input string in a string array,
     * considering the tags as delimiter for splitting.
     * <BR>Use Class class as input parameter
     * instead of tags[] string array.
     * @see ParserUtils#splitTags (String input, String[] tags, boolean recursive, boolean insideTag).
     */
    public static String[] splitTags (String input, Class nodeType)
        throws ParserException, UnsupportedEncodingException
    {
        return splitTags (input, new NodeClassFilter (nodeType), true, true);
    }
    
    /**
     * Split the input string in a string array,
     * considering the tags as delimiter for splitting.
     * <BR>Use Class class as input parameter
     * instead of tags[] string array.
     * @see ParserUtils#splitTags (String input, String[] tags, boolean recursive, boolean insideTag).
     */
    public static String[] splitTags (String input, Class nodeType, boolean recursive, boolean insideTag)
        throws ParserException, UnsupportedEncodingException
    {
        return splitTags (input, new NodeClassFilter (nodeType), recursive, insideTag);
    }
 	
    /**
     * Split the input string in a string array,
     * considering the tags as delimiter for splitting.
     * <BR>Use NodeFilter class as input parameter
     * instead of tags[] string array.
     * @see ParserUtils#splitTags (String input, String[] tags, boolean recursive, boolean insideTag).
     */
    public static String[] splitTags (String input, NodeFilter filter)
        throws ParserException, UnsupportedEncodingException
    {
        return splitTags (input, filter, true, true);
    }
    
    /**
     * Split the input string in a string array,
     * considering the tags as delimiter for splitting.
     * <BR>Use NodeFilter class as input parameter
     * instead of tags[] string array.
     * @see ParserUtils#splitTags (String input, String[] tags, boolean recursive, boolean insideTag).
     */
    public static String[] splitTags (String input, NodeFilter filter, boolean recursive, boolean insideTag)
        throws ParserException, UnsupportedEncodingException
    {
 	
        ArrayList outputArrayList = new ArrayList();
        int minCapacity = 0;
        String output = new String();
        
        String dummyString = createDummyString (' ', input.length());

        // loop inside the tags of the same type
        NodeList links = getLinks (input, filter, recursive);
        for (int j=0; j<links.size(); j++)
        {
            CompositeTag beginTag = (CompositeTag)links.elementAt(j);
            Tag endTag = beginTag.getEndTag();

            // positions of begin and end tags
            int beginTagBegin = beginTag.getStartPosition ();
            int endTagBegin = beginTag.getEndPosition ();
            int beginTagEnd = endTag.getStartPosition ();
            int endTagEnd = endTag.getEndPosition ();

            if (insideTag)
            {
                dummyString = modifyDummyString (new String(dummyString), beginTagBegin, endTagEnd);
            }
            else
            {
                dummyString = modifyDummyString (new String(dummyString), beginTagBegin, endTagBegin);
                dummyString = modifyDummyString (new String(dummyString), beginTagEnd, endTagEnd);
            }
        }
        for (int k=dummyString.indexOf(' '); (k<dummyString.length()) && (k!=-1);)
        {
            int kNew = dummyString.indexOf('*',k);
            if (kNew!=-1)
            {
                output = input.substring(k,kNew);
                k = dummyString.indexOf(' ',kNew);
                    
                minCapacity++;
                outputArrayList.ensureCapacity(minCapacity);
                if (outputArrayList.add(output))
                    output = new String();
                else
                    minCapacity--;
            }
            else
            {
                output = input.substring(k,dummyString.length());
                k = kNew;
                    
                minCapacity++;
                outputArrayList.ensureCapacity(minCapacity);
                if (outputArrayList.add(output))
                    output = new String();
                else
                    minCapacity--;
            }
            
        }
        
        outputArrayList.trimToSize();
        Object[] outputObj = outputArrayList.toArray();
        String[] outputStr = new String[outputArrayList.size()];
        for (int i=0; i<outputArrayList.size(); i++)
            outputStr[i] = new String((String) outputObj[i]);
        return outputStr;
        
    }

    /**
     * Trim the input string, removing all the tags in the input string.
     * <BR>The method trims all the substrings included in the input string of the following type:
     * &quot;&lt;XXX&gt;&quot;, where XXX could be a string of any type.
     * <BR>If you set to true the inside parameter, the method deletes also the YYY string in the following input string:
     * &quot;&lt;XXX&gt;YYY&lt;ZZZ&gt;&quot;, note that ZZZ is not necessary the closing tag of XXX.
     * @param input The string in input.
     * @param inside If true, it forces the method to delete also what is inside the tags.
     * @return The string without tags.
     */
    public static String trimAllTags (String input, boolean inside)
    {
 	
        StringBuffer output = new StringBuffer();

        if (inside) {
            if ((input.indexOf('<')==-1) || (input.lastIndexOf('>')==-1) || (input.lastIndexOf('>')<input.indexOf('<'))) {
                output.append(input);
            } else {
                output.append(input.substring(0, input.indexOf('<')));
                output.append(input.substring(input.lastIndexOf('>')+1, input.length()));
            }
        } else {
            boolean write = true;
            for (int index=0; index<input.length(); index++)
            {    
                if (input.charAt(index)=='<' && write)
                    write = false;
                if (write)
                    output.append(input.charAt(index));
                if (input.charAt(index)=='>' && (!write))
                    write = true;
            }
        }

        return output.toString();
    }
    

    /**
     * Trim all tags in the input string and
     * return a string like the input one
     * without the tags and their content.
     * @see ParserUtils#trimTags (String input, String[] tags, boolean recursive, boolean insideTag).
     */
    public static String trimTags (String input, String[] tags)
        throws ParserException, UnsupportedEncodingException
    {
        return trimTags (input, tags, true, true);
    }
    
    /**
     * Trim all tags in the input string and
     * return a string like the input one
     * without the tags and their content (optional).
     * <BR>For example if you call trimTags(&quot;&lt;DIV&gt;&lt;DIV&gt;  +12.5 &lt;/DIV&gt;&lt;/DIV&gt; ALL OK&quot;, new String[] {&quot;DIV&quot;}),
     * <BR>you obtain a string &quot; ALL OK&quot; as output (trimmed &lt;DIV&gt; tags and their content recursively).
     * <BR>For example if you call trimTags(&quot;&lt;DIV&gt;&lt;DIV&gt;  +12.5 &lt;/DIV&gt;&lt;/DIV&gt; ALL OK&quot;, new String[] {&quot;DIV&quot;}, false, false),
     * <BR>you obtain a string &quot;&lt;DIV&gt;  +12.5 &lt;/DIV&gt; ALL OK&quot; as output (trimmed &lt;DIV&gt; tags and not their content and no recursively).
     * <BR>For example if you call trimTags(&quot;&lt;DIV&gt;&lt;DIV&gt;  +12.5 &lt;/DIV&gt;&lt;/DIV&gt; ALL OK&quot;, new String[] {&quot;DIV&quot;}, true, false),
     * <BR>you obtain a string &quot;  +12.5  ALL OK&quot; as output (trimmed &lt;DIV&gt; tags and not their content recursively).
     * <BR>For example if you call trimTags(&quot;&lt;DIV&gt;&lt;DIV&gt;  +12.5 &lt;/DIV&gt;&lt;/DIV&gt; ALL OK&quot;, new String[] {&quot;DIV&quot;}, false, true),
     * <BR>you obtain a string &quot; ALL OK&quot; as output (trimmed &lt;DIV&gt; tags and their content).
     * @param input The string in input.
     * @param tags The tags to be removed.
     * @param recursive Optional parameter (true if not present), if true delete all the tags recursively.
     * @param insideTag Optional parameter (true if not present), if true delete also the content of the tags.
     * @return The string without tags.
     */
    public static String trimTags (String input, String[] tags, boolean recursive, boolean insideTag)
        throws ParserException, UnsupportedEncodingException
    {
 	
        StringBuffer output = new StringBuffer();
        String inputModified = new String(input);
        String dummyString = createDummyString (' ', input.length());
            
        // loop inside the different tags to be trimmed
        for (int i=0; i<tags.length; i++)
        {
            output = new StringBuffer();
            
            // loop inside the tags of the same type
            NodeList links = getLinks (inputModified, tags[i], recursive);
            for (int j=0; j<links.size(); j++)
            {
                CompositeTag beginTag = (CompositeTag)links.elementAt(j);
                Tag endTag = beginTag.getEndTag();

                // positions of begin and end tags
                int beginTagBegin = beginTag.getStartPosition ();
                int endTagBegin = beginTag.getEndPosition ();
                int beginTagEnd = endTag.getStartPosition ();
                int endTagEnd = endTag.getEndPosition ();


                if (insideTag)
                {
                    dummyString = modifyDummyString (new String(dummyString), beginTagBegin, endTagEnd);
                }
                else
                {
                    dummyString = modifyDummyString (new String(dummyString), beginTagBegin, endTagBegin);
                    dummyString = modifyDummyString (new String(dummyString), beginTagEnd, endTagEnd);
                }
            }
            for (int k=dummyString.indexOf(' '); (k<dummyString.length()) && (k!=-1);)
            {
                int kNew = dummyString.indexOf('*',k);
                if (kNew!=-1)
                {
                    output = output.append(inputModified.substring(k,kNew));
                    k = dummyString.indexOf(' ',kNew);
                }
                else
                {
                    output = output.append(inputModified.substring(k,dummyString.length()));
                    k = kNew;
                }
            }
            inputModified = new String(output);
            dummyString = createDummyString (' ', inputModified.length());
        }
        
        return output.toString();
        
    }
    
    /**
     * Trim all tags in the input string and
     * return a string like the input one
     * without the tags and their content.
     * <BR>Use Class class as input parameter
     * instead of tags[] string array.
     * @see ParserUtils#trimTags (String input, String[] tags, boolean recursive, boolean insideTag).
     */
    public static String trimTags (String input, Class nodeType)
        throws ParserException, UnsupportedEncodingException
    {
        return trimTags (input, new NodeClassFilter (nodeType), true, true);
    }

    /**
     * Trim all tags in the input string and
     * return a string like the input one
     * without the tags and their content (optional).
     * <BR>Use Class class as input parameter
     * instead of tags[] string array.
     * @see ParserUtils#trimTags (String input, String[] tags, boolean recursive, boolean insideTag).
     */
    public static String trimTags (String input, Class nodeType, boolean recursive, boolean insideTag)
        throws ParserException, UnsupportedEncodingException
    {
        return trimTags (input, new NodeClassFilter (nodeType), recursive, insideTag);
    }

    /**
     * Trim all tags in the input string and
     * return a string like the input one
     * without the tags and their content.
     * <BR>Use NodeFilter class as input parameter
     * instead of tags[] string array.
     * @see ParserUtils#trimTags (String input, String[] tags, boolean recursive, boolean insideTag).
     */
    public static String trimTags (String input, NodeFilter filter)
        throws ParserException, UnsupportedEncodingException
    {
        return trimTags (input, filter, true, true);
    }
    
    /**
     * Trim all tags in the input string and
     * return a string like the input one
     * without the tags and their content (optional).
     * <BR>Use NodeFilter class as input parameter
     * instead of tags[] string array.
     * @see ParserUtils#trimTags (String input, String[] tags, boolean recursive, boolean insideTag).
     */
    public static String trimTags (String input, NodeFilter filter, boolean recursive, boolean insideTag)
        throws ParserException, UnsupportedEncodingException
    {
 	
        StringBuffer output = new StringBuffer();
        
        String dummyString = createDummyString (' ', input.length());

        // loop inside the tags of the same type
        NodeList links = getLinks (input, filter, recursive);
        for (int j=0; j<links.size(); j++)
        {
            CompositeTag beginTag = (CompositeTag)links.elementAt(j);
            Tag endTag = beginTag.getEndTag();

            // positions of begin and end tags
            int beginTagBegin = beginTag.getStartPosition ();
            int endTagBegin = beginTag.getEndPosition ();
            int beginTagEnd = endTag.getStartPosition ();
            int endTagEnd = endTag.getEndPosition ();

            if (insideTag)
            {
                dummyString = modifyDummyString (new String(dummyString), beginTagBegin, endTagEnd);
            }
            else
            {
                dummyString = modifyDummyString (new String(dummyString), beginTagBegin, endTagBegin);
                dummyString = modifyDummyString (new String(dummyString), beginTagEnd, endTagEnd);
            }
        }
        for (int k=dummyString.indexOf(' '); (k<dummyString.length()) && (k!=-1);)
        {
            int kNew = dummyString.indexOf('*',k);
            if (kNew!=-1)
            {
                output = output.append(input.substring(k,kNew));
                k = dummyString.indexOf(' ',kNew);
            }
            else
            {
                output = output.append(input.substring(k,dummyString.length()));
                k = kNew;
            }
            
        }
        
        return output.toString();
        
    }
    
    /**
     * Create a Parser Object having a String Object as input (instead of a url or a string representing the url location).
     * <BR>The string will be parsed as it would be a file.
     * @param input The string in input.
     * @return The Parser Object with the string as input stream.
     */
    public static Parser createParserParsingAnInputString (String input)
        throws ParserException, UnsupportedEncodingException
    {
 	
        Parser parser = new Parser();
        Lexer lexer = new Lexer();
        Page page = new Page(input);
        lexer.setPage(page);
        parser.setLexer(lexer);
        
        return parser;
        
    }

    private static NodeList getLinks (String output, String tag, boolean recursive)
        throws ParserException, UnsupportedEncodingException
    {
        
        Parser parser = new Parser();
        NodeFilter filterLink = new TagNameFilter (tag);
        NodeList links = new NodeList ();
        parser = createParserParsingAnInputString(output);
        links = parser.extractAllNodesThatMatch(filterLink);

        // loop to remove tags added recursively
        // so if you have selected 'not recursive option'
        // you have only the tag container and not the contained tags.
        if (!recursive)
        {
            for (int j=0; j<links.size(); j++)
            {
                CompositeTag jStartTag = (CompositeTag)links.elementAt(j);
                Tag jEndTag = jStartTag.getEndTag();
                int jStartTagBegin = jStartTag.getStartPosition ();
                int jEndTagEnd = jEndTag.getEndPosition ();
                for (int k=0; k<links.size(); k++)
                {
                    CompositeTag kStartTag = (CompositeTag)links.elementAt(k);
                    Tag kEndTag = kStartTag.getEndTag();
                    int kStartTagBegin = kStartTag.getStartPosition ();
                    int kEndTagEnd = kEndTag.getEndPosition ();
                    if ((k!=j) && (kStartTagBegin>jStartTagBegin) && (kEndTagEnd<jEndTagEnd))
                    {
                        links.remove(k);
                        k--;
                        j--;
                    }
                }
            }
        }
        
        return links;
        
    }
    
    private static NodeList getLinks (String output, NodeFilter filter, boolean recursive)
        throws ParserException, UnsupportedEncodingException
    {
        
        Parser parser = new Parser();
        NodeList links = new NodeList ();
        parser = createParserParsingAnInputString(output);
        links = parser.extractAllNodesThatMatch(filter);

        // loop to remove tags added recursively
        // so if you have selected 'not recursive option'
        // you have only the tag container and not the contained tags.
        if (!recursive)
        {
            for (int j=0; j<links.size(); j++)
            {
                CompositeTag jStartTag = (CompositeTag)links.elementAt(j);
                Tag jEndTag = jStartTag.getEndTag();
                int jStartTagBegin = jStartTag.getStartPosition ();
                int jEndTagEnd = jEndTag.getEndPosition ();
                for (int k=0; k<links.size(); k++)
                {
                    CompositeTag kStartTag = (CompositeTag)links.elementAt(k);
                    Tag kEndTag = kStartTag.getEndTag();
                    int kStartTagBegin = kStartTag.getStartPosition ();
                    int kEndTagEnd = kEndTag.getEndPosition ();
                    if ((k!=j) && (kStartTagBegin>jStartTagBegin) && (kEndTagEnd<jEndTagEnd))
                    {
                        links.remove(k);
                        k--;
                        j--;
                    }
                }
            }
        }
        
        return links;
        
    }
    
    private static String createDummyString (char fillingChar, int length)
    {
        StringBuffer dummyStringBuffer = new StringBuffer();
        for (int j=0; j<length; j++)
            dummyStringBuffer = dummyStringBuffer.append(fillingChar);
        return new String(dummyStringBuffer);
    }
    
    private static String modifyDummyString (String dummyString, int beginTag, int endTag)
    {
        String dummyStringInterval = createDummyString ('*', endTag-beginTag);
        return new String(dummyString.substring(0, beginTag) + dummyStringInterval + dummyString.substring(endTag, dummyString.length()));
    }
    
}
