package org.jxapi.generator.java.pojo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ExchangeGeneratorMain;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.PojosDescriptor;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.pojo.parser.PojosDescriptorParseUtil;
import org.jxapi.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class to generate all POJOs defined in POJOs descriptor files located in
 * the "src/main/resources/jxapi/pojos" folder of the current project.
 *
 */
public class PojosGeneratorMain {
  
  private static final Logger log = LoggerFactory.getLogger(PojosGeneratorMain.class);

  /**
   * Folder where exchange descriptor files are located in the wrapper project.
   */
  public static final Path DESCRIPTOR_FOLDER = Paths.get("src", "main", "resources", "jxapi", "pojos");

  /**
   * Key for system property that can be passed to JVM running
   * {@link #main(String[])} to specify the base URL for Java module project to
   * use by generator. This should be the cuurrent project folder path. Can be
   * retrieved using maven property ${project.basedir}. If not set, the current
   * JVM folder is used.
   */
  public static final String BASE_PROJECT_DIR_PROP = "baseProjectDir";
  
  /**
   * Key for system property that can be passed to JVM running
   * {@link #main(String[])} to specify the source directory relative to the base
   * project folder where to generate the POJOs. If not set, "src/main/java" is
   * used.
   */
  public static final String GENERATED_SOURCES_FOLDER_PROP = "srcDirectory";

  /**
   * Main entry point to generate exchange API wrappers for all exchange
   * descriptor files in the current project "src/main/resources/" folder.
   * 
   * @param args Not used
   */
  public static void main(String[] args) {
    try {
      String baseProjectDir = System.getProperty(BASE_PROJECT_DIR_PROP);
      String srcDirectory = System.getProperty(GENERATED_SOURCES_FOLDER_PROP);
      generatePojosInCurrentProject(baseProjectDir, srcDirectory);
    } catch (Throwable t) {
      log.error("Error from " + ExchangeGeneratorMain.class.getName() + ".main", t);
      System.exit(-1);
    }
    System.exit(0);
  }

  /**
   * Generates exchange API wrappers for all exchange descriptor files in the
   * specified project "src/main/resources/jxapi/pojos" folder.
   * 
   * @param baseProjectDir the base folder of the project where to generate the
   *                       exchange API wrappers. If null, current folder (".") is
   *                       used.
   * @param srcDirectory   the source directory relative to the base project                       
   * @throws Exception if an error occurs
   */
  public static final void  generatePojosInCurrentProject(String baseProjectDir, String srcDirectory) throws Exception {
    baseProjectDir = Optional.ofNullable(baseProjectDir).orElse(".");
    Path projectFolder = Paths.get(baseProjectDir);
    Path srcDir = Optional
        .ofNullable(srcDirectory)
          .map(dir -> Paths.get(dir))
        .orElse(ExchangeGeneratorMain.DEFAULT_GENERATED_SOURCES_FOLDER);
    Path resources = projectFolder.resolve(DESCRIPTOR_FOLDER);
    if (!Files.exists(resources)) {
      resources.toFile().mkdirs();
      Files.writeString(resources.resolve("README.txt"), 
          "JXAPI POJOs descriptor files (.json or .yaml) should be written in this folder.");
      return;
    }
    log.info("Generating exchange API wrapper and demos for all exchange descriptor files in {}", resources.toAbsolutePath());
    AtomicReference<Exception> error = new AtomicReference<>();
    PojosDescriptorParseUtil.collectAndMergePojosDescriptors(resources)
        .forEach(pojosDescriptor -> {
          try {
            generatePojos(pojosDescriptor, srcDir);
          } catch (Exception ex) {
            // Remark: intentionally catching all exceptions to continue generation for other exchanges
            log.error("Error while generating POJOs with base package:{} : {}" ,
                pojosDescriptor.getBasePackage(),
                 ex.getMessage(), 
                 ex);
            error.set(ex);
          }
        });
    Exception ex = error.get();
    if (ex != null) {
      throw ex;
    }
    
  }
  
  /**
   * Generates all POJOs defined in the specified POJOs descriptor into the
   * specified base package folder.
   * 
   * @param pojosDescriptor   the POJOs descriptor defining the POJOs to generate
   * @param srcMainFolder     the base package folder where to generate the POJOs
   * @throws IOException if an I/O error occurs
   */
  public static void generatePojos(PojosDescriptor pojosDescriptor, Path srcMainFolder) throws IOException {
    Path mainPackagePath = Paths.get(pojosDescriptor.getBasePackage().replace('.', '/'));
    Path genPackagesFolder = srcMainFolder.resolve(mainPackagePath);
    JavaCodeGenUtil.deletePath(genPackagesFolder);
    log.info("Generating pojos with base package :{} in {}", 
        pojosDescriptor.getBasePackage(), 
        genPackagesFolder.toAbsolutePath());
    
    for (Field pojoDescriptor : pojosDescriptor.getPojos()) {
      generateClassesForPojo(pojoDescriptor, pojosDescriptor.getBasePackage(), srcMainFolder);
    }
  }
  
  private static void generateClassesForPojo(Field pojoDescriptor, String basePackage, Path basePackageFolder) throws IOException {
    Type type = PojoGenUtil.getFieldType(pojoDescriptor);
    if (!type.isObject()) {
      throw new IllegalArgumentException("POJO descriptor must be of type 'object'. POJO name:" + pojoDescriptor.getName());
    }
    String objectClassName = PojoGenUtil.getFieldObjectClassName(
        pojoDescriptor,
        basePackage + ".");
      
    List<Field> properties = CollectionUtil.emptyIfNull(pojoDescriptor.getProperties());
    new PojoClassesGenerator(
          objectClassName, 
          pojoDescriptor.getDescription(), 
          properties,
          pojoDescriptor.getImplementedInterfaces(),
          null,
          null).generateClasses(basePackageFolder);
            
    new JsonPojoSerializerClassesGenerator( 
          objectClassName,
          properties).generateClasses(basePackageFolder);
          
    new JsonMessageDeserializerClassesGenerator(
          objectClassName, 
          properties).generateClasses(basePackageFolder);
  }
}
