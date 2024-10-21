package com.scz.jxapi.exchange.descriptor.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.scz.jxapi.exchange.descriptor.ExchangeDescriptor;

/**
 * The ExchangeDescriptorParser class is responsible for parsing JSON data into ExchangeDescriptor objects.<br>
 * Relies on Jackson library for JSON parsing.
 */
public class ExchangeDescriptorParser {

	/**
	 * Parses a JSON file into an ExchangeDescriptor object.
	 * 
	 * @param jsonFile the path to the JSON file
	 * @return the ExchangeDescriptor object parsed from the JSON file
	 * @throws IOException if an I/O error occurs while reading the JSON file
	 */
	public ExchangeDescriptor fromJson(Path jsonFile) throws IOException {
		return fromJson(Files.readString(jsonFile));
	}
	
	/**
	 * Parses a JSON string into an ExchangeDescriptor object.
	 * 
	 * @param jsonString the JSON string to parse
	 * @return the ExchangeDescriptor object parsed from the JSON string
	 * @throws JsonMappingException if there is an error mapping the JSON to the ExchangeDescriptor object
	 * @throws JsonProcessingException if there is an error processing the JSON string
	 */
	public ExchangeDescriptor fromJson(String jsonString) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return mapper.readValue(jsonString, ExchangeDescriptor.class);
	}
}
