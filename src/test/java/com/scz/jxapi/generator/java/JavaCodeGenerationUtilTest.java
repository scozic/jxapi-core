package com.scz.jxapi.generator.java;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link JavaCodeGenerationUtil}
 */
public class JavaCodeGenerationUtilTest {
	
	@Test
	public void testIndent() {
		Assert.assertEquals("  Hello,\n  World\n  !", JavaCodeGenerationUtil.indent("Hello,\nWorld\n!"));
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
		Assert.assertEquals(expected, JavaCodeGenerationUtil.generateCodeBlock(content));
	}

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
	public void testGenerateJavaDocNullContent() {
		Assert.assertEquals("", JavaCodeGenerationUtil.generateJavaDoc(null));
	}
	
	@Test
	public void testGenerateJavaPojoFieldsWithAccessors() {
		List<PojoField> l = Arrays.asList(
								PojoField.create("String", "foo"),
								PojoField.create("boolean", "bar", "b", null),
								PojoField.create("com.xyz.Toto", "myToto", "mt", "you know toto?")
							);
		String actual = JavaCodeGenerationUtil.generateJavaPojoFieldsWithAccessors(l);
		Assert.assertEquals("private String foo;\n"
				+ "private boolean bar;\n"
				+ "private Toto myToto;\n"
				+ "\n"
				+ "public String getFoo() {\n"
				+ "  return foo;\n"
				+ "}\n"
				+ "\n"
				+ "public void setFoo(String foo) {\n"
				+ "  this.foo = foo;\n"
				+ "}\n"
				+ "\n"
				+ "/**\n"
				+ " * @return  Message field <strong>b</strong>\n"
				+ " */\n"
				+ "public boolean isBar() {\n"
				+ "  return bar;\n"
				+ "}\n"
				+ "\n"
				+ "/**\n"
				+ " * @param bar  Message field <strong>b</strong>\n"
				+ " */\n"
				+ "public void setBar(boolean bar) {\n"
				+ "  this.bar = bar;\n"
				+ "}\n"
				+ "\n"
				+ "/**\n"
				+ " * @return you know toto? Message field <strong>mt</strong>\n"
				+ " */\n"
				+ "public Toto getMyToto() {\n"
				+ "  return myToto;\n"
				+ "}\n"
				+ "\n"
				+ "/**\n"
				+ " * @param myToto you know toto? Message field <strong>mt</strong>\n"
				+ " */\n"
				+ "public void setMyToto(Toto myToto) {\n"
				+ "  this.myToto = myToto;\n"
				+ "}\n"
				+ "\n"
				+ ""
				, actual);
	}
	
	@Test
	public void testFirstLetterToUpperCase() {
		Assert.assertEquals("Hello", JavaCodeGenerationUtil.firstLetterToUpperCase("hello"));
	}
	
	@Test
	public void testFirstLetterToUpperCaseNullString() {
		Assert.assertEquals(null, JavaCodeGenerationUtil.firstLetterToUpperCase(null));
	}
	
	@Test
	public void testFirstLetterToUpperCaseEmptyString() {
		Assert.assertEquals("", JavaCodeGenerationUtil.firstLetterToUpperCase(""));
	}
	
	@Test
	public void testFirstLetterToLowerCase() {
		Assert.assertEquals("hellO", JavaCodeGenerationUtil.firstLetterToLowerCase("HellO"));
	}
	
	@Test
	public void testFirstLetterToLowerCaseNullString() {
		Assert.assertEquals(null, JavaCodeGenerationUtil.firstLetterToLowerCase(null));
	}
	
	@Test
	public void testFirstLetterToLowerCaseEmptyString() {
		Assert.assertEquals("", JavaCodeGenerationUtil.firstLetterToLowerCase(""));
	}
	
	@Test
	public void testGetStaticVariableName() {
		Assert.assertNull(JavaCodeGenerationUtil.getStaticVariableName(null));
		Assert.assertEquals("", JavaCodeGenerationUtil.getStaticVariableName(""));
		Assert.assertEquals("X", JavaCodeGenerationUtil.getStaticVariableName("x"));
		Assert.assertEquals("MY_VARIABLE_NAME0", JavaCodeGenerationUtil.getStaticVariableName("myVariableName0"));
	}
	
