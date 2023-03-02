package com.scz.jcex.generator.exchange;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import com.scz.jcex.generator.JavaCodeGenerationUtil;
import com.scz.jcex.generator.JavaTypeGenerator;
import com.scz.jcex.generator.JsonMessageDeserializerGenerator;
import com.scz.jcex.generator.JsonPojoSerializerGenerator;
import com.scz.jcex.generator.PojoField;
import com.scz.jcex.generator.PojoGenerator;
import com.scz.jcex.netutils.deserialization.MessageDeserializer;
import com.scz.jcex.netutils.rest.RestEndpoint;
import com.scz.jcex.netutils.rest.RestEndpointUrlParameters;
import com.scz.jcex.netutils.rest.RestRequest;
import com.scz.jcex.netutils.websocket.DefaultWebsocketEndpoint;
import com.scz.jcex.netutils.websocket.WebsocketEndpoint;
import com.scz.jcex.netutils.websocket.WebsocketSubscribeParameters;
import com.scz.jcex.netutils.websocket.WebsocketSubscribeRequest;
import com.scz.jcex.util.EncodingUtil;

/**
 * Helper methods for generation of Java classes of a given crypto exchange wrapper
 */
public class ExchangeJavaWrapperGeneratorUtil {
	
	private static final EnumMap<EndpointParameterType, String> PARAMETER_TYPE_CLASSES = new EnumMap<>(EndpointParameterType.class);
	private static final String DEFAULT_STRING_LIST_SEPARATOR = ",";
	static {
		PARAMETER_TYPE_CLASSES.put(EndpointParameterType.BIGDECIMAL, BigDecimal.class.getName());
		PARAMETER_TYPE_CLASSES.put(EndpointParameterType.BOOLEAN, "boolean");
		PARAMETER_TYPE_CLASSES.put(EndpointParameterType.INT, "int");
		PARAMETER_TYPE_CLASSES.put(EndpointParameterType.LONG, "long");
		PARAMETER_TYPE_CLASSES.put(EndpointParameterType.TIMESTAMP, "long");
		PARAMETER_TYPE_CLASSES.put(EndpointParameterType.STRING, "String");
		PARAMETER_TYPE_CLASSES.put(EndpointParameterType.STRING_LIST, "java.util.List<String>");
	}
	
	public static void generatePojo(Path src, String className, String description, List<EndpointParameter> fields) throws IOException {
		generatePojo(src, className, description, fields, null, null);
	}
	
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
				generateSimpleParameterTypePojoField(generator, field);
				break;
			case STRUCT:
			case STRUCT_LIST:
				generateStructParameterTypePojoField(src, className, generator, field);
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
			if (field.getType() == EndpointParameterType.STRUCT 
				|| field.getType() == EndpointParameterType.STRUCT_LIST) {
				generateDeserializer(src, deserializedClassName + JavaCodeGenerationUtil.firstLetterToUpperCase(field.getName()), field.getParameters());
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
		generator.addField(PojoField.create(parameterClass, field.getName(), field.getDescription()));
	}
	
	private static void generateStructParameterTypePojoField(Path src, String className, PojoGenerator generator, EndpointParameter field) throws IOException {
//		String structTypeSimpleName = JavaCodeGenerationUtil.firstLetterToUpperCase(field.getName());
		String structTypeName = className + JavaCodeGenerationUtil.firstLetterToUpperCase(field.getName());
		generatePojo(src, structTypeName, field.getDescription(), field.getParameters());
		String parameterTypeName = structTypeName;
		generator.addImport(structTypeName);
		if (field.getType() == EndpointParameterType.STRUCT_LIST) {
			parameterTypeName = "java.util.List<" + JavaCodeGenerationUtil.getClassNameWithoutPackage(structTypeName) + ">";
		}
		generator.addField(PojoField.create(parameterTypeName, field.getName(), field.getDescription()));
	}
	
