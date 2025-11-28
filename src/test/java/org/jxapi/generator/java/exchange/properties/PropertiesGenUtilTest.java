package org.jxapi.generator.java.exchange.properties;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.descriptor.ConfigPropertyDescriptor;
import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.ConfigProperty;
import org.jxapi.util.DefaultConfigProperty;
import org.jxapi.util.EncodingUtil;
import org.jxapi.util.JsonUtil;
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
    PropertiesGenUtil.asConfigProperty(ConfigPropertyDescriptor.createGroup(
        "myGroup", 
        null, 
        List.of(ConfigPropertyDescriptor.create("myProp", Type.STRING, null, null))));
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
    String code = PropertiesGenUtil.generateSimplePropertyValueDeclaration(property, List.of(property), null, imports, null, null);
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
  public void testGenerateSimplePropertyValueDeclaration_NonPrimitiveType() {
    Imports imports = new Imports();
    ConfigPropertyDescriptor property = ConfigPropertyDescriptor.create("myProp", Type.fromTypeName("INT_LIST"), "A test property",
        "[1, 2, 3]");
    String code = PropertiesGenUtil.generateSimplePropertyValueDeclaration(property, List.of(property), null, imports, null, null);
    Assert.assertEquals(
        "/**\n"
        + " * A test property\n"
        + " */\n"
        + "public static final ConfigProperty MY_PROP = DefaultConfigProperty.create(\n"
        + "  \"myProp\",\n"
        + "  Type.STRING,\n"
        + "  \"A test property\",\n"
        + "  \"[1, 2, 3]\");\n"
        + "",
        code);
    Assert.assertEquals(3, imports.size());
    Iterator<String> it = imports.iterator();
    Assert.assertEquals(Type.class.getName(), it.next());
    Assert.assertEquals(ConfigProperty.class.getName(), it.next());
    Assert.assertEquals(DefaultConfigProperty.class.getName(), it.next());
  }
  
  @Test
  public void testGenerateSimplePropertyValueDeclaration_WithDescriptionAndSampleVlauePlaceholders() {
    Imports imports = new Imports();
    ConfigPropertyDescriptor property = ConfigPropertyDescriptor.create(
        "helloProp", 
        Type.STRING, 
        "A test ${hello} property",
        "Hi ${foo}!");
    PlaceHolderResolver docPlaceHolderResolver = PlaceHolderResolver.create(Map.of("hello", "Hello World"));
    PlaceHolderResolver sampleValuePlaceHolderResolver = createSampleValueResolver(Map.of("foo", "bar"));
    String code = PropertiesGenUtil.generateSimplePropertyValueDeclaration(
        property, 
        List.of(property), 
        "demo", 
        imports, 
        docPlaceHolderResolver, 
        sampleValuePlaceHolderResolver);
    Assert.assertEquals(
        "/**\n"
        + " * A test Hello World property\n"
        + " */\n"
        + "public static final ConfigProperty HELLO_PROP = DefaultConfigProperty.create(\n"
        + "  \"demo.helloProp\",\n"
        + "  Type.STRING,\n"
        + "  \"A test Hello World property\",\n"
        + "  \"Hi bar!\");\n"
        + "",
        code);
    Assert.assertEquals(3, imports.size());
    Iterator<String> it = imports.iterator();
    Assert.assertEquals(Type.class.getName(), it.next());
    Assert.assertEquals(ConfigProperty.class.getName(), it.next());
    Assert.assertEquals(DefaultConfigProperty.class.getName(), it.next());
  }
  
  @Test
  public void testGenerateSimplePropertyValueDeclaration_ObjectPropertyRawWValueDemoProperty() {
    Imports imports = new Imports();
    Person samplePerson = new Person("John ${lastName}", 30);
    ConfigPropertyDescriptor rawValueProperty = ConfigPropertyDescriptor.create(
        "personProp", 
        Type.STRING, 
        "A test person like ${fullName}  property",
        JsonUtil.pojoToJsonString(samplePerson, EncodingUtil.createDefaultPojoToToStringObjectMapper()));
    ConfigPropertyDescriptor groupProperty = ConfigPropertyDescriptor.createGroup(
        "personProp", 
        "Group for object type 'personProp'", 
        List.of());
    List<ConfigPropertyDescriptor> allProperties = List.of(rawValueProperty, groupProperty);
    PlaceHolderResolver docPlaceHolderResolver = PlaceHolderResolver.create(Map.of("fullName", "Bob Smith"));
    PlaceHolderResolver sampleValuePlaceHolderResolver = createSampleValueResolver(Map.of("lastName", "Doe"));
    String code = PropertiesGenUtil.generateSimplePropertyValueDeclaration(
        rawValueProperty, 
        allProperties, 
        "demo", 
        imports, 
        docPlaceHolderResolver, 
        sampleValuePlaceHolderResolver);
    Assert.assertEquals(
        "/**\n"
        + " * A test person like Bob Smith  property\n"
        + " */\n"
        + "public static final ConfigProperty PERSON_PROP = DefaultConfigProperty.create(\n"
        + "  \"demo.personProp\",\n"
        + "  Type.STRING,\n"
        + "  \"A test person like Bob Smith  property\",\n"
        + "  \"{\\\"name\\\":\\\"John Doe\\\",\\\"age\\\":30}\");\n"
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
    String code = PropertiesGenUtil.generateSimplePropertyValueDeclaration(property, List.of(property), null, imports, null, null);
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
    
    ConfigPropertyDescriptor groupProperty = ConfigPropertyDescriptor.createGroup("myGroup", "A test group", List.of(property, boolProperty));
    Assert.assertEquals("getMyGroup", PropertiesGenUtil.getPropertyGetterMethodName(groupProperty, List.of(property)));
  }
  
  @Test
  public void testGetPropertyFullName() {
    Assert.assertEquals("myProp", PropertiesGenUtil.getPropertyFullName(null, "myProp"));
    Assert.assertEquals("myProp", PropertiesGenUtil.getPropertyFullName("", "myProp"));
    Assert.assertEquals("demo.myProp", PropertiesGenUtil.getPropertyFullName("demo", "myProp"));
  }
  
  @Test
  public void testGetPropertyVariableName_SimpleProp() {
    ConfigPropertyDescriptor p = ConfigPropertyDescriptor.create("myProp", Type.STRING, null, null);
    Assert.assertEquals("MY_PROP", PropertiesGenUtil.getPropertyVariableName(p, List.of(p)));
  }
  
  @Test
  public void testGetPropertyVariableName_SimplePropWithSameVariableNameAsOtherSimpleProps() {
    ConfigPropertyDescriptor p1 = ConfigPropertyDescriptor.create("com.x.gen.p1", Type.STRING, null, null);
    ConfigPropertyDescriptor p1Up = ConfigPropertyDescriptor.create("com.x.gen.P1", Type.STRING, null, null);
    ConfigPropertyDescriptor p1Underscore = ConfigPropertyDescriptor.create("com.x.gen.p1_", Type.STRING, null, null);
    ConfigPropertyDescriptor p1UpUnderscore = ConfigPropertyDescriptor.createGroup("com.x.gen.P1_", null, null);
    List<ConfigPropertyDescriptor> allProps = List.of(p1, p1Up, p1Underscore, p1UpUnderscore);
    Assert.assertEquals("P1", PropertiesGenUtil.getPropertyVariableName(p1, allProps));
    Assert.assertEquals("P1_", PropertiesGenUtil.getPropertyVariableName(p1Up, allProps));
    Assert.assertEquals("P1__", PropertiesGenUtil.getPropertyVariableName(p1Underscore, allProps));
    Assert.assertEquals("P1___", PropertiesGenUtil.getPropertyVariableName(p1UpUnderscore, allProps));
  }
  
  @Test
  public void testGetPropertyVariableName_SimplePropWithSameStaticVariableNameAsGroupPropClassName() {
    ConfigPropertyDescriptor p1 = ConfigPropertyDescriptor.create("p1", Type.STRING, null, null);
    ConfigPropertyDescriptor g1 = ConfigPropertyDescriptor.createGroup("P1", "A group", List.of());
    List<ConfigPropertyDescriptor> allProps = List.of(p1, g1);
    Assert.assertEquals("P1", PropertiesGenUtil.getPropertyVariableName(p1, allProps));
    Assert.assertEquals("P1_", PropertiesGenUtil.getPropertyVariableName(g1, allProps));
  }
  
  @Test
  public void testGetPropertyVariableName_ObjectPropAsRawValueAndGroupProp() {
    ConfigPropertyDescriptor p1 = ConfigPropertyDescriptor.create("myObjectProp", Type.STRING, "Raw value prop for myGroupProp", null);
    ConfigPropertyDescriptor g1 = ConfigPropertyDescriptor.createGroup("myObjectProp", "Group property for myGrouupProp", List.of());
    List<ConfigPropertyDescriptor> allProps = List.of(p1, g1);
    Assert.assertEquals("MY_OBJECT_PROP", PropertiesGenUtil.getPropertyVariableName(p1, allProps));
    Assert.assertEquals("MyObjectProp", PropertiesGenUtil.getPropertyVariableName(g1, allProps));
  }
  
  @Test
  public void testGetPropertyValueDeclaration() {
    ConfigPropertyDescriptor p = ConfigPropertyDescriptor.create(
        "myProp", 
        Type.STRING, 
        "A test string value property, for instance '${constants.bar}'", 
        "foo");
    Imports imports = new Imports();
    PlaceHolderResolver placeholderResolver = PlaceHolderResolver.create(Map.of("constants.bar", "bar"));
    Assert.assertEquals("/**\n"
        + " * A test string value property, for instance 'bar'\n"
        + " */\n"
        + "public static final ConfigProperty MY_PROP = DefaultConfigProperty.create(\n"
        + "  \"myProp\",\n"
        + "  Type.STRING,\n"
        + "  \"A test string value property, for instance 'bar'\",\n"
        + "  \"foo\");\n", 
        PropertiesGenUtil.generateSimplePropertyValueDeclaration(p, List.of(p), null, imports, placeholderResolver, null));
  }
  
  @Test
  public void testGetPropertyValueDeclaration_ValueWithPlaceholder() {
    ConfigPropertyDescriptor p = ConfigPropertyDescriptor.create("myProp", Type.STRING, "A test string value property,\nfor instance '${constants.bar}'", "${constants.foo}");
    Imports imports = new Imports();
    PlaceHolderResolver docPlaceholderResolver = PlaceHolderResolver.create(Map.of("constants.bar", "bar"));
    PlaceHolderResolver defaultValuePlaceholderResolver = PlaceHolderResolver.create(Map.of("constants.foo", "\"myFooValue\""));
    Assert.assertEquals("/**\n"
        + " * A test string value property,\n"
        + " * for instance 'bar'\n"
        + " */\n"
        + "public static final ConfigProperty MY_PROP = DefaultConfigProperty.create(\n"
        + "  \"myProp\",\n"
        + "  Type.STRING,\n"
        + "  \"A test string value property,\\nfor instance 'bar'\",\n"
        + "  \"myFooValue\");\n"
        + "", 
        PropertiesGenUtil.generateSimplePropertyValueDeclaration(p, List.of(p), "", imports, docPlaceholderResolver, defaultValuePlaceholderResolver));
  }
  
  private static PlaceHolderResolver createSampleValueResolver(Map<String, Object> values) {
    PlaceHolderResolver map = PlaceHolderResolver.create(values);
    return s -> JavaCodeGenUtil.getQuotedString(map.resolve(s));
  }

  private static class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
      this.setName(name);
      this.setAge(age);
    }

    @SuppressWarnings("unused")
    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    @SuppressWarnings("unused")
    public int getAge() {
      return age;
    }

    public void setAge(int age) {
      this.age = age;
    }
  }
  
}
