package org.jxapi.generator.java.exchange.properties;

import java.util.List;

import org.jxapi.exchange.descriptor.ConfigPropertyDescriptor;
import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.constants.ConstantsGenUtil;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.ConfigProperty;
import org.jxapi.util.DefaultConfigProperty;

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
           .append(JavaCodeGenUtil.getStaticVariableName(ConstantsGenUtil.getPropertyKeyPropertyName(property)));
      }
  }


}
