// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/http/ConnectionManager.java,v $
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

package org.htmlparser.http;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import org.htmlparser.util.ParserException;

/**
 * Handles proxies, password protected URLs and request properties
 * including cookies.
 */
public class ConnectionManager
{
    /**
     * Default Request header fields.
     * So far this is just "User-Agent" and "Accept-Encoding".
     */
    protected static Hashtable mDefaultRequestProperties = new Hashtable ();
    static
    {
        mDefaultRequestProperties.put ("User-Agent", "HTMLParser/"
            + org.htmlparser.Parser.VERSION_NUMBER);
        mDefaultRequestProperties.put ("Accept-Encoding", "gzip, deflate");
    }

    /**
     * Messages for page not there (404).
     */
    private static final String[] FOUR_OH_FOUR =
    {
        "The web site you seek cannot be located,"
            + " but countless more exist",
        "You step in the stream, but the water has moved on."
            + " This page is not here.",
        "Yesterday the page existed. Today it does not."
            + " The internet is like that.",
        "That page was so big. It might have been very useful."
            + " But now it is gone.",
        "Three things are certain: death, taxes and broken links."
            + " Guess which has occured.",
        "Chaos reigns within. Reflect, repent and enter the correct URL."
            + " Order shall return.",
        "Stay the patient course. Of little worth is your ire."
            + " The page is not found.",
        "A non-existant URL reduces your expensive computer to a simple stone.",
        "Many people have visited that page."
            + " Today, you are not one of the lucky ones.",
        "Cutting the wind with a knife. Bookmarking a URL."
            + " Both are ephemeral.",
    };

