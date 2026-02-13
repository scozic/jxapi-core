package org.jxapi.generator.java;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.jxapi.pojo.descriptor.Type;

/**
 * Unit test for {@link JavaCodeGenUtil}
 */
public class JavaCodeGenUtilTest {
  
  @Test
  public void testIndent() {
    Assert.assertEquals("  Hello,\n  World\n  !", JavaCodeGenUtil.indent("Hello,\nWorld\n!"));
  }
  
  @Test
  public void testGenerateCodeBlock() {
    String content = 
          "for (int i = 0; i < 10; i++) {\n"
        + "  System.out.println(\"Hello #\" + i);\n"
        + "}\n";
    
    String expected = 
          "{\n"
        + "  for (int i = 0; i < 10; i++) {\n"
        + "    System.out.println(\"Hello #\" + i);\n"
        + "  }\n"
        + "}\n";
    Assert.assertEquals(expected, JavaCodeGenUtil.generateCodeBlock(content));
  }

  @Test
  public void testGenerateJavaDoc() {
    String content = "Some piece of\n Javadoc";
    String expected = "/**\n"
            + " * Some piece of\n"
            + " *  Javadoc\n"
            + " */";
    Assert.assertEquals(expected, JavaCodeGenUtil.generateJavaDoc(content));
  }
  
  @Test
  public void testGenerateJavaDocNullContent() {
    Assert.assertEquals("", JavaCodeGenUtil.generateJavaDoc(null));
  }
  
  @Test
  public void testFirstLetterToUpperCase() {
    Assert.assertEquals("Hello", JavaCodeGenUtil.firstLetterToUpperCase("hello"));
  }
  
  @Test
  public void testFirstLetterToUpperCaseNullString() {
    Assert.assertEquals(null, JavaCodeGenUtil.firstLetterToUpperCase(null));
  }
  
  @Test
  public void testFirstLetterToUpperCaseEmptyString() {
    Assert.assertEquals("", JavaCodeGenUtil.firstLetterToUpperCase(""));
  }
  
  @Test
  public void testFirstLetterToLowerCase() {
    Assert.assertEquals("hellO", JavaCodeGenUtil.firstLetterToLowerCase("HellO"));
  }
  
  @Test
  public void testFirstLetterToLowerCaseNullString() {
    Assert.assertEquals(null, JavaCodeGenUtil.firstLetterToLowerCase(null));
  }
  
  @Test
  public void testFirstLetterToLowerCaseEmptyString() {
    Assert.assertEquals("", JavaCodeGenUtil.firstLetterToLowerCase(""));
  }
  
  @Test
  public void testGetStaticVariableName() {
    Assert.assertNull(JavaCodeGenUtil.getStaticVariableName(null));
    Assert.assertEquals("", JavaCodeGenUtil.getStaticVariableName(""));
    Assert.assertEquals("X", JavaCodeGenUtil.getStaticVariableName("x"));
    Assert.assertEquals("MY_VARIABLE_NAME0", JavaCodeGenUtil.getStaticVariableName("myVariableName0"));
  }
  
  @Test
  public void testClassPackage() {
    Assert.assertEquals("com.x.y.z", JavaCodeGenUtil.getClassPackage("com.x.y.z.Foo"));
  }
  
  @Test
  public void testClassPackageDefaultPackage() {
    Assert.assertEquals("", JavaCodeGenUtil.getClassPackage("Foo"));
  }
  
  @Test
  public void testClassPackageNullClassPackage() {
    Assert.assertEquals("", JavaCodeGenUtil.getClassPackage(null));
  }
  
  @Test
  public void testGetSetAccessorMethodName() {
    Assert.assertEquals("setBar", JavaCodeGenUtil.getSetAccessorMethodName("bar", List.of()));
  }
  
  @Test
  public void testGetAccessorMethodName() {
    Assert.assertEquals("setBar", JavaCodeGenUtil.getAccessorMethodName("set", "bar", List.of()));
  }
  
  @Test
  public void testGetAccessorMethodNameNullFieldList() {
    Assert.assertEquals("setBar", JavaCodeGenUtil.getAccessorMethodName("set", "bar", null));
  }
  
