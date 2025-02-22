package com.scz.jxapi.generator.java.exchange.api.pojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.JavaTypeGenerator;
import com.scz.jxapi.generator.java.exchange.ExchangeJavaGenUtil;
import com.scz.jxapi.generator.java.exchange.api.ExchangeApiGeneratorUtil;
import com.scz.jxapi.util.CollectionUtil;
import com.scz.jxapi.util.DeepCloneable;
import com.scz.jxapi.util.EncodingUtil;

public class PojoGenerator2 extends JavaTypeGenerator {

	private final List<Field> fields = new ArrayList<>();
	
	/**
	 * Creates a new POJO generator for the given type name
	 * @param fullTypeName Full class name e.g. <i>com.x.y.z.Foo</i>
	 * @throws IOException 
	 */
	public PojoGenerator2(String fullTypeName) {
		this(fullTypeName, null, null, null);
		
	}
	
	public PojoGenerator2(String className, 
			 String description, 
			 List<Field> fields, 
			 List<String> implementedInterfaces) {
		super(className);
		setTypeDeclaration("public class");
		this.fields.addAll(Optional.ofNullable(fields).orElse(List.of()));
		String serializerClassName = EndpointPojoGeneratorUtil.getSerializerClassName(className);
		addImport(serializerClassName);
		addImport(com.fasterxml.jackson.databind.annotation.JsonSerialize.class.getName());
		setTypeDeclaration("@JsonSerialize(using = " 
										+ JavaCodeGenerationUtil.getClassNameWithoutPackage(serializerClassName) 
										+ ".class)\n" 
										+ getTypeDeclaration());
		setDescription(description);
		addImport(DeepCloneable.class.getName());
		String deepClonable = DeepCloneable.class.getName() + "<" + getSimpleName() + ">";
		setImplementedInterfaces(CollectionUtil.mergeLists(List.of(deepClonable), implementedInterfaces));
	}
	
	/**
	 * Adds a field to the POJO. If field type is not a primitive or java.lang.*
	 * type, it will be added to imports.
	 * 
	 * @param field Field to add
	 */
	public void addField(Field field) {
		fields.add(field);
	}

	/**
	 * @return Array of fields in the order they were added
	 */
	public List<Field> getFields() {
		return fields;
	}
	
	@Override
	public String generate() {
		StringBuilder fieldDeclarations = new StringBuilder();
		StringBuilder accessorsDeclarations = new StringBuilder();
		fields.forEach(f -> generateField(fieldDeclarations, accessorsDeclarations, f));
		appendToBody(fieldDeclarations.toString());
		appendToBody("\n");
		appendToBody(accessorsDeclarations.toString());
		generateEqualsMethod();
		appendToBody("\n");
		generateHashCodeMethod();
		appendToBody("\n");
		generateDeepCloneMethod();
		appendToBody("\n");
		generateToStringMethod();
		return super.generate();
	}
	
	private void generateField(StringBuilder fieldDeclarations, StringBuilder accessorsDeclarations, Field field) {
		String typeClass = getFieldClass(field);
		typeClass = JavaCodeGenerationUtil.getClassNameWithoutPackage(typeClass);
		String name = field.getName();
		String description = field.getDescription();
		String msgFieldDescription = "";
		if (field.getMsgField() != null) {
			msgFieldDescription = "Message field <strong>" + field.getMsgField() + "</strong>";
			if (description == null) {
				description = msgFieldDescription;
			} else {
				description += " " + msgFieldDescription;
			}
		}
		
		if (description != null) {
			accessorsDeclarations.append(JavaCodeGenerationUtil.generateJavaDoc("@return " + description)).append("\n");
		}
		List<String> allFieldNames = fields.stream().map(Field::getName).collect(Collectors.toList());
		accessorsDeclarations
			.append("public ")
			.append(typeClass)
			.append(" ")
			.append(JavaCodeGenerationUtil.getGetAccessorMethodName(field.getName(), typeClass, allFieldNames))
			.append("() ")
			.append(JavaCodeGenerationUtil.generateCodeBlock("return " + name + ";"))
			.append("\n");
		
		fieldDeclarations.append("private ").append(typeClass).append(" ").append(name).append(";\n");
		if (description != null) {
			accessorsDeclarations.append(JavaCodeGenerationUtil.generateJavaDoc("@param " + name + " " + description)).append("\n");
		}
		accessorsDeclarations
			.append("public void ")
			.append(JavaCodeGenerationUtil.getSetAccessorMethodName(field.getName(), allFieldNames))
			.append("(")
			.append(typeClass)
			.append(" ")
			.append(name)
			.append(") ")
			.append(JavaCodeGenerationUtil.generateCodeBlock("this." + name + " = " + name + ";"))
			.append("\n");
	}
	
	private String getFieldClass(Field field) {
		String fieldClass = null;
		Type fieldType = ExchangeJavaGenUtil.getFieldType(field);
		if (ExchangeJavaGenUtil.isObjectField(field)) {
			String className = getName();
			fieldClass = ExchangeApiGeneratorUtil.getClassNameForField(field, getImports(), className);
		} else {
			fieldClass = ExchangeJavaGenUtil.getClassNameForType(fieldType, getImports(), null);
		}
		
		return JavaCodeGenerationUtil.getClassNameWithoutPackage(fieldClass);
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
			for (Field field : fields) {
				String f = field.getName();
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
			for (Field field : fields) {
				String f = field.getName();
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
	
	private void generateDeepCloneMethod() {
		String signature = "@Override\npublic " + getSimpleName() + " deepClone()";
		String newDeclaration = "new " + getSimpleName() + "();\n";
		StringBuilder body = new StringBuilder();
		if (CollectionUtil.isEmpty(fields)) {
			body.append("return ").append(newDeclaration);
			
		} else {
			body.append(getSimpleName())
			.append(" clone = ")
			.append(newDeclaration);
		
			fields.forEach(f -> 
				body.append("clone.")
			    	.append(f.getName())
			    	.append(" = ")
			    	.append(EndpointPojoGeneratorUtil.generateDeepCloneFieldInstruction(f, getImports()))
			    	.append(";\n")
			);
			body.append("return clone;\n");
		}
		appendMethod(signature, body.toString());
	}

}
