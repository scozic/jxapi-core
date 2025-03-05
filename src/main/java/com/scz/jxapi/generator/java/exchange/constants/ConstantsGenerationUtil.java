package com.scz.jxapi.generator.java.exchange.constants;

import com.scz.jxapi.exchange.descriptor.ConfigProperty;
import com.scz.jxapi.exchange.descriptor.Constant;
import com.scz.jxapi.exchange.descriptor.DefaultConfigProperty;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.generator.java.Imports;
import com.scz.jxapi.generator.java.JavaCodeGenUtil;
import com.scz.jxapi.generator.java.exchange.ExchangeJavaGenUtil;

/**
 * Helper methods for generating constants and property interfaces.
 */
public class ConstantsGenerationUtil {

	private ConstantsGenerationUtil() {}

	/**
	 * Generates the Java code for a declared <code>public static final</code> constant in a Java class.
	 * <p>
	 * Example:
	 * <pre>
	 * {@code
	 * Integer MY_INT = Integer.valueOf(42);
	 * }
	 * </pre>
	 * Where {@code MY_INT} is the constant name, {@code Integer} is the type of the constant and {@code 42} is the value of the constant.
	 * @param constant the constant to generate the declaration for
	 * @param imports the set of imports to add to the generated code
	 * @return the Java code for the constant declaration
	 */
	public static String generateConstantDeclaration(Constant constant, Imports imports) {
		StringBuilder code = new StringBuilder();
		Type type = constant.getType();
		if (!type.getCanonicalType().isPrimitive) {
			throw new IllegalArgumentException("Constant " + constant + " has not a primitive type");
		}
		String className = ExchangeJavaGenUtil.getClassNameForType(type, imports, null);
		String varName = JavaCodeGenUtil.getStaticVariableName(constant.getName());
		String value = ExchangeJavaGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(type, constant.getValue(), imports);
		String description = constant.getDescription();
		if (description != null) {
			code.append(JavaCodeGenUtil.generateJavaDoc(description))
				.append("\n");
		}
		code.append("public static final ")
			.append(className)
			.append(" ")
			.append(varName)
			.append(" = ")
			.append(value)
			.append(";\n");
		return code.toString();
	}
	
	/**
	 * @param property the property to generate the property key property name for, for instance 'myProperty'.
	 * @return the property key property name, for instance 'myPropertyProperty'
	 */
	public static String getPropertyKeyPropertyName(ConfigProperty property) {
		return  property.getName();
	}
	
	/**
	 * Generates the Java code for a declared <code>public static final</code>
	 * property in a Java class.
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * {@code
	 * public static final ConfigProperty MY_PROPERTY_PROPERTY = DefaultConfigProperty.create("myProperty", Type.STRING,
	 * 		"This is a description of my property", "myDefaultValue");
	 * }
	 * </pre>
	 * 
	 * Where {@code myProperty} is the property key, {@code Type.STRING} is the type of the property,
	 * {@code This is a description of my property} is the description of the
	 * property and {@code myDefaultValue} is the default value of the property.
	 * 
	 * @param property the property to generate the declaration for
	 * @param imports  the set of imports to add to the generated code
	 * @return the Java code for the property declaration
	 */
	public static String getPropertyValueDeclation(ConfigProperty property, Imports imports) {
		imports.add(DefaultConfigProperty.class);
		imports.add(Type.class);
		imports.add(ConfigProperty.class);
		String name = property.getName();
		return new StringBuilder()
				.append(JavaCodeGenUtil.generateJavaDoc(property.getDescription()))
				.append("\npublic static final ConfigProperty ")
				.append(JavaCodeGenUtil.getStaticVariableName(getPropertyKeyPropertyName(property)))
				.append(" = DefaultConfigProperty.create(\n")
				.append(JavaCodeGenUtil.INDENTATION)
				.append(JavaCodeGenUtil.getQuotedString(name))
				.append(",\n")
				.append(JavaCodeGenUtil.INDENTATION)
				.append(Type.class.getSimpleName())
				.append(".")
				.append(property.getType().toString())
				.append(",\n")
				.append(JavaCodeGenUtil.INDENTATION)
				.append(JavaCodeGenUtil.getQuotedString(property.getDescription()))
				.append(",\n")
				.append(JavaCodeGenUtil.INDENTATION)
				.append(JavaCodeGenUtil.getQuotedString(property.getDefaultValue()))
				.append(");")
				.toString();
	}

}