  @Test
  public void testGetAccessorMethodNameSameFieldDifferentCaseInFieldList() {
    Assert.assertEquals("setbAr", JavaCodeGenUtil.getAccessorMethodName("set", "bAr", List.of("BAR")));
  }
  
  @Test
  public void testGetAccessorMethodNameSameFieldInFieldList() {
    Assert.assertEquals("setBar", JavaCodeGenUtil.getAccessorMethodName("set", "bar", List.of("bar")));
  }
  
  @Test
  public void testGetGetAccessorMethodName_StringFieldType() {
    Assert.assertEquals("getBar", JavaCodeGenUtil.getGetAccessorMethodName("bar", Type.STRING, List.of()));
  }
  
  @Test
  public void testGetGetAccessorMethodName_booleanFieldType() {
    Assert.assertEquals("isBar", JavaCodeGenUtil.getGetAccessorMethodName("bar", Type.BOOLEAN, List.of("foo")));
  }
  
  @Test
  public void testGetClassNameWithoutPackage_FullClassName() {
    Assert.assertEquals("Bar", JavaCodeGenUtil.getClassNameWithoutPackage("com.x.y.z.Bar"));
  }
  
  @Test
  public void testGetClassNameWithoutPackage_NullClassName() {
    Assert.assertEquals(null, JavaCodeGenUtil.getClassNameWithoutPackage(null));
  }
  
  @Test
  public void testGetClassNameWithoutPackage_SimpleClassName() {
    Assert.assertEquals("Bar", JavaCodeGenUtil.getClassNameWithoutPackage("Bar"));
  }
  
  @Test
  public void testGetClassNameWithoutPackage_SimpleGenericClassNameWithFullClassNameVariableType() {
    Assert.assertEquals("Bar<com.x.y.z.Foo>", JavaCodeGenUtil.getClassNameWithoutPackage("Bar<com.x.y.z.Foo>"));
  }
  
  @Test
  public void testGetClassNameWithoutPackage_FullGenericClassNameWithFullClassNameVariableType() {
    Assert.assertEquals("Bar<com.x.y.z.Foo>", JavaCodeGenUtil.getClassNameWithoutPackage("com.x.y.z.Bar<com.x.y.z.Foo>"));
  }
  
  @Test
  public void testDeletePathPathDoesNotExist() throws IOException {
    File dir = new File("tmpDir" + Math.random());
    Assert.assertFalse(dir.exists());
    JavaCodeGenUtil.deletePath(dir.toPath());
    Assert.assertFalse(new File(dir.getName()).exists());
  }
  
  @Test
  public void testDeletePathPathExistsAndContainsSubDirAndFiles() throws IOException {
    File dir = new File("tmpDir" + Math.random());
    Assert.assertFalse(new File(dir.getName()).exists());
    dir.mkdir();
    File f1 = new File(dir, "file1");
    f1.createNewFile();
    File subDir = new File(dir, "subDir");
    subDir.mkdir();
    File f2 = new File(subDir, "file2");
    f2.createNewFile();
    File f3 = new File(subDir, "file3");
    f3.createNewFile();
    JavaCodeGenUtil.deletePath(dir.toPath());
    Assert.assertFalse(new File(dir.getName()).exists());
  }
  
  @Test
  public void testGenerateSlf4jLoggerDeclaration() {
    JavaTypeGenerator gen = new JavaTypeGenerator("com.foo.Bar");
    gen.setTypeDeclaration("class");
    JavaCodeGenUtil.generateSlf4jLoggerDeclaration(gen);
    Assert.assertEquals("package com.foo;\n"
        + "\n"
        + "import javax.annotation.processing.Generated;\n"
        + "import org.slf4j.Logger;\n"
        + "import org.slf4j.LoggerFactory;\n"
        + "\n"
        + "\n"
        + "@Generated(\"org.jxapi.generator.java.JavaTypeGenerator\")\n"
        + "class Bar {\n"
        + "  private static final Logger log = LoggerFactory.getLogger(Bar.class);\n"
        + "  \n"
        + "}\n", gen.generate());
  }
  
