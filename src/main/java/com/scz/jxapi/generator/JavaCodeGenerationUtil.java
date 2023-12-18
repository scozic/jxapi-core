package com.scz.jxapi.generator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper methods around java code generation
 * @author Sylvestre COZIC
 */
public class JavaCodeGenerationUtil {
	
	public static final String NULL = "null";

	private JavaCodeGenerationUtil() {}
	
	/**
	 * Default indentation used in generated source code
	 */
	public static final String INDENTATION = "  ";
	
	/**
	 * Text to write in generated sources javadoc to warn code is generated and not to be edited.
	 */
	public static final String GENERATED_CODE_WARNING = "<br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>";
	
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
	 *         <pre>
	 *         "/**\n" + indented_javadoc + "\n*\/"
	 */
	public static final String generateJavaDoc(String javadoc) {
		if (javadoc == null)
			return "";
		return "/**\n" + indent(javadoc, " * ") + "\n */"; 
	}
	
	/**
	 * Generates Java source code chunk with private member declarations, and getter
	 * and setter method declaration with javadoc. The result corresponds to body of
	 * a POJO class (without class declaration and imports)
	 * 
	 * @param fields list of fields
	 * @return Java source code chunk with private member declarations, and getter
	 *         and setter method declaration with javadoc.
	 */
	public static String generateJavaPojoFieldsWithAccessors(List<PojoField> fields) {
		StringBuilder fieldDeclarations = new StringBuilder();
		StringBuilder accessorsDeclarations = new StringBuilder();
		for (PojoField field : fields) {
			String type = field.getType();
			type = getClassNameWithoutPackage(type);
			String name = field.getName();
			String description = field.getDescription();
			String msgFieldDescription = "";
			if (field.getMsgField() != null) {
				msgFieldDescription = " Message field <strong>" + field.getMsgField() + "</strong>";
				if (description == null) {
					description = msgFieldDescription;
				} else {
					description += msgFieldDescription;
				}
			}
			
			if (description != null) {
				accessorsDeclarations.append(generateJavaDoc("@return " + description)).append("\n");
			}
			List<String> allFieldNames = fields.stream().map(f -> f.getName()).collect(Collectors.toList());
			accessorsDeclarations
				.append("public ")
				.append(type)
				.append(" ")
				.append(getGetAccessorMethodName(field.getName(), field.getType(), allFieldNames))
				.append("() ")
				.append(generateCodeBlock("return " + name + ";"))
				.append("\n");
			
			fieldDeclarations.append("private ").append(type).append(" ").append(name).append(";\n");
			if (description != null) {
				accessorsDeclarations.append(generateJavaDoc("@param " + name + " " + description)).append("\n");
			}
			accessorsDeclarations
				.append("public void ")
				.append(getSetAccessorMethodName(field.getName(), allFieldNames))
				.append("(")
				.append(type)
				.append(" ")
				.append(name)
				.append(") ")
				.append(generateCodeBlock("this." + name + " = " + name + ";"))
				.append("\n");
		}
		
		return fieldDeclarations.toString() + "\n" + accessorsDeclarations.toString();
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
	public static String getGetAccessorMethodName(String fieldName, String fieldType, List<String> allFieldNames) {
		String prefix = "get";
		if ("boolean".equalsIgnoreCase(fieldType) || Boolean.class.getName().equals(fieldType)) {
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
	 * @param fieldName     field name
	 * @param allFieldNames list of all fields, or <code>null</code> belonging to
	 *                      same destination class as field name, to check for
	 *                      unicity
	 * @return "set" + first letter to upper case field name if unique, field name
	 *         as is if another field exist that is equal ignoring case to fieldName
	 */
	public static String getAccessorMethodName(String prefix, String name, List<String> allFields) {
		String accessorSuffix = name;
		// If there is another field with same name but different case, keep the part after accessor as is to make sure it is unique,
		// otherwise use first letter upper case for more readable camel case accessor name.
		if (allFields == null || !allFields.stream().anyMatch(n -> !name.equals(n) && name.equalsIgnoreCase(n))) {
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
		if (fullClassName != null && fullClassName.contains(".")) {
			return StringUtils.substringAfterLast(fullClassName, ".");
		}
		return fullClassName;
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
	 * Generates static variable name according to its camel case name, e.g.
	 * <code>myStaticVariable</code> -> <code>MY_STATIC_VARIABLE</code>
	 * 
	 * @param camelCaseVariableName
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
	 * @throws IOException eventually raised by {@link Files#walk(Path)}
	 */
	public static void deletePath(Path path) throws IOException {
		if (!path.toFile().exists())
			return;
		Files.walk(path)
	      .sorted(Comparator.reverseOrder())
	      .map(Path::toFile)
	      .forEach(File::delete);
	}
	
	/**
	 * Appends {@link Logger} and {@link LoggerFactory} imports and logger
	 * declaration followed by blank line and beginning of type body like
	 * 
	 * <pre>
	 * private static final Logger log = LoggerFactory.getLogger(" +
	 * typeGenerator.getSimpleName() + ".class);\n\n
	 * 
	 * <pre>
	 * 
	 * @param typeGenerator type generator to append logger declaration at beginning
	 *                      of body of
	 */
	public static void generateSlf4jLoggerDeclaration(JavaTypeGenerator typeGenerator) {
		typeGenerator.addImport(Logger.class);
		typeGenerator.addImport(LoggerFactory.class);
		typeGenerator.appendToBody("private static final Logger log = LoggerFactory.getLogger(" + typeGenerator.getSimpleName() + ".class);\n\n");
	}
}
