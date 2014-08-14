package gov.va.med.lom.vistabroker.admin.data;

import java.io.Serializable;
import java.util.Comparator;

public class TidesDisplayOrderComparator implements Comparator<Object>,Serializable{
	public int compare(Object displayOrder1, Object displayOrder2) {
		try{
	     Integer a = new Integer(displayOrder1.toString());
	     Integer b = new Integer(displayOrder2.toString());
	     return a.compareTo(b);
		}catch( NumberFormatException e){
			String a = displayOrder1.toString();
			String b = displayOrder2.toString();
			return a.compareToIgnoreCase(b);
		}
	}
}
