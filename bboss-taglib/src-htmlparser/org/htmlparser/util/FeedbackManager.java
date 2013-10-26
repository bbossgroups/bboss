// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Claude Duguay
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/util/FeedbackManager.java,v $
// $Author: derrickoswald $
// $Date: 2004/01/02 16:24:58 $
// $Revision: 1.44 $
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
 * Implementaiton of static methods that allow the parser to
 * route various messages to any implementation of the
 * HTMLParserFeedback interface. End users can use the default
 * DefaultHTMLParserFeedback or may provide their own by calling
 * the setParserFeedback method.
 *
 * @see ParserFeedback
 * @see DefaultParserFeedback
**/

public class FeedbackManager
{
  protected static ParserFeedback callback =
    new DefaultParserFeedback();

  public static void setParserFeedback(ParserFeedback feedback)
  {
    callback = feedback;
  }

  public static void info(String message)
  {
    callback.info(message);
  }

  public static void warning(String message)
  {
    callback.warning(message);
  }

  public static void error(String message, ParserException e)
  {
    callback.error(message, e);
  }
}
