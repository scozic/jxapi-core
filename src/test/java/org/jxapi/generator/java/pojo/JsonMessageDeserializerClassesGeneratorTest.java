package org.jxapi.generator.java.pojo;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ClassesGeneratorTestUtil;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.Type;

/**
 * Unit test for {@link JsonMessageDeserializerClassesGenerator}
 */
public class JsonMessageDeserializerClassesGeneratorTest {
  
  private static final Path BASE_PKG = Paths.get("com", "x", "deserializers");
  
  private Path srcFolder;
  
  @After
  public void tearDown() throws IOException {
    if (srcFolder != null) {
      JavaCodeGenUtil.deletePath(srcFolder);
      srcFolder = null;
    }
  }

  @Test
  public void testGenerateJsonDeserializerClasses() throws IOException {
    srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
    
    String typeName = "com.x.MyPojo";
    List<Field> endpointParameters = List.of(
      Field.builder().type(Type.LONG).name("id").description("identifier").build(),
      Field.builder().type(Type.INT).name("score").description("Current score").build(),
      Field.builder().type("OBJECT_LIST").name("foo")
               .property(Field.builder().type(Type.LONG).name("time").build())
               .property(Field.builder().name("bar")
                      .property(Field.builder().type(Type.STRING).name("name").build())
                      .build())
               .build(),
      Field.builder().type("OBJECT_LIST_MAP").name("toto")
               .property(Field.builder().type(Type.STRING).name("id").build())
               .build()
    );
    JsonMessageDeserializerClassesGenerator generator = new JsonMessageDeserializerClassesGenerator(typeName, endpointParameters);
    generator.generateClasses(srcFolder);
    checkJavaFilesCount(4);
    checkSourceFileExists(Path.of("MyPojoDeserializer.java"));
    checkSourceFileExists(Path.of("MyPojoFooDeserializer.java"));
    checkSourceFileExists(Path.of("MyPojoFooBarDeserializer.java"));
    checkSourceFileExists(Path.of("MyPojoTotoDeserializer.java"));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGenerateJsonDeserializerClasses_NullObjectProperties() throws IOException {
    srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
    String typeName = "com.x.MyPojo";
    new JsonMessageDeserializerClassesGenerator(typeName, null);
  }
  
  @Test
  public void testGenerateJsonDeserializerClasses_NullObjectSubProperties() throws IOException {
    srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
    String typeName = "com.x.MyPojo";
    List<Field> endpointParameters = new ArrayList<>();
    endpointParameters.add(Field.builder().type(Type.OBJECT).name("foo").msgField("f").build());
    JsonMessageDeserializerClassesGenerator generator = new JsonMessageDeserializerClassesGenerator(typeName, endpointParameters);
    generator.generateClasses(srcFolder);
    checkJavaFilesCount(1);
    checkSourceFileExists(Path.of("MyPojoDeserializer.java"));
  }
  
  private void checkJavaFilesCount(int count) {
    ClassesGeneratorTestUtil.checkJavaFilesCount(srcFolder.resolve(BASE_PKG), count);
  }
  
  private void checkSourceFileExists(Path srcFilePath) {
    ClassesGeneratorTestUtil.checkSourceFileExists(srcFolder.resolve(BASE_PKG), srcFilePath);
  }

}
