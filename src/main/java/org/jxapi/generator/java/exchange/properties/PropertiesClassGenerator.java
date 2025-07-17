package org.jxapi.generator.java.exchange.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import org.jxapi.exchange.descriptor.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.generator.html.HtmlGenerationUtil;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.JavaTypeGenerator;
import org.jxapi.generator.java.exchange.ExchangeGenUtil;
import org.jxapi.generator.java.exchange.constants.ConstantsGenUtil;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.ConfigProperty;
import org.jxapi.util.PlaceHolderResolver;
import org.jxapi.util.PropertiesUtil;

/**
 * Generates a Java class exposing {@link ConfigProperty} instances and
 * <code>static</code> methods for retrieving value of configuration properties
 * of a given exchange.
 * <p>
 * Example:
 * 
 * <pre>
 * {@code
 *  public class MyProperties {
 *  
 *    private MyProperties(){}
 *  
 *    public static final ConfigProperty HOST = DefaultConfigProperty.create(
 *      "host",
 *      Type.STRING,
 *      "Mock HTTP server host",
 *      "localhost");
 *      
 *    public static final ConfigProperty HTTP_PORT = DefaultConfigProperty.create(
 *      "httpPort",
 *      Type.INT,
 *      "Mock HTTP/Websocket server port",
 *      80);
 * 
 *    public static String getHost(Properties properties) {return PropertiesUtil.getStringProperty(properties, HOST.getName(), HOST.getDefaultValue());}
 *
 *    public static Integer getHttpPort(Properties properties) {return PropertiesUtil.getIntProperty(properties, HTTP_PORT.getName(), HTTP_PORT.getDefaultValue());}
 *    
 *   }
 * }  
 * </pre>
 * 
 * Where {@code MyProperties} is the interface name, {@code MY_STRING},
 * {@code MY_INT} and {@code MY_TIMESTAMP} are the property names,
 * {@code String}, {@code Integer} and {@code Long} are the types of the
 * properties and {@code "myString"}, {@code 42} and
 * {@code System.currentTimeMillis()} are the default values of the properties.
 */
public class PropertiesClassGenerator extends JavaTypeGenerator {
  
  private final String exchangeName;
  private final List<ConfigPropertyDescriptor> properties;
  private final PlaceHolderResolver docPlaceHolderResolver;
  private final PlaceHolderResolver sampleValuePlaceHolderResolver;

  /**
   * Constructor
   * 
   * @param fullClassName          Full name of the interface to generate,
   *                               example: com.example.MyProperties
   * @param exchangeName           The name of exchange configuration properties
   *                               are generated for
   * @param properties             List of properties to generate in the interface
   * @param docPlaceHolderResolver PlaceHolderResolver to resolve placeholders in
   */
  public PropertiesClassGenerator(String fullClassName, 
                                  ExchangeDescriptor exchange, 
                                  List<ConfigPropertyDescriptor> properties) {
    super(fullClassName);
    this.exchangeName = exchange.getId();
    this.properties = properties;
    this.sampleValuePlaceHolderResolver = s -> ExchangeGenUtil.generateSubstitutionInstructionDeclaration(
        s, 
        exchange, 
        null,
        getImports());
    this.docPlaceHolderResolver = PlaceHolderResolver.create(ExchangeGenUtil.getDescriptionReplacements(exchange, null));
    setTypeDeclaration("public class");
    setDescription(generateDescription());
  }
  
  private String generateDescription() {
    StringBuilder sb = new StringBuilder();
    sb.append("Configurable properties for <strong>")
      .append(exchangeName)
      .append("</strong> exchange:<br>\n");
      
    List<String> columns = List.of("Name", "Type", "Description", "Default value");
    List<List<String>> rows = new ArrayList<>();
    addDescriptionRows(rows, "", properties);
    sb.append(HtmlGenerationUtil.generateTable(exchangeName + " properties", columns, rows))
      .append("<br>\nExposes helper methods are available to retrieve value of each of these properties ")
      .append("with right type, returning default value if not present in properties.")
      .append("\n@see ConfigProperty");
    return sb.toString();
  }
  
  private void addDescriptionRows(List<List<String>> rows, String pfx, List<ConfigPropertyDescriptor> properties) {
    for (ConfigPropertyDescriptor p : CollectionUtil.emptyIfNull(properties)) {
      String pName = pfx + String.valueOf(p.getName());
      if (p.isGroup()) {
        addDescriptionRows(rows, pName + ".", p.getProperties());
      } else {
        String pType = String.valueOf(p.getType());
        String pDesc = Optional.ofNullable(p.getDescription()).orElse("");
        pDesc = docPlaceHolderResolver.resolve(pDesc);
        String pDef = Optional.ofNullable(p.getDefaultValue()).orElse("").toString();
        pDef = docPlaceHolderResolver.resolve(pDef);
        rows.add(List.of(pName, pType, pDesc, pDef));
      }
    }
  }

