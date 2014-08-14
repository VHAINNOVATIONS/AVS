package gov.va.med.lom.javaBroker.util;

import java.util.Vector;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;

@SuppressWarnings("unchecked")
public class StringUtils {

  public static final char UP = '^';
    
  public static String piece(String x, char d, int piece) {
    piece -= 1;
    int index = 0, i = 0;
    int len = x.length();
    StringBuffer sb = new StringBuffer();
    if ((piece >= 0) && (len > 0)) {
        while ((i < piece) && (index < len)) {
            if (x.charAt(index++) == d)
                i++;
        }
        while ((index < len) && (x.charAt(index) != d)) {
            sb.append(x.charAt(index++));
        }
    }
    return sb.toString();
  }
  
  public static String piece(String x, int piece) {
    return piece(x, UP, piece);
  }      
    
  public static String piece(String x, String del, int piece1, int piece2) {
    int delIndex, pieceNum = 1;
    StringBuffer resVal = new StringBuffer();
    StringBuffer str = new StringBuffer(x);
    if (piece1 == 0)
      piece1 = 1;
    if (piece2 == 0)
      piece2 = piece1;
    do {
      delIndex = str.indexOf(del);
      if ((delIndex > 0) || ((pieceNum > (piece1 - 1)) && (pieceNum < (piece2 + 1)))) {
        if ((pieceNum > (piece1 - 1)) && (pieceNum < (piece2 + 1))) {
          if ((pieceNum > piece1) && (str.length() > 0))
            resVal.append(del);
          if (delIndex > 0) {
            resVal.append(str.substring(0, delIndex));
            str.delete(0, delIndex + del.length());
          } else {
            resVal.append(str.toString());
            str.delete(0, str.length());
          }
        } else
          str.delete(0, delIndex + del.length());
      } else {
        if (str.length() > 0)
          str.delete(0, str.length());
      }
      pieceNum++;
    } while(pieceNum <= piece2);
    return resVal.toString();
  }
  
  public static String pieces(String s, char delim, int first, int last) {
    StringBuffer sb = new StringBuffer();
    for (int i = first; i <= last; i++) {
      sb.append(piece(s, delim, i));
    }
    return sb.toString();
  }
  
  public static String padString(String text, int len) {
    StringBuffer sb = new StringBuffer();
    sb.append(text);
    for(int i=0; i < (len - text.length()); i++)
      sb.append(" ");
    return sb.toString();
  }

  public static String wrapLine(String line, int maxLen) {
    int lineLen = line.length();
    if (lineLen <= maxLen)
      return line;
    else {
      int numSegs = (int)Math.ceil(lineLen / (double)maxLen);
      StringBuffer orig = new StringBuffer(line);
      StringBuffer wrapped = new StringBuffer();
      int start = 0;
      int count = maxLen;
      for (int i = 0; i < numSegs; i++) {
        String s = orig.substring(start, count);
        int index = s.length();
        if ((count < orig.length()) && orig.charAt(index) != ' ') {
          while ((index > 0) && (s.charAt(index-1) != ' ')) 
            index--;
        }
        wrapped.append(s.substring(0, index).trim());
        start += index;
        if (start < lineLen)
          wrapped.append("\n");
        if (((lineLen - count) + 1) > maxLen)
          count += maxLen - (maxLen - index);
        else
          count = lineLen;
      }
      if (start < lineLen) {
        wrapped.append(orig.substring(start, lineLen));
      }
      return wrapped.toString();
    }
  }
  
  public static String[] sortStrings(String[] strings) {
    // Shell Sort
    int h = 1;
    while ((h * 3 + 1) < strings.length) {
      h = 3 * h + 1;
    }
    while( h > 0 ) {
      for (int i = h - 1; i < strings.length; i++) {
        String B = strings[i];
        int j = i;
        for( j = i; (j >= h) && (strings[j-h].compareToIgnoreCase(B) > 0); j -= h) {
          strings[j] = strings[j-h];
        } 
        strings[j] = B;
      }
      h = h / 3;
    }
    return strings;
  }
  
