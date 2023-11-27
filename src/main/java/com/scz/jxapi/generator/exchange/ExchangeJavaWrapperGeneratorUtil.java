package com.scz.jxapi.generator.exchange;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.JsonMessageDeserializerGenerator;
import com.scz.jxapi.generator.JsonPojoSerializerGenerator;
import com.scz.jxapi.generator.PojoField;
import com.scz.jxapi.generator.PojoGenerator;
import com.scz.jxapi.netutils.rest.RestEndpointUrlParameters;
import com.scz.jxapi.netutils.websocket.WebsocketSubscribeParameters;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Helper methods for generation of Java classes of a given exchange wrapper
 */
public class ExchangeJavaWrapperGeneratorUtil {
	
	public static final EnumMap<EndpointParameterType, String> PARAMETER_TYPE_CLASSES = new EnumMap<>(EndpointParameterType.class);
	private static final String DEFAULT_STRING_LIST_SEPARATOR = ",";
	static {
		PARAMETER_TYPE_CLASSES.put(EndpointParameterType.BIGDECIMAL, BigDecimal.class.getName());
		PARAMETER_TYPE_CLASSES.put(EndpointParameterType.BOOLEAN, "java.lang.Boolean");
		PARAMETER_TYPE_CLASSES.put(EndpointParameterType.INT, "java.lang.Integer");
		PARAMETER_TYPE_CLASSES.put(EndpointParameterType.LONG, "java.lang.Long");
		PARAMETER_TYPE_CLASSES.put(EndpointParameterType.TIMESTAMP, "java.lang.Long");
		PARAMETER_TYPE_CLASSES.put(EndpointParameterType.STRING, "String");
		PARAMETER_TYPE_CLASSES.put(EndpointParameterType.STRING_LIST, "java.util.List<String>");
		PARAMETER_TYPE_CLASSES.put(EndpointParameterType.INT_LIST, "java.util.List<Integer>");
	}
	
//	public static void generatePojo(Path src, String className, String description, List<EndpointParameter> fields) throws IOException {
//		generatePojo(src, className, description, fields, null, null);
//	}
	
	public static void generatePojo(Path src, String className, String description, List<EndpointParameter> fields, List<String> implementedInterfaces, String additionnalClassBody) throws IOException {
		PojoGenerator generator = new PojoGenerator(className);
		String deserializerClassName = getSerializerClassName(className);
		generator.addImport(deserializerClassName);
		generator.addImport(com.fasterxml.jackson.databind.annotation.JsonSerialize.class.getName());
		generator.setTypeDeclaration("@JsonSerialize(using = " 
										+ JavaCodeGenerationUtil.getClassNameWithoutPackage(deserializerClassName) 
										+ ".class)\n" 
										+ generator.getTypeDeclaration());
		generator.setDescription(description);
		generator.setImplementedInterfaces(implementedInterfaces);
		for (EndpointParameter field: fields) {
			switch (field.getType()) {
			case BIGDECIMAL:
			case INT:
			case LONG:
			case BOOLEAN:
			case STRING:
			case TIMESTAMP:
			case STRING_LIST:
			case INT_LIST:
				generateSimpleParameterTypePojoField(generator, field);
				break;
			case OBJECT:
			case OBJECT_LIST:
			case OBJECT_MAP:
				generateObjectParameterTypePojoField(src, className, generator, field);
				break;
			default:
				break;	
			}
		}
		if (additionnalClassBody != null) {
			generator.appendToBody(additionnalClassBody);
		}
		generator.writeJavaFile(src);
	}
	
	private static String getSerializerClassName(String pojoClassName) {
		String pkg = StringUtils.substringBefore(JavaCodeGenerationUtil.getClassPackage(pojoClassName), ".pojo");
		return pkg + ".serializers." + JavaCodeGenerationUtil.getClassNameWithoutPackage(pojoClassName) + "Serializer";
	}

