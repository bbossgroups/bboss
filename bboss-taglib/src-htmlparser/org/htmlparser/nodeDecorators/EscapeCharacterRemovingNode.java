// HTMLParser Library $Name: v1_5 $ - A java-based parser for HTML
// http://sourceforge.org/projects/htmlparser
// Copyright (C) 2004 Joshua Kerievsky
//
// Revision Control Information
//
// $Source: /cvsroot/htmlparser/htmlparser/src/org/htmlparser/nodeDecorators/EscapeCharacterRemovingNode.java,v $
// $Author: derrickoswald $
// $Date: 2005/04/10 23:20:43 $
// $Revision: 1.15 $
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

package org.htmlparser.nodeDecorators;

import org.htmlparser.Text;
import org.htmlparser.util.ParserUtils;

/**
 * @deprecated Use direct subclasses or dynamic proxies instead.
 * <p>Use either direct subclasses of the appropriate node and set them on the
 * {@link org.htmlparser.PrototypicalNodeFactory PrototypicalNodeFactory},
 * or use a dynamic proxy implementing the required node type interface.</p>
 * @see AbstractNodeDecorator
 */
public class EscapeCharacterRemovingNode extends AbstractNodeDecorator {
    public EscapeCharacterRemovingNode(Text newDelegate) {
        super(newDelegate);
    }

    public String toPlainTextString() {
        return ParserUtils.removeEscapeCharacters(delegate.toPlainTextString());
    }
}
