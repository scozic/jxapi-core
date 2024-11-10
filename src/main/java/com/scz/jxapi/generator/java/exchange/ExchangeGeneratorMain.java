package com.scz.jxapi.generator.java.exchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.parser.ExchangeDescriptorParser;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.exchange.api.ExchangeApiClassesGenerator;
import com.scz.jxapi.generator.java.exchange.api.demo.ExchangeDemoClassesGenerator;
import com.scz.jxapi.netutils.rest.ratelimits.RateLimitManager;

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
	 * @param args 1 argument expected : Name of exchange JSON descriptor file located in src/main/resources.
	 */
	public static void main(String[] args) {
		try {
			final AtomicInteger exitCode = new AtomicInteger(0);
			Path resources = Paths.get(".", "src", "main", "resources");
			Files.walk(resources)
				 .filter(p -> p.toFile().getName().endsWith("Descriptor.json"))
				 .forEach(path -> {
				try {
					generateExchangeWrapperAndDemos(path);
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
	 * Generate exchange API wrapper and demo snippets code for a given JSON descriptor file.
	 * @param jsonFile the JSON descriptor file
	 * @throws IOException if an I/O error occurs
	 */
	public static void generateExchangeWrapperAndDemos(Path jsonFile) throws IOException {
		if (log.isInfoEnabled())
			log.info("Generating exchange wrapper code for descriptor:{}", jsonFile.getFileName());
		ExchangeDescriptor exchangeDescriptor = new ExchangeDescriptorParser().fromJson(jsonFile);
		Path outputSrcMainFolder = Paths.get(".", "src", "main", "java");
		generateExchangeWrapper(exchangeDescriptor, outputSrcMainFolder);
		
		if (log.isInfoEnabled())
			log.info("Generating exchange demos code for descriptor:{}", jsonFile.getFileName());
		Path outputSrcTestFolder = Paths.get(".", "src", "test", "java");
		generateExchangeWrapperDemos(exchangeDescriptor, outputSrcTestFolder);
		if (log.isInfoEnabled()) {
			log.info("Done generating java code for {}", jsonFile.getFileName());
		}
	}
	
	/**
	 * Generates exchange API wrapper (without demo snippets) for given exchange descriptor
	 * @param exchangeDescriptor The exchange descriptor to generate Java wrapper for all APIs of
	 * @param srcFolder The src%2Fmain%2Fpackage of wrapper project
	 * @throws IOException If error occurs during generation
	 */
	public static void generateExchangeWrapper(ExchangeDescriptor exchangeDescriptor, Path srcFolder) throws IOException {
		Path packagePath = Paths.get(StringUtils.replace(exchangeDescriptor.getBasePackage(), ".", "/"));
		Path genPackagesFolder = srcFolder.resolve(packagePath);
		JavaCodeGenerationUtil.deletePath(genPackagesFolder);
		new ExchangeClassesGenerator(exchangeDescriptor).generateClasses(srcFolder);
	}
	
	/**
	 * Generates exchange API wrapper (without demo snippets) for given exchange descriptor
	 * @param exchangeDescriptor The exchange descriptor to generate Java wrapper for all APIs of
	 * @param srcFolder The src%2Fmain%2Fpackage of wrapper project
	 * @throws IOException If error occurs during generation
	 */
	public static void generateExchangeWrapperDemos(ExchangeDescriptor exchangeDescriptor, Path srcFolder) throws IOException {
		Path packagePath = Paths.get(StringUtils.replace(exchangeDescriptor.getBasePackage(), ".", "/"));
		Path genTestPackagesFolder = srcFolder.resolve(packagePath);
		JavaCodeGenerationUtil.deletePath(genTestPackagesFolder);
		new ExchangeDemoClassesGenerator(exchangeDescriptor).generateClasses(srcFolder);
	}
	

}
