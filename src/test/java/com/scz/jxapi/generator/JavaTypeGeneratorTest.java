package com.scz.jxapi.generator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link JavaTypeGenerator}
 */
public class JavaTypeGeneratorTest {
	
	private Path srcFolder;
	
	@Test
	public void testGenerateInterface() {
		JavaTypeGenerator gen = new JavaTypeGenerator("x.y.z.HelloInterface");
		gen.addImport("java.util.Date");
		gen.addImport("java.text.SimpleDateFormat");
		// Import that should be ignored in generated code because same package as generated type.
		gen.addImport("x.y.z.Foo");
		// Import that should be ignored in generated code because starts with 'java.lang'
		gen.addImport("java.lang.Boolean");
		// Import where type is provided as generic, the <..> part should be removed.
		gen.addImport("java.util.List<String>");
		gen.setDescription("Hello interface");
		gen.setTypeDeclaration("public interface");
		gen.appendToBody("void sayHello();\n\n");
		gen.appendToBody("List<String> getHellos();");
		Assert.assertEquals("package x.y.z;\n"
				+ "\n"
				+ "import java.text.SimpleDateFormat;\n"
				+ "import java.util.Date;\n"
				+ "import java.util.List;\n"
				+ "\n"
				+ "/**\n"
				+ " * Hello interface\n"
				+ " */\n"
				+ "public interface HelloInterface {\n"
				+ "  void sayHello();\n  \n"
				+ "  List<String> getHellos();\n"
				+ "}\n", gen.generate());
		
	}
	
	@After
	public void tearDown() throws IOException {
		if (srcFolder != null) {
			Files.walk(srcFolder)
		      .sorted(Comparator.reverseOrder())
		      .map(Path::toFile)
		      .forEach(File::delete);
			srcFolder = null;
		}
	}

	@Test
	public void testGenerateJavaFile() throws IOException {
		JavaTypeGenerator gen = new JavaTypeGenerator("x.y.z.HelloInterface");
		gen.addImport("java.util.Date");
		gen.addImport("java.text.SimpleDateFormat");
		gen.setDescription("Hello interface");
		gen.setTypeDeclaration("public interface");
		gen.appendToBody("void sayHello();");
		srcFolder = Paths.get("tmp" + Math.random());
		gen.writeJavaFile(srcFolder);
		String actualJavaFileContent = Files.readString(srcFolder.resolve(Paths.get("x", "y", "z", "HelloInterface.java")));
		Assert.assertEquals(gen.generate(), actualJavaFileContent);
	}
	
	@Test
	public void testGenerateJavaClassWithSuperClassAndInterfaces() {
		JavaTypeGenerator gen = new JavaTypeGenerator("x.y.z.HelloInterface");
		gen.setParentClassName("a.b.c.HelloParent"); 
		gen.setImplementedInterfaces(Arrays.asList("d.e.f.HelloInterface", "f.g.h.OtherInterface"));
		gen.setDescription("Hello class");
		gen.setTypeDeclaration("public interface");
		gen.appendMethod("@Override\npublic void sayHello()", "Sytem.out.println(\"Hello\");");
		Assert.assertEquals("package x.y.z;\n"
				+ "\n"
				+ "import a.b.c.HelloParent;\n"
				+ "import d.e.f.HelloInterface;\n"
				+ "import f.g.h.OtherInterface;\n"
				+ "\n"
				+ "/**\n"
				+ " * Hello class\n"
				+ " */\n"
				+ "public interface HelloInterface extends HelloParent implements HelloInterface, OtherInterface {\n"
				+ "  @Override\n"
				+ "  public void sayHello() {\n"
				+ "    Sytem.out.println(\"Hello\");\n"
				+ "  }\n"
				+ "}\n", gen.generate());
		
	}
}