	public static void generateCEX(ExchangeDescriptor exchangeDescriptor, Path ouputFolder) throws IOException {
		generateCEXPojos(exchangeDescriptor, ouputFolder);
		generateCEXPojoDeserializers(exchangeDescriptor, ouputFolder);
		generateCEXPojoSerializers(exchangeDescriptor, ouputFolder);
		
	}
	
	public static void generateCEXPojoSerializers(ExchangeDescriptor exchangeDescriptor, Path ouputFolder) throws IOException {
		for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {
			String pkgPrefix =  exchangeDescriptor.getBasePackage() + "." + api.getName().toLowerCase() + ".pojo.";
			for (RestEndpointDescriptor restEndpointDescriptor: api.getRestEndpoints()) {
				ExchangeJavaWrapperGeneratorUtil.generateSerializer(ouputFolder, 
						pkgPrefix
						+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
						+ JavaCodeGenerationUtil.firstLetterToUpperCase(restEndpointDescriptor.getName())
						+ "Request",
						restEndpointDescriptor.getParameters());
				
				ExchangeJavaWrapperGeneratorUtil.generateSerializer(ouputFolder, 
						pkgPrefix 
							+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
							+ JavaCodeGenerationUtil.firstLetterToUpperCase(restEndpointDescriptor.getName())
							+ "Response",
						restEndpointDescriptor.getResponse());
			}
			
			for (WebsocketEndpointDescriptor wsEndpointDescriptor: api.getWebsocketEndpoints()) {
				ExchangeJavaWrapperGeneratorUtil.generateSerializer(ouputFolder, 
						pkgPrefix 
							+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
							+ JavaCodeGenerationUtil.firstLetterToUpperCase(wsEndpointDescriptor.getName())
							+ "Request"
							, wsEndpointDescriptor.getParameters());
				
				ExchangeJavaWrapperGeneratorUtil.generateSerializer(ouputFolder, 
						pkgPrefix
							+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
							+ JavaCodeGenerationUtil.firstLetterToUpperCase(wsEndpointDescriptor.getName())
							+ "Response",
						wsEndpointDescriptor.getResponse());
			}
		}
	}

	private static void generateSerializer(Path ouputFolder, String pojoClassName, List<EndpointParameter> fields) throws IOException {
		String serializerPkg = JavaCodeGenerationUtil.getClassPackage(getSerializerClassName(pojoClassName));
		JsonPojoSerializerGenerator generator = new JsonPojoSerializerGenerator(serializerPkg, pojoClassName, fields);
		
		for (EndpointParameter field: fields) {
			if (field.getType() == EndpointParameterType.STRUCT 
				|| field.getType() == EndpointParameterType.STRUCT_LIST) {
				generateSerializer(ouputFolder, pojoClassName + JavaCodeGenerationUtil.firstLetterToUpperCase(field.getName()), field.getParameters());
			}
		}
		
		generator.writeJavaFile(ouputFolder);
	}

	public static void generateCEXPojoDeserializers(ExchangeDescriptor exchangeDescriptor, Path ouputFolder) throws IOException {
		for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {
			String pkgPrefix =  exchangeDescriptor.getBasePackage() + "." + api.getName().toLowerCase() + ".pojo.";
			for (RestEndpointDescriptor restEndpointDescriptor: api.getRestEndpoints()) {
				ExchangeJavaWrapperGeneratorUtil.generateDeserializer(ouputFolder, 
						pkgPrefix 
							+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
							+ JavaCodeGenerationUtil.firstLetterToUpperCase(restEndpointDescriptor.getName())
							+ "Response",
						restEndpointDescriptor.getResponse());
			}
			
			for (WebsocketEndpointDescriptor wsEndpointDescriptor: api.getWebsocketEndpoints()) {
				ExchangeJavaWrapperGeneratorUtil.generateDeserializer(ouputFolder, 
						pkgPrefix
							+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
							+ JavaCodeGenerationUtil.firstLetterToUpperCase(wsEndpointDescriptor.getName())
							+ "Response",
						wsEndpointDescriptor.getResponse());
			}
		}
	}

