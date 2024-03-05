package com.scz.jxapi.generator.exchange;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.JavaTypeGenerator;
import com.scz.jxapi.util.DemoUtil;
import com.scz.jxapi.util.TestJXApiProperties;

public class RestEndpointDemoGenerator extends JavaTypeGenerator {
	
	private final ExchangeDescriptor exchangeDescriptor;
	private final ExchangeApiDescriptor exchangeApiDescriptor;
	private final RestEndpointDescriptor restApi;
	private StringBuilder body;
	private final boolean hasArguments;
	private String requestSimpleClassName;
	private String apiInterfaceClassName;
	
	public RestEndpointDemoGenerator(ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor exchangeApiDescriptor, RestEndpointDescriptor restApi) {
		super(ExchangeJavaWrapperGeneratorUtil.getRestApiDemoClassName(exchangeDescriptor, exchangeApiDescriptor, restApi));
		this.exchangeDescriptor = exchangeDescriptor;
		this.exchangeApiDescriptor = exchangeApiDescriptor;
		this.restApi = restApi;
		setTypeDeclaration("public class");
		this.hasArguments = ExchangeJavaWrapperGeneratorUtil.restEndpointHasArguments(restApi, exchangeApiDescriptor);
		EndpointParameterType requestDataType = restApi.getRequest() == null? null: restApi.getRequest().getEndpointParameterType();
		this.requestSimpleClassName = "Object";
		if (hasArguments) {
			String requestClassName = ExchangeJavaWrapperGeneratorUtil.generateRestEnpointRequestClassName(exchangeDescriptor, exchangeApiDescriptor, restApi);
			requestSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
					requestDataType, 
					getImports(), 
					requestClassName);
		}
		this.apiInterfaceClassName = ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor);
		addImport(apiInterfaceClassName);
		String apiMethodName = JavaCodeGenerationUtil.firstLetterToLowerCase(restApi.getName());
		setDescription("Snippet to test call to {@link " 
						+ ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor) 
						+ "#" 
						+ apiMethodName 
						+ "(" + requestSimpleClassName + ")}\n"
						+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING);
		
	}
	
	public String generate() {
		String requestClassName = ExchangeJavaWrapperGeneratorUtil.generateRestEnpointRequestClassName(exchangeDescriptor, exchangeApiDescriptor, restApi);
		addImport(requestClassName);
		String requestSimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(requestClassName);
		String apiInterfaceClassName = ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor);
		addImport(apiInterfaceClassName);
		String apiMethodName = JavaCodeGenerationUtil.firstLetterToLowerCase(restApi.getName());
		setDescription("Snippet to test call to {@link " 
						+ ExchangeJavaWrapperGeneratorUtil.getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor) 
						+ "#" 
						+ apiMethodName 
						+ "(" + requestClassName + ")}\n"
						+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING);
		JavaCodeGenerationUtil.generateSlf4jLoggerDeclaration(this);
		List<EndpointParameter> requestParams = restApi.getRequest() == null? null: restApi.getRequest().getParameters();
		requestParams.forEach(p -> generateParameterValueConstantDeclaration(exchangeDescriptor, exchangeApiDescriptor, restApi, p));
		String exchangeInterfaceClassName = ExchangeJavaWrapperGeneratorUtil.getExchangeInterfaceName(exchangeDescriptor);
		generateMainMethodBody();
		return super.generate();
	}
	
	private void generateMainMethodBody() {
		body = new StringBuilder();
		String exchangeInterfaceClassName = ExchangeJavaWrapperGeneratorUtil.getExchangeInterfaceName(exchangeDescriptor);
		String exchangeName = JavaCodeGenerationUtil.firstLetterToLowerCase(exchangeDescriptor.getName());
		boolean hasArguments = ExchangeJavaWrapperGeneratorUtil.restEndpointHasArguments(restApi, exchangeApiDescriptor);
		EndpointParameter request = restApi.getRequest();
		EndpointParameterType requestDataType = request == null? null: request.getEndpointParameterType();
		String requestSimpleClassName = "Object";
		if (hasArguments) {
			String requestClassName = ExchangeJavaWrapperGeneratorUtil.generateRestEnpointRequestClassName(exchangeDescriptor, exchangeApiDescriptor, restApi);
			requestSimpleClassName = ExchangeJavaWrapperGeneratorUtil.getClassNameForParameterType(
					requestDataType, 
					getImports(), 
					requestClassName);
		}
		
		String exchangeImplClassName = exchangeInterfaceClassName + "Impl";
		String simpleApiClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(apiInterfaceClassName);
		addImport(exchangeImplClassName);
		addImport(TestJXApiProperties.class);
		addImport(DemoUtil.class);
		body.append(simpleApiClassName)
			.append(" api = new ")
			.append(JavaCodeGenerationUtil.getClassNameWithoutPackage(exchangeImplClassName))
			.append("(TestJXApiProperties.filterProperties(\"")
			.append(exchangeName)
			.append("\", true)).get")
			.append(simpleApiClassName)
			.append("();\n");
			
		String requestVar = "";
		if (hasArguments) {
			requestVar = "request";
			if (requestDataType.isObject()) {
				createSampleValueForObject(requestVar, 
						requestSimpleClassName, 
						ExchangeJavaWrapperGeneratorUtil.getEndpointParameters(request.getParameters(), 
																			   request.getObjectName(), 
																			   exchangeApiDescriptor));
			} else {
//				requestVar = cr
			}
		}
		
		// FIXME
		if (requestDataType.isObject()) {
			
//			createSampleValueForObject("request", 
//					ExchangeJavaWrapperGeneratorUtil.getClassNameForEndpointParameter(parameter, getImports(), requestSimpleClassName), 
//					ExchangeJavaWrapperGeneratorUtil.findParametersForObjectName(fieldName, null));
		}
			
//		List<String> allParametersNames = parameters.stream().map(p -> p.getName()).collect(Collectors.toList());
//		for (EndpointParameter p : parameters) {
//			body.append("request.")
//				.append(JavaCodeGenerationUtil.getSetAccessorMethodName(p.getName(), allParametersNames))
//				.append("(")
//				.append(getParameterValueConstantName(p))
//				.append(");\n");
//		}
		String apiMethodName = JavaCodeGenerationUtil.firstLetterToLowerCase(restApi.getName());
		body.append("log.info(\"Calling '")
			.append(apiInterfaceClassName)
			.append(".")
			.append(apiMethodName)
			.append("() API with request:\" + request);\n")
			
//			.append(JavaCodeGenerationUtil.INDENTATION)
			.append("DemoUtil.checkResponse(api.")
			.append(apiMethodName)
			.append("(request));\n")
//			.append(JavaCodeGenerationUtil.INDENTATION)
			.append("System.exit(0);");
//			.append("});");
		
		appendMethod("public static void main(String[] args)", 
					"try {\n" 
						+ JavaCodeGenerationUtil.indent(body.toString(), JavaCodeGenerationUtil.INDENTATION) 
						+ "\n} catch (Throwable t) {\n"
						+ JavaCodeGenerationUtil.indent("log.error(\"Exception raised from main()\", t);\nSystem.exit(-1);", JavaCodeGenerationUtil.INDENTATION)
						+ "\n}");
	}
	
	private String generateSampleValueForParameter(String fieldName, EndpointParameter parameter) {
		
		Object v = parameter.getSampleValue();
		if (v == null)
			return JavaCodeGenerationUtil.NULL;
		switch (parameter.getEndpointParameterType().getCanonicalType()) {
		case BIGDECIMAL:
			addImport(BigDecimal.class);
			return "new BigDecimal(\"" + String.valueOf(v) + "\");";
		case BOOLEAN:
		case INT:
			return String.valueOf(v);
		case LIST:
			addImport(List.class);
			String strList = v.toString().trim();
			if (!strList.startsWith("[") || !strList.endsWith("]")) {
				throw new IllegalArgumentException("Sample value for parameter:" + parameter + ":" + strList + " does must be surrounded with '[]'");
			}
			if (parameter.getEndpointParameterType().isObject()) {
				return "List.of()";
			}
			return "List.of(" + strList.substring(1, strList.length() - 1) + ")";
		case STRING:
			return "\"" + StringUtils.replace(v.toString(), "\"", "\\\"")  + "\"";
		case TIMESTAMP:
		case LONG:
			if ("now()".equalsIgnoreCase(v.toString())) {
				return "System.currentTimeMillis()";
			}
			return String.valueOf(v) + "L";
		case OBJECT:
			return getObjectFieldVarName(fieldName, parameter.getName());
		case MAP:
			addImport(Map.class);
			// FIXME
			/// return createMapParameterSampleValue(parameter.getSampleValue(), null, null);
			return null;
		default:
			throw new IllegalArgumentException("Unexpected parameter type for parameter:" + parameter);
		}
	}
	
	private String createMapParameterSampleValue(String parameterType, String objectClassName, String mapSampleValue, String mapSampleKey, String mapSampleValueVar) {
		String res = JavaCodeGenerationUtil.NULL;
		if (mapSampleValue != null) {
			// FIXME
			// return ExchangeJavaWrapperGeneratorUtil.getNewJsonParameterDeserializerInstruction(messageType, messageFullClassName, getImports()) + ".deserialize(\"" + mapSampleValue  + "\")";
			return JavaCodeGenerationUtil.NULL;
		} else if (mapSampleKey != null && mapSampleValueVar != null){
			
		}

		return res;
	}

	private String getObjectFieldVarName(String enclosingObjectVarName, String fieldName) {
		return enclosingObjectVarName + JavaCodeGenerationUtil.firstLetterToUpperCase(fieldName);
	}
	
	private String createSampleValueForObject(String fieldName, 
											  String simpleObjectClassName, 
											  List<EndpointParameter> parameters) {
		StringBuilder s = new StringBuilder()
		 .append("\n")
		 .append(simpleObjectClassName)
		 .append(" ")
		 .append(fieldName)
		 .append(" = new ")
		 .append(simpleObjectClassName)
		 .append("();\n");
		parameters.forEach(param -> {
			String subFieldName = null;
			// FIXME
//			if (param.getEndpointParameterType().isObject()) {
//				 subFieldName = fieldName + JavaCodeGenerationUtil.firstLetterToUpperCase(param.getName());
//				 createSampleValueForObject(subFieldName, simpleObjectClassName, parameters);
//			}
			List<String> allParametersNames = parameters.stream().map(p -> p.getName()).collect(Collectors.toList());
			s.append(fieldName)
			 .append(".")
			 .append(JavaCodeGenerationUtil.getSetAccessorMethodName(param.getName(), allParametersNames))
			 .append("(");
			if (subFieldName != null) {
				s.append(subFieldName);
			} else {
				// FIXME
				s.append(false);
			}
			
			s.append(");\n");
		});
		 
		return s.toString();
	}

	private void generateParameterValueConstantDeclaration(ExchangeDescriptor exchangeDescriptor, 
														   ExchangeApiDescriptor exchangeApiDescriptor, 
														   RestEndpointDescriptor restApi, 
														   EndpointParameter parameter) {
			StringBuilder constantDeclaration = new StringBuilder();
			appendToBody(JavaCodeGenerationUtil.generateJavaDoc("Sample value for <i>" 
																+ parameter.getName() 
																+ "</i> parameter of <i>" 
																+ restApi.getName() 
																+ "</i> API") + "\n");
			constantDeclaration.append("public static final ")
								.append(ExchangeJavaWrapperGeneratorUtil
										.getClassNameForEndpointParameter(
												parameter, 
												getImports(),
												ExchangeJavaWrapperGeneratorUtil
												.generateRestEnpointRequestClassName(
														exchangeDescriptor, 
														exchangeApiDescriptor, 
														restApi)))
								.append(" ")
								.append(getParameterValueConstantName(parameter))
								.append(" = ")
								.append(getParameterValueDeclaration(parameter))
								.append(";\n\n");
			appendToBody(constantDeclaration.toString());
	}
	
	private String getParameterValueDeclaration(EndpointParameter parameter) {
		Object v = parameter.getSampleValue();
		if (v == null)
			return JavaCodeGenerationUtil.NULL;
		switch (parameter.getEndpointParameterType().getCanonicalType()) {
		case BIGDECIMAL:
			addImport(BigDecimal.class);
			return "new BigDecimal(\"" + String.valueOf(v) + "\");";
		case BOOLEAN:
		case INT:
			return String.valueOf(v);
		case LIST:
			addImport(List.class);
			String strList = v.toString().trim();
			if (!strList.startsWith("[") || !strList.endsWith("]")) {
				throw new IllegalArgumentException("Sample value for parameter:" + parameter + ":" + strList + " does must be surrounded with '[]'");
			}
			if (parameter.getEndpointParameterType().isObject()) {
				return "List.of()";
			}
			return "List.of(" + strList.substring(1, strList.length() - 1) + ")";
		case STRING:
			return "\"" + StringUtils.replace(v.toString(), "\"", "\\\"")  + "\"";
		case TIMESTAMP:
		case LONG:
			if ("now()".equalsIgnoreCase(v.toString())) {
				return "System.currentTimeMillis()";
			}
			return String.valueOf(v) + "L";
		case OBJECT:
		case MAP:
			addImport(Map.class);
			return "Map.of()";
		default:
			throw new IllegalArgumentException("Unexpected parameter type for parameter:" + parameter);
		}
	}
	
	private static String getParameterValueConstantName(EndpointParameter parameter) {
		return parameter.getName().toUpperCase();
	}

}
