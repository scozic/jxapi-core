package org.jxapi.generator.java.exchange;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.util.FileComparator;

/**
 * Unit test for {@link ExchangeGeneratorMain}.
 * <p>
 * Also acts as an integration test for the whole exchange generation process.
 */
public class ExchangeGeneratorMainTest {
  
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
  public void testGenerateDemoExchange() throws IOException {
    String baseJavaDocUrl = "./doc/javadoc/";
    String baseSrcUrl = ".";
    Path projectSrcTest  = Paths.get(".", "src", "test");
    Path projectSrcTestJava  = projectSrcTest.resolve("java");
    Path demoExchangePackage = Paths.get("org", "jxapi", "exchanges", "demo", "gen");
    Path projectDemoWrapperPackage = projectSrcTestJava.resolve(demoExchangePackage);
    Path projectSrcTestResources = Paths.get(".", "src", "test", "resources");
    Path descriptorFile = projectSrcTestResources.resolve("demoExchange.json");
    
    ExchangeGeneratorMain.generateExchangeWrapperAndDemos(ExchangeDescriptorParser.fromJson(descriptorFile), tmpDir, baseJavaDocUrl, baseSrcUrl);
    
    // Check the generated files
    Path actualSrcMainJava = tmpDir.resolve(Paths.get("src", "main", "java"));
    Path actualSrcTestJava = tmpDir.resolve(Paths.get("src", "test", "java"));
    Path mainGenPackage = actualSrcMainJava.resolve(demoExchangePackage);
    Path testGenPackage = actualSrcTestJava.resolve(demoExchangePackage);
    
    // Check main package containing exchange wrapper classes
    for (String fileName : new String[] { 
        "DemoExchangeConstants.java", 
        "DemoExchangeExchange.java",
        "DemoExchangeExchangeImpl.java",
        "DemoExchangeProperties.java",
        "marketdata/DemoExchangeMarketDataApi.java",
        "marketdata/DemoExchangeMarketDataApiImpl.java",
        "marketdata/deserializers",
        "marketdata/serializers",
        "marketdata/pojo",}) {
      FileComparator.checkSameFiles(
          projectDemoWrapperPackage.resolve(fileName), 
          mainGenPackage.resolve(fileName));
    }
    
    // Check test package containing demo snippets
    FileComparator.checkSameFiles(projectDemoWrapperPackage.resolve("marketdata/demo"), 
                    testGenPackage.resolve("marketdata/demo"));
    
    // Check generated README.md
    FileComparator.checkSameFiles(Paths.get(".", "DemoExchange_README.md"), 
                    tmpDir.resolve("DemoExchange_README.md"));
    
    // Check generated demo properties file
    FileComparator.checkSameFiles(
      projectSrcTestResources.resolve("demo-DemoExchange.properties.dist"),
      tmpDir.resolve(Paths.get("src", "test", "resources", "demo-DemoExchange.properties.dist")));
  }
}
