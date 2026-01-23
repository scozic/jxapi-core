package org.jxapi.generator.java.exchange;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.gen.HttpClientDescriptor;
import org.jxapi.exchange.descriptor.gen.NetworkDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketClientDescriptor;
import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.constants.ConstantsGenUtil;
import org.jxapi.generator.java.exchange.properties.PropertiesGenUtil;
import org.jxapi.netutils.rest.ratelimits.RateLimitRule;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.ConfigProperty;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Helper static methods for generation of Java classes of a given exchange wrapper
 */
public class ExchangeGenUtil {
  
  private ExchangeGenUtil() {}
  
  /**
   * Default separator for string lists
   */
  public static final String DEFAULT_STRING_LIST_SEPARATOR = ",";
  
  /**
   * Name of static variable storing the base HTTP (REST) URL of an exchange (see
   * {@link ExchangeDescriptor#getHttpUrl()} or an API group (see
   * {@link ExchangeApiDescriptor#getHttpUrl()}.
   */
  public static final String HTTP_URL_STATIC_VARIABLE = "HTTP_URL";
  
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
  
  public static final Pattern PLACEHOLDER_PATERN = Pattern.compile("\\$\\{([^}]+)}");
  
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
          .toList(),
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
            .toList();
    if (CollectionUtil.isEmpty(propertiesMatchingName)) {
      return null; // Property not found
    } else {
      return propertiesMatchingName.stream()
          .filter(PropertiesGenUtil::isGroup)
          .findFirst()
          .orElse(propertiesMatchingName.get(0)); // Return first property if no group found
    }
  }
  
  private static List<Constant> retrieveConstantHierarchy(String constantName, ExchangeDescriptor exchangeDescriptor) {
    String[] parts = StringUtils.split(constantName, '.');
    List<Constant> l = new ArrayList<>();
    List<Constant> constants = Constant.fromDescriptors(exchangeDescriptor.getConstants());
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
      ConfigPropertyDescriptor p = findProperty(parts[i], exchangeProps);
      if (p == null || (!PropertiesGenUtil.isGroup(p) && i < parts.length - 1)) {
        return Collections.emptyList(); // Constant not found
      }
      l.add(p);
      exchangeProps = CollectionUtil.emptyIfNull(p.getProperties());
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
    List<ConfigPropertyDescriptor> properties = retrievePropertiesHierarchy(configPropertyName, exchangeDescriptor.getProperties());
    if (properties.isEmpty()) {
      // Property not found in exchange properties, try demo properties
      properties = retrievePropertiesHierarchy(configPropertyName, exchangeDescriptor.getProperties());
      sb.append(getExchangeDemoPropertiesClassName(exchangeDescriptor));
    } else {
      // Property found in exchange properties
      sb.append(getExchangePropertiesClassName(exchangeDescriptor));
    }
    if (properties.isEmpty()) {
      // Property not found in exchange properties or demo properties
      return null;
    }
    properties.forEach(p -> {
      if (PropertiesGenUtil.isGroup(p)) {
        sb.append(".").append(JavaCodeGenUtil.firstLetterToUpperCase(p.getName()));
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
      sieblingProperties = Constant.fromDescriptors(exchangeDescriptor.getConstants());
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
        Constant.fromDescriptors(exchangeDescriptor.getConstants()),
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
      if (PropertiesGenUtil.isGroup(prop)) {
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
        Constant.fromDescriptors(exchangeDescriptor.getConstants()));
    
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
    List<String> placeHolderNames = JavaCodeGenUtil.findPlaceHolders(template);
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
        apis.stream().map(ExchangeApiDescriptor::getName).toList(),
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

  /**
   * Generates static variable declarations for list of camel-case variable
   *  names with given suffix.
   * <p>
   * For each name in <code>names</code>, a static variable (uppercase) is
   * generated with name <code>name+suffix</code> and value the name as quoted
   * string .
   * <p>
   * If <code>classBody</code> is not <code>null</code>, the generated static
   * variable declarations are appended to it.
   * 
   * @param names        List of  names
   * @param suffix       Suffix to append to name to generate static
   *                     variable name
   * @param classBody    StringBuilder to append generated field declarations to.
   *                     Can be null, in which case no field declarations are
   *                     appended.
   * @return Map indexed with name, mapped to associated static variable
   *         name.
   */
  public static Map<String, String> generateNamesStaticVariablesDeclarations(
      List<String> names, 
      String suffix, 
      StringBuilder classBody,
      String modifiers) {
    Map<String, String> res = CollectionUtil.createMap();
    Map<String, Constant> constants = CollectionUtil.createMap();
    for (String f : names) {
        Constant c = Constant.create(
            f + suffix,
            Type.STRING,
           "Name of <code>" + f + "</code> " + suffix + ".",
           JavaCodeGenUtil.getQuotedString(f)
         );
        constants.put(f, c);
    }
    
    List<Constant> allConstants = new ArrayList<>(constants.values());
    boolean first = true;
    for(Entry<String, Constant> e : constants.entrySet()) {
      Constant c = e.getValue();
      res.put(e.getKey(), ConstantsGenUtil.getConstantVariableName(c, allConstants));
      if (classBody != null) {
        if (first) {
          first = false;
        } else {
          classBody.append("\n");
        }
        classBody.append(ConstantsGenUtil.generateConstantDeclaration(
          c, 
          allConstants,
          new Imports(), 
          PlaceHolderResolver.NO_OP, 
          PlaceHolderResolver.NO_OP,
          modifiers)
        );
      }
    }
    return res;     
  }
  
  /**
   * Generates static variable declarations for HTTP client names defined in the
   * given network descriptor.
   * 
   * @param network The network descriptor defining HTTP clients, may be
   *                <code>null</code>.
   * @param body    The class body to append the generated static variable
   *                declarations to, may be <code>null</code>.
   * @return A map indexed with HTTP client name, mapped to associated static
   *         variable name.
   */
  public static Map<String, String> generateHttpClientNamesStaticVariablesDeclarations(NetworkDescriptor network, StringBuilder body) {
    network = Optional.ofNullable(network).orElse(new NetworkDescriptor());
    return ExchangeGenUtil.generateNamesStaticVariablesDeclarations(
        CollectionUtil.emptyIfNull(network.getHttpClients())
          .stream()
          .map(HttpClientDescriptor::getName)
          .toList(),
        "HttpClient", 
        body,
        "");
  }
  
  /**
   * Generates static variable declarations for websocket client names defined in
   * the given network descriptor.
   * 
   * @param network The network descriptor defining websocket clients, may be
   *                <code>null</code>.
   * @param body    The class body to append the generated static variable
   *                declarations to, may be <code>null</code>.
   * @return A map indexed with websocket client name, mapped to associated static
   *         variable name.
   */
  public static Map<String, String> generateWebsocketClientNamesStaticVariablesDeclarations(NetworkDescriptor network, StringBuilder body) {
    network = Optional.ofNullable(network).orElse(new NetworkDescriptor());
    return ExchangeGenUtil.generateNamesStaticVariablesDeclarations(
        CollectionUtil.emptyIfNull(network.getWebsocketClients())
          .stream()
          .map(WebsocketClientDescriptor::getName)
          .toList(),
        "WebsocketClient", 
        body,
        "");
  }
  
}