	public static void generateDeserializer(Path src, String deserializedClassName, List<EndpointParameter> fields) throws IOException {
		for (EndpointParameter field: fields) {
			if ((field.getType() == EndpointParameterType.OBJECT 
					|| field.getType() == EndpointParameterType.OBJECT_LIST
					|| field.getType() == EndpointParameterType.OBJECT_MAP)
				&& field.getParameters() != null) {
				generateDeserializer(src, getObjectParameterClassName(deserializedClassName, field), field.getParameters());
			}
		}
		JsonMessageDeserializerGenerator deserializerGenerator = new JsonMessageDeserializerGenerator(deserializedClassName, fields);
		deserializerGenerator.writeJavaFile(src);
	}
	
	public static void generateSimpleParameterTypePojoField(PojoGenerator generator, EndpointParameter field) {
		String parameterClass = PARAMETER_TYPE_CLASSES.get(field.getType());
		if (!parameterClass.startsWith("java.lang") && parameterClass.contains(".")) {
			generator.addImport(parameterClass);
			parameterClass = JavaCodeGenerationUtil.getClassNameWithoutPackage(parameterClass);
		}
		generator.addField(PojoField.create(parameterClass, field.getName(), field.getMsgField(), field.getDescription()));
	}
	
	public static String getObjectParameterClassName(String className, EndpointParameter field) {
		String objectName = field.getObjectName();
		if (objectName != null) {
			return JavaCodeGenerationUtil.getClassPackage(className) + "." + objectName; 
		}
		return className + JavaCodeGenerationUtil.firstLetterToUpperCase(field.getName());
	}
	
	private static void generateObjectParameterTypePojoField(Path src, String className, PojoGenerator generator, EndpointParameter field) throws IOException {
		String objectName = getObjectParameterClassName(className, field);
		if (field.getParameters() != null) {
			generatePojo(src, objectName, field.getDescription(), field.getParameters(), field.getImplementedInterfaces(), null);
		}
		String parameterTypeName = objectName;
		generator.addImport(objectName);
		if (field.getType() == EndpointParameterType.OBJECT_LIST) {
			parameterTypeName = "java.util.List<" + JavaCodeGenerationUtil.getClassNameWithoutPackage(objectName) + ">";
		}
		if (field.getType() == EndpointParameterType.OBJECT_MAP) {
			parameterTypeName = "java.util.Map<String, " + JavaCodeGenerationUtil.getClassNameWithoutPackage(objectName) + ">";
		}
		generator.addField(PojoField.create(parameterTypeName, field.getName(), field.getMsgField(), field.getDescription()));
	}
	
	public static void generateCEX(ExchangeDescriptor exchangeDescriptor, Path ouputFolder) throws IOException {
		generateCEXPojos(exchangeDescriptor, ouputFolder);
		generateCEXPojoDeserializers(exchangeDescriptor, ouputFolder);
		generateCEXPojoSerializers(exchangeDescriptor, ouputFolder);
	}
	
	public static void generateCEXDemos(ExchangeDescriptor exchangeDescriptor, Path ouputFolder) throws IOException {
		for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {
			
			if (api.getRestEndpoints() != null) {
				for (RestEndpointDescriptor restApi: api.getRestEndpoints()) {
					RestEndpointDemoGenerator restEndpointDemoGenerator = new RestEndpointDemoGenerator(exchangeDescriptor, api, restApi);
					restEndpointDemoGenerator.writeJavaFile(ouputFolder);
				}
			}
			
			if (api.getWebsocketEndpoints() != null) {
				for (WebsocketEndpointDescriptor websocketApi: api.getWebsocketEndpoints()) {
					WebsocketEndpointDemoGenerator websocketEndpointDemoGenerator = new WebsocketEndpointDemoGenerator(exchangeDescriptor, api, websocketApi);
					websocketEndpointDemoGenerator.writeJavaFile(ouputFolder);
				}
			}
		}
	}
	
