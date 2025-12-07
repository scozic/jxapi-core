package org.jxapi.generator.java.exchange.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.generator.html.HtmlGenUtil;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.JavaTypeGenerator;
import org.jxapi.generator.java.exchange.ExchangeGenUtil;
import org.jxapi.generator.java.pojo.PojoGenUtil;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.ConfigProperty;
import org.jxapi.util.JsonUtil;
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
  
  private final ExchangeDescriptor exchange;
  private final List<ConfigPropertyDescriptor> properties;
  private final String prefix;
  private final PlaceHolderResolver docPlaceHolderResolver;
  private final PlaceHolderResolver sampleValuePlaceHolderResolver;

  /**
   * Constructor
   * 
   * @param fullClassName          Full name of the interface to generate,
   *                               example: com.example.MyProperties
   * @param exchange               The exchange descriptor configuration 
   *                               properties are generated for
   * @param properties             List of properties to generate in the interface
   * @param prefix                 Prefix to use for property names, can be empty 
   *                               or <code>null</code>
   */
  public PropertiesClassGenerator(String fullClassName, 
                                  ExchangeDescriptor exchange, 
                                  List<ConfigPropertyDescriptor> properties,
                                  String prefix) {
    super(fullClassName);
    this.exchange = exchange;
    this.properties = properties;
    this.prefix = prefix;
    List<ConfigPropertyDescriptor> demoProperties = StringUtils.defaultString(prefix).startsWith("demo")? properties: List.of();
    this.sampleValuePlaceHolderResolver = s -> ExchangeGenUtil.generateSubstitutionInstructionDeclaration(
        s, 
        exchange, 
        demoProperties,
        null,
        getImports());
    this.docPlaceHolderResolver = PlaceHolderResolver.create(ExchangeGenUtil.getDescriptionReplacements(exchange, null));
    setTypeDeclaration("public class");
    setDescription(generateExchangePropertiesClassDescription());
  }
  
  private String generateExchangePropertiesClassDescription() {
    StringBuilder sb = new StringBuilder();
    sb.append("Configurable")
      .append(StringUtils.isEmpty(prefix) ? "": " " + prefix)
      .append(" properties for <strong>")
      .append(exchange.getId())
      .append("</strong> exchange:<br>\n");
      
    List<String> columns = List.of("Name", "Type", "Description", "Default value");
    List<List<String>> rows = new ArrayList<>();
    addDescriptionRows(rows, "", properties);
    sb.append(HtmlGenUtil.generateTable(exchange.getId() + " properties", columns, rows))
      .append("\n<br>\nExposes helper methods are available to retrieve value of each of these properties ")
      .append("with right type, returning default value if not present in properties.")
      .append("\n@see ConfigProperty");
    return sb.toString();
  }
  
  private void addDescriptionRows(List<List<String>> rows, String pfx, List<ConfigPropertyDescriptor> properties) {
    for (ConfigPropertyDescriptor p : CollectionUtil.emptyIfNull(properties)) {
      String pName = pfx + String.valueOf(p.getName());
      if (PropertiesGenUtil.isGroup(p)) {
        addDescriptionRows(rows, pName + ".", p.getProperties());
      } else {
        Type pType = p.getType() == null ? Type.STRING : Type.fromTypeName(p.getType());
        String pDesc = Optional.ofNullable(p.getDescription()).orElse("");
        pDesc = docPlaceHolderResolver.resolve(pDesc);
        String pDef = Optional.ofNullable(p.getDefaultValue()).orElse("").toString();
        pDef = docPlaceHolderResolver.resolve(pDef);
        rows.add(List.of(pName, pType.toString(), pDesc, pDef));
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
                      .append(generatePropertyDeclaration(c, this.prefix)));
    this.properties.forEach(this::generatePropertyGetterMethod);
    appendToBody(PropertiesGenUtil.generateAllPropertiesListMethod(properties, getImports()));
    return super.generate();
  }

  private String generatePropertyDeclaration(ConfigPropertyDescriptor property, String prefix) {
    if (PropertiesGenUtil.isGroup(property)) {
      StringBuilder s = new StringBuilder();
      String groupClassName = PropertiesGenUtil.getPropertyVariableName(property, properties);
      PropertiesClassGenerator groupGen = new PropertiesClassGenerator(getName() + "." + groupClassName,
          exchange, // No exchange descriptor for group properties
          property.getProperties(),
          PropertiesGenUtil.getPropertyFullName(prefix, property.getName()));
      groupGen.setTypeDeclaration("public static class");
      groupGen.setGeneratePackageAndImports(false);
      groupGen.setDescription(property.getDescription());
      s.append(groupGen.generate());
      this.getImports().addAll(groupGen.getImports());
      return s.toString();
    } else {
      return PropertiesGenUtil.generateSimplePropertyValueDeclaration(
          property, 
          this.properties,
          prefix,
          getImports(), 
          docPlaceHolderResolver, 
          sampleValuePlaceHolderResolver);
    }
  }
  
  private void generatePropertyGetterMethod(ConfigPropertyDescriptor property) {
    if (PropertiesGenUtil.isGroup(property)) {
      return;
    }
    StringBuilder sb = new StringBuilder()
        .append("\n");
    String name = property.getName();
    Type type = PropertiesGenUtil.getType(property);
    Object def = property.getDefaultValue();
    if (!type.getCanonicalType().isPrimitive) {
      type = Type.STRING;
      def = JsonUtil.pojoToJsonString(def);
    }
    StringBuilder desc = new StringBuilder()
        .append("Retrieves value of '")
        .append(name)
        .append("' property.\n")
        .append("@param properties Properties to look for value of '")
        .append(name)
        .append("' property into.\n")
        .append("@return Value found in properties or ");
    if (def != null) {
      desc.append("default value <i>")
        .append(docPlaceHolderResolver.resolve(def.toString()))
        .append("</i>");
        
    } else {
      desc.append("<code>")
        .append(JavaCodeGenUtil.NULL)
        .append("</code>");
    }
    desc.append(" if not found.");
        
    sb.append(JavaCodeGenUtil.generateJavaDoc(desc.toString()))
      .append("\n");
    String typeClass = PojoGenUtil.getClassNameForType(type, getImports(), null);
    String methodName = PropertiesGenUtil.getPropertyGetterMethodName(property, properties);
    String getPropertiesUtilMethodName = getPropertiesUtilGetPropertyMethodName(property);
    String keyVariableName = PropertiesGenUtil.getPropertyVariableName(property, this.properties);
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
    switch (PropertiesGenUtil.getType(property).getCanonicalType()) {
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
