package org.jxapi.generator.properties.exchange;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.generator.md.exchange.ExchangeReadmeMdGeneratorUtil;

/**
 * Test class for {@link ExchangeReadmeMdGeneratorUtil}
 */
public class ExchangeReadmeMdGeneratorUtilTest {

  @Test
  public void testGenerateTableOfContent() {
    String mdFileContent = "# Main Title\n"
        + "Some blah\n"
        + "\nAnd the table of contents should be inserted here:\n"
        + "<!-- BEGIN TABLE OF CONTENTS -->\n"
        + " - Some existing table of contents\n"
        + "<!-- END TABLE OF CONTENTS -->\n"
        + "## Subtitle1@\n"
        + "Some more blah! for subtitle1\n"
        + "\n"
        + "## Sub Title2\n"
        + "Some more blah for subtitle3\n"
        + "\n"
        + "### Sub Sub Title1\n"
        + "Some more blah for subtitle3_1"
        + "";

    String expected = "# Main Title\n"
        + "Some blah\n"
        + "\n"
        + "And the table of contents should be inserted here:\n"
        + "<!-- BEGIN TABLE OF CONTENTS -->\n"
        + "  - [Main Title](#main-title)\n"
        + "    - [Subtitle1@](#subtitle1)\n"
        + "    - [Sub Title2](#sub-title2)\n"
        + "      - [Sub Sub Title1](#sub-sub-title1)\n"
        + "\n"
        + "<!-- END TABLE OF CONTENTS -->\n"
        + "## Subtitle1@\n"
        + "Some more blah! for subtitle1\n"
        + "\n"
        + "## Sub Title2\n"
        + "Some more blah for subtitle3\n"
        + "\n"
        + "### Sub Sub Title1\n"
        + "Some more blah for subtitle3_1";
    String generated = ExchangeReadmeMdGeneratorUtil.generateTableOfContent(mdFileContent);
    Assert.assertEquals(expected, generated);
    generated = ExchangeReadmeMdGeneratorUtil.generateTableOfContent(generated);
    Assert.assertEquals(expected, generated);
  }
  
  @Test
  public void testGenerateTableOfContent_NoEndTag() {
    String mdFileContent = "# Main Title\n"
        + "Some blah\n"
        + "\nAnd the table of contents should be inserted here:\n"
        + "<!-- BEGIN TABLE OF CONTENTS -->\n"
        + " - Some existing table of contents\n"
        + "## Subtitle1\n"
        + "Some more blah for subtitle1\n"
        + "\n"
        + "## Sub Title2\n"
        + "Some more blah for subtitle3\n"
        + "\n"
        + "### Sub Sub Title1\n"
        + "Some more blah for subtitle3_1";
    Assert.assertEquals(mdFileContent, ExchangeReadmeMdGeneratorUtil.generateTableOfContent(mdFileContent));
  }
  
  @Test
  public void testGenerateTableOfContent_NoBeginTag() {
    String mdFileContent = "# Main Title\n"
        + "Some blah\n"
        + "\nAnd the table of contents should be inserted here:\n"
        + " - Some existing table of contents\n"
        + "<!-- END TABLE OF CONTENTS -->\n"
        + "## Subtitle1\n"
        + "Some more blah for subtitle1\n"
        + "\n"
        + "## Sub Title2\n"
        + "Some more blah for subtitle3\n"
        + "\n"
        + "### Sub Sub Title1\n"
        + "Some more blah for subtitle3_1"
        + "";
    Assert.assertEquals(mdFileContent, ExchangeReadmeMdGeneratorUtil.generateTableOfContent(mdFileContent));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGenerateTableContent_InvalidHeaderLine() {
    ExchangeReadmeMdGeneratorUtil.generateTableOfContent("<!-- BEGIN TABLE OF CONTENTS --><!-- END TABLE OF CONTENTS -->\n#Invalid header line missing space");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGenerateTableContent_EmptyHeaderLine() {
    ExchangeReadmeMdGeneratorUtil.generateTableOfContent("<!-- BEGIN TABLE OF CONTENTS --><!-- END TABLE OF CONTENTS -->\n#");
  }
  
  @Test
  public void testGenerateMarkdownLink() {
    String text = "Link Text";
    String url = "https://example.com";
    String expectedLink = "[Link Text](https://example.com)";

    Assert.assertEquals(expectedLink, ExchangeReadmeMdGeneratorUtil.generateMarkdownLink(text, url));
  }
  
  @Test
  public void testGenerateMarkdownAnchorFromHeader() {
    String headerLine = "This is a Header";
    String expectedAnchor = "this-is-a-header";

    Assert.assertEquals(expectedAnchor, ExchangeReadmeMdGeneratorUtil.generateMarkdownAnchorFromHeader(headerLine));
  }
  
  @Test
  public void testFindTitleLines() {
    String mdFileContent = 
          "# Main Title\n" 
        + "Some text\n" 
        + "## Subtitle1\n" 
        + "More text\n" 
        + "### Sub Subtitle1\n"
        + "Even more text\n";

    Assert.assertEquals(3, ExchangeReadmeMdGeneratorUtil.findTitleLines(mdFileContent).size());
    Assert.assertTrue(ExchangeReadmeMdGeneratorUtil.findTitleLines(mdFileContent).contains("# Main Title"));
    Assert.assertTrue(ExchangeReadmeMdGeneratorUtil.findTitleLines(mdFileContent).contains("## Subtitle1"));
    Assert.assertTrue(ExchangeReadmeMdGeneratorUtil.findTitleLines(mdFileContent).contains("### Sub Subtitle1"));
  }

}
