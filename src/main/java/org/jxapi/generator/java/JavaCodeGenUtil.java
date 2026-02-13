package org.jxapi.generator.java;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jxapi.generator.java.exchange.ExchangeGenUtil;
import org.jxapi.pojo.descriptor.CanonicalType;
import org.jxapi.pojo.descriptor.Type;
import org.jxapi.util.CollectionUtil;

/**
 * Helper methods around java code generation
 */
public class JavaCodeGenUtil {
  
  /**
   * String value of <code>null</code>
   */
  public static final String NULL = "null";
  
  /**
   * Arguments separator in super constructor call when such arguments should be
   * displayed in multiple lines to make code more readable.
   */
  public static final String SUPER_ARG_SEPARATOR = ",\n      ";

  private JavaCodeGenUtil() {}
  
  /**
   * Default indentation used in generated source code
   */
  public static final String INDENTATION = "  ";
  
  /**
   * Indent all lines in source code chunk, appending {@link #INDENTATION} after
   * every line return.
   * 
   * @param code source code chunk to indent
   * @return indented code
   */
  public static final String indent(String code) {
    return indent(code, INDENTATION);
  }
  
  /**
   * Indent all lines in source code chunk, appending indentation after every line
   * return.
   * 
   * @param code   source code chunk to indent
   * @param indent indentation, spaces or tab
   * @return Indented code
   */
  public static final String indent(String code, String indent) {
    return indent + code.replace("\n", "\n" + indent);
  }
  
  /**
   * Creates Java source code block with given source code chunk content
   * 
   * @param codeBlockContent source code chunk to use as block body
   * @return Source code block, e.g.
   * 
   *         <pre>
   *         "{\n" + indented_codeBlockContent + "}\n"
   *         </pre>
   */
  public static final String generateCodeBlock(String codeBlockContent) {
    if (codeBlockContent.endsWith("\n")) {
      codeBlockContent = codeBlockContent.substring(0, codeBlockContent.length() - 1);
    }
    return "{\n" + indent(codeBlockContent) + "\n}\n";
  }
  
  /**
   * Generates Javadoc source code chunk with given javadoc text
   * 
   * @param javadoc Javadoc text
   * @return Javadoc code chunk e.g.
   * 
   *         <pre>{@code
   *         "/**\n" + indented_javadoc + "\n*\/"
   *           }</pre>
   * 
   *         where <code>indented_javadoc</code> is the javadoc text indented with
   *         <code>' * '</code> prefix.}
   */
  public static final String generateJavaDoc(String javadoc) {
    if (javadoc == null)
      return "";
    return "/**\n" + indent(javadoc, " * ") + "\n */"; 
  }
  
  /**
   * Generates getter method declaration for given field
   * 
   * @param fieldName     field name
   * @param fieldType     type of field
   * @param allFieldNames list of all fields, or <code>null</code> belonging to
   *                      same destination class as field name, to check for
   *                      unicity
   * @return "get" or "is" if boolean field type + first letter to uppercase field
   *         name if unique, field name as is if another field exist that is equal
   *         ignoring case to fieldName
   */
  public static String getGetAccessorMethodName(String fieldName, Type fieldType, List<String> allFieldNames) {
    String prefix = "get";
    if (Type.BOOLEAN.equals(fieldType)) {
      prefix = "is";
    }
    return getAccessorMethodName(prefix, fieldName, allFieldNames);
  }
  
  /**
   * Generates setter method declaration for given field
   * 
   * @param fieldName     field name
   * @param allFieldNames list of all fields, or <code>null</code> belonging to
   *                      same destination class as field name, to check for
   *                      unicity
   * @return "set" + first letter to uppercase field name if unique, field name as
   *         is if another field exist that is equal ignoring case to fieldName
   */
  public static String getSetAccessorMethodName(String fieldName, List<String> allFieldNames) {
    return getAccessorMethodName("set", fieldName, allFieldNames);
  }
  
  /**
   * Generates getter or setter method declaration for given field
   * 
   * @param prefix        "get" "set" or "is" accessor method prefix.
   * @param name         field name
   * @param allFieldNames list of all fields, or <code>null</code> belonging to
   *                      same destination class as field name, to check for
   *                      unicity
   * @return "set" + first letter to upper case field name if unique, field name
   *         as is if another field exist that is equal ignoring case to fieldName
   */
  public static String getAccessorMethodName(String prefix, String name, List<String> allFieldNames) {
    String accessorSuffix = name;
    // If there is another field with same name but different case, keep the part after accessor as is to make sure it is unique,
    // otherwise use first letter upper case for more readable camel case accessor name.
    if (allFieldNames == null || allFieldNames.stream().noneMatch(n -> !name.equals(n) && name.equalsIgnoreCase(n))) {
      accessorSuffix = firstLetterToUpperCase(name);
    }
    return prefix + accessorSuffix;
  }
  
