package org.jxapi.generator.java.exchange;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import org.jxapi.exchange.AbstractExchange;
import org.jxapi.exchange.Exchange;
import org.jxapi.exchange.ExchangeApi;
import org.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.JavaTypeGenerator;
import org.jxapi.netutils.rest.ratelimits.RateLimitRule;
import org.jxapi.netutils.rest.ratelimits.RequestThrottler;

/**
 * Generates source code of implementation of an interface  {@link ExchangeDescriptor}
 * <ul>
 * <li>Generates a class that implements the interface {@link ExchangeDescriptor}</li>
 * <li>Generates a constructor that initializes the exchange name and properties, and {@link ExchangeApi} implementation instances</li>
 * <li>Generates a public static final field for each rate limit rule. Exchange API implementation should 
 * use reference to that field when in REST endpoint calls</li>
 * <li>Generates a field for each API</li>
 * <li>Generates a getter for each API, implmenting corresponding Exchange interface</li>
 * <li>Generates a request throttler if there are rate limits, and at least one API that has REST endpoints. 
 * When some rate limits are defined in exchange, and exchange API has at least one API, its implementation 
 * constructor is expected to have a RequestThrottler argument</li>
 * <li>Generated class javadoc will display link to corresponding Exchange interface and a warning about generated code</li>
 * <li>Will throw an exception if there are duplicate rate limit rule names</li>
 * <li>Will throw an exception if a rate limit rule has a <code>null</code> id</li>
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
	private static final String REQUEST_THROTTLER_VARIABLE_NAME = "requestThrottler";
	
	/**
	 * Generates the name of the interface implementation class for the given exchange descriptor
	 * @param exchangeDescriptor exchange descriptor
	 * @return full name of the interface implementation class
	 */
	public static String getExchangeInterfaceName(ExchangeDescriptor exchangeDescriptor) {
		return exchangeDescriptor.getBasePackage() + "." + JavaCodeGenUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) + "Exchange";
	}
	
	private final ExchangeDescriptor exchangeDescriptor;
	private final Set<String> rateLimitNames = new HashSet<>();
	
	/**
	 * Constructor.
	 * 
	 * @param exchangeDescriptor the exchange descriptor to generate classes for
	 */
	public ExchangeInterfaceImplementationGenerator(ExchangeDescriptor exchangeDescriptor) {
		super(ExchangeJavaGenUtil.getExchangeInterfaceImplementationName(exchangeDescriptor));
		this.exchangeDescriptor = exchangeDescriptor;
	}
	
	@Override
	public String generate() {		
		String pkgPrefix =  exchangeDescriptor.getBasePackage() + ".";
		String simpleInterfaceName = JavaCodeGenUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) + "Exchange";
		String fullInterfaceName = pkgPrefix + simpleInterfaceName;
		String simpleImplementationName = simpleInterfaceName + "Impl";
		setTypeDeclaration("public class");
		setImplementedInterfaces(Arrays.asList(fullInterfaceName));
		setParentClassName(AbstractExchange.class.getName());
		setDescription("Actual implementation of {@link " + simpleInterfaceName + "}<br>");
		appendToBody("\n");
		
		String httpUrlDeclaration = ExchangeJavaGenUtil.getHttpUrlVariableDeclaration(exchangeDescriptor);
		if (httpUrlDeclaration != null) {
			appendToBody(httpUrlDeclaration).append("\n\n");
		}
		
		String websocketUrlDeclaration = ExchangeJavaGenUtil.getWebsocketUrlVariableDeclaration(exchangeDescriptor);
		if (websocketUrlDeclaration != null) {
			appendToBody(websocketUrlDeclaration).append("\n\n");
		}
		
		List<RateLimitRule> rateLimits = exchangeDescriptor.getRateLimits();
		List<ExchangeApiDescriptor> apis = exchangeDescriptor.getApis();
		boolean hasRateLimits = !CollectionUtils.isEmpty(rateLimits);
		if (hasRateLimits) {
			rateLimits.forEach(this::generateRateLimitVariable);
			if (apis != null && apis.stream().anyMatch(api -> !CollectionUtils.isEmpty(api.getRestEndpoints()))) {
				addImport(RequestThrottler.class);
				appendToBody("\nprivate final ");
				appendToBody(RequestThrottler.class.getSimpleName());
				appendToBody(" ");
				appendToBody(REQUEST_THROTTLER_VARIABLE_NAME);
				appendToBody(" = new ");
				appendToBody(RequestThrottler.class.getSimpleName());
				appendToBody("(\"");
				appendToBody(exchangeDescriptor.getName());
				appendToBody("\");\n");
			}
		}
		
		StringBuilder implementationConstructorBody = new StringBuilder();
		implementationConstructorBody
			.append("super(")
			.append(ExchangeInterfaceGenerator.EXCHANGE_ID_VARIABLE)
			.append(", ")
			.append(EXCHANGE_NAME_PARAMETER)
			.append(", ")
			.append(PROPERTIES_PARAMETER)
			.append(");\n");
		
		StringBuilder apiMethodsDeclarations = new StringBuilder(); 
		if (exchangeDescriptor.getApis() != null) {
			for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {
				String apiClassName = ExchangeJavaGenUtil.getApiInterfaceClassName(exchangeDescriptor, api);
				String apiSimpleClassName = JavaCodeGenUtil.getClassNameWithoutPackage(apiClassName);
				String apiImplClassName = apiClassName + "Impl";
				String simpleApiImplClassName = JavaCodeGenUtil.getClassNameWithoutPackage(apiImplClassName);
				addImport(apiClassName);
				addImport(apiImplClassName);
				String apiVariableName = JavaCodeGenUtil.firstLetterToLowerCase(apiSimpleClassName);
				String getApiMethodSignature = apiSimpleClassName + " get" + apiSimpleClassName + "()";
				appendToBody("private final " + apiSimpleClassName + " " + apiVariableName + ";\n");
				implementationConstructorBody
						.append("this.")
						.append(apiVariableName)
						.append(" = addApi(new ")
						.append(simpleApiImplClassName)
						.append("(getName(), ")
						.append(PROPERTIES_PARAMETER);
				if (hasRateLimits && !CollectionUtils.isEmpty(api.getRestEndpoints())) {
					implementationConstructorBody.append(", ").append(REQUEST_THROTTLER_VARIABLE_NAME);
				}
				implementationConstructorBody.append("));\n");
				apiMethodsDeclarations.append("@Override\npublic ")
									  .append(getApiMethodSignature)
									  .append(" ")
									  .append(JavaCodeGenUtil.generateCodeBlock("return this." + apiVariableName + ";\n"))
									  .append("\n");
			}
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
		
		return super.generate();
	}
	
	private String generateRateLimitVariable(RateLimitRule rateLimitRule) {
		String name = rateLimitRule.getId();
		if (name == null) {
			throw new IllegalArgumentException("rateLimitRule:" + rateLimitRule + " should have an id");
		}
		String variableName = ExchangeJavaGenUtil.generateRateLimitVariableName(name);
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
		appendToBody("public static final " + declaration + "\n");
		return variableName;
	}

}
