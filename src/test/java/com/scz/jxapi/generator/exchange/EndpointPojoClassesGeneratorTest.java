package com.scz.jxapi.generator.exchange;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;

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
		List<EndpointParameter> endpointParameters = new ArrayList<>();
		endpointParameters.add(EndpointParameter.create(CanonicalEndpointParameterTypes.LONG.name(), "id", null, "identifier", "123"));
		endpointParameters.add(EndpointParameter.create(CanonicalEndpointParameterTypes.INT.name(), "score", null, "Current score", "0"));
		endpointParameters.add(EndpointParameter.createObject("OBJECT_LIST", "foo", "f", null,
				Arrays.asList(EndpointParameter.create(CanonicalEndpointParameterTypes.TIMESTAMP.name(), "time", null, "Creation time", "0"),
							  EndpointParameter.createObject(CanonicalEndpointParameterTypes.OBJECT.name(), "bar", "b", "The bar",
									  Arrays.asList(EndpointParameter.create(CanonicalEndpointParameterTypes.STRING.name(), "name", null, "Bar name", "my bar")))
						)
				));
		endpointParameters.add(EndpointParameter.createObject( "OBJECT_LIST_MAP", "toto", "toto", null,
				Arrays.asList(EndpointParameter.create(CanonicalEndpointParameterTypes.STRING.name(), "id", null, "Toto ID", "toto#1"))
				));
		
		EndpointPojoClassesGenerator generator = new EndpointPojoClassesGenerator(typeName, typeDescription, endpointParameters, List.of("com.x.common.MyInterface"), null);
		generator.generateClasses(srcFolder);
		checkJavaFilesCount(4);
		checkSourceFileExists(Path.of("MyPojo.java"));
		checkSourceFileExists(Path.of("MyPojoFoo.java"));
		checkSourceFileExists(Path.of("MyPojoFooBar.java"));
		checkSourceFileExists(Path.of("MyPojoToto.java"));
	}
	
	private void checkJavaFilesCount(int count) throws IOException {
		ClassesGeneratorTestUtil.checkJavaFilesCount(srcFolder.resolve(Paths.get("com", "x")), count);
	}
	
	private void checkSourceFileExists(Path srcFilePath) {
		ClassesGeneratorTestUtil.checkSourceFileExists(srcFolder.resolve(Paths.get("com", "x")), srcFilePath);
	}
}
