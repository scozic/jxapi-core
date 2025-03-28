package org.jxapi.generator.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.annotation.processing.Generated;

/**
 * A simple generator for .java files of any type. Generator should be supplied
 * full type name, imports, super type name (can be multiple in case type is an
 * interface), description to display as class javadoc, type declaration (like
 * 'public class'), and body containing fields, constructor or other methods as
 * a raw {@link StringBuilder}.
 */
public class JavaTypeGenerator {

  private final Imports imports = new Imports();
  private final String name;
  private String parentClassName;
  private List<String> implementedInterfaces;
  
  private String typeDeclaration;
  
  private String description;
  
  private boolean generatePackageAndImports = true;
  
  /**
   * Inner class body
     */
  protected final StringBuilder body = new StringBuilder();
  
  /**
   * @param fullTypeName Full class name e.g. <i>com.x.y.z.Foo</i>
   */
  public JavaTypeGenerator(String fullTypeName) {
    this.name = fullTypeName;
  }
  
  /**
   * Shortcut for {@link #addImport(String)} using name of class as argument.
   * @param cls class to add to imports list
   * @see #addImport(String)
   */
  public void addImport(Class<?> cls) {
    imports.add(cls);
  }
  
  /**
   * Adds an import to list. Can be any type name, even in default package but in
   * this case, or if package of import is same as one of type name of class to
   * generated with this generator, the import will be omitted in generation.
   * Also, if import name has generic parameters e.g. <code>com.x.Foo&lt;T&gt;</code>,
   * this parameter is also omitted in generation.
   * 
   * @param im The import to add
   * @see Imports#add(String)
   */
  public void addImport(String im) {
    imports.add(im);
  }

  /** 
   * @return Unfiltered lit of all imports added using {@link #addImport(Class)}
   * @see #addImport(String)
   */
  public Imports getImports() {
    return imports;
  }

  /**
   * @return type declaration, see {@link #setTypeDeclaration(String)}
   */
  public String getTypeDeclaration() {
    return typeDeclaration;
  }

  /**
   * @param typeDeclaration modifiers and kind (class, interface) that come before class name, 
   * for instance "public interface" or "private final class"
   */
  public void setTypeDeclaration(String typeDeclaration) {
    this.typeDeclaration = typeDeclaration;
  }
  
  /**
   * Appends some content (fields, methods) to the generated type body. That
   * content will be indented once during generation.
   * 
   * @param bodyContent content to append to generated class body
   * @return the class body builder
   */
  public StringBuilder appendToBody(String bodyContent) {
    return body.append(bodyContent);
  }
  
  /**
   * Appends a method to generated class body.
   * 
   * @param methodDeclaration method modifiers, name and parameters, e.g.
   *                          <code>public static void foo(int count)</code>
   * @param methodBody        body of method, will be encapsulated in an indented
   *                          block (see
   *                          JavaCodeGenerationUtil#generateCodeBlock(String)) in
   *                          generated class body after method declaration.
   */
  public void appendMethod(String methodDeclaration, String methodBody) {
    appendMethod(methodDeclaration, methodBody, null);
  }
  
  /**
   * Appends a method to generated class body.
   * 
   * @param methodDeclaration method modifiers, name and parameters, e.g.
   *                          <code>public static void foo(int count)</code>
   * @param methodBody        body of method, will be encapsulated in an indented
   *                          block (see
   *                          JavaCodeGenerationUtil#generateCodeBlock(String)) in
   *                          generated class body after method declaration.
   * @param javadoc      JavaDoc for the method                          
   */
  public void appendMethod(String methodDeclaration, String methodBody, String javadoc) {
    if (javadoc != null) {
      appendToBody(JavaCodeGenUtil.generateJavaDoc(javadoc)).append("\n");
    }
    appendToBody(methodDeclaration + " " + JavaCodeGenUtil.generateCodeBlock(methodBody));
  }
  
  /**
   * @return Generated class full name e.g. <code>com.x.y.z.Foo</code>
   */
  public String getName() {
    return name;
  }
  
  /**
   * @return Generated class simple name without package e.g. <code>Foo</code>
   */
  public String getSimpleName() {
    return JavaCodeGenUtil.getClassNameWithoutPackage(name);
  }
  
  /**
   * @return package name of generated class e.g. <code>com.x.y.z</code>
   */
  public String getPackage() {
    return JavaCodeGenUtil.getClassPackage(name);
  }
  
