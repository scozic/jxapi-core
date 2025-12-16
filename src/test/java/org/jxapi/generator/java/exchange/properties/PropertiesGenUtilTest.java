package org.jxapi.generator.java.exchange.properties;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.descriptor.gen.ConfigPropertyDescriptor;
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
    ConfigPropertyDescriptor propertyDescriptor = ConfigPropertyDescriptor
        .builder()
        .name("myProp")
        .description("A test property")
        .type(Type.INT.toString())
        .defaultValue(456)
        .build();
    ConfigProperty property = PropertiesGenUtil.asConfigProperty(propertyDescriptor);
    Assert.assertNotNull(property);
    Assert.assertEquals("myProp", property.getName());
    Assert.assertEquals(Type.INT, property.getType());
    Assert.assertEquals("A test property", property.getDescription());
    Assert.assertEquals(456, property.getDefaultValue());    
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testAsConfigProperty_Group() {
    ConfigPropertyDescriptor groupPropertyDescriptor = ConfigPropertyDescriptor
        .builder()
        .name("myGroup")
        .addToProperties(ConfigPropertyDescriptor
            .builder()
            .name("myProp")
            .type(Type.STRING.toString())
            .build())
        .build();
    PropertiesGenUtil.asConfigProperty(groupPropertyDescriptor);
  }
  
  @Test
  public void testAsConfigPropertyDescriptor() {
    Assert.assertNull(PropertiesGenUtil.asConfigPropertyDescriptor(null));
    ConfigProperty property = DefaultConfigProperty.create("myProp", Type.BOOLEAN, "A boolean property", true);
    ConfigPropertyDescriptor propertyDescriptor = PropertiesGenUtil.asConfigPropertyDescriptor(property);
    Assert.assertNotNull(propertyDescriptor);
    Assert.assertEquals("myProp", propertyDescriptor.getName());
    Assert.assertEquals(Type.BOOLEAN.toString(), propertyDescriptor.getType());
    Assert.assertEquals("A boolean property", propertyDescriptor.getDescription());
    Assert.assertEquals(true, propertyDescriptor.getDefaultValue());
  }
  
  @Test
  public void testGenerateSimplePropertyValueDeclaration() {
    Imports imports = new Imports();
    ConfigPropertyDescriptor property = ConfigPropertyDescriptor.builder()
        .name("myProp")
        .type(Type.STRING.toString())
        .description("A test property")
        .defaultValue("defaultValue")
        .build();
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
    ConfigPropertyDescriptor property = ConfigPropertyDescriptor.builder()
        .name("myProp")
        .type("INT_LIST")
        .description("A test property")
        .defaultValue(List.of(1, 2, 3))
        .build();
    String code = PropertiesGenUtil.generateSimplePropertyValueDeclaration(property, List.of(property), null, imports, null, null);
    Assert.assertEquals(
        "/**\n"
        + " * A test property\n"
        + " */\n"
        + "public static final ConfigProperty MY_PROP = DefaultConfigProperty.create(\n"
        + "  \"myProp\",\n"
        + "  Type.STRING,\n"
        + "  \"A test property\",\n"
        + "  \"[1,2,3]\");\n"
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
    ConfigPropertyDescriptor property = ConfigPropertyDescriptor.builder()
        .name("helloProp")
        .type(Type.STRING.toString())
        .description("A test ${hello} property")
        .defaultValue("Hi ${foo}!")
        .build();
    
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
    ConfigPropertyDescriptor rawValueProperty = ConfigPropertyDescriptor.builder()
        .name("personProp")
        .type(Type.STRING.toString())
        .description("A test person like ${fullName}  property")
        .defaultValue(JsonUtil.pojoToJsonString(samplePerson, EncodingUtil.createDefaultPojoToToStringObjectMapper()))
        .build();

    ConfigPropertyDescriptor groupProperty = ConfigPropertyDescriptor.builder()
        .name("groupProp")
        .description("Group property'")
        .properties(List.of())
        .build();
        
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
    ConfigPropertyDescriptor property = ConfigPropertyDescriptor.builder()
        .name("myProp")
        .type(Type.STRING.toString())
        .description("A test property")
        .build();
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
    ConfigPropertyDescriptor prop1 = ConfigPropertyDescriptor.builder()
        .name("prop1")
        .type(Type.STRING.toString())
        .description("First property")
        .defaultValue("default1")
        .build();
    ConfigPropertyDescriptor prop2 = ConfigPropertyDescriptor.builder()
        .name("prop2")
        .type(Type.INT.toString())
        .description("Second property")
        .defaultValue(42)
        .build();
    ConfigPropertyDescriptor prop3 = ConfigPropertyDescriptor.builder()
        .name("prop3")
        .type(Type.BOOLEAN.toString())
        .description("Third property")
        .defaultValue(false)
        .build();
    ConfigPropertyDescriptor group1 = ConfigPropertyDescriptor.builder()
        .name("group1")
        .description("A group of properties")
        .properties(List.of(prop2, prop3))
        .build();    
    
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
    ConfigPropertyDescriptor prop1 = ConfigPropertyDescriptor.builder()
        .name("prop1")
        .type(Type.STRING.toString())
        .description("First property")
        .defaultValue("default1")
        .build();
    ConfigPropertyDescriptor prop2 = ConfigPropertyDescriptor.builder()
        .name("prop2")
        .type(Type.INT.toString())
        .description("Second property")
        .defaultValue(42)
        .build();
    ConfigPropertyDescriptor prop3 = ConfigPropertyDescriptor.builder()
        .name("prop3")
        .type(Type.BOOLEAN.toString())
        .description("Third property")
        .defaultValue(false)
        .build();
    ConfigPropertyDescriptor group1 = ConfigPropertyDescriptor.builder()
        .name("group1")
        .description("A group of properties")
        .properties(List.of(prop2, prop3))
        .build();      
    
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
    ConfigPropertyDescriptor prop1 = ConfigPropertyDescriptor.builder()
        .name("prop1")
        .type(Type.STRING.toString())
        .description("First property")
        .defaultValue("default1")
        .build();
    ConfigPropertyDescriptor prop2 = ConfigPropertyDescriptor.builder()
        .name("prop2")
        .type(Type.INT.toString())
        .description("Second property")
        .defaultValue(42)
        .build();
    ConfigPropertyDescriptor prop3 = ConfigPropertyDescriptor.builder()
        .name("prop3")
        .type(Type.BOOLEAN.toString())
        .description("Third property")
        .defaultValue(false)
        .build();
    ConfigPropertyDescriptor group1 = ConfigPropertyDescriptor.builder()
        .name("group1")
        .description("A group of properties")
        .addToProperties(prop2)
        .build();        
    
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
    ConfigPropertyDescriptor prop1 = ConfigPropertyDescriptor.builder()
        .name("prop1")
        .type(Type.STRING.toString())
        .description("First property")
        .defaultValue("default1")
        .build();
    ConfigPropertyDescriptor prop2 = ConfigPropertyDescriptor.builder()
        .name("prop2")
        .type(Type.INT.toString())
        .description("Second property")
        .defaultValue(42)
        .build();
    ConfigPropertyDescriptor prop3 = ConfigPropertyDescriptor.builder()
        .name("prop3")
        .type(Type.BOOLEAN.toString())
        .description("Third property")
        .defaultValue(false)
        .build();   
    
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
    ConfigPropertyDescriptor prop1 = ConfigPropertyDescriptor.builder()
        .name("prop1")
        .type(Type.STRING.toString())
        .description("First property")
        .defaultValue("default1")
        .build();
    ConfigPropertyDescriptor prop2 = ConfigPropertyDescriptor.builder()
        .name("prop2")
        .type(Type.INT.toString())
        .description("Second property")
        .defaultValue(42)
        .build();
    ConfigPropertyDescriptor prop3 = ConfigPropertyDescriptor.builder()
        .name("prop3")
        .type(Type.BOOLEAN.toString())
        .description("Third property")
        .defaultValue(false)
        .build();
    ConfigPropertyDescriptor group1 = ConfigPropertyDescriptor.builder()
        .name("group1")
        .description("A group of properties")
        .properties(List.of(prop2, prop3))
        .build();
    ConfigPropertyDescriptor group2 = ConfigPropertyDescriptor.builder()
        .name("group2")
        .description("A 2nd group of properties")
        .addToProperties(prop1)
        .build();
    
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
    ConfigPropertyDescriptor property = ConfigPropertyDescriptor.builder()
        .name("myProp")
        .type(Type.STRING.toString())
        .description("A test property")
        .build();
    Assert.assertEquals("getMyProp", PropertiesGenUtil.getPropertyGetterMethodName(property, List.of(property)));
    
    ConfigPropertyDescriptor propertyWithSameNameButFirstLetterUppercase = ConfigPropertyDescriptor.builder()
        .name("MyProp")
        .type(Type.STRING.toString())
        .description("A test property with same name as 'myProp' but first letter as uppercase")
        .defaultValue("defaultValue")
        .build();

    Assert.assertEquals("getmyProp", PropertiesGenUtil.getPropertyGetterMethodName(property, List.of(property, propertyWithSameNameButFirstLetterUppercase)));
    
    ConfigPropertyDescriptor boolProperty = ConfigPropertyDescriptor.builder()
        .name("myBoolProp")
        .type(Type.BOOLEAN.toString())
        .description("A test property")
        .build();
    Assert.assertEquals("isMyBoolProp", PropertiesGenUtil.getPropertyGetterMethodName(boolProperty, null));
    
    ConfigPropertyDescriptor groupProperty = ConfigPropertyDescriptor.builder()
        .name("myGroup")
        .description("A test group")
        .properties(List.of(property, boolProperty))
        .build();

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
    ConfigPropertyDescriptor p = ConfigPropertyDescriptor.builder()
        .name("myProp")
        .type(Type.STRING.toString())
        .description("A test property")
        .build();
    Assert.assertEquals("MY_PROP", PropertiesGenUtil.getPropertyVariableName(p, List.of(p)));
  }
  
  @Test
  public void testGetPropertyVariableName_NullProp() {
    Assert.assertNull(PropertiesGenUtil.getPropertyVariableName(null, null));
  }
  
  @Test
  public void testGetPropertyVariableName_SimplePropWithSameVariableNameAsOtherSimpleProps() {
    ConfigPropertyDescriptor p1 = ConfigPropertyDescriptor.builder()
        .name("p1")
        .type(Type.STRING.toString())
        .description("First p1 property")
        .build();
    ConfigPropertyDescriptor p1Up = ConfigPropertyDescriptor.builder()
        .name("P1")
        .type(Type.STRING.toString())
        .description("Second p1 property with uppercase first letter")
        .build();
    ConfigPropertyDescriptor p1Underscore = ConfigPropertyDescriptor
        .builder()
        .name("p1_")
        .type(Type.STRING.toString())
        .description("Third p1 property with underscore")
        .build();
    ConfigPropertyDescriptor p1UpUnderscore = ConfigPropertyDescriptor.builder()
        .name("P1_")
        .type(Type.STRING.toString())
        .description("Fourth p1 property with uppercase first letter and underscore")
        .build();
    List<ConfigPropertyDescriptor> allProps = List.of(p1, p1Up, p1Underscore, p1UpUnderscore);
    Assert.assertEquals("P1", PropertiesGenUtil.getPropertyVariableName(p1, allProps));
    Assert.assertEquals("P1_", PropertiesGenUtil.getPropertyVariableName(p1Up, allProps));
    Assert.assertEquals("P1__", PropertiesGenUtil.getPropertyVariableName(p1Underscore, allProps));
    Assert.assertEquals("P1___", PropertiesGenUtil.getPropertyVariableName(p1UpUnderscore, allProps));
  }
  
  @Test
  public void testGetPropertyVariableName_SimplePropWithSameStaticVariableNameAsGroupPropClassName() {
    ConfigPropertyDescriptor p1 = ConfigPropertyDescriptor.builder()
        .name("p1")
        .type(Type.STRING.toString())
        .description("A simple p1 property")
        .build();
    ConfigPropertyDescriptor g1 = ConfigPropertyDescriptor.builder()
        .name("P1")
        .description("A group p1 property")
        .properties(List.of())
        .build();
    List<ConfigPropertyDescriptor> allProps = List.of(p1, g1);
    Assert.assertEquals("P1", PropertiesGenUtil.getPropertyVariableName(p1, allProps));
    Assert.assertEquals("P1_", PropertiesGenUtil.getPropertyVariableName(g1, allProps));
  }
  
  @Test
  public void testGetPropertyVariableName_ObjectPropAsRawValueAndGroupProp() {
    ConfigPropertyDescriptor p1 = ConfigPropertyDescriptor.builder()
        .name("myObjectProp")
        .type(Type.STRING.toString())
        .description("Raw value prop for myGroupProp")
        .build();
    ConfigPropertyDescriptor g1 = ConfigPropertyDescriptor.builder()
        .name("myObjectProp")
        .description("Group property for myGrouupProp")
        .properties(List.of())
        .build();
    List<ConfigPropertyDescriptor> allProps = List.of(p1, g1);
    Assert.assertEquals("MY_OBJECT_PROP", PropertiesGenUtil.getPropertyVariableName(p1, allProps));
    Assert.assertEquals("MyObjectProp", PropertiesGenUtil.getPropertyVariableName(g1, allProps));
  }
  
  @Test
  public void testGetPropertyValueDeclaration() {
    ConfigPropertyDescriptor p = ConfigPropertyDescriptor.builder()
        .name("myProp")
        .type(Type.STRING.toString())
        .description("A test string value property, for instance '${constants.bar}'")
        .defaultValue("foo")
        .build();
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
    ConfigPropertyDescriptor p = ConfigPropertyDescriptor.builder()
        .name("myProp")
        .type(Type.STRING.toString())
        .description("A test string value property,\nfor instance '${constants.bar}'")
        .defaultValue("${constants.foo}")
        .build();
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
