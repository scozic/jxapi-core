package com.scz.jxapi.generator.exchange;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
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
		srcFolder = Paths.get("tmp" + Math.random());
		String typeName = "com.x.MyPojo";
		String typeDescription = "Used in EndpointPojoGeneratorTest";
		List<EndpointParameter> endpointParameters = new ArrayList<>();
		endpointParameters.add(EndpointParameter.create(EndpointParameterTypes.LONG.name(), "id", null, "identifier", "123"));
		endpointParameters.add(EndpointParameter.create(EndpointParameterTypes.INT.name(), "score", null, "Current score", "0"));
		endpointParameters.add(EndpointParameter.create("OBJECT_LIST", "foo", "f", null,
				Arrays.asList(EndpointParameter.create(EndpointParameterTypes.TIMESTAMP.name(), "time", null, "Creation time", "0"),
							  EndpointParameter.create(EndpointParameterTypes.OBJECT.name(), "bar", "b", "The bar",
									  Arrays.asList(EndpointParameter.create(EndpointParameterTypes.STRING.name(), "name", null, "Bar name", "my bar")))
						)
				));
		endpointParameters.add(EndpointParameter.create( "OBJECT_LIST_MAP", "toto", "toto", null,
				Arrays.asList(EndpointParameter.create(EndpointParameterTypes.STRING.name(), "id", null, "Toto ID", "toto#1"))
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
		Path pkg = srcFolder.resolve(Paths.get("com", "x"));
		File folder = pkg.toFile(); 
		Assert.assertTrue(folder.exists());
		Assert.assertTrue(folder.isDirectory());
		Assert.assertEquals("Expected " + count + " files, but got:" + Arrays.toString(folder.listFiles()),
							 count,	
							 folder.listFiles().length);
	}
	
	private void checkSourceFileExists(Path srcFilePath) {
		Path pkg = srcFolder.resolve(Paths.get("com", "x"));
		Path fullPath = pkg.resolve(srcFilePath);
		Assert.assertTrue(fullPath.toFile().exists());
	}
}
