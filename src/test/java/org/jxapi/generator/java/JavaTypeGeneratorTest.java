package org.jxapi.generator.java;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
    gen.addImport(Date.class);
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
    
    Assert.assertEquals("x.y.z.HelloInterface", gen.getName());
    Assert.assertEquals("public interface", gen.getTypeDeclaration());
    
    Assert.assertEquals("package x.y.z;\n"
        + "\n"
        + "import java.text.SimpleDateFormat;\n"
        + "import java.util.Date;\n"
        + "import java.util.List;\n"
        + "\n"
        + "import javax.annotation.processing.Generated;\n"
        + "\n"
        + "/**\n"
        + " * Hello interface\n"
        + " */\n"
        + "@Generated(\"org.jxapi.generator.java.JavaTypeGenerator\")\n"
        + "public interface HelloInterface {\n"
        + "  void sayHello();\n"
        + "  \n"
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
    JavaTypeGenerator gen = new JavaTypeGenerator("x.y.z.Hello");
    gen.addImport("java.util.Date");
    gen.addImport("java.text.SimpleDateFormat");
    gen.setDescription("Hello interface");
    gen.setTypeDeclaration("public class");
    gen.appendToBody("void sayHello();");
    srcFolder = Paths.get("tmp" + Math.random());
    gen.writeJavaFile(srcFolder);
    String actualJavaFileContent = Files.readString(srcFolder.resolve(Paths.get("x", "y", "z", "Hello.java")));
    Assert.assertEquals("package x.y.z;\n"
        + "\n"
        + "import java.text.SimpleDateFormat;\n"
        + "import java.util.Date;\n"
        + "\n"
        + "import javax.annotation.processing.Generated;\n"
        + "\n"
        + "/**\n"
        + " * Hello interface\n"
        + " */\n"
        + "@Generated(\"org.jxapi.generator.java.JavaTypeGenerator\")\n"
        + "public class Hello {\n"
        + "  void sayHello();\n"
        + "}\n", 
        actualJavaFileContent);
    Imports actualImports = gen.getImports();
    Assert.assertEquals(3, actualImports.size());
    Iterator<String> actualImportsIterator = actualImports.iterator();
    Assert.assertEquals("java.text.SimpleDateFormat", actualImportsIterator.next());
    Assert.assertEquals("java.util.Date", actualImportsIterator.next());
    Assert.assertEquals("javax.annotation.processing.Generated", actualImportsIterator.next());
  }
  
  @Test
  public void testGenerateJavaFileWithoutPackageAndImports() throws IOException {
    JavaTypeGenerator gen = new JavaTypeGenerator("x.y.z.Hello");
    gen.addImport("java.util.Date");
    gen.addImport("java.text.SimpleDateFormat");
    gen.setDescription("Hello interface");
    gen.setTypeDeclaration("public class");
    gen.appendToBody("void sayHello();");
    gen.setGeneratePackageAndImports(false);
    srcFolder = Paths.get("tmp" + Math.random());
    gen.writeJavaFile(srcFolder);
    String actualJavaFileContent = Files.readString(srcFolder.resolve(Paths.get("x", "y", "z", "Hello.java")));
    Assert.assertEquals( "/**\n"
          + " * Hello interface\n"
          + " */\n"
          + "@Generated(\"org.jxapi.generator.java.JavaTypeGenerator\")\n"
          + "public class Hello {\n"
          + "  void sayHello();\n"
          + "}\n", 
        actualJavaFileContent);
    Imports actualImports = gen.getImports();
    Assert.assertEquals(3, actualImports.size());
    Iterator<String> actualImportsIterator = actualImports.iterator();
    Assert.assertEquals("java.text.SimpleDateFormat", actualImportsIterator.next());
    Assert.assertEquals("java.util.Date", actualImportsIterator.next());
    Assert.assertEquals("javax.annotation.processing.Generated", actualImportsIterator.next());
  }
  
  @Test
  public void testGeneratorWithEmptyImplementedInterfacesList() {
    JavaTypeGenerator gen = new JavaTypeGenerator("x.y.z.MyClass");
    gen.appendMethod("void sayHello()", "System.out.println(\"Hello\");");
    gen.setImplementedInterfaces(List.of());
    gen.setTypeDeclaration("public class");
    Assert.assertEquals(0, gen.getImplementedInterfaces().size());
    Assert.assertEquals("package x.y.z;\n"
        + "\n"
        + "import javax.annotation.processing.Generated;\n"
        + "\n"
        + "\n"
        + "@Generated(\"org.jxapi.generator.java.JavaTypeGenerator\")\n"
        + "public class MyClass {\n"
        + "  void sayHello() {\n"
        + "    System.out.println(\"Hello\");\n"
        + "  }\n"
        + "}\n", 
        gen.generate());
  }
  
  @Test
  public void testGenerateJavaFileDefaultPackageSrcFolderAlreadyExists() throws IOException {
    JavaTypeGenerator gen = new JavaTypeGenerator("HelloInterface");
    gen.addImport("java.util.Date");
    gen.addImport("java.text.SimpleDateFormat");
    gen.setDescription("Hello interface");
    gen.setTypeDeclaration("public interface");
    gen.appendToBody("void sayHello();");
    srcFolder = Paths.get("tmp" + Math.random());
    Files.createDirectories(srcFolder);
    gen.writeJavaFile(srcFolder);
    String actualJavaFileContent = Files.readString(srcFolder.resolve(Paths.get("HelloInterface.java")));
    Assert.assertEquals("import java.text.SimpleDateFormat;\n"
        + "import java.util.Date;\n"
        + "\n"
        + "import javax.annotation.processing.Generated;\n"
        + "\n"
        + "/**\n"
        + " * Hello interface\n"
        + " */\n"
        + "@Generated(\"org.jxapi.generator.java.JavaTypeGenerator\")\n"
        + "public interface HelloInterface {\n"
        + "  void sayHello();\n"
        + "}\n", 
        actualJavaFileContent);
  }
  
  @Test
  public void testGenerateJavaClassWithSuperClassAndInterfaces() {
    JavaTypeGenerator gen = new JavaTypeGenerator("x.y.z.HelloInterface");
    String parentClassName = "a.b.c.HelloParent";
    gen.setParentClassName(parentClassName);
    List<String> implmentedInterfaces = Arrays.asList("d.e.f.HelloInterface", "f.g.h.OtherInterface");
    gen.setImplementedInterfaces(implmentedInterfaces);
    String description = "Hello class";
    gen.setDescription(description);
    gen.setTypeDeclaration("public interface");
    gen.appendMethod("@Override\npublic void sayHello()", 
             "Sytem.out.println(\"Hello\");", 
             "A method that prints \"Hello\" on standard output");
    
    Assert.assertEquals(implmentedInterfaces, gen.getImplementedInterfaces());
    Assert.assertEquals(parentClassName, gen.getParentClassName());
    Assert.assertEquals(description, gen.getDescription());
    Assert.assertEquals("package x.y.z;\n"
        + "\n"
        + "import a.b.c.HelloParent;\n"
        + "import d.e.f.HelloInterface;\n"
        + "import f.g.h.OtherInterface;\n"
        + "import javax.annotation.processing.Generated;\n"
        + "\n"
        + "/**\n"
        + " * Hello class\n"
        + " */\n"
        + "@Generated(\"org.jxapi.generator.java.JavaTypeGenerator\")\n"
        + "public interface HelloInterface extends HelloParent implements HelloInterface, OtherInterface {\n"
        + "  /**\n"
        + "   * A method that prints \"Hello\" on standard output\n"
        + "   */\n"
        + "  @Override\n"
        + "  public void sayHello() {\n"
        + "    Sytem.out.println(\"Hello\");\n"
        + "  }\n"
        + "}\n", 
        gen.generate());
    
  }
}