    /**
     * Base 64 character translation table.
     */
    private static final char[] BASE64_CHAR_TABLE =
         ("ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        + "abcdefghijklmnopqrstuvwxyz0123456789+/").toCharArray ();

    /**
     * Request header fields.
     */
    protected Hashtable mRequestProperties;

    /**
     * The proxy server name.
     */
    protected String mProxyHost;

    /**
     * The proxy port number.
     */
    protected int mProxyPort;

    /**
     * The proxy username name.
     */
    protected String mProxyUser;

    /**
     * The proxy user password.
     */
    protected String mProxyPassword;

    /**
     * The username name for accessing the URL.
     */
    protected String mUser;

    /**
     * The user password for accessing the URL.
     */
    protected String mPassword;

    /**
     * Cookie storage, a hashtable (by site or host) of vectors of Cookies.
     * This will be null if cookie processing is disabled (default).
     */
    protected Hashtable mCookieJar;

    /**
     * The object to be notified prior to and after each connection.
     */
    protected ConnectionMonitor mMonitor;

    /**
     * Create a connection manager.
     */
    public ConnectionManager ()
    {
        this (getDefaultRequestProperties ());
    }

    /**
     * Create a connection manager with the given connection properties.
     * @param properties Name/value pairs to be added to the HTTP request.
     */
    public ConnectionManager (Hashtable properties)
    {
        mRequestProperties = properties;
        mProxyHost = null;
        mProxyPort = 0;
        mProxyUser = null;
        mProxyPassword = null;
        mUser = null;
        mPassword = null;
        mCookieJar = null;
        mMonitor = null;
    }

    //
    // static methods
    //

    /**
     * Get the current default request header properties.
     * A String-to-String map of header keys and values.
     * These fields are set by the parser when creating a connection.
     * @return The default set of request header properties that will
     * currently be used.
     * @see #mDefaultRequestProperties
     * @see #setRequestProperties
     */
    public static Hashtable getDefaultRequestProperties ()
    {
        return (mDefaultRequestProperties);
    }

    /**
     * Set the default request header properties.
     * A String-to-String map of header keys and values.
     * These fields are set by the parser when creating a connection.
     * Some of these can be set directly on a <code>URLConnection</code>,
     * i.e. If-Modified-Since is set with setIfModifiedSince(long),
     * but since the parser transparently opens the connection on behalf
     * of the developer, these properties are not available before the
     * connection is fetched. Setting these request header fields affects all
     * subsequent connections opened by the parser. For more direct control
     * create a <code>URLConnection</code> massage it the way you want and
     * then set it on the parser.<p>
     * From <a href="http://www.ietf.org/rfc/rfc2616.txt">
     * RFC 2616 Hypertext Transfer Protocol -- HTTP/1.1</a>: 
     * <pre>
     * 5.3 Request Header Fields
     *
     *    The request-header fields allow the client to pass additional
     *    information about the request, and about the client itself, to the
     *    server. These fields act as request modifiers, with semantics
     *    equivalent to the parameters on a programming language method
     *    invocation.
     *
     *        request-header = Accept                   ; Section 14.1
     *                       | Accept-Charset           ; Section 14.2
     *                       | Accept-Encoding          ; Section 14.3
     *                       | Accept-Language          ; Section 14.4
     *                       | Authorization            ; Section 14.8
     *                       | Expect                   ; Section 14.20
     *                       | From                     ; Section 14.22
     *                       | Host                     ; Section 14.23
     *                       | If-Match                 ; Section 14.24
     *                       | If-Modified-Since        ; Section 14.25
     *                       | If-None-Match            ; Section 14.26
     *                       | If-Range                 ; Section 14.27
     *                       | If-Unmodified-Since      ; Section 14.28
     *                       | Max-Forwards             ; Section 14.31
     *                       | Proxy-Authorization      ; Section 14.34
     *                       | Range                    ; Section 14.35
     *                       | Referer                  ; Section 14.36
     *                       | TE                       ; Section 14.39
     *                       | User-Agent               ; Section 14.43
     *
     *    Request-header field names can be extended reliably only in
     *    combination with a change in the protocol version. However, new or
     *    experimental header fields MAY be given the semantics of request-
     *    header fields if all parties in the communication recognize them to
     *    be request-header fields. Unrecognized header fields are treated as
     *    entity-header fields.
     * </pre>
     * @param properties The new set of default request header properties to
     * use. This affects all subsequently created connections.
     * @see #mDefaultRequestProperties
     * @see #setRequestProperties
     */
    public static void setDefaultRequestProperties (Hashtable properties)
    {
        mDefaultRequestProperties = properties;
    }

    /**
     * Gets the request header for the connection.
     * <em>This header is generated from the contents of the connection
     * and may not be exactly the same as the request that will be sent.</em>
     * @param connection The connection to convert into an HTTP request header.
     * @return The string that would be sent by the HTTP request.
     */
    public static String getRequestHeader (HttpURLConnection connection)
    {
        // dump it
        StringBuffer buffer;
        Map map;
        String key;
        List items;

        buffer = new StringBuffer (1024);
        buffer.append (connection.getRequestMethod ());
        buffer.append (" ");
        buffer.append (connection.getURL ());
        buffer.append (" HTTP/1.1\n");
        map  = connection.getRequestProperties ();
        for (Iterator iter = map.keySet ().iterator (); iter.hasNext (); )
        {
            key = (String)iter.next ();
            items = (List)map.get (key);
            buffer.append (key);
            buffer.append (": ");
            for (int i = 0; i < items.size (); i++)
            {
                if (0 != i)
                    buffer.append (", ");
                buffer.append (items.get (i));
            }
            buffer.append ("\n");
        }

        return (buffer.toString ());
    }

    /**
     * Gets the response header for the connection.
     * Calling this method on an un-connected connection will
     * generate an error, as will an attempt to get information
     * from a connected but invalid connection.
     * <em>This header is generated from the contents of the connection
     * and may not be exactly the same as the response that was received.</em>
     * @param conn The connection to convert into an HTTP response header.
     * @return The string that was sent as the HTTP response.
     */
    public static String getResponseHeader (HttpURLConnection conn)
    {
        // dump it
        StringBuffer buffer;
        int code;
        String message;
        String key;
        String value;

        buffer = new StringBuffer (1024);
        try
        {
            code = conn.getResponseCode ();
            if (-1 != code)
            {
                message = conn.getResponseMessage ();
                buffer.append ("HTTP/1.1 ");
                buffer.append (code);
                buffer.append (" ");
                buffer.append (message);
                buffer.append ("\n");
                for (int i = 0; null != (value = conn.getHeaderField (i)); i++)
                {
                    key = conn.getHeaderFieldKey (i);
                    if (null != key)
                    {
                        buffer.append (key);
                        buffer.append (": ");
                        buffer.append (value);
                        buffer.append ("\n");
                    }
                }
            }
        }
        catch (IOException ioe)
        {
            buffer.append (ioe.toString ());
        }

        return (buffer.toString ());
    }

    /**
     * Get the current request header properties.
     * A String-to-String map of header keys and values,
     * excluding proxy items, cookies and URL authorization.
     * @return The request header properties for this connection manager.
     */
    public Hashtable getRequestProperties ()
    {
        return (mRequestProperties);
    }

    /**
     * Set the current request properties.
     * Replaces the current set of fixed request properties with the given set.
     * This does not replace the Proxy-Authorization property which is
     * constructed from the values of {@link #setProxyUser}
     * and {@link #setProxyPassword} values or the Authorization property
     * which is constructed from the {@link #setUser}
     * and {@link #setPassword} values. Nor does it replace the
     * Cookie property which is constructed from the current cookie jar.
     * @param properties The new fixed properties.
     */
    public void setRequestProperties (Hashtable properties)
    {
        mRequestProperties = properties;
    }

    /**
     * Get the proxy host name, if any.
     * @return Returns the proxy host.
     */
    public String getProxyHost ()
    {
        return (mProxyHost);
    }

    /**
     * Set the proxy host to use.
     * @param host The host to use for proxy access.
     * <em>Note: You must also set the proxy {@link #setProxyPort port}.</em>
     */
    public void setProxyHost (String host)
    {
        mProxyHost = host;
    }

    /**
     * Get the proxy port number.
     * @return Returns the proxy port.
     */
    public int getProxyPort ()
    {
        return (mProxyPort);
    }

    /**
     * Set the proxy port number.
     * @param port The proxy port.
     * <em>Note: You must also set the proxy {@link #setProxyHost host}.</em>
     */
    public void setProxyPort (int port)
    {
        mProxyPort = port;
    }

    /**
     * Get the user name for proxy authorization, if any.
     * @return Returns the proxy user,
     * or <code>null</code> if no proxy authorization is required.
     */
    public String getProxyUser ()
    {
        return (mProxyUser);
    }

    /**
     * Set the user name for proxy authorization.
     * @param user The proxy user name.
     * <em>Note: You must also set the proxy {@link #setProxyPassword password}.</em>
     */
    public void setProxyUser (String user)
    {
        mProxyUser = user;
    }

    /**
     * Set the proxy user's password.
     * @return Returns the proxy password.
     */
    public String getProxyPassword ()
    {
        return (mProxyPassword);
    }

    /**
     * Get the proxy user's password.
     * @param password The password for the proxy user.
     * <em>Note: You must also set the proxy {@link #setProxyUser user}.</em>
     */
    public void setProxyPassword (String password)
    {
        mProxyPassword = password;
    }

    /**
     * Get the user name to access the URL.
     * @return Returns the username that will be used to access the URL,
     * or <code>null</code> if no authorization is required.
     */
    public String getUser ()
    {
        return (mUser);
    }

    /**
     * Set the user name to access the URL.
     * @param user The user name for accessing the URL.
     * <em>Note: You must also set the {@link #setPassword password}.</em>
     */
    public void setUser (String user)
    {
        mUser = user;
    }

    /**
     * Get the URL users's password.
     * @return Returns the URL password.
     */
    public String getPassword ()
    {
        return (mPassword);
    }

    /**
     * Set the URL users's password.
     * @param password The password for the URL.
     */
    public void setPassword (String password)
    {
        mPassword = password;
    }

    /**
     * Predicate to determine if cookie processing is currently enabled.
     * @return <code>true</code> if cookies are being processed.
     */
    public boolean getCookieProcessingEnabled ()
    {
        return (null != mCookieJar);
    }

    /**
     * Enables and disabled cookie processing.
     * @param enable if <code>true</code> cookie processing will occur,
     * else cookie processing will be turned off.
     */
    public void setCookieProcessingEnabled (boolean enable)
    {
        if (enable)
            mCookieJar = (null == mCookieJar) ? new Hashtable () : mCookieJar;
        else
            mCookieJar = null;
    }

    /**
     * Adds a cookie to the cookie jar.
     * @param cookie The cookie to add.
     * @param domain The domain to use in case the cookie has no domain attribute.
     */
    public void setCookie (Cookie cookie, String domain)
    {
        String path;
        Vector cookies;
        Cookie probe;

        if (null != cookie.getDomain ())
            domain = cookie.getDomain ();
        path = cookie.getPath ();
        if (null == mCookieJar)
            mCookieJar = new Hashtable (); // turn on cookie processing
        cookies = (Vector)mCookieJar.get (domain);
        if (null != cookies)
        {
            for (int j = 0; j < cookies.size (); j++)
            {
                probe = (Cookie)cookies.elementAt (j);
                if (probe.getName ().equalsIgnoreCase (cookie.getName ()))
                {
                    // we keep paths sorted most specific to least
                    if (probe.getPath ().equals (path))
                    {
                        cookies.setElementAt (cookie, j); // replace
                        break;
                    }
                    else if (path.startsWith (probe.getPath ()))
                    {
                        cookies.insertElementAt (cookie, j);
                        break;
                    }
                }
            }
        }
        else
        {   // new cookie list needed
            cookies = new Vector ();
            cookies.addElement (cookie);
            mCookieJar.put (domain, cookies);
        }

    }

    /**
     * Get the monitoring object, if any.
     * @return Returns the monitor, or null if none has been assigned.
     */
    public ConnectionMonitor getMonitor ()
    {
        return (mMonitor);
    }

    /**
     * Set the monitoring object.
     * @param monitor The monitor to set.
     */
    public void setMonitor (ConnectionMonitor monitor)
    {
        mMonitor = monitor;
    }

    /**
     * Opens a connection using the given url.
     * @param url The url to open.
     * @return The connection.
     * @exception ParserException if an i/o exception occurs accessing the url.
     */
    public URLConnection openConnection (URL url)
        throws
            ParserException
    {
        Properties sysprops;
        Hashtable properties;
        Enumeration enumeration;
        String key;
        String value;
        String set = null; // old proxySet value
        String host = null; // old proxyHost value
        String port = null; // old proxyPort value
        String host2 = null; // old http.proxyHost value
        String port2 = null; // old http.proxyPort value
        HttpURLConnection http;
        String auth;
        String encoded;
        URLConnection ret;

        try
        {
            try
            {
                // set up for proxy
                if ((null != getProxyHost ()) && (0 != getProxyPort ()))
                {
                    sysprops = System.getProperties ();
                    set = (String)sysprops.put ("proxySet", "true");
                    host = (String)sysprops.put ("proxyHost", getProxyHost ());
                    port = (String)sysprops.put ("proxyPort",
                        Integer.toString (getProxyPort ()));
                    // see http://java.sun.com/j2se/1.4.2/docs/guide/net/properties.html
                    host2 = (String)sysprops.put ("http.proxyHost",
                        getProxyHost ());
                    port2 = (String)sysprops.put ("http.proxyPort",
                        Integer.toString (getProxyPort ()));
                    System.setProperties (sysprops);
                    
                }
    
                // open the connection... but don't connect yet
                ret = url.openConnection ();
                if (ret instanceof HttpURLConnection)
                {
                    http = (HttpURLConnection)ret;
                    
                    // set the fixed request properties
                    properties = getRequestProperties ();
                    if (null != properties)
                        for (enumeration = properties.keys ();
                                enumeration.hasMoreElements ();)
                        {
                            key = (String)enumeration.nextElement ();
                            value = (String)properties.get (key);
                            ret.setRequestProperty (key, value);
                        }
    
                    // set the proxy name and password
                    if ((null != getProxyUser ())
                        && (null != getProxyPassword ()))
                    {
                        auth = getProxyUser () + ":" + getProxyPassword ();
                        encoded = encode (auth.getBytes("ISO-8859-1"));
                        ret.setRequestProperty ("Proxy-Authorization", encoded);
                    }
                    
                    // set the URL name and password
                    if ((null != getUser ()) && (null != getPassword ()))
                    {
                        auth = getUser () + ":" + getPassword ();
                        encoded = encode (auth.getBytes("ISO-8859-1"));
                        ret.setRequestProperty ("Authorization",
                            "Basic " + encoded);
                    }
    
                    // set the cookies based on the url
                    addCookies (ret);

                    if (null != getMonitor ())
                        getMonitor ().preConnect (http);
                }
                else
                    http = null;

                try
                {
                    ret.connect ();
                    
                    if (null != http)
                    {
                        if (null != getMonitor ())
                            getMonitor ().postConnect (http);
    
                        parseCookies (ret);
                    }
                }
                catch (UnknownHostException uhe)
                {
                    int message = (int)(Math.random () * FOUR_OH_FOUR.length);
                    throw new ParserException (FOUR_OH_FOUR[message], uhe);
                }
                catch (IOException ioe)
                {
                    throw new ParserException (ioe.getMessage (), ioe);
                }
            }
            finally
            {
                if ((null != getProxyHost ()) && (0 != getProxyPort ()))
                {
                    sysprops = System.getProperties ();
                    if (null != set)
                        sysprops.put ("proxySet", set);
                    else
                        sysprops.remove ("proxySet");
                    if (null != host)
                        sysprops.put ("proxyHost", host);
                    else
                        sysprops.remove ("proxyHost");
                    if (null != port)
                        sysprops.put ("proxyPort", port);
                    else
                        sysprops.remove ("proxyPort");
                    if (null != host2)
                        sysprops.put ("http.proxyHost", host2);
                    else
                        sysprops.remove ("http.proxyHost");
                    if (null != port2)
                        sysprops.put ("http.proxyPort", port2);
                    else
                        sysprops.remove ("http.proxyPort");
                    System.setProperties (sysprops);
                }
            }
        }
        catch (IOException ioe)
        {
            String msg = "Error in opening a connection to "
                + url.toExternalForm ();
            ParserException ex = new ParserException (msg, ioe);
            throw ex;
        }

        return (ret);
    }

    /**
     * Encodes a byte array into BASE64 in accordance with
     * <a href="http://www.faqs.org/rfcs/rfc2045.html">RFC 2045</a>.
     * @param array The bytes to convert.
     * @return A BASE64 encoded string.
     */
    public final static String encode (byte[] array)
    {
        int last; // last byte
        int count; // character count
        int separators; // line separator count
        int length; // length of returned string
        char[] encoded; // encoded characters
        int left; // bytes left
        int end;
        int block; // encoding buffer
        int r; // shift count
        int n; // byte to encode
        int index; // index into output array
        String ret;

        if ((null != array) && (0 != array.length))
        {
            last = array.length - 1;
            count = (last / 3 + 1) << 2;
            separators = (count - 1) / 76;
            length = count + separators;
            encoded = new char[length];
            index = 0;
            separators = 0;
            for (int i = 0; i <= last; i += 3)
            {
                left = last - i;
                end = (left > 1 ? 2 : left);
    
                // collect 1 to 3 bytes to encode
                block = 0;
                r = 16;
                for (int j = 0; j <= end; j++)
                {
                    n = array[i + j];
                    block += (n < 0 ? n + 256 : n) << r;
                    r -= 8;
                }
    
                // encode into 2-4 chars padding with '=' if no data left
                encoded[index++] = BASE64_CHAR_TABLE[(block >>> 18) & 0x3f];
                encoded[index++] = BASE64_CHAR_TABLE[(block >>> 12) & 0x3f];
                encoded[index++] = left > 0 ?
                    BASE64_CHAR_TABLE[(block >>> 6) & 0x3f] :
                    '=';
                encoded[index++] = left > 1 ?
                    BASE64_CHAR_TABLE[block & 0x3f] :
                    '=';
    
                if ((0 == (index - separators) % 76) && (index < length))
                {
                    encoded[index++] = '\n';
                    separators += 1;
                }
            }
            ret = new String (encoded);
        }
        else
            ret = "";

        return (ret);
    }

    /**
     * Turn spaces into %20.
     * ToDo: make this more generic
     * (see RFE #1010593 provide URL encoding/decoding utilities).
     * @param url The url containing spaces.
     * @return The URL with spaces as %20 sequences.
     */
    public String fixSpaces (String url)
    {
        int index;
        int length;
        char ch;
        StringBuffer buffer;

        index = url.indexOf (' ');
        if (-1 != index)
        {
            length = url.length ();
            buffer = new StringBuffer (length * 3);
            buffer.append (url.substring (0, index));
            for (int i = index; i < length; i++)
            {
                ch = url.charAt (i);
                if (ch==' ')
                    buffer.append ("%20");
                else
                    buffer.append (ch);
            }
            url = buffer.toString ();
        }

        return (url);
    }

    /**
     * Opens a connection based on a given string.
     * The string is either a file, in which case <code>file://localhost</code>
     * is prepended to a canonical path derived from the string, or a url that
     * begins with one of the known protocol strings, i.e. <code>http://</code>.
     * Embedded spaces are silently converted to %20 sequences.
     * @param string The name of a file or a url.
     * @return The connection.
     * @exception ParserException if the string is not a valid url or file.
     */
    public URLConnection openConnection (String string)
        throws
            ParserException
    {
        final String prefix = "file://localhost";
        String resource;
        URL url;
        StringBuffer buffer;
        URLConnection ret;

        try
        {
            url = new URL (fixSpaces (string));
            ret =  openConnection (url);
        }
        catch (MalformedURLException murle)
        {   // try it as a file
            try
            {
                File file = new File (string);
                resource = file.getCanonicalPath ();
                buffer = new StringBuffer (prefix.length ()
                    + resource.length ());
                buffer.append (prefix);
                if (!resource.startsWith ("/"))
                    buffer.append ("/");
                buffer.append (resource);
                url = new URL (fixSpaces (buffer.toString ()));
                ret = openConnection (url);
            }
            catch (MalformedURLException murle2)
            {
                String msg = "Error in opening a connection to " + string;
                ParserException ex = new ParserException (msg, murle2);
                throw ex;
            }
            catch (IOException ioe)
            {
                String msg = "Error in opening a connection to " + string;
                ParserException ex = new ParserException (msg, ioe);
                throw ex;
            }
        }

        return (ret);
    }

    /**
     * Generate a HTTP cookie header value string from the cookie jar.
     * <pre>
     *   The syntax for the header is:
     *
     *    cookie          =       "Cookie:" cookie-version
     *                            1*((";" | ",") cookie-value)
     *    cookie-value    =       NAME "=" VALUE [";" path] [";" domain]
     *    cookie-version  =       "$Version" "=" value
     *    NAME            =       attr
     *    VALUE           =       value
     *    path            =       "$Path" "=" value
     *    domain          =       "$Domain" "=" value
     *
     * </pre>
     * @param connection The connection being accessed.
     * @see <a href="http://www.ietf.org/rfc/rfc2109.txt">RFC 2109</a>
     * @see <a href="http://www.ietf.org/rfc/rfc2396.txt">RFC 2396</a>
     */
    public void addCookies (URLConnection connection)
    {
        Vector list;
        URL url;
        String host;
        String path;
        String domain;

        if (null != mCookieJar)
        {
            list = null;
            // get the site from the URL
            url = connection.getURL ();
            host = url.getHost ();
            path = url.getPath ();
            if (0 == path.length ())
                path = "/";
            if (null != host)
            {   // http://www.objectsdevelopment.com/portal/modules/freecontent/content/javawebserver.html
                list = addCookies ((Vector)mCookieJar.get (host), path, list);
                domain = getDomain (host);
                if (null != domain)
                    list = addCookies ((Vector)mCookieJar.get (domain),
                        path, list);
                else
                    // maybe it is the domain we're accessing
                    list = addCookies ((Vector)mCookieJar.get ("." + host),
                        path, list);
            }
            if (null != list)
                connection.setRequestProperty ("Cookie",
                    generateCookieProperty (list));
        }
    }

    /**
     * Add qualified cookies from cookies into list.
     * @param cookies The list of cookies to check (may be null).
     * @param path The path being accessed.
     * @param list The list of qualified cookies.
     * @return The list of qualified cookies.
     */
    protected Vector addCookies (Vector cookies, String path, Vector list)
    {
        Cookie cookie;
        Date expires;
        Date now;

        if (null != cookies)
        {
            now = new Date ();
            for (int i = 0; i < cookies.size (); i++)
            {
                cookie = (Cookie)cookies.elementAt (i);
                expires = cookie.getExpiryDate ();
                if ((null != expires) && expires.before (now))
                {
                    cookies.remove (i);
                    i--; // dick with the loop variable
                }
                else
                    if (path.startsWith (cookie.getPath ()))
                    {
                        if (null == list)
                            list = new Vector ();
                        list.addElement (cookie);
                    }
            }
        }
        
        return (list);
    }

    /**
     * Get the domain from a host.
     * @param host The supposed host name.
     * @return The domain (with the leading dot),
     * or null if the domain cannot be determined.
     */
    protected String getDomain (String host)
    {
        StringTokenizer tokenizer;
        int count;
        String server;
        int length;
        boolean ok;
        char c;
        String ret;
        
        ret = null;
        
        tokenizer = new StringTokenizer (host, ".");
        count = tokenizer.countTokens ();
        if (3 <= count)
        {
            // have at least two dots,
            // check if we were handed an IP address by mistake
            length = host.length ();
            ok = false;
            for (int i = 0; i < length && !ok; i++)
            {
                c = host.charAt (i);
                if (!(Character.isDigit (c) || (c == '.')))
                    ok = true;
            }
            if (ok)
            {
                // so take everything after the first token
                server = tokenizer.nextToken ();
                length = server.length ();
                ret = host.substring (length);
            }
        }

        return (ret);
    }

    /**
     * Creates the cookie request property value from the list of
     * valid cookies for the domain.
     * @param cookies The list of valid cookies to be encoded in the request.
     * @return A string suitable for inclusion as the value of
     * the "Cookie:" request property.
     */
    protected String generateCookieProperty (Vector cookies)
    {
        int version;
        Cookie cookie;
        StringBuffer buffer;
        String ret;
        
        ret = null;

        buffer = new StringBuffer ();
        version = 0;
        for (int i = 0; i < cookies.size (); i++)
            version = Math.max (version,
                ((Cookie)cookies.elementAt (i)).getVersion ());
        if (0 != version)
        {
            buffer.append ("$Version=\"");
            buffer.append (version);
            buffer.append ("\"");
        }
        for (int i = 0; i < cookies.size (); i++)
        {
            cookie = (Cookie)cookies.elementAt (i);
            if (0 != buffer.length ())
                buffer.append ("; ");
            buffer.append (cookie.getName ());
            buffer.append ("=");
            if (0 != version)
                buffer.append ("\"");
            buffer.append (cookie.getValue ());
            if (0 != version)
                buffer.append ("\"");
            if (0 != version)
            {
                if ((null != cookie.getPath ())
                    && (0 != cookie.getPath ().length ()))
                {
                    buffer.append ("; $Path=\"");
                    buffer.append (cookie.getPath ());
                    buffer.append ("\"");
                }
                if ((null != cookie.getDomain ())
                    && (0 != cookie.getDomain ().length ()))
                {
                    buffer.append ("; $Domain=\"");
                    buffer.append (cookie.getDomain ());
                    buffer.append ("\"");
                }
            }
        }
        if (0 != buffer.length ())
            ret = buffer.toString ();

        return (ret);
    }
    
    /**
     * Check for cookie and parse into cookie jar.
     * @param connection The connection to extract cookie information from.
     */
    public void parseCookies (URLConnection connection)
    {
        String string;
        Vector cookies;
        StringTokenizer tokenizer;
        String token;
        int index;
        String name;
        String key;
        String value;
        Cookie cookie;
        
        string = connection.getHeaderField ("Set-Cookie");
        if (null != string)
        {
//            set-cookie      =       "Set-Cookie:" cookies
//            cookies         =       1#cookie
//            cookie          =       NAME "=" VALUE *(";" cookie-av)
//            NAME            =       attr
//            VALUE           =       value
//            cookie-av       =       "Comment" "=" value
//                            |       "Domain" "=" value
//                            |       "Max-Age" "=" value
//                            |       "Path" "=" value
//                            |       "Secure"
//                            |       "Version" "=" 1*DIGIT
            cookies = new Vector ();
            tokenizer = new StringTokenizer (string, ";,", true);
            cookie = null;
            while (tokenizer.hasMoreTokens ())
            {
                token = tokenizer.nextToken ().trim ();
                if (token.equals (";"))
                    continue;
                else if (token.equals (","))
                {
                    cookie = null;
                    continue;
                }
                    
                index = token.indexOf ('=');
                if (-1 == index)
                {
                    name = token;
                    value = null;
                    if (null == cookie)
                        throw new IllegalStateException ("no cookie value");
                    key = name.toLowerCase ();
                }
                else
                {
                    name = token.substring (0, index);
                    value = token.substring (index + 1);
                    key = name.toLowerCase ();
                }

                if (null == cookie)
                {
                    cookie = new Cookie (name, value);
                    cookies.addElement (cookie);
                }
                else
                {
                    if (key.equals ("expires")) // Wdy, DD-Mon-YY HH:MM:SS GMT
                    {
                        String comma = tokenizer.nextToken ();
                        String rest = tokenizer.nextToken ();
                        SimpleDateFormat format = new SimpleDateFormat (
                            "EEE, dd-MMM-yy kk:mm:ss z");
                        try
                        {
                            Date date = format.parse (value + comma + rest);
                            cookie.setExpiryDate (date);
                        }
                        catch (ParseException pe)
                        {
                            // ok now what
                            cookie.setExpiryDate (null);
                        }
                    }
                    else
                        if (key.equals ("domain"))
                            cookie.setDomain (value);
                        else
                            if (key.equals ("path"))
                                cookie.setPath (value);
                            else
                                if (key.equals ("secure"))
                                    cookie.setSecure (true);
                                else
                                    if (key.equals ("comment"))
                                        cookie.setComment (value);
                                    else
                                        if (key.equals ("version"))
                                            cookie.setVersion (
                                                Integer.parseInt (value));
                                        else
                                            if (key.equals ("max-age"))
                                            {
                                                Date date = new Date ();
                                                long then = date.getTime ()
                                                + Integer.parseInt (value)
                                                * 1000;
                                                date.setTime (then);
                                                cookie.setExpiryDate (date);
                                            }
                                            else
                                            {   // error,? unknown attribute,
                                                // maybe just another cookie
                                                // not separated by a comma
                                                cookie = new Cookie (name,
                                                    value);
                                                cookies.addElement (cookie);
                                            }
                }
           }
           if (0 != cookies.size ())
               saveCookies (cookies, connection);
        }
    }

    /**
     * Save the cookies received in the response header.
     * @param list The list of cookies extracted from the response header.
     * @param connection The connection (used when a cookie has no domain).
     */
    protected void saveCookies (Vector list, URLConnection connection)
    {
        Cookie cookie;
        String domain;

        for (int i = 0; i < list.size (); i++)
        {
            cookie = (Cookie)list.elementAt (i);
            domain = cookie.getDomain ();
            if (null == domain)
                domain = connection.getURL ().getHost ();
            setCookie (cookie, domain);
        }
    }
}

