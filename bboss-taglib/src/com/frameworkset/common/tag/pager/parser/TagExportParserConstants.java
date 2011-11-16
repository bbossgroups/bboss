/*****************************************************************************
 *                                                                           *
 *  This file is part of the tna framework distribution.                     *
 *  Documentation and updates may be get from  biaoping.yin the author of    *
 *  this framework							     *
 *                                                                           *
 *  Sun Public License Notice:                                               *
 *                                                                           *
 *  The contents of this file are subject to the Sun Public License Version  *
 *  1.0 (the "License"); you may not use this file except in compliance with *
 *  the License. A copy of the License is available at http://www.sun.com    * 
 *                                                                           *
 *  The Original Code is tag. The Initial Developer of the Original          *    
 *  Code is biaoping yin. Portions created by biaoping yin are Copyright     *
 *  (C) 2000.  All Rights Reserved.                                          *
 *                                                                           *
 *  GNU Public License Notice:                                               *
 *                                                                           *
 *  Alternatively, the contents of this file may be used under the terms of  *
 *  the GNU Lesser General Public License (the "LGPL"), in which case the    *
 *  provisions of LGPL are applicable instead of those above. If you wish to *
 *  allow use of your version of this file only under the  terms of the LGPL *
 *  and not to allow others to use your version of this file under the SPL,  *
 *  indicate your decision by deleting the provisions above and replace      *
 *  them with the notice and other provisions required by the LGPL.  If you  *
 *  do not delete the provisions above, a recipient may use your version of  *
 *  this file under either the SPL or the LGPL.                              *
 *                                                                           *
 *  biaoping.yin (yin-bp@163.com)                                            *
 *  Author of Learning Java 						     					 *
 *                                                                           *
 *****************************************************************************/
package com.frameworkset.common.tag.pager.parser;

import java.io.Serializable;

public interface TagExportParserConstants extends Serializable {

  int EOF = 0;
  int EQUALS = 6;
  int COMMA = 7;
  int SEMICOLON = 8;
  int PAGER_NUMBER = 9;
  int PAGER_OFFSET = 10;
  int PAGER_PAGE = 11;
  int PAGER_PAGENUMBER = 12;
  int PAGER_PAGEOFFSET = 13;
  int INDEX_ITEMCOUNT = 14;
  int INDEX_ITEMS = 15;
  int INDEX_PAGECOUNT = 16;
  int INDEX_PAGES = 17;
  int PAGE_FIRST = 18;
  int PAGE_FIRSTITEM = 19;
  int PAGE_LAST = 20;
  int PAGE_LASTITEM = 21;
  int PAGE_NUMBER = 22;
  int PAGE_PAGE = 23;
  int PAGE_PAGENUMBER = 24;
  int PAGE_PAGEURL = 25;
  int PAGE_URL = 26;
  int IDENTIFIER = 27;
  int LETTER = 28;
  int DIGIT = 29;

  int DEFAULT = 0;
  int PAGER_STATE = 1;
  int INDEX_STATE = 2;
  int PAGE_STATE = 3;

  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\"\\f\"",
    "\"=\"",
    "\",\"",
    "\";\"",
    "\"number\"",
    "\"offset\"",
    "\"page\"",
    "\"pageNumber\"",
    "\"pageOffset\"",
    "\"itemCount\"",
    "\"items\"",
    "\"pageCount\"",
    "\"pages\"",
    "\"first\"",
    "\"firstItem\"",
    "\"last\"",
    "\"lastItem\"",
    "\"number\"",
    "\"page\"",
    "\"pageNumber\"",
    "\"pageUrl\"",
    "\"url\"",
    "<IDENTIFIER>",
    "<LETTER>",
    "<DIGIT>",
  };

}
