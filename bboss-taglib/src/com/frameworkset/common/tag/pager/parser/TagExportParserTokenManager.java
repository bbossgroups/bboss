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

public class TagExportParserTokenManager implements TagExportParserConstants,Serializable
{
  public  java.io.PrintStream debugStream = System.out;
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private final int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   return pos + 1;
}
private final int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
private final int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 44:
         return jjStopAtPos(0, 7);
      case 59:
         return jjStopAtPos(0, 8);
      case 61:
         return jjStopAtPos(0, 6);
      default :
         return jjMoveNfa_0(0, 0);
   }
}
private final void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private final void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private final void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}
private final void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}
private final void jjCheckNAddStates(int start)
{
   jjCheckNAdd(jjnextStates[start]);
   jjCheckNAdd(jjnextStates[start + 1]);
}
static final long[] jjbitVec0 = {
   0x1ff00000fffffffeL, 0xffffffffffffc000L, 0xffffffffL, 0x600000000000000L
};
static final long[] jjbitVec2 = {
   0x0L, 0x0L, 0x0L, 0xff7fffffff7fffffL
};
static final long[] jjbitVec3 = {
   0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffffffffffffffL
};
static final long[] jjbitVec4 = {
   0xffffffffffffffffL, 0xffffffffffffffffL, 0xffffL, 0x0L
};
static final long[] jjbitVec5 = {
   0xffffffffffffffffL, 0xffffffffffffffffL, 0x0L, 0x0L
};
static final long[] jjbitVec6 = {
   0x3fffffffffffL, 0x0L, 0x0L, 0x0L
};
private final int jjMoveNfa_0(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 2;
   int i = 1;
   jjstateSet[0] = startState;
   int j, kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if (curChar != 36)
                     break;
                  if (kind > 27)
                     kind = 27;
                  jjCheckNAdd(1);
                  break;
               case 1:
                  if ((0x3ff001000000000L & l) == 0L)
                     break;
                  if (kind > 27)
                     kind = 27;
                  jjCheckNAdd(1);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
               case 1:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 27)
                     kind = 27;
                  jjCheckNAdd(1);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int hiByte = (int)(curChar >> 8);
         int i1 = hiByte >> 6;
         long l1 = 1L << (hiByte & 077);
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
               case 1:
                  if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 27)
                     kind = 27;
                  jjCheckNAdd(1);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 2 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
