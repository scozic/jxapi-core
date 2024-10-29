package com.scz.jxapi.generator.java.exchange.api.pojo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.exchange.ClassesGenerator;
import com.scz.jxapi.generator.java.exchange.api.ExchangeApiGeneratorUtil;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;

/**
 * Generator for all classes for deserializing from JSON a particular POJO, that is:
 * <ul>
 * <li>A class extending {@link AbstractJsonMessageDeserializer} for serialization of POJO.
 * <li>For each of 'Object' type {@link Field} belonging to that pojo, the deserializer classes for corresponding nested POJO.
 * <ul>
 */
public class JsonMessageDeserializerClassesGenerator implements ClassesGenerator {
	
	private String deserializedClassName;
	private List<Field> fields;
	
	/**
	 * Constructor.
	 * 
	 * @param deserializedClassName the fully qualified name of the POJO class to deserialize
	 * @param fields the properties of the POJO class
	 * @throws IllegalArgumentException if fields is <code>null</code>
	 */
	public JsonMessageDeserializerClassesGenerator(String deserializedClassName, List<Field> fields) {
		this.deserializedClassName = deserializedClassName;
		if (fields == null) {
			throw new IllegalArgumentException("null fields provided for " + deserializedClassName);
		}
		this.fields = fields;
	}

	@Override
	public void generateClasses(Path outputFolder) throws IOException {
		Set<String> imports = new HashSet<>();
		for (Field field: fields) {
			if ((field.getType().isObject())
				&& field.getProperties() != null) {
				Field objectParam = Field.builder()
										 .name(field.getName())
										 .type(Type.OBJECT)
										 .description(field.getDescription())
										 .msgField(field.getMsgField())
										 .properties(field.getProperties())
										 .objectName(field.getObjectName())
										 .build();
				new JsonMessageDeserializerClassesGenerator(JavaCodeGenerationUtil.getClassPackage(deserializedClassName) + "."
																+ ExchangeApiGeneratorUtil.getClassNameForField(
																		objectParam, 
																		imports, 
																		deserializedClassName), 
															field.getProperties()).generateClasses(outputFolder);
			}
		}
		JsonMessageDeserializerGenerator deserializerGenerator = new JsonMessageDeserializerGenerator(deserializedClassName, fields);
		deserializerGenerator.writeJavaFile(outputFolder);
	}

}
