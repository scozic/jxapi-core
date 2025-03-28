package org.jxapi.generator.html;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link HtmlGenerationUtil}
 */
public class HtmlGenerationUtilTest {

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
          + "</table>\n", 
          HtmlGenerationUtil.generateTable("myTable", cols, cells));    
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
          + "</table>\n", 
          HtmlGenerationUtil.generateTable(null, null, cells));    
  }
  
  @Test
  public void testGenerateOnlyHeaderTable() {
    List<String> cols = List.of("col1", "col2");
      Assert.assertEquals("<table>\n"
          + "  <tr>\n"
          + "    <th>col1</th>\n"
          + "    <th>col2</th>\n"
          + "  </tr>\n"
          + "</table>\n", 
          HtmlGenerationUtil.generateTable(null, cols, null));    
  }
  
  @Test
  public void testGenerateEmptyRowTable() {
    List<String> cols = List.of("col1", "col2");
    List<List<String>> cells = List.of(
      List.of(),
      List.of("val10", "val11")
    );
      Assert.assertEquals("<table>\n"
          + "  <tr>\n"
          + "    <th>col1</th>\n"
          + "    <th>col2</th>\n"
          + "  </tr>\n"
          + "  <tr>\n"
          + "  </tr>\n"
          + "  <tr>\n"
          + "    <td>val10</td>\n"
          + "    <td>val11</td>\n"
          + "  </tr>\n"
          + "</table>\n", 
          HtmlGenerationUtil.generateTable(null, cols, cells));    
  }

}
