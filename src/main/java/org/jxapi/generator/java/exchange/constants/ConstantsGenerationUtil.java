package org.jxapi.generator.java.exchange.constants;

import java.util.Optional;

import org.jxapi.exchange.descriptor.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ExchangeGenUtil;
import org.jxapi.util.ConfigProperty;
import org.jxapi.util.DefaultConfigProperty;
import org.jxapi.util.PlaceHolderResolver;

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
   * @param docPlaceHolderResolver the resolver for placeholders in the constant's description
   * @param sampleValuePlaceHolderResolver the resolver for placeholders in the constant's sample value
   * @return the Java code for the constant declaration
   */
  public static String generateConstantDeclaration(Constant constant, 
                                                   Imports imports, 
                                                   PlaceHolderResolver docPlaceHolderResolver,
                                                   PlaceHolderResolver sampleValuePlaceHolderResolver) {
    StringBuilder code = new StringBuilder();
    Type type = constant.getType();
    if (!type.getCanonicalType().isPrimitive) {
      throw new IllegalArgumentException("Constant " + constant + " has not a primitive type");
    }
    String className = ExchangeGenUtil.getClassNameForType(type, imports, null);
    String varName = JavaCodeGenUtil.getStaticVariableName(constant.getName());
    String value = ExchangeGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(
                    type, 
                    constant.
                    getValue(), 
                    imports, 
                    sampleValuePlaceHolderResolver);
    String description = Optional.ofNullable(docPlaceHolderResolver)
                                 .orElse(PlaceHolderResolver.NO_OP)
                                 .resolve(constant.getDescription());
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
  public static String getPropertyKeyPropertyName(ConfigPropertyDescriptor property) {
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
   *     "This is a description of my property", "myDefaultValue");
   * }
   * </pre>
   * 
   * Where {@code myProperty} is the property key, {@code Type.STRING} is the type of the property,
   * {@code This is a description of my property} is the description of the
   * property and {@code myDefaultValue} is the default value of the property.
   * 
   * @param property the property to generate the declaration for
   * @param imports  the set of imports to add to the generated code
   * @param docPlaceHolderResolver the resolver for placeholders in the property's description
   * @return the Java code for the property declaration
   */
  public static String getPropertyValueDeclaration(
      ConfigPropertyDescriptor property, 
      Imports imports, 
      PlaceHolderResolver docPlaceHolderResolver, 
      PlaceHolderResolver sampleValuePlaceHolderResolver) {
    imports.add(DefaultConfigProperty.class);
    imports.add(Type.class);
    imports.add(ConfigProperty.class);
    String name = property.getName();
    String description = Optional.ofNullable(docPlaceHolderResolver).orElse(PlaceHolderResolver.NO_OP).resolve(property.getDescription());
    Object sampleValue = property.getDefaultValue();
    String sampleValueStr = sampleValue == null? null: 
      Optional.ofNullable(sampleValuePlaceHolderResolver)
        .orElse(JavaCodeGenUtil::getQuotedString)
        .resolve(sampleValue.toString());
    return new StringBuilder()
        .append(JavaCodeGenUtil.generateJavaDoc(description))
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
        .append(JavaCodeGenUtil.getQuotedString(description))
        .append(",\n")
        .append(JavaCodeGenUtil.INDENTATION)
        .append(sampleValueStr)
        .append(");\n")
        .toString();
  }

}
