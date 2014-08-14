package gov.va.med.lom.foundation.validate;


import gov.va.med.lom.foundation.service.response.messages.CoreMessages;
import gov.va.med.lom.foundation.service.response.messages.Message;
import gov.va.med.lom.foundation.service.response.messages.MessageUtils;
import gov.va.med.lom.foundation.service.response.messages.Messages;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.enums.Enum;

public final class Validations {

	private Validations() {}
	
	public static void validateRequired(Object value, String property, 
        String label, Messages messages) 
    {
		if (value == null) {
			Message msg = MessageUtils.createErrorMessage(CoreMessages.
                NULL_NOT_ALLOWED, new String [] {property},
                new Object [] {label});
			messages.addMessage(msg);
		}
	}
	
	public static void validateRequired(String value, String property, 
        String label, Messages messages) 
    {
		if (StringUtils.isBlank(value)) {
			Message msg = MessageUtils.createErrorMessage(CoreMessages.
                NULL_NOT_ALLOWED, new String [] {property},
                new Object [] {label});
			messages.addMessage(msg);
		}
	}
	
	public static void validateRequired(String month, String day, String year, 
        String property, String label, Messages messages) 
    {
		if (StringUtils.isBlank(day) && StringUtils.isBlank(month) && 
            StringUtils.isBlank(year)) 
        {
			Message msg = MessageUtils.createErrorMessage(CoreMessages.
                NULL_NOT_ALLOWED, new String [] {property}, 
                new Object [] {label});
			messages.addMessage(msg);
		}
	}
	
	public static void validateMinValue(String value, BigDecimal minValue, 
        String property, String label, Messages messages) 
    {
		if (isValidNumber(value)) {
			validateMinValue(new BigDecimal(value), minValue, property, label, 
                messages);
		}
	}
	
	@SuppressWarnings("unchecked")
    public static void validateMinValue(Comparable value, Comparable minValue, 
        String property, String label, Messages messages) 
    {
		if (value != null && value.compareTo(minValue) < 0) {
			Message msg = MessageUtils.createErrorMessage(CoreMessages.
                MIN_VALUE_NOT_MET, new String [] {property}, 
                new Object [] {label, value, minValue});
			messages.addMessage(msg);
		}
	}
	
	public static void validateMaxValue(String value, BigDecimal maxValue, 
        String property, String label, Messages messages) 
    {
		if (isValidNumber(value)) {
			validateMaxValue(new BigDecimal(value), maxValue, property, label, 
                messages);
		}
	}
	
	@SuppressWarnings("unchecked")
    public static void validateMaxValue(Comparable value, Comparable maxValue, 
        String property, String label, Messages messages) 
    {
		if (value != null && value.compareTo(maxValue) > 0) {
			Message msg = MessageUtils.createErrorMessage(CoreMessages.
                MAX_VALUE_EXCEEDED, new String [] {property},
				new Object [] {label, value, maxValue});
			messages.addMessage(msg);
		}
	}
	
	public static void validateMinLength(String value, int minLength, 
        String property, String label, Messages messages) 
    {
		if (!StringUtils.isBlank(value) && value.trim().length() < minLength) {
			Message msg = MessageUtils.createErrorMessage(CoreMessages.
                MIN_LENGTH_NOT_MET, new String [] {property},
				new Object [] {label, value, new Integer(minLength)});
			messages.addMessage(msg);
		}
	}
	
	public static void validateMaxLength(String value, int maxLength, 
        String property, String label, Messages messages) 
    {
		if (!StringUtils.isBlank(value) && value.trim().length() > maxLength) {
			Message msg = MessageUtils.createErrorMessage(CoreMessages.
                MAX_LENGTH_EXCEEDED, new String [] {property}, 
                new Object [] {label, value, new Integer(maxLength)});
			messages.addMessage(msg);
		}
	}
	
	public static void validateScale(String value, int scale, String property, 
        String label, Messages messages) 
    {
		if (isValidNumber(value)) {
			validateScale(new BigDecimal(value), scale, property, label, 
                messages);
		}
	}
	
	public static void validateScale(BigDecimal value, int scale, 
        String property, String label, Messages messages) 
    {
		if (value != null && value.scale() > scale) {
			Message msg = MessageUtils.createErrorMessage(CoreMessages.
                INVALID_SCALE, new String [] {property}, 
                new Object [] {label, new Integer(scale)});
			messages.addMessage(msg);
		}
	}
	
	public static void validateEnumeration(Enum value, Enum enumeratedValue, 
        String property, String label, Messages messages) 
    {
		if (value != null && !value.equals(enumeratedValue)) {
			Message msg = MessageUtils.createErrorMessage(CoreMessages.
                INVALID_ENUM_INSTANCE, new String [] {property}, 
                new Object [] {label, value, value.getName()});
			messages.addMessage(msg);
		}
	}
	
	public static void validateType(Object value, Validator v, String property, 
        String label, Messages messages) 
    {
		messages.addMessages(v.validate(value, new String [] {property}, 
            new Object [] {label}));
	}
	
