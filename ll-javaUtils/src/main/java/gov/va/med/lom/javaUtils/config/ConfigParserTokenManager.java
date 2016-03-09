/* Generated By:JavaCC: Do not edit this line. ConfigParserTokenManager.java */

package gov.va.med.lom.javaUtils.config;

public class ConfigParserTokenManager implements ConfigParserConstants
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
      case 91:
         return jjStopAtPos(0, 1);
      case 93:
         return jjStopAtPos(0, 2);
      default :
         return jjMoveNfa_0(2, 0);
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
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private final int jjMoveNfa_0(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 11;
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
               case 2:
                  if ((0x1ffffdbffL & l) != 0L)
                  {
                     if (kind > 4)
                        kind = 4;
                     jjCheckNAdd(1);
                  }
                  else if ((0xdffffff600000000L & l) != 0L)
                  {
                     if (kind > 3)
                        kind = 3;
                     jjCheckNAdd(0);
                  }
                  else if ((0x2400L & l) != 0L)
                  {
                     if (kind > 7)
                        kind = 7;
                  }
                  else if (curChar == 61)
                  {
                     if (kind > 14)
                        kind = 14;
                  }
                  else if (curChar == 35)
                     jjCheckNAddStates(0, 2);
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 8;
                  break;
               case 0:
                  if ((0xdffffff600000000L & l) == 0L)
                     break;
                  if (kind > 3)
                     kind = 3;
                  jjCheckNAdd(0);
                  break;
               case 1:
                  if ((0x1ffffdbffL & l) == 0L)
                     break;
                  if (kind > 4)
                     kind = 4;
                  jjCheckNAdd(1);
                  break;
               case 3:
                  if ((0xffffffffffffdbffL & l) != 0L)
                     jjCheckNAddStates(0, 2);
                  break;
               case 4:
                  if ((0x2400L & l) != 0L && kind > 5)
                     kind = 5;
                  break;
               case 5:
                  if (curChar == 10 && kind > 5)
                     kind = 5;
                  break;
               case 6:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 5;
                  break;
               case 7:
                  if ((0x2400L & l) != 0L && kind > 7)
                     kind = 7;
                  break;
               case 8:
                  if (curChar == 10 && kind > 7)
                     kind = 7;
                  break;
               case 9:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 8;
                  break;
               case 10:
                  if (curChar == 61 && kind > 14)
                     kind = 14;
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
               case 2:
                  if ((0x7fffffffd7ffffffL & l) != 0L)
                  {
                     if (kind > 3)
                        kind = 3;
                     jjCheckNAdd(0);
                  }
                  else if (curChar == 127)
                  {
                     if (kind > 4)
                        kind = 4;
                     jjCheckNAdd(1);
                  }
                  break;
               case 0:
                  if ((0x7fffffffd7ffffffL & l) == 0L)
                     break;
                  if (kind > 3)
                     kind = 3;
                  jjCheckNAdd(0);
                  break;
               case 1:
                  if (curChar != 127)
                     break;
                  if (kind > 4)
                     kind = 4;
                  jjCheckNAdd(1);
                  break;
               case 3:
                  jjAddStates(0, 2);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 2:
               case 0:
                  if ((jjbitVec0[i2] & l2) == 0L)
                     break;
                  if (kind > 3)
                     kind = 3;
                  jjCheckNAdd(0);
                  break;
               case 3:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjAddStates(0, 2);
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
      if ((i = jjnewStateCnt) == (startsAt = 11 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
private final int jjMoveStringLiteralDfa0_1()
{
   return jjMoveNfa_1(1, 0);
}
private final int jjMoveNfa_1(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 46;
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
               case 1:
                  if ((0x1ffffdbffL & l) != 0L)
                  {
                     if (kind > 4)
                        kind = 4;
                     jjCheckNAdd(0);
                  }
                  else if ((0xffffe7fa00000000L & l) != 0L)
                  {
                     if (kind > 9)
                        kind = 9;
                     jjCheckNAdd(35);
                  }
                  else if ((0x2400L & l) != 0L)
                  {
                     if (kind > 7)
                        kind = 7;
                  }
                  else if (curChar == 44)
                  {
                     if (kind > 12)
                        kind = 12;
                     jjCheckNAddStates(3, 5);
                  }
                  else if (curChar == 43)
                  {
                     if (kind > 11)
                        kind = 11;
                     jjCheckNAddStates(6, 8);
                  }
                  else if (curChar == 34)
                     jjCheckNAddStates(9, 14);
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 7;
                  else if (curChar == 35)
                     jjCheckNAddStates(15, 17);
                  break;
               case 0:
                  if ((0x1ffffdbffL & l) == 0L)
                     break;
                  if (kind > 4)
                     kind = 4;
                  jjCheckNAdd(0);
                  break;
               case 2:
                  if ((0xffffffffffffdbffL & l) != 0L)
                     jjCheckNAddStates(15, 17);
                  break;
               case 3:
                  if ((0x2400L & l) != 0L && kind > 5)
                     kind = 5;
                  break;
               case 4:
                  if (curChar == 10 && kind > 5)
                     kind = 5;
                  break;
               case 5:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 4;
                  break;
               case 6:
                  if ((0x2400L & l) != 0L && kind > 7)
                     kind = 7;
                  break;
               case 7:
                  if (curChar == 10 && kind > 7)
                     kind = 7;
                  break;
               case 8:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 7;
                  break;
               case 9:
                  if (curChar == 34)
                     jjCheckNAddStates(9, 14);
                  break;
               case 10:
                  if (curChar == 43)
                     jjCheckNAddStates(18, 26);
                  break;
               case 11:
                  if ((0x1ffffdbffL & l) != 0L)
                     jjCheckNAddStates(18, 26);
                  break;
               case 12:
                  if ((0x2400L & l) != 0L)
                     jjCheckNAddStates(9, 14);
                  break;
               case 13:
                  if (curChar == 44)
                     jjCheckNAddStates(27, 35);
                  break;
               case 14:
                  if ((0x1ffffdbffL & l) != 0L)
                     jjCheckNAddStates(27, 35);
                  break;
               case 15:
                  if (curChar == 10)
                     jjCheckNAddStates(9, 14);
                  break;
               case 16:
                  if ((0xffffe7fbffffdbffL & l) != 0L)
                     jjCheckNAddStates(9, 14);
                  break;
               case 17:
                  if (curChar == 34 && kind > 8)
                     kind = 8;
                  break;
               case 19:
                  jjCheckNAddStates(9, 14);
                  break;
               case 22:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 23;
                  break;
               case 23:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 24;
                  break;
               case 24:
               case 27:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAdd(25);
                  break;
               case 25:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddStates(9, 14);
                  break;
               case 28:
               case 33:
               case 34:
                  if (curChar == 13)
                     jjCheckNAdd(15);
                  break;
               case 29:
                  if (curChar == 13)
                     jjCheckNAddStates(9, 14);
                  break;
               case 30:
                  if ((0xf000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 31;
                  break;
               case 31:
                  if ((0xff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 32;
                  break;
               case 32:
                  if ((0xff000000000000L & l) != 0L)
                     jjCheckNAddStates(9, 14);
                  break;
               case 35:
                  if ((0xffffe7fa00000000L & l) == 0L)
                     break;
                  if (kind > 9)
                     kind = 9;
                  jjCheckNAdd(35);
                  break;
               case 36:
                  if (curChar != 43)
                     break;
                  if (kind > 11)
                     kind = 11;
                  jjCheckNAddStates(6, 8);
                  break;
               case 37:
                  if ((0x1ffffdbffL & l) == 0L)
                     break;
                  if (kind > 11)
                     kind = 11;
                  jjCheckNAddStates(6, 8);
                  break;
               case 38:
                  if ((0x2400L & l) != 0L && kind > 11)
                     kind = 11;
                  break;
               case 39:
                  if (curChar == 10 && kind > 11)
                     kind = 11;
                  break;
               case 40:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 39;
                  break;
               case 41:
                  if (curChar != 44)
                     break;
                  if (kind > 12)
                     kind = 12;
                  jjCheckNAddStates(3, 5);
                  break;
               case 42:
                  if ((0x1ffffdbffL & l) == 0L)
                     break;
                  if (kind > 12)
                     kind = 12;
                  jjCheckNAddStates(3, 5);
                  break;
               case 43:
                  if ((0x2400L & l) != 0L && kind > 12)
                     kind = 12;
                  break;
               case 44:
                  if (curChar == 10 && kind > 12)
                     kind = 12;
                  break;
               case 45:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 44;
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
               case 1:
                  if ((0x7fffffffffffffffL & l) != 0L)
                  {
                     if (kind > 9)
                        kind = 9;
                     jjCheckNAdd(35);
                  }
                  else if (curChar == 127)
                  {
                     if (kind > 4)
                        kind = 4;
                     jjCheckNAdd(0);
                  }
                  break;
               case 0:
                  if (curChar != 127)
                     break;
                  if (kind > 4)
                     kind = 4;
                  jjCheckNAdd(0);
                  break;
               case 2:
                  jjAddStates(15, 17);
                  break;
               case 11:
                  if (curChar == 127)
                     jjCheckNAddStates(18, 26);
                  break;
               case 14:
                  if (curChar == 127)
                     jjCheckNAddStates(27, 35);
                  break;
               case 16:
                  if ((0xffffffffefffffffL & l) != 0L)
                     jjCheckNAddStates(9, 14);
                  break;
               case 18:
                  if (curChar == 92)
                     jjAddStates(36, 37);
                  break;
               case 19:
                  jjCheckNAddStates(9, 14);
                  break;
               case 20:
                  if (curChar == 92)
                     jjAddStates(38, 42);
                  break;
               case 21:
                  if (curChar == 117)
                     jjstateSet[jjnewStateCnt++] = 22;
                  break;
               case 22:
                  if ((0x7e0000007eL & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 23;
                  break;
               case 23:
                  if ((0x7e0000007eL & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 24;
                  break;
               case 24:
               case 27:
                  if ((0x7e0000007eL & l) != 0L)
                     jjCheckNAdd(25);
                  break;
               case 25:
                  if ((0x7e0000007eL & l) != 0L)
                     jjCheckNAddStates(9, 14);
                  break;
               case 26:
                  if (curChar == 120)
                     jjstateSet[jjnewStateCnt++] = 27;
                  break;
               case 35:
                  if ((0x7fffffffffffffffL & l) == 0L)
                     break;
                  if (kind > 9)
                     kind = 9;
                  jjCheckNAdd(35);
                  break;
               case 37:
                  if (curChar != 127)
                     break;
                  if (kind > 11)
                     kind = 11;
                  jjAddStates(6, 8);
                  break;
               case 42:
                  if (curChar != 127)
                     break;
                  if (kind > 12)
                     kind = 12;
                  jjAddStates(3, 5);
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 2:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjAddStates(15, 17);
                  break;
               case 16:
               case 19:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjCheckNAddStates(9, 14);
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
      if ((i = jjnewStateCnt) == (startsAt = 46 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   3, 4, 6, 42, 43, 45, 37, 38, 40, 10, 13, 16, 17, 18, 20, 2, 
   3, 5, 10, 11, 12, 34, 13, 16, 17, 18, 20, 10, 13, 14, 12, 33, 
   16, 17, 18, 20, 19, 30, 21, 26, 28, 29, 15, 
};
public static final String[] jjstrLiteralImages = {
"", "\133", "\135", null, null, null, null, null, null, null, null, null, null, 
null, null, };
public static final String[] lexStateNames = {
   "DEFAULT", 
   "RHS", 
};
public static final int[] jjnewLexState = {
   -1, -1, -1, -1, -1, -1, -1, 0, -1, -1, -1, -1, -1, -1, 1, 
};
private SimpleCharStream input_stream;
private final int[] jjrounds = new int[46];
private final int[] jjstateSet = new int[92];
protected char curChar;
public ConfigParserTokenManager(SimpleCharStream stream)
{
   if (SimpleCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}
public ConfigParserTokenManager(SimpleCharStream stream, int lexState)
{
   this(stream);
   SwitchTo(lexState);
}
public void ReInit(SimpleCharStream stream)
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
   for (i = 46; i-- > 0;)
      jjrounds[i] = 0x80000000;
}
public void ReInit(SimpleCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}
public void SwitchTo(int lexState)
{
   if (lexState >= 2 || lexState < 0)
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
       jjmatchedKind = 0x7fffffff;
       jjmatchedPos = 0;
       curPos = jjMoveStringLiteralDfa0_0();
       break;
     case 1:
       jjmatchedKind = 0x7fffffff;
       jjmatchedPos = 0;
       curPos = jjMoveStringLiteralDfa0_1();
       break;
   }
     if (jjmatchedKind != 0x7fffffff)
     {
        if (jjmatchedPos + 1 < curPos)
           input_stream.backup(curPos - jjmatchedPos - 1);
           matchedToken = jjFillToken();
       if (jjnewLexState[jjmatchedKind] != -1)
         curLexState = jjnewLexState[jjmatchedKind];
           return matchedToken;
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