package com.scz.jxapi.generator.java.exchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.exchange.descriptor.ConfigProperty;
import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.exchange.api.ExchangeApiClassesGenerator;
import com.scz.jxapi.generator.java.exchange.api.demo.ExchangeDemoClassesGenerator;
import com.scz.jxapi.generator.md.exchange.ExchangeReadmeMdGenerator;
import com.scz.jxapi.generator.properties.exchange.ExchangeDemoPropertiesFileGenerator;
import com.scz.jxapi.netutils.rest.ratelimits.RateLimitManager;
import com.scz.jxapi.util.DemoUtil;

/**
 * Java main class that performs generation of exchange APIs for every file found with name ending with 'Descriptor.json' in 'src/main/resources/' folder. 
 * This is the main entry point to generate all exchange API classes, and target of the maven exec plugin in wrapper module pom.xml.
 * <p>
 * The generated code is written in 'src/main/java/' folder.
 * <p>
 * The generated code is based on the JSON descriptor file format defined in {@link ExchangeDescriptor}.
 * <p>
 * The generated code includes:
 * <ul>
 * <li>Java interface for the exchange with a getter method to retrieve each exchange API interface.
 * <li>Implementation of that exchange interface with eventual {@link RateLimitManager} to enforce rate limit rules if any.
 * <li>Java interface for each API listed in {@link ExchangeDescriptor} (see {@link ExchangeApiDescriptor} and generator for its classes {@link ExchangeApiClassesGenerator}), with all classes for every REST/Websocket endpoint.
 * <li>All demo classes for each API listed in {@link ExchangeDescriptor} (see {@link ExchangeApiDescriptor} see {@link ExchangeDemoClassesGenerator}).
 * </ul>
 * 
 */
public class ExchangeGeneratorMain {
	
	private static final Logger log = LoggerFactory.getLogger(ExchangeGeneratorMain.class);
	
	/**
	 * Key for system property that can be passed to JVM running
	 * {@link #main(String[])} to specify the base URL for Java wrapper project
	 *  to use by generator. This should be the cuurrent project folder path. Can be retrieved using maven property ${project.basedir}.
	 *  If not set, the current JVM folder is used.
	 */
	public static final String BASE_PROJECT_DIR_PROP = "baseProjectDir";
	
	/**
	 * Key for system property that can be passed to JVM running
	 * {@link #main(String[])} to specify the base URL for Java wrapper project
	 * documentation
	 */
	public static final String BASE_JAVADOC_URL_SYS_PROP = "baseJavaDocUrl";
	
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
	 * @throws Exception If error occurs during
	 * @see #generateExchangeWrappersInCurrentProject()
	 */
	public static void main(String[] args) {
		try {
			String baseProjectDir = System.getProperty(BASE_PROJECT_DIR_PROP);
			String baseJavaDocUrl = System.getProperty(BASE_JAVADOC_URL_SYS_PROP);
			String baseSrcUrl = System.getProperty(BASE_SRC_URL_SYS_PROP);
			generateExchangeWrappersInCurrentProject(baseProjectDir, baseJavaDocUrl, baseSrcUrl);
		} catch (Throwable t) {
			log.error("Error from " + ExchangeGeneratorMain.class.getName() + ".main", t);
			System.exit(-1);
		}
	}
	
	/**
	 * Generate exchange API wrappers for all exchange descriptor files in the
	 * current project "src/main/resources/" folder. Will walk through all files in
	 * the folder and generate Java code for each file ending with 'Descriptor.json'
	 * using {@link #generateExchangeWrapperAndDemos(Path, Path, String @throws
	 * Exception
	 * 
	 * @param baseProjectDir Base project directory where the generated code will be written
	 * @param baseJavaDocUrl The base url for project classes javadoc, used for links generation
	 * @param baseSrcUrl The base url for sources on public repo, used for links generation.
	 * 
	 * @throws Exception If error occurs during generation
	 * @see #generateExchangeWrapperAndDemos(Path, Path, String, String)
	 */
	public static final void generateExchangeWrappersInCurrentProject(String baseProjectDir, String baseJavaDocUrl, String baseSrcUrl) throws Exception {
		baseProjectDir = Optional.ofNullable(baseProjectDir).orElse(".");
		Path projectFolder = Paths.get(baseProjectDir);
		Path resources = projectFolder.resolve(Paths.get("src", "main", "resources"));
		log.info("Generating exchange API wrapper and demos for all exchange descriptor files in {}", resources.toAbsolutePath());
		AtomicReference<Exception> error = new AtomicReference<>();
		try (Stream<Path> stream = Files.walk(resources)
			 .filter(p -> p.toFile().getName().endsWith("Descriptor.json"))) {
			stream.forEach(path -> {
				try {
					generateExchangeWrapperAndDemos(path, projectFolder, baseJavaDocUrl, baseSrcUrl);
				} catch (Exception ex) {
					log.error("Error while generating exchange descriptor for file:" + path.getFileName() + ":" + ex.getMessage(), ex);
					error.set(ex);
				}
			});
		} catch (IOException e) {
            log.error("Error while visiting exchange descriptor files in current project {}", resources.toAbsolutePath() , e);
            error.set(e);
		}
		Exception ex = error.get();
		if (ex != null) {
			throw ex;
		}
	}
	
