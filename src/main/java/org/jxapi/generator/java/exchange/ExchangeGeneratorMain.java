package org.jxapi.generator.java.exchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.ExchangeDescriptor;
import org.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.api.ExchangeApiClassesGenerator;
import org.jxapi.generator.java.exchange.api.demo.EndpointDemoGenUtil;
import org.jxapi.generator.java.exchange.api.demo.ExchangeDemoClassesGenerator;
import org.jxapi.generator.java.pojo.PojosGeneratorMain;
import org.jxapi.generator.md.exchange.ExchangeReadmeMdGenerator;
import org.jxapi.generator.properties.exchange.ExchangeDemoPropertiesTemplateGenerator;
import org.jxapi.netutils.rest.ratelimits.RateLimitManager;
import org.jxapi.util.DemoUtil;
import org.jxapi.util.PlaceHolderResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Java main class that performs generation of exchange APIs for every file
 * found with name ending with 'Descriptor.json' in 'src/main/resources/'
 * folder. This is the main entry point to generate all exchange API classes,
 * and target of the maven exec plugin in wrapper module pom.xml.
 * <p>
 * The generated code is written in 'src/main/java/' folder.
 * <p>
 * The generated code is based on the JSON descriptor file format defined in
 * {@link ExchangeDescriptor}.
 * <p>
 * The generated code includes:
 * <ul>
 * <li>Java interface for the exchange with a getter method to retrieve each
 * exchange API interface.
 * <li>Implementation of that exchange interface with eventual
 * {@link RateLimitManager} to enforce rate limit rules if any.
 * <li>Java interface for each API listed in {@link ExchangeDescriptor} (see
 * {@link ExchangeApiDescriptor} and generator for its classes
 * {@link ExchangeApiClassesGenerator}), with all classes for every
 * REST/Websocket endpoint.
 * <li>All demo classes for each API listed in {@link ExchangeDescriptor} (see
 * {@link ExchangeApiDescriptor} see {@link ExchangeDemoClassesGenerator}).
 * </ul>
 * 
 */
public class ExchangeGeneratorMain {

  private static final Logger log = LoggerFactory.getLogger(ExchangeGeneratorMain.class);
  
  private static final String JXAPI = "jxapi";
  
  /**
   * Folder where exchange descriptor files are located in the wrapper project.
     */
  public static final Path DESCRIPTOR_FOLDER = Paths.get("src", "main", "resources", JXAPI, "exchange");
  
  /**
   * Default folder where to generate main sources
   */
  public static final Path DEFAULT_GENERATED_SOURCES_FOLDER = Paths.get("target", "generated-sources", JXAPI);
  
  /**
   * Default folder where to generate test sources
   */
  public static final Path DEFAULT_GENERATED_TEST_SOURCES_FOLDER = Paths.get("target", "generated-test-sources", JXAPI);
  
  /**
   * Key for system property that can be passed to JVM running
   * {@link #main(String[])} to specify the base URL for Java wrapper project
   * documentation
   */
  public static final String BASE_JAVADOC_URL_SYS_PROP = "baseJavaDocUrl";
  
  /**
   * Key for system property that can be passed to JVM running
   * {@link #main(String[])} to specify the main source directory relative to
   * project folder where to write generated code.
   */
  public static final String SRC_MAIN_DIRECTORY_SYS_PROP = "srcMainDirectory";
  
  /**
   * Key for system property that can be passed to JVM running
   * {@link #main(String[])} to specify the test source directory relative to
   * project folder where to write generated demo code.
   */
  public static final String SRC_TEST_DIRECTORY_SYS_PROP = "srcTestDirectory";
  
