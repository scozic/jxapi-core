package com.scz.jcex.generator.exchange;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.scz.jcex.generator.JavaCodeGenerationUtil;
import com.scz.jcex.generator.JsonMessageDeserializerGenerator;
import com.scz.jcex.generator.JsonPojoSerializerGenerator;
import com.scz.jcex.generator.PojoField;
import com.scz.jcex.generator.PojoGenerator;
import com.scz.jcex.netutils.rest.RestEndpointUrlParameters;
import com.scz.jcex.netutils.websocket.WebsocketSubscribeParameters;
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
		String pkg = StringUtils.substringBefore(JavaCodeGenerationUtil.getClassPackage(deserializedClassName), ".pojo") + ".deserializers";
		JsonMessageDeserializerGenerator deserializerGenerator = new JsonMessageDeserializerGenerator(pkg, deserializedClassName, fields);
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
		for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {
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
											pkgPrefix
											+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
											+ JavaCodeGenerationUtil.firstLetterToUpperCase(restEndpointDescriptor.getName())
											+ "Request", 
										"Request for " + exchangeDescriptor.getName() + " " + api.getName() + " API " 
											+ restEndpointDescriptor.getName() + " REST endpoint"
											+ restEndpointDescriptor.getDescription()
											+ JavaCodeGenerationUtil.GENERATED_CODE_WARNING,
										restEndpointDescriptor.getParameters(),
										implementedInterfaces,
										additionalBody);
				ExchangeJavaWrapperGeneratorUtil.generatePojo(ouputFolder, 
						pkgPrefix 
							+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
							+ JavaCodeGenerationUtil.firstLetterToUpperCase(restEndpointDescriptor.getName())
							+ "Response", 
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
