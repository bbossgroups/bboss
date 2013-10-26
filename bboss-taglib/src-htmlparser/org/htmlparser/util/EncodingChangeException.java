// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Claude Duguay
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/util/EncodingChangeException.java,v $
// $Author: derrickoswald $
// $Date: 2004/01/10 15:23:33 $
// $Revision: 1.1 $
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

/**
 * The encoding is changed invalidating already scanned characters.
 * When the encoding is changed, as for example when encountering a &lt;META&gt;
 * tag that includes a charset directive in the content attribute that
 * disagrees with the encoding specified by the HTTP header (or the default
 * encoding if none), the parser retraces the bytes it has interpreted so far
 * comparing the characters produced under the new encoding. If the new
 * characters differ from those it has already yielded to the application, it
 * throws this exception to indicate that processing should be restarted under
 * the new encoding.
 * This exception is the object thrown so that applications may distinguish
 * between an encoding change, which may be successfully cured by restarting
 * the parse from the beginning, from more serious errors.
 * @see IteratorImpl
 * @see ParserException
 **/
public class EncodingChangeException
    extends
        ParserException
{
    /**
     * Create an exception idicative of a problematic encoding change.
     * @param message The message describing the error condifion.
     */
    public EncodingChangeException (String message)
    {
        super(message);
    }
}

