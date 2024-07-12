package com.scz.jxapi.exchange.descriptor;

import java.util.ArrayList;
import java.util.List;

import com.scz.jxapi.util.EncodingUtil;

/**
 * Element of exchange descriptor JSON document describing a given field of a request to an endpoint or its response.<br/>
 * A field is defined by:<br/>
 * <ul>
 * <li>Its <code>name</code> property, which has to be unique among fields of same object structure</li>
 * <li>Its <code>type</code> property, see {@link Type} which qualifies the type of data found as value. 
 *  This type can be either primitive (<code>STRING</code>, <code>INT</code>... see {@link CanonicalType#isPrimitive}) 
 *  or defining an object structure (JSON map) as a list of nested {@link Field} of any type. 
 *  Therefore a field can describe most tree data structure.</li>
 * <li>Its <code>description</code>, a human readable description of the field</li>
 * <li>Its <code>sample Value</code>, a sample value of the field, used for documentation purpose and sample value creation in demo classes</li>
 * <li>Its <code>msgField</code>, the name of the field in the message sent or received to the endpoint. 
 *  This means the actual key in JSON structure. This can differ from actual name when APIs are designed to minify 
 *  JSON data transferred by using single letter as field names. In this case it is recommended to 
 *  define <code>name</code> property in descriptor with understandable name, and set <code>msgField</code> value/li>
 * <li>Its <code>objectName</code>, the simple (without package) name of java class to represent corresponding 
 *  to object defined by this parameter. Relevant only when type is an object see {@link CanonicalType#isObject}. 
 * 	<strong>Remarks:</strong>
 * 	<ul>
 * 	<li>In a descriptor file, the first field defining a given object name should define that object sub-fields, 
 *   other parameters using same object name need not define sub-fields. This allow not to repeat identical structures in different APIs.</li>
 * 	<li>Defining <code>objectName</code> property is not mandatory for. 
 *   For an 'object' field, the object name will be generated as concatenation of exchange, api, and field names</li>
 *  <li>Defining <code>objectName</code> property is important for an 'object' field, when the object is used in multiple APIs. 
 *   This will allow to generate a single class for the object, and reuse it in multiple APIs.</li>
 *  <li>The destination package of the generated class is sub package 'pojo' of enclosing API group package, 
 *   which is sub package 'apiName' of <code>basePackage</code> property in the descriptor file.<br/> 
 * 	 Example: for a field with objectName <i>Foo</i> in an endpoint of API group named <i>myapi</i> group of exchange 
 *   with base package <i>com.x.y</i>, the class Foo will be generated in package <i>com.x.y.myapi.pojo</i></i> </li>
 *  </ul>
 * </li>
 * <li>Its <code>parameters</code>, for an <code>object</code> type field, see {@link CanonicalType#isObject}, 
 *  the fields in nested structure, <code>null</code> otherwise.</li>
 * <li>Its <code>implementedInterfaces</code>, the list of interfaces implemented by the object defined by this field. 
 *  Relevant only when type is an object see {@link CanonicalType#isObject}.</li>
 * </ul>
 */
public class Field {
	
	public static Field create(String type, String name, String msgField, String description, Object sampleValue) {
		Field p = new Field();
		p.setType(type);
		p.setName(name);
		p.setMsgField(msgField);
		p.setDescription(description);
		p.setSampleValue(sampleValue);
		return p;
	}
	
	public static Field createObject(String type, String name, String msgField, String description, List<Field> parameters) {
		Field p = new Field();
		p.setType(type);
		p.setName(name);
		p.setMsgField(msgField);
		p.setDescription(description);
		p.setParameters(parameters);
		return p;
	}
	
	private static <T> List<T> cloneList(List<T> l) {
		return l == null? null: new ArrayList<>(l);
	}
	
	private String name;
	
	private String description;
	
	private Type type;
	
	private List<String> sampleMapKeyValue;
	
	private Object sampleValue;
	
	private String msgField;
	
	private String objectName;
	
	private List<Field> parameters;
	
	private List<String> implementedInterfaces;
	
	public Field clone() {
		Field clone = new Field();
		clone.name = this.name;
		clone.description = this.description;
		clone.type = this.type;
		clone.sampleMapKeyValue = cloneList(this.sampleMapKeyValue);
		clone.sampleValue = this.sampleValue;
		clone.msgField = this.msgField;
		clone.objectName = this.objectName;
		clone.parameters = cloneList(this.parameters);
		clone.implementedInterfaces = cloneList(this.implementedInterfaces);
		return clone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public void setType(String type) {
		this.type = Type.fromTypeName(type);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Object getSampleValue() {
		return sampleValue;
	}

	public void setSampleValue(Object sampleValue) {
		this.sampleValue = sampleValue;
	}
	
	public String getMsgField() {
		return msgField;
	}

	public void setMsgField(String msgField) {
		this.msgField = msgField;
	}
	
	/**
	 * @return For an 'object' type parameter, see
	 *         {@link CanonicalType#isObject}, the parameters in nested
	 *         structure, <code>null</code> otherwise.
	 */
	public List<Field> getParameters() {
		return parameters;
	}

	public void setParameters(List<Field> fields) {
		this.parameters = fields;
	}
	
	/**
	 * @return The simple (without package) name of java class to represent
	 *         corresponding to object defined by this parameter. Relevant only when
	 *         type is an object see {@link CanonicalType#isObject}. Remark: in a descriptor
	 *         file, the first parameter defining a given object name should define
	 *         sub-parameters, other parameters using same object name need not
	 *         define sub-parameters. This allow not to repeat identical structures
	 *         in different APIs.
	 */
	public String getObjectName() {
		return objectName;
	}

	/**
	 * @param objectName
	 * @see #getObjectName()
	 */
	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}
	
	public List<String> getImplementedInterfaces() {
		return implementedInterfaces;
	}

	public void setImplementedInterfaces(List<String> implementedInterfaces) {
		this.implementedInterfaces = implementedInterfaces;
	}
	
	public List<String> getSampleMapKeyValue() {
		return sampleMapKeyValue;
	}

	public void setSampleMapKeyValue(List<String> sampleMapKeyValue) {
		this.sampleMapKeyValue = sampleMapKeyValue;
	}
	
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}
}