	public static void generateCEXPojos(ExchangeDescriptor exchangeDescriptor, Path ouputFolder) throws IOException {
		generateExchangeInterface(exchangeDescriptor, ouputFolder);
		for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {
			generateExchangeApiInterface(exchangeDescriptor, api, ouputFolder);
			String pkgPrefix =  exchangeDescriptor.getBasePackage() + "." + api.getName().toLowerCase() + ".pojo.";
			for (RestEndpointDescriptor restEndpointDescriptor: api.getRestEndpoints()) {
				String urlParams = restEndpointDescriptor.getUrlParameters();
				List<String> implementedInterfaces = null;
				String additionalBody = null;
				if (urlParams != null) {
					implementedInterfaces = Arrays.asList(RestEndpointUrlParameters.class.getName());
					additionalBody = generateRestEndpointGetUrlParametersMethod(restEndpointDescriptor);
				}
				ExchangeJavaWrapperGeneratorUtil.generatePojo(ouputFolder, 
										generateRestEnpointRequestClassName(exchangeDescriptor, api, restEndpointDescriptor), 
										"Request for " + exchangeDescriptor.getName() + " " + api.getName() + " API " 
											+ restEndpointDescriptor.getName() + " REST endpoint"
											+ restEndpointDescriptor.getDescription()
											+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING,
										restEndpointDescriptor.getParameters(),
										implementedInterfaces,
										additionalBody);
				ExchangeJavaWrapperGeneratorUtil.generatePojo(ouputFolder, 
						generateRestEnpointResponseClassName(exchangeDescriptor, api, restEndpointDescriptor), 
						"Response to " + exchangeDescriptor.getName() 
							+ " " + api.getName() + " API " 
							+ restEndpointDescriptor.getName() 
							+ " REST endpoint request<br/>"
							+ restEndpointDescriptor.getDescription()
							+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING,
						restEndpointDescriptor.getResponse());
			}
			
			for (WebsocketEndpointDescriptor wsEndpointDescriptor: api.getWebsocketEndpoints()) {
				ExchangeJavaWrapperGeneratorUtil.generatePojo(ouputFolder, 
										pkgPrefix 
											+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
											+ JavaCodeGenerationUtil.firstLetterToUpperCase(wsEndpointDescriptor.getName())
											+ "Request", 
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
						pkgPrefix
							+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
							+ JavaCodeGenerationUtil.firstLetterToUpperCase(wsEndpointDescriptor.getName())
							+ "Response", 
						"Response message disseminated upon subscription to " 
							+ exchangeDescriptor.getName() + " " 
							+ api.getName() + " API " 
							+ wsEndpointDescriptor.getName() + " websocket endpoint request<br/>"
							+ wsEndpointDescriptor.getDescription()
							+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING,
						wsEndpointDescriptor.getResponse());
			}
		}
	}
	
	public static String generateRestEnpointRequestClassName(ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor exchangeApiDescriptor, RestEndpointDescriptor restEndpointDescriptor) {
		return generateRestEnpointPojoClassName(exchangeDescriptor, exchangeApiDescriptor, restEndpointDescriptor, "Request");
	}
	
	public static String generateRestEnpointResponseClassName(ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor exchangeApiDescriptor, RestEndpointDescriptor restEndpointDescriptor) {
		return generateRestEnpointPojoClassName(exchangeDescriptor, exchangeApiDescriptor, restEndpointDescriptor, "Response");
	}
	
	private static String generateRestEnpointPojoClassName(ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor exchangeApiDescriptor, RestEndpointDescriptor restEndpointDescriptor, String suffix) {
		return exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".pojo."
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
				+ JavaCodeGenerationUtil.firstLetterToUpperCase(restEndpointDescriptor.getName())
				+ suffix;
	}
	
