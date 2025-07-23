package org.jxapi.generator.java.exchange.constants;

import java.util.Optional;

import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ExchangeGenUtil;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Helper methods for generating constants and property interfaces.
 */
public class ConstantsGenUtil {

  private ConstantsGenUtil() {}

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

}
