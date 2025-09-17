package org.jxapi.generator.java.exchange.constants;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.jxapi.exchange.descriptor.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ExchangeGenUtil;
import org.jxapi.util.CollectionUtil;
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
   * @param sieblings the list of constants declared in the same class as the constant, used to avoid name clashes
   * @param imports the set of imports to add to the generated code
   * @param docPlaceHolderResolver the resolver for placeholders in the constant's description
   * @param sampleValuePlaceHolderResolver the resolver for placeholders in the constant's sample value
   * @return the Java code for the constant declaration
   */
  public static String generateConstantDeclaration(Constant constant, 
                                                   List<Constant> sieblings,
                                                   Imports imports, 
                                                   PlaceHolderResolver docPlaceHolderResolver,
                                                   PlaceHolderResolver sampleValuePlaceHolderResolver) {
    StringBuilder code = new StringBuilder();
    Type type = constant.getType();
    if (!type.getCanonicalType().isPrimitive) {
      throw new IllegalArgumentException("Constant " + constant + " has not a primitive type");
    }
    String className = ExchangeGenUtil.getClassNameForType(type, imports, null);
    String varName = getConstantVariableName(constant, sieblings);
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
   * Returns the name of variable holding either the property or group property class name.
   * <p>
   * For a regular (not group) constant the returned  value is the static variable name (uppercase) of the constant name.
   * For a group constant the returned value is the constant name with first letter upper cased.
   * @param constant the property to generate the property key property name for, for instance 'myProperty'.
   * @param sieblings the list of properties declared in the same class as the property, used to avoid name clashes
   * @return the property key property name, for instance 'myPropertyProperty'
   */
  public static String getConstantVariableName(Constant constant, List<Constant> sieblings) {
    sieblings = CollectionUtil.emptyIfNull(sieblings);
    int off = sieblings.indexOf(constant);
    if (off < 0 ) {
      throw new IllegalArgumentException("Constant '" + constant.getName() + "' is not part of the sieblings list: " + sieblings);
    }
    return getConstantVariableName(constant, sieblings, off, CollectionUtil.createList(sieblings.size() + 1));
  }
  
  private static String getConstantVariableName(
      Constant property, 
      List<Constant> sieblings, 
      int index, 
      List<String> constantNames) {
    if (index >= constantNames.size()) {
      StringBuilder name = new StringBuilder()
          .append(getConstantDefaultVariableName(property));
      sieblings = CollectionUtil.emptyIfNull(sieblings);
      while (hasDifferentConstantWithSameVariableName(
                property,
                name.toString(),  
                sieblings,
                constantNames)) {
        name.append("_");
      }
      constantNames.add(name.toString());
    }
    
    return constantNames.get(index);
  }
  
  private static boolean hasDifferentConstantWithSameVariableName(
      Constant property, 
      String staticVariableName,
      List<Constant> sieblings,
      List<String> constantNames) {
    List<Constant> l = CollectionUtil.emptyIfNull(sieblings);
    for (int i = 0; i < l.size(); i++) {
      Constant p = l.get(i);
      if (p == property) {
        return false;
      }
      if (getConstantVariableName(p, sieblings, i, constantNames).equals(staticVariableName)) {
        return true;
      }
    }
    return false;
  }
  
  private static String getConstantDefaultVariableName(Constant constant) {
    String name = constant.getName();
    if (name.contains(".")) {
      name = StringUtils.substringAfterLast(name, ".");
    }
    if (constant.isGroup()) {
      return JavaCodeGenUtil.firstLetterToUpperCase(name);
    }
    return JavaCodeGenUtil.getStaticVariableName(name);
  }

}
