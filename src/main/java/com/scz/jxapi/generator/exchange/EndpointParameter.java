package com.scz.jxapi.generator.exchange;

import java.util.List;

import com.scz.jxapi.util.EncodingUtil;

/**
 * Part of exchange descriptor JSON document describing a given field of a request to an endpoint or its response.
 * Such field can be recursive, see {@link CanonicalEndpointParameterTypes#OBJECT} or {@link CanonicalEndpointParameterTypes#OBJECT_LIST}.
 */
public class EndpointParameter {
	
	public static EndpointParameter create(String type, String name, String msgField, String description, Object sampleValue) {
		EndpointParameter p = new EndpointParameter();
		p.setType(type);
		p.setName(name);
		p.setMsgField(msgField);
		p.setDescription(description);
		p.setSampleValue(sampleValue);
		return p;
	}
	
	public static EndpointParameter createObject(String type, String name, String msgField, String description, List<EndpointParameter> parameters) {
		EndpointParameter p = new EndpointParameter();
		p.setType(type);
		p.setName(name);
		p.setMsgField(msgField);
		p.setDescription(description);
		p.setParameters(parameters);
		return p;
	}
	
	private String name;
	
	private String description;
	
	private String type;
	
	private List<String> sampleMapKeyValue;
	
	private Object sampleValue;
	
	private String msgField;
	
	private String objectName;
	
	private List<EndpointParameter> parameters;
	
	private List<String> implementedInterfaces;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public EndpointParameterType getEndpointParameterType() {
		return EndpointParameterType.fromTypeName(type);
	}

	public String setType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
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
	 *         {@link CanonicalEndpointParameterTypes#isObject}, the parameters in nested
	 *         structure, <code>null</code> otherwise.
	 */
	public List<EndpointParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<EndpointParameter> parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * @return The simple (without package) name of java class to represent
	 *         corresponding to object defined by this parameter. Relevant only when
	 *         type is an object see {@link CanonicalEndpointParameterTypes#isObject}. Remark: in a descriptor
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
