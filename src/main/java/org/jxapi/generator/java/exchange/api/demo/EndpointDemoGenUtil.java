package org.jxapi.generator.java.exchange.api.demo;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.jxapi.exchange.Exchange;
import org.jxapi.exchange.ExchangeApi;
import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor;
import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ExchangeGenUtil;
import org.jxapi.generator.java.exchange.api.ExchangeApiGenUtil;
import org.jxapi.generator.java.pojo.PojoGenUtil;
import org.jxapi.pojo.descriptor.CanonicalType;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.DemoUtil;

/**
 * Helper methods around REST or Websocket endpoint demo snippets code generation.
 */
public class EndpointDemoGenUtil {
  
  /**
   * Name of the demo configuration property group for REST endpoints.
   */
  public static final String REST_DEMO_GROUP_NAME = "rest";
  
  /**
   * Name of the demo configuration property group for Websocket endpoints.
   */
  public static final String WEBSOCKET_DEMO_GROUP_NAME = "ws";
  
  /**
   * Name of the properties argument for methods that create request or response
   * sample values.
   */
  public static final String CREATE_REQUEST_PROPERTIES_ARG_NAME = "properties";
  
  private EndpointDemoGenUtil() {}
  
  /**
   * Generates a method (with full signature and method body) that creates a
   * sample value for the given {@link Field} (which can be the data type
   * definition of a REST/Websocket request, response/message, or of a nested
   * {@link Type#OBJECT} property.
   *    
   * @param property               the property for which to generate the creation
   *                               method.
   * @param objectClassName        Default class name for property value, relevant
   *                               when property is an object type and has no object
   *                               name.
   * @param exchangeDescriptor     the exchange descriptor containing the API.
   * @param exchangeApiDescriptor  the API descriptor containing the endpoint.
   * @param apiEndpointGroupName   the name of the API endpoint group containing the endpoint.
   * @param endpointName           the name of the endpoint containing the property.
   * @param demoConfigProperties   the list of demo configuration properties for
   *                               the exchange.
   * @param imports                the set of imports to add to the class containing
   *                               the method. Will be populated with the necessary
   *                               imports.
   * @return the method code with signature (method declaration) and body
   * 
   * @see #generateFieldCreationMethodDeclaration(Field, String, String, Imports)
   */
  public static String generateFieldCreationMethod(
      Field property, 
      String objectClassName,
      ExchangeDescriptor exchangeDescriptor,
      ExchangeApiDescriptor exchangeApiDescriptor,
      String apiEndpointGroupName,
      String endpointName,
      List<ConfigPropertyDescriptor> demoConfigProperties,
      Imports imports) {
    String fieldName = ExchangeApiGenUtil.getRequestArgName(property.getName());
    String demoPropertyName = StringUtils.join(List.of(
        exchangeApiDescriptor.getName(),
        apiEndpointGroupName,
        endpointName, 
        fieldName), 
        ".");
    return new StringBuilder()
        .append(generateFieldCreationMethodDeclaration(
            property, 
            objectClassName, 
            fieldName,
            imports))
        .append(" ")
        .append(JavaCodeGenUtil.generateCodeBlock(
              "return " 
              + generateFieldSampleValueDeclaration(
                  property,  
                  demoPropertyName,
                  objectClassName,
                  exchangeDescriptor,
                  exchangeApiDescriptor,
                  demoConfigProperties,
                  imports) 
              + ";"))
        .toString();
  }
  
