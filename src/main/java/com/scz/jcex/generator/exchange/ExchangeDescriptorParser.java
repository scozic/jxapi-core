package com.scz.jcex.generator.exchange;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExchangeDescriptorParser {

	public ExchangeDescriptor fromJson(Path jsonFile) throws IOException {
		return fromJson(Files.readString(jsonFile));
	}
	
	public ExchangeDescriptor fromJson(String jsonString) throws JsonMappingException, JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(jsonString, ExchangeDescriptor.class);
	}
}
