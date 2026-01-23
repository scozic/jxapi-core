package org.jxapi.generator.java.exchange;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.jxapi.exchange.AbstractExchange;
import org.jxapi.exchange.Exchange;
import org.jxapi.exchange.ExchangeApi;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.gen.HttpClientDescriptor;
import org.jxapi.exchange.descriptor.gen.NetworkDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketClientDescriptor;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.JavaTypeGenerator;
import org.jxapi.generator.java.exchange.api.ExchangeApiInterfaceGenerator;
import org.jxapi.netutils.rest.ratelimits.RateLimitRule;
import org.jxapi.netutils.rest.ratelimits.RequestThrottler;
import org.jxapi.util.CollectionUtil;
import org.springframework.util.CollectionUtils;

/**
 * Generates source code of implementation of an interface
 * {@link ExchangeDescriptor}
 * <ul>
 * <li>Generates a class that extends {@link AbstractExchange} and therefore
 * implements the {@link ExchangeDescriptor} interface</li>
 * <li>Generates a constructor that initializes the exchange name and
 * properties, and {@link ExchangeApi} implementation instances</li>
 * <li>Generates a public static final field for each rate limit rule. Exchange
 * API implementation should use reference to that field when in REST endpoint
 * calls</li>
 * <li>Generates a field for each API</li>
 * <li>Generates a getter for each API, implmenting corresponding Exchange
 * interface</li>
 * <li>Generates a request throttler if there are rate limits, and at least one
 * API that has REST endpoints. When some rate limits are defined in exchange,
 * and exchange API has at least one API, its implementation constructor is
 * expected to have a RequestThrottler argument</li>
 * <li>Generates a getter for each rate limit rule</li>
 * <li>The <code>httpUrl</code> and <code>websocket url</code> properties are
 * set in constructor using corresponding properties in descriptor, with
 * eventual placeholders resolved against constants and configuration properties
 * </li>
 * <li>Generated class javadoc will display link to corresponding Exchange
 * interface and a warning about generated code</li>
 * <li>Will throw an exception if there are duplicate rate limit rule names</li>
 * <li>Will throw an exception if a rate limit rule has a <code>null</code>
 * id</li>
 * </ul>
 * 
 * @see ExchangeDescriptor
 * @see ExchangeApi
 * @see RateLimitRule
 * @see RequestThrottler
 * @see Exchange
 */
public class ExchangeInterfaceImplementationGenerator extends JavaTypeGenerator {
  
  private static final String EXCHANGE_NAME_PARAMETER = "exchangeName";
  private static final String PROPERTIES_PARAMETER = "properties";
  private static final String ARG_SEPARATOR = ",\n" + JavaCodeGenUtil.INDENTATION;
  
  
  /**
   * Generates the name of the interface implementation class for the given exchange descriptor
   * @param exchangeDescriptor exchange descriptor
   * @return full name of the interface implementation class
   */
  public static String getExchangeInterfaceName(ExchangeDescriptor exchangeDescriptor) {
    return exchangeDescriptor.getBasePackage() + "." + JavaCodeGenUtil.firstLetterToUpperCase(exchangeDescriptor.getId()) + "Exchange";
  }
  
  private final ExchangeDescriptor exchangeDescriptor;
  private final Set<String> rateLimitNames = new HashSet<>();
  private final List<RateLimitRule> rateLimits;
  
  /**
   * Constructor.
   * 
   * @param exchangeDescriptor the exchange descriptor to generate classes for
   */
  public ExchangeInterfaceImplementationGenerator(ExchangeDescriptor exchangeDescriptor) {
    super(ExchangeGenUtil.getExchangeInterfaceImplementationName(exchangeDescriptor));
    this.exchangeDescriptor = exchangeDescriptor;
    this.setParentClassName(AbstractExchange.class.getName());
    rateLimits = RateLimitRule.fromDescriptors(exchangeDescriptor.getRateLimits());
  }
  