	public static void generateExchangeApiInterface(ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor exchangeApiDescriptor, Path outputFolder) throws IOException {
		String fullInterfaceName = getApiInterfaceClassName(exchangeDescriptor, exchangeApiDescriptor);
		String simpleInterfaceName = JavaCodeGenerationUtil.getClassNameWithoutPackage(fullInterfaceName);
		String simpleImplementationName = simpleInterfaceName + "Impl";
		String fullImplementationName = fullInterfaceName + "Impl";
		
		JavaTypeGenerator interfaceGenerator = new JavaTypeGenerator(fullInterfaceName);
		interfaceGenerator.setDescription(exchangeApiDescriptor.getName() + " CEX " + exchangeApiDescriptor.getName() + " API</br>\n" 
				+ exchangeApiDescriptor.getDescription() + "\n" 
				+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING);
		interfaceGenerator.setTypeDeclaration("public interface ");
		
		JavaTypeGenerator implementationGenerator = new JavaTypeGenerator(fullImplementationName);
		implementationGenerator.setTypeDeclaration("public class ");
		implementationGenerator.setImplementedInterfaces(Arrays.asList(fullInterfaceName));
		implementationGenerator.setDescription("Actual implementation of {@link " + simpleInterfaceName + "}<br/>\n"
				   + JavaCodeGenerationUtil.GENERATED_CODE_WARNING);
		JavaCodeGenerationUtil.generateLoggerDeclaration(implementationGenerator);
		String restApiFactoryFullClassName = exchangeApiDescriptor.getRestEndpointFactory();
		implementationGenerator.addImport(restApiFactoryFullClassName);
		String restApiFactorySimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(restApiFactoryFullClassName);
		String restApiFactoryVariableName = "restEndpointFactory";
		implementationGenerator.appendToBody("private final "
				  							 + restApiFactorySimpleClassName
				  							 + " "
				  							 + restApiFactoryVariableName
				  							 + " = new "
				  							 + restApiFactorySimpleClassName + "();\n\n");
		
		String websocketEndpointFactoryFullClassName = exchangeApiDescriptor.getWebsocketEndpointFactory();
		implementationGenerator.addImport(websocketEndpointFactoryFullClassName);
		String websocketEndpointFactorySimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(websocketEndpointFactoryFullClassName);
		String websocketEndpointFactoryVariableName = "websocketEndpointFactory";
		implementationGenerator.appendToBody("private final "
					 + websocketEndpointFactorySimpleClassName
					 + " "
					 + websocketEndpointFactoryVariableName
					 + " = new "
					 + websocketEndpointFactorySimpleClassName + "();\n\n");
		
		
		StringBuilder implementationConstructorBody = new StringBuilder();
		implementationConstructorBody.append("this." + restApiFactoryVariableName + ".setProperties(properties);\n");
		
		for (RestEndpointDescriptor restApi: exchangeApiDescriptor.getRestEndpoints()) {
			implementationGenerator.addImport(RestEndpoint.class);
			String requestClassName = generateRestEnpointRequestClassName(exchangeDescriptor, exchangeApiDescriptor, restApi);
			String requestSimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(requestClassName);
			implementationGenerator.addImport(requestClassName);
			String responseClassName = generateRestEnpointResponseClassName(exchangeDescriptor, exchangeApiDescriptor, restApi);
			String responseSimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(responseClassName);
			implementationGenerator.addImport(responseClassName);
			String responseDeserializerClassName = JsonMessageDeserializerGenerator.getJsonMessageDeserializerClassName(responseClassName);
			implementationGenerator.addImport(responseDeserializerClassName);
			
			String apiMethodName = JavaCodeGenerationUtil.firstLetterToLowerCase(restApi.getName());
			String restEndpointVariableName = apiMethodName + "Api";
			
			implementationGenerator.appendToBody("\nprivate final RestEndpoint<" + requestSimpleClassName + ", " + responseSimpleClassName + "> " + restEndpointVariableName + ";\n\n"); 
			implementationConstructorBody.append("this." + restEndpointVariableName + " = "  
												 		 + restApiFactoryVariableName + ".createRestEndpoint(new " 
												 		 + JavaCodeGenerationUtil.getClassNameWithoutPackage(responseDeserializerClassName) 
												 		 + "());\n");
			
			String apiMethodSignature = responseSimpleClassName + " " + apiMethodName + "(" + requestSimpleClassName + " request) throws IOException"; 
			
			interfaceGenerator.addImport(IOException.class);
			interfaceGenerator.appendToBody(JavaCodeGenerationUtil.generateJavaDoc(restApi.getDescription()) + "\n");
			interfaceGenerator.appendToBody(apiMethodSignature + ";\n");
			
			implementationGenerator.addImport(IOException.class);
			implementationGenerator.addImport(RestRequest.class);
			StringBuilder apiMethodBody = new StringBuilder()
					.append("if (log.isDebugEnabled())\n")
					.append(JavaCodeGenerationUtil.INDENTATION)
					.append("log.debug(\"")
					.append(restApi.getHttpMethod().toUpperCase())
					.append(" ")
					.append(restApi.getName())
					.append(" > \" + request);\n")
					.append(responseSimpleClassName)
					.append(" response = ")
					.append(restEndpointVariableName)
					.append(".call(RestRequest.create(\"")
					.append(restApi.getUrl())
					.append("\", \"")
					.append(restApi.getHttpMethod().toUpperCase())
					.append("\", request));\n")
					.append("if (log.isDebugEnabled())\n")
					.append(JavaCodeGenerationUtil.INDENTATION)
					.append("log.debug(\"")
					.append(restApi.getHttpMethod().toUpperCase())
					.append(" ")
					.append(restApi.getName())
					.append(" < \" + response);\n")
					.append("return response;\n");
			implementationGenerator.appendMethod("@Override\npublic " + apiMethodSignature, apiMethodBody.toString());
		}
		
		for (WebsocketEndpointDescriptor websocketApi : exchangeApiDescriptor.getWebsocketEndpoints()) {
			implementationGenerator.addImport(WebsocketEndpoint.class);
			String requestClassName = generateWebsocketEndpointRequestClassName(exchangeDescriptor, exchangeApiDescriptor, websocketApi);
			String requestClassSimpleName = JavaCodeGenerationUtil.getClassNameWithoutPackage(requestClassName);
			implementationGenerator.addImport(requestClassName);
			String messageClassName = generateWebsocketEndpointMessageClassName(exchangeDescriptor, exchangeApiDescriptor, websocketApi);
			String messageClassSimpleName = JavaCodeGenerationUtil.getClassNameWithoutPackage(messageClassName);
			implementationGenerator.addImport(messageClassName);
			String messageDeserializerClassName = JsonMessageDeserializerGenerator.getJsonMessageDeserializerClassName(messageClassName);
			implementationGenerator.addImport(messageDeserializerClassName);
			String subscribeMethodName = "subscribe" + websocketApi.getName();
			String unsubscribeMethodName = "subscribe" + websocketApi.getName();
			String websocketEndpointVariableName = JavaCodeGenerationUtil.firstLetterToLowerCase(websocketApi.getName()) + "Ws";
			implementationGenerator.appendToBody("\nprivate final WebsocketEndpoint<" + requestClassSimpleName + ", " + messageClassSimpleName + "> " + websocketEndpointVariableName + ";\n\n");
			
			/*
			 * 	@Override
	public <S extends WebsocketSubscribeParameters, M> WebsocketEndpoint<S, M> createWebsocketEndpoint(
			String endpoitName, MessageDeserializer<M> messageDeserializer) {
		return new DefaultWebsocketEndpoint<>(websocketManager, messageDeserializer);
	}
			 */
			implementationGenerator.appendToBody("\nprivate final RestEndpoint<" + requestClassSimpleName + ", " + messageClassSimpleName + "> " + websocketEndpointVariableName + ";\n\n");
			implementationConstructorBody.append("this." + websocketEndpointVariableName + " = "  
												 		 + websocketEndpointFactoryVariableName + ".createWebsocketEndpoint(new " 
												 		 + JavaCodeGenerationUtil.getClassNameWithoutPackage(messageDeserializerClassName) 
												 		 + "());\n");
			String subscribeMethodSignature = "String " + subscribeMethodName + "(" + requestClassSimpleName + " request)";
			interfaceGenerator.appendToBody("Subscribe to " + websocketApi.getName() + " stream.<br/>\n" 
											+ JavaCodeGenerationUtil.generateJavaDoc(websocketApi.getDescription()) 
											+ "\n"
											+ "\n@return client subscriptionId to use for unsubscription");
			interfaceGenerator.appendToBody(subscribeMethodSignature + ";\n");
			
			String unsubscribeMethodSignature = " void " + unsubscribeMethodName + "(String subscriptionId)";
			interfaceGenerator.appendToBody("Unsubscribe from " + websocketApi + " stream.\n"
											+ "\n@param subscriptionId ID of subscription returned by #" + subscribeMethodName + "()");
			interfaceGenerator.appendToBody(unsubscribeMethodSignature + ";\n");
			implementationGenerator.addImport(WebsocketSubscribeRequest.class);
			
			// TODO
//			StringBuilder apiMethodBody = new StringBuilder()
//					.append("if (log.isDebugEnabled())\n")
//					.append(JavaCodeGenerationUtil.INDENTATION)
//					.append("log.debug(\"")
//					.append(restApi.getHttpMethod().toUpperCase())
//					.append(" ")
//					.append(restApi.getName())
//					.append(" > \" + request);\n")
//					.append(responseSimpleClassName)
//					.append(" response = ")
//					.append(restEndpointVariableName)
//					.append(".call(RestRequest.create(\"")
//					.append(restApi.getUrl())
//					.append("\", \"")
//					.append(restApi.getHttpMethod().toUpperCase())
//					.append("\", request));\n")
//					.append("return response;\n");
//			implementationGenerator.appendMethod("@Override\npublic " + apiMethodSignature, apiMethodBody.toString());
		}
		
		
		implementationGenerator.addImport(Properties.class);
		implementationGenerator.appendMethod("public " + simpleImplementationName + "(Properties properties)", implementationConstructorBody.toString());
		interfaceGenerator.writeJavaFile(outputFolder);
		implementationGenerator.writeJavaFile(outputFolder);
	}
	
