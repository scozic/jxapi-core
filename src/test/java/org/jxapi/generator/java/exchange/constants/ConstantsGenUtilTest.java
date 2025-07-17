package org.jxapi.generator.java.exchange.constants;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.exchange.descriptor.ConfigPropertyDescriptor;
import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.generator.java.Imports;
import org.jxapi.util.PlaceHolderResolver;

/**
 * Unit test for {@link ConstantsGenUtil}
 */
public class ConstantsGenUtilTest {

  @Test
  public void testCreateStringConstant() {
    Imports imports = new Imports();
    Constant c = Constant.create("myString", Type.STRING, null, "foo");
    Assert.assertEquals( "public static final String MY_STRING = \"foo\";\n", ConstantsGenUtil.generateConstantDeclaration(c, imports, null, null));
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
        ConstantsGenUtil.generateConstantDeclaration(c, imports, placeholderResolver, null));
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
         ConstantsGenUtil.generateConstantDeclaration(c, imports, null, null));
    Assert.assertEquals(0, imports.size());
  }

  @Test
  public void testCreateLongConstantWithNow() {
    Imports imports = new Imports();
    Constant c = Constant.create("myTimestamp", Type.LONG, null, "now()");
    Assert.assertEquals( "public static final Long MY_TIMESTAMP = Long.valueOf(System.currentTimeMillis());\n", ConstantsGenUtil.generateConstantDeclaration(c, imports, null, null));
    Assert.assertEquals(0, imports.size());
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateStringConstantInvalidType() {
    Constant c = Constant.create("myString", Type.fromTypeName("STRING_LIST"), null, "foo");
    ConstantsGenUtil.generateConstantDeclaration(c, new Imports(), null, null);
  }
  
  @Test
  public void testGetPropertyKeyPropertyName() {
    ConfigPropertyDescriptor p = ConfigPropertyDescriptor.create("myProp", Type.STRING, null, null);
    Assert.assertEquals("myProp", ConstantsGenUtil.getPropertyKeyPropertyName(p));
  }
  
  @Test
  public void testGetPropertyValueDeclaration() {
    ConfigPropertyDescriptor p = ConfigPropertyDescriptor.create("myProp", Type.STRING, "A test string value property, for instance '${constants.bar}'", "foo");
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
        ConstantsGenUtil.getPropertyValueDeclaration(p, imports, placeholderResolver, null));
  }
  
  @Test
  public void testGetPropertyValueDeclaration_ValueWithPlaceholder() {
    ConfigPropertyDescriptor p = ConfigPropertyDescriptor.create("myProp", Type.STRING, "A test string value property, for instance '${constants.bar}'", "${constants.foo}");
    Imports imports = new Imports();
    PlaceHolderResolver docPlaceholderResolver = PlaceHolderResolver.create(Map.of("constants.bar", "bar"));
    PlaceHolderResolver defaultValuePlaceholderResolver = PlaceHolderResolver.create(Map.of("constants.foo", "\"myFooValue\""));
    Assert.assertEquals("/**\n"
        + " * A test string value property, for instance 'bar'\n"
        + " */\n"
        + "public static final ConfigProperty MY_PROP = DefaultConfigProperty.create(\n"
        + "  \"myProp\",\n"
        + "  Type.STRING,\n"
        + "  \"A test string value property, for instance 'bar'\",\n"
        + "  \"myFooValue\");\n"
        + "", 
        ConstantsGenUtil.getPropertyValueDeclaration(p, imports, docPlaceholderResolver, defaultValuePlaceholderResolver));
  }

}
