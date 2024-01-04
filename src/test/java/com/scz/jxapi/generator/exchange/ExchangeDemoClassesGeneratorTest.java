package com.scz.jxapi.generator.exchange;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.generator.JavaCodeGenerationUtil;

/**
 * Unit test for {@link ExchangeDemoClassesGenerator}
 */
public class ExchangeDemoClassesGeneratorTest {
	
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
		srcFolder = Paths.get("tmp" + Math.random());
		ExchangeDescriptor exchange = new ExchangeDescriptorParser().fromJson(Paths.get(".", "src", "test", "resources", "testCEXDescriptor.json"));
		new ExchangeDemoClassesGenerator(exchange).generateClasses(srcFolder);
		Path pkgPath = Paths.get(".");
		checkJavaFilesCount(pkgPath, 1);
		pkgPath = pkgPath.resolve("marketData");
		checkJavaFilesCount(pkgPath, 1);
		pkgPath = pkgPath.resolve("demo");
		checkJavaFilesCount(pkgPath, 3);
		checkSourceFileExists(Paths.get("MyTestCEXMarketDataExchangeInfoDemo.java"));
		checkSourceFileExists(Paths.get("MyTestCEXMarketDataTickersDemo.java"));
		checkSourceFileExists(Paths.get("MyTestCEXMarketDataTickerStreamDemo.java"));
	}
	
	private void checkJavaFilesCount(Path relativePkg, int count) throws IOException {
		Path pkg = srcFolder.resolve(Paths.get("com", "foo", "bar", "gen"));
		File folder = pkg.resolve(relativePkg).toFile(); 
		Assert.assertTrue(folder.exists());
		Assert.assertTrue(folder.isDirectory());
		Assert.assertEquals("Expected " + count + " files, but got:" + Arrays.toString(folder.listFiles()),
							 count,	
							 folder.listFiles().length);
	}
	
	private Path checkSourceFileExists(Path srcFilePath) {
		Path pkg = srcFolder.resolve(Paths.get("com", "foo", "bar", "gen", "marketdata", "demo"));
		Path fullPath = pkg.resolve(srcFilePath);
		Assert.assertTrue(fullPath.toFile().exists());
		return fullPath;
	}

}
