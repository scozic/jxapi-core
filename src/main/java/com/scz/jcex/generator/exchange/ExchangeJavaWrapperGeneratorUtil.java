package com.scz.jcex.generator.exchange;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.List;

import com.scz.jcex.generator.JavaCodeGenerationUtil;
import com.scz.jcex.generator.PojoField;
import com.scz.jcex.generator.PojoGenerator;

/**
 * Helper methods for generation of Java classes of a given crypto exchange wrapper
 */
public class ExchangeJavaWrapperGeneratorUtil {
	
	private static final EnumMap<EndpointParameterType, String> PARAMETER_TYPE_CLASSES = new EnumMap<>(EndpointParameterType.class);
	static {
		PARAMETER_TYPE_CLASSES.put(EndpointParameterType.BIGDECIMAL, BigDecimal.class.getName());
		PARAMETER_TYPE_CLASSES.put(EndpointParameterType.INT, "int");
		PARAMETER_TYPE_CLASSES.put(EndpointParameterType.TIMESTAMP, "long");
		PARAMETER_TYPE_CLASSES.put(EndpointParameterType.STRING, "String");
		PARAMETER_TYPE_CLASSES.put(EndpointParameterType.STRING_LIST, "java.util.List<String>");
	}
	
	public static void generatePojo(Path src, String className, String description, List<EndpointParameter> fields) throws IOException {
		PojoGenerator generator = new PojoGenerator(className);
		generator.setDescription(description);
		for (EndpointParameter field: fields) {
			switch (field.getType()) {
			case BIGDECIMAL:
			case INT:
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
		
		generator.writeJavaFile(src);
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
		
		
	}
	
	private static void generateCEXPojos(ExchangeDescriptor exchangeDescriptor, Path ouputFolder) throws IOException {
		for (ExchangeApiDescriptor api: exchangeDescriptor.getApis()) {
			String pkgPrefix =  exchangeDescriptor.getBasePackage() + "." + api.getName().toLowerCase() + ".pojo.";
			for (RestEndpointDescriptor restEndpointDescriptor: api.getRestEndpoints()) {
				ExchangeJavaWrapperGeneratorUtil.generatePojo(ouputFolder, 
											pkgPrefix
											+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
											+ JavaCodeGenerationUtil.firstLetterToUpperCase(restEndpointDescriptor.getName())
											+ "Request", 
										"Request for " + exchangeDescriptor.getName() + " " + api.getName() + " API " 
											+ restEndpointDescriptor.getName() + " REST endpoint",
										restEndpointDescriptor.getParameters());
				ExchangeJavaWrapperGeneratorUtil.generatePojo(ouputFolder, 
						pkgPrefix 
							+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
							+ JavaCodeGenerationUtil.firstLetterToUpperCase(restEndpointDescriptor.getName())
							+ "Response", 
						"Response to " + exchangeDescriptor.getName() + " " + api.getName() + " API " 
							+ restEndpointDescriptor.getName() + " REST endpoint request",
						restEndpointDescriptor.getResponse());
			}
			
			for (WebsocketEndpointDescriptor wsEndpointDescriptor: api.getWebsocketEndpoints()) {
				ExchangeJavaWrapperGeneratorUtil.generatePojo(ouputFolder, 
											pkgPrefix 
											+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
											+ JavaCodeGenerationUtil.firstLetterToUpperCase(wsEndpointDescriptor.getName())
											+ "Request", 
										"Subscription request to" + exchangeDescriptor.getName() + " " + api.getName() + " API " 
											+ wsEndpointDescriptor.getName() + " REST endpoint",
											wsEndpointDescriptor.getParameters());
				ExchangeJavaWrapperGeneratorUtil.generatePojo(ouputFolder, 
							pkgPrefix
							+ JavaCodeGenerationUtil.firstLetterToUpperCase(exchangeDescriptor.getName()) 
							+ JavaCodeGenerationUtil.firstLetterToUpperCase(wsEndpointDescriptor.getName())
							+ "Response", 
						"Response to " + exchangeDescriptor.getName() + " " + api.getName() + " API " 
							+ wsEndpointDescriptor.getName() + " REST endpoint request",
							wsEndpointDescriptor.getResponse());
			}
		}
		
		
	}

}
