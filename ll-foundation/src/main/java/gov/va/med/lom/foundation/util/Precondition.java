package gov.va.med.lom.foundation.util;


import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;

/**
 * Utility methods to verify preconditions of non-private class method.
 * The methods of this class throw an IllegalArgumentException,
 * when the asserted condition is not met. 
 * This supports a programming-by-contract style.
 * </p><p>
 * {@link http://en.wikipedia.org/wiki/Programming_by_contract "Wikepedia"}:
 * <quote>
 *   the whole idea is that code should "fail hard", 
 *   with the contract verification being the safety net. 
 *   DBC's "fail hard" property makes debugging for-contract behavior 
 *   much easier because the intended behaviour of each routine 
 *   is clearly specified.
 *   The contract conditions should never be violated in program execution.
 * </quote>
 * </p><p>
 * Example:
 * </p>
 * <pre>
 *       public boolean doTask(MyClass myObject) {
 *           Precondition.assertNotNull("myObject", myObject);
 *           ...
 *       }
 * </pre>
 * <p>
 * Furthermore, this class reduces the verbosity of precondition assertion and 
 * more clearly expresses the intent.
 * Compare the example to:
 * </p>
 * <pre>
 *       public boolean doTask(MyClass myObject) {
 *       	if (myObject == null) {
 *       		throw IllegalArgumentException("myObject: "
 *       			+ " Expected a non-null value"); 
 *       	}
 *          ...
 *       }
 * </pre> 
 * <p>
 * For invariants and post-conditions asserts should be used, 
 * as the programmer has control over these conditions.
 * Also, for private methods asserts should be used, as the programmer has
 * control over these conditions.  
 * </p>
 * @author Rob Proper
 */
public final class Precondition {
	// Note: It may be preferable to use a 3rd party product that uses a 
	// Java 5 annotation approach, to not cluter the body
	// of the methods that need to enforce the precondition with 
	// preconditions, as these are not the primary purpose of the method
	// but can still be verbose.

	private Precondition() {
		// Methods of this utility class are all static and should be 
		// referenced through the class
	}

