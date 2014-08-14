package gov.va.med.lom.javaUtils.misc;

import java.util.Random;
import java.util.Collection;

public class QuickSort {
  
  private QSCallBack qsCallBack;
  
  public void setQSCallBack(QSCallBack qsCallBack) {
    this.qsCallBack = qsCallBack;
  }
  
  public void quickSort(Collection col) {
    Object[] elements = col.toArray();
    qsort1(elements, 0, elements.length - 1);
    col.clear();
    for(int i = 0; i < elements.length; i++) 
      col.add(elements[i]);
  }

  public void quickSort(Object[] elements) {
    qsort1(elements, 0, elements.length - 1);
  }
  
  private void qsort1(Object a[], int lo, int hi) {
    int h, l;
    Object p, t;
    if (lo < hi) {
      l = lo;
      h = hi;
      p = a[hi];
      do {
        while ((l < h) && qsCallBack.compare(a[l], p) <= 0) l++;
        while ((h > l) && qsCallBack.compare(a[h], p) >= 0) h--;
        if (l < h) {
          t = a[l];
          a[l] = a[h];
          a[h] = t;
        }
      } while (l < h);
      t = a[l];
      a[l] = a[hi];
      a[hi] = t;
      qsort1(a, lo, l-1);
      qsort1(a, l+1, hi);
    }
  }

  private int[] initialize(int n) {
    Random rand = new Random(System.currentTimeMillis());   // initialize seed "randomly"
    int a[] = new int[n];
  
    // fill the array in order
    for (int i = 0; i < n; i++)
      a[i] = i;
  
    // shuffle all elements by randomly exchanging each with one other.
    for (int i = 0; i < n; i++) {
      int r = rand.nextInt(n);  // generate a random position
      int temp = a[i]; a[i] = a[r]; a[r] = temp;
    }
    return a;
  }
}