	/**
	 * Ensure that a string value can be converted into an integer.
	 * @param value
	 * @param key
	 */
	public static void validateInteger(String value, String property, 
        String label, Messages messages) 
    {
		if(!StringUtils.isBlank(value) && ! Validations.isValidInteger(value)) {
			messages.addMessage(MessageUtils.createErrorMessage(CoreMessages.
                INVALID_NUMBER, new String [] {property}, 
                new Object[]{ value }));
		}
	}

	
	/**
	 * Ensure that a string value can be converted into a long.
	 * @param value
	 * @param key
	 */
	public static void validateLong(String value, String property, String label, 
        Messages messages) 
    {
		if(!StringUtils.isBlank(value) && ! Validations.isValidLong(value)) {
			messages.addMessage(MessageUtils.createErrorMessage(CoreMessages.
                INVALID_NUMBER, new String [] {property}, 
                new Object[]{ value }));
		}
	}

	public final static void validateBoolean(String value, String property, 
        String label, Messages messages) 
    {
		// TODO Implement this.  For now, can leave blank, as unknown strings 
        // will become false Booleans, which is fine
	}
	
	/**
	 * Ensure that a string value can be converted into a decimal.
	 * @param value
	 * @param key
	 */
	public static void validateBigDecimal(String value, String property, 
        String label, Messages messages) 
    {
		if(!StringUtils.isBlank(value) && ! Validations.isValidDecimal(value)) {
			messages.addMessage(MessageUtils.createErrorMessage(CoreMessages.
                INVALID_NUMBER, new String [] {property}, 
                new Object[]{ value }));
		}
	}
	
	public static void validateDate(String month, String day, String year, 
        String property, String label, boolean dayRequired, Messages messages) 
    {
		if (!Validations.isValidDate(month, day, year, dayRequired)) {
			messages.addMessage(MessageUtils.createErrorMessage(CoreMessages.
                INVALID_DATE, new String [] {property}, new Object[]{}));
		}
	}
	
	public static void validateTimestamp(String month, String day, String year, 
        String hours, String minutes, String property, String label, 
        boolean dayRequired, Messages messages) 
    {
		if (!Validations.isValidTimestamp(month, day, year, hours, minutes, 
                dayRequired)) 
        {
			messages.addMessage(MessageUtils.createErrorMessage(CoreMessages.
                INVALID_DATE, new String [] {property}, new Object[]{}));
		}
	}

	private static boolean isValidNumber(String s) {
		try {
			BigDecimal dec = new BigDecimal(s);
			return dec != null;
		} catch (Exception ex) {
			return false;
		}
	}
	
	/**
	 * Anserws whether a string can be converted into an integer
	 * @param value
	 * @return
	 */
	private static boolean isValidInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		}
		catch(NumberFormatException nfe) {
			return false;
		}
	}

	
	private static boolean isValidDate(String month, String day, String year, 
        boolean dayRequired) 
    {
		// A blank date may or may not be valid - up the the BO
		if (StringUtils.isBlank(month) && StringUtils.isBlank(day) && 
            StringUtils.isBlank(year)) 
        {
			return true;
		}
		
		try {
			int iMonth = Integer.parseInt(month) - 1;
			int iDay;
			// Only default the day if it is not required AND blank.
			// In any other case try to parse it
			if (!dayRequired && StringUtils.isBlank(day)) {
				iDay = 1;
			} else {
				iDay = Integer.parseInt(day);
			}
			int iYear = Integer.parseInt(year);
			
			Calendar cal = GregorianCalendar.getInstance();
			cal.clear();
			cal.setLenient(false);
			cal.set(Calendar.MONTH, iMonth);
			cal.set(Calendar.DAY_OF_MONTH, iDay);
			cal.set(Calendar.YEAR, iYear);
			
			// This will cause an exception if the day is invalid 
            // (e.g. 2/29/2001)
			cal.getTime();
			
			return true;
		} catch(Exception ex) {
			return false;
		}
	}
	
	private static boolean isValidTimestamp(String month, String day, 
        String year, String hours, String minutes, boolean dayRequired) 
    {
		try {
			boolean validDate = isValidDate(month, day, year, dayRequired);
			// TODO Handle time portion
			return validDate;
		} catch(Exception pe) {
			return false;
		}
	}

	/**
	 * Anserws whether a string can be converted into a long
	 * @param value
	 * @return
	 */
	
	private static boolean isValidLong(String value) {
		try {
			Long.parseLong(value);
			return true;
		}
		catch(NumberFormatException nfe) {
			return false;
		}
	}

	
	/**
	 * Anserws whether a string can be converted into a decimal
	 * @param value
	 * @return
	 */
	private static boolean isValidDecimal(String value) {
		try {
			new BigDecimal(value);
			return true;
		}
		catch(NumberFormatException nfe) {
			return false;
		}
	}

}
