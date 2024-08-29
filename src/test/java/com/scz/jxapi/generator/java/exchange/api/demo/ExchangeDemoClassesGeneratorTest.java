package com.scz.jxapi.generator.java.exchange.api.demo;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptorParser;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.exchange.ClassesGeneratorTestUtil;

/**
 * Unit test for {@link ExchangeDemoClassesGenerator}
 */
public class ExchangeDemoClassesGeneratorTest {
	
	private static final Path BASE_PKG = Paths.get("com", "foo", "bar", "gen");
	
	private Path srcFolder;
	
	@After
	public void tearDown() throws IOException {
		if (srcFolder != null) {
			JavaCodeGenerationUtil.deletePath(srcFolder);
			srcFolder = null;
		}
	}

	@Test
	public void testGenerateExchangeDemoClasses() throws IOException {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
		new ExchangeDemoClassesGenerator(exchange).generateClasses(srcFolder);
		Path pkgPath = Paths.get(".");
		checkJavaFilesCount(pkgPath, 1);
		pkgPath = pkgPath.resolve("marketData");
		checkJavaFilesCount(pkgPath, 1);
		pkgPath = pkgPath.resolve("demo");
		checkJavaFilesCount(pkgPath, 3);
		checkSourceFileExists(Paths.get("MyTestExchangeMarketDataExchangeInfoDemo.java"));
		checkSourceFileExists(Paths.get("MyTestExchangeMarketDataTickersDemo.java"));
		checkSourceFileExists(Paths.get("MyTestExchangeMarketDataTickerStreamDemo.java"));
	}
	
	@Test
	public void testGenerateExchangeDemoClasses_NullRestEndpoints() throws IOException {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
		exchange.getApis().get(0).setRestEndpoints(null);
		new ExchangeDemoClassesGenerator(exchange).generateClasses(srcFolder);
		Path pkgPath = Paths.get(".");
		checkJavaFilesCount(pkgPath, 1);
		pkgPath = pkgPath.resolve("marketData");
		checkJavaFilesCount(pkgPath, 1);
		pkgPath = pkgPath.resolve("demo");
		checkJavaFilesCount(pkgPath, 1);
		checkSourceFileExists(Paths.get("MyTestExchangeMarketDataTickerStreamDemo.java"));
	}
	
	@Test
	public void testGenerateExchangeDemoClasses_NullWsEndpoints() throws IOException {
		srcFolder = ClassesGeneratorTestUtil.generateTmpDir();
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testExchangeDescriptor.json"));
		exchange.getApis().get(0).setWebsocketEndpoints(null);
		new ExchangeDemoClassesGenerator(exchange).generateClasses(srcFolder);
		Path pkgPath = Paths.get(".");
		checkJavaFilesCount(pkgPath, 1);
		pkgPath = pkgPath.resolve("marketData");
		checkJavaFilesCount(pkgPath, 1);
		pkgPath = pkgPath.resolve("demo");
		checkJavaFilesCount(pkgPath, 2);
		checkSourceFileExists(Paths.get("MyTestExchangeMarketDataExchangeInfoDemo.java"));
		checkSourceFileExists(Paths.get("MyTestExchangeMarketDataTickersDemo.java"));
	}
	
	private void checkJavaFilesCount(Path relativePkg, int count) throws IOException {
		ClassesGeneratorTestUtil.checkJavaFilesCount(srcFolder.resolve(BASE_PKG).resolve(relativePkg), count);
	}
	
	private Path checkSourceFileExists(Path srcFilePath) {
		return ClassesGeneratorTestUtil.checkSourceFileExists(srcFolder.
																resolve(BASE_PKG)
																.resolve("marketData")
																.resolve("demo"), 
															  srcFilePath);
	}

}
