package org.jxapi.generator.java.exchange.api.demo;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jxapi.exchange.Exchange;
import org.jxapi.exchange.ExchangeApi;
import org.jxapi.exchange.descriptor.CanonicalType;
import org.jxapi.exchange.descriptor.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.Field;
import org.jxapi.exchange.descriptor.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ExchangeGenUtil;
import org.jxapi.generator.java.exchange.api.ExchangeApiGenUtil;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.DemoUtil;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Helper methods around REST or Websocket endpoint demo snippets code generation.
 */
public class EndpointDemoGenUtil {
  
  public static final String REST_DEMO_GROUP_NAME = "rest";
  
  public static final String WEBSOCKET_DEMO_GROUP_NAME = "ws";
  
  /**
   * Name of the properties argument for methods that create request or response
   * sample values.
   */
  public static final String CREATE_REQUEST_PROPERTIES_ARG_NAME = "properties";
  
//  private static final String OPTIONAL_OF_NULLABLE = "Optional.ofNullable(";
//  private static final String OR_ELSE = ")\n.orElse(\n";
  
  private EndpointDemoGenUtil() {}
  
  /**
   * Generates a method (with full signature and method body) that creates a
   * sample value for the given {@link Field} (which can be the data type
   * definition of a REST/Websocket request, response/message, or of a nested
   * {@link Type#OBJECT} property.
   * 
   * @param property            the property for which to generate the creation
   *                            method.
   * @param objectClassName     Default class name for property value, relevant
   *                            when property is an object type and has no object
   *                            name.
   * @param defaultFieldName    The name to use for field, either 'request' or
   *                            'response' when the field name is
   *                            <code>null</code>.
   * @param imports             the set of imports to add to the class containing
   *                            the method. Will be populated with the necessary
   *                            imports.
   * @param placeholderResolver the placeholder resolver to use to resolve
   *                            constants and configuration properties
   *                            placeholders. This should be a reference to
   *                            constant in its inteface, or property resolution
   *                            from using {@link Properties} reference variable
   *                            named <code>properties</code>.
   * @return the method code with signature (method declaration) and body
   * 
   * @see #generateFieldCreationMethodDeclaration(Field, String, String, Imports)
   */
  @Deprecated
  public static String generateFieldCreationMethod(Field property, 
                                                   String objectClassName,
                                                   String defaultFieldName,
                                                   Imports imports,
                                                   PlaceHolderResolver placeholderResolver) {
    return new StringBuilder()
           .append(generateFieldCreationMethodDeclaration(
               property, 
               objectClassName, 
               defaultFieldName,
               imports))
           .append(" ")
           .append(JavaCodeGenUtil.generateCodeBlock(
                 generateFieldSampleValueDeclaration(
                   property,  
                   defaultFieldName,
                   objectClassName, 
                   imports,
                   "return ",
                   placeholderResolver) 
                 + ";"))
           .toString();
  }
  