  public static List<String> sortByPiece(List list, char delim, int pieceNum) {
    String[] strings = new String[list.size()];
    for(int i = 0; i < list.size(); i++) {
      strings[i] = piece((String)list.get(i), delim, pieceNum) + delim + (String)list.get(i);   
    }
    strings = sortStrings(strings);
    List<String> aList = new ArrayList<String>();
    for(int i = 0; i < list.size(); i++) {
      aList.add(strings[i].substring(strings[i].indexOf(delim) + 1));   
    }
    return aList;
  }
  
  public static List<String> sortByPiece(List<String> list, int pieceNum) {
    return sortByPiece(list, UP, pieceNum);
  }
  
  private static int comparePieces(String p1, String p2, int[] pieces, char delim, boolean caseInsensitive) {
    int i = 0;
    int result = 0;
    while (i < pieces.length) {
      if (caseInsensitive)
        result = piece(p1, delim, pieces[i]).compareToIgnoreCase(piece(p2, delim, pieces[i]));
      else
        result = piece(p1, delim, pieces[i]).compareTo(piece(p2, delim, pieces[i]));
      if (result == 0)
        i++;
      else 
        return result;
    }
    return 0;
  }
  
  private static String[] sortByPieces(String[] strings, int[] pieces, char delim, int l, int r) {
    int i;
    int j;
    do {
      i = l;
      j = r;
      String p = strings[(l + r) >> 1];
      do {
        while (comparePieces(strings[i], p, pieces, delim, true) < 0)
          i++;
        while (comparePieces(strings[j], p, pieces, delim, true) > 0)
          j--;
        if (i <= j) {
          String temp = strings[i];
          strings[i] = strings[j];
          strings[j] = temp;
          i++;
          j--;
        }
      } while(i <= j);
      if (l < j)
        sortByPieces(strings, pieces, delim, l, j);
      l = i;
    } while (i < r);
    return strings;
  }
  
  public static List<String> sortByPieces(List<String> list, int[] pieces, char delim) {
    String[] strings = new String[list.size()];
    for(int i = 0; i < list.size(); i++) {
      strings[i] = (String)list.get(i);   
    }
    strings = sortByPieces(strings, pieces, delim, 0, list.size()- 1);
    List<String> aList = new ArrayList<String>();
    for(int i = 0; i < list.size(); i++) {
      aList.add(strings[i]);   
    }
    return aList;    
  }  
  
  public static List<String> sortByPieces(List<String> list, int[] pieces) {
    return sortByPieces(list, pieces, UP);
  }
  
  // performs a character-for-character replacement within a string
  public static String translate(String passedString, String identifier, String associator) {
    StringBuffer newString = new StringBuffer();
    for (int i = 0; i < passedString.length(); i++) {
      String substr = passedString.substring(i, i + 1);
      int position = identifier.indexOf(substr);
      if (position >= 0)
        newString.append(associator.substring(position, position + 1));
      else
        newString.append(passedString.substring(i, i + 1));
    }
    return newString.toString();
  }
  
  public static String[] extractSection(List list, String section, boolean mixed) {
    int i = -1;
    while ((i++ == list.size()) || (((String)list.get(i)).equals(section)));
    Vector v = new Vector();
    while ( (i < list.size()) && (!((String)list.get(i)).equals("$$END"))) {
      if (mixed)
        v.add(mixedCase((String)list.get(i)));
      else
        v.add((String)list.get(i));
      i++;
    }
    String[] results = new String[v.size()];
    for (int j=0; j < results.length; j++)
      results[j] = (String)v.get(j);
    return results;
  }
  
  public static String extractDefault(List<String> source, String section) {
    String result = "";
    int i = 0;
    while ((i < source.size()) && (!source.get(i).equals("~" + section))) {
      i++;
    }
    i++;
    if ((i < source.size()) && (source.get(i).charAt(0) != '~')) {
      do {
        if (source.get(i).charAt(0) == 'd') {
          result = source.get(i).substring(1, source.get(i).length());
        }
        i++;
      } while((i < source.size()) && (source.get(i).charAt(0) != '~') && (result.length() == 0));
    }
    return result;
  }
  
