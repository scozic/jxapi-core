package com.scz.jxapi.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import com.scz.jxapi.util.EncodingUtil;

/**
 * Specialized {@link JavaTypeGenerator} to create a 'Plain Old Java Object' (POJO) 
 * that is a class containing only a default public constructor, private field with getter and setter methods.
 * In addition, such generated objects will have {@link #toString()} method overridden to return result 
 * of {@link EncodingUtil#pojoToString(Object)}
 */
public class PojoGenerator extends JavaTypeGenerator {
	
	public PojoGenerator(String fullTypeName) {
		super(fullTypeName);
		setTypeDeclaration("public class");
	}

	private final Map<String, PojoField> fields = new TreeMap<String, PojoField>();
	
	public void addField(PojoField field) {
		if (field.getType().contains(".")) {
			addImport(field.getType());
		}
		fields.put(field.getName(), field);
	}

	public PojoField[] getFields() {
		return fields.values().toArray(new PojoField[fields.values().size()]);
	}
	
	@Override
	public String generate() {
		List<PojoField> l = new ArrayList<PojoField>(fields.values());
		body.insert(0, JavaCodeGenerationUtil.generateJavaPojoFieldsWithAccessors(l));
		generateEqualsMethod();
		body.append("\n");
		generateHashCodeMethod();
		body.append("\n");
		generateToStringMethod();
		return super.generate();
	}
	
	private void generateToStringMethod() {
		addImport(EncodingUtil.class.getName());
		appendMethod("@Override\npublic String toString()", 
					 "return EncodingUtil.pojoToString(this);");

	}
	
	private void generateEqualsMethod() {
		StringBuilder body = new StringBuilder()
			.append("if (other == null)\n")
			.append(JavaCodeGenerationUtil.indent("return false;"))
			.append("\nif (!getClass().equals(other.getClass()))\n")
			.append(JavaCodeGenerationUtil.indent("return false;"))
			.append("\n");
			
		if (fields.isEmpty()) {
			body.append("return true;\n");
		} else {
			body.append(getSimpleName())
				.append(" o = (")
				.append(getSimpleName())
				.append(") other;\nreturn ");
			boolean first = true;
			for (String f : fields.keySet()) {
				if (first) {
					addImport(Objects.class);
					first = false;
				} else {
					body.append("\n")
						.append(JavaCodeGenerationUtil.INDENTATION)
						.append(JavaCodeGenerationUtil.INDENTATION)
						.append(JavaCodeGenerationUtil.INDENTATION)
						.append(JavaCodeGenerationUtil.INDENTATION)
						.append("&& ");
				}
				body.append("Objects.equals(")
					.append(f)
					.append(", o.")
					.append(f)
					.append(")");
			}
			body.append(";\n");
		}
		appendMethod("@Override\npublic boolean equals(Object other)", body.toString());
	}
	
	private void generateHashCodeMethod() {
		StringBuilder body = new StringBuilder();
		if (fields.isEmpty()) {
			body.append("return 31 * getClass().hashCode();\n");
		} else {
			boolean first = true;
			body.append("return Objects.hash(");
			for (String f : fields.keySet()) {
				if (first) {
					first = false;
				} else {
					body.append(", ");
				}
				body.append(f);
			}
			body.append(");\n");
		}
		appendMethod("@Override\npublic int hashCode()", body.toString());
	}

}
