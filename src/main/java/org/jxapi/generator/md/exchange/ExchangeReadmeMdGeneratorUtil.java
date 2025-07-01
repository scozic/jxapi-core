package org.jxapi.generator.md.exchange;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods class for generating Markdown content for Exchange Readme files
 */
public class ExchangeReadmeMdGeneratorUtil {
  
  private ExchangeReadmeMdGeneratorUtil() {}
  
  /**
   * Placeholder for the table of contents in Markdown files. This placeholder
   * will be replaced with the actual table of contents.
   */
  public static final String TABLE_OF_CONTENTS_PLACEHOLDER = "<!-- TABLE OF CONTENTS -->";

  /**
   * Generates the table of contents for a Markdown file based on its content.<br>
   * The table of contents will be inserted at the location of the 
   * {@link #TABLE_OF_CONTENTS_PLACEHOLDER}.
   * It will contain a bullet list of links to each header in the file.
   *
   * @param mdFileContent The content of the Markdown file as a String.
   * @return The Markdown content with the table of contents inserted.
   */
  public static String generateTableOfContent(String mdFileContent) {
    StringBuilder tableOfContents = new StringBuilder();
    for (String titleLine : findTitleLines(mdFileContent)) {
      tableOfContents.append(toTableContentHeader(titleLine)).append("\n");
    }

    return mdFileContent.replace(TABLE_OF_CONTENTS_PLACEHOLDER, tableOfContents.toString());
  }
  
  private static String toTableContentHeader(String headerLine) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < headerLine.length(); i++) {
      char c = headerLine.charAt(i);
      if ('#' == c) {
        sb.append("  ");
      } else if (c == ' ') {
        sb.append("- ");
        String headerText = headerLine.substring(i + 1).trim();
        sb.append(generateMarkdownLink(headerText, "#" + generateMarkdownAnchorFromHeader(headerText)));
        return sb.toString();
      } else {
        throw new IllegalArgumentException("Invalid header line: " + headerLine);
      }
    }
    throw new IllegalArgumentException("Invalid header line: " + headerLine);
  }
  
  /**
   * Generates a Markdown link with the given text and URL.
   *
   * @param text The text to display for the link.
   * @param url  The URL that the link points to.
   * @return A Markdown formatted link as a String.
   */
  public static String generateMarkdownLink(String text, String url) {
    return "[" + text + "](" + url + ")";
  }
  
  /**
   * Generates a Markdown anchor from a header line. The anchor will be in
   * lowercase and spaces will be replaced with hyphens.
   *
   * @param headerLine The header line to generate the anchor from.
   * @return A String representing the Markdown anchor.
   */
  public static String generateMarkdownAnchorFromHeader(String headerLine) {
    StringBuilder anchor = new StringBuilder();
    for (char c : headerLine.toCharArray()) {
      if (Character.isLetterOrDigit(c)) {
        anchor.append(Character.toLowerCase(c));
      } else if (c == ' ') {
        anchor.append('-');
      }
    }
    return anchor.toString();
  }
  
  /**
   * Finds all title lines in the Markdown file content. A title line is defined
   * as a line that starts with a '#' character.
   *
   * @param mdFileContent The content of the Markdown file as a String.
   * @return A list of title lines found in the Markdown content.
   */
  public static List<String> findTitleLines(String mdFileContent) {
    List<String> titleLines = new ArrayList<>();
    String[] lines = mdFileContent.split("\n");

    for (String line : lines) {
      if (line.startsWith("#")) {
        titleLines.add(line.trim());
      }
    }

    return titleLines;
  }

}