  public static List<String> extractItems(List<String> source, String section) {
    int i = 0;
    List<String> result = new ArrayList<String>();
    while ((i < source.size()) && (!source.get(i).equals("~" + section))) {
      i++;
    }
    i++;
    if ((i < source.size()) && (source.get(i).charAt(0) != '~')) {
      do {
        if (source.get(i).charAt(0) == 'i') {
          result.add(source.get(i).substring(1, source.get(i).length()));
        }
        i++;
      } while((i < source.size()) && (source.get(i).charAt(0) != '~'));
    }
    return result;    
  }
  
  public static List<String> extractText(List<String> source, String section) {
    return extractText(source, section, 1);
  }
  
  public static List<String> extractText(List<String> source, String section, int index) {
    int i = 0;
    int num = 0;
    List<String> result = new ArrayList<String>();
    while (num < index) {
      while ((i < source.size()) && (!source.get(i).equals("~" + section))) {
        i++;
      }
      i++;
      if ((i < source.size()) && (source.get(i).charAt(0) != '~')) {
        num++;
        do {
          if ((num == index) && (source.get(i).charAt(0) == 't')) {
            result.add(source.get(i).substring(1, source.get(i).length()));
          }
          i++;
        } while((i < source.size()) && (source.get(i).charAt(0) != '~'));
      }
      if (i >= source.size()) {
        index = 0;
      }
    }
    return result;    
  }
  
  public static String[] pieceList(String from, char delim) {
    Vector vect = new Vector();
    int startIndex = 0;
    int endIndex = from.indexOf(delim, startIndex);
    while (startIndex <= endIndex) {
      vect.add(from.substring(startIndex, endIndex));
      startIndex = endIndex + 1;
      endIndex = from.indexOf(delim, startIndex);
    }
    vect.add(from.substring(startIndex));
    String[] result = new String[vect.size()];
    for(int i = 0; i < vect.size(); i++)
      result[i] = (String)vect.elementAt(i);
    return result;
  }

