package org.jxapi.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.generator.java.exchange.ClassesGeneratorTestUtil;

/**
 * Unit test for {@link FileComparator}
 */
public class FileComparatorTest {

  private Path expectedBase;
  private Path actualBase;
  private int counter;
  
  @Before
  public void setUp() throws IOException {
    counter = 0;
    expectedBase = ClassesGeneratorTestUtil.generateTmpDir();
    actualBase = ClassesGeneratorTestUtil.generateTmpDir();
  }
  
  @After
  public void tearDown() throws IOException {
    if (expectedBase != null) {
      JavaCodeGenUtil.deletePath(expectedBase);
      expectedBase = null;
    }
    if (actualBase != null) {
      JavaCodeGenUtil.deletePath(actualBase);
      actualBase = null;
    }
  }
  
  @Test
  public void testCompareIdenticalTextFiles() throws IOException {
    String content = generateRandomText();
    createFile(expectedBase, "file1.txt", content);
    createFile(actualBase, "file1.txt", content);
    testCompareOk();
  }
  
  @Test
  public void testCompareDifferentTextFiles() throws IOException {
    String content1 = generateRandomText();
    String content2 = generateRandomText();
    createFile(expectedBase, "file1.txt", content1);
    createFile(actualBase, "file1.txt", content2);
    testCompareError();
  }
  
  @Test
  public void testCompareIdenticalEmptyFolders() throws IOException {
    createFolder(expectedBase, "folder1");
    createFolder(actualBase, "folder1");
    testCompareOk();
  }
  
  @Test
  public void testCompareIdenticalFoldersContainingSubFoldersAndTextFiles() throws IOException {
    createFolder(expectedBase, "folder1");
        createFolder(expectedBase, "folder1/subfolder1");
        createFolder(expectedBase, "folder1/subfolder2");
        String randomText1 = generateRandomText();
        createFile(expectedBase, "folder1/file1.txt", randomText1);
        String randomText2 = generateRandomText();
        createFile(expectedBase, "folder1/subfolder1/file2.txt", randomText2);
        String randomText3 = generateRandomText();
        createFile(expectedBase, "folder1/subfolder2/file3.txt", randomText3);
        
        createFolder(actualBase, "folder1");
        createFolder(actualBase, "folder1/subfolder1");
        createFolder(actualBase, "folder1/subfolder2");
        createFile(actualBase, "folder1/file1.txt", randomText1);
        createFile(actualBase, "folder1/subfolder1/file2.txt", randomText2);
        createFile(actualBase, "folder1/subfolder2/file3.txt", randomText3);
        testCompareOk();
    }
  
  @Test
  public void testCompareIdenticalFolders_OneFileHasUnexpectedContentInActual() throws IOException {
    createFolder(expectedBase, "folder1");
        createFolder(expectedBase, "folder1/subfolder1");
        createFolder(expectedBase, "folder1/subfolder2");
        String randomText1 = generateRandomText();
        createFile(expectedBase, "folder1/file1.txt", randomText1);
        String randomText2 = generateRandomText();
        createFile(expectedBase, "folder1/subfolder1/file2.txt", randomText2);
        String randomText3 = generateRandomText();
        createFile(expectedBase, "folder1/subfolder2/file3.txt", randomText3);
        
        createFolder(actualBase, "folder1");
        createFolder(actualBase, "folder1/subfolder1");
        createFolder(actualBase, "folder1/subfolder2");
        createFile(actualBase, "folder1/file1.txt", randomText1);
        createFile(actualBase, "folder1/subfolder1/file2.txt", randomText2);
        createFile(actualBase, "folder1/subfolder2/file3.txt", generateRandomText());
        testCompareError();
    }
  
  @Test
  public void testCompareFolders_OneFileIsMissingInActualFolder() throws IOException {
    createFolder(expectedBase, "folder1");
        createFolder(expectedBase, "folder1/subfolder1");
        createFolder(expectedBase, "folder1/subfolder2");
        String randomText1 = generateRandomText();
        createFile(expectedBase, "folder1/file1.txt", randomText1);
        String randomText2 = generateRandomText();
        createFile(expectedBase, "folder1/subfolder1/file2.txt", randomText2);
        String randomText3 = generateRandomText();
        createFile(expectedBase, "folder1/subfolder2/file3.txt", randomText3);
        
        createFolder(actualBase, "folder1");
        createFolder(actualBase, "folder1/subfolder1");
        createFile(actualBase, "folder1/subfolder1/file2.txt", randomText2);
        createFolder(actualBase, "folder1/subfolder2");
        createFile(actualBase, "folder1/subfolder2/file3.txt", randomText3);
        testCompareError();
  }
  
  @Test
  public void testCompareFolders_OneUnexpectedAdditionnalFileInActualFolder() throws IOException {
    createFolder(expectedBase, "folder1");
    createFolder(expectedBase, "folder1/subfolder1");
    createFolder(expectedBase, "folder1/subfolder2");
    String randomText1 = generateRandomText();
    createFile(expectedBase, "folder1/file1.txt", randomText1);
    String randomText2 = generateRandomText();
    createFile(expectedBase, "folder1/subfolder1/file2.txt", randomText2);
    String randomText3 = generateRandomText();
    createFile(expectedBase, "folder1/subfolder2/file3.txt", randomText3);

    createFolder(actualBase, "folder1");
    createFolder(actualBase, "folder1/subfolder1");
    createFolder(actualBase, "folder1/subfolder2");
    createFile(actualBase, "folder1/file1.txt", randomText1);
    createFile(actualBase, "folder1/subfolder1/file2.txt", randomText2);
    createFile(actualBase, "folder1/subfolder2/file3.txt", randomText3);
    createFile(actualBase, "folder1/subfolder2/file4.txt", generateRandomText());
    testCompareError();
  }
  
  @Test
  public void testCompare_ActualDoesNotExist() throws IOException {
    JavaCodeGenUtil.deletePath(actualBase);
    testCompareError();
  }
  
  private void testCompareOk() {
    FileComparator.checkSameFiles(expectedBase, actualBase);
  }
  
  private void testCompareError() {
    AssertionError expectedError = null;
        try {
          FileComparator.checkSameFiles(expectedBase, actualBase);
        } catch (AssertionError e) {
            expectedError = e;
        }
        Assert.assertNotNull("Expected AssertionError", expectedError);
  }
  
  private void createFile(Path base, String name, String content) throws IOException {
    Path file = base.resolve(name);
    Files.write(file, content.getBytes());
  }
  
  private void createFolder(Path base, String name) throws IOException {
    Path folder = base.resolve(name);
    Files.createDirectories(folder);
  }
  
  private String generateRandomText() {
    return "Random text " + counter++;
  }
}
