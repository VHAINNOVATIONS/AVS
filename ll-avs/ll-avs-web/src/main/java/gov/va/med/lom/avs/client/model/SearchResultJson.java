package gov.va.med.lom.avs.client.model;

import java.io.Serializable;

public class SearchResultJson implements Serializable {

  private String contentId;
  private String contentTypeId;
  private String contentObjectType;
  private String language;
  private String title;
  private String gender;
  private String blurb;
  
  public String getContentId() {
    return contentId;
  }
  public void setContentId(String contentId) {
    this.contentId = contentId;
  }
  public String getContentTypeId() {
    return contentTypeId;
  }
  public void setContentTypeId(String contentTypeId) {
    this.contentTypeId = contentTypeId;
  }
  public String getContentObjectType() {
    return contentObjectType;
  }
  public void setContentObjectType(String contentObjectType) {
    this.contentObjectType = contentObjectType;
  }
  public String getLanguage() {
    return language;
  }
  public void setLanguage(String language) {
    this.language = language;
  }
  public String getGender() {
    return gender;
  }
  public void setGender(String gender) {
    this.gender = gender;
  }
  public String getBlurb() {
    return blurb;
  }
  public void setBlurb(String blurb) {
    this.blurb = blurb;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }

}
