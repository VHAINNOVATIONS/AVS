package gov.va.med.lom.javaUtils.misc;

public class Console {
   public static void print(String text) {
      System.out.print(text);
      System.out.flush();
   }

   public static void println(String text) {
      System.out.println(text);
      System.out.flush();
   }
   
   public static void paddedPrint(String text, int len) {
     System.out.print(text);
     for(int i=0; i < (len - text.length()); i++)
       System.out.print(" ");
     System.out.flush();
   }
 
   public static void paddedPrintln(String text, int len) {
     paddedPrint(text, len);
     System.out.println();
     System.out.flush();
   }
   
   public static String readLine() {
      int ch;
      String r = "";
      boolean done = false;
      while (!done) {
        try {
          ch = System.in.read();
          if (ch < 0 || (char)ch == '\n')
            done = true;
          else if ((char)ch != '\r')
            r = r + (char) ch;
         } catch(java.io.IOException e) {
           done = true;
         }
      }
      return r;
   }

   public static String readLine(String prompt) {
      print(prompt + " ");
      return readLine();
   }

   public static int readInt(String prompt) {
     while(true) {
       print(prompt + " ");
       try {
         return Integer.valueOf(readLine().trim()).intValue();
       } catch(NumberFormatException e) {
         System.out.println("Not an integer. Please try again!");
       }
     }
   }
   
   public static long readLong(String prompt) {
     while(true) {
       print(prompt + " ");
       try {
         return Long.valueOf(readLine().trim()).longValue();
       } catch(NumberFormatException e) {
         System.out.println("Not an integer. Please try again!");
       }
     }
   }   

   public static double readDouble(String prompt) {
     while(true) {
       print(prompt + " ");
       try {
         Double d = new Double(readLine().trim());
         return d.doubleValue();
       } catch(NumberFormatException e) {
         System.out.println("Not a floating point number. Please try again!");
       }
     }
   }
}
