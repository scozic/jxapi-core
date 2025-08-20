package org.jxapi.generator.java.exchange.properties;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jxapi.exchange.descriptor.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ExchangeGenUtil;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.ConfigProperty;
import org.jxapi.util.DefaultConfigProperty;
import org.jxapi.util.JsonUtil;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Helper methods around configuration properties related code generation.
 */
public class PropertiesGenUtil {
  
  /**
   * Name of the static 'ALL' variable that contains a list of all configuration properties.
   */
  public static final String ALL_PROPERTY = "ALL";

  private PropertiesGenUtil() {}
  
  /**
   * Creates a {@link ConfigProperty} from a given
   * {@link ConfigPropertyDescriptor} instance.
   * 
   * @param propertyDescriptor The property descriptor to convert.
   * @return The converted ConfigProperty, or null if the propertyDescriptor is
   *         null.
   * @throws IllegalArgumentException if the propertyDescriptor is a group. A
   *                                  {@link ConfigProperty} represents a single
   *                                  configuration property, not a group.
   */
  public static ConfigProperty asConfigProperty(ConfigPropertyDescriptor propertyDescriptor) {
    if (propertyDescriptor == null) {
      return null;
    }
    if (propertyDescriptor.isGroup()) {
      throw new IllegalArgumentException("Property '" + propertyDescriptor.getName() + "' is a group, not a property");
    }
    return DefaultConfigProperty.create(
        propertyDescriptor.getName(),
        propertyDescriptor.getType(),
        propertyDescriptor.getDescription(),
        propertyDescriptor.getDefaultValue());
  }
  
  /**
   * Creates a {@link ConfigPropertyDescriptor} matching the given
   * {@link ConfigProperty} instance.
   * 
   * @param configProperty The {@link ConfigProperty} to convert.
   * @return a {@link ConfigPropertyDescriptor} matching the given
   *         {@link ConfigProperty} instance or <code>null</code> if input is
   *         <code>null</code>
   */
  public static ConfigPropertyDescriptor asConfigPropertyDescriptor(ConfigProperty configProperty) {
    if (configProperty == null) {
      return null;
    }
    return ConfigPropertyDescriptor.create(
        configProperty.getName(),
        configProperty.getType(),
        configProperty.getDescription(),
        configProperty.getDefaultValue());
  }
  

  /**
   * Generates the Java code for a static member named {@link #ALL_PROPERTY} variable that refers to a list of all
   * configuration properties defined in the class. 
   * @param properties the list of configuration properties to include in the generated list.
   * @param imports the set of imports which will be updated with the necessary imports for the generated code.
   * @return the Java code for the static member variable that contains a list of all configuration properties.
   */
  public static String generateAllPropertiesListMethod(List<ConfigPropertyDescriptor> properties, Imports imports) {
      imports.add(List.class);
      imports.add(ConfigProperty.class);
  
      StringBuilder s = new StringBuilder();
      s.append(JavaCodeGenUtil.generateJavaDoc("List of all configuration properties defined in this class"))
       .append("\npublic static final List<ConfigProperty> ")
       .append(ALL_PROPERTY)
       .append(" = ");
  
      if (CollectionUtil.isEmpty(properties)) {
          s.append("List.of();\n");
          return s.toString();
      }
  
      imports.add(CollectionUtil.class);
      s.append("List.copyOf(")
       .append(CollectionUtil.class.getSimpleName())
       .append(".mergeLists(List.of(\n");
  
      appendPropertiesListToAllPropertiesListMethod(properties, s);
  
      s.append(")));\n");
      return s.toString();
  }
  
  private static void appendPropertiesListToAllPropertiesListMethod(List<ConfigPropertyDescriptor> properties, StringBuilder s) {
      boolean inList = false;
      boolean first = true;
      String indent = JavaCodeGenUtil.INDENTATION;
  
      for (ConfigPropertyDescriptor property : properties) {
          if (first) {
              first = false;
              s.append(indent);
          } else {
              appendSeparatorToAllPropertiesListMethod(s, inList, property.isGroup(), indent);
          }
  
          if (inList) {
              if (property.isGroup()) {
                  closeGroupListInAllPropertiesListMethod(s, indent);
                  inList = false;
              }
          } else if (!property.isGroup()) {
              startNewListInAllPropertiesListMethod(s, indent);
              inList = true;
          }
  
          appendPropertyReferenceToAllPropertiesListMethod(property, s, indent);
      }
  
      if (inList) {
          s.append(")");
      }
  }
  
