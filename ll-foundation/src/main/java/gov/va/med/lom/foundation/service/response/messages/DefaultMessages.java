package gov.va.med.lom.foundation.service.response.messages;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * Basic message queue and categorization functionality.
 * @author cmorrisette
 *
 */
public class DefaultMessages implements Messages {

	/**
	 * 
	 */
	private static final long serialVersionUID = -302302555792226149L;
	private Map<Severity, HashSet<Message>> messages;
	
	/**
	 * Create a new instance.
	 */
	public DefaultMessages() {
		super();
		
		messages = new HashMap<Severity, HashSet<Message>>();
		for (Severity s : Severity.values()) {
			messages.put(s, new HashSet<Message>());
        }
	}
	
	
	/*
	 *  (non-Javadoc)
	 * @see gov.va.med.mhv.foundation.service.response.messages.Messages#
	 * addMessage(gov.va.med.mhv.foundation.service.response.messages.Message)
	 */
	public void addMessage(Message message) {
		if (message==null) {
			throw new IllegalArgumentException( "Message must be non-null" );
		}
		
		if (StringUtils.isBlank(message.getKey()) 
			|| (message.getSeverity() == null)) 
		{
			throw new IllegalArgumentException(
				"Severity and key must both be non-null");
		}
		
		Collection<Message> msgs = getMessagesList(message.getSeverity());
		msgs.add(message);
	}

	/*
	 *  (non-Javadoc)
	 * @see gov.va.med.mhv.foundation.service.response.messages.Messages#
	 * getErrorMessageCount()
	 */
	public int getErrorMessageCount() {
		return getMessageCount(Severity.ERROR);
	}

	/*
	 *  (non-Javadoc)
	 * @see gov.va.med.mhv.foundation.service.response.messages.Messages#
	 * getErrorMessageCount(java.lang.String)
	 */
	public int getErrorMessageCount(String property) {
		return getMessageCount(Severity.ERROR, property);
	}

	/*
	 *  (non-Javadoc)
	 * @see gov.va.med.mhv.foundation.service.response.messages.Messages#
	 * getErrorMessages()
	 */
	public Collection<Message> getErrorMessages() {
		return getMessagesList(Severity.ERROR);
	}

	/*
	 *  (non-Javadoc)
	 * @see gov.va.med.mhv.foundation.service.response.messages.Messages#
	 * getErrorMessages(java.lang.String)
	 */
	public Collection<Message> getErrorMessages(String property) {
		return getMessagesList(Severity.ERROR, property);
	}

	public int getInformationalMessageCount() {
		Collection msgs = getMessagesList(Severity.INFORMATIONAL);
		return msgs.size();
	}

	/*
	 *  (non-Javadoc)
	 * @see gov.va.med.mhv.foundation.service.response.messages.Messages#
	 * getInformationalMessageCount(java.lang.String)
	 */
	public int getInformationalMessageCount(String property) {
		return getMessageCount(Severity.INFORMATIONAL, property);
	}

	/*
	 *  (non-Javadoc)
	 * @see gov.va.med.mhv.foundation.service.response.messages.Messages#
	 * getInformationalMessages()
	 */
	public Collection<Message> getInformationalMessages() {
		return getMessagesList(Severity.INFORMATIONAL);
	}

	/*
	 *  (non-Javadoc)
	 * @see gov.va.med.mhv.foundation.service.response.messages.Messages#
	 * getInformationalMessages(java.lang.String)
	 */
	public Collection<Message> getInformationalMessages(String property) {
		return getMessagesList(Severity.INFORMATIONAL, property);
	}

