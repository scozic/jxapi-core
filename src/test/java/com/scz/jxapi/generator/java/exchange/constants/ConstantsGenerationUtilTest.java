package com.scz.jxapi.generator.java.exchange.constants;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.DefaultConfigProperty;
import com.scz.jxapi.exchange.descriptor.ConfigProperty;
import com.scz.jxapi.exchange.descriptor.Constant;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.generator.java.Imports;

/**
 * Unit test for {@link ConstantsGenerationUtil}
 */
public class ConstantsGenerationUtilTest {

	@Test
	public void testCreateStringConstant() {
		Imports imports = new Imports();
		Constant c = Constant.create("myString", Type.STRING, null, "foo");
		Assert.assertEquals( "String MY_STRING = \"foo\";\n", ConstantsGenerationUtil.generateConstantDeclaration(c, imports));
		Assert.assertEquals(0, imports.size());
	}

	@Test
	public void testCreateIntConstant_StringValue() {
		Imports imports = new Imports();
		Constant c = Constant.create("myInt", Type.INT, "My int constant with string value", "42");
		Assert.assertEquals( "/**\n"
						   + " * My int constant with string value\n"
						   + " */\n"
						   + "Integer MY_INT = Integer.valueOf(42);\n", 
							 ConstantsGenerationUtil.generateConstantDeclaration(c, imports));
		Assert.assertEquals(0, imports.size());
	}

	@Test
	public void testCreateIntConstant_IntValue() {
		Imports imports = new Imports();
		Constant c = Constant.create("myInt", Type.INT, "My int constant with int value", 42);
		Assert.assertEquals( "/**\n"
						   + " * My int constant with int value\n"
						   + " */\n"
						   + "Integer MY_INT = Integer.valueOf(42);\n", 
							 ConstantsGenerationUtil.generateConstantDeclaration(c, imports));
		Assert.assertEquals(0, imports.size());
	}

	@Test
	public void testCreateLongConstantWithNow() {
		Imports imports = new Imports();
		Constant c = Constant.create("myTimestamp", Type.TIMESTAMP, null, "now()");
		Assert.assertEquals( "Long MY_TIMESTAMP = Long.valueOf(System.currentTimeMillis());\n", ConstantsGenerationUtil.generateConstantDeclaration(c, imports));
		Assert.assertEquals(0, imports.size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateStringConstantInvalidType() {
		Constant c = Constant.create("myString", Type.fromTypeName("STRING_LIST"), null, "foo");
		ConstantsGenerationUtil.generateConstantDeclaration(c, new Imports());
	}

	@Test
	public void testGetConstantsForProperties_PropertyWithDefaultValueAndDescription() {
		DefaultConfigProperty p = DefaultConfigProperty.create("myProp", Type.BIGDECIMAL, "My property description", "1.2345");
		Constant c1 = Constant.create("myPropProperty", Type.STRING, "'myProp' property key.<br>\nMy property description<br>\nProperty value type:BIGDECIMAL", "myProp");
		Constant c2 = Constant.create("myPropDefaultValue", Type.BIGDECIMAL, "{@link #MY_PROP_PROPERTY} property default value", "1.2345");
		List<Constant> expected = List.of(c1, c2);
		List<Constant> actual = ConstantsGenerationUtil.getConstantsForProperties(List.of(p));
		Assert.assertEquals(expected.size(), actual.size());
		for (int i = 0; i < expected.size(); i++) {
			Constant expectedC = expected.get(i);
			Constant actualC = actual.get(i);
			Assert.assertEquals(expectedC.getName(), actualC.getName());
			Assert.assertEquals(expectedC.getType(), actualC.getType());
			Assert.assertEquals(expectedC.getDescription(), actualC.getDescription());
			Assert.assertEquals(expectedC.getValue(), actualC.getValue());
		}
	}
	
	@Test
	public void testGetConstantsForProperties_PropertyWithNullDefaultValueAndNullDescription() {
		DefaultConfigProperty p = DefaultConfigProperty.create("myProp", Type.BIGDECIMAL, null, null);
		Constant expectedC = Constant.create("myPropProperty", Type.STRING, "'myProp' property key.<br>\nProperty value type:BIGDECIMAL", "myProp");
		List<Constant> actual = ConstantsGenerationUtil.getConstantsForProperties(List.of(p));
		Assert.assertEquals(1, actual.size());
		Constant actualC = actual.get(0);
		Assert.assertEquals(expectedC.getName(), actualC.getName());
		Assert.assertEquals(expectedC.getType(), actualC.getType());
		Assert.assertEquals(expectedC.getDescription(), actualC.getDescription());
		Assert.assertEquals(expectedC.getValue(), actualC.getValue());
	}
	
	@Test
	public void testGetPropertyKeyPropertyName() {
		ConfigProperty p = DefaultConfigProperty.create("myProp", Type.STRING, null, null);
		Assert.assertEquals("myPropProperty", ConstantsGenerationUtil.getPropertyKeyPropertyName(p));
	}
	
	@Test
	public void testGetPropertyDefaultValueName() {
		ConfigProperty p = DefaultConfigProperty.create("myProp", Type.STRING, null, null);
		Assert.assertEquals("myPropDefaultValue", ConstantsGenerationUtil.getPropertyDefaultValuePropertyName(p));
	}

}
