package org.jxapi.generator.html;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link HtmlGenUtil}
 */
public class HtmlGenUtilTest {

  @Test
  public void testGenerateMarkers() {
    String text = "Hello, World!";
    Assert.assertEquals("<span></span>", HtmlGenUtil.generateMarkers(null, "span"));
    Assert.assertEquals("<span>Hello, World!</span>", HtmlGenUtil.generateMarkers(text, "span"));
  }
  
  @Test
  public void testGenerateHtmlForElement() {
    XmlElement element = XmlElement.builder()
        .tag("div")
        .attr("class", "my-class")
        .attr("id", "my-id")
        .attr("style", "color: red;")
        .child(XmlElement.builder().tag("span").content("Child Element").build())
        .child(
          XmlElement.builder()
           .tag("table")
           .children(List.of(
             XmlElement.builder()
               .tag("tr")
               .children(List.of(
                 XmlElement.builder()
                   .tag("td")
                   .content("Cell 1")
                   .build(),
                 XmlElement.builder()
                   .tag("td")
                   .content("Cell 2")
                   .build()))
                .build(),
            XmlElement.builder()
              .tag("tr")
              .children(List.of(
                XmlElement.builder()
                  .tag("td")
                  .content("Cell 3")
                  .build(),
                XmlElement.builder()
                  .tag("td")
                  .content("Cell 4")
                  .build()))
            .build()))
          .build())
        .build();
    Assert.assertEquals("<div class=\"my-class\" id=\"my-id\" style=\"color: red;\">\n"
        + "  <span>Child Element</span>\n"
        + "  <table>\n"
        + "    <tr>\n"
        + "      <td>Cell 1</td>\n"
        + "      <td>Cell 2</td>\n"
        + "    </tr>\n"
        + "    <tr>\n"
        + "      <td>Cell 3</td>\n"
        + "      <td>Cell 4</td>\n"
        + "    </tr>\n"
        + "  </table>\n"
        + "</div>", HtmlGenUtil.generateHtmlForElement(element));
  }
  
  @Test
  public void testGenerateRegularTable() {
    List<String> cols = List.of("col1", "col2");
    List<List<String>> cells = List.of(
      List.of("val00", "val01"),
      List.of("val10", "val11")
    );
      Assert.assertEquals("<table>\n"
          + "  <caption>myTable</caption>\n"
          + "  <tr>\n"
          + "    <th>col1</th>\n"
          + "    <th>col2</th>\n"
          + "  </tr>\n"
          + "  <tr>\n"
          + "    <td>val00</td>\n"
          + "    <td>val01</td>\n"
          + "  </tr>\n"
          + "  <tr>\n"
          + "    <td>val10</td>\n"
          + "    <td>val11</td>\n"
          + "  </tr>\n"
          + "</table>", 
          HtmlGenUtil.generateTable("myTable", cols, cells));    
  }
  
  @Test
  public void testGenerateNoHeaderTable() {
    List<List<String>> cells = List.of(
      List.of("val00", "val01"),
      List.of("val10", "val11")
    );
      Assert.assertEquals("<table>\n"
          + "  <tr>\n"
          + "    <td>val00</td>\n"
          + "    <td>val01</td>\n"
          + "  </tr>\n"
          + "  <tr>\n"
          + "    <td>val10</td>\n"
          + "    <td>val11</td>\n"
          + "  </tr>\n"
          + "</table>", 
          HtmlGenUtil.generateTable(null, null, cells));    
  }
  
  @Test
  public void testGenerateOnlyHeaderTable() {
    List<String> cols = List.of("col1", "col2");
      Assert.assertEquals("<table>\n"
          + "  <tr>\n"
          + "    <th>col1</th>\n"
          + "    <th>col2</th>\n"
          + "  </tr>\n"
          + "</table>", 
          HtmlGenUtil.generateTable(null, cols, null));    
  }

}