  /**
   * Key for system property that can be passed to JVM running
   * {@link #main(String[])} to specify the base URL for Java wrapper project
   * sources to use in README.md generation documentation links.
   */
  public static final String BASE_SRC_URL_SYS_PROP = "baseSrcUrl";
  

  
  /**
   * Main entry point to generate exchange API wrappers for all exchange
   * descriptor files in the current project "src/main/resources/" folder.
   * 
   * @param args Not used
   * @see #generateExchangeWrappersInCurrentProject(String, String, String, String, String)
   */
  public static void main(String[] args) {
    try {
      String baseProjectDir = System.getProperty(PojosGeneratorMain.BASE_PROJECT_DIR_PROP);
      String mainSrcDirectory = System.getProperty(SRC_MAIN_DIRECTORY_SYS_PROP, DEFAULT_GENERATED_SOURCES_FOLDER.toString());
      String testSrcDirectory = System.getProperty(SRC_TEST_DIRECTORY_SYS_PROP, DEFAULT_GENERATED_TEST_SOURCES_FOLDER.toString());
      String baseJavaDocUrl = System.getProperty(BASE_JAVADOC_URL_SYS_PROP);
      String baseSrcUrl = System.getProperty(BASE_SRC_URL_SYS_PROP);
      generateExchangeWrappersInCurrentProject(
          baseProjectDir, 
          mainSrcDirectory, 
          testSrcDirectory, 
          baseJavaDocUrl, 
          baseSrcUrl);
    } catch (Throwable t) {
      log.error("Error from " + ExchangeGeneratorMain.class.getName() + ".main", t);
      System.exit(-1);
    }
    System.exit(0);
  }
  
  /**
   * Generate exchange API wrappers for all exchange descriptor files in the
   * current project "src/main/resources/" folder. Will walk through all files in
   * the folder and generate Java code for each file ending with 'Descriptor.json'
   * using {@link #generateExchangeWrapperAndDemos(ExchangeDescriptor, Path, Path, Path, String, String)}.
   * 
   * @param baseProjectDir Base project directory where the generated code will be written
   * @param mainSrcDirectory The main source directory relative to project folder where to write generated code
   * @param testSrcDirectory The test source directory relative to project folder where to write generated demo code
   * @param baseJavaDocUrl The base url for project classes javadoc, used for links generation
   * @param baseSrcUrl The base url for sources on public repo, used for links generation.
   * 
   * @throws Exception If error occurs during generation
   * @see #generateExchangeWrapperAndDemos(ExchangeDescriptor, Path, Path, Path, String, String)
   */
  public static final void generateExchangeWrappersInCurrentProject(
      String baseProjectDir, 
      String mainSrcDirectory, 
      String testSrcDirectory, 
      String baseJavaDocUrl, 
      String baseSrcUrl) throws Exception {
    baseProjectDir = Optional.ofNullable(baseProjectDir).orElse(".");
    Path projectFolder = Paths.get(baseProjectDir);
    Path resources = projectFolder.resolve(DESCRIPTOR_FOLDER);
    if (!Files.exists(resources)) {
      resources.toFile().mkdirs();
      Files.writeString(resources.resolve("README.txt"), 
          "JXAPI Exchange descriptor files (.json or .yaml) should be written in this folder.\nYou may delete this README.txt file");
      return;
    }
    log.info("Generating exchange API wrapper and demos for all exchange descriptor files in {}", resources.toAbsolutePath());
    AtomicReference<Exception> error = new AtomicReference<>();
    ExchangeDescriptorParser.collectAndMergeExchangeDescriptors(resources)
        .forEach(exchangeDescriptor -> {
          try {
            generateExchangeWrapperAndDemos(
                exchangeDescriptor, 
                projectFolder, 
                Path.of(mainSrcDirectory), 
                Path.of(testSrcDirectory),  
                baseJavaDocUrl, 
                baseSrcUrl);
          } catch (Exception ex) {
            // Remark: intentionally catching all exceptions to continue generation for other exchanges
            log.error("Error while generating exchange descriptor for exchange:"
                + exchangeDescriptor.getId() + ":" + ex.getMessage(), ex);
            error.set(ex);
          }
        });
    Exception ex = error.get();
    if (ex != null) {
      throw ex;
    }
  }
  