  public static String delimitString(String[] lines, char delim) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0;i < lines.length;i++) {
      sb.append(lines[i]);
      if (i < lines.length-1)
        sb.append(delim);
    }
    return sb.toString();
  }

  public static boolean getBoolean(String value) {
    String v = value.toUpperCase();
    return ((v.equals("Y")) || (v.equals("YES")) || 
            (v.equals("TRUE")) || (v.equals("T")));
  }

  public static String boolToStr(boolean bool, String sTrue, String sFalse) {
    if (bool) 
      return sTrue;
    else
      return sFalse;
  }
  
  public static boolean strToBool(String str, String[] sTrue) {
    int i = 0;
    while(i < sTrue.length) {
      if (str.equalsIgnoreCase(sTrue[i]))
        return true;
      else
        i++;
    }
    return false;
  }
  
  public static boolean strToBool(String str, String sTrue) {
    return str.equalsIgnoreCase(sTrue);
  }

  public static boolean isInDelimList(String from, String value, char delim) {
    int startIndex = 0;
    int endIndex = from.indexOf(delim, startIndex);
    boolean found = false;
    while ((startIndex <= endIndex) && !found) {
      found = from.substring(startIndex, endIndex).equalsIgnoreCase(value);
      startIndex = endIndex + 1;
      endIndex = from.indexOf(delim, startIndex);
    }
    if (!found)
      found = from.substring(startIndex, from.length()).equalsIgnoreCase(value);
    return found;
  }

  public static String parseDelimitedSSN(String ssn, String delim) {
    StringBuffer sb = new StringBuffer();
    int startIndex = 0;
    int endIndex = ssn.indexOf(delim, startIndex);
    while (startIndex <= endIndex) {
      sb.append(ssn.substring(startIndex, endIndex));
      startIndex = endIndex + 1;
      endIndex = ssn.indexOf(delim, startIndex);
    }
    sb.append(ssn.substring(startIndex));
    return sb.toString();
  }

  public static String formatSSN(String ssn, char delim) {
    StringBuffer sb = new StringBuffer(ssn);
    if ((sb.length() == 9) || (sb.length() == 10)){
      sb.insert(3, delim);
      sb.insert(6, delim);
      return sb.toString();
    } else
      return ssn;
  }

  public static String unformatSSN(String text, char delim) {
    StringBuffer sb = new StringBuffer(text);
    for (int i=0; i < sb.length(); i++) {
      if (sb.charAt(i) == delim)
        sb.deleteCharAt(i);
    }
    return sb.toString();
  }

  public static String formatPhoneNumber(String str, String areaCode) {
    if ((str != null) && (!str.equals(""))) {
      if ((str.charAt(3) != '-') ||
         (str.charAt(7) != '-')) {
        // remove a preceding "1-"
        if (str.substring(0,2).equals("1-"))
          str = str.substring(2, str.length());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
          char c = str.charAt(i);
          if (Character.isDigit(c))
            sb.append(c);
        }
        if (sb.length() == 10) {
          sb.insert(3, '-');
          sb.insert(7, '-');
          return sb.toString();
        } else if (sb.length() == 7) {
          sb.insert(0, areaCode + "-");
          sb.insert(7, "-");
          return sb.toString();
        } else
          return "";
      } else
        return str;
    } else
      return "";
  }

  public static String escapeSingleQuotes(String text) {
    if (text == null)
      return text;
    if (text.indexOf('\'') == -1)
      return text;

    char[] vec = text.toCharArray();
    int veclen = text.length();

    char[] out = new char[veclen * 2];
    int outindex = 0;

    for (int i = 0; i < veclen; i++) {
      char c = vec[i];
      if (c == '\'')
        out[outindex++] = '\'';
      out[outindex++] = c;
    }
    return new String(out, 0, outindex);
  }
    
  public static String escapeDoubleQuotes(String text) {
    if (text == null)
      return text;
    if (text.indexOf('\"') == -1)
      return text;

    char[] vec = text.toCharArray();
    int veclen = text.length();

    char[] out = new char[veclen * 2];
    int outindex = 0;

    for (int i = 0; i < veclen; i++) {
      char c = vec[i];
      if (c == '\"')
        out[outindex++] = '\"';
      out[outindex++] = c;
    }
    return new String(out, 0, outindex);
  }  
  
  public static String filterText(String text) {
    if ((text != null) && (text.length() > 0)) {
      StringBuffer sb = new StringBuffer(text);
      int index = 0;
      while (index != -1) {
        index = sb.indexOf("'", index);
        if (index >= 0) {
          sb.replace(index, index+1, "\\'");
          index += 2;
        }
      }
      index = 0;
      while (index != -1) {
        index = sb.indexOf("\\", index);
        if (index >= 0) {
          if (index < sb.length()-1) {
            char c = sb.charAt(index++);
            if ((c != 'n') && (c != 'r') && (c != '"') && (c != '\\'))
              sb.replace(index, index+1, "");
          } else
            sb.replace(index, index+1, "");
        }
      }

      return sb.toString();
    }
    return text;
  }
  
  public static String filterExtASCII(String text) {
    if ((text != null) && (text.length() > 0)) {
      StringBuffer sb = new StringBuffer(text);
      for (int i = 0; i < sb.length(); i++) {
       if (sb.charAt(i) > 127)
         sb.replace(i, i+1, "");
      }
      return sb.toString();
    } 
    return text;
  }
  
  public static String filterControlChars(String text) {
    if ((text != null) && (text.length() > 0)) {
      StringBuffer sb = new StringBuffer(text);
      char[] c = {189, 188, 190, 177, 242, 243, 246, 247};
      String[] s = {"1/2", "1/4", "3/4", "+/-", ">=", ",+", "/", "=", "n"};
      for (int i = 0; i < c.length; i++) {
        int index = 0;
        while (index != -1) {
          index = sb.toString().indexOf(c[i], index);
          if (index >= 0) {
            sb.replace(index, index+1, s[i]);
            index += s.length;
          }
        }
      }
      return sb.toString();
    } 
    return text;
  }
  
  public static String escapeEntities(String text) {
    if (text != null) {
      String[] entities = {"&", "<", ">", "\'", "\""};
      String[] references = {"&amp;", "&lt;", "&gt;", "&apos;", "&quot;"};
      StringBuffer sb = new StringBuffer(text);
      for (int i=0; i < entities.length;i++) {
        int index = 0;
        while (index != -1) {
          index = sb.indexOf(entities[i], index);
          if (index >= 0) {
            sb.replace(index, index+1, references[i]);
            index += references[i].length();
          }
        }
      }
      return sb.toString();
    }
    return text;
  }
  
  public static String escapeQuotes(String text) {
    if (text != null) {
      String[] entities = {"\'"};
      String[] references = {"\'\'"};
      StringBuffer sb = new StringBuffer(text);
      for (int i=0; i < entities.length;i++) {
        int index = 0;
        while (index != -1) {
          index = sb.indexOf(entities[i], index);
          if (index >= 0) {
            sb.replace(index, index+1, references[i]);
            index += references[i].length();
          }
        }
      }
      return sb.toString();
    }
    return text;
  }  

  public static String crnlToBR(String text) {
    StringBuffer sb = new StringBuffer(text);
    int index = 0;
    while (index != -1) {
      index = sb.indexOf("\\", index);
      if (index >= 0) {
        if (index < sb.length()-1) {
          char c = sb.charAt(index++);
          if ((c == 'n') || (c == 'r'))
            sb.replace(index, index+1, "<BR>");
        } else
          sb.replace(index, index+1, "");
      }
    }
    return sb.toString();
  }
  
  public static String replaceString(String inString, String oldStr, String newStr) {
    StringBuffer sb = new StringBuffer(inString);
    int index = 0;
    while (index != -1) {
      index = sb.indexOf(oldStr, index);
      if (index > -1) {
        sb.replace(index, index + oldStr.length(), newStr);
        index += newStr.length();
      }
    }
    return sb.toString();
  }

  public static String replaceChar(String text, char originalChar, char replacementChar) {
    StringBuffer sb = new StringBuffer(text);
    for (int i=0; i < sb.length(); i++) {
      if (sb.charAt(i) == originalChar)
        sb.setCharAt(i, replacementChar);
    }
    return sb.toString();
  }

  public static String deleteChar(String text, char c) {
    StringBuffer sb = new StringBuffer(text);
    for (int i=0; i < sb.length(); i++) {
      if (sb.charAt(i) == c)
        sb.deleteCharAt(i);
    }
    return sb.toString();
  }

  public static String deleteTrailingChar(String text, char c) {
    StringBuffer sb = new StringBuffer(text);
    if (text.length() > 0) {
      if (sb.charAt(text.length()-1) == c)
        sb.deleteCharAt(text.length()-1);
      return sb.toString();
    } else
      return text;
  }

  public static boolean containsNumber(String text) {
    char[] nums = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    for (int i=0;i < text.length(); i++) {
      for (int j=0; j < nums.length; j++) {
        if (text.charAt(i) == nums[j]) 
          return true;
      }
    }
    return false;
  }
  
  public static String removeAlphaChars(String text) {
    if ((text != null) && (text.length() > 0)) {
      StringBuffer sb = new StringBuffer(text);
      for (int i = 0; i < sb.length(); i++) {
       int c = (int)sb.charAt(i);
       if ((c != 46) && ((c < 48) || (c > 57)))
         sb.replace(i, i+1, "");
      }
      return sb.toString();
    } 
    return text;    
  }
  
  public static String mixedCase(String x) {
    StringBuffer result = new StringBuffer();
    if ((x != null) && (x.length() > 0)) {
        result.append(x.charAt(0));
            for(int i = 1; i < x.length();i++) {
                char c1 = x.charAt(i - 1);
                char c2 = x.charAt(i);
                // Lower case if the previous character was a space (' '), single-quote ('''), open-paren ('('),
                // comma (','), hyphen ('-'), period ('.'), forward-slash ('/'), or underscore ('_')
                if (((c1 != 32) && (c1 != 39) && (c1 != 40) && (c1 != 44) && (c1 != 45) && 
                        (c1 != 46) && (c1 != 47) && (c1 != 94)) &&  (c2 >= 65) && (c2 <= 90)) {
                    char c = (char)(x.charAt(i) + 32);
                    result.append(c);
                } else
                result.append(x.charAt(i));
            }
    }
    return result.toString();
  }
  
  public static List<String> mixedCaseList(List<String> list) {
    List<String> aList = new ArrayList<String>();
    for(int i = 0; i < list.size(); i++)
      aList.add(mixedCase((String)list.get(i)));
    return aList;
  }
  
  public static String parseDelimitedSSN(String ssn) {
    String delim = "-";
    StringBuffer sb = new StringBuffer();
    int startIndex = 0;
    int endIndex = ssn.indexOf(delim, startIndex);
    while (startIndex <= endIndex) {
      sb.append(ssn.substring(startIndex, endIndex));
      startIndex = endIndex + 1;
      endIndex = ssn.indexOf(delim, startIndex);
    }
    sb.append(ssn.substring(startIndex));
    return sb.toString();
  }

  public static String formatSSN(String ssn) {
    char delim = '-';
    StringBuffer sb = new StringBuffer(ssn);
    if ((sb.length() == 9) || (sb.length() == 10)){
      sb.insert(3, delim);
      sb.insert(6, delim);
      return sb.toString();
    } else
      return ssn;
  }

  public static String unformatSSN(String text) {
    char delim = '-';
    StringBuffer sb = new StringBuffer(text);
    for (int i=0; i < sb.length(); i++) {
      if (sb.charAt(i) == delim)
        sb.deleteCharAt(i);
    }
    return sb.toString();
  }

  public static ArrayList<String> getArrayList(String text) {
    return (ArrayList)getStringList(text);    
  }
  
  public static List<String> getStringList(String text) {
    BufferedReader br = new BufferedReader(new StringReader(text));
    List<String> list = new ArrayList<String>();
    String line = null;
    try {
      while (true) {
        line = br.readLine();
        if (line != null)
          list.add(line);
        else
          break;
      }
    } catch(IOException ioe) {}
    return list;
  }

  public static int getCharCount(String text, char c) {
    int num = 0;
    for (int i=0; i < text.length(); i++) {
      if (text.charAt(i) == c)
        num++;
    }
    return num;
  }  
  
  public static int toInt(String s, int def) {
    try {
        if (s == null)
            return def;
        return Integer.valueOf(s).intValue();
    } catch(NumberFormatException nfe) {
        return def;
    }
  }
  
  public static long toLong(String s, long def) {
    try {
        if (s == null)
            return def;
        return Long.valueOf(s).longValue();
    } catch(NumberFormatException nfe) {
        return def;
    }
  }  
  
  public static double toDouble(String s, double def) {
    try {
        if (s == null)
            return def;
        return Double.valueOf(s).doubleValue();
    } catch(NumberFormatException nfe) {
        return def;
    }
  } 
  
  public static String getSubstringWithEllipsis(String str, int length) {
    if (str.length() <= length)
      return str;
    else
      return str.subSequence(0, length-3) + "...";
  }
  
  public static String firstCharToUpper(String text) {
    //  Set the first letter of the property name to upper-case
    StringBuffer name = new StringBuffer(text);
    char c = name.charAt(0);
    if (c >= 'a' && c <= 'z') {
      c += 'A' - 'a';
      name.setCharAt(0, c);
    }
    return name.toString();
  }
  
  public static String firstCharToLower(String text) {
    //  Set the first letter of the property name to lower-case
    StringBuffer name = new StringBuffer(text);
    char c = name.charAt(0);
    if (c >= 'A' && c <= 'Z') {
      c -= 'A' - 'a';
      name.setCharAt(0, c);
    }
    return name.toString();
  }  
  
  public static String formatCurrency(double value) {
     return formatCurrency(value, 2); 
  }
  
  public static String formatCurrency(double value, int numSigDigits) {
    boolean neg = value < 0;
    if (neg)
      value = Math.abs(value);
    StringBuffer sb = new StringBuffer(new Format("%." + numSigDigits + "f").format(value));
    sb.insert(0, "$");
    if (neg)
      sb.insert(0, "-");
    if (numSigDigits == 0)
      sb.deleteCharAt(sb.indexOf("."));
    return sb.toString();
  }
  
  public static int getRandomInt(int lo, int hi) {
    java.util.Random rn = new java.util.Random();
    int n = hi - lo + 1;
    int i = rn.nextInt() % n;
    if (i < 0)
      i = -i;
    return lo + i;
  }
  
  public static String getRandomString(int lo, int hi) {
    int n = getRandomInt(lo, hi);
    byte b[] = new byte[n];
    for (int i = 0; i < n; i++)
      b[i] = (byte)getRandomInt('a', 'z');
    try {
      return new String(b, "US-ASCII");
    } catch(Exception e) {
      return null;
    }
  }
  
}