	@Test
	public void testClassPackage() {
		Assert.assertEquals("com.x.y.z", JavaCodeGenerationUtil.getClassPackage("com.x.y.z.Foo"));
	}
	
	@Test
	public void testClassPackageDefaultPackage() {
		Assert.assertEquals("", JavaCodeGenerationUtil.getClassPackage("Foo"));
	}
	
	@Test
	public void testClassPackageNullClassPackage() {
		Assert.assertEquals("", JavaCodeGenerationUtil.getClassPackage(null));
	}
	
	@Test
	public void testGetAccessorMethodName() {
		Assert.assertEquals("setBar", JavaCodeGenerationUtil.getAccessorMethodName("set", "bar", List.of()));
	}
	
	@Test
	public void testGetAccessorMethodNameNullFieldList() {
		Assert.assertEquals("setBar", JavaCodeGenerationUtil.getAccessorMethodName("set", "bar", null));
	}
	
	@Test
	public void testGetAccessorMethodNameSameFieldDifferentCaseInFieldList() {
		Assert.assertEquals("setbAr", JavaCodeGenerationUtil.getAccessorMethodName("set", "bAr", List.of("BAR")));
	}
	
	@Test
	public void testGetAccessorMethodNameSameFieldInFieldList() {
		Assert.assertEquals("setBar", JavaCodeGenerationUtil.getAccessorMethodName("set", "bar", List.of("bar")));
	}
	
	@Test
	public void testGetGetAccessorMethodName_StringFieldType() {
		Assert.assertEquals("getBar", JavaCodeGenerationUtil.getGetAccessorMethodName("bar", "String", List.of()));
	}
	
	@Test
	public void testGetGetAccessorMethodName_booleanFieldType() {
		Assert.assertEquals("isBar", JavaCodeGenerationUtil.getGetAccessorMethodName("bar", "boolean", List.of()));
	}
	
	@Test
	public void testGetGetAccessorMethodName_JavaLangBooleanFieldType() {
		Assert.assertEquals("isBar", JavaCodeGenerationUtil.getGetAccessorMethodName("bar", Boolean.class.getName(), List.of()));
	}
	
	@Test
	public void testGetGetAccessorMethodName_BooleanFieldType() {
		Assert.assertEquals("isBar", JavaCodeGenerationUtil.getGetAccessorMethodName("bar", Boolean.class.getSimpleName(), List.of()));
	}
	
	@Test
	public void testGetClassNameWithoutPackage_FullClassName() {
		Assert.assertEquals("Bar", JavaCodeGenerationUtil.getClassNameWithoutPackage("com.x.y.z.Bar"));
	}
	
	@Test
	public void testGetClassNameWithoutPackage_NullClassName() {
		Assert.assertEquals(null, JavaCodeGenerationUtil.getClassNameWithoutPackage(null));
	}
	
	@Test
	public void testGetClassNameWithoutPackage_SimpleClassName() {
		Assert.assertEquals("Bar", JavaCodeGenerationUtil.getClassNameWithoutPackage("Bar"));
	}
	
	@Test
	public void testGetClassNameWithoutPackage_SimpleGenericClassNameWithFullClassNameVariableType() {
		Assert.assertEquals("Bar<com.x.y.z.Foo>", JavaCodeGenerationUtil.getClassNameWithoutPackage("Bar<com.x.y.z.Foo>"));
	}
	
	@Test
	public void testGetClassNameWithoutPackage_FullGenericClassNameWithFullClassNameVariableType() {
		Assert.assertEquals("Bar<com.x.y.z.Foo>", JavaCodeGenerationUtil.getClassNameWithoutPackage("com.x.y.z.Bar<com.x.y.z.Foo>"));
	}
	