  /**
   * Generates the method declaration for a method that creates a sample value for
   * the given endpoint property.
   * 
   * @param field                  the endpoint property for which to generate the
   *                               creation method.
   * @param defaultObjectClassName Default class name for property value, relevant
   *                               when property is an object type and has no
   *                               object name.
   * @param defaultFieldName       The name to use for field, either 'request' or
   *                               'response' when the field name is
   *                               <code>null</code>.
   * @param imports                the set of imports to add to the class
   *                               containing the method. Will be populated with
   *                               the necessary imports.
   * @return the method declaration
   */
  public static String generateFieldCreationMethodDeclaration(
      Field field, 
      String defaultObjectClassName,
      String defaultFieldName,
      Imports imports) {
    Type type = PojoGenUtil.getFieldType(field);
    String fieldClassName =  PojoGenUtil.getClassNameForType(
                        type, 
                        imports, 
                        defaultObjectClassName);
    imports.add(Properties.class);
    String fieldName = Optional.ofNullable(field.getName()).orElse(defaultFieldName);
    StringBuilder doc = new StringBuilder()
        .append("Creates a sample value for the ")
        .append(fieldName)
        .append(" field of type ")
        .append(fieldClassName)
        .append(" using sample value(s) defined in demo configuration properties.\n\n")
        .append("@param ")
        .append(CREATE_REQUEST_PROPERTIES_ARG_NAME)
        .append(" the configuration properties to use for the sample value generation.");
    return new StringBuilder()
                  .append(JavaCodeGenUtil.generateJavaDoc(doc.toString()))
                  .append("\npublic static ")
                  .append(fieldClassName)
                  .append(" ")
                  .append(generateFieldCreationMethodName(field))
                  .append("(Properties ")
                  .append(CREATE_REQUEST_PROPERTIES_ARG_NAME)
                  .append(")")
                  .toString();
  }
  
  /**
   * Generates the method name for a method that creates a sample value for the
   * given endpoint property.
   * 
   * @param field the field for which to generate the
   *                          creation method.
   * @return the method name
   */
  public static String generateFieldCreationMethodName(Field field) {
    return "create" + JavaCodeGenUtil.firstLetterToUpperCase(Optional.ofNullable(field.getName()).orElse("request"));
  }


  
  private static String generateFieldSampleValueDeclaration(
      Field field, 
      String demoPropertyName,
      String objectClassName, 
      ExchangeDescriptor exchangeDescriptor,
      ExchangeApiDescriptor exchangeApiDescriptor,
      List<ConfigPropertyDescriptor> demoConfigProperties,
      Imports imports) {
    Type type = PojoGenUtil.getFieldType(field);
    if (type.getCanonicalType() == CanonicalType.LIST 
        || type.getCanonicalType() == CanonicalType.MAP) {
      return generateFieldSampleValueDeclaration2MapOrList(
          field, 
          demoPropertyName, 
          objectClassName, 
          exchangeDescriptor,
          exchangeApiDescriptor, 
          demoConfigProperties, 
          imports);
    }
    if (type.isObject()) {
      return generateObjectFieldSampleValueDeclaration(
          field, 
          demoPropertyName, 
          objectClassName, 
          exchangeDescriptor,
          exchangeApiDescriptor, 
          demoConfigProperties, 
          imports);
    }
    return ExchangeGenUtil.getValueDeclarationForConfigProperty(
        demoPropertyName, 
        exchangeDescriptor, 
        demoConfigProperties, 
        CREATE_REQUEST_PROPERTIES_ARG_NAME, 
        imports);
  }
  
  private static String generateFieldSampleValueDeclaration2MapOrList(
      Field field, 
      String demoPropertyName,
      String objectClassName, 
      ExchangeDescriptor exchangeDescriptor,
      ExchangeApiDescriptor exchangeApiDescriptor,
      List<ConfigPropertyDescriptor> demoConfigProperties,
      Imports imports) {
    Type type = PojoGenUtil.getFieldType(field);
    String deserializeSampleValueInstr = generateDeserializeSampleValueInstruction(
        field, 
        demoPropertyName, 
        objectClassName, 
        exchangeDescriptor, 
        demoConfigProperties, 
        imports);
    if (type.isObject()) {
      return JavaCodeGenUtil.generateOptionalOfNullableStatement(
          deserializeSampleValueInstr, 
          generateObjectListOrMapWithoutGlobalSampleValueInstruction(
            field, 
            demoPropertyName, 
            objectClassName, 
            exchangeDescriptor, 
            exchangeApiDescriptor, 
            demoConfigProperties, 
            imports), 
          imports,
          true);
    }
    return deserializeSampleValueInstr;
  }
  
