package gov.va.med.lom.foundation.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class ExceptionUtils {
    
    private ExceptionUtils () {
    }
    
    @SuppressWarnings("unchecked")
    public static boolean hasCause(Throwable t, Class type) {
        Precondition.assertAssignableFrom("type", type, Throwable.class);
        for(;t != null; t = t.getCause()) {
            if (type.isAssignableFrom(t.getClass())) {
                return true;
            }
        }
        return false;
    }

    public static String printStackToString(Exception e) {
        PrintWriter printWriter = new PrintWriter(new StringWriter());
        e.printStackTrace(printWriter);
        return printWriter.toString();
    }

}
