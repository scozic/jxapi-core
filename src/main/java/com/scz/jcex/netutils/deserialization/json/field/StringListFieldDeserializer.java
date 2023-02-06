package com.scz.jcex.netutils.deserialization.json.field;

public class StringListFieldDeserializer extends StructListFieldDeserializer<String> {
	
	private static final StringListFieldDeserializer INSTANCE = new StringListFieldDeserializer();

	public static final StringListFieldDeserializer getInstance() {
		return INSTANCE;
	}

	/**
	 * Hidden singleton constructor 
	 */
	private StringListFieldDeserializer() {
		super(parser -> parser.nextTextValue());
	}

}
