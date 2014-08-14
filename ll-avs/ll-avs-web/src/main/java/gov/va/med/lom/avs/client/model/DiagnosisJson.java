package gov.va.med.lom.avs.client.model;

import java.io.Serializable;

public class DiagnosisJson implements Serializable {

    private String diagnosis;
    private String code;
    
    public String getDiagnosis() {
      return diagnosis;
    }
    public void setDiagnosis(String diagnosis) {
      this.diagnosis = diagnosis;
    }
    public String getCode() {
      return code;
    }
    public void setCode(String code) {
      this.code = code;
    }

}
