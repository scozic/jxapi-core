package com.scz.jxapi.generator.java;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link PojoGenerator}
 */
public class PojoGeneratorTest {

	@Test
	public void testGeneratePojoCodeMultipleFields() {
		PojoGenerator generator = new PojoGenerator("x.y.z.Foo");
		generator.addField(PojoField.create("String", "name", null, "the name"));
		generator.addField(PojoField.create("x.y.t.Bar", "bar", "b", "my bar"));
		generator.addField(PojoField.create("int", "a", null, "lower case 'a'"));
		generator.addField(PojoField.create("int", "A", null, "upper case 'A'"));
		Assert.assertEquals("package x.y.z;\n"
				+ "\n"
				+ "import java.util.Objects;\n"
				+ "\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
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
				+ "  public int getA() {\n"
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
				+ "  public int geta() {\n"
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
				+ "   * @return my bar Message field <strong>b</strong>\n"
				+ "   */\n"
				+ "  public Bar getBar() {\n"
				+ "    return bar;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @param bar my bar Message field <strong>b</strong>\n"
				+ "   */\n"
				+ "  public void setBar(Bar bar) {\n"
				+ "    this.bar = bar;\n"
				+ "  }\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return the name\n"
				+ "   */\n"
				+ "  public String getName() {\n"
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
				+ "  public boolean equals(Object other) {\n"
				+ "    if (other == null)\n"
				+ "      return false;\n"
				+ "    if (!getClass().equals(other.getClass()))\n"
				+ "      return false;\n"
				+ "    Foo o = (Foo) other;\n"
				+ "    return Objects.equals(A, o.A)\n"
				+ "            && Objects.equals(a, o.a)\n"
				+ "            && Objects.equals(bar, o.bar)\n"
				+ "            && Objects.equals(name, o.name);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public int hashCode() {\n"
				+ "    return Objects.hash(A, a, bar, name);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "}\n", 
				generator.generate());
	}
	
	@Test
	public void testGeneratePojoCodeZeroField() {
		PojoGenerator generator = new PojoGenerator("x.y.z.Foo");
		Assert.assertEquals("package x.y.z;\n"
				+ "\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "\n"
				+ "\n"
				+ "public class Foo {\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public boolean equals(Object other) {\n"
				+ "    if (other == null)\n"
				+ "      return false;\n"
				+ "    if (!getClass().equals(other.getClass()))\n"
				+ "      return false;\n"
				+ "    return true;\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public int hashCode() {\n"
				+ "    return 31 * getClass().hashCode();\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "}\n"
				+ ""
				, generator.generate());
	}
	
	@Test
	public void testGeneratePojoCodeOneField() {
		PojoGenerator generator = new PojoGenerator("x.y.z.Foo");
		generator.addField(PojoField.create("String", "name", null, "the name"));
		Assert.assertEquals("package x.y.z;\n"
				+ "\n"
				+ "import java.util.Objects;\n"
				+ "\n"
				+ "import com.scz.jxapi.util.EncodingUtil;\n"
				+ "\n"
				+ "\n"
				+ "public class Foo {\n"
				+ "  private String name;\n"
				+ "  \n"
				+ "  /**\n"
				+ "   * @return the name\n"
				+ "   */\n"
				+ "  public String getName() {\n"
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
				+ "  public boolean equals(Object other) {\n"
				+ "    if (other == null)\n"
				+ "      return false;\n"
				+ "    if (!getClass().equals(other.getClass()))\n"
				+ "      return false;\n"
				+ "    Foo o = (Foo) other;\n"
				+ "    return Objects.equals(name, o.name);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public int hashCode() {\n"
				+ "    return Objects.hash(name);\n"
				+ "  }\n"
				+ "  \n"
				+ "  @Override\n"
				+ "  public String toString() {\n"
				+ "    return EncodingUtil.pojoToString(this);\n"
				+ "  }\n"
				+ "}\n", 
				generator.generate());
	}

	@Test
	public void testGetFields() {
		PojoGenerator generator = new PojoGenerator("x.y.z.Foo");
		Assert.assertEquals(0, generator.getFields().length);
		generator.addField(PojoField.create("String", "name", null, "the name"));
		Assert.assertEquals(1, generator.getFields().length);
		PojoField f0 = generator.getFields()[0];
		Assert.assertEquals("name", f0.getName());
		Assert.assertEquals("String", f0.getType());
		Assert.assertEquals("the name", f0.getDescription());
		Assert.assertNull(f0.getMsgField());
	}
	
	
}
