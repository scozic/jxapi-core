package com.scz.jxapi.generator.java.exchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
import com.scz.jxapi.generator.properties.ExchangeDemoPropertiesFileGenerator;
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
	 * documentation
	 */
	public static final String BASE_JAVADOC_URL_SYS_PROP = "baseJavaDocUrl";
	
	/**
	 * Key for system property that can be passed to JVM running
	 * {@link #main(String[])} to specify the base URL for Java wrapper project
	 * sources
	 */
	public static final String BASE_SRC_URL_SYS_PROP = "baseSrcUrl";
	
	/**
	 * @param args 1 argument expected : Name of exchange JSON descriptor file located in src/main/resources.
	 */
	public static void main(String[] args) {
		try {
			final AtomicInteger exitCode = new AtomicInteger(0);
			Path projectFolder = Paths.get(".");
			String baseJavadocUrl = System.getProperty(BASE_JAVADOC_URL_SYS_PROP);
			String baseSrcUrl = System.getProperty(BASE_SRC_URL_SYS_PROP);
			Path resources = Paths.get(".", "src", "main", "resources");
			Files.walk(resources)
				 .filter(p -> p.toFile().getName().endsWith("Descriptor.json"))
				 .forEach(path -> {
				try {
					generateExchangeWrapperAndDemos(path, projectFolder, baseJavadocUrl, baseSrcUrl);
				} catch (Exception ex) {
					log.error("Error while generating exchange descriptor for file:" + path.getFileName(), ex);
					exitCode.set(-1);
				}
			});
			System.exit(exitCode.get());
		} catch (Throwable t) {
			log.error("Error from " + ExchangeGeneratorMain.class.getName() + ".main", t);
			System.exit(-1);
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
		Path outputSrcTestFolder = Paths.get(".", "src", "test", "java");
		Path genTestPackagesFolder = outputSrcTestFolder.resolve(mainPackagePath);
		JavaCodeGenerationUtil.deletePath(genTestPackagesFolder);
		generateExchangeWrapperDemos(exchangeDescriptor, outputSrcTestFolder);
		
		Path srcTestResourcesFolder = Paths.get(".", "src", "test", "resources");
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
	
	public static void generateDemoPropertiesFileTemplate(ExchangeDescriptor exchangeDescriptor, Path resourcesFolder) throws IOException {
		String fileName = DemoUtil.getDefaultDemoExchangePropertiesFileName(exchangeDescriptor.getName()) + ".dist";
		Path filePath = resourcesFolder.resolve(Paths.get(fileName));
		log.info("Generating demo exchange properties template file:{}", filePath);
		List<ConfigProperty> configProperties = new ArrayList<>();
		configProperties.addAll(exchangeDescriptor.getProperties());
		new ExchangeDemoPropertiesFileGenerator(exchangeDescriptor.getName(), 
												configProperties)
			.writeJavaFile(filePath);
	}
	

}
