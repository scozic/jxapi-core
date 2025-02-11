package com.scz.jxapi.generator.java.exchange.api.pojo;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.exchange.ClassesGeneratorTestUtil;

/**
 * Unit test for {@link EndpointPojoClassesGenerator}
 */
public class EndpointPojoClassesGeneratorTest {

	private Path srcFolder;
	
	@After
	public void tearDown() throws IOException {
		if (srcFolder != null) {
			JavaCodeGenerationUtil.deletePath(srcFolder);
			srcFolder = null;
		}
	}
	
	@Test
	public void testGenerateClasses() throws Exception {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		String typeName = "com.x.MyPojo";
		String typeDescription = "Used in EndpointPojoGeneratorTest";
		List<Field> endpointParameters = new ArrayList<>();
		endpointParameters.add(Field.builder().type(Type.LONG).name("id").description("identifier").build());
		endpointParameters.add(Field.builder().type(Type.INT).name("score").description("Current score").build());
		endpointParameters.add(Field.builder().type("OBJECT_LIST").name("foo")
									.property(Field.builder().type(Type.TIMESTAMP).name("time").build())
									.property(Field.builder().name("bar")
												   .property(Field.builder().type(Type.STRING).name("name").build())
												   .build())
									.build());
		endpointParameters.add(Field.builder().type("OBJECT_LIST_MAP").name("toto")
									.property(Field.builder().type(Type.STRING).name("id").build())
									.build());
		EndpointPojoClassesGenerator generator = new EndpointPojoClassesGenerator(typeName, typeDescription, endpointParameters, null, null);
		generator.generateClasses(srcFolder);
		checkJavaFilesCount(4);
		checkSourceFileExists(Path.of("MyPojo.java"));
		checkSourceFileExists(Path.of("MyPojoFoo.java"));
		checkSourceFileExists(Path.of("MyPojoFooBar.java"));
		checkSourceFileExists(Path.of("MyPojoToto.java"));
	}
	
	@Test
	public void testGenerateClassesNullFieldParameters() throws Exception {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		String typeName = "com.x.MyPojo";
		String typeDescription = "Used in EndpointPojoGeneratorTest";
		List<Field> endpointParameters = new ArrayList<>();
		endpointParameters.add(Field.builder().type(Type.LONG).name("id").description("identifier").build());
		endpointParameters.add(Field.builder().type(Type.INT).name("score").description("Current score").build());
		endpointParameters.add(Field.builder().type("OBJECT_LIST").name("foo").build());
		EndpointPojoClassesGenerator generator = new EndpointPojoClassesGenerator(typeName, typeDescription, endpointParameters, null, null);
		generator.generateClasses(srcFolder);
		checkJavaFilesCount(1);
		checkSourceFileExists(Path.of("MyPojo.java"));
	}
	
	private void checkJavaFilesCount(int count) {
		ClassesGeneratorTestUtil.checkJavaFilesCount(srcFolder.resolve(Paths.get("com", "x")), count);
	}
	
	private void checkSourceFileExists(Path srcFilePath) {
		ClassesGeneratorTestUtil.checkSourceFileExists(srcFolder.resolve(Paths.get("com", "x")), srcFilePath);
	}
}
