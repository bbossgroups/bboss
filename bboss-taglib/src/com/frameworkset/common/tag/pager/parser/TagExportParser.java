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
import java.io.StringReader;
import java.util.Hashtable;

public class TagExportParser implements TagExportParserConstants, Serializable {

        private static Hashtable pagerTagExportCache = new Hashtable();
        private static Hashtable indexTagExportCache = new Hashtable();
        private static Hashtable pageTagExportCache = new Hashtable();

        public static PagerTagExport parsePagerTagExport(String expression)
                throws ParseException
        {
                synchronized (pagerTagExportCache) {
                        PagerTagExport pagerTagExport =
                                (PagerTagExport) pagerTagExportCache.get(expression);
                        if (pagerTagExport == null) {
                                StringReader reader = new StringReader(expression);
                                TagExportParser parser = new TagExportParser(reader);
                                pagerTagExport = parser.parsePagerTagExport();
                                pagerTagExportCache.put(expression, pagerTagExport);
                        }
                        return pagerTagExport;
                }
        }

        public static IndexTagExport parseIndexTagExport(String expression)
                throws ParseException
        {
                synchronized (indexTagExportCache) {
                        IndexTagExport indexTagExport =
                                (IndexTagExport) indexTagExportCache.get(expression);
                        if (indexTagExport == null) {
                                StringReader reader = new StringReader(expression);
                                TagExportParser parser = new TagExportParser(reader);
                                indexTagExport = parser.parseIndexTagExport();
                                indexTagExportCache.put(expression, indexTagExport);
                        }
                        return indexTagExport;
                }
        }

        public static PageTagExport parsePageTagExport(String expression)
                throws ParseException
        {
                synchronized (pageTagExportCache) {
                        PageTagExport pageTagExport =
                                (PageTagExport) pageTagExportCache.get(expression);
                        if (pageTagExport == null) {
                                StringReader reader = new StringReader(expression);
                                TagExportParser parser = new TagExportParser(reader);
                                pageTagExport = parser.parsePageTagExport();
                                pageTagExportCache.put(expression, pageTagExport);
                        }
                        return pageTagExport;
                }
        }

  final public PagerTagExport parsePagerTagExport() throws ParseException {
        PagerTagExport pagerTagExport = new PagerTagExport();
                token_source.SwitchTo(PAGER_STATE);
    pagerExportDeclaration(pagerTagExport);
    jj_consume_token(0);
                {if (true) return pagerTagExport;}
    throw new Error("Missing return statement in function");
  }

  final public void pagerExportDeclaration(PagerTagExport pagerTagExport) throws ParseException {
    label_1:
    while (true) {
      pagerExportList(pagerTagExport);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PAGER_NUMBER:
      case PAGER_OFFSET:
      case PAGER_PAGE:
      case PAGER_PAGENUMBER:
      case PAGER_PAGEOFFSET:
      case IDENTIFIER:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
    }
  }

