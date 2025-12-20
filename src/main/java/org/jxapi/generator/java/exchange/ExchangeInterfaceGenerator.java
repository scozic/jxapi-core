package org.jxapi.generator.java.exchange;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jxapi.exchange.Exchange;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.gen.NetworkDescriptor;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.JavaTypeGenerator;
import org.jxapi.netutils.rest.ratelimits.RateLimitRule;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Generates Source code of Java interface described by a {@link ExchangeInterfaceGenerator}. 
 * <ul>
 * <li>Generates a public static final field for exchange ID</li>
 * <li>Generates a getter method for each API</li>
 * </ul>
 */
public class ExchangeInterfaceGenerator extends JavaTypeGenerator {
  
  /**
   * Name of the exchange 'id' field.
   */
  public static final String EXCHANGE_ID_VARIABLE = "ID";
  
  /**
   * Name of the exchange 'version' field.
   */
  public static final String EXCHANGE_VERSION_VARIABLE = "VERSION";
  
  private final ExchangeDescriptor exchangeDescriptor;
  
  private final PlaceHolderResolver docPlaceHolderResolver;
  
  /**
   * Constructor.
   * 
   * @param exchangeDescriptor the exchange descriptor to generate classes for
   * @param docPlaceHolderResolver the place holder resolver to use for resolving placeholders in descriptions.
   */
  public ExchangeInterfaceGenerator(ExchangeDescriptor exchangeDescriptor, PlaceHolderResolver docPlaceHolderResolver) {
    super(ExchangeGenUtil.getExchangeInterfaceName(exchangeDescriptor));
    this.exchangeDescriptor = exchangeDescriptor;
    this.docPlaceHolderResolver = Optional.ofNullable(docPlaceHolderResolver).orElse(PlaceHolderResolver.NO_OP);
    this.setParentClassName(Exchange.class.getName());
  }
  
  @Override
  public String generate() {    
    setDescription(generateDescription());
    setTypeDeclaration("public interface");
    
    // Static fields
    appendToBody("\n")
      .append(JavaCodeGenUtil.generateJavaDoc("ID of the '" + exchangeDescriptor.getId() + "' exchange"))
      .append("\nString ")
      .append(EXCHANGE_ID_VARIABLE)
      .append(" = ")
      .append(JavaCodeGenUtil.getQuotedString(exchangeDescriptor.getId()))
      .append(";\n\n")
      .append(JavaCodeGenUtil.generateJavaDoc("Version of the '" + exchangeDescriptor.getId() + "' exchange"))
      .append("\nString ")
      .append(EXCHANGE_VERSION_VARIABLE)
      .append(" = ")
      .append(JavaCodeGenUtil.getQuotedString(exchangeDescriptor.getVersion()))
      .append(";\n");
    generateNetworkClientIdsConstants();
    generateApiMethodsDeclarations();
    
    generateRateLimitRuleMethodDeclarations();
    
    return super.generate();
  }
  
  private void generateNetworkClientIdsConstants() {
    NetworkDescriptor network = exchangeDescriptor.getNetwork();
    ExchangeGenUtil.generateHttpClientNamesStaticVariablesDeclarations(network, body);
    ExchangeGenUtil.generateWebsocketClientNamesStaticVariablesDeclarations(network, body);
  }
  
  private void generateApiMethodsDeclarations() {
    List<ExchangeApiDescriptor> apis = CollectionUtil.emptyIfNull(exchangeDescriptor.getApis());
    if (apis.isEmpty()) {
      return;
    }
    Map<String, String> apiGetterMethodNames = ExchangeGenUtil.getApiGroupGetterMethodNames(apis);
    appendToBody("\n// API groups\n");
    for (ExchangeApiDescriptor api: apis) {
      String apiClassName = ExchangeGenUtil.getApiInterfaceClassName(exchangeDescriptor, api);
      String apiSimpleClassName = JavaCodeGenUtil.getClassNameWithoutPackage(apiClassName);
      String getApiMethodSignature = apiSimpleClassName + " " + apiGetterMethodNames.get(api.getName()) + "()";
      addImport(apiClassName);
      appendToBody("\n")
        .append(getGetApiMethodJavadoc(api))
        .append("\n")
        .append( getApiMethodSignature)
        .append(";\n");
    }
  }
  
  private String getGetApiMethodJavadoc(ExchangeApiDescriptor api) {
    StringBuilder s = new StringBuilder();
    s.append("@return " ).append(this.docPlaceHolderResolver.resolve(api.getDescription()));
    return JavaCodeGenUtil.generateJavaDoc(s.toString());
  }
  
  private void generateRateLimitRuleMethodDeclarations() {
    List<RateLimitRule> rateLimits = RateLimitRule.fromDescriptors(exchangeDescriptor.getRateLimits());
    if (rateLimits.isEmpty()) {
      return;
    }
    appendToBody("\n// Rate limits\n");
    for (RateLimitRule rateLimit : rateLimits) {
      String rateLimitName = rateLimit.getId();
      if (!JavaCodeGenUtil.isValidCamelCaseIdentifier(rateLimitName)) {
        throw new IllegalArgumentException(String.format(
            "Rate limit name '%s' is not a valid camel case identifier in exchange '%s'", 
            rateLimitName, 
            exchangeDescriptor.getId()));
      }
      addImport(RateLimitRule.class);
      appendToBody("\n")
      .append(ExchangeGenUtil.generateRateLimitRuleInterfaceMethodDeclaration(rateLimitName));
    }
  }
  
  
  
  private String generateDescription() {
    StringBuilder s = new StringBuilder()
        .append(exchangeDescriptor.getId())
        .append(" API<br>\n")
        .append(this.docPlaceHolderResolver.resolve(exchangeDescriptor.getDescription()))
        .append("\n");
    String docUrl = exchangeDescriptor.getDocUrl();
    if (docUrl != null) {
      s.append("@see ")
       .append(JavaCodeGenUtil.getHtmlLink(docUrl, "Reference documentation"));
    }
    return s.toString();
  }

}