  @Test
  public void testGetClassWithoutGenericType_NoGenericType() {
    Assert.assertEquals("com.x.y.z.Bar", JavaCodeGenUtil.getClassNameWithoutGenericType("com.x.y.z.Bar"));
  }
  
  @Test
  public void testGetClassWithoutGenericType_GenericType() {
    Assert.assertEquals("com.x.y.z.Bar", JavaCodeGenUtil.getClassNameWithoutGenericType("com.x.y.z.Bar<java.util.List<com.x.y.z.Foo>>"));
  }

  @Test
  public void testGetClassNameWithoutGenericType_NullGenericType() {
    Assert.assertNull(JavaCodeGenUtil.getClassNameWithoutGenericType(null));
  }
  
  @Test
  public void testGetGenericType_NoGenericType() {
    Assert.assertNull(JavaCodeGenUtil.getGenericType("com.x.y.z.Bar"));
  }
  
  @Test
  public void testGetGenericType_GenericType() {
    Assert.assertEquals("java.util.List<com.x.y.z.Foo>", 
              JavaCodeGenUtil.getGenericType("com.x.y.z.Bar<java.util.List<com.x.y.z.Foo>>"));
  }
  
  @Test
  public void testGetGenericType_InvalidGenericType() {
    try {
      JavaCodeGenUtil.getGenericType("com.x.y.z.Bar<");
    } catch(IllegalArgumentException ex) {
      return;
    }
    Assert.fail();
  }

  @Test
  public void testGetGenericType_NullGenericType() {
    Assert.assertNull(JavaCodeGenUtil.getGenericType(null));
  }
  
  @Test
  public void testGetQuotedString_NullString() {
    Assert.assertEquals(null, JavaCodeGenUtil.getQuotedString(null));
  }
  
  @Test
  public void testGetQuotedString_EmptyString() {
    Assert.assertEquals("\"\"", JavaCodeGenUtil.getQuotedString(""));
  }
  
  @Test
  public void testGetQuotedString_StringWithQuotes() {
    Assert.assertEquals("\"Hello \\\"World\\\"\"", 
              JavaCodeGenUtil.getQuotedString("Hello \"World\""));
  }
  
  @Test
  public void testGetQuotedString_StringWithQuotesAndLineFeed() {
    Assert.assertEquals("\"Hello\\n\\\"World\\\"\"", 
              JavaCodeGenUtil.getQuotedString("Hello\n\"World\""));
  }
  
  @Test
  public void testGetHtmlLink() {
    Assert.assertEquals("<a href=\"https://scam.org\">Click here</a>", 
              JavaCodeGenUtil.getHtmlLink("https://scam.org", "Click here"));
  }

  @Test
  public void testGenerateTryBlock_TryWithResourcesWithCatchAndFinally() {
    Assert.assertEquals("try(InputStream in = new FileInputStream(file)) {\n"
                + "  throw new IOException();\n"
                + "}\n"
                + "catch (IOException ex) {\n"
                + "  log.error(ex);\n"
                + "}\n"
                + "finally {\n"
                + "  return null;\n"
                + "}\n", 
              JavaCodeGenUtil.generateTryBlock(
                "throw new IOException();", 
                "log.error(ex);", 
                "IOException ex", 
                "return null;", 
                "InputStream in = new FileInputStream(file)"));
  }

  @Test
  public void testGenerateTryBlock_TryWithResourcesWithFinally() {
    Assert.assertEquals("try(InputStream in = new FileInputStream(file)) {\n"
                + "  throw new IOException();\n"
                + "}\n"
                + "finally {\n"
                + "  return null;\n"
                + "}\n", 
              JavaCodeGenUtil.generateTryBlock(
                "throw new IOException();", 
                null, 
                null, 
                "return null;", 
                "InputStream in = new FileInputStream(file)"));
  }

