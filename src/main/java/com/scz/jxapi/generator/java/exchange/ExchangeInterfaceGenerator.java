package com.scz.jxapi.generator.java.exchange;

import com.scz.jxapi.exchange.Exchange;
import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.JavaTypeGenerator;

/**
 * Generates Source code of Java interface described by a {@link ExchangeInterfaceGenerator}. 
 * <ul>
 * <li>Generates a public static final field for exchange ID</li>
 * <li>Generates a getter method for each API</li>
 * </ul>
 */
public class ExchangeInterfaceGenerator extends JavaTypeGenerator {
	
	public static final String EXCHANGE_ID_VARIABLE = "ID";
	
	private final ExchangeDescriptor exchangeDescriptor;
	
	public ExchangeInterfaceGenerator(ExchangeDescriptor exchangeDescriptor) {
		super(ExchangeJavaWrapperGeneratorUtil.getExchangeInterfaceName(exchangeDescriptor));
		this.exchangeDescriptor = exchangeDescriptor;
		this.setParentClassName(Exchange.class.getName());
	}
	
	@Override
	public String generate() {		
		setDescription(exchangeDescriptor.getName() + " API</br>\n" 
				+ exchangeDescriptor.getDescription() + "\n" 
				+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING);
		setTypeDeclaration("public interface");
		
		appendToBody("\nString ")
			.append(EXCHANGE_ID_VARIABLE)
			.append(" = ")
			.append(JavaCodeGenerationUtil.getQuotedString(exchangeDescriptor.getName()))
			.append(";\n");
		
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
