package com.scz.jxapi.generator.exchange;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.netutils.deserialization.json.AbstractJsonMessageDeserializer;

/**
 * Generator for all classes for deserializing from JSON a particular POJO, that is:
 * <ul>
 * <li>A class extending {@link AbstractJsonMessageDeserializer} for serialization of POJO.
 * <li>For each of 'Object' type {@link EndpointParameter} belonging to that POJO, the deserializer class for corresponding nested POJO.
 * <ul>
 */
public class JsonPojoSerializerClassesGenerator implements ClassesGenerator {
	
	private static final Logger log = LoggerFactory.getLogger(JsonPojoSerializerClassesGenerator.class);
	
	private String deserializedClassName;
	private List<EndpointParameter> fields;
	
	public JsonPojoSerializerClassesGenerator(String deserializedClassName, List<EndpointParameter> fields) {
		this.deserializedClassName = deserializedClassName;
		this.fields = fields;
	}

	@Override
	public void generateClasses(Path outputFolder) throws IOException {
		if (log.isDebugEnabled())
			log.debug("Generating serializer for :" 
						+ deserializedClassName 
						+ " with fields:" 
						+ StringUtils.truncate(String.valueOf(fields), 192) 
						+ " to:" + outputFolder);
		JsonPojoSerializerGenerator generator = new JsonPojoSerializerGenerator(deserializedClassName, fields);
		
		for (EndpointParameter field: fields) {
			if ((field.getEndpointParameterType().isObject())
				&& field.getParameters() != null) {
				new JsonPojoSerializerClassesGenerator( 
								   EndpointParameterTypeGenerationUtil.getLeafObjectParameterClassName(
										   	field.getName(), 
										   	field.getEndpointParameterType(), 
										   	field.getObjectName(), 
										   	generator.getImports(), 
										   	deserializedClassName),
								   field.getParameters()).generateClasses(outputFolder);;
			}
		}
		
		generator.writeJavaFile(outputFolder);
		
	}

}
