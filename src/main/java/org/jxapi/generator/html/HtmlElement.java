package org.jxapi.generator.html;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jxapi.util.CollectionUtil;
import org.jxapi.util.DeepCloneable;
import org.jxapi.util.EncodingUtil;

/**
 * Represents a <a href="https://developer.mozilla.org/fr/docs/Web/HTML/Reference/Elements/td">cell in an HTML table</a>. It can contain content and has a map of
 * attributes that can be added to the cell.<p>
 * The 
 */
public class HtmlElement implements DeepCloneable<HtmlElement> {
  
  /**
   * Creates a new builder for {@link HtmlElement}.
   * 
   * @return a new instance of {@link Builder} for constructing an
   *         {@link HtmlElement}.
   */
  public static Builder builder() {
    return new Builder();
  }
  
  private String tag;
  
  private String content;
  
  private final Map<String, String> attributes = CollectionUtil.createMap();
  
  private final List<HtmlElement> children = CollectionUtil.createList();

  /**
   * @return the content of the cell, which can be text or HTML.
   */
  public String getContent() {
    return content;
  }

  /**
   * Sets the content of the cell.
   * 
   * @param content the content to set, which can be text or HTML.
   */
  public void setContent(String content) {
    this.content = content;
  }

  /**
   * Returns the HTML attributes of the cell like rowspan, colspan, style, etc. 
   * 
   * @return a map of attributes where the key is the attribute name and the value
   *         is the attribute value. The returned map is mutable, so you can modify it to add or remove attributes.
   */
  public Map<String, String> getAttributes() {
    return attributes;
  }
  
  /**
   * Adds an attribute to the cell.
   * 
   * @param name  the name of the attribute (e.g., "rowspan", "colspan", "style").
   * @param value the value of the attribute.
   */
  public void addAttribute(String name, String value) {
    if (name != null && value != null) {
      attributes.put(name, value);
    }
  }
  
  /**
   * Adds multiple attributes to the cell.
   * 
   * @param attributes a map of attributes where the key is the attribute name and
   *                   the value is the attribute value. If null, no attributes
   *                   are added.
   */
  public void addAttributes(Map<String, String> attributes) {
    if (attributes != null) {
      attributes.forEach(this::addAttribute);
    }
  }
  
  /**
   * Returns the list of child elements contained within this HTML element.
   * 
   * @param child the child element to add. The list is mutable, so you can add or remove children.
   */
  public List<HtmlElement> getChildren() {
    return children;
  }
  
  /**
   * Adds a child element to this HTML element.
   * 
   * @param child the child element to add. If null, no child is added.
   */
  public void addChild(HtmlElement child) {
    if (child != null) {
      children.add(child);
    }
  }
  
  /**
   * Adds multiple child elements to this HTML element.
   * 
   * @param children a list of child elements to add. If null, no children are
   *                 added.
   */
  public void addChildren(List<HtmlElement> children) {
    if (children != null) {
     children.forEach(this::addChild);
    }
  }
  
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }

  @Override
  public HtmlElement deepClone() {
    HtmlElement clone = new HtmlElement();
    clone.setContent(this.content);
    clone.addAttributes(this.attributes);
    clone.setTag(this.tag);
    this.children.forEach(child -> {
      clone.addChild(child.deepClone());
    });
    return clone;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    HtmlElement other = (HtmlElement) obj;
    return Objects.equals(this.tag, other.tag)
            && Objects.equals(this.content, other.content) 
            && Objects.equals(this.attributes, other.attributes)
            && Objects.equals(this.children, other.children);
  }

  /**
   * @return the tag name of the HTML element, such as "td", "th", "div", etc.
   */
  public String getTag() {
    return tag;
  }

  /**
   * Sets the tag name of the HTML element.
   * 
   * @param tag the tag name to set, such as "td", "th", "div", etc.
   */
  public void setTag(String tag) {
    this.tag = tag;
  }

  /**
   * Builder class for creating instances of {@link HtmlElement}.
   */
  public static class Builder {
    private final HtmlElement cell = new HtmlElement();

    public Builder content(String content) {
      cell.setContent(content);
      return this;
    }
    
    public Builder tag(String tag) {
      cell.setTag(tag);
      return this;
    }

    public Builder attribute(String name, String value) {
      cell.addAttribute(name, value);
      return this;
    }

    public Builder addAttributes(Map<String, String> attributes) {
      cell.addAttributes(attributes);
      return this;
    }
    
    public Builder child(HtmlElement child) {
      cell.addChild(child);
      return this;
    }
    
    public Builder children(List<HtmlElement> children) {
      cell.addChildren(children);
      return this;
    }

    public HtmlElement build() {
      return cell;
    }
  }

}
