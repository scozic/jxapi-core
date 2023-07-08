package com.scz.jxapi.generator.exchange;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.JavaTypeGenerator;

/**
 * Generates Source code of Java interface described by a {@link ExchangeInterfaceGenerator}. 
 */
public class ExchangeInterfaceGenerator extends JavaTypeGenerator {
	
	private final ExchangeDescriptor exchangeDescriptor;
	
	public ExchangeInterfaceGenerator(ExchangeDescriptor exchangeDescriptor) {
		super(ExchangeJavaWrapperGeneratorUtil.getExchangeInterfaceName(exchangeDescriptor));
		this.exchangeDescriptor = exchangeDescriptor;
	}
	
	@Override
	public String generate() {		
		setDescription(exchangeDescriptor.getName() + " API</br>\n" 
				+ exchangeDescriptor.getDescription() + "\n" 
				+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING);
		setTypeDeclaration("public interface");
		for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {
			String apiClassName = ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, api);
			String apiSimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(apiClassName);
			String getApiMethodSignature = apiSimpleClassName + " get" + apiSimpleClassName + "()";
			addImport(apiClassName);
			appendToBody("\n" + getApiMethodSignature + ";\n");
		}
		return super.generate();
	}

}
