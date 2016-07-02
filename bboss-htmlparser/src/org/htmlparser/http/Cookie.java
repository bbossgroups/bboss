// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/http/Cookie.java,v $
// $Author: derrickoswald $
// $Date: 2005/05/15 11:49:04 $
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

package org.htmlparser.http;

import java.io.Serializable;
import java.util.Date;

/**
 * A HTTP cookie.
 * This class represents a "Cookie", as used for session management with HTTP
 * and HTTPS protocols. Cookies are used to get user agents (web browsers etc)
 * to hold small amounts of state associated with a user's web browsing. Common
 * applications for cookies include storing user preferences, automating low
 * security user signon facilities, and helping collect data used for "shopping
 * cart" style applications.
 * <P>
 * Cookies are named, and have a single value. They may have optional
 * attributes, including a comment presented to the user, path and domain
 * qualifiers for which hosts see the cookie, a maximum age, and a version.
 * Current web browsers often have bugs in how they treat those attributes, so
 * interoperability can be improved by not relying on them heavily.
 * <P>
 * Cookies are assigned by servers, using fields added to HTTP response headers.
 * Cookies are passed back to those servers using fields added to HTTP request
 * headers. Several cookies with the same name can be returned;
 * they have different path attributes, but those attributes
 * will not be visible when using "old format" cookies.
 * <P>
 * Cookies affect the caching of the web pages used to set their values. At this
 * time, none of the sophisticated HTTP/1.1 cache control models are supported.
 * Standard HTTP/1.0 caches will not cache pages which contain
 * cookies created by this class.
 * <P>
 * Cookies are being standardized by the IETF. This class supports the original
 * Cookie specification (from Netscape Communications Corp.) as well as the
 * updated <a href="http://www.ietf.org/rfc/rfc2109.txt">RFC 2109</a>
 * specification.
 */