	/**
	 * Generate exchange API wrapper and demo snippets code for a given JSON
	 * descriptor file.
	 * 
	 * @param jsonFile       The JSON descriptor file
	 * @param projectFolder  Project folder, the <code>src/main/java/</code>,
	 *                       <code>src/test/java/</code> source folders and README.md
	 *                       sample will be generated here.
	 * @param baseJavaDocUrl The base url for project classes javadoc, used for links generation
	 * @param baseSrcUrl     The base url for sources on public repo, used for links generation.
	 * @throws IOException if an I/O error occurs
	 */
	public static void generateExchangeWrapperAndDemos(Path jsonFile,
													   Path projectFolder, 
													   String baseJavaDocUrl, 
													   String baseSrcUrl) throws IOException {
		log.info("Generating exchange wrapper code for descriptor:{}", jsonFile.getFileName());
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptorParser().fromJson(jsonFile);
		Path outputSrcMainFolder = projectFolder.resolve(Paths.get("src", "main", "java"));
		Path mainPackagePath = Paths.get(StringUtils.replace(exchangeDescriptor.getBasePackage(), ".", "/"));
		Path genMainPackagesFolder = outputSrcMainFolder.resolve(mainPackagePath);
		JavaCodeGenerationUtil.deletePath(genMainPackagesFolder);
		generateExchangeWrapper(exchangeDescriptor, outputSrcMainFolder);
		
		log.info("Generating exchange demos code for descriptor:{}", jsonFile.getFileName());
		Path outputSrcTestFolder = projectFolder.resolve(Paths.get("src", "test", "java"));
		Path genTestPackagesFolder = outputSrcTestFolder.resolve(mainPackagePath);
		JavaCodeGenerationUtil.deletePath(genTestPackagesFolder);
		generateExchangeWrapperDemos(exchangeDescriptor, outputSrcTestFolder);
		
		Path srcTestResourcesFolder = projectFolder.resolve(Paths.get("src", "test", "resources"));
		log.info("Generating demo exchange configuration properties file template in {}", srcTestResourcesFolder);
		generateDemoPropertiesFileTemplate(exchangeDescriptor, srcTestResourcesFolder);
		
		log.info("Generating exchange README for:{}", jsonFile.getFileName());
		generateExchangeWrapperReadme(exchangeDescriptor, projectFolder, baseJavaDocUrl, baseSrcUrl);
		
		if (log.isInfoEnabled()) {
			log.info("Done generating java code for {}", jsonFile.getFileName());
		}
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
		new ExchangeReadmeMdGenerator(exchangeDescriptor, baseJavaDocUrl, baseSrcUrl).writeJavaFile(projectFolder);
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
	 * Generates exchange API wrapper (without demo snippets) for given exchange descriptor
	 * @param exchangeDescriptor The exchange descriptor to generate Java wrapper for all APIs of
	 * @param srcFolder The src%2Fmain%2Fpackage of wrapper project
	 * @throws IOException If error occurs during generation
	 */
	public static void generateExchangeWrapperDemos(ExchangeDescriptor exchangeDescriptor, Path srcFolder) throws IOException {
		new ExchangeDemoClassesGenerator(exchangeDescriptor).generateClasses(srcFolder);
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
		String fileName = DemoUtil.getDefaultDemoExchangePropertiesFileName(exchangeDescriptor.getName()) + ".dist";
		Files.createDirectories(resourcesFolder);
		Path filePath = resourcesFolder.resolve(Paths.get(fileName));
		log.info("Generating demo exchange properties template file:{}", filePath);
		List<ConfigProperty> configProperties = new ArrayList<>();
		configProperties.addAll(Optional.ofNullable(exchangeDescriptor.getProperties()).orElse(List.of()));
		new ExchangeDemoPropertiesFileGenerator(exchangeDescriptor.getName(), 
												configProperties)
			.writeJavaFile(filePath);
	}

}
