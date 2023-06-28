package com.scz.jxapi.generator.exchange;

import java.util.Arrays;
import java.util.Properties;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.JavaTypeGenerator;

public class ExchangeInterfaceImplementationGenerator extends JavaTypeGenerator {
	
	public static String getExchangeInterfaceName(ExchangeDescriptor exchangeDescriptor) {
		return exchangeDescriptor.getBasePackage() + "." + JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) + "ExchangeImpl";
	}

	private final ExchangeDescriptor exchangeDescriptor; 
	
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
		
		setTypeDeclaration("public class ");
		setImplementedInterfaces(Arrays.asList(fullInterfaceName));
		setDescription("Actual implementation of {@link " + simpleInterfaceName + "}<br/>\n"
				   + JavaCodeGenerationUtil.GENERATED_CODE_WARNING);
		appendToBody("\n");
		
		StringBuilder implementationConstructorBody = new StringBuilder();
		for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {
			String apiClassName = ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, api);
			String apiSimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(apiClassName);
			String apiImplClassName = apiClassName + "Impl";
			String simpleApiImplClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(apiImplClassName);
			addImport(apiClassName);
			addImport(apiImplClassName);
			String apiVariableName = JavaCodeGenerationUtil.firstLetterToLowerCase(apiSimpleClassName);
			implementationConstructorBody.append("this." + apiVariableName + " = new " + simpleApiImplClassName + "(properties);\n");
			appendToBody("private final " + apiSimpleClassName + " " + apiVariableName + ";\n");
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

}
