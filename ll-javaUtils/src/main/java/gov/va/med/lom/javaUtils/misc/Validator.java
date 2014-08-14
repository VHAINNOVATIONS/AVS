package gov.va.med.lom.javaUtils.misc;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Validator {

  private static Pattern datePattern;
  private static Pattern emailPattern;
  private static Pattern usPostalCodePattern;
  private static Pattern namePattern;
  private static Pattern addressPattern;
  private static Pattern usernamePattern;
  private static Pattern passwordPattern;
  private static Pattern phoneNumberPattern;
  
  static {
    try {
      datePattern = Pattern.compile("^[0-1][0-9](\\/)[0-3][0-9](\\/)(\\d){4}$");
      emailPattern = Pattern.compile("^[A-Za-z0-9](([_\\.\\-]?[a-zA-Z0-9]+)*)@([A-Za-z0-9]+)(([\\.\\-]?[a-zA-Z0-9]+)*)\\.([A-Za-z]{2,})$");
      usPostalCodePattern = Pattern.compile("^\\d{5}((-|\\s)?\\d{4})?$");
      namePattern = Pattern.compile("^([1-zA-Z0-1'\\s]{2,30})$");
      addressPattern = Pattern.compile("^([A-Za-z0-9'#&\\(\\)-\\.,\\s/]{2,40})$");
      usernamePattern = Pattern.compile("^([1-zA-Z0-1]{4,20})$");
      passwordPattern = Pattern.compile("^([a-zA-Z0-9`~@#%&_,<>\\!\\[\\]\\{\\}\\'\\\"\\+\\?\\$\\^\\*\\(\\)\\-\\=]{6,20})$"); //(?!^[0-9]*$)(?!^[a-zA-Z]*$)
      phoneNumberPattern = Pattern.compile("^([A-Za-z0-9\\.\\-\\(\\)\\s]{6,30})$");
    } catch(PatternSyntaxException pse) {
      pse.printStackTrace();
    }
  }
  
  public static boolean isValidDate(String text) {
    return (text != null) && (text.trim().length() > 0) && 
           datePattern.matcher(text).matches();
  }
  
  public static boolean isValidEmail(String text) {
    return (text != null) && (text.trim().length() > 0) && 
           emailPattern.matcher(text).matches();
  }
  
  public static boolean isValidUSPostalCode(String text) {
    return (text != null) && (text.trim().length() > 0) && 
           usPostalCodePattern.matcher(text).matches();
  }
  
  public static boolean isValidName(String text) {
    return (text != null) && (text.trim().length() > 0) && 
           namePattern.matcher(text).matches();
  }
  
  public static boolean isValidAddress(String text) {
    return (text != null) && (text.trim().length() > 0) && 
           addressPattern.matcher(text).matches();
  }
  
  public static boolean isValidUsername(String text) {
    return (text != null) && (text.trim().length() > 0) && 
           usernamePattern.matcher(text).matches();
  }
  
  public static boolean isValidPassword(String text) {
    return (text != null) && (text.trim().length() > 0) && 
           passwordPattern.matcher(text).matches();
  }
  
  public static boolean isValidPhoneNumber(String text) {
    return (text != null) && (text.trim().length() > 0) && 
           phoneNumberPattern.matcher(text).matches();
  }
  
}
