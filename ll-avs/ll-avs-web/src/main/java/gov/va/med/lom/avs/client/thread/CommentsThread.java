package gov.va.med.lom.avs.client.thread;

public class CommentsThread extends SheetDataThread {

  private String comments;
  
  public CommentsThread(String comments) {
    super();
    this.comments = comments;
  }
  
  public void run() {
    
    StringBuffer body = new StringBuffer();
    String data = null;
    try {
      body.append("<div id=\"comments-div\">");
      if (super.media == MEDIA_HTML) {
        body.append("<pre id=\"comments-area\">");
      }
      if ((this.comments != null) && (this.comments.trim().length() > 0)) {
        data = this.comments;
        body.append(this.comments);
      } else {
        data = super.getStringResource("none");
        body.append(data);
      }
      if (super.media == MEDIA_HTML) {
        body.append("</pre>");
      }
      body.append("</div>");
    } finally {
      String content = TPL_SECTION
        .replace("__SECTION_CLASS__", "section")        
        .replace("__SECTION_ID_SUFFIX__", "instructions")
        .replace("__SECTION_TITLE__", super.getStringResource("instructions"))
        .replace("__CONTENTS__", body.toString());
      
      setContentData("comments", "\n\n" + content + "\n\n", data);
    }    
  }
  
}