  private static String generateDeserializeSampleValueInstruction(
      Field field, 
      String demoPropertyName,
      String objectClassName, 
      ExchangeDescriptor exchangeDescriptor, 
      List<ConfigPropertyDescriptor> demoConfigProperties, 
      Imports imports) {
    Type type = PojoGenUtil.getFieldType(field);
    return new StringBuilder()
        .append(PojoGenUtil.getNewJsonFieldDeserializerInstruction(
            type, 
            objectClassName, 
            PojoGenUtil.isExternalClassObjectField(field), 
            imports))
        .append(".deserialize(")
        .append(ExchangeGenUtil.getValueDeclarationForConfigProperty(
            demoPropertyName,
            exchangeDescriptor, 
            demoConfigProperties, 
            CREATE_REQUEST_PROPERTIES_ARG_NAME, 
            imports))
        .append(")")
        .toString();
  }
  
  private static String generateObjectListOrMapWithoutGlobalSampleValueInstruction(Field field, String demoPropertyName,
      String objectClassName, ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor exchangeApiDescriptor,
      List<ConfigPropertyDescriptor> demoConfigProperties, Imports imports) {
    Type type = PojoGenUtil.getFieldType(field);
    if (type.getCanonicalType() == CanonicalType.MAP) {
      return null;
    }
    if (type.getCanonicalType() == CanonicalType.LIST) {
      Field itemField = field.deepClone();
      itemField.setType(type.getSubType());
      String subFieldInstruction = generateObjectListOrMapWithoutGlobalSampleValueInstruction(
          itemField, 
          demoPropertyName, 
          objectClassName, 
          exchangeDescriptor, 
          exchangeApiDescriptor, 
          demoConfigProperties, 
          imports);
      if (subFieldInstruction == null) {
        return null; // No sample value to set
      }
      imports.add(List.class);
      return "List.of(" + subFieldInstruction + ")";
    }
    // else: leaf object type
    return generateSampleObjectBuilderInstruction(
        field, 
        demoPropertyName, 
        objectClassName,
        exchangeDescriptor, 
        exchangeApiDescriptor, 
        demoConfigProperties, 
        imports);
  }
  
  private static String generateObjectFieldSampleValueDeclaration(Field field, 
      String demoPropertyName,
      String objectClassName, 
      ExchangeDescriptor exchangeDescriptor,
      ExchangeApiDescriptor exchangeApiDescriptor,
      List<ConfigPropertyDescriptor> demoConfigProperties,
      Imports imports) {
    return JavaCodeGenUtil.generateOptionalOfNullableStatement(
        generateDeserializeSampleValueInstruction(
            field, 
            demoPropertyName, 
            objectClassName, 
            exchangeDescriptor,
            demoConfigProperties, 
            imports),
        generateObjectListOrMapWithoutGlobalSampleValueInstruction(
            field, 
            demoPropertyName, 
            objectClassName,
            exchangeDescriptor, 
            exchangeApiDescriptor, 
            demoConfigProperties, 
            imports),
        imports,
        true);
  }
  
  private static String generateSampleObjectBuilderInstruction(Field field, 
      String demoPropertyName,
      String objectClassName, 
      ExchangeDescriptor exchangeDescriptor,
      ExchangeApiDescriptor exchangeApiDescriptor,
      List<ConfigPropertyDescriptor> demoConfigProperties,
      Imports imports) {
    StringBuilder s = new StringBuilder();
    imports.add(objectClassName);
    s.append(JavaCodeGenUtil.getClassNameWithoutPackage(objectClassName))
     .append(".builder()");
    StringBuilder builderInstruction = new StringBuilder();
    builderInstruction.append("\n");
    List<Field> properties = ExchangeApiGenUtil.resolveFieldProperties(exchangeApiDescriptor, field).getProperties();
    for (Field childParam : properties) {
      String childParamName = childParam.getName();
      String childDemoPropertyName = demoPropertyName + "." + childParamName;
      String childClassName = PojoGenUtil.getFieldObjectClassName(childParam, objectClassName);
      String itemValue = generateFieldSampleValueDeclaration(
          childParam,
          childDemoPropertyName, 
          childClassName,
          exchangeDescriptor, 
          exchangeApiDescriptor, 
          demoConfigProperties, 
          imports);
      if (itemValue != null) {
        builderInstruction
          .append(".")
          .append(childParamName)
          .append("(")
          .append(itemValue)
          .append(")\n");
      }
    }
    return s
        .append(JavaCodeGenUtil.indent(builderInstruction.toString()))
        .append(".build()")
        .toString();
  }

