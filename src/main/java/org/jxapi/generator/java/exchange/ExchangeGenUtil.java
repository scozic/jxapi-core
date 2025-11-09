package org.jxapi.generator.java.exchange;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jxapi.exchange.descriptor.CanonicalType;
import org.jxapi.exchange.descriptor.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.Field;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.constants.ConstantsGenUtil;
import org.jxapi.generator.java.exchange.properties.PropertiesGenUtil;
import org.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;
import org.jxapi.netutils.deserialization.json.field.BigDecimalJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.BooleanJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.IntegerJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.LongJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;
import org.jxapi.netutils.rest.ratelimits.RateLimitRule;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.ConfigProperty;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Helper static methods for generation of Java classes of a given exchange wrapper
 */
public class ExchangeGenUtil {
  
  private static final String GET_INSTANCE = ".getInstance()";

  private ExchangeGenUtil() {}
  
  /**
   * Default separator for string lists
   */
  public static final String DEFAULT_STRING_LIST_SEPARATOR = ",";
  
  /**
   * Special value that can be used in sample value of
   * {@link CanonicalType#LONG} type, which means
   * current time {@link System#currentTimeMillis()} should be used.
   */
  public static final String SPECIAL_SAMPLE_VALUE_NOW = "now()";
  
  /**
   * Name of static variable storing the base HTTP (REST) URL of an exchange (see
   * {@link ExchangeDescriptor#getHttpUrl()} or an API group (see
   * {@link ExchangeApiDescriptor#getHttpUrl()}.
   */
  public static final String HTTP_URL_STATIC_VARIABLE = "HTTP_URL";

  /**
   * Name of static variable storing the base Websocket URL of an exchange (see
   * {@link ExchangeDescriptor#getWebsocketUrl()} or an API group (see
   * {@link ExchangeApiDescriptor#getWebsocketUrl()}.
   */
  public static final String WEBSOCKET_URL_STATIC_VARIABLE = "WEBSOCKET_URL";
  
  /**
   * Prefix of constant placeholder name. This prefix is used to identify constant
   * from exchange constants or exchange API constants.
   * 
   * @see Constant
   */
  public static final String CONSTANT_PLACEHOLDER_PREFIX = "constants.";
  
  /**
   * Prefix of configuration property placeholder name. This prefix is used to identify configuration 
   * property from exchange
   * 
   * @see ConfigProperty
   */
  public static final String CONFIG_PLACEHOLDER_PREFIX = "config.";
  
  /**
   * Prefix of a demo configuration property placeholder name.
   * <p>
   * Placeholders starting with this prefix are used to identify demo
   * configuration properties.
   * 
   * @see ConfigProperty
   */
  public static final String DEMO_CONFIG_PLACEHOLDER_PREFIX = "demo.config.";
  
  private static final Pattern PLACEHOLDER_PATERN = Pattern.compile("\\$\\{([^}]+)}");
  
  /**
   * @param exchangeDescriptor The exchange the API group belongs to
   * @param exchangeApiDescriptor The exchange API group to get the full class name for
   * @return The full name of the ExchangeApi interface class for the given
   *         exchange and API. The returned class name package is base package is
   *         <code>exchangeBasePackage.exchangeApiDescriptorName</code>. Its named
   *         is generated as follows:
   *         <code>ExchangeDescriptorNameExchangeApiDescriptorNameApi</code>.
   */
  public static String getApiInterfaceClassName(ExchangeDescriptor exchangeDescriptor, 
                          ExchangeApiDescriptor exchangeApiDescriptor) {
    String pkgPrefix =  exchangeDescriptor.getBasePackage() 
       + "." 
       + exchangeApiDescriptor.getName().toLowerCase() 
       + ".";
    String apiUniqueName = JavaCodeGenUtil.getUniqueCamelCaseVariableNames(
        exchangeDescriptor.getApis().stream()
          .map(ExchangeApiDescriptor::getName)
          .collect(Collectors.toList()),
        true)
      .get(exchangeApiDescriptor.getName());
    String simpleInterfaceName = JavaCodeGenUtil.firstLetterToUpperCase(exchangeDescriptor.getId()) 
       + apiUniqueName 
       + "Api";
    return pkgPrefix + simpleInterfaceName;
  }
  
