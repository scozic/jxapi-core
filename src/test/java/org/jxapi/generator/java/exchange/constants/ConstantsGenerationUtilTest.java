package org.jxapi.generator.java.exchange.constants;

import org.junit.Assert;
import org.junit.Test;

import org.jxapi.exchange.descriptor.ConfigProperty;
import org.jxapi.exchange.descriptor.Constant;
import org.jxapi.exchange.descriptor.DefaultConfigProperty;
import org.jxapi.exchange.descriptor.Type;
import org.jxapi.generator.java.Imports;

/**
 * Unit test for {@link ConstantsGenerationUtil}
 */
public class ConstantsGenerationUtilTest {

	@Test
	public void testCreateStringConstant() {
		Imports imports = new Imports();
		Constant c = Constant.create("myString", Type.STRING, null, "foo");
		Assert.assertEquals( "public static final String MY_STRING = \"foo\";\n", ConstantsGenerationUtil.generateConstantDeclaration(c, imports));
		Assert.assertEquals(0, imports.size());
	}

	@Test
	public void testCreateIntConstant_StringValue() {
		Imports imports = new Imports();
		Constant c = Constant.create("myInt", Type.INT, "My int constant with string value", "42");
		Assert.assertEquals( "/**\n"
				+ " * My int constant with string value\n"
				+ " */\n"
				+ "public static final Integer MY_INT = Integer.valueOf(42);\n", 
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
				+ "public static final Integer MY_INT = Integer.valueOf(42);\n"
				+ "", 
				 ConstantsGenerationUtil.generateConstantDeclaration(c, imports));
		Assert.assertEquals(0, imports.size());
	}

	@Test
	public void testCreateLongConstantWithNow() {
		Imports imports = new Imports();
		Constant c = Constant.create("myTimestamp", Type.TIMESTAMP, null, "now()");
		Assert.assertEquals( "public static final Long MY_TIMESTAMP = Long.valueOf(System.currentTimeMillis());\n", ConstantsGenerationUtil.generateConstantDeclaration(c, imports));
		Assert.assertEquals(0, imports.size());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testCreateStringConstantInvalidType() {
		Constant c = Constant.create("myString", Type.fromTypeName("STRING_LIST"), null, "foo");
		ConstantsGenerationUtil.generateConstantDeclaration(c, new Imports());
	}
	
	@Test
	public void testGetPropertyKeyPropertyName() {
		ConfigProperty p = DefaultConfigProperty.create("myProp", Type.STRING, null, null);
		Assert.assertEquals("myProp", ConstantsGenerationUtil.getPropertyKeyPropertyName(p));
	}

}
