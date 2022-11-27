package com.scz.jcex.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;

/**
 * Generates content of .java file for a given type
 */
public class JavaTypeGenerator {

	private final Set<String> imports = new TreeSet<String>();
	private final String name;
	
	private String typeDeclaration;
	
	private String description;
	
	private final StringBuilder body = new StringBuilder();
	
	public JavaTypeGenerator(String fullTypeName) {
		this.name = fullTypeName;
	}
	
	public void addImport(String im) {
		imports.add(im);
	}

	public Set<String> getImports() {
		return imports;
	}

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
	
	public void appendToBody(String bodyContent) {
		body.append(bodyContent);
	}
	
	public void appendMethod(String methodDeclaration, String methodBody) {
		appendToBody(methodDeclaration + JavaCodeGenerationUtil.generateCodeBlock(methodBody));
	}
	
	public String getName() {
		return name;
	}
	
	public String getSimpleName() {
		return JavaCodeGenerationUtil.getClassNameWithoutPackage(name);
	}
	
	public String getPackage() {
		return JavaCodeGenerationUtil.getClassPackage(name);
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String generate() {
		StringBuilder sb = new StringBuilder();
		sb.append("package ").append(getPackage()).append(";\n\n");
		for (String im: imports) {
			sb.append("import ").append(im).append(";\n");
		}
		return sb.append("\n")
				 .append(JavaCodeGenerationUtil.generateJavaDoc(description))
				 .append("\n")
				 .append(typeDeclaration)
				 .append(" ")
				 .append(JavaCodeGenerationUtil.generateCodeBlock(body.toString()))
				 .toString();
	}

	/**
	 * Write the .java file
	 * @param sourceFolder base directory (default package) for sources
	 * @throws IOException 
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
