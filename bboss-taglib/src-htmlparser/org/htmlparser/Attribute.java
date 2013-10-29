// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/Attribute.java,v $
// $Author: derrickoswald $
// $Date: 2005/05/15 11:49:03 $
// $Revision: 1.7 $
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

package org.htmlparser;

import java.io.Serializable;

/**
 * An attribute within a tag.
 * Holds the name, assignment string, value and quote character.
 * <p>
 * This class was made deliberately simple. Except for
 * {@link #setRawValue RawValue}, the properties are completely orthogonal,
 * that is: each property is independant of the others. This means you have
 * enough rope here to hang yourself, and it's very easy to create
 * malformed HTML. Where it's obvious, warnings and notes have been provided
 * in the setters javadocs, but it is up to you -- the programmer --
 * to ensure that the contents of the four fields will yield valid HTML
 * (if that's what you want).
 * <p>
 * Be especially mindful of quotes and assignment strings. These are handled
 * by the constructors where it's obvious, but in general, you need to set
 * them explicitly when building an attribute. For example to construct
 * the attribute <b><code>label="A multi word value."</code></b> you could use:
 * <pre>
 *     attribute = new Attribute ();
 *     attribute.setName ("label");
 *     attribute.setAssignment ("=");
 *     attribute.setValue ("A multi word value.");
 *     attribute.setQuote ('"');
 * </pre>
 * or
 * <pre>
 *     attribute = new Attribute ();
 *     attribute.setName ("label");
 *     attribute.setAssignment ("=");
 *     attribute.setRawValue ("A multi word value.");
 * </pre>
 * or
 * <pre>
 *     attribute = new Attribute ("label", "A multi word value.");
 * </pre>
 * Note that the assignment value and quoting need to be set separately when
 * building the attribute from scratch using the properties.
 * <p>
 * <table width="100.0%" align="Center" border="1">
 *   <caption>Valid States for Attributes.
 *   <tr>
 *     <th align="Center">Description</th>
 *     <th align="Center">toString()</th>
 *     <th align="Center">Name</th>
 *     <th align="Center">Assignment</th>
 *     <th align="Center">Value</th>
 *     <th align="Center">Quote</th>
 *   </tr>
 *   <tr>
 *     <td align="Center">whitespace attribute</td>
 *     <td align="Center">value</td>
 *     <td align="Center"><code>null</code></td>
 *     <td align="Center"><code>null</code></td>
 *     <td align="Center">"value"</td>
 *     <td align="Center"><code>0</code></td>
 *   </tr>
 *   <tr>
 *     <td align="Center">standalone attribute</td>
 *     <td align="Center">name</td>
 *     <td align="Center">"name"</td>
 *     <td align="Center"><code>null</code></td>
 *     <td align="Center"><code>null</code></td>
 *     <td align="Center"><code>0</code></td>
 *   </tr>
 *   <tr>
 *     <td align="Center">empty attribute</td>
 *     <td align="Center">name=</td>
 *     <td align="Center">"name"</td>
 *     <td align="Center">"="</td>
 *     <td align="Center"><code>null</code></td>
 *     <td align="Center"><code>0</code></td>
 *   </tr>
 *   <tr>
 *     <td align="Center">empty single quoted attribute</td>
 *     <td align="Center">name=''</td>
 *     <td align="Center">"name"</td>
 *     <td align="Center">"="</td>
 *     <td align="Center"><code>null</code></td>
 *     <td align="Center"><code>'</code></td>
 *   </tr>
 *   <tr>
 *     <td align="Center">empty double quoted attribute</td>
 *     <td align="Center">name=""</td>
 *     <td align="Center">"name"</td>
 *     <td align="Center">"="</td>
 *     <td align="Center"><code>null</code></td>
 *     <td align="Center"><code>"</code></td>
 *   </tr>
 *   <tr>
 *     <td align="Center">naked attribute</td>
 *     <td align="Center">name=value</td>
 *     <td align="Center">"name"</td>
 *     <td align="Center">"="</td>
 *     <td align="Center">"value"</td>
 *     <td align="Center"><code>0</code></td>
 *   </tr>
 *   <tr>
 *     <td align="Center">single quoted attribute</td>
 *     <td align="Center">name='value'</td>
 *     <td align="Center">"name"</td>
 *     <td align="Center">"="</td>
 *     <td align="Center">"value"</td>
 *     <td align="Center"><code>'</code></td>
 *   </tr>
 *   <tr>
 *     <td align="Center">double quoted attribute</td>
 *     <td align="Center">name="value"</td>
 *     <td align="Center">"name"</td>
 *     <td align="Center">"="</td>
 *     <td align="Center">"value"</td>
 *     <td align="Center"><code>"</code></td>
 *   </tr>
 * </table>
 * <br>In words:
 * <br>If Name is null, and Assignment is null, and Quote is zero,
 *   it's whitepace and Value has the whitespace text -- value
 * <br>If Name is not null, and both Assignment and Value are null
 *   it's a standalone attribute -- name
 * <br>If Name is not null, and Assignment is an equals sign, and Quote is zero
 *   it's an empty attribute -- name=
 * <br>If Name is not null, and Assignment is an equals sign,
 *   and Value is "" or null, and Quote is '
 *   it's an empty single quoted attribute -- name=''
 * <br>If Name is not null, and Assignment is an equals sign,
 *   and Value is "" or null, and Quote is "
 *   it's an empty double quoted attribute -- name=""
 * <br>If Name is not null, and Assignment is an equals sign,
 *   and Value is something, and Quote is zero
 *   it's a naked attribute -- name=value
 * <br>If Name is not null, and Assignment is an equals sign,
 *   and Value is something, and Quote is '
 *   it's a single quoted attribute -- name='value'
 * <br>If Name is not null, and Assignment is an equals sign,
 *   and Value is something, and Quote is "
 *   it's a double quoted attribute -- name="value"
 * <br>All other states are invalid HTML.
 * <p>
 * From the <a href="http://www.w3.org/TR/html4/intro/sgmltut.html#h-3.2.2">
 * HTML 4.01 Specification, W3C Recommendation 24 December 1999</a>
 * http://www.w3.org/TR/html4/intro/sgmltut.html#h-3.2.2:<p>
 * <cite>
 * 3.2.2 Attributes<p>
 * Elements may have associated properties, called attributes, which may
 * have values (by default, or set by authors or scripts). Attribute/value
 * pairs appear before the final ">" of an element's start tag. Any number
 * of (legal) attribute value pairs, separated by spaces, may appear in an
 * element's start tag. They may appear in any order.<p>
 * In this example, the id attribute is set for an H1 element:
 * <pre>
 * <code>
 * {@.html
 *  <H1 id="section1">
 *  This is an identified heading thanks to the id attribute
 *  </H1>}
 * </code>
 * </pre>
 * By default, SGML requires that all attribute values be delimited using
 * either double quotation marks (ASCII decimal 34) or single quotation
 * marks (ASCII decimal 39). Single quote marks can be included within the
 * attribute value when the value is delimited by double quote marks, and
 * vice versa. Authors may also use numeric character references to
 * represent double quotes (&amp;#34;) and single quotes (&amp;#39;).
 * For doublequotes authors can also use the character entity reference
 * &amp;quot;.<p>
 * In certain cases, authors may specify the value of an attribute without
 * any quotation marks. The attribute value may only contain letters
 * (a-z and A-Z), digits (0-9), hyphens (ASCII decimal 45),
 * periods (ASCII decimal 46), underscores (ASCII decimal 95),
 * and colons (ASCII decimal 58). We recommend using quotation marks even
 * when it is possible to eliminate them.<p>
 * Attribute names are always case-insensitive.<p>
 * Attribute values are generally case-insensitive. The definition of each
 * attribute in the reference manual indicates whether its value is
 * case-insensitive.<p>
 * All the attributes defined by this specification are listed in the
 * <a href="http://www.w3.org/TR/html4/index/attributes.html">attribute
 * index</a>.<p>
 * </cite>
 * <p>
 */
