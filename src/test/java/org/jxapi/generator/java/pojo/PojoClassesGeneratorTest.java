package org.jxapi.generator.java.pojo;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Test;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ClassesGeneratorTestUtil;
import org.jxapi.generator.java.exchange.ConstantValuePlaceholderResolverFactory;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Unit test for {@link PojoClassesGenerator}
 */
public class PojoClassesGeneratorTest {

  private Path srcFolder;
  
  @After
  public void tearDown() throws IOException {
    if (srcFolder != null) {
      JavaCodeGenUtil.deletePath(srcFolder);
      srcFolder = null;
    }
  }
  
  @Test
  public void testGenerateClasses() throws Exception {
    srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
    String typeName = "com.x.MyPojo";
    String typeDescription = "Used in EndpointPojoGeneratorTest";
    List<Field> properties = new ArrayList<>();
    properties.add(Field.builder().type(Type.LONG).name("id").description("identifier").build());
    properties.add(Field.builder().type(Type.INT).name("score").description("Current score").build());
    properties.add(Field.builder().type("OBJECT_LIST").name("foo")
                  .property(Field.builder().type(Type.LONG).name("time").build())
                  .property(Field.builder().name("bar")
                           .property(Field.builder().type(Type.STRING).name("name").build())
                           .build())
                  .build());
    properties.add(Field.builder().type("OBJECT_LIST_MAP").name("toto")
                  .property(Field.builder().type(Type.STRING).name("id").build())
                  .build());
    PojoClassesGenerator generator = new PojoClassesGenerator(typeName, typeDescription, properties, null, null, ConstantValuePlaceholderResolverFactory.NO_OP);
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
    List<Field> properties = new ArrayList<>();
    properties.add(Field.builder().type(Type.LONG).name("id").description("identifier").build());
    properties.add(Field.builder().type(Type.INT).name("score").description("Current score, max is ${constants.maxScore}").build());
    properties.add(Field.builder().type("OBJECT_LIST").name("foo").build());
    PlaceHolderResolver docPlaceHolderResolver = PlaceHolderResolver.create(Map.of("constants.maxScore", "100"));
    PojoClassesGenerator generator = new PojoClassesGenerator(typeName, typeDescription, properties, null, docPlaceHolderResolver, ConstantValuePlaceholderResolverFactory.NO_OP);
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
