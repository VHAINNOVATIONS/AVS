package gov.va.med.lom.javaUtils.classloader;

import java.util.*;
import java.lang.reflect.*;
import java.lang.reflect.InvocationTargetException;

class HashEntry {
  private Class cls;
  private Object[] constArgs;      // constructor arguments
  private Class[] constArgTypes;   // constructor argument types
  private Method meth;

  public HashEntry(Class cls, Class[] constArgTypes, Object[] constArgs, Method meth) {
    this.cls = cls;
    this.constArgTypes = constArgTypes;
    this.constArgs = constArgs;
    this.meth = meth;
  }

  public Class getEntryClass() {
    return cls;
  }
  
  public Object[] getConstArgs() {
    return constArgs;
  }

  public Class[] getConstArgTypes() {
    return constArgTypes;
  }

  public Method getMethod() {
    return meth;
  }
}

public class HashedMethods {

  Hashtable methodsHashtable;
  Class[] classes;
  private String[] excludedMethodNames;

  public HashedMethods(String classLoaderStrategy, String[] classLoaderArgs,
                       String[] classNames, Class[][] constArgTypes, Object[][] constArgs,
                       String[] excludedMethodNames) {
    this.excludedMethodNames = excludedMethodNames;
    methodsHashtable = new Hashtable();
    classes = new Class[classNames.length];
    for(int i= 0;i < classNames.length;i++) {
      Class c = getClass(classLoaderStrategy, classLoaderArgs, classNames[i]);
      classes[i] = c;
      Method[] m = getMethods(c);
      loadHashTable(c, constArgTypes[i], constArgs[i], m);
    }
  }

  public HashedMethods(String classLoaderStrategy, String[] classLoaderArgs, 
                       String[] classNames, String[] excludedMethodNames) {
    this.excludedMethodNames = excludedMethodNames;
    methodsHashtable = new Hashtable();
    classes = new Class[classNames.length];
    for(int i= 0;i < classNames.length;i++) {
      Class c = getClass(classLoaderStrategy, classLoaderArgs, classNames[i]);
      classes[i] = c;
      Method[] m = getMethods(c);
      loadHashTable(c,null, null, m);
    }
  }

  public HashedMethods(String classLoaderStrategy, String[] classLoaderArgs, 
                       Class[] classes, String[] excludedMethodNames) {
    this.excludedMethodNames = excludedMethodNames;
    methodsHashtable = new Hashtable();
    this.classes = classes;
    for(int i= 0;i < classes.length;i++) {
      Method[] m = getMethods(classes[i]);
      loadHashTable(classes[i],null, null, m);
    }
  }

  public Class[] getClasses() {
    return classes;
  }

  public Method[] getMethods(Class cls) {
    if (cls != null) {
      Vector mVect = new Vector();
      try {
        Method[] methods = cls.getDeclaredMethods();
        for(int i = 0;i < methods.length;i++) {
          // store public methods only
          if (Modifier.isPublic(methods[i].getModifiers()))
            mVect.add(methods[i]);
        }
        return (Method[])mVect.toArray(methods);
      } catch(Throwable e) {
        System.err.println(e);
      }
    }
    return null;
  }
  
  public Class getClassForMethod(String methodName) {
     HashEntry hashEntry = getHashEntry(methodName);
     if (hashEntry != null)
       return hashEntry.getEntryClass();
     else 
       return null;
  }
  
  public Object getMethodObject(String methodName) throws InvocationTargetException,
                                                          IllegalAccessException,
                                                          NoSuchMethodException,
                                                          InstantiationException {
    HashEntry entry = getHashEntry(methodName);
    if (entry != null) {
      Class cls = entry.getEntryClass();
      Object[] constArgs = entry.getConstArgs();
      Class[] constArgTypes = entry.getConstArgTypes();
      Constructor ct = cls.getConstructor(constArgTypes);
      return ct.newInstance(constArgs);
    } else {
      throw new NoSuchMethodException();
    }
  }
  
  public Method[] getMethods() {
    Vector v = new Vector();
    for (java.util.Enumeration e = methodsHashtable.elements(); e.hasMoreElements();) {
      HashEntry entry = (HashEntry)e.nextElement();    
      if (entry != null) {
        Method meth = entry.getMethod();
        v.add(meth);
      }
    }
    Method[] methods = new Method[v.size()];
    for (int i = 0; i < methods.length; i++)
      methods[i] = (Method)v.get(i);
    return methods;
  }
  
  public Method getMethod(String methodName) {
    HashEntry entry = getHashEntry(methodName);
    if (entry != null)
      return entry.getMethod();
    else
      return null;
  }
  
  public Class getMethodReturnType(String methodName) {
    Method  method = getMethod(methodName);
    if (method != null)
      return method.getReturnType();
    else
      return null;
  }
  
  public Object invokeMethod(Object obj, String methodName, Object[] argValues) throws InvocationTargetException,
                                                                                       IllegalAccessException,
                                                                                       NoSuchMethodException,
                                                                                       InstantiationException {
    Method method = getMethod(methodName);
    if (method != null) {
      return invokeMethod(obj, method, argValues);
    } else {
      throw new NoSuchMethodException();
    }
  }

  public Object[] invokeMethod(String methodName, Object[] argValues) throws InvocationTargetException,
                                                                             IllegalAccessException,
                                                                             NoSuchMethodException,
                                                                             InstantiationException {
    HashEntry entry = getHashEntry(methodName);
    if (entry != null) {
      Object[] objects = new Object[2];
      objects[0] = getMethodObject(methodName);
      Method method = entry.getMethod();
      objects[1] = invokeMethod(objects[0], method, argValues);
      return objects;
    } else {
      throw new NoSuchMethodException();
    }
  }

  public Object invokeMethod(Object obj, Method method, 
                             Object[] argValues) throws InvocationTargetException,
                                                        IllegalAccessException,
                                                        NoSuchMethodException,
                                                        InstantiationException {
    return method.invoke(obj, argValues);
  }
  
  private HashEntry getHashEntry(String methodName) {
    return (HashEntry)methodsHashtable.get(methodName);
  }
  
  private Class getClass(String classLoaderStrategy, String[] classLoaderArgs, String className) {
    try {
      ClassLoaderStrategy cls = ClassLoaderUtil.getClassLoader(classLoaderStrategy, classLoaderArgs);
      Class c = ((ClassLoader)cls).loadClass(className);
      return c;
    } catch(Throwable e) {
      System.err.println(e);
    }
    return null;
  }

  private void loadHashTable(Class c, Class[] constArgTypes, Object[] constArgs, Method[] m) {
    if (m != null) {
      for (int i = 0; i < m.length;i++) {
        if ((m[i] != null) && (Modifier.isPublic(m[i].getModifiers()))) {
          boolean excluded = false;     
          if (excludedMethodNames != null) {
            for (int j = 0; j < excludedMethodNames.length; j++) {
              if (m[i].getName().equals(excludedMethodNames[j])) {
                excluded = true;
                break;
              }
            }
          }
          if (!excluded)
            methodsHashtable.put(m[i].getName(), new HashEntry(c, constArgTypes, constArgs, m[i]));
        }
      }
    }
  }

}
