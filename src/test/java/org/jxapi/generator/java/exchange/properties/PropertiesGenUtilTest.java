package org.jxapi.generator.java.exchange.properties;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.descriptor.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.generator.java.Imports;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.ConfigProperty;
import org.jxapi.util.DefaultConfigProperty;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Unit test for {@link PropertiesGenUtil}
 */
public class PropertiesGenUtilTest {

  @Test
  public void testAsConfigProperty() {
    Assert.assertNull(PropertiesGenUtil.asConfigProperty(null));
    ConfigPropertyDescriptor propertyDescriptor = ConfigPropertyDescriptor.create("myProp", Type.INT, "A test property", 456);
    ConfigProperty property = PropertiesGenUtil.asConfigProperty(propertyDescriptor);
    Assert.assertNotNull(property);
    Assert.assertEquals("myProp", property.getName());
    Assert.assertEquals(Type.INT, property.getType());
    Assert.assertEquals("A test property", property.getDescription());
    Assert.assertEquals(456, property.getDefaultValue());    
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testAsConfigProperty_Group() {
    PropertiesGenUtil.asConfigProperty(ConfigPropertyDescriptor.createGroup("myGroup", null, List.of(ConfigPropertyDescriptor.create("myProp", Type.STRING, null, null))));
  }
  
  @Test
  public void testAsConfigPropertyDescriptor() {
    Assert.assertNull(PropertiesGenUtil.asConfigPropertyDescriptor(null));
    ConfigProperty property = DefaultConfigProperty.create("myProp", Type.BOOLEAN, "A boolean property", true);
    ConfigPropertyDescriptor propertyDescriptor = PropertiesGenUtil.asConfigPropertyDescriptor(property);
    Assert.assertNotNull(propertyDescriptor);
    Assert.assertEquals("myProp", propertyDescriptor.getName());
    Assert.assertEquals(Type.BOOLEAN, propertyDescriptor.getType());
    Assert.assertEquals("A boolean property", propertyDescriptor.getDescription());
    Assert.assertEquals(true, propertyDescriptor.getDefaultValue());
  }
  
  @Test
  public void testGenerateSimplePropertyValueDeclaration() {
    Imports imports = new Imports();
    ConfigPropertyDescriptor property = ConfigPropertyDescriptor.create("myProp", Type.STRING, "A test property",
        "defaultValue");
    String code = PropertiesGenUtil.generateSimplePropertyValueDeclaration(property, null, imports, null, null);
    Assert.assertEquals(
        "/**\n"
        + " * A test property\n"
        + " */\n"
        + "public static final ConfigProperty MY_PROP = DefaultConfigProperty.create(\n"
        + "  \"myProp\",\n"
        + "  Type.STRING,\n"
        + "  \"A test property\",\n"
        + "  \"defaultValue\");\n"
        + "",
        code);
    Assert.assertEquals(3, imports.size());
    Iterator<String> it = imports.iterator();
    Assert.assertEquals(Type.class.getName(), it.next());
    Assert.assertEquals(ConfigProperty.class.getName(), it.next());
    Assert.assertEquals(DefaultConfigProperty.class.getName(), it.next());
  }
  
  @Test
  public void ttestGenerateSimplePropertyValueDeclaration_WithDescriptionAndSampleVlauePlaceholders() {
    Imports imports = new Imports();
    ConfigPropertyDescriptor property = ConfigPropertyDescriptor.create(
        "helloProp", 
        Type.STRING, 
        "A test ${hello} property",
        "Hi ${foo}!");
    PlaceHolderResolver docPlaceHolderResolver = PlaceHolderResolver.create(Map.of("hello", "Hello World"));
    PlaceHolderResolver sampleValuePlaceHolderResolver = PlaceHolderResolver.create(Map.of("foo", "bar"));
    String code = PropertiesGenUtil.generateSimplePropertyValueDeclaration(property, "demo", imports, docPlaceHolderResolver, sampleValuePlaceHolderResolver);
    Assert.assertEquals(
        "/**\n"
        + " * A test Hello World property\n"
        + " */\n"
        + "public static final ConfigProperty HELLO_PROP = DefaultConfigProperty.create(\n"
        + "  \"demo.helloProp\",\n"
        + "  Type.STRING,\n"
        + "  \"A test Hello World property\",\n"
        + "  Hi bar!);\n"
        + "",
        code);
    Assert.assertEquals(3, imports.size());
    Iterator<String> it = imports.iterator();
    Assert.assertEquals(Type.class.getName(), it.next());
    Assert.assertEquals(ConfigProperty.class.getName(), it.next());
    Assert.assertEquals(DefaultConfigProperty.class.getName(), it.next());
  }
  
  @Test
  public void testGenerateSimplePropertyValueDeclaration_NullSampleValue() {
    Imports imports = new Imports();
    ConfigPropertyDescriptor property = ConfigPropertyDescriptor.create(
      "myProp", 
      Type.STRING, 
      "A test property",
      null);
    String code = PropertiesGenUtil.generateSimplePropertyValueDeclaration(property, null, imports, null, null);
    Assert.assertEquals(
        "/**\n"
        + " * A test property\n"
        + " */\n"
        + "public static final ConfigProperty MY_PROP = DefaultConfigProperty.create(\n"
        + "  \"myProp\",\n"
        + "  Type.STRING,\n"
        + "  \"A test property\",\n"
        + "  null);\n"
        + "",
        code);
    Assert.assertEquals(3, imports.size());
    Iterator<String> it = imports.iterator();
    Assert.assertEquals(Type.class.getName(), it.next());
    Assert.assertEquals(ConfigProperty.class.getName(), it.next());
    Assert.assertEquals(DefaultConfigProperty.class.getName(), it.next());
  }
  
  @Test
  public void testGenerateAllPropertiesListMethod_EmptyProperties() {
    Imports imports = new Imports();
    Assert.assertEquals(
        "/**\n" + " * List of all configuration properties defined in this class\n" + " */\n"
            + "public static final List<ConfigProperty> ALL = List.of();\n",
        PropertiesGenUtil.generateAllPropertiesListMethod(List.of(), imports));
    Assert.assertEquals(2, imports.size());
    Iterator<String> it = imports.iterator();
    Assert.assertEquals(List.class.getName(), it.next());
    Assert.assertEquals(ConfigProperty.class.getName(), it.next());
  }
  
  @Test
  public void testGenerateAllPropertiesListMethod_PropertiesWithGroupFirst() {
    Imports imports = new Imports();
    ConfigPropertyDescriptor prop1 = ConfigPropertyDescriptor.create("prop1", Type.STRING, "First property", "default1");
    ConfigPropertyDescriptor prop2 = ConfigPropertyDescriptor.create("prop2", Type.INT, "Second property", 42);
    ConfigPropertyDescriptor prop3 = ConfigPropertyDescriptor.create("prop3", Type.BOOLEAN, "Third property", false);
    ConfigPropertyDescriptor group1 = ConfigPropertyDescriptor.createGroup("group1", "A group of properties", List.of(prop2, prop3));    
    
    Assert.assertEquals("/**\n"
        + " * List of all configuration properties defined in this class\n"
        + " */\n"
        + "public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(\n"
        + "  Group1.ALL,\n"
        + "  List.of(\n"
        + "    PROP1))));\n"
        + "", PropertiesGenUtil.generateAllPropertiesListMethod(List.of(group1, prop1), imports));
    
    Assert.assertEquals(3, imports.size());
    Iterator<String> it = imports.iterator();
    Assert.assertEquals(List.class.getName(), it.next());
    Assert.assertEquals(CollectionUtil.class.getName(), it.next());
    Assert.assertEquals(ConfigProperty.class.getName(), it.next());
  }
  
  @Test
  public void testGenerateAllPropertiesListMethod_PropertiesWithGroupLast() {
    Imports imports = new Imports();
    ConfigPropertyDescriptor prop1 = ConfigPropertyDescriptor.create("prop1", Type.STRING, "First property", "default1");
    ConfigPropertyDescriptor prop2 = ConfigPropertyDescriptor.create("prop2", Type.INT, "Second property", 42);
    ConfigPropertyDescriptor prop3 = ConfigPropertyDescriptor.create("prop3", Type.BOOLEAN, "Third property", false);
    ConfigPropertyDescriptor group1 = ConfigPropertyDescriptor.createGroup("group1", "A group of properties", List.of(prop2, prop3));    
    
    Assert.assertEquals("/**\n"
        + " * List of all configuration properties defined in this class\n"
        + " */\n"
        + "public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(\n"
        + "  List.of(\n"
        + "    PROP1\n"
        + "  ),\n"
        + "  Group1.ALL)));\n"
        + "", PropertiesGenUtil.generateAllPropertiesListMethod(List.of(prop1, group1), imports));
    
    Assert.assertEquals(3, imports.size());
    Iterator<String> it = imports.iterator();
    Assert.assertEquals(List.class.getName(), it.next());
    Assert.assertEquals(CollectionUtil.class.getName(), it.next());
    Assert.assertEquals(ConfigProperty.class.getName(), it.next());
  }
  
  @Test
  public void testGenerateAllPropertiesListMethod_PropertiesWithGroupInMiddle() {
    Imports imports = new Imports();
    ConfigPropertyDescriptor prop1 = ConfigPropertyDescriptor.create("prop1", Type.STRING, "First property", "default1");
    ConfigPropertyDescriptor prop2 = ConfigPropertyDescriptor.create("prop2", Type.INT, "Second property", 42);
    ConfigPropertyDescriptor prop3 = ConfigPropertyDescriptor.create("prop3", Type.BOOLEAN, "Third property", false);
    ConfigPropertyDescriptor group1 = ConfigPropertyDescriptor.createGroup("group1", "A group of properties", List.of(prop2));    
    
    Assert.assertEquals("/**\n"
        + " * List of all configuration properties defined in this class\n"
        + " */\n"
        + "public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(\n"
        + "  List.of(\n"
        + "    PROP1\n"
        + "  ),\n"
        + "  Group1.ALL,\n"
        + "  List.of(\n"
        + "    PROP3))));\n"
        + "", PropertiesGenUtil.generateAllPropertiesListMethod(List.of(prop1, group1, prop3), imports));
    
    Assert.assertEquals(3, imports.size());
    Iterator<String> it = imports.iterator();
    Assert.assertEquals(List.class.getName(), it.next());
    Assert.assertEquals(CollectionUtil.class.getName(), it.next());
    Assert.assertEquals(ConfigProperty.class.getName(), it.next());
  }
  
  @Test
  public void testGenerateAllPropertiesListMethod_PropertiesWithoutGroup() {
    Imports imports = new Imports();
    ConfigPropertyDescriptor prop1 = ConfigPropertyDescriptor.create("prop1", Type.STRING, "First property", "default1");
    ConfigPropertyDescriptor prop2 = ConfigPropertyDescriptor.create("prop2", Type.INT, "Second property", 42);
    ConfigPropertyDescriptor prop3 = ConfigPropertyDescriptor.create("prop3", Type.BOOLEAN, "Third property", false);    
    
    Assert.assertEquals("/**\n"
        + " * List of all configuration properties defined in this class\n"
        + " */\n"
        + "public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(\n"
        + "  List.of(\n"
        + "    PROP1,\n"
        + "    PROP2,\n"
        + "    PROP3))));\n"
        + "", PropertiesGenUtil.generateAllPropertiesListMethod(List.of(prop1, prop2, prop3), imports));
    
    Assert.assertEquals(3, imports.size());
    Iterator<String> it = imports.iterator();
    Assert.assertEquals(List.class.getName(), it.next());
    Assert.assertEquals(CollectionUtil.class.getName(), it.next());
    Assert.assertEquals(ConfigProperty.class.getName(), it.next());
  }
  
  
  @Test
  public void testGenerateAllPropertiesListMethod_PropertiesWithOnlyGroups() {
    Imports imports = new Imports();
    ConfigPropertyDescriptor prop1 = ConfigPropertyDescriptor.create("prop1", Type.STRING, "First property", "default1");
    ConfigPropertyDescriptor prop2 = ConfigPropertyDescriptor.create("prop2", Type.INT, "Second property", 42);
    ConfigPropertyDescriptor prop3 = ConfigPropertyDescriptor.create("prop3", Type.BOOLEAN, "Third property", false);
    ConfigPropertyDescriptor group1 = ConfigPropertyDescriptor.createGroup("group1", "A group of properties", List.of(prop2, prop3));
    ConfigPropertyDescriptor group2 = ConfigPropertyDescriptor.createGroup("group2", "A 2nd group of properties", List.of(prop1));
    
    Assert.assertEquals("/**\n"
        + " * List of all configuration properties defined in this class\n"
        + " */\n"
        + "public static final List<ConfigProperty> ALL = List.copyOf(CollectionUtil.mergeLists(List.of(\n"
        + "  Group1.ALL,\n"
        + "  Group2.ALL)));\n"
        + "", PropertiesGenUtil.generateAllPropertiesListMethod(List.of(group1, group2), imports));
    
    Assert.assertEquals(3, imports.size());
    Iterator<String> it = imports.iterator();
    Assert.assertEquals(List.class.getName(), it.next());
    Assert.assertEquals(CollectionUtil.class.getName(), it.next());
    Assert.assertEquals(ConfigProperty.class.getName(), it.next());
  }
  
  @Test
  public void testGetPropertyGetterMethodName() {
    ConfigPropertyDescriptor property = ConfigPropertyDescriptor.create("myProp", Type.STRING, "A test property", null);
    Assert.assertEquals("getMyProp", PropertiesGenUtil.getPropertyGetterMethodName(property, List.of(property)));
    ConfigPropertyDescriptor propertyWithSameNameButFirstLetterUppercase = ConfigPropertyDescriptor.create("MyProp", Type.STRING, "A test property with same name as 'myProp' but first letter as uppercase", "defaultValue");
    Assert.assertEquals("getmyProp", PropertiesGenUtil.getPropertyGetterMethodName(property, List.of(property, propertyWithSameNameButFirstLetterUppercase)));
    
    ConfigPropertyDescriptor boolProperty = ConfigPropertyDescriptor.create("myBoolProp", Type.BOOLEAN, "A test property", null);
    Assert.assertEquals("isMyBoolProp", PropertiesGenUtil.getPropertyGetterMethodName(boolProperty, null));
  }

}
