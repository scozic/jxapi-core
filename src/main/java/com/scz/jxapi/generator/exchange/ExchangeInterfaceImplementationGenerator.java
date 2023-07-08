package com.scz.jxapi.generator.exchange;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.JavaTypeGenerator;
import com.scz.jxapi.netutils.rest.ratelimits.RateLimitRule;
import com.scz.jxapi.netutils.rest.ratelimits.RequestThrottler;

/**
 * Generates source code of implementation of an interface  {@link ExchangeDescriptor}
 */
public class ExchangeInterfaceImplementationGenerator extends JavaTypeGenerator {
	
	private static final String REQUEST_THROTTLER_VARIABLE_NAME = "requestThrottler";
	
	public static String getExchangeInterfaceName(ExchangeDescriptor exchangeDescriptor) {
		return exchangeDescriptor.getBasePackage() + "." + JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) + "ExchangeImpl";
	}

	private final ExchangeDescriptor exchangeDescriptor;
	private final Set<String> rateLimitVariableNames = new HashSet<>();
	
	public ExchangeInterfaceImplementationGenerator(ExchangeDescriptor exchangeDescriptor) {
		super(getExchangeInterfaceName(exchangeDescriptor));
		this.exchangeDescriptor = exchangeDescriptor;
	}
	
	@Override
	public String generate() {		
		String pkgPrefix =  exchangeDescriptor.getBasePackage() + ".";
		String simpleInterfaceName = JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) + "Exchange";
		String fullInterfaceName = pkgPrefix + simpleInterfaceName;
		String simpleImplementationName = simpleInterfaceName + "Impl";
		setTypeDeclaration("public class");
		setImplementedInterfaces(Arrays.asList(fullInterfaceName));
		setDescription("Actual implementation of {@link " + simpleInterfaceName + "}<br/>\n"
				   + JavaCodeGenerationUtil.GENERATED_CODE_WARNING);
		appendToBody("\n");
		
		List<RateLimitRule> rateLimits = exchangeDescriptor.getRateLimits();
		boolean hasRateLimits = rateLimits != null && !rateLimits.isEmpty();
		if (hasRateLimits) {
			rateLimits.forEach(rateLimit -> generateRateLimitVariable(rateLimit));
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
		
		StringBuilder implementationConstructorBody = new StringBuilder();
		for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {
			String apiClassName = ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, api);
			String apiSimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(apiClassName);
			String apiImplClassName = apiClassName + "Impl";
			String simpleApiImplClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(apiImplClassName);
			addImport(apiClassName);
			addImport(apiImplClassName);
			String apiVariableName = JavaCodeGenerationUtil.firstLetterToLowerCase(apiSimpleClassName);
			appendToBody("private final " + apiSimpleClassName + " " + apiVariableName + ";\n");
			implementationConstructorBody
					.append("this.")
					.append(apiVariableName)
					.append(" = new ")
					.append(simpleApiImplClassName)
					.append("(properties");
			if (hasRateLimits) {
				implementationConstructorBody.append(", ").append(REQUEST_THROTTLER_VARIABLE_NAME);
			}
			
			implementationConstructorBody.append(");\n");
		}
		addImport(Properties.class);
		
		appendMethod("\npublic " + simpleImplementationName + "(Properties properties)", implementationConstructorBody.toString());
		appendToBody("\n");
		for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {
			String apiClassName = ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, api);
			String apiSimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(apiClassName);
			String getApiMethodSignature = apiSimpleClassName + " get" + apiSimpleClassName + "()";
			String apiVariableName = JavaCodeGenerationUtil.firstLetterToLowerCase(apiSimpleClassName);
			appendMethod("@Override\npublic " + getApiMethodSignature, "return this." + apiVariableName + ";\n");
			appendToBody("\n");
		}
		
		return super.generate();
	}
	
	private String generateRateLimitVariable(RateLimitRule rateLimitRule) {
		String name = rateLimitRule.getId();
		if (name == null) {
			throw new IllegalArgumentException("rateLimitRule:" + rateLimitRule + " should have an id");
		}
		String variableName = ExchangeJavaWrapperGeneratorUtil.generateRateLimitVariableName(name);
		// Add new rule definition if no one exists with same name. Otherwise, rule is expected to be a reference to existing one.
		if (!rateLimitVariableNames.contains(name)) {
			rateLimitVariableNames.add(name);
			String declaration = RateLimitRule.class.getSimpleName() + " " + variableName + " = ";
			addImport(RateLimitRule.class);
			if (rateLimitRule.getMaxTotalWeight() >= 0) {
				declaration +=  "RateLimitRule.createWeightedRule(\"" + name + "\", " + rateLimitRule.getTimeFrame()+ ", " + rateLimitRule.getMaxTotalWeight() + ");";
			} else {
				declaration +=  "RateLimitRule.createRule(\"" + name + "\", " + rateLimitRule.getTimeFrame()+ ", " + rateLimitRule.getMaxRequestCount() + ");";
			}
			appendToBody("public static final " + declaration + "\n");
		}
		
		return variableName;
	}

}
