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
   * exist and contain the placeholder
   * {@link ExchangeReadmeMdGeneratorUtil#TABLE_OF_CONTENTS_PLACEHOLDER} located
   * where the table of contents should be generated..
   * 
   * @param args Command line arguments, expects one argument: path to the
   *             Markdown file.
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
      
      try (Stream<Path> paths = Files.walk(p)) {
        paths.filter(Files::isRegularFile)
             .filter(f -> f.toString().endsWith(".md"))
             .forEach(WriteMdFileSummaryMain::writeSummaryToFile);
      }
      
    } catch (Throwable t) {
      log.error("Error occurred while writing summary Markdown file", t);
      System.exit(1);
    }
  }

  private static void writeSummaryToFile(Path p) {
    try {
      Files.writeString(p , ExchangeReadmeMdGeneratorUtil.generateTableOfContent(Files.readString(p)));
    } catch (IOException e) {
      log.error("Error occurred while writing summary Markdown file", e);
    }
  }

}
