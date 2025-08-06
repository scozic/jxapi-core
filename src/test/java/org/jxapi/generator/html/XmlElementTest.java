package org.jxapi.generator.html;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link XmlElement}
 */
public class XmlElementTest {

  @Test
  public void testGettersAndSettersAndHashCode() {
    XmlElement element = new XmlElement();
    element.setTag("div");
    element.setContent("Hello, World!");
    element.addAttribute("class", "my-class");
    element.addAttribute(null, "foo");
    element.addAttribute("bar", null);
    element.addAttributes(Map.of("id", "my-id", "style", "color: red;"));
    element.addAttributes(null);
    element.addChild(XmlElement.builder()
        .tag("span")
        .content("Child Element")
        .build());
    element.addChild(null);
    element.addChildren(List.of(
        XmlElement.builder().tag("p").content("Paragraph 1").build(),
        XmlElement.builder().tag("p").content("Paragraph 2").build()
    ));
    element.addChildren(null);
    Assert.assertEquals("div", element.getTag());
    Assert.assertEquals("Hello, World!", element.getContent());
    Assert.assertEquals("my-class", element.getAttributes().get("class"));
    Assert.assertEquals("my-id", element.getAttributes().get("id"));
    Assert.assertEquals("color: red;", element.getAttributes().get("style"));
    Assert.assertEquals(3, element.getChildren().size());
    Assert.assertEquals("span", element.getChildren().get(0).getTag());
    Assert.assertEquals("Child Element", element.getChildren().get(0).getContent());
    Assert.assertEquals("p", element.getChildren().get(1).getTag());
    Assert.assertEquals("Paragraph 1", element.getChildren().get(1).getContent());
    Assert.assertEquals("p", element.getChildren().get(2).getTag());
    Assert.assertEquals("Paragraph 2", element.getChildren().get(2).getContent());
    Assert.assertEquals(1525206845, element.hashCode());
  }
  
  @Test
  public void testToString() {
    XmlElement element = XmlElement.builder()
        .tag("div")
        .content("Hello, World!")
        .attr("class", "my-class")
        .child(XmlElement.builder()
            .tag("span")
            .content("Child Element")
            .build())
        .build();
    Assert.assertEquals("XmlElement{\"tag\":\"div\",\"content\":\"Hello, World!\",\"attributes\":{\"class\":\"my-class\"},\"children\":[{\"tag\":\"span\",\"content\":\"Child Element\",\"attributes\":{},\"children\":[]}]}", element.toString());
  }
  
  @Test
  public void testBuilder() {
    XmlElement element = XmlElement.builder()
        .tag("div").content("Hello, World!")
        .attr("class", "my-class")
        .addAttributes(Map.of("id", "my-id", "style", "color: red;"))
        .child(XmlElement.builder().tag("span").content("Child Element").build())
        .children(List.of(
            XmlElement.builder().tag("p").content("Paragraph 1").build(),
            XmlElement.builder().tag("p").content("Paragraph 2").build()))
        .build();

    Assert.assertEquals("div", element.getTag());
    Assert.assertEquals("Hello, World!", element.getContent());
    Assert.assertEquals("my-class", element.getAttributes().get("class"));
    Assert.assertEquals(3, element.getChildren().size());
    Assert.assertEquals("span", element.getChildren().get(0).getTag());
    Assert.assertEquals("Child Element", element.getChildren().get(0).getContent());
    Assert.assertEquals("p", element.getChildren().get(1).getTag());
    Assert.assertEquals("Paragraph 1", element.getChildren().get(1).getContent());
    Assert.assertEquals("p", element.getChildren().get(2).getTag());
    Assert.assertEquals("Paragraph 2", element.getChildren().get(2).getContent());
  }
  
  @Test
  public void testDeepClone() {
    XmlElement element = XmlElement.builder()
        .tag("div").content("Hello, World!")
        .attr("class", "my-class")
        .addAttributes(Map.of("id", "my-id", "style", "color: red;"))
        .child(XmlElement.builder().tag("span").content("Child Element").build())
        .children(List.of(
            XmlElement.builder().tag("p").content("Paragraph 1").build(),
            XmlElement.builder().tag("p").content("Paragraph 2").build()))
        .build();
    XmlElement clonedElement = element.deepClone();

    Assert.assertEquals(element.getTag(), clonedElement.getTag());
    Assert.assertEquals(element.getContent(), clonedElement.getContent());
    Assert.assertEquals(element.getAttributes(), clonedElement.getAttributes());
    Assert.assertNotSame(element.getAttributes(), clonedElement.getAttributes());
    Assert.assertEquals(element.getChildren().size(), clonedElement.getChildren().size());
    Assert.assertNotSame(element.getChildren(), clonedElement.getChildren());
    for (int i = 0; i < element.getChildren().size(); i++) {
      Assert.assertEquals(element.getChildren().get(i), clonedElement.getChildren().get(i));
      Assert.assertNotSame(element.getChildren().get(i), clonedElement.getChildren().get(i));
    }
  }
  
  @Test
  public void testEquals() {
    
    XmlElement element = XmlElement.builder()
        .tag("div").content("Hello, World!")
        .attr("class", "my-class")
        .addAttributes(Map.of("id", "my-id", "style", "color: red;"))
        .child(XmlElement.builder().tag("span").content("Child Element").build())
        .children(List.of(
            XmlElement.builder().tag("p").content("Paragraph 1").build(),
            XmlElement.builder().tag("p").content("Paragraph 2").build()))
        .build();
    
    Assert.assertEquals(element, element); // reflexive
    Assert.assertFalse(element.equals(null)); // non-null
    Assert.assertFalse(element.equals(new Object())); // non-instance
    
    XmlElement clonedElement = element.deepClone();

    Assert.assertEquals(element, clonedElement);
    
    clonedElement.setTag("span");
    Assert.assertNotEquals(element, clonedElement);
    
    clonedElement = element.deepClone();
    clonedElement.setContent("Goodbye, World!");
    Assert.assertNotEquals(element, clonedElement);
    
    clonedElement = element.deepClone();
    clonedElement.addAttribute("class", "another-class");
    Assert.assertNotEquals(element, clonedElement);
    
    clonedElement = element.deepClone();
    clonedElement.addChild(XmlElement.builder().tag("span").content("Another Child").build());
    Assert.assertNotEquals(element, clonedElement);
  }

}
