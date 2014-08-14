package gov.va.med.lom.javaUtils.misc;

import java.util.*;
import java.io.*;

public class CopyFind {
  
  // Document inner class
  class Document {

    private String docName;
    private File file;
    private FileReader inputStream;
    private List<Long> wordHash;
    private List<Long> sortedWordHash;
    private List<Integer> sortedWordNum;
    private String text;
    private long inHash;
    private int firstHash;
    private int minString;
    private int words;
    private int wordCount;
    private int charCount;
    private int matchedWords;
    private int[] docBuffer;
    private int docBufferCount;
    
    static final int TAB = 0x9;
    static final int LF = 0xA;
    static final int CR = 0xD;
    static final int SPACE = 0x20;
    
    public Document(int minString) {
      this.minString = minString;
      wordHash = new ArrayList<Long>();
      sortedWordHash = new ArrayList<Long>();
      sortedWordNum = new ArrayList<Integer>();
      inHash = 0;
      wordCount = 0;
      charCount = 0;
      docBufferCount = 0;
      docBuffer = new int[minString];
    }
    
    public void analyze() throws IOException {
      try {
        inputStream = new FileReader(file);
      } catch(Exception e) {
        e.printStackTrace();
      }      
      while (true) {
        int ch = getChar();
        if (ch < 0) {
          break;
        }
        if ((ch > 0x20) && (ch != 0x7F) && (ch != 0xFF)) {
          inHash = ((inHash << 7) | (inHash >> 23))^ch;
          charCount++;
        } else {
          if (charCount > 0) {
            wordHash.add(inHash);
            wordCount++;
            charCount = 0;
            inHash = 0;
          }
        }
      }
      words = wordCount;
      for (lCount = 0; lCount < words; lCount++) {
        sortedWordNum.add(lCount, lCount);
        sortedWordHash.add(lCount, wordHash.get(lCount));
      }
      heapSort(sortedWordHash, sortedWordNum, words);
      
      for (lCount = 0; lCount < words; lCount++) {
        if ((sortedWordHash.get(lCount).longValue() & 0xFFC00000) != 0)
          break;
      }
      firstHash = lCount;
    }
    
    private int getChar() throws IOException {
      int ch;
      int chOut = 0;
      if (docBufferCount == 0) {
        while (docBufferCount < minString) {
          ch = inputStream.read();
          if (ch < 0) {
            docBuffer[docBufferCount++] = 0x00;
            break;
          }
          if ((ch == 0x93) || (ch == 0x94))
            ch = 0x22;
          if ((ch == 0x91) || (ch == 0x92))
            ch = 0x27;
          if ((ch > 0x20) || (ch != 0x74) && (ch != 0xFF)) {
            docBuffer[docBufferCount++] = ch;
          } else if (ch == LF) {
            docBuffer[docBufferCount++] = LF;
          } else if (ch == SPACE) {
            docBuffer[docBufferCount++] = SPACE;
          } else if (ch == TAB) {
            docBuffer[docBufferCount++] = SPACE;    
          } else if (ch == CR) {
            docBuffer[docBufferCount++] = LF;                 
          } else
            docBufferCount = 0;
        }
      }
      if ((docBufferCount == minString) && (docBuffer[docBufferCount -1] >= 0)) {
        chOut = docBuffer[0];
        for (int lCount = 0; lCount < minString-1; lCount++) {
          docBuffer[lCount] = docBuffer[lCount+1];
        }
        docBufferCount--;
        while ((docBufferCount < minString) && (docBuffer[docBufferCount-1] >= 0)) {
          ch = inputStream.read();
          if ((ch == 0x93) || (ch == 0x94))
            ch = 0x22;
          if ((ch == 0x91) || (ch == 0x92))
            ch = 0x27;
          if ((ch < 0) || ((ch > 0x20) && (ch != 0x7F) && (ch != 0xFF)) || (ch == LF) || (ch == SPACE)) {
            docBuffer[docBufferCount++] = ch;
          } else if (ch == TAB) {
            docBuffer[docBufferCount++] = SPACE;
          } else if (ch == CR) {
            docBuffer[docBufferCount++] = LF;
          } else
            break;
        }
      } else {
        chOut = docBuffer[0];
        for (int lCount = 0; lCount < minString-1; lCount++) {
          docBuffer[lCount] = docBuffer[lCount+1];
        }
        docBufferCount--;        
      }
      return chOut;
    }
    
