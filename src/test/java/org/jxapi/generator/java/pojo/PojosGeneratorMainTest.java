package org.jxapi.generator.java.pojo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ClassesGeneratorTestUtil;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.PojosDescriptor;
import org.jxapi.util.FileComparator;



/**
 * Unit tests for {@link PojosGeneratorMain}.
 */
public class PojosGeneratorMainTest {

  private Path tmpDir;

  @Before
  public void setUp() throws IOException {
    tmpDir = ClassesGeneratorTestUtil.generateTmpDir();
  }
  
  @After
  public void tearDown() throws IOException {
    if (tmpDir != null) {
      JavaCodeGenUtil.deletePath(tmpDir);
      tmpDir = null;
    }
  }
  
  @Test
  public void testGenerateExchangeDescriptorPojos() throws Exception {
    Path src = Paths.get("src", "gen", "java");
    Path projectSrc  = Paths.get(".").resolve(src);
    Path tmpSrc = tmpDir.resolve(src);
    PojosGeneratorMain.generatePojosInCurrentProject(".", tmpSrc.toString());
    // Check generated demo properties file
    FileComparator.checkSameFiles(projectSrc, tmpSrc);
  }
  
  @Test
  public void testGenerateExchangeDescriptorPojos_NoDescriptorFound() throws Exception {
    Path src = Paths.get("src", "gen", "java");
    Path tmpSrc = tmpDir.resolve(src);
    PojosGeneratorMain.generatePojosInCurrentProject(tmpDir.toString(), tmpSrc.resolve("unused").toString());
    // Check generated demo properties file
    Path readmeFile = tmpDir.resolve(Paths.get("src", "main", "resources", "jxapi", "pojos", "README.txt"));
    Assert.assertEquals("JXAPI POJOs descriptor files (.json or .yaml) should be written in this folder.", Files.readString(readmeFile));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGenerateClassesForPojos_InvalidPojoTypeNonObject() throws Exception {
    Path src = Paths.get("src", "gen", "java");
    Path tmpSrc = tmpDir.resolve(src);
    
    Field nonObjectField = Field.builder()
        .name("invalidField")
        .type("STRING") // Invalid type for a POJO
        .build();
    PojosDescriptor pojosDescriptor = new PojosDescriptor();
    pojosDescriptor.setBasePackage("org.jxapi.pojos.test");
    pojosDescriptor.setPojos(java.util.List.of(nonObjectField));
    PojosGeneratorMain.generatePojos(pojosDescriptor, tmpSrc);
  }
}
