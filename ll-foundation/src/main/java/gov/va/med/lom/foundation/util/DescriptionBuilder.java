package gov.va.med.lom.foundation.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Collator;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Provides the capability to describe objects contents.
 * This class can safely handle circular references, as it will only fully
 * describe each instance once, and subsequently describe only its class and 
 * hashcode.
 * @author Rob Proper
 */
public class DescriptionBuilder implements Cloneable {
	
	private static final Log LOG = LogFactory.getLog(DescriptionBuilder.class);
	
	private static final Properties DEFAULT_PROPERTIES = new Properties();

	private static ArrayList<Appender> DEFAULT_APPENDERS = createAppenders();

	private static final String UNKNOWN = "<?>";
	
	private final List<Appender> appenders = getDefaultAppenders();
	private final StringBuilder builder = new StringBuilder();
	private final Properties properties;
	private String indentation = "";
	private boolean indent = true;
	
	// Must remember which (non-collection) objects have been described
	// already, such that cycles are prevented
	private final Set<Object> described = new HashSet<Object>();  
	
	private static ArrayList<Appender> createAppenders() {
		ArrayList<Appender> appenders = new ArrayList<Appender>();
		// Note these appenders are progressively more complex
		// For that reason the Number and Boolean appenders have been added
		// to increase performance for the simpler data types/classes.
		appenders.add(new StringAppender());
		appenders.add(new NumberAppender());
		appenders.add(new DateAppender());
		appenders.add(new ClassAppender());
		appenders.add(new BooleanAppender());
		appenders.add(new CharacterAppender());
		appenders.add(new ArrayAppender());
		appenders.add(new ListAppender());
		appenders.add(new SetAppender());
		appenders.add(new MapAppender());
		appenders.add(new DescribeableAppender());
		appenders.add(new BeanAppender());
		
		return appenders;
	}

	@SuppressWarnings("unchecked")
	private static final List<Appender> getDefaultAppenders() {
		return (List<Appender>) DEFAULT_APPENDERS.clone(); // unchecked
	}
	
	/**
	 * Convenience method that uses a standard DescriptionBuilder to build 
	 * a description of the given object.
	 * <pre>
	 *     return new DescriptionBuilder().append(value).toString();
	 * </pre>
	 * @param value The object to describe.
	 */
	public static String describe(Object value) {
		return new DescriptionBuilder().append(value).toString();
	}

	/**
	 * Creates a standard DescriptionBuilder with default 
	 * DescriptionBuilder.Properties.
	 *
	 */
	public DescriptionBuilder() {
		this(null);
	}

	public DescriptionBuilder(Properties properties) {
		this.properties = (properties != null) ? properties 
			: DEFAULT_PROPERTIES;
	}
	
	protected void addAppender(Appender appender) {
		Precondition.assertNotNull("appender", appender);
		appenders.add(0, appender);
	}