  private static void appendSeparatorToAllPropertiesListMethod(StringBuilder s, boolean inList, boolean isGroup, String indent) {
      if (!(inList && isGroup)) {
          s.append(",");
      }
      s.append("\n").append(indent);
  }
  
  private static void closeGroupListInAllPropertiesListMethod(StringBuilder s, String indent) {
      s.append("),\n").append(indent);
  }
  
  private static void startNewListInAllPropertiesListMethod(StringBuilder s, String indent) {
      s.append("List.of(\n").append(indent);
  }
  
  private static void appendPropertyReferenceToAllPropertiesListMethod(ConfigPropertyDescriptor property, StringBuilder s, String indent) {
      if (property.isGroup()) {
          s.append(JavaCodeGenUtil.firstLetterToUpperCase(property.getName()))
           .append(".").append(ALL_PROPERTY);
      } else {
          s.append(indent)
           .append(JavaCodeGenUtil.getStaticVariableName(PropertiesGenUtil.getPropertyKeyPropertyName(property)));
      }
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
   * @param property                       the property to generate the declaration for
   * @param prefix                         the prefix to prepend to the property name, for instance 'myExchange'.
   * @param imports                        the set of imports to add to the generated code
   * @param docPlaceHolderResolver         the resolver for placeholders in the property's description
   * @param sampleValuePlaceHolderResolver the resolver for placeholders in the property's sample value
   * @return the Java code for the property declaration
   */
  public static String generateSimplePropertyValueDeclaration(
      ConfigPropertyDescriptor property, 
      String  prefix,
      Imports imports, 
      PlaceHolderResolver docPlaceHolderResolver, 
      PlaceHolderResolver sampleValuePlaceHolderResolver) {
    imports.add(DefaultConfigProperty.class);
    imports.add(Type.class);
    imports.add(ConfigProperty.class);
    String name = getPropertyFullName(prefix, property.getName());
    String description = Optional.ofNullable(docPlaceHolderResolver).orElse(PlaceHolderResolver.NO_OP).resolve(property.getDescription());
    Object sampleValue = property.getDefaultValue();
    if (!property.getType().getCanonicalType().isPrimitive) {
      sampleValue = JsonUtil.pojoToJsonString(sampleValue);
    }
    String sampleValueStr = sampleValue == null? null: 
      Optional.ofNullable(sampleValuePlaceHolderResolver)
        .orElse(JavaCodeGenUtil::getQuotedString)
        .resolve(sampleValue.toString());
    return new StringBuilder()
        .append(JavaCodeGenUtil.generateJavaDoc(description))
        .append("\npublic static final ConfigProperty ")
        .append(JavaCodeGenUtil.getStaticVariableName(PropertiesGenUtil.getPropertyKeyPropertyName(property)))
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

  /**
   * @param property the property to generate the property key property name for, for instance 'myProperty'.
   * @return the property key property name, for instance 'myPropertyProperty'
   */
  public static String getPropertyKeyPropertyName(ConfigPropertyDescriptor property) {
    return  property.getName();
  }
  
  /**
   * Generates the getter method name for a property inside a generated configuration properties class.
   * The method will take the form of 'getMyProperty' for a property named 'myProperty' with a return value depending on
   * property type, and expecting one {@link Properties} argument.
   * 
   * @param property      the property to generate the getter method name for
   * @param allProperties the list of all properties to avoid name conflicts
   * @return the getter method name, for instance 'getMyProperty'
   */
  public static String getPropertyGetterMethodName(ConfigPropertyDescriptor property, List<ConfigPropertyDescriptor> allProperties) {
    String name = property.getName();
    Type type = property.getType();
    if (property.isGroup()) {
      type = Type.STRING;
    }
    String typeClass = ExchangeGenUtil.getClassNameForType(type, new Imports(), null);
    return JavaCodeGenUtil.getGetAccessorMethodName(
        name, 
        typeClass, 
        CollectionUtil.emptyIfNull(allProperties).stream().map(p -> p.getName()).collect(Collectors.toList())
    );
  }
  
  /**
   * Generates the full property name for a given property, including the prefix.
   * @param prefix the prefix to prepend to the property name, for instance 'myExchange'.
   * @param propertyName the property name, for instance 'myProperty'.
   * @return the full property name, for instance 'myExchange.myProperty', 
   *         or just 'myProperty' if the prefix is empty or <code>null</code>. 
   */
  public static String getPropertyFullName(String prefix, String propertyName) {
    if (StringUtils.isEmpty(prefix)) {
      return propertyName;
    }
    return prefix + "." + propertyName;
  }

}
