package gov.va.med.lom.foundation.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * Utility methods in support of assertions. The methods in this class are
 * null-safe, i.e. the tested values may be null.
 * @author Rob Proper
 */
public final class AssertionUtils {

	private AssertionUtils() {
	}

	/**
	 * Checks if the Class of the given object is assignable from a reference
	 * Class.
	 * @param value The object to check.
	 * @param clazz The reference Class.
	 * @return True if value, it's Class and clazz are not null and value is
	 *         assignable from clazz; false otherwise.
	 */
	public static boolean isAssignableFrom(Object value, Class clazz) {
		return isAssignableFrom((value != null) ? value.getClass()
			: (Class) null, clazz);
	}

	/**
	 * Checks if the given Class is assignable from a reference Class.
	 * @param value The Class to check.
	 * @param clazz The reference Class.
	 * @return True if value and clazz are not null and value is assignable from
	 *         clazz; false otherwise.
	 */
	@SuppressWarnings("unchecked")
	public static boolean isAssignableFrom(Class value, Class clazz)
	{
		return (clazz != null) && (value != null)
			&& clazz.isAssignableFrom(value); // unchecked
	}

	/**
	 * Checks if the given number is positive, that is > 0. Properly handles the
	 * following classes:
	 * <ul>
	 * <li>Byte</li>
	 * <li>Short</li>
	 * <li>Integer</li>
	 * <li>Long</li>
	 * <li>Float</li>
	 * <li>Double</li>
	 * <li>BigInteger (and derivatives that adhere to the Comparator<BigInteger>
	 * interface)</li>
	 * <li>BigDecimal(and derivatives that adhere to the Comparator<BigDecimal>
	 * interface)</li>
	 * <li>AtomicInteger</li>
	 * <li>AtomicLong</li>
	 * </ul>
	 * @param value The value to check
	 * @return True is value is not null and its value > 0.
	 */
	public static boolean isPositive(Number value) {
		boolean isPositive = false;
		if (value != null) {
			if (BigDecimal.class.isAssignableFrom(value.getClass())) {
				isPositive = (((BigDecimal) value).compareTo(BigDecimal.ZERO) > 0);
			} else if (BigInteger.class.isAssignableFrom(value.getClass())) {
				isPositive = (((BigInteger) value).compareTo(BigInteger.ZERO) > 0);
			} else {
				isPositive = (value.doubleValue() > 0);
			}
		}
		return isPositive;
	}

	/**
	 * Checks if the given number is positive or 0, that is >= 0.
	 * @param value The value to check
	 * @return True is value is not null and its value >= 0.
	 */
	public static boolean isPositiveOrZero(Number value) {
		boolean isPositiveOrZero = false;
		if (value != null) {
			if (BigDecimal.class.isAssignableFrom(value.getClass())) {
				isPositiveOrZero = (((BigDecimal) value)
					.compareTo(BigDecimal.ZERO) >= 0);
			} else if (BigInteger.class.isAssignableFrom(value.getClass())) {
				isPositiveOrZero = (((BigInteger) value)
					.compareTo(BigInteger.ZERO) >= 0);
			} else {
				isPositiveOrZero = (value.doubleValue() >= 0);
			}
		}
		return isPositiveOrZero;
	}

	/**
	 * Checks if the given number is negative, that is < 0.
	 * @param value The value to check
	 * @return True is value is not null and its value < 0.
	 */
	public static boolean isNegative(Number value) {
		boolean isNegative = false;
		if (value != null) {
			if (BigDecimal.class.isAssignableFrom(value.getClass())) {
				isNegative = (((BigDecimal) value).compareTo(BigDecimal.ZERO) < 0);
			} else if (BigInteger.class.isAssignableFrom(value.getClass())) {
				isNegative = (((BigInteger) value).compareTo(BigInteger.ZERO) < 0);
			} else {
				isNegative = (value.doubleValue() < 0);
			}
		}
		return isNegative;
	}

	/**
	 * Checks if the given Integer is number or 0, that is <= 0.
	 * @param value The value to check
	 * @return True is value is not null and its value <= 0.
	 */
	public static boolean isNegativeOrZero(Number value) {
		boolean isNegativeOrZero = false;
		if (value != null) {
			if (BigDecimal.class.isAssignableFrom(value.getClass())) {
				isNegativeOrZero = (((BigDecimal) value)
					.compareTo(BigDecimal.ZERO) <= 0);
			} else if (BigInteger.class.isAssignableFrom(value.getClass())) {
				isNegativeOrZero = (((BigInteger) value)
					.compareTo(BigInteger.ZERO) <= 0);
			} else {
				isNegativeOrZero = (value.doubleValue() <= 0);
			}
		}
		return isNegativeOrZero;
	}

