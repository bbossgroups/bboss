// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/http/ConnectionMonitor.java,v $
// $Author: derrickoswald $
// $Date: 2005/04/10 23:20:43 $
// $Revision: 1.2 $
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

import java.net.HttpURLConnection;

import org.htmlparser.util.ParserException;

/**
 * Interface for HTTP connection notification callbacks.
 */
public interface ConnectionMonitor
{
    /**
     * Called just prior to calling connect.
     * The connection has been conditioned with proxy, URL user/password,
     * and cookie information. It is still possible to adjust the
     * connection, to alter the request method for example. 
     * @param connection The connection which is about to be connected.
     * @exception ParserException This exception is thrown if the connection
     * monitor wants the ConnectionManager to bail out.
     */
    void preConnect (HttpURLConnection connection)
    	throws
            ParserException;

    /** Called just after calling connect.
     * The response code and header fields can be examined.
     * @param connection The connection that was just connected.
     * @exception ParserException This exception is thrown if the connection
     * monitor wants the ConnectionManager to bail out.
     */
    void postConnect (HttpURLConnection connection)
    	throws
            ParserException;
}
