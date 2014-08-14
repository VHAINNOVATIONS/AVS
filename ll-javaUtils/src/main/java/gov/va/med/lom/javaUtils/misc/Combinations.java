package gov.va.med.lom.javaUtils.misc;

import java.util.*;

class Enumerate {

  static int numbits(int n) {
    int cnt = 0;
	while (n != 0) {
	  cnt += (n & 0x1);
	  n >>= 1;
    }
    return cnt;
  }

  static void enumerate(java.util.List inlist, java.util.List outlist, int n, int max) {
    for (int i = 0; i <= max; i++) {
	  if (numbits(i) != n)
	    continue;
	  java.util.List lst = new ArrayList();
	  int x = i;
	  int j = 0;
      while (x != 0) {
        if ((x & 0x1) != 0) {
          if (j < inlist.size())
            lst.add(inlist.get(j));
        } else {
          j++;
        }
        x >>= 1;
      }
      if (lst.size() == n) {
        outlist.add(lst);
      }
    }
  }

  static java.util.List enumerate(java.util.List inlist, int max) {
    java.util.List outlist = new ArrayList();
    int n = (16 << inlist.size()) - 1;
    for (int i = 1; i <= max; i++)
      enumerate(inlist, outlist, i, n);
    return outlist;
  }

}

public class Combinations {

  static final double[] PILLS = {0.5, 1.0, 1.25, 1.5, 2, 2.5, 3.0, 3.75, 4.0, 5.0, 6.0, 7.5, 10.0};
//  static final double[] PILLS = {1.0, 2, 2.5, 3.0, 4.0, 5.0, 6.0, 7.5, 10.0};

  static final int MAX = 5; // 1=1, 2=2, 3=4, 4=8, 5=16

  public static void main(String args[]) {
    for (int i = 1; i <= 5; i++)
      System.out.println(Math.pow(2, i-1));
/*

    java.util.List list = new ArrayList();
    for (int i = 0; i < PILLS.length; i++)
      list.add(new Double(PILLS[i]));
    java.util.List outlist = Enumerate.enumerate(list, MAX);
    for (int i = 0; i < outlist.size(); i++) {
      java.util.List lst = (java.util.List)outlist.get(i);
      System.out.println(lst);
    }
    System.out.println("# = " + outlist.size());
*/
  }
}