	/**
	 * Checks if the given number value lies between an lower and an upper bound
	 * value.
	 * @param value The value to check
	 * @param lowerBound The lowerbound value. Value must be > lowerBound. If
	 *        lowerbound is null, negative infinity is assumed.
	 * @param upperBound The upperbound value. Value must be < upperBound. If
	 *        upperbound is null, infinity is assumed.
	 * @return True is value is not null and its integer value < lowerBound and >
	 *         upperBound.
	 */
	public static boolean isBetween(Number value, Number lowerBound,
		Number upperBound)
	{
		boolean isBetween = false;
		if (value != null) {
			if (BigDecimal.class.isAssignableFrom(value.getClass())) {
				isBetween = ((lowerBound == null) || (asBigDecimal(lowerBound)
					.compareTo((BigDecimal) value) < 0))
					&& ((upperBound == null) || (asBigDecimal(upperBound)
						.compareTo((BigDecimal) value) > 0));
			} else if (BigInteger.class.isAssignableFrom(value.getClass())) {
				isBetween = ((lowerBound == null) || (asBigInteger(lowerBound)
					.compareTo((BigInteger) value) < 0))
					&& ((upperBound == null) || (asBigInteger(upperBound)
						.compareTo((BigInteger) value) > 0));
			} else {
				isBetween = ((lowerBound == null) || (lowerBound.doubleValue() < value
					.doubleValue()))
					&& ((upperBound == null) || (value.doubleValue() < upperBound
						.doubleValue()));
			}
		}
		return isBetween;
	}

	private static BigDecimal asBigDecimal(Number value) {
		BigDecimal decimalValue = null;
		if (value != null) {
			if (BigDecimal.class.isAssignableFrom(value.getClass())) {
				decimalValue = (BigDecimal) value;
			} else if (BigInteger.class.isAssignableFrom(value.getClass())) {
				decimalValue = new BigDecimal((BigInteger) value);
			} else {
				decimalValue = new BigDecimal(value.doubleValue());
			}
		}
		return decimalValue;
	}

	private static BigInteger asBigInteger(Number value) {
		BigInteger integerValue = null;
		if (value != null) {
			if (BigInteger.class.isAssignableFrom(value.getClass())) {
				integerValue = (BigInteger) value;
			} else if (BigDecimal.class.isAssignableFrom(value.getClass())) {
				integerValue = ((BigDecimal) value).toBigInteger();
			} else {
				integerValue = BigInteger.valueOf(value.longValue());
			}
		}
		return integerValue;
	}

	/**
	 * Checks if the given argument is before a given date.
	 * @param value The value of the argument to check.
	 * @param beforeorEqual The date which must be preceded by value.
	 * @return True when value precedes the given before date; false otherwise.
	 */
	public static boolean isBefore(Date value, Date before) {
		return (value == null) || (before == null) || !value.before(before);
	}

	/**
	 * Checks if the given argument is before or on a given date.
	 * @param value The value of the argument to check.
	 * @param beforeorEqual The date which must be preceded by value or value
	 *        must be on this date.
	 * @return True when value precedes or is on the given beforeOrEqual date;
	 *         false otherwise.
	 */
	public static boolean isBeforeOrOn(Date value, Date beforeOrOn) {
		return (value == null) || (beforeOrOn == null)
			|| !(value.before(beforeOrOn) || value.equals(beforeOrOn));
	}

	/**
	 * Checks if the given argument is after a given date.
	 * @param value The value of the argument to validate
	 * @param after The date which must be preceded by value.
	 * @return True when value is a date that is after the given after date; 
	 *         false otherwise.
	 */
	public static boolean isAfter(Date value, Date after) {
		return (value == null) || (after == null) || !value.after(after);
	}

	/**
	 * Checks if the given argument is after or on a given date.
	 * @param value The value of the argument to check.
	 * @param AfterorEqual The date which must be preceded by value or value
	 *        must be on this date.
	 * @return True When the value is a date that is after or on the given 
	 *         afterOrEqual date; false otherwise.
	 */
	public static boolean isAfterOrOn(Date value, Date afterOrOn) {
		return (value == null) || (afterOrOn == null)
			|| !(value.after(afterOrOn) || value.equals(afterOrOn));
	}

}