  /**
   * @param s Any String
   * @return <code>s</code> String with first letter to upper case
   */
  public static String firstLetterToUpperCase(String s) {
    if (s == null || s.isEmpty())
      return s;
    return Character.toUpperCase(s.charAt(0)) + s.substring(1);
  }
  
  /**
   * @param s Any String
   * @return <code>s</code> String with first letter to lower case
   */
  public static String firstLetterToLowerCase(String s) {
    if (s == null || s.isEmpty())
      return s;
    return Character.toLowerCase(s.charAt(0)) + s.substring(1);
  }
  
  /**
   * @param fullClassName full class name with package prefix
   * @return class name without package prefix, e.g. part of
   *         <code>fullClassName</code> param after last occurrence of '.' char,
   *         or unchanged <code>fullClassName</code> param if it is
   *         <code>null</code> or contains no '.'.
   */
  public static String getClassNameWithoutPackage(String fullClassName) {
    if (fullClassName != null) {
      String genericType = getGenericType(fullClassName);
      if (genericType != null) {
        String classNameWithoutGenericType = getClassNameWithoutGenericType(fullClassName);
        return getClassNameWithoutPackage(classNameWithoutGenericType) + "<" + genericType + ">";
      }
      int dotOff = fullClassName.indexOf('.');
      if (dotOff >=0) {
        return StringUtils.substringAfterLast(fullClassName, ".");
        
      }
    }
    return fullClassName;
  }
  
  /**
   * @param className full or simple class name with eventual generic parameter
   * @return the className, without part after first '&lt;' if any, or
   *         <code>null</code> if <code>className</code> is <code>null</code>
   */
  public static String getClassNameWithoutGenericType(String className) {
    if (className == null) {
      return null;
    }
      
    int lessOff = className.indexOf('<');
    if (lessOff >= 0) {
      return className.substring(0, lessOff);
    }
    return className;
  }
  
  /**
   * @param className full or simple class name with eventual generic parameter
   * @return the generic parameter, e.g. part of class name between first '&lt;' and
   *         last '&gt;', or <code>null</code> if there is no generic
   *         parameter.
   * @throws IllegalArgumentException if no closing '&gt;' is found after first '&lt;'.
   */
  public static String getGenericType(String className) {
    if (className == null) {
      return null;
    }
      
    int lessOff = className.indexOf('<');
    if (lessOff >= 0) {
      int supOff = className.lastIndexOf('>');
      if (supOff < lessOff + 1) {
        throw new IllegalArgumentException("Invalid generic class name, missing closing '>':" + className);
      }
      return className.substring(lessOff + 1, supOff);
    }
    return null;
  }
  
  /**
   * @param fullClassName full class name with package prefix
   * @return package prefix, e.g. part of <code>fullClassName</code> param before
   *         last occurrence of '.' char, or empty string if
   *         <code>fullClassName</code> is <code>null</code> or does not contain
   *         '.' char.
   */
  public static String getClassPackage(String fullClassName) {
    if (fullClassName != null && fullClassName.contains(".")) {
      return StringUtils.substringBeforeLast(fullClassName, ".");
    }
    return "";
  }
  
  /**
   * @param className full or simple class name
   * @return <code>true</code> if <code>className</code> contains a package prefix
   *         (contains a '.' char), <code>false</code> otherwise.
   */
  public static boolean isFullClassName(String className) {
    return className != null && className.contains(".");
  }
  
  /**
   * Generates static variable name according to its camel case name, e.g.
   * <code>myStaticVariable</code> &rarr; <code>MY_STATIC_VARIABLE</code>
   * 
   * @param camelCaseVariableName variable name using camel case convention
   * @return static variable name using upper case convention
   */
  public static String getStaticVariableName(String camelCaseVariableName) {
    if (camelCaseVariableName == null || camelCaseVariableName.isEmpty()) {
      return camelCaseVariableName;
    }
    StringBuilder res = new StringBuilder();
    res.append(Character.toUpperCase(camelCaseVariableName.charAt(0)));
    for (int i = 1; i < camelCaseVariableName.length(); i++) {
      char c = camelCaseVariableName.charAt(i);
      if (Character.isUpperCase(c)) {
        res.append("_").append(c);
      } else {
        res.append(Character.toUpperCase(c));
      }
    }
    return res.toString();
  }