	@Test
	public void testDeletePathPathDoesNotExist() throws IOException {
		File dir = new File("tmpDir" + Math.random());
		Assert.assertFalse(dir.exists());
		JavaCodeGenerationUtil.deletePath(dir.toPath());
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
		JavaCodeGenerationUtil.deletePath(dir.toPath());
		Assert.assertFalse(new File(dir.getName()).exists());
	}
	
	@Test
	public void testGenerateSlf4jLoggerDeclaration() {
		JavaTypeGenerator gen = new JavaTypeGenerator("com.foo.Bar");
		gen.setTypeDeclaration("class");
		JavaCodeGenerationUtil.generateSlf4jLoggerDeclaration(gen);
		Assert.assertEquals("package com.foo;\n"
				+ "\n"
				+ "import org.slf4j.Logger;\n"
				+ "import org.slf4j.LoggerFactory;\n"
				+ "\n"
				+ "\n"
				+ "class Bar {\n"
				+ "  private static final Logger log = LoggerFactory.getLogger(Bar.class);\n"
				+ "  \n"
				+ "}\n", gen.generate());
	}
	
	@Test
	public void testGetClassWithoutGenericType_NoGenericType() {
		Assert.assertEquals("com.x.y.z.Bar", JavaCodeGenerationUtil.getClassNameWithoutGenericType("com.x.y.z.Bar"));
	}
	
	@Test
	public void testGetClassWithoutGenericType_GenericType() {
		Assert.assertEquals("com.x.y.z.Bar", JavaCodeGenerationUtil.getClassNameWithoutGenericType("com.x.y.z.Bar<java.util.List<com.x.y.z.Foo>>"));
	}

	@Test
	public void testGetClassNameWithoutGenericType_NullGenericType() {
		Assert.assertNull(JavaCodeGenerationUtil.getClassNameWithoutGenericType(null));
	}
	
	@Test
	public void testGetGenericType_NoGenericType() {
		Assert.assertNull(JavaCodeGenerationUtil.getGenericType("com.x.y.z.Bar"));
	}
	
	@Test
	public void testGetGenericType_GenericType() {
		Assert.assertEquals("java.util.List<com.x.y.z.Foo>", 
							JavaCodeGenerationUtil.getGenericType("com.x.y.z.Bar<java.util.List<com.x.y.z.Foo>>"));
	}
	
	@Test
	public void testGetGenericType_InvalidGenericType() {
		try {
			JavaCodeGenerationUtil.getGenericType("com.x.y.z.Bar<");
		} catch(IllegalArgumentException ex) {
			return;
		}
		Assert.fail();
	}

	@Test
	public void testGetGenericType_NullGenericType() {
		Assert.assertNull(JavaCodeGenerationUtil.getGenericType(null));
	}
	
	@Test
	public void testGetQuotedString_NullString() {
		Assert.assertEquals(null, JavaCodeGenerationUtil.getQuotedString(null));
	}
	
	@Test
	public void testGetQuotedString_EmptyString() {
		Assert.assertEquals("\"\"", JavaCodeGenerationUtil.getQuotedString(""));
	}
	
	@Test
	public void testGetQuotedString_StringWithQuotes() {
		Assert.assertEquals("\"Hello \\\"World\\\"\"", 
							JavaCodeGenerationUtil.getQuotedString("Hello \"World\""));
	}
	
	@Test
	public void testGetHtmlLink() {
		Assert.assertEquals("<a href=\"https://scam.org\">Click here</a>", 
							JavaCodeGenerationUtil.getHtmlLink("https://scam.org", "Click here"));
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
							JavaCodeGenerationUtil.generateTryBlock(
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
							JavaCodeGenerationUtil.generateTryBlock(
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
							JavaCodeGenerationUtil.generateTryBlock(
								"throw new IOException();", 
								"log.error(ex);", 
								"IOException ex", 
								null, 
								null));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGenerateTryBlock_TryCatchMissingCatchException() {
		JavaCodeGenerationUtil.generateTryBlock(
				"throw new IOException();",
				"log.error(ex);",
				null,
				null,
				null);
	}
}
