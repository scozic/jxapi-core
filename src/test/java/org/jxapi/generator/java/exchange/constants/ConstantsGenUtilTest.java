package org.jxapi.generator.java.exchange.constants;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.generator.java.Imports;
import org.jxapi.netutils.deserialization.json.field.IntegerJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.ListJsonFieldDeserializer;
import org.jxapi.netutils.deserialization.json.field.MapJsonFieldDeserializer;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.JsonUtil;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Unit test for {@link ConstantsGenUtil}
 */
public class ConstantsGenUtilTest {

  @Test
  public void testCreateStringConstant() {
    Imports imports = new Imports();
    Constant c = Constant.create("myString", Type.STRING, null, "foo");
    Assert.assertEquals( "public static final String MY_STRING = \"foo\";\n", 
        ConstantsGenUtil.generateConstantDeclaration(
            c, 
            List.of(c), 
            imports, 
            null, 
            null));
    Assert.assertEquals(0, imports.size());
  }

  @Test
  public void testCreateIntConstant_StringValue() {
    Imports imports = new Imports();
    Constant c = Constant.create("myInt", Type.INT, "My int constant with string value, for instance '${constants.sampleMyInt}'", "42");
    PlaceHolderResolver placeholderResolver = PlaceHolderResolver.create(Map.of("constants.sampleMyInt", "123"));
    Assert.assertEquals( "/**\n"
        + " * My int constant with string value, for instance '123'\n"
        + " */\n"
        + "public static final Integer MY_INT = Integer.valueOf(\"42\");\n", 
        ConstantsGenUtil.generateConstantDeclaration(c, List.of(c), imports, placeholderResolver, null));
    Assert.assertEquals(0, imports.size());
  }

  @Test
  public void testCreateIntConstant_IntValue() {
    Imports imports = new Imports();
    Constant c = Constant.create("myInt", Type.INT, "My int constant with int value", 42);
    Assert.assertEquals( "/**\n"
        + " * My int constant with int value\n"
        + " */\n"
        + "public static final Integer MY_INT = Integer.valueOf(\"42\");\n", 
         ConstantsGenUtil.generateConstantDeclaration(c, List.of(c), imports, null, null));
    Assert.assertEquals(0, imports.size());
  }
  
  @Test
  public void testCreateIntConstant_IntMapListValueWithValueAsMapObject() {
    Imports imports = new Imports();
    List<Map<String, Integer>> value = List.of(Map.of("a", 1, "b", 2), Map.of("c", 3));
    Constant c = Constant.create("myInt", Type.fromTypeName("INT_MAP_LIST"), "My int constant with list of map with integer values", value);
    Assert.assertEquals( "/**\n"
        + " * My int constant with list of map with integer values\n"
        + " */\n"
        + "public static final List<Map<String, Integer>> MY_INT = new ListJsonFieldDeserializer<>(new MapJsonFieldDeserializer<>(IntegerJsonFieldDeserializer.getInstance())).deserialize(\"[{\\\"a\\\":1,\\\"b\\\":2},{\\\"c\\\":3}]\");\n"
        + "", 
         ConstantsGenUtil.generateConstantDeclaration(c, List.of(c), imports, null, null));
    Assert.assertEquals(5, imports.size());
    Assert.assertTrue(imports.contains(Map.class.getName()));
    Assert.assertTrue(imports.contains(List.class.getName()));
    Assert.assertTrue(imports.contains(IntegerJsonFieldDeserializer.class.getName()));
    Assert.assertTrue(imports.contains(ListJsonFieldDeserializer.class.getName()));
    Assert.assertTrue(imports.contains(MapJsonFieldDeserializer.class.getName()));
  }
  
  @Test
  public void testCreateIntConstant_IntMapListValueWithValueAsStringObject() {
    Imports imports = new Imports();
    String value = JsonUtil.pojoToJsonString(List.of(Map.of("a", 1, "b", 2), Map.of("c", 3)));
    Constant c = Constant.create("myInt", Type.fromTypeName("INT_MAP_LIST"), "My int constant with list of map with integer values", value);
    Assert.assertEquals( "/**\n"
        + " * My int constant with list of map with integer values\n"
        + " */\n"
        + "public static final List<Map<String, Integer>> MY_INT = new ListJsonFieldDeserializer<>(new MapJsonFieldDeserializer<>(IntegerJsonFieldDeserializer.getInstance())).deserialize(\"[{\\\"a\\\":1,\\\"b\\\":2},{\\\"c\\\":3}]\");\n"
        + "", 
         ConstantsGenUtil.generateConstantDeclaration(c, List.of(c), imports, null, null));
    Assert.assertEquals(5, imports.size());
    Assert.assertTrue(imports.contains(Map.class.getName()));
    Assert.assertTrue(imports.contains(List.class.getName()));
    Assert.assertTrue(imports.contains(IntegerJsonFieldDeserializer.class.getName()));
    Assert.assertTrue(imports.contains(ListJsonFieldDeserializer.class.getName()));
    Assert.assertTrue(imports.contains(MapJsonFieldDeserializer.class.getName()));
  }

  @Test
  public void testCreateLongConstantWithNow() {
    Imports imports = new Imports();
    Constant c = Constant.create("myTimestamp", Type.LONG, null, "now()");
    Assert.assertEquals(
      "public static final Long MY_TIMESTAMP = Long.valueOf(System.currentTimeMillis());\n", 
      ConstantsGenUtil.generateConstantDeclaration(c, List.of(c), imports, null, null));
    Assert.assertEquals(0, imports.size());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateStringConstantInvalidType() {
    Constant c = Constant.create("myString", Type.fromTypeName("OBJECT_LIST"), null, "foo");
    ConstantsGenUtil.generateConstantDeclaration(c, List.of(c), new Imports(), null, null);
  }
  
  @Test
  public void testGetConstantVariableName_SimpleConstant() {
    Constant c = Constant.create("myConstant", Type.STRING, null, "foo");
    Assert.assertEquals("MY_CONSTANT", ConstantsGenUtil.getConstantVariableName(c, List.of(c)));
  }
  
  @Test
  public void testGetConstantVariableName_ConstantNameConflictingWithSieblings() {
    Constant c1 = Constant.create("c1", Type.STRING, null, "c1Value");
    Constant c1Up = Constant.create("x.C1", Type.STRING, null, "c2Value");
    Constant c1Underscore = Constant.create("c1_", Type.STRING, null, "c1Value");
    Constant c1UpUnderscore = Constant.createGroup("C1_", null, List.of(Constant.create("sub", Type.STRING, null, "subValue")));
    List<Constant> all = List.of(c1, c1Up, c1Underscore, c1UpUnderscore);
    Assert.assertEquals("C1", ConstantsGenUtil.getConstantVariableName(c1, all));
    Assert.assertEquals("C1_", ConstantsGenUtil.getConstantVariableName(c1Up, all));
    Assert.assertEquals("C1__", ConstantsGenUtil.getConstantVariableName(c1Underscore, all));
    Assert.assertEquals("C1___", ConstantsGenUtil.getConstantVariableName(c1UpUnderscore, all));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testGetConstantVariableNameConstantNotProvidedInSieblings() {
    Constant c = Constant.create("myConstant", Type.STRING, null, "foo");
    ConstantsGenUtil.getConstantVariableName(c, List.of());
  }

}
