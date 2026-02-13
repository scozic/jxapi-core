package org.jxapi.generator.md.exchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class to insert a summary into an existing Markdown file using {@link ExchangeReadmeMdGeneratorUtil#generateTableOfContent(String)}
 */
public class WriteMdFileSummaryMain {

  private static final Logger log = LoggerFactory.getLogger(WriteMdFileSummaryMain.class);
  
  /**
   * Main method to run the Markdown file summary generation. Expects a single
   * command line argument which is the path to the Markdown file or a folder
   * containing markdown files to visit recursively. This markdown file should
   * exist and contain the placeholders
   * {@link ExchangeReadmeMdGeneratorUtil#BEGIN_TABLE_OF_CONTENTS_PLACEHOLDER}
   * and {@link ExchangeReadmeMdGeneratorUtil#END_TABLE_OF_CONTENTS_PLACEHOLDER}. 
   * The exissting content between these placeholders will be replaced with the
   * table of contents generated from the Markdown file's headers. It can be used
   * to update the table of contents in a Markdown file or files recursively in a
   * directory.
   * 
   * @param args Command line arguments, expects one argument: path to the
   *             Markdown file or a directory containing Markdown files.
   */
  public static void main(String[] args) {
    try {
      if (args.length != 1) {
        throw new IllegalArgumentException("Usage: java WriteMdFileSummaryMain <path-to-md-file>");
      }
      Path p = Path.of(args[0]);
      if (!Files.exists(p)) {
        throw new IllegalArgumentException("File does not exist: " + p);
      }
      
      writeSummaryToFile(p);
      
    } catch (Throwable t) {
      log.error("Error occurred while writing summary Markdown file", t);
      System.exit(1);
    }
  }

  public static void writeSummaryToFile(Path p) throws IOException {
    if (!Files.exists(p)) {
      throw new IllegalArgumentException("File does not exist: " + p);
    }
    if (Files.isDirectory(p)) {
      try (Stream<Path> paths = Files.walk(p)) {
          for (Path f : paths
                  .filter(Files::isRegularFile)
                  .toList()) {
              WriteMdFileSummaryMain.writeSummaryToFile(f);
          }
      }
    } else if (isMarkdownFile(p)) {
      Files.writeString(p , ExchangeReadmeMdGeneratorUtil.generateTableOfContent(Files.readString(p)));
    }
  }
  
  private static boolean isMarkdownFile(Path p) {
    return p.toString().endsWith(".md");
  }

}