	public static void generateCEXPojoSerializers(ExchangeDescriptor exchangeDescriptor, Path ouputFolder) throws IOException {
		for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {
			if (api.getRestEndpoints() != null) {
				for (RestEndpointDescriptor restEndpointDescriptor: api.getRestEndpoints()) {
					ExchangeJavaWrapperGeneratorUtil.generateSerializer(ouputFolder, 
							generateRestEnpointRequestClassName(exchangeDescriptor, api, restEndpointDescriptor),
							restEndpointDescriptor.getParameters());
					if (restEndpointDescriptor.getResponse() != null) {
						ExchangeJavaWrapperGeneratorUtil.generateSerializer(ouputFolder, 
								generateRestEnpointResponseClassName(exchangeDescriptor, api, restEndpointDescriptor),
								restEndpointDescriptor.getResponse());
					}
				}
			}
			
			if (api.getWebsocketEndpoints() != null) {
				for (WebsocketEndpointDescriptor wsEndpointDescriptor: api.getWebsocketEndpoints()) {
					ExchangeJavaWrapperGeneratorUtil.generateSerializer(ouputFolder, 
								generateWebsocketEndpointRequestClassName(exchangeDescriptor, api, wsEndpointDescriptor),
								wsEndpointDescriptor.getParameters());
					
					ExchangeJavaWrapperGeneratorUtil.generateSerializer(ouputFolder, 
							generateWebsocketEndpointMessageClassName(exchangeDescriptor, api, wsEndpointDescriptor),
							wsEndpointDescriptor.getResponse());
				}
			}
		}
	}

	private static void generateSerializer(Path ouputFolder, String pojoClassName, List<EndpointParameter> fields) throws IOException {
		String serializerPkg = JavaCodeGenerationUtil.getClassPackage(getSerializerClassName(pojoClassName));
		JsonPojoSerializerGenerator generator = new JsonPojoSerializerGenerator(serializerPkg, pojoClassName, fields);
		
		for (EndpointParameter field: fields) {
			if ((field.getType() == EndpointParameterType.OBJECT 
					|| field.getType() == EndpointParameterType.OBJECT_LIST
					|| field.getType() == EndpointParameterType.OBJECT_MAP)
				&& field.getParameters() != null) {
				generateSerializer(ouputFolder, getObjectParameterClassName(pojoClassName, field), field.getParameters());
			}
		}
		
		generator.writeJavaFile(ouputFolder);
	}

	public static void generateCEXPojoDeserializers(ExchangeDescriptor exchangeDescriptor, Path ouputFolder) throws IOException {
		for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {
			if (api.getRestEndpoints() != null) {
				for (RestEndpointDescriptor restEndpointDescriptor: api.getRestEndpoints()) {
					if (restEndpointDescriptor.getResponse() != null) {
						ExchangeJavaWrapperGeneratorUtil.generateDeserializer(ouputFolder, 
								generateRestEnpointResponseClassName(exchangeDescriptor, api, restEndpointDescriptor),
								restEndpointDescriptor.getResponse());
					}
				}
			}
			
			if (api.getWebsocketEndpoints() != null) {
				for (WebsocketEndpointDescriptor wsEndpointDescriptor: api.getWebsocketEndpoints()) {
					ExchangeJavaWrapperGeneratorUtil.generateDeserializer(ouputFolder, 
							generateWebsocketEndpointMessageClassName(exchangeDescriptor, api, wsEndpointDescriptor),
							wsEndpointDescriptor.getResponse());
				}
			}
		}
	}

