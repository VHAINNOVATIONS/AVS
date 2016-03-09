/* Generated By:JavaCC: Do not edit this line. ConfigStringParserTokenManager.java */
/*-----------------------------------------------------------------------------
 * ConfigStringParser (DO NOT EDIT - MACHINE GENERATED)
 *   JavaCC-generated parser for parsing a Config value String
 *-----------------------------------------------------------------------------
 */

package gov.va.med.lom.javaUtils.config;

public class ConfigStringParserTokenManager implements ConfigStringParserConstants
{
  public  java.io.PrintStream debugStream = System.out;
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjMoveStringLiteralDfa0_0()
{
   return jjMoveNfa_0(1, 0);
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
   jjnewStateCnt = 38;
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
                     if (kind > 1)
                        kind = 1;
                     jjCheckNAdd(0);
                  }
                  else if ((0xffffe7fa00000000L & l) != 0L)
                  {
                     if (kind > 4)
                        kind = 4;
                     jjCheckNAdd(27);
                  }
                  else if (curChar == 44)
                  {
                     if (kind > 7)
                        kind = 7;
                     jjCheckNAddStates(0, 2);
                  }
                  else if (curChar == 43)
                  {
                     if (kind > 6)
                        kind = 6;
                     jjCheckNAddStates(3, 5);
                  }
                  else if (curChar == 34)
                     jjCheckNAddStates(6, 11);
                  break;
               case 0:
                  if ((0x1ffffdbffL & l) == 0L)
                     break;
                  if (kind > 1)
                     kind = 1;
                  jjCheckNAdd(0);
                  break;
               case 2:
                  if (curChar == 43)
                     jjCheckNAddStates(12, 20);
                  break;
               case 3:
                  if ((0x1ffffdbffL & l) != 0L)
                     jjCheckNAddStates(12, 20);
                  break;
               case 4:
                  if ((0x2400L & l) != 0L)
                     jjCheckNAddStates(6, 11);
                  break;
               case 5:
                  if (curChar == 44)
                     jjCheckNAddStates(21, 29);
                  break;
               case 6:
                  if ((0x1ffffdbffL & l) != 0L)
                     jjCheckNAddStates(21, 29);
                  break;
               case 7:
                  if (curChar == 10)
                     jjCheckNAddStates(6, 11);
                  break;
               case 8:
                  if ((0xffffe7fbffffdbffL & l) != 0L)
                     jjCheckNAddStates(6, 11);
                  break;
               case 9:
                  if (curChar == 34 && kind > 3)
                     kind = 3;
                  break;
               case 11:
                  jjCheckNAddStates(6, 11);
                  break;
               case 14:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 15;
                  break;
               case 15:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 16;
                  break;
               case 16:
               case 19:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAdd(17);
                  break;
               case 17:
                  if ((0x3ff000000000000L & l) != 0L)
                     jjCheckNAddStates(6, 11);
                  break;
               case 20:
               case 25:
               case 26:
                  if (curChar == 13)
                     jjCheckNAdd(7);
                  break;
               case 21:
                  if (curChar == 13)
                     jjCheckNAddStates(6, 11);
                  break;
               case 22:
                  if ((0xf000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 23;
                  break;
               case 23:
                  if ((0xff000000000000L & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 24;
                  break;
               case 24:
                  if ((0xff000000000000L & l) != 0L)
                     jjCheckNAddStates(6, 11);
                  break;
               case 27:
                  if ((0xffffe7fa00000000L & l) == 0L)
                     break;
                  if (kind > 4)
                     kind = 4;
                  jjCheckNAdd(27);
                  break;
               case 28:
                  if (curChar != 43)
                     break;
                  if (kind > 6)
                     kind = 6;
                  jjCheckNAddStates(3, 5);
                  break;
               case 29:
                  if ((0x1ffffdbffL & l) == 0L)
                     break;
                  if (kind > 6)
                     kind = 6;
                  jjCheckNAddStates(3, 5);
                  break;
               case 30:
                  if ((0x2400L & l) != 0L && kind > 6)
                     kind = 6;
                  break;
               case 31:
                  if (curChar == 10 && kind > 6)
                     kind = 6;
                  break;
               case 32:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 31;
                  break;
               case 33:
                  if (curChar != 44)
                     break;
                  if (kind > 7)
                     kind = 7;
                  jjCheckNAddStates(0, 2);
                  break;
               case 34:
                  if ((0x1ffffdbffL & l) == 0L)
                     break;
                  if (kind > 7)
                     kind = 7;
                  jjCheckNAddStates(0, 2);
                  break;
               case 35:
                  if ((0x2400L & l) != 0L && kind > 7)
                     kind = 7;
                  break;
               case 36:
                  if (curChar == 10 && kind > 7)
                     kind = 7;
                  break;
               case 37:
                  if (curChar == 13)
                     jjstateSet[jjnewStateCnt++] = 36;
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
                     if (kind > 4)
                        kind = 4;
                     jjCheckNAdd(27);
                  }
                  else if (curChar == 127)
                  {
                     if (kind > 1)
                        kind = 1;
                     jjCheckNAdd(0);
                  }
                  break;
               case 0:
                  if (curChar != 127)
                     break;
                  if (kind > 1)
                     kind = 1;
                  jjCheckNAdd(0);
                  break;
               case 3:
                  if (curChar == 127)
                     jjCheckNAddStates(12, 20);
                  break;
               case 6:
                  if (curChar == 127)
                     jjCheckNAddStates(21, 29);
                  break;
               case 8:
                  if ((0xffffffffefffffffL & l) != 0L)
                     jjCheckNAddStates(6, 11);
                  break;
               case 10:
                  if (curChar == 92)
                     jjAddStates(30, 31);
                  break;
               case 11:
                  jjCheckNAddStates(6, 11);
                  break;
               case 12:
                  if (curChar == 92)
                     jjAddStates(32, 36);
                  break;
               case 13:
                  if (curChar == 117)
                     jjstateSet[jjnewStateCnt++] = 14;
                  break;
               case 14:
                  if ((0x7e0000007eL & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 15;
                  break;
               case 15:
                  if ((0x7e0000007eL & l) != 0L)
                     jjstateSet[jjnewStateCnt++] = 16;
                  break;
               case 16:
               case 19:
                  if ((0x7e0000007eL & l) != 0L)
                     jjCheckNAdd(17);
                  break;
               case 17:
                  if ((0x7e0000007eL & l) != 0L)
                     jjCheckNAddStates(6, 11);
                  break;
               case 18:
                  if (curChar == 120)
                     jjstateSet[jjnewStateCnt++] = 19;
                  break;
               case 27:
                  if ((0x7fffffffffffffffL & l) == 0L)
                     break;
                  if (kind > 4)
                     kind = 4;
                  jjCheckNAdd(27);
                  break;
               case 29:
                  if (curChar != 127)
                     break;
                  if (kind > 6)
                     kind = 6;
                  jjAddStates(3, 5);
                  break;
               case 34:
                  if (curChar != 127)
                     break;
                  if (kind > 7)
                     kind = 7;
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
               case 8:
               case 11:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjCheckNAddStates(6, 11);
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
      if ((i = jjnewStateCnt) == (startsAt = 38 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   34, 35, 37, 29, 30, 32, 2, 5, 8, 9, 10, 12, 2, 3, 4, 26, 
   5, 8, 9, 10, 12, 2, 5, 6, 4, 25, 8, 9, 10, 12, 11, 22, 
   13, 18, 20, 21, 7, 
};
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, null, null, null, };
public static final String[] lexStateNames = {
   "DEFAULT", 
};
private SimpleCharStream input_stream;
private final int[] jjrounds = new int[38];
private final int[] jjstateSet = new int[76];
protected char curChar;
public ConfigStringParserTokenManager(SimpleCharStream stream)
{
   if (SimpleCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}
public ConfigStringParserTokenManager(SimpleCharStream stream, int lexState)
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
   for (i = 38; i-- > 0;)
      jjrounds[i] = 0x80000000;
}
public void ReInit(SimpleCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}
public void SwitchTo(int lexState)
{
   if (lexState >= 1 || lexState < 0)
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

   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
         matchedToken = jjFillToken();
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