package org.jxapi.generator.java.pojo;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.generator.java.Imports;
import org.jxapi.generator.java.JavaCodeGenUtil;
import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.StringJsonFieldDeserializer;
import org.jxapi.pojo.descriptor.Field;
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
        Assert.assertEquals("com.x.y.serializers.MyPojoSerializer", 
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
}
