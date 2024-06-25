package com.scz.jxapi.generator.exchange.api.pojo;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.CanonicalEndpointParameterTypes;
import com.scz.jxapi.exchange.descriptor.EndpointParameter;
import com.scz.jxapi.generator.exchange.ClassesGeneratorTestUtil;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;

/**
 * Unit test for JsonPojoSerializerClassesGenerator
 */
public class JsonPojoSerializerClassesGeneratorTest {
	
	private static final Path BASE_PKG = Paths.get("com", "x", "serializers");
	
	private Path srcFolder;
	
	@After
	public void tearDown() throws IOException {
		if (srcFolder != null) {
			JavaCodeGenerationUtil.deletePath(srcFolder);
			srcFolder = null;
		}
	}

	@Test
	public void testGenerateJsonDeserializerClasses() throws IOException {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		
		String typeName = "com.x.MyPojo";
		List<EndpointParameter> endpointParameters = new ArrayList<>();
		endpointParameters.add(EndpointParameter.create(CanonicalEndpointParameterTypes.LONG.name(), "id", null, "identifier", "123"));
		endpointParameters.add(EndpointParameter.create(CanonicalEndpointParameterTypes.INT.name(), "score", null, "Current score", "0"));
		endpointParameters.add(EndpointParameter.createObject("OBJECT_LIST", "foo", "f", null,
				Arrays.asList(EndpointParameter.create(CanonicalEndpointParameterTypes.TIMESTAMP.name(), "time", null, "Creation time", "0"),
							  EndpointParameter.createObject(CanonicalEndpointParameterTypes.OBJECT.name(), "bar", "b", "The bar",
									  Arrays.asList(EndpointParameter.create(CanonicalEndpointParameterTypes.STRING.name(), "name", null, "Bar name", "my bar")))
						)
				));
		endpointParameters.add(EndpointParameter.createObject("OBJECT_LIST_MAP", "toto", "toto", null,
				Arrays.asList(EndpointParameter.create(CanonicalEndpointParameterTypes.STRING.name(), "id", null, "Toto ID", "toto#1"))
				));
		
		JsonPojoSerializerClassesGenerator generator = new JsonPojoSerializerClassesGenerator(typeName, endpointParameters);
		generator.generateClasses(srcFolder);
		checkJavaFilesCount(4);
		checkSourceFileExists(Path.of("MyPojoSerializer.java"));
		checkSourceFileExists(Path.of("MyPojoFooSerializer.java"));
		checkSourceFileExists(Path.of("MyPojoFooBarSerializer.java"));
		checkSourceFileExists(Path.of("MyPojoTotoSerializer.java"));
	}
	
	private void checkJavaFilesCount(int count) throws IOException {
		ClassesGeneratorTestUtil.checkJavaFilesCount(srcFolder.resolve(BASE_PKG), count);
	}
	
	private void checkSourceFileExists(Path srcFilePath) {
		ClassesGeneratorTestUtil.checkSourceFileExists(srcFolder.resolve(BASE_PKG), srcFilePath);
	}

}
