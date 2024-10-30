package com.scz.jxapi.generator.java.exchange.constants;

import java.util.ArrayList;
import java.util.List;

import com.scz.jxapi.exchange.descriptor.ConfigProperty;
import com.scz.jxapi.exchange.descriptor.Constant;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.generator.java.Imports;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.exchange.ExchangeJavaWrapperGeneratorUtil;

/**
 * Helper methods for generating constants and property interfaces.
 */
public class ConstantsGenerationUtil {

	private ConstantsGenerationUtil() {}

	/**
	 * Generates the Java code for a declared constant in a Java interface.
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
		String className = ExchangeJavaWrapperGeneratorUtil.getClassNameForType(type, imports, null);
		String varName = JavaCodeGenerationUtil.getStaticVariableName(constant.getName());
		String value = ExchangeJavaWrapperGeneratorUtil.getPrimitiveTypeFieldSampleValueDeclaration(type, constant.getValue(), imports);
		String description = constant.getDescription();
		if (description != null) {
			code.append(JavaCodeGenerationUtil.generateJavaDoc(description))
				.append("\n");
		}
		code.append(className)
			.append(" ")
			.append(varName)
			.append(" = ")
			.append(value)
			.append(";\n");
		return code.toString();
	}
	
	/*
	 * @param property the property to generate the property key property name for, for instance 'myProperty'.
	 * @return the property key property name, for instance 'myPropertyProperty'
	 */
	public static String getPropertyKeyPropertyName(ConfigProperty property) {
		return  property.getName() + "Property";
	}
	
	/*
	 * @param property the property to generate the property default value property name for, for instance 'myProperty'.
	 * @return the property default value property name, for instance 'myPropertyDefaultValue'
	 */
	public static String getPropertyDefaultValuePropertyName(ConfigProperty property) {
		return  property.getName() + "DefaultValue";
	}

	/**
	 * Generates a list of constants for the given properties.
	 * <p>
	 * For each property, two constants are generated:
	 * <ul>
	 * <li>the property key constant, for instance 'myPropertyProperty' with the property key as value</li>
	 * <li>the property default value constant, for instance 'myPropertyDefaultValue' with the property default value as value</li>
	 * </ul>
	 * @param properties the properties to generate the constants for
	 * @return the list of constants for the given properties
	 */
	public static List<Constant> getConstantsForProperties(List<ConfigProperty> properties) {
		List<Constant> constants = new ArrayList<>();
		properties.forEach(p -> {
			String propKeyName = getPropertyKeyPropertyName(p);
			StringBuilder propertyDesc = new StringBuilder()
					.append("'")
					.append(p.getName())
					.append("' property key.");
			if (p.getDescription() != null) {
				propertyDesc.append("<br/>\n")
							.append(p.getDescription());
			}
			propertyDesc.append("<br/>\nProperty value type:")
						.append(p.getType());
			
			constants.add(Constant.create(propKeyName, Type.STRING, propertyDesc.toString(), p.getName()));
			if (p.getDefaultValue() != null) {
				String propDefValName = getPropertyDefaultValuePropertyName(p);
				StringBuilder defValueDesc = new StringBuilder()
						.append("{@link #")
						.append(JavaCodeGenerationUtil.getStaticVariableName(propKeyName))
						.append("} property default value");
				constants.add(Constant.create(propDefValName, p.getType(), defValueDesc.toString(), p.getDefaultValue()));
			}
			
		});
		return constants;
	}

}
