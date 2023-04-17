package com.scz.jxapi.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
		generateToStringMethod();
		return super.generate();
	}
	
	private void generateToStringMethod() {
		addImport(EncodingUtil.class.getName());
//		StringBuilder body = new StringBuilder();
//		body.append("return EncodingUtil.formatArgsToJsonStruct(");
//		for (Iterator<String> it = fields.keySet().iterator(); it.hasNext();) {
//			String fieldName = it.next();
//			body.append("\"").append(fieldName).append("\", ").append(fieldName);
//			if (it.hasNext()) {
//				body.append(", ");
//			}
//		}
//		body.append(");");
//		appendMethod("@Override\npublic String toString()", 
//				 body.toString());
		appendMethod("@Override\npublic String toString()", 
					 "return EncodingUtil.pojoToString(this);");

	}

}
