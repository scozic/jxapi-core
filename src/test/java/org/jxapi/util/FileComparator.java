package org.jxapi.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.Assert;

/**
 * Test tool that compares two files that can be either text files or folders
 * and throws {@link AssertionError} if they are different.
 * <ul>
 * <li>In case of folders, the comparison is performed recursively on all files
 * and subfolders. Expected and actual folders are expected to carry same files
 * with same content</li>
 * <li>In case of text files, the comparison between text file content is
 * performed.
 * </ul>
 */
public class FileComparator {
  
  /**
   * Compares two files or folders and throws {@link AssertionError} if they are
   * different.
   * 
   * @param expected expected file or folder
   * @param actual   actual file or folder
   * @throws AssertionError if they are different
   * @see #compare()
   */
  public static void checkSameFiles(Path expected, Path actual) {
    new FileComparator(expected, actual).compare();
  }

  private final Path expectedBase;
  private final Path actualBase;
  
  /**
   * Constructor that initializes the comparator with expected and actual files or folders.
   * @param expected expected file or folder
   * @param actual   actual file or folder
   */
  public FileComparator(Path expected, Path actual) {
    this.expectedBase = expected;
    this.actualBase = actual;
  }
  

  /**
   * Compares the content of the expected and actual files or folders.
   * 
   * @throws AssertionError if they are different, this means both expected and
   *                        actual are folders and have the same files with same
   *                        content
   */
  public void compare() {
    final Set<Path> expectedFilesInActual = new TreeSet<>();
    visitPath(expectedBase, p -> {
       Path relative = expectedBase.relativize(p);
       Path actual = actualBase.resolve(relative);
       expectedFilesInActual.add(actual);
       compare(p, actual);
     });
    
    visitPath(actualBase, p -> {
            if (!expectedFilesInActual.contains(p)) {
                throw new AssertionError("Unexpected file/folder in actual:" + p);
            }
    });
  }
  
  private void visitPath(Path path, Consumer<Path> visitor) {
    try (Stream<Path> paths = Files.walk(path)) {
      paths.forEach(visitor::accept);
    } catch (IOException e) {
      throw new AssertionError("Error visiting path:" + path.toAbsolutePath(), e);
    }
  }
  
  private void compare(Path expected, Path actual) {
    if (!actual.toFile().exists()) {
      throw new AssertionError("Actual file/folder does not exist:" + actual);
    }
    
    if (expected.toFile().isDirectory()) {
      Assert.assertTrue("File is not a directory:" + expectedBase, 
                expectedBase.toFile().isDirectory());
    } else {
      compareFiles(expected, actual);
    }
  }


  private void compareFiles(Path expected, Path actual) {
    Assert.assertTrue("Expected file does not exist:" + expected, expected.toFile().exists());
    Assert.assertTrue("Actual file does not exist:" + actual, actual.toFile().exists());
    try {
            String expectedContent = new String(Files.readAllBytes(expected));
            String actualContent = new String(Files.readAllBytes(actual));
            Assert.assertEquals("File content is different:" + expected, expectedContent, actualContent);
        } catch (IOException e) {
            throw new AssertionError("Error comparing files:" + expected + " and " + actual, e);
        }
    
  }
  
}
