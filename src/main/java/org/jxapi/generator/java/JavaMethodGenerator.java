package org.jxapi.generator.java;

import org.jxapi.util.EncodingUtil;

/**
 * Generates java source code for a method. Method declaration is provided as
 * plain string and method body can be built incrementally using
 * {@link #appendToBody(String)}.
 */
public class JavaMethodGenerator {

  private String medthodDeclaration;
  
  /**
   * Method body
   */
  protected final StringBuilder body = new StringBuilder();
  
  private String javadoc;

  /**
   * Appends some content to method body.
   * 
   * @param code source code to append. May contain line returns that will be
   *             indented by generated code block for method body
   * @return builder containing method source code, can be used to append other
   *         content.
   */
  public StringBuilder appendToBody(String code) {
    return body.append(code);
  }
  
  /**
   * Generates the full method source code (signature + body).
   * @return method source code
   */
  public String generate() {
    return new StringBuilder()
          .append(javadoc == null? "": 
              JavaCodeGenUtil.generateJavaDoc(javadoc) + "\n")
          .append(this.getMedthodDeclaration())
          .append(" ")
          .append(JavaCodeGenUtil.generateCodeBlock(body.toString()))
          .toString();
  }

  /**
   * @return Method javadoc
   */
  public String getJavadoc() {
    return javadoc;
  }

  /**
   * @param javadoc Method javadoc
   */
  public void setJavadoc(String javadoc) {
    this.javadoc = javadoc;
  }
  
  /**
   * @return Method declaration
   * @see #setMedthodDeclaration(String)
   */
  public String getMedthodDeclaration() {
    return medthodDeclaration;
  }

  /**
   * @param medthodDeclaration Full method declaration like
   *                           <code>public static void foo(int bar) throws Exception</code>
   */
  public void setMedthodDeclaration(String medthodDeclaration) {
    this.medthodDeclaration = medthodDeclaration;
  }

  /**
   * @return A string representation of this object
   * @see EncodingUtil#pojoToString(Object)
   */
  @Override
  public String toString() {
    return EncodingUtil.pojoToString(this);
  }
}