  /**
   * @return Generated class javadoc
   * @see #setDescription(String)
   */
  public String getDescription() {
    return description;
  }

  /**
   * @param description Generated class javadoc
   */
  public void setDescription(String description) {
    this.description = description;
  }
  
  /**
   * @return parent class name
   * @see #setParentClassName(String)
   */
  public String getParentClassName() {
    return parentClassName;
  }

  /**
   * @param parentClassName parent class name of generated class. Leading package
   *                        will be omitted and parentClassName added to imports
   *                        (and that import may be omitted if same package as
   *                        generated class) during generation.
   */
  public void setParentClassName(String parentClassName) {
    this.parentClassName = parentClassName;
  }

  /**
   * @return List of interfaces implemented by generated class interfaces. Initially <code>null</code>
   * @see #setImplementedInterfaces(List)
   */
  public List<String> getImplementedInterfaces() {
    return implementedInterfaces;
  }

  /**
   * 
   * @param implementedInterfaces List of interfaces implemented by generated
   *                              class interfaces. If not <code>null</code> nor
   *                              empty, will be generated after
   *                              <code>implements</code> added to generated class
   *                              signature, with simple class names. Full class
   *                              names are added to imports during generation
   *                              unless same package as generated class.
   */
  public void setImplementedInterfaces(List<String> implementedInterfaces) {
    this.implementedInterfaces = implementedInterfaces;
  }
  
  /**
   * @return If <code>true</code> (default), package and imports will be generated
   *         in output.
   */
  public boolean isGeneratePackageAndImports() {
    return generatePackageAndImports;
  }

  /**
   * @param generatePackageAndImports If <code>true</code> (default), package and
   *                                  imports will be generated in output.
   */
  public void setGeneratePackageAndImports(boolean generatePackageAndImports) {
    this.generatePackageAndImports = generatePackageAndImports;
  }
  
  /**
   * Performs generation. Parent class and implemented interfaces will be added to
   * import unless of same package as generated class
   * 
   * @return Generated content of .java file for generated class
   */
  public String generate() {
    StringBuilder sb = new StringBuilder();
    addImport(Generated.class);
    if (parentClassName != null) {
      addImport(parentClassName);
    }
    if (implementedInterfaces != null) {
      implementedInterfaces.forEach(this::addImport);
    }
    
    if (generatePackageAndImports) {
      String pkg = getPackage();
      if (!pkg.isEmpty()) {
        sb.append("package ").append(pkg).append(";\n\n");
      }
      
      sb.append(imports.generate(pkg))
        .append("\n");
    }
    
    sb.append(JavaCodeGenUtil.generateJavaDoc(description))
      .append("\n")
      .append("@Generated(\"").append(getClass().getName()).append("\")\n")
      .append(typeDeclaration)
      .append(" ")
      .append(getSimpleName())
      .append(" ");
    if (parentClassName != null) {
      sb.append("extends ")
        .append(JavaCodeGenUtil.getClassNameWithoutPackage(parentClassName))
        .append(" ");
    }
    if (implementedInterfaces != null && !implementedInterfaces.isEmpty()) {
      sb.append("implements ");
      for (int i = 0; i < implementedInterfaces.size(); i++) {
        sb.append(JavaCodeGenUtil.getClassNameWithoutPackage(implementedInterfaces.get(i)));
        if (i < implementedInterfaces.size() - 1) {
          sb.append(", ");
        }
      }
      sb.append(" ");
    }
    
      sb.append(JavaCodeGenUtil.generateCodeBlock(body.toString()));
      body.delete(0, body.length());
    return sb.toString();
  }

  /**
   * Performs generation and writes corresponding .java file.
   * Folders will be generated according to package hierarchy.
   * @param sourceFolder base directory (default package) for sources
   * @throws IOException If an error occurs writing the file
   */
  public void writeJavaFile(Path sourceFolder) throws IOException {
    String pkg = getPackage();
    if (!pkg.isEmpty()) {
      for (String f: pkg.split("\\.")) {
        sourceFolder = sourceFolder.resolve(f);
      }
    }
    if (!Files.exists(sourceFolder)) {
      Files.createDirectories(sourceFolder);
    }
    Files.writeString(sourceFolder.resolve(getSimpleName() + ".java"), generate());
  }


  
}
