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
		String structTypeName = className + JavaCodeGenerationUtil.firstLetterToUpperCase(field.getName());
		generatePojo(src, structTypeName, field.getDescription(), field.getParameters());
		generator.addField(PojoField.create(structTypeName, field.getName(), field.getDescription()));
	}

}