  /**
   * Deletes all files of a {@link Path} recursively
   * @param path path to delete all files of
   * @throws IOException eventually raised by {@link Files#walk(Path, java.nio.file.FileVisitOption...)}
   */
  public static void deletePath(Path path) throws IOException {
    if (!path.toFile().exists())
      return;
    try (Stream<Path> p = Files.walk(path)) {
       p.sorted(Comparator.reverseOrder())
          .map(Path::toFile)
          .forEach(File::delete);
    }
  }
  
  /**
   * Appends {@link Logger} and {@link LoggerFactory} imports and logger
   * declaration followed by blank line and beginning of type body like
   * 
   * <pre>
   * private static final Logger log = LoggerFactory.getLogger(" +
   * typeGenerator.getSimpleName() + ".class);\n\n
   * </pre>
   * 
   * @param typeGenerator type generator to append logger declaration at beginning
   *                      of body of
   */
  public static void generateSlf4jLoggerDeclaration(JavaTypeGenerator typeGenerator) {
    typeGenerator.addImport(Logger.class);
    typeGenerator.addImport(LoggerFactory.class);
    typeGenerator.appendToBody("private static final Logger log = LoggerFactory.getLogger(" + typeGenerator.getSimpleName() + ".class);\n\n");
  }

  /**
   * Turns a string into a quoted string, escaping double quotes, suitable for
   * JSON or Java code of string value.<br>
   * <strong>Example:</strong> <code>getQuotedString("Hello World")</code> &rarr;
   * <code>"Hello \"World\""</code>
   * 
   * @param sampleValue string to quote, as arbitrary object. Object
   *                    <code>toString()</code> method will be used.
   * @return <code>null</code> if <code>sampleValue</code> is <code>null</code>,
   *         quoted string of parameter <code>toStrin()</code> otherwise.
   */
  public static String getQuotedString(Object sampleValue) {
    if (sampleValue == null) {
        return null;
    }
    String sampleValueStr = sampleValue.toString();
    StringBuilder sb = new StringBuilder();
    sb.append('"');
    for (int i = 0; i < sampleValueStr.length(); i++) {
      char c = sampleValueStr.charAt(i);
      if (c == '"') {
        sb.append("\\\"");
      } else if (c == '\n') {
        sb.append("\\n");
      } else {
        sb.append(c);
      }
    }
    return sb.append('"').toString();
  }
  
  /**
   * Generates a HTML link with given href and text
   * @param href URL to link to
   * @param text text to display
   * @return HTML link
   */
  public static String getHtmlLink(String href, String text) {
    return "<a href=\"" + href + "\">" + text + "</a>";
  }
  
  /**
   * Generates a Java try/catch/finally instructions set. 
   * @param tryClause <code>try</code> clause
   * @param catchClause <code>Catch clause</code> can be <code>null</code> if there is no catch
   * @param catchException Exception caught in <code>catch</code> clause, for instance <code>MyException ex</code>
   * @param finallyClause <code>finally</code> clause
   * @param resource In case of a 'try with resource' block, the resource clause <code>try (resource) { ... }</code>. 
   * @return Java try (resource) { tryBlock } catch ( catchException ) { catchClause } finally { finallyClause } code.
   */
  public static String generateTryBlock(String tryClause, 
                      String catchClause, 
                      String catchException, 
                      String finallyClause, 
                      String resource) {
    StringBuilder s = new StringBuilder().append("try");
    if (resource != null) {
      s.append("(")
       .append(resource)
       .append(")");
    }
     s.append(" ")
     .append(generateCodeBlock(tryClause));
     if (catchClause != null) {
       if (catchException == null) {
         throw new IllegalArgumentException("Catch clause provided but caught exception is null");
       }
       s.append("catch (")
        .append(catchException)
        .append(") ")
        .append(generateCodeBlock(catchClause));
     }
     if (finallyClause != null) {
       s.append("finally ")
       .append(generateCodeBlock(finallyClause));
     }
     return s.toString();
  }
  
  /**
   * Class of an argument as it should be declared in a method javadoc. This means
   * the class name without generic parameters.
   * 
   * @param argumentClassName Simple or full (with package) method argument class
   *                          name, can be <code>null</code>
   * @return Empty string if <code>argumentClassName</code> is <code>null</code>,
   *         <code>argumentClassName</code> without generic argument, for
   *         instance: <code>List&lt;Integer&gt;</code> &rarr; <code>List</code>
   */
  public static String getMethodJavadocArgumentDeclaration(String argumentClassName) {
    if (argumentClassName == null) {
      return "";
    }
    int off = argumentClassName.indexOf('<');
    if (off >= 0) {
      argumentClassName = argumentClassName.substring(0, off);
    }
    return argumentClassName;
  }
  
