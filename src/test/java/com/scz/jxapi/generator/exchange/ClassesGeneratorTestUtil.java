package com.scz.jxapi.generator.exchange;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scz.jxapi.exchange.descriptor.ExchangeApiDescriptor;
import com.scz.jxapi.exchange.descriptor.RestEndpointDescriptor;
import com.scz.jxapi.exchange.descriptor.WebsocketEndpointDescriptor;

/**
 * Helper methods around .java files generator tests.
 * @see ClassesGenerator
 */
public class ClassesGeneratorTestUtil {
	
	private static final Logger log = LoggerFactory.getLogger(ClassesGeneratorTestUtil.class);

	public static void checkJavaFilesCount(Path folderPath, int count) throws IOException {
		File folder = folderPath.toFile();
		if (!folder.exists()) {
			Assert.assertEquals("Expected " + count + " files but folder does nto exist:" + folderPath, count, 0);
		} else {
			Assert.assertTrue(folderPath + "is not a directory", folder.isDirectory());
			Assert.assertEquals("Expected " + count + " files in " + folder.getAbsolutePath() 
									+ ", but got:" + Arrays.toString(folder.listFiles()),
								 count,	
								 folder.listFiles().length);
		}
		
	}
	
	public static Path checkSourceFileExists(Path pkg, Path srcFilePath) {
		Path fullPath = pkg.resolve(srcFilePath);
		Assert.assertTrue("File " + fullPath + " does not exists", fullPath.toFile().exists());
		return fullPath;
	}
	
	public static synchronized Path generateTmpDir() {
		Path p = Paths.get("tmp" 
							+ new SimpleDateFormat("yyyyMMdd-HHmmss-SSS").format(new Date())
							+ (RandomUtils.nextInt() % 1000));
		if (p.toFile().exists()) {
			long delay = 1;
			log.warn("Path " + p.toFile().getAbsolutePath() 
						+ " already exists, waiting " + delay 
						+ "ms before retrying generation of tmp dir");
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				log.warn("Interrupted", e);
			}
			return generateTmpDir();
		}
		return p;
	}
	
	public static RestEndpointDescriptor findRestEndpointByName(String name, ExchangeApiDescriptor exchangeDescriptor) {
		for (RestEndpointDescriptor api: exchangeDescriptor.getRestEndpoints()) {
			if (api.getName().equals(name)) {
				return api;
			}
		}
		throw new AssertionError("No such API:" + name + " in:" + exchangeDescriptor);
	}
	
	public static WebsocketEndpointDescriptor findWebsocketEndpointByName(String name, ExchangeApiDescriptor exchangeDescriptor) {
		for (WebsocketEndpointDescriptor api: exchangeDescriptor.getWebsocketEndpoints()) {
			if (api.getName().equals(name)) {
				return api;
			}
		}
		throw new AssertionError("No such API:" + name + " in:" + exchangeDescriptor);
	}
}
