/**
 * 
 */
package gov.va.med.lom.foundation.util;

import gov.va.med.lom.foundation.service.response.messages.Message;
import gov.va.med.lom.foundation.service.response.messages.MessageUtils;
import gov.va.med.lom.foundation.service.response.messages.Messages;
import gov.va.med.lom.foundation.service.response.messages.Severity;

import java.util.MissingResourceException;

import org.apache.commons.lang.StringUtils;


/**
 * Concatenates the resource bundle text of messages appended to 
 * this MessagesStringBuilder.
 * Error and informational are messages are concatenated separatedly 
 * and can be requested separately.
 * @author Rob Proper (Aquilent Inc.)
 *
 */
public class MessagesStringBuilder {
    
    
    private StringBuilder errorBuilder = new StringBuilder();
    private StringBuilder warningBuilder = new StringBuilder();
    private StringBuilder infoBuilder = new StringBuilder();
    
    /**
     * Append all messages.
     * @param messages The messages
     * @param clazz The class used to search for the messages bundle
     * @return This MessagsStringBuilder, such that append() calls can be 
     * concatenated.
     */
    public MessagesStringBuilder append(Messages messages, Class clazz) {
        if (messages != null) {
            appendErrorMessages(messages, clazz);
            appendWarningMessages(messages, clazz);
            appendInformationalMessages(messages, clazz);
        }
        return this;
    }
    
    /**
     * Append a single message.
     * @param messages The messages to get the informational messages from
     * @param clazz The class used to search for the messages bundle
     * @return This MessagsStringBuilder, such that append() calls can be 
     * concatenated.
     */
    public MessagesStringBuilder append(Message message, Class clazz) {
        if (message != null) {
            StringBuilder builder = (Severity.ERROR  == message.getSeverity()) 
                ? errorBuilder : ((Severity.WARNING == message.getSeverity())
                ? warningBuilder : infoBuilder);
            String formattedMsg = null;
            try { 
                formattedMsg = MessageUtils.getSummaryMessage(message.getKey(), 
                    message.getInserts(), clazz);
                if (builder.length() > 0) {
                    builder.append("; ");
                }
            } catch (MissingResourceException e) {
                // Simply show message unformated, i.e. show the key with 
                // properties and inserts 
                formattedMsg = message.toString();
            }
            builder.append(formattedMsg);
        }
        return this;
    }

    /**
     * Append only informational messages.
     * This can be used to e.g. create info log output. 
     * @param messages The messages to get the informational messages from
     * @param clazz The class used to search for the messages bundle
     */
    public void appendInformationalMessages(Messages messages, Class clazz) {
        if (messages != null) {
            if (messages.hasInformationalMessages()) {
                for (Object m: messages.getInformationalMessages()) {
                    append((Message) m, clazz);
                }
            }
        }
    }

    /**
     * Append only informational messages.
     * This can be used to e.g. create info log output. 
     * @param messages The messages to get the informational messages from
     * @param clazz The class used to search for the messages bundle
     */
    public void appendWarningMessages(Messages messages, Class clazz) {
        if (messages != null) {
            if (messages.hasWarningMessages()) {
                for (Object m: messages.getWarningMessages()) {
                    append((Message) m, clazz);
                }
            }
        }
    }

    /**
     * Append only error messages.
     * This can be used to e.g. create error log output. 
     * @param messages The messages to get the error messages from
     * @param clazz The class used to search for the messages bundle
     */
    public void appendErrorMessages(Messages messages, Class clazz) {
        if (messages != null) {
            if (messages.hasErrorMessages()) {
                for (Object m: messages.getErrorMessages()) {
                    append((Message) m, clazz);
                }
            }
        }
    }

    /**
     * Concatenates all error messages appended to this MessagsStringBuilder 
     * into a single string.
     * @return The string with all the messages.
     */
    public String getErrorString() {
        return errorBuilder.toString();
    }

    /**
     * Concatenates all warning messages appended to this 
     * MessagesStringBuilder into a single string.
     * @return The string with all the messages.
     */
    public String getWarningString() {
        return warningBuilder.toString();
    }
    
    /**
     * Concatenates all informational messages appended to this 
     * MessagsStringBuilder into a single string.
     * @return The string with all the messages.
     */
    public String getInfoString() {
        return infoBuilder.toString();
    }

    /**
     * Convcatenates all error and informational messages appended to this 
     * MessagsStringBuilder into a single string.
     * The error and informational messages are separated by a newline.
     * @return The string with all the messages.
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        append(builder, Severity.ERROR, getErrorString());
        append(builder, Severity.WARNING, getWarningString());
        append(builder, Severity.INFORMATIONAL, getInfoString());
        return builder.toString();
    }
    
    private void append(StringBuilder builder, Severity header, String messages) {
        assert builder != null;
        if (StringUtils.isBlank(messages)) {
            return;
        }
        if (builder.length() > 0) {
            builder.append("\n");
        }
        builder.append(header).append(": ");
        builder.append(messages);
    }


}
