package gov.va.med.lom.javaUtils.xml;


/*
 * A class that describes format of the output xml file.
 */
public class Indent {

	/*
	 * Default tab value.
	 */
    public static String DEFAULT_TAB = "    ";
    	
    /*
     * Indent size.
     */
	private int indent;
	
	/*
	 * Tab string, the value that is going to be treated as tab.
	 */
	private String tab;
	
	
	
	/*
	 * Constructs new Indent with the given size of indentation and the tab string.
	 */
	public Indent(int ind, String tab) {
		this.indent = ind;
		this.tab = tab;
	}
	
	
	/*
	 * toString method
	 */
	public String toString() {
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < indent; i++)
			buff.append(tab);
		return buff.toString();
	}	


	/*
	 * Increments the indentation size.
	 */
	public void increment() {
		indent++;
	}	


	/*
	 * Decrements the indentation size.
	 */
	public void decrement() {
		indent--;
	}	


	/*
	 * Returns the tab string.
	 */
	public String getTab() {
		return tab;
	}	

	/*
	 * Sets the tab string.
	 */
	public void setTab(String tab) {
		this.tab = tab;
	}	
	
}