    protected void addAppenders(List<Appender> appenders) {
        Precondition.assertNotEmpty("appenders", appenders);
        for(Appender appender: appenders) {
            addAppender(appender);
        }
    }

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		DescriptionBuilder clone = new DescriptionBuilder(properties);
		clone.described.addAll(this.described);
		clone.indent = this.indent;
		return clone;
	}
	
	/**
	 * Forces the output to be indented. 
	 */
	public void indent() {
		indentation += properties.getIndentation();
	}
	
	/**
	 * Forces the output to be unindented. 
	 */
	public void unindent() {
		if (StringUtils.isBlank(indentation)) {
			Precondition.fail("unindent does not match indent");
		}
		indentation = StringUtils.stripEnd(indentation, properties.
			getIndentation());
	}
	
	/**
	 * Describes a given object. 
	 * The method recognizes the following types:
	 * <ul>
	 * <li>{@link java.lang.String}s are put between single quotes</li>
	 * <li>{@link java.lang.Number}s use toString()</li>
	 * <li>{@link java.lang.Boolean}s use toString()</li>
	 * <li>{@link java.lang.Character}s use toString()</li>
	 * <li>{@link java.util.Date}s are formatted according to the set 
	 * {@link Properties#getDateFormat()} format</li>
	 * <li>Classes are shown as set by 
	 * {@link Properties#useFullClassName()}</li>
	 * </ul>
	 * <li>arrays (both primitive and Object)</li>
	 * <li>{@link java.util.List}s</li>
	 * <li>{@link java.util.Map}s are sorted by key</li>
	 * <li>{@link java.util.Set}s are sorted by value</li>
	 * <li>{@link Describeable} uses the describe() method</li>
	 * <li>JavaBeans uses {@link java.beans.BeanInfo} to find the properties
	 * </li>
	 * </ul>
	 * For any other types the method simply uses {@link java.lang#toString()}.
	 * @param value The object to describe.
	 * @return The description
	 */
	public DescriptionBuilder append(Object value) {
		if (value == null) {
			unknown();
			return this;
		}
		
		for (Appender describer: appenders) {
			if (describer.canDescribe(this, value)) {
				boolean headerOnly = false;
				if (!describer.isPrimitive()) { 
					if (hasBeenDescribed(value)) {
						headerOnly = true;
					} else {
						markAsDescribed(value);
					}
				}
				if (headerOnly) {
					header(value);
				} else {
					describer.append(this, value);
				}
				return this;
			}
		}
		builder.append(value.toString());
		
		return this;
	}
	
	public DescriptionBuilder unknown() {
		appendString(properties.getNullString());
		return this;
	}

	public DescriptionBuilder newline() {
		appendString(properties.getNewLine());
		return this;
	}

    public DescriptionBuilder separate() {
        appendString(properties.getSeparator());
        return this;
    }
    
	public DescriptionBuilder openCollection() {
		appendString(properties.getCollectionOpen());
		return this;
	}


    public DescriptionBuilder separateProperty() {
        appendString(properties.getPropertySeparator());
        return this;
    }
    
    public DescriptionBuilder separatePropertyValue() {
        appendString(properties.getPropertyValueSeparator());
        return this;
    }
    
	public DescriptionBuilder closeCollection() {
		appendString(properties.getCollectionClose());
		return this;
	}

    public DescriptionBuilder openPropertyList() {
        appendString(properties.getPropertyListOpen());
        return this;
    }

    public DescriptionBuilder closePropertyList() {
        appendString(properties.getPropertyListClose());
        return this;
    }


	/**
	 * Describe the generic object properties, such as the unqualified class
	 * name and the hash code. The output will look as follows: <code>
	 *       <unqualified class name>@<hash code>
	 * @param object The object to create the description header for.
	 */
	public DescriptionBuilder header(Object object) {
		Precondition.assertNotNull("object", object);
		builder.append(StringUtils.substringAfterLast(object.getClass().
			getName(), "."));
		builder.append("@");
		builder.append(Integer.toHexString(object.hashCode()));
		return this;
	}
	
	@SuppressWarnings("unqualified-field-access")
	public DescriptionBuilder appendString(String value) {
		if (value == null) {
			unknown();
		} else {
			builder.append(value);
		}
		return this;
	}

	

	public DescriptionBuilder appendProperty(String name, Object value) {
	    return appendProperty(name, value, false);   
    }
    
    public DescriptionBuilder appendProperty(String name, Object value, 
        boolean isFirst) 
    {
		if (indent) {
			builder.append(indentation);
		}
        if (!isFirst) {
            separateProperty();
        }
		if (!StringUtils.isEmpty(name)) {
			appendString(StringUtils.capitalize(name));
            separatePropertyValue();
		}
		append(value);
		return this;
	}
	

	public String toString() {
		return builder.toString();
		
	}


	protected final boolean hasBeenDescribed(Object value) {
		return described.contains(value);
	}
	
	protected final void markAsDescribed(Object value) {
		if (value != null) {
			described.add(value);
		}
	}


	/**
	 * Properties that can be passed to a DescriptionBuilder.
	 * The property define the values used by the DescriptionBuilder.
	 * @author rproper
	 *
	 */
	public static final class Properties {
		
		/**
		 * Default string used to designate a null value.
		 */
		public static final String NULL_STRING = "<null>";

		/**
		 * Default collection element separator string.
		 */
		public static final String SEPARATOR = ",";

		/**
		 * String used to show the start of a collection.
		 */
		public static final String COLLECTION_OPEN = "{";

		/**
		 * String used to show the end of a collection.
		 */
		public static final String COLLECTION_CLOSE = "}";

        /**
         * Default propery separator string.
         */
        public static final String PROPERTY_SEPARATOR = "; ";

        /**
         * Default property-value separator string.
         */
        public static final String PROPERTY_VALUE_SEPARATOR = ": ";

        /**
         * String used to show the start of a list of properties.
         */
        public static final String PROPERTY_LIST_OPEN = "[";

        /**
         * String used to show the end of a list of properties.
         */
        public static final String PROPERTY_LIST_CLOSE = "]";

		/**
		 * String used for indentation
		 */
		public static final String INDENTATION = "\t";

		/**
		 * String used for indentation
		 */
		public static final String NEWLINE = defaultNewline();

		// The describer settings
		private String separator = SEPARATOR;

        private String propertySeparator = PROPERTY_SEPARATOR;

        private String propertyValueSeparator = PROPERTY_VALUE_SEPARATOR;

		private String nullString = NULL_STRING;

		private String collectionOpen = COLLECTION_OPEN;

		private String collectionClose = COLLECTION_CLOSE;

        private String propertyListOpen = PROPERTY_LIST_OPEN;

        private String propertyListClose = PROPERTY_LIST_CLOSE;

		private DateFormat dateFormat = null;

		private boolean useFullClassName = true;
		
		private String indentation = INDENTATION;
		
		private String newLine = NEWLINE;
		
		private boolean verbose = false;
		
		private static String defaultNewline() {
			String newline = System.getProperty("line.separator");
			return StringUtils.isBlank(newline) ? "\n" : newline;
		}	

		/**
		 * @return Returns the newLine.
		 */
		public String getNewLine() {
			return newLine;
		}

		/**
		 * @param newLine The newLine to set.
		 */
		public void setNewLine(String newLine) {
			this.newLine = newLine;
		}

		public Properties() {
			
		}
		
		/**
		 * Sets the string that denotes a null value
		 * @param nullString The string designating a null value
		 */
		public void setNullString(String nullString) {
			this.nullString = nullString;
		}

		/**
		 * The string displayed when a null value is encountered.
		 * @return The null-string.
		 */
		public String getNullString() {
			return nullString;
		}

		/**
		 * Sets the separator string to use in descriptions of collections.
		 * @param separator The separator string
		 */
		public void setSeparator(String separator) {
			this.separator = separator;
		}

		/**
		 * The separator string used in descriptions of collections.
		 * @return
		 */
		public String getSeparator() {
			return separator;
		}

		/**
		 * Sets the collectionOpen string to use in descriptions of collections.
		 * @param collectionOpen The collectionOpen string
		 */
		public void setCollectionOpen(String collectionOpen) {
			this.collectionOpen = collectionOpen;
		}

		/**
		 * The collectionOpen string used in descriptions of collections.
		 * @return
		 */
		public String getCollectionOpen() {
			return collectionOpen;
		}

		/**
		 * Sets the collectionClose string to use in descriptions of collections.
		 * @param collectionClose The collectionClose string
		 */
		public void setCollectionClose(String collectionClose) {
			this.collectionClose = collectionClose;
		}

		/**
		 * The collectionClose string used in descriptions of collections.
		 * @return
		 */
		public String getCollectionClose() {
			return collectionClose;
		}

        /**
         * Sets the separator string to use in property lists.
         * @param separator The separator string
         */
        public void setPropertySeparator(String separator) {
            this.propertySeparator = separator;
        }

        /**
         * The separator string used in property lists.
         * @return
         */
        public String getPropertySeparator() {
            return propertySeparator;
        }

        /**
         * Sets the separator string used to separate a property name and value. 
         * @param separator The separator string
         */
        public void setPropertyValueSeparator(String separator) {
            this.propertyValueSeparator = separator;
        }

        /**
         * The separator string used to separate a property name and value.
         * @return
         */
        public String getPropertyValueSeparator() {
            return propertyValueSeparator;
        }

        /**
         * Sets the propertyListOpen string to use in descriptions of propertyLists.
         * @param propertyListOpen The propertyListOpen string
         */
        public void setPropertyListOpen(String propertyListOpen) {
            this.propertyListOpen = propertyListOpen;
        }

        /**
         * The propertyListOpen string used in descriptions of propertyLists.
         * @return
         */
        public String getPropertyListOpen() {
            return propertyListOpen;
        }

        /**
         * Sets the propertyListClose string to use in descriptions of propertyLists.
         * @param propertyListClose The propertyListClose string
         */
        public void setPropertyListClose(String propertyListClose) {
            this.propertyListClose = propertyListClose;
        }

        /**
         * The propertyListClose string used in descriptions of propertyLists.
         * @return
         */
        public String getPropertyListClose() {
            return propertyListClose;
        }

        
        
		/**
		 * Sets the date format to use when describing dates. It is applied to any
		 * instance assignable from {@link java.util.Date}
		 * @param dateFormat The format
		 */
		public void setDateFormat(DateFormat dateFormat) {
			this.dateFormat = dateFormat;
		}

		/**
		 * The date format to use when describing dates. It is applied to any
		 * instance assignable from {@link java.util.Date}
		 * @return The date format
		 */
		public DateFormat getDateFormat() {
			return dateFormat;
		}

		/**
		 * Changes the manner in which class names are displayed
		 * @param useFullClassName Denotes whether class names are displayed with
		 *        the full package name (true) or only the class name is displayed
		 *        (false).
		 */
		public void setUseFullClassName(boolean useFullClassName) {
			this.useFullClassName = useFullClassName;
		}

		/**
		 * Are class names displayed in full.
		 * @return True if class names are displayed with the full package name;
		 *         false if only the class name is displayed.
		 */
		public boolean useFullClassName() {
			return useFullClassName;
		}

		/**
		 * @return Returns the indentation.
		 */
		public String getIndentation() {
			return indentation;
		}

		/**
		 * @param indentation The indentation to set.
		 */
		public void setIndentation(String indentation) {
			this.indentation = indentation;
		}

		/**
		 * Denotes whether the DescriptionBuilder logs errors.
		 * Default is false. 
		 * @return 
		 */
		public boolean isVerbose() {
			return verbose;
		}

		public void setVerbose(boolean verbose) {
			this.verbose = verbose;
		}

	}
	
	protected static abstract class Appender {
		private final Class base;
		
		public Appender(Class base) {
			assert base != null;
			this.base = base;
		}
		
		protected Class getBase() {
			return base;
		}
		
		@SuppressWarnings("unchecked")
		public boolean canDescribe(DescriptionBuilder builder, 
			Object value) 
		{
			return getBase().isAssignableFrom(value.getClass()); // "unchecked"
		}
		
		public void append(DescriptionBuilder builder, Object value) {
            if (builder != null) { 
                builder.appendString(value.toString());
            }
		}

        public boolean isPrimitive() {
            return false;
        }
        
        protected void header(DescriptionBuilder builder, Object value) { 
            if (builder != null) {
                builder.header(value);
            }
        }

        protected void appendProperty(DescriptionBuilder builder, String name, 
            Object value)
        { 
            if (builder != null) {
                builder.appendProperty(name, value);
            }
        }

        protected void appendProperty(DescriptionBuilder builder, String name, 
            Object value, boolean isFirst)
        { 
            if (builder != null) {
                builder.appendProperty(name, value, isFirst);
            }
        }
        
        protected void appendString(DescriptionBuilder builder, String value) {
            builder.appendString(value);
        }

        public void unknown(DescriptionBuilder builder) {
            if (builder != null) {
                builder.unknown();
            }
        }

        public void newline(DescriptionBuilder builder) {
            if (builder != null) {
                builder.newline();
            }
        }

        public void indent(DescriptionBuilder builder) {
            if (builder != null) {
                builder.indent();
            }
        }

        public void unindent(DescriptionBuilder builder) {
            if (builder != null) {
                builder.unindent();
            }
        }

        public void openCollection(DescriptionBuilder builder) {
            if (builder != null) {
                builder.openCollection();
            }
        }

        public void closeCollection(DescriptionBuilder builder) {
            if (builder != null) {
                builder.closeCollection();
            }
        }

        public void openPropertyList(DescriptionBuilder builder) {
            if (builder != null) {
                builder.openPropertyList();
            }
        }

        public void closePropertyList(DescriptionBuilder builder) {
            if (builder != null) {
                builder.closePropertyList();
            }
        }


        public void separate(DescriptionBuilder builder) {
            if (builder != null) {
                builder.separate();
            }
        }

        public void separateProperty(DescriptionBuilder builder) {
            if (builder != null) {
                builder.separateProperty();
            }
        }
        public void separatePropertyValue(DescriptionBuilder builder) {
            if (builder != null) {
                builder.separatePropertyValue();
            }
        }

	}

	private static class StringAppender extends Appender {
		
		public StringAppender() {
			super(String.class);
		}

		@Override
		public boolean canDescribe(DescriptionBuilder builder,
			Object value) 
		{
			return value instanceof String;
		}
		
		@Override
		public void append(DescriptionBuilder builder, Object value) {
			builder.appendString("'");
			builder.appendString((String) value);
			builder.appendString("'");
		}

        @Override
        public boolean isPrimitive() {
            return true;
        }
	}

	private static class NumberAppender extends Appender {
		
		public NumberAppender() {
			super(Number.class);
		}

        @Override
        public boolean isPrimitive() {
            return true;
        }
	}

	private static class BooleanAppender extends Appender {
		
		public BooleanAppender() {
			super(Boolean.class);
		}


        @Override
        public boolean isPrimitive() {
            return true;
        }
}

	private static class CharacterAppender extends Appender {
		
		public CharacterAppender() {
			super(Character.class);
		}


        @Override
        public boolean isPrimitive() {
            return true;
        }
}

	private static class ClassAppender extends Appender {
		
		public ClassAppender() {
			super(Class.class);
		}

		@Override
		public void append(DescriptionBuilder builder, Object value) {
			String className = ((Class) value).getName();
			if (!builder.properties.useFullClassName()) {
				className = StringUtils.substringAfterLast(className, ".");
			}
			builder.appendString(className);
		}

        @Override
        public boolean isPrimitive() {
            return true;
        }
	}

	private static class DateAppender extends Appender {
		
		public DateAppender() {
			super(Date.class);
		}

		@Override
		public boolean canDescribe(DescriptionBuilder builder, Object value)  {
			return super.canDescribe(builder, value);
		}

		@Override
		public void append(DescriptionBuilder builder, Object value) {
			if (builder.properties.getDateFormat() != null) {
				builder.appendString(builder.properties.getDateFormat().format(
						(Date) value));
			} else {
				builder.appendString(value.toString());
			}
		}

        @Override
        public boolean isPrimitive() {
            return true;
        }
	}

	private static class DescribeableAppender extends Appender {
		
		public DescribeableAppender () {
			super(Describeable.class);
		}

		@Override
		public void append(DescriptionBuilder builder, Object value) {
			((Describeable) value).describe(builder);
		}

    }

	/**
	 * Describes the contents of a given array. Invokes
	 * {@link #append(java.lang.Object)} on each of the array's elements.
	 * @param values The array to describe.
	 * @return The description
	 */
	private static class ArrayAppender extends Appender {
		
		public ArrayAppender() {
			super(Object.class);
		}
		
		@Override
		public boolean canDescribe(DescriptionBuilder builder, Object value) {
			return value.getClass().isArray();
		}
		
		@Override
		public void append(DescriptionBuilder builder, Object value) {
			builder.openCollection();
			boolean first = true; 
			for (Iterator i = IteratorUtils.arrayListIterator(value);
				 i.hasNext();)
			{
				if (!first) {
					builder.separate();
				} else {
					first = false;
				}
				builder.append(i.next());
			}
			builder.closeCollection();
		}
        
	}

	/**
	 * Describes the contents of a given List. 
	 * Invokes {@link #append(java.lang.Object)} on each of its elements. 
	 * Add the separator (see {@link Properties#getSeparator()}) between all 
	 * elements.
	 * The list is not sorted in anyway.
	 * @param values The list to describe.
	 * @return The description
	 */
	protected static class ListAppender extends Appender {
		
		public ListAppender() {
			super(List.class);
		}
		
		@Override
		public void append(DescriptionBuilder builder, Object value) {
			List values = (List) value; 
			builder.openCollection();
			boolean first = true;
			for (Object element: values) {
				if (!first) {
					builder.separate();
				} else {
					first = false;
				}
				builder.append(element);
			}
			builder.closeCollection();
		}
    }		
	
	/**
	 * Describes the contents of a given Set. The elements are show sorted
	 * alphabetically. Invokes {@link #append(java.lang.Object)} on each of
	 * its elements. Add the separator (see {@link #getSeparator()}) between
	 * all elements.
	 */
	private static class SetAppender extends Appender {
		
		public SetAppender() {
			super(Set.class);
		}
        
		@Override
		public void append(DescriptionBuilder builder, Object value) {
			Set values = (Set) value; 
			// Can be sorted, because a set does not define order
			// and displayed alphabetically is easier to read
			TreeSet<String> sortedNames = new TreeSet<String>(Collator
				.getInstance());
			DescriptionBuilder clonedBuilder; 
			for (Object v: values) {
				clonedBuilder = (DescriptionBuilder) builder.clone();
				sortedNames.add(clonedBuilder.append(v).toString());
			}
			builder.openCollection();
			boolean first = true;
			for (String name: sortedNames) {
				if (!first) {
					builder.separate();
				} else {
					first = false;
				}
				builder.appendString(name);
			}
			builder.closeCollection();
		}
	}		

	/**
	 * Describes the contents of a given Map. Shows the key and the associated
	 * value for all map elements. The keys are shown sorted alphabetically.
	 * Invokes {@link #append(java.lang.Object)} on each of its elements. Add
	 * the separator (see {@link #getSeparator()}) between all elements
	 */
	private static class MapAppender extends Appender {
		
		public MapAppender() {
			super(Map.class);
		}
		
        @Override
		public void append(DescriptionBuilder builder, Object value) {
			Map values = (Map) value; 
			// Can be sorted, because a set does not define order
			// and displayed alphabetically is easier to read
			TreeSet<String> sortedNames = new TreeSet<String>(Collator.
				getInstance());
			Map<String, Object> nameToKey = new HashMap<String, Object>();
			DescriptionBuilder clonedBuilder;
			for (Iterator i = values.keySet().iterator(); i.hasNext();) {
				Object key = i.next();
				clonedBuilder = (DescriptionBuilder) builder.clone();
				String describedKey = clonedBuilder.append(key).toString(); 
				sortedNames.add(describedKey);
				nameToKey.put(describedKey, key);
			}
			builder.openCollection();
			boolean first = true;
			for (String name: sortedNames) {
				if (!first) {
					builder.separate();
				} else {
					first = false;
				}
				Object key = nameToKey.get(name);
				builder.appendString(name);
				builder.appendString("=");
				builder.append(values.get(key));
			}
			builder.closeCollection();
		}
	}
	
	private static class BeanAppender extends Appender {
		
		public BeanAppender() {
			super(Object.class);
		}
		
		@Override
		public boolean canDescribe(DescriptionBuilder builder,
			Object value) 
		{
			TreeMap<String, PropertyDescriptor> readableProperties =
				getReadableProperties(builder, value);
			return (readableProperties != null)
				// Do not want to print the class property, so > 1
				&& (readableProperties.size() > 1); 
		}
		
		@Override
		public void append(DescriptionBuilder builder, Object value) {
            TreeMap<String, PropertyDescriptor> readableProperties = 
                getReadableProperties(builder, value);
			builder.header(value);
			builder.openPropertyList();
			boolean isFirst = true;
			for (String key: readableProperties.keySet()) {
				if (key.equals("class")) {
					// ignore the class property; this is printed in the header
					continue;
				}
				appendProperty(builder, key, readableProperties.get(key), value, 
                    isFirst);
                isFirst = false;
			}
            builder.closePropertyList();
		}
		
		private void appendProperty(DescriptionBuilder builder, String key, 
			PropertyDescriptor descriptor, Object bean, boolean isFirst)
		{
			if (descriptor == null) {
				logException(builder, key, bean, 
					new IllegalArgumentException("Descriptor not found."));
				return;
			}
			Method readMethod = descriptor.getReadMethod();
			if (readMethod == null) {
				logException(builder, key, bean, 
					new IllegalArgumentException("readMethod not found."));
				return;
			}
            Object value = "<error>";
			try {
				Object[] noParameters = {};
				value = readMethod.invoke(bean, noParameters);
			} catch (IllegalArgumentException e) {
				logException(builder, key, bean, e);
			} catch (IllegalAccessException e) {
				logException(builder, key, bean, e);
			} catch (InvocationTargetException e) {
				logException(builder, key, bean, e);
			}
            builder.appendProperty(descriptor.getName(), value, isFirst);
		}
		
		private void logException(DescriptionBuilder builder, String property, 
            Object bean, Exception e) 
		{
			builder.appendString(UNKNOWN);
			if (builder.properties.isVerbose()) {
				String className = (bean != null) ? bean.getClass().getName() 
					: "null>";
				String message = (e != null) ? e.getMessage() : null;
				Throwable rootCause = ExceptionUtils.getRootCause(e); 
				LOG.error(message + ", while appending property '" 
					+ className + "." + property + "'" + ((rootCause != null) 
						? " caused by " + rootCause.getMessage() : "."));
			}
		}

		private TreeMap<String, PropertyDescriptor> getReadableProperties(
			DescriptionBuilder builder, Object bean) 
		{ 
			TreeMap<String, PropertyDescriptor> readProperties = 
				new TreeMap<String, PropertyDescriptor>
				(Collator.getInstance());
			if (bean == null) {
				return readProperties;
			}
			try {
				BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
				for (PropertyDescriptor descriptor: beanInfo.
						getPropertyDescriptors()) 
				{
					if (descriptor.getReadMethod() != null) {
						readProperties.put(descriptor.getName(), descriptor);
					}
				}
			} catch (IntrospectionException e) {
				if (builder.properties.isVerbose()) {
					LOG.error("Introspection problem " + e.getMessage() 
						+ " for " + ((bean != null) 
							? bean.getClass().getName() : UNKNOWN));
				}
			}
			
			return readProperties;
		}
	}		

}
