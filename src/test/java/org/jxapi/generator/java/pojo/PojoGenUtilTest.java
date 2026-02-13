package org.jxapi.generator.java.pojo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.netutils.deserialization.MessageDeserializer;
import org.jxapi.netutils.deserialization.json.field.BigDecimalJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.BooleanJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.GenericObjectJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.IntegerJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.LongJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.RawObjectJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;
import org.jxapi.netutils.serialization.MessageSerializer;
import org.jxapi.pojo.descriptor.Field;
import org.jxapi.pojo.descriptor.FieldBuilder;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.CollectionUtil;
import org.jxapi.util.DeepCloneable;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Unit test for {@link PojoGenUtil}
 */
public class PojoGenUtilTest {

  @Test
  public void testGetSerializerClassName() {
        Assert.assertEquals("com.x.y.pojo.serializers.MyPojoSerializer", 
                            PojoGenUtil.getSerializerClassName("com.x.y.pojo.MyPojo"));
  }
  
  @Test
  public void testGenerateDeepCloneFieldInstruction() {
    doTestGenerateDeepCloneFieldInstruction(Type.INT, 
        "this.myField");
    doTestGenerateDeepCloneFieldInstruction(Type.OBJECT, 
        "this.myField != null ? this.myField.deepClone() : null");
    doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("INT_LIST"), 
        "CollectionUtil.cloneList(this.myField)", 
        CollectionUtil.class.getName());
    doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("OBJECT_LIST"), 
        "CollectionUtil.deepCloneList(this.myField, DeepCloneable::deepClone)", 
        CollectionUtil.class.getName(), 
        DeepCloneable.class.getName());
    doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("INT_LIST_LIST"), 
        "CollectionUtil.deepCloneList(this.myField, CollectionUtil::cloneList)", 
        CollectionUtil.class.getName());
    doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("INT_LIST_LIST_LIST"),
        "CollectionUtil.deepCloneList(this.myField, l0 -> CollectionUtil.deepCloneList(l0, CollectionUtil::cloneList))", 
        CollectionUtil.class.getName());
    doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("INT_MAP"),
        "CollectionUtil.cloneMap(this.myField)", 
        CollectionUtil.class.getName());
    doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("INT_MAP_MAP"),
        "CollectionUtil.deepCloneMap(this.myField, CollectionUtil::cloneMap)", 
        CollectionUtil.class.getName());
    doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("INT_MAP_MAP_MAP"),
        "CollectionUtil.deepCloneMap(this.myField, m0 -> CollectionUtil.deepCloneMap(m0, CollectionUtil::cloneMap))", 
        CollectionUtil.class.getName());
    doTestGenerateDeepCloneFieldInstruction(Type.fromTypeName("OBJECT_MAP_LIST_MAP_LIST"),
        "CollectionUtil.deepCloneList(this.myField, m0 -> CollectionUtil.deepCloneMap(m0, l1 -> CollectionUtil.deepCloneList(l1, m2 -> CollectionUtil.deepCloneMap(m2, DeepCloneable::deepClone))))", 
        CollectionUtil.class.getName(),
        DeepCloneable.class.getName());
    Field f = Field.builder().name("myField").type(Type.OBJECT).objectName(Object.class.getName()).build();
    Assert.assertEquals("this.myField",
        PojoGenUtil.generateDeepCloneFieldInstruction(f, new Imports()));
        
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGenerateDeepCloneFieldInstruction_NullField() {
    PojoGenUtil.generateDeepCloneFieldInstruction(null, new Imports());
  }
  
  private void doTestGenerateDeepCloneFieldInstruction(Type type, String expectedInstruction, String... expectedImports) {
    Imports imports = new Imports();
    Field f = Field.builder().name("myField").type(type).build();
    Assert.assertEquals(expectedInstruction, PojoGenUtil.generateDeepCloneFieldInstruction(f, imports));
    Assert.assertEquals(expectedImports.length, imports.size());
    for (String expectedImport : expectedImports) {
      Assert.assertTrue(imports.contains(expectedImport));
    }
  }
  
  @Test
  public void testGenerateCompareFieldsInstruction() {
    doTestGenerateCompareFieldsInstruction(Type.INT, "CompareUtil.compare(this.myField, other.myField)");
    doTestGenerateCompareFieldsInstruction(Type.OBJECT, "CompareUtil.compare(this.myField, other.myField)");
    doTestGenerateCompareFieldsInstruction(Type.fromTypeName("INT_LIST"),
        "CompareUtil.compareLists(this.myField, other.myField, CompareUtil::compare)");
    doTestGenerateCompareFieldsInstruction(Type.fromTypeName("OBJECT_LIST"),
        "CompareUtil.compareLists(this.myField, other.myField, CompareUtil::compare)");
    doTestGenerateCompareFieldsInstruction(Type.fromTypeName("INT_LIST_LIST"),
        "CompareUtil.compareLists(this.myField, other.myField, (l0a, l0b) -> CompareUtil.compareLists(l0a,l0b, CompareUtil::compare))");
    doTestGenerateCompareFieldsInstruction(Type.fromTypeName("INT_LIST_LIST_LIST"),
        "CompareUtil.compareLists(this.myField, other.myField, (l0a, l0b) -> CompareUtil.compareLists(l0a,l0b, (l1a, l1b) -> CompareUtil.compareLists(l1a,l1b, CompareUtil::compare)))");
    doTestGenerateCompareFieldsInstruction(Type.fromTypeName("INT_MAP"),
        "CompareUtil.compareMaps(this.myField, other.myField, CompareUtil::compare)");
    doTestGenerateCompareFieldsInstruction(Type.fromTypeName("INT_MAP_MAP"),
        "CompareUtil.compareMaps(this.myField, other.myField, (m0a, m0b) -> CompareUtil.compareMaps(m0a,m0b, CompareUtil::compare))");
    doTestGenerateCompareFieldsInstruction(Type.fromTypeName("INT_MAP_MAP_MAP"),
        "CompareUtil.compareMaps(this.myField, other.myField, (m0a, m0b) -> CompareUtil.compareMaps(m0a,m0b, (m1a, m1b) -> CompareUtil.compareMaps(m1a,m1b, CompareUtil::compare)))");
    doTestGenerateCompareFieldsInstruction(Type.fromTypeName("OBJECT_MAP_LIST_MAP_LIST"),
        "CompareUtil.compareLists(this.myField, other.myField, (m0a, m0b) -> CompareUtil.compareMaps(m0a,m0b, (l1a, l1b) -> CompareUtil.compareLists(l1a,l1b, (m2a, m2b) -> CompareUtil.compareMaps(m2a,m2b, CompareUtil::compare))))");
    Field f = Field.builder().name("myField").type(Type.OBJECT).objectName(Object.class.getName()).build();
    Assert.assertEquals("CompareUtil.compareObjects(this.myField, other.myField)", PojoGenUtil.generateCompareFieldsInstruction(f));
  }
  
  private void doTestGenerateCompareFieldsInstruction(Type type, String expectedInstruction) {
    Field f = Field.builder().name("myField").type(type).build();
    Assert.assertEquals(expectedInstruction, PojoGenUtil.generateCompareFieldsInstruction(f));
  }
  
  @Test
  public void testGenerateSerialVersionUid() {
    String className = "com.x.y.MyClass";
    List<Field> fields = List.of(
        Field.builder().name("field1").type(Type.INT).build(),
        Field.builder().name("field2").type(Type.fromTypeName("OBJECT_LIST_MAP")).build()
      );
    List<String> implementedInterfaces = List.of("com.x.y.MyInterface", "com.x.z.MyOtherInterface");
    Assert.assertEquals(-1752733059768616242L, PojoGenUtil.generateSerialVersionUid(className, fields, implementedInterfaces));
  }
  
  @Test
  public void testGenerateDefaultValuesStaticFieldDeclarations() {
    List<Field> fields = List.of(
      Field.builder().type(Type.INT).name("score").description("Current score, max is ${constants.maxScore}").defaultValue(100).build(),
      Field.builder().type("STRING_LIST").name("profiles").description("Profile, one of ${constants.profiles}").defaultValue(List.of("${constants.profiles.admin}", "${constants.profiles.regular}")).build(),
      Field.builder().type(Type.STRING).name("a").description("Random 'a' property").defaultValue("aVal").build(),
      Field.builder().type(Type.STRING).name("A").description("Random 'A' property").defaultValue("AVal").build()
    );
    PlaceHolderResolver docPlaceHolderResolver = PlaceHolderResolver.create(Map.of("constants.maxScore", "100",
                                                                                   "constants.barName", "Happy hour",
                                                                                   "constants.profiles", "all_profiles"));
    PlaceHolderResolver defaultValuePlaceHolderResolver = PlaceHolderResolver
        .create(Map.of("constants.profiles.admin", "admin_profile", 
                      "constants.profiles.regular", "regular_profile"));
    PlaceHolderResolver defaultValuePlaceHolderResolverAsQuoteString = 
        s -> JavaCodeGenUtil.getQuotedString(defaultValuePlaceHolderResolver.resolve(s));
        
    Imports imports = new Imports();
    StringBuilder classBody = new StringBuilder();
    Map<String, String> res = PojoGenUtil.generateDefaultValuesStaticFieldDeclarations(
        fields, 
        imports, 
        docPlaceHolderResolver, 
        defaultValuePlaceHolderResolverAsQuoteString, 
        classBody);
    Assert.assertEquals(4, res.size());
    Assert.assertEquals("SCORE_DEFAULT_VALUE", res.get("score"));
    Assert.assertEquals("PROFILES_DEFAULT_VALUE", res.get("profiles"));
    Assert.assertEquals("A_DEFAULT_VALUE", res.get("a"));
    Assert.assertEquals("A_DEFAULT_VALUE_", res.get("A"));
    
    Assert.assertEquals("\n"
        + "/**\n"
        + " * Default value for <code>score</code>\n"
        + " */\n"
        + "public static final Integer SCORE_DEFAULT_VALUE = Integer.valueOf(\"100\");\n"
        + "\n"
        + "/**\n"
        + " * Default value for <code>profiles</code>\n"
        + " */\n"
        + "public static final List<String> PROFILES_DEFAULT_VALUE = new ListJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance()).deserialize(\"[\\\"admin_profile\\\",\\\"regular_profile\\\"]\");\n"
        + "\n"
        + "/**\n"
        + " * Default value for <code>a</code>\n"
        + " */\n"
        + "public static final String A_DEFAULT_VALUE = \"aVal\";\n"
        + "\n"
        + "/**\n"
        + " * Default value for <code>A</code>\n"
        + " */\n"
        + "public static final String A_DEFAULT_VALUE_ = \"AVal\";\n"
        + "", classBody.toString());
    
    Assert.assertEquals(3, imports.size());
    Assert.assertTrue(imports.contains(List.class.getName()));
    Assert.assertTrue(imports.contains(ListJsonFieldDeserializer.class.getName()));
    Assert.assertTrue(imports.contains(StringJsonFieldDeserializer.class.getName()));
  }
  
  @Test
  public void testGenerateDefaultValuesStaticFieldDeclarations_NoClassBody() {
    List<Field> fields = List.of(
      Field.builder().type(Type.INT).name("score").description("Current score, max is ${constants.maxScore}").defaultValue(100).build(),
      Field.builder().type("STRING_LIST").name("profiles").description("Profile, one of ${constants.profiles}").defaultValue(List.of("${constants.profiles.admin}", "${constants.profiles.regular}")).build(),
      Field.builder().type(Type.STRING).name("a").description("Random 'a' property").defaultValue("aVal").build(),
      Field.builder().type(Type.STRING).name("A").description("Random 'A' property").defaultValue("AVal").build()
    );
    PlaceHolderResolver docPlaceHolderResolver = PlaceHolderResolver.create(Map.of("constants.maxScore", "100",
                                                                                   "constants.barName", "Happy hour",
                                                                                   "constants.profiles", "all_profiles"));
    PlaceHolderResolver defaultValuePlaceHolderResolver = PlaceHolderResolver
        .create(Map.of("constants.profiles.admin", "admin_profile", 
                      "constants.profiles.regular", "regular_profile"));
    PlaceHolderResolver defaultValuePlaceHolderResolverAsQuoteString = 
        s -> JavaCodeGenUtil.getQuotedString(defaultValuePlaceHolderResolver.resolve(s));
        
    Imports imports = new Imports();
    Map<String, String> res = PojoGenUtil.generateDefaultValuesStaticFieldDeclarations(
        fields, 
        imports, 
        docPlaceHolderResolver, 
        defaultValuePlaceHolderResolverAsQuoteString, 
        null);
    Assert.assertEquals(4, res.size());
    Assert.assertEquals("SCORE_DEFAULT_VALUE", res.get("score"));
    Assert.assertEquals("PROFILES_DEFAULT_VALUE", res.get("profiles"));
    Assert.assertEquals("A_DEFAULT_VALUE", res.get("a"));
    Assert.assertEquals("A_DEFAULT_VALUE_", res.get("A"));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGenerateDefaultValuesStaticFieldDeclarations_InvalidObjectTypeFieldWithDefaultValue() {
    List<Field> fields = List.of(
      Field.builder()
        .type("OBJECT_LIST")
        .name("score")
        .description("Current score, max is ${constants.maxScore}")
        .defaultValue("[{\"name\": \"bob\", \"score\": 85},{\"name\": \"alice\",\"score\": 92}]")
        .build()
    );
        
    PojoGenUtil.generateDefaultValuesStaticFieldDeclarations(
        fields, 
        new Imports(), 
        PlaceHolderResolver.NO_OP, PlaceHolderResolver.NO_OP, 
        null);
  }
  
  @Test
  public void testGetFieldType_NullField() {
    Assert.assertNull(PojoGenUtil.getFieldType(null));
  }
  
  @Test
  public void testGetFieldType_ObjecListMapType() {
    Type objectListMapType = Type.fromTypeName("OBJECT_LIST_MAP");
    Assert.assertEquals(objectListMapType, 
        PojoGenUtil.getFieldType((Field.builder().name("test").type(objectListMapType).build())));
  }
  
  @Test
  public void testGetFieldType_ObjectType_Explicit() {
    Assert.assertEquals(Type.OBJECT, 
        PojoGenUtil.getFieldType(Field.builder().name("test").type(Type.OBJECT).build()));
  }
  
  @Test
  public void testGetFieldType_ObjectType_ImplicitFromObjectName() {
    Assert.assertEquals(Type.OBJECT, 
        PojoGenUtil.getFieldType(Field.builder().name("test").objectName("MyObjectName").build()));
  }
  
  @Test
  public void testGetFieldType_ObjectType_ImplicitFromProperties() {
    Assert.assertEquals(Type.OBJECT, 
        PojoGenUtil.getFieldType(Field.builder().name("test").properties(List.of()).build()));
  }
  
  @Test
  public void testGetFieldType_String_Implicit() {
    Assert.assertEquals(Type.STRING, 
        PojoGenUtil.getFieldType(Field.builder().name("test").build()));
  }
  
  @Test
  public void testIsObjectField_NullField() {
    Assert.assertFalse(PojoGenUtil.isObjectField(null));
  }
  
  @Test
  public void testIsObjectField_NullFieldType_ObjectTypeImplictFromProperties() {
    Assert.assertTrue(PojoGenUtil.isObjectField(Field.builder().name("test").properties(List.of()).build()));
  }
  
  @Test
  public void testIsObjectField_ObjectType() {
    Assert.assertTrue(PojoGenUtil.isObjectField(Field.builder().name("test").type(Type.OBJECT).build()));
  }
  
  @Test
  public void testIsObjectField_StringType() {
    Assert.assertFalse(PojoGenUtil.isObjectField(Field.builder().name("test").type(Type.STRING).build()));
  }
  
  @Test
  public void testGetClassNameForType_INT() {
    Assert.assertEquals("Integer", PojoGenUtil.getClassNameForType(Type.INT, new Imports(), null));
  }

  @Test
  public void testGetClassNameForType_STRING() {
    Assert.assertEquals("String", PojoGenUtil.getClassNameForType(Type.STRING, new Imports(), null));
  }

  @Test
  public void testGetClassNameForType_BOOLEAN() {
    Assert.assertEquals("Boolean", PojoGenUtil.getClassNameForType(Type.BOOLEAN, new Imports(), null));
  }

  @Test
  public void testGetClassNameForType_BIGDECIMAL() {
    Imports imports = new Imports();
    Assert.assertEquals("BigDecimal", PojoGenUtil.getClassNameForType(Type.BIGDECIMAL, imports, null));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(BigDecimal.class));
  }

  @Test
  public void testGetClassNameForType_BIGDECIMAL_NullImports() {
    Assert.assertEquals("BigDecimal", PojoGenUtil.getClassNameForType(Type.BIGDECIMAL, null, null));
  }

  @Test
  public void testGetClassNameForType_LONG() {
    Assert.assertEquals("Long", PojoGenUtil.getClassNameForType(Type.LONG, new Imports(), null));  
  }

  @Test
  public void testGetClassNameForType_STRING_LIST() {
    Imports imports = new Imports();
    Assert.assertEquals("List<String>", PojoGenUtil.getClassNameForType(Type.fromTypeName("STRING_LIST"), imports, null));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(List.class));
  }

  @Test
  public void testGetClassNameForType_STRING_LIST_NullImports() {
    Assert.assertEquals("List<String>", PojoGenUtil.getClassNameForType(Type.fromTypeName("STRING_LIST"), null, null));
  }

  @Test
  public void testGetClassNameForType_INT_MAP() {
    Imports imports = new Imports();
    Assert.assertEquals("Map<String, Integer>", PojoGenUtil.getClassNameForType(Type.fromTypeName("INT_MAP"), imports, null));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(Map.class));
  }

  @Test
  public void testGetClassNameForType_INT_MAP_NullImports() {
    Assert.assertEquals("Map<String, Integer>", PojoGenUtil.getClassNameForType(Type.fromTypeName("INT_MAP"), null, null));
  }

  @Test
  public void testGetClassNameForType_OBJECT() {
    Imports imports = new Imports();
    String objectClassName = "com.x.y.z.MyObject";
    Assert.assertEquals("MyObject", PojoGenUtil.getClassNameForType(Type.OBJECT, imports, objectClassName));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(objectClassName));
  }
  
  @Test
  public void testGetClassNameForType_OBJECT_NullImports() {
    String objectClassName = "com.x.y.z.MyObject";
    Assert.assertEquals("MyObject", PojoGenUtil.getClassNameForType(Type.OBJECT, null, objectClassName));
  }

  @Test
  public void testGetClassNameForType_OBJECT_LIST_MAP() {
    Imports imports = new Imports();
    String objectClassName = "com.x.y.z.MyObject";
    Assert.assertEquals("Map<String, List<MyObject>>", PojoGenUtil.getClassNameForType(Type.fromTypeName("OBJECT_LIST_MAP"), imports, objectClassName));
    Assert.assertEquals(3, imports.size());
    Assert.assertTrue(imports.contains(objectClassName));
    Assert.assertTrue(imports.contains(Map.class));
    Assert.assertTrue(imports.contains(List.class));
  }

  @Test
  public void testGetClassNameForType_NullType() {
    Assert.assertNull(PojoGenUtil.getClassNameForType(null, null, null));
  }
  
  @Test