  /**
   * Generates a method (with full signature and method body) that creates a
   * sample value for the given {@link Field} (which can be the data type
   * definition of a REST/Websocket request, response/message, or of a nested
   * {@link Type#OBJECT} property.
   * 
   * @param property            the property for which to generate the creation
   *                            method.
   * @param objectClassName     Default class name for property value, relevant
   *                            when property is an object type and has no object
   *                            name.
   * @param defaultFieldName    The name to use for field, either 'request' or
   *                            'response' when the field name is
   *                            <code>null</code>.
   * @param imports             the set of imports to add to the class containing
   *                            the method. Will be populated with the necessary
   *                            imports.
   * @param placeholderResolver the placeholder resolver to use to resolve
   *                            constants and configuration properties
   *                            placeholders. This should be a reference to
   *                            constant in its inteface, or property resolution
   *                            from using {@link Properties} reference variable
   *                            named <code>properties</code>.
   * @return the method code with signature (method declaration) and body
   * 
   * @see #generateFieldCreationMethodDeclaration(Field, String, String, Imports)
   */
  public static String generateFieldCreationMethod(Field property, 
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
              + generateFieldSampleValueDeclaration2(
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
  public static String generateFieldCreationMethodDeclaration(Field field, 
                                                              String defaultObjectClassName,
                                                              String defaultFieldName,
                                                              Imports imports) {
    Type type = ExchangeGenUtil.getFieldType(field);
    String fieldClassName =  ExchangeGenUtil.getClassNameForType(
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

  private static String generateFieldSampleValueDeclaration(Field field, 
                                                            String sampleValueVariableName, 
                                                            String objectClassName, 
                                                            Imports imports,
                                                            String returnOrResultAffectation,
                                                            PlaceHolderResolver placeholderResolver) {
    Type type = ExchangeGenUtil.getFieldType(field);
    Object sampleValue = field.getSampleValue();
    if (sampleValue == null && !type.isObject()) {
      return returnOrResultAffectation + "null";
    }
    CanonicalType canonicalType = type.getCanonicalType();
    if (canonicalType.isPrimitive) {
      imports.add(canonicalType.typeClass.getName());
      return returnOrResultAffectation + getPrimitiveTypeFieldSampleValueDeclaration(field, imports, placeholderResolver);
    }

    String fieldValue = null;
    StringBuilder res = new StringBuilder();
    if (type.isObject() && sampleValue == null) {
      fieldValue = generateSampleFieldValueDeclarationObjectFieldWithNoSampleValue(
                     res, 
                     field, 
                     sampleValueVariableName, 
                     objectClassName, 
                     imports, 
                     placeholderResolver);
    } else {
      fieldValue = JavaCodeGenUtil.getQuotedString(sampleValue);
    }
    if (canonicalType == CanonicalType.OBJECT && sampleValue != null) {
      fieldValue =  ExchangeApiGenUtil.getNewMessageDeserializerInstruction(type, objectClassName, imports) 
                      + ".deserialize(" + fieldValue + ")";
    }
    
   if (canonicalType != CanonicalType.OBJECT) {
      // Not primitive nor object type -> map or list type.
      if (type.isObject() && sampleValue == null) {
        fieldValue = getMapOrListSampleValueDeclaration(
                      type, 
                      fieldValue, 
                      field.getSampleMapKeyValue() == null? null: 
                      field.getSampleMapKeyValue().iterator(), 
                      imports);
      } 
      else {
        fieldValue = ExchangeApiGenUtil.getNewMessageDeserializerInstruction(type, objectClassName, imports) 
                  + ".deserialize(" + fieldValue + ")";
      }
    }
    
    if (fieldValue == null) {
      return returnOrResultAffectation + "null";
    } else {
      res.append(returnOrResultAffectation).append(fieldValue);
      return res.toString();
    }
  }
  
  private static String generateFieldSampleValueDeclaration2(
      Field field, 
      String demoPropertyName,
      String objectClassName, 
      ExchangeDescriptor exchangeDescriptor,
      ExchangeApiDescriptor exchangeApiDescriptor,
      List<ConfigPropertyDescriptor> demoConfigProperties,
      Imports imports) {
    Type type = ExchangeGenUtil.getFieldType(field);
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
      return generateFieldSampleValueDeclaration2Object(
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
    Type type = ExchangeGenUtil.getFieldType(field);
    String deserializeSampleValueInstr = generateDeserializeSampleValueInstruction(
        field, 
        demoPropertyName, 
        objectClassName, 
        exchangeDescriptor, 
        exchangeApiDescriptor,
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
      ExchangeApiDescriptor exchangeApiDescriptor,
      List<ConfigPropertyDescriptor> demoConfigProperties, 
      Imports imports) {
    Type type = ExchangeGenUtil.getFieldType(field);
    imports.add(objectClassName);
    return new StringBuilder()
        .append(ExchangeApiGenUtil.getNewMessageDeserializerInstruction(type, objectClassName, imports))
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
    Type type = ExchangeGenUtil.getFieldType(field);
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
  
  private static String generateFieldSampleValueDeclaration2Object(Field field, 
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
            exchangeApiDescriptor, 
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
      String childClassName = ExchangeApiGenUtil.getFieldObjectClassName(childParam, objectClassName);
      String itemValue = generateFieldSampleValueDeclaration2(
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
  
  
  private static String generateSampleFieldValueDeclarationObjectFieldWithNoSampleValue(
        StringBuilder res, 
        Field field, 
        String sampleValueVariableName, 
        String objectClassName, 
        Imports imports,
        PlaceHolderResolver sampleValuePlaceholderResolver) {
    Type type = ExchangeGenUtil.getFieldType(field);
    CanonicalType canonicalType = type.getCanonicalType();
    String itemVariableName = sampleValueVariableName;
    String fieldValue = null;
    if (canonicalType != CanonicalType.OBJECT) {
      itemVariableName = itemVariableName + "Item";
    }
    String itemClassName = ExchangeGenUtil.getClassNameForType(
                  Type.getLeafSubType(type), 
                  imports, 
                  objectClassName);
    res.append(itemClassName)
       .append(" ")
       .append(itemVariableName)
       .append(" = new ")
       .append(itemClassName)
       .append("();\n");
      
    for (Field childParam: field.getProperties()) {
      generateSampleFieldValueDeclarationObjectFieldChild(
        res, 
        field, 
        childParam, 
        itemVariableName, 
        objectClassName, 
        imports, 
        sampleValuePlaceholderResolver);
    }
      
    fieldValue = itemVariableName;
    
    return fieldValue;
  }
  
  private static void generateSampleFieldValueDeclarationObjectFieldChild(
      StringBuilder res, 
      Field field, 
      Field childParam, 
      String itemVariableName, 
      String objectClassName, 
      Imports imports, 
      PlaceHolderResolver sampleValuePlaceholderResolver) {
    Type childParamType = ExchangeGenUtil.getFieldType(childParam);
    Object childParamSampleValue = childParam.getSampleValue();
    if (childParamSampleValue == null && !childParamType.isObject()) {
      return; // No sample value to set
    }
    String setArg = Optional.ofNullable(sampleValuePlaceholderResolver)
                      .orElse(JavaCodeGenUtil::getQuotedString)
                      .resolve(String.valueOf(childParamSampleValue));
    String setAccessorName = JavaCodeGenUtil.getSetAccessorMethodName(
        childParam.getName(),  
        field.getProperties().stream()
                 .map(Field::getName).collect(Collectors.toList()));
    String setChildParamInstruction = 
          new StringBuilder()
              .append(itemVariableName)
              .append(".")
              .append(setAccessorName)
              .append("(")
              .toString();
    if (childParamType.isObject()) {
      setArg = itemVariableName + "_" + childParam.getName();
      res.append(generateFieldSampleValueDeclaration(childParam, 
              setArg, 
              ExchangeApiGenUtil.getFieldObjectClassName(childParam, objectClassName), 
              imports,
              setChildParamInstruction, 
              sampleValuePlaceholderResolver));
    } else if(childParamType.getCanonicalType().isPrimitive) {
      setArg = getPrimitiveTypeFieldSampleValueDeclaration(childParam, imports, sampleValuePlaceholderResolver);
      if (setArg != null) {
        res.append(setChildParamInstruction).append(setArg);
      }
    } else {
      // List or map
      if (setArg != null) {
        setArg = ExchangeApiGenUtil.getNewMessageDeserializerInstruction(childParamType, objectClassName, imports) 
              + ".deserialize(" + setArg + ")";
        res.append(setChildParamInstruction).append(setArg);
      }
    }
    if (setArg != null) {
      res.append(");\n");
    }
  }
  
  private static String getMapOrListSampleValueDeclaration(Type type, 
                               String itemValue, 
                               Iterator<String> sampleMapKeyValues, 
                               Imports imports) {
    if (type.getSubType() == null) {
      return itemValue;
    }
    
    if (type.getCanonicalType() == CanonicalType.LIST) {
      imports.add(List.class.getName());
      return "List.of(" + getMapOrListSampleValueDeclaration(type.getSubType(), itemValue, sampleMapKeyValues, imports) + ")";
    } else { // MAP
      if (sampleMapKeyValues == null || !sampleMapKeyValues.hasNext()) {
        return null;
      }
      String sampleKeyValue = JavaCodeGenUtil.getQuotedString(sampleMapKeyValues.next());
      return "Map.of(" 
              + sampleKeyValue 
              + ", " 
              + getMapOrListSampleValueDeclaration(type.getSubType(), itemValue, sampleMapKeyValues, imports) 
              + ")";
    }
  }
  
  private static String getPrimitiveTypeFieldSampleValueDeclaration(Field field, Imports imports, PlaceHolderResolver placeholderResolver) {
    return ExchangeGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(
          ExchangeGenUtil.getFieldType(field), 
          field.getSampleValue(), 
          imports,
          placeholderResolver);
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
   * @return the new test API instantiation instruction.
   * 
   * @see Exchange
   * @see ExchangeApi
   */
  public static String getNewTestApiInstruction(String exchangeVariableName,
                                                String apiVariableName,
                                                String simpleApiClassName) {
    return new StringBuilder()
        .append(simpleApiClassName)
        .append(" ")
        .append(apiVariableName)
        .append(" = ")
        .append(exchangeVariableName)
        .append(".get")
        .append(simpleApiClassName)
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

  public static List<ConfigPropertyDescriptor> collectDemoConfigProperties(ExchangeDescriptor exchangeDescriptor) {
      return CollectionUtil.emptyIfNull(exchangeDescriptor.getApis()).stream()
                .map(EndpointDemoGenUtil::createDemoConfigPropertyGroup)
                .filter(o -> !CollectionUtil.isEmpty(o.getProperties()))
                .collect(Collectors.toList());
    }
  
  private static ConfigPropertyDescriptor createDemoConfigPropertyGroup(ExchangeApiDescriptor api) {
    String apiName = api.getName();
    return ConfigPropertyDescriptor.createGroup
      (
        apiName, 
        String.format("Configuration properties for %s API group endpoints demo snippets", apiName), 
        List.of
        (
          ConfigPropertyDescriptor.createGroup
          (
            REST_DEMO_GROUP_NAME, 
            String.format("Configuration properties for REST endpoints demo snippets of %s API group", apiName), 
            CollectionUtil.emptyIfNull(api.getRestEndpoints()).stream()
              .map
                (endpoint -> createEndpointDemoConfigPropertyForEndpointRequest
                  (
                   api, 
                   REST_DEMO_GROUP_NAME,
                   endpoint.getName(), 
                   endpoint.getRequest()
                  )
                )
              .filter(o -> !CollectionUtil.isEmpty(o.getProperties()))
              .collect(Collectors.toList())
          ),
          ConfigPropertyDescriptor.createGroup
          (
            WEBSOCKET_DEMO_GROUP_NAME, 
            String.format("Configuration properties for websocket endpoints demo snippets of %s API group", apiName), 
            CollectionUtil.emptyIfNull(api.getWebsocketEndpoints()).stream()
              .map
                (endpoint -> createEndpointDemoConfigPropertyForEndpointRequest
                  (
                    api, 
                    WEBSOCKET_DEMO_GROUP_NAME,
                    endpoint.getName(), 
                    endpoint.getRequest()
                  )
                )
              .filter(o -> !CollectionUtil.isEmpty(o.getProperties()))
              .collect(Collectors.toList())
          )
        ).stream()
         .filter(o -> !CollectionUtil.isEmpty(o.getProperties()))
         .collect(Collectors.toList())
      );
  }
  
  private static ConfigPropertyDescriptor createEndpointDemoConfigPropertyForEndpointRequest(
      ExchangeApiDescriptor api, 
      String endpointGroup, 
      String endpointName, 
      Field endpointRequest) {
    List<ConfigPropertyDescriptor> requestProperty = 
        generateDemoConfigPropertyForField(api, endpointName, endpointRequest);
    String endpointGroupDisplayName = Objects.equals(endpointGroup, REST_DEMO_GROUP_NAME)? "REST": "websocket";
    return ConfigPropertyDescriptor.createGroup(
        endpointName,
        String.format(
            "Configuration properties for %s %s endpoint of %s API group", 
            endpointGroupDisplayName, endpointName, 
            api.getName()), 
        requestProperty
    );
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
    Type type = ExchangeGenUtil.getFieldType(f);
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
          ConfigPropertyDescriptor.create(
              propertyName, 
              Type.STRING,
              rawValueDemoPropertyDescription, 
              f.getSampleValue()),
          ConfigPropertyDescriptor.createGroup(
            propertyName, 
            objectGroupDescription,
            CollectionUtil.emptyIfNull(f.getProperties())
              .stream()
              .map(p -> generateDemoConfigPropertyForField(api, fieldName, p))
              .reduce(CollectionUtil::mergeLists)
              .orElse(List.of()))
          );
    }
    return List.of(ConfigPropertyDescriptor.create(
        propertyName, 
        type,
        String.format(
            "Demo configuration property for %s.%s field.%s", 
            parentField, 
            fieldName,
            fieldDescription), 
        f.getSampleValue()));
  }
  
}
