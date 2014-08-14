package gov.va.med.lom.javaBroker.rpc.lists.models;

import gov.va.med.lom.javaBroker.rpc.BaseBean;

import java.io.Serializable;

public class PersonsList extends BaseBean implements Serializable {

  private Person[] persons;
  
  public PersonsList() {
    this.persons = null;
  }
  
  public Person[] getPersons() {
    return persons;
  }
  
  public void setPersons(Person[] persons) {
    this.persons = persons;
  }
}
