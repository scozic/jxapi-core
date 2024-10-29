package com.scz.jxapi.exchange.descriptor;

import com.scz.jxapi.exchange.Exchange;
import com.scz.jxapi.util.EncodingUtil;

/**
 * Represents a configuration property of an exchange, that client should
 * provide in constructor of an {@link Exchange} implementation to instantiate a
 * wrapper. For instance when an API requires authentication, the client should
 * provide the authentication credentials (API Key,secret) as properties<br/>
 * Exchange descriptor may contain a list of such properties as value of
 * 'properties' property of exchange.<br/>
 * The name of a property should be spelled 'camelCase' like a Java variable
 * name.<br/>
 * The value of a property can be a 'primitive' type e.g. {@link Type#STRING},
 * {@link Type#INT}, {@link Type#BOOLEAN}, {@link Type#BIGDECIMAL},
 * {@link Type#LONG}. It can't be a list, map, or object.<br/>
 * The properties will be exposed in a generated Java interface in the generated
 * Java wrapper class for the exchange. That interface wlll list constants for
 * property names and default values, and default 'getter' methods for
 * retrieving there values from properties<br/>
 * 
 */
public class ConfigProperty {

	/**
	 * Factory method to create a property instance
	 * 
	 * @param name         Property name
	 * @param type         Property value type, see {@link Type}, should be a
	 *                     primitive type e.g. {@link Type#STRING}, {@link Type#INT},
	 *                     {@link Type#BOOLEAN}, {@link Type#BIGDECIMAL},
	 *                     {@link Type#LONG}.
	 * @param description  Description of the property
	 * @param defaultValue Default value of the property. Should be of the type
	 *                     specified in 'type' or string representation of the value
	 *                     of that type. Can be <code>null</code> if no default value.
	 * @return Property instance
	 */
	public static ConfigProperty create(String name, Type type, String description, Object defaultValue) {
		ConfigProperty p = new ConfigProperty();
		p.setName(name);
		p.setDescription(description);
		p.setType(type);
		p.setDefaultValue(defaultValue);
		return p;
	}

	private String name;
	private String description;
	private Type type = Type.STRING;
	private Object defaultValue;
	
	/**
	 * Property name
	 * @return Property name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Property name
	 * @param name Property name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Description of the property
	 * @return Description of the property
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Description of the property
	 * @param description Description of the property
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/*
	 * Property value type, see {@link Type}, should be a primitive type e.g.
	 * {@link Type#STRING}, {@link Type#INT}, {@link Type#BOOLEAN},
	 * {@link Type#BIGDECIMAL}, {@link Type#LONG}.
	 */ 
	public Type getType() {
		return type;
	}
	
	/**
	 * Property value type, see {@link Type}, should be a primitive type e.g.
	 * {@link Type#STRING}, {@link Type#INT}, {@link Type#BOOLEAN},
	 * {@link Type#BIGDECIMAL}, {@link Type#LONG}.
	 * @param type Property value type
	 */
	public void setType(Type type) {
		this.type = type;
	}
	
	/**
	 * Shortcut for <code>setType(Type.fromTypeName(type))</code>
	 * @param type The type name see {@link Type#toString()}
	 * 
	 * @see #setType(Type)
	 */
	public void setType(String type) {
		setType(Type.fromTypeName(type));
	}
	
	/**
	 * Default value of the property. Should be of the type specified in 'type' or
	 * string representation of the value of that type. Can be <code>null</code> if
	 * no default value.
	 * @return Default value of the property
	 */
	public Object getDefaultValue() {
		return defaultValue;
	}
	
	/**
	 * Default value of the property. Should be of the type specified in 'type' or
	 * string representation of the value of that type. Can be <code>null</code> if
	 * no default value.
	 * @param defaultValue Default value of the property
	 */
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	/**
	 * Returns a string representation of this object
	 * @see EncodingUtil#pojoToString(Object)
	 */
	@Override
	public String toString() {
		return EncodingUtil.pojoToString(this);
	}

}
