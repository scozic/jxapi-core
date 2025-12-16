package org.jxapi.generator.java.exchange.properties;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.pojo.descriptor.Type;
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
   * Prefix for endpoint demo snippet specific properties.
   */
  public static final String DEMO_PREFIX = "demo";
  
  /**
   * Name of the static 'ALL' variable that contains a list of all configuration properties.
   */
  public static final String ALL_PROPERTY = "all";

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
    if (isGroup(propertyDescriptor)) {
      throw new IllegalArgumentException("Property '" + propertyDescriptor.getName() + "' is a group, not a property");
    }
    return DefaultConfigProperty.create(
        propertyDescriptor.getName(),
        getType(propertyDescriptor),
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
    return ConfigPropertyDescriptor.builder()
        .name(configProperty.getName())
        .type(configProperty.getType() != null ? configProperty.getType().toString() : null)
        .description(configProperty.getDescription())
        .defaultValue(configProperty.getDefaultValue())
        .build();
  }
  

  /**
   * Generates the Java code for a static member variable named 'ALL' that refers to a list of all
   * configuration properties defined in the class. The list will contain references to all properties and 
   * properties of group properties defined in the class flattened in a single list.<p>
   * Remark: If properties contains a property named 'all' the generated variable will be named 'ALL_'
   * (underscores will be appended until name does not collide with another property static variable name).
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
       .append(generateAllPropertiesVariableName(properties))
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
  
  private static String generateAllPropertiesVariableName(List<ConfigPropertyDescriptor> properties) {
    ConfigPropertyDescriptor allPropertyVar = ConfigPropertyDescriptor.builder()
        .name(ALL_PROPERTY)
        .type(Type.STRING.toString())
        .build();
    return getPropertyVariableName(allPropertyVar, CollectionUtil.mergeLists(properties, List.of(allPropertyVar)));
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
              appendSeparatorToAllPropertiesListMethod(s, inList, isGroup(property), indent);
          }
  
          if (inList) {
              if (isGroup(property)) {
                  closeGroupListInAllPropertiesListMethod(s, indent);
                  inList = false;
              }
          } else if (!isGroup(property)) {
              startNewListInAllPropertiesListMethod(s, indent);
              inList = true;
          }
  
          appendPropertyReferenceToAllPropertiesListMethod(property, properties, s, indent);
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
  
  private static void appendPropertyReferenceToAllPropertiesListMethod(
      ConfigPropertyDescriptor property,
      List<ConfigPropertyDescriptor> sieblings,
      StringBuilder s, 
      String indent) {
      if (isGroup(property)) {
          s.append(PropertiesGenUtil.getPropertyVariableName(property, sieblings))
           .append(".").append(generateAllPropertiesVariableName(property.getProperties()));
      } else {
          s.append(indent)
           .append(PropertiesGenUtil.getPropertyVariableName(property, sieblings));
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
   * @param sieblings                      the list of properties declared in the same class as the property, used to avoid name clashes
   * @param prefix                         the prefix to prepend to the property name, for instance 'myExchange'.
   * @param imports                        the set of imports to add to the generated code
   * @param docPlaceHolderResolver         the resolver for placeholders in the property's description
   * @param sampleValuePlaceHolderResolver the resolver for placeholders in the property's sample value
   * @return the Java code for the property declaration
   */
  public static String generateSimplePropertyValueDeclaration(
      ConfigPropertyDescriptor property, 
      List<ConfigPropertyDescriptor> sieblings,
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
    Type propType = property.getType() == null ? Type.STRING : Type.fromTypeName(property.getType());
    if (!propType.getCanonicalType().isPrimitive) {
      propType = Type.STRING;
    }
    String sampleValueStr = null;
    if (sampleValue != null) {
      if (!(sampleValue instanceof String)) {
        sampleValue = JsonUtil.pojoToJsonString(sampleValue);
      }
      sampleValueStr = Optional.ofNullable(sampleValuePlaceHolderResolver)
      .orElse(JavaCodeGenUtil::getQuotedString)
      .resolve(sampleValue.toString());
    }

    return new StringBuilder()
        .append(JavaCodeGenUtil.generateJavaDoc(description))
        .append("\npublic static final ConfigProperty ")
        .append(PropertiesGenUtil.getPropertyVariableName(property, sieblings))
        .append(" = DefaultConfigProperty.create(\n")
        .append(JavaCodeGenUtil.INDENTATION)
        .append(JavaCodeGenUtil.getQuotedString(name))
        .append(",\n")
        .append(JavaCodeGenUtil.INDENTATION)
        .append(Type.class.getSimpleName())
        .append(".")
        .append(propType.toString())
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
   * Returns the name of variable holding either the property or group property class name.
   * That name may be the default static (to upperc case with underscores) variable name for the property,
   * if it stands for a simple property, or the property name with first letter to upper case e.g. associated class
   * name if it stands for a group property.
   * <p>
   * In case the variable name clashes with another property or group property in the same class, which means 
   * two properties have same name ignoring case and dots, underscores will be appended to the variable (or class) name.
   * 
   * @param property  the property to generate the property key property name for, for instance 'myProperty'.
   * @param sieblings the list of properties declared in the same class as the property, used to avoid name clashes
   * @return the property key property name, for instance 'myPropertyProperty'
   */
  public static String getPropertyVariableName(ConfigPropertyDescriptor property, List<ConfigPropertyDescriptor> sieblings) {
    if (property == null) {
      return null;
    }
    sieblings = CollectionUtil.emptyIfNull(sieblings);
    int off = sieblings.indexOf(property);
    if (off < 0 ) {
      throw new IllegalArgumentException("Property '" + property.getName() + "' is not part of the sieblings list: " + sieblings);
    }
    return getPropertyVariableName(property, sieblings, off, CollectionUtil.createList(sieblings.size() + 1));
  }
  
  private static String getPropertyVariableName(
      ConfigPropertyDescriptor property, 
      List<ConfigPropertyDescriptor> sieblings, 
      int index, 
      List<String> propertyNames) {
    if (index >= propertyNames.size()) {
      StringBuilder name = new StringBuilder()
          .append(getPropertyDefaultVariableName(property));
      sieblings = CollectionUtil.emptyIfNull(sieblings);
      while (hasDifferentPropertyWithSameVariableName(
                property,
                name.toString(),  
                sieblings,
                propertyNames)) {
        name.append("_");
      }
      propertyNames.add(name.toString());
    }
    
    return propertyNames.get(index);
  }
  
  private static boolean hasDifferentPropertyWithSameVariableName(
      ConfigPropertyDescriptor property, 
      String staticVariableName,
      List<ConfigPropertyDescriptor> sieblings,
      List<String> propertyNames) {
    List<ConfigPropertyDescriptor> l = CollectionUtil.emptyIfNull(sieblings);
    for (int i = 0; i < l.size(); i++) {
      ConfigPropertyDescriptor p = l.get(i);
      if (p == property) {
        return false;
      }
      if (getPropertyVariableName(p, sieblings, i, propertyNames).equals(staticVariableName)) {
        return true;
      }
    }
    return false;
  }
  
  private static String getPropertyDefaultVariableName(ConfigPropertyDescriptor property) {
    String name = property.getName();
    if (name.contains(".")) {
      name = StringUtils.substringAfterLast(name, ".");
    }
    if (isGroup(property)) {
      return JavaCodeGenUtil.firstLetterToUpperCase(name);
    }
    return JavaCodeGenUtil.getStaticVariableName(name);
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
    Type type = getType(property);
    if (isGroup(property)) {
      type = Type.STRING;
    }
    return JavaCodeGenUtil.getGetAccessorMethodName(
        name, 
        type, 
        CollectionUtil.emptyIfNull(allProperties).stream()
          .map(p -> p.getName())
          .collect(Collectors.toList())
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
  
  /**
   * Returns <code>true</code> if the given property is a group of properties,
   * i.e. it contains nested properties.
   * 
   * @param property the property to check
   * @return <code>true</code> if the given property is a group of properties,
   *         otherwise <code>false</code>
   */
  public static boolean isGroup(ConfigPropertyDescriptor property) {
    return property != null 
        && property.getProperties() != null;
  }
  
  public static Type getType(ConfigPropertyDescriptor property) {
    if (property == null) {
      return null;
    }
    if (property.getType() == null) {
      if (isGroup(property)) {
        return Type.OBJECT;
      }
      return Type.STRING;
    }
    return Type.fromTypeName(property.getType());
  }

}