public void testGetClassNameForField_STRING_Type() {
      Imports imports = new Imports();
      Field f = new Field();
      f.setType(Type.STRING);
      Assert.assertEquals(String.class.getSimpleName(), 
                PojoGenUtil.getClassNameForField(f, imports, null));
      Assert.assertEquals(0, imports.size());
  }

  @Test
public void testGetClassNameForField_BIGDECIMAL_Type() {
      Imports imports = new Imports();
      Field f = new Field();
      f.setType(Type.BIGDECIMAL);
      Assert.assertEquals(BigDecimal.class.getSimpleName(), 
                PojoGenUtil.getClassNameForField(f, imports, null));
      Assert.assertEquals(1, imports.size());
      Assert.assertTrue(imports.contains(BigDecimal.class.getName()));
  }

  @Test
  public void testGetClassNameForField_OBJECT_NoObjectName() {
      Imports imports = new Imports();
      Field f = new Field();
      f.setName("bar");
      f.setType(Type.OBJECT);
      Assert.assertEquals("FooBar", 
                PojoGenUtil.getClassNameForField(f, imports, "com.x.y.gen.pojo.Foo"));
      Assert.assertEquals(1, imports.size());
      Assert.assertTrue(imports.contains("com.x.y.gen.pojo.FooBar"));
  }

  @Test
  public void testGetClassNameForField_Implicit_OBJECT_WithObjectName() {
      Imports imports = new Imports();
      Field f = new Field();
      f.setName("bar");
      f.setObjectName("MyCustomObjectName");
      Assert.assertEquals("MyCustomObjectName", 
                PojoGenUtil.getClassNameForField(f, imports, "com.x.y.gen.pojo.Foo"));
      Assert.assertEquals(1, imports.size());
      Assert.assertTrue(imports.contains("com.x.y.gen.pojo.MyCustomObjectName"));
  }

  @Test
  public void testGetClassNameForField_OBJECT_MAP_LIST() {
      Imports imports = new Imports();
      Field f = new Field();
      f.setName("bar");
      f.setType(Type.fromTypeName("OBJECT_MAP_LIST"));
      Assert.assertEquals("List<Map<String, FooBar>>", 
                PojoGenUtil.getClassNameForField(f, imports, "com.x.y.gen.pojo.Foo"));
      Assert.assertEquals(3, imports.size());
      Assert.assertTrue(imports.contains("com.x.y.gen.pojo.FooBar"));
      Assert.assertTrue(imports.contains(Map.class.getName()));
      Assert.assertTrue(imports.contains(List.class.getName()));
  }
  
  @Test
  public void testGetFieldObjectClassName_WithSimpleClassObjectName() {
      Field f = new Field();
      f.setName("bar");
      f.setType(Type.OBJECT);
      f.setObjectName("MyCustomObjectName");
      Assert.assertEquals("com.x.y.gen.pojo.MyCustomObjectName", 
                PojoGenUtil.getFieldObjectClassName(f, "com.x.y.gen.pojo.Foo"));
  }
  
  @Test
  public void testGetFieldObjectClassName_WithFullClassObjectName() {
      Field f = new Field();
      f.setName("bar");
      f.setType(Type.OBJECT);
      f.setObjectName("com.y.MyCustomObjectName");
      Assert.assertEquals("com.y.MyCustomObjectName", 
                PojoGenUtil.getFieldObjectClassName(f, "com.x.y.gen.pojo.Foo"));
  }

  @Test
  public void testGetFieldObjectClassName_WithoutObjectName() {
      Field f = new Field();
      f.setName("bar");
      f.setType(Type.OBJECT);
      Assert.assertEquals("com.x.y.gen.pojo.FooBar", 
                PojoGenUtil.getFieldObjectClassName(f, "com.x.y.gen.pojo.Foo"));
  }
  
  @Test
  public void testGetFieldLeafSubTypeClassName_OBJECT() {
      String endpointParameterName = "bar";
      Assert.assertEquals("com.x.y.gen.pojo.FooBar", 
        PojoGenUtil.getFieldLeafSubTypeClassName(
          endpointParameterName, 
          Type.OBJECT, 
          null, 
          "com.x.y.gen.pojo.Foo"));
  }

  @Test
  public void testGetFieldLeafSubTypeClassName_OBJECT_LIST_MAP() {
      String endpointParameterName = "bar";
      Assert.assertEquals("com.x.y.gen.pojo.FooBar", 
        PojoGenUtil.getFieldLeafSubTypeClassName(
          endpointParameterName, 
          Type.fromTypeName("OBJECT_LIST_MAP"), 
          null, 
          "com.x.y.gen.pojo.Foo"));
  }
  
  @Test
  public void testGetFieldLeafSubTypeClassName_OBJECTWithObjectName() {
      String endpointParameterName = "bar";
      Assert.assertEquals("com.x.y.gen.pojo.MyPojo", 
        PojoGenUtil.getFieldLeafSubTypeClassName(
          endpointParameterName, 
          Type.OBJECT, 
          "MyPojo", 
          "com.x.y.gen.pojo.Foo"));
  }
  
  @Test
  public void testGetFieldLeafSubTypeClassName_INT() {
      Assert.assertEquals("java.lang.Integer", 
        PojoGenUtil.getFieldLeafSubTypeClassName(
            "bar", 
            Type.INT, 
            null, 
            "com.x.y.gen.pojo.Foo"));
  }
  
  @Test
  public void testFindPropertiesForObjectNameInField_NullField() {
    Assert.assertNull(PojoGenUtil.findPropertiesForObjectNameInField("MyPojo", null));
  }
  
  @Test
  public void testFindPropertiesForObjectNameInField_NullObjectName() {
      Assert.assertNull(PojoGenUtil.findPropertiesForObjectNameInField(null, new Field()));
  }
  
  @Test
  public void testFindPropertiesForObjectNameInField_NullFieldProperties() {
      Assert.assertNull(PojoGenUtil.findPropertiesForObjectNameInField("MyPojo", new Field()));
  }
  
  @Test
  public void testFindPropertiesForObjectNameInField_FieldCarriesExpectedObjectName() {
    String objectName = "MyPojo";
    Field f = new Field();
    f.setObjectName(objectName);
    
    Field prop = new Field();
    prop.setName("foo");
    prop.setType(Type.STRING);
    List<Field> expectedProperties = List.of(prop);
    f.setProperties(expectedProperties);
    Assert.assertEquals(expectedProperties, PojoGenUtil.findPropertiesForObjectNameInField(objectName, f));
  }
  
  @Test
  public void testFindPropertiesForObjectNameInField_FieldSubPropertyCarriesExpectedObjectName() {
    String objectName = "MyPojo";
    Field f = new Field();
    Field prop = new Field();
    prop.setName("foo");
    prop.setType(Type.OBJECT);
    prop.setObjectName(objectName);
    Field subProp = new Field();
    subProp.setName("bar");
    subProp.setType(Type.STRING);
    List<Field> expectedProperties = List.of(subProp);
    prop.setProperties(List.of(subProp));
    f.setProperties(List.of(prop));
    Assert.assertEquals(expectedProperties, PojoGenUtil.findPropertiesForObjectNameInField(objectName, f));
  }
  
  @Test
  public void testFindPropertiesForObjectNameInField_ObjectNameNotFound() {
    String objectName = "MyPojo";
    Field f = new Field();
    Field prop = new Field();
    prop.setName("foo");
    prop.setType(Type.OBJECT);
    Field subProp = new Field();
    subProp.setName("bar");
    subProp.setType(Type.STRING);
    prop.setProperties(List.of(subProp));
    f.setProperties(List.of(prop));
    Assert.assertNull(PojoGenUtil.findPropertiesForObjectNameInField(objectName, f));
  }
  
  @Test
  public void testGetMsgFieldName() {
    Assert.assertNull(PojoGenUtil.getMsgFieldName("foo", null));
    Field field1 = Field.builder().name("field1").type(Type.BOOLEAN).build();
    Assert.assertEquals("field1", PojoGenUtil.getMsgFieldName("field1", field1));
    Assert.assertNull(PojoGenUtil.getMsgFieldName("f", field1));
    Field field2 = Field.builder().name("field2").type(Type.BOOLEAN).msgField("f2").build();
    Assert.assertEquals("f2", PojoGenUtil.getMsgFieldName("field2", field2));
    Field objectField = Field.builder().name("objField").type(Type.OBJECT).property(field1).property(field2).build();
    Assert.assertEquals("objField", PojoGenUtil.getMsgFieldName("objField", objectField));
    Assert.assertEquals("field1", PojoGenUtil.getMsgFieldName("field1", objectField));
    Assert.assertEquals("f2", PojoGenUtil.getMsgFieldName("field2", objectField));
    Assert.assertNull(PojoGenUtil.getMsgFieldName("f", objectField));
  }
  
  @Test( expected = IllegalArgumentException.class)
  public void testGetMsgFieldName_NullName() {
    Field field1 = Field.builder().name("field1").type(Type.BOOLEAN).build();
    PojoGenUtil.getMsgFieldName(null, field1);
  }
  
  @Test
  public void testIsExternalClassObjectField() {
    FieldBuilder fb = Field.builder().name("f1");
    Assert.assertFalse(PojoGenUtil.isExternalClassObjectField(null));
    Assert.assertFalse(PojoGenUtil.isExternalClassObjectField(fb.type(Type.STRING).build()));
    Assert.assertFalse(PojoGenUtil.isExternalClassObjectField(fb.type(Type.OBJECT).build()));
    Assert.assertFalse(PojoGenUtil.isExternalClassObjectField(fb.type(Type.OBJECT).objectName("MyPojo").build()));
    Assert.assertFalse(PojoGenUtil.isExternalClassObjectField(fb.type(Type.OBJECT).objectName(Object.class.getName()).build()));
    Assert.assertTrue(PojoGenUtil.isExternalClassObjectField(fb.type(Type.OBJECT).objectName("com.x.y.MyPojo").build()));
  }
  
  @Test
  public void testIsRawObjectField() {
    FieldBuilder fb = Field.builder().name("f1");
    Assert.assertFalse(PojoGenUtil.isRawObjectField(null));
    Assert.assertFalse(PojoGenUtil.isRawObjectField(fb.type(Type.STRING).build()));
    Assert.assertFalse(PojoGenUtil.isRawObjectField(fb.type(Type.OBJECT).build()));
    Assert.assertFalse(PojoGenUtil.isRawObjectField(fb.type(Type.OBJECT).objectName("MyPojo").build()));
    Assert.assertTrue(PojoGenUtil.isRawObjectField(fb.type(Type.OBJECT).objectName(Object.class.getName()).build()));
    Assert.assertFalse(PojoGenUtil.isRawObjectField(fb.type(Type.OBJECT).objectName("com.x.y.MyPojo").build()));
  }
  
  @Test 
  public void testGetPrimitiveTypeFieldSampleValueDeclaration_NullSampleValue() {
    Imports imports = new Imports();
    Assert.assertEquals("null", PojoGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.INT, null, imports, null));
    Assert.assertEquals(0, imports.size());
  }

  @Test 
  public void testGetPrimitiveTypeFieldSampleValueDeclaration_BigDecimalSampleValue() {
    Imports imports = new Imports();
    Assert.assertEquals("new BigDecimal(\"1.23\")", PojoGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.BIGDECIMAL, "1.23", imports, null));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(BigDecimal.class));
  }

  @Test
  public void testGetPrimitiveTypeFieldSampleValueDeclaration_LongSampleValue() {
    Imports imports = new Imports();
    Assert.assertEquals("Long.valueOf(\"123\")", PojoGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.LONG, "123", imports, null));
    Assert.assertEquals(0, imports.size());
  }

  @Test
  public void testGetPrimitiveTypeFieldSampleValueDeclaration_LongpNowSampleValue() {
    Imports imports = new Imports();
    Assert.assertEquals("Long.valueOf(System.currentTimeMillis())", PojoGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.LONG, "now()", imports, null));
    Assert.assertEquals(0, imports.size());
  }

  @Test
  public void testGetPrimitiveTypeFieldSampleValueDeclaration_StringSampleValue() {
    Imports imports = new Imports();
    Assert.assertEquals("\"test\"", PojoGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.STRING, "test", imports, null));
    Assert.assertEquals(0, imports.size());
  }
  
  @Test
  public void testGetPrimitiveTypeFieldSampleValueDeclaration_BooleanSampleValue() {
    Imports imports = new Imports();
    Assert.assertEquals("Boolean.valueOf(\"true\")", PojoGenUtil.getPrimitiveTypeFieldSampleValueDeclaration(Type.BOOLEAN, "true", imports, null));
    Assert.assertEquals(0, imports.size());
  }
  
  @Test
  public void testGetNewJsonFieldDeserializerInstruction_INT() {
    Imports imports = new Imports();
    Assert.assertEquals("IntegerJsonFieldDeserializer.getInstance()", PojoGenUtil.getNewJsonFieldDeserializerInstruction(Type.INT, null, false, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(IntegerJsonFieldDeserializer.class));
  }

  @Test
  public void testGetNewJsonFieldDeserializerInstruction_BOOLEAN() {
    Imports imports = new Imports();
    Assert.assertEquals("BooleanJsonFieldDeserializer.getInstance()", PojoGenUtil.getNewJsonFieldDeserializerInstruction(Type.BOOLEAN, null, false, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(BooleanJsonFieldDeserializer.class));
  }

  @Test
  public void testGetNewJsonFieldDeserializerInstruction_BIGDECIMAL() {
    Imports imports = new Imports();
    Assert.assertEquals("BigDecimalJsonFieldDeserializer.getInstance()", PojoGenUtil.getNewJsonFieldDeserializerInstruction(Type.BIGDECIMAL, null, false, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(BigDecimalJsonFieldDeserializer.class));
  }

  @Test
  public void testGetNewJsonFieldDeserializerInstruction_LONG() {
    Imports imports = new Imports();
    Assert.assertEquals("LongJsonFieldDeserializer.getInstance()", PojoGenUtil.getNewJsonFieldDeserializerInstruction(Type.LONG, null, false, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(LongJsonFieldDeserializer.class));
  }

  @Test
  public void testGetNewJsonFieldDeserializerInstruction_STRING() {
    Imports imports = new Imports();
    Assert.assertEquals("StringJsonFieldDeserializer.getInstance()", PojoGenUtil.getNewJsonFieldDeserializerInstruction(Type.STRING, null, false, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(StringJsonFieldDeserializer.class));
  }
  
  @Test
  public void testGetNewJsonFieldDeserializerInstruction_STRING_LIST() {
    Imports imports = new Imports();
    Assert.assertEquals("new ListJsonFieldDeserializer<>(StringJsonFieldDeserializer.getInstance())", PojoGenUtil.getNewJsonFieldDeserializerInstruction(Type.fromTypeName("STRING_LIST"), null, false, imports));
    Assert.assertEquals(2, imports.size());
    Assert.assertTrue(imports.contains(ListJsonFieldDeserializer.class));
    Assert.assertTrue(imports.contains(StringJsonFieldDeserializer.class));
  }

  @Test
  public void testGetNewJsonFieldDeserializerInstruction_INT_MAP() {
    Imports imports = new Imports();
    Assert.assertEquals("new MapJsonFieldDeserializer<>(IntegerJsonFieldDeserializer.getInstance())", PojoGenUtil.getNewJsonFieldDeserializerInstruction(Type.fromTypeName("INT_MAP"), null, false, imports));
    Assert.assertEquals(2, imports.size());
    Assert.assertTrue(imports.contains(MapJsonFieldDeserializer.class));
    Assert.assertTrue(imports.contains(IntegerJsonFieldDeserializer.class));
  }

  @Test
  public void testGetNewJsonFieldDeserializerInstruction_OBJECT() {
    Imports imports = new Imports();
    String objectClassName = "com.x.gen.MyObject";
    Assert.assertEquals("new MyObjectDeserializer()", PojoGenUtil.getNewJsonFieldDeserializerInstruction(Type.fromTypeName("OBJECT"), objectClassName, false, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains("com.x.gen.deserializers.MyObjectDeserializer"));
  }

  @Test
  public void testGetNewJsonFieldDeserializerInstruction_NullType() {
    Imports imports = new Imports();
    Assert.assertEquals("MessageDeserializer.NO_OP", PojoGenUtil.getNewJsonFieldDeserializerInstruction(null, null, false, imports));
    Assert.assertEquals(1, imports.size());
    Assert.assertTrue(imports.contains(MessageDeserializer.class));
  }
  
  @Test
  public void testGetNewJsonFieldDeserializerInstructio_OBJECT() {
      Imports imports = new Imports();
      Assert.assertEquals("new MyMessageDeserializer()", 
          PojoGenUtil.getNewJsonFieldDeserializerInstruction(Type.OBJECT, "com.x.y.z.MyMessage", false,  imports));
      Assert.assertEquals(1, imports.size());
      Assert.assertTrue(imports.contains("com.x.y.z.deserializers.MyMessageDeserializer")); 
  }
  
  @Test
  public void testGetNewJsonFieldDeserializerInstructio_RawObject() {
      Imports imports = new Imports();
      Assert.assertEquals("RawObjectJsonFieldDeserializer.getInstance()", 
          PojoGenUtil.getNewJsonFieldDeserializerInstruction(Type.OBJECT, Object.class.getName(), false, imports));
      Assert.assertEquals(1, imports.size());
      Assert.assertTrue(imports.contains(RawObjectJsonFieldDeserializer.class)); 
  }
  
  @Test
  public void testGetNewJsonFieldDeserializerInstructio_GenericObject() {
      Imports imports = new Imports();
      Assert.assertEquals("new GenericObjectJsonFieldDeserializer<>(MyMessage.class)", 
          PojoGenUtil.getNewJsonFieldDeserializerInstruction(Type.OBJECT, "com.x.y.z.MyMessage", true, imports));
      Assert.assertEquals(2, imports.size());
      Assert.assertTrue(imports.contains(GenericObjectJsonFieldDeserializer.class));
      Assert.assertTrue(imports.contains("com.x.y.z.MyMessage"));
  }
  
  @Test
  public void testGetJsonMessageDeserializerClassName() {
    Assert.assertEquals("com.x.y.pojo.deserializers.MyObjectDeserializer", 
              PojoGenUtil.getJsonMessageDeserializerClassName("com.x.y.pojo.MyObject"));
  }
  
  @Test
  public void testGetNewJsonValueSerializerInstructionForPrimitiveTypes() {
      Imports imports = new Imports();

      String result = PojoGenUtil.getNewJsonValueSerializerInstruction(Type.BIGDECIMAL, null, false, imports);
      Assert.assertEquals("BigDecimalJsonValueSerializer.getInstance()", result);
      Assert.assertTrue(imports.contains("org.jxapi.netutils.serialization.json.BigDecimalJsonValueSerializer"));

      result = PojoGenUtil.getNewJsonValueSerializerInstruction(Type.BOOLEAN, null, false, imports);
      Assert.assertEquals("BooleanJsonValueSerializer.getInstance()", result);
      Assert.assertTrue(imports.contains("org.jxapi.netutils.serialization.json.BooleanJsonValueSerializer"));
      
      result = PojoGenUtil.getNewJsonValueSerializerInstruction(Type.INT, null, false, imports);
      Assert.assertEquals("IntegerJsonValueSerializer.getInstance()", result);
      Assert.assertTrue(imports.contains("org.jxapi.netutils.serialization.json.IntegerJsonValueSerializer"));
      
      result = PojoGenUtil.getNewJsonValueSerializerInstruction(Type.LONG, null, false, imports);
      Assert.assertEquals("LongJsonValueSerializer.getInstance()", result);
      Assert.assertTrue(imports.contains("org.jxapi.netutils.serialization.json.LongJsonValueSerializer"));
      
      result = PojoGenUtil.getNewJsonValueSerializerInstruction(Type.STRING, null, false, imports);
      Assert.assertEquals("StringJsonValueSerializer.getInstance()", result);
      Assert.assertTrue(imports.contains("org.jxapi.netutils.serialization.json.StringJsonValueSerializer"));
  }

  @Test
  public void testGetNewJsonValueSerializerInstructionForList() {
      Imports imports = new Imports();

      // Test for LIST with STRING subType
      Type listType = Type.fromTypeName("STRING_LIST");
      String result = PojoGenUtil.getNewJsonValueSerializerInstruction(listType, null, false, imports);
      Assert.assertEquals("new ListJsonValueSerializer<>(StringJsonValueSerializer.getInstance())", result);
      Assert.assertTrue(imports.contains("org.jxapi.netutils.serialization.json.ListJsonValueSerializer"));
      Assert.assertTrue(imports.contains("org.jxapi.netutils.serialization.json.StringJsonValueSerializer"));
  }

  @Test
  public void testGetNewJsonValueSerializerInstructionForMap() {
      Imports imports = new Imports();

      // Test for MAP with STRING subType
      Type mapType = Type.fromTypeName("STRING_MAP");
      String result = PojoGenUtil.getNewJsonValueSerializerInstruction(mapType, null, false, imports);
      Assert.assertEquals("new MapJsonValueSerializer<>(StringJsonValueSerializer.getInstance())", result);
      Assert.assertTrue(imports.contains("org.jxapi.netutils.serialization.json.MapJsonValueSerializer"));
      Assert.assertTrue(imports.contains("org.jxapi.netutils.serialization.json.StringJsonValueSerializer"));
  }

  @Test
  public void testGetNewJsonValueSerializerInstructionForExternalObject() {
      Imports imports = new Imports();

      // Test for OBJECT with external class
      String objectClassName = "com.example.MyObject";
      Type objectType = Type.OBJECT;
      String result = PojoGenUtil.getNewJsonValueSerializerInstruction(objectType, objectClassName, true, imports);
      Assert.assertEquals("ObjectJsonValueSerializer.getInstance()", result);
      Assert.assertTrue(imports.contains("org.jxapi.netutils.serialization.json.ObjectJsonValueSerializer"));
  }
  
  @Test
  public void testGetNewJsonValueSerializerInstructionForRawObject() {
      Imports imports = new Imports();

      // Test for OBJECT with external class
      String objectClassName = "java.lang.Object";
      Type objectType = Type.OBJECT;
      String result = PojoGenUtil.getNewJsonValueSerializerInstruction(objectType, objectClassName, true, imports);
      Assert.assertEquals("ObjectJsonValueSerializer.getInstance()", result);
      Assert.assertTrue(imports.contains("org.jxapi.netutils.serialization.json.ObjectJsonValueSerializer"));
  }
  
  @Test
  public void testGetNewJsonValueSerializerInstructionForObject() {
      Imports imports = new Imports();

      // Test for OBJECT with internal (in generator scope) class
      String objectClassName = "com.x.gen.MyObject";
      Type objectType = Type.OBJECT;
      String result = PojoGenUtil.getNewJsonValueSerializerInstruction(objectType, objectClassName, false, imports);
      Assert.assertEquals("new MyObjectSerializer()", result);
      Assert.assertTrue(imports.contains("com.x.gen.serializers.MyObjectSerializer"));
  }
  
  @Test
  public void testGetNewJsonValueSerializerNullType() {
      Imports imports = new Imports();
      String result = PojoGenUtil.getNewJsonValueSerializerInstruction(null, null, false, imports);
      Assert.assertEquals("MessageSerializer.NO_OP", result);
      Assert.assertTrue(imports.contains(MessageSerializer.class));
  }
  
  @Test
  public void testGetObjectDescription() {
    FieldBuilder fb = Field.builder().name("myField");
    Assert.assertNull(PojoGenUtil.getObjectDescription(fb.build()));
    fb.description("The field description");
    Assert.assertEquals("The field description", PojoGenUtil.getObjectDescription(fb.build()));
    fb.objectDescription("This is my field object description");
    Assert.assertEquals("This is my field object description", PojoGenUtil.getObjectDescription(fb.build()));
  }
}
