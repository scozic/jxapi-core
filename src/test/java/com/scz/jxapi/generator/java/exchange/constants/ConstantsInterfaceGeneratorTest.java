package com.scz.jxapi.generator.java.exchange.constants;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.scz.jxapi.exchange.descriptor.Constant;
import com.scz.jxapi.exchange.descriptor.Type;

/**
 * Unit test for {@link ConstantsInterfaceGenerator}
 */
public class ConstantsInterfaceGeneratorTest {

	@Test
	public void testGenerateInterfaceNoConstants() {
		ConstantsInterfaceGenerator gen = new ConstantsInterfaceGenerator("com.x.y.MyConstants", List.of());
		Assert.assertEquals("package com.x.y;\n"
				+ "\n"
				+ "\n"
				+ "\n"
				+ "public interface MyConstants {\n"
				+ "  \n"
				+ "}\n", gen.generate());
	}
	
	@Test
	public void testGenerateInterfaceMultipleConstants() {
		Constant c01 = Constant.create("myString", Type.STRING, null, "foo");
		Constant c02 = Constant.create("myInt", Type.INT, "A test Integer constant", 12);
		Constant c03 = Constant.create("myIntWithValueAsString", Type.INT, "A test Integer constant with value specified as String", "123");
		Constant c04 = Constant.create("myBool", Type.BOOLEAN, "A test Boolean constant", true);
		Constant c05 = Constant.create("myBoolWithValueAsString", Type.INT, "A test Boolean constant with value specified as String", "true");
		Constant c06 = Constant.create("myLong", Type.LONG, "A test Long constant", 1234567890123456L);
		Constant c07 = Constant.create("myLongWithValueAsString", Type.LONG, "A test Long constant with value specified as String", "12345678901234567");
		Constant c08 = Constant.create("myTimestamp", Type.TIMESTAMP, "A test Timestamp constant using 'now()' placeholder", "now()");
		Constant c09 = Constant.create("myLong", Type.BIGDECIMAL, "A test BigDecimal constant", 1.2345);
		Constant c10 = Constant.create("myBigDecimalWithValueAsString", Type.BIGDECIMAL, "A test BigDecimal constant with value specified as String", "5.4321");		
		
		ConstantsInterfaceGenerator gen = new ConstantsInterfaceGenerator("com.x.y.MyConstants", List.of(c01, c02, c03, c04, c05, c06, c07, c08, c09, c10));
		Assert.assertEquals("package com.x.y;\n"
				+ "\n"
				+ "import java.math.BigDecimal;\n"
				+ "\n"
				+ "\n"
				+ "public interface MyConstants {\n"
				+ "  \n"
				+ "  String MY_STRING = \"foo\";\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * A test Integer constant\n"
				+ "   */\n"
				+ "  Integer MY_INT = Integer.valueOf(12);\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * A test Integer constant with value specified as String\n"
				+ "   */\n"
				+ "  Integer MY_INT_WITH_VALUE_AS_STRING = Integer.valueOf(123);\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * A test Boolean constant\n"
				+ "   */\n"
				+ "  Boolean MY_BOOL = Boolean.valueOf(true);\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * A test Boolean constant with value specified as String\n"
				+ "   */\n"
				+ "  Integer MY_BOOL_WITH_VALUE_AS_STRING = Integer.valueOf(true);\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * A test Long constant\n"
				+ "   */\n"
				+ "  Long MY_LONG = Long.valueOf(\"1234567890123456\");\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * A test Long constant with value specified as String\n"
				+ "   */\n"
				+ "  Long MY_LONG_WITH_VALUE_AS_STRING = Long.valueOf(\"12345678901234567\");\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * A test Timestamp constant using 'now()' placeholder\n"
				+ "   */\n"
				+ "  Long MY_TIMESTAMP = Long.valueOf(System.currentTimeMillis());\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * A test BigDecimal constant\n"
				+ "   */\n"
				+ "  BigDecimal MY_LONG = new BigDecimal(\"1.2345\");\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * A test BigDecimal constant with value specified as String\n"
				+ "   */\n"
				+ "  BigDecimal MY_BIG_DECIMAL_WITH_VALUE_AS_STRING = new BigDecimal(\"5.4321\");\n"
				+ "}\n"
				+ "", gen.generate());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstantNotAPrimitiveType() {
		new ConstantsInterfaceGenerator("com.x.y.MyConstants", List.of(Constant.create("myConst", Type.fromTypeName("INT_LIST"), "My description", "[1, 2, 3]"))).generate();
	}
}