  @Override
  public String generate() {    
    String pkgPrefix =  exchangeDescriptor.getBasePackage() + ".";
    String simpleInterfaceName = JavaCodeGenUtil.firstLetterToUpperCase(exchangeDescriptor.getId()) + "Exchange";
    String fullInterfaceName = pkgPrefix + simpleInterfaceName;
    String simpleImplementationName = simpleInterfaceName + "Impl";
    setTypeDeclaration("public class");
    setImplementedInterfaces(Arrays.asList(fullInterfaceName));
    setParentClassName(AbstractExchange.class.getName());
    setDescription("Actual implementation of {@link " + simpleInterfaceName + "}<br>");
    appendToBody("\n");
    
    
    List<ExchangeApiDescriptor> apis = CollectionUtil.emptyIfNull(exchangeDescriptor.getApis());
    Map<String, String> apiGetterUniqueNames = ExchangeGenUtil.getApiGroupGetterMethodNames(apis);
    Map<String, String> apiVariableNames = getApiVariableNames(apis);
    boolean hasRateLimits = !CollectionUtils.isEmpty(rateLimits);
    if (hasRateLimits) {
      rateLimits.forEach(this::generateRateLimitVariable);
    }
    
    StringBuilder implementationConstructorBody = new StringBuilder();
    implementationConstructorBody
      .append("super(")
      .append(ExchangeInterfaceGenerator.EXCHANGE_ID_VARIABLE)
      .append(JavaCodeGenUtil.SUPER_ARG_SEPARATOR)
      .append(ExchangeInterfaceGenerator.EXCHANGE_VERSION_VARIABLE)
      .append(JavaCodeGenUtil.SUPER_ARG_SEPARATOR)
      .append(EXCHANGE_NAME_PARAMETER)
      .append(JavaCodeGenUtil.SUPER_ARG_SEPARATOR)
      .append(PROPERTIES_PARAMETER)
      .append(JavaCodeGenUtil.SUPER_ARG_SEPARATOR)
      .append(ExchangeGenUtil.generateSubstitutionInstructionDeclaration(
          exchangeDescriptor.getHttpUrl(), 
          exchangeDescriptor, 
          null,
          PROPERTIES_PARAMETER,
          getImports()))
      .append(JavaCodeGenUtil.SUPER_ARG_SEPARATOR)
      .append(hasRateLimits)
      .append(");\n");
    generateCreateNetworkInstructions(implementationConstructorBody);
    StringBuilder apiMethodsDeclarations = new StringBuilder(); 
    if (exchangeDescriptor.getApis() != null) {
      implementationConstructorBody.append("\n // APIs\n");
      for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {
        String apiClassName = ExchangeGenUtil.getApiInterfaceClassName(exchangeDescriptor, api);
        String apiSimpleClassName = JavaCodeGenUtil.getClassNameWithoutPackage(apiClassName);
        String apiImplClassName = apiClassName + "Impl";
        String simpleApiImplClassName = JavaCodeGenUtil.getClassNameWithoutPackage(apiImplClassName);
        addImport(apiClassName);
        addImport(apiImplClassName);
        String apiVariableName = apiVariableNames.get(api.getName());
        String getApiMethodSignature = apiSimpleClassName + " " + apiGetterUniqueNames.get(api.getName()) + "()";
        appendToBody("private final " + apiSimpleClassName + " " + apiVariableName + ";\n");
        implementationConstructorBody
            .append("this.")
            .append(apiVariableName)
            .append(" = addApi(")
            .append(apiSimpleClassName)
            .append(".")
            .append(ExchangeApiInterfaceGenerator.EXCHANGE_API_NAME_VARIABLE)
            .append(", new ")
            .append(simpleApiImplClassName)
            .append("(this, exchangeObserver));\n");
        apiMethodsDeclarations.append("@Override\npublic ")
                    .append(getApiMethodSignature)
                    .append(" ")
                    .append(JavaCodeGenUtil.generateCodeBlock("return this." + apiVariableName + ";\n"))
                    .append("\n");
      }
    }
    
    String afterInitHookFactory = exchangeDescriptor.getAfterInitHookFactory();
    if (afterInitHookFactory != null) {
      implementationConstructorBody
          .append("afterInit(")
          .append(JavaCodeGenUtil.getQuotedString(afterInitHookFactory))
          .append(");\n");
    }
    addImport(Properties.class);
    
    appendToBody("\n");
    String implementationConstructorSignature = new StringBuilder()
            .append("public ")
            .append(simpleImplementationName)
            .append("(String ")
            .append(EXCHANGE_NAME_PARAMETER)
            .append(", Properties ")
            .append(PROPERTIES_PARAMETER)
            .append(")").toString();
          
    appendMethod(implementationConstructorSignature, implementationConstructorBody.toString());
    appendToBody("\n");
    appendToBody(apiMethodsDeclarations.toString());
    generateRateLimitRuleGetters();
    return super.generate();
  }
  
