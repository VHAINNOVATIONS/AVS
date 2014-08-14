package gov.va.med.lom.foundation.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Comparator class useful for determining order based on objects' position
 * in a list.  When comparing 2 object, this comparator will return a value
 * based on where the object appears in the list.  So, if a list contains 
 * the following: ('foo', 'bar'), 'foo' is said to be 'less than' bar when
 * performing a compare (-1).  If an item is not found in the list, it will
 * return 'greater than' (+1) an item found in the list, or 'equals' to another
 * object not in the list (0).
 */
public class ListBasedComparator implements Comparator {

	private List items;
	
	/**
	 * Create a new comparator based on a list of items.
	 * 
	 * @param items The objects to be used in comparisons
	 */
	@SuppressWarnings("unchecked")
    public ListBasedComparator(List items) { 
		super();
		
		this.items = new ArrayList();
		if (items != null) {
			this.items.addAll(items);
		}
	}
	
	/**
	 * Compare two objects.
	 * 
	 * @param o1 The first object to compare
	 * @param o2 The second object to compare
	 * @return -1 of o1 is less than o2, 0 if the two objects are the same,
	 *         or 1 if o1 is greater than 02
	 */
	public int compare(Object o1, Object o2) {
		int index1 = items.indexOf(o1) == -1 ? Integer.MAX_VALUE : items.indexOf(o1);
		int index2 = items.indexOf(o2) == -1 ? Integer.MAX_VALUE : items.indexOf(o2);
		
		if (index1 > index2) {
			return 1;
		} else if (index1 < index2) {
			return -1;
		} else {
			return 0;
		}
	}

}