  /**
   * @param exchangeDescriptor The exchange the API group belongs to
   * @param exchangeApiDescriptor The exchange API group to get the full class name for
   * @return The full name of the ExchangeApi interface implmentation class for
   *         the given exchange and API, which is interface class name (see
   *         {@link #getApiInterfaceClassName(ExchangeDescriptor, ExchangeApiDescriptor)})
   *         suffixed with <code>Impl</code>.
   */
  public static String getApiInterfaceImplementationClassName(ExchangeDescriptor exchangeDescriptor, 
                                  ExchangeApiDescriptor exchangeApiDescriptor) {
    return getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor) + "Impl";
  }
  
  /**
   * @param rateLimitName The name of the rate limit to generate the property name for the interface implementation holder.
   * @return The name of static variable for the given rate limit name.
   */
  public static String generateRateLimitVariableName(String rateLimitName) {
    return "rateLimit" + JavaCodeGenUtil.firstLetterToUpperCase(rateLimitName);
  }
  
  /**
   * Generates the name of the getter method for the given rate limit name.
   * @param rateLimitName The name of the rate limit to generate the getter method name for
   * @return The name of the getter method for the given rate limit name.
   * @see RateLimitRule#getId()
   */
  public static String generateRateLimitGetterMethodName(String rateLimitName) {
    return "get" + JavaCodeGenUtil.firstLetterToUpperCase(rateLimitName) + "RateLimit";
  }
  
  /**
   * Generates the getter method declaration for the given rate limit name in the interface class holder.
   * @param rateLimitName The name of the rate limit to generate the getter method declaration for
   * @return The getter method declaration for the given rate limit name.
   */
  public static String generateRateLimitRuleInterfaceMethodDeclaration(String rateLimitName) {
    return new StringBuilder()
            .append(JavaCodeGenUtil.generateJavaDoc("@return '" + rateLimitName + "' rate limit rule."))
            .append("\npublic ")
            .append(RateLimitRule.class.getSimpleName())
            .append(" ")
            .append(ExchangeGenUtil.generateRateLimitGetterMethodName(rateLimitName))
            .append("()")
            .append(";\n")
            .toString();
  }
  
  /**
   * Generates the getter method implementation for the given rate limit name.
   * @param rateLimitName The name of the rate limit to generate the getter method declaration for
   * @return The getter method declaration for the given rate limit name.
   */
  public static String generateRateLimitGetterImplementationMethodDeclaration(String rateLimitName) {
    return new StringBuilder()
                .append("@Override\npublic ")
                .append(RateLimitRule.class.getSimpleName())
                .append(" ")
                .append(generateRateLimitGetterMethodName(rateLimitName))
                .append("() ")
                .append(JavaCodeGenUtil.generateCodeBlock("return this." + generateRateLimitVariableName(rateLimitName) + ";"))
                .toString();
  }
  

  /**
   * @param exchangeDescriptor The exchange to generate the full class name for
   * @return The name of the Exchange interface class for the given exchange.
   */
  public static String getExchangeInterfaceName(ExchangeDescriptor exchangeDescriptor) {
    return exchangeDescriptor.getBasePackage() + "." 
        + JavaCodeGenUtil.firstLetterToUpperCase(exchangeDescriptor.getId()) + "Exchange";
  }

  /**
   * @param exchangeDescriptor The exchange where constants are defined
   * @return The full class name of constants interface defined at exchange level
   * @see ExchangeDescriptor#getConstants()
   */
  public static String getExchangeConstantsClassName(ExchangeDescriptor exchangeDescriptor) {
    return exchangeDescriptor.getBasePackage() + "." 
        + JavaCodeGenUtil.firstLetterToUpperCase(exchangeDescriptor.getId()) + "Constants";
  }
  
  /**
   * @param exchangeDescriptor The exchange where configuration properties are defined
   * @return The full class name of configuration properties interface for the given exchange.
   * @see ExchangeDescriptor#getProperties()
   */
  public static String getExchangePropertiesClassName(ExchangeDescriptor exchangeDescriptor) {
    return exchangeDescriptor.getBasePackage() 
        + "." 
        + JavaCodeGenUtil.firstLetterToUpperCase(exchangeDescriptor.getId()) 
        + "Properties";
  }
  
  /**
   * @param exchangeDescriptor The exchange where demo configuration properties are defined
   * @return The full class name of demo configuration properties class for the given exchange.
   */
  public static String getExchangeDemoPropertiesClassName(ExchangeDescriptor exchangeDescriptor) {
    return exchangeDescriptor.getBasePackage() 
        + "." 
        + JavaCodeGenUtil.firstLetterToUpperCase(exchangeDescriptor.getId())
        + "DemoProperties";
  }

  /**
   * @param deserializedTypeClassName The full class name of the type to generate JSON message deserializer class name for.
   * @return The name of JSON message deserializer class for the given deserialized type.
   */
  public static String getJsonMessageDeserializerClassName(String deserializedTypeClassName) {
    return new StringBuilder()
          .append(StringUtils.substringBefore(JavaCodeGenUtil.getClassPackage(deserializedTypeClassName), ".pojo"))
          .append(".deserializers.")
          .append(JavaCodeGenUtil.getClassNameWithoutPackage(deserializedTypeClassName))
          .append("Deserializer")
          .toString();
  }

  /**
   * @param type            the type of field (see {@link Field}) to get type class name
   *                        of.
   * @param imports         The imports of generator context that will be
   *                        populated with classes used by returned type. That set
   *                        must be not <code>null</code> and mutable.
   * @param objectClassName the object (full) class name of leaf object type if <code>type</code> is of object type
   * @return The simple class name associated to given <code>type</code>:
   *        <ul> 
   *          <li>if this type is a 'Primitive' type, returns the simple class name of the corresponding Java class:
   *            <ul>
   *              <li>BIGDECIMAL: {@link BigDecimal}</li>
   *              <li>BOOLEAN: {@link Boolean}</li>
   *              <li>INT: {@link Integer}</li>
   *              <li>LONG: {@link Long}</li>
   *              <li>STRING: {@link String}</li>
   *            </ul>
   *          <li>If this type is a 'List' type, returns generic <code>List&lt;SubTypeClassName&gt;</code>, 
   *         where <code>subTypeClassName</code> is the simple class name of the subType of the list, see {@link Type#getSubType()}.
   *          <li>If this type is a 'Map' type, returns generic <code>Map&lt;String, SubTypeClassName&gt;</code>, 
   *         where <code>subTypeClassName</code> is the simple class name of the subType of the map, see {@link Type#getSubType()}.
   *          <li>If this type is an 'Object' type, returns the simple class name of the object class name, 
   *          see {@link JavaCodeGenUtil#getClassNameWithoutPackage(String)}.
   *          </li>
   *        </ul>
   * @see #getClassNameForType(Type, Imports, String)
   * @throws IllegalArgumentException if the type is not recognized.
   */
  public static String getClassNameForType(Type type, 
                         Imports imports, 
                       String objectClassName) {
    if (type == null) {
      return null;
    }
    String subTypeClassName = null;
    CanonicalType canonicalType = type.getCanonicalType();
    Class<?> canonicalTypeClass = canonicalType.typeClass;
    switch(canonicalType) {
    case BIGDECIMAL:
      if (imports != null) {
        imports.add(BigDecimal.class.getName());
      }
      return canonicalTypeClass.getSimpleName();
    case BOOLEAN:
    case INT:
    case LONG:
    case STRING:
      return canonicalTypeClass.getSimpleName();
    case LIST:
      subTypeClassName = getClassNameForType(type.getSubType(), imports, objectClassName);
      if (imports != null) {
        imports.add(canonicalTypeClass.getName());
      }
      return canonicalTypeClass.getSimpleName() 
          + "<" 
          + JavaCodeGenUtil.getClassNameWithoutPackage(subTypeClassName) 
          + ">";
    case MAP:
      subTypeClassName = getClassNameForType(type.getSubType(), imports, objectClassName);
      if (imports != null) {
        imports.add(canonicalTypeClass.getName());
      }
      return canonicalTypeClass.getSimpleName() 
          + "<String, " 
          + JavaCodeGenUtil.getClassNameWithoutPackage(subTypeClassName) 
          + ">";
    case OBJECT:
      if (imports != null) {
        imports.add(objectClassName);
      }
      return JavaCodeGenUtil.getClassNameWithoutPackage(objectClassName);
    default:
      throw new IllegalArgumentException("Unexpected type for:" + type);
    }
  }

  /**
   * Returns the instruction to create a new instance of a {@link AbstractJsonMessageDeserializer} for the given type.
   * @param type The type of the field to deserialize
   * @param objectClassName The object class name of the field to generate JsonFieldDeserializer for. 
   *               This parameter is used only if the type is an 'object' type (see {@link Type#isObject()} ).
   * @param imports The imports of the generator context that will be populated with classes 
   *           used by returned type. That set must be not <code>null</code> and mutable.
   * @return The instruction to get or create a new instance of a {@link AbstractJsonMessageDeserializer} for the given type:
   *         <ul>
   *           <li>if this type is a 'primitive' type, returns the instruction to get the 
   *           singleton instance of the corresponding JsonFieldDeserializer:
   *             <ul>
   *               <li>BIGDECIMAL: {@link BigDecimalJsonFieldDeserializer#getInstance()}</li>
   *               <li>BOOLEAN: {@link BooleanJsonFieldDeserializer#getInstance()}</li>
   *               <li>INT: {@link IntegerJsonFieldDeserializer#getInstance()}</li>
   *               <li>LONG: {@link LongJsonFieldDeserializer#getInstance()}</li>
   *               <li>STRING: {@link StringJsonFieldDeserializer#getInstance()}</li>
   *             </ul>
   *           </li>
   *           <li>if this type is a 'List' type, returns the instruction to create a new instance of 
   *           a {@link ListJsonFieldDeserializer} with the subType of the list, see {@link Type#getSubType()}.
   *           <li>if this type is a 'Map' type, returns the instruction to create a new instance of 
   *           a {@link MapJsonFieldDeserializer} with the subType of the map, see {@link Type#getSubType()}.
   *           <li>if this type is an 'Object' type, returns the 'new' instruction to create a new instance of 
   *           a {@link AbstractJsonMessageDeserializer} for the object 
   *           class name, see {@link #getJsonMessageDeserializerClassName(String)}.
   *         </ul>
   * @see Type
   * @throws IllegalArgumentException if the type is not recognized.
   */
  public static String getNewJsonFieldDeserializerInstruction(Type type, String objectClassName, Imports imports) {
    if (type == null) {
      type  = Type.fromTypeName(CanonicalType.STRING.name());
    }
    switch (type.getCanonicalType()) {
    case BIGDECIMAL:
      imports.add(BigDecimalJsonFieldDeserializer.class.getName());
      return BigDecimalJsonFieldDeserializer.class.getSimpleName() +GET_INSTANCE;
    case BOOLEAN:
      imports.add(BooleanJsonFieldDeserializer.class.getName());
      return  BooleanJsonFieldDeserializer.class.getSimpleName() + GET_INSTANCE;
    case INT:
      imports.add(IntegerJsonFieldDeserializer.class.getName());
      return  IntegerJsonFieldDeserializer.class.getSimpleName() + GET_INSTANCE;
    case LONG:
      imports.add(LongJsonFieldDeserializer.class.getName());
      return  LongJsonFieldDeserializer.class.getSimpleName() + GET_INSTANCE;
    case STRING:
      imports.add(StringJsonFieldDeserializer.class.getName());
      return  StringJsonFieldDeserializer.class.getSimpleName() + GET_INSTANCE;
    case LIST:
      imports.add(ListJsonFieldDeserializer.class.getName());
      return "new " + ListJsonFieldDeserializer.class.getSimpleName() + "<>(" 
          + getNewJsonFieldDeserializerInstruction(type.getSubType(), objectClassName, imports) + ")";
    case MAP:
      imports.add(MapJsonFieldDeserializer.class.getName());
      return "new " + MapJsonFieldDeserializer.class.getSimpleName() 
          + "<>(" + getNewJsonFieldDeserializerInstruction(type.getSubType(), objectClassName, imports) +")";
    case OBJECT:
      String objectDeserializerClass = getJsonMessageDeserializerClassName(objectClassName);
      imports.add(objectDeserializerClass);
      return "new " +  JavaCodeGenUtil.getClassNameWithoutPackage(objectDeserializerClass) + "()";
    default:
      throw new IllegalArgumentException("Unexpected field type:" + type);
    }
  }

  /**
   * @param type                           must be a primitive type:
   *                                       {@link Type#STRING}, {@link Type#INT},
   *                                       {@link Type#LONG},
   *                                       {@link Type#BIGDECIMAL}. Otherwise,
   * @param sampleValue                    primitive type sample value which can
   *                                       be a string '\"12\"' or object value
   *                                       '12'. Can be <code>null</code> in which
   *                                       case returned value is
   *                                       <code>null</code>
   * @param imports                        Imports set to add eventual additional
   *                                       classes required by the generation
   *                                       instruction to
   * @param sampleValuePlaceHolderResolver a placeholder resolver to resolve
   *                                       references to constants or
   *                                       configuration properties in the sample
   *                                       value.
   * @return An instruction to create value represented by sample value
   */
  public static String getPrimitiveTypeFieldSampleValueDeclaration(Type type, 
                                   Object sampleValue,  
                                   Imports imports,
                                   PlaceHolderResolver sampleValuePlaceHolderResolver) {
    if (sampleValue == null) {
      return JavaCodeGenUtil.NULL;
    }
    String sampleValueStr = Optional.ofNullable(sampleValuePlaceHolderResolver)
                                    .orElse(JavaCodeGenUtil::getQuotedString)
                                    .resolve(sampleValue.toString());
    switch (type.getCanonicalType()) {
    case BIGDECIMAL:
      imports.add(BigDecimal.class.getName());
      return "new BigDecimal(" + sampleValueStr + ")";
    case BOOLEAN:
      return "Boolean.valueOf(" + sampleValueStr + ")";
    case INT:
      return "Integer.valueOf(" + sampleValueStr + ")";
    case LONG:
      if (SPECIAL_SAMPLE_VALUE_NOW.equals(sampleValue)) {
        return "Long.valueOf(System.currentTimeMillis())";
      }
      return "Long.valueOf(" + sampleValueStr + ")";
    default: // STRING
      return sampleValueStr;
    }
  }

  /**
   * Generates the name of the interface implementation class for the given exchange descriptor
   * @param exchangeDescriptor exchange descriptor
   * @return full name of the interface implementation class
   */
  public static String getExchangeInterfaceImplementationName(ExchangeDescriptor exchangeDescriptor) {
    return getExchangeInterfaceImplementationName(ExchangeInterfaceImplementationGenerator.getExchangeInterfaceName(exchangeDescriptor));
  }
  
  /**
   * Generates the name of the interface implementation class for the given exchange class name
   * @param exchangeClassName exchange class bale
   * @return full name of the interface implementation class
   */
  public static String getExchangeInterfaceImplementationName(String exchangeClassName) {
    return exchangeClassName + "Impl";
  }
  
  /**
   * Find the type of a field in context of REST/Websocket API code generation: If
   * field type is specified, returns it, otherwise, if field properties or objectName are specified,
   * returns {@link Type#OBJECT}, otherwise returns {@link Type#STRING}.
   * 
   * @param field The field to retrieve type of in context of REST/Websocket API
   *              code generation
   * @return <code>null</code> if field is <code>null</code>, the field type if it is specified, or 
   */
  public static Type getFieldType(Field field) {
    if (field == null) {
      return null;
    }
    Type  type = field.getType();
    if (type != null) {
      return type;
    }
    if (field.getProperties() != null || field.getObjectName() != null) {
      return Type.OBJECT;
    }
    return Type.STRING;
  }
  
  /**
   * @param field The field to check if its type is an object type
   * @return <code>true</code> if the field is not <code>null</code> and its type is an object type,
   *         <code>false</code> otherwise
   * @see #getFieldType(Field)  
   * @see Type#isObject()      
   */
  public static boolean isObjectField(Field field) {
    if (field == null) {
      return false;
    }
    return getFieldType(field).isObject();
  }
  
  /**
   * Finds all placeholders in the given string value. For instance with input
   * string
   * <code>"Hello ${name} you are using exchange ${exchange.name}"</code>, will return <code>["name", "exchange.name"]</code>
   * 
   * @param value The string value to find placeholders in
   * @return A list of placeholders found in the given string value
   */
  public static List<String> findPlaceHolders(String value) {
    List<String> res = new ArrayList<>();
    if (StringUtils.isEmpty(value)) {
      return res;
    }

    Matcher matcher = PLACEHOLDER_PATERN.matcher(value);
    while (matcher.find()) {
      res.add(matcher.group(1));
    }
    return res;
  }
  
  /**
   * Removes the constants placeholder prefix
   * {@link #CONFIG_PLACEHOLDER_PREFIX} from the given placeholder.
   * 
   * @param placeHolder The constant placeholder to get value without the prefix
   *                    for
   * @return The constant placeholder name without the prefix
   *         <code>constants.</code>, or <code>null</code> if the placeholder does
   *         not start with that prefix.
   */
  public static String getConstantPlaceHolder(String placeHolder) {
    return EncodingUtil.removePrefix(placeHolder, CONSTANT_PLACEHOLDER_PREFIX);
  }
  
  /**
   * Removes the configuration property placeholder prefix
   * {@link #CONFIG_PLACEHOLDER_PREFIX} from the given placeholder.
   * 
   * @param placeHolder The configuration property placeholder to get value without the prefix for
   * @return The configuration property placeholder name without the prefix
   */
  public static String getConfigPropertyPlaceHolder(String placeHolder) {
    return EncodingUtil.removePrefix(placeHolder, CONFIG_PLACEHOLDER_PREFIX);
  }
  
  /**
   * Removes the demo configuration property placeholder prefix
   * {@link #DEMO_CONFIG_PLACEHOLDER_PREFIX} from the given placeholder.
   * 
   * @param placeHolder The demo configuration property placeholder to get value
   *                    without the prefix for
   * @return The configuration property placeholder name without the prefix
   */
  public static String getDemoConfigPropertyPlaceHolder(String placeHolder) {
    return EncodingUtil.removePrefix(placeHolder, DEMO_CONFIG_PLACEHOLDER_PREFIX);
  }
  
  /**
   * Returns the full class name of the generated constant class where the given
   * constant is defined. When the given constant is a group constant, the
   * returned class name is this constant group class name, for instance
   * 'com.jxapi.myexchange.MyExchangeConstants.GroupConstantName'. Otherwise, the
   * returned class name is the full class name of the class containing given
   * constant property.
   * 
   * @param constantName       The constant to get the class name for as provided
   *                           by {@link Constant#getName()}
   * @param exchangeDescriptor The exchange descriptor where to look for
   *                           constants, cannot be <code>null</code>.
   * @return The full class name of the generated constant interface where the
   *         constant is defined, or <code>null</code> if the constant is not
   *         found in exchange constants.
   * @throws IllegalArgumentException if the exchange descriptor is
   *                                  <code>null</code> or if constant name is
   *                                  <code>null</code> or empty.
   */
  public static String getClassNameForConstant(String constantName, 
                                               ExchangeDescriptor exchangeDescriptor) {
    if (exchangeDescriptor == null) {
      throw new IllegalArgumentException("Exchange descriptor cannot be null");
    }
    if (StringUtils.isEmpty(constantName)) {
      throw new IllegalArgumentException("Constant name cannot be null or empty");
    }
    
    
    StringBuilder sb = new StringBuilder()
        .append(getExchangeConstantsClassName(exchangeDescriptor));
    List<Constant> constants = retrieveConstantHierarchy(constantName, exchangeDescriptor);
    if (constants.isEmpty()) {
      return null;
    }
    constants.forEach(constant -> {
      if (constant.isGroup()) {
        sb.append(".").append(JavaCodeGenUtil.firstLetterToUpperCase(constant.getName()));
      }
    });
    return sb.toString();
  }
  
  private static Constant findConstant(String constantName, List<Constant> constants) {
    return CollectionUtil.emptyIfNull(constants).stream()
        .filter(c -> constantName.equals(c.getName()))
        .findFirst()
        .orElse(null);
  }
  
  private static ConfigPropertyDescriptor findProperty(String propertyName, List<ConfigPropertyDescriptor> properties) {
    List<ConfigPropertyDescriptor> propertiesMatchingName = CollectionUtil.emptyIfNull(properties)
            .stream().filter(p -> propertyName.equals(p.getName()))
            .collect(Collectors.toList());
    if (CollectionUtil.isEmpty(propertiesMatchingName)) {
      return null; // Property not found
    } else {
      return propertiesMatchingName.stream()
          .filter(ConfigPropertyDescriptor::isGroup)
          .findFirst()
          .orElse(propertiesMatchingName.get(0)); // Return first property if no group found
    }
  }
  
  private static List<Constant> retrieveConstantHierarchy(String constantName, ExchangeDescriptor exchangeDescriptor) {
    String[] parts = StringUtils.split(constantName, '.');
    List<Constant> l = new ArrayList<>();
    List<Constant> constants = CollectionUtil.emptyIfNull(exchangeDescriptor.getConstants());
    for (int i = 0; i < parts.length; i++) {
      Constant c = findConstant(parts[i], constants);
      if (c == null || (!c.isGroup() && i < parts.length - 1)) {
        return Collections.emptyList(); // Constant not found
      }
      l.add(c);
      constants = CollectionUtil.emptyIfNull(c.getConstants());
    }
    return Collections.unmodifiableList(l);
  }
  
  private static List<ConfigPropertyDescriptor> retrievePropertiesHierarchy(String propertyName, List<ConfigPropertyDescriptor> properties) {
    String[] parts = StringUtils.split(propertyName, '.');
    List<ConfigPropertyDescriptor > l = new ArrayList<>();
    List<ConfigPropertyDescriptor> exchangeProps = CollectionUtil.emptyIfNull(properties);
    for (int i = 0; i < parts.length; i++) {
      ConfigPropertyDescriptor c = findProperty(parts[i], exchangeProps);
      if (c == null || (!c.isGroup() && i < parts.length - 1)) {
        return Collections.emptyList(); // Constant not found
      }
      l.add(c);
      exchangeProps = CollectionUtil.emptyIfNull(c.getProperties());
    }
    return Collections.unmodifiableList(l);
  }
  
  /**
   * Returns the full class name of the generated properties interface where the
   * given configuration property is defined.
   * 
   * @param configPropertyName The configuration property name to get the class
   *                           name for
   * @param exchangeDescriptor The exchange descriptor where the configuration
   *                           property is defined
   * @return The full class name of the generated properties interface where the
   *         configuration property is defined, or <code>null</code> if the
   *         configuration property is not found.
   */
  public static String getClassNameForConfigProperty(String configPropertyName, 
                                                     ExchangeDescriptor exchangeDescriptor) {
    if (exchangeDescriptor == null) {
      throw new IllegalArgumentException("Exchange descriptor cannot be null");
    }
    if (StringUtils.isEmpty(configPropertyName)) {
      throw new IllegalArgumentException("Constant name cannot be null or empty");
    }
    
    
    StringBuilder sb = new StringBuilder();
    List<ConfigPropertyDescriptor> constants = retrievePropertiesHierarchy(configPropertyName, exchangeDescriptor.getProperties());
    if (constants.isEmpty()) {
      // Property not found in exchange properties, try demo properties
      constants = retrievePropertiesHierarchy(configPropertyName, exchangeDescriptor.getProperties());
      sb.append(getExchangeDemoPropertiesClassName(exchangeDescriptor));
    } else {
      // Property found in exchange properties
      sb.append(getExchangePropertiesClassName(exchangeDescriptor));
    }
    if (constants.isEmpty()) {
      // Property not found in exchange properties or demo properties
      return null;
    }
    constants.forEach(constant -> {
      if (constant.isGroup()) {
        sb.append(".").append(JavaCodeGenUtil.firstLetterToUpperCase(constant.getName()));
      }
    });
    return sb.toString();
  }
  
  /**
   * Returns the value declaration for the given constant name. The value
   * declaration is the reference to the constant in the generated constants class
   * 
   * @param constantName       The name of the constant to get the value
   *                           declaration for. If the constant is nested in a
   *                           group, the name must provide full constant address
   *                           like 'myGroup.myConstant'.
   * @param exchangeDescriptor The exchange descriptor where the constant may be
   *                           defined
   * @param imports            The imports of the generator context that will be
   *                           populated with classes used by returned type. That
   *                           set must be not <code>null</code> and mutable.
   * @return The value declaration for the given constant name, or
   *         <code>null</code> if the constant is not found in constants. exchange
   *         level constants.
   */
  public static String getValueDeclarationForConstant(String constantName, 
                                                      ExchangeDescriptor exchangeDescriptor,
                                                      Imports imports) {
    String className = getExchangeConstantsClassName(exchangeDescriptor);
    List<Constant> sieblingProperties = null;
    List<Constant> hierarchy = retrieveConstantHierarchy(constantName, exchangeDescriptor);
    
    if(hierarchy.isEmpty()) {
      // Property not found or is a group property
      return null;
    } else {
      sieblingProperties = CollectionUtil.emptyIfNull(exchangeDescriptor.getConstants());
    }
    
    imports.add(className);
    StringBuilder s = new StringBuilder()
        .append(JavaCodeGenUtil.getClassNameWithoutPackage(className))
        .append(".");
    for (int i = 0; i < hierarchy.size(); i++) {
      Constant p = hierarchy.get(i);
      s.append(ConstantsGenUtil.getConstantVariableName(p, sieblingProperties));
      if (i < hierarchy.size() - 1) {
        s.append(".");
        sieblingProperties = p.getConstants();
      }
      
    }
    return s.toString();
  }
  
  /**
   * Returns the value declaration for the given configuration property name. The
   * value declaration is the reference to the configuration property in either
   * the generated properties class, or the demo generated properties if provided.
   * 
   * @param configPropertyName The name of the configuration property to get the
   *                           value declaration for
   * @param exchangeDescriptor The exchange descriptor where the configuration
   *                           property is defined
   * @param demoProperties     The demo configuration properties for the exchange,
   *                           may be <code>null</code> or empty if value must be
   *                           retrieved from exchange properties only.
   * @param propertiesVariable The name of the variable holding the properties
   *                           map, usually <code>properties</code>.
   * @param imports            The imports of the generator context that will be
   *                           populated with classes used by returned type. That
   *                           set must be not <code>null</code> and mutable.
   * @return The value declaration for the given configuration property name, or
   *         <code>null</code> if the configuration property is not found or is a
   *         group.
   */
  public static String getValueDeclarationForConfigProperty(String configPropertyName, 
                                                          ExchangeDescriptor exchangeDescriptor,
                                                          List<ConfigPropertyDescriptor> demoProperties,
                                                          String propertiesVariable,
                                                          Imports imports) {
    String className = null;
    List<ConfigPropertyDescriptor> sieblingProperties = null;
    List<ConfigPropertyDescriptor> hierarchy = retrievePropertiesHierarchy(configPropertyName, exchangeDescriptor.getProperties());
    if(hierarchy.isEmpty()) {
      hierarchy = retrievePropertiesHierarchy(configPropertyName, demoProperties);
      className = getExchangeDemoPropertiesClassName(exchangeDescriptor);
      sieblingProperties = demoProperties;
    } else {
      className = getExchangePropertiesClassName(exchangeDescriptor);
      sieblingProperties = exchangeDescriptor.getProperties();
    }
    
    if(hierarchy.isEmpty()) {
      // Property not found or is a group property
      return null;
    }
    
    imports.add(className);
    StringBuilder s = new StringBuilder()
        .append(JavaCodeGenUtil.getClassNameWithoutPackage(className))
        .append(".");
    for (int i = 0; i < hierarchy.size() - 1; i++) {
      ConfigPropertyDescriptor p = hierarchy.get(i);
      s.append(PropertiesGenUtil.getPropertyVariableName(p, sieblingProperties)).append(".");
      sieblingProperties = p.getProperties();
      
    }
    String methodName = PropertiesGenUtil.getPropertyGetterMethodName(hierarchy.get(hierarchy.size() - 1), sieblingProperties);
    return s.append(methodName)                
            .append("(") 
            .append(propertiesVariable)
            .append(")")
            .toString();
  }
  
  /**
   * Retrieves all possible placeholders keys for the given exchange, and their
   * replacement as JavaDoc code links like
   * {@link #getDescriptionReplacements(ExchangeDescriptor, String)} using
   * <code>null</code> as value for javaDoc base URL.
   * 
   * @param exchangeDescriptor The exchange descriptor to get the placeholders
   *                           keys for
   * @return A map of placeholders keys to their replacement values as javadoc
   *         links.
   * @see #getDescriptionReplacements(ExchangeDescriptor, String)        
   */
  public static Map<String, Object> getDescriptionReplacements(ExchangeDescriptor exchangeDescriptor) {
    return getDescriptionReplacements(exchangeDescriptor, null);
  }
  
  /**
   * Retrieves all possible placeholders keys for the given exchange, and their
   * replacement as JavaDoc code or HTML links. These placeholders are:
   * <ul>
   * <li>Exchange constants, see {@link ExchangeDescriptor#getConstants()}</li>
   * <li>Exchange configuration properties, see
   * {@link ExchangeDescriptor#getProperties()}</li>
   * <li>Exchange demo configuration properties</li>
   * </ul>
   * 
   * @param exchangeDescriptor The exchange descriptor to get the placeholders
   *                           keys for
   * @param baseHtmlDocUrl     The base HTML documentation URL to use for Javadoc links. If <code>null</code>, a code link will be generated instead.                       
   * @return A map of placeholders keys to their replacement values as javadoc links.
   */
  public static Map<String, Object> getDescriptionReplacements(ExchangeDescriptor exchangeDescriptor, String baseHtmlDocUrl) {
    Map<String, Object> replacements = CollectionUtil.createMap();
    if (exchangeDescriptor == null) {
      return replacements;
    }
    // Add exchange constants
    collectConstantDescriptionReplacements(
        replacements, 
        CONSTANT_PLACEHOLDER_PREFIX,
        getExchangeConstantsClassName(exchangeDescriptor), 
        "",
        exchangeDescriptor.getConstants(), 
        baseHtmlDocUrl);
    
    // Add exchange configuration properties
    collectPropertiesDescriptionReplacements(
        replacements, 
        CONFIG_PLACEHOLDER_PREFIX,
        getExchangePropertiesClassName(exchangeDescriptor), 
        "",
        exchangeDescriptor.getProperties(),
        baseHtmlDocUrl);
    
    return replacements;
  }
  
  private static void collectConstantDescriptionReplacements(
      Map<String, Object> replacements, 
      String prefix, 
      String propertiesClassName, 
      String innerPropertiesClassName, 
      List<Constant> constants, 
      String baseHtmlDocUrl) {
    innerPropertiesClassName = StringUtils.defaultString(innerPropertiesClassName);
    for (Constant constant : CollectionUtil.emptyIfNull(constants)) {      
      String cname = constant.getName();
      String cfullName = prefix + cname;
      if (constant.isGroup()) {
        String groupSimpleClassName = ConstantsGenUtil.getConstantVariableName(constant, constants);
        StringBuilder groupPropertiesClassName = new StringBuilder();
        if (!innerPropertiesClassName.isEmpty()) {
          groupPropertiesClassName.append(innerPropertiesClassName).append(".");
        }
        groupPropertiesClassName.append(groupSimpleClassName);
        replacements.put(cfullName, getDocLink(propertiesClassName, groupPropertiesClassName.toString(), null, baseHtmlDocUrl));
        collectConstantDescriptionReplacements(
            replacements, 
            cfullName + ".", 
            propertiesClassName, 
            groupPropertiesClassName.toString(), 
            constant.getConstants(), 
            baseHtmlDocUrl);
      } else {
        replacements.put(cfullName, getDocLink(propertiesClassName, innerPropertiesClassName, cname, baseHtmlDocUrl));
      }
    }
  }
  
  private static void collectPropertiesDescriptionReplacements(
      Map<String, Object> replacements, 
      String prefix, 
      String propertiesClassName, 
      String innerPropertiesClassName, 
      List<ConfigPropertyDescriptor> properties, 
      String baseHtmlDocUrl) {
    innerPropertiesClassName = StringUtils.defaultString(innerPropertiesClassName);
    for (ConfigPropertyDescriptor prop : CollectionUtil.emptyIfNull(properties)) {      
      String cname = prop.getName();
      String cfullName = prefix + cname;
      if (prop.isGroup()) {
        String groupSimpleClassName = PropertiesGenUtil.getPropertyVariableName(prop, properties);
        StringBuilder groupPropertiesClassName = new StringBuilder();
        if (!innerPropertiesClassName.isEmpty()) {
          groupPropertiesClassName.append(innerPropertiesClassName).append(".");
        }
        groupPropertiesClassName.append(groupSimpleClassName);
        replacements.put(cfullName, getDocLink(propertiesClassName, groupPropertiesClassName.toString(), null, baseHtmlDocUrl));
        collectPropertiesDescriptionReplacements(
            replacements, 
            cfullName + ".", 
            propertiesClassName, 
            groupPropertiesClassName.toString(), 
            prop.getProperties(), 
            baseHtmlDocUrl);
      } else {
        replacements.put(cfullName, getDocLink(propertiesClassName, innerPropertiesClassName, cname, baseHtmlDocUrl));
      }
    }
  }
  
  /**
   * Retrieves all possible constant placeholders keys for the given exchange, and their
   * replacement as actual constant values. These placeholders are
   * Exchange constants, see {@link ExchangeDescriptor#getConstants()}
   * 
   * @param exchangeDescriptor The exchange descriptor to collect the placeholders
   *                           keys for                       
   * @return A map of placeholders keys to their replacement values as javadoc links.
   */
  public static Map<String, Object> getValuesReplacements(ExchangeDescriptor exchangeDescriptor) {
    Map<String, Object> replacements = CollectionUtil.createMap();
    if (exchangeDescriptor == null) {
      return replacements;
    }
    // Add exchange constants
    collectConstantValuesReplacements(
        replacements, 
        CONSTANT_PLACEHOLDER_PREFIX,
        exchangeDescriptor.getConstants());
    
    return replacements;
  }
  
  private static void collectConstantValuesReplacements(
      Map<String, Object> replacements, 
      String prefix,   
      List<Constant> constants) {
    for (Constant constant : CollectionUtil.emptyIfNull(constants)) {      
      String cname = constant.getName();
      String cfullName = prefix + cname;
      if (constant.isGroup()) {
        collectConstantValuesReplacements(replacements, cfullName + ".", constant.getConstants());
      } else {
        replacements.put(cfullName, constant.getValue());
      }
      
    }
  }
  
  private static String getDocLink(String className, String innerClassName, String variableName, String baseHtmlDocUrl) {
    String staticVariableName = JavaCodeGenUtil.getStaticVariableName(variableName);
    innerClassName = StringUtils.defaultString(innerClassName);
    String fullClassName = className + (StringUtils.isEmpty(innerClassName) ? "" : "." + innerClassName);
    if (baseHtmlDocUrl == null) {
      return JavaCodeGenUtil.getJavaDocLink(fullClassName, staticVariableName);
    }
    StringBuilder url = new StringBuilder().append(JavaCodeGenUtil.getClassJavadocUrl(baseHtmlDocUrl, className, innerClassName));
    
    String text = variableName;
    if (StringUtils.isAllEmpty(staticVariableName)) {
      text = JavaCodeGenUtil.firstLetterToLowerCase(JavaCodeGenUtil.getClassNameWithoutPackage(fullClassName));
    } else {
      url.append("#").append(staticVariableName);
    }
    
    return JavaCodeGenUtil.getHtmlLink(url.toString(), text);
  }

  /**
   * Generates the argument substitution declaration for a given template.
   * <ul>
   * <li>If the template does not contain any placeholder, the template is
   * returned as a quoted string.</li>
   * <li>If the template contains placeholders, the returned value is a call to
   * {@link EncodingUtil#substituteArguments(String, Object...)}, with the
   * template and key and value pairs for each placeholder. The placeholders are
   * expected to refer to either be a reference to a constant
   * (<code>constants.myConstant</code>) or a reference to a configuration
   * property (<code>config.myConfigProperty</code>) if <code>propertyName</code> 
   * arg is not <code>null</code>.
   * If the placeholder is not for a constant or configuration property, it is not replaced.
   * <ul>
   * <li>For a constant, the value is the constant value returned by
   * {@link getValueDeclarationForConstant}.</li>
   * <li>For a configuration property, the value is the configuration property
   * value returned by
   * {@link getValueDeclarationForConfigProperty}.</li>
   * </ul>
   * </li>
   * </ul>
   * 
   * @param template              The template to generate the argument
   *                              substitution declaration for, may contain
   *                              placeholders. If <code>null</code>, the returned 
   *                              value is <code>null</code>.
   * @param exchangeDescriptor    The exchange descriptor to use to resolve the
   *                              constants and configuration properties used in
   *                              the template.
   * @param demoProperties        The demo configuration properties for the exchange,
   * @param propertiesVariable    The name of the variable or instruction
   *                              referencing the configuration properties.
   *                              If <code>null</code>, the configuration properties 
   *                              are not used for replacements.
   * @param imports               The imports of the generated class, that will be
   *                              populated with classes used by the returned
   *                              value.
   * @return The argument substitution declaration instruction for the template.
   */
  public static String generateSubstitutionInstructionDeclaration(
                        String template, 
                        ExchangeDescriptor exchangeDescriptor, 
                        List<ConfigPropertyDescriptor> demoProperties,
                        String propertiesVariable,
                        Imports imports) {
    if (template == null) {
      return JavaCodeGenUtil.NULL;
    }
    List<String> placeHolderNames = findPlaceHolders(template);
    if (placeHolderNames.isEmpty()) {
      return JavaCodeGenUtil.getQuotedString(template);
    }
    List<String> placeHoldersDeclarations  = new ArrayList<>(placeHolderNames.size());
    for (String placeHolder: placeHolderNames) {
      String constantName = getConstantPlaceHolder(placeHolder);
      String valueDeclaration = null;
      if (constantName != null) {
          valueDeclaration = getValueDeclarationForConstant(constantName, exchangeDescriptor, imports);
      } else if (propertiesVariable != null) {
        String propertyName = Optional.ofNullable(getConfigPropertyPlaceHolder(placeHolder))
                                      .orElse(getDemoConfigPropertyPlaceHolder(placeHolder));
        if (propertyName != null) {
          valueDeclaration = getValueDeclarationForConfigProperty(propertyName, exchangeDescriptor, demoProperties, propertiesVariable, imports);
        }
      }
      if (valueDeclaration != null) {
        placeHoldersDeclarations.add(JavaCodeGenUtil.getQuotedString(placeHolder));
        placeHoldersDeclarations.add(valueDeclaration);
      }
    }
    imports.add(EncodingUtil.class);
    return new StringBuilder()
                .append(EncodingUtil.class.getSimpleName())
                .append(".substituteArguments(")
                .append(JavaCodeGenUtil.getQuotedString(template))
                .append(", ")
                .append(StringUtils.join(placeHoldersDeclarations, ", "))
                .append(")")
                .toString();
  }
  
  /**
   * Generates unique camel case method names for each API defined in the given
   * exchange descriptor.
   * 
   * @param apis list of APIs defined in the exchange descriptor, should not be <code>null</code>
   * @return A map of API name to unique camel case getter method name for that
   *         API.
   */
  public static Map<String, String> getApiGroupGetterMethodNames(List<ExchangeApiDescriptor> apis) {
    return JavaCodeGenUtil.getUniqueCamelCaseVariableNames(
        apis.stream().map(ExchangeApiDescriptor::getName).collect(Collectors.toList()),
        true)
      .entrySet()
      .stream()
      .collect(Collectors.toMap(
          Map.Entry::getKey, 
          entry -> "get" + entry.getValue() + "Api"
      ));
  }
  
  /**
   * Generates the unique camel case method name for the given API defined in the
   * given exchange descriptor.
   * 
   * @param exchangeDescriptor exchange descriptor
   * @param api                The API to get the getter method name for
   * @return The unique camel case getter method name for the given API.
   */
  public static String getApiGroupGetterMethodName(ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor api) {
    return getApiGroupGetterMethodNames(CollectionUtil.emptyIfNull(exchangeDescriptor.getApis()))
            .get(api.getName());
  }
}