    private void heapSort(List<Long> tableA, List<Integer> tableB, int n) {
      long tempA;
      int tempB;
      int nr;
      int index1, index2, indexC, indexStart;
      
      indexStart = (n >> 1);
      index1 = (n >> 1);
      index2 = (index1 << 1) - 1;
      
      for (indexC = indexStart; indexC >= 1; indexC--) {
        index1 = indexC;
        index2 = (indexC << 1) - 1;
        while (index2 <= n) {
          if (index2 < n) {
            if (tableA.get(index2).longValue() < tableA.get(index2+1).longValue()) {
              index2++;
            }
          }
          if (tableA.get(index1).longValue() < tableA.get(index2)) {
            tempA = tableA.get(index1);
            tempB = tableB.get(index1);
            tableA.add(index1, tableA.get(index2));
            tableB.add(index1, tableB.get(index2));
            tableA.add(index2, tempA);
            tableB.add(index2, tempB);
            index1 = index2;
            index2 = (index2 << 1);
          } else {
            break;
          }
        }
      }
      
      for (nr = n-1; nr >= 1; nr--) {
        tempA = tableA.get(1);
        tempB = tableB.get(1);
        tableA.add(1, tableA.get(nr+1));
        tableB.add(1, tableB.get(nr+1));
        tableA.add(nr+1, tempA);
        tableB.add(nr+1, tempB);
        index1 = 1;
        index2 = 2;
        while (index2 <= nr) {
          if (index2 < nr) {
            if (tableA.get(index2) < tableA.get(index2+1)) {
              index2++;
            }
          }
          if (tableA.get(index1) < tableA.get(index2)) {
            tempA = tableA.get(index1);
            tempB = tableB.get(index1);
            tableA.add(index1, tableA.get(index2));
            tableB.add(index1, tableB.get(index2));
            tableA.add(index2, tempA);
            tableB.add(index2, tempB);
            index1 = index2;
            index2 = (index2 << 1);
          } else {
            break;
          }
        }
      }
    }
    
    // Getter/Setter Methods
    public String getDocName() {
      return docName;
    }

    public void setDocName(String docName) {
      this.docName = docName;
    }
    
    public File getFile() {
      return file;
    }

    public void setFile(File file) {
      this.file = file;
    }

    public int getWords() {
      return words;
    }

    public void setWords(int words) {
      this.words = words;
    }

    public int getFirstHash() {
      return firstHash;
    }

    public void setFirstHash(int firstHash) {
      this.firstHash = firstHash;
    }

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }

    public int getMinString() {
      return minString;
    }

    public void setMinString(int minString) {
      this.minString = minString;
    }

    public List<Long> getWordHash() {
      return wordHash;
    }

    public void setWordHash(List<Long> wordHash) {
      this.wordHash = wordHash;
    }

    public List<Long> getSortedWordHash() {
      return sortedWordHash;
    }

    public void setSortedWordHash(List<Long> sortedWordHash) {
      this.sortedWordHash = sortedWordHash;
    }

    public List<Integer> getSortedWordNum() {
      return sortedWordNum;
    }

    public void setSortedWordNum(List<Integer> sortedWordNum) {
      this.sortedWordNum = sortedWordNum;
    }

    public int getWordCount() {
      return wordCount;
    }

    public void setWordCount(int wordCount) {
      this.wordCount = wordCount;
    }
    
    public int getMatchedWords() {
      return matchedWords;
    }

