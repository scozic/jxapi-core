package org.jxapi.generator.java.exchange.api.demo;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;

import org.jxapi.exchange.Exchange;
import org.jxapi.exchange.ExchangeApi;
import org.jxapi.exchange.descriptor.CanonicalType;
import org.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.Field;
import org.jxapi.exchange.descriptor.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;
import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ExchangeJavaGenUtil;
import org.jxapi.generator.java.exchange.api.ExchangeApiGenUtil;
import org.jxapi.util.DemoUtil;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Helper methods around REST or Websocket endpoint demo snippets code generation.
 */
public class EndpointDemoGenUtil {
  
  public static final String CREATE_REQUEST_PROPERTIES_ARG_NAME = "properties";
  
  private EndpointDemoGenUtil() {}
  
  /**
   * Generates a method (with full signature and method body) that creates a
   * sample value for the given {@link Field} (which can be the data type
   * definition of a REST/Websocket request, response/message, or of a nested
   * {@link Type#OBJECT} property.
   * 
   * @param property        the property for which to generate the
   *                        creation method.
   * @param objectClassName Default class name for property value, relevant when
   *                        property is an object type and has no object name.
   * @param imports         the set of imports to add to the class containing the
   *                        method. Will be populated with the necessary imports.
   * @param placeholderResolver the placeholder resolver to use to resolve constants and configuration properties placeholders. This should be a reference to constant in its inteface, or property resolution from using {@link Properties} reference variable named <code>properties</code>.                      
   * @return the method code with signature (method declaration) and body
   * 
   * @see #generateFieldCreationMethodDeclaration(Field, String, Imports)
   * @see #generateFieldSampleValueDeclaration(Field, String, String, Imports, String)
   */
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
   * Generates the method declaration for a method that creates a sample value for
   * the given endpoint property.
   * 
   * @param field      the endpoint property for which to generate the
   *                               creation method.
   * @param defaultObjectClassName Default class name for property value, relevant
   *                               when property is an object type and has no
   *                               object name.
   * @param imports                the set of imports to add to the class
   *                               containing the method. Will be populated with
   *                               the necessary imports.
   * @return the method declaration
   */
  public static String generateFieldCreationMethodDeclaration(Field field, 
                                String defaultObjectClassName,
                                String defaultFieldName,
                                Imports imports) {
    Type type = ExchangeJavaGenUtil.getFieldType(field);
    String fieldClassName =  ExchangeJavaGenUtil.getClassNameForType(
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
        .append(" using sample value(s) defined in the field descriptor.\n\n")
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
    Type type = ExchangeJavaGenUtil.getFieldType(field);
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
    if (type.isObject()) {
      fieldValue = generateSampleFieldValueDeclarationObjectField(res, field, sampleValueVariableName, objectClassName, imports, placeholderResolver);
    } else {
      fieldValue = JavaCodeGenUtil.getQuotedString(sampleValue);
    }
    
  if (canonicalType != CanonicalType.OBJECT) {
      // Not primitive nor object type -> map or list type.
      if (type.isObject()) {
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
  
  private static String generateSampleFieldValueDeclarationObjectField(
        StringBuilder res, 
        Field field, 
        String sampleValueVariableName, 
        String objectClassName, 
        Imports imports,
        PlaceHolderResolver placeholderResolver) {
    Type type = ExchangeJavaGenUtil.getFieldType(field);
    CanonicalType canonicalType = type.getCanonicalType();
    Object sampleValue = field.getSampleValue();
    String itemVariableName = sampleValueVariableName;
    String fieldValue = null;
    if (type.isObject()) {
      if (canonicalType != CanonicalType.OBJECT) {
        itemVariableName = itemVariableName + "Item";
      }
      String itemClassName = ExchangeJavaGenUtil.getClassNameForType(
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
        generateSampleFieldValueDeclarationObjectFieldChild(res, field, childParam, itemVariableName, objectClassName, imports, placeholderResolver);
      }
      
      fieldValue = itemVariableName;
    } else {
      fieldValue = JavaCodeGenUtil.getQuotedString(sampleValue);
    }
    return fieldValue;
  }
  
  private static void generateSampleFieldValueDeclarationObjectFieldChild(
      StringBuilder res, 
      Field field, 
      Field childParam, 
      String itemVariableName, 
      String objectClassName, 
      Imports imports, 
      PlaceHolderResolver placeholderResolver) {
    Type childParamType = ExchangeJavaGenUtil.getFieldType(childParam);
    String setArg = JavaCodeGenUtil.getQuotedString(childParam.getSampleValue());
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
              placeholderResolver));
    } else if(childParamType.getCanonicalType().isPrimitive) {
      setArg = getPrimitiveTypeFieldSampleValueDeclaration(childParam, imports, placeholderResolver);
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
      return "Map.of(" + sampleKeyValue + ", " + getMapOrListSampleValueDeclaration(type.getSubType(), itemValue, sampleMapKeyValues, imports) + ")";
    }
  }
  
  private static String getPrimitiveTypeFieldSampleValueDeclaration(Field field, Imports imports, PlaceHolderResolver placeholderResolver) {
    return ExchangeJavaGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(
          ExchangeJavaGenUtil.getFieldType(field), 
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
    String pkgPrefix =  exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".demo.";
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
    String pkgPrefix =  exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".demo.";
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
    String exchangeImplClassName = ExchangeJavaGenUtil.getExchangeInterfaceImplementationName(exchangeClassName);
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

}
