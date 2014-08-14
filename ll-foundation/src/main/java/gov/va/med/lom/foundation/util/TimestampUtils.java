/**
 * 
 */
package gov.va.med.lom.foundation.util;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Utilities for Timestamps.
 * @author Rob Proper (Aquilent Inc.)
 *
 */
public final class TimestampUtils {

    private TimestampUtils() {
        // Hide constructor in utility class
    }

    /**
     * Indicates whether this Timestamp object is earlier than the given 
     * Timestamp object (null-safe).
     * @param t1 The TimeStamp to check for if it is before t2.  
     * @param t2 The TimeStamp to check for if it is after t1.
     * @return true if and only if the instant of time represented by t1 
     * object is strictly earlier than the instant represented by t2; 
     * false otherwise.   
     */
    public static boolean before(Timestamp t1, Timestamp t2) {
        return (t1 != null) && (t2 != null) && t1.before(t2);
    }

    /**
     * Creates a Timestamp for the current system time (in milliseconds).
     * @return The current time TimeStamp.
     */
    public static Timestamp createCurrentTime() {
        return create(null);
    }

    /**
     * Creates a TimeStamp the time represented by given date. 
     * @param date The date to create the TimeStamp for.
     * @return The TimeStamp created from the date. If the date is null, the 
     * current time of the system is used.  
     * 
     */
    public static Timestamp create(Date date) {
        return new Timestamp((date != null) ? date.getTime() 
            : System.currentTimeMillis());
    }
}
