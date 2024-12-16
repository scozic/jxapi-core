package com.scz.jxapi.generator.java.exchange.api.pojo;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.exchange.ClassesGeneratorTestUtil;

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
	public void testGenerateJsonSerializerClasses() throws IOException {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		
		String typeName = "com.x.MyPojo";
		List<Field> endpointParameters = List.of(
			Field.builder().type(Type.LONG).name("id").description("identifier").build(),
			Field.builder().type(Type.INT).name("score").description("Current score").build(),
			Field.builder().type("OBJECT_LIST").name("foo")
						   .property(Field.builder().type(Type.TIMESTAMP).name("time").build())
						   .property(Field.builder().type(Type.OBJECT).name("bar")
							    		  .property(Field.builder().type(Type.STRING).name("name").build())
										  .build())
						   .build(),
			Field.builder().type("OBJECT_LIST_MAP").name("toto")
						   .property(Field.builder().type(Type.STRING).name("id").build())
						   .build()
		);
		
		JsonPojoSerializerClassesGenerator generator = new JsonPojoSerializerClassesGenerator(typeName, endpointParameters);
		generator.generateClasses(srcFolder);
		checkJavaFilesCount(4);
		checkSourceFileExists(Path.of("MyPojoSerializer.java"));
		checkSourceFileExists(Path.of("MyPojoFooSerializer.java"));
		checkSourceFileExists(Path.of("MyPojoFooBarSerializer.java"));
		checkSourceFileExists(Path.of("MyPojoTotoSerializer.java"));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testGenerateJsonSerializerClasses_NullObjectFieldProperties() {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		String typeName = "com.x.MyPojo";
		new JsonPojoSerializerClassesGenerator(typeName, null);
	}
	
	@Test
	public void testGenerateJsonSerializerClasses_NullObjectFieldSubProperties() throws IOException {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		
		String typeName = "com.x.MyPojo";
		List<Field> endpointParameters = List.of(
			Field.builder().type(Type.LONG).name("id").description("identifier").build(),
			Field.builder().type(Type.INT).name("score").description("Current score").build(),
			Field.builder().type("OBJECT_LIST").name("foo").msgField("f").build()
		);
		
		JsonPojoSerializerClassesGenerator generator = new JsonPojoSerializerClassesGenerator(typeName, endpointParameters);
		generator.generateClasses(srcFolder);
		checkJavaFilesCount(1);
		checkSourceFileExists(Path.of("MyPojoSerializer.java"));
	}
	
	private void checkJavaFilesCount(int count) {
		ClassesGeneratorTestUtil.checkJavaFilesCount(srcFolder.resolve(BASE_PKG), count);
	}
	
	private void checkSourceFileExists(Path srcFilePath) {
		ClassesGeneratorTestUtil.checkSourceFileExists(srcFolder.resolve(BASE_PKG), srcFilePath);
	}

}
