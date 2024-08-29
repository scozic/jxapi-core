package com.scz.jxapi.generator.java.exchange.api.pojo;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.CanonicalType;
import com.scz.jxapi.exchange.descriptor.Field;
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
		endpointParameters.add(Field.create(CanonicalType.LONG.name(), "id", null, "identifier", "123"));
		endpointParameters.add(Field.create(CanonicalType.INT.name(), "score", null, "Current score", "0"));
		endpointParameters.add(Field.createObject("OBJECT_LIST", "foo", "f", null,
				Arrays.asList(Field.create(CanonicalType.TIMESTAMP.name(), "time", null, "Creation time", "0"),
							  Field.createObject(CanonicalType.OBJECT.name(), "bar", "b", "The bar",
									  Arrays.asList(Field.create(CanonicalType.STRING.name(), "name", null, "Bar name", "my bar")), null)
						), null
				));
		endpointParameters.add(Field.createObject( "OBJECT_LIST_MAP", "toto", "toto", null,
				Arrays.asList(Field.create(CanonicalType.STRING.name(), "id", null, "Toto ID", "toto#1")), null
				));
		
		EndpointPojoClassesGenerator generator = new EndpointPojoClassesGenerator(typeName, typeDescription, endpointParameters, List.of("com.x.common.MyInterface"), null);
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
		endpointParameters.add(Field.create(CanonicalType.LONG.name(), "id", null, "identifier", "123"));
		endpointParameters.add(Field.create(CanonicalType.INT.name(), "score", null, "Current score", "0"));
		endpointParameters.add(Field.createObject("OBJECT_LIST", "foo", "f", null, null, null));
		
		EndpointPojoClassesGenerator generator = new EndpointPojoClassesGenerator(typeName, typeDescription, endpointParameters, List.of("com.x.common.MyInterface"), null);
		generator.generateClasses(srcFolder);
		checkJavaFilesCount(1);
		checkSourceFileExists(Path.of("MyPojo.java"));
	}
	
	private void checkJavaFilesCount(int count) throws IOException {
		ClassesGeneratorTestUtil.checkJavaFilesCount(srcFolder.resolve(Paths.get("com", "x")), count);
	}
	
	private void checkSourceFileExists(Path srcFilePath) {
		ClassesGeneratorTestUtil.checkSourceFileExists(srcFolder.resolve(Paths.get("com", "x")), srcFilePath);
	}
}