	public static void generateCEXPojos(ExchangeDescriptor exchangeDescriptor, Path ouputFolder) throws IOException {
		generateExchangeInterface(exchangeDescriptor, ouputFolder);
		for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {
			generateExchangeApiInterface(exchangeDescriptor, api, ouputFolder);
			if (api.getRestEndpoints() != null) {
				for (RestEndpointDescriptor restEndpointDescriptor: api.getRestEndpoints()) {
					List<String> requestInterfaces = new ArrayList<>();
					requestInterfaces.add(RestEndpointUrlParameters.class.getName());
					if (restEndpointDescriptor.getRequestInterfaces() != null) {
						requestInterfaces.addAll(restEndpointDescriptor.getRequestInterfaces());
					}
					String additionalBody = null;
					additionalBody = generateRestEndpointGetUrlParametersMethod(restEndpointDescriptor);
					ExchangeJavaWrapperGeneratorUtil.generatePojo(ouputFolder, 
											generateRestEnpointRequestClassName(exchangeDescriptor, api, restEndpointDescriptor), 
											"Request for " + exchangeDescriptor.getName() + " " + api.getName() + " API " 
												+ restEndpointDescriptor.getName() + " REST endpoint"
												+ restEndpointDescriptor.getDescription()
												+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING,
											restEndpointDescriptor.getParameters(),
											requestInterfaces,
											additionalBody);
					if (restEndpointDescriptor.getResponse() != null) {
						ExchangeJavaWrapperGeneratorUtil.generatePojo(ouputFolder, 
								generateRestEnpointResponseClassName(exchangeDescriptor, api, restEndpointDescriptor), 
								"Response to " + exchangeDescriptor.getName() 
									+ " " + api.getName() + " API " 
									+ restEndpointDescriptor.getName() 
									+ " REST endpoint request<br/>"
									+ restEndpointDescriptor.getDescription()
									+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING,
								restEndpointDescriptor.getResponse(),
								restEndpointDescriptor.getResponseInterfaces(),
								null);
					}
				}
			}

			if (api.getWebsocketEndpoints() != null) {
				for (WebsocketEndpointDescriptor wsEndpointDescriptor: api.getWebsocketEndpoints()) {
					List<String> requestInterfaces = new ArrayList<>();
					requestInterfaces.add(WebsocketSubscribeParameters.class.getName());
					if (wsEndpointDescriptor.getRequestInterfaces() != null) {
						requestInterfaces.addAll(wsEndpointDescriptor.getRequestInterfaces());
					}
					ExchangeJavaWrapperGeneratorUtil.generatePojo(ouputFolder, 
											generateWebsocketEndpointRequestClassName(exchangeDescriptor, api, wsEndpointDescriptor), 
											"Subscription request to" + exchangeDescriptor.getName() 
												+ " " + api.getName() + " API " 
												+ wsEndpointDescriptor.getName() 
												+ " websocket endpoint<br/>" 
												+ wsEndpointDescriptor.getDescription()
												+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING,
											wsEndpointDescriptor.getParameters(), 
											Arrays.asList(WebsocketSubscribeParameters.class.getName()), 
											generateWebsocketSubscribeParametersGetTopicMethod(wsEndpointDescriptor));
					ExchangeJavaWrapperGeneratorUtil.generatePojo(ouputFolder, 
							generateWebsocketEndpointMessageClassName(exchangeDescriptor, api, wsEndpointDescriptor), 
							"Message disseminated upon subscription to " 
								+ exchangeDescriptor.getName() + " " 
								+ api.getName() + " API " 
								+ wsEndpointDescriptor.getName() + " websocket endpoint request<br/>"
								+ wsEndpointDescriptor.getDescription()
								+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING,
							wsEndpointDescriptor.getResponse(), 
							wsEndpointDescriptor.getResponseInterfaces(), 
							null);
				}
			}
		}
	}
	
	public static String generateRestEnpointRequestClassName(ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor exchangeApiDescriptor, RestEndpointDescriptor restEndpointDescriptor) {
		return generateRestEnpointPojoClassName(exchangeDescriptor, exchangeApiDescriptor, restEndpointDescriptor, "Request");
	}
	
	public static String generateRestEnpointResponseClassName(ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor exchangeApiDescriptor, RestEndpointDescriptor restEndpointDescriptor) {
		if (ResponseDataType.STRING.equals(restEndpointDescriptor.getResponseDataType())) {
			return String.class.getName();
		}
		String responseObjectName = restEndpointDescriptor.getResponseObjectName();
		if (responseObjectName != null) {
			return exchangeDescriptor.getBasePackage() + "." 
					+ exchangeApiDescriptor.getName().toLowerCase() + ".pojo." 
					+ JavaCodeGenerationUtil.firstLetterToUpperCase(responseObjectName);
		}
		return generateRestEnpointPojoClassName(exchangeDescriptor, exchangeApiDescriptor, restEndpointDescriptor, "Response");
	}
	
