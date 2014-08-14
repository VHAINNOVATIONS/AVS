/**
 * 
 */
package gov.va.med.lom.foundation.service.response.messages;

import gov.va.med.lom.foundation.util.Precondition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/**
 * Utilitary methods for {@link org.tigris.atlas.messages.Messages}.
 * Methods are null-safe. 
 * @author Rob Proper (Aquilent Inc.)
 */
public final class MessagesUtils {
    
    private MessagesUtils() {
    }

    /**
     * Determines (null safe) if the messages instance has a message 
     * with a given message key.
     * The message can be an error or an informational message. 
     * @param messages The messages to check
     * @param key The message key to check.
     * @return True if the messages instance has the given message; 
     * false otherwise. If messages is null false is returned.
     */
    public static boolean hasMessage(Messages messages, String key) {
        Precondition.assertNotBlank("key", key);
        return hasErrorMessage(messages, key)
            || hasWarningMessage(messages, key)
            || hasInformationalMessage(messages, key);
    }

    /**
     * Determines (null safe) if the messages instance has any error messages.
     * @param messages The messages to check
     * @return True if the messages instance has any error messages; 
     * false otherwise. If messages is null false is returned.
     */
    public static boolean hasErrorMessage(Messages messages) {
        return (messages != null) && (messages.hasErrorMessages() 
            || messages.hasErrorMessages());
    }

    /**
     * Determines (null safe) if the messages instance has an error message
     * with a given message kety. 
     * @param messages The messages to check
     * @param key The message key to check.
     * @return True if the messages instance has the given error message; 
     * false otherwise. If messages is null false is returned.
     */
    public static boolean hasErrorMessage(Messages messages, String key) {
        Precondition.assertNotBlank("key", key);
        if (messages == null) {
            return false;
        }
        for (Object m : messages.getErrorMessages()) {
            if ((m != null) && key.equals(((Message) m).getKey())) { 
                return true;
            }
        }
        
        return false;
    }

    /**
     * Determines (null safe) if the messages instance has any error messages.
     * @param messages The messages to check
     * @return True if the messages instance has any error messages; 
     * false otherwise. If messages is null false is returned.
     */
    public static boolean hasWarningMessage(Messages messages) {
        return (messages != null) && (messages.hasWarningMessages() 
            || messages.hasWarningMessages());
    }

    /**
     * Determines (null safe) if the messages instance has an error message
     * with a given message kety. 
     * @param messages The messages to check
     * @param key The message key to check.
     * @return True if the messages instance has the given error message; 
     * false otherwise. If messages is null false is returned.
     */
    public static boolean hasWarningMessage(Messages messages, String key) {
        Precondition.assertNotBlank("key", key);
        if (messages == null) {
            return false;
        }
        for (Object m : messages.getWarningMessages()) {
            if ((m != null) && key.equals(((Message) m).getKey())) { 
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Determines (null safe) if the messages instance has an informational 
     * message with a given message key. 
     * @param messages The messages to check
     * @param key The message key to check.
     * @return True if the messages instance has the given informational 
     * message; false otherwise. If messages is null false is returned.
     */
    public static boolean hasInformationalMessage(Messages messages, 
        String key) 
    {
        Precondition.assertNotBlank("key", key);
        if (messages == null) {
            return false;
        }
        for (Object m : messages.getInformationalMessages()) {
            if ((m != null) && key.equals(((Message) m).getKey())) { 
                return true;
            }
        }
        
        return false;
    }

    /**
     * Determines (null safe) if the messages instance has an informational 
     * message.
     * @param messages The messages to check
     * @return True if the messages instance has any informational 
     * message; false otherwise. If messages is null false is returned.
     */
    public static boolean hasInformationalMessage(Messages messages) {
        if (messages == null) {
            return false;
        }
        return messages.hasInformationalMessages();
    }


    @SuppressWarnings("unchecked")
    public static boolean findAndConvertMessage(Messages messages, String key,
        Severity severity, String newKey, Severity newSeverity)
    {
        Precondition.assertNotNull("messages", messages);
        Precondition.assertNotBlank("key", key);
        Precondition.assertNotNull("severity", severity);
        Precondition.assertNotBlank("newKey", newKey);
        List<Message> messagesToRemove = new ArrayList<Message>();
        Collection<Message> messagesToCheck = 
            (Severity.ERROR == severity) ? messages.getErrorMessages() 
            : ((Severity.WARNING == severity) ? messages.getWarningMessages()
               : messages.getInformationalMessages());
        for (Message message: messagesToCheck) {
            if (key.equals(message.getKey())) {
                messages.addMessage(convertMessage(message, newKey, 
                    newSeverity));
                messagesToRemove.add(message);
                break;
            }
        }
        messagesToCheck.removeAll(messagesToRemove);
        return messagesToRemove.size() > 0;
    }


    @SuppressWarnings("unchecked")
    public static Message convertMessage(Message message, String key,
        Severity severity)
    {
        String[] properties = new String[message.getProperties().size()];
        Object[] inserts= new Object[message.getInserts().size()];
        message.getProperties().toArray(properties);
        message.getInserts().toArray(inserts);
        return MessageUtils.createMessage(key, (severity != null)
            ? severity : message.getSeverity(), properties, inserts);
    }

}