	private static String generateWebsocketEndpointMessageClassName(ExchangeDescriptor exchangeDescriptor,
			ExchangeApiDescriptor exchangeApiDescriptor, WebsocketEndpointDescriptor websocketApi) {
		// TODO Auto-generated method stub
		return null;
	}

	private static String generateWebsocketEndpointRequestClassName(ExchangeDescriptor exchangeDescriptor,
			ExchangeApiDescriptor exchangeApiDescriptor, WebsocketEndpointDescriptor websocketApi) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void generateExchangeInterface(ExchangeDescriptor exchangeDescriptor, Path outputFolder) throws IOException {
		String pkgPrefix =  exchangeDescriptor.getBasePackage() + ".";
		String simpleInterfaceName = JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName());
		String fullInterfaceName = pkgPrefix + simpleInterfaceName;
		String simpleImplementationName = simpleInterfaceName + "Impl";
		String fullImplementationName = pkgPrefix + simpleImplementationName;
		
		JavaTypeGenerator interfaceGenerator = new JavaTypeGenerator(fullInterfaceName);
		interfaceGenerator.setDescription(exchangeDescriptor.getName() + " CEX API</br>\n" 
				+ exchangeDescriptor.getDescription() + "\n" 
				+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING);
		interfaceGenerator.setTypeDeclaration("public interface ");
		