  /**
   * Generates URL to a class javadoc file from a base URL and full class name
   * @param baseUrl The base URL used as prefix
   * @param javaClassName Full class name with package prefix
   * @return URL to HTML Javadoc file
   */
  public static String getClassJavadocUrl(String baseUrl, String javaClassName) {
    return getClassJavadocUrl(baseUrl, javaClassName, null);
  }
  
  /**
   * Generates URL to a class javadoc file from a base URL and full class name
   * @param baseUrl The base URL used as prefix
   * @param javaClassName Full class name with package prefix
   * @param innerClassName Name of inner class, e.g. <code>Foo.Bar</code>. Optional, ignored if null or empty.
   * @return URL to HTML Javadoc file
   */
  public static String getClassJavadocUrl(String baseUrl, String javaClassName, String innerClassName) {
    return getClassUrl(baseUrl, javaClassName, innerClassName, ".html");
  }
  
  /**
   * Generates URL to a class file (may be HTML or source file according to
   * suffix) from a base URL and full class name
   * <p>
   * Example:
   * 
   * <pre>
   * getClassUrl("https://docs.oracle.com/javase/8/docs/api/", "java.lang.String", ".html") &rarr; "https://docs.oracle.com/javase/8/docs/api/java/lang/String.html"
   * </pre>
   * 
   * @param baseUrl        The base URL used as prefix
   * @param javaClassName  Full class name with package prefix
   * @param innerClassName Name of inner class, e.g. <code>Foo.Bar</code>. Optional, ignored if null or empty.
   * @param suffix        Suffix to append to class name for instance ".html" 
   * @return URL to class file
   */
  public static String getClassUrl(String baseUrl, String javaClassName, String innerClassName, String suffix) {
    StringBuilder s = new StringBuilder()
        .append(baseUrl)
        .append(javaClassName.replace('.', '/'));
    if (StringUtils.isNotBlank(innerClassName)) {
       s.append('.')
        .append(innerClassName);
    }
     return s.append(suffix).toString();
  }

  /**
   * Gets class name as it should be written in a javadoc method link as argument
   * of this method.<br>
   * e.g. comma separated full class names of function argument. If one argument
   * is generic, the full class name of class holding generic (for instance
   * <code>java.util.List</code> if argument is of
   * <code>List&lt;Integer&gt;</code> type).
   * 
   * @param type            the type of argument
   * @param objectClassName if argument is of object type (see
   *                        {@link Type#isObject()}). Will be the value returned
   *                        if type is {@link Type#OBJECT}.
   * @return full class name of argument of given type
   */
  public static String getMethodArgumentJavadoc(Type type, String objectClassName) {
    if (type == null) {
      return "";
    }
    CanonicalType canonicalType = type.getCanonicalType();
    Class<?> canonicalTypeClass = canonicalType.typeClass;
    switch (canonicalType) {
    case LIST:
      return List.class.getName();
    case MAP:
      return Map.class.getName();
    case OBJECT:
      return objectClassName;
    default:
      return canonicalTypeClass.getName();
    }
  }
  
  /**
   * Checks if the given identifier is a valid camel case identifier. A valid
   * camel case identifier starts with a lowercase letter, and contains only
   * letters, digits, and underscores.
   * 
   * @param identifier the identifier to check
   * @return <code>true</code> if the identifier is valid, <code>false</code>
   *         otherwise
   */
  public static boolean isValidCamelCaseIdentifier(String identifier) {
    if (identifier == null || identifier.isEmpty()) {
      return false;
    }
    char start = identifier.charAt(0);
    if (!Character.isLetter(start) || !Character.isLowerCase(start)) {
      return false;
    }
    for (int i = 1; i < identifier.length(); i++) {
      char c = identifier.charAt(i);
      if (!Character.isJavaIdentifierPart(c)) {
        return false;
      }
    }
    return true;
  }
  
  /**
   * Generates a Javadoc link to a class or property or method of a class, for instance
   * <code>{\@link com.x.y.z.Foo#bar()}</code> for method <code>bar()</code> of
   * class <code>com.x.y.z.Foo</code>.
   * 
   * @param link the link to generate, can be a class name, or a class name with
   *             method name, like <code>com.x.y.z.Foo#bar()</code>
   * @return Javadoc link to given class or method
   */
  public static final String getJavaDocLink(String link) {
    if (link == null) {
      return null;
    }
    return new StringBuilder()
        .append("{@link ")
        .append(link)
        .append("}").toString();
  }
  
