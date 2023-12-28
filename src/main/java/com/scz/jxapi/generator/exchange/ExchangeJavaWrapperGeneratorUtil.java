package com.scz.jxapi.generator.exchange;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	private static final Logger log = LoggerFactory.getLogger(ExchangeJavaWrapperGeneratorUtil.class);
	
	private static final String DEFAULT_STRING_LIST_SEPARATOR = ",";
	
//	public static void generatePojo(Path src, String className, String description, List<EndpointParameter> fields) throws IOException {
//		generatePojo(src, className, description, fields, null, null);
//	}
	
	public static void generatePojo(Path src, 
									String className, 
									String description, 
									List<EndpointParameter> fields, 
									List<String> implementedInterfaces, 
									String additionnalClassBody) throws IOException {
		if (log.isDebugEnabled())
			log.debug("Generating POJO:" + className + " with fields:" + StringUtils.truncate(String.valueOf(fields), 192) + ", implemented interfaces:" + implementedInterfaces  + " to:" + src);
		PojoGenerator generator = new PojoGenerator(className);
		String serializerClassName = getSerializerClassName(className);
		generator.addImport(serializerClassName);
		generator.addImport(com.fasterxml.jackson.databind.annotation.JsonSerialize.class.getName());
		generator.setTypeDeclaration("@JsonSerialize(using = " 
										+ JavaCodeGenerationUtil.getClassNameWithoutPackage(serializerClassName) 
										+ ".class)\n" 
										+ generator.getTypeDeclaration());
		generator.setDescription(description);
		generator.setImplementedInterfaces(implementedInterfaces);
		for (EndpointParameter field: fields) {
			if (field.getEndpointParameterType().isObject()) {
				generateObjectParameterTypePojoField(src, className, generator, field);
			} else {
				generateSimpleParameterTypePojoField(generator, field);
			}
		}
		if (additionnalClassBody != null) {
			generator.appendToBody(additionnalClassBody);
		}
		generator.writeJavaFile(src);
	}
	
	public static String getSerializerClassName(String pojoClassName) {
		String pkg = StringUtils.substringBefore(JavaCodeGenerationUtil.getClassPackage(pojoClassName), ".pojo");
		return pkg + ".serializers." + JavaCodeGenerationUtil.getClassNameWithoutPackage(pojoClassName) + "Serializer";
	}

	public static void generateDeserializer(Path src, String deserializedClassName, List<EndpointParameter> fields) throws IOException {
		if (log.isDebugEnabled())
			log.debug("Generating Deserializer for :" + deserializedClassName + " with fields:" + StringUtils.truncate(String.valueOf(fields), 192) + " to:" + src);
		Set<String> imports = new HashSet<>();
		for (EndpointParameter field: fields) {
			if ((field.getEndpointParameterType().isObject())
				&& field.getParameters() != null) {
				EndpointParameter objectParam = EndpointParameter.create(EndpointParameterTypes.OBJECT.name(), field.getName(), field.getMsgField(), field.getDescription(), field.getParameters());
				objectParam.setObjectName(field.getObjectName());
				generateDeserializer(src, EndpointParameterTypeGenerationUtil.getClassNameForEndpointParameter(objectParam, imports, deserializedClassName), field.getParameters());
			}
		}
		JsonMessageDeserializerGenerator deserializerGenerator = new JsonMessageDeserializerGenerator(deserializedClassName, fields);
		deserializerGenerator.getImports().addAll(imports);
		deserializerGenerator.writeJavaFile(src);
	}
	
	public static void generateSimpleParameterTypePojoField(PojoGenerator generator, EndpointParameter field) {
		String parameterClass = EndpointParameterTypeGenerationUtil.getClassNameForParameterType(field.getEndpointParameterType(), generator.getImports(), null);
		if (!parameterClass.startsWith("java.lang") && parameterClass.contains(".")) {
			generator.addImport(parameterClass);
			parameterClass = JavaCodeGenerationUtil.getClassNameWithoutPackage(parameterClass);
		}
		generator.addField(PojoField.create(parameterClass, field.getName(), field.getMsgField(), field.getDescription()));
	}
	
	private static void generateObjectParameterTypePojoField(Path src, String className, PojoGenerator generator, EndpointParameter field) throws IOException {
		String objectParamClassName = EndpointParameterTypeGenerationUtil.getLeafObjectParameterClassName(
																				field.getName(), 
																				field.getEndpointParameterType(), 
																				field.getObjectName(), 
																				generator.getImports(), 
																				className);
		if (log.isDebugEnabled())
			log.debug("generateObjectParameterTypePojoField: className:" + className + ", field:" + field + " objectParamClassName:" + objectParamClassName);
		generator.addImport(objectParamClassName);
		if (field.getParameters() != null) {
			generatePojo(src, objectParamClassName, field.getDescription(), field.getParameters(), field.getImplementedInterfaces(), null);
		}
		String objectClass = EndpointParameterTypeGenerationUtil.getClassNameForEndpointParameter(field, generator.getImports(), className);
		generator.addField(PojoField.create(objectClass, field.getName(), field.getMsgField(), field.getDescription()));
	}
	
	public static void generateCEX(ExchangeDescriptor exchangeDescriptor, Path outputFolder) throws IOException {
		if (log.isDebugEnabled())
			log.debug("Generating CEX classes for:" + StringUtils.truncate(exchangeDescriptor.toString(), 192) + " to:" + outputFolder);
		generateCEXPojos(exchangeDescriptor, outputFolder);
		generateCEXPojoDeserializers(exchangeDescriptor, outputFolder);
		generateCEXPojoSerializers(exchangeDescriptor, outputFolder);
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

	public static void generateSerializer(Path ouputFolder, String pojoClassName, List<EndpointParameter> fields) throws IOException {
		if (log.isDebugEnabled())
			log.debug("Generating serializer for :" + pojoClassName + " with fields:" + StringUtils.truncate(String.valueOf(fields), 192) + " to:" + ouputFolder);
		JsonPojoSerializerGenerator generator = new JsonPojoSerializerGenerator(pojoClassName, fields);
		
		for (EndpointParameter field: fields) {
			if ((field.getEndpointParameterType().isObject())
				&& field.getParameters() != null) {
				
				generateSerializer(ouputFolder, 
								   EndpointParameterTypeGenerationUtil.getLeafObjectParameterClassName(field.getName(), field.getEndpointParameterType(), field.getObjectName(), generator.getImports(), pojoClassName),
								   field.getParameters());
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

	public static void generateCEXPojos(ExchangeDescriptor exchangeDescriptor, Path outputFolder) throws IOException {
		if (log.isDebugEnabled())
			log.debug("Generating pojos for:" + StringUtils.truncate(exchangeDescriptor.toString(), 192) + " to:" + outputFolder);
		generateExchangeInterface(exchangeDescriptor, outputFolder);
		for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {
			generateExchangeApiInterface(exchangeDescriptor, api, outputFolder);
			if (api.getRestEndpoints() != null) {
				for (RestEndpointDescriptor restEndpointDescriptor: api.getRestEndpoints()) {
					List<String> requestInterfaces = new ArrayList<>();
					requestInterfaces.add(RestEndpointUrlParameters.class.getName());
					if (restEndpointDescriptor.getRequestInterfaces() != null) {
						requestInterfaces.addAll(restEndpointDescriptor.getRequestInterfaces());
					}
					String additionalBody = null;
					additionalBody = generateRestEndpointGetUrlParametersMethod(restEndpointDescriptor);
					ExchangeJavaWrapperGeneratorUtil.generatePojo(outputFolder, 
											generateRestEnpointRequestClassName(exchangeDescriptor, api, restEndpointDescriptor), 
											"Request for " + exchangeDescriptor.getName() + " " + api.getName() + " API " 
												+ restEndpointDescriptor.getName() + " REST endpoint"
												+ restEndpointDescriptor.getDescription()
												+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING,
											restEndpointDescriptor.getParameters(),
											requestInterfaces,
											additionalBody);
					if (restEndpointDescriptor.getResponse() != null) {
						ExchangeJavaWrapperGeneratorUtil.generatePojo(outputFolder, 
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
					ExchangeJavaWrapperGeneratorUtil.generatePojo(outputFolder, 
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
					ExchangeJavaWrapperGeneratorUtil.generatePojo(outputFolder, 
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
		if (log.isDebugEnabled())
			log.debug("Generating exchange interface:" + StringUtils.truncate(exchangeDescriptor.toString(), 192) + " to:" + outputFolder);
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
	
	public static String generateRestEndpointGetUrlParametersMethod(RestEndpointDescriptor restEndpointDescriptor) {
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
			if (endpointParameters.get(i).getEndpointParameterType().getType() == EndpointParameterTypes.LIST) {
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
			if (param.getEndpointParameterType().getType() == EndpointParameterTypes.LIST) {
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

	public static String getJsonMessageDeserializerClassName(String deserializedTypeClassName) {
		String pkg = StringUtils.substringBefore(JavaCodeGenerationUtil.getClassPackage(deserializedTypeClassName), ".pojo") + ".deserializers";
		return pkg + "." + JavaCodeGenerationUtil.getClassNameWithoutPackage(deserializedTypeClassName) + "Deserializer";
	}

}
