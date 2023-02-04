package com.scz.jcex.generator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * Helper methods around java code generation
 */
public class JavaCodeGenerationUtil {
	
	private JavaCodeGenerationUtil() {}
	
	public static final String INDENTATION = "  ";
	
	public static final String GENERATED_CODE_WARNING = "<br><strong>THIS CODE IS GENERATED. DO NOT EDIT MANUALLY!</strong>";
	
	public static final String indent(String code) {
		return indent(code, INDENTATION);
	}
	
	public static final String indent(String code, String indent) {
		return indent + code.replace("\n", "\n" + indent);
	}
	
	public static final String generateCodeBlock(String codeBlockContent) {
		return "{\n" + indent(codeBlockContent) + "\n}\n";
	}
	
	public static final String generateJavaDoc(String javadoc) {
		if (javadoc == null)
			return "";
		return "/**\n" + indent(javadoc, " * ") + "\n */"; 
	}
	
	/**
	 * 
	 * @param fields
	 * @return
	 */
	public static String generateJavaPojoFieldsWithAccessors(List<PojoField> fields) {
		StringBuilder fieldDeclarations = new StringBuilder();
		StringBuilder accessorsDeclarations = new StringBuilder();
		for (PojoField field : fields) {
			String type = field.getType();
			type = getClassNameWithoutPackage(type);
			String name = field.getName();
			String accessorSuffix = name;
			// If there is another field with same name but different case, keep the part after accessor as is to make sure it is unique,
			// otherwise use first letter uppercase for more readable camel case accessor name.
			if (!fields.stream().anyMatch(f -> !name.equals(f.getName()) && name.equalsIgnoreCase(f.getName()))) {
				accessorSuffix = firstLetterToUpperCase(name);
			}
			String description = field.getDescription();
			
			if (description != null) {
				accessorsDeclarations.append(generateJavaDoc("@return " + description)).append("\n");
			}
			accessorsDeclarations.append("public ").append(type).append(" ");
			if ("boolean".equals(type)) {
				accessorsDeclarations.append("is");	
			} else {
				accessorsDeclarations.append("get");
			}
			accessorsDeclarations.append(accessorSuffix)
			.append("()")
			.append(generateCodeBlock("return " + name + ";"))
			.append("\n");
			
			fieldDeclarations.append("private ").append(type).append(" ").append(name).append(";\n");
			if (description != null) {
				accessorsDeclarations.append(generateJavaDoc("@param " + name + " " + description)).append("\n");
			}
			accessorsDeclarations
				.append("public void set")
				.append(accessorSuffix)
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
	
	public static String firstLetterToUpperCase(String s) {
		if (s == null || s.isEmpty())
			return s;
		return Character.toUpperCase(s.charAt(0)) + s.substring(1);
	}
	
	public static String firstLetterToLowerCase(String s) {
		if (s == null || s.isEmpty())
			return s;
		return Character.toLowerCase(s.charAt(0)) + s.substring(1);
	}
	
	public static String getClassNameWithoutPackage(String fullClassName) {
		if (fullClassName != null && fullClassName.contains(".")) {
			return StringUtils.substringAfterLast(fullClassName, ".");
		}
		return fullClassName;
	}
	
	public static String getClassPackage(String fullClassName) {
		if (fullClassName != null && fullClassName.contains(".")) {
			return StringUtils.substringBeforeLast(fullClassName, ".");
		}
		return "";
	}

	public static void deletePath(Path path) throws IOException {
		Files.walk(path)
	      .sorted(Comparator.reverseOrder())
	      .map(Path::toFile)
	      .forEach(File::delete);
	}
}