  /**
   * Generate exchange API wrapper and demo snippets code for a given JSON
   * descriptor file.
   * 
   * @param exchangeDescriptor The exchange descriptor to generate the Java
   *                           wrapper
   * @param projectFolder      Project folder, the <code>src/main/java/</code>,
   *                           <code>src/test/java/</code> source folders and
   *                           README.md sample will be generated here.
   * @param baseJavaDocUrl     The base url for project classes javadoc, used for
   *                           links generation
   * @param baseSrcUrl         The base url for sources on public repo, used for
   *                           links generation.
   * @throws IOException if an I/O error occurs
   */
  public static void generateExchangeWrapperAndDemos(ExchangeDescriptor exchangeDescriptor,
                             Path projectFolder, 
                             Path mainSrcDirectory,
                             Path testSrcDirectory,
                             String baseJavaDocUrl, 
                             String baseSrcUrl) throws IOException {
    log.info("Generating exchange wrapper code for descriptor:{}", exchangeDescriptor.getId());
    Path outputSrcMainFolder = mainSrcDirectory;
    Path mainPackagePath = Paths.get(exchangeDescriptor.getBasePackage().replace('.', '/'));
    Path genMainPackagesFolder = outputSrcMainFolder.resolve(mainPackagePath);
    JavaCodeGenUtil.deletePath(genMainPackagesFolder);
    generateExchangeWrapper(exchangeDescriptor, outputSrcMainFolder);
    
    log.info("Generating exchange demos code for descriptor:{}", exchangeDescriptor.getId());
    Path outputSrcTestFolder = testSrcDirectory;
    Path genTestPackagesFolder = outputSrcTestFolder.resolve(mainPackagePath);
    JavaCodeGenUtil.deletePath(genTestPackagesFolder);
    List<ConfigPropertyDescriptor> demoProperties = EndpointDemoGenUtil.collectDemoConfigProperties(exchangeDescriptor);
    generateExchangeWrapperDemos(exchangeDescriptor, demoProperties, outputSrcTestFolder);
    
    Path srcTestResourcesFolder = projectFolder.resolve(Paths.get("src", "test", "resources"));
    log.info("Generating demo exchange configuration properties file template in {}", srcTestResourcesFolder);
    generateDemoPropertiesFileTemplate(exchangeDescriptor, demoProperties, srcTestResourcesFolder);
    
    log.info("Generating exchange README for:{}", exchangeDescriptor.getId());
    boolean hasDemoProperties = !demoProperties.isEmpty();
    generateExchangeWrapperReadme(exchangeDescriptor, projectFolder, baseJavaDocUrl, baseSrcUrl, hasDemoProperties);
    
    log.info("Done generating java code for {}", exchangeDescriptor.getId());
  }

  /**
   * Generates sample README.md file for exchange Java wrapper.
   * @param exchangeDescriptor Java wrapper exchange descriptor
   * @param projectFolder Java wrapper module project folder
   * @param baseJavaDocUrl Base URL for javadoc links in generated README. For instance a link to javadoc.io 
   * @param baseSrcUrl Base URL for source files for instance Github project url suffixed with corresponding branch.
   * @throws IOException If error occurs while trying to generate exchangeName_README.md file at root of project folder
   */
  public static void generateExchangeWrapperReadme(ExchangeDescriptor exchangeDescriptor, 
                             Path projectFolder, 
                             String baseJavaDocUrl, 
                             String baseSrcUrl) throws IOException {
    boolean hasDemoProperties = !EndpointDemoGenUtil.collectDemoConfigProperties(exchangeDescriptor).isEmpty();
    new ExchangeReadmeMdGenerator(exchangeDescriptor, baseJavaDocUrl, baseSrcUrl, hasDemoProperties).writeJavaFile(projectFolder);
  }
  
