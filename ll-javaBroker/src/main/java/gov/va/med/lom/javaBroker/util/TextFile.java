package gov.va.med.lom.javaBroker.util;

import java.io.*;

public class TextFile {

  // member fields
  private String filepath;
  private String text;
  private long numChars;

  // constructors
  public TextFile() {
    this.text = null;
    this.numChars = -1;
  }
  
  public TextFile(String filepath) throws IOException {
    this();
    this.filepath = filepath;
    loadFile(filepath);
  }

  // member methods
  public static boolean fileExists(String filepath) {
    return new File(filepath).exists();
  }
  
  public String loadFile(String filepath) throws IOException {
    this.filepath = filepath;
    StringBuffer fileContents = new StringBuffer();
    BufferedReader in = new BufferedReader(new FileReader(filepath));
    String line;
    while((line = in.readLine()) != null) {
      fileContents.append(line + "\n");
    }
    in.close();
    text = fileContents.toString();
    return text;
  }
  
  public String loadFile() throws IOException {
    return loadFile(filepath);
  }

  public long getCharCount() {
    if (numChars == -1) {
      for(int i=0;i < text.length();i++) {
        if(text.charAt(i) != '\n') {
          numChars++;
        }
      }
    }
    return numChars;
  }

  public String toLowerCase() {
    this.text = this.text.toLowerCase();
    return this.text;
  }

  public String toUpperCase() {
    this.text = this.text.toUpperCase();
    return this.text;
  }

  public void print() {
    System.out.println(this.text);
  }

  public String getText() {
    return this.text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void saveFile(String filename) throws IOException {
    BufferedWriter out = new BufferedWriter(new FileWriter(filename));
    out.write(this.text);
    out.flush();
    out.close();
  }
  
  public void saveFile() throws IOException {
    saveFile(filepath);
  }
  
  public String getFileName() {
    return new File(filepath).getName();
  }
}






































