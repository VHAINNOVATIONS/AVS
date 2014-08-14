package gov.va.med.lom.avs.util;

import gov.va.med.lom.javaUtils.misc.StringUtils;

public class AvsUtils {

  public static String adjustForNumericSearch(String target) {
    if (target.indexOf(';') > 0) {
      target = StringUtils.piece(target, ';', 1);
    }
    target = target.replaceAll("[^\\d.]", "");
    long iTarget = Long.valueOf(target);
    return String.valueOf(iTarget - 1);
  }  
  
}
