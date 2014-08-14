


import gov.va.med.lom.javaBroker.util.Hash;

public class ExampleHash {

	
	
	public static void main(String[] args) {
	
		String avCodes = "abcdefghijklmnop1234";
		String e;
		String d;
		
		System.out.println("control: " + avCodes);
		
		e = Hash.encrypt(avCodes);
		System.out.println("encrypted: " + e);
		
		d = Hash.decrypt(e);
		System.out.println("decryped: "  + d);
		
	}
	
	
}
