package com.scz.jcex.generator;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link JavaCodeGenerationUtil}
 */
public class JUJavaCodeGenerationUtil {
	
	@Test
	public void testIndent() {
		Assert.assertEquals("  Hello,\n  World\n  !", JavaCodeGenerationUtil.indent("Hello,\nWorld\n!"));
	}
	
	@Test
	public void testGenerateCodeBlock() {
		String content = 
				  "for (int i = 0; i < 10; i++) {\n"
				+ "  System.out.println(\"Hello #\" + i);\n"
				+ "}";
		
		String expected = 
				  "{\n"
				+ "  for (int i = 0; i < 10; i++) {\n"
				+ "    System.out.println(\"Hello #\" + i);\n"
				+ "  }\n"
				+ "}\n";
		Assert.assertEquals(expected, JavaCodeGenerationUtil.generateCodeBlock(content));
	}

	/**
	 * Some piece of Javadoc
	 */
	@Test
	public void testGenerateJavaDoc() {
		String content = "Some piece of\n Javadoc";
		String expected = "/**\n"
						+ " * Some piece of\n"
						+ " *  Javadoc\n"
						+ " */";
		Assert.assertEquals(expected, JavaCodeGenerationUtil.generateJavaDoc(content));
	}
	
	@Test
	public void testGenerateJavaPojoFieldsWithAccessors() {
		List<PojoField> l = Arrays.asList(
								PojoField.create("String", "foo"),
								PojoField.create("boolean", "bar"),
								PojoField.create("com.xyz.Toto", "myToto", "you know toto?")
							);
		String actual = JavaCodeGenerationUtil.generateJavaPojoFieldsWithAccessors(l);
		Assert.assertEquals("private String foo;\n"
				+ "private boolean bar;\n"
				+ "private Toto myToto;\n"
				+ "\n"
				+ "public String getFoo(){\n"
				+ "  return foo;\n"
				+ "}\n"
				+ "\n"
				+ "public void setFoo(String foo) {\n"
				+ "  this.foo = foo;\n"
				+ "}\n"
				+ "\n"
				+ "public boolean isBar(){\n"
				+ "  return bar;\n"
				+ "}\n"
				+ "\n"
				+ "public void setBar(boolean bar) {\n"
				+ "  this.bar = bar;\n"
				+ "}\n"
				+ "\n"
				+ "/**\n"
				+ " * @return you know toto?\n"
				+ " */\n"
				+ "public Toto getMyToto(){\n"
				+ "  return myToto;\n"
				+ "}\n"
				+ "\n"
				+ "/**\n"
				+ " * @param myToto you know toto?\n"
				+ " */\n"
				+ "public void setMyToto(Toto myToto) {\n"
				+ "  this.myToto = myToto;\n"
				+ "}\n\n"
				, actual);
	}
	
	@Test
	public void testFirstLetterToUpperCase() {
		Assert.assertEquals("Hello", JavaCodeGenerationUtil.firstLetterToUpperCase("hello"));
	}
	
	@Test
	public void testFirstLetterToLowerCase() {
		Assert.assertEquals("hellO", JavaCodeGenerationUtil.firstLetterToLowerCase("HellO"));
	}
	
}