private final int jjStopStringLiteralDfa_2(int pos, long active0)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0x3c000L) != 0L)
         {
            jjmatchedKind = 27;
            return 1;
         }
         return -1;
      case 1:
         if ((active0 & 0x3c000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 1;
            return 1;
         }
         return -1;
      case 2:
         if ((active0 & 0x3c000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 2;
            return 1;
         }
         return -1;
      case 3:
         if ((active0 & 0x3c000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 3;
            return 1;
         }
         return -1;
      case 4:
         if ((active0 & 0x28000L) != 0L)
            return 1;
         if ((active0 & 0x14000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 4;
            return 1;
         }
         return -1;
      case 5:
         if ((active0 & 0x14000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 5;
            return 1;
         }
         return -1;
      case 6:
         if ((active0 & 0x14000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 6;
            return 1;
         }
         return -1;
      case 7:
         if ((active0 & 0x14000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 7;
            return 1;
         }
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_2(int pos, long active0)
{
   return jjMoveNfa_2(jjStopStringLiteralDfa_2(pos, active0), pos + 1);
}
private final int jjStartNfaWithStates_2(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_2(state, pos + 1);
}
private final int jjMoveStringLiteralDfa0_2()
{
   switch(curChar)
   {
      case 44:
         return jjStopAtPos(0, 7);
      case 59:
         return jjStopAtPos(0, 8);
      case 61:
         return jjStopAtPos(0, 6);
      case 105:
         return jjMoveStringLiteralDfa1_2(0xc000L);
      case 112:
         return jjMoveStringLiteralDfa1_2(0x30000L);
      default :
         return jjMoveNfa_2(0, 0);
   }
}
private final int jjMoveStringLiteralDfa1_2(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_2(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa2_2(active0, 0x30000L);
      case 116:
         return jjMoveStringLiteralDfa2_2(active0, 0xc000L);
      default :
         break;
   }
   return jjStartNfa_2(0, active0);
}
private final int jjMoveStringLiteralDfa2_2(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_2(0, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_2(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 101:
         return jjMoveStringLiteralDfa3_2(active0, 0xc000L);
      case 103:
         return jjMoveStringLiteralDfa3_2(active0, 0x30000L);
      default :
         break;
   }
   return jjStartNfa_2(1, active0);
}
private final int jjMoveStringLiteralDfa3_2(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_2(1, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_2(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 101:
         return jjMoveStringLiteralDfa4_2(active0, 0x30000L);
      case 109:
         return jjMoveStringLiteralDfa4_2(active0, 0xc000L);
      default :
         break;
   }
   return jjStartNfa_2(2, active0);
}
private final int jjMoveStringLiteralDfa4_2(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_2(2, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_2(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 67:
         return jjMoveStringLiteralDfa5_2(active0, 0x14000L);
      case 115:
         if ((active0 & 0x8000L) != 0L)
            return jjStartNfaWithStates_2(4, 15, 1);
         else if ((active0 & 0x20000L) != 0L)
            return jjStartNfaWithStates_2(4, 17, 1);
         break;
      default :
         break;
   }
   return jjStartNfa_2(3, active0);
}
private final int jjMoveStringLiteralDfa5_2(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_2(3, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_2(4, active0);
      return 5;
   }
   switch(curChar)
   {
      case 111:
         return jjMoveStringLiteralDfa6_2(active0, 0x14000L);
      default :
         break;
   }
   return jjStartNfa_2(4, active0);
}
private final int jjMoveStringLiteralDfa6_2(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_2(4, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_2(5, active0);
      return 6;
   }
   switch(curChar)
   {
      case 117:
         return jjMoveStringLiteralDfa7_2(active0, 0x14000L);
      default :
         break;
   }
   return jjStartNfa_2(5, active0);
}
private final int jjMoveStringLiteralDfa7_2(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_2(5, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_2(6, active0);
      return 7;
   }
   switch(curChar)
   {
      case 110:
         return jjMoveStringLiteralDfa8_2(active0, 0x14000L);
      default :
         break;
   }
   return jjStartNfa_2(6, active0);
}
private final int jjMoveStringLiteralDfa8_2(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_2(6, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_2(7, active0);
      return 8;
   }
   switch(curChar)
   {
      case 116:
         if ((active0 & 0x4000L) != 0L)
            return jjStartNfaWithStates_2(8, 14, 1);
         else if ((active0 & 0x10000L) != 0L)
            return jjStartNfaWithStates_2(8, 16, 1);
         break;
      default :
         break;
   }
   return jjStartNfa_2(7, active0);
}
private final int jjMoveNfa_2(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 2;
   int i = 1;
   jjstateSet[0] = startState;
   int j, kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if (curChar != 36)
                     break;
                  if (kind > 27)
                     kind = 27;
                  jjCheckNAdd(1);
                  break;
               case 1:
                  if ((0x3ff001000000000L & l) == 0L)
                     break;
                  if (kind > 27)
                     kind = 27;
                  jjCheckNAdd(1);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
               case 1:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 27)
                     kind = 27;
                  jjCheckNAdd(1);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int hiByte = (int)(curChar >> 8);
         int i1 = hiByte >> 6;
         long l1 = 1L << (hiByte & 077);
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
               case 1:
                  if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 27)
                     kind = 27;
                  jjCheckNAdd(1);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 2 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
private final int jjStopStringLiteralDfa_3(int pos, long active0)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0x7fc0000L) != 0L)
         {
            jjmatchedKind = 27;
            return 1;
         }
         return -1;
      case 1:
         if ((active0 & 0x7fc0000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 1;
            return 1;
         }
         return -1;
      case 2:
         if ((active0 & 0x4000000L) != 0L)
            return 1;
         if ((active0 & 0x3fc0000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 2;
            return 1;
         }
         return -1;
      case 3:
         if ((active0 & 0x3b00000L) != 0L)
            return 1;
         if ((active0 & 0x4c0000L) != 0L)
         {
            if (jjmatchedPos != 3)
            {
               jjmatchedKind = 27;
               jjmatchedPos = 3;
            }
            return 1;
         }
         return -1;
      case 4:
         if ((active0 & 0xc0000L) != 0L)
            return 1;
         if ((active0 & 0x3600000L) != 0L)
         {
            if (jjmatchedPos != 4)
            {
               jjmatchedKind = 27;
               jjmatchedPos = 4;
            }
            return 1;
         }
         return -1;
      case 5:
         if ((active0 & 0x400000L) != 0L)
            return 1;
         if ((active0 & 0x3280000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 5;
            return 1;
         }
         return -1;
      case 6:
         if ((active0 & 0x2000000L) != 0L)
            return 1;
         if ((active0 & 0x1280000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 6;
            return 1;
         }
         return -1;
      case 7:
         if ((active0 & 0x200000L) != 0L)
            return 1;
         if ((active0 & 0x1080000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 7;
            return 1;
         }
         return -1;
      case 8:
         if ((active0 & 0x1000000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 8;
            return 1;
         }
         if ((active0 & 0x80000L) != 0L)
            return 1;
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_3(int pos, long active0)
{
   return jjMoveNfa_3(jjStopStringLiteralDfa_3(pos, active0), pos + 1);
}
private final int jjStartNfaWithStates_3(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_3(state, pos + 1);
}
private final int jjMoveStringLiteralDfa0_3()
{
   switch(curChar)
   {
      case 44:
         return jjStopAtPos(0, 7);
      case 59:
         return jjStopAtPos(0, 8);
      case 61:
         return jjStopAtPos(0, 6);
      case 102:
         return jjMoveStringLiteralDfa1_3(0xc0000L);
      case 108:
         return jjMoveStringLiteralDfa1_3(0x300000L);
      case 110:
         return jjMoveStringLiteralDfa1_3(0x400000L);
      case 112:
         return jjMoveStringLiteralDfa1_3(0x3800000L);
      case 117:
         return jjMoveStringLiteralDfa1_3(0x4000000L);
      default :
         return jjMoveNfa_3(0, 0);
   }
}
private final int jjMoveStringLiteralDfa1_3(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_3(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa2_3(active0, 0x3b00000L);
      case 105:
         return jjMoveStringLiteralDfa2_3(active0, 0xc0000L);
      case 114:
         return jjMoveStringLiteralDfa2_3(active0, 0x4000000L);
      case 117:
         return jjMoveStringLiteralDfa2_3(active0, 0x400000L);
      default :
         break;
   }
   return jjStartNfa_3(0, active0);
}
private final int jjMoveStringLiteralDfa2_3(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_3(0, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_3(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 103:
         return jjMoveStringLiteralDfa3_3(active0, 0x3800000L);
      case 108:
         if ((active0 & 0x4000000L) != 0L)
            return jjStartNfaWithStates_3(2, 26, 1);
         break;
      case 109:
         return jjMoveStringLiteralDfa3_3(active0, 0x400000L);
      case 114:
         return jjMoveStringLiteralDfa3_3(active0, 0xc0000L);
      case 115:
         return jjMoveStringLiteralDfa3_3(active0, 0x300000L);
      default :
         break;
   }
   return jjStartNfa_3(1, active0);
}
private final int jjMoveStringLiteralDfa3_3(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_3(1, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_3(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 98:
         return jjMoveStringLiteralDfa4_3(active0, 0x400000L);
      case 101:
         if ((active0 & 0x800000L) != 0L)
         {
            jjmatchedKind = 23;
            jjmatchedPos = 3;
         }
         return jjMoveStringLiteralDfa4_3(active0, 0x3000000L);
      case 115:
         return jjMoveStringLiteralDfa4_3(active0, 0xc0000L);
      case 116:
         if ((active0 & 0x100000L) != 0L)
         {
            jjmatchedKind = 20;
            jjmatchedPos = 3;
         }
         return jjMoveStringLiteralDfa4_3(active0, 0x200000L);
      default :
         break;
   }
   return jjStartNfa_3(2, active0);
}
private final int jjMoveStringLiteralDfa4_3(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_3(2, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_3(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 73:
         return jjMoveStringLiteralDfa5_3(active0, 0x200000L);
      case 78:
         return jjMoveStringLiteralDfa5_3(active0, 0x1000000L);
      case 85:
         return jjMoveStringLiteralDfa5_3(active0, 0x2000000L);
      case 101:
         return jjMoveStringLiteralDfa5_3(active0, 0x400000L);
      case 116:
         if ((active0 & 0x40000L) != 0L)
         {
            jjmatchedKind = 18;
            jjmatchedPos = 4;
         }
         return jjMoveStringLiteralDfa5_3(active0, 0x80000L);
      default :
         break;
   }
   return jjStartNfa_3(3, active0);
}
private final int jjMoveStringLiteralDfa5_3(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_3(3, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_3(4, active0);
      return 5;
   }
   switch(curChar)
   {
      case 73:
         return jjMoveStringLiteralDfa6_3(active0, 0x80000L);
      case 114:
         if ((active0 & 0x400000L) != 0L)
            return jjStartNfaWithStates_3(5, 22, 1);
         return jjMoveStringLiteralDfa6_3(active0, 0x2000000L);
      case 116:
         return jjMoveStringLiteralDfa6_3(active0, 0x200000L);
      case 117:
         return jjMoveStringLiteralDfa6_3(active0, 0x1000000L);
      default :
         break;
   }
   return jjStartNfa_3(4, active0);
}
private final int jjMoveStringLiteralDfa6_3(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_3(4, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_3(5, active0);
      return 6;
   }
   switch(curChar)
   {
      case 101:
         return jjMoveStringLiteralDfa7_3(active0, 0x200000L);
      case 108:
         if ((active0 & 0x2000000L) != 0L)
            return jjStartNfaWithStates_3(6, 25, 1);
         break;
      case 109:
         return jjMoveStringLiteralDfa7_3(active0, 0x1000000L);
      case 116:
         return jjMoveStringLiteralDfa7_3(active0, 0x80000L);
      default :
         break;
   }
   return jjStartNfa_3(5, active0);
}
private final int jjMoveStringLiteralDfa7_3(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_3(5, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_3(6, active0);
      return 7;
   }
   switch(curChar)
   {
      case 98:
         return jjMoveStringLiteralDfa8_3(active0, 0x1000000L);
      case 101:
         return jjMoveStringLiteralDfa8_3(active0, 0x80000L);
      case 109:
         if ((active0 & 0x200000L) != 0L)
            return jjStartNfaWithStates_3(7, 21, 1);
         break;
      default :
         break;
   }
   return jjStartNfa_3(6, active0);
}
private final int jjMoveStringLiteralDfa8_3(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_3(6, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_3(7, active0);
      return 8;
   }
   switch(curChar)
   {
      case 101:
         return jjMoveStringLiteralDfa9_3(active0, 0x1000000L);
      case 109:
         if ((active0 & 0x80000L) != 0L)
            return jjStartNfaWithStates_3(8, 19, 1);
         break;
      default :
         break;
   }
   return jjStartNfa_3(7, active0);
}
private final int jjMoveStringLiteralDfa9_3(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_3(7, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_3(8, active0);
      return 9;
   }
   switch(curChar)
   {
      case 114:
         if ((active0 & 0x1000000L) != 0L)
            return jjStartNfaWithStates_3(9, 24, 1);
         break;
      default :
         break;
   }
   return jjStartNfa_3(8, active0);
}
private final int jjMoveNfa_3(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 2;
   int i = 1;
   jjstateSet[0] = startState;
   int j, kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if (curChar != 36)
                     break;
                  if (kind > 27)
                     kind = 27;
                  jjCheckNAdd(1);
                  break;
               case 1:
                  if ((0x3ff001000000000L & l) == 0L)
                     break;
                  if (kind > 27)
                     kind = 27;
                  jjCheckNAdd(1);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
               case 1:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 27)
                     kind = 27;
                  jjCheckNAdd(1);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int hiByte = (int)(curChar >> 8);
         int i1 = hiByte >> 6;
         long l1 = 1L << (hiByte & 077);
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
               case 1:
                  if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 27)
                     kind = 27;
                  jjCheckNAdd(1);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 2 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
private final int jjStopStringLiteralDfa_1(int pos, long active0)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0x3e00L) != 0L)
         {
            jjmatchedKind = 27;
            return 1;
         }
         return -1;
      case 1:
         if ((active0 & 0x3e00L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 1;
            return 1;
         }
         return -1;
      case 2:
         if ((active0 & 0x3e00L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 2;
            return 1;
         }
         return -1;
      case 3:
         if ((active0 & 0x3800L) != 0L)
            return 1;
         if ((active0 & 0x600L) != 0L)
         {
            if (jjmatchedPos != 3)
            {
               jjmatchedKind = 27;
               jjmatchedPos = 3;
            }
            return 1;
         }
         return -1;
      case 4:
         if ((active0 & 0x3600L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 4;
            return 1;
         }
         return -1;
      case 5:
         if ((active0 & 0x600L) != 0L)
            return 1;
         if ((active0 & 0x3000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 5;
            return 1;
         }
         return -1;
      case 6:
         if ((active0 & 0x3000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 6;
            return 1;
         }
         return -1;
      case 7:
         if ((active0 & 0x3000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 7;
            return 1;
         }
         return -1;
      case 8:
         if ((active0 & 0x3000L) != 0L)
         {
            jjmatchedKind = 27;
            jjmatchedPos = 8;
            return 1;
         }
         return -1;
      default :
         return -1;
   }
}
private final int jjStartNfa_1(int pos, long active0)
{
   return jjMoveNfa_1(jjStopStringLiteralDfa_1(pos, active0), pos + 1);
}
private final int jjStartNfaWithStates_1(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_1(state, pos + 1);
}
private final int jjMoveStringLiteralDfa0_1()
{
   switch(curChar)
   {
      case 44:
         return jjStopAtPos(0, 7);
      case 59:
         return jjStopAtPos(0, 8);
      case 61:
         return jjStopAtPos(0, 6);
      case 110:
         return jjMoveStringLiteralDfa1_1(0x200L);
      case 111:
         return jjMoveStringLiteralDfa1_1(0x400L);
      case 112:
         return jjMoveStringLiteralDfa1_1(0x3800L);
      default :
         return jjMoveNfa_1(0, 0);
   }
}
private final int jjMoveStringLiteralDfa1_1(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_1(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa2_1(active0, 0x3800L);
      case 102:
         return jjMoveStringLiteralDfa2_1(active0, 0x400L);
      case 117:
         return jjMoveStringLiteralDfa2_1(active0, 0x200L);
      default :
         break;
   }
   return jjStartNfa_1(0, active0);
}
private final int jjMoveStringLiteralDfa2_1(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_1(0, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_1(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 102:
         return jjMoveStringLiteralDfa3_1(active0, 0x400L);
      case 103:
         return jjMoveStringLiteralDfa3_1(active0, 0x3800L);
      case 109:
         return jjMoveStringLiteralDfa3_1(active0, 0x200L);
      default :
         break;
   }
   return jjStartNfa_1(1, active0);
}
private final int jjMoveStringLiteralDfa3_1(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_1(1, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_1(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 98:
         return jjMoveStringLiteralDfa4_1(active0, 0x200L);
      case 101:
         if ((active0 & 0x800L) != 0L)
         {
            jjmatchedKind = 11;
            jjmatchedPos = 3;
         }
         return jjMoveStringLiteralDfa4_1(active0, 0x3000L);
      case 115:
         return jjMoveStringLiteralDfa4_1(active0, 0x400L);
      default :
         break;
   }
   return jjStartNfa_1(2, active0);
}
private final int jjMoveStringLiteralDfa4_1(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_1(2, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_1(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 78:
         return jjMoveStringLiteralDfa5_1(active0, 0x1000L);
      case 79:
         return jjMoveStringLiteralDfa5_1(active0, 0x2000L);
      case 101:
         return jjMoveStringLiteralDfa5_1(active0, 0x600L);
      default :
         break;
   }
   return jjStartNfa_1(3, active0);
}
private final int jjMoveStringLiteralDfa5_1(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_1(3, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_1(4, active0);
      return 5;
   }
   switch(curChar)
   {
      case 102:
         return jjMoveStringLiteralDfa6_1(active0, 0x2000L);
      case 114:
         if ((active0 & 0x200L) != 0L)
            return jjStartNfaWithStates_1(5, 9, 1);
         break;
      case 116:
         if ((active0 & 0x400L) != 0L)
            return jjStartNfaWithStates_1(5, 10, 1);
         break;
      case 117:
         return jjMoveStringLiteralDfa6_1(active0, 0x1000L);
      default :
         break;
   }
   return jjStartNfa_1(4, active0);
}
private final int jjMoveStringLiteralDfa6_1(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_1(4, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_1(5, active0);
      return 6;
   }
   switch(curChar)
   {
      case 102:
         return jjMoveStringLiteralDfa7_1(active0, 0x2000L);
      case 109:
         return jjMoveStringLiteralDfa7_1(active0, 0x1000L);
      default :
         break;
   }
   return jjStartNfa_1(5, active0);
}
private final int jjMoveStringLiteralDfa7_1(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_1(5, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_1(6, active0);
      return 7;
   }
   switch(curChar)
   {
      case 98:
         return jjMoveStringLiteralDfa8_1(active0, 0x1000L);
      case 115:
         return jjMoveStringLiteralDfa8_1(active0, 0x2000L);
      default :
         break;
   }
   return jjStartNfa_1(6, active0);
}
private final int jjMoveStringLiteralDfa8_1(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_1(6, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_1(7, active0);
      return 8;
   }
   switch(curChar)
   {
      case 101:
         return jjMoveStringLiteralDfa9_1(active0, 0x3000L);
      default :
         break;
   }
   return jjStartNfa_1(7, active0);
}
private final int jjMoveStringLiteralDfa9_1(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_1(7, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_1(8, active0);
      return 9;
   }
   switch(curChar)
   {
      case 114:
         if ((active0 & 0x1000L) != 0L)
            return jjStartNfaWithStates_1(9, 12, 1);
         break;
      case 116:
         if ((active0 & 0x2000L) != 0L)
            return jjStartNfaWithStates_1(9, 13, 1);
         break;
      default :
         break;
   }
   return jjStartNfa_1(8, active0);
}
private final int jjMoveNfa_1(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 2;
   int i = 1;
   jjstateSet[0] = startState;
   int j, kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if (curChar != 36)
                     break;
                  if (kind > 27)
                     kind = 27;
                  jjCheckNAdd(1);
                  break;
               case 1:
                  if ((0x3ff001000000000L & l) == 0L)
                     break;
                  if (kind > 27)
                     kind = 27;
                  jjCheckNAdd(1);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
               case 1:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 27)
                     kind = 27;
                  jjCheckNAdd(1);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int hiByte = (int)(curChar >> 8);
         int i1 = hiByte >> 6;
         long l1 = 1L << (hiByte & 077);
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
               case 1:
                  if (!jjCanMove_0(hiByte, i1, i2, l1, l2))
                     break;
                  if (kind > 27)
                     kind = 27;
                  jjCheckNAdd(1);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if ((i = jjnewStateCnt) == (startsAt = 2 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
};
private static final boolean jjCanMove_0(int hiByte, int i1, int i2, long l1, long l2)
{
   switch(hiByte)
   {
      case 0:
         return ((jjbitVec2[i2] & l2) != 0L);
      case 48:
         return ((jjbitVec3[i2] & l2) != 0L);
      case 49:
         return ((jjbitVec4[i2] & l2) != 0L);
      case 51:
         return ((jjbitVec5[i2] & l2) != 0L);
      case 61:
         return ((jjbitVec6[i2] & l2) != 0L);
      default : 
         if ((jjbitVec0[i1] & l1) != 0L)
            return true;
         return false;
   }
}
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, null, "\75", "\54", "\73", 
"\156\165\155\142\145\162", "\157\146\146\163\145\164", "\160\141\147\145", 
"\160\141\147\145\116\165\155\142\145\162", "\160\141\147\145\117\146\146\163\145\164", 
"\151\164\145\155\103\157\165\156\164", "\151\164\145\155\163", "\160\141\147\145\103\157\165\156\164", 
"\160\141\147\145\163", "\146\151\162\163\164", "\146\151\162\163\164\111\164\145\155", 
"\154\141\163\164", "\154\141\163\164\111\164\145\155", "\156\165\155\142\145\162", 
"\160\141\147\145", "\160\141\147\145\116\165\155\142\145\162", "\160\141\147\145\125\162\154", 
"\165\162\154", null, null, null, };
public static final String[] lexStateNames = {
   "DEFAULT", 
   "PAGER_STATE", 
   "INDEX_STATE", 
   "PAGE_STATE", 
};
public static final int[] jjnewLexState = {
   -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 
   -1, -1, -1, -1, -1, 
};
static final long[] jjtoToken = {
   0xfffffc1L, 
};
static final long[] jjtoSkip = {
   0x3eL, 
};
private JavaCharStream input_stream;
private final int[] jjrounds = new int[2];
private final int[] jjstateSet = new int[4];
protected char curChar;
public TagExportParserTokenManager(JavaCharStream stream)
{
   if (JavaCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}
public TagExportParserTokenManager(JavaCharStream stream, int lexState)
{
   this(stream);
   SwitchTo(lexState);
}
public void ReInit(JavaCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private final void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 2; i-- > 0;)
      jjrounds[i] = 0x80000000;
}
public void ReInit(JavaCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}
public void SwitchTo(int lexState)
{
   if (lexState >= 4 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

private final Token jjFillToken()
{
   Token t = Token.newToken(jjmatchedKind);
   t.kind = jjmatchedKind;
   String im = jjstrLiteralImages[jjmatchedKind];
   t.image = (im == null) ? input_stream.GetImage() : im;
   t.beginLine = input_stream.getBeginLine();
   t.beginColumn = input_stream.getBeginColumn();
   t.endLine = input_stream.getEndLine();
   t.endColumn = input_stream.getEndColumn();
   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

public final Token getNextToken() 
{
  int kind;
  Token specialToken = null;
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {   
   try   
   {     
      curChar = input_stream.BeginToken();
   }     
   catch(java.io.IOException e)
   {        
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   switch(curLexState)
   {
     case 0:
       try { input_stream.backup(0);
          while (curChar <= 32 && (0x100003600L & (1L << curChar)) != 0L)
             curChar = input_stream.BeginToken();
       }
       catch (java.io.IOException e1) { continue EOFLoop; }
       jjmatchedKind = 0x7fffffff;
       jjmatchedPos = 0;
       curPos = jjMoveStringLiteralDfa0_0();
       break;
     case 1:
       try { input_stream.backup(0);
          while (curChar <= 32 && (0x100003600L & (1L << curChar)) != 0L)
             curChar = input_stream.BeginToken();
       }
       catch (java.io.IOException e1) { continue EOFLoop; }
       jjmatchedKind = 0x7fffffff;
       jjmatchedPos = 0;
       curPos = jjMoveStringLiteralDfa0_1();
       break;
     case 2:
       try { input_stream.backup(0);
          while (curChar <= 32 && (0x100003600L & (1L << curChar)) != 0L)
             curChar = input_stream.BeginToken();
       }
       catch (java.io.IOException e1) { continue EOFLoop; }
       jjmatchedKind = 0x7fffffff;
       jjmatchedPos = 0;
       curPos = jjMoveStringLiteralDfa0_2();
       break;
     case 3:
       try { input_stream.backup(0);
          while (curChar <= 32 && (0x100003600L & (1L << curChar)) != 0L)
             curChar = input_stream.BeginToken();
       }
       catch (java.io.IOException e1) { continue EOFLoop; }
       jjmatchedKind = 0x7fffffff;
       jjmatchedPos = 0;
       curPos = jjMoveStringLiteralDfa0_3();
       break;
   }
     if (jjmatchedKind != 0x7fffffff)
     {
        if (jjmatchedPos + 1 < curPos)
           input_stream.backup(curPos - jjmatchedPos - 1);
        if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
        {
           matchedToken = jjFillToken();
       if (jjnewLexState[jjmatchedKind] != -1)
         curLexState = jjnewLexState[jjmatchedKind];
           return matchedToken;
        }
        else
        {
         if (jjnewLexState[jjmatchedKind] != -1)
           curLexState = jjnewLexState[jjmatchedKind];
           continue EOFLoop;
        }
     }
     int error_line = input_stream.getEndLine();
     int error_column = input_stream.getEndColumn();
     String error_after = null;
     boolean EOFSeen = false;
     try { input_stream.readChar(); input_stream.backup(1); }
     catch (java.io.IOException e1) {
        EOFSeen = true;
        error_after = curPos <= 1 ? "" : input_stream.GetImage();
        if (curChar == '\n' || curChar == '\r') {
           error_line++;
           error_column = 0;
        }
        else
           error_column++;
     }
     if (!EOFSeen) {
        input_stream.backup(1);
        error_after = curPos <= 1 ? "" : input_stream.GetImage();
     }
     throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

}