	private static String generateRestEnpointPojoClassName(ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor exchangeApiDescriptor, RestEndpointDescriptor restEndpointDescriptor, String suffix) {
		return exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".pojo."
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName())
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(restEndpointDescriptor.getName())
				+ suffix;
	}
	
	public static void generateExchangeApiInterface(ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor exchangeApiDescriptor, Path outputFolder) throws IOException {
		new ExchangeApiInterfaceGenerator(exchangeDescriptor, exchangeApiDescriptor).writeJavaFile(outputFolder);
		new ExchangeApiInterfaceImplementationGenerator(exchangeDescriptor, exchangeApiDescriptor).writeJavaFile(outputFolder);
	}
	
	public static String getRestApiDemoClassName(ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor exchangeApiDescriptor, RestEndpointDescriptor restApi) {
		String pkgPrefix =  exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".demo.";
		return pkgPrefix + JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
									 + JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName())
									 + JavaCodeGenerationUtil.firstLetterToUpperCase(restApi.getName())
									 + "Demo";
	}
	
	public static String getWebsocketApiDemoClassName(ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor exchangeApiDescriptor, WebsocketEndpointDescriptor websocketApi) {
		String pkgPrefix =  exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".demo.";
		return pkgPrefix + JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
									 + JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName())
									 + JavaCodeGenerationUtil.firstLetterToUpperCase(websocketApi.getName())
									 + "Demo";
	}
	
	public static String generateWebsocketEndpointMessageClassName(ExchangeDescriptor exchangeDescriptor,
			ExchangeApiDescriptor exchangeApiDescriptor, WebsocketEndpointDescriptor websocketApi) {
		if (ResponseDataType.STRING.equals(websocketApi.getResponseDataType())) {
			return String.class.getName();
		}
		return exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".pojo."
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName())
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName())
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(websocketApi.getName())
				+ "Message";
	}

	public static String generateWebsocketEndpointRequestClassName(ExchangeDescriptor exchangeDescriptor,
			ExchangeApiDescriptor exchangeApiDescriptor, WebsocketEndpointDescriptor websocketApi) {
		return exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".pojo."
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName())
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(websocketApi.getName())
				+ "Request";
	}

	public static void generateExchangeInterface(ExchangeDescriptor exchangeDescriptor, Path outputFolder) throws IOException {
		ExchangeInterfaceGenerator interfaceGenerator = new ExchangeInterfaceGenerator(exchangeDescriptor);
		ExchangeInterfaceImplementationGenerator implementationGenerator = new ExchangeInterfaceImplementationGenerator(exchangeDescriptor);
		implementationGenerator.addImport(Properties.class);
		interfaceGenerator.writeJavaFile(outputFolder);
		implementationGenerator.writeJavaFile(outputFolder);
	}
	
	public static String getApiInterfaceClassName(ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor exchangeApiDescriptor) {
		String pkgPrefix =  exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".";
		String simpleInterfaceName = JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) + JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName()) + "Api";
		return pkgPrefix + simpleInterfaceName;
	}
	
	private static String generateRestEndpointGetUrlParametersMethod(RestEndpointDescriptor restEndpointDescriptor) {
		String getUrlParametersBody = "return \"\";\n";
		if (restEndpointDescriptor.getUrlParameters() != null) {
			getUrlParametersBody =	generateGetUrlParametersBodyFromTemplate(restEndpointDescriptor.getUrlParameters(), 
																			 restEndpointDescriptor.getParameters(), 
																			 restEndpointDescriptor.getUrlParametersListSeparator());
		} else if (restEndpointDescriptor.getParameters().size() > 0
					&& (restEndpointDescriptor.isQueryParams()	|| "GET".equalsIgnoreCase(restEndpointDescriptor.getHttpMethod()))) {
			getUrlParametersBody = generateGetUrlParametersBodyUsingQueryParams(restEndpointDescriptor.getParameters());
		}
		StringBuilder sb = new StringBuilder();
		sb.append("@Override\npublic String getUrlParameters() {\n")
		  .append(JavaCodeGenerationUtil.INDENTATION)
		  .append(getUrlParametersBody)
		  .append("}\n\n");
		return sb.toString();
	}

	private static String generateWebsocketSubscribeParametersGetTopicMethod(WebsocketEndpointDescriptor wsEndpointDescriptor) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n@Override\npublic String getTopic() {\n")
		  .append(JavaCodeGenerationUtil.INDENTATION)
		  .append(generateGetUrlParametersBodyFromTemplate(wsEndpointDescriptor.getTopic(), wsEndpointDescriptor.getParameters(), wsEndpointDescriptor.getTopicParametersListSeparator()))
		  .append("}\n\n");
		return sb.toString(); 
	}
	
	private static String generateGetUrlParametersBodyFromTemplate(String urlParametersTemplate, List<EndpointParameter> endpointParameters, String stringListSeparator) {
		if (endpointParameters.isEmpty()) {
			return "return \"" + urlParametersTemplate + "\";\n";
		}
		if (stringListSeparator == null) {
			stringListSeparator = DEFAULT_STRING_LIST_SEPARATOR;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("return ")
		  .append(EncodingUtil.class.getSimpleName())
		  .append(".substituteArguments(\"")
		  .append(urlParametersTemplate)
		  .append("\", ");
		int n = endpointParameters.size();
		for (int i = 0; i < n; i++) {
			String name = endpointParameters.get(i).getName();
			String value = name;
			if (endpointParameters.get(i).getType() == EndpointParameterType.STRING_LIST) {
				value = EncodingUtil.class.getSimpleName() + ".listToString(" + name + ", \"" + stringListSeparator + "\")"; 
			}
			sb.append("\"").append(name).append("\", ").append(value);
			if (i < n - 1) {
				sb.append(", ");
			}
		}
		sb.append(");\n");
		return sb.toString();
	}
	
	private static String generateGetUrlParametersBodyUsingQueryParams(List<EndpointParameter> endpointParameters) {
		StringBuilder s = new StringBuilder().append("return " + EncodingUtil.class.getSimpleName() + ".createUrlQueryParameters(");
		for (int i = 0; i < endpointParameters.size(); i++) {
			if (i > 0) {
				s.append(",");
			}
			EndpointParameter param = endpointParameters.get(i);
			String name = param.getName();
			s.append("\"")
			 .append(name)
			 .append("\", ");
			if (param.getType() == EndpointParameterType.STRING_LIST) {
				s.append(EncodingUtil.class.getSimpleName())
				 .append(".listToUrlParamString(")
				 .append(name)
				 .append(")");
			} else {
				s.append(name);
			}
			
		}
		return s.append(");\n").toString();
	}
	
	public static boolean exchangeApiHasRateLimits(ExchangeApiDescriptor exchangeApiDescriptor, ExchangeDescriptor exchangeDescriptor) {
		if (exchangeDescriptor.getRateLimits() != null && !exchangeDescriptor.getRateLimits().isEmpty()) {
			return true;
		}
		if (exchangeApiDescriptor.getRateLimits() != null && !exchangeDescriptor.getRateLimits().isEmpty()) {
			return true;
		}
		for (RestEndpointDescriptor api : exchangeApiDescriptor.getRestEndpoints()) {
			if (api.getRateLimits() != null && !api.getRateLimits().isEmpty()) {
				return true;
			}
		}
		return false;
	}
	
	public static String generateRateLimitVariableName(String rateLimitName) {
		return "RATE_LIMIT_" + JavaCodeGenerationUtil.getStaticVariableName(rateLimitName);
	}

	public static String getExchangeInterfaceName(ExchangeDescriptor exchangeDescriptor) {
		return exchangeDescriptor.getBasePackage() + "." + JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) + "Exchange";
	}

}
