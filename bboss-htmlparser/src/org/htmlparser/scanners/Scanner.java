// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2003 Derrick Oswald
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/scanners/Scanner.java,v $
// $Author: derrickoswald $
// $Date: 2004/07/02 00:49:28 $
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

package org.htmlparser.scanners;

import org.htmlparser.Tag;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

/**
 * Generic interface for scanning.
 * Tags needing specialized operations can provide an object that implements
 * this interface via getThisScanner().
 * By default non-composite tags simply perform the semantic action and
 * return while composite tags will gather their children.
 */
public interface Scanner
{
    /**
     * Scan the tag.
     * The Lexer is provided in order to do a lookahead operation.
     * @param tag HTML tag to be scanned for identification.
     * @param lexer Provides html page access.
     * @param stack The parse stack. May contain pending tags that enclose
     * this tag. Nodes on the stack should be considered incomplete.
     * @return The resultant tag (may be unchanged).
     * @exception ParserException if an unrecoverable problem occurs.
     */
    public Tag scan (Tag tag, Lexer lexer, NodeList stack) throws ParserException;
}
