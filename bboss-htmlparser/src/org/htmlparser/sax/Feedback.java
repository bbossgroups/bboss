// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/sax/Feedback.java,v $
// $Author: derrickoswald $
// $Date: 2005/04/12 11:27:43 $
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

package org.htmlparser.sax;

import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXParseException;

import org.htmlparser.util.ParserException;
import org.htmlparser.util.ParserFeedback;
import org.xml.sax.SAXException;

/**
 * Mediates between the feedback mechanism of the htmlparser and an error handler.
 */
public class Feedback
    implements
        ParserFeedback
{
    /**
     * The error handler to call back on.
     */
    protected ErrorHandler mErrorHandler;

    /**
     * The locator for tag positions.
     */
    protected Locator mLocator;

    /**
     * Create a feedback/error handler mediator.
     * @param handler The callback object.
     * @param locator A locator for error locations.
     */
    public Feedback (ErrorHandler handler, Locator locator)
    {
        mErrorHandler = handler;
        mLocator = locator;
    }

    /**
     * Information message.
     * <em>Just eats the info message.</em>
     * @param message {@inheritDoc} 
     */
    public void info (String message)
    {
        // swallow
    }

    /**
     * Warning message.
     * Calls {@link ErrorHandler#warning(SAXParseException) ErrorHandler.warning}.
     * @param message {@inheritDoc} 
     */
    public void warning (String message)
    {
        try
        {
            mErrorHandler.warning (
                new SAXParseException (message, mLocator));
        }
        catch (SAXException se)
        {
            se.printStackTrace ();
        }
    }

    /**
     * Error message.
     * Calls {@link ErrorHandler#error(SAXParseException) ErrorHandler.error}.
     * @param message {@inheritDoc} 
     * @param e {@inheritDoc} 
     */
    public void error (String message, ParserException e)
    {
        try
        {
            mErrorHandler.error (
                new SAXParseException (message, mLocator, e));
        }
        catch (SAXException se)
        {
            se.printStackTrace ();
        }
    }
}