public class Attribute
    implements
        Serializable
{
    /**
     * The name of this attribute.
     * The part before the equals sign, or the stand-alone attribute.
     * This will be <code>null</code> if the attribute is whitespace.
     */
    protected String mName;

    /**
     * The assignment string of the attribute.
     * The equals sign.
     * This will be <code>null</code> if the attribute is a
     * stand-alone attribute.
     */
    protected String mAssignment;

    /**
     * The value of the attribute.
     * The part after the equals sign.
     * This will be <code>null</code> if the attribute is an empty or
     * stand-alone attribute.
     */
    protected String mValue;

    /**
     * The quote, if any, surrounding the value of the attribute, if any.
     * This will be zero if there are no quotes around the value.
     */
    protected char mQuote;

    /**
     * Create an attribute with the name, assignment, value and quote given.
     * If the quote value is zero, assigns the value using {@link #setRawValue}
     * which sets the quote character to a proper value if necessary.
     * @param name The name of this attribute.
     * @param assignment The assignment string of this attribute.
     * @param value The value of this attribute.
     * @param quote The quote around the value of this attribute.
     */
    public Attribute (String name, String assignment, String value, char quote)
    {
        setName (name);
        setAssignment (assignment);
        if (0 == quote)
            setRawValue (value);
        else
        {
            setValue (value);
            setQuote (quote);
        }
    }

    /**
     * Create an attribute with the name, value and quote given.
     * Uses an equals sign as the assignment string if the value is not
     * <code>null</code>, and calls {@link #setRawValue} to get the
     * correct quoting if <code>quote</code> is zero.
     * @param name The name of this attribute.
     * @param value The value of this attribute.
     * @param quote The quote around the value of this attribute.
     */
    public Attribute (String name, String value, char quote)
    {
        this (name, (null == value ? "" : "="), value, quote);
    }

    /**
     * Create a whitespace attribute with the value given.
     * @param value The value of this attribute.
     * @exception IllegalArgumentException if the value contains other than
     * whitespace. To set a real value use {@link #Attribute(String,String)}.
     */
    public Attribute (String value)
        throws
            IllegalArgumentException
    {
        if (0 != value.trim ().length ())
            throw new IllegalArgumentException ("non whitespace value");
        else
        {
            setName (null);
            setAssignment (null);
            setValue (value);
            setQuote ((char)0);
        }
    }

    /**
     * Create an attribute with the name and value given.
     * Uses an equals sign as the assignment string if the value is not
     * <code>null</code>, and calls {@link #setRawValue} to get the
     * correct quoting.
     * @param name The name of this attribute.
     * @param value The value of this attribute.
     */
    public Attribute (String name, String value)
    {
        this (name, (null == value ? "" : "="), value, (char)0);
    }

    /**
     * Create an attribute with the name, assignment string and value given.
     * Calls {@link #setRawValue} to get the correct quoting.
     * @param name The name of this attribute.
     * @param assignment The assignment string of this attribute.
     * @param value The value of this attribute.
     */
    public Attribute (String name, String assignment, String value)
    {
        this (name, assignment, value, (char)0);
    }

    /**
     * Create an empty attribute.
     * This will provide "" from the {@link #toString} and
     * {@link #toString(StringBuffer)} methods.
     */
    public Attribute ()
    {
        this (null, null, null, (char)0);
    }

    /**
     * Get the name of this attribute.
     * The part before the equals sign, or the contents of the
     * stand-alone attribute.
     * @return The name, or <code>null</code> if it's just a whitepace
     * 'attribute'.
     */
    public String getName ()
    {
        return (mName);
    }

    /**
     * Get the name of this attribute.
     * @param buffer The buffer to place the name in.
     * @see #getName()
     */
    public void getName (StringBuffer buffer)
    {
        if (null != mName)
            buffer.append (mName);
    }

    /**
     * Set the name of this attribute.
     * Set the part before the equals sign, or the contents of the
     * stand-alone attribute.
     * <em>WARNING:</em> Setting this to <code>null</code> can result in
     * malformed HTML if the assignment string is not <code>null</code>.
     * @param name The new name.
     */
    public void setName (String name)
    {
        mName = name;
    }

    /**
     * Get the assignment string of this attribute.
     * This is usually just an equals sign, but in poorly formed attributes it
     * can include whitespace on either or both sides of an equals sign.
     * @return The assignment string.
     */
    public String getAssignment ()
    {
        return (mAssignment);
    }

    /**
     * Get the assignment string of this attribute.
     * @param buffer The buffer to place the assignment string in.
     * @see #getAssignment()
     */
    public void getAssignment (StringBuffer buffer)
    {
        if (null != mAssignment)
            buffer.append (mAssignment);
    }

    /**
     * Set the assignment string of this attribute.
     * <em>WARNING:</em> Setting this property to other than an equals sign
     * or <code>null</code> will result in malformed HTML. In the case of a
     * <code>null</code>, the {@link  #setValue value} should also be set to
     * <code>null</code>.
     * @param assignment The new assignment string.
     */
    public void setAssignment (String assignment)
    {
        mAssignment = assignment;
    }

    /**
     * Get the value of the attribute.
     * The part after the equals sign, or the text if it's just a whitepace
     * 'attribute'.
     * <em>NOTE:</em> This does not include any quotes that may have enclosed
     * the value when it was read. To get the un-stripped value use
     * {@link  #getRawValue}.
     * @return The value, or <code>null</code> if it's a stand-alone or
     * empty attribute, or the text if it's just a whitepace 'attribute'.
     */
    public String getValue ()
    {
        return (mValue);
    }

    /**
     * Get the value of the attribute.
     * @param buffer The buffer to place the value in.
     * @see #getValue()
     */
    public void getValue (StringBuffer buffer)
    {
        if (null != mValue)
            buffer.append (mValue);
    }

    /**
     * Set the value of the attribute.
     * The part after the equals sign, or the text if it's a whitepace
     * 'attribute'.
     * <em>WARNING:</em> Setting this property to a value that needs to be
     * quoted without also setting the quote character will result in malformed
     * HTML.
     * @param value The new value.
     */
    public void setValue (String value)
    {
        mValue = value;
    }

    /**
     * Get the quote, if any, surrounding the value of the attribute, if any.
     * @return Either ' or " if the attribute value was quoted, or zero
     * if there are no quotes around it.
     */
    public char getQuote ()
    {
        return (mQuote);
    }

    /**
     * Get the quote, if any, surrounding the value of the attribute, if any.
     * @param buffer The buffer to place the quote in.
     * @see #getQuote()
     */
    public void getQuote (StringBuffer buffer)
    {
        if (0 != mQuote)
            buffer.append (mQuote);
    }

    /**
     * Set the quote surrounding the value of the attribute.
     * <em>WARNING:</em> Setting this property to zero will result in malformed
     * HTML if the {@link  #getValue value} needs to be quoted (i.e. contains
     * whitespace).
     * @param quote The new quote value.
     */
    public void setQuote (char quote)
    {
        mQuote = quote;
    }

    /**
     * Get the raw value of the attribute.
     * The part after the equals sign, or the text if it's just a whitepace
     * 'attribute'. This includes the quotes around the value if any.
     * @return The value, or <code>null</code> if it's a stand-alone attribute,
     * or the text if it's just a whitepace 'attribute'.
     */
    public String getRawValue ()
    {
        char quote;
        StringBuffer buffer;
        String ret;

        if (isValued ())
        {
            quote = getQuote ();
            if (0 != quote)
            {
                buffer = new StringBuffer (); // todo: what is the value length?
                buffer.append (quote);
                getValue (buffer);
                buffer.append (quote);
                ret = buffer.toString ();
            }
            else
                ret = getValue ();
        }
        else
            ret = null;

        return (ret);
    }

    /**
     * Get the raw value of the attribute.
     * The part after the equals sign, or the text if it's just a whitepace
     * 'attribute'. This includes the quotes around the value if any.
     * @param buffer The string buffer to append the attribute value to.
     * @see #getRawValue()
     */
    public void getRawValue (StringBuffer buffer)
    {
        getQuote (buffer);
        getValue (buffer);
        getQuote (buffer);
    }

    /**
     * Set the value of the attribute and the quote character.
     * If the value is pure whitespace, assign it 'as is' and reset the
     * quote character. If not, check for leading and trailing double or
     * single quotes, and if found use this as the quote character and
     * the inner contents of <code>value</code> as the real value.
     * Otherwise, examine the string to determine if quotes are needed
     * and an appropriate quote character if so. This may involve changing
     * double quotes within the string to character references.
     * @param value The new value.
     */
    public void setRawValue (String value)
    {
        char ch;
        boolean needed;
        boolean singleq;
        boolean doubleq;
        String ref;
        StringBuffer buffer;
        char quote;

        quote = 0;
        if ((null != value) && (0 != value.trim ().length ()))
        {
            if (value.startsWith ("'") && value.endsWith ("'")
                && (2 <= value.length ()))
            {
                quote = '\'';
                value = value.substring (1, value.length () - 1);
            }
            else if (value.startsWith ("\"") && value.endsWith ("\"")
                && (2 <= value.length ()))
            {
                quote = '"';
                value = value.substring (1, value.length () - 1);
            }
            else
            {
                // first determine if there's whitespace in the value
                // and while we're at it find a suitable quote character
                needed = false;
                singleq = true;
                doubleq = true;
                for (int i = 0; i < value.length (); i++)
                {
                    ch = value.charAt (i);
                    if ('\'' == ch)
                    {
                        singleq  = false;
                        needed = true;
                    }
                    else if ('"' == ch)
                    {
                        doubleq = false;
                        needed = true;
                    }
                    else if (!('-' == ch) && !('.' == ch) && !('_' == ch)
                       && !(':' == ch) && !Character.isLetterOrDigit (ch))
                    {
                        needed = true;
                    }
                }

                // now apply quoting
                if (needed)
                {
                    if (doubleq)
                        quote = '"';
                    else if (singleq)
                        quote = '\'';
                    else
                    {
                        // uh-oh, we need to convert some quotes into character
                        // references, so convert all double quotes into &#34;
                        quote = '"';
                        ref = "&quot;"; // Translate.encode (quote);
                        // JDK 1.4: value = value.replaceAll ("\"", ref);
                        buffer = new StringBuffer (
                                value.length() * (ref.length () - 1));
                        for (int i = 0; i < value.length (); i++)
                        {
                            ch = value.charAt (i);
                            if (quote == ch)
                                buffer.append (ref);
                            else
                                buffer.append (ch);
                        }
                        value = buffer.toString ();
                    }
                }
            }
        }
        setValue (value);
        setQuote (quote);
    }

    /**
     * Predicate to determine if this attribute is whitespace.
     * @return <code>true</code> if this attribute is whitespace,
     * <code>false</code> if it is a real attribute.
     */
    public boolean isWhitespace ()
    {
        return (null == getName ());
    }

    /**
     * Predicate to determine if this attribute has no equals sign (or value).
     * @return <code>true</code> if this attribute is a standalone attribute.
     * <code>false</code> if has an equals sign.
     */
    public boolean isStandAlone ()
    {
        return ((null != getName ()) && (null == getAssignment ()));
    }

    /**
     * Predicate to determine if this attribute has an equals sign but no value.
     * @return <code>true</code> if this attribute is an empty attribute.
     * <code>false</code> if has an equals sign and a value.
     */
    public boolean isEmpty ()
    {
        return ((null != getAssignment ()) && (null == getValue ()));
    }

    /**
     * Predicate to determine if this attribute has a value.
     * @return <code>true</code> if this attribute has a value.
     * <code>false</code> if it is empty or standalone.
     */
    public boolean isValued ()
    {
        return (null != getValue ());
    }

    /**
     * Get the length of the string value of this attribute.
     * @return The number of characters required to express this attribute.
     */
    public int getLength ()
    {
        String name;
        String assignment;
        String value;
        char quote;
        int ret;

        ret = 0;
        name = getName ();
        if (null != name)
            ret += name.length ();
        assignment = getAssignment ();
        if (null != assignment)
            ret += assignment.length ();
        value = getValue ();
        if (null != value)
            ret += value.length ();
        quote = getQuote ();
        if (0 != quote)
            ret += 2;

        return (ret);
    }

    /**
     * Get a text representation of this attribute.
     * Suitable for insertion into a tag, the output is one of
     * the forms:
     * <code>
     * <pre>
     * value
     * name
     * name=
     * name=value
     * name='value'
     * name="value"
     * </pre>
     * </code>
     * @return A string that can be used within a tag.
     */
    public String toString ()
    {
        int length;
        StringBuffer ret;

        // get the size to avoid extra StringBuffer allocations
        length = getLength ();
        ret = new StringBuffer (length);
        toString (ret);

        return (ret.toString ());
    }

    /**
     * Get a text representation of this attribute.
     * @param buffer The accumulator for placing the text into.
     * @see #toString()
     */
    public void toString (StringBuffer buffer)
    {
        getName (buffer);
        getAssignment (buffer);
        getRawValue (buffer);
    }

}
