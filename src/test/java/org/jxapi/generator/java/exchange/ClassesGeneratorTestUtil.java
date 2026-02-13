package org.jxapi.generator.java.exchange;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.jxapi.exchange.descriptor.gen.ExchangeApiDescriptor;
import org.jxapi.exchange.descriptor.gen.RestEndpointDescriptor;
import org.jxapi.exchange.descriptor.gen.WebsocketEndpointDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper methods around .java files generator tests.
 * @see ClassesGenerator
 */
public class ClassesGeneratorTestUtil {
  
  private static final Logger log = LoggerFactory.getLogger(ClassesGeneratorTestUtil.class);

  public static void checkJavaFilesCount(Path folderPath, int count) {
    File folder = folderPath.toFile();
    if (!folder.exists()) {
      Assert.assertEquals("Expected " + count + " files but folder does nto exist:" + folder.getAbsolutePath(), 0, count);
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
  
  public static synchronized Path generateTmpDir() throws IOException {
    Path p = Paths.get("tmp" 
              + new SimpleDateFormat("yyyyMMdd-HHmmss-SSS").format(new Date())
              + (RandomUtils.secure().randomInt() % 1000));
    if (p.toFile().exists()) {
      long delay = 1;
      log.warn("Path {} already exists, waiting {}ms before retrying generation of tmp dir", 
           p.toFile().getAbsolutePath(), delay);
      try {
        Thread.sleep(delay);
      } catch (InterruptedException e) {
        log.warn("Interrupted", e);
      }
      return generateTmpDir();
    }
    Files.createDirectories(p);
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
