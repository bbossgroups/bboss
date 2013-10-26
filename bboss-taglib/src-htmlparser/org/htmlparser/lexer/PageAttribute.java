// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/lexer/PageAttribute.java,v $
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

package org.htmlparser.lexer;

import org.htmlparser.Attribute;

/**
 * An attribute within a tag on a page.
 * This attribute is similar to Attribute but 'lazy loaded' from the
 * <code>Page</code> by providing the page and cursor offsets
 * into the page for the name and value. This is done for speed, since
 * if the name and value are not needed we can avoid the cost and memory
 * overhead of creating the strings.
 * <p>
 * Thus the property getters, defer to the base class unless the property
 * is null, in which case an attempt is made to read it from the underlying
 * page. Optimizations in the predicates and length calculation defer the
 * actual instantiation of strings until absolutely needed.
 */
public class PageAttribute
    extends
        Attribute
{
    /**
     * The page this attribute is extracted from.
     */
    protected Page mPage;

    /**
     * The starting offset of the name within the page.
     * If negative, the name is considered <code>null</code>.
     */
    protected int mNameStart;

    /**
     * The ending offset of the name within the page.
     */
    protected int mNameEnd;

    /**
     * The starting offset of the value within the page.
     * If negative, the value is considered <code>null</code>.
     */
    protected int mValueStart;

    /**
     * The ending offset of the name within the page.
     */
    protected int mValueEnd;

    /**
     * Create an attribute.
     * @param page The page containing the attribute.
     * @param name_start The starting offset of the name within the page.
     * If this is negative, the name is considered null.
     * @param name_end The ending offset of the name within the page.
     * @param value_start he starting offset of the value within the page.
     * If this is negative, the value is considered null.
     * @param value_end The ending offset of the value within the page.
     * @param quote The quote, if any, surrounding the value of the attribute,
     * (i.e. ' or "), or zero if none.
     */
    public PageAttribute (Page page, int name_start, int name_end, int value_start, int value_end, char quote)
    {
        mPage = page;
        mNameStart = name_start;
        mNameEnd = name_end;
        mValueStart = value_start;
        mValueEnd = value_end;
        setName (null);
        setAssignment (null);
        setValue (null);
        setQuote (quote);
    }

    //
    // provide same constructors as super class
    //

    private void init ()
    {
        mPage = null;
        mNameStart = -1;
        mNameEnd = -1;
        mValueStart = -1;
        mValueEnd = -1;
    }

    /**
     * Create an attribute with the name, assignment string, value and quote given.
     * If the quote value is zero, assigns the value using {@link #setRawValue}
     * which sets the quote character to a proper value if necessary.
     * @param name The name of this attribute.
     * @param assignment The assignment string of this attribute.
     * @param value The value of this attribute.
     * @param quote The quote around the value of this attribute.
     */
    public PageAttribute (String name, String assignment, String value, char quote)
    {
        super (name, assignment, value, quote);
        init ();
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
    public PageAttribute (String name, String value, char quote)
    {
        super (name, value, quote);
        init ();
    }

    /**
     * Create a whitespace attribute with the value given.
     * @param value The value of this attribute.
     * @exception IllegalArgumentException if the value contains other than
     * whitespace. To set a real value use {@link #PageAttribute(String,String)}.
     */
    public PageAttribute (String value)
        throws
            IllegalArgumentException
    {
        super (value);
        init ();
    }

    /**
     * Create an attribute with the name and value given.
     * Uses an equals sign as the assignment string if the value is not
     * <code>null</code>, and calls {@link #setRawValue} to get the
     * correct quoting.
     * @param name The name of this attribute.
     * @param value The value of this attribute.
     */
    public PageAttribute (String name, String value)
    {
        super (name, value);
        init ();
    }

    /**
     * Create an attribute with the name, assignment string and value given.
     * Calls {@link #setRawValue} to get the correct quoting.
     * @param name The name of this attribute.
     * @param assignment The assignment string of this attribute.
     * @param value The value of this attribute.
     */
    public PageAttribute (String name, String assignment, String value)
    {
        super (name, assignment, value);
        init ();
    }

    /**
     * Create an empty attribute.
     * This will provide "" from the {@link #toString} and 
     * {@link #toString(StringBuffer)} methods.
     */
    public PageAttribute ()
    {
        super ();
        init ();
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
        String ret;

        ret = super.getName ();
        if (null == ret)
        {
            if ((null != mPage) && (0 <= mNameStart))
            {
                ret = mPage.getText (mNameStart, mNameEnd);
                setName (ret); // cache the value
            }
        }

        return (ret);
    }

    /**
     * Get the name of this attribute.
     * @param buffer The buffer to place the name in.
     * @see #getName()
     */
    public void getName (StringBuffer buffer)
    {
        String name;

        name = super.getName ();
        if (null == name)
        {
            if ((null != mPage) && (0 <= mNameStart))
                mPage.getText (buffer, mNameStart, mNameEnd);
        }
        else
            buffer.append (name);
    }

    /**
     * Get the assignment string of this attribute.
     * This is usually just an equals sign, but in poorly formed attributes it
     * can include whitespace on either or both sides of an equals sign.
     * @return The assignment string.
     */
    public String getAssignment ()
    {
        String ret;

        ret = super.getAssignment ();
        if (null == ret)
        {
            if ((null != mPage) && (0 <= mNameEnd) && (0 <= mValueStart))
            {
                ret = mPage.getText (mNameEnd, mValueStart);
                // remove a possible quote included in the assignment
                // since mValueStart points at the real start of the value
                if (ret.endsWith ("\"") || ret.endsWith ("'"))
                    ret = ret.substring (0, ret.length () - 1);
                setAssignment (ret); // cache the value
            }
        }

        return (ret);
    }

    /**
     * Get the assignment string of this attribute.
     * @param buffer The buffer to place the assignment string in.
     * @see #getAssignment()
     */
    public void getAssignment (StringBuffer buffer)
    {
        int length;
        char ch;
        String assignment;

        assignment = super.getAssignment ();
        if (null == assignment)
        {
            if ((null != mPage) && (0 <= mNameEnd) && (0 <= mValueStart))
            {
                mPage.getText (buffer, mNameEnd, mValueStart);
                // remove a possible quote included in the assignment
                // since mValueStart points at the real start of the value
                length = buffer.length () - 1;
                ch = buffer.charAt (length);
                if (('\'' == ch) || ('"' == ch))
                    buffer.setLength (length);
            }
        }
        else
            buffer.append (assignment);
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
        String ret;

        ret = super.getValue ();
        if (null == ret)
        {
            if ((null != mPage) && (0 <= mValueEnd))
            {
                ret = mPage.getText (mValueStart, mValueEnd);
                setValue (ret); // cache the value
            }
        }

        return (ret);
    }

    /**
     * Get the value of the attribute.
     * @param buffer The buffer to place the value in.
     * @see #getValue()
     */
    public void getValue (StringBuffer buffer)
    {
        String value;

        value = super.getValue ();
        if (null == value)
        {
            if ((null != mPage) && (0 <= mValueEnd))
                mPage.getText (buffer, mNameStart, mNameEnd);
        }
        else
            buffer.append (value);
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

        ret = getValue ();
        if (null != ret && (0 != (quote = getQuote ())))
        {
            buffer = new StringBuffer (ret.length() + 2);
            buffer.append (quote);
            buffer.append (ret);
            buffer.append (quote);
            ret = buffer.toString ();
        }

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
        char quote;

        if (null == mValue)
        {
            if (0 <= mValueEnd)
            {
                if (0 != (quote = getQuote ()))
                    buffer.append (quote);
                if (mValueStart != mValueEnd)
                    mPage.getText (buffer, mValueStart, mValueEnd);
                if (0 != quote)
                    buffer.append (quote);
            }
        }
        else
        {
            if (0 != (quote = getQuote ()))
                buffer.append (quote);
            buffer.append (mValue);
            if (0 != quote)
                buffer.append (quote);
        }
    }

    /**
     * Get the page this attribute is anchored to, if any.
     * @return The page used to construct this attribute, or null if this
     * is just a regular attribute.
     */
    public Page getPage ()
    {
        return (mPage);
    }

    /**
     * Set the page this attribute is anchored to.
     * @param page The page to be used to construct this attribute.
     * Note: If you set this you probably also want to uncache the property
     * values by setting them to null.
     */
    public void setPage (Page page)
    {
        mPage = page;
    }

    /**
     * Get the starting position of the attribute name.
     * @return The offset into the page at which the name begins.
     */
    public int getNameStartPosition ()
    {
        return (mNameStart);
    }

    /**
     * Set the starting position of the attribute name.
     * @param start The new offset into the page at which the name begins.
     */
    public void setNameStartPosition (int start)
    {
        mNameStart = start;
        setName (null); // uncache value
    }

    /**
     * Get the ending position of the attribute name.
     * @return The offset into the page at which the name ends.
     */
    public int getNameEndPosition ()
    {
        return (mNameEnd);
    }

    /**
     * Set the ending position of the attribute name.
     * @param end The new offset into the page at which the name ends.
     */
    public void setNameEndPosition (int end)
    {
        mNameEnd = end;
        setName (null); // uncache value
        setAssignment (null); // uncache value
    }

    /**
     * Get the starting position of the attribute value.
     * @return The offset into the page at which the value begins.
     */
    public int getValueStartPosition ()
    {
        return (mValueStart);
    }

    /**
     * Set the starting position of the attribute value.
     * @param start The new offset into the page at which the value begins.
     */
    public void setValueStartPosition (int start)
    {
        mValueStart = start;
        setAssignment (null); // uncache value
        setValue (null); // uncache value
    }

    /**
     * Get the ending position of the attribute value.
     * @return The offset into the page at which the value ends.
     */
    public int getValueEndPosition ()
    {
        return (mValueEnd);
    }

    /**
     * Set the ending position of the attribute value.
     * @param end The new offset into the page at which the value ends.
     */
    public void setValueEndPosition (int end)
    {
        mValueEnd = end;
        setValue (null); // uncache value
    }

    /**
     * Predicate to determine if this attribute is whitespace.
     * @return <code>true</code> if this attribute is whitespace,
     * <code>false</code> if it is a real attribute.
     */
    public boolean isWhitespace ()
    {
        return (((null == super.getName ()) && (null == mPage))
            || ((null != mPage) && (0 > mNameStart)));
    }

    /**
     * Predicate to determine if this attribute has no equals sign (or value).
     * @return <code>true</code> if this attribute is a standalone attribute.
     * <code>false</code> if has an equals sign.
     */
    public boolean isStandAlone ()
    {
        return (!isWhitespace () // not whitespace
            && (null == super.getAssignment ()) // and no explicit assignment provided
            && !isValued () // and has no value
            && ((null == mPage) // and either its not coming from a page
                // or it is coming from a page and it doesn't have an assignment part
                || ((null != mPage) && (0 <= mNameEnd) && (0 > mValueStart))));
    }

    /**
     * Predicate to determine if this attribute has an equals sign but no value.
     * @return <code>true</code> if this attribute is an empty attribute.
     * <code>false</code> if has an equals sign and a value.
     */
    public boolean isEmpty ()
    {
        return (!isWhitespace () // not whitespace
            && !isStandAlone () // and not standalone
            && (null == super.getValue ()) // and no explicit value provided
            && ((null == mPage) // and either its not coming from a page
                // or it is coming from a page and has no value
                || ((null != mPage) && (0 > mValueEnd))));
    }

    /**
     * Predicate to determine if this attribute has a value.
     * @return <code>true</code> if this attribute has a value.
     * <code>false</code> if it is empty or standalone.
     */
    public boolean isValued ()
    {
        return ((null != super.getValue ()) // an explicit value provided
            // or it is coming from a page and has a non-empty value
            || ((null != mPage) && ((0 <= mValueStart) && (0 <= mValueEnd)) && (mValueStart != mValueEnd)));
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
        name = super.getName ();
        if (null != name)
            ret += name.length ();
        else if ((null != mPage) && (0 <= mNameStart) && (0 <= mNameEnd))
            ret += mNameEnd - mNameStart;
        assignment = super.getAssignment ();
        if (null != assignment)
            ret += assignment.length ();
        else if ((null != mPage) && (0 <= mNameEnd) && (0 <= mValueStart))
            ret += mValueStart - mNameEnd;
        value = super.getValue ();
        if (null != value)
            ret += value.length ();
        else if ((null != mPage) && (0 <= mValueStart) && (0 <= mValueEnd))
            ret += mValueEnd - mValueStart;
        quote = getQuote ();
        if (0 != quote)
            ret += 2;
        
        return (ret);
    }
}