  /**
   * Generates the class name for the REST API demo class.
   * <ul>
   * <li>The returned class package name is the base package of the exchange
   * descriptor + the API name + ".demo".</li>
   * <li>The returned class name is the concatenation of the exchange name, the
   * API name and the REST API name, and <code>Demo</code> with the first letter
   * of each word capitalized.</li>
   * </ul>
   * 
   * Example:
   * <code>org.jxapi.exchange.api.demo.MyExchangeMyApiMyRestEndpointDemo</code>
   * 
   * @param exchangeDescriptor    The exchange descriptor containing the API.
   * @param exchangeApiDescriptor The API descriptor containing the REST API.
   * @param restApi               The REST API descriptor.
   * @return the class name for the REST API demo class.
   */
  public static String getRestApiDemoClassName(ExchangeDescriptor exchangeDescriptor, 
                         ExchangeApiDescriptor exchangeApiDescriptor, 
                         RestEndpointDescriptor restApi) {
    String pkgPrefix =  exchangeDescriptor.getBasePackage() 
                          + "." 
                          + exchangeApiDescriptor.getName().toLowerCase() 
                          + ".demo.";
    return pkgPrefix + JavaCodeGenUtil.firstLetterToUpperCase(exchangeDescriptor.getId()) 
                   + JavaCodeGenUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName())
                   + JavaCodeGenUtil.firstLetterToUpperCase(restApi.getName())
                   + "Demo";
  }

  /**
   * Generates the class name for the Websocket API demo class.
   * <ul>
   * <li>The returned class package name is the base package of the exchange
   * descriptor + the API name + ".demo".</li>
   * <li>The returned class name is the concatenation of the exchange name, the
   * API name and the Websocket API name, and <code>Demo</code> with the first
   * letter of each word capitalized.</li>
   * </ul>
   * 
   * Example:
   * <code>org.jxapi.exchange.api.demo.MyExchangeMyApiMyWebsocketEndpointDemo</code>
   * 
   * @param exchangeDescriptor    The exchange descriptor containing the API.
   * @param exchangeApiDescriptor The API descriptor containing the Websocket API.
   * @param websocketApi          The Websocket API descriptor.
   * @return the class name for the Websocket API demo class.
   */
  public static String getWebsocketApiDemoClassName(ExchangeDescriptor exchangeDescriptor, 
                            ExchangeApiDescriptor exchangeApiDescriptor, 
                            WebsocketEndpointDescriptor websocketApi) {
    String pkgPrefix =  exchangeDescriptor.getBasePackage() 
                          + "." + exchangeApiDescriptor.getName().toLowerCase() 
                          + ".demo.";
    return pkgPrefix + JavaCodeGenUtil.firstLetterToUpperCase(exchangeDescriptor.getId()) 
                   + JavaCodeGenUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName())
                   + JavaCodeGenUtil.firstLetterToUpperCase(websocketApi.getName())
                   + "Demo";
  }
  
  /**
   * Generates a variable declaration instruction referencing a test API group ( {@link ExchangeApi}), 
   * assuming there is a variable referencing an {@link Exchange} instance, that exposes a getter 
   * method to retrieve tha {@link ExchangeApi} instance. 
   * 
   * Example:
   * <code>MyApi api = exchange.getMyApi();</code>
   * 
   * @param exchangeVariableName The name of variable referencing an {@link Exchange} instance.
   * @param apiVariableName      The name of the variable to declare for the test API instance.
   * @param simpleApiClassName    The simple {@link ExchangeApi} interface class name.
   * @param apiGetterMethodName  The getter method name in {@link Exchange} interface to retrieve the API instance.
   * @return the new test API instantiation instruction.
   * 
   * @see Exchange
   * @see ExchangeApi
   */
  public static String getNewTestApiInstruction(String exchangeVariableName,
                                                String apiVariableName,
                                                String simpleApiClassName,
                                                String apiGetterMethodName) {
    return new StringBuilder()
        .append(simpleApiClassName)
        .append(" ")
        .append(apiVariableName)
        .append(" = ")
        .append(exchangeVariableName)
        .append(".")
        .append(apiGetterMethodName)
        .append("();")
        .toString();
  }
  
  /**
   * Generates a variable declaration instruction referencing a new {@link Exchange} instance, using the 
   * provided exchange class name, exchange variable name and properties variable name.
   * The exchange instance is created with a unique ID containing exchange ID (see {@link Exchange#getId()}), 
   * assuming this ID is available as Exchange interface static variable named <code>ID</code>
   *  and the configuration properties passed from a variable referencing them.
   * @param exchangeClassName The {@link Exchange} interface class name.
   * @param exchangeVariableName The name of the variable to declare for the new exchange instance.
   * @param propertiesVariableName The name of the variable referencing the configuration properties to use for the new exchange.
   * @return the new test exchange instantiation instruction, for instance:<br>
   *        <code>MyExchange exchange = new MyExchangeImpl("test-MyExchange.ID", properties);</code>
   */
  public static String getNewTestExchangeInstruction(String exchangeClassName, 
                                                     String exchangeVariableName, 
                                                     String propertiesVariableName) {
    String simpleExchangeClassName = JavaCodeGenUtil.getClassNameWithoutPackage(exchangeClassName);
    String exchangeImplClassName = ExchangeGenUtil.getExchangeInterfaceImplementationName(exchangeClassName);
    String simpleExchangeImplClassName = JavaCodeGenUtil.getClassNameWithoutPackage(exchangeImplClassName);
    return new StringBuilder()
        .append(simpleExchangeClassName)
        .append(" ")
        .append(exchangeVariableName)
        .append(" = new ")
        .append(simpleExchangeImplClassName)
        .append("(\"test-\" + ").append(simpleExchangeClassName)
        .append(".ID, ")
        .append(propertiesVariableName)
        .append(");")
        .toString();
  }
  
  /**
   * Generates {@link DemoUtil#loadDemoExchangeProperties(String)} instruction, using <code>ID</code> constant 
   * generated in exchange interface name as value of expected <code>exchangeId</code> argument
   * @param simpleExchangeClassName Simple (without package) class name of generated {@link Exchange} 
   * @param imports The imports that can be populated in generation context
   * @return Instruction call to {@link DemoUtil#loadDemoExchangeProperties(String)}
   */
  public static String getTestPropertiesInstruction(String simpleExchangeClassName, Imports imports) {
    imports.add(DemoUtil.class);
    return new StringBuilder()
        .append(DemoUtil.class.getSimpleName())
        .append(".loadDemoExchangeProperties(")
        .append(simpleExchangeClassName)
        .append(".ID)")
        .toString();
  }

  /**
   * Collects the demo configuration properties for all endpoints of all APIs of the given exchange descriptor.
   * @param exchangeDescriptor the exchange descriptor containing the APIs and endpoints
   * @return the list of demo configuration properties for all endpoints of all APIs of the given exchange descriptor.
   */
  public static List<ConfigPropertyDescriptor> collectDemoConfigProperties(ExchangeDescriptor exchangeDescriptor) {
      return CollectionUtil.emptyIfNull(exchangeDescriptor.getApis()).stream()
                .map(EndpointDemoGenUtil::createDemoConfigPropertyGroup)
                .filter(o -> !CollectionUtil.isEmpty(o.getProperties()))
                .toList();
  }
  
  private static ConfigPropertyDescriptor createDemoConfigPropertyGroup(ExchangeApiDescriptor api) {
    String apiName = api.getName();
    return ConfigPropertyDescriptor.builder()
        .name(apiName)
        .description(String.format("Configuration properties for %s API group endpoints demo snippets", apiName))
        .properties(List.of(
            ConfigPropertyDescriptor.builder()
              .name(REST_DEMO_GROUP_NAME)
              .description(String.format("Configuration properties for REST endpoints demo snippets of %s API group", apiName))
              .properties(
                  CollectionUtil.emptyIfNull(api.getRestEndpoints()).stream()
                    .map(endpoint -> createEndpointDemoConfigPropertyForEndpointRequest(
                         api, 
                         REST_DEMO_GROUP_NAME,
                         endpoint.getName(), 
                         getEndpointDemoRequest(api, endpoint)))
                    .filter(o -> !CollectionUtil.isEmpty(o.getProperties()))
                    .toList()
                ).build(),
            ConfigPropertyDescriptor.builder()
              .name(WEBSOCKET_DEMO_GROUP_NAME)
              .description(String.format("Configuration properties for websocket endpoints demo snippets of %s API group", apiName))
              .properties(
                  CollectionUtil.emptyIfNull(api.getWebsocketEndpoints()).stream()
                    .map(endpoint -> createEndpointDemoConfigPropertyForEndpointRequest(
                      api, 
                      WEBSOCKET_DEMO_GROUP_NAME,
                      endpoint.getName(), 
                      endpoint.getRequest()))
                    .filter(o -> !CollectionUtil.isEmpty(o.getProperties()))
                    .toList()
                ).build())
          .stream()
          .filter(o -> !CollectionUtil.isEmpty(o.getProperties()))
          .toList())
        .build();
  }
  
  private static Field getEndpointDemoRequest(ExchangeApiDescriptor exchangeApiDescriptor, RestEndpointDescriptor restApi) {
    boolean hasArguments = ExchangeApiGenUtil.restEndpointHasArguments(restApi, exchangeApiDescriptor);
    boolean requestHasBody = ExchangeApiGenUtil.restEndpointRequestHasBody(restApi);
    if (requestHasBody && !hasArguments) {
      return ExchangeApiGenUtil.createDefaultRawBodyRequest();
    }
    return restApi.getRequest();
  }
  
  private static ConfigPropertyDescriptor createEndpointDemoConfigPropertyForEndpointRequest(
      ExchangeApiDescriptor api, 
      String endpointGroup, 
      String endpointName, 
      Field endpointRequest) {
    List<ConfigPropertyDescriptor> requestProperties = 
        generateDemoConfigPropertyForField(api, endpointName, endpointRequest);
    String endpointGroupDisplayName = Objects.equals(endpointGroup, REST_DEMO_GROUP_NAME)? "REST": "websocket";
    return ConfigPropertyDescriptor.builder()
        .name(endpointName)
        .description(String.format(
            "Configuration properties for %s %s endpoint of %s API group", 
            endpointGroupDisplayName, endpointName, 
            api.getName()))
        .properties(requestProperties)
        .build();
  }
  
  private static List<ConfigPropertyDescriptor> generateDemoConfigPropertyForField(
      ExchangeApiDescriptor api,  
      String parentField, 
      Field f) {
    if (f == null) {
      return List.of();
    }
    String fieldName = ExchangeApiGenUtil.getRequestArgName(f.getName());
    String fieldDescription = f.getDescription();
    fieldDescription = fieldDescription == null? "" : "<p>\n" + fieldDescription;
    Type type = PojoGenUtil.getFieldType(f);
    String propertyName = fieldName;
    if (type.isObject()) {
      f = ExchangeApiGenUtil.resolveFieldProperties(api, f);
      String rawValueDemoPropertyDescription = String.format(
          "Demo configuration property for %s.%s field as raw JSON string value.%s", 
          parentField, 
          fieldName,
          fieldDescription);
      String objectGroupDescription = String.format(
          "Demo configuration properties for %s.%s field object instance.%s", 
          parentField, 
          fieldName,
          fieldDescription);
      return List.of(
          ConfigPropertyDescriptor.builder()
              .name(propertyName)
              .type(Type.STRING.toString())
              .description(rawValueDemoPropertyDescription)
              .defaultValue(f.getSampleValue())
              .build(),
          ConfigPropertyDescriptor.builder()
              .name(propertyName)
              .description(objectGroupDescription)
              .properties(
                CollectionUtil.emptyIfNull(f.getProperties())
                  .stream()
                  .map(p -> generateDemoConfigPropertyForField(api, fieldName, p))
                  .reduce(CollectionUtil::mergeLists)
                  .orElse(List.of()))
              .build()
          );
    }
    
    return List.of(ConfigPropertyDescriptor.builder()
        .name(propertyName) 
        .type(type.toString())
        .description(
          String.format(
            "Demo configuration property for %s.%s field.%s", 
            parentField, 
            fieldName,
            fieldDescription))
        .defaultValue(f.getSampleValue())
        .build());
  }
  
}