   /**
	 * Asserts that the given condition is true.
	 * @param message The message to display when the condition is false.
	 * @param condition The condition to check.
	 * @throws IllegalArgumentException When the condition is false.
	 */
	public static void assertTrue(boolean condition, String message)  
		throws IllegalArgumentException 
	{
		if (!condition) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Asserts that the given condition is false.
	 * @param condition The condition to check
	 * @param message The message to display when the condition is true.
	 * @throws IllegalArgumentException When the condition is true.
	 */
	public static void assertFalse(boolean condition, String message) 
		throws IllegalArgumentException 
	{
		if (condition) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Asserts that the given value is not null.
	 * @param argument The name of the argument.
	 * @param value The value of the argument to check.
	 * @throws IllegalArgumentException if value is null.
	 */
	public static void assertNotNull(String argument, Object value)
		throws IllegalArgumentException
	{
		if (value == null) {
			fail(argument, "a value", "null");
		}
	}

	/**
	 * Asserts that the given value is not blank (that is, not null or just
	 * containing whitespace).
	 * @param argument The name of the argument.
	 * @param value The value of the argument to check.
	 * @throws IllegalArgumentException if value is blank.
	 */
	public static void assertNotBlank(String argument, String value) 
		throws IllegalArgumentException
	{
		if (StringUtils.isBlank(value)) {
			fail(argument, "a non-blank string", value);
		}
	}

   /**
	 * Asserts that the given argument is not null nor an empty string.
	 * @param argument The name of the argument to validate
	 * @param value The value of the argument to validate
	 * @throws IllegalArgumentException When the value is null or empty
	 */
	public static void assertNotEmpty(String argument, String value) 
		throws IllegalArgumentException
	{
		if (StringUtils.isEmpty(value)) {
			fail(argument, "a non-empty string", value);
		}
	}

	/**
	 * Asserts that the given argument is a string of a given length.
	 * @param argument The name of the argument to validate.
	 * @param value The value of the argument to validate.
	 * @param length The required length of the string.
	 * @throws IllegalArgumentException When the value is null, or a string of
	 *         length unequal to length.
	 */
	public static void assertLength(String argument, String value, int length) {
		assertNotNull(argument, value);
		if (value.length() != length) {
			fail(argument, "a string of length " + length, value + " (length:" 
				+ value.length() + ")");
		}
	}

	/**
	 * Asserts that the given argument is a string of at least a given length.
	 * @param argument The name of the argument to validate.
	 * @param value The value of the argument to check.
	 * @param length The minimal required length
	 * @throws IllegalArgumentException When the value is null or a string of
	 *         length less than length.
	 */
	public static void assertMinLength(String argument, String value, 
		int length) throws IllegalArgumentException
	{
		assertNotEmpty(argument, value);
		if (value.length() < length) {
			fail(argument, "a string of at least length " + length, value + 
				" (length: " + value.length() + ")");
		}
	}

	/**
	 * Asserts that the given argument is a string of at least a given length.
	 * @param argument The name of the argument to check.
	 * @param value The value of the argument to check.
	 * @param length The maximal allowed length.
	 * @throws IllegalArgumentException When the value is null or a string 
	 *         which length exceeds length.
	 */
	public static void assertMaxLength(String argument, String value, 
		int length) throws IllegalArgumentException
	{
		assertNotEmpty(argument, value);
		if (value.length() > length) {
			fail(argument, "a string of at most length " + length, value 
				+ " (length:" + value.length() + ")");
		}
	}

	/**
	 * Asserts that two given objects are equal. 
	 * Unless both objects are null,
	 * the equals() method of value is applied to determine equality.
	 * Two null objects are considered equal.
	 * @param argument The name of the argument.
	 * @param value The value of the argument to check.
	 * @param equalTo The reference object, to which value is compared.
	 * @throws IllegalArgumentException If values are not equals.
	 */
	public static void assertEquals(String argument, Object value, 
		Object equalTo) throws IllegalArgumentException
	{
		if (((value == null) && (equalTo != null))
			|| ((value != null) && (equalTo == null)) 
			|| ((value != null) && !value.equals(equalTo)))
		{
			fail(argument, "an instance equal to '" + equalTo + "'", value);
		}
	}

	/**
	 * Asserts that the given object is assignable from a given Class.
	 * @param argument The name of the argument.
	 * @param value The value of the argument to check.
	 * @param clazz The reference Class, to which value is checked.
	 * @throws IllegalArgumentException if value's Class is not assignable from
	 *         clazz.
	 */
	public static void assertAssignableFrom(String argument, Object value,
		Class clazz) throws IllegalArgumentException
	{
		if (!AssertionUtils.isAssignableFrom(value, clazz)) {
			fail(argument, "a value of " + clazz, ((value != null) 
				? value.getClass() : "null"));
		}
	}

	/**
	 * Asserts that the given Class is assignable from a given Class.
	 * @param argument The name of the argument.
	 * @param value The value of the argument to check.
	 * @param clazz The reference Class, to which value is checked.
	 * @throws IllegalArgumentException if value is not assignable from clazz.
	 */
	public static void assertAssignableFrom(String argument, Class value,
		Class clazz) throws IllegalArgumentException
	{
		if (!AssertionUtils.isAssignableFrom(value, clazz)) {
			fail(argument, clazz, value);
		}
	}

	/**
	 * Asserts that the given argument is a positive number.
	 * Autoboxing allows to primitive numbers as well. 
	 * @param argument The name of the argument.
	 * @param value The value of the argument to check.  
	 * @throws IllegalArgumentException If the value is null or not a positive 
	 *         number.
	 */
	public static void assertPositive(String argument, Number value)
		throws IllegalArgumentException
	{
		if (!AssertionUtils.isPositive(value)) { 
			fail(argument, "a positive number", value);
		}
	}

	/**
	 * Asserts that the given argument is a positive number or 0.
	 * Autoboxing allows to pass primitive numbers as well. 
	 * @param argument The name of the argument.
	 * @param value The value of the argument to check.  
	 * @throws IllegalArgumentException If the value is null, not a positive 
	 *         number or 0.
	 */
	public static void assertPositiveOrZero(String argument, 
		Number value) throws IllegalArgumentException
	{
		if (!AssertionUtils.isPositiveOrZero(value)) { 
			fail(argument, "a positive number or 0", value);
		}
	}

	/**
	 * Asserts that the given argument is a negative number.
	 * Autoboxing allows to pass primitive numbers as well. 
	 * @param argument The name of the argument.
	 * @param value The value of the argument to check.  
	 * @throws IllegalArgumentException If the value is null or not a negative 
	 *         number.
	 */
	public static void assertNegative(String argument, Number value)
		throws IllegalArgumentException
	{
		if (!AssertionUtils.isNegative(value)) { 
			fail(argument, "a negative integer", value);
		}
	}

	/**
	 * Asserts that the given argument is a negative integer or 0.
	 * Autoboxing allows to pass primitive number as well. 
	 * @param argument The name of the argument.
	 * @param value The value of the argument to check.  
	 * @throws IllegalArgumentException If the value is null, not a negative 
	 *         number or 0.
	 */
	public static void assertNegativeOrZero(String argument, 
		Number value) throws IllegalArgumentException
	{
		if (!AssertionUtils.isNegativeOrZero(value)) { 
			fail(argument, "a negative integer or 0", value);
		}
	}

	/**
	 * Asserts that the given number value lies between an lower and 
	 * an upper bound value.
	 * @param argument The name of the argument to check
	 * @param value The value to check
	 * @param lowerBound The lowerbound value. Value must be > lowerBound.
	 *        If lowerbound is null, negative infinity is assumed.
	 * @param upperBound The upperbound value. Value must be < upperBound.
	 *        If upperbound is null, infinity is assumed.
	 * @throws IllegalArgumentException When the value > 0
	 */
	public static void assertBetween(String argument, Number value, 
		Number lowerBound, Number upperBound)
	{
		if (!AssertionUtils.isBetween(value, lowerBound, upperBound)) {
			fail(argument, "a value between " + ((lowerBound != null) ?
				lowerBound.toString() : "negative infinity") + " and " 
				+ ((upperBound != null) ? upperBound.toString() 
				: "(positive) infinity"), value);
		}
	}

   /**
	 * Asserts that the given argument is before a given date.
	 * @param argument The name of the argument to validate
	 * @param value The value of the argument to validate
	 * @param before The date which must be preceded by value.
	 * @throws IllegalArgumentException When the value is not a date that
	 *         precedes the given before date. 
	 */
	public static void assertBefore(String argument, Date value, Date before) {
		if (AssertionUtils.isBefore(value, before)) {
			fail(argument, "a date before " + before, value);
		}
	}

	/**
	 * Asserts that the given argument is before or on a given date.
	 * @param argument The name of the argument to check
	 * @param value The value of the argument to check
	 * @param beforeorEqual The date which must be preceded by value or value
	 *        must be on this date
	 * @throws IllegalArgumentException When the value is not a date that
	 *         precedes or is on the given beforeOrEqual date. 
	 */
	public static void assertBeforeOrOn(String argument, Date value,
		Date beforeOrOn)
	{
		if (AssertionUtils.isBeforeOrOn(value, beforeOrOn)) {
			fail(argument, "a date before or on " + beforeOrOn, value);
		}
	}   

   /**
	 * Asserts that the given argument is after a given date.
	 * @param argument The name of the argument to validate
	 * @param value The value of the argument to validate
	 * @param after The date which must be preceded by value.
	 * @throws IllegalArgumentException When the value is not a date that
	 *         precedes the given after date. 
	 */
	public static void assertAfter(String argument, Date value, Date after) 
		throws IllegalArgumentException
	{
		if (AssertionUtils.isAfter(value, after)) {
			fail(argument, "a date after " + after, value);
		}
	}

	/**
	 * Asserts that the given argument is After or on a given date.
	 * @param argument The name of the argument to check
	 * @param value The value of the argument to check
	 * @param AfterorEqual The date which must be preceded by value or value
	 *        must be on this date
	 * @throws IllegalArgumentException When the value is not a date that
	 *         precedes or is on the given afterOrEqual date. When either the
	 *         value or the afterOrEqual date is null no exception is thrown.
	 */
	public static void assertAfterOrOn(String argument, Date value,
		Date afterOrOn) throws IllegalArgumentException
	{
		if (AssertionUtils.isAfterOrOn(value, afterOrOn)) {
			fail(argument, "a date after or on " + afterOrOn, value);
		}
	}   
	

   /**
	 * Asserts that the given argument is not null and its elements are
	 * not null.
	 * @param name The name of the argument to check.
	 * @param values The value of the argument to check.
	 * @throws IllegalArgumentException When the value is null or its elements
	 *         are null.
	 */
	public static void assertNotNull(String name, Object[] values) {
		assertNotNull(name, (Object) values);
		final int LENGTH = values.length;
		for (int i = 0; i < LENGTH; i++) {
			if (values[i] == null) {
				fail(name, "Expected a non-null value for " + i + "-th element",
					values);
			}
		}
	}

	/**
	 * Asserts that the given collection is not null and its elements are not
	 * null.
	 * @param name The name of the argument to check.
	 * @param value The value of the argument to check.
	 * @throws IllegalArgumentException When the collection is null or contain
	 *         nulls.
	 */
	public static void assertNotNull(String name, Collection values)
		throws IllegalArgumentException
	{
		assertNotNull(name, (Object) values);
		int n = 0;
		for (Iterator i = values.iterator(); i.hasNext(); n++) {
			if (i.next() == null) {
				fail(name, "Expected a non-null value for " + n + "-th element",
					values);
			}
		}
	}
	
	/**
	 * Asserts that the given collection is not null, its elements are not null
	 * and it contains at least one element.
	 * @param name The name of the argument to check.
	 * @param value The value of the argument to check.
	 * @throws IllegalArgumentException When the collection is null, empty or
	 *         contains nulls.
	 */
	public static void assertNotEmpty(String name, Collection values) 
		throws IllegalArgumentException
	{
		assertNotNull(name, values);
		if (values.size() == 0) {
			fail(name, "a non-empty collection", "Empty collection");
		}
	}
	
	/**
	 * Precondition is not statisfied.
	 * @param argument The name of the argument
	 * @param expected The expected value of the argument
	 * @param actual The actual value of the argument.
	 * @throws IllegalArgumentException Always
	 */
	public static void fail(String argument, Object expected, Object actual)
		throws IllegalArgumentException
	{
		fail(argument, "Expected " + expected + ", but got " + actual);
	}

	/**
	 * Precondition is not statisfied.
	 * @param argument The name of the argument
	 * @param expected The expected value of the argument
	 * @param actual The actual value of the argument.
	 * @throws IllegalArgumentException Always
	 */
	public static void fail(String argument, Object expected, Object actual,
		String description) throws IllegalArgumentException
	{
		fail(argument, "Expected " + expected + ", but got " + actual + ". "
			+ description);
	}

	/**
	 * Precondition is not statisfied.
	 * @param argument The name of the argument
	 * @param description The description of the condition that is not met.
	 * @throws IllegalArgumentException Always
	 */
	public static void fail(String argument, String description)
		throws IllegalArgumentException
	{
		throw new IllegalArgumentException(
			((argument != null) ? "Argument '" + argument + "': " : "") 
			+ description);
	}

	/**
	 * Precondition is not statisfied.
	 * @param description The description of the condition that is not met.
	 * @throws IllegalArgumentException Always
	 */
	public static void fail(String description)
		throws IllegalArgumentException
	{
		throw new IllegalArgumentException(description);
	}

}
