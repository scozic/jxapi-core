package org.jxapi.generator.html;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.util.CollectionUtil;

/**
 * Helper methods around HTML generation
 */
public class HtmlGenerationUtil {
  
  /**
   * Indentation string used in HTML generation
   */
  public static final String INDENTATION = "  ";

  private HtmlGenerationUtil() {}
  
  /**
   * Generates a HTML table with given caption, header and rows
   * @param caption HTML table <code>&lt;caption&gt;</code> content
   * @param columns Column names nested in <code>&lt;th&gt;</code>
   * @param cells List of rows containing values for cells
   * @return HTML <code>&lt;table&gt;</code> code fragment.
   */
  public static String generateTable(String caption, List<String> columns, List<List<String>> cells) {
    HtmlElement.Builder tableBuilder = HtmlElement.builder().tag("table");
    if (!StringUtils.isBlank(caption)) {
      tableBuilder.child(HtmlElement.builder().tag("caption").content(caption).build());
    }
        
    if (!CollectionUtil.isEmpty(columns)) {
      tableBuilder.child(HtmlElement.builder().tag("tr")
          .children(columns.stream()
              .map(c -> HtmlElement.builder()
                .tag("th")
                .content(c)
                .build())
              .collect(Collectors.toList()))
          .build());
    }
    if (!CollectionUtils.isEmpty(cells)) {
      cells.forEach(row -> {
        HtmlElement.Builder rowBuilder = HtmlElement.builder().tag("tr");
        if (!CollectionUtil.isEmpty(row)) {
          rowBuilder.children(
              row.stream()
                .map(c -> HtmlElement.builder()
                  .tag("td")
                  .content(c)
                  .build())
                .collect(Collectors.toList()));
        }
        tableBuilder.child(rowBuilder.build());
      });
    }
    return generateHtmlForElement(tableBuilder.build());
  }
  
//  /**
//   * Generates a HTML table with given caption, header and rows
//   * @param caption HTML table <code>&lt;caption&gt;</code> content
//   * @param columns Column names nested in <code>&lt;th&gt;</code>
//   * @param cells List of rows containing values for cells
//   * @return HTML <code>&lt;table&gt;</code> code fragment.
//   */
//  public static String generateTable(String caption, List<HtmlElement> columns, List<List<HtmlElement>> cells) {
//    HtmlElement.Builder tableBuilder = HtmlElement.builder()
//        .tag("table")
//        .attribute("caption", caption);
//    if (!CollectionUtil.isEmpty(columns)) {
//      tableBuilder.child(HtmlElement.builder().tag("tr").children(columns).build());
//    }
//    
//    if (!CollectionUtils.isEmpty(cells)) {
//      cells.forEach(row -> {
//        HtmlElement.Builder rowBuilder = HtmlElement.builder().tag("tr");
//        if (!CollectionUtil.isEmpty(row)) {
//          rowBuilder.children(row);
//        }
//        tableBuilder.child(rowBuilder.build());
//      });
//    }
//    return generateHtmlForElement(tableBuilder.build());
//    
//  }
  
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
  
  public static String generateHtmlForElement(HtmlElement htmlElement) {
    StringBuilder sb = new StringBuilder();
    sb.append("<")
      .append(htmlElement.getTag());
    if (!CollectionUtil.isEmptyMap(htmlElement.getAttributes())) {
      htmlElement.getAttributes().forEach((key, value) -> {
        sb.append(" ").append(key).append("=\"").append(value).append("\"");
      });
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
