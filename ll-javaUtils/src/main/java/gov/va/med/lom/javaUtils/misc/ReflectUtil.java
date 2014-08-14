package gov.va.med.lom.javaUtils.misc;

import java.lang.reflect.Method;

public class ReflectUtil {
  
  // Simulates the compile-time binding of a method-name and a list of
  // parameter types. Whereas Class.getMethod(String, Class[])
  // looks for methods with the exact signature, this looks through all the
  // applicable public methods of the specified class, and selects the most specific.
  public static Method getMethod(Class clazz, String methodName,
                                  Class[] paramTypes) throws NoSuchMethodException {
    // Check the arguments are all non-null.
    if (clazz == null || methodName == null || paramTypes == null) {
      throw new IllegalArgumentException();
    }
    Method currentBestCandidate = null;
    // Iterate through the public methods of the class.
    Method[] candidates = clazz.getMethods();
    for (int i = 0; i < candidates.length; i++) {
      // We discard non-applicable methods immediately.
      if (methodApplicable(candidates[i], methodName, paramTypes)) {
        if (currentBestCandidate == null ||
            methodMoreSpecific(candidates[i], currentBestCandidate)) {
          // candidates[i] is the new current best candidate.
          currentBestCandidate = candidates[i];
        } else if (!methodMoreSpecific(currentBestCandidate, candidates[i])) {
          // Neither method is more specific than the other.
          throw new NoSuchMethodException("Ambiguity");
        }
      }
    }
    // If we have a best candidate, that's the method to return.
    if (currentBestCandidate != null) {
      return currentBestCandidate;
    }
    // No such method found.
    throw new NoSuchMethodException("No applicable public method");
  }

  // Checks whether or not a method is applicable to a method call. 
  public static boolean methodApplicable(Method m, String name, Class[] paramTypes) {
    // Check the parameters are non-null.
    if (m == null || name == null || paramTypes == null) {
      throw new IllegalArgumentException();
    }
    // Check that the name corresponds.
    if (!m.getName().equals(name)) {
      return false;
    }
    // Get the parameter types of the method, and check that the number of
    // parameters corresponds.
    Class[] actualParamTypes = m.getParameterTypes();
    if (actualParamTypes.length != paramTypes.length) {
      return false;
    }
    // Check that all of the parameter types are assignable.
    for (int i = 0; i < paramTypes.length; i++) {
      if (actualParamTypes[i].isPrimitive()) {
        if (!((actualParamTypes[i].getName().equals("int") && paramTypes[i].getName().equals("java.lang.Integer")) ||
              (actualParamTypes[i].getName().equals("boolean") && paramTypes[i].getName().equals("java.lang.Boolean")) ||
              (actualParamTypes[i].getName().equals("double") && paramTypes[i].getName().equals("java.lang.Double")) ||
              (actualParamTypes[i].getName().equals("long") && paramTypes[i].getName().equals("java.lang.Long")) ||
              (actualParamTypes[i].getName().equals("char") && paramTypes[i].getName().equals("java.lang.Char")) ||
              (actualParamTypes[i].getName().equals("short") && paramTypes[i].getName().equals("java.lang.Short")) ||
              (actualParamTypes[i].getName().equals("byte") && paramTypes[i].getName().equals("java.lang.Byte")) ||
              (actualParamTypes[i].getName().equals("float") && paramTypes[i].getName().equals("java.lang.Float"))))
          return false;
      } else {
        if ((paramTypes[i] != null) && !actualParamTypes[i].isAssignableFrom(paramTypes[i]))
          return false;
      }
    }
    return true;
  }

  // Checks whether or not the first parameter is more specific than the
  // second parameter. 
  public static boolean methodMoreSpecific(Method more, Method less) {
    // Check that both of the parameters are non-null.
    if (more == null || less == null) {
      throw new IllegalArgumentException("Null parameter");
    }
    // Check that they have the same names.
    if (!more.getName().equals(less.getName())) {
      throw new IllegalArgumentException("Different names");
    }
    // Get their parameter types and check that they have the same number of
    // parameters.
    Class[] moreParamTypes = more.getParameterTypes();
    Class[] lessParamTypes = less.getParameterTypes();
    if (moreParamTypes.length != lessParamTypes.length) {
      throw new IllegalArgumentException("Different numbers of params");
    }
    // To be more specific, more needs to have a declaring class assignable
    // to that of less.
    if (!less.getDeclaringClass().isAssignableFrom(more.getDeclaringClass())) {
      return false;
    }
    // To be more specific, more has to have parameters assignable to the
    // corresponding parameters of less.
    for (int i = 0; i < moreParamTypes.length; i++) {
      if (!lessParamTypes[i].isAssignableFrom(moreParamTypes[i])) {
        return false;
      }
    }
    return true;
  }

}
