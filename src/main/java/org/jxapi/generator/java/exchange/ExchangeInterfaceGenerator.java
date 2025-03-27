package org.jxapi.generator.java.exchange;

import org.jxapi.exchange.Exchange;
import org.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.ExchangeDescriptor;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.JavaTypeGenerator;

/**
 * Generates Source code of Java interface described by a {@link ExchangeInterfaceGenerator}. 
 * <ul>
 * <li>Generates a public static final field for exchange ID</li>
 * <li>Generates a getter method for each API</li>
 * </ul>
 */
public class ExchangeInterfaceGenerator extends JavaTypeGenerator {
	
	/**
	 * Name of the exchange ID field.
	 */
	public static final String EXCHANGE_ID_VARIABLE = "ID";
	
	private final ExchangeDescriptor exchangeDescriptor;
	
	/**
	 * Constructor.
	 * 
	 * @param exchangeDescriptor the exchange descriptor to generate classes for
	 */
	public ExchangeInterfaceGenerator(ExchangeDescriptor exchangeDescriptor) {
		super(ExchangeJavaGenUtil.getExchangeInterfaceName(exchangeDescriptor));
		this.exchangeDescriptor = exchangeDescriptor;
		this.setParentClassName(Exchange.class.getName());
	}
	
	@Override
	public String generate() {		
		setDescription(generateDescription());
		setTypeDeclaration("public interface");
		
		appendToBody("\n")
		    .append(JavaCodeGenUtil.generateJavaDoc("ID of the '" + exchangeDescriptor.getName() + "' exchange"))
		    .append("\nString ")
			.append(EXCHANGE_ID_VARIABLE)
			.append(" = ")
			.append(JavaCodeGenUtil.getQuotedString(exchangeDescriptor.getName()))
			.append(";\n");
		
		for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {
			String apiClassName = ExchangeJavaGenUtil.getApiInterfaceClassName(exchangeDescriptor, api);
			String apiSimpleClassName = JavaCodeGenUtil.getClassNameWithoutPackage(apiClassName);
			String getApiMethodSignature = apiSimpleClassName + " get" + apiSimpleClassName + "()";
			addImport(apiClassName);
			appendToBody("\n")
				.append(getGetApiMethodJavadoc(api))
				.append("\n")
				.append( getApiMethodSignature)
				.append(";\n");
		}
		return super.generate();
	}
	
	private String getGetApiMethodJavadoc(ExchangeApiDescriptor api) {
		StringBuilder s = new StringBuilder();
		s.append("@return " ).append(api.getDescription());
		return JavaCodeGenUtil.generateJavaDoc(s.toString());
	}
	
	private String generateDescription() {
		StringBuilder s = new StringBuilder()
				.append(exchangeDescriptor.getName())
				.append(" API<br>\n")
				.append(exchangeDescriptor.getDescription())
				.append("\n");
		String docUrl = exchangeDescriptor.getDocUrl();
		if (docUrl != null) {
			s.append("@see ")
			 .append(JavaCodeGenUtil.getHtmlLink(docUrl, "Reference documentation"));
		}
		return s.toString();
	}

}