  final public void pagerExportList(PagerTagExport pagerTagExport) throws ParseException {
    pagerExport(pagerTagExport);
    label_2:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        jj_la1[1] = jj_gen;
        break label_2;
      }
      jj_consume_token(COMMA);
      pagerExport(pagerTagExport);
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SEMICOLON:
      jj_consume_token(SEMICOLON);
      break;
    default:
      jj_la1[2] = jj_gen;
      ;
    }
  }

  final public void pagerExport(PagerTagExport pagerTagExport) throws ParseException {
        Token t = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case PAGER_OFFSET:
    case PAGER_PAGEOFFSET:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PAGER_OFFSET:
        t = jj_consume_token(PAGER_OFFSET);
        break;
      case PAGER_PAGEOFFSET:
        t = jj_consume_token(PAGER_PAGEOFFSET);
        break;
      default:
        jj_la1[3] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                pagerTagExport.setPageOffset(t.image);
      break;
    case PAGER_NUMBER:
    case PAGER_PAGE:
    case PAGER_PAGENUMBER:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PAGER_PAGE:
        t = jj_consume_token(PAGER_PAGE);
        break;
      case PAGER_NUMBER:
        t = jj_consume_token(PAGER_NUMBER);
        break;
      case PAGER_PAGENUMBER:
        t = jj_consume_token(PAGER_PAGENUMBER);
        break;
      default:
        jj_la1[4] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                pagerTagExport.setPageNumber(t.image);
      break;
    case IDENTIFIER:
      t = jj_consume_token(IDENTIFIER);
      jj_consume_token(EQUALS);
      pagerVar(pagerTagExport, t);
      break;
    default:
      jj_la1[5] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void pagerVar(PagerTagExport pagerTagExport, Token id) throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case PAGER_OFFSET:
    case PAGER_PAGEOFFSET:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PAGER_OFFSET:
        jj_consume_token(PAGER_OFFSET);
        break;
      case PAGER_PAGEOFFSET:
        jj_consume_token(PAGER_PAGEOFFSET);
        break;
      default:
        jj_la1[6] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                pagerTagExport.setPageOffset(id.image);
      break;
    case PAGER_NUMBER:
    case PAGER_PAGE:
    case PAGER_PAGENUMBER:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PAGER_PAGE:
        jj_consume_token(PAGER_PAGE);
        break;
      case PAGER_NUMBER:
        jj_consume_token(PAGER_NUMBER);
        break;
      case PAGER_PAGENUMBER:
        jj_consume_token(PAGER_PAGENUMBER);
        break;
      default:
        jj_la1[7] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                pagerTagExport.setPageNumber(id.image);
      break;
    default:
      jj_la1[8] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public IndexTagExport parseIndexTagExport() throws ParseException {
        IndexTagExport indexTagExport = new IndexTagExport();
                token_source.SwitchTo(INDEX_STATE);
    indexExportDeclaration(indexTagExport);
    jj_consume_token(0);
                {if (true) return indexTagExport;}
    throw new Error("Missing return statement in function");
  }

  final public void indexExportDeclaration(IndexTagExport indexTagExport) throws ParseException {
    label_3:
    while (true) {
      indexExportList(indexTagExport);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INDEX_ITEMCOUNT:
      case INDEX_ITEMS:
      case INDEX_PAGECOUNT:
      case INDEX_PAGES:
      case IDENTIFIER:
        ;
        break;
      default:
        jj_la1[9] = jj_gen;
        break label_3;
      }
    }
  }

  final public void indexExportList(IndexTagExport indexTagExport) throws ParseException {
    indexExport(indexTagExport);
    label_4:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        jj_la1[10] = jj_gen;
        break label_4;
      }
      jj_consume_token(COMMA);
      indexExport(indexTagExport);
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SEMICOLON:
      jj_consume_token(SEMICOLON);
      break;
    default:
      jj_la1[11] = jj_gen;
      ;
    }
  }

  final public void indexExport(IndexTagExport indexTagExport) throws ParseException {
        Token t = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INDEX_ITEMCOUNT:
    case INDEX_ITEMS:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INDEX_ITEMS:
        t = jj_consume_token(INDEX_ITEMS);
        break;
      case INDEX_ITEMCOUNT:
        t = jj_consume_token(INDEX_ITEMCOUNT);
        break;
      default:
        jj_la1[12] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                indexTagExport.setItemCount(t.image);
      break;
    case INDEX_PAGECOUNT:
    case INDEX_PAGES:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INDEX_PAGES:
        t = jj_consume_token(INDEX_PAGES);
        break;
      case INDEX_PAGECOUNT:
        t = jj_consume_token(INDEX_PAGECOUNT);
        break;
      default:
        jj_la1[13] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                indexTagExport.setPageCount(t.image);
      break;
    case IDENTIFIER:
      t = jj_consume_token(IDENTIFIER);
      jj_consume_token(EQUALS);
      indexVar(indexTagExport, t);
      break;
    default:
      jj_la1[14] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void indexVar(IndexTagExport indexTagExport, Token id) throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case INDEX_ITEMCOUNT:
    case INDEX_ITEMS:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INDEX_ITEMS:
        jj_consume_token(INDEX_ITEMS);
        break;
      case INDEX_ITEMCOUNT:
        jj_consume_token(INDEX_ITEMCOUNT);
        break;
      default:
        jj_la1[15] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                indexTagExport.setItemCount(id.image);
      break;
    case INDEX_PAGECOUNT:
    case INDEX_PAGES:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case INDEX_PAGES:
        jj_consume_token(INDEX_PAGES);
        break;
      case INDEX_PAGECOUNT:
        jj_consume_token(INDEX_PAGECOUNT);
        break;
      default:
        jj_la1[16] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                indexTagExport.setPageCount(id.image);
      break;
    default:
      jj_la1[17] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public PageTagExport parsePageTagExport() throws ParseException {
        PageTagExport pageTagExport = new PageTagExport();
                token_source.SwitchTo(PAGE_STATE);
    pageExportDeclaration(pageTagExport);
    jj_consume_token(0);
                {if (true) return pageTagExport;}
    throw new Error("Missing return statement in function");
  }

  final public void pageExportDeclaration(PageTagExport pageTagExport) throws ParseException {
    label_5:
    while (true) {
      pageExportList(pageTagExport);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PAGE_FIRST:
      case PAGE_FIRSTITEM:
      case PAGE_LAST:
      case PAGE_LASTITEM:
      case PAGE_NUMBER:
      case PAGE_PAGE:
      case PAGE_PAGENUMBER:
      case PAGE_PAGEURL:
      case PAGE_URL:
      case IDENTIFIER:
        ;
        break;
      default:
        jj_la1[18] = jj_gen;
        break label_5;
      }
    }
  }

  final public void pageExportList(PageTagExport pageTagExport) throws ParseException {
    pageExport(pageTagExport);
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case COMMA:
        ;
        break;
      default:
        jj_la1[19] = jj_gen;
        break label_6;
      }
      jj_consume_token(COMMA);
      pageExport(pageTagExport);
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case SEMICOLON:
      jj_consume_token(SEMICOLON);
      break;
    default:
      jj_la1[20] = jj_gen;
      ;
    }
  }

  final public void pageExport(PageTagExport pageTagExport) throws ParseException {
        Token t = null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case PAGE_PAGEURL:
    case PAGE_URL:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PAGE_URL:
        t = jj_consume_token(PAGE_URL);
        break;
      case PAGE_PAGEURL:
        t = jj_consume_token(PAGE_PAGEURL);
        break;
      default:
        jj_la1[21] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                pageTagExport.setPageUrl(t.image);
      break;
    case PAGE_NUMBER:
    case PAGE_PAGE:
    case PAGE_PAGENUMBER:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PAGE_PAGE:
        t = jj_consume_token(PAGE_PAGE);
        break;
      case PAGE_NUMBER:
        t = jj_consume_token(PAGE_NUMBER);
        break;
      case PAGE_PAGENUMBER:
        t = jj_consume_token(PAGE_PAGENUMBER);
        break;
      default:
        jj_la1[22] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                pageTagExport.setPageNumber(t.image);
      break;
    case PAGE_FIRST:
    case PAGE_FIRSTITEM:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PAGE_FIRST:
        t = jj_consume_token(PAGE_FIRST);
        break;
      case PAGE_FIRSTITEM:
        t = jj_consume_token(PAGE_FIRSTITEM);
        break;
      default:
        jj_la1[23] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                pageTagExport.setFirstItem(t.image);
      break;
    case PAGE_LAST:
    case PAGE_LASTITEM:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PAGE_LAST:
        t = jj_consume_token(PAGE_LAST);
        break;
      case PAGE_LASTITEM:
        t = jj_consume_token(PAGE_LASTITEM);
        break;
      default:
        jj_la1[24] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                pageTagExport.setLastItem(t.image);
      break;
    case IDENTIFIER:
      t = jj_consume_token(IDENTIFIER);
      jj_consume_token(EQUALS);
      pageVar(pageTagExport, t);
      break;
    default:
      jj_la1[25] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void pageVar(PageTagExport pageTagExport, Token id) throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case PAGE_PAGEURL:
    case PAGE_URL:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PAGE_URL:
        jj_consume_token(PAGE_URL);
        break;
      case PAGE_PAGEURL:
        jj_consume_token(PAGE_PAGEURL);
        break;
      default:
        jj_la1[26] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                pageTagExport.setPageUrl(id.image);
      break;
    case PAGE_NUMBER:
    case PAGE_PAGE:
    case PAGE_PAGENUMBER:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PAGE_PAGE:
        jj_consume_token(PAGE_PAGE);
        break;
      case PAGE_NUMBER:
        jj_consume_token(PAGE_NUMBER);
        break;
      case PAGE_PAGENUMBER:
        jj_consume_token(PAGE_PAGENUMBER);
        break;
      default:
        jj_la1[27] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                pageTagExport.setPageNumber(id.image);
      break;
    case PAGE_FIRST:
    case PAGE_FIRSTITEM:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PAGE_FIRST:
        jj_consume_token(PAGE_FIRST);
        break;
      case PAGE_FIRSTITEM:
        jj_consume_token(PAGE_FIRSTITEM);
        break;
      default:
        jj_la1[28] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                pageTagExport.setFirstItem(id.image);
      break;
    case PAGE_LAST:
    case PAGE_LASTITEM:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case PAGE_LAST:
        jj_consume_token(PAGE_LAST);
        break;
      case PAGE_LASTITEM:
        jj_consume_token(PAGE_LASTITEM);
        break;
      default:
        jj_la1[29] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
                pageTagExport.setLastItem(id.image);
      break;
    default:
      jj_la1[30] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  public TagExportParserTokenManager token_source;
  JavaCharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[31];
  final private int[] jj_la1_0 = {0x8003e00,0x80,0x100,0x2400,0x1a00,0x8003e00,0x2400,0x1a00,0x3e00,0x803c000,0x80,0x100,0xc000,0x30000,0x803c000,0xc000,0x30000,0x3c000,0xffc0000,0x80,0x100,0x6000000,0x1c00000,0xc0000,0x300000,0xffc0000,0x6000000,0x1c00000,0xc0000,0x300000,0x7fc0000,};

  public TagExportParser(java.io.InputStream stream) {
    jj_input_stream = new JavaCharStream(stream, 1, 1);
    token_source = new TagExportParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 31; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.InputStream stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 31; i++) jj_la1[i] = -1;
  }

  public TagExportParser(java.io.Reader stream) {
    jj_input_stream = new JavaCharStream(stream, 1, 1);
    token_source = new TagExportParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 31; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 31; i++) jj_la1[i] = -1;
  }

  public TagExportParser(TagExportParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 31; i++) jj_la1[i] = -1;
  }

  public void ReInit(TagExportParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 31; i++) jj_la1[i] = -1;
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.Vector jj_expentries = new java.util.Vector();
  private int[] jj_expentry;
  private int jj_kind = -1;

  final public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[30];
    for (int i = 0; i < 30; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 31; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 30; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  final public void enable_tracing() {
  }

  final public void disable_tracing() {
  }

}
