package gov.va.med.lom.javaBroker.util;

public interface QSCallBack {

  /*
   * Implementing class should return 0 if the values for which the two objects
   * are being compared are equal, -1 if obj1 is < obj2, and 1 if ob1 > obj2.
   */
  public abstract int compare(Object obj1, Object obj2);

}
