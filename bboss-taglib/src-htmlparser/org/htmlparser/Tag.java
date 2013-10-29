// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/Tag.java,v $
// $Author: derrickoswald $
// $Date: 2005/05/15 11:49:03 $
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

package org.htmlparser;

import java.util.Hashtable;
import java.util.Vector;

import org.htmlparser.scanners.Scanner;

/**
 * This interface represents a tag (&lt;xxx yyy="zzz"&gt;) in the HTML document.
 * Adds capabilities to a Node that are specific to a tag.
 */
public interface Tag extends Node
{
    /**
     * Returns the value of an attribute.
     * @param name Name of attribute, case insensitive.
     * @return The value associated with the attribute or null if it does
     * not exist, or is a stand-alone or
     */
    String getAttribute (String name);

    /**
     * Set attribute with given key, value pair.
     * Figures out a quote character to use if necessary.
     * @param key The name of the attribute.
     * @param value The value of the attribute.
     */
    void setAttribute (String key, String value);

    /**
     * Set attribute with given key/value pair, the value is quoted by quote.
     * @param key The name of the attribute.
     * @param value The value of the attribute.
     * @param quote The quote character to be used around value.
     * If zero, it is an unquoted value.
     */
    void setAttribute (String key, String value, char quote);

    /**
     * Remove the attribute with the given key, if it exists.
     * @param key The name of the attribute.
     */
    void removeAttribute (String key);

    /**
     * Returns the attribute with the given name.
     * @param name Name of attribute, case insensitive.
     * @return The attribute or null if it does
     * not exist.
     */
    Attribute getAttributeEx (String name);

    /**
     * Set an attribute.
     * This replaces an attribute of the same name.
     * To set the zeroth attribute (the tag name), use setTagName().
     * @param attribute The attribute to set.
     */
    void setAttributeEx (Attribute attribute);

    /**
     * Gets the attributes in the tag.
     * @return Returns the list of {@link Attribute Attributes} in the tag.
     */
    Vector getAttributesEx ();

    /**
     * Sets the attributes.
     * NOTE: Values of the extended hashtable are two element arrays of String,
     * with the first element being the original name (not uppercased),
     * and the second element being the value.
     * @param attribs The attribute collection to set.
     */
    void setAttributesEx (Vector attribs);

    /**
     * Gets the attributes in the tag.
     * This is not the preferred  method to get attributes, see {@link
     * #getAttributesEx getAttributesEx} which returns a list of {@link
     * Attribute} objects, which offer more information than the simple
     * <code>String</code> objects available from this <code>Hashtable</code>.
     * @return Returns a list of name/value pairs representing the attributes.
     * These are not in order, the keys (names) are converted to uppercase
     * and the values are not quoted, even if they need to be.
     * The table <em>will</em> return <code>null</code> if there was no value
     * for an attribute (either no equals sign or nothing to the right of the
     * equals sign). A special entry with a key of
     * SpecialHashtable.TAGNAME ("$<TAGNAME>$") holds the tag name.
     * The conversion to uppercase is performed with an ENGLISH locale.
     * @deprecated Use getAttributesEx() instead.
     */
    Hashtable getAttributes ();

    /**
     * Sets the attributes.
     * A special entry with a key of SpecialHashtable.TAGNAME ("$<TAGNAME>$")
     * sets the tag name.
     * @param attributes The attribute collection to set.
     * @deprecated Use setAttributesEx() instead.
     */
    void setAttributes (Hashtable attributes);

    /**
     * Return the name of this tag.
     * <p>
     * <em>
     * Note: This value is converted to uppercase and does not
     * begin with "/" if it is an end tag. Nor does it end with
     * a slash in the case of an XML type tag.
     * The conversion to uppercase is performed with an ENGLISH locale.
     * </em>
     * @return The tag name.
     */
    String getTagName ();

    /**
     * Set the name of this tag.
     * This creates or replaces the first attribute of the tag (the
     * zeroth element of the attribute vector).
     * @param name The tag name.
     */
    void setTagName (String name);

    /**
     * Return the name of this tag.
     * @return The tag name or null if this tag contains nothing or only
     * whitespace.
     */
    String getRawTagName ();

    /**
     * Determines if the given tag breaks the flow of text.
     * @return <code>true</code> if following text would start on a new line,
     * <code>false</code> otherwise.
     */
    boolean breaksFlow ();

    /**
     * Predicate to determine if this tag is an end tag (i.e. &lt;/HTML&gt;).
     * @return <code>true</code> if this tag is an end tag.
     */
    boolean isEndTag ();

    /**
     * Is this an empty xml tag of the form &lt;tag/&gt;.
     * @return true if the last character of the last attribute is a '/'.
     */
    boolean isEmptyXmlTag ();

    /**
     * Set this tag to be an empty xml node, or not.
     * Adds or removes an ending slash on the tag.
     * @param emptyXmlTag If true, ensures there is an ending slash in the node,
     * i.e. &lt;tag/&gt;, otherwise removes it.
     */
    void setEmptyXmlTag (boolean emptyXmlTag);

    /**
     * Return the set of names handled by this tag.
     * Since this a a generic tag, it has no ids.
     * @return The names to be matched that create tags of this type.
     */
    String[] getIds ();

    /**
     * Return the set of tag names that cause this tag to finish.
     * These are the normal (non end tags) that if encountered while
     * scanning (a composite tag) will cause the generation of a virtual
     * tag.
     * Since this a a non-composite tag, the default is no enders.
     * @return The names of following tags that stop further scanning.
     */
    String[] getEnders ();

    /**
     * Return the set of end tag names that cause this tag to finish.
     * These are the end tags that if encountered while
     * scanning (a composite tag) will cause the generation of a virtual
     * tag.
     * Since this a a non-composite tag, it has no end tag enders.
     * @return The names of following end tags that stop further scanning.
     */
    String[] getEndTagEnders ();

    /**
     * Get the end tag for this (composite) tag.
     * For a non-composite tag this always returns <code>null</code>.
     * @return The tag that terminates this composite tag, i.e. &lt;/HTML&gt;.
     */
    Tag getEndTag ();

    /**
     * Set the end tag for this (composite) tag.
     * For a non-composite tag this is a no-op.
     * @param tag The tag that closes this composite tag, i.e. &lt;/HTML&gt;.
     */
    void setEndTag (Tag tag);

    /**
     * Return the scanner associated with this tag.
     * @return The scanner associated with this tag.
     */
    Scanner getThisScanner ();

    /**
     * Set the scanner associated with this tag.
     * @param scanner The scanner for this tag.
     */
    void setThisScanner (Scanner scanner);

    /**
     * Get the line number where this tag starts.
     * @return The (zero based) line number in the page where this tag starts.
     */
    int getStartingLineNumber ();
    /**
     * Get the line number where this tag ends.
     * @return The (zero based) line number in the page where this tag ends.
     */
    int getEndingLineNumber ();
}
