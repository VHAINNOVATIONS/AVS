package gov.va.med.lom.javaUtils.html;

import java.io.*;

public class TextToHtml{
  public static void main(String[] args){
    if (args.length == 0){
      try {
        htmlify(System.in, System.out,"Standard Input");
      } catch (IOException e){
        System.err.println(e.getMessage());
      }
    }
    for (int i=0; i<args.length; i+=2) {
      try {
        InputStream in = new FileInputStream(args[i]);
        in.close();
          PrintStream out =  new PrintStream(new FileOutputStream(args[i+1]+".html"));
        out.println("<" + "!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
        out.println("<" + "html>");
        out.println("<" + "head>");
        out.println("<" + "title>");
        out.println(args[i]);
        out.println("<" + "/title>");
        out.println("<" + "/head>");
        out.println("<" + "body>");
        htmlify(new FileInputStream(args[i]), out, args[i+1]);
        out.println("<" + "/tt>");
        out.println("<" + "/body>");
        out.println("<" + "/html>");
      }
      catch (IOException e) {
        System.err.println(e.getMessage());
      }
    }
  }

  private static void htmlify(InputStream in, PrintStream out, String fileName) throws IOException{
    int c;
    boolean inEscape = false;
    boolean continueLine = false;
    StringBuffer escapeBuffer = new StringBuffer();
    while ((c=in.read()) != -1) {
      if (inEscape){
        if (c == '\n' || c == '\r') {
          inEscape = false;
          writeEscapedHTML("\\" + escapeBuffer.toString(), out);
          escapeBuffer.setLength(0);
        } else  if (c == '\\'){
          inEscape = false;
          if (escapeBuffer.toString().equals("->")){
            out.print("&#8594;");
          } else if (escapeBuffer.toString().equals("pi")){
            out.print("&#960;");
          } else if (escapeBuffer.toString().equals(">=")){
            out.print("&#8805;");
          } else if (escapeBuffer.toString().equals("<=")){
            out.print("&#8804;");
          } else if (escapeBuffer.toString().equals("<>") ||
              escapeBuffer.toString().equals("!=")){
            out.print("&#8800;");
          } else if (escapeBuffer.toString().equals("sqrt") ||
              escapeBuffer.toString().equals("root")){
            out.print("&#8730;");
          } else if (escapeBuffer.toString().equals("CubeRt")){
            out.print("&#179;&#8730;");
          } else if (escapeBuffer.toString().equals("10^")){
            out.print("<small>10</small>^");
          } else if (escapeBuffer.toString().equals("(-)")){
            out.print("<sup>-</sup>");
          } else if (escapeBuffer.toString().equals("XrootY")){
            out.print("<sup>x</sup>&#8730;");
          } else if (escapeBuffer.toString().equals("n")){
            out.print("<i>n</i>");
          } else if (escapeBuffer.toString().equals("T")){
            out.print("<sup>T</sup>");
          } else if (escapeBuffer.toString().equals("^2") ||
              escapeBuffer.toString().equals("sqrd")){
            out.print("&#178;");
          } else if (escapeBuffer.toString().equals("^3")){
            out.print("&#179;");
          } else if (escapeBuffer.toString().equals("^x")){
            out.print("<sup>x</sup>");
          } else if (escapeBuffer.toString().equals("^-1")){
            out.print("<sup>-1</sup>");
          } else if (escapeBuffer.toString().equals("rad") ||
              escapeBuffer.toString().equals("r")){
            out.print("<sup>r</sup>");
          } else if (escapeBuffer.toString().equals("deg") ||
              escapeBuffer.toString().equals("o")){
            out.print("&#176;");
          } else if (escapeBuffer.toString().equals("sinh^-1")){
            out.print("sinh<sup>-1</sup>");
          } else if (escapeBuffer.toString().equals("sin^-1")){
            out.print("sin<sup>-1</sup>");
          } else if (escapeBuffer.toString().equals("cosh^-1")){
            out.print("cosh<sup>-1</sup>");
          } else if (escapeBuffer.toString().equals("cos^-1")){
            out.print("cos<sup>-1</sup>");
          } else if (escapeBuffer.toString().equals("tanh^-1")){
            out.print("tanh<sup>-1</sup>");
          } else if (escapeBuffer.toString().equals("tan^-1")){
            out.print("tan<sup>-1</sup>");
          } else if (escapeBuffer.toString().equals(">Frac")){
            out.print("<"+ "i"+"mg s"+"rc=\"to.gif\" width=\"5\" height=\"7\" alt=\"\" align=\"absmiddle\">Frac");
          } else if (escapeBuffer.toString().equals(">DMS")){
            out.print("<"+ "i"+"mg s"+"rc=\"to.gif\" width=\"5\" height=\"7\" alt=\"\" align=\"absmiddle\">DMS");
          } else if (escapeBuffer.toString().equals(">Dec")){
            out.print("<"+ "i"+"mg s"+"rc=\"to.gif\" width=\"5\" height=\"7\" alt=\"\" align=\"absmiddle\">Dec");
          } else if (escapeBuffer.toString().equals("E") ||
              escapeBuffer.toString().equals("EE")){
            out.print("<small>E</small>");
          } else if (escapeBuffer.toString().equals("Z-@max")){
            out.print("Z&#920;max");
          } else if (escapeBuffer.toString().equals("Z-@min")){
            out.print("Z&#920;min");
          } else if (escapeBuffer.toString().equals("Z-@step")){
            out.print("Z&#920;step");
          } else if (escapeBuffer.toString().equals("Delta-x")){
            out.print("&#916;X");
          } else if (escapeBuffer.toString().equals("Delta-y")){
            out.print("&#916;Y");
          } else if (escapeBuffer.toString().equals("@max")){
            out.print("&#920;max");
          } else if (escapeBuffer.toString().equals("@min")){
            out.print("&#920;min");
          } else if (escapeBuffer.toString().equals("@step")){
            out.print("&#920;step");
          } else if (escapeBuffer.toString().equals("Sigma-x")){
            out.print("&#931;x");
          } else if (escapeBuffer.toString().equals("Sigma-x^2")){
            out.print("&#931;x&#178;");
          } else if (escapeBuffer.toString().equals("Sigma-xy")){
            out.print("&#931;xy");
          } else if (escapeBuffer.toString().equals("Sigma-y")){
            out.print("&#931;y");
          } else if (escapeBuffer.toString().equals("Sigma-y^2")){
            out.print("&#931;y&#178;");
          } else if (escapeBuffer.toString().equals("Sigma")){
            out.print("&#931;");
          } else if (escapeBuffer.toString().equals("sx")){
            out.print("&#963;x");
          } else if (escapeBuffer.toString().equals("sy")){
            out.print("&#963;y");
          } else if (escapeBuffer.toString().equals("sigma")){
            out.print("&#963;");
          } else if (escapeBuffer.toString().equals("x-bar") ||
              escapeBuffer.toString().equals("xbar")){
            out.print("<"+ "i"+"mg s"+"rc=\"xbar.gif\" width=\"5\" height=\"7\" alt=\"\" align=\"absmiddle\">");
          } else if (escapeBuffer.toString().equals("y-bar") ||
              escapeBuffer.toString().equals("ybar")){
            out.print("<"+ "i"+"mg s"+"rc=\"ybar.gif\" width=\"5\" height=\"7\" alt=\"\" align=\"absmiddle\">");
          } else if (escapeBuffer.toString().equals("Un")){
            out.print("U<i>n</i>");
          } else if (escapeBuffer.toString().equals("Vn")){
            out.print("V<i>n</i>");
          } else if (escapeBuffer.toString().equals("UnStart")){
            out.print("U<i>n</i>Start");
          } else if (escapeBuffer.toString().equals("VnStart")){
            out.print("V<i>n</i>Start");
          } else if (escapeBuffer.toString().equals("nStart")){
            out.print("<i>n</i>Start");
          } else if (escapeBuffer.toString().equals("nMin")){
            out.print("<i>n</i>Min");
          } else if (escapeBuffer.toString().equals("nMax")){
            out.print("<i>n</i>Max");
          } else if (escapeBuffer.toString().equals("Un-1")){
            out.print("U<i>n</i>-1");
          } else if (escapeBuffer.toString().equals("Vn-1")){
            out.print("V<i>n</i>-1");
          } else if (escapeBuffer.toString().equals("ZUnStart")){
            out.print("ZU<i>n</i>Start");
          } else if (escapeBuffer.toString().equals("ZVnStart")){
            out.print("ZV<i>n</i>Start");
          } else if (escapeBuffer.toString().equals("ZnStart")){
            out.print("Z<i>n</i>Start");
          } else if (escapeBuffer.toString().equals("ZnMin")){
            out.print("Z<i>n</i>Min");
          } else if (escapeBuffer.toString().equals("ZnMax")){
            out.print("Z<i>n</i>Max");
          } else if (escapeBuffer.toString().equals("Delta-Tbl")){
            out.print("&#916;Tbl");
          } else if (escapeBuffer.toString().equals("X1t")){
            out.print("X<small>1T</small>");
          } else if (escapeBuffer.toString().equals("Y1t")){
            out.print("Y<small>1T</small>");
          } else if (escapeBuffer.toString().equals("X2t")){
            out.print("X<small>2T</small>");
          } else if (escapeBuffer.toString().equals("Y2t")){
            out.print("Y<small>2T</small>");
          } else if (escapeBuffer.toString().equals("X3t")){
            out.print("X<small>3T</small>");
          } else if (escapeBuffer.toString().equals("Y3t")){
            out.print("Y<small>3T</small>");
          } else if (escapeBuffer.toString().equals("X4t")){
            out.print("X<small>4T</small>");
          } else if (escapeBuffer.toString().equals("Y4t")){
            out.print("Y<small>4T</small>");
          } else if (escapeBuffer.toString().equals("X5t")){
            out.print("X<small>5T</small>");
          } else if (escapeBuffer.toString().equals("Y5t")){
            out.print("Y<small>5T</small>");
          } else if (escapeBuffer.toString().equals("X6t")){
            out.print("X<small>6T</small>");
          } else if (escapeBuffer.toString().equals("Y6t")){
            out.print("Y<small>6T</small>");
          } else if (escapeBuffer.toString().equals("Y1")){
            out.print("Y<small>1</small>");
          } else if (escapeBuffer.toString().equals("Y2")){
            out.print("Y<small>2</small>");
          } else if (escapeBuffer.toString().equals("Y3")){
            out.print("Y<small>3</small>");
          } else if (escapeBuffer.toString().equals("Y4")){
            out.print("Y<small>4</small>");
          } else if (escapeBuffer.toString().equals("Y5")){
            out.print("Y<small>5</small>");
          } else if (escapeBuffer.toString().equals("Y6")){
            out.print("Y<small>6</small>");
          } else if (escapeBuffer.toString().equals("Y7")){
            out.print("Y<small>7</small>");
          } else if (escapeBuffer.toString().equals("Y8")){
            out.print("Y<small>8</small>");
          } else if (escapeBuffer.toString().equals("Y9")){
            out.print("Y<small>9</small>");
          } else if (escapeBuffer.toString().equals("Y0")){
            out.print("Y<small>0</small>");
          } else if (escapeBuffer.toString().equals("r1")){
            out.print("r<small>1</small>");
          } else if (escapeBuffer.toString().equals("r2")){
            out.print("r<small>2</small>");
          } else if (escapeBuffer.toString().equals("r3")){
            out.print("r<small>3</small>");
          } else if (escapeBuffer.toString().equals("r4")){
            out.print("r<small>4</small>");
          } else if (escapeBuffer.toString().equals("r5")){
            out.print("r<small>5</small>");
          } else if (escapeBuffer.toString().equals("r6")){
            out.print("r<small>6</small>");
          } else if (escapeBuffer.toString().equals("L1")){
            out.print("L<small>1</small>");
          } else if (escapeBuffer.toString().equals("L2")){
            out.print("L<small>2</small>");
          } else if (escapeBuffer.toString().equals("L3")){
            out.print("L<small>3</small>");
          } else if (escapeBuffer.toString().equals("L4")){
            out.print("L<small>4</small>");
          } else if (escapeBuffer.toString().equals("L5")){
            out.print("L<small>5</small>");
          } else if (escapeBuffer.toString().equals("L6")){
            out.print("L<small>6</small>");
          } else if (escapeBuffer.toString().equals("x1")){
            out.print("x<small>1</small>");
          } else if (escapeBuffer.toString().equals("y1")){
            out.print("y<small>1</small>");
          } else if (escapeBuffer.toString().equals("x2")){
            out.print("x<small>2</small>");
          } else if (escapeBuffer.toString().equals("y2")){
            out.print("y<small>2</small>");
          } else if (escapeBuffer.toString().equals("x3")){
            out.print("x<small>3</small>");
          } else if (escapeBuffer.toString().equals("y3")){
            out.print("y<small>3</small>");
          } else if (escapeBuffer.toString().equals("Q1")){
            out.print("Q<small>1</small>");
          } else if (escapeBuffer.toString().equals("Q3")){
            out.print("Q<small>3</small>");
          } else if (escapeBuffer.toString().equals("@") ||
              escapeBuffer.toString().equals("theta")){
            out.print("&#920;");
          } else if (escapeBuffer.toString().equals("box icon") ||
              escapeBuffer.toString().equals("box")){
            out.print("<"+ "i"+"mg s"+"rc=\"box.gif\" width=\"5\" height=\"7\" alt=\"\" align=\"absmiddle\">");
          } else if (escapeBuffer.toString().equals("crs icon") ||
              escapeBuffer.toString().equals("cross")){
            out.print("<"+ "i"+"mg s"+"rc=\"cross.gif\" width=\"5\" height=\"7\" alt=\"\" align=\"absmiddle\">");
          } else if (escapeBuffer.toString().equals("dot icon") ||
              escapeBuffer.toString().equals("dot")){
            out.print("&#149;");
          } else if (escapeBuffer.toString().equals("R>P@")){
            out.print("R<"+ "img s"+"r"+"c=\"to.gif\" width=\"5\" height=\"7\" alt=\"\" align=\"absmiddle\">P&#920;");
          } else if (escapeBuffer.toString().equals("R>Pr")){
            out.print("R<"+ "img s"+"r"+"c=\"to.gif\" width=\"5\" height=\"7\" alt=\"\" align=\"absmiddle\">Pr");
          } else if (escapeBuffer.toString().equals("P>Rx")){
            out.print("R<"+ "img s"+"r"+"c=\"to.gif\" width=\"5\" height=\"7\" alt=\"\" align=\"absmiddle\">Px");
          } else if (escapeBuffer.toString().equals("P>Ry")){
            out.print("R<"+ "img s"+"r"+"c=\"to.gif\" width=\"5\" height=\"7\" alt=\"\" align=\"absmiddle\">Py");
          } else if (escapeBuffer.toString().equals(">")){
            out.print("<"+ "img s"+"r"+"c=\"to.gif\" width=\"5\" height=\"7\" alt=\"\" align=\"absmiddle\">");
          } else if (escapeBuffer.toString().equals("1")) {
            out.print("<small>1</small>");
          } else if (escapeBuffer.toString().equals("2")){
            out.print("<small>2</small>");
          } else if (escapeBuffer.toString().equals("3")){
            out.print("<small>3</small>");
          } else if (escapeBuffer.toString().equals("4")){
            out.print("<small>4</small>");
          } else if (escapeBuffer.toString().equals("5")){
            out.print("<small>5</small>");
          } else if (escapeBuffer.toString().equals("6")){
            out.print("<small>6</small>");
          } else if (escapeBuffer.toString().equals("7")){
            out.print("<small>7</small>");
          } else if (escapeBuffer.toString().equals("8")){
            out.print("<small>8</small>");
          } else if (escapeBuffer.toString().equals("9")){
            out.print("<small>9</small>");
          } else if (escapeBuffer.toString().equals("0")){
            out.print("<small>0</small>");
          } else if (escapeBuffer.toString().equals("10")){
            out.print("<small>10</small>");
          } else if (escapeBuffer.toString().equals("t")){
            out.print("<small>t</small>");
          } else if (escapeBuffer.toString().equals("Delta")){
            out.print("&#916;");
          } else if (escapeBuffer.toString().equals("i")){
            out.print("<i>i</i>");
          } else if (escapeBuffer.toString().equals("e")){
            out.print("<i>e</i>");
          } else if (escapeBuffer.toString().equals("#")){
            continueLine = true;
          } else if (escapeBuffer.toString().toLowerCase().startsWith("start") ||
              escapeBuffer.toString().toLowerCase().startsWith("stop")){
            writeEscapedHTML("\\" + escapeBuffer.toString() + "\\", out);
          } else {
            writeEscapedHTML("\\" + escapeBuffer.toString() + "\\", out);
            System.err.println(fileName + ": Unknown special symbol: " + escapeBuffer.toString());
          }
          escapeBuffer.setLength(0);
        } else {
          escapeBuffer.append("" + (char)c);
        }
      } else {
        if (c != '\r'){
          if (c != '\n' || !continueLine){
            if (c == '\\'){
              inEscape = true;
              escapeBuffer.setLength(0);
            } else {
              writeEscapedHTML("" + (char)c, out);
            }
            if (c != '\n' && continueLine){
              System.err.println(fileName + ": Line continuation symbol found not followed by new line.");
            }
          }
          continueLine = false;
        }
      }
    }
  }


  public static void writeEscapedHTML(String text, PrintStream out){
    for (int i=0; i < text.length(); i++){
      char ch = text.charAt(i);
      if (ch == '<'){
        out.print("&lt;");
      } else if (ch == '>'){
        out.print("&gt;");
      } else if (ch == '&'){
        out.print("&amp;");
      } else if (ch == '"') {
        out.print("&quot;");
      } else if (ch == ' '){
        out.print("&nbsp;");
      } else if (ch == '\t'){
        out.print("&nbsp;&nbsp;&nbsp;&nbsp;");
      } else if (ch == '\r'){
      } else if (ch == '\n'){
        out.print("<BR>\n");
      } else {
        out.print(ch);
      }
    }
  }
}
