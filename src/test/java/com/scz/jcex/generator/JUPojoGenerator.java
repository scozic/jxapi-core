package com.scz.jcex.generator;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link PojoGenerator}
 */
public class JUPojoGenerator {

	@Test
	public void testGeneratePojoCode() {
		PojoGenerator generator = new PojoGenerator("x.y.z.Foo");
		generator.addField(PojoField.create("String", "name", "the name"));
		generator.addField(PojoField.create("x.y.t.Bar", "bar", "my bar"));
		Assert.assertEquals("package x.y.z;\n"
				+ "\n"
				+ "import com.scz.jcex.util.EncodingUtil;\n"
				+ "import x.y.t.Bar;\n"
				+ "\n"
				+ "\n"
				+ "public class Foo {\n"
				+ "  private Bar bar;\n"
				+ "  private String name;\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return my bar\n"
				+ "   */\n"
				+ "  public void getBar(Bar bar) {\n"
				+ "    return bar;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param bar my bar\n"
				+ "   */\n"
				+ "  public void setBar(Bar bar) {\n"
				+ "    this.bar = bar;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return the name\n"
				+ "   */\n"
				+ "  public void getName(String name) {\n"
				+ "    return name;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param name the name\n"
				+ "   */\n"
				+ "  public void setName(String name) {\n"
				+ "    this.name = name;\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString(){\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "  \n"
				+ "}\n" , generator.generate());
	}
	
}