  /**
   * Generates a Javadoc link to a class property or method, for instance
   * <code>{\@link com.x.y.z.Foo#bar}</code> for property <code>bar</code> of class
   * <code>com.x.y.z.Foo</code>.
   * 
   * @param className full class name with package prefix, e.g. <code>com.x.y.z.Foo</code>
   * @param attribute property name of the class, e.g. <code>bar</code>
   * @return Javadoc link to given class property or method
   */
  public static final String getJavaDocLink(String className, String attribute) {
    if (attribute == null) {
      return getJavaDocLink(className);
    }
    return getJavaDocLink(className + "#" + attribute);
  }
  
  /**
   * Generates Java code like <code>Optional.ofNullable(optionalStatement).orElse(orElseStatement)</code>
   * <ul>
   * <li>If <code>optionalStatement</code> is empty, returns <code>orElseStatement</code>.
   * <li>If <code>orElseStatement</code> is empty, returns <code>optionalStatement</code>.
   * </ul>
   * @param optionalStatement the optional statement to evaluate
   * @param orElseStatement the statement to return if the optional is empty
   * @param imports the imports to add
   * @param multiline whether to format the code in multiple lines
   * @return Java code for constant declaration
   */
  public static String generateOptionalOfNullableStatement(String optionalStatement, String orElseStatement, Imports imports, boolean multiline) {
    if (StringUtils.isEmpty(optionalStatement)) {
      return orElseStatement;
    }
    if (StringUtils.isEmpty(orElseStatement)) {
      return optionalStatement;
    }
    imports.add(Optional.class);
    StringBuilder sb = new StringBuilder().append("Optional");
    StringBuilder optStatement = new StringBuilder()
        .append(".ofNullable(")
        .append(optionalStatement)
        .append(")");
    
    if (multiline) {
      sb.append("\n")
        .append(JavaCodeGenUtil.indent(optStatement.toString()));
    } else {
      sb.append(optStatement);
    }
    

    
    StringBuilder orElse = new StringBuilder()
        .append(".orElse(")
        .append(orElseStatement)
        .append(")");
    
    if (multiline) {
      sb.append("\n")
        .append(JavaCodeGenUtil.indent(orElse.toString()));
    } else {
      sb.append(orElse);
    }
    return sb.toString();
  }
  
  /**
   * Generates a map of unique camel case variable names for given endpoint names.
   * If two endpoint names generate the same camel case variable name, the
   * original endpoint name is used as variable name to ensure unicity.
   * Otherwise, the camel case variable name is generated by converting the first
   * letter of the endpoint name to lower case.
   * 
   * @param allVariableNames list of all endpoint names assumed to be distinct
   * @param firstLetterToUpperCase whether to convert first letter to upper case (<code>true</code>) or lower case (<code>false</code>)
   * @return map of endpoint name to unique camel case variable name
   */
  public static Map<String, String> getUniqueCamelCaseVariableNames(List<String> allVariableNames, boolean firstLetterToUpperCase) {
    Set<String> endpointNamesSet = Set.copyOf(allVariableNames);
    Map<String, String> res = CollectionUtil.createMap();
    for (String endpointName : allVariableNames) {
      for (String otherEndpointName : endpointNamesSet) {
        if (!Objects.equals(endpointName, otherEndpointName)  
              && Objects.equals(
                  JavaCodeGenUtil.firstLetterToLowerCase(endpointName),
                  JavaCodeGenUtil.firstLetterToLowerCase(otherEndpointName))) {
          res.put(endpointName, endpointName);
          break;
        }
      }
      res.computeIfAbsent(
          endpointName, 
          firstLetterToUpperCase? 
              JavaCodeGenUtil::firstLetterToUpperCase : 
              JavaCodeGenUtil::firstLetterToLowerCase);
    }
    return res;
  }

  /**
   * Finds all placeholders in the given string value. For instance with input
   * string
   * <code>"Hello ${name} you are using exchange ${exchange.name}"</code>, will return <code>["name", "exchange.name"]</code>
   * 
   * @param value The string value to find placeholders in
   * @return A list of placeholders found in the given string value
   */
  public static List<String> findPlaceHolders(String value) {
    List<String> res = new ArrayList<>();
    if (StringUtils.isEmpty(value)) {
      return res;
    }
  
    Matcher matcher = ExchangeGenUtil.PLACEHOLDER_PATERN.matcher(value);
    while (matcher.find()) {
      res.add(matcher.group(1));
    }
    return res;
  }
}
