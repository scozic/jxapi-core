package org.jxapi.generator.html;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.util.CollectionUtil;
import org.springframework.util.CollectionUtils;

/**
 * Helper methods around HTML generation
 */
public class HtmlGenUtil {
  
  /**
   * Indentation string used in HTML generation
   */
  public static final String INDENTATION = "  ";

  private HtmlGenUtil() {}
  
  /**
   * Generates a HTML table with given caption, header and rows
   * @param caption HTML table <code>&lt;caption&gt;</code> content
   * @param columns Column names nested in <code>&lt;th&gt;</code>
   * @param cells List of rows containing values for cells
   * @return HTML <code>&lt;table&gt;</code> code fragment.
   */
  public static String generateTable(String caption, List<String> columns, List<List<String>> cells) {
    XmlElement.Builder tableBuilder = XmlElement.builder().tag("table");
    if (!StringUtils.isBlank(caption)) {
      tableBuilder.child(XmlElement.builder().tag("caption").content(caption).build());
    }
        
    if (!CollectionUtil.isEmpty(columns)) {
      tableBuilder.child(XmlElement.builder().tag("tr")
          .children(columns.stream()
              .map(c -> XmlElement.builder()
                .tag("th")
                .content(c)
                .build())
              .toList())
          .build());
    }
    if (!CollectionUtils.isEmpty(cells)) {
      cells.forEach(row -> {
        XmlElement.Builder rowBuilder = XmlElement.builder().tag("tr");
        if (!CollectionUtil.isEmpty(row)) {
          rowBuilder.children(
              row.stream()
                .map(c -> XmlElement.builder()
                  .tag("td")
                  .content(c)
                  .build())
                .toList());
        }
        tableBuilder.child(rowBuilder.build());
      });
    }
    return generateHtmlForElement(tableBuilder.build());
  }
  
  /**
   * Generates HTML markers for given string and marker name
   * <p>Example:</p>
   * <pre>
   * &lt;marker&gt;string&lt;/marker&gt;
   * </pre>
   * 
   * @param string String to be marked
   * @param marker Marker name
   * @return String with HTML markers
   */
  public static String generateMarkers(String string, String marker) {
    if (StringUtils.isBlank(marker)) {
      throw new IllegalArgumentException("Marker cannot be blank or null");
    }
    return new StringBuilder()
        .append("<")
        .append(marker)
        .append(">")
        .append(StringUtils.defaultString(string))
        .append("</")
        .append(marker)
        .append(">")
        .toString();
  }
  
  /**
   * Generates HTML for the given XML element.
   * 
   * @param htmlElement The XML element to generate HTML for.
   * @return A String containing the generated HTML.
   */
  public static String generateHtmlForElement(XmlElement htmlElement) {
    StringBuilder sb = new StringBuilder();
    sb.append("<")
      .append(htmlElement.getTag());
    if (!CollectionUtil.isEmptyMap(htmlElement.getAttributes())) {
      htmlElement.getAttributes().forEach((key, value) -> 
        sb.append(" ")
          .append(key)
          .append("=\"")
          .append(value)
          .append("\"")
      );
    }
    sb.append(">");
    sb.append(StringUtils.defaultString(htmlElement.getContent()));
    if (!CollectionUtils.isEmpty(htmlElement.getChildren())) {
      htmlElement.getChildren().forEach(child -> {
        sb.append("\n");
        sb.append(JavaCodeGenUtil.indent(generateHtmlForElement(child), INDENTATION));
      });
      sb.append("\n");
    }
    sb.append("</")
      .append(htmlElement.getTag())
      .append(">");
    return sb.toString();
  }

}
