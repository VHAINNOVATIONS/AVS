package gov.va.med.lom.foundation.util;


/**
 * A Describeable provides a describptions of its implementator for debugging 
 * purposes.
 * @see gov.va.med.mhv.util.DescriptionBuilder
 * @author Rob Proper
 */
public interface Describeable {

   /**
    * Appends a string representation of the describeable instance to
    * the given DescriptionBuilder.
    * @param builder The DescriptionBuilder to append the instance of the 
    *        implementing class to.
    */
   public void describe(DescriptionBuilder builder);

}
