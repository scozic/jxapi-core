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
		generator.addField(PojoField.create("int", "a", "lower case 'a'"));
		generator.addField(PojoField.create("int", "A", "upper case 'A'"));
		Assert.assertEquals("package x.y.z;\n"
				+ "\n"
				+ "import com.scz.jcex.util.EncodingUtil;\n"
				+ "import x.y.t.Bar;\n"
				+ "\n"
				+ "\n"
				+ "public class Foo {\n"
				+ "  private int A;\n"
				+ "  private int a;\n"
				+ "  private Bar bar;\n"
				+ "  private String name;\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return upper case 'A'\n"
				+ "   */\n"
				+ "  public int getA(){\n"
				+ "    return A;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param A upper case 'A'\n"
				+ "   */\n"
				+ "  public void setA(int A) {\n"
				+ "    this.A = A;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return lower case 'a'\n"
				+ "   */\n"
				+ "  public int geta(){\n"
				+ "    return a;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param a lower case 'a'\n"
				+ "   */\n"
				+ "  public void seta(int a) {\n"
				+ "    this.a = a;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return my bar\n"
				+ "   */\n"
				+ "  public Bar getBar(){\n"
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
				+ "  public String getName(){\n"
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
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.formatArgsToJsonStruct(\"A\", A, \"a\", a, \"bar\", bar, \"name\", name);\n"
				+ "  }\n"
				+ "  \n"
				+ "}\n"
				+ "" , generator.generate());
	}
	
}