  private Map<String, String> getApiVariableNames(List<ExchangeApiDescriptor> apis) {
    return JavaCodeGenUtil.getUniqueCamelCaseVariableNames(
        apis.stream().map(ExchangeApiDescriptor::getName).toList(),
        false)
      .entrySet()
      .stream()
      .collect(Collectors.toMap(
          Map.Entry::getKey, 
          entry -> entry.getValue() + "Api"
      ));
  }
  
  private void generateRateLimitRuleGetters() {
    for (RateLimitRule rateLimitRule : rateLimits) {
      appendToBody(ExchangeGenUtil.generateRateLimitGetterImplementationMethodDeclaration(rateLimitRule.getId()))
          .append("\n");
    }
  }
  
  private String generateRateLimitVariable(RateLimitRule rateLimitRule) {
    String name = rateLimitRule.getId();
    if (name == null) {
      throw new IllegalArgumentException("rateLimitRule:" + rateLimitRule + " should have an id");
    }
    String variableName = ExchangeGenUtil.generateRateLimitVariableName(name);
    if (rateLimitNames.contains(name)) {
      throw new IllegalArgumentException("Duplicate rate limit rule name:[" + name + "] in " + exchangeDescriptor);
    }
    rateLimitNames.add(name);
    String declaration = RateLimitRule.class.getSimpleName() + " " + variableName + " = ";
    addImport(RateLimitRule.class);
    if (rateLimitRule.getMaxTotalWeight() >= 0) {
      declaration +=  "RateLimitRule.createWeightedRule(\"" + name + "\", " + rateLimitRule.getTimeFrame()+ ", " + rateLimitRule.getMaxTotalWeight() + ");";
    } else {
      declaration +=  "RateLimitRule.createRule(\"" + name + "\", " + rateLimitRule.getTimeFrame()+ ", " + rateLimitRule.getMaxRequestCount() + ");";
    }
    appendToBody("private final " + declaration + "\n");
    return variableName;
  }
  
  private void generateCreateNetworkInstructions(StringBuilder constructorBody) {
    constructorBody.append("// Network\n");
    NetworkDescriptor network = exchangeDescriptor.getNetwork();
    for (HttpClientDescriptor httpClient: CollectionUtil.emptyIfNull(network.getHttpClients())) {
      constructorBody.append("createHttpClient(")
          .append(JavaCodeGenUtil.getQuotedString(httpClient.getName()))
          .append(ARG_SEPARATOR)
          .append(JavaCodeGenUtil.getQuotedString(httpClient.getHttpRequestInterceptorFactory()))
          .append(ARG_SEPARATOR)
          .append(JavaCodeGenUtil.getQuotedString(httpClient.getHttpRequestExecutorFactory()))
          .append(ARG_SEPARATOR)
          .append(httpClient.getHttpRequestTimeout())
          .append(");\n");
    }
    
    for (WebsocketClientDescriptor websocketClient : CollectionUtil.emptyIfNull(network.getWebsocketClients())) {
      constructorBody.append("createWebsocketClient(")
          .append(JavaCodeGenUtil.getQuotedString(websocketClient.getName()))
          .append(ARG_SEPARATOR)
          .append(generateConstantOrPropertiesPlaceholdersSubstitutionInstruction(websocketClient.getWebsocketUrl()))
          .append(ARG_SEPARATOR)
          .append(generateConstantOrPropertiesPlaceholdersSubstitutionInstruction(websocketClient.getWebsocketFactory()))
          .append(ARG_SEPARATOR)
          .append(generateConstantOrPropertiesPlaceholdersSubstitutionInstruction(websocketClient.getWebsocketHookFactory()))
          .append(");\n");
    }
  }
  
  private String generateConstantOrPropertiesPlaceholdersSubstitutionInstruction(String template) {
    return ExchangeGenUtil.generateSubstitutionInstructionDeclaration(
        template, 
        exchangeDescriptor,  
        List.of(),
        PROPERTIES_PARAMETER, 
        getImports());
  }

}
