package gov.va.med.lom.javaUtils.config;

import java.util.StringTokenizer;
import java.text.MessageFormat;

/*
 * Class used to parse KeywordsValueTable keywords.
 *
 */

public class KeywordParser {
  static private String separator = ".";

  static private String parseComponent (StringTokenizer tokens,int compIdx, String keyword)
                                       throws KeywordValueException {
    String comp = tokens.nextToken ();
    if (comp.equals (separator) && (compIdx == 0)) {
      String pattern = "keyword should not start with a {0} separator: \"{1} \"";
      String msg = MessageFormat.format(pattern,new String[] {separator,keyword} );
      throw new KeywordValueException (msg);
    }

    boolean isOk = (comp.length () > 0);
    if (isOk) {
      if (!Character.isJavaIdentifierStart (comp.charAt (0))) {
        isOk = false;
      }
      for (int j = 1; j < comp.length (); j++) {
        if (!Character.isJavaIdentifierPart (comp.charAt (j))) {
          isOk = false;
          break;
        }
      }
    }
    if (!isOk) {
      String msg = "keyword component must be a legal Java identifier " +
                   "component \"" + comp + "\": \"" + keyword + "\"";
      throw new KeywordValueException (msg);
    }

    if (tokens.hasMoreTokens ()) {
      String sep = tokens.nextToken ();
      if (!sep.equals (separator)) {
        String msg = "keyword component separator must be a " +
                     "single '" + separator + "', got \"" + sep + "\": " +
                     keyword + "\"";
        throw new KeywordValueException (msg);
      }
    }
    return comp;
  }

  static public String [] parse (String keyword) throws KeywordValueException {
    StringTokenizer tokens = new StringTokenizer (keyword, separator, true);
    int numTokens = tokens.countTokens ();
    if ((numTokens % 2) != 1) {
      String msg = "keyword component must be single word or words " +
                   "separated by '" + separator + "': \"" + keyword + "\"";
      throw new KeywordValueException (msg);
    }
    int numComps = (numTokens / 2) + 1;
    String [] keyParts = new String [numComps];

    for (int compIdx = 0; compIdx < numComps; compIdx++) {
      keyParts [compIdx] =  parseComponent (tokens, compIdx, keyword);
    }
    return keyParts;
  }

  static public String join (String [] keywordPath) {
    StringBuffer keyword = new StringBuffer ();

    for (int idx = 0; idx < keywordPath.length; idx++) {
      if (idx > 0) {
        keyword.append (separator);
      }
      keyword.append (keywordPath [idx]);
    }
    return keyword.toString ();
  }

  static public String concat (String keyword1,String keyword2) {
    return keyword1 + separator + keyword2;
  }
}
