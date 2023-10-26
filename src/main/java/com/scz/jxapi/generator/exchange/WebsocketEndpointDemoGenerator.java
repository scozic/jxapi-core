package com.scz.jxapi.generator.exchange;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.JavaTypeGenerator;
import com.scz.jxapi.util.TestJXApiProperties;

public class WebsocketEndpointDemoGenerator extends JavaTypeGenerator {
	
	public WebsocketEndpointDemoGenerator(ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor exchangeApiDescriptor, WebsocketEndpointDescriptor websocketApi) {
		super(ExchangeJavaWrapperGeneratorUtil.getWebsocketApiDemoClassName(exchangeDescriptor, exchangeApiDescriptor, websocketApi));
		setTypeDeclaration("public class");
		String requestClassName = ExchangeJavaWrapperGeneratorUtil.generateWebsocketEndpointRequestClassName(exchangeDescriptor, exchangeApiDescriptor, websocketApi);
		addImport(requestClassName);
		String requestSimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(requestClassName);
		String apiInterfaceClassName = ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor);
		addImport(apiInterfaceClassName);
		String subscribeMethodName = "subscribe" + JavaCodeGenerationUtil.firstLetterToUpperCase(websocketApi.getName());
		setDescription("Snippet to test call to {@link " 
						+ ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor) 
						+ "#" 
						+ subscribeMethodName 
						+ "(" + requestClassName + ")}\n"
						+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING);
		JavaCodeGenerationUtil.generateSlf4jLoggerDeclaration(this);
		websocketApi.getParameters().forEach(p -> generateParameterValueConstantDeclaration(exchangeDescriptor, exchangeApiDescriptor, websocketApi, p));
		String exchangeInterfaceClassName = ExchangeJavaWrapperGeneratorUtil.getExchangeInterfaceName(exchangeDescriptor);
		generateMainMethodBody(requestSimpleClassName, 
							   apiInterfaceClassName, 
							   exchangeInterfaceClassName,
							   JavaCodeGenerationUtil.firstLetterToLowerCase(exchangeDescriptor.getName()), 
							   websocketApi.getParameters(), 
							   subscribeMethodName);
	}
	
	private void generateMainMethodBody(String requestSimpleClassName, String apiInterfaceClassName, String exchangeInterfaceClassName, String exchangeName, List<EndpointParameter> parameters, String apiMethodName) {
		String exchangeImplClassName = exchangeInterfaceClassName + "Impl";
		String simpleApiClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(apiInterfaceClassName);
		addImport(exchangeImplClassName);
		addImport(TestJXApiProperties.class);
		StringBuilder body = new StringBuilder();
		body.append(JavaCodeGenerationUtil.getClassNameWithoutPackage(apiInterfaceClassName))
			.append(" api = new ")
			.append(JavaCodeGenerationUtil.getClassNameWithoutPackage(exchangeImplClassName))
			.append("(TestJXApiProperties.filterProperties(\"")
			.append(exchangeName)
			.append("\", true)).get")
			.append(simpleApiClassName)
			.append("();\n")
			.append(requestSimpleClassName)
			.append(" request = new ")
			.append(requestSimpleClassName)
			.append("();\n");
		List<String> allParametersNames = parameters.stream().map(p -> p.getName()).collect(Collectors.toList());
		for (EndpointParameter p : parameters) {
			body.append("request.")
				.append(JavaCodeGenerationUtil.getSetAccessorMethodName(p.getName(), allParametersNames))
				.append("(")
				.append(getParameterValueConstantName(p))
				.append(");\n");
		}
		body.append("log.info(\"Subscribing to stream ")
			.append(apiInterfaceClassName)
			.append(".")
			.append(apiMethodName)
			.append("() websocket stream with request:\" + request);\n");
		body.append("api.")
			.append(apiMethodName)
			.append("(request, m -> log.info(\"Received message:\" + m));");
		
		appendMethod("public static void main(String[] args)", 
					"try {\n" 
						+ JavaCodeGenerationUtil.indent(body.toString(), JavaCodeGenerationUtil.INDENTATION) 
						+ "\n} catch (Throwable t) {\n"
						+ JavaCodeGenerationUtil.indent("log.error(\"Exception raised from main()\", t);", JavaCodeGenerationUtil.INDENTATION)
						+ "\n}");
	}

	private void generateParameterValueConstantDeclaration(ExchangeDescriptor exchangeDescriptor, 
														   ExchangeApiDescriptor exchangeApiDescriptor, 
														   WebsocketEndpointDescriptor websocketApi, 
														   EndpointParameter parameter) {
			StringBuilder constantDeclaration = new StringBuilder();
			appendToBody(JavaCodeGenerationUtil.generateJavaDoc("Sample value for <i>" 
																+ parameter.getName() 
																+ "</i> parameter of <i>" 
																+ websocketApi.getName() 
																+ "</i> API") + "\n");
			constantDeclaration.append("public static final ")
								.append(getParameterTypeDeclaration(parameter.getType()))
								.append(" ")
								.append(getParameterValueConstantName(parameter))
								.append(" = ")
								.append(getParameterValueDeclaration(parameter))
								.append(";\n\n");
			appendToBody(constantDeclaration.toString());
	}
	
	private String getParameterValueDeclaration(EndpointParameter parameter) {
		Object v = parameter.getSampleValue();
		switch (parameter.getType()) {
		case BIGDECIMAL:
			addImport(BigDecimal.class);
			if (v == null)
				return "BigDecimal.ZERO";
			return "new BigDecimal(\"" + String.valueOf(v) + "\");";
		case BOOLEAN:
		case INT:
			return String.valueOf(v);
		case STRING_LIST:
			addImport(List.class);
			if (v == null) {
				return "List.of()";
			}
			String strList = v.toString().trim();
			if (!strList.startsWith("[") || !strList.endsWith("]")) {
				throw new IllegalArgumentException("Sample value for parameter:" + parameter + ":" + strList + " does must be surrounded with '[]'");
			}
			return "List.of(" + strList.substring(1, strList.length() - 1) + ")";
		case STRING:
			return "\"" + v + "\"";
		case TIMESTAMP:
		case LONG:
			if (v == null) {
				return "0L";
			}
			if ("now()".equalsIgnoreCase(v.toString())) {
				return "System.currentTimeMillis()";
			}
			return String.valueOf(v) + "L";
		case OBJECT:
		case OBJECT_LIST:
		default:
			throw new IllegalArgumentException("Unexpected parameter type for parameter:" + parameter);
		}
	}

	private String getParameterTypeDeclaration(EndpointParameterType endpointParameterType) {
		switch (endpointParameterType) {
		case BIGDECIMAL:
			addImport(BigDecimal.class);
			return BigDecimal.class.getSimpleName();
		case BOOLEAN:
			return "boolean";
		case INT:
			return "int";
		case TIMESTAMP:
		case LONG:
			return "long";
		case STRING:
			return "String";
		case STRING_LIST:
			addImport(List.class);
			return "List<String>";
		case OBJECT:
		case OBJECT_LIST:
			// FIXME not managed yet: Construction of demo values for parameters that are structured object types.
			// Usually, requests do not use struct parameters
		default:
			throw new IllegalArgumentException("Unexpected parameter type:" + endpointParameterType);
		}
	}
	
	private static String getParameterValueConstantName(EndpointParameter parameter) {
		return parameter.getName().toUpperCase();
	}

}