  private static void generateExchangeWrapperReadme(ExchangeDescriptor exchangeDescriptor, 
      Path projectFolder, 
      String baseJavaDocUrl, 
      String baseSrcUrl,
      boolean hasDemoProperties) throws IOException {
    new ExchangeReadmeMdGenerator(exchangeDescriptor, baseJavaDocUrl, baseSrcUrl, hasDemoProperties).writeJavaFile(projectFolder);
}

  /**
   * Generates exchange API wrapper (without demo snippets) for given exchange descriptor
   * @param exchangeDescriptor The exchange descriptor to generate Java wrapper for all APIs of
   * @param srcFolder The src%2Fmain%2Fpackage of wrapper project
   * @throws IOException If error occurs during generation
   */
  public static void generateExchangeWrapper(ExchangeDescriptor exchangeDescriptor, Path srcFolder) throws IOException {
    new ExchangeClassesGenerator(exchangeDescriptor).generateClasses(srcFolder);
  }
  
  /**
   * Generates exchange API wrapper demo snippets for given exchange descriptor
   * @param exchangeDescriptor The exchange descriptor to generate Java wrapper for all APIs of
   * @param srcFolder The <code>src/test/java/</code> source folder of wrapper project
   * @throws IOException If error occurs during generation
   */
  public static void generateExchangeWrapperDemos(ExchangeDescriptor exchangeDescriptor, Path srcFolder) throws IOException {
    generateExchangeWrapperDemos(
        exchangeDescriptor,
        EndpointDemoGenUtil.collectDemoConfigProperties(exchangeDescriptor), 
        srcFolder);
  }
  
  private static void generateExchangeWrapperDemos(
      ExchangeDescriptor exchangeDescriptor, 
      List<ConfigPropertyDescriptor> demoProperties, Path srcFolder) throws IOException {
    new ExchangeDemoClassesGenerator(exchangeDescriptor, demoProperties).generateClasses(srcFolder);
  }
  
  /**
   * Generates a demo exchange properties file template for the given exchange descriptor in the resources folder.
   * 
   * @param exchangeDescriptor The exchange descriptor to generate the properties
   *                           file template for
   * @param resourcesFolder    The resources folder where to write the properties
   *                           file
   * @throws IOException If error occurs while writing the file
   */
  public static void generateDemoPropertiesFileTemplate(ExchangeDescriptor exchangeDescriptor, Path resourcesFolder) throws IOException {
    generateDemoPropertiesFileTemplate(exchangeDescriptor,
        EndpointDemoGenUtil.collectDemoConfigProperties(exchangeDescriptor), 
        resourcesFolder);
  }
  
  private static void generateDemoPropertiesFileTemplate(
      ExchangeDescriptor exchangeDescriptor, 
      List<ConfigPropertyDescriptor> demoProperties, 
      Path resourcesFolder) throws IOException {
    String fileName = DemoUtil.getDefaultDemoExchangePropertiesFileName(exchangeDescriptor.getId()) + ".dist";
    Files.createDirectories(resourcesFolder);
    Path filePath = resourcesFolder.resolve(Paths.get(fileName));
    log.info("Generating demo exchange properties template file:{}", filePath);
    List<ConfigPropertyDescriptor> configProperties = new ArrayList<>();
    configProperties.addAll(Optional.ofNullable(exchangeDescriptor.getProperties()).orElse(List.of()));
    PlaceHolderResolver valuesPlaceHolderResolver = PlaceHolderResolver.create(ExchangeGenUtil.getValuesReplacements(exchangeDescriptor));
    PlaceHolderResolver descriptionPlaceHolderResolver = PlaceHolderResolver
      .create(ExchangeGenUtil.getDescriptionReplacements(exchangeDescriptor, null));
    new ExchangeDemoPropertiesTemplateGenerator(
      exchangeDescriptor.getId(), 
      configProperties, 
      demoProperties,
      valuesPlaceHolderResolver,
      descriptionPlaceHolderResolver)
    .writeJavaFile(filePath);
  }

}
