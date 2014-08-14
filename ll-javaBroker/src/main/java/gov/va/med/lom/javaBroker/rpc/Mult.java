package gov.va.med.lom.javaBroker.rpc;

import java.util.ArrayList;
 
public class Mult {
  
  // Private fields	
  private ArrayList names, values;
  
  // Constructors
  public Mult() {
  	names = new ArrayList();
  	values = new ArrayList();
  }
 
  public Mult(Mult mult) {
  	this();
    for(int i=0; i < mult.getCount(); i++) {
    	String[] multiple = mult.getMultiple(i);
    	setMultiple(i, multiple[0], multiple[1]);
    }
  }
 
  // Public methods
  public void clearAll() {
  	names.clear();
  	values.clear();
  }
   
  public int position(String subscript) {
  	return names.indexOf(subscript);
  }
  
  public int getCount() {
  	return names.size();
  }
  
  public String[] order(String startSubscript, int direction) {
    int index = position(startSubscript);
    if (index > -1) {
      if (index < (getCount()-1) && (direction > 0)) {
        String[] multiple = getMultiple(index + 1);
        return multiple;
      } else if (direction == 0)
        return getMultiple(index - 1);
    }
  	return new String[] {"",""};
  }
	
  public String[] getFirst() {
  	String[] result = new String[2];
  	if (names.size() > 0) {
	    result[0] = (String)names.get(0);
	    result[1] = (String)values.get(0);
  	} else {
  	  result[0] = "";
  	  result[1] = "";
  	}
  	return result;
  }
  
  public String[] getLast() {
  	String[] result = new String[2];
  	if (names.size() > 0) {
	    result[0] = (String)names.get(names.size());
	    result[1] = (String)values.get(values.size());
  	} else {
  	  result[0] = "";
  	  result[1] = "";
  	}
  	return result;
  }
  
  public String[] getMultiple(int index) {
  	String[] result = new String[2];
  	if ((index >= 0) && (index < names.size())) {
	    result[0] = (String)names.get(index);
	    result[1] = (String)values.get(index);
  	} else {
  	  result[0] = "";
  	  result[1] = "";
  	}
  	return result;
  }
  
  public String getValue(String name) {
  	int index = position(name);
  	String[] mult = getMultiple(index);
  	return mult[1];
  }  
  
  public void setValue(String name, String value) {
    int index = position(name);
    if (index >= 0)
      values.set(index, value);
  }  
  
  public boolean getSorted() {
  	return false;
  }
  
  public void setMultiple(int index, String name, String value) {
    if ((index >= 0) && (index < names.size())) {
    	names.set(index, value);
    	values.set(index, value);
    } else {
      names.add(name);
      values.add(value);
    }
  }
  
  public void setMultiple(String name, String value) {
  	names.add(name);
    values.add(value);
  }
  
  public void setSorted(boolean value) {
  	//
  }
}