  @Override
  public String generate() {
    appendToBody("\nprivate ")
    .append(getSimpleName())
    .append("(){}\n");
    properties
      .forEach(c -> appendToBody("\n")
                      .append(ConstantsGenUtil.getPropertyValueDeclaration(
                          c, 
                          getImports(), 
                          docPlaceHolderResolver, 
                          sampleValuePlaceHolderResolver)));
    this.properties.forEach(this::generatePropertyGetterMethod);
    appendToBody(PropertiesGenUtil.generateAllPropertiesListMethod(properties, getImports()));
    return super.generate();
  }

//  private void generateAllPropertiesListMethod() {
//    addImport(List.class);
//    addImport(ConfigProperty.class);
//    addImport(CollectionUtil.class);
//    appendToBody("\n")
//      .append(JavaCodeGenUtil.generateJavaDoc("List of all configuration properties defined in this class"))
//      .append("\npublic static final List<ConfigProperty> ")
//      .append(ALL_PROPERTY)
//      .append(" = ")
//      .append("List.copyOf(")
//      .append(CollectionUtil.class.getSimpleName())
//      .append(".mergeLists(List.of(\n");
//    
//    boolean inList = false;
//    String indent = JavaCodeGenUtil.INDENTATION;
//    String dblIndent = indent + indent;
//    boolean first = true;
//    for (ConfigPropertyDescriptor property : properties) {
//      if (!first) {
//        appendToBody(",\n");
//      } else {
//        first = false;
//      }
//      if (inList) {
//        if (property.isGroup()) {
//          // End of group, close the list
//          appendToBody(indent)
//          .append("),\n")
//          .append(indent);
//          inList = false;
//        }
//      } else {
//        if (!property.isGroup()) {
//          // Not a group, not in list, start a new list
//          inList = true;
//          appendToBody("List.of(\n")
//            .append(dblIndent);
//        }
//      }
//      
//      if (property.isGroup()) {
//        appendToBody(JavaCodeGenUtil.firstLetterToUpperCase(property.getName()))
//            .append(".").append(ALL_PROPERTY);
//      }
//      appendToBody(indent)
//          .append(JavaCodeGenUtil.getStaticVariableName(ConstantsGenerationUtil.getPropertyKeyPropertyName(property)));
//    }
//    if (inList) {
//      // Close the last list
//      appendToBody(")");
//    }
//    appendToBody("));");
////      "ALL = List.of(\n")
////      .append(JavaCodeGenUtil.INDENTATION)
////      .append(properties.stream()
////        .map(p -> JavaCodeGenUtil.getStaticVariableName(ConstantsGenerationUtil.getPropertyKeyPropertyName(p)))
////        .collect(Collectors.joining(", \n" + JavaCodeGenUtil.INDENTATION)))
////      .append(");");
//  }

  private void generatePropertyGetterMethod(ConfigPropertyDescriptor property) {
    StringBuilder sb = new StringBuilder()
        .append("\n");
    String name = property.getName();
    Type type = property.getType();
    Object def = property.getDefaultValue();
    StringBuilder desc = new StringBuilder()
        .append("Retrieves value of '")
        .append(name)
        .append("' property.\n")
        .append("@param properties Properties to look for value of '")
        .append(name)
        .append("' property into.\n")
        .append("@return Value found in properties or ");
    if (def != null) {
      desc.append("default value '")
        .append(property.getDefaultValue())
        .append("'");
        
    } else {
      desc.append("<code>")
        .append(JavaCodeGenUtil.NULL)
        .append("</code>");
    }
    desc.append(" if not found.");
        
    sb.append(JavaCodeGenUtil.generateJavaDoc(desc.toString()))
      .append("\n");
    String typeClass = ExchangeGenUtil.getClassNameForType(type, getImports(), null);
    String methodName = JavaCodeGenUtil.getGetAccessorMethodName(
                  name, 
                  typeClass, 
                  properties.stream().map(p -> p.getName()).collect(Collectors.toList())
              );
    String getPropertiesUtilMethodName = getPropertiesUtilGetPropertyMethodName(property);
    String keyVariableName = JavaCodeGenUtil.getStaticVariableName(ConstantsGenUtil.getPropertyKeyPropertyName(property));
    addImport(Properties.class);
    addImport(PropertiesUtil.class);
    sb.append("public static ")
      .append(typeClass)
      .append(" ")
      .append(methodName)
      .append("(Properties properties) {return PropertiesUtil.")
      .append(getPropertiesUtilMethodName)
      .append("(properties, ")
      .append(keyVariableName)
      .append(");}\n");
    appendToBody(sb.toString());
  }
  
  private String getPropertiesUtilGetPropertyMethodName(ConfigPropertyDescriptor property) {
    switch (property.getType().getCanonicalType()) {
    case BIGDECIMAL:
      return "getBigDecimal";
    case BOOLEAN:
      return "getBoolean";
    case INT:
      return "getInt";
    case LONG:
      return "getLong";
    default:
      return "getString";
    }
  }
}