    public void setMatchedWords(int matchedWords) {
      this.matchedWords = matchedWords;
    }

  }
  
  // Class member fields
  private List<Document> documents;
  private int docCount;
  private int lCount;
  private int docL;
  private int docR;
  private int wordCountL;
  private int wordCountR;
  private int firstL;
  private int firstR;
  private int lastL;
  private int lastR;
  private int matchedWordsL;
  private int matchedWordsR;
  private int wordCountRSave;
  private List<Integer> matchL;
  private List<Integer> matchR;
  private int compareCount;
  private int phraseLength;
  private int minString;
  private int wordThreshold;
  private int firstNewDoc;
  
  static final int DEFAULT_PHRASE_LENGTH = 6;
  static final int DEFAULT_MIN_STRING = 10;
  static final int DEFAULT_WORD_THRESHOLD = 500;
  static final int DEFAULT_FIRST_NEW_DOC = 1;
  
  public CopyFind() {
    documents = new ArrayList<Document>();
    docCount = 0;
    phraseLength = DEFAULT_PHRASE_LENGTH;
    minString = DEFAULT_MIN_STRING;
    wordThreshold = DEFAULT_WORD_THRESHOLD;
    firstNewDoc = DEFAULT_FIRST_NEW_DOC;
  }

  
  // Add new document
  public void addDocument(String path) throws IOException {
    File file = new File(path);
    addDocument(file);
  }
  
  public void addDocument(File file) throws IOException {
    Document doc = new Document(minString);
    doc.setDocName(file.getName());
    doc.setFile(file);
    documents.add(doc);
    docCount = documents.size();
  }
  
  public void compareDocs() throws IOException {
    for (Document doc : documents) {
      doc.analyze();
    }
    matchL = new ArrayList<Integer>();
    matchR = new ArrayList<Integer>();
    if (firstNewDoc < 2) {
      firstNewDoc = 2;
    }
    compareCount = 0;
    for (docL = firstNewDoc-1; docL < docCount; docL++) {
      for (docR = 0; docR < docL; docR++) {
        for (wordCountL = 0; wordCountL < documents.get(docL).getWords(); wordCountL++) {
          matchL.add(wordCountL, 0);
        }
        for (wordCountR = 0; wordCountR < documents.get(docR).getWords(); wordCountR++) {
          matchR.add(wordCountR, 0);
        }      
        wordCountL = documents.get(docL).firstHash;
        wordCountR = documents.get(docR).firstHash;
        wordCountRSave = wordCountR;
        matchedWordsL = 0;
        matchedWordsR = 0;
        while ((wordCountL < documents.get(docL).getWords()) && (wordCountR < documents.get(docR).getWords())) {
          if (matchL.get(documents.get(docL).getSortedWordNum().get(wordCountL).intValue()) != 0) {
            wordCountL++;
            continue;
          }
          if (matchR.get(documents.get(docR).getSortedWordNum().get(wordCountR).intValue()) != 0) {
            wordCountR++;
            continue;
          }
          if (documents.get(docL).sortedWordHash.get(wordCountL).longValue() < documents.get(docR).sortedWordHash.get(wordCountR).longValue()) {
            wordCountL++;
            if (wordCountL >= documents.get(docL).getWords())
              break;
            if (documents.get(docL).getSortedWordHash().get(wordCountL).longValue() == documents.get(docL).getSortedWordHash().get(wordCountL-1).longValue()) {
              wordCountR = wordCountRSave;
            } else {
              wordCountRSave = wordCountR;
            }
            continue;
          }
          if (documents.get(docL).getSortedWordHash().get(wordCountL).longValue() > documents.get(docR).getSortedWordHash().get(wordCountR).longValue()) {
            wordCountR++;
            wordCountRSave = wordCountR;
            continue;
          }
          firstL = documents.get(docL).getSortedWordNum().get(wordCountL).intValue() - 1;
          lastL = documents.get(docL).getSortedWordNum().get(wordCountL).intValue() + 1;
          firstR = documents.get(docR).getSortedWordNum().get(wordCountR).intValue() - 1;
          lastR = documents.get(docR).getSortedWordNum().get(wordCountR).intValue() + 1;
          
          while ((firstL >= 0) && (firstR >= 0)) {
            if (matchL.get(firstL).intValue() != 0)
              break;
            if (matchR.get(firstR).intValue() != 0)
              break;
            if (documents.get(docL).getWordHash().get(firstL).longValue() != documents.get(docR).getWordHash().get(firstR).longValue())
              break;
            firstL--;
            firstR--;
          }
          while ((lastL < documents.get(docL).getWords()) && (lastR < documents.get(docR).getWords())) {
            if (matchL.get(lastL).intValue() != 0)
              break;
            if (matchR.get(lastR).intValue() != 0)
              break;
            if (documents.get(docL).getWordHash().get(lastL).longValue() != documents.get(docR).getWordHash().get(lastR).longValue())
              break;
            lastL++;
            lastR++;
          }
          firstL++;
          firstR++;
          lastL--;
          lastR++;
          
          if (lastL - (firstL+1) >= phraseLength) {
            for (lCount = firstL; lCount <= lastL; lCount++) {
              matchL.add(lCount, 1);
            }
            for (lCount = firstR; lCount <= lastR; lCount++) {
              matchR.add(lCount, 1);
            }
            matchedWordsL += lastL - (firstL+1);
            matchedWordsR += lastR - (firstR+1);
          }
          wordCountR++;          
        }
        documents.get(docL).setMatchedWords(matchedWordsL);
        documents.get(docR).setMatchedWords(matchedWordsR);
        System.out.println("# Matching words = " +  documents.get(docL).getMatchedWords() + " for " + documents.get(docL).getDocName());
        System.out.println("# Matching words = " +  documents.get(docR).getMatchedWords() + " for " + documents.get(docR).getDocName());
        compareCount++;        
      }

      if (matchedWordsL >= wordThreshold) {
        
      }
    }
  }
  
  // Getter/Setter Methods
  public int getPhraseLength() {
    return phraseLength;
  }

  public void setPhraseLength(int phraseLength) {
    this.phraseLength = phraseLength;
  }

  public int getMinString() {
    return minString;
  }

  public void setMinString(int minString) {
    this.minString = minString;
  }

  public int getWordThreshold() {
    return wordThreshold;
  }

  public void setWordThreshold(int wordThreshold) {
    this.wordThreshold = wordThreshold;
  }

  public int getFirstNewDoc() {
    return firstNewDoc;
  }

  public void setFirstNewDoc(int firstNewDoc) {
    this.firstNewDoc = firstNewDoc;
  }

  public static void main(String[] args) throws Exception {
    CopyFind cf = new CopyFind();
    cf.addDocument("C:/dev/textproc/copyfind/note1.txt");
    cf.addDocument("C:/dev/textproc/copyfind/note2.txt");
    cf.setWordThreshold(50);
    cf.compareDocs();
  }
  
}
