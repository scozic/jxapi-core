package com.scz.jcex.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Generates content of .java file for a given type
 */
public class JavaTypeGenerator {

	private final Set<String> imports = new TreeSet<String>();
	private final String name;
	private String parentClassName;
	private List<String> implementedInterfaces;
	
	private String typeDeclaration;
	
	private String description;
	
	protected final StringBuilder body = new StringBuilder();
	
	public JavaTypeGenerator(String fullTypeName) {
		this.name = fullTypeName;
	}
	
	public void addImport(Class<?> cls) {
		addImport(cls.getName());
	}
	
	public void addImport(String im) {
		if (im.contains("<")) {
			im = im.substring(0, im.indexOf('<'));
		}
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
		appendToBody(methodDeclaration + " " + JavaCodeGenerationUtil.generateCodeBlock(methodBody));
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
	
	public String getParentClassName() {
		return parentClassName;
	}

	public void setParentClassName(String parentClassName) {
		this.parentClassName = parentClassName;
	}

	public List<String> getImplementedInterfaces() {
		return implementedInterfaces;
	}

	public void setImplementedInterfaces(List<String> implementedInterfaces) {
		this.implementedInterfaces = implementedInterfaces;
	}
	
	public String generate() {
		StringBuilder sb = new StringBuilder();
		if (parentClassName != null) {
			addImport(parentClassName);
		}
		if (implementedInterfaces != null) {
			implementedInterfaces.forEach(this::addImport);
		}
		
		sb.append("package ").append(getPackage()).append(";\n\n");
		for (String im: imports) {
			String imPkg = JavaCodeGenerationUtil.getClassPackage(im);
			if (!imPkg.equals(getPackage())
				&& !imPkg.startsWith("java.lang")
				&& imPkg.contains(".")) {
				sb.append("import ").append(im).append(";\n");
			}
		}
		sb.append("\n")
		  .append(JavaCodeGenerationUtil.generateJavaDoc(description))
		  .append("\n")
		  .append(typeDeclaration)
		  .append(" ")
		  .append(getSimpleName())
		  .append(" ");
		if (parentClassName != null) {
			sb.append("extends ")
			  .append(JavaCodeGenerationUtil.getClassNameWithoutPackage(parentClassName))
			  .append(" ");
		}
		if (implementedInterfaces != null && implementedInterfaces.size() > 0) {
			sb.append("implements ");
			for (int i = 0; i < implementedInterfaces.size(); i++) {
				sb.append(JavaCodeGenerationUtil.getClassNameWithoutPackage(implementedInterfaces.get(i)));
				if (i < implementedInterfaces.size() - 1) {
					sb.append(", ");
				}
			}
			sb.append(" ");
		}
		
	    sb.append(JavaCodeGenerationUtil.generateCodeBlock(body.toString()));
		return sb.toString();
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
