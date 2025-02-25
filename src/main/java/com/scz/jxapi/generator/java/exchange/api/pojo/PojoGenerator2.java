package com.scz.jxapi.generator.java.exchange.api.pojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import com.scz.jxapi.exchange.descriptor.CanonicalType;
import com.scz.jxapi.exchange.descriptor.Field;
import com.scz.jxapi.exchange.descriptor.Type;
import com.scz.jxapi.generator.java.JavaCodeGenerationUtil;
import com.scz.jxapi.generator.java.JavaTypeGenerator;
import com.scz.jxapi.generator.java.exchange.ExchangeJavaGenUtil;
import com.scz.jxapi.generator.java.exchange.api.ExchangeApiGeneratorUtil;
import com.scz.jxapi.util.CollectionUtil;
import com.scz.jxapi.util.CompareUtil;
import com.scz.jxapi.util.EncodingUtil;
import com.scz.jxapi.util.Pojo;

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
		addImport(Pojo.class.getName());
		String pojoInterface = Pojo.class.getName() + "<" + getSimpleName() + ">";
		setImplementedInterfaces(CollectionUtil.mergeLists(List.of(pojoInterface), implementedInterfaces));
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
		appendToBody("\n");
		appendToBody(generateSerialVersionUidDeclaration());
		appendToBody("\n");
		appendMethod("public static Builder builder()", 
					 "return new Builder();" , 
					 "@return A new builder to build {@link " + getSimpleName() + "} objects");
		
		appendToBody("\n");
		appendToBody(generateAllFieldsDeclaration());
		appendToBody("\n");
		this.fields.forEach(f -> {
			appendToBody("\n");
		    generateAccessorsDeclaration(f);		
		});
		appendToBody("\n");
		generateEqualsMethod();
		appendToBody("\n");
		generateCompareToMethod();
		appendToBody("\n");
		generateHashCodeMethod();
		appendToBody("\n");
		generateDeepCloneMethod();
		appendToBody("\n");
		generateToStringMethod();
		generateBuilderClass();
		return super.generate();
	}
	
	private String generateSerialVersionUidDeclaration() {
		return "private static final long serialVersionUID = " 
				+ EndpointPojoGeneratorUtil.generateSerialVersionUid(getName(), fields, getImplementedInterfaces()) 
				+ "L;\n";
	}
	
	private void generateCompareToMethod() {
		StringBuilder compareBody = new StringBuilder()
				.append("if (other == null) {\n")
				.append(JavaCodeGenerationUtil.indent("return 1;"))
				.append("\n}\n");
		if (CollectionUtil.isEmpty(this.fields)) {
			compareBody.append("return 0;\n");
		} else {
			compareBody.append("int res = 0;\n");
			for (int i = 0; i < fields.size(); i++) {
				addImport(CompareUtil.class);
				String ret = "return res;";
				compareBody.append("res = ")
					.append(EndpointPojoGeneratorUtil.generateCompareFieldsInstruction(fields.get(i)))
					.append(";\n");
				if (i < fields.size() - 1) {
					compareBody.append("if (res != 0) {\n")
						.append(JavaCodeGenerationUtil.indent(ret))
						.append("\n}\n");
				} else {
					compareBody.append(ret)
						.append("\n");
				}
			}
		}
			
		appendMethod("@Override\npublic int compareTo(" + getSimpleName() + " other)", compareBody.toString());
		
	}

	private void generateBuilderClass() {
		JavaTypeGenerator builder = new JavaTypeGenerator("Builder");
		String name = getSimpleName();
		builder.setDescription("Builder for {@link " + name + "}");
		builder.setTypeDeclaration("public static class");
		builder.appendToBody("\n");
		builder.appendToBody(generateAllFieldsDeclaration());
		builder.appendToBody("\n");
		fields.forEach(f -> generateBuilderMethodsDeclaration(f, builder));
		builder.appendToBody("\n");
		
		StringBuilder buildMethodBody = new StringBuilder();
		if (!CollectionUtil.isEmpty(fields)) {
			buildMethodBody.append(name)
				.append(" res = new ")
				.append(name)
				.append("();\n");
		    fields.forEach(f -> buildMethodBody
				.append("res.")
				.append(f.getName())
				.append(" = this.")
				.append(f.getName())
				.append(";\n"));
			buildMethodBody.append("return res;\n");
		} else {
			buildMethodBody.append("return new ").append(name).append("();\n");
		}
		
		builder.appendMethod(
				"public " + name + " build()", 
				buildMethodBody.toString(), 
				"@return a new instance of " + name + " using the values set in this builder");
		appendToBody(builder.generate());
	}
	
	public String generateAllFieldsDeclaration() {
		return fields.stream().map(this::generateFieldDeclaration).collect(Collectors.joining("\n"));
	}
	
	private String generateFieldDeclaration(Field field) {
		String typeClass = getFieldClass(field);
		typeClass = JavaCodeGenerationUtil.getClassNameWithoutPackage(typeClass);
		return new StringBuilder()
				.append("private ")
				.append(typeClass)
				.append(" ")
				.append(field.getName())
				.append(";").toString();
	}
	
	private void generateAccessorsDeclaration(Field field) {
		
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
		
		String getJavadoc = null;
		String setJavadoc = null;
		if (description != null) {
			getJavadoc = "@return " + description;
			setJavadoc = "@param " + name + " " + description;
		}

		String getSignature = new StringBuilder()
			.append("public ")
			.append(typeClass)
			.append(" ")
			.append(JavaCodeGenerationUtil.getGetAccessorMethodName(field.getName(), typeClass, getAllFieldNames()))
			.append("()")
			.toString();
		
		appendMethod(getSignature, "return " + name + ";\n", getJavadoc);
		appendToBody("\n");
		
		String setMethodBody = new StringBuilder()
				.append("this.")
				.append(name)
				.append(" = ")
				.append(name)
				.append(";\n")
				.toString();
		String argDeclaration = new StringBuilder()
				.append("(")
				.append(typeClass)
				.append(" ")
				.append(name)
				.append(")").toString();
		
		String setSignature = new StringBuilder()
			.append("public void ")
			.append(JavaCodeGenerationUtil.getSetAccessorMethodName(field.getName(), getAllFieldNames()))
			.append(argDeclaration)
			.toString();
		
		appendMethod(setSignature, setMethodBody, setJavadoc);
	}
	
	private void generateBuilderMethodsDeclaration(Field field, JavaTypeGenerator builder) {
		Type fieldType = ExchangeJavaGenUtil.getFieldType(field);
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

		String setMethodBody = new StringBuilder()
				.append("this.")
				.append(name)
				.append(" = ")
				.append(name)
				.append(";\n")
				.append("return this;\n")
				.toString();
		
		String argDeclaration = new StringBuilder()
				.append("(")
				.append(typeClass)
				.append(" ")
				.append(name)
				.append(") ").toString();
		
		String javadoc = new StringBuilder()
				.append("Will set the value of <code>")
				.append(name)
				.append("</code> field in the builder\n")
				.append("@param ")
				.append(name).append(" ")
				.append(description)
				.append("\n@return Builder instance")
				.append("\n@see #")
				.append(JavaCodeGenerationUtil.getSetAccessorMethodName(field.getName(), getAllFieldNames()))
				.append("(")
				.append(typeClass)
				.append(")")
				.toString();
		
		String signature = new StringBuilder()
                .append("public Builder ")
                .append(name)
                .append(argDeclaration)
                .toString();
		
		builder.appendToBody("\n");
		builder.appendMethod(signature, setMethodBody, javadoc);
		if (fieldType.getCanonicalType() == CanonicalType.LIST) {
			builder.appendToBody("\n");
			generateAddToListBuilderMethod(field, builder);
		} else if (fieldType.getCanonicalType() == CanonicalType.MAP) {
			builder.appendToBody("\n");
			generateAddToMapBuilderMethod(field, builder);
		}
	}
	
	private void generateAddToListBuilderMethod(Field field, JavaTypeGenerator builder) {
		String name = field.getName();
		Type fieldType = ExchangeJavaGenUtil.getFieldType(field);
		Field itemField = Field.builder()
						 	   .name(name)
						 	   .type(fieldType.getSubType())
						 	   .objectName(field.getObjectName())
						 	   .properties(field.getProperties())
						 	   .build();
		String itemTypeClass = getFieldClass(itemField);
		itemTypeClass = JavaCodeGenerationUtil.getClassNameWithoutPackage(itemTypeClass);
		
		String signature = new StringBuilder()
				.append("public Builder ")
				.append(JavaCodeGenerationUtil.getAccessorMethodName("addTo", name, getAllFieldNames()))
				.append("(").append(itemTypeClass)
				.append(" item)").toString();
		
		String javadoc = new StringBuilder("Will add an item to the <code>")
				.append(name)
				.append("</code> list.\n")
				.append("@param item Item to add to current <code>")
				.append(name)
				.append("</code> list\n")
				.append("@return Builder instance\n")
				.append("@see ")
				.append(getSimpleName())
				.append("#")
				.append(JavaCodeGenerationUtil.getSetAccessorMethodName(name, getAllFieldNames()))
				.append("(").append(itemTypeClass)
				.append(")").toString();
		addImport(CollectionUtil.class);
		String methodBody = new StringBuilder()
				.append("if (this.")
				.append(name)
				.append(" == null) {\n")
				.append(JavaCodeGenerationUtil.indent("this." + name + " = CollectionUtil.createList();"))
				.append("\n}\n")
				.append("this.")
				.append(name)
				.append(".add(item);\n")
				.append("return this;\n")
				.toString();
		
		builder.appendToBody("\n");
		builder.appendMethod(signature, methodBody, javadoc);

	}
	
	private void generateAddToMapBuilderMethod(Field field, JavaTypeGenerator builder) {
		String name = field.getName();
		Type fieldType = ExchangeJavaGenUtil.getFieldType(field);
		Field itemField = Field.builder()
						 	   .name(name)
						 	   .type(fieldType.getSubType())
						 	   .objectName(field.getObjectName())
						 	   .properties(field.getProperties())
						 	   .build();
		String itemTypeClass = getFieldClass(itemField);
		itemTypeClass = JavaCodeGenerationUtil.getClassNameWithoutPackage(itemTypeClass);
		
		String signature = new StringBuilder()
				.append("public Builder ")
				.append(JavaCodeGenerationUtil.getAccessorMethodName("addTo", name, getAllFieldNames()))
				.append("(")
				.append("String key, ")
				.append(itemTypeClass)
				.append(" item)").toString();
		
		String javadoc = new StringBuilder("Will add or update a key/value pair to the <code>")
				.append(name)
				.append("</code> map.\n")
				.append("@param item Item to add to current <code>")
				.append(name)
				.append("</code> list\n")
				.append("@return Builder instance\n")
				.append("@see ")
				.append(getSimpleName())
				.append("#")
				.append(JavaCodeGenerationUtil.getSetAccessorMethodName(name, getAllFieldNames()))
				.append("(").append(itemTypeClass)
				.append(")").toString();
		
		addImport(CollectionUtil.class);
		String methodBody = new StringBuilder()
				.append("if (this.")
				.append(name)
				.append(" == null) {\n")
				.append(JavaCodeGenerationUtil.indent("this." + name + " = CollectionUtil.createMap();"))
				.append("\n}\n")
				.append("this.")
				.append(name)
				.append(".put(key, item);\n")
				.append("return this;\n")
				.toString();
		
		builder.appendToBody("\n");
		builder.appendMethod(signature, methodBody, javadoc);
	}
	
	private List<String> getAllFieldNames() {
        return fields.stream().map(Field::getName).collect(Collectors.toList());
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
