package gov.va.med.lom.javaUtils.misc;

public class Statistics {  

  public static double min(double[] data) {
    double low = Double.MAX_VALUE;
    for(int i=0;i < data.length;i++) {
      if(data[i] < low)
        low = data[i];
    }
    return low;
  }
 
  public static double max(double[] data) {
    double high = Double.MIN_VALUE;
    for(int i=0;i < data.length;i++) {
      if(data[i] > high)
        high = data[i];
    }
    return high;
  } 

  public static double sum(double[] data) {
    double sum = 0.0;
    for(int i=0;i < data.length;i++)
      sum += data[i];
    return sum;
  }

  public static double sum(Double[] data) {
    double sum = 0.0;
    for(int i=0;i < data.length;i++)
      sum += data[i].doubleValue();
    return sum;
  }

  public static double sumOfSquares(double[] data) {
    double sum = 0.0;
    for(int i=0;i < data.length;i++)
      sum += Math.pow(data[i], 2);
    return sum;
  }

  public static double sumOfSquares(Double[] data) {
    double sum = 0.0;
    for(int i=0;i < data.length;i++)
      sum += Math.pow(data[i].doubleValue(), 2);
    return sum;
  }

  public static double product(double[] data) {
    double prod = 1;
    for(int i=0;i < data.length;i++)
      prod *= data[i]; 
    return prod;
  }

  public static double standardDeviation(double[] data) {
    double sum = sum(data);
    double sumSqrs = sumOfSquares(data);
    int n = data.length;
    double variance = ((n*sumSqrs) - Math.pow(sum,2)) / (n*(n-1));
    return Math.sqrt(variance);
  }

  public static double standardDeviation(Double[] data) {
    double sum = sum(data);
    double sumSqrs = sumOfSquares(data);
    int n = data.length;
    double variance = ((n*sumSqrs) - Math.pow(sum,2)) / (n*(n-1));
    return Math.sqrt(variance);
  }

  public static void main(String[] args) {    
    java.util.Vector vect = new java.util.Vector();
    vect.add(new Double(1.0));
    vect.add(new Double(2.0));
    vect.add(new Double(3.0));
    
    Double[] data = (Double[])vect.toArray(new Double[vect.size()]);
    System.out.println("Standard Deviation = " + Statistics.standardDeviation(data));
  }  
}  
    