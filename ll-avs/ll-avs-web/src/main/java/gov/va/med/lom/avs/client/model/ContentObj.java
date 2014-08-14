package gov.va.med.lom.avs.client.model;

import java.util.List;
import java.io.Serializable;

public class ContentObj implements Serializable {

  private int index;
  private String contentTypeId;
  private String contentId;
  private String createdDate;
  private String publishedDate;
  private String title;
  private String blurb;
  private String copyrightStatement;
  private String content;
  private List<String> printSources;
  private List<String> onlineSources;
  private String html;
  
  public int getIndex() {
    return index;
  }
  public void setIndex(int index) {
    this.index = index;
  }
  public String getContentTypeId() {
    return contentTypeId;
  }
  public void setContentTypeId(String contentTypeId) {
    this.contentTypeId = contentTypeId;
  }
  public String getContentId() {
    return contentId;
  }
  public void setContentId(String contentId) {
    this.contentId = contentId;
  }
  public String getCreatedDate() {
    return createdDate;
  }
  public void setCreatedDate(String createdDate) {
    this.createdDate = createdDate;
  }
  public String getPublishedDate() {
    return publishedDate;
  }
  public void setPublishedDate(String publishedDate) {
    this.publishedDate = publishedDate;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getBlurb() {
    return blurb;
  }
  public void setBlurb(String blurb) {
    this.blurb = blurb;
  }
  public String getCopyrightStatement() {
    return copyrightStatement;
  }
  public void setCopyrightStatement(String copyrightStatement) {
    this.copyrightStatement = copyrightStatement;
  }
  public String getContent() {
    return content;
  }
  public void setContent(String content) {
    this.content = content;
  }
  public List<String> getPrintSources() {
    return printSources;
  }
  public void setPrintSources(List<String> printSources) {
    this.printSources = printSources;
  }
  public List<String> getOnlineSources() {
    return onlineSources;
  }
  public void setOnlineSources(List<String> onlineSources) {
    this.onlineSources = onlineSources;
  }
  public String getHtml() {
    return html;
  }
  public void setHtml(String html) {
    this.html = html;
  }
  
}
