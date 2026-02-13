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
    
    ExchangeGeneratorMain.generateExchangeWrapperAndDemos(
        ExchangeDescriptorParser.fromJson(descriptorFile), 
        tmpDir,
        tmpDir.resolve(ExchangeGeneratorMain.DEFAULT_GENERATED_SOURCES_FOLDER),
        tmpDir.resolve(ExchangeGeneratorMain.DEFAULT_GENERATED_TEST_SOURCES_FOLDER),
        baseJavaDocUrl, 
        baseSrcUrl);
    
    // Check the generated files
    Path actualSrcMainJava = tmpDir.resolve(ExchangeGeneratorMain.DEFAULT_GENERATED_SOURCES_FOLDER);
    Path actualSrcTestJava = tmpDir.resolve(ExchangeGeneratorMain.DEFAULT_GENERATED_TEST_SOURCES_FOLDER);
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
        "marketdata/pojo",}) {
      FileComparator.checkSameFiles(
          projectDemoWrapperPackage.resolve(fileName), 
          mainGenPackage.resolve(fileName));
    }
    
    // Check demo properties file
    FileComparator.checkSameFiles(projectDemoWrapperPackage.resolve("DemoExchangeDemoProperties.java"), 
                    testGenPackage.resolve("DemoExchangeDemoProperties.java"));
    
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
  
  @Test
  public void testGenerateEmployeeExchange() throws IOException {
    String baseJavaDocUrl = "./doc/javadoc/";
    String baseSrcUrl = ".";
    Path projectSrcTest  = Paths.get(".", "src", "test");
    Path projectSrcTestJava  = projectSrcTest.resolve("java");
    Path demoExchangePackage = Paths.get("org", "jxapi", "exchanges", "employee", "gen");
    Path projectDemoWrapperPackage = projectSrcTestJava.resolve(demoExchangePackage);
    Path projectSrcTestResources = Paths.get(".", "src", "test", "resources");
    Path descriptorFile = projectSrcTestResources.resolve("employeeExchange.yaml");
    
    ExchangeGeneratorMain.generateExchangeWrapperAndDemos(
        ExchangeDescriptorParser.fromYaml(descriptorFile), 
        tmpDir, 
        tmpDir.resolve(ExchangeGeneratorMain.DEFAULT_GENERATED_SOURCES_FOLDER),
        tmpDir.resolve(ExchangeGeneratorMain.DEFAULT_GENERATED_TEST_SOURCES_FOLDER),
        baseJavaDocUrl, 
        baseSrcUrl);
    
    // Check the generated files
    Path actualSrcMainJava = tmpDir.resolve(ExchangeGeneratorMain.DEFAULT_GENERATED_SOURCES_FOLDER);
    Path actualSrcTestJava = tmpDir.resolve(ExchangeGeneratorMain.DEFAULT_GENERATED_TEST_SOURCES_FOLDER);
    Path mainGenPackage = actualSrcMainJava.resolve(demoExchangePackage);
    Path testGenPackage = actualSrcTestJava.resolve(demoExchangePackage);
    
    // Check main package containing exchange wrapper classes
    for (String fileName : new String[] { 
        "EmployeeConstants.java", 
        "EmployeeExchange.java",
        "EmployeeExchangeImpl.java",
        "EmployeeProperties.java",
        "v1/EmployeeV1Api.java",
        "v1/EmployeeV1ApiImpl.java",
        "v1/pojo",}) {
      FileComparator.checkSameFiles(
          projectDemoWrapperPackage.resolve(fileName), 
          mainGenPackage.resolve(fileName));
    }
    
    // Check demo properties file
    FileComparator.checkSameFiles(projectDemoWrapperPackage.resolve("EmployeeDemoProperties.java"), 
                    testGenPackage.resolve("EmployeeDemoProperties.java"));
    
    // Check test package containing demo snippets
    FileComparator.checkSameFiles(projectDemoWrapperPackage.resolve("v1/demo"), 
                    testGenPackage.resolve("v1/demo"));
    
    // Check generated README.md
    FileComparator.checkSameFiles(Paths.get(".", "Employee_README.md"), 
                    tmpDir.resolve("Employee_README.md"));
    
    // Check generated demo properties file
    FileComparator.checkSameFiles(
      projectSrcTestResources.resolve("demo-Employee.properties.dist"),
      tmpDir.resolve(Paths.get("src", "test", "resources", "demo-Employee.properties.dist")));
  }
}
