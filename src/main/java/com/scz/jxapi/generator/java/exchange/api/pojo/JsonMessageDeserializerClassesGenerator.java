package com.scz.jxapi.generator.java.exchange.api.pojo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.scz.jxapi.exchange.descriptor.CanonicalType;
import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.exchange.ClassesGenerator;
import com.scz.jxapi.generator.java.exchange.api.ExchangeApiGeneratorUtil;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;

/**
 * Generator for all classes for deserializing from JSON a particular POJO, that is:
 * <ul>
 * <li>A class extending {@link AbstractJsonMessageDeserializer} for serialization of POJO.
 * <li>For each of 'Object' type {@link Field} belonging to that pojo, the deserializer class for corresponding nested POJO.
 * <ul>
 */
public class JsonMessageDeserializerClassesGenerator implements ClassesGenerator {
	
	private String deserializedClassName;
	private List<Field> fields;
	
	public JsonMessageDeserializerClassesGenerator(String deserializedClassName, List<Field> fields) {
		this.deserializedClassName = deserializedClassName;
		this.fields = fields;
	}

	@Override
	public void generateClasses(Path outputFolder) throws IOException {
		if (fields == null) {
			// Object with objectName referencing an object field defined somewhere else in API descriptor.
			return;
		}
		Set<String> imports = new HashSet<>();
		for (Field field: fields) {
			if ((field.getType().isObject())
				&& field.getParameters() != null) {
				Field objectParam = Field.createObject(CanonicalType.OBJECT.name(), 
																		 field.getName(), 
																		 field.getMsgField(), 
																		 field.getDescription(), 
																		 field.getParameters(),
																		 field.getObjectName());
				new JsonMessageDeserializerClassesGenerator(JavaCodeGenerationUtil.getClassPackage(deserializedClassName) + "."
																+ ExchangeApiGeneratorUtil.getClassNameForField(
																		objectParam, 
																		imports, 
																		deserializedClassName), 
															field.getParameters()).generateClasses(outputFolder);
			}
		}
		JsonMessageDeserializerGenerator deserializerGenerator = new JsonMessageDeserializerGenerator(deserializedClassName, fields);
		deserializerGenerator.writeJavaFile(outputFolder);
	}

}