  @Test
  public void testGenerateTryBlock_TryCatch() {
    Assert.assertEquals("try {\n"
                + "  throw new IOException();\n"
                + "}\n"
                + "catch (IOException ex) {\n"
                + "  log.error(ex);\n"
                + "}\n", 
              JavaCodeGenUtil.generateTryBlock(
                "throw new IOException();", 
                "log.error(ex);", 
                "IOException ex", 
                null, 
                null));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGenerateTryBlock_TryCatchMissingCatchException() {
    JavaCodeGenUtil.generateTryBlock(
        "throw new IOException();",
        "log.error(ex);",
        null,
        null,
        null);
  }
  
  @Test
  public void testGetMethodJavadocArgumentDeclaration_NullArgClass() {
    Assert.assertEquals("", JavaCodeGenUtil.getMethodJavadocArgumentDeclaration(null));
  }
  
  @Test
  public void testGetMethodJavadocArgumentDeclaration_SimpleArgClass() {
    Assert.assertEquals(Integer.class.getName(), JavaCodeGenUtil.getMethodJavadocArgumentDeclaration(Integer.class.getName()));
  }
  
  @Test
  public void testGetMethodJavadocArgumentDeclaration_GenericArgClass() {
    Assert.assertEquals("List", JavaCodeGenUtil.getMethodJavadocArgumentDeclaration("List<Map<String, Integer>>"));
  }
  
  @Test
  public void testGetClassJavadocUrl() {
    Assert.assertEquals("https://docs.oracle.com/javase/8/docs/api/java/util/List.html", 
              JavaCodeGenUtil.getClassJavadocUrl("https://docs.oracle.com/javase/8/docs/api/", List.class.getName()));
  }
  
  @Test
  public void testGetClassUrl() {
    Assert.assertEquals("https://docs.oracle.com/javase/8/docs/api/java/util/List.html", 
        JavaCodeGenUtil.getClassUrl("https://docs.oracle.com/javase/8/docs/api/", List.class.getName(), null, ".html"));
  }
  
  @Test
  public void testGetClassUrl_InnerClass() {
    Assert.assertEquals("https://docs.oracle.com/javase/8/docs/api/java/util/List.MyInnerClass.MySubInnerClass.html", 
        JavaCodeGenUtil.getClassUrl("https://docs.oracle.com/javase/8/docs/api/", List.class.getName(), "MyInnerClass.MySubInnerClass", ".html"));
  }
  
  @Test
  public void testIsValidCamelCaseIdentifier() {
    Assert.assertFalse(JavaCodeGenUtil.isValidCamelCaseIdentifier(null));
    Assert.assertFalse(JavaCodeGenUtil.isValidCamelCaseIdentifier(""));
    Assert.assertFalse(JavaCodeGenUtil.isValidCamelCaseIdentifier("9Wrong"));
    Assert.assertFalse(JavaCodeGenUtil.isValidCamelCaseIdentifier("wrong-identifier"));
    Assert.assertFalse(JavaCodeGenUtil.isValidCamelCaseIdentifier("Wrong_identifier"));
    Assert.assertTrue(JavaCodeGenUtil.isValidCamelCaseIdentifier("wrong_identifier"));
    Assert.assertTrue(JavaCodeGenUtil.isValidCamelCaseIdentifier("myVariableName"));
    Assert.assertTrue(JavaCodeGenUtil.isValidCamelCaseIdentifier("myVariableName0"));
  }
  
  @Test
  public void testGetMethodArgumentJavaDoc() {
    Assert.assertEquals("", JavaCodeGenUtil.getMethodArgumentJavadoc(null, null));
    Assert.assertEquals(Integer.class.getName(), JavaCodeGenUtil.getMethodArgumentJavadoc(Type.INT, null));
    Assert.assertEquals(List.class.getName(), JavaCodeGenUtil.getMethodArgumentJavadoc(Type.fromTypeName("INT_LIST"), null));
    Assert.assertEquals(Map.class.getName(), JavaCodeGenUtil.getMethodArgumentJavadoc(Type.fromTypeName("OBJECT_MAP"), "com.foo.Bar"));
    Assert.assertEquals("com.foo.Bar", JavaCodeGenUtil.getMethodArgumentJavadoc(Type.OBJECT, "com.foo.Bar")); 
  }
  
  @Test
  public void testGetJavaDocLink_NullLink() {
    Assert.assertNull(JavaCodeGenUtil.getJavaDocLink(null));
  }
  
  @Test
  public void testGetClassJavaDocLink() {
    Assert.assertEquals("{@link com.x.y.Foo}", JavaCodeGenUtil.getJavaDocLink("com.x.y.Foo"));
    Assert.assertEquals("{@link com.x.y.Foo}", JavaCodeGenUtil.getJavaDocLink("com.x.y.Foo", null));
  }
  
  @Test
  public void testGetJavaDocLinkForAttribute() {
    Assert.assertEquals("{@link com.x.y.Foo#BAR}", JavaCodeGenUtil.getJavaDocLink("com.x.y.Foo", "BAR"));
  }
  
  @Test
  public void testGenerateOptionalOfNullableStatement() {
    testGenerateOptionalOfNullableStatement("bar", false, null, "bar", false);
    testGenerateOptionalOfNullableStatement("bar", false, "bar", null, false);
    testGenerateOptionalOfNullableStatement("bar", false, null, "bar", true);
    testGenerateOptionalOfNullableStatement("bar", false, "bar", null, true);
    
    testGenerateOptionalOfNullableStatement("Optional.ofNullable(foo).orElse(bar)", true, "foo", "bar", false);
    testGenerateOptionalOfNullableStatement("Optional\n"
        + "  .ofNullable(foo)\n"
        + "  .orElse(bar)", true, "foo", "bar", true);
  }
  
  @Test
  public void testGetUniqueCamelCaseVariableNamesLowerCase() {
    List<String> names = List.of("MyVariable", "aa", "Aa");
    Map<String, String> uniqueNames = JavaCodeGenUtil.getUniqueCamelCaseVariableNames(names, false);
    Assert.assertEquals(3, names.size());
    Assert.assertEquals("myVariable", uniqueNames.get("MyVariable"));
    Assert.assertEquals("aa", uniqueNames.get("aa"));
    Assert.assertEquals("Aa", uniqueNames.get("Aa"));
  }
  
  @Test
  public void testGetUniqueCamelCaseVariableNamesUpperCase() {
    List<String> names = List.of("myVariable", "aa", "Aa");
    Map<String, String> uniqueNames = JavaCodeGenUtil.getUniqueCamelCaseVariableNames(names, true);
    Assert.assertEquals(3, names.size());
    Assert.assertEquals("MyVariable", uniqueNames.get("myVariable"));
    Assert.assertEquals("aa", uniqueNames.get("aa"));
    Assert.assertEquals("Aa", uniqueNames.get("Aa"));
  }
  
  private void testGenerateOptionalOfNullableStatement(
      String expected, 
      boolean optionalClassImported,
      String ofNullableStatement, 
      String orElseStatemet, 
      boolean multiline) {
    Imports imports = new Imports();
    String actual = JavaCodeGenUtil.generateOptionalOfNullableStatement(ofNullableStatement, orElseStatemet, imports, multiline);
    Assert.assertEquals(expected, actual);
    if (optionalClassImported) {
      Assert.assertEquals(1, imports.size());
      Assert.assertTrue(imports.contains(Optional.class));
    }
  }
  
  @Test
  public void testFindPlaceHolders() {
    Assert.assertEquals(0, JavaCodeGenUtil.findPlaceHolders(null).size());
    String value = "Hello ${name} you are using exchange ${exchange.name}";
    List<String> placeHolders = JavaCodeGenUtil.findPlaceHolders(value);
    Assert.assertEquals(2, placeHolders.size());
    Assert.assertEquals("name", placeHolders.get(0));
    Assert.assertEquals("exchange.name", placeHolders.get(1));
  }
  
  @Test
  public void testIsFullClassName() {
    Assert.assertFalse(JavaCodeGenUtil.isFullClassName(null));
    Assert.assertFalse(JavaCodeGenUtil.isFullClassName(""));
    Assert.assertFalse(JavaCodeGenUtil.isFullClassName("MyClass"));
    Assert.assertTrue(JavaCodeGenUtil.isFullClassName("com.x.y.MyClass"));
  }
  
}