public class Cookie
    implements
        Cloneable,
        Serializable
{
    /**
     * Special characters to watch out for.
     * From RFC 2068, token special case characters.
     */
    private static final String SPECIALS = "()<>@,;:\\\"/[]?={} \t";

    /**
     * The name of the cookie.
     */
    protected String mName;

    /**
     * The cookie value.
     */
    protected String mValue; // value of NAME

    /**
     * Describes the cookie's use.
     */
    protected String mComment;

    /**
     * Domain that sees cookie.
     */
    protected String mDomain;

    /**
     * Cookie expires after this date.
     */
    protected Date mExpiry;

    /**
     * URLs that see the cookie.
     */
    protected String mPath;

    /**
     * Use SSL.
     */
    protected boolean mSecure;

    /**
     * If Version=1 it means RFC 2109++ style cookies.
     */
    protected int mVersion;

    /**
     * Defines a cookie with an initial name/value pair. The name must be an
     * HTTP/1.1 "token" value; alphanumeric ASCII strings work. Names starting
     * with a "$" character are reserved by RFC 2109.
     * The path for the cookie is set to the root ("/") and there is no
     * expiry time set.
     * @param name  The name of the cookie.
     * @param value The value of the cookie.
     * @exception IllegalArgumentException
     *             if the cookie name is not an HTTP/1.1 "token", or if it is
     *             one of the tokens reserved for use by the cookie protocol
     */
    public Cookie (String name, String value)
        throws
            IllegalArgumentException
    {
        if (!isToken (name) || name.equalsIgnoreCase ("Comment") // rfc2019
                || name.equalsIgnoreCase ("Discard") // 2019++
                || name.equalsIgnoreCase ("Domain")
                || name.equalsIgnoreCase ("Expires") // (old cookies)
                || name.equalsIgnoreCase ("Max-Age") // rfc2019
                || name.equalsIgnoreCase ("Path")
                || name.equalsIgnoreCase ("Secure")
                || name.equalsIgnoreCase ("Version"))
            throw new IllegalArgumentException ("invalid cookie name: " + name);
        mName = name;
        mValue = value;
        mComment = null;
        mDomain = null;
        mExpiry = null; // not persisted
        mPath = "/";
        mSecure = false;
        mVersion = 0;
    }

    /**
     * If a user agent (web browser) presents this cookie to a user, the
     * cookie's purpose will be described using this comment. This is not
     * supported by version zero cookies.
     * @param purpose The cookie comment.
     * @see #getComment
     */
    public void setComment (String purpose)
    {
        mComment = purpose;
    }

    /**
     * Returns the comment describing the purpose of this cookie, or null if no
     * such comment has been defined.
     * @see #setComment
     * @return The cookie comment, or <code>null</code> if none.
     */
    public String getComment ()
    {
        return (mComment);
    }

    /**
     * This cookie should be presented only to hosts satisfying this domain name
     * pattern. Read RFC 2109 for specific details of the syntax. Briefly, a
     * domain name name begins with a dot (".foo.com") and means that hosts in
     * that DNS zone ("www.foo.com", but not "a.b.foo.com") should see the
     * cookie. By default, cookies are only returned to the host which saved
     * them.
     * @see #getDomain
     * @param pattern The domain name pattern. The pattern is converted to
     * lower case to accommodate less capable browsers.
     */
    public void setDomain (String pattern)
    {
        mDomain = pattern.toLowerCase (); // IE allegedly needs this
    }

    /**
     * Returns the domain of this cookie.
     * @return The cookie domain (the base URL name it applies to).
     * @see #setDomain
     */
    public String getDomain ()
    {
        return (mDomain);
    }

    /**
     * Sets the expiry date of the cookie. The cookie will expire after the
     * date specified. A null value indicates the default behaviour:
     * the cookie is not stored persistently, and will be deleted when the user
     * agent (web browser) exits.
     * @param expiry The expiry date for this cookie, or <code>null</code> if
     * the cookie is persistent.
     * @see #getExpiryDate
     */
    public void setExpiryDate (Date expiry)
    {
        mExpiry = expiry;
    }

    /**
     * Returns the expiry date of the cookie. If none was specified,
     * null is returned, indicating the default behaviour described
     * with <em>setExpiryDate</em>.
     * @return The cookie expiry date, or <code>null</code> if it is persistent.
     * @see #setExpiryDate
     */
    public Date getExpiryDate ()
    {
        return (mExpiry);
    }

    /**
     * This cookie should be presented only with requests beginning with this
     * URL. Read RFC 2109 for a specification of the default behaviour.
     * Basically, URLs in the same "directory" as the one which set the cookie,
     * and in subdirectories, can all see the cookie unless a different path is
     * set.
     * @param uri The exclusion prefix for the cookie.
     * @see #getPath
     */
    public void setPath (String uri)
    {
        mPath = uri;
    }

    /**
     * Returns the prefix of all URLs for which this cookie is targetted.
     * @return The cookie path (or "/" if no specific path is specified).
     * @see #setPath
     */
    public String getPath ()
    {
        return (mPath);
    }

    /**
     * Indicates to the user agent that the cookie should only be sent using a
     * secure protocol (https). This should only be set when the cookie's
     * originating server used a secure protocol to set the cookie's value.
     * @see #getSecure
     * @param flag Use <code>true</code> if the cookie is to be sent using
     * secure protocols, <code>false</code> otherwise.
     */
    public void setSecure (boolean flag)
    {
        mSecure = flag;
    }

    /**
     * Returns the value of the 'secure' flag.
     * @return The <code>true</code> if this cookie should only be sent using
     * a secure protocol, <code>false</code> otherwise.
     * @see #setSecure
     */
    public boolean getSecure ()
    {
        return (mSecure);
    }

    /**
     * Returns the name of the cookie. This name may not be changed after the
     * cookie is created.
     * @return The name of the cookie.
     */
    public String getName ()
    {
        return (mName);
    }

    /**
     * Sets the value of the cookie. BASE64 encoding is suggested for use with
     * binary values.
     * <p>With version zero cookies, you need to be careful about the kinds of
     * values you use. Values with various special characters (whitespace,
     * brackets and parentheses, the equals sign, comma, double quote, slashes,
     * question marks, the "at" sign, colon, and semicolon) should be avoided.
     * Empty values may not behave the same way on all browsers.</p>
     * @param newValue The new value for the cookie.
     * @see #getValue
     */
    public void setValue (String newValue)
    {
        mValue = newValue;
    }

    /**
     * Returns the value of the cookie.
     * @return The cookie value.
     * @see #setValue
     */
    public String getValue ()
    {
        return (mValue);
    }

    /**
     * Returns the version of the cookie. Version 1 complies with RFC 2109,
     * version 0 indicates the original version, as specified by Netscape. Newly
     * constructed cookies use version 0 by default, to maximize
     * interoperability. Cookies provided by a user agent will identify the
     * cookie version used by the browser.
     * @see #setVersion
     * @return The cookie version.
     */
    public int getVersion ()
    {
        return (mVersion);
    }

    /**
     * Sets the version of the cookie protocol used when this cookie saves
     * itself. Since the IETF standards are still being finalized, consider
     * version 1 as experimental; do not use it (yet) on production sites.
     * @param version The version of the cookie, either 0 or 1.
     * @see #getVersion
     */
    public void setVersion (int version)
    {
        mVersion = version;
    }

    /**
     * Return true iff the string counts as an HTTP/1.1 "token".
     * Valid tokens cannot have characters outside the ASCII range 0x20-0x7e,
     * and cannot contain any of these characters: "()<>@,;:\\\"/[]?={} \t".
     * @return The <code>true</code> if the provided string is a valid
     * token, <code>false</code> otherwise.
     */
    private boolean isToken (String value)
    {
        int length;
        char c;
        boolean ret;

        ret = true;

        length = value.length ();
        for (int i = 0; i < length && ret; i++)
        {
            c = value.charAt (i);
            if (c < ' ' || c > '~' || SPECIALS.indexOf (c) != -1)
                ret = false;
        }

        return (ret);
    }

    /**
     * Returns a copy of this object.
     * @return The clone of this cookie.
     */
    public Object clone ()
    {
        try
        {
            return (super.clone ());
        }
        catch (CloneNotSupportedException e)
        {
            throw new RuntimeException (e.getMessage ());
        }
    }

    /**
     * Convert this cookie into a user friendly string.
     * @return A short form string representing this cookie.
     */
    public String toString ()
    {
        StringBuffer ret;

        ret = new StringBuffer (50);
        if (getSecure ())
            ret.append ("secure ");
        if (0 != getVersion ())
        {
            ret.append ("version ");
            ret.append (getVersion ());
            ret.append (" ");
        }
        ret.append ("cookie");
        if (null != getDomain ())
        {
            ret.append (" for ");
            ret.append (getDomain ());

            if (null != getPath ())
                ret.append (getPath ());
        }
        else
        {
            if (null != getPath ())
            {
                ret.append (" (path ");
                ret.append (getPath ());
                ret.append (")");
            }
        }
        ret.append (": ");
        ret.append (getName ());
        ret.append ("=");
        if (getValue ().length () > 40)
        {
            ret.append (getValue ().substring (1, 40));
            ret.append ("...");
        }
        else
            ret.append (getValue ());
        if (null != getComment ())
        {
            ret.append (" // ");
            ret.append (getComment ());
        }

        return (ret.toString ());
    }
}