	/*
	 *  (non-Javadoc)
	 * @see gov.va.med.mhv.foundation.service.response.messages.Messages#
	 * hasErrorMessages()
	 */
	public boolean hasErrorMessages() {
		return getErrorMessageCount() > 0;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see gov.va.med.mhv.foundation.service.response.messages.Messages#
	 * hasErrorMessages(java.lang.String)
	 */
	public boolean hasErrorMessages(String property) {
		return getErrorMessageCount(property) > 0;
	}

	/*
	 *  (non-Javadoc)
	 * @see gov.va.med.mhv.foundation.service.response.messages.Messages#
	 * hasInformationalMessages()
	 */
	public boolean hasInformationalMessages() {
		return getInformationalMessageCount() > 0;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see gov.va.med.mhv.foundation.service.response.messages.Messages#
	 * hasInformationalMessages(java.lang.String)
	 */
	public boolean hasInformationalMessages(String property) {
		return getInformationalMessageCount(property) > 0; 
	}
	
	/**
	 * Return a collection of all messages that have the specified severity
	 * @param s the severity which is used to lookup messages
	 * @return a collection of messages or an empty collection
	 */
	private Collection<Message> getMessagesList(Severity s) {
		return messages.get(s);
	}
	
	/**
	 * Return a collection of messages where the property within
	 * the collection has the specified severity and in addition
	 * has the specified property
	 * @param s - the specified severity to find 
	 * @param property - the property that a returned message must have
	 * @return a collection of messages or an empty collection
	 */
	private Collection<Message> getMessagesList(Severity s, String property) {
		Collection<Message> msgs = getMessagesList(s);
		Collection<Message> returnList = new HashSet<Message>();
		for (Iterator i = msgs.iterator(); i.hasNext();) {
			Message msg = (Message) i.next();
			if (msg.getProperties().contains(property)) {
				returnList.add(msg);
			}
		}
		return Collections.unmodifiableCollection(returnList);
	}
	
	/**
	 * Return the number oof messages that have the specified severity
	 * @param s serverity to attempt to find
	 * @return the number of messages with that severity
	 */
	private int getMessageCount(Severity s) {
		return getMessagesList(s).size();
	}
	
	/**
	 * Return the number of messages that have the specified severity 
	 * and the specified property within the message
	 * @param s the severity that attempt to find
	 * @param property property that must exist in a message for it to be 
	 * returned in the coutn
	 * @return the number of messages
	 */
	private int getMessageCount(Severity s, String property) {
		Collection msgs = getMessagesList(s);
		int count = 0;
		for (Iterator i = msgs.iterator(); i.hasNext();) {
			Message msg = (Message) i.next();
			if (msg.getProperties().contains(property)) {
				count += 1;
			}
		}
		return count;
	}

	/**
	 * Add an entire list of messages to this message list 
	 * @param messages
	 */
	public void addMessages(Messages messages) {		
		Iterator m = messages.getErrorMessages().iterator();
		while( m.hasNext() ) {
			addMessage( (Message) m.next() );
		}
		m = messages.getInformationalMessages().iterator();
		while( m.hasNext() ) {
			addMessage( (Message) m.next() );
		}
	}


    /*
     *  (non-Javadoc)
     * @see gov.va.med.mhv.foundation.service.response.messages.Messages#
     * getWarningMessageCount()
     */
    public int getWarningMessageCount() {
        return getMessageCount(Severity.WARNING);
    }

    /*
     *  (non-Javadoc)
     * @see gov.va.med.mhv.foundation.service.response.messages.Messages#
     * getWarningMessageCount(java.lang.String)
     */
    public int getWarningMessageCount(String property) {
        return getMessageCount(Severity.WARNING, property);
    }

    /*
     *  (non-Javadoc)
     * @see gov.va.med.mhv.foundation.service.response.messages.Messages#
     * getWarningMessages()
     */
    public Collection<Message> getWarningMessages() {
        return getMessagesList(Severity.WARNING);
    }

    /*
     *  (non-Javadoc)
     * @see gov.va.med.mhv.foundation.service.response.messages.Messages#
     * getWarningMessages(java.lang.String)
     */
    public Collection<Message> getWarningMessages(String property) {
        return getMessagesList(Severity.WARNING, property);
    }


    /*
     *  (non-Javadoc)
     * @see gov.va.med.mhv.foundation.service.response.messages.Messages#
     * hasWarningMessages()
     */
    public boolean hasWarningMessages() {
        return getWarningMessageCount() > 0;
    }
    
    /*
     *  (non-Javadoc)
     * @see gov.va.med.mhv.foundation.service.response.messages.Messages#
     * hasWarningMessages(java.lang.String)
     */
    public boolean hasWarningMessages(String property) {
        return getWarningMessageCount(property) > 0;
    }

	
}