		JavaTypeGenerator implementationGenerator = new JavaTypeGenerator(fullImplementationName);
		implementationGenerator.setTypeDeclaration("public class ");
		implementationGenerator.setImplementedInterfaces(Arrays.asList(fullInterfaceName));
		implementationGenerator.setDescription("Actual implementation of {@link " + simpleInterfaceName + "}<br/>\n"
				   + JavaCodeGenerationUtil.GENERATED_CODE_WARNING);
		
		StringBuilder implementationConstructorBody = new StringBuilder();
		for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {
			String apiClassName = getApiInterfaceClassName(exchangeDescriptor, api);
			String apiSimpleClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(apiClassName);
			String apiImplClassName = apiClassName + "Impl";
			String simpleApiImplClassName = JavaCodeGenerationUtil.getClassNameWithoutPackage(apiImplClassName);
			String getApiMethodSignature = apiSimpleClassName + " get" + apiSimpleClassName + "()";
			
			interfaceGenerator.addImport(apiClassName);
			interfaceGenerator.appendToBody("\n" + getApiMethodSignature + ";\n");
			
			implementationGenerator.addImport(apiClassName);
			implementationGenerator.addImport(apiImplClassName);
			String apiVariableName = JavaCodeGenerationUtil.firstLetterToLowerCase(apiSimpleClassName);
			implementationGenerator.appendToBody("private final " + apiSimpleClassName + " " + apiVariableName + ";\n\n");
			
			implementationConstructorBody.append("this." + apiVariableName + " = new " + simpleApiImplClassName + "(properties);\n");
			implementationGenerator.appendMethod("@Override\npublic " + getApiMethodSignature, "return this." + apiVariableName + ";\n");
			implementationGenerator.appendToBody("\n");
		}
		
		
		
