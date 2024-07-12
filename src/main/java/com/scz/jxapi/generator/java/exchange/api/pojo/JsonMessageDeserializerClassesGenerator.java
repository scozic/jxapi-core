package com.scz.jxapi.generator.java.exchange.api.pojo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.exchange.descriptor.CanonicalType;
import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.exchange.ClassesGenerator;
import com.scz.jxapi.generator.java.exchange.ExchangeJavaWrapperGeneratorUtil;
import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;

/**
 * Generator for all classes for deserializing from JSON a particular POJO, that is:
 * <ul>
 * <li>A class extending {@link AbstractJsonMessageDeserializer} for serialization of POJO.
 * <li>For each of 'Object' type {@link Field} belonging to that pojo, the deserializer class for corresponding nested POJO.
 * <ul>
 */
public class JsonMessageDeserializerClassesGenerator implements ClassesGenerator {
	
	private static final Logger log = LoggerFactory.getLogger(JsonMessageDeserializerClassesGenerator.class);
	private String deserializedClassName;
	private List<Field> fields;
	
	public JsonMessageDeserializerClassesGenerator(String deserializedClassName, List<Field> fields) {
		this.deserializedClassName = deserializedClassName;
		this.fields = fields;
	}

	@Override
	public void generateClasses(Path outputFolder) throws IOException {
		if (log.isDebugEnabled())
			log.debug("Generating Deserializer for :" 
						+ deserializedClassName 
						+ " with fields:" 
						+ StringUtils.truncate(String.valueOf(fields), 192) 
						+ " to:" + outputFolder);
		Set<String> imports = new HashSet<>();
		for (Field field: fields) {
			if ((field.getType().isObject())
				&& field.getParameters() != null) {
				Field objectParam = Field.createObject(CanonicalType.OBJECT.name(), 
																		 field.getName(), 
																		 field.getMsgField(), 
																		 field.getDescription(), 
																		 field.getParameters());
				objectParam.setObjectName(field.getObjectName());
				new JsonMessageDeserializerClassesGenerator(JavaCodeGenerationUtil.getClassPackage(deserializedClassName) + "."
																+ ExchangeJavaWrapperGeneratorUtil.getClassNameForEndpointParameter(
																		objectParam, 
																		imports, 
																		deserializedClassName), 
															field.getParameters()).generateClasses(outputFolder);
			}
		}
		JsonMessageDeserializerGenerator deserializerGenerator = new JsonMessageDeserializerGenerator(deserializedClassName, fields);
//		deserializerGenerator.getImports().addAll(imports);
		deserializerGenerator.writeJavaFile(outputFolder);
	}
}
