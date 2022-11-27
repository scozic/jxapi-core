package com.scz.jcex.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.scz.jcex.util.EncodingUtil;

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
		appendToBody(JavaCodeGenerationUtil.generateJavaPojoFieldsWithAccessors(l));
		generateToStringMethod();
		return super.generate();
	}
	
	private void generateToStringMethod() {
		addImport(EncodingUtil.class.getName());
		appendMethod("@Override\npublic String toString()", 
					 "return EncodingUtil.pojoToString(this);");
	}

}