		implementationGenerator.addImport(Properties.class);
		implementationGenerator.appendMethod("public " + simpleImplementationName + "(Properties properties)", implementationConstructorBody.toString());
		interfaceGenerator.writeJavaFile(outputFolder);
		implementationGenerator.writeJavaFile(outputFolder);
	}
	
	private static String getApiInterfaceClassName(ExchangeDescriptor exchangeDescriptor, ExchangeApiDescriptor exchangeApiDescriptor) {
		String pkgPrefix =  exchangeDescriptor.getBasePackage() + "." + exchangeApiDescriptor.getName().toLowerCase() + ".";
		String simpleInterfaceName = JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) + JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeApiDescriptor.getName());
		return pkgPrefix + simpleInterfaceName;
	}
	
	private static String generateRestEndpointGetUrlParametersMethod(RestEndpointDescriptor restEndpointDescriptor) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n@Override\npublic String getUrlParameters() {\n")
		  .append(JavaCodeGenerationUtil.INDENTATION)
		  .append(generateGetUrlParametersBody(restEndpointDescriptor.getUrlParameters(), restEndpointDescriptor.getParameters(), restEndpointDescriptor.getUrlParametersListSeparator()))
		  .append("}\n\n");
		return sb.toString(); 
	}

	private static String generateWebsocketSubscribeParametersGetTopicMethod(WebsocketEndpointDescriptor wsEndpointDescriptor) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n@Override\npublic String getTopic() {\n")
		  .append(JavaCodeGenerationUtil.INDENTATION)
		  .append(generateGetUrlParametersBody(wsEndpointDescriptor.getTopic(), wsEndpointDescriptor.getParameters(), wsEndpointDescriptor.getTopicParametersListSeparator()))
		  .append("}\n\n");
		return sb.toString(); 
	}
	
	private static String generateGetUrlParametersBody(String urlParametersTemplate, List<EndpointParameter> endpointParameters, String stringListSeparator) {
		if (endpointParameters.isEmpty()) {
			return "return \"" + urlParametersTemplate + "\";\n";
		}
		if (stringListSeparator == null) {
			stringListSeparator = DEFAULT_STRING_LIST_SEPARATOR;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("return ")
		  .append(EncodingUtil.class.getName())
		  .append(".substituteArguments(\"")
		  .append(urlParametersTemplate)
		  .append("\", ");
		int n = endpointParameters.size();
		for (int i = 0; i < n; i++) {
			String name = endpointParameters.get(i).getName();
			String value = name;
			if (endpointParameters.get(i).getType() == EndpointParameterType.STRING_LIST) {
				value = EncodingUtil.class.getName() + ".listToString(" + name + ", \"" + stringListSeparator + "\")"; 
			}
			sb.append("\"").append(name).append("\", ").append(value);
			if (i < n - 1) {
				sb.append(", ");
			}
		}
		sb.append(");\n");
		return sb.toString();
	}

}
