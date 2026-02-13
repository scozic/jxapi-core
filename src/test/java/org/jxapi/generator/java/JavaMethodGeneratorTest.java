package org.jxapi.generator.java;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link JavaMethodGenerator}.
 */
public class JavaMethodGeneratorTest {

  @Test
  public void testGenerateJavaMethodNoJavaDoc() {
    JavaMethodGenerator generator = new JavaMethodGenerator();
    generator.setMedthodDeclaration("public static int foo() throws FooException");
    generator.appendToBody("//Dummy\n");
    generator.appendToBody("return 1;\n");
    Assert.assertEquals(
      "public static int foo() throws FooException {\n"
      + "  //Dummy\n"
      + "  return 1;\n"
      + "}\n", 
      generator.generate());
  }
  
  @Test
  public void testJavaMethodGeneratorToString() {
    JavaMethodGenerator generator = new JavaMethodGenerator();
    generator.setMedthodDeclaration("public static int foo() throws FooException");
    generator.appendToBody("return 1;\n");
    generator.setJavadoc("foo!\n@return 1\n");
    Assert.assertEquals("JavaMethodGenerator{\"medthodDeclaration\":\"public static int foo() throws FooException\",\"javadoc\":\"foo!\\n@return 1\\n\"}", generator.toString());
  }
  
  @Test
  public void testGenerateJavaMethodWithJavaDoc() {
    JavaMethodGenerator generator = new JavaMethodGenerator();
    generator.setMedthodDeclaration("public static int parseInt(String intAsString) throws NumberFormatException");
    generator.appendToBody("return Integer.parseInt(intAsString);\n");
    generator.setJavadoc("Parses string as int\n"
               + "@param intAsString a String representing an int value\n"
               + "@return int value of <code>stringAsInt</code>\n"
               + "@throws NumberFormatException if argument is not a valid int value");
    Assert.assertEquals(
            "/**\n"
            + " * Parses string as int\n"
            + " * @param intAsString a String representing an int value\n"
            + " * @return int value of <code>stringAsInt</code>\n"
            + " * @throws NumberFormatException if argument is not a valid int value\n"
            + " */\n"
            + "public static int parseInt(String intAsString) throws NumberFormatException {\n"
            + "  return Integer.parseInt(intAsString);\n"
            + "}\n", 
      generator.generate());
  }
